package backend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;

//import Jama.Matrix;

public class Scoring {

	private Stats st;
	
	private boolean split;

	protected double[][] clusterScore;
	
	protected double[][] scoreS;
	protected double[][] scoreU;
	
	public Scoring(Stats stat, boolean stressSplit) {
		this.split = stressSplit;
		this.st = stat;
		
		scoreS = new double[Stats.PNUM][Stats.PNUM];
		if (split) scoreU = new double[Stats.PNUM][Stats.PNUM];
		
		for (int i=0; i<Stats.PNUM; i++) {
			for (int j=0; j<Stats.PNUM; j++) {
				scoreS[i][j] = -5;
				if (split) {
					scoreU[i][j] = -5;
				}
			}
		}
		setScores();
	}
	
	public double[] scoreRhyme(PLine a, PLine b, String method) {
		double ret[] = new double[0];
		if (a.size()<1 || b.size()<1) return ret;
		ArrayList<Syllable> aSyl = a.getSyllables(Stats.FLAT);
		ArrayList<Syllable> bSyl = b.getSyllables(Stats.FLAT);
		
		if ("primary".equals(method)) {
			// only match syllables with primary stress
			int sp = Stats.stressPlace(aSyl, bSyl);
			ret = new double[1];
			if (aSyl.get(aSyl.size()-sp).getStress()==1 && bSyl.get(bSyl.size()-sp).getStress()==1) { 
				ret[0] = sScore(aSyl.get(aSyl.size()-sp), bSyl.get(bSyl.size()-sp));
			}
		} else if ("all".equals(method)) {
			// match all syllables following final primary stress
			int sp = Stats.stressPlace(aSyl, bSyl);
			ret = new double[sp];
			for (int i=1; i<=sp; i++) {
				ret[sp-i] = sScore(aSyl.get(aSyl.size()-i), bSyl.get(bSyl.size()-i));
			}
		} else if ("syllable".equals(method)) {
			// match last syllable
			ret = new double[1];
			ret[0] = sScore(aSyl.get(aSyl.size()-1), bSyl.get(bSyl.size()-1));
		} else if ("iteration".equals(method)) {
			// match all ending syllables over threshold
			int sp = rhymeStart(aSyl, bSyl);
			ret = new double[sp];
			for (int i=1; i<=sp; i++) {
				ret[sp-i] = sScore(aSyl.get(aSyl.size()-i), bSyl.get(bSyl.size()-i));
			}
		}
		return ret;
	}
	
	public double sScore(Syllable A, Syllable B) {
		double ret = 0;
		
		ret += vScore(A,B);
		if (ret<-2) return ret;
		
		//ret += cScore(A,B);
		ret += consAlign(A.getEndC(), B.getEndC(), (A.getStress()>0 || B.getStress()>0));
		
		//ret += clustScore(A.getEndC().size(),B.getEndC().size(), (A.getStress()>0 || B.getStress()>0));
		ret += stressScore(A,B);
		
		return ret;
	}
	
	public double vScore(Syllable A, Syllable B) {
		int indA = Stats.indexVal(A.getVowel());
		int indB = Stats.indexVal(B.getVowel());
		if (A.getStress()==1 || B.getStress()==1 || !Stats.SPLIT) {
			return scoreS[Math.min(indA, indB)][Math.max(indA, indB)];
		}
		return scoreU[Math.min(indA, indB)][Math.max(indA, indB)];
	}
	
	public double pScore(String cA, String cB) {
		int indA = Stats.indexVal(cA);
		int indB = Stats.indexVal(cB);
		if (Stats.stressPart(cA)>0 || Stats.stressPart(cB)>0) {
			return scoreS[Math.min(indA, indB)][Math.max(indA, indB)];
		}
		return scoreU[Math.min(indA, indB)][Math.max(indA, indB)];
	}
	
	public double cScore(Syllable A, Syllable B) {
		double ret = 0;
		double[][] score = (A.getStress()==1||B.getStress()==1||!Stats.SPLIT)?scoreS:scoreU;
		int aSize = A.getEndC().size();
		int bSize = B.getEndC().size();
		for (int i=0; i<Math.min(aSize, bSize); i++) {
			int indA = Stats.indexVal(A.getEndC().get(i));
			int indB = Stats.indexVal(B.getEndC().get(i));
			ret += score[Math.min(indA,indB)][Math.max(indA,indB)];
		}

		if (aSize > bSize) {
			for (int i=bSize; i<aSize; i++) {
				ret += score[Stats.indexVal(A.getEndC().get(i))][Stats.PNUM-1];
			}
		} else if (bSize > aSize) {
			for (int i=aSize; i<bSize; i++) {
				ret += score[Stats.indexVal(B.getEndC().get(i))][Stats.PNUM-1];
			}
		}
		
		if (aSize>0 || bSize>0) {
			ret = ret/Math.max(aSize, bSize);
		} else {
			ret = 1;
		}
		return ret;
	}
	
	public double clustScore(int aSize, int bSize, boolean stress) {
		int[] clusterCount = (stress)?st.clusterCountS:st.clusterCountU;
		int[][] clusterMatch = (stress)?st.clusterMatchS:st.clusterMatchU;
		int vSum = (stress)?st.vSumS:st.vSumU;
		int vMatchSum = (stress)?st.vMatchSumS:st.vMatchSumU;

		aSize = Math.min(aSize, Stats.MAX_CLUSTER-1);
		bSize = Math.min(bSize, Stats.MAX_CLUSTER-1);
		double exp = ((double)clusterCount[aSize]*clusterCount[bSize]/((double)vSum*vSum));
		double occ = (clusterMatch[Math.min(aSize,bSize)][Math.max(aSize,bSize)]+0.01)/(vMatchSum+0.01);
		
		return Math.log(occ/exp); 
	}
	
	public double stressScore(Syllable A, Syllable B) {
		if (A.getStress()<0 || B.getStress()<0) return 0;
		double pStressRand = (double)st.vSumS/(st.vSumS+st.vSumU);
		double pStressRhyme = (double)(st.vMatchSumS-st.mixedStress)/(st.vMatchSumS+st.vMatchSumU);
		double pUnStressRhyme = (double)(st.vMatchSumU)/(st.vMatchSumS+st.vMatchSumU);
		double pMixedRhyme = (double)st.mixedStress/(st.vMatchSumS+st.vMatchSumU);
		
		double exp;
		double occ;
		
		if (A.getStress()>0 && B.getStress()>0) {
			exp = pStressRand*pStressRand;
			occ = pStressRhyme;
		} else if (A.getStress()==0 && B.getStress()==0) {
			exp = (1-pStressRand)*(1-pStressRand);
			occ = pUnStressRhyme;
		} else {
			exp = pStressRand*(1-pStressRand);
			occ = pMixedRhyme;
		}
		
		return Math.log(occ/exp);
	}
	
	public double consAlign(ArrayList<String> a, ArrayList<String> b, boolean stress) {
		double gap = -1;
		double length = Math.max(a.size(), b.size());
		if (length==0) return 1;
		double[][] score = stress?scoreS:scoreU;
		if (a.size()==1 && b.size()==1) {
			return score[Math.min(Stats.indexVal(a.get(0)), Stats.indexVal(b.get(0)))][Math.max(Stats.indexVal(a.get(0)), Stats.indexVal(b.get(0)))];
		}
		double[][] matrix = new double[a.size()+1][b.size()+1];
		matrix[0][0] = 0;
		for (int i=1; i<matrix.length; i++) {
			if (this.st.sc != null && b.size()>0) {
				matrix[i][0] = matrix[i-1][0] + score[Stats.indexVal(a.get(i-1))][Stats.PNUM-2] + gap - 1;
			} else {
				matrix[i][0] = matrix[i-1][0] + score[Stats.indexVal(a.get(i-1))][Stats.PNUM-1] + gap;
			}
		}
		for (int j=1; j<matrix[0].length; j++) {
			if (this.st.sc != null && a.size()>0) {
				matrix[0][j] = matrix[0][j-1] + score[Stats.indexVal(b.get(j-1))][Stats.PNUM-2] + gap - 1;
			} else {
				matrix[0][j] = matrix[0][j-1] + score[Stats.indexVal(b.get(j-1))][Stats.PNUM-1] + gap;
			}
		}
		for (int i=1; i<matrix.length; i++) {
			int indexA = Stats.indexVal(a.get(i-1));
			for (int j=1; j<matrix[i].length; j++) {
				int indexB = Stats.indexVal(b.get(j-1));
				double indel = Math.max(matrix[i-1][j] + score[indexA][Stats.PNUM-1], matrix[i][j-1] + score[indexB][Stats.PNUM-1]) + gap;
				double match = matrix[i-1][j-1] + score[Math.min(indexA, indexB)][Math.max(indexA, indexB)];
				matrix[i][j] = Math.max(indel, match);
			}
		}
		//matrix[matrix.length-1][matrix[0].length-1] -= Math.abs(a.size()-b.size())*gap;
		return matrix[matrix.length-1][matrix[0].length-1]/length;
	}
	
	public int rhymeStart(ArrayList<Syllable> a, ArrayList<Syllable> b) {
		int lowSize = Math.min(a.size(), b.size());
		int ret = 1;
		Syllable aSyl;
		Syllable bSyl;
		for (int i=1; i<=lowSize; i++) {
			aSyl = a.get(a.size()-i);
			bSyl = b.get(b.size()-i);
			if (aSyl.getStress()>0 || bSyl.getStress()>0) {
				if (sScore(aSyl,bSyl)>=0) {
					ret = i;
				} else {
					break;
				}
			} else if (sScore(aSyl,bSyl)<-1) {
				break;
			}
		}
		return ret;
	}
	
	public double score(int indexA, int indexB, boolean stress) {
		int[] count;
		int[][] match;
		double[][] score;
		int vSum;
		int cSum;
		int vMatchSum;
		int cMatchSum;
		
		if (!split) {
			count = MatrixTools.arrayAdd(st.countS, st.countU);
			match = MatrixTools.arrayAdd(st.matchS, st.matchU);
			score = scoreS;
			vSum = st.vSumS + st.vSumU;
			cSum = st.cSumS + st.cSumU;
			vMatchSum = st.vMatchSumS + st.vMatchSumU;
			cMatchSum = st.cMatchSumS + st.cMatchSumU;
		} else if (stress) {
			count = st.countS;
			match = st.matchS;
			score = scoreS;
			vSum = st.vSumS;
			cSum = st.cSumS;
			vMatchSum = st.vMatchSumS;
			cMatchSum = st.cMatchSumS;
		} else {
			count = st.countU;
			match = st.matchU;
			score = scoreU;
			vSum = st.vSumU;
			cSum = st.cSumU;
			vMatchSum = st.vMatchSumU;
			cMatchSum = st.cMatchSumU;
		}
		
		int countA = count[(indexA<0)?Stats.PNUM-1:indexA];
		int countB = count[(indexB<0)?Stats.PNUM-1:indexB];
		
		if (countA==0 || countB==0) {
			//System.out.println("No occurrences of this symbol?");
			score[indexA][indexB] = 0;
			return score[indexA][indexB];
		}
		double exp = 0;
		double occ = 0;
		if (Stats.isVowel(Stats.phoneVal(indexA))) {
			if (!Stats.isVowel(Stats.phoneVal(indexB))) {
				System.out.println("Attempt to score vowel with consonant.");
				score[indexA][indexB] = -5;
				return score[indexA][indexB];
			}
			exp = ((double)countA/(double)vSum)*((double)countB/(double)vSum);
			occ = ((double)(match[Math.min(indexA,indexB)][Math.max(indexA,indexB)]+0.1)/(double)(vMatchSum+0.1));
		} else {
			if (Stats.isVowel(Stats.phoneVal(indexB))) {
				System.out.println("Attempt to score vowel with consonant.");
				score[indexA][indexB] = -5;
				return score[indexA][indexB];
			}
			exp = ((double)countA/(double)cSum)*((double)countB/(double)cSum);
			occ = ((double)(match[Math.min(indexA,indexB)][Math.max(indexA,indexB)]+0.01)/(double)(cMatchSum+0.01)); 
		}
		
		score[Math.min(indexA,indexB)][Math.max(indexA,indexB)] = Math.log(occ/exp);
		//dirty hack! make sure all exact matches are nonnegative
		if (indexA==indexB && score[indexA][indexB]<0) {
			score[indexA][indexB] = 0;
		}
		return score[Math.min(indexA,indexB)][Math.max(indexA,indexB)];
	}
	
	public void setScores() {
		double expGap = 0;
		for (int i=1; i<st.clusterCountS.length; i++) {
			for (int j=0; j<i; j++) {
				expGap += (double)(i-j)*(double)st.clusterCountS[i]/(double)st.vSumS*(double)st.clusterCountS[j]/(double)st.vSumS;
			}
		}
		if (st.countS[Stats.PNUM-1]==0) {
			st.countS[Stats.PNUM-1] = (int)(expGap*st.vSumS);
		}
		
		if (split) {
			expGap = 0;
			for (int i=1; i<st.clusterCountU.length; i++) {
				for (int j=0; j<i; j++) {
					expGap += (double)(i-j)*(double)st.clusterCountU[i]/(double)st.vSumU*(double)st.clusterCountU[j]/(double)st.vSumU;
				}
			}
			if (st.countU[Stats.PNUM-1]==0) {
				st.countU[Stats.PNUM-1] = (int)(expGap*st.vSumU);
			}
		}
		
		for (int i=0; i<Stats.PNUM; i++) {
			for (int j=i; j<Stats.PNUM; j++) {
				if (Stats.isVowel(Stats.phoneVal(i)) == Stats.isVowel(Stats.phoneVal(j))) {
					score(i,j, true);
					if (split) score(i,j, false);
				}
			}
		}
	}
	
	public void outScores(String fName) throws Exception {
		BufferedWriter f = new BufferedWriter(new FileWriter(fName));
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		f.write("Stressed Vowel scores");
		f.newLine();
		f.write("      ");
		for (int i=0; i<Stats.PNUM; i++) {
			String ph = Stats.phoneVal(i);
			if (Stats.isVowel(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.newLine();
		for (int i=0; i<Stats.PNUM; i++) {
			String phi = Stats.phoneVal(i);
			if (Stats.isVowel(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<Stats.PNUM; j++) {
					String phj = Stats.phoneVal(j);
					if (Stats.isVowel(phj)) {
						//DecimalFormat twoDecimalPlaces = new DecimalFormat("#0.00")
						
						String num = nf.format(scoreS[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.newLine();
		f.write("Stressed Consonant scores");
		f.newLine();
		f.write("      ");
		for (int i=0; i<Stats.PNUM; i++) {
			String ph = Stats.phoneVal(i);
			if (Stats.isConsonant(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.write("-");
		f.newLine();
		for (int i=0; i<Stats.PNUM; i++) {
			String phi = Stats.phoneVal(i);
			if (Stats.isConsonant(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<Stats.PNUM; j++) {
					if (!Stats.isVowel(Stats.phoneVal(j))) {
						String num = nf.format(scoreS[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		if (!split) {
			f.close();
			return;
		}
		
		f.write("Unstressed Vowel scores");
		f.newLine();
		f.write("      ");
		for (int i=0; i<Stats.PNUM; i++) {
			String ph = Stats.phoneVal(i);
			if (Stats.isVowel(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.newLine();
		for (int i=0; i<Stats.PNUM; i++) {
			String phi = Stats.phoneVal(i);
			if (Stats.isVowel(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<Stats.PNUM; j++) {
					String phj = Stats.phoneVal(j);
					if (Stats.isVowel(phj)) {
						//DecimalFormat twoDecimalPlaces = new DecimalFormat("#0.00")
						
						String num = nf.format(scoreU[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.newLine();
		f.write("Unstressed Consonant scores");
		f.newLine();
		f.write("      ");
		for (int i=0; i<Stats.PNUM; i++) {
			String ph = Stats.phoneVal(i);
			if (Stats.isConsonant(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.write("-");
		f.newLine();
		for (int i=0; i<Stats.PNUM; i++) {
			String phi = Stats.phoneVal(i);
			if (Stats.isConsonant(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<Stats.PNUM; j++) {
					if (!Stats.isVowel(Stats.phoneVal(j))) {
						String num = nf.format(scoreU[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.close();
	}
	
	/*public Matrix createVowelMatrix(boolean stress) {
		Matrix ret = Matrix.identity(Stats.VNUM, Stats.VNUM);
		int iInd = -1;
		for (int i=0; i<ret.getColumnDimension(); i++) {
			iInd++;
			while (!Stats.isVowel(Stats.phoneVal(iInd))) {
				iInd++;
			}
			int jInd = -1;
			for (int j=0; j<ret.getColumnDimension(); j++) {
				jInd++;
				while (!Stats.isVowel(Stats.phoneVal(jInd))) {
					jInd++;
				}
				if (stress || !split) {
					ret.set(i, j, scoreS[Math.min(iInd, jInd)][Math.max(iInd, jInd)]);
				} else {
					ret.set(i, j, scoreU[Math.min(iInd, jInd)][Math.max(iInd, jInd)]);
				}
			}
		}
		return ret;
	}*/
	
	/*public Matrix createConsMatrix(boolean stress) {
		Matrix ret = Matrix.identity(Stats.CNUM, Stats.CNUM);
		int iInd = -1;
		for (int i=0; i<ret.getColumnDimension(); i++) {
			iInd++;
			while (!Stats.isConsonant(Stats.phoneVal(iInd))) {
					iInd++;
			}
			int jInd = -1;
			for (int j=0; j<ret.getColumnDimension(); j++) {
				jInd++;
				while (!Stats.isConsonant(Stats.phoneVal(jInd))) {
					jInd++;
				}
				if (stress || !split) {
					ret.set(i, j, scoreS[Math.min(iInd, jInd)][Math.max(iInd, jInd)]);
				} else {
					ret.set(i, j, scoreU[Math.min(iInd, jInd)][Math.max(iInd, jInd)]);
				}
			}
		}
		return ret;
	}*/
	
}

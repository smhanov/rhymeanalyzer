package backend;

import java.io.*;
import java.util.ArrayList;
import java.text.NumberFormat;

//import Jama.Matrix;

public class Stats {

	public static final String STAT_FILE = "stats.txt";
	public static final int PNUM = 41;
	public static final int VNUM = 15;
	public static final int CNUM = 24;
	public static final boolean FLAT = false;
	public static final boolean SPLIT = true;
	public static final int MAX_CLUSTER = 5;
	
	protected Scoring sc;
	
	public int countLines = 0;
	
	protected int[] countS;
	protected int[] countU;
	
	protected int vSumS;
	protected int cSumS;
	protected int vMatchSumS;
	protected int cMatchSumS;
	protected int vSumU;
	protected int cSumU;
	protected int vMatchSumU;
	protected int cMatchSumU;
	protected int mixedStress;
	
	protected int[] clusterCountS;
	protected int[][] clusterMatchS;
	protected int[] clusterCountU;
	protected int[][] clusterMatchU;

	protected int[][] matchS;
	protected int[][] matchU;
	
	protected ArrayList<String> lastS;
	protected ArrayList<String> lastU;
	
	public Stats(boolean fresh) throws Exception {
		if (fresh) {
			resetCounts();
		} else {
			readIn(STAT_FILE);
		}
	}
	
	public Stats(String fileName) throws Exception {
		readIn(fileName);
	}
	
	public Stats(Scoring _sc) {
		this.sc = _sc;
		resetCounts();
	}
	
	public void procRhymes(String fName, String method, Transcriptor tr) throws Exception {
		BufferedReader fi = new BufferedReader(new FileReader(fName));
		String aLine = "";
		String bLine = "";
		while (fi.ready()) {
			while (aLine.isEmpty()) {
				aLine = fi.readLine();
			}
			bLine = fi.readLine();
			if (bLine.isEmpty()) {
				System.out.println("Error in input file!");
				System.out.println("No match for: " + aLine);
				return;
			}
			procRhyme(tr.transcribe(aLine), tr.transcribe(bLine), method);
			countLines++;
			aLine = "";
		}
	}
	
	public void procRhyme(PLine a, PLine b, String method) {
		if (a.size()<1 || b.size()<1) return;
		ArrayList<Syllable> aSyl = a.getSyllables(Stats.FLAT);
		ArrayList<Syllable> bSyl = b.getSyllables(Stats.FLAT);
		
		for (int i=0; i<a.size(); i++) {
			countWord(a.get(i), ("iteration".equals(method)));
		}
		for (int i=0; i<b.size(); i++) {
			countWord(b.get(i), ("iteration".equals(method)));
		}
		
		if ("primary".equals(method)) {
			// only match syllables with primary stress
			int sp = stressPlace(aSyl, bSyl);
			if (aSyl.get(aSyl.size()-sp).getStress()>0 && bSyl.get(bSyl.size()-sp).getStress()>0) { 
				procRhyme(aSyl.get(aSyl.size()-sp), bSyl.get(bSyl.size()-sp), false);
			}
		} else if ("all".equals(method)) {
			// match all syllables following final primary stress
			int sp = stressPlace(aSyl, bSyl);
			for (int i=1; i<=sp; i++) {
				procRhyme(aSyl.get(aSyl.size()-i), bSyl.get(bSyl.size()-i), false);
			}
		} else if ("syllable".equals(method)) {
			// match last syllable
			procRhyme(aSyl.get(aSyl.size()-1), bSyl.get(bSyl.size()-1), false);
		} else if ("iteration".equals(method)) {
			// match all ending syllables considered rhyming
			int rp = sc.rhymeStart(aSyl, bSyl);
			for (int i=1; i<=rp; i++) {
				procRhyme(aSyl.get(aSyl.size()-i), bSyl.get(bSyl.size()-i), true);
			}
		}
	}
	
	public void procRhyme(Syllable a, Syllable b, boolean iter) {
		int aVow = indexVal(a.getVowel());
		int bVow = indexVal(b.getVowel());
		int[][] match = (a.getStress()>0 || b.getStress()>0)?matchS:matchU;
		int[][] clusterMatch = (a.getStress()>0 || b.getStress()>0)?clusterMatchS:clusterMatchU;
		
		match[Math.min(aVow,bVow)][Math.max(aVow,bVow)]++;
		if (a.getStress()>0 || b.getStress()>0) { 
			vMatchSumS++;
			if (a.getStress()==0 || b.getStress()==0) mixedStress++;
		} else { 
			vMatchSumU++;
		}
		
		ArrayList<String> aList = a.getEndC();
		ArrayList<String> bList = b.getEndC();
		int aSize = Math.min(MAX_CLUSTER-1, aList.size());
		int bSize = Math.min(MAX_CLUSTER-1, bList.size());
		clusterMatch[Math.min(aSize,bSize)][Math.max(aSize,bSize)]++;

		if (iter) {
			align(aList, bList, (a.getStress()>0 || b.getStress()>0), this.sc, false);
		} else {
			int aCon;
			int bCon;
			for (int i=0; i<aList.size(); i++) {
				aCon = indexVal(aList.get(i));
				if (i<bList.size()) {
					bCon = indexVal(bList.get(i));
					match[Math.min(aCon,bCon)][Math.max(aCon,bCon)]++;
				} else {
					match[aCon][PNUM-1]++;
				}
				if (a.getStress()>0 || b.getStress()>0) { cMatchSumS++;
				} else { cMatchSumU++;
				}
			}
			if (bList.size()>aList.size()) { 
				for (int i=aList.size(); i<bList.size(); i++) {
					bCon = indexVal(bList.get(i));
					match[bCon][PNUM-1]++;
					if (a.getStress()>0 || b.getStress()>0) { cMatchSumS++;
					} else { cMatchSumU++;
					}
				}
			}
		}
	}
	
	public void countWord(PWord pw, boolean iter) {
		ArrayList<Syllable> syls = pw.getSyllables();
		for (int i=0; i<syls.size(); i++) {
			Syllable s = syls.get(i);
			int clust = Math.min(MAX_CLUSTER-1, s.getEndC().size());
			if (s.getStress()>0) {
				clusterCountS[clust]++;
				countS[indexVal(s.getVowel())]++;
				vSumS++;
				for (int j=0; j<s.getStartC().size(); j++) {
					//countS[indexVal(s.getStartC().get(j))]++;
					//cSumS++;
				}
				if (iter) {
					if (lastS == null) {
						lastS = s.getEndC();
					}
					align(lastS, s.getEndC(), true, this.sc, true);
					lastS = s.getEndC();
				} else {
					for (int j=0; j<s.getEndC().size(); j++) {
						countS[indexVal(s.getEndC().get(j))]++;
						cSumS++;
					}
				}
			} else {
				clusterCountU[clust]++;
				countU[indexVal(s.getVowel())]++;
				vSumU++;
				if (iter) {
					if (lastU == null) {
						lastU = s.getEndC();
					}
					align(lastU, s.getEndC(), false, this.sc, true);
					lastU = s.getEndC();
				} else {
					for (int j=0; j<s.getStartC().size(); j++) {
						//countU[indexVal(s.getStartC().get(j))]++;
						//cSumU++;
					}
					for (int j=0; j<s.getEndC().size(); j++) {
						countU[indexVal(s.getEndC().get(j))]++;
						cSumU++;
					}
				}
			}
		}
	}
	
	// return ending number of syllables including a primary stress if available
	public static int stressPlace(ArrayList<Syllable> a, ArrayList<Syllable> b) {
		int lowSize = Math.min(a.size(), b.size());
		int ret = 1;
		Syllable aSyl;
		Syllable bSyl;
		
		for (int i=1; i<=lowSize; i++) {
			ret = i;
			aSyl = a.get(a.size()-i);
			bSyl = b.get(b.size()-i);
			if (aSyl.getStress()!=0) break;
			if (bSyl.getStress()!=0) break;
		}
		return ret;
	}
	
	public void resetCounts() {
		vSumS = 0;
		cSumS = 0;
		vMatchSumS = 0;
		cMatchSumS = 0;
		
		vSumU = 0;
		cSumU = 0;
		vMatchSumU = 0;
		cMatchSumU = 0;
		
		mixedStress = 0;
		
		countS = new int[PNUM];
		matchS = new int[PNUM][PNUM];
		countU = new int[PNUM];
		matchU = new int[PNUM][PNUM];
		
		for (int i=0; i<PNUM; i++) {
			countS[i] = 0;
			countU[i] = 0;
			for (int j=i; j<PNUM; j++) {
				matchS[i][j] = 0;
				matchU[i][j] = 0;
			}
		}
		
		clusterCountS = new int[MAX_CLUSTER];
		clusterMatchS = new int[MAX_CLUSTER][MAX_CLUSTER];
		clusterCountU = new int[MAX_CLUSTER];
		clusterMatchU = new int[MAX_CLUSTER][MAX_CLUSTER];
		for (int i=0; i<MAX_CLUSTER; i++) {
			clusterCountS[i] = 0;
			clusterCountU[i] = 0;
			for (int j=0; j<MAX_CLUSTER; j++) {
				clusterMatchS[i][j] = 0;
				clusterMatchU[i][j] = 0;
			}
		}
	}
	
	private void align(ArrayList<String> a, ArrayList<String> b, boolean stress, Scoring sc, boolean count) {
		double gap = -1.5;
		int[][] match = stress?matchS:matchU;
		double[][] score = stress?sc.scoreS:sc.scoreU;
		double[][] matrix = new double[a.size()+1][b.size()+1];
		int[][] pointer = new int[a.size()+1][b.size()+1];
		int[] countMat = new int[PNUM];
		if (count) {
			countMat = (stress)?countS:countU;
		}
		
		matrix[0][0] = 0;
		for (int i=1; i<matrix.length; i++) {
			matrix[i][0] = matrix[i-1][0] + score[Stats.indexVal(a.get(i-1))][Stats.PNUM-1] + gap;
			pointer[i][0] = 1;
		}
		for (int j=1; j<matrix[0].length; j++) {
			matrix[0][j] = matrix[0][j-1] + score[Stats.indexVal(b.get(j-1))][Stats.PNUM-1] + gap;
			pointer[0][j] = -1;
		}
		for (int i=1; i<matrix.length; i++) {
			int indexA = Stats.indexVal(a.get(i-1));
			for (int j=1; j<matrix[i].length; j++) {
				int indexB = Stats.indexVal(b.get(j-1));
				double indelB = matrix[i][j-1] + score[indexB][Stats.PNUM-1] + gap;
				double indelA = matrix[i-1][j] + score[indexA][Stats.PNUM-1] + gap;
				double matchAB = matrix[i-1][j-1] + score[Math.min(indexA, indexB)][Math.max(indexA, indexB)];
				if (indelB>=indelA && indelB>matchAB) {
					matrix[i][j] = indelB;
					pointer[i][j] = -1;
				} else if (indelA>=indelB && indelA>matchAB) {
					matrix[i][j] = indelA;
					pointer[i][j] = 1;
				} else {
					matrix[i][j] = matchAB;
					pointer[i][j] = 0;
				}
			}
		}
		
		boolean matched = false;
		int i = a.size();
		int j = b.size();
		while (i>0 || j>0) {
			if (pointer[i][j] == 0) {
				int indexA = Stats.indexVal(a.get(i-1));
				int indexB = Stats.indexVal(b.get(j-1));
				if (count) {
					countMat[indexB]++;
				} else {
					match[Math.min(indexA, indexB)][Math.max(indexA, indexB)]++;
				}
				matched = true;
				i--;
				j--;
			} else if (pointer[i][j] == 1) {
				int indexA = Stats.indexVal(a.get(i-1));
				if (count && matched) {
					countMat[PNUM-2]++;
				} else if (count) {
					countMat[PNUM-1]++;
				} else if (matched) {
					match[indexA][Stats.PNUM-2]++;
				} else {
					match[indexA][Stats.PNUM-1]++;
				}
				i--;
			} else if (pointer[i][j] == -1) {
				int indexB = Stats.indexVal(b.get(j-1));
				if (count) {
					countMat[indexB]++;
				} else if (matched) {
					match[indexB][Stats.PNUM-2]++;
				} else {
					match[indexB][Stats.PNUM-1]++;
				}
				j--;
			}
			if (stress) {
				if (count) {
					cSumS++;
				} else {
					cMatchSumS++;
				}
			} else {
				if (count) {
					cSumU++;
				} else {
					cMatchSumU++;
				}
			}
		}
	}
	
	private void readIn(String fName) throws Exception {
		vSumS = 0;
		cSumS = 0;
		vMatchSumS = 0;
		cMatchSumS = 0;
		
		vSumU = 0;
		cSumU = 0;
		vMatchSumU = 0;
		cMatchSumU = 0;
		
		countS = new int[PNUM];
		matchS = new int[PNUM][PNUM];
		countU = new int[PNUM];
		matchU = new int[PNUM][PNUM];
		
		clusterCountS = new int[MAX_CLUSTER];
		clusterMatchS = new int[MAX_CLUSTER][MAX_CLUSTER];
		clusterCountU = new int[MAX_CLUSTER];
		clusterMatchU = new int[MAX_CLUSTER][MAX_CLUSTER];
		
		//BufferedReader f = new BufferedReader(new FileReader(fName));
		InputStream in = this.getClass().getResourceAsStream(fName);
                BufferedReader f = new BufferedReader(new InputStreamReader(in));

                String line = "";
		for (int i=0; i<PNUM; i++) {
			line = f.readLine();
			countS[i] = Integer.parseInt(line);
			if (isVowel(phoneVal(i))) {
				vSumS += countS[i];
			} else {
				cSumS += countS[i];
			}
		}
		
		line = f.readLine();
		for (int i=0; i<PNUM; i++) {
			line = f.readLine();
			String[] ms = line.split(" ");
			for (int j=0; j<ms.length; j++) {
				matchS[i][j] = Integer.parseInt(ms[j]);
				if (isVowel(phoneVal(i))) {
					vMatchSumS += matchS[i][j];
				} else {
					cMatchSumS += matchS[i][j];
				}
			}
		}
		
		line = f.readLine();
		for (int i=0; i<MAX_CLUSTER; i++) {
			line = f.readLine();
			String[] ms = line.split(" ");
			for (int j=0; j<ms.length; j++) {
				clusterMatchS[i][j] = Integer.parseInt(ms[j]);
			}
		}
		line = f.readLine();
		for (int i=0; i<MAX_CLUSTER; i++) {
			line = f.readLine();
			clusterCountS[i] = Integer.parseInt(line);
		}
		
		line = f.readLine();
		for (int i=0; i<PNUM; i++) {
			line = f.readLine();
			countU[i] = Integer.parseInt(line);
			if (isVowel(phoneVal(i))) {
				vSumU += countU[i];
			} else {
				cSumU += countU[i];
			}
		}
		
		line = f.readLine();
		for (int i=0; i<PNUM; i++) {
			line = f.readLine();
			String[] ms = line.split(" ");
			for (int j=0; j<ms.length; j++) {
				matchU[i][j] = Integer.parseInt(ms[j]);
				if (isVowel(phoneVal(i))) {
					vMatchSumU += matchU[i][j];
				} else {
					cMatchSumU += matchU[i][j];
				}
			}
		}
		
		line = f.readLine();
		for (int i=0; i<MAX_CLUSTER; i++) {
			line = f.readLine();
			String[] ms = line.split(" ");
			for (int j=0; j<ms.length; j++) {
				clusterMatchU[i][j] = Integer.parseInt(ms[j]);
			}
		}
		
		line = f.readLine();
		for (int i=0; i<MAX_CLUSTER; i++) {
			line = f.readLine();
			clusterCountU[i] = Integer.parseInt(line);
		}
		
		line = f.readLine();
		mixedStress = Integer.parseInt(line);
		
		f.close();
	}
	
	public void saveStats(String fName) throws Exception {
		if (fName==null) {
			fName = STAT_FILE;
		}
		if (fName.isEmpty()) {
			fName = STAT_FILE;
		}
		BufferedWriter f = new BufferedWriter(new FileWriter(fName));
		for (int i=0; i<PNUM; i++) {
			f.write(String.valueOf(countS[i]));
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<PNUM; i++) {
			for (int j=0; j<PNUM; j++) {
				f.write(matchS[i][j] + " ");
			}
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<MAX_CLUSTER; i++) {
			for (int j=0; j<MAX_CLUSTER; j++) {
				f.write(clusterMatchS[i][j] + " ");
			}
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<MAX_CLUSTER; i++) {
			f.write(clusterCountS[i] + "");
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<PNUM; i++) {
			f.write(String.valueOf(countU[i]));
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<PNUM; i++) {
			for (int j=0; j<PNUM; j++) {
				f.write(matchU[i][j] + " ");
			}
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<MAX_CLUSTER; i++) {
			for (int j=0; j<MAX_CLUSTER; j++) {
				f.write(clusterMatchU[i][j] + " ");
			}
			f.newLine();
		}
		f.newLine();
		
		for (int i=0; i<MAX_CLUSTER; i++) {
			f.write(clusterCountU[i] + "");
			f.newLine();
		}
		
		f.write(mixedStress + "");
		f.newLine();
		
		f.close();
	}
	
	public void outStats(String fName) throws Exception {
		BufferedWriter f = new BufferedWriter(new FileWriter(fName));
		
		f.write("Stressed Vowel matches");
		f.newLine();
		f.write("      ");
		for (int i=0; i<PNUM; i++) {
			String ph = phoneVal(i);
			if (isVowel(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.newLine();
		for (int i=0; i<PNUM; i++) {
			String phi = phoneVal(i);
			if (isVowel(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<PNUM; j++) {
					String phj = phoneVal(j);
						if (isVowel(phj)) {
						String num = String.valueOf(matchS[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.newLine();
		f.write("Stressed Consonant matches");
		f.newLine();
		f.write("      ");
		for (int i=0; i<PNUM; i++) {
			String ph = phoneVal(i);
			if (isConsonant(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.write("-");
		f.newLine();
		for (int i=0; i<PNUM; i++) {
			String phi = phoneVal(i);
			if (isConsonant(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<PNUM; j++) {
					if (!isVowel(phoneVal(j))) {
						String num = String.valueOf(matchS[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.write("Counts");
		f.newLine();
		for (int i=0; i<PNUM; i++) {
			f.write(phoneVal(i) + ": " + countS[i]);
			f.newLine();
		}
		f.newLine();
		
		f.write("Unstressed Vowel matches");
		f.newLine();
		f.write("      ");
		for (int i=0; i<PNUM; i++) {
			String ph = phoneVal(i);
			if (isVowel(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.newLine();
		for (int i=0; i<PNUM; i++) {
			String phi = phoneVal(i);
			if (isVowel(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<PNUM; j++) {
					String phj = phoneVal(j);
						if (isVowel(phj)) {
						String num = String.valueOf(matchU[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.newLine();
		f.write("Unstressed Consonant matches");
		f.newLine();
		f.write("      ");
		for (int i=0; i<PNUM; i++) {
			String ph = phoneVal(i);
			if (isConsonant(ph)) {
				f.write(ph + "    " + ((ph.length()==1)?" ":""));
			}
		}
		f.write("-");
		f.newLine();
		for (int i=0; i<PNUM; i++) {
			String phi = phoneVal(i);
			if (isConsonant(phi)) {
				f.write(phi + "    " + ((phi.length()==1)?" ":""));
				for (int j=0; j<PNUM; j++) {
					if (!isVowel(phoneVal(j))) {
						String num = String.valueOf(matchU[i][j]);
						f.write(num);
						for (int k=0; k<6-num.length(); k++) f.write(" ");
					}
				}
				f.newLine();
			}
		}
		f.newLine();
		
		f.write("Counts");
		f.newLine();
		for (int i=0; i<PNUM; i++) {
			f.write(phoneVal(i) + ": " + countU[i]);
			f.newLine();
		}
		f.newLine();
		
		f.close();
	}
	
	public static int stressPart(String phoneme) {
		if (phoneme == null || !isVowel(phoneme)) {
			return -1;
		}
		if (phoneme.length()>2) {
			return Integer.parseInt(phoneme.substring(2));
		}
		return 1;
	}
	
	public static int indexVal(String phoneme) {
		if (phoneme == null) return PNUM-1;
		if (phoneme.isEmpty()) return PNUM-1;
		if (phoneme.length()>3)	return PNUM-1;
		String mainPart;
		if (phoneme.length()>2) {
			mainPart = phoneme.substring(0,2);
		} else {
			mainPart = phoneme;
		}
		if (mainPart.equals("AA")) return 0;
        if (mainPart.equals("AE")) return 1;
        if (mainPart.equals("AH")) return 2;
        if (mainPart.equals("AO")) return 3;
        if (mainPart.equals("AW")) return 4;
        if (mainPart.equals("AY")) return 5;
        if (mainPart.equals("B")) return 6;
        if (mainPart.equals("CH")) return 7;
        if (mainPart.equals("D")) return 8;
        if (mainPart.equals("DH")) return 9;
        if (mainPart.equals("EH")) return 10;
        if (mainPart.equals("ER")) return 11;
        if (mainPart.equals("EY")) return 12;
        if (mainPart.equals("F")) return 13;
        if (mainPart.equals("G")) return 14;
        if (mainPart.equals("HH")) return 15;
        if (mainPart.equals("IH")) return 16;
        if (mainPart.equals("IY")) return 17;
        if (mainPart.equals("JH")) return 18;
        if (mainPart.equals("K")) return 19;
        if (mainPart.equals("L")) return 20;
        if (mainPart.equals("M")) return 21;
        if (mainPart.equals("N")) return 22;
        if (mainPart.equals("NG")) return 23;
        if (mainPart.equals("OW")) return 24;
        if (mainPart.equals("OY")) return 25;
        if (mainPart.equals("P")) return 26;
        if (mainPart.equals("R")) return 27;
        if (mainPart.equals("S")) return 28;
        if (mainPart.equals("SH")) return 29;
        if (mainPart.equals("T")) return 30;
        if (mainPart.equals("TH")) return 31;
        if (mainPart.equals("UH")) return 32;
        if (mainPart.equals("UW")) return 33;
        if (mainPart.equals("V")) return 34;
        if (mainPart.equals("W")) return 35;
        if (mainPart.equals("Y")) return 36;
        if (mainPart.equals("Z")) return 37;
        if (mainPart.equals("ZH")) return 38;
		return PNUM-1;
	}
	
	public static String phoneVal(int indexNum) {
		switch (indexNum) {
			case 0: return "AA";
			case 1: return "AE";
			case 2: return "AH";
			case 3: return "AO";
			case 4: return "AW";
			case 5: return "AY";
			case 6: return "B";
			case 7: return "CH";
			case 8: return "D";
			case 9: return "DH";
			case 10: return "EH";
			case 11: return "ER";
			case 12: return "EY";
			case 13: return "F";
			case 14: return "G";
			case 15: return "HH";
			case 16: return "IH";
			case 17: return "IY";
			case 18: return "JH";
			case 19: return "K";
			case 20: return "L";
			case 21: return "M";
			case 22: return "N";
			case 23: return "NG";
			case 24: return "OW";
			case 25: return "OY";
			case 26: return "P";
			case 27: return "R";
			case 28: return "S";
			case 29: return "SH";
			case 30: return "T";
			case 31: return "TH";
			case 32: return "UH";
			case 33: return "UW";
			case 34: return "V";
			case 35: return "W";
			case 36: return "Y";
			case 37: return "Z";
			case 38: return "ZH";
		}
        return "";
	}
	
	public static boolean isVowel(String phoneme) {
		int val = indexVal(phoneme);
		if (val>=0 && val<=5) return true;
		if (val>=10 && val<=12) return true;
		if (val==16 || val==17 || val==24 || val==25 || val==32 || val==33) return true;
		return false;
	}
	
	public static boolean isConsonant(String phoneme) {
		int val = indexVal(phoneme);
		if (val>=0 && val<PNUM-1 && !isVowel(phoneme)) return true;
		return false;
	}
}
package backend;

import java.util.ArrayList;

// Main class for returning rhyme stat features (as Analysis object)
// Needs a RhymeCollection with corresponding ArrayList<PLine>
public class Analyzer {

	public static final double EVEN_THRESH = 0.85;

	private String name;
	private Scoring scorer;
	private RhymeCollection rhymes;
	private double numLines;
	private double[] lineLengths;
	private double numWords;

	private double numSylls;
	private double numRhymes;
	private double weightedNum;

	private double numPerfect;
	private double numDouble;
	private double numTriple;
	private double numQuad;
	private double numLong;

	private double numInt;
	private double numLink;
	private double numBridge;
	private double chainCount;
	private double numCompound;

	private double endScore;
	private double endSylCount;
	private double endCount;
	private double growCount;
	private double shrinkCount;
	private double evenCount;

	private double diffWords;

	public Analyzer(String n, Scoring sc) {
		this.name = n;
		this.scorer = sc;
	}

	public boolean addRhymes(RhymeCollection rc) {
		if (rc.lines==null) return false;
		rhymes = rc;
		return true;
	}

	public boolean addRhymes(RhymeCollection rc, ArrayList<PLine> ls) {
		if (rc.lines!=null) {
			if (rc.lines.equals(ls)) {
				rhymes = rc;
				return true;
			}
		}
		if (rc.size()!=ls.size()) return false;
		for (int i=0; i<rc.collection.length; i++) {
			ArrayList<Rhyme> rs = rc.collection[i];
			for (int j=0; j<rs.size(); j++) {
				Rhyme r = rs.get(j);
				if (r.aEnd().syllable >= ls.get(r.aEnd().line).getSyllables(Stats.FLAT).size()) {
					System.err.println("Bad rhyme address:" + r.aEnd().line + ", " + r.aEnd().syllable);
					return false;
				}
				if (r.bEnd().syllable >= ls.get(r.bEnd().line).getSyllables(Stats.FLAT).size()) {
					System.err.println("Bad rhyme address:" + r.bEnd().line + ", " + r.bEnd().syllable);
					return false;
				}
			}
		}
		rc.lines = ls;
		rhymes = rc;
		return true;
	}

	public void analyze() {
		Analysis an = createAnalysis();
		if (an == null) {
			System.out.println("Could not create analysis object. No rhymes.");
			return;
		}

		System.out.println("Analysis_for " + name + ":");
		System.out.println("Num_Lines " + numLines);
		System.out.println("Num_Syllables " + numSylls);
		System.out.println("Num_Rhymes " + numRhymes);
		System.out.println();

		for (int i=0; i<an.numFeatures(); i++) {
			System.out.println(an.getStatName(i) + " " + an.getStat(i));
			//if (i%4==3) System.out.println();
		}
		/*System.out.print("Syllables_per_Line " + (double)(numSylls[s]/numLines[s]));
		System.out.println(" std_dev: " + stddev(numSylls,numLines));
		System.out.print("Syllables_per_Word " + (double)(numSylls[s]/numWords[s]));
		System.out.println(" std_dev: " + stddev(numSylls,numWords));
		System.out.println();
		System.out.print("Rhymes_per_Line " + (double)(numRhymes[s]/numLines[s]));
		System.out.println(" std_dev: " + stddev(numRhymes,numLines));
		System.out.print("Rhymes_per_Syllable " + (double)(numRhymes[s]/numSylls[s]));
		System.out.println(" std_dev: " + stddev(numRhymes,numSylls));
		System.out.print("Rhyme_Density " + (double)(weightedNum[s]/numSylls[s]));
		System.out.println(" std_dev: " + stddev(weightedNum,numSylls));
		System.out.print("Average_End_Score " + (double)(endScore[s]/endCount[s]));
		System.out.println(" std_dev: " + stddev(endScore,endCount));
		System.out.print("Average_End_Syl_Score " + (double)(endScore[s]/endSylCount[s]));
		System.out.println(" std_dev: " + stddev(endScore,endSylCount));
		System.out.println();
		System.out.print("Doubles_per_Rhyme " + (double)(numDouble[s]/numRhymes[s]));
		System.out.println(" std_dev: " + stddev(numDouble,numRhymes));
		System.out.print("Triples_per_Rhyme " + (double)(numTriple[s]/numRhymes[s]));
		System.out.println(" std_dev: " + stddev(numTriple,numRhymes));
		System.out.print("Quads_per_Rhyme " + (double)(numQuad[s]/numRhymes[s]));
		System.out.println(" std_dev: " + stddev(numQuad,numRhymes));
		System.out.print("Longs_per_Rhyme " + (double)(numLong[s]/numRhymes[s]));
		System.out.println(" std_dev: " + stddev(numLong,numRhymes));
		System.out.println();
		System.out.print("Internals_per_Line " + (double)(numInt[s]/numLines[s]));
		System.out.println(" std_dev: " + stddev(numInt,numLines));
		System.out.print("Links_per_Line " + (double)(numLink[s]/numLines[s]));
		System.out.println(" std_dev: " + stddev(numLink,numLines));
		System.out.print("Bridges_per_Line " + (double)(numBridge[s]/numLines[s]));
		System.out.println(" std_dev: " + stddev(numBridge,numLines));
		System.out.print("Chaining_per_Line " + (double)(chainCount[s]/numLines[s]));
		System.out.println(" std_dev: " + stddev(chainCount,numLines));
		System.out.println();
		*/
	}

	public Analysis createAnalysis() {
		if (rhymes.size()==0) {
			return null;
		}
		countRhymes();

		Analysis ret = new Analysis();
		//ret.addStat("Num_Lines", numLines[s]);
		//ret.addStat("Num_Syllables", numSylls[s]);
		ret.addStat("Syllables_per_Line", (double)(numSylls/numLines));
		ret.addStat("Syllables_per_Word", (double)(numSylls/numWords));
		ret.addStat("Syllable_Variation", stddev(lineLengths));
		ret.addStat("Novel_Word_Proportion", (double)(diffWords/numLines));

		//ret.addStat("Num_Rhymes", numRhymes);
		ret.addStat("Rhymes_per_Line", (double)(numRhymes/numLines));
		ret.addStat("Rhymes_per_Syllable", (double)(numRhymes/numSylls));
		ret.addStat("Rhyme_Density", (double)(weightedNum/numSylls));

		ret.addStat("End_Pairs_per_Line", (double)(endCount/numLines));
		ret.addStat("End_Pairs_Grown", (double)(growCount/endCount));
		ret.addStat("End_Pairs_Shrunk", (double)(shrinkCount/endCount));
		ret.addStat("End_Pairs_Even", (double)(evenCount/endCount));

		ret.addStat("Average_End_Score", (double)(endScore/endCount));
		ret.addStat("Average_End_Syl_Score", (double)(endScore/endSylCount));

		ret.addStat("Singles_per_Rhyme", (double)((numRhymes-numDouble-numTriple-numQuad-numLong)/numRhymes));
		ret.addStat("Doubles_per_Rhyme", (double)(numDouble/numRhymes));
		ret.addStat("Triples_per_Rhyme", (double)(numTriple/numRhymes));
		ret.addStat("Quads_per_Rhyme", (double)(numQuad/numRhymes));
		ret.addStat("Longs_per_Rhyme", (double)(numLong/numRhymes));

		ret.addStat("Perfect_Rhymes", (double)(numPerfect/numRhymes));
		ret.addStat("Line_Internals_per_Line", (double)(numInt/numLines));
		ret.addStat("Links_per_Line", (double)(numLink/numLines));
		ret.addStat("Bridges_per_Line", (double)(numBridge/numLines));
		ret.addStat("Compounds_per_Line", (double)(numCompound/numLines));
		ret.addStat("Chaining_per_Line", (double)(chainCount/numLines));

		return ret;
	}

	private void countRhymes() {
		numLines = 0;
		numWords = 0;
		numSylls = 0;
		numRhymes = 0;
		weightedNum = 0;
		numPerfect = 0;
		numDouble = 0;
		numTriple = 0;
		numQuad = 0;
		numLong = 0;
		numInt = 0;
		numLink = 0;
		numBridge = 0;
		chainCount = 0;
		numCompound = 0;
		endScore = 0;
		endCount = 0;
		endSylCount = 0;
		growCount = 0;
		shrinkCount = 0;
		evenCount = 0;
		diffWords = 0;

		ArrayList<PLine> ls = rhymes.lines;
		ArrayList<Double> lengths = new ArrayList<Double>();
		//numLines += ls.size();
		for (int j=0; j<ls.size(); j++) {
			PLine pl = ls.get(j);
			//skip title/empty lines
			if (pl.isEmpty()) continue;
			//if (j==0 && ls.size()>0 && ls.get(1).isEmpty()) continue;
			//if (j<ls.size()-1 && j>0 && ls.get(j-1).isEmpty() && ls.get(j+1).isEmpty()) continue;
			//for each line in the current collection
			numLines++;
			numWords+= pl.size();
			if (j>0 && !ls.get(j-1).isEmpty()) {
				PLine pl2 = ls.get(j-1);
				double countDiffWords = 0;
				for (int k=0; k<pl.size(); k++) {
					if (!pl2.contains(pl.get(k))) {
						countDiffWords++;
					}
				}
				diffWords += countDiffWords/(double)pl.size();
			} else {
				diffWords += 1;
			}

			ArrayList<Syllable> syls = pl.getSyllables(Stats.FLAT);
			ArrayList<Rhyme> rs = rhymes.collection[j];
			numSylls += syls.size();
			lengths.add(new Double(syls.size()));

			for (int k=0; k<rs.size(); k++) {
				//for each rhyme at this line
				Rhyme r = rs.get(k);
				ArrayList<Syllable> bSyls = ls.get(r.bStart.line).getSyllables(Stats.FLAT);
				numRhymes++;
				weightedNum += r.length;
				if (r.length==2) {
					numDouble++;
				}
				if (r.length==3) {
					numTriple++;
				}
				if (r.length==4) {
					numQuad++;
				}
				if (r.length>4) {
					numLong++;
				}

				if (rhymes.isPerfect(r)) {
					numPerfect++;
				}

				if (r.aStart.line == r.bStart.line) {
					//same line
					numInt++;
					if (r.aEnd().syllable+1 == r.bStart.syllable) {
						chainCount += r.length;
					}
					//check if compound
					for (int m=0; m<rs.size(); m++) {
						if (m!=k) {
							Rhyme r2 = rs.get(m);
							if (r.compoundIn(r2)) {
								numCompound++;
								break;
							}
						}
					}

				} else if (r.aStart.syllable > syls.size()-4) {
					//ends A
					if (bSyls.size()-r.bStart.syllable == syls.size()-r.aStart.syllable) {
						//end rhyme
						for (int m=0; m<r.length; m++) {
							endScore += scorer.sScore(syls.get(r.aStart.syllable+m), bSyls.get(r.bStart.syllable+m));
							endSylCount++;

						}
						endCount++;
						if ((double)bSyls.size() / (double)syls.size() < EVEN_THRESH) {
							shrinkCount++;
						} else if ((double)bSyls.size() / (double)syls.size() > 1/EVEN_THRESH) {
							growCount++;
						} else {
							evenCount++;
						}
					} else {
						numLink++;
					}
				} else {
					numBridge++;
				}
			}
		}
		lineLengths = new double[lengths.size()];
		for (int i=0; i<lineLengths.length; i++) {
			lineLengths[i] = lengths.get(i).doubleValue();
		}
	}

	public double stddev(double[] num) {
		if (num.length<1) {
			return -1;
		}

		double sum = 0;
		for (int i=0; i<num.length; i++) {
			sum += num[i];
		}
		double mean = sum / num.length;

		double sqSum = 0;
		for (int i=0; i<num.length; i++) {
			sqSum += (mean-num[i])*(mean-num[i]);
		}
		return Math.sqrt(sqSum/num.length);
	}

}

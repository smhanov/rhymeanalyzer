package backend;

import java.util.ArrayList;

// Class for detecting rhymes given a Scoring class
// main function takes an ArrayList<PLine> and returns a RhymeCollection
public class Detector {


	public double anchorThresh = 1; // 1 minimum score needed to start rhyme from a position
	public double monoThresh = 3.6; // 3.6 minimum score to keep a one syllable rhyme
	public double sylThresh = 2.7; // 2.7 min per syllable score for extending longer rhymes
	public int linkLength = 3; // 3 maximum distance to "jump" extend between anchors

	private Scoring scor;
	private double[][] raw;
	private boolean[] mono;
	private boolean[] anchor;
	private int[] largest;
        private int[] closest;
	private int size1;
	private int size2;
	private int curLineNum;

	public Detector(Scoring sc) {
		scor = sc;
	}

	public RhymeCollection getRhymes(ArrayList<PLine> pls) {
		RhymeCollection rc = new RhymeCollection(pls);
		for (int i=0; i<pls.size(); i++) {
			ArrayList<Syllable> lastLine;
			if (i>0) {
				lastLine = pls.get(i-1).getSyllables(Stats.FLAT);
			} else {
				lastLine = new ArrayList<Syllable>();
			}
			ArrayList<Syllable> curLine = pls.get(i).getSyllables(Stats.FLAT);
			curLineNum = i;
			size1 = lastLine.size();
			size2 = curLine.size();
			raw = new double[size1+size2][size1+size2];
			mono = new boolean[size1+size2];
			anchor = new boolean[size1+size2];

			for (int j=0; j<size1; j++) {
				Syllable sylJ = lastLine.get(j);
				if (sylJ.getStress()>0) { // || j==size1-1) {
					anchor[j] = true;
				} else {
					anchor[j] = false;
				}
				mono[j] = (sylJ.getWLength()==1 && sylJ.getWPos()==0);
			}
                        largest = new int[raw.length];
                        closest = new int[raw.length];
			for (int j=size1; j<raw.length; j++) {
                                largest[j] = 0;
                                closest[j] = raw.length;
				Syllable sylJ = curLine.get(j-size1);
				if (sylJ.getStress()>0) { // || j==raw.length-1) {
					anchor[j] = true;
				} else {
					anchor[j] = false;
				}
				mono[j] = (sylJ.getWLength()==1 && sylJ.getWPos()==0);
				for (int k=0; k<j; k++) {
					double r = scor.sScore((k<size1)?lastLine.get(k):curLine.get(k-size1), sylJ);
					raw[k][j] = r;
				}
			}

			for (int j=size1; j<raw.length; j++) {
				for (int k=j-1; k>=0; k--) {
					if ((anchor[j] || anchor[k] || j==raw.length-1 && k==size1-1) && (raw[k][j]>anchorThresh )) {
							//|| (k<j-2 && j<raw.length-2 && raw[k][j]>0 && raw[k+1][j+1]>0 && raw[k+2][j+2]>0))) {
						boolean endR = ((raw.length-j)==(size1-k) && raw.length-j<=3);
						Rhyme r = extend(k,j, endR);
                                                if (j+r.length == raw.length && k+r.length == size1)  {
                                                    endR = true;
                                                }
						if (r.length > largest[j] || (j-k) < closest[j]) {
							if (r.length>1 || (raw[k][j]>monoThresh && (anchor[j]) && (anchor[k])) || endR) {
								for (int l=0; l<r.length; l++) {
                                                                    largest[j+l] = r.length;
                                                                    closest[j+l] = j-k;
                                                                }
								if (rc.missedSyls(r)>0) {
									rc.addRhyme(r);
								}
							}
						}
					}
				}
			}
		}
		rc.consolidate();
		return rc;
	}

	private Rhyme extend(int a, int b, boolean endRhyme) {

		int aS = a;
		int aE = a;
		int bS = b;
		int bE = b;

		// test for nearby anchor to see if can form link
		for (int i=2; i<=linkLength; i++) {
			if (b+i<raw.length && a+i<b && (a+i>=size1 == a>=size1)) {
				if (raw[a][b]>=monoThresh && (anchor[a] && anchor[b] || anchor[a+i] && anchor[b+i])
						&& raw[a+i][b+i] >= monoThresh) {
					aE = a + i;
					bE = b + i;
				}
			}
		}

		// now extend rhyme forward
		while (aE<bS-1 && bE<raw.length-1 && (aE+1>=size1 == aE>=size1)) {
			if (chainScore(aS,bS,aE+1,bE+1) >= sylThresh*(aE+2-aS)*(endRhyme?0.4:1) || raw[aE+1][bE+1]>monoThresh) {
				aE++;
				bE++;
			} else {
				break;
			}
		}
		// then extend backward
		/*
		while (aS>0 && bS>size1 && (aS-1>=size1 == aS>=size1)) {
			if (raw[aS-1][bS-1] >= extendThreshB && (chainScore(aS-1,bS-1,aE,bE) >= sylThresh*(aE+2-aS)||endRhyme||raw[aS-1][bS-1]>monoThresh)) {
				aS--;
				bS--;
			} else {
				break;
			}
		}
		*/

		Rhyme ret;
		if (aS < size1) {
			ret = new Rhyme(curLineNum-1, aS, curLineNum,bS-size1, (aE-aS+1));
		} else {
			ret = new Rhyme(curLineNum, aS-size1, curLineNum, bS-size1, (aE-aS+1));
		}
		return ret;

	}

	public double chainScore(int aS, int bS, int aE, int bE) {
		double ret = 0;
		for (int i=0; i<=aE-aS; i++) {
			ret += raw[aS+i][bS+i];
		}
		return ret;
	}

	public static Syllable sylAt(ArrayList<PLine> pls, Address ad) {
		int line = ad.line;
		int sylSpot = ad.syllable;

		if (line<0 || line>=pls.size()) {
			System.err.println("Syllable access out of bounds.");
			return null;
		}
		PLine pl = pls.get(line);
		if (sylSpot<0 || sylSpot>=pl.size()) {
			System.err.println("Syllable access out of bounds.");
			return null;
		}
		return pl.getSyllables(Stats.FLAT).get(sylSpot);
	}

}

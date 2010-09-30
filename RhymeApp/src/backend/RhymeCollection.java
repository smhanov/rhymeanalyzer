package backend;

import java.util.ArrayList;

// An ArrayList of Rhymes plus PLines, plus methods to find/combine rhymes
public class RhymeCollection {

	public ArrayList<Rhyme>[] collection;
	public ArrayList<PLine> lines;

	public RhymeCollection(ArrayList<PLine> _lines) {
		lines = _lines;
		collection = new ArrayList[lines.size()];
		for (int i=0; i<collection.length; i++) {
			collection[i] = new ArrayList<Rhyme>();
		}
	}

	public RhymeCollection(int numLines) {
		lines = null;
		collection = new ArrayList[numLines];
		for (int i=0; i<collection.length; i++) {
			collection[i] = new ArrayList<Rhyme>();
		}
	}

	public void addRhyme(Rhyme r) {
		int aLine = r.aStart.line;
		if (aLine<0 || aLine>=collection.length) {
			System.err.println("Attempt to add rhyme outside of collection boundaries.");
		} else {
			r.sourceText = lines;
			collection[aLine].add(r);
		}
	}

	public void consolidate() {
		for (int i=0; i<collection.length; i++) {
			ArrayList<Rhyme> cur = collection[i];
			// remove all entirely contained rhymes
			for (int j=0; j<cur.size(); j++) {
				Rhyme rj = cur.get(j);
				for (int k=0; k<cur.size(); k++) {
					if (k!=j) {
						Rhyme rk = cur.get(k);
						boolean contained = true;
						for (int m=0; m<rk.length; m++) {
							if (!rj.contains(rk.aSpot(m), rk.bSpot(m))) {
								contained = false;
								break;
							}
						}
						if (contained) {
							cur.remove(k);
						}
					}
				}
			}

			// concatenate all adjacent/overlapping rhymes
			boolean done = true;
			do {
				done = true;
				for (int j=0; j<cur.size(); j++) {
					Rhyme rj = cur.get(j);
					for (int k=0; k<cur.size(); k++) {
						if (k!=j) {
							Rhyme rk = cur.get(k);
							if (rj.precedes(rk)) {
								Rhyme rC = rj.concat(rk);
								cur.remove(Math.max(k,j));
								cur.remove(Math.min(k,j));
								cur.add(rC);
								done = false;
								break;
							}
						}//if k!=j
					}
					if (!done) {
						break;
					}
				}
			} while (!done);
		}
	}

	public boolean contains(Address a, Address b) {
		int aLine = a.line;
		if (aLine<0 || aLine>=collection.length) {
			return false;
		}
		for (int i=0; i<collection[aLine].size(); i++) {
			Rhyme cur = collection[aLine].get(i);
			if (cur.contains(a, b)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Rhyme r) {
		int aLine = r.aStart.line;
		if (aLine<0 || aLine>=collection.length) {
			return false;
		}
		for (int i=0; i<collection[aLine].size(); i++) {
			Rhyme cur = collection[aLine].get(i);
			if (cur.aStart.equals(r.aStart) && cur.aEnd().equals(r.aEnd()) && cur.bStart.equals(r.bStart) && cur.bEnd().equals(r.bEnd())) {
				return true;
			}
		}
		return false;
	}

	public int missedSyls(Rhyme r) {
		int ret = 0;
		int aLine = r.aStart.line;
		if (aLine<0 || aLine>=collection.length) {
			return r.length;
		}
		for (int i=0; i<collection[aLine].size(); i++) {
			Rhyme cur = collection[aLine].get(i);
			if (cur.aStart.equals(r.aStart) && cur.aEnd().equals(r.aEnd()) && cur.bStart.equals(r.bStart) && cur.bEnd().equals(r.bEnd())) {
				return 0;
			}
		}
		for (int i=0; i<r.length; i++) {
			if (!contains(r.aSpot(i), r.bSpot(i))) {
				ret++;
			}
		}
		return ret;
	}

	public int missedSyls(RhymeCollection rc) {
		int ret = 0;
		for (int i=0; i<rc.collection.length; i++) {
			ArrayList<Rhyme> rhymes = rc.collection[i];
			for (int j=0; j<rhymes.size(); j++) {
				ret += missedSyls(rhymes.get(j));
			}
		}

		return ret;
	}

	public int missedRhymes(RhymeCollection rc) {
		int ret = 0;
		for (int i=0; i<rc.collection.length; i++) {
			ArrayList<Rhyme> rhymes = rc.collection[i];
			for (int j=0; j<rhymes.size(); j++) {
				if (!contains(rhymes.get(j))) {
					ret++;
				}
			}
		}
		return ret;
	}

	public int size() {
		int ret = 0;
		for (int i=0; i<collection.length; i++) {
			ret += collection[i].size();
		}
		return ret;
	}

	public int sylCount() {
		int ret = 0;
		for (int i=0; i<collection.length; i++) {
			ArrayList<Rhyme> rhymes = collection[i];
			for (int j=0; j<rhymes.size(); j++) {
				ret += rhymes.get(j).length;
			}
		}
		return ret;
	}

	public boolean isPerfect(Rhyme r) {
		if (lines == null) {
			return false;
		}

		ArrayList<Syllable> aSyls = lines.get(r.aStart.line).getSyllables(true);
		ArrayList<Syllable> bSyls = lines.get(r.bStart.line).getSyllables(true);
		for (int i=0; i<r.length; i++) {
			Syllable aSyl = aSyls.get(i + r.aStart.syllable);
			Syllable bSyl = bSyls.get(i + r.bStart.syllable);
			if (!aSyl.getVowel().equals(bSyl.getVowel())) return false;
			if (aSyl.getEndC().size() != bSyl.getEndC().size()) return false;
			for (int j=0; j<aSyl.getEndC().size(); j++) {
				if (!aSyl.getEndC().get(j).equals(bSyl.getEndC().get(j))) return false;
			}
		}
		return true;
	}

}


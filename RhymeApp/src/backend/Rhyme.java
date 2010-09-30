package backend;

import java.util.ArrayList;

public class Rhyme {

	public Address aStart;
	public Address bStart;
	public int length;
	
	public ArrayList<PLine> sourceText;
	
	public Rhyme(Address aS, Address bS, int l) {
		aStart = aS;
		bStart = bS;
		length = l;
		
		if (aStart.greaterThan(bStart)) {
			System.err.println("Bad rhyme created - A after B.");
		}
		sourceText = null;
	}
	
	public Rhyme(int aL, int aS1, int bL, int bS1, int l) {
		aStart = new Address(aL, aS1);
		bStart = new Address(bL, bS1);
		length = l;
		
		if (aStart.greaterThan(bStart)) {
			System.err.println("Bad rhyme created - A after B.");
		}
		sourceText = null;
	}
	
	public Rhyme(int aL, int aS1, int aS2, int bL, int bS1, int bS2) {
		aStart = new Address(aL, aS1);
		bStart = new Address(bL, bS1);
		length = aS2 - aS1 + 1;
		
		if (aStart.greaterThan(bStart)) {
			System.err.println("Bad rhyme created - A after B.");
		}
		sourceText = null;
	}
	
	public Rhyme(int aL, int aS, int bL, int bS) {
		aStart = new Address(aL, aS);
		bStart = new Address(bL, bS);
		length = 1;

		if (aStart.greaterThan(bStart)) {
			System.err.println("Bad rhyme created - A after B.");
		}
		sourceText = null;
	}
	
	public boolean contains(Address x, Address y) {
		if (aStart.sameLine(x) && bStart.sameLine(y)) {
			if (!x.lessThan(aStart) && !x.greaterThan(aEnd())) {
				if (!y.lessThan(bStart) && !y.greaterThan(bEnd())) {
					if (x.syllable-aStart.syllable == y.syllable-bStart.syllable) {
						return true;
					}
				}
			}
		}
		return false;
	}

        public boolean compoundIn(Rhyme r2) {
		if (aStart.greaterThan(r2.aEnd()) && bEnd().lessThan(r2.bStart)) {
			return true;
		}
		return false;
	}

	public boolean precedes(Rhyme r2) {
		//check same line
		if (!(this.aStart.sameLine(r2.aStart)&&this.bStart.sameLine(r2.bStart))) {
			return false;
		}
		//check starts in middle or right after
		if (r2.aStart.lessThan(this.aStart) || r2.aStart.greaterThan(this.aStart.addLength(this.length))) {
			return false;
		}
		//check a and b have same offset
		if (r2.aStart.syllable-this.aStart.syllable != r2.bStart.syllable-this.bStart.syllable) {
			return false;
		}
		//check no overlap
		if (!r2.aEnd().lessThan(this.bStart)) {
			return false;
		}
		return true;
	}
	
	//create new rhyme with r2 appended onto the end of this one
	public Rhyme concat(Rhyme r2) {
		if (!this.aStart.sameLine(r2.aStart) || !this.bStart.sameLine(r2.bStart)) {
			System.err.println("Bad rhyme concatenation.");
			return null;
		}
		if (r2.aStart.syllable>this.aEnd().syllable+1 || r2.aEnd().syllable<this.aStart.syllable) {
			System.err.println("Bad rhyme concatenation.");
			return null;
		}
		if (r2.bStart.syllable>this.bEnd().syllable+1 || r2.bEnd().syllable<this.bStart.syllable) {
			System.err.println("Bad rhyme concatenation.");
			return null;
		}
		
		Address newAStart = (aStart.lessThan(r2.aStart))?aStart:r2.aStart;
		Address newBStart = (bStart.lessThan(r2.bStart))?bStart:r2.bStart;
		int newLength = r2.aEnd().syllable - newAStart.syllable + 1;
		Rhyme ret = new Rhyme(newAStart, newBStart, newLength);
		ret.sourceText = this.sourceText;
		
		return ret;
	}
	
	public String toString() {
		String ret = "";
		if (sourceText!=null) {
			PLine aLine = sourceText.get(aStart.line);
			int wordSpot = -1;
			int sum = 0;
			while (sum<aStart.syllable+1) {
				wordSpot++;
				sum += aLine.get(wordSpot).numSyls();	
			}
			ret += aLine.get(wordSpot).getPlainWord() + " ";
			while (sum<aEnd().syllable+1) {
				wordSpot++;
				sum += aLine.get(wordSpot).numSyls();
				ret += aLine.get(wordSpot).getPlainWord() + " ";
			}
			ret += "~ ";
			wordSpot = -1;
			sum = 0;
			PLine bLine = sourceText.get(bStart.line);
			while (sum<bStart.syllable+1) {
				wordSpot++;
				sum += bLine.get(wordSpot).numSyls();	
			}
			ret += bLine.get(wordSpot).getPlainWord() + " ";
			while (sum<bEnd().syllable+1) {
				wordSpot++;
				sum += bLine.get(wordSpot).numSyls();
				ret += bLine.get(wordSpot).getPlainWord() + " ";
			}
		} else {
			ret += "Line " + aStart.line + " syllable " + aStart.syllable;
			if (length>1) ret += " to " + (aStart.syllable + length - 1);
			ret += " with Line " + bStart.line + " syllable " + bStart.syllable;
			if (length>1) ret += " to " + (bStart.syllable + length - 1);
		}
		
		return ret;
	}
	
	public String toString(ArrayList<PLine> source) {
		String ret = "";
		if (source!=null) {
			PLine aLine = source.get(aStart.line);
			int sum = 0;
			for (int i=0; i<aLine.size(); i++) {
				PWord pw = aLine.get(i);
				int numSyls = pw.numSyls();
				if (sum<=aEnd().syllable && sum+numSyls>aStart.syllable) {
					ret += pw.getPlainWord() + " ";
				}
				sum += numSyls;
			}
			ret += "~ ";
			sum = 0;
			PLine bLine = source.get(bStart.line);
			for (int i=0; i<bLine.size(); i++) {
				PWord pw = bLine.get(i);
				int numSyls = pw.numSyls();
				if (sum<=bEnd().syllable && sum+numSyls>bStart.syllable) {
					ret += pw.getPlainWord() + " ";
				}
				sum += numSyls;
			}
		} else {
			ret += "Line " + aStart.line + " syllable " + aStart.syllable;
			if (length>1) ret += " to " + (aStart.syllable+length-1);
			ret += " with Line " + bStart.line + " syllable " + bStart.syllable;
			if (length>1) ret += " to " + (bStart.syllable+length-1);
		}
		
		return ret;
	}
	
	public Address aSpot(int pos) {
		int syl = pos + aStart.syllable;
		return new Address(aStart.line, syl);
	}
	
	public Address bSpot(int pos) {
		int syl = pos + bStart.syllable;
		return new Address(bStart.line, syl);
	}
	
	public Address aEnd() {
		return (aStart.addLength(this.length-1));
	}
	
	public Address bEnd() {
		return (bStart.addLength(this.length-1));
	}
	
}

package backend;

import java.util.ArrayList;

public class Syllable {
	private int stress;
	private int wordLength;
	private int wordPos;
	
	private ArrayList<String> startC;
	private String vowel;
	private ArrayList<String> endC;
	
	public Syllable(ArrayList<String> sc, String v, ArrayList<String> ec) {
		procVowel(v);
		startC = sc;
		endC = ec;
		wordLength = 1;
		wordPos = 0;
	}
	
	private void procVowel(String v) {
		if (v.length()>2) {
			stress = Integer.parseInt(v.substring(2));
			vowel = v.substring(0,2);
		} else {
			vowel = v;
			stress = -1;
		}
	}
	
	public void addCons(String c) {
		endC.add(c);
	}
	
	public void remStart() {
		for (int i=startC.size()-1; i>=0; i--) {
			startC.remove(i);
		}
	}
	
	public void setStart(ArrayList<String> sc) {
		startC = sc;
	}
	
	public boolean equals(Syllable other) {
		if (!this.vowel.equals(other.vowel)) return false;
		if (this.stress != other.stress) return false;
		if (this.startC.size() != other.startC.size()) return false;
		if (this.endC.size() != other.endC.size()) return false;
		for (int i=0; i<this.startC.size(); i++) {
			if (!this.startC.get(i).equals(other.startC.get(i))) return false;
		}
		for (int i=0; i<this.endC.size(); i++) {
			if (!this.endC.get(i).equals(other.endC.get(i))) return false;
		}
		return true;
	}
	
	public int getStress() {
		return stress;
	}
	
	public String getVowel() {
		if (stress<0) {
			return vowel;
		}
		return "" + vowel + stress;
	}

	public ArrayList<String> getStartC() {
		return startC;
	}
	
	public ArrayList<String> getEndC() {
		return endC;
	}
	
	public boolean hasStress() {
		return (stress >= 0);
	}
	
	public void setWord(int l, int p) {
		wordLength = l;
		wordPos = p;
	}
	
	public int getWLength() {
		return wordLength;
	}
	
	public int getWPos() {
		return wordPos;
	}
	
	public String toString() {
		String ret = startC.toString() + " " + vowel + ((hasStress())?stress:"") + endC.toString();
		return ret;
	}
	
}

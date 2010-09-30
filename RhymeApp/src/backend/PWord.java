package backend;

import java.util.ArrayList;

public class PWord {
	
	private String plainWord;
	private String[] phonemes;
	private int numSyls;
	
	public PWord(String w, String p) {
		this.plainWord = w.toUpperCase();
		phonemes = p.split(" ");
		numSyls = -1;
	}
	
	public String getPlainWord() {
		return this.plainWord;
	}
	
	public String toString() {
		String ret = "";
		for (int i = 0; i<phonemes.length; i++) {
			ret = ret + phonemes[i] + ((i<phonemes.length-1)? " " : "");
		}
		return ret;
	}
	
	public String[] getPhonemes() {
		return phonemes; 
	}
	
	public int numSyls() {
		if (this.numSyls<0) {
			getSyllables();
		}
		return this.numSyls;
	}
	
	public ArrayList<Syllable> getSyllables() {
		ArrayList<Syllable> ret = new ArrayList<Syllable>();
		ArrayList<String> middleConsonants = new ArrayList<String>();
		
		for (int i=0; i<phonemes.length; i++) {
			if (Stats.isConsonant(phonemes[i])) {
				middleConsonants.add(phonemes[i]);
			} else if (Stats.isVowel(phonemes[i])) {
				ret.add(new Syllable(new ArrayList<String>(), phonemes[i], new ArrayList<String>()));
				if (ret.size()==1) {
					ret.get(0).setStart(middleConsonants);
					middleConsonants = new ArrayList<String>();
				} else {	
					int splitNum = (middleConsonants.size()+1)/2;
					//if (middleConsonants.size()%2==1 && ret.get(ret.size()-2).getStress()>=ret.get(ret.size()-2).getStress()) {
					//	splitNum++;
					//}
					//if (middleConsonants.size()%2==0 && middleConsonants.size()>0) {
					//	splitNum++;
					//}
					if (Stats.FLAT) {
						splitNum = middleConsonants.size();
					}
					for (int j=0; j<splitNum; j++) {
						ret.get(ret.size()-2).addCons(middleConsonants.remove(0));
					}
					ret.get(ret.size()-1).setStart(middleConsonants);
					middleConsonants = new ArrayList<String>();
				}
			}
		}
		if (middleConsonants.size()>0 && ret.size()>0) {
			for (int i=0; i<middleConsonants.size(); i++) {
				ret.get(ret.size()-1).addCons(middleConsonants.get(i));
			}
		}
		
		int wLength = ret.size();
		for (int i=0; i<ret.size(); i++) {
			ret.get(i).setWord(wLength, i);
		}
		
		this.numSyls = ret.size();
		return ret;
	}
	
}

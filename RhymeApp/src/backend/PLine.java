package backend;

import java.util.ArrayList;

public class PLine extends ArrayList<PWord> {

	public String toString() {
		String retString = "";
		
		for (int i=0; i<this.size(); i++) {
			retString += this.get(i).toString() + " " + "|" /*Transcriptor.WORD_BREAK*/ + " ";
		}
		
		if (retString.length()>0) {
			retString = retString.substring(0, retString.length()-2) + Transcriptor.LINE_BREAK;
		}
		
		return retString;
	}
	
	public String[] getPhonemes() {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i=0; i<this.size(); i++) {
			PWord pw = this.get(i);
			String[] pwP = pw.getPhonemes();
			for (int j=0; j<pwP.length; j++) {
				temp.add(pwP[j]);
			}
		}
		String[] ret = new String[temp.size()];
		for (int i=0; i<temp.size(); i++) {
			ret[i] = temp.get(i);
		}
		return ret;
	}
	
	public ArrayList<Syllable> getSyllables(boolean flatten) {
		ArrayList<Syllable> ret = new ArrayList<Syllable>();
		for (int i=0; i<this.size(); i++) {
			ArrayList<Syllable> wordSylls = this.get(i).getSyllables();
			for (int j=0; j<wordSylls.size(); j++) {
				if (flatten) {
					Syllable curSyl = wordSylls.get(j);
					if (ret.size()>0 && curSyl.getStartC().size()>0) {
						Syllable lastSyl = ret.get(ret.size()-1);
						for (int k=0; k<curSyl.getStartC().size(); k++) {
							lastSyl.addCons(curSyl.getStartC().get(k));
						}
						curSyl.remStart();
					}
				}
				ret.add(wordSylls.get(j));
			}
		}
		return ret;
	}
	
}
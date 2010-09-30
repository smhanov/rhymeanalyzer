package backend;

import java.util.ArrayList;
import java.io.*;

public class Transcriptor {
	
	public static final String DICTIONARY_FILE = "cmudict.txt";
	public static final String NOT_IN_DICT_FILE = "notInDict.txt";
	public static final char WORD_BREAK = ' ';
	public static final char LINE_BREAK = '/';
	
	private PhoneDict pd;
	private Ruleset rs;
	
	public Transcriptor() throws Exception {
		pd = new PhoneDict(DICTIONARY_FILE);
		rs = new Ruleset();
	}
	
	public PLine transcribe(String line) {//throws Exception {
		PLine retLine = new PLine();
		PWord cur;
		
		line = eatComments(line);
		String[] words = Ruleset.cleanSpaces(line).split("[ -]");
		String w;
		for (int i=0; i<words.length; i++) {
			w = stripPunct(words[i]);
			if (hasChars(w)) {
				cur = pd.getPWord(w);
				boolean notInDict = (cur==null);
				cur = (cur==null)?endsInIn(w):cur;
				cur = (cur==null)?endsInS(w):cur;
				cur = (cur==null)?endsInLL(w):cur;
				if (cur == null) {
					cur = new PWord(words[i], rs.transcribe(w));
					/*BufferedWriter nidL = new BufferedWriter (new FileWriter(NOT_IN_DICT_FILE, true));
					nidL.append(w + "-> " + cur.toString() + ": " + line);
					nidL.newLine();
					nidL.close();*/
				}
				if (notInDict) {
					pd.addPWord(w.toUpperCase(), cur);
				}
				retLine.add(cur);
			}
		}
		
		return retLine;
	}
	
	public ArrayList<PLine> transFile(String filename) throws Exception {
		ArrayList<PLine> ret = new ArrayList<PLine>();
		BufferedReader fi = new BufferedReader(new FileReader(filename));
		String nextLine;
		
		while (fi.ready()) {
			nextLine = fi.readLine();
			ret.add(transcribe(nextLine));
		}
		
		fi.close();
		return ret;
	}
	
	public void writeFile(String fileName, ArrayList<PLine> transc, boolean markers) throws Exception {
		BufferedWriter fo = new BufferedWriter(new FileWriter(fileName));
		for (int i=0; i<transc.size(); i++) {
			if (markers) {
				fo.write(i + ": ");
			}
			fo.write(transc.get(i).toString());
			if (markers) {
				fo.write(" " + transc.get(i).getSyllables(Stats.FLAT).size());
			}
			fo.newLine();
		}
		fo.close();
	}
	
	public void writePlain(String fileName, ArrayList<PLine> transc) throws Exception {
		BufferedWriter fo = new BufferedWriter(new FileWriter(fileName));
		for (int i=0; i<transc.size(); i++) {
			fo.write(i + ": ");
			PLine pl = transc.get(i);
			for (int j=0; j<pl.size(); j++) {
				fo.write(pl.get(j).getPlainWord() + " ");
			}
			fo.write(" " + transc.get(i).getSyllables(Stats.FLAT).size());
			fo.newLine();
		}
		fo.close();
	}
	
	public static String stripPunct(String word) {
		if (word.length()<1) {
			return word;
		}
		int start = 0;
		int end = word.length()-1;
		while ((word.charAt(start)==',' || word.charAt(start)=='.' || word.charAt(start)==';' || word.charAt(start)==':' || word.charAt(start)=='"' || word.charAt(start)=='\'' || word.charAt(start)=='?' || word.charAt(start)=='!' || word.charAt(start)=='*' || word.charAt(start)=='(' || word.charAt(start)=='[') && start<end) {
			start++;
		}
		while ((word.charAt(end)==',' || word.charAt(end)=='.' || word.charAt(end)==';' || word.charAt(end)==':' || word.charAt(end)=='"' || word.charAt(end)=='?' || word.charAt(end)=='\'' || word.charAt(end)=='!' || word.charAt(end)=='*' || word.charAt(end)==')' || word.charAt(end)==']') && end>start) {
			end--;
		}
		return word.substring(start,end+1);
	}
	
	public boolean hasChars(String word) {
		String bWord = word.toUpperCase();
		for (int i=0; i<word.length(); i++) {
			if (bWord.charAt(i)<='Z' && bWord.charAt(i)>='A') return true;
			if (bWord.charAt(i)<='9' && bWord.charAt(i)>='0') return true;
		}
		return false;
	}
	
	public PWord endsInIn(String word) {
		if (word.length()<3) {
			return null;
		}
		if ("IN".equals(word.toUpperCase().substring(word.length()-2))) {
			PWord ing = pd.getPWord(word + "g");
			if (ing!=null) {
				String phones = ing.toString();
				phones = phones.substring(0,phones.length()-2) + "N";
				ing = new PWord(word,phones);
				return ing;
			}
		}
		return null;
	}
	
	public PWord endsInLL(String word) {
		if (word.length()<4) {
			return null;
		}
		if ("'LL".equals(word.toUpperCase().substring(word.length()-3))) {
			PWord root = pd.getPWord(word.substring(0,word.length()-3));
			if (root!=null) {
				String phones = root.toString();
				phones = phones + " " + "AH0" + " " + "L";
				root = new PWord(word, phones);
				return root;
			}
		}
		return null;
	}
	
	public PWord endsInS(String word) {
		if (word.length()<3) {
			return null;
		}
		if ("'S".equals(word.toUpperCase().substring(word.length()-2))) {
			PWord root = pd.getPWord(word.substring(0,word.length()-2));
			if (root!=null) {
				String phones = root.toString();
				phones = phones + " " + "Z";
				root = new PWord(word, phones);
				return root;
			}
		}
		return null;
	}
	
	public String eatComments(String l) {
		if (l.indexOf('[')<0 && l.indexOf('{')<0) return l;
		String ret = "";
		boolean comment = false;
		for (int i=0; i<l.length(); i++ ) {
			char c = l.charAt(i);
			if (c=='[' || c=='{') comment = true;
			if (!comment) ret += c;
			if (c==']' || c=='}') comment = false;
		}
		return ret;
	}
	
	/*public static String[] getWords(String text) {
		String[] ret = Ruleset.cleanSpaces(text).split(" ");
		ArrayList<String> temp = new ArrayList<String>();
		String[] second;
		for (int i=0; i<ret.length; i++) {
			second = ret[i].split("-");
			for (int j=0; j<second.length; j++) {
				temp.add(second[j]);
			}
		}
		ret = new String[temp.size()];
		for (int i=0; i<temp.size(); i++) {
			ret[i] = temp.get(i);
		}
		return ret;
	}*/

	public static void main(String[] args) throws Exception {
		
		long tStart = System.currentTimeMillis();
		Transcriptor t = new Transcriptor();
		System.out.println("Done initialization. Time: " + (System.currentTimeMillis()-tStart));
		PLine pws = t.transcribe("Y'all don't wanna do that!!!");
		System.out.println(pws.toString());
		pws = t.transcribe("Jigga Jay-Z, Biggie Bay-Bee");
		System.out.println(pws.toString());
		System.out.println("Done Strings. Time: " + (System.currentTimeMillis()-tStart));
		ArrayList<PLine> ice = t.transFile("in/BB hellonasty.txt");
		t.writeFile("out/nasty.txt", ice, false);
		System.out.println("Done hello nasty. Time: " + (System.currentTimeMillis()-tStart));
		ArrayList<PLine> sonn = t.transFile("mainIn/sonnets.txt");
		t.writeFile("out/nasty.txt", sonn, false);
		System.out.println("Done sonnets. Time: " + (System.currentTimeMillis()-tStart));
	}
	
}

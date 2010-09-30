package backend;

import java.util.Hashtable;
import java.io.*;

public class PhoneDict {
	
	Hashtable<String, PWord> dict;
	
	public PhoneDict(String dictfile) throws Exception {
		
                InputStream in = this.getClass().getResourceAsStream(dictfile);
                BufferedReader f = new BufferedReader(new InputStreamReader(in));

		dict = new Hashtable<String, PWord>();
		String s = "";
		//PWord last = null;
		PWord add = null;
		String plain;
		String pron;
		int spaceBrk = 0;
		
		while (f.ready()) {
		//for (int i=0;i<50000;i++) {
			s = f.readLine();
			spaceBrk = s.indexOf(" ");
			plain = s.substring(0, spaceBrk);
			pron = s.substring(spaceBrk+2); // two spaces between word and pronunciation
			if (plain.length()>3 && plain.charAt(spaceBrk-1)==')') {
				/* alternate pronunciation - ignore now
				plain = plain.substring(0,spaceBrk-3);
				if (last==null) {
					last = dict.get(plain);
				} else if (!last.getPlainWord().equals(plain)) {
					last = dict.get(plain);
				}
				if (last!=null) {
					if (last.getPlainWord().equals(plain)) {
						last.addPron(pron);
						continue;
					}
				}
				*/
			} else {
				add = new PWord(plain,pron);
				dict.put(plain, add);
				//last = add;
			}
		}
		
		f.close();
		//System.out.println("Done loading dictionary.");
		//System.out.println(last.toString());
		
	}
	
	public String transcribe(String plainWord) {
		PWord t = dict.get(plainWord.toUpperCase());
		if (t!=null) {
			return t.toString();
		}
		return "";
	}
	
	public PWord getPWord(String plainWord) {
		return dict.get(plainWord.toUpperCase());
	}
	
	public void addPWord(String w, PWord pw) {
		dict.put(w, pw);
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader f = null;
		String dictfile = "cmudict.txt";
		Hashtable<String, PWord> dic = new Hashtable<String, PWord>();
		Hashtable<String, PWord> polys = new Hashtable<String, PWord>();
		
		while (f == null) {
			try {
				f = new BufferedReader(new FileReader(dictfile));
			} catch ( IOException e ) {
				System.out.println("Error reading dicionary file: " + e.getMessage());
				System.out.println("Please enter a new filename or q to quit.");
				dictfile = System.in.toString();
				if ("q".equals(dictfile)) {
					Runtime.getRuntime().halt(0);
				}
			}
		}
		
		int numEntries = 0;
		int numWords = 0;
		int numPolyphones = 0;
		
		String s = "";
		
		PWord add = null;
		PWord poly = null;
		
		String plain;
		String pron;
		int spaceBrk = 0;
		
		while (f.ready()) {
			s = f.readLine();
			spaceBrk = s.indexOf(" ");
			plain = s.substring(0, spaceBrk);
			pron = s.substring(spaceBrk+2); // two spaces between word and pronunciation
			
			if (plain.length()>3 && plain.charAt(spaceBrk-1)==')') {
				// alternate pronunciation - ignore now
				plain = plain.substring(0,spaceBrk-3);
				
				poly = polys.get(plain);
				if (poly==null) {
					polys.put(plain, new PWord(plain, pron));
					numPolyphones++;
				}
				//*/
			} else {
				add = dic.get(plain);
				if (add!=null) {
					System.out.println("Duplicate entry!  " + plain);
				} else {
					add = new PWord(plain,pron);
					dic.put(plain, add);
				}
				numWords++;
			}
			
			numEntries++;
		}
		
		f.close();
		System.out.println("Number of entries: " + numEntries);
		System.out.println(dic.size());
		System.out.println("Number of distinct words: " + numWords);
		System.out.println("Number of polyphonic words: " + numPolyphones);
		System.out.println(polys.size());
		
		
	}
	
}

package backend;


public class Rule {

	private String left;
	private String right;
	private String match;
	private String ret;
	
	public Rule(String _left, String _match, String _right, String _ret) {
		this.left = _left;
		this.match = _match;
		this.right = _right;
		this.ret = _ret;
	}
	
	public String getLeft() {
		return this.left;
	}
	
	public String getMatch() {
		return this.match;
	}
	
	public String getRight() {
		return this.right;
	}
	
	public String getPhoneme() {
		return this.ret;
	}
	
	public boolean isValid(String leftC, String middle, String rightC) {
		//check middle
		if (!middle.toUpperCase().equals(this.match.toUpperCase())) {
			return false;
		}
		
		//check left
		/*if (isAlpha(this.left)) || this.left.equals(" ") || this.left.equals("'")) {
			if (!leftC.equals(this.left)) {
				return false;
			}
		} else {*/
		int cIndex = leftC.length()-1;
		for (int i=this.left.length(); i>0; i--) {
			switch (left.charAt(i-1)) {
			case '#': if (!isVowel(leftC.substring(cIndex, cIndex+1))) {return false;} 
				while (isVowel(leftC.substring(cIndex, cIndex+1))) {cIndex--; }
				break;
			case ':': while (isConsonant(leftC.substring(cIndex, cIndex+1))) {cIndex--;}
				break;
			case '^': if (!isConsonant(leftC.substring(cIndex, cIndex+1))) {return false;}
				cIndex--;
				break;
			case '.': if (!isVoiced(leftC.substring(cIndex, cIndex+1))) {return false;}
				cIndex--;
				break;
			case '+': if (!isFront(leftC.substring(cIndex, cIndex+1))) {return false;}
				cIndex--;
				break;
			default: if (left.charAt(i-1)!=leftC.charAt(cIndex)) {return false;}
				cIndex--;
				break;
			}
		}
		
		//check right
		cIndex = 0;
		for (int i=0; i<this.right.length(); i++) {
			switch (right.charAt(i)) {
			case '#': if (!isVowel(rightC.substring(cIndex, cIndex+1))) {return false;} 
				while (isVowel(rightC.substring(cIndex, cIndex+1))) {cIndex++; }
				break;
			case ':': while (isConsonant(rightC.substring(cIndex, cIndex+1))) {cIndex++;}
				break;
			case '^': if (!isConsonant(rightC.substring(cIndex, cIndex+1))) {return false;}
				cIndex++;
				break;
			case '.': if (!isVoiced(rightC.substring(cIndex, cIndex+1))) {return false;}
				cIndex++;
				break;
			case '+': if (!isFront(rightC.substring(cIndex, cIndex+1))) {return false;}
				cIndex++;
				break;
			case '%': if (!hasSuffix(rightC)) {return false;}
				break;
			default: if (right.charAt(i)!=rightC.charAt(cIndex)) {return false;}
				cIndex++;
				break;
			}
		}
		
		return true;
	}
	
	public static boolean isAlpha(String s) {
		if (s.length() != 1) {
			return false;
		}
		//if (s.equals("#") || s.equals(":") || s.equals("^") || s.equals(".") || s.equals("%") || s.equals("+")) {
		//	return false;
		//}
		char c = s.charAt(0);
		if ((c>='A' && c<='Z') || (c>='a' && c<='z')) {
			return true;
		}
		return false;
	}
	
	public static boolean isVowel(String s) {
		String S = s.toUpperCase();
		if (S.equals("A") || S.equals("E") || S.equals("I") || S.equals("O") || S.equals("U")) {
			return true;
		}
		return false;
	}
	
	public static boolean isConsonant(String s) {
		if (isAlpha(s)) {
			if (!isVowel(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isVoiced(String s) {
		String S = s.toUpperCase();
		if (S.equals("B") || S.equals("D") || S.equals("V") || S.equals("G") || S.equals("J")
			 || S.equals("L") || S.equals("M") || S.equals("N") || S.equals("R") || S.equals("W") 
			|| S.equals("Z")) {
			return true;
		}
		return false;
	}
	
	public static boolean isFront(String s) {
		String S = s.toUpperCase();
		if (S.equals("E") || S.equals("I") || S.equals("Y")) {
			return true;
		}
		return false;
	}
	
	public static boolean hasSuffix(String s) {
		String S=s.toUpperCase();
		if (S.charAt(0) == 'I') {
			if (S.length()>2) {
				if ("ING".equals(S.substring(0,3))) {
					return true;
				}
			}
		} else if (S.charAt(0) == 'E') {
			if (S.length()>1) {
				if (S.charAt(1)=='R' || S.charAt(1)=='S' || S.charAt(1)=='D' || S.charAt(1)==' ') {
					return true;
				} else if (S.length()>2) { 
					if ("ELY".equals(S.substring(0,3))) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
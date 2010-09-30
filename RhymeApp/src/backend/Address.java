package backend;

public class Address {

	public int line;
	public int syllable;
	
	public Address(int l, int s) {
		line = l;
		syllable = s;
	}
	
	public Address addLength(int l) {
		return new Address(line, syllable+l);
	}
	
	public boolean equals(Address b) {
		if (b.line == this.line) {
			if (b.syllable == this.syllable) {
				return true;
			}
		}
		return false;
	}
	
	public boolean lessThan(Address b) {
		if (b.line > this.line) {
			return true;
		}
		if (b.line == this.line) {
			if (b.syllable > this.syllable) {
				return true;
			}
		}
		return false;
	}
	
	public boolean greaterThan(Address b) {
		if (this.lessThan(b)) {
			return false;
		}
		if (this.equals(b)) {
			return false;
		}
		return true;
	}
	
	public boolean sameLine(Address b) {
		return (this.line == b.line);
	}
}
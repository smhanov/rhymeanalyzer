package backend;

import java.util.ArrayList;

public class RuleHolder {

	public String startChar;
	private ArrayList<Rule> rules;
	
	RuleHolder(String start) {
		this.startChar = start;
		rules = new ArrayList<Rule>();
	}
	
	public void addRule(String left, String middle, String right, String ret) {
		Rule r = new Rule(left, middle, right, ret);
		rules.add(r);
	}
	
	public Rule getRule(String leftC, String match, String rightC) {
		for (int i=0; i<rules.size(); i++) {
			if (rules.get(i).isValid(leftC.toUpperCase(), match.toUpperCase(), rightC.toUpperCase())) {
				return rules.get(i);
			}
		}
		return null;
	}
	
}

package backend;

import java.util.ArrayList;

// Holds features: names + values
public class Analysis {
	
	private ArrayList<String> features;
	private ArrayList<Double> values;
	
	public Analysis() {
		features = new ArrayList<String>();
		values = new ArrayList<Double>();
	}
	
	public void addStat(String name, double value) {
		for (int i=0; i<features.size(); i++) {
			if (features.get(i).equals(name)) {
				values.set(i, new Double(value));
				return;
			}
		}
		features.add(name);
		values.add(new Double(value));
	}
	
	public int numFeatures() {
		return features.size();
	}
	
	public double getStat(String name) {
		for (int i=0; i<features.size(); i++) {
			if (features.get(i).equals(name)) {
				return values.get(i).doubleValue();
			}
		}
		return -1;
	}
	
	public double getStat(int index) {
		if (index>=0 && index<values.size()) {
			return values.get(index).doubleValue();
		}
		return -1;
	}
	
	public String getStatName(int index) {
		if (index>=0 && index<values.size()) {
			return features.get(index);
		}
		return null;
	}
	
	public boolean hasFeature(String name) {
		for (int i=0; i<features.size(); i++) {
			if (features.get(i).equals(name)) {
				return true;
			}
		}
		return false;
	}

        public String toString() {
            String ret ="";
            for (int i=0; i<features.size(); i++) {
                ret += features.get(i) + ": " + values.get(i) + System.getProperty("line.separator");
            }
            return ret;
        }

}

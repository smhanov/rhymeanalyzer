/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SimpleLogistic;
import weka.core.Instance;
import weka.core.Instances;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;


public class Classification {

	public String[] getClass(Classifier cl, Instances inst) throws Exception {

		String[] ret = new String[inst.numInstances()];
		for (int i=0; i<ret.length; i++) {
			int cIndex = (int)cl.classifyInstance(inst.instance(i));
			ret[i] = inst.classAttribute().value(cIndex);
		}
		return ret;
	}

	public String getClass(Classifier cl, Instance inst) throws Exception {

		int cIndex = (int)cl.classifyInstance(inst);
		return inst.classAttribute().value(cIndex);
                //return ("" + cIndex);
	}

	public Classifier rhymeClassifier(Instances insts) throws Exception {
		SimpleLogistic ret = new SimpleLogistic();
		ret.buildClassifier(insts);
		ret.toString();
		return ret;
	}

	public Classifier rhymeClassifier(Instances insts, String fileName) throws Exception {
		SimpleLogistic ret = new SimpleLogistic();
		ret.buildClassifier(insts);
		weka.core.SerializationHelper.write(fileName, ret);
		return ret;
	}

	public Classifier bowClassifier(Instances insts) throws Exception {
		NaiveBayes ret = new NaiveBayes();
		ret.buildClassifier(insts);
		return ret;
	}

	public Classifier bowClassifier(Instances insts, String fileName) throws Exception {
		NaiveBayes ret = new NaiveBayes();
		ret.buildClassifier(insts);
		weka.core.SerializationHelper.write(fileName, ret);
		return ret;
	}

	public Classifier readClassifier(String fileName) throws Exception {
            InputStream in = this.getClass().getResourceAsStream(fileName);
            return (Classifier) weka.core.SerializationHelper.read(in);

	}

        public Instance getRhymeInstance(ArrayList<PLine> pls, Scoring sc, Detector det) throws Exception {

		RhymeCollection rc = det.getRhymes(pls);
		Analyzer an = new Analyzer("temp", sc);
		an.addRhymes(rc);
		Analysis anI = an.createAnalysis();
		if (anI==null) {
			System.out.println("No rhyme features!");
			return null;
		}

	    //Create instance from rhyme analysis
		double[] vals = new double[anI.numFeatures()+1];
		for (int k=0; k<anI.numFeatures(); k++) {
	    	vals[k+1] = anI.getStat(k);
	    }
		return new Instance(1, vals);

	}

        public Instances readInstances(String fileName) throws Exception {
            InputStream in = this.getClass().getResourceAsStream(fileName);
            BufferedReader fi = new BufferedReader(new InputStreamReader(in));
		Instances ret = new Instances(fi);
		if (ret.attribute("class") != null) {
			ret.setClass(ret.attribute("class"));
		} else {
			ret.setClassIndex(0);		//ret.setClassIndex(ret.numAttributes()-1);
		}
		return ret;
	}


}


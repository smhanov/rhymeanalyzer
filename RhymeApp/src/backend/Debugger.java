/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.unsupervised.attribute.StringToWordVector;
/**
 *
 * @author Hussein
 */
public class Debugger {

    public static void main(String[] args) throws Exception {

        Transcriptor t = new Transcriptor();

        Stats st = new Stats(rhymeapp.mainUI.STATS_FILE);
        Scoring sc = new Scoring(st, Stats.SPLIT);
        Detector det = new Detector(sc);
        Classification classon = new Classification();

        //Instances testI = Tools.getRhymeInstance("renJay.txt", t, sc, det, true);

        Instances train = classon.readInstances("lib/rhymeFeatures25.arff");
        //Classifier cl = classon.rhymeClassifier(train, "rhymeFeatures25.model");
        Classifier cl = classon.readClassifier("lib/rhymeFeatures25.model");

        Instance testI = classon.getRhymeInstance(t.transFile("renJay.txt"), sc, det);
        testI.setDataset(train);
        System.out.println(classon.getClass(cl, testI));
        testI = classon.getRhymeInstance(t.transFile("intergalactic.txt"), sc, det);
        testI.setDataset(train);
        System.out.println(classon.getClass(cl, testI));
        testI = classon.getRhymeInstance(t.transFile("ice.txt"), sc, det);
        testI.setDataset(train);
        System.out.println(classon.getClass(cl, testI));
        testI = classon.getRhymeInstance(t.transFile("Fab ghettofab.txt"), sc, det);
        testI.setDataset(train);
        System.out.println(classon.getClass(cl, testI));

            //ArrayList<PLine> pls = t.transFile("renJay.txt");
        //Instance testI = Tools.getRhymeInstance(pls, sc, det);
        //testI.setDataset(train);

    }
}

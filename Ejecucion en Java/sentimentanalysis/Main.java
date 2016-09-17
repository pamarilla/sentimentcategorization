/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sentimentanalysis;

import algorithms.*;
import wekaevaluation.*;

/**
 *
 * @author usuario
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        SimpleCount sc = new SimpleCount();
//        sc.run();
//        ArffGenerator ag = new ArffGenerator ();
//        ag.generate();
//        NaiveBayes nb = new NaiveBayes();
//        nb.run();
//        BayesClassifier bc = new BayesClassifier();
//        bc.run();
          SVMClassifier svmc = new SVMClassifier();
          svmc.run();
//        MaxEntClassifier mec = new MaxEntClassifier();
//        mec.run();
    }

}

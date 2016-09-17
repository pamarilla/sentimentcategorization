package wekaevaluation;

import weka.core.Instances;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.classifiers.functions.LibSVM;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.Ranker;

/**
 *
 * @author Adolfo
 */
public class SVMClassifier {

    private final String UNBALANCED_TRAIN_FILE = "C:/files/ent_desbal.arff";
    private final String UNBALANCED_EVAL_FILE = "C:/files/test_desbal.arff";
    private final String BALANCED_TRAIN_FILE = "C:/files/ent_bal.arff";
    private final String BALANCED_EVAL_FILE = "C:/files/test_bal.arff";
    private final String OUTPUT_FILE = "C:/files/svm.csv";
    // From 2^MIN to 2^MAX (+1)
    private final int MIN_COEF0 = 0;
    private final int MAX_COEF0 = 13;
    // From 2^MIN to 2^MAX (+1)
    private final int MIN_COST = 0;
    private final int MAX_COST = 13;
    // From MIN to MAX (+1)
    private final int MIN_DEGREE = 0;
    private final int MAX_DEGREE = 4;
    double gamma;
    BufferedReader reader = null;
    BufferedWriter output = null;

    public void run() {
        try {
            output = new BufferedWriter(new FileWriter(OUTPUT_FILE));
            output.write("DISTRIBUTION,TESTED_ON,KERNEL,COEF_0,COST,DEGREE,GAMMA,NUM_INSTANCES,CORRECTION_PCT, " +
                    "PRECISION_POS, RECALL_POS, FSCORE_POS, PRECISION_NEG, RECALL_NEG, FSCORE_NEG, PRECISION_NEU, RECALL_NEU, FSCORE_NEU");
            output.newLine();

            /***** UNBALANCED *****/
            reader = new BufferedReader(new FileReader(UNBALANCED_TRAIN_FILE));
            Instances data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // <editor-fold defaultstate="collapsed" desc="Linear - Training set">
            LibSVM svm = this.setNewClassifier(LibSVM.KERNELTYPE_LINEAR);
            System.out.println("Number of Attributes: " + data.numAttributes());
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, data, "UNBALANCED,TRAINING_SET,LIN,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Linear - Test set">
            reader = new BufferedReader(new FileReader(UNBALANCED_EVAL_FILE));
            Instances test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, test, "UNBALANCED,EVAL_SET,LIN,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Polynomial - Training set">
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_POLYNOMIAL);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){ 
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, data, "UNBALANCED,TRAINING_SET,POL,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>


            // <editor-fold defaultstate="collapsed" desc="Polynomial - Test set">
            reader = new BufferedReader(new FileReader(UNBALANCED_EVAL_FILE));
            test = new Instances(reader); 
            test.setClassIndex(test.numAttributes() - 1);
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_POLYNOMIAL);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, test, "UNBALANCED,EVAL_SET,POL,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>


            // <editor-fold defaultstate="collapsed" desc="Radial Basis - Training set">
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_RBF);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, data, "UNBALANCED,TRAINING_SET,RAD,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Radial Basis - Test set">
            reader = new BufferedReader(new FileReader(UNBALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_RBF);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, test, "UNBALANCED,EVAL_SET,RAD,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            /***** BALANCED *****/
            reader = new BufferedReader(new FileReader(BALANCED_TRAIN_FILE));
            data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // <editor-fold defaultstate="collapsed" desc="Linear - Training set">
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_LINEAR);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("LIN TS ***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, data, "BALANCED,TRAINING_SET,LIN,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Linear - Test set">
            reader = new BufferedReader(new FileReader(BALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("LIN ES ***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, test, "BALANCED,EVAL_SET,LIN,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Polynomial - Training set">
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_POLYNOMIAL);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("POL TS ***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, data, "BALANCED,TRAINING_SET,POL,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Polynomial - Test set">
            reader = new BufferedReader(new FileReader(BALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("POL ES ***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, test, "BALANCED,EVAL_SET,POL,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Radial Basis - Training set">
            svm = this.setNewClassifier(LibSVM.KERNELTYPE_RBF);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("RAD TS ***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, data, "BALANCED,TRAINING_SET,RAD,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Radial Basis - Test set">
            reader = new BufferedReader(new FileReader(BALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            for (int coef0 = MIN_COEF0; coef0 <= MAX_COEF0; coef0++){
                for (int cost = MIN_COST; cost <= MAX_COST; cost++){
                    for (int degree = MIN_DEGREE; degree <= MAX_DEGREE; degree++){
                        gamma = 1 / Double.valueOf(data.numAttributes());
                        while (gamma <= 1){
                            System.out.println("RAD ES ***** " + coef0 + " / " + cost + " / " + degree + " *****");
                            svm.setCoef0(Math.pow(Double.valueOf(2), Double.valueOf(coef0)));
                            svm.setCost(Math.pow(Double.valueOf(2), Double.valueOf(cost)));
                            svm.setDegree(degree);
                            svm.setGamma(gamma);
                            svm.setDebug(false);
                            svm.buildClassifier(data);
                            this.classifyAndSummarize(svm, test, "BALANCED,EVAL_SET,RAD,");
                            gamma = gamma * Double.valueOf(10);
                        }
                    }
                }
            }
            // </editor-fold>

            // Finish
            output.flush();
            output.close();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

    }

    /*
     * @param svm - The LibSVM object reference
     * @param instances - The Instances collection reference
     * @param header - The DISTRIBUION,TESTED_ON,KERNEL_TYPE, values for the output
     */
    private void classifyAndSummarize(LibSVM svm, Instances instances, String header) {
        int corrects = 0;
        int posPred = 0, negPred = 0, neuPred = 0; // Cantidad de predicciones del algoritmo por clase
        int posAct = 0, negAct = 0, neuAct = 0; //Cantidad real por clase
        int posOK = 0, negOK = 0, neuOK = 0; //Cantidad de aciertos por clase
        double predicted, actual, percent;
        double precisionPos, precisionNeg, precisionNeu;
        double recallPos, recallNeg, recallNeu;
        double fscorePos, fscoreNeg, fscoreNeu;
        double CERO = Double.valueOf("0.0");
        double UNO = Double.valueOf("1.0");
        double DOS = Double.valueOf("2.0");

        try {
            // Classify
            for (int i = 0; i < instances.numInstances(); i++) {
                predicted = svm.classifyInstance(instances.instance(i));
                actual = instances.instance(i).classValue();
                //Cantidad por predicciones del algoritmo
                if(predicted == CERO){
                    posPred++;
                } else if (predicted == UNO) {
                    negPred++;
                } else if (predicted == DOS) {
                    neuPred++;
                }
                //Cantidad por clases reales de las instancias
                if(actual == CERO){
                    posAct++;
                } else if (actual == UNO) {
                    negAct++;
                } else if (actual == DOS) {
                    neuAct++;
                }
                //Calculo de los valores de corrección/exactitud
                if (predicted == actual) {
                    corrects++;
                    if(predicted == CERO){
                        posOK++;
                    } else if (predicted == UNO) {
                        negOK++;
                    } else if (predicted == DOS) {
                        neuOK++;
                    }
                }
            }

            // Summary - Hasta precision
            percent = corrects / Double.valueOf(instances.numInstances());
            percent = percent * Double.valueOf("100.0");
            output.write(header + svm.getCoef0() + ","
                    + svm.getCost() + "," + svm.getDegree() + ","
                    + svm.getGamma() + "," + instances.numInstances() + "," + percent + ",");
            // Summary - Precision, Recall y FScore por clase
            precisionPos = (double)posOK / (double)posPred;
            recallPos = (double)posOK / (double)posAct;
            fscorePos = (2 * (double)precisionPos * (double)recallPos) / ((double)precisionPos + (double)recallPos);
            precisionNeg = (double)negOK / (double)negPred;
            recallNeg = (double)negOK / (double)negAct;
            fscoreNeg = (2 * (double)precisionNeg * (double)recallNeg) / ((double)precisionNeg + (double)recallNeg);
            precisionNeu = (double)neuOK / (double)neuPred;
            recallNeu = (double)neuOK / (double)neuAct;
            fscoreNeu = (2 * (double)precisionNeu * (double)recallNeu) / ((double)precisionNeu + (double)recallNeu);
            output.write(precisionPos + "," + recallPos + "," + fscorePos + ","
                    + precisionNeg + "," + recallNeg + "," + fscoreNeg + ","
                    + precisionNeu + "," + recallNeu + "," + fscoreNeu);
            output.newLine();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    /*
     * @param kernelType - Identifier for kernel type selected
     */
    private LibSVM setNewClassifier(int kernelType) {
        LibSVM svm = new LibSVM();
        weka.core.SelectedTag tagKernel = new weka.core.SelectedTag(kernelType, LibSVM.TAGS_KERNELTYPE);
        svm.setKernelType(tagKernel);
        svm.setCoef0(0.0);
        svm.setCost(1.0);
        svm.setDegree(3);
        svm.setEps(0.0010);
        svm.setGamma(0.0);
        svm.setLoss(0.1);
        svm.setNu(0.5);
        svm.setNormalize(false);
        svm.setProbabilityEstimates(false);
        svm.setDoNotReplaceMissingValues(false);
        svm.setSeed(1);
        svm.setShrinking(true);
        return svm;
    }
}
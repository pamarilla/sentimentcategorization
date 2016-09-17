package wekaevaluation;

import weka.core.Instances;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SimpleLogistic;

/**
 *
 * @author Adolfo
 */
public class MaxEntClassifier {

    private final String UNBALANCED_TRAIN_FILE = "C:/files/ent_desbal.arff";
    private final String UNBALANCED_EVAL_FILE = "C:/files/test_desbal.arff";
    private final String BALANCED_TRAIN_FILE = "C:/files/ent_bal.arff";
    private final String BALANCED_EVAL_FILE = "C:/files/test_bal.arff";
    private final String LOG_FILE = "C:/files/maxent_logistic.csv";
    private final String SIMPLELOG_FILE = "C:/files/maxent_simplelog.csv";
    // From MIN_RIDGE to MAX_RIDGE (x 10)
    private final double MIN_RIDGE = 0.01;
    private final double MAX_RIDGE = 100;
    // From MIN_HEURISTIC_STOP to Number of Attributes
    private final int MIN_HEURISTIC_STOP = 1;
    BufferedReader reader = null;
    BufferedWriter output = null;
    BufferedWriter out = null;

    public void run() {
        try {
            output = new BufferedWriter(new FileWriter(LOG_FILE));
            output.write("DISTRIBUTION,TESTED_ON,MAX_ITS,RIDGE,NUM_INSTANCES,CORRECTION_PCT, " +
                    "PRECISION_POS, RECALL_POS, FSCORE_POS, PRECISION_NEG, RECALL_NEG, FSCORE_NEG, PRECISION_NEU, RECALL_NEU, FSCORE_NEU");
            output.newLine();

            /***** UNBALANCED *****/
            reader = new BufferedReader(new FileReader(UNBALANCED_TRAIN_FILE));
            Instances data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // <editor-fold defaultstate="collapsed" desc="Logistic - Training set">
            Logistic logistic = this.setNewLogClassifier();
            for (double ridge = MIN_RIDGE; ridge <= MAX_RIDGE; ridge = ridge * 10){
                System.out.println("Ridge: " + ridge);
                logistic.setRidge(ridge);
                logistic.buildClassifier(data);
                this.classifyLogistic(logistic, data, "UNBALANCED,TRAINING_SET,");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Logistic - Test set">
            reader = new BufferedReader(new FileReader(UNBALANCED_EVAL_FILE));
            Instances test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            for (double ridge = MIN_RIDGE; ridge <= MAX_RIDGE; ridge = ridge * 10){
                System.out.println("Ridge: " + ridge);
                logistic.setRidge(ridge);
                logistic.buildClassifier(data);
                this.classifyLogistic(logistic, test, "UNBALANCED,EVAL_SET,");
            }
            // </editor-fold>

            /***** BALANCED *****/
            reader = new BufferedReader(new FileReader(BALANCED_TRAIN_FILE));
            data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // <editor-fold defaultstate="collapsed" desc="Logistic - Training set">
            logistic = this.setNewLogClassifier();
            for (double ridge = MIN_RIDGE; ridge <= MAX_RIDGE; ridge = ridge * 10){
                System.out.println("Ridge: " + ridge);
                logistic.setRidge(ridge);
                logistic.buildClassifier(data);
                this.classifyLogistic(logistic, data, "BALANCED,TRAINING_SET,");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Logistic - Test set">
            reader = new BufferedReader(new FileReader(BALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            for (double ridge = MIN_RIDGE; ridge <= MAX_RIDGE; ridge = ridge * 10){
                System.out.println("Ridge: " + ridge);
                logistic.setRidge(ridge);
                logistic.buildClassifier(data);
                this.classifyLogistic(logistic, test, "BALANCED,EVAL_SET,");
            }
            // </editor-fold>

            // Finish
            output.flush();
            output.close();

            /*** SIMPLE LOGISTIC ***/
            out = new BufferedWriter(new FileWriter(SIMPLELOG_FILE));
            out.write("DISTRIBUTION,TESTED_ON,HEURISTIC_STOP,MAX_BOOST,USE_CV,NUM_INSTANCES,CORRECTION_PCT, " +
                    "PRECISION_POS, RECALL_POS, FSCORE_POS, PRECISION_NEG, RECALL_NEG, FSCORE_NEG, PRECISION_NEU, RECALL_NEU, FSCORE_NEU");
            out.newLine();

            /***** UNBALANCED *****/
            reader = new BufferedReader(new FileReader(UNBALANCED_TRAIN_FILE));
            data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // <editor-fold defaultstate="collapsed" desc="SimpleLogistic - Training set">
            SimpleLogistic simpleLog = this.setNewSimpleLogClassifier();
            
            simpleLog.setUseCrossValidation(true); // PRIMER CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, data, "UNBALANCED,TRAINING_SET,");
            }

            simpleLog.setUseCrossValidation(false); // SEGUNDO CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, data, "UNBALANCED,TRAINING_SET,");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="SimpleLogistic - Test set">
            reader = new BufferedReader(new FileReader(UNBALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);

            simpleLog.setUseCrossValidation(true); // PRIMER CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, test, "UNBALANCED,EVAL_SET,");
            }

            simpleLog.setUseCrossValidation(false); // SEGUNDO CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, test, "UNBALANCED,EVAL_SET,");
            }
            // </editor-fold>

            /***** BALANCED *****/
            reader = new BufferedReader(new FileReader(BALANCED_TRAIN_FILE));
            data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // <editor-fold defaultstate="collapsed" desc="SimpleLogistic - Training set">
            simpleLog = this.setNewSimpleLogClassifier();

            simpleLog.setUseCrossValidation(true); // PRIMER CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, data, "BALANCED,TRAINING_SET,");
            }

            simpleLog.setUseCrossValidation(false); // SEGUNDO CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, data, "BALANCED,TRAINING_SET,");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Logistic - Test set">
            reader = new BufferedReader(new FileReader(BALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);

            simpleLog.setUseCrossValidation(true); // PRIMER CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, test, "BALANCED,EVAL_SET,");
            }

            simpleLog.setUseCrossValidation(false); // SEGUNDO CASO
            simpleLog.setMaxBoostingIterations(data.numAttributes());
            for(int i = MIN_HEURISTIC_STOP; i < data.numAttributes(); i = i * 10){
                System.out.println("Heuristic Stop: " + i);
                simpleLog.setHeuristicStop(i);
                simpleLog.buildClassifier(data);
                this.classifySimpleLog(simpleLog, test, "BALANCED,EVAL_SET,");
            }
            // </editor-fold>

            // Finish
            out.flush();
            out.close();

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

    }

    /*
     * @param logistic - The Logistic object reference
     * @param instances - The Instances collection reference
     * @param header - The DISTRIBUION,TESTED_ON, values for the output
     */
    private void classifyLogistic(Logistic logistic, Instances instances, String header) {
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
                predicted = logistic.classifyInstance(instances.instance(i));
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
            output.write(header + logistic.getMaxIts() + ","
                    + logistic.getRidge() + "," + instances.numInstances() + "," + percent + ",");
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
     * @param logistic - The Logistic object reference
     * @param instances - The Instances collection reference
     * @param header - The DISTRIBUION,TESTED_ON, values for the output
     */
    private void classifySimpleLog(SimpleLogistic logistic, Instances instances, String header) {
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
                predicted = logistic.classifyInstance(instances.instance(i));
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
            out.write(header + logistic.getHeuristicStop() + ","
                    + logistic.getMaxBoostingIterations() + ","
                    + logistic.getUseCrossValidation() + ","
                    + instances.numInstances() + "," + percent + ",");
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
            out.write(precisionPos + "," + recallPos + "," + fscorePos + ","
                    + precisionNeg + "," + recallNeg + "," + fscoreNeg + ","
                    + precisionNeu + "," + recallNeu + "," + fscoreNeu);
            out.newLine();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    private Logistic setNewLogClassifier() {
        Logistic logistic = new Logistic();
        logistic.setMaxIts(-1);
        double ridge = new Double("1.0E8");
        logistic.setRidge(ridge);
        return logistic;
    }

    private SimpleLogistic setNewSimpleLogClassifier() {
        SimpleLogistic logistic = new SimpleLogistic();
        logistic.setHeuristicStop(50);
        logistic.setMaxBoostingIterations(500);
        logistic.setNumBoostingIterations(0);
        logistic.setWeightTrimBeta(0.0);
        return logistic;
    }
}

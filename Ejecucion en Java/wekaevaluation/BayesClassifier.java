package wekaevaluation;

import weka.core.Instances;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.classifiers.bayes.NaiveBayes;

/**
 *
 * @author Adolfo
 */
public class BayesClassifier {

    private final String UNBALANCED_TRAIN_FILE = "C:/files/ent_desbal.arff";
    private final String UNBALANCED_EVAL_FILE = "C:/files/test_desbal.arff";
    private final String BALANCED_TRAIN_FILE = "C:/files/ent_bal.arff";
    private final String BALANCED_EVAL_FILE = "C:/files/test_bal.arff";
    private final String OUTPUT_FILE = "C:/files/bayes.csv";

    public void run() {
        try {
            int corrects;
            double predicted, actual, percent;
            BufferedReader reader = null;
            BufferedWriter output = null;
            output = new BufferedWriter(new FileWriter(OUTPUT_FILE));
            output.write("DISTRIBUTION,TESTED_ON,NUM_INSTANCES,CORRECTION_PCT");
            output.newLine();

            /***** UNBALANCED *****/
            reader = new BufferedReader(new FileReader(UNBALANCED_TRAIN_FILE));
            Instances data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // Set classifier
            NaiveBayes nb = new NaiveBayes();
            nb.buildClassifier(data);
            corrects = 0;
            
            // Classify
            for(int i=0; i < data.numInstances(); i++){
                predicted = nb.classifyInstance(data.instance(i));
                actual = data.instance(i).classValue();
                if(predicted == actual) { corrects++; }
            }

            // Summary
            percent = corrects / Double.valueOf(data.numInstances());
            percent = percent * Double.valueOf("100.0");
            output.write("UNBALANCED,TRAINING_SET," + data.numInstances() + "," + percent);
            output.newLine();

            // Evaluate on test set
            reader = new BufferedReader(new FileReader(UNBALANCED_EVAL_FILE));
            Instances test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            corrects = 0;

             // Classify
            for(int i=0; i < test.numInstances(); i++){
                predicted = nb.classifyInstance(test.instance(i));
                actual = test.instance(i).classValue();
                if(predicted == actual) { corrects++; }
            }

            // Summary
            percent = corrects / Double.valueOf(test.numInstances());
            percent = percent * Double.valueOf("100.0");
            output.write("UNBALANCED,TEST_SET," + test.numInstances() + "," + percent);
            output.newLine();

            /***** BALANCED *****/
            reader = new BufferedReader(new FileReader(BALANCED_TRAIN_FILE));
            data = new Instances(reader);
            reader.close();
            data.setClassIndex(data.numAttributes() - 1);

            // Set classifier
            nb = new NaiveBayes();
            nb.buildClassifier(data);
            corrects = 0;

            // Classify
            for(int i=0; i < data.numInstances(); i++){
                predicted = nb.classifyInstance(data.instance(i));
                actual = data.instance(i).classValue();
                if(predicted == actual) { corrects++; }
            }

            // Summary
            percent = corrects / Double.valueOf(data.numInstances());
            percent = percent * Double.valueOf("100.0");
            output.write("BALANCED,TRAINING_SET," + data.numInstances() + "," + percent);
            output.newLine();

            // Evaluate on test set
            reader = new BufferedReader(new FileReader(BALANCED_EVAL_FILE));
            test = new Instances(reader);
            test.setClassIndex(test.numAttributes() - 1);
            corrects = 0;

             // Classify
            for(int i=0; i < test.numInstances(); i++){
                predicted = nb.classifyInstance(test.instance(i));
                actual = test.instance(i).classValue();
                if(predicted == actual) { corrects++; }
            }

            // Summary
            percent = corrects / Double.valueOf(test.numInstances());
            percent = percent * Double.valueOf("100.0");
            output.write("BALANCED,TEST_SET," + test.numInstances() + "," + percent);
            output.newLine();

            // Close
            output.flush();
            output.close();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

    }
}

//            System.out.println("Cantidad de predicciones correctas: " + corrects);
//            System.out.println("Porcentaje de aciertos: " + percent);
//            System.out.println("Predicted: " + nb.classifyInstance(data.instance(1498)));
//            System.out.println("Actual: " + (data.instance(1498).stringValue(data.attribute("polarity"))));
//            System.out.println("Actual: " + (data.instance(1498).classValue()));
//            System.out.println(data.attribute("polarity").value(0));
//            System.out.println(data.attribute("polarity").value(1));
//            System.out.println(data.attribute("polarity").value(2));

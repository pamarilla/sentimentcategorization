package algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.StringTokenizer;
import sentimentanalysis.Preprocess;

/**
 *
 * @author Adolfo
 */
public class ArffGenerator {

    private final String POSITIVE_TRAINING_FILE = "C:/files/training_pos.txt";
    private final String NEGATIVE_TRAINING_FILE = "C:/files/training_neg.txt";
    private final String NEUTRAL_TRAINING_FILE = "C:/files/training_neu.txt";
    private final String STOPWORDS_FILE = "C:/files/stopwords.txt";
    private final String ARFF_FILE = "C:/files/Telecom_Tweets.arff";

    public void generate() {
        BufferedReader entrada = null;
        BufferedWriter salida = null;
        try {
            Preprocess pp = new Preprocess();
            List<String> stopwords = pp.loadList(STOPWORDS_FILE);
            // POSITIVE
            File f = new File(POSITIVE_TRAINING_FILE);
            entrada = new BufferedReader(new FileReader(f));
            salida = new BufferedWriter(new FileWriter(ARFF_FILE));
            createHeader(salida);
            if (f.exists()) {
                String tweet = entrada.readLine();
                do {
                    tweet = pp.preprocessTweet(tweet);
                    tweet = tweet.replaceAll("\\s+", " ");
                    // Esto porque la comilla es un caracter reservado en el archivo arff
                    tweet = tweet.replace("'", "");
                    salida.newLine();
                    ////////////////////////
                    salida.write("'");
                    StringTokenizer st = new StringTokenizer(tweet, " ");
                    while (st.hasMoreTokens()) {
                        String palabra = st.nextToken();
                        if(!stopwords.contains(palabra)){
                            salida.write(palabra);
                            salida.write(" ");
                        }
                    }
                    salida.write(" ',POS");
                    ////////////////////////
                    //salida.write("'" + tweet + " ',POS");
                    tweet = entrada.readLine();
                } while (tweet != null);
            }
            entrada.close();
            // NEGATIVE
            f = new File(NEGATIVE_TRAINING_FILE);
            entrada = new BufferedReader(new FileReader(f));
            if (f.exists()) {
                String tweet = entrada.readLine();
                do {
                    tweet = pp.preprocessTweet(tweet);
                    tweet = tweet.replaceAll("\\s+", " ");
                    tweet = tweet.replace("'", "");
                    salida.newLine();
                    ////////////////////////
                    salida.write("'");
                    StringTokenizer st = new StringTokenizer(tweet, " ");
                    while (st.hasMoreTokens()) {
                        String palabra = st.nextToken();
                        if(!stopwords.contains(palabra)){
                            salida.write(palabra);
                            salida.write(" ");
                        }
                    }
                    salida.write(" ',NEG");
                    ////////////////////////
                    //salida.write("'" + tweet + " ',NEG");
                    tweet = entrada.readLine();
                } while (tweet != null);
            }
            entrada.close();
            // NEUTRAL
            f = new File(NEUTRAL_TRAINING_FILE);
            entrada = new BufferedReader(new FileReader(f));
            if (f.exists()) {
                String tweet = entrada.readLine();
                do {
                    tweet = pp.preprocessTweet(tweet);
                    tweet = tweet.replaceAll("\\s+", " ");
                    tweet = tweet.replace("'", "");
                    salida.newLine();
                    ////////////////////////
                    salida.write("'");
                    StringTokenizer st = new StringTokenizer(tweet, " ");
                    while (st.hasMoreTokens()) {
                        String palabra = st.nextToken();
                        if(!stopwords.contains(palabra)){
                            salida.write(palabra);
                            salida.write(" ");
                        }
                    }
                    salida.write(" ',NEU");
                    ////////////////////////
                    //salida.write("'" + tweet + " ',NEU");
                    tweet = entrada.readLine();
                } while (tweet != null);
            }
            salida.newLine();
            entrada.close();
            salida.flush();
            salida.close();
        } catch (Exception ex) {
            System.out.println("ERROR. Excepcion: " + ex);
        }
    }

    private void createHeader(BufferedWriter salida) {
        try {
            salida.write("% Tuits de entrenamiento para los clasificadores");
            salida.newLine();
            salida.write("% Mismo conjunto de muestra utilizado para el diccionario");
            salida.newLine();
            salida.newLine();
            salida.write("@relation Telecom_Tweets");
            salida.newLine();
            salida.newLine();
            salida.write("@attribute text string");
            salida.newLine();
            salida.write("@attribute polarity {POS,NEG,NEU}");
            salida.newLine();
            salida.newLine();
            salida.write("@data");
            salida.newLine();
        } catch (Exception ex) {
            System.out.println("Error creando cabecera: " + ex.getMessage());
        }
    }

}

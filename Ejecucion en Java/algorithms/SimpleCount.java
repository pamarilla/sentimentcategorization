package algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import sentimentanalysis.Preprocess;

/**
 *
 * @author Adolfo
 */
public class SimpleCount {
    private final String POSITIVE_SET_FILE = "C:/files/positive.txt";
    private final String NEGATIVE_SET_FILE = "C:/files/negative.txt";

    public void run() {
        Preprocess pp = new Preprocess();
        List<String> positiveSet = new LinkedList<String>();
        List<String> negativeSet = new LinkedList<String>();
        List<String> negationSet = new LinkedList<String>();
        positiveSet = pp.loadList(POSITIVE_SET_FILE);
        negativeSet = pp.loadList(NEGATIVE_SET_FILE);
        negationSet = loadNegationList();
        BufferedReader entrada = null;
        int posTotal = 0;
        int negTotal = 0;
        int neuTotal = 0;
        try {
            File f = new File("C:/files/in.txt");
            entrada = new BufferedReader(new FileReader(f));
            BufferedWriter salida = null;
            salida = new BufferedWriter(new FileWriter("C:/files/out.txt"));
            if (f.exists()) {
                String tweet = entrada.readLine();
                System.out.println("ANTES: " + tweet);
                tweet = pp.preprocessTweet(tweet);
                System.out.println("DESPUES: " + tweet);
                do {
                    int posCount = 0;
                    int negCount = 0;
                    StringTokenizer st = new StringTokenizer(tweet, " ");
                    while (st.hasMoreTokens()) {
                        String palabra = st.nextToken();
                        // Caso de negacion - Toma la siguiente palabra
                        if (negationSet.contains(palabra.toLowerCase())) {
                            if (st.hasMoreTokens()) {
                                palabra = st.nextToken();
                            } else {
                                break;
                            }
                            if (positiveSet.contains(palabra.toLowerCase())) {
                                negCount++;
                            } else if (negativeSet.contains(palabra.toLowerCase())) {
                                posCount++;
                            }
                        // Caso contrario - Analiza normalmente
                        } else {
                            if (positiveSet.contains(palabra.toLowerCase())) {
                                posCount++;
                            } else if (negativeSet.contains(palabra.toLowerCase())) {
                                negCount++;
                            }
                        }
                    }
                    // Si el tuit fue negativo, sin contar los emoticones positivos, 
                    // estos se toman como sarcasticos, por tanto no se suman.
                    if (posCount >= negCount && tweet.contains("_emopos_")) {
                        posCount++;
                    }
                    if (posCount > negCount) {
                        salida.write("POS");
                        posTotal++;
                    } else if (negCount > posCount) {
                        salida.write("NEG");
                        negTotal++;
                    } else {
                        salida.write("NEU");
                        neuTotal++;
                    }
                    salida.newLine();
                    tweet = entrada.readLine();
                    System.out.println("ANTES: " + tweet);
                    tweet = pp.preprocessTweet(tweet);
                    System.out.println("DESPUES: " + tweet);
                } while (tweet != null);
            }
            entrada.close();
            salida.flush();
            salida.close();
            System.out.println("--- RESUMEN ---");
            System.out.println("Cantidad de tuits positivos: " + posTotal);
            System.out.println("Cantidad de tuits negativos: " + negTotal);
            System.out.println("Cantidad de tuits neutrales: " + neuTotal);
        } catch (Exception ex) {
            System.out.println("ERROR. Excepcion: " + ex);
        }
    }

    private List<String> loadNegationList() {
        List<String> result = new LinkedList<String>();
        result.add("jamas");
        result.add("nada");
        result.add("no");
        result.add("nunca");
        result.add("tampoco");
        return result;
    }
}

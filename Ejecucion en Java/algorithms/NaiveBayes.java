package algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.lucene.index.TermsEnum;
import sentimentanalysis.CorpusReader;
import sentimentanalysis.Preprocess;

/**
 *
 * @author Adolfo
 */
public class NaiveBayes {

    // Rutas de los archivos de entrada
    private final String POSITIVE_TRAINING_FILE = "C:/files/training_pos.txt";
    private final String NEGATIVE_TRAINING_FILE = "C:/files/training_neg.txt";
    private final String NEUTRAL_TRAINING_FILE = "C:/files/training_neu.txt";
    private final String STOPWORDS_FILE = "C:/files/stopwords.txt";
    private final String INPUT_FILE = "C:/files/in.txt";
    private final String OUTPUT_FILE = "C:/files/out.txt";
    // Cantidad total de palabras de cada categoria
    private float totalPos = 0;
    private float totalNeg = 0;
    private float totalNeu = 0;
    // Conteo de palabras por cada categoria
    private Map<String, Float> wordsPos = new HashMap<String, Float>();
    private Map<String, Float> wordsNeg = new HashMap<String, Float>();
    private Map<String, Float> wordsNeu = new HashMap<String, Float>();

    /*
     * @return Cantidad de ocurrencias de una palabra para una categoria
     * @param word - Palabra a buscar
     * @param map - Conjunto de la categoria en la que se quiere efectuar la busqueda
     */
    private float getWordCatCount(String word, Map<String, Float> map) {
        Float result = map.get(word);
        if (result == null) {
            result = Float.valueOf(0);
        }
        // Sumamos 0.1 por aproximacion de Laplace para evitar el productorio cero.
        result = result + (float)0.001;
        return result;
    }

    private void train() {
        Preprocess pp = new Preprocess();
        CorpusReader cr = new CorpusReader();
        TermsEnum catPos = cr.getTermsFrequency(POSITIVE_TRAINING_FILE);
        TermsEnum catNeg = cr.getTermsFrequency(NEGATIVE_TRAINING_FILE);
        TermsEnum catNeu = cr.getTermsFrequency(NEUTRAL_TRAINING_FILE);
        List<String> stopwords = pp.loadList(STOPWORDS_FILE);

        try {
            // Tuits positivos
            while (catPos.next() != null) {
                String word = catPos.term().utf8ToString();
                if (!stopwords.contains(word)) {
                    wordsPos.put(word, (float)catPos.totalTermFreq());
                    totalPos = totalPos + catPos.totalTermFreq();
                }
            }
            // Tuits negativos
            while (catNeg.next() != null) {
                String word = catNeg.term().utf8ToString();
                if (!stopwords.contains(word)) {
                    wordsNeg.put(word, (float)catNeg.totalTermFreq());
                    totalNeg = totalNeg + catNeg.totalTermFreq();
                }
            }
            // Tuits neutrales
            while (catNeu.next() != null) {
                String word = catNeu.term().utf8ToString();
                if (!stopwords.contains(word)) {
                    wordsNeu.put(word, (float)catNeu.totalTermFreq());
                    totalNeu = totalNeu + catNeu.totalTermFreq();
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    private void classify() {
        float scorePos, scoreNeg, scoreNeu;
        BufferedReader entrada = null;
        BufferedWriter salida = null;
        Preprocess pp = new Preprocess();
        // Calculo P(c) para cada categoria
        float probPos = totalPos / (totalPos + totalNeg + totalNeu);
        float probNeg = totalNeg / (totalPos + totalNeg + totalNeu);
        float probNeu = totalNeu / (totalPos + totalNeg + totalNeu);
        List<String> stopwords = pp.loadList(STOPWORDS_FILE);
        try {
            File f = new File(INPUT_FILE);
            entrada = new BufferedReader(new FileReader(f));
            salida = new BufferedWriter(new FileWriter(OUTPUT_FILE));
            if (f.exists()) {
                String tweet = entrada.readLine();
                do {
                    tweet = pp.preprocessTweet(tweet);
                    StringTokenizer st = new StringTokenizer(tweet, " ");
                    scorePos = scoreNeg = scoreNeu = 1;
                    while (st.hasMoreTokens()) {
                        String palabra = st.nextToken();
                        // P(w|c) por cada palabra
                        if(!stopwords.contains(palabra)){
                            scorePos = scorePos * (getWordCatCount(palabra, wordsPos) / totalPos);
                            scoreNeg = scoreNeg * (getWordCatCount(palabra, wordsNeg) / totalNeg);
                            scoreNeu = scoreNeu * (getWordCatCount(palabra, wordsNeu) / totalNeu);
                        }
                        

                    }
                   // P(c) por el productorio
                    scorePos = scorePos * probPos;
                    scoreNeg = scoreNeg * probNeg;
                    scoreNeu = scoreNeu * probNeu;
                    salida.write(getPolarityFromScores(scorePos, scoreNeg, scoreNeu));
                    salida.newLine();
                    tweet = entrada.readLine();
                } while (tweet != null);
            }
            entrada.close();
            salida.flush();
            salida.close();
        } catch (Exception ex) {
            System.out.println("ERROR. Excepcion: " + ex);
        }
    }

    private String getPolarityFromScores(float scorePos, float scoreNeg, float scoreNeu){
        System.out.println(scorePos + " / " +  scoreNeg + " / " + scoreNeu);
        float sum = scorePos + scoreNeg + scoreNeu;
        //if(scorePos.compareTo(scoreNeg) > 0 && scorePos.compareTo(scoreNeu) > 0){
        if(scorePos > scoreNeg && scorePos > scoreNeu){
            return "POS";
        }
        if(scoreNeg > scorePos && scoreNeg > scoreNeu){
            return "NEG";
        }
        if(scoreNeu > scorePos && scoreNeu > scoreNeg){
            return "NEU";
        }
        return "NEU";
    }


    public void run() {
        train();
        classify();
        System.out.println(wordsPos);
        System.out.println(wordsNeg);
        System.out.println(wordsNeu);
        System.out.println(totalPos);
        System.out.println(totalNeg);
        System.out.println(totalNeu);
    }
}

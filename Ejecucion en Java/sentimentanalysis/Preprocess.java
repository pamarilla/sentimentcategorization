/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sentimentanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Adolfo
 */
public class Preprocess {

    public String preprocessTweet(String tweet){
        if (tweet == null || tweet.isEmpty()) { return tweet; }
        tweet = removeLinks(tweet);
        tweet = replaceRepeats(tweet);
        tweet = removeSymbols(tweet);
        tweet = replaceEmoticons(tweet);
        tweet = removeUsers(tweet);
        tweet = removeAccents(tweet);
        tweet = removeFreqCharacters(tweet);
        tweet = removeNeutralBigrams(tweet);
        tweet = removeNumbers(tweet);
        return tweet;
    }
    
    private String replaceEmoticons (String line){
        // Positives
        line = line.replace(":)", " _EMOPOS_ ");
        line = line.replace(":-)", " _EMOPOS_ ");
        line = line.replace("=)", " _EMOPOS_ ");
        line = line.replace("=-)", " _EMOPOS_ ");
        line = line.replace("(:", " _EMOPOS_ ");
        line = line.replace(";)", " _EMOPOS_ ");
        line = line.replace(";-)", " _EMOPOS_ ");
        line = line.replace(";D", " _EMOPOS_ ");
        line = line.replace(":D", " _EMOPOS_ ");
        line = line.replace(":-D", " _EMOPOS_ ");
        line = line.replace(":*", " _EMOPOS_ ");
        line = line.replace("<3", " _EMOPOS_ ");
        line = line.replace(":')", " _EMOPOS_ ");
        line = line.replace(":'D", " _EMOPOS_ ");
        // Negatives
        line = line.replace(":(", " _EMONEG_ ");
        line = line.replace(":-(", " _EMONEG_ ");
        line = line.replace("=(", " _EMONEG_ ");
        line = line.replace("=-(", " _EMONEG_ ");
        line = line.replace("):", " _EMONEG_ ");
        line = line.replace(")':", " _EMONEG_ ");
        line = line.replace(":'(", " _EMONEG_ ");
        line = line.replace(":'\"(", " _EMONEG_ ");
        line = line.replace("¬¬", " _EMONEG_ ");
        line = line.replace("-.-", " _EMONEG_ ");
        line = line.replace(".l.", " _EMONEG_ ");
        line = line.replace(".i.", " _EMONEG_ ");
        line = line.replace(".I.", " _EMONEG_ ");
        line = line.replace(".|.", " _EMONEG_ ");
        line = line.replace(":/", " _EMONEG_ ");
        line = line.replace(":-/", " _EMONEG_ ");
        line = line.replace(":-|", " _EMONEG_ ");
        line = line.replace(":|", " _EMONEG_ ");
        line = line.replace(":@", " _EMONEG_ ");
        line = line.replace("</3", " _EMONEG_ ");
        return line;
    }
    private String removeUsers (String line){
        String newLine = "";
        StringTokenizer st = new StringTokenizer(line, " ");
        while(st.hasMoreTokens()){
            String word = st.nextToken();
            if (!word.startsWith("@")){
                newLine += word + " ";
            }
        }
        return newLine;
    }
    private String removeLinks (String line){
        String newLine = "";
        StringTokenizer st = new StringTokenizer(line, " ");
        while(st.hasMoreTokens()){
            String word = st.nextToken();
            if (!word.startsWith("http")){
                newLine += word + " ";
            }
        }
        return newLine;
    }
    private String removeAccents(String line){
        line = line.replace("á", "a");
        line = line.replace("à", "a");
        line = line.replace("ä", "a");
        line = line.replace("â", "a");
        line = line.replace("Á", "A");
        line = line.replace("é", "e");
        line = line.replace("è", "e");
        line = line.replace("ë", "e");
        line = line.replace("ê", "e");
        line = line.replace("É", "E");
        line = line.replace("í", "i");
        line = line.replace("ì", "i");
        line = line.replace("ï", "i");
        line = line.replace("î", "i");
        line = line.replace("Í", "I");
        line = line.replace("ó", "o");
        line = line.replace("ò", "o");
        line = line.replace("ö", "o");
        line = line.replace("ô", "o");
        line = line.replace("Ó", "O");
        line = line.replace("ú", "u");
        line = line.replace("ù", "u");
        line = line.replace("ü", "u");
        line = line.replace("û", "u");
        line = line.replace("Ú", "U");
        return line;
    }
    private String replaceRepeats(String line){
        line = line.replace("ll", "_1_");
        line = line.replace("LL", "_2_");
        line = line.replace("cc", "_3_");
        line = line.replace("CC", "_4_");
        line = line.replace("rr", "_5_");
        line = line.replace("RR", "_5_");
        line = line.replaceAll("(.)\\1{1,100}", "$1");
        line = line.replace("_1_", "ll");
        line = line.replace("_2_", "LL");
        line = line.replace("_3_", "cc");
        line = line.replace("_4_", "CC");
        line = line.replace("_5_", "rr");
        line = line.replace("_6_", "RR");
        return line;
    }
    private String removeFreqCharacters(String line){
        line = line.replace("#", " ");
        line = line.replace("@", " ");
        line = line.replace("*", " ");
        line = line.replace("(", " ");
        line = line.replace(")", " ");
        line = line.replace("¿", " ");
        line = line.replace("?", " ");
        line = line.replace("!", " ");
        line = line.replace("¡", " ");
        line = line.replace("{", " ");
        line = line.replace("}", " ");
        line = line.replace("/", " ");
        line = line.replace(".", " ");
        line = line.replace(",", " ");
        line = line.replace("\""," ");
        line = line.replace(":"," ");
        line = line.replace("-"," ");
        line = line.replace("_"," ");
        line = line.replace("="," ");
        line = line.replace("%"," ");
        line = line.replace("+"," ");
        line = line.replace("*"," ");
        line = line.replace(","," ");
        line = line.replace(";"," ");
        line = line.replace("<"," ");
        line = line.replace(">"," ");
        return line;
    }
    public String removeSymbols(String line) {
        line = line.replace("&quot;", "\"");
        line = line.replace("&amp;", "&");
        line = line.replace("&lt;", "<");
        line = line.replace("&gt;", ">");
        line = line.replace("♥", "<3");
        return line;
    }
    public String removeNeutralBigrams(String line){
        line = line.toLowerCase();
        line = line.replace("buen dia", " ");
        line = line.replace("buenos dias", " ");
        line = line.replace("buenas tardes", " ");
        line = line.replace("buenas noches", " ");
        return line;
    }
    private String removeNumbers (String line){
        String newLine = "";
        StringTokenizer st = new StringTokenizer(line, " ");
        while(st.hasMoreTokens()){
            String word = st.nextToken();
            if (!word.matches("[\\d].*")){
                newLine += word + " ";
            }
        }
        return newLine;
    }

    public List<String> loadList(String path) {
        BufferedReader entrada = null;
        List<String> result = new LinkedList<String>();
        try {
            File f = new File(path);
            entrada = new BufferedReader(new FileReader(f));

            if (f.exists()) {
                String palabra = entrada.readLine();
                do {
                    result.add(palabra);
                    palabra = entrada.readLine();
                } while (palabra != null && !palabra.isEmpty());
            }
            entrada.close();
        } catch (Exception ex) {
            System.out.println("Excepcion: " + ex.getMessage());
        }
        return result;
    }
}

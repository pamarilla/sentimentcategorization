/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sentimentanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Adolfo
 */
public class CorpusReader {
    private void createIndex(Analyzer analyzer, Directory index, String filePath) {
        BufferedReader entrada = null;
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
        config.setOpenMode(OpenMode.CREATE);

        try {
            IndexWriter w = new IndexWriter(index, config);
            File f = new File(filePath);
            entrada = new BufferedReader(new FileReader(f));
            Preprocess pp = new Preprocess();
            if (f.exists()) {
                String tweet = "";
                do {
                    tweet = entrada.readLine();
                    tweet = pp.preprocessTweet(tweet);
                    addTweet(w, tweet);
                } while (tweet != null);

            }
            entrada.close();
            w.commit();
            w.close();
        } catch (Exception ex) {
            System.out.println("ERROR. Excepcion: " + ex.getLocalizedMessage());
        }
    }

    private static void addTweet(IndexWriter w, String text) throws IOException {
        if(text == null || text.isEmpty()) return;
        Document doc = new Document();
        FieldType f1type = new FieldType();
        f1type.setIndexed(true);
        f1type.setStored(false);
        f1type.setTokenized(true);
        f1type.setStoreTermVectors(true);
        f1type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        //doc.add(new TextField("text", text, Field.Store.YES));
        Field f1 = new Field("text", text, f1type);
        doc.add(f1);
        w.addDocument(doc);
    }

    public TermsEnum getTermsFrequency(String filePath) {
        Directory dir = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);

        createIndex(analyzer, dir, filePath);

        TermsEnum termEnum = null;

        try {
            DirectoryReader directoryReader = DirectoryReader.open(dir);
            termEnum = MultiFields.getTerms(directoryReader, "text").iterator(null);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        return termEnum;
    }
}

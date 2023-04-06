/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RelevanceFeedback;

import WeightingTerm.TfIdfWeight;
import dataQuery.CranfieldQuery;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import DocumentResult.DocumentResultRochio;

/**
 *
 * @author richa
 */
public class RochioFeedback {

    private IndexSearcher searcher;
    private IndexReader reader;
    private double BETA = 0.5;
 
    private int batasTerm;

    public RochioFeedback(int x, String index_path) throws IOException {
        this.batasTerm = x;
        Path indexPath = Paths.get(index_path);
        Directory fsDir = FSDirectory.open(indexPath);
        this.reader = DirectoryReader.open(fsDir);
    }

    public String getNewQuery(String query, ArrayList<Integer> documentRelevant, String boost, double factor) throws IOException {
        TfIdfWeight weight = new TfIdfWeight(this.batasTerm);
        weight.setNumOfDocs(reader.maxDoc());
        for (int idRelevan : documentRelevant) {
            Terms termVector = reader.getTermVector(idRelevan, "content");
            ArrayList<String> tempListTerm = weight.getAllTermsInDocumentAsString(termVector);
           
            for (int i = 0; i < tempListTerm.size(); i++) {
                Term temp = new Term("content", tempListTerm.get(i));
                weight.addDocDF(tempListTerm.get(i), reader.docFreq(temp));
                PostingsEnum postingEnum = MultiFields.getTermDocsEnum(reader, "content", temp.bytes());
                int doc = PostingsEnum.NO_MORE_DOCS;
                int tf = 0;
                while ((doc = postingEnum.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
                    if (documentRelevant.contains(doc)) {
                        tf += postingEnum.freq();
                    }
                }
               weight.addTF(tempListTerm.get(i), tf);
            }
        }
        weight.assignIDF();
        weight.setTermScore(factor,documentRelevant.size());
        String boostedOldQuery = ""; 
        String[] word = query.split("\\s");
        for (int i = 0; i < word.length; i++) {
            if(!word[i].equals("")){
             word[i] = word[i] + boost; 
             boostedOldQuery += word[i] + " ";
            }
        }
        String newS = boostedOldQuery + " " + weight.getSortedTerm();
        return newS;
    }
    
}
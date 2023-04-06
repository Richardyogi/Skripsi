/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceRochio;

import TestingPerformanceRochio.TfIdfTester;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author richa
 */
public class RochioFeedbackTester {

    private IndexSearcher searcher;
    private IndexReader idxReader;
    private int numOfDocs;

    private int batasTerm;

    public RochioFeedbackTester(int x, IndexReader idxReader) throws IOException {
        this.batasTerm = x;

        this.idxReader = idxReader;
    }

    public String getNewQuery(String query, ArrayList<Integer> documentRelevant, String boost, double factor) throws IOException {
        TfIdfTester weight = new TfIdfTester(this.batasTerm);
        weight.setNumOfDocs(idxReader.maxDoc());
        for (int idRelevan : documentRelevant) {
            Terms termVector = idxReader.getTermVector(idRelevan, "content");
            ArrayList<String> tempListTerm = weight.getAllTermsInDocumentAsString(termVector);

            for (int i = 0; i < tempListTerm.size(); i++) {
                Term temp = new Term("content", tempListTerm.get(i));
                weight.addDocDF(tempListTerm.get(i), idxReader.docFreq(temp));
                PostingsEnum postingEnum = MultiFields.getTermDocsEnum(idxReader, "content", temp.bytes());
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
            if (!word[i].equals("")) {
                word[i] = word[i] + boost;
                boostedOldQuery += word[i] + " ";
            }
        }
        String newS = boostedOldQuery + " " + weight.getSortedTerm();
        return newS;
    }
}

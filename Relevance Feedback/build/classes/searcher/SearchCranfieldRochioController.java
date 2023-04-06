/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searcher;

import DocumentResult.DocumentResultRochio;
import Path.PathDocument;
import RelevanceFeedback.RochioFeedback;
import WeightingTerm.TfIdfWeight;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author richa
 */
public class SearchCranfieldRochioController {

    private ArrayList<DocumentResultRochio> res;
    private SearcherRochio searcher;
    private RochioFeedback rf;

    private static final int TERM_LIMIT = 200;
    private ArrayList<Integer> docRelevant;

    public SearchCranfieldRochioController() throws IOException {
        this.searcher = new SearcherRochio(PathDocument.CranIndexDir);
        this.rf = new RochioFeedback(TERM_LIMIT, PathDocument.CranIndexDir);
    }

    public ArrayList<DocumentResultRochio> searchDocument(String s) throws ParseException, IOException {
        this.res = searcher.search(s);
        return this.res;
    }

    public void getDocRelevant(ObservableList<DocumentResultRochio> resTemp) {
        this.docRelevant = new ArrayList<>();
        for (int i = 0; i < resTemp.size(); i++) {
            this.docRelevant.add(resTemp.get(i).getDocId());
        }
    }

    public ArrayList<DocumentResultRochio> doRochioFeedback(String s) throws IOException, ParseException {
        TfIdfWeight weight = new TfIdfWeight(TERM_LIMIT);
        double BOOST_VALUE = 2;
        String BOOST = "^" + BOOST_VALUE;

        double factor = 0.5;

        String newQuery = this.rf.getNewQuery(s, this.docRelevant, BOOST, factor);

        return this.searchDocument(newQuery);
    }
    
    public void resetWeightTfIdf() throws IOException{
        TfIdfWeight weight = new TfIdfWeight(TERM_LIMIT);
        weight.reset();
    }
}

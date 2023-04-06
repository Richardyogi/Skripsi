/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searcher;

import DocumentResult.DocumentResultProbabilistic;
import Path.PathDocument;
import RelevanceFeedback.ProbabilisticFeedback;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author richa
 */
public class SearchCranfieldProbabilisticController {
    
    private ProbabilisticFeedback pf;
    
    
    public SearchCranfieldProbabilisticController() throws IOException {
        this.pf = new ProbabilisticFeedback(PathDocument.CranIndexDir);
    }
    
    public ArrayList<DocumentResultProbabilistic> doProbabilisticFeedback(String s) throws IOException, ParseException{
        return this.pf.retrieveQuery(s, 0.8);
    }
    
    
}

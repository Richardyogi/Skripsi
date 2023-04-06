/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceRochio;

import Path.PathDocument;
import RelevanceFeedback.RochioFeedback;
import TestingPerformanceProbabilistic.SearcherProbabilisticTester;
import dataQuery.CranfieldQuery;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import query_reader.QueryReaderCranfield;
import query_reader.QueryReaderRelevanceCranfield;

/**
 *
 * @author richa
 */
public class SearchRochioTester {

    private static int TERM_LIMIT;
    private static double BOOST_VALUE;
    private static String BOOST;
    private static double FACTOR;

    private QueryReaderCranfield queryReader;
    private QueryReaderRelevanceCranfield relevanceReader;

    private CranfieldQuery[] cranQuery;

    private Path indexPath;
    private Directory fsDir;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private RochioFeedbackTester rf;
    private QueryParser queryParser;
    private SearcherRochioTester searcher;
    private TfIdfTester weighter;

    public SearchRochioTester(int limit, double boost, double factor) throws IOException {
        TERM_LIMIT = limit;
        BOOST_VALUE = boost;
        BOOST = "^" + BOOST_VALUE;
        FACTOR = factor;

        queryReader = new QueryReaderCranfield();
        relevanceReader = new QueryReaderRelevanceCranfield();
        try {
            cranQuery = queryReader.readQuery();
//            System.out.println(cranQuery.length);
            relevanceReader.readQueryRelevance();
            indexPath = Paths.get(PathDocument.CranIndexDir);
            fsDir = FSDirectory.open(indexPath);
            indexReader = DirectoryReader.open(fsDir);
            indexSearcher = new IndexSearcher(indexReader);
            weighter = new TfIdfTester(TERM_LIMIT);
            rf = new RochioFeedbackTester(TERM_LIMIT, indexReader);
            queryParser = new QueryParser("content", new StandardAnalyzer());
            searcher = new SearcherRochioTester(indexSearcher, queryParser);
            weighter.setNumOfDocs(indexReader.maxDoc());

        } catch (Exception e) {

        }

    }

    public ArrayList<RochioValueTester> getTestSearchResult() throws ParseException, IOException {
        ArrayList<RochioValueTester> tempRes = new ArrayList<>();

        weighter.setNumOfDocs(indexReader.maxDoc());
        for (int i = 0; i < cranQuery.length; i++) {
            relevanceReader.getRelevanceDocument(cranQuery[i]);

            cranQuery[i] = searcher.search(cranQuery[i], false);
            int idQuery = cranQuery[i].getId();
            String percentagePrecision = cranQuery[i].getPrecisionFormat();
            String percentageRecall = cranQuery[i].getRecallFormat();
            RochioValueTester cranVal = new RochioValueTester(idQuery, percentagePrecision, percentageRecall);
            tempRes.add(cranVal);
        }
        return tempRes;
    }

    public ArrayList<RochioValueTester> getTestFeedbackResult() throws IOException, ParseException {
        ArrayList<RochioValueTester> tempRes = new ArrayList<>();
        for (int i = 0; i < cranQuery.length; i++) {
            weighter.reset();
            CranfieldQuery tempQuery = new CranfieldQuery(cranQuery[i].getId(), cranQuery[i].getQuery());
            relevanceReader.getRelevanceDocument(tempQuery);
            tempQuery = searcher.search(tempQuery, false);
            ArrayList<Integer> intersectRelevantIdList;
            intersectRelevantIdList = tempQuery.getRetrievedRelevantDocument();
            for (int j = 0; j < intersectRelevantIdList.size(); j++) {
                intersectRelevantIdList.set(j, intersectRelevantIdList.get(j) - 1);
            }
            String newQuery = rf.getNewQuery(tempQuery.getQuery(), intersectRelevantIdList, BOOST, FACTOR);
            tempQuery.setQuery(newQuery);
            tempQuery.clearList();

            tempQuery = searcher.search(tempQuery, false);
            int idQuery = tempQuery.getId();
            String percentagePrecision = tempQuery.getPrecisionFormat();
            String percentageRecall = tempQuery.getRecallFormat();

            RochioValueTester cranVal = new RochioValueTester(idQuery, percentagePrecision, percentageRecall);
            tempRes.add(cranVal);
        }
        return tempRes;
    }

}

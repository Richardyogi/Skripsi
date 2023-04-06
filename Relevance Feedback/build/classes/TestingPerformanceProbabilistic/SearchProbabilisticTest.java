/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceProbabilistic;

import Path.PathDocument;
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
public class SearchProbabilisticTest {

    private static double ALPHA;
    private static double MU;

    private QueryReaderCranfield queryReader;
    private QueryReaderRelevanceCranfield relevanceReader;

    private CranfieldQuery[] cranQuery;

    private Path indexPath;
    private Directory fsDir;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private ProbabilisticFeedbackTester pf;
    private SearcherProbabilisticTester searcher;

    public SearchProbabilisticTest(double ALPHA, double MU) {
        ALPHA = ALPHA;
        MU = MU;
        queryReader = new QueryReaderCranfield();
        relevanceReader = new QueryReaderRelevanceCranfield();
        try {
            cranQuery = queryReader.readQuery();
            relevanceReader.readQueryRelevance();
            indexPath = Paths.get(PathDocument.CranIndexDir);
            fsDir = FSDirectory.open(indexPath);
            indexReader = DirectoryReader.open(fsDir);
            indexSearcher = new IndexSearcher(indexReader);
            queryParser = new QueryParser("content", new StandardAnalyzer());
            pf = new ProbabilisticFeedbackTester(indexReader, indexSearcher, queryParser, ALPHA, MU);
            searcher = new SearcherProbabilisticTester(indexSearcher, queryParser, indexReader);
            pf.setNumOfDocs(indexReader.maxDoc());
        } catch (Exception e) {

        }
    }

    public ArrayList<ProbabilisticValueTester> getTestSearchResult() throws ParseException, IOException {
        ArrayList<ProbabilisticValueTester> tempRes = new ArrayList<>();
        pf.setNumOfDocs(indexReader.maxDoc());
        for (int i = 0; i < cranQuery.length; i++) {
            relevanceReader.getRelevanceDocument(cranQuery[i]);
            cranQuery[i] = searcher.search(cranQuery[i], false);
            int idQuery = cranQuery[i].getId();
            String percentagePrecision = cranQuery[i].getPrecisionFormat();
            String percentageRecall = cranQuery[i].getRecallFormat();
            ProbabilisticValueTester cranVal = new ProbabilisticValueTester(idQuery, percentagePrecision, percentageRecall);
            tempRes.add(cranVal);
        }
        return tempRes;
    }

    public ArrayList<ProbabilisticValueTester> getTestProbabilisticResult() throws IOException, ParseException {
        ArrayList<ProbabilisticValueTester> tempRes = new ArrayList<>();
        pf.setNumOfDocs(indexReader.maxDoc());
        for (int i = 0; i < cranQuery.length; i++) {
            relevanceReader.getRelevanceDocument(cranQuery[i]);
            cranQuery[i] = pf.retrieveQuery(cranQuery[i]);
            int idQuery = cranQuery[i].getId();
            String percentagePrecision = cranQuery[i].getPrecisionFormatProb();
            String percentageRecall = cranQuery[i].getRecallFormatProb();
            ProbabilisticValueTester cranVal = new ProbabilisticValueTester(idQuery, percentagePrecision, percentageRecall);
            tempRes.add(cranVal);
        }
        return tempRes;
    }
}

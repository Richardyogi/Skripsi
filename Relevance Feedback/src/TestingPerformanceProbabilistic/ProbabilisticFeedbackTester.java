/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceProbabilistic;

import DocumentResult.DocumentResultProbabilistic;
import Path.PathDocument;
import dataQuery.CranfieldQuery;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import searcher.SearcherProbabilistic;

/**
 *
 * @author richa
 */
public class ProbabilisticFeedbackTester {

    private HashMap<Integer, HashMap<String, Integer>> queryResult;
    private HashMap<String, Long> termFreq;

    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private long corpusSize;
    private int numOfDocs;
    private double MU;
    private double alpha;

    public ProbabilisticFeedbackTester(IndexReader reader, IndexSearcher searcher, QueryParser parser, double MU, double alpha) throws IOException {
        this.indexReader = reader;
        this.indexSearcher = searcher;
        this.queryParser = parser;
        this.MU = MU;
        this.alpha = alpha;
    }

    public void setNumOfDocs(int N) {
        this.numOfDocs = N;
    }
   
    public HashMap<String, Double> getTokenRFScore(CranfieldQuery query) throws IOException, ParseException {
        HashMap<String, Double> tokenRFScore = new HashMap<String, Double>();
        Query q = queryParser.parse(QueryParser.escape(query.getQuery()));

        String str = q.toString();
        String tempSTR = str.replaceAll("content:", "");
        String[] tokens = tempSTR.split(" ");
        SearcherProbabilisticTester searcher = new SearcherProbabilisticTester(indexSearcher, queryParser, indexReader);
        
        corpusSize = searcher.getCorpusSize();
        CranfieldQuery cq = searcher.search(query, false);

        queryResult = new HashMap<>();
        termFreq = new HashMap<>();

        for (String token : tokens) {
            long cf = searcher.tokenFreq(token);
            termFreq.put(token, cf);
            if (cf == 0) {
                continue;
            }
            int[][] postingList = searcher.getPostingList(token);
            for (int[] posting : postingList) {
                if (!queryResult.containsKey(posting[0])) {
                    HashMap<String, Integer> ttf = new HashMap<>();
                    ttf.put(token, posting[1]);
                    queryResult.put(posting[0], ttf);
                } else {
                    queryResult.get(posting[0]).put(token, posting[1]);
                }
            }
        }

        int length = 0;
        Map<String, Integer> pseuDoc = new HashMap<>();
        for (Integer documentResult : cq.getRetrievedDocument()) {
            int temp = documentResult-1;
            queryResult.get(temp).forEach((term, tf) -> {
                if (pseuDoc.containsKey(term)) {
                    pseuDoc.put(term, tf + pseuDoc.get(term));
                } else {
                    pseuDoc.put(term, tf);
                }
            });
            length += searcher.docLength(temp);
        }

        final int pseudoLen = length;
        double c1 = pseudoLen / (pseudoLen + MU);
        double c2 = MU / (pseudoLen + MU);

        pseuDoc.forEach((token, tf) -> {
            long cf = termFreq.get(token);
            double p_doc = (double) tf / pseudoLen;
            double p_ref = (double) cf / corpusSize;
            double score = c1 * p_doc + c2 * p_ref;
            tokenRFScore.put(token, score);
        });

        return tokenRFScore;
    }

    public CranfieldQuery retrieveQuery(CranfieldQuery query) throws IOException, ParseException {
       SearcherProbabilisticTester searcher = new SearcherProbabilisticTester(indexSearcher, queryParser, indexReader);
       corpusSize = searcher.getCorpusSize();
        HashMap<String, Double> tokenRFScore = getTokenRFScore(query);
        Query q = queryParser.parse(QueryParser.escape(query.getQuery()));
        String str = q.toString();
        String tempSTR = str.replaceAll("content:", "");
        String[] tokens = tempSTR.split(" ");
        ArrayList<DocScore> listDocScore = new ArrayList<>();

        this.queryResult.forEach((docId, ttf) -> {
            int docLength = 0;
            double score = 1.0;

            try {
                docLength = searcher.docLength(docId);
            } catch (IOException ex) {
            }

            double c1 = docLength / (docLength + MU);
            double c2 = MU / (docLength + MU);
            for (int i = 0; i < tokens.length; i++) {
                double cf = termFreq.get(tokens[i]);

                if (cf == 0) {
                    continue;
                }
                double tf = ttf.getOrDefault(tokens[i], 0);
                double p_doc = tf / docLength;
                double p_ref = cf / corpusSize;

                score *= alpha * (c1 * p_doc + c2 * p_ref) + (1 - alpha) * tokenRFScore.get(tokens[i]);
            }
            DocScore tempDocScore = new DocScore(docId, score);
            listDocScore.add(tempDocScore);
        });

        Collections.sort(listDocScore, new DocScoreComparator());

        for (int count = 0; count < listDocScore.size(); count++) {
            DocScore ds = listDocScore.get(count);
            DocumentResultProbabilistic doc = null;
            try {
                int docId = ds.getId();
                query.addRetrievedDocumentIDProb(docId + 1);
                
                if (!query.getCustom()) {
                    query.intersectRelevantRetrievedProb();
                }
            } catch (Exception e) {
            };
            
        }
        return query;
    }

    class DocScore {

        private int docId;
        private double score;

        DocScore(int docId, double score) {
            this.docId = docId;
            this.score = score;
        }

        public int getId() {
            return this.docId;
        }

        public double getScore() {
            return this.score;
        }
    }

    private class DocScoreComparator implements Comparator<DocScore> {

        @Override
        public int compare(DocScore t, DocScore t1) {
            if (t.score != t1.score) {
                return t.score < t1.score ? 1 : -1;
            } else {
                return 1;
            }
        }

    }
}

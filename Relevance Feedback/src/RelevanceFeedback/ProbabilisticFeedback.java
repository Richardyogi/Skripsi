/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RelevanceFeedback;

import Path.PathDocument;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import DocumentResult.DocumentResultProbabilistic;
import org.apache.lucene.search.CollectionStatistics;
import searcher.SearcherRochio;
import searcher.SearcherProbabilistic;

/**
 *
 * @author richa
 */
public class ProbabilisticFeedback {

    private HashMap<Integer, HashMap<String, Integer>> queryResult;
    private HashMap<String, Long> termFreq;

    private IndexReader indexReader;
    private long corpusSize;
    private CollectionStatistics ics;

    private static double MU = 2000.0;
    SearcherProbabilistic searcher = new SearcherProbabilistic(PathDocument.CranIndexDir);

    public ProbabilisticFeedback(String index_path) throws IOException {
        Path indexPath = Paths.get(index_path);
        Directory fsDir = FSDirectory.open(indexPath);
        this.indexReader = DirectoryReader.open(fsDir);
        this.corpusSize = searcher.getCorpusSize();
    }
    

    public HashMap<String, Double> getTokenRFScore(String query) throws IOException, ParseException {
        HashMap<String, Double> tokenRFScore = new HashMap<String, Double>();

        String[] tokens = query.split(" ");
        ArrayList<DocumentResultProbabilistic> docResult = searcher.search(query);

        queryResult = new HashMap<>();
        termFreq = new HashMap<>();

        for (String token : tokens) {
            long cf = searcher.TokenFreq(token);
            termFreq.put(token, cf);
//            System.out.println(termFreq.get(token));
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
        for (DocumentResultProbabilistic documentResult : docResult) {
            queryResult.get(documentResult.getDocId()).forEach((term, tf) -> {
                if (pseuDoc.containsKey(term)) {
                    pseuDoc.put(term, tf + pseuDoc.get(term));
                } else {
                    pseuDoc.put(term, tf);
                }
            });
            length += searcher.docLength(documentResult.getDocId());
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

    public ArrayList<DocumentResultProbabilistic> retrieveQuery(String query, double alpha) throws IOException, ParseException {

        HashMap<String, Double> tokenRFScore = getTokenRFScore(query);
        ArrayList<DocumentResultProbabilistic> result = new ArrayList<DocumentResultProbabilistic>();

        String[] tokens = query.split("\\s");
//        for (int i = 0; i < tokens.length; i++) {
//            System.out.println(tokens[i]);
//        }
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
//                System.out.println(token);
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
                int id = ds.getId();
                doc = new DocumentResultProbabilistic(id, searcher.getJudulDoc(id), ds.getScore());
            } catch (Exception e) {
            };
            result.add(doc);
        }
        return result;
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

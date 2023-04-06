/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceProbabilistic;

import DocumentResult.DocumentResultProbabilistic;
import dataQuery.CranfieldQuery;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author richa
 */
public class SearcherProbabilisticTester {
    private IndexSearcher searcher;
    private IndexReader reader;
    private QueryParser parser;
    private CollectionStatistics ics;

    public SearcherProbabilisticTester(IndexSearcher indexSearcher, QueryParser queryParser, IndexReader indexReader) throws IOException {
        this.searcher = indexSearcher;
        this.reader = indexReader;
        this.parser = queryParser;
        ics = searcher.collectionStatistics("content");
    }

    public CranfieldQuery search(CranfieldQuery query, boolean printResults) throws ParseException, IOException {
        String s = query.getQuery();
        Query queryProcess = parser.parse(QueryParser.escape(s));
        int count = searcher.count(queryProcess);
        if (count > 0) {
            TopDocs docs = searcher.search(queryProcess, searcher.count(queryProcess));
            ScoreDoc[] hits = docs.scoreDocs;
            int docId;
            for (int i = 0; i < hits.length; i++) {
                docId = hits[i].doc;
                query.addRetrievedDocumentID(docId + 1);
                Document d = searcher.doc(docId);
                query.addDocScoreDocument(hits[i].score);
            }
            query.setCount(count);
            if (!query.getCustom()) {
                query.intersectRelevantRetrieved();
            }
        }
        return query;
    }

    public String getJudulDoc(int docId) throws ParseException, IOException {
        String title = "";
        Document d = searcher.doc(docId);
        title = d.get("Judul");

        return title;
    }

    public int[][] getPostingList(String token) throws IOException {
        Term term = new Term("content", token);
        int df = reader.docFreq(term);

        Query query = new TermQuery(term);
        TopDocs tops = searcher.search(query, df);
        ScoreDoc[] scoreDoc = tops.scoreDocs;
        int[][] posting = new int[df][];
        int idx = 0;
        Terms termVector;
        TermsEnum termsEnum;
        BytesRef text;
        for (ScoreDoc score : scoreDoc) {
            int id = score.doc;
            int freq = 0;
            termVector = reader.getTermVector(id, "content");
            termsEnum = termVector.iterator();
            while (termsEnum.next() != null) {
                text = termsEnum.term();
                if (text.utf8ToString().equals(token)) {
                    freq += (int) termsEnum.totalTermFreq();
                }
            }
            posting[idx] = new int[]{id, freq};
            idx++;
        }
        return posting;
    }

    public long tokenFreq(String token) throws IOException {
        Term term = new Term("content", token);
        long ctf = reader.totalTermFreq(term);
        return ctf;
    }

    public int docLength(int docId) throws IOException {
        int doc_length = 0;
        Terms vector = reader.getTermVector(docId, "content");
        TermsEnum termsEnum = vector.iterator();
        BytesRef text;
        while ((text = termsEnum.next()) != null) {
            doc_length += (int) termsEnum.totalTermFreq();
        }
        return doc_length;
    }
    
    public long getCorpusSize(){
        return ics.sumTotalTermFreq();
    }
}

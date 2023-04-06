/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceRochio;

import Path.PathDocument;
import dataQuery.CranfieldQuery;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import query_reader.QueryReaderCranfield;
import query_reader.QueryReaderRelevanceCranfield;
import DocumentResult.DocumentResultRochio;

/**
 *
 * @author richa
 */
public class SearcherRochioTester {

    private IndexSearcher indexSearcher;
    private QueryParser parser;

    public SearcherRochioTester(IndexSearcher searcher, QueryParser queryParser) {
        this.indexSearcher = searcher;
        this.parser = queryParser;
    }

    public CranfieldQuery search(CranfieldQuery query, boolean printResults) throws ParseException, IOException {
        String s = query.getQuery();
        Query queryProcess = parser.parse(QueryParser.escape(s));
        int count = indexSearcher.count(queryProcess);
        if (count > 0) {
            TopDocs docs = indexSearcher.search(queryProcess, indexSearcher.count(queryProcess));
            ScoreDoc[] hits = docs.scoreDocs;
            int docId;
            for (int i = 0; i < hits.length; i++) {
                docId = hits[i].doc;
                query.addRetrievedDocumentID(docId + 1);
                Document d = indexSearcher.doc(docId);
                query.addDocScoreDocument(hits[i].score);
            }
            query.setCount(count);
            if (!query.getCustom()) {
                query.intersectRelevantRetrieved();

            }
        }
        return query;
    }
}

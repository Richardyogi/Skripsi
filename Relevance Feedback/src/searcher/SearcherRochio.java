/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searcher;

import DocumentResult.DocumentResultRochio;
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
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author richa
 */
public class SearcherRochio {

    private IndexSearcher searcher;
    private IndexReader reader;
    private QueryParser parser;

    public SearcherRochio(String pathIndex) throws IOException {
        Path indexPath = Paths.get(pathIndex);
        this.reader = DirectoryReader.open(FSDirectory.open(indexPath));
        this.searcher = new IndexSearcher(reader);
        ClassicSimilarity sim = new ClassicSimilarity();
        this.parser = new QueryParser("content", new StandardAnalyzer());
        searcher.setSimilarity(sim);
    }

    public ArrayList<DocumentResultRochio> search(String s) throws ParseException, IOException {
        Query query = parser.parse(QueryParser.escape(s));
        int count = searcher.count(query);
        ArrayList<DocumentResultRochio> docCran = new ArrayList<>();
        if (count > 0) {
            TopDocs docs = searcher.search(query, count);
            ScoreDoc[] hits = docs.scoreDocs;
            int docId;
            float score;
            for (int i = 0; i < hits.length; i++) {
                docId = hits[i].doc ;
                score = hits[i].score;
                Document d = searcher.doc(docId);
                String title = d.get("Judul");
                DocumentResultRochio res = new DocumentResultRochio(docId, title, score);
                docCran.add(res);
            }
        }
        return docCran;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searcher;

import DocumentResult.DocumentResultProbabilistic;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
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
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author richa
 */
public class SearcherProbabilistic {

    private IndexSearcher searcher;
    private IndexReader reader;
    private QueryParser parser;
    private CollectionStatistics ics;

    public SearcherProbabilistic(String pathIndex) throws IOException {
        Path indexPath = Paths.get(pathIndex);
        Directory fsDir = FSDirectory.open(indexPath);
        this.reader = DirectoryReader.open(fsDir);
        this.searcher = new IndexSearcher(reader);
        ClassicSimilarity sim = new ClassicSimilarity();
        this.parser = new QueryParser("content", new StandardAnalyzer());
        searcher.setSimilarity(sim);
        ics = searcher.collectionStatistics("content");
    }

    public ArrayList<DocumentResultProbabilistic> search(String s) throws ParseException, IOException {
        Query query = parser.parse(QueryParser.escape(s));
        ArrayList<DocumentResultProbabilistic> docCran = new ArrayList<>();
        int count = searcher.count(query);
        if (count > 0) {
            TopDocs docs = searcher.search(query, count);
            ScoreDoc[] hits = docs.scoreDocs;
            int docId;
            float score;
            for (int i = 0; i < hits.length; i++) {
                docId = hits[i].doc;
                score = hits[i].score;
                Document doc = searcher.doc(docId);
                String title = doc.get("Judul");
                DocumentResultProbabilistic res = new DocumentResultProbabilistic(docId, title, score);
                docCran.add(res);
            }
        }
        return docCran;
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

    public long TokenFreq(String token) throws IOException {
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

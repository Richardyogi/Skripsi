/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import Path.PathDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author richard
 */
public class CranfieldIndexer {

    private static IndexWriter indexWriter;
    
    Path pathIndex;
    CranfieldHandlerDocument handler;
    private double time;
    
    public CranfieldIndexer(String pathDocument) throws IOException{
        handler = new CranfieldHandlerDocument(pathDocument);
        pathIndex = Paths.get(PathDocument.CranIndexDir);
        time = 0;
    }

    public void indexingDocument() throws IOException{
        Directory indexFir = FSDirectory.open(pathIndex);
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setSimilarity(new ClassicSimilarity());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriter = new IndexWriter(indexFir,config);
        for (int i = 0; i < handler.getSize(); i++) {
            long startTime = System.currentTimeMillis();
            Document doc = handler.getDocument(i);
            indexWriter.addDocument(doc);
            long endTime = System.currentTimeMillis();
            this.time += endTime - startTime;
            System.out.println("Done!");
        }
        System.out.println("Doc count = " + handler.getSize());
        indexWriter.close();
    }
    
    public int getCountDocument(){
        return handler.getSize();
    }
    
    public double getSpeed(){
        double speed = 1400/this.time;
        System.out.println(time);
        this.time = 0;
//        System.out.println(speed);
        return speed;
    }
    
}

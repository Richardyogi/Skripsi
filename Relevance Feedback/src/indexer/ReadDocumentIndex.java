/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import Path.PathDocument;
import cranfield_collection.CranfieldDocument;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author richa
 */
public class ReadDocumentIndex {

    public ArrayList<CranfieldDocument> getAllDocument() throws IOException{
        Path indexPath = Paths.get(PathDocument.CranIndexDir);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));
        ArrayList<CranfieldDocument> resultDoc = new ArrayList<CranfieldDocument>();
        for (int i = 0; i < reader.maxDoc(); i++) {
            Document doc = reader.document(i);
            String judulDocument = doc.get("Judul");
            String pengarang = doc.get("Pengarang");
            String bibliography = doc.get("Bibliography");
            String isi = doc.get("content");
            CranfieldDocument cd = new CranfieldDocument(i,judulDocument,pengarang, bibliography, isi);
            resultDoc.add(cd);
        }
        return resultDoc;
    }
    
    
}

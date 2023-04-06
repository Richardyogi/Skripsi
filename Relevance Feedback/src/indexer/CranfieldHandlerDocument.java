/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import cranfield_collection.CranfieldDocument;
import cranfield_collection.CranfieldTextCollection;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;

/**
 *
 * @author richa
 */
public class CranfieldHandlerDocument {
    
    private CranfieldDocument[] cranDoc;
    
    public CranfieldHandlerDocument(String path) throws IOException{
        this.getAllCranDocs(path);
    }

    private CranfieldDocument[] getAllCranDocs(String path) throws IOException {
        CranfieldTextCollection cranCollection = new CranfieldTextCollection();
        this.cranDoc = cranCollection.getDocs(path);
        return cranDoc;
    }
    
    public Document getDocument(int index){
        String judul = cranDoc[index].getJudul();
        String pengarang = cranDoc[index].getPengarang();
        String bibliography = cranDoc[index].getBibliography();
        String isi = cranDoc[index].getIsi();
        
        
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        Field contentField = new Field("content",isi,type);
        Document doc = new Document();
        if((judul != null) && (!judul.equals(""))){
            doc.add(new StringField("Judul", judul, Field.Store.YES));
        }
        if((pengarang != null) && (!pengarang.equals(""))){
            doc.add(new StringField("Pengarang",pengarang,Field.Store.YES));
        }
        if((bibliography != null) && (!bibliography.equals(""))){
            doc.add(new StringField("Bibliography", bibliography, Field.Store.YES));
        }
        if((isi!=null) && (!isi.equals(""))){
            doc.add(contentField);
        }
        return doc;
    }
    
    public int getSize(){
        return cranDoc.length;
    }
}

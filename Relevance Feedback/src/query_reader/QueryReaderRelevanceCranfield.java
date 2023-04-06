/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query_reader;

import Path.PathDocument;
import dataQuery.CranfieldQuery;
import dataQuery.CranfieldRelevanceQuery;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author richa
 */
public class QueryReaderRelevanceCranfield {

    private static CranfieldRelevanceQuery[] cranRel;

    public void readQueryRelevance() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(PathDocument.queryRelevanceCranfield));
        cranRel = new CranfieldRelevanceQuery[1837];
        int count = 0;
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] words = line.split("\\s");
            CranfieldRelevanceQuery r = new CranfieldRelevanceQuery(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            cranRel[count] = r;
            count++;
        } 
    }

    public CranfieldRelevanceQuery[] getCranfieldRelevance() {
        return this.cranRel;
    }

    public void getRelevanceDocument(CranfieldQuery q) {
        if (q.getStart() >= 0) {
            for (int i = 0; i < q.getEnd(); i++) {
                q.addRelevantDocumentId(cranRel[i].getDocId());
            }
        } else {
            for (int i = 0; i < cranRel.length; i++) {
                if (cranRel[i].getQueryId() == q.getId()) {
                    q.setStart(i);
                    break;
                }
            }
            for (int i = q.getStart(); i < cranRel.length; i++) {
                if (cranRel[i].getQueryId() != q.getId()) {
                    q.setEnd(i);
                    break;
                }
                q.addRelevantDocumentId(cranRel[i].getDocId());
            }
        }
    }
    
//    public static void main(String[] args) throws IOException {
//        readQueryRelevance();
//    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query_reader;

import Path.PathDocument;
import dataQuery.CranfieldQuery;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author richa
 */
public class QueryReaderCranfield {
        
    public CranfieldQuery[] readQuery() throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(PathDocument.queryTestCranfield));
        CranfieldQuery[] query = new CranfieldQuery[225];
        String line = "";
        String content = "";
        int count = 0;
        String queryType = "";
        while((line = br.readLine()) != null){
            if(line.startsWith(".I")){
                queryType = "";
                if(count > 0){
                    query[count-1] = new CranfieldQuery(count,content);
                    System.out.println(query[count-1].toString());
                }
                count++;
                content = "";
            }
            else if(line.startsWith(".W")){
                queryType = ".w";
            }
            else{
                if (queryType == ".w"){
                    content += line + " ";
                }
            }
        }
        query[count-1] = new CranfieldQuery(count,content);
//        System.out.println(query[count-1].toString());
        return query;
    }
    
//    public static void main(String[] args) throws IOException {
//        readQuery();
//    }

    
}

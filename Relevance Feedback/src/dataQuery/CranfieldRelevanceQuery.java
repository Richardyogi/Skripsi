/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataQuery;

/**
 *
 * @author richa
 */
public class CranfieldRelevanceQuery {
    private int idQuery;
    private int idDoc;
    
    public CranfieldRelevanceQuery(int idQuery, int idDoc){
        this.idQuery = idQuery;
        this.idDoc = idDoc;
    }
    
    public int getQueryId(){
        return this.idQuery;
    }
    
    public int getDocId(){
        return this.idDoc;
    }
    
    
}

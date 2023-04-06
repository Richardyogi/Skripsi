/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataQuery;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author richa
 */
public class CranfieldQuery {

    private int id;
    private String query;
    private int start;
    private int end;
    private int count;
    private boolean custom;
    private String percentagePrecision;
    private String percentageRecall;
    private String percentagePrecisionProb;
    private String percentageRecallProb;

    private ArrayList<Integer> retrievedDocument;
    private ArrayList<Integer> retrievedDocumentProb;
    private ArrayList<Integer> relevantDocument;
    private ArrayList<Integer> retrievedRelevantDocument;
    private ArrayList<Integer> retrievedRelevantDocumentProb;
    private ArrayList<Float> docScoreDocument;
    
    NumberFormat formatter = new DecimalFormat("#0.000");
    private HashMap<String,Integer[]> scoreTfIdf;

    public CranfieldQuery(int id, String q) {
        this.id = id;
        this.query = q;
        this.start = -1;
        this.end = -1;
        this.retrievedDocument = new ArrayList<>();
        this.retrievedDocumentProb = new ArrayList<>();
        this.relevantDocument = new ArrayList<>();
        this.retrievedRelevantDocument = new ArrayList<>();
        this.retrievedRelevantDocumentProb = new ArrayList<>();
        this.docScoreDocument = new ArrayList<>();
        this.retrievedRelevantDocument = new ArrayList<>();
        this.custom = false;
    }

    public CranfieldQuery(String q) {
        this.query = q;
        this.start = -1;
        this.end = -1;
        this.retrievedDocument = new ArrayList<>();
        this.relevantDocument = new ArrayList<>();
        this.retrievedDocumentProb = new ArrayList<>();
        this.docScoreDocument = new ArrayList<>();
        this.retrievedRelevantDocument = new ArrayList<>();
        this.retrievedRelevantDocumentProb = new ArrayList<>();
        this.custom = true;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuery() {
        return query;
    }
    
    public Boolean getCustom(){
        return custom;
    }

    public double getPrecision() {
        double precision = ((double) this.retrievedRelevantDocument.size()) / this.retrievedDocument.size() * 1.0;
        return precision;
    }
    
    public double getRecall() {
        double recall = ((double) this.retrievedRelevantDocument.size()) / this.relevantDocument.size() * 1.0;
        return recall;
    }
    
    public double getPrecisionProb() {
        double precision = ((double) this.retrievedRelevantDocumentProb.size()) / this.retrievedDocument.size() * 1.0;
        return precision;
    }
    
    public double getRecallProb() {
        double recall = ((double) this.retrievedRelevantDocumentProb.size()) / this.relevantDocument.size() * 1.0;
        return recall;
    }
    
    public String getPrecisionFormat(){
        double temp = this.getPrecision();
        this.percentagePrecision = formatter.format(temp);
        return this.percentagePrecision;
    }
    
    public String getRecallFormat(){
        double temp = this.getRecall();
        this.percentageRecall = formatter.format(temp);
        return this.percentageRecall;
    }
    
    public String getPrecisionFormatProb(){
        double temp = this.getPrecisionProb();
        this.percentagePrecisionProb = formatter.format(temp);
        return this.percentagePrecisionProb;
    }
    
    public String getRecallFormatProb(){
        double temp = this.getRecallProb();
        this.percentageRecallProb = formatter.format(temp);
        return this.percentageRecallProb;
    }

    public void addDocScoreDocument(float score) {
        this.docScoreDocument.add(score);
    }

    public ArrayList<Float> getScoreList() {
        return this.docScoreDocument;
    }

    public float getScoreDocumentRetrieved(int idx){
        int i;
        for (i = 0; i <= retrievedDocument.size(); i++) {
            if(i== retrievedDocument.size()){
                i++;
                break;
            }
            if(retrievedDocument.get(idx) == idx){
                break;
            }
        }
        if(i<docScoreDocument.size()){
            return docScoreDocument.get(i);
        }
        else{
            return 0;
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void addRetrievedDocumentID(int index) {
        this.retrievedDocument.add(index);
    }
    public void addRetrievedDocumentIDProb(int index){
        this.retrievedDocumentProb.add(index);
    }
    public void addRelevantDocumentId(int index) {
        this.relevantDocument.add(index);
    }


    public ArrayList<Integer> getRelevantDocument() {
        return this.relevantDocument;
    }

    public ArrayList<Integer> getRetrievedDocument() {
        return this.retrievedDocument;
    }

    public ArrayList<Integer> getRetrievedDocumentProb() {
        return retrievedDocumentProb;
    }

    public ArrayList<Integer> getRetrievedRelevantDocument() {
        return this.retrievedRelevantDocument;
    }
    
    private ArrayList<Integer> relevantIntersect(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        ArrayList<Integer> list3 = new ArrayList<>();
        for (Integer i : list1) {
            if (list2.contains(i)) {
                list3.add(i);
//                System.out.println(i);
            }
        }
        return list3;
    }
    
    public void intersectRelevantRetrieved(){
        this.retrievedRelevantDocument = this.relevantIntersect(retrievedDocument, relevantDocument);
    }
    
    public void intersectRelevantRetrievedProb(){
        this.retrievedRelevantDocumentProb = this.relevantIntersect(retrievedDocumentProb, relevantDocument);
    }

    public String toString() {
        return this.id + " " + this.query;
    }
    
    public void clearList(){
        this.retrievedDocument.clear();
        this.retrievedRelevantDocument.clear();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingPerformanceRochio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author richa
 */
public class TfIdfTester {
    private HashMap<String, Long> docTFMap;
    private HashMap<String, Integer> docDFMap;
    private HashMap<String, Double> docIdfMap;
    private HashMap<String, Double> termScoreMap;
    private int limitTerm;
    private int maxDoc;
  

    public TfIdfTester(int limitTerm) {
        this.docTFMap = new HashMap<>();
        this.docDFMap = new HashMap<>();
        this.docIdfMap = new HashMap<>();
        this.termScoreMap = new HashMap<>();
        this.limitTerm = limitTerm;
        
    }
    
    public void setNumOfDocs(int N) {
        this.maxDoc = N;
    }
    
    public void reset(){
        this.docTFMap.clear();
        this.docDFMap.clear();
        this.docIdfMap.clear();
        this.termScoreMap.clear();
    }
    
    /**
     * Menyimpan nilai document frequency milik kata s kedalam hashmap
     *
     * @param s
     * @param df
     */
    public void addDocDF(String s, int df) {
        docDFMap.put(s, df);
    }
    
    /**
     * Menyimpan nilai term frequency kata s
     *
     * @param s
     */
    public void addDocTerm(String s) {
        long x = 0;
        docTFMap.put(s, x);
    }
    
    public void addTF(String s, long count) {
          docTFMap.put(s, count);
    }
    
     /**
     * Mengambil semua term dari sebuah dokumen
     *
     * @param terms
     * @return daftar kata dalam arrayList string
     * @throws IOException
     */
    public ArrayList<String> getAllTermsInDocumentAsString(Terms terms) throws IOException {
        ArrayList<String> termList = new ArrayList<>();
        TermsEnum termsEnum = terms.iterator();
        BytesRef b;
        while ((termsEnum.next()) != null) {
            b = termsEnum.term();
            this.addDocTerm(b.utf8ToString());
            termList.add(b.utf8ToString());
        }
        return termList;
    }

    /**
     * Kelas untuk membuat nilai idf dari term yang telah tersimpan
     */
    public void assignIDF() {
        Set<String> keys = docDFMap.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            double df = docDFMap.get(key) * 1.0;
            double idf = Math.log10(maxDoc / df);
            this.docIdfMap.put(key, idf);
        }
    }

    public void setTermScore(double factor, int jumlahDokumenRelevan) {
        Set<String> keys = docTFMap.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            double tempTF = tempTF = docTFMap.get(key) * 1.0;
            double weight = ((tempTF * docIdfMap.get(key)) * factor)/jumlahDokumenRelevan; 
            this.termScoreMap.put(key, weight);
        }

    }
    
    public String getSortedTerm(){
        Set<String> keys = termScoreMap.keySet();
        TermScore[] terms = new TermScore[keys.size()];
        Iterator it = keys.iterator();
        int index = 0;
        while(it.hasNext()){
            String key = it.next().toString();
            double score = termScoreMap.get(key);
            terms[index] = new TermScore(key,score);
            index++;
        }
        Arrays.sort(terms);
        String res = "";
        for (int i = 0; i < limitTerm && i< terms.length; i++) {
            res += terms[i].getTerm() + " ";
        }
        return res;
    }
    
    
    class TermScore implements Comparable{
        
        String term;
        double score;
        
        public TermScore(String t, double s){
            this.term = t;
            this.score = s;
        }

        public String getTerm() {
            return term;
        }

        public double getScore() {
            return score;
        }
        
        
        @Override
        public int compareTo(Object t) {
            TermScore other = (TermScore) t;
            if (this.getScore()> other.getScore()) {
                return -1;
            }
            else if(this.getScore() == other.getScore()){
                return 0;
            }
            else{
                return 1;
            }
        }
        
    }
    
    
}

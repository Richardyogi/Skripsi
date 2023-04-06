/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DocumentResult;

/**
 *
 * @author richa
 */
public class DocumentResultProbabilistic {

    private int docId;
    private String judul;
    private double score;

    public DocumentResultProbabilistic(int docId, String judul, double score) {
        this.docId = docId;
        this.judul = judul;
        this.score = score;
    }

    public String getJudul() {
        return judul;
    }

    public int getDocId() {
        return docId;
    }

    public double getScore() {
        return score;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DocumentResult;

import javafx.scene.control.CheckBox;

/**
 *
 * @author richa
 */
public class DocumentResultRochio {

    private int docId;
    private String judul;
    private float score;
    public CheckBox selectDocument;

    public DocumentResultRochio(int docId, String judul, float score) {
        this.docId = docId;
        this.judul = judul;
        this.score = score;
        this.selectDocument = new CheckBox();
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public int getDocId() {
        return docId;
    }

    public String getJudul() {
        return judul;
    }

    public CheckBox getSelectDocument() {
        return selectDocument;
    }

    public void setSelectDocument(CheckBox selectDocument) {
        this.selectDocument = selectDocument;
    }

    public float getScore() {
        return score;
    }

}

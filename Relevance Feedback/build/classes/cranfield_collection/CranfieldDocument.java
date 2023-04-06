/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cranfield_collection;

/**
 *
 * @author richa
 */
public class CranfieldDocument {
    private int id;
    private String judul;
    private String pengarang;
    private String bibliography;
    private String isi;
    
    public CranfieldDocument(int id, String judul, String pengarang, String bibliography, String isi){
        this.id = id;
        this.judul = judul;
        this.pengarang = pengarang;
        this.bibliography = bibliography;
        this.isi = isi;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public String getIsi() {
        return isi;
    }

    public String getBibliography() {
        return bibliography;
    }
    
    
    
    public String toString(){
        return "Id = " + id + "\nJudul = " + judul + "\nPengarang = " + pengarang + "\nBibliography = " + bibliography +  "\nIsi = " + isi; 
    }
}

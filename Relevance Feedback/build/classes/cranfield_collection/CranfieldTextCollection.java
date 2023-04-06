/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cranfield_collection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author richard
 */
public class CranfieldTextCollection {

    public CranfieldDocument[] getDocs(String path) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        CranfieldDocument[] crandoc = new CranfieldDocument[1400];
        String type = "";
        String line = "";
        String pengarang = "";
        String bibliography = "";
        String judul = "";
        String isi = "";
        int writeType = 0;
        int count = 0;
        while ((line = br.readLine()) != null) {
            if (line.startsWith(".I")) {
                writeType = 0;
                if (count > 0) {
                    crandoc[count - 1] = new CranfieldDocument(count, judul,pengarang, bibliography,isi);
                    System.out.println(crandoc[count - 1].toString());
                }
                count++;
                judul = "";
                pengarang = "";
                bibliography = "";
                isi = "";
            } else if (line.startsWith(".T")) {
                  writeType = 1;
            }  else if (line.startsWith(".A")) {
                  writeType = 2;
            } else if (line.startsWith(".B")) {
                writeType = 3;
            }
            else if (line.startsWith(".W")) {
                  writeType = 4;
            }
            else {
                if (writeType == 1) {
                    judul += line + " ";
                }
                if (writeType == 2) {
                    pengarang += line + " ";
                }
                if (writeType == 3) {
                    bibliography += line + " ";
                }
                if(writeType == 4){
                    isi += line + " ";
                }
            }
        }
        crandoc[count - 1] = new CranfieldDocument(count, judul, pengarang,bibliography, isi);
        return crandoc;

    }


}

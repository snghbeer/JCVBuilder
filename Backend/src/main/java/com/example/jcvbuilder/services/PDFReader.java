package com.example.jcvbuilder.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PDFReader {
    String path;

    public PDFReader(String path){
        this.path = path;
    }

    public String readContent(){
        String text = "";
        try{
            File cv = new File(this.path);
            PDDocument document = PDDocument.load(cv);
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(document);
            document.close();
        }
        catch (FileNotFoundException ex) {
            // Handle FileNotFoundException here
            ex.printStackTrace(); // This is just an example, you can handle exceptions as needed
        } catch (IOException ex) {
            // Handle other IOExceptions here
            ex.printStackTrace(); // This is just an example, you can handle exceptions as needed
        }
        return text;
    }

    public String extractSkills(String content){
        int start = content.indexOf("IT KENNIS");
        int end = content.indexOf("HOBBY’S & STERKTES");
        if (start != -1 && end != -1) {
            String section = content.substring(start, end);
            System.out.println(section);
            return section;
        } else {
            System.out.println("Section not found");
            return "";
        }
    }
}

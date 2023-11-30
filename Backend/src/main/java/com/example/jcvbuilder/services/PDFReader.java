package com.example.jcvbuilder.services;

import com.example.jcvbuilder.models.DTO.Skill;
import com.example.jcvbuilder.models.DTO.SkillCategory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFReader {


    public String readContent(String path){
        String text = "";
        try{
            File cv = new File(path);
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

    public List<SkillCategory> extractSkills(String path){
        String content = this.readContent(path);
        List<SkillCategory> otp = parseSkills(content);
        return otp;
    }

    private static List<SkillCategory> parseSkills(String content) {
        String[] lines = content.split("\n");
        List<SkillCategory> skillCategories = new ArrayList<SkillCategory>();
        int skillIndex = 0;
        boolean processLines = false;

        for (String line : lines) {
            if (line.contains("skills")) {
                processLines = true;
                ++skillIndex;
                SkillCategory skillCategory = new SkillCategory();
                skillCategory.setTitle(line.split(" ")[0]);
                skillCategories.add(skillCategory);

            }
            else if(processLines && !(line.toLowerCase().contains("skill level")) && !line.endsWith("skills")){
                String[] aSkill = line.split(" ");
                String aSkillName = aSkill[0];
                String aSkillLevel = aSkill[1].replaceAll("\\r$", "");
                SkillCategory aSkillCategory = skillCategories.get(skillIndex-1);
                Skill newSkill = new Skill();
                newSkill.setSkill(aSkillName);
                newSkill.setLevel(aSkillLevel);

                aSkillCategory.addSkill(newSkill);
            }
        }

        return skillCategories;
    }
}

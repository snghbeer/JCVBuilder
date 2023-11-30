package com.example.jcvbuilder.models.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SkillCategory {
    @JsonProperty("title")
    public String title;
    @JsonProperty("skills")
    public ArrayList<Skill> skills;

    public SkillCategory(){
        this.skills = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void addSkill(Skill aSkill){
        this.skills.add(aSkill);
    }

    public ArrayList<Skill> getSkills(){
        return this.skills;
    }
}
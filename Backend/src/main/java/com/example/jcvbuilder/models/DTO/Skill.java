package com.example.jcvbuilder.models.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Skill {
    @JsonProperty("skill")
    public String skill;
    @JsonProperty("level")
    public String level;

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

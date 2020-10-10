package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PriorityTemplate {
    private String type; //A,B,C三种
    private Set<Symptom> symptom;//优先包含的症状

    public PriorityTemplate(String type, Set<Symptom> symptom) {
        this.type = type;
        this.symptom = symptom;
    }

    public PriorityTemplate() {
    }


}

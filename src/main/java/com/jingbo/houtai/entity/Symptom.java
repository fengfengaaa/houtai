package com.jingbo.houtai.entity;

import com.jingbo.houtai.constParam.SymptomType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* 症状名称以及级别
* @author liufeng
* @date 2020/8/22 0022 13:41
 */
@Getter
@Setter
public class Symptom {
    private Integer priority;
    private String symptomName;//症状名字
    private Set<Integer> level = new HashSet<>();//症状级别
    private boolean hasLevel = false;//类型 区分是否有级别
    private SymptomType type = SymptomType.NORMAL;
    private Integer lgtype = 0;//0大于 1小于 2或者处于中间
    public Symptom(Integer priority,String symptomName, Set<Integer> level, boolean hasLevel) {
        this.priority = priority;
        this.symptomName = symptomName;
        this.level = level;
        this.hasLevel = hasLevel;
    }

    public Symptom(Integer priority,String symptomName, Set<Integer> level, boolean hasLevel,SymptomType type) {
        this.type = type;
        this.priority = priority;
        this.symptomName = symptomName;
        this.level = level;
        this.hasLevel = hasLevel;
    }

    public Symptom(Integer priority,String symptomName, Set<Integer> level, boolean hasLevel,SymptomType type,Integer lgtype) {
        this.lgtype = lgtype;
        this.type = type;
        this.priority = priority;
        this.symptomName = symptomName;
        this.level = level;
        this.hasLevel = hasLevel;
    }

    public Symptom() {
    }

    public Symptom(Integer priority,String symptomName, boolean hasLevel) {
        this.priority = priority;
        this.symptomName = symptomName;
        this.hasLevel = hasLevel;
    }

    public Symptom(Integer priority,String symptomName) {
        this.priority = priority;
        this.symptomName = symptomName;
    }

    public boolean equals(Symptom symptom) {
        return (this.symptomName.equals(symptom.getSymptomName()));
    }


}

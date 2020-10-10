package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* 材料
* @author liufeng
* @date 2020/8/22 0022 23:16
 */
@Getter
@Setter
public class Material {
    private String materialType;
    private List<String> symptomList;
    private List<Integer> pur = new ArrayList<>();//为了区分用途
    public Material(String materialType, List<String> symptomList) {
        this.materialType = materialType;
        this.symptomList = symptomList;
    }

    public Material(String materialType, List<String> symptomList,List<Integer> pur) {
        this.materialType = materialType;
        this.symptomList = symptomList;
        this.pur = pur;
    }
    public Material() {
    }
}

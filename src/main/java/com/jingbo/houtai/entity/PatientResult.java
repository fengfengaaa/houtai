package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PatientResult {
    private String key;
    private Map value;

    public PatientResult(String key, Map value) {
        this.key = key;
        this.value = value;
    }

    public PatientResult() {
    }
}

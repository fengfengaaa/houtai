package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatientPDFParam {
    private Integer patientId;
    private List<String> imgPaths;
    private Integer pdfType;
}

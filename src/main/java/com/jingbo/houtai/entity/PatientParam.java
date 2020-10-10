package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PatientParam {
    private String patientName;
    private String phoneNum;
    @NotNull
    private Integer pageSize = 10;
    @NotNull
    private Integer pageIndex = 1;
    private Integer offset;
    private String userName;
    private List<String> userNames;
    private List<Integer> makeStatus;
    private String orderBy = "DESC";
    private String sysUserName;
    private Integer makeStatu = -1;
}

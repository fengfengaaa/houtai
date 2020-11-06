package com.jingbo.houtai.service;

import com.jingbo.houtai.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PatientService {

    int addPatient(Patient patient);

    Patient getPatientByid(Integer id);

    PageResult<Patient> getAllPatient(PatientParam patientParam);

    void updatePatient(Patient patient);

    void deletPatient(Patient patient);

    void batchdeletePatient(List<String> patientIds);

    List<Patient> getPatientByName(String name);

    void updatePatientAndFoot(Patient patient);

    void updateInsoleState(Patient patient);

    void updateExpress(Patient patient);

    void updatePatientFilePath(Integer patientId,String path);

    void updatePatientReportPath(Integer patientId,String path);

    void updateSysUserNameByUserName(String UserName,String sysUserName);
}

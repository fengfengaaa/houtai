package com.jingbo.houtai.dao;


import com.jingbo.houtai.entity.Patient;
import com.jingbo.houtai.entity.PatientParam;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatientMapper {

    Patient getPatientByid(Integer id);

    List<Patient> getAllPatient(PatientParam patientParam);

    void addPatient(Patient patient);

    Integer getCountPatient(PatientParam patientParam);

    void updatePatient(Patient patient);

    void deletePatient(Patient patient);

    void batchdeletePatient(List<String> patientIds);

    List<Patient> getPatientByName(String name);

    List<Patient> test();

    void updateInsoleState(Patient patient);

    void updateExpress(Patient patient);

    void updatePatientFilePath(@Param("patientId") Integer patientId, @Param("filePath")String path);

    void updatePatientReportPath(@Param("patientId")Integer patientId, @Param("reportPath")String path);

    void updateSysUserNameByUserName(@Param("userName")String userName, @Param("sysUserName")String sysUserName);
}

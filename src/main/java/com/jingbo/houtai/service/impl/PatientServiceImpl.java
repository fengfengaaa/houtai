package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.PatientMapper;
import com.jingbo.houtai.entity.*;
import com.jingbo.houtai.service.FootService;
import com.jingbo.houtai.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private FootService footServiceImpl;

    @Override
    @Transactional
    public int addPatient(Patient patient) {
        patientMapper.addPatient(patient);
        Integer patientId = patient.getPatientId();
        List<Foot> foots = patient.getFoots();
        foots.forEach(foot -> {
            foot.setPatientId(patientId);
            footServiceImpl.addFoot(foot);
        });
        return patientId;
    }

    @Override
    public Patient getPatientByid(Integer id) {
        return patientMapper.getPatientByid(id);
    }

    @Override
    public PageResult<Patient> getAllPatient(PatientParam patientParam) {
        Integer offset = (patientParam.getPageIndex() - 1) * patientParam.getPageSize();
        patientParam.setOffset(offset);
        List<Patient> allUser = patientMapper.getAllPatient(patientParam);
        Integer countUser = patientMapper.getCountPatient(patientParam);
        PageResult pageResult = new PageResult();
        pageResult.setResults(allUser);
        pageResult.setPageNo(patientParam.getPageIndex());
        pageResult.setPageSize(patientParam.getPageSize());
        pageResult.setTotalRecords(Long.valueOf(countUser));
        return pageResult;
    }

    @Override
    public void updatePatient(Patient patient) {
        patientMapper.updatePatient(patient);
    }

    @Override
    public void deletPatient(Patient patient) {
        patientMapper.deletePatient(patient);
    }

    @Override
    public void batchdeletePatient(List<String> patientIds) {
        patientMapper.batchdeletePatient(patientIds);
    }

    @Override
    public List<Patient> getPatientByName(String name) {
        return patientMapper.getPatientByName(name);
    }

    @Override
    @Transactional
    public void updatePatientAndFoot(Patient patient) {
        patientMapper.updatePatient(patient);
        Integer patientId = patient.getPatientId();
        List<Foot> foots = patient.getFoots();
        foots.forEach(foot -> {
            foot.setPatientId(patientId);
            footServiceImpl.updateFoot(foot);
        });
    }

    @Override
    public void updateInsoleState(Patient patient) {
        patientMapper.updateInsoleState(patient);
    }

    @Override
    public void updateExpress(Patient patient) {
        patientMapper.updateExpress(patient);
    }

    @Override
    public void updatePatientFilePath(Integer patientId, String path) {
        patientMapper.updatePatientFilePath(patientId,path);
    }

    @Override
    public void updatePatientReportPath(Integer patientId, String path) {
        patientMapper.updatePatientReportPath(patientId,path);
    }

    @Override
    public void updateSysUserNameByUserName(String UserName, String sysUserName) {
        patientMapper.updateSysUserNameByUserName(UserName, sysUserName);
    }
}

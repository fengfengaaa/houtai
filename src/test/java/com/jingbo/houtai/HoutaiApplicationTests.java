package com.jingbo.houtai;

import com.google.gson.Gson;
import com.jingbo.houtai.dao.PatientMapper;
import com.jingbo.houtai.dao.RoleMapper;
import com.jingbo.houtai.dao.UserMapper;
import com.jingbo.houtai.entity.*;
import com.jingbo.houtai.service.PatientService;
import com.jingbo.houtai.util.AESUtil;
import com.jingbo.houtai.util.CalculationUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.redisson.api.mapreduce.RMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class HoutaiApplicationTests {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private PatientService patientService;
    @Test
    void contextLoads() {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Gson gson = new Gson();

        for (int i = 100; i < 1000; i++) {
            User user = new User();
            user.setAddress("北京市西城区 " + i + " 号");
            user.setCompanyName("阿里巴巴第 " + i + " 分公司");
            String pass = "pass " + i;
            user.setPassword(AESUtil.encrypt(pass));
            user.setPhoneNum("1882309" + i);
            user.setUserName("曹操" + i);
            mapper.addUser(user);
        }
    }

    @Test
    void selectAllPatient() {
        PatientMapper mapper = sqlSession.getMapper(PatientMapper.class);
        Gson gson = new Gson();
        PatientParam patientParam = new PatientParam();
        patientParam.setOffset(0);
        patientParam.setPageSize(100);
        List<Patient> allPatient = mapper.test();
        String s = gson.toJson(allPatient);
        System.out.println(s);
    }

    @Test
    void selectOnePatient() {
        PatientMapper mapper = sqlSession.getMapper(PatientMapper.class);
        Gson gson = new Gson();
/*
        List<Patient> patientByName = mapper.getPatientByName("侠侣");
        System.out.println("xx:" + gson.toJson(patientByName));

        Patient patientByid = mapper.getPatientByid(2);
        System.out.println("xx11111:" + gson.toJson(patientByid));
*/

        PageResult<Patient> allPatient = patientService.getAllPatient(new PatientParam());
        System.out.println("xx11111:" + gson.toJson(allPatient.getResults()));

       /* Map map = CalculationUtil.CalculationTemplate(patientByName);
        System.out.println("xxbb:" + gson.toJson(map));*/
    }

    @Test
    void selectRole() {
        RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
        Gson gson = new Gson();
        SysRole roleByRoleId = mapper.getRoleByRoleId(1);
        System.out.println("xx11111:" + gson.toJson(roleByRoleId));

    }

    @Test
    void selectUser() {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserParam param = new UserParam();
        param.setOffset(0);
        param.setPageSize(10);
        List<User> allUser = mapper.getAllUser(param);
        Gson gson = new Gson();
        System.out.println("xx11111:" + gson.toJson(allUser));

    }


    @Test
    void selectPatient() {
        Patient patientByid = patientService.getPatientByid(1);
        System.out.println("===>" + patientByid.getUserName());
    }
}

package com.jingbo.houtai.web;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.jingbo.houtai.constParam.AccountTypeEnum;
import com.jingbo.houtai.constParam.ConfigConstant;
import com.jingbo.houtai.entity.*;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.result.ResponseCode;
import com.jingbo.houtai.service.PatientService;
import com.jingbo.houtai.service.SysUserService;
import com.jingbo.houtai.service.UserService;
import com.jingbo.houtai.util.CalculationUtil;
import com.jingbo.houtai.util.FileUtils;
import com.jingbo.houtai.util.PDFUtil;
import com.jingbo.houtai.util.ShiroUtils;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = {"http://localhost:8080"})
@Slf4j
public class PatientController {
    @Autowired
    private PatientService patientServiceImpl;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private SysUserService sysUserServiceImpl;
    
    @Autowired
    private ConfigConstant configConstant;

    @PostMapping("/app/add")
    public JsonResult addPatient(@Valid @RequestBody Patient patient, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getFieldError().getDefaultMessage());
        }
        int patientId;
        try{
  /*          List<Patient> patientByName = patientServiceImpl.getPatientByName(patient.getPatientName());
            for (Patient patientItem : patientByName) {
                if(patientByName != null && (patientItem.getPhoneNum().equals(patient.getPhoneNum()))){ //姓名和电话都相同认为是同一个用户
                    return JsonResult.fail("用户已经存在");
                }
            }*/

            Subject subject = ShiroUtils.getSubject();//获取当前登录用户
            Object principal = subject.getPrincipal();
            if(principal == null){
                return JsonResult.fail("未登录");
            }
            if(principal instanceof User){
                String userName = ((User) principal).getUserName();
                Integer userId = ((User) principal).getUserId();
                patient.setUserName(userName);
                patient.setUserId(userId);
                patient.setCreateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                
                String sysUserName = ((User) principal).getSysUserName();//获取服务商用户
                SysUser sysUser = this.sysUserServiceImpl.getUserByName(sysUserName);
                if(sysUser != null){
                    SysUser sysUserByid = this.sysUserServiceImpl.getSysUserByid(sysUser.getUserIdCreate());
                    if(sysUserByid != null){
                        patient.setSysUserName(sysUserByid.getUserName());
                    }
                }
                patientId = patientServiceImpl.addPatient(patient);
            }else{
                return JsonResult.fail("非法用户");
            }
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("添加患者信息失败，请联系it人员");
        }
        Map<String,Object> result = new HashMap<>();

        String purpose = patient.getPurpose();
        List<PatientResult> templatesList = new ArrayList<>();
        List<PatientResult> calculationsList = new ArrayList<>();
        for (int i = 0; i < purpose.length(); i++) {
            char c = purpose.charAt(i);
            if(!String.valueOf(c).equals("0")){ //表示选择
                String newpurpose = createPurpose(i,purpose.length());
                patient.setPurpose(newpurpose);
                Map data = CalculationUtil.CalculationTemplate(patient);
                Map calculations = CalculationUtil.CalculationMaterial(patient);

             /*   Map<String,Map> templatesmap = new HashMap<>();
                Map<String,Map> calculationsmap = new HashMap<>();*/

                String purposeType = getPurposeType(i);
             /*   templatesmap.put(purposeType,data);
                calculationsmap.put(purposeType,calculations);*/

                templatesList.add(new PatientResult(purposeType,data));
                calculationsList.add(new PatientResult(purposeType,calculations));
            }
        }
        result.put("TEMPLATES",templatesList);
        result.put("CALCULATION",calculationsList);
        result.put("PATIENTID",patientId);
        return JsonResult.success(result);
    }

    public String createPurpose(Integer i,Integer length){
        StringBuilder purpose = new StringBuilder();
        for (int j = 0; j < length; j++) {
            if(j == i){
                purpose.append("1");
            }else{
                purpose.append("0");
            }
        }
        return purpose.toString();
    }
    public String getPurposeType(Integer i){
       switch (i){
           case 0:
               return "Basketball";
           case 1:
               return "Football";
           case 2:
               return "run";
           case 3:
               return "skiing";
           case 4:
               return "golf";
           case 5:
               return "Riding";
           case 6:
               return "IndoorSports";
           case 7:
               return "comfortable";
           case 8:
               return "leatherShoes";
       }
       return "";
    }


    @PostMapping("/app/update")
    public JsonResult APPupdatePatientInfo(@Valid @RequestBody Patient patient,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getFieldError().getDefaultMessage());
        }
        try{
            patient.setUpdateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            patientServiceImpl.updatePatient(patient);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        Map<String,Object> result = new HashMap<>();
        Map data = CalculationUtil.CalculationTemplate(patient);
        Map calculations = CalculationUtil.CalculationMaterial(patient);
        result.put("TEMPLATES",data);
        result.put("CALCULATION",calculations);
        return JsonResult.success(result);
    }

    /**
    * 绑定报告和结果的文件路径到患者
    * @author liufeng
    * @date 2020/8/25 0025 18:45
     */
    @PostMapping("/app/updateFilePath")
    public JsonResult APPupdatePatientFilePath(@RequestParam(name = "reportPath",required = true)String reportPath,@RequestParam(name = "filePath",required = true)String filePath,@RequestParam(name = "patientId",required = true) Integer patientId){
        try{
            Patient patient = patientServiceImpl.getPatientByid(patientId);
            if(patient == null){
                return JsonResult.fail("患者不存在");
            }
            patient.setUpdateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            patient.setReportPath(reportPath);
            patient.setFilePath(filePath);
            patientServiceImpl.updatePatient(patient);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
     * 更新鞋垫状态
     * @author liufeng
     * @date 2020/9/01 0025 18:45
     */
    @PostMapping("/update/insoleState")
    public JsonResult updateInsole(@RequestParam(name = "makeStatus",required = true)Integer makeStatus,@RequestParam(name = "patientId",required = true) Integer patientId){
        try{
            Patient patient = patientServiceImpl.getPatientByid(patientId);
            if(patient == null){
                return JsonResult.fail("患者不存在");
            }
            patient.setUpdateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            patient.setMakeStatus(makeStatus + 1);
            patientServiceImpl.updateInsoleState(patient);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
     * 更新快递状态
     * @author liufeng
     * @date 2020/9/01 0025 18:45
     */
    @PostMapping("/update/express")
    public JsonResult updateExpress(@Valid ExpressParam expressParam,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getFieldError().getDefaultMessage());
        }
        try{
            Patient patient = patientServiceImpl.getPatientByid(expressParam.getPatientId());
            if(patient == null){
                return JsonResult.fail("患者不存在");
            }
            patient.setAddressee(expressParam.getAddressee());
            patient.setAddresseePhone(expressParam.getAddresseePhone());
            patient.setToAddress(expressParam.getToAddress());
            patient.setUpdateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            patient.setMakeStatus(expressParam.getMakeStatus() + 1);
            patient.setExpressNumber(expressParam.getExpressNumber());
            patient.setExpressRemark(expressParam.getExpressRemark());
            patientServiceImpl.updateExpress(patient);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }


    @GetMapping("/one/{id}")
    public Patient getPatientInfo(@PathVariable("id") Integer patientId){
        Patient patient = new Patient();
        try{
            patient = patientServiceImpl.getPatientByid(patientId);
        }catch(Exception e){
            e.printStackTrace();
        }
        return patient;
    }

    @GetMapping("/all")
    public PageResult<Patient> getAllPatientInfo(@Valid PatientParam patientParam){
        PageResult<Patient> result = new PageResult();
        try{
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                Integer userIdCreate = ((SysUser) principal).getUserIdCreate();
                if(AccountTypeEnum.SERVICE.getType().equals(accountType)){//服务商账号只有自己名下的机器
                    List<String> userNames = this.userServiceImpl.getUserNameBySysUserName(((SysUser) principal).getUserName());
                    patientParam.setUserNames(userNames);
                }
                if(AccountTypeEnum.MAKE.getType().equals(accountType)){//制作账号 只能查看待制作、制作中的鞋垫数据 如果快递账号是服务商下边的用户只能查看服务商的权限
                    List makestatus = new ArrayList();
                    makestatus.add(1);
                    makestatus.add(2);
                    patientParam.setMakeStatus(makestatus);
                    SysUser sysUser = this.sysUserServiceImpl.getSysUserByid(userIdCreate);
                    if(sysUser != null){
                        if(AccountTypeEnum.SERVICE.getType().equals(sysUser.getAccountType())){
                            List<String> userNames = this.userServiceImpl.getUserNameBySysUserName(sysUser.getUserName());
                            patientParam.setUserNames(userNames);
                        }else if(AccountTypeEnum.ORDINARYROOT.getType().equals(sysUser.getAccountType())){ //普通root
                            patientParam.setSysUserName(sysUser.getUserName());
                        }
                    }
                }
                if(AccountTypeEnum.EXPRESS.getType().equals(accountType)){//快递账号 只能查看制作完成的鞋垫数据
                    List makestatus = new ArrayList();
                    makestatus.add(3);
                    patientParam.setMakeStatus(makestatus);

                    SysUser sysUser = this.sysUserServiceImpl.getSysUserByid(userIdCreate);
                    if(sysUser != null){
                        if(AccountTypeEnum.SERVICE.getType().equals(sysUser.getAccountType())){
                            List<String> userNames = this.userServiceImpl.getUserNameBySysUserName(sysUser.getUserName());
                            patientParam.setUserNames(userNames);
                        }else if(AccountTypeEnum.ORDINARYROOT.getType().equals(sysUser.getAccountType())){ //普通root
                            patientParam.setSysUserName(sysUser.getUserName());
                        }
                    }
                }

                if(AccountTypeEnum.FINANCE.getType().equals(accountType) || AccountTypeEnum.OPERATE.getType().equals(accountType)){//运营、财务账号 只能查看他们隶属于的服务商下边的权限
                    SysUser sysUser = this.sysUserServiceImpl.getSysUserByid(userIdCreate);
                    if(sysUser != null){
                        if(AccountTypeEnum.SERVICE.getType().equals(sysUser.getAccountType())){
                            List<String> userNames = this.userServiceImpl.getUserNameBySysUserName(sysUser.getUserName());
                            patientParam.setUserNames(userNames);
                        }else if(AccountTypeEnum.ORDINARYROOT.getType().equals(sysUser.getAccountType())){ //普通root
                            patientParam.setSysUserName(sysUser.getUserName());
                        }
                    }
                }

                if(AccountTypeEnum.ORDINARYROOT.getType().equals(accountType)){//普通root只能查看他名下的服务商的机器下的账号
                    patientParam.setSysUserName(((SysUser) principal).getUserName());
                }
            }
            result = patientServiceImpl.getAllPatient(patientParam);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/update")
    public JsonResult updatePatientInfo(@Valid Patient patient,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getFieldError().getDefaultMessage());
        }
        try{
            patient.setUpdateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            patientServiceImpl.updatePatient(patient);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
    * 更新为待制作状态
    * @author liufeng
    * @date 2020/9/16 0016 11:26
     */
    @PostMapping("/app/toMake")
    public JsonResult updatePatientMakeStatus(Integer patientId,String makePurpose,String type){
        if (patientId == null ) {
            return JsonResult.fail("患者ID不能为空");
        }
        if (StringUtil.isBlank(makePurpose) ) {
            return JsonResult.fail("用途不能为空");
        }
        if(StringUtil.isBlank(type)){
            return JsonResult.fail("用途类型不能为空");
        }
        try{
            Patient patientByid = patientServiceImpl.getPatientByid(patientId);
            if(patientByid == null){
                return JsonResult.fail("患者不存在");
            }
            patientByid.setMakeStatus(1);
            patientByid.setUpdateDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

            StringBuilder makePurposes = new StringBuilder();
            for (int i = 0; i < makePurpose.length(); i++) {
                if("1".equals(String.valueOf(makePurpose.charAt(i)))){
                    String purpose = getPurposeChi(i);
                    makePurposes.append(purpose);
                    if("1".equals(String.valueOf(type.charAt(i)))){
                        makePurposes.append("（进阶）");
                    }else if("2".equals(String.valueOf(type.charAt(i)))){
                        makePurposes.append("（实用、进阶）");
                    }else{
                        makePurposes.append("（实用）");
                    }
                    makePurposes.append("、");
                }
            }
            if(makePurposes.length() < 1){
                makePurposes.append("无、");
            }
            patientByid.setMakePurpose(makePurposes.toString().substring(0,makePurposes.toString().lastIndexOf("、")));
            patientServiceImpl.updateInsoleState(patientByid);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    public String getPurposeChi(Integer i){
        switch (i){
            case 0:
                return "篮球";
            case 1:
                return "足球";
            case 2:
                return "跑步";
            case 3:
                return "滑雪";
            case 4:
                return "高尔夫";
            case 5:
                return "骑行";
            case 6:
                return "室内运动";
            case 7:
                return "舒适";
            case 8:
                return "皮鞋";
        }
        return "";
    }

    @PostMapping("/delete")
    public JsonResult deletePatientInfo(Integer patientId){
        if(StringUtils.isEmpty(patientId)){
            return JsonResult.fail(ResponseCode.ILLEGAL_PARAMS,"患者id不能为空");
        }
        Patient patient = this.patientServiceImpl.getPatientByid(patientId);
        if(patient == null){
            return JsonResult.fail("患者不存在");
        }
        try{
            patient.setStatus("D");
            patientServiceImpl.deletPatient(patient);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @DeleteMapping("/batchdelete")
    public JsonResult batchdeletePatientInfo(String patientId){
        try{
            if(!StringUtils.isEmpty(patientId)){
                String[] split = patientId.split(",");
                List<String> patientIds = new ArrayList<>(split.length);
                Collections.addAll(patientIds,split);
                if(patientIds != null && patientIds.size() > 0){
                    patientServiceImpl.batchdeletePatient(patientIds);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
    * 绑定pdf
    * @author liufeng
    * @date 2020/9/21 0021 21:59
     */
    @PostMapping("/app/bingdingPdf")
    public JsonResult bingdingPdf(@RequestBody PatientPDFParam param){
        Map<String, String> pdfPathMap = new HashMap<>();
        try{
            Gson gson = new Gson();
            log.info("==>PatientPDFParam:" + gson.toJson(param));
            Patient patient = patientServiceImpl.getPatientByid(param.getPatientId());
            if(patient == null){
                return JsonResult.fail("患者不存在");
            }
            pdfPathMap = FileUtils.createPdfPath(configConstant.getPdfUploadPath());
            List processImgPaths = new ArrayList();
            param.getImgPaths().forEach(item -> {
                processImgPaths.add(FileUtils.getClassPath(item));
            });
            PDFUtil.toPdf(processImgPaths,pdfPathMap.get("fullPath"));
            if(0 == param.getPdfType()){
                patientServiceImpl.updatePatientFilePath(param.getPatientId(),pdfPathMap.get("path"));
            }else{
                patientServiceImpl.updatePatientReportPath(param.getPatientId(),pdfPathMap.get("path"));
            }
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success(pdfPathMap.get("path"));
    }

    /**
     * 查看制作账号是否真的有查看结果的权限
     * @author liufeng
     * @date 2020/9/21 0021 21:59
     */
    @PostMapping("/createAccount/priv")
    public JsonResult showCreatePriv(){
        try{
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                Integer userIdCreate = ((SysUser) principal).getUserIdCreate();
                if(AccountTypeEnum.MAKE.getType().equals(accountType)){//制作账号
                    SysUser sysUserByid = this.sysUserServiceImpl.getSysUserByid(userIdCreate);
                    if(sysUserByid != null){
                        Integer accountType1 = sysUserByid.getAccountType();
                        if(AccountTypeEnum.ORDINARYROOT.getType().equals(accountType) || AccountTypeEnum.MAKESERVICE.getType().equals(accountType)){//普通root才有查看结果的权限
                            return JsonResult.success(true);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return JsonResult.success(false);
    }



}

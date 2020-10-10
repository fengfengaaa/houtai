package com.jingbo.houtai.web;

import com.jingbo.houtai.entity.*;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.service.ApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/apk")
@CrossOrigin(origins = {"http://localhost:8080"})
public class ApkController extends BaseController {
    @Autowired
    private ApkService ApkServiceImpl;


    @GetMapping("/current")
    public JsonResult getCurrentAPK(){
        try{
            APK currentApk = ApkServiceImpl.getCurrentApk();
            List result = new ArrayList();
            result.add(currentApk);
            return JsonResult.success(result);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
    }

    @PostMapping("/update")
    public JsonResult updateAPK(@Valid APK apk, BindingResult bindingResult){
        JsonResult jsonResult = processValid(bindingResult);
        if (jsonResult != null) {
            return jsonResult;
        }
        try{
            APK currentApk = ApkServiceImpl.getCurrentApk();
            if(!(apk.getVersionNum() > currentApk.getVersionNum())){
                return JsonResult.fail("版本号比之前版本号小");
            }
            ApkServiceImpl.insertAPK(apk);
            return JsonResult.success();
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
    }

    /**
    * 校验是否需要升级
    * @author liufeng
    * @date 2020/9/17 0017 9:54
     */
    @PostMapping("/check")
    public JsonResult updateAPK(Double versionNum){
        if (versionNum == null) {
            return JsonResult.fail("版本号不能为空");
        }
        Map result = new HashMap();
        result.put("isUpgrade",false);
        try{
            APK currentApk = ApkServiceImpl.getCurrentApk();
            if(currentApk.getVersionNum() > versionNum){
                result.put("isUpgrade",true);
                result.put("apkPath",currentApk.getApkPath());
                result.put("versionNum",currentApk.getVersionNum());
                result.put("versionDes",currentApk.getVersionDes());
                result.put("versionName",currentApk.getVersionName());
                result.put("isForcedUpgrade",currentApk.getIsForcedUpgrade());
                return JsonResult.success(result);
            }
            return JsonResult.success(result);
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
    }
}

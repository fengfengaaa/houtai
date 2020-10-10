package com.jingbo.houtai.web;

import cn.hutool.core.date.DateUtil;
import com.jingbo.houtai.constParam.AccountTypeEnum;
import com.jingbo.houtai.entity.*;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.result.ResponseCode;
import com.jingbo.houtai.service.SysUserService;
import com.jingbo.houtai.util.MD5Utils;
import com.jingbo.houtai.util.ShiroUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/sysuser")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/all")
    public PageResult<SysUser> getAllSysUserInfo(@Valid SysUserParam sysUserParam) {
        PageResult<SysUser> result = new PageResult();
        try {
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                if(AccountTypeEnum.OPERATE.getType().equals(accountType) || AccountTypeEnum.FINANCE.getType().equals(accountType)){//运营 财务只能查看服务商账号
                    sysUserParam.setAccountType(AccountTypeEnum.SERVICE.getType());
                }
                if(AccountTypeEnum.ORDINARYROOT.getType().equals(accountType) || AccountTypeEnum.SERVICE.getType().equals(accountType)){//服务商和普通root只能查看自己创建的账号
                    Integer userId = ((SysUser) principal).getUserId();
                    sysUserParam.setUserIdCreate(userId);
                }
                result = sysUserService.getAllSysUser(sysUserParam);
            }else{
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取服务商账号的名字
     * @author liufeng
     * @date 2020/9/13 0013 21:59
     */
    @GetMapping("/allname")
    public JsonResult getAllSysUserInfo() {
        List<String> result = new ArrayList<>();
        try {

            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                if(AccountTypeEnum.ORDINARYROOT.getType().equals(accountType)){//服务商账号只有自己名下的机器
                    List<String> nameByCreateId = sysUserService.getNameByCreateIdAndService(((SysUser) principal).getUserId());
                    nameByCreateId.forEach(item -> {
                        result.add(item);
                    });
                }else if(AccountTypeEnum.SUPERADMIN.getType().equals(accountType) || AccountTypeEnum.MAKESERVICE.getType().equals(accountType)){
                    List<SysUser> userByAccountType = sysUserService.getUserByAccountType(5);
                    userByAccountType.forEach(item -> {
                        result.add(item.getUserName());
                    });
                }else if(AccountTypeEnum.SERVICE.getType().equals(accountType) || AccountTypeEnum.MAKESERVICE.getType().equals(accountType)){
                    result.add(((SysUser) principal).getUserName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.success(result);
    }

    @PostMapping("/update")
    public JsonResult updateSysUserInfo(SysUser sysUser) {
        try {
            sysUser.setUpdateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sysUserService.updateSysUser(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @DeleteMapping("/delete")
    public JsonResult deleteSysUserInfo(SysUser sysUser) {
        if (StringUtils.isEmpty(sysUser.getUserId())) {
            return JsonResult.fail(ResponseCode.ILLEGAL_PARAMS, "用户id不能为空");
        }
        try {
            sysUser.setStatus("D");
            sysUserService.deleteSysUser(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }
    /**
    * 禁用 启用
    * @author liufeng
    * @date 2020/9/27 0027 21:56
     */
    @PostMapping("/status")
    public JsonResult updateStatus(SysUser sysUser) {
        if (StringUtils.isEmpty(sysUser.getUserId())) {
            return JsonResult.fail(ResponseCode.ILLEGAL_PARAMS, "用户id不能为空");
        }
        try {
            if("N".equals(sysUser.getStatus())){
                sysUser.setStatus("D");
            }else{
                sysUser.setStatus("N");
            }
            sysUserService.deleteSysUser(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @PostMapping("/updatePass")
    public JsonResult UpdatePassword(@Valid SysUser sysUser, BindingResult bindingResult) {
        JsonResult jsonResult = processValid(bindingResult);
        if (jsonResult != null) {
            return jsonResult;
        }
        try {
            String password = MD5Utils.encrypt(sysUser.getUserName(), sysUser.getPassword());
            sysUser.setUpdateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sysUser.setPassword(password);
            sysUserService.updatePassword(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @PostMapping("/add")
    public JsonResult addUser(@Valid SysUser sysUser, BindingResult bindingResult) {
        JsonResult jsonResult = processValid(bindingResult);
        if (jsonResult != null) {
            return jsonResult;
        }
        try {
            String password = MD5Utils.encrypt(sysUser.getUserName(), sysUser.getPassword());
            sysUser.setPassword(password);
            SysUser userByName = sysUserService.getUserByName(sysUser.getUserName());
            if (userByName != null) {
                return JsonResult.fail("用户已经存在");
            }
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                if (AccountTypeEnum.ORDINARYROOT.getType().equals(accountType) || AccountTypeEnum.SERVICE.getType().equals(accountType)) { //服务商和普通root都可以创建系统账号
                    Integer userId = ((SysUser) principal).getUserId();
                    sysUser.setUserIdCreate(userId);
                }
            }
            sysUser.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sysUserService.addSysUser(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("添加用户失败，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
     * 获取当前登录用户的角色类型
     * @author liufeng
     * @date 2020/9/13 0013 21:59
     */
    @GetMapping("/getAccountType")
    public JsonResult getAccountType() {
        try {
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                return JsonResult.success(accountType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.success(-1);
    }

    @DeleteMapping("/batchdelete")
    public JsonResult batchdeletePatientInfo(String sysUserId){
        try{
            if(!StringUtils.isEmpty(sysUserId)){
                String[] split = sysUserId.split(",");
                List<String> patientIds = new ArrayList<>(split.length);
                Collections.addAll(patientIds,split);
                if(patientIds != null && patientIds.size() > 0){
                    sysUserService.batchdeleteSysUser(patientIds);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
     * 获取服务商名下的系统用户
     * @author liufeng
     * @date 2020/9/13 0013 21:59
     */
    @GetMapping("/service/sysuser")
    public JsonResult getServiceUnderSysUser() {
        try {
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                return JsonResult.success(accountType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.success(-1);
    }
}

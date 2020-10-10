package com.jingbo.houtai.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.jingbo.houtai.constParam.AccountTypeEnum;
import com.jingbo.houtai.constParam.LoginType;
import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.SysUser;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.result.ResponseCode;
import com.jingbo.houtai.service.SysUserService;
import com.jingbo.houtai.service.UserService;
import com.jingbo.houtai.shiro.UserPasswordToken;
import com.jingbo.houtai.util.MD5Utils;
import com.jingbo.houtai.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:8080"})
public class UserController extends BaseController{
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private SysUserService sysUserServiceImpl;

    @PostMapping("/add")
    public JsonResult addUser(@Valid User user, BindingResult bindingResult) {
        JsonResult jsonResult = processValid(bindingResult);
        if (jsonResult != null) {
            return jsonResult;
        }
        try {
            String password = MD5Utils.encrypt(user.getUserName(), user.getPassword());
            user.setPassword(password);
            User userByName = userServiceImpl.getUserByName(user.getUserName());
            if (userByName != null) {
                return JsonResult.fail("用户已经存在");
            }
            user.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            userServiceImpl.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("添加用户失败，请联系it人员");
        }
        return JsonResult.success();
    }

    @GetMapping("/one/{id}")
    public User getUserInfo(@PathVariable("id") Integer userId) {
        User user = new User();
        try {
            user = userServiceImpl.getUserByid(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @GetMapping("/all")
    public PageResult<User> getAllUserInfo(@Valid UserParam userParam) {
        PageResult<User> result = new PageResult();
        try {
            Subject subject = ShiroUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof SysUser) {
                Integer accountType = ((SysUser) principal).getAccountType();
                if(AccountTypeEnum.SERVICE.getType().equals(accountType)){//服务商账号只有自己名下的机器
                    userParam.setSysUserName(((SysUser) principal).getUserName());
                }
                if(AccountTypeEnum.ORDINARYROOT.getType().equals(accountType)){//普通root需要list集合
                    List<String> nameByCreateId = sysUserServiceImpl.getNameByCreateId(((SysUser) principal).getUserId());
                    nameByCreateId.add(((SysUser) principal).getUserName());//普通root自己创建的机器 也能查看
                    userParam.setSysUserNames(nameByCreateId);
                }
                result = userServiceImpl.getAllUser(userParam);
            }else{
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/update")
    public JsonResult updateUserInfo(@Valid User user, BindingResult bindingResult) {
        JsonResult jsonResult = processValid(bindingResult);
        if (jsonResult != null) {
            return jsonResult;
        }
        try {
            user.setUpdateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            userServiceImpl.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @PostMapping("/updateHead")
    public JsonResult updateHeadPortrait(String headPortrait) {
        Subject subject = ShiroUtils.getSubject();
        Object principal = subject.getPrincipal();
        try {
            if (principal instanceof User) {
                Integer userId = ((User) principal).getUserId();
                User user = userServiceImpl.getUserByid(userId);
                user.setUpdateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                user.setHeadPortrait(headPortrait);
                userServiceImpl.updateUser(user);
            }else{
                return JsonResult.fail("账号类型错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @PostMapping("/updatePassword")
    public JsonResult updatePassword(@Valid User user, BindingResult bindingResult) {
        JsonResult jsonResult = processValid(bindingResult);
        if (jsonResult != null) {
            return jsonResult;
        }
        try {
            String password = MD5Utils.encrypt(user.getUserName(), user.getPassword());
            user.setPassword(password);
            userServiceImpl.updatePassword(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }


    @PostMapping("/updatePass")
    public JsonResult appUpdatePassword(String oldPass, String newPass) {
        Subject subject = ShiroUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return JsonResult.fail("未登录！！");
        }
        try {
            if (principal instanceof User) {
                Integer userId = ((User) principal).getUserId();
                User user = userServiceImpl.getUserByid(userId);
                String password = MD5Utils.encrypt(user.getUserName(), oldPass);
                if (!user.getPassword().equals(password)) {
                    return JsonResult.fail("原密码不正确");
                }
                newPass = MD5Utils.encrypt(user.getUserName(), newPass);
                user.setPassword(newPass);
                user.setUpdateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                userServiceImpl.updatePassword(user);
            }else{
                return JsonResult.fail("账号类型错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @DeleteMapping("/delete")
    public JsonResult deleteUserInfo(User user) {
        if (StringUtils.isEmpty(user.getUserId())) {
            return JsonResult.fail(ResponseCode.ILLEGAL_PARAMS, "用户id不能为空");
        }
        try {
            user.setStatus("D");
            userServiceImpl.deleteUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @PostMapping("/status")
    public JsonResult updateStatus(User user) {
        if (StringUtils.isEmpty(user.getUserId())) {
            return JsonResult.fail(ResponseCode.ILLEGAL_PARAMS, "用户id不能为空");
        }
        try {
            if("N".equals(user.getStatus())){
                user.setStatus("D");
            }else{
                user.setStatus("N");
            }
            userServiceImpl.deleteUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    @CrossOrigin
    @DeleteMapping("/batchdelete")
    public JsonResult batchdeleteUserInfo(String userid) {
        try {

            if (!StringUtils.isEmpty(userid)) {
                String[] split = userid.split(",");
                List<String> userIds = new ArrayList<>(split.length);
                Collections.addAll(userIds, split);
                if (userIds != null && userIds.size() > 0) {
                    userServiceImpl.batchdeleteUser(userIds);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
        return JsonResult.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public JsonResult login(String username, String password) {
        Map map = new HashMap();
        try {
            Subject subject = ShiroUtils.getSubject();
            password = MD5Utils.encrypt(username, password);
            UsernamePasswordToken token = new UserPasswordToken(username, password, LoginType.MOBILE.getType());
            //subject.getSession().setTimeout(-1000L);//登录永不过期
            subject.getSession().setTimeout(30 * 24 * 60 * 60 * 1000);//设置一个月过期
            subject.login(token);
            Serializable logintoken = subject.getSession().getId();
            map.put("token", logintoken.toString());
            User userByName = userServiceImpl.getUserByName(username);
            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(userByName);
            map.putAll(stringObjectMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.success(map);
    }


    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public JsonResult logout(HttpServletRequest request) {
        try {
            ShiroUtils.getSubject().logout();
        } catch (Exception ex) {
            return JsonResult.fail(ex.getMessage());
        }
        return JsonResult.success("退出成功");
    }
}

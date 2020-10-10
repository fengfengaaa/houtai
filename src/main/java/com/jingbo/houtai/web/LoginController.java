package com.jingbo.houtai.web;

import com.jingbo.houtai.constParam.LoginType;
import com.jingbo.houtai.entity.SysUser;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.service.SysUserService;
import com.jingbo.houtai.shiro.UserPasswordToken;
import com.jingbo.houtai.util.MD5Utils;
import com.jingbo.houtai.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/sysuser")
@Slf4j
public class LoginController {
    /**
     * 登录
     */
    @PostMapping("/login")
    public JsonResult login(String username, String password){
        Subject subject = ShiroUtils.getSubject();
        try{
            password = MD5Utils.encrypt(username, password);
            UsernamePasswordToken token = new UserPasswordToken(username, password, LoginType.WEB.getType());
            subject.login(token);
        }catch (Exception e) {
            e.printStackTrace();
            log.info("后台账号登录失败，校验是否为app端账号");
            try{
                UsernamePasswordToken token = new UserPasswordToken(username, password, LoginType.MOBILE.getType());//机器账号也可以登录后端
                subject.login(token);
            }catch(Exception e1){
                e1.printStackTrace();
                return JsonResult.fail(e.getMessage());
            }
        }
        return JsonResult.success("登录成功");
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public JsonResult register(SysUser user){
        return JsonResult.success("注册成功,去登陆");
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public JsonResult logout(HttpServletRequest request){
        StringBuilder redirectUrl = new StringBuilder();
        try {
            String scheme = request.getScheme();//-->http
            String serverName = request.getServerName();//-->localhost
            int serverPort = request.getServerPort();//-->3000
            redirectUrl.append(scheme);
            redirectUrl.append("://");
            redirectUrl.append(serverName);
            redirectUrl.append(":");
            redirectUrl.append(serverPort);
            redirectUrl.append("/login.html");
            ShiroUtils.getSubject().logout();
        } catch (Exception ex) {
            log.error("Could not redirect to: " + "", ex);
            return JsonResult.fail(ex.getMessage());
        }
        return JsonResult.success(redirectUrl);
    }
}

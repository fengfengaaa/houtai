package com.jingbo.houtai.shiro;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.jingbo.houtai.entity.SysRole;
import com.jingbo.houtai.entity.SysUser;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.result.ResponseCode;
import com.jingbo.houtai.service.RoleService;
import com.jingbo.houtai.service.SysUserService;
import com.jingbo.houtai.util.MobileDevice;
import com.jingbo.houtai.util.ShiroUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 继承FormAuthenticationFilter，重写onAccessDenied方法
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(ShiroFormAuthenticationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                return true;
            }
        } else {
            if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                resp.setStatus(HttpStatus.OK.value());
                return true;
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [{}]" ,this.getLoginUrl());
                }

                boolean mobileDevice = MobileDevice.isMobileDevice(((HttpServletRequest) request));
                if(!mobileDevice){
                    super.onAccessDenied(request,response);
                    return false;
                }
                /**
                 * 在这里实现自己想返回的信息，其他地方和源码一样就可以了
                 */
                resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
                resp.setHeader("Access-Control-Allow-Credentials", "true");
                resp.setContentType("application/json; charset=utf-8");
                resp.setCharacterEncoding("UTF-8");
                JsonResult fail = JsonResult.fail(ResponseCode.NO_LOGIN,"未登录");
                PrintWriter out = resp.getWriter();
                out.println(JSONUtil.toJsonStr(fail));
                out.flush();
                out.close();
                return false;
            }
        }
    }

}

package com.jingbo.houtai.shiro;

import com.jingbo.houtai.constParam.LoginType;
import com.jingbo.houtai.entity.SysUser;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.service.SysUserService;
import com.jingbo.houtai.service.UserService;
import com.jingbo.houtai.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userServiceImpl;


    /**
     * 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样。
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名
        Object principal = ShiroUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(principal instanceof SysUser){
            Integer accountType = ((SysUser) principal).getAccountType();
            log.info("==>accountType:" + accountType);
            simpleAuthorizationInfo.addRole(String.valueOf(accountType));
        }else{
            simpleAuthorizationInfo.addRole("0");
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 身份认证。即登录通过账号和密码验证登陆人的身份信息。
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UserPasswordToken userPasswordToken = (UserPasswordToken) authenticationToken;
        String loginType = userPasswordToken.getLoginType();
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        if(LoginType.WEB.getType().equals(loginType)){//说明是系统用户
            SysUser sysUser = sysUserService.getUserByName(username);
            if(sysUser != null){
                if(!password.equals(sysUser.getPassword())) {
                    throw new IncorrectCredentialsException("密码不正确");
                }
                if(!"N".equals(sysUser.getStatus())) {//禁用
                    throw new IncorrectCredentialsException("账户未启用");
                }
                return new SimpleAuthenticationInfo(sysUser, password, getName());
            }else{
                throw new IncorrectCredentialsException("账户不存在");
            }
        }

        if(LoginType.MOBILE.getType().equals(loginType)){
            User user = userServiceImpl.getUserByName(username);
            if(user != null){//说明是移动端用户
                if(!password.equals(user.getPassword())) {
                    throw new IncorrectCredentialsException("密码不正确");
                }
                if(!"N".equals(user.getStatus())) {//禁用
                    throw new IncorrectCredentialsException("账户未启用");
                }
                return new SimpleAuthenticationInfo(user, password, getName());
            }else{
                throw new UnknownAccountException("账户不存在");
            }
        }

        throw new UnknownAccountException("不支持的登陆类型");
    }
}


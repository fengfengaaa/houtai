package com.jingbo.houtai.shiro;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class ShiroConfig {

    private RedisProperties redis;

    public ShiroConfig(RedisProperties redis) {
        this.redis = redis;
    }

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("kickout", kickoutSessionControlFilter());
        filtersMap.put("authc", new ShiroFormAuthenticationFilter());
        filtersMap.put("roles", new RolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        /**
         * 静态文件
         */
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/lib/**", "anon");
        filterChainDefinitionMap.put("/data/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/uploadfile/**", "anon");
     /*   filterChainDefinitionMap.put("/dist/**", "anon");*/
        /*
         * 登录注册
         */
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("/register.html", "anon");
        filterChainDefinitionMap.put("/forget.html", "anon");
        filterChainDefinitionMap.put("/api/sysuser/logout", "anon");
        filterChainDefinitionMap.put("/api/sysuser/login", "anon");
        filterChainDefinitionMap.put("/api/role/**", "anon");
        filterChainDefinitionMap.put("/api/user/login", "anon");
        filterChainDefinitionMap.put("/sys/register", "anon");
        /*
         * 短链
         */
        filterChainDefinitionMap.put("/r/**", "anon");
        /*
        小程序
         */
        filterChainDefinitionMap.put("/minApp/**", "anon");
        /*
        管理后台
         */
        filterChainDefinitionMap.put("/api/user/batchdelete", "roles[0,6]");
        filterChainDefinitionMap.put("/api/user/add", "roles[0,1,6]");
        filterChainDefinitionMap.put("/api/user/update", "roles[0,6]");
        filterChainDefinitionMap.put("/api/user/delete", "roles[0,1,6]");
        filterChainDefinitionMap.put("/api/user/updatePassword", "roles[0,6]");

        filterChainDefinitionMap.put("/api/price/update", "roles[0,1,6]");

        filterChainDefinitionMap.put("/api/patient/all", "roles[0,2,4,5,6]");
        filterChainDefinitionMap.put("/api/patient/batchdelete", "roles[0,6]");
        filterChainDefinitionMap.put("/api/patient/update", "roles[0,5,6]");
        filterChainDefinitionMap.put("/api/patient/delete", "roles[0,6]");
        filterChainDefinitionMap.put("/api/patient/update/insoleState", "roles[0,3,5,6,7]");
        filterChainDefinitionMap.put("/api/patient/update/express", "roles[0,4,6]");
        filterChainDefinitionMap.put("/api/patient/batchdelete", "roles[0,6]");

        filterChainDefinitionMap.put("/api/sysuser/add", "roles[0,1,5,6]");
        filterChainDefinitionMap.put("/api/sysuser/update", "roles[0,6,5]");
        filterChainDefinitionMap.put("/api/sysuser/delete", "roles[0,1,6,5]");
        filterChainDefinitionMap.put("/api/sysuser/updatePass", "roles[0,6]");

        filterChainDefinitionMap.put(" /api/apk/update", "roles[0]");

        filterChainDefinitionMap.put("/**", "kickout,authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager(sessionIdCookie()));
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(@Qualifier("sessionIdCookie") Cookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);//去掉 JSESSIONID
        /**
         * 如果这里赋值，默认会使用这里的参数
         */
        long time = 2 * 60 * 60 * 1000;
        sessionManager.setGlobalSessionTimeout(time);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        return sessionManager;
    }

    @Bean
    public Cookie sessionIdCookie() {
      return new SimpleCookie("token");
    }


    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setPrincipalIdFieldName("userId");
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager(redis);
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现
     * 原理就是重写 AbstractSessionDAO
     * 有兴趣的小伙伴自行阅读源码
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setKeyPrefix("shiro:session:cloudbed:");
        return redisSessionDAO;
    }

    /**
     * 限制同一账号登录同时登录人数控制
     *
     * @return
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        kickoutSessionControlFilter.setSessionManager(sessionManager(sessionIdCookie()));
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(10);
        kickoutSessionControlFilter.setKickoutUrl("/login.html");
        return kickoutSessionControlFilter;
    }

}
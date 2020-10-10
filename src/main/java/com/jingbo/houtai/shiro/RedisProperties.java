package com.jingbo.houtai.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;

}

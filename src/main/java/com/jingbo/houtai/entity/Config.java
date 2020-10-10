package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class Config {
    @Value("superuser.name")
    private String superUser;
    @Value("superuser.password")
    private String password;
}

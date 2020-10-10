package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FootTemplate {
    private String foot_type;//L 左脚 R 右脚
    private String template_type;//模板级别 A、B、C

    public FootTemplate(String foot_type, String template_type) {
        this.foot_type = foot_type;
        this.template_type = template_type;
    }

    public FootTemplate() {
    }
}

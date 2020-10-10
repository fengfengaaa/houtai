package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class APK {
    private String versionId;
    @NotBlank(message = "请输入版本名字")
    private String versionName;//版本名字
    @NotNull(message = "请输入版本号")
    private Double versionNum;//版本号
    private String versionDes;//版本描述
    @NotBlank(message = "请上传APK")
    private String apkPath;//apk路径
    @NotBlank(message = "请确定是否强制升级")
    private String isForcedUpgrade;
}

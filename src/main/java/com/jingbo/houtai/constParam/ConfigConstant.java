package com.jingbo.houtai.constParam;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ConfigConstant {
    @Value("${file.pdf.upload.path}")
    private String pdfUploadPath;
    @Value("${file.img.upload.path}")
    private String imageUploadPath;
    @Value("${file.apk.upload.path}")
    private String apkUploadPath;
}

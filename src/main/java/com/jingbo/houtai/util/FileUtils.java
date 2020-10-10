package com.jingbo.houtai.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.jingbo.houtai.constParam.SystemConstant;
import com.jingbo.houtai.result.JsonResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileUtils {

    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    /*  *//**
     * 获取上传文件的MD5值
     * @param file
     * @return
     * @throws IOException
     *//*
    public static String getMd5(MultipartFile file) {
        try {
            return DigestUtils.md5Hex(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }*/

    /**
     * 创建多级文件夹
     *
     * @return
     */
    public static File createByDay(String filePath) {
        File parentFile = new File(filePath + SystemConstant.SF_FILE_SEPARATOR + DateUtil.thisYear());
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        parentFile = new File(parentFile, (DateUtil.thisMonth() + 1) + "");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        parentFile = new File(parentFile, DateUtil.thisDayOfMonth() + "");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return parentFile;
    }

    /**
     * 获取classpath
     *
     * @return
     */
    public static String getClassPath(String filePath) {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static/";
        path = path + filePath;
        return path;
    }

    /**
     * 创建多级文件夹
     *
     * @return
     */
    public static String uploadFile(MultipartFile file, String uploadPath, String fileType) throws IOException {
        String path = getClassPath(uploadPath);
        File parentFile = new File(path);
        String fileName = file.getOriginalFilename();
        if ("img".equals(fileType)) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String uuid = IdUtil.simpleUUID();
            fileName = uuid + suffix;
        }
        File imageFile = new File(parentFile, fileName);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        FileUtil.writeFromStream(file.getInputStream(), imageFile);
        return uploadPath + "/" + fileName;
    }

    /**
     * 创建多级文件夹
     *
     * @return
     */
    public static Map<String, String> createPdfPath(String pdfPath) throws IOException {
        Map<String,String> result = new HashMap<>();
        String path = getClassPath(pdfPath);
        String uuid = IdUtil.simpleUUID();
        String fileName = uuid + ".pdf";
        result.put("fullPath",path + "/" + fileName);
        result.put("path",pdfPath + "/" + fileName);
        return result;
    }


}

package com.jingbo.houtai.web;

import com.jingbo.houtai.constParam.ConfigConstant;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = {"http://localhost:8080"})
@Slf4j
public class FileController {

    @Autowired
    private ConfigConstant configConstant;
    /**
     * 文件上传
     */
    @PostMapping("pdfUpload")
    public JsonResult pdfUpload(MultipartFile file) {
        try {
            if(file!=null){
                String fileUploadPath = configConstant.getPdfUploadPath();
                String filePath = FileUtils.uploadFile(file, fileUploadPath,"pdf");
                return JsonResult.success(filePath);
            }else{
                return JsonResult.fail("上传文件失败");
            }
        } catch (Exception e) {
            return JsonResult.fail("上传文件失败");
        }
    }

    /**
     * 图片上传
     */
    @PostMapping("imageUpload")
    public JsonResult imgUpload(MultipartFile file) {
        try {
            if(file!=null){
                String imageUploadPath = configConstant.getImageUploadPath();
                String filePath = FileUtils.uploadFile(file, imageUploadPath,"img");
                return JsonResult.success(filePath);
            }else{
                return JsonResult.fail("上传图片失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("上传图片失败");
        }
    }

    /**
     * 文件上传
     */
    @PostMapping("apkUpload")
    public JsonResult apkUpload(MultipartFile file) {
        try {
            if(file!=null){
                String fileUploadPath = configConstant.getApkUploadPath();
                String filePath = FileUtils.uploadFile(file, fileUploadPath,"apk");
                return JsonResult.success(filePath);
            }else{
                return JsonResult.fail("上传文件失败");
            }
        } catch (Exception e) {
            return JsonResult.fail("上传文件失败");
        }
    }

    /**
     * 图片下载
     */
    @GetMapping("fileDownload")
    public JsonResult fileDownLoad(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            //String fileUploadPath = configConstant.getPdfUploadPath();
            // 如果文件存在，则进行下载
                File file = new File(fileName);
                // 如果文件名存在，则进行下载
                if (file.exists()) {
                    // 配置文件下载
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                    // 实现文件下载
                    byte[] buffer = new byte[1024];
                    FileInputStream fis = null;
                    BufferedInputStream bis = null;
                    try {
                        fis = new FileInputStream(file);
                        bis = new BufferedInputStream(fis);
                        OutputStream os = response.getOutputStream();
                        int i = bis.read(buffer);
                        while (i != -1) {
                            os.write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                        log.info("Download the song successfully!");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        log.info("Download the song failed!" + e.getMessage());
                        return JsonResult.fail("下载失败");
                    }
                    finally {
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            return JsonResult.success("下载成功");
        } catch (Exception e) {
            return JsonResult.fail("下载失败");
        }
    }
}

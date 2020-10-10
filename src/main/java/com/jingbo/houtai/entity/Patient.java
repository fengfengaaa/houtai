package com.jingbo.houtai.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
/**
 * 患者信息
 */
public class Patient implements Serializable {
    private Integer patientId;
    private Integer userId; //app端用户Id
    private String userName; //app端用户名
    private String patientName;
    @NotBlank(message = "联系方式不能为空")
    private String phoneNum;//联系方式
    private Integer sex;//性别 1 男 0 女
    @DateTimeFormat(pattern = "%Y-%m-%d")
    private String birthday;//出生日期 2020-07-11
    private Integer height;//身高 厘米
    private Double weight;//体重 KG
    private String address;//地址
    private List<Foot> foots;//左右脚的信息
    private Integer selfCheck;//自检 0内八脚 1外八脚 2正常
    private String status = "N";//患者状态 D删除 N正常
    @DateTimeFormat(pattern = "%Y-%m-%d %H:%i:%s")
    private String createDate;
    @DateTimeFormat(pattern = "%Y-%m-%d %H:%i:%s")
    private String updateDate;
    private String filePath;//结果文件路径
    private String handedness;//惯用手 L 左手 R 右手
    private String purpose="000000000";//用途 以000000000代表是否选择 顺序为篮球、足球、跑步、滑雪、高尔夫、骑行、室内运动、舒适、皮鞋
    private String assessor;//评估人
    private Integer outboardLeft;//外侧加高左脚 毫米
    private Integer outboardRight;//外侧加高右脚 毫米
    private Integer outboardLocation;//外侧加高位置 0 全部 1前足 2后足
    private Integer inboardLeft;//内侧加高左脚 毫米
    private Integer inboardRight;//内侧加高右脚 毫米
    private Integer inboardLocation;//内侧加高位置  0 全部 1前足 2后足
    private Integer heelboardLeft;//后跟加高左脚 毫米
    private Integer heelboardRight;//后跟加高右脚 毫米
    private String remarks;//备注
    private Double shoeSize;//鞋码
    private String viewOnFoot;//足上面观成像
    private String plantarPressure;//足底压力成像
    private String weightDistribution;//重量分布成像
    private String calcanealAngle;//跟骨角成像
    private Integer areInsolesMade;//0 不制作 1 制作
    private String reportPath;//报告文件路径
    private Integer makeStatus = 0;//鞋垫制作状态 0 不制作 1 待制作 2 制作中 3 制作完成 4 邮递中 5 已签收
    private String addressee;//收件人
    private String addresseePhone;//收件人电话
    private String toAddress;//收件地址
    private String expressNumber;//快递单号
    private String expressRemark;//订单备注
    private Integer pixelPosition;//像素位置 用于App
    private String makePurpose;//要制作鞋垫的类型 0 篮球、1 足球、2 跑步、3 滑雪、4 高尔夫、5 骑行、6 室内运动、7 舒适、8 皮鞋
    private String sysUserName;//普通用户
    public Patient(){
    }
    public Patient(String json) throws JsonProcessingException {//解决不能解析json的问题 貌似是springboot的问题
        Patient patient = new ObjectMapper().readValue(json, Patient.class);
        this.userId = patient.getUserId(); //app端用户Id
        userName = patient.getUserName(); //app端用户名
        patientName = patient.getPatientName();
        phoneNum = patient.getPhoneNum();//联系方式
        sex = patient.getSex();//性别 1 男 0 女
        birthday = patient.getBirthday();//出生日期 2020-07-11
        height = patient.getHeight();//身高 厘米
        weight = patient.getWeight();//体重 KG
        address = patient.getAddress();//地址
        foots = patient.getFoots();//左右脚的信息
        selfCheck = patient.getSelfCheck();//自检 0内八脚 1外八脚 2正常
        status = patient.getStatus() == null ? "N": patient.getStatus();//患者状态 D删除 N正常
        createDate = patient.getCreateDate();
        updateDate = patient.getUpdateDate();
        filePath = patient.getFilePath();
        handedness = patient.getHandedness();//惯用手 L 左手 R 右手
        purpose = patient.getPurpose() == null ? "000000000": patient.getPurpose();//用途 以000000000代表是否选择 顺序为篮球、足球、跑步、滑雪、高尔夫、骑行、室内运动、舒适、皮鞋
        assessor = patient.getAssessor();//评估人
        outboardLeft = patient.getOutboardLeft();//外侧加高左脚 毫米
        outboardRight = patient.getOutboardRight();//外侧加高右脚 毫米
        outboardLocation = patient.getOutboardLocation();//外侧加高位置 0 全部 1前足 2后足
        inboardLeft = patient.getInboardLeft();//内侧加高左脚 毫米
        inboardRight = patient.getInboardRight();//内侧加高右脚 毫米
        inboardLocation = patient.getInboardLocation();//内侧加高位置  0 全部 1前足 2后足
        heelboardLeft = patient.getHeelboardLeft();//后跟加高左脚 毫米
        heelboardRight = patient.getHeelboardRight();//后跟加高右脚 毫米
        remarks = patient.getRemarks();//备注
        shoeSize = patient.getShoeSize();//鞋码
        viewOnFoot= patient.getViewOnFoot();
        plantarPressure = patient.getPlantarPressure();
        calcanealAngle=patient.getCalcanealAngle();
    }
}

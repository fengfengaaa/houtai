package com.jingbo.houtai.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
* 左右脚的信息
* @author liufeng
* @date 2020/8/10 0010 23:02
 */
@Getter
@Setter
public class Foot {
    private Integer footId;
    private Integer patientId;
    private String footType;//L左脚R右脚
    private Integer stiffToe;//拇趾僵硬
    private Integer metatarsalPain;//跖骨痛
    private Integer mortonNeuroma;//莫顿神经瘤
    private Integer tendernessPain;//横弓压痛或敏感
    private Integer plantarFasciitis;//足底筋膜炎
    private Integer calcanealSpur;//跟骨骨刺
    private Integer heelPain;//足跟痛
    private Integer tarsalArthropathy;//跗骨关节病
    private Integer atHeelPain;//跟腱足跟痛
    private Integer rheumatism;//风湿病
    private Integer saccharosis;//糖尿病
    private Integer malletToe;//锤状趾
    private Integer clawToe;//爪状趾
    private Integer halluxValgus;//拇外翻
    private Integer highArchedFoot;//高弓足
    private Integer flatfoot;//扁平足
    private Integer pronation;//旋前
    private Integer supination;//旋后
    private Integer varusDeformity;//内翻畸形
    private Integer valgusDeformity;//外翻畸形
    private Integer pelvicTilt;//盆骨倾斜
    private Integer pelvicTorsion;//盆骨扭转
    private Integer pelvicPain;//盆骨痛
    private Integer genuVarus;//膝内翻
    private Integer genuValgus;//膝外翻
    private Integer unequalLength;//双下肢不等长
    private Integer unilHemiplegia;//单侧偏瘫
    private Integer spasticity;//痉挛状态
    private Integer hipFlexorShortening;//屈髋肌短缩
    private Integer shorteningoptm;//大腿后肌群短缩
    private Integer shorteningoatm;//大腿前肌群短缩
    private Integer shorteningoplm;//小腿后肌群短缩
    private Integer shorteningopcmaat;//小腿后肌群及跟腱短缩
    private Integer bearing;//承重
    private Double archLength;//足弓长
    private Double footLength;//脚长

    public Foot(){
    }

    public Foot(String json)throws JsonProcessingException {//解决不能解析json的问题 貌似是springboot的问题
        Foot foot = new ObjectMapper().readValue(json, Foot.class);
        this.footType = foot.getFootType();//L左脚R右脚
        this.stiffToe = foot.getStiffToe();//拇趾僵硬
        this.metatarsalPain = foot.getMetatarsalPain() ;//跖骨痛
        this.mortonNeuroma = foot.getMortonNeuroma();//莫顿神经瘤
        this.tendernessPain = foot.getTendernessPain();//横弓压痛或敏感
        this.plantarFasciitis = foot.getPlantarFasciitis();//足底筋膜炎
        this.calcanealSpur = foot.getCalcanealSpur();//跟骨骨刺
        this.heelPain = foot.getHeelPain();//足跟痛
        this.tarsalArthropathy = foot.getTarsalArthropathy();//跗骨关节病
        this.atHeelPain = foot.getAtHeelPain();//跟腱足跟痛
        this.rheumatism = foot.getRheumatism();//风湿病
        this.saccharosis = foot.getSaccharosis();//糖尿病
        this.malletToe = foot.getMalletToe();//锤状趾
        this.clawToe = foot.getClawToe();//爪状趾
        this.halluxValgus = foot.getHalluxValgus();//拇外翻
        this.highArchedFoot = foot.getHighArchedFoot();//高弓足
        this.flatfoot = foot.getFlatfoot();//扁平足
        this.pronation = foot.getPronation();//旋前
        this.supination = foot.getSupination();//旋后
        this.varusDeformity = foot.getVarusDeformity();//内翻畸形
        this.valgusDeformity = foot.getValgusDeformity();//外翻畸形
        this.pelvicTilt = foot.getPelvicTilt();//盆骨倾斜
        this.pelvicTorsion = foot.getPelvicTorsion();//盆骨扭转
        this.pelvicPain = foot.getPelvicPain();//盆骨痛
        this.genuVarus = foot.getGenuVarus();//膝内翻
        this.genuValgus = foot.getGenuValgus();//膝外翻
        this.unequalLength = foot.getUnequalLength();//双下肢不等长
        this.unilHemiplegia = foot.getUnilHemiplegia();//单侧偏瘫
        this.spasticity = foot.getSpasticity();//痉挛状态
        this.hipFlexorShortening = foot.getHipFlexorShortening();//屈髋肌短缩
        this.shorteningoptm = foot.getShorteningoptm();//大腿后肌群短缩
        this.shorteningoatm = foot.getShorteningoatm();//大腿前肌群短缩
        this.shorteningoplm = foot.getShorteningoplm();//小腿后肌群短缩
        this.shorteningopcmaat = foot.getShorteningopcmaat();//小腿后肌群及跟腱短缩
        this.bearing = foot.getBearing();//承重
    }
}

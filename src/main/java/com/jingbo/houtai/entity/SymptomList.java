package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymptomList {
    private  Integer FOOTTYPE;//L左脚R右脚
    private  Integer STIFFTOE;//拇趾僵硬
    private  Integer METATARSALPAIN;//跖骨痛
    private  Integer MORTONNEUROMA;//莫顿神经瘤
    private  Integer TENDERNESSPAIN;//横弓压痛或敏感
    private  Integer PLANTARFASCIITIS;//足底筋膜炎
    private  Integer CALCANEALSPUR;//跟骨骨刺
    private  Integer HEELPAIN;//足跟痛
    private  Integer TARSALARTHROPATHY;//跗骨关节病
    private  Integer ATHEELPAIN ;//跟腱足跟痛
    private  Integer RHEUMATISM;//风湿病
    private  Integer SACCHAROSIS ;//糖尿病
    private  Integer MALLETTOE;//锤状趾
    private  Integer CLAWTOE ;//爪状趾
    private  Integer HALLUXVALGUS ;//拇外翻
    private  Integer HIGHARCHEDFOOT;//高弓足
    private  Integer FLATFOOT ;//扁平足
    private  Integer PRONATION ;//旋前
    private  Integer SUPINATION ;//旋后
    private  Integer VARUSDEFORMITY ;//内翻畸形
    private  Integer VALGUSDEFORMITY ;//外翻畸形
    private  Integer PELVICTILT ;//盆骨倾斜
    private  Integer PELVICTORSION ;//盆骨扭转
    private  Integer PELVICPAIN ;//盆骨痛
    private  Integer GENUVARUS ;//膝内翻
    private  Integer GENUVALGUS ;//膝外翻
    private  Integer UNEQUALLENGTH ;//双下肢不等长
    private  Integer UNILHEMIPLEGIA ;//单侧偏瘫
    private  Integer SPASTICITY ;//痉挛状态
    private  Integer HIPFLEXORSHORTENING ;//屈髋肌短缩
    private  Integer SHORTENINGOPTM ;//大腿后肌群短缩
    private  Integer SHORTENINGOATM ;//大腿前肌群短缩
    private  Integer SHORTENINGOPLM ;//小腿后肌群短缩
    private  Integer SHORTENINGOPCMAAT ;//小腿后肌群及跟腱短缩
    private  Integer BEARING ;//承重
    private  String PURPOSE;//用途 以000000000代表是否选择 顺序为篮球、足球、跑步、滑雪、高尔夫、骑行、室内运动、舒适、皮鞋
    private  Integer OUTBOARDLEFT ;//外侧加高左脚 毫米
    private  Integer OUTBOARDRIGHT ;//外侧加高右脚 毫米
    private  Integer OUTBOARDLOCATION ;//外侧加高位置 0 全部 1前足 2后足
    private  Integer INBOARDLEFT ;//内侧加高左脚 毫米
    private  Integer INBOARDRIGHT ;//内侧加高右脚 毫米
    private  Integer INBOARDLOCATION ;//内侧加高位置  0 全部 1前足 2后足
    private  Integer HEELBOARDLEFT ;//后跟加高左脚 毫米
    private  Integer HEELBOARDRIGHT ;//后跟加高右脚 毫米
    private  Integer SELFCHECK ;//自检 0内八脚 1外八脚 2正常
    private  Integer WEIGHT ;//体重
}

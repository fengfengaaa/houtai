package com.jingbo.houtai.constParam;

import com.jingbo.houtai.entity.PriorityTemplate;
import com.jingbo.houtai.entity.Symptom;

import java.util.*;

/**
* 优先级map
* @author liufeng
* @date 2020/8/22 0022 10:36
 */
public class PriorityConfig {
    public final static Map<String, List<PriorityTemplate>> tempaltePrioryityMap = new HashMap<>();//模板的优先级
    static{
        //模板一
        //模板一的B级
        List<PriorityTemplate> template_one = new ArrayList<>();
        Set<Symptom> t1_b_set = new HashSet<>();
        t1_b_set.add(new Symptom(3,FootConst.SUPINATION,new HashSet<>(Arrays.asList(1,2,3)),true));
        t1_b_set.add(new Symptom(3,FootConst.SELFCHECK,new HashSet<>(Arrays.asList(2)),true));
        PriorityTemplate t1_b = new PriorityTemplate(TemplateType.B.name(),t1_b_set);
        template_one.add(t1_b);
        //模板一的C级
        Set<Symptom> t1_c_set = new HashSet<>();
        t1_c_set.add(new Symptom(2,FootConst.CLAWTOE,new HashSet<>(Arrays.asList(1,2,3)),true));
        t1_c_set.add(new Symptom(2,FootConst.MALLETTOE,new HashSet<>(Arrays.asList(1,2,3)),true));
        t1_c_set.add(new Symptom(1,FootConst.PURPOSE,new HashSet<>(Arrays.asList(3,5)),true,SymptomType.PURPOSE));
        t1_c_set.add(new Symptom(2,FootConst.MORTONNEUROMA,new HashSet<>(Arrays.asList(1,2,3)),true));
        t1_c_set.add(new Symptom(2,FootConst.RHEUMATISM,new HashSet<>(Arrays.asList(1,2,3)),true));
        t1_c_set.add(new Symptom(3,FootConst.HALLUXVALGUS,new HashSet<>(Arrays.asList(1,2,3)),true));
        PriorityTemplate t1_c = new PriorityTemplate(TemplateType.C.name(),t1_c_set);
        template_one.add(t1_c);

        tempaltePrioryityMap.put(TemplateKeyType.T1.name(),template_one);

        //模板二
        //模板二的A级
        List<PriorityTemplate> template_two = new ArrayList<>();
        Set<Symptom> t2_a_set = new HashSet<>();
        t2_a_set.add(new Symptom(4,FootConst.WEIGHT,new HashSet<>(Arrays.asList(40,100)),true,SymptomType.WEIGHT,2));
        t2_a_set.add(new Symptom(3,FootConst.METATARSALPAIN,new HashSet<>(Arrays.asList(1)),true));
        PriorityTemplate t2_a = new PriorityTemplate(TemplateType.A.name(),t2_a_set);
        template_two.add(t2_a);
        //模板二的B级
        Set<Symptom> t2_b_set = new HashSet<>();
        t2_b_set.add(new Symptom(4,FootConst.WEIGHT,new HashSet<>(Arrays.asList(100)),true,SymptomType.WEIGHT,0));
        t2_b_set.add(new Symptom(5,FootConst.HIGHARCHEDFOOT,new HashSet<>(Arrays.asList(1,2,3)),true));
        t2_b_set.add(new Symptom(3,FootConst.METATARSALPAIN,new HashSet<>(Arrays.asList(2,3)),true));
        t2_b_set.add(new Symptom(2,FootConst.PLANTARFASCIITIS,new HashSet<>(Arrays.asList(2,3)),true));
        t2_b_set.add(new Symptom(4,FootConst.HALLUXVALGUS,new HashSet<>(Arrays.asList(1)),true));
        PriorityTemplate t2_b = new PriorityTemplate(TemplateType.B.name(),t2_b_set);
        template_two.add(t2_b);

        //模板二的C级
        Set<Symptom> t2_c_set = new HashSet<>();
        t2_c_set.add(new Symptom(4,FootConst.WEIGHT,new HashSet<>(Arrays.asList(40)),true,SymptomType.WEIGHT,1));
        t2_c_set.add(new Symptom(1,FootConst.TENDERNESSPAIN,new HashSet<>(Arrays.asList(1,2,3)),true));
        t2_c_set.add(new Symptom(1,FootConst.HALLUXVALGUS,new HashSet<>(Arrays.asList(2,3)),true));
        PriorityTemplate t2_c = new PriorityTemplate(TemplateType.C.name(),t2_c_set);
        template_two.add(t2_c);

        tempaltePrioryityMap.put(TemplateKeyType.T2.name(),template_two);

        //模板三
        //模板三的A级
        List<PriorityTemplate> template_three = new ArrayList<>();
        Set<Symptom> t3_a_set = new HashSet<>();
        t3_a_set.add(new Symptom(5,FootConst.FLATFOOT,new HashSet<>(Arrays.asList(2,3)),true));
        t3_a_set.add(new Symptom(7,FootConst.TARSALARTHROPATHY,new HashSet<>(Arrays.asList(1,2,3)),true));
        t3_a_set.add(new Symptom(2,FootConst.PURPOSE,new HashSet<>(Arrays.asList(0,1,2)),true,SymptomType.PURPOSE));//代表篮球足球跑步
        PriorityTemplate t3_a = new PriorityTemplate(TemplateType.A.name(),t3_a_set);
        template_three.add(t3_a);
        //模板三的B级
        Set<Symptom> t3_b_set = new HashSet<>();
        t3_b_set.add(new Symptom(5,FootConst.FLATFOOT,new HashSet<>(Arrays.asList(1)),true));
        t3_b_set.add(new Symptom(6,FootConst.GENUVALGUS,new HashSet<>(Arrays.asList(1)),true));
        t3_b_set.add(new Symptom(4,FootConst.VALGUSDEFORMITY,new HashSet<>(Arrays.asList(1)),true));
        t3_b_set.add(new Symptom(5,FootConst.HIGHARCHEDFOOT,new HashSet<>(Arrays.asList(1)),true));
        t3_b_set.add(new Symptom(1,FootConst.HEELPAIN,new HashSet<>(Arrays.asList(1)),true));
        t3_b_set.add(new Symptom(3,FootConst.PLANTARFASCIITIS,new HashSet<>(Arrays.asList(1)),true));
        PriorityTemplate t3_b = new PriorityTemplate(TemplateType.B.name(),t3_b_set);
        template_three.add(t3_b);
        //模板三的C级
        Set<Symptom> t3_c_set = new HashSet<>();
        t3_c_set.add(new Symptom(5,FootConst.HIGHARCHEDFOOT,new HashSet<>(Arrays.asList(2,3)),true));
        t3_c_set.add(new Symptom(6,FootConst.GENUVALGUS,new HashSet<>(Arrays.asList(2,3)),true));
        t3_c_set.add(new Symptom(4,FootConst.VALGUSDEFORMITY,new HashSet<>(Arrays.asList(2,3)),true));
        t3_c_set.add(new Symptom(1,FootConst.HEELPAIN,new HashSet<>(Arrays.asList(2,3)),true));
        t3_c_set.add(new Symptom(1,FootConst.CALCANEALSPUR,new HashSet<>(Arrays.asList(1,2,3)),true));
        t3_c_set.add(new Symptom(1,FootConst.CALCANEALSPUR,new HashSet<>(Arrays.asList(1,2,3)),true));
        t3_a_set.add(new Symptom(2,FootConst.PURPOSE,new HashSet<>(Arrays.asList(3,5)),true,SymptomType.PURPOSE));//滑雪，骑行
        t3_c_set.add(new Symptom(1,FootConst.PLANTARFASCIITIS,new HashSet<>(Arrays.asList(2,3)),true));
        PriorityTemplate t3_c = new PriorityTemplate(TemplateType.C.name(),t3_c_set);
        template_three.add(t3_c);

        tempaltePrioryityMap.put(TemplateKeyType.T3.name(),template_three);
        //模板四
        //模板四的A级
        List<PriorityTemplate> template_four = new ArrayList<>();
        Set<Symptom> t4_a_set = new HashSet<>();
        t4_a_set.add(new Symptom(1,FootConst.BEARING,new HashSet<>(Arrays.asList(55)),true,SymptomType.BEARING,0));
        PriorityTemplate t4_a = new PriorityTemplate(TemplateType.A.name(),t4_a_set);
        template_four.add(t4_a);

        //模板四的B级
        Set<Symptom> t4_b_set = new HashSet<>();
        t4_b_set.add(new Symptom(3,FootConst.SUPINATION,new HashSet<>(Arrays.asList(1)),true));
        t4_b_set.add(new Symptom(4,FootConst.GENUVARUS,new HashSet<>(Arrays.asList(1)),true));
        t4_b_set.add(new Symptom(2,FootConst.VARUSDEFORMITY,new HashSet<>(Arrays.asList(1)),true));
        PriorityTemplate t4_b = new PriorityTemplate(TemplateType.B.name(),t4_b_set);
        template_four.add(t4_b);

        //模板四的C级
        Set<Symptom> t4_c_set = new HashSet<>();
        t4_c_set.add(new Symptom(1,FootConst.PURPOSE,new HashSet<>(Arrays.asList(6)),true,SymptomType.PURPOSE));//室内运动
        t4_c_set.add(new Symptom(3,FootConst.SUPINATION,new HashSet<>(Arrays.asList(2,3)),true));
        t4_c_set.add(new Symptom(4,FootConst.GENUVARUS,new HashSet<>(Arrays.asList(2,3)),true));
        t4_c_set.add(new Symptom(2,FootConst.VARUSDEFORMITY,new HashSet<>(Arrays.asList(2,3)),true));
        t4_c_set.add(new Symptom(1,FootConst.BEARING,new HashSet<>(Arrays.asList(60)),true,SymptomType.BEARING,0));
        PriorityTemplate t4_c = new PriorityTemplate(TemplateType.C.name(),t4_c_set);
        template_four.add(t4_c);

        tempaltePrioryityMap.put(TemplateKeyType.T4.name(),template_four);
    }
}

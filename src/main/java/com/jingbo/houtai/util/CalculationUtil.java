package com.jingbo.houtai.util;

import cn.hutool.core.bean.BeanUtil;
import com.jingbo.houtai.constParam.*;
import com.jingbo.houtai.entity.*;

import java.util.*;

/**
 * 计算脚的模板以及鞋子材料
 *
 * @author liufeng
 * @date 2020/8/21 0021 22:50
 */
public class CalculationUtil {


    public static Map CalculationTemplate(Patient patient) {
        Map<String, Map<String, String>> result = new HashMap<>();
        List<String> templateList = new ArrayList<>();
        templateList.add(TemplateKeyType.T1.name());
        templateList.add(TemplateKeyType.T2.name());
        templateList.add(TemplateKeyType.T3.name());
        templateList.add(TemplateKeyType.T4.name());
        for (String t : templateList) {
            Map<String, String> stringMap = calculationFootLevel(patient, t);
            result.put(t, stringMap);
        }
        return result;
    }

    public static Map<String, Set<String>> CalculationMaterial(Patient patient) {//计算材料
        Map<String, Set<String>> result = new HashMap<>();
        Map<String, Object> patientMap = BeanUtil.beanToMap(patient);
        List<Foot> footList = patient.getFoots();
        for (Foot foot : footList) {
            Set footResult = new HashSet();
            MaterialConfig.MaterialList.forEach(item -> {
                List<String> symptomList = item.getSymptomList();
                for (String symp : symptomList) {
                    if (FootConst.PURPOSE.equals(symp)) {//用途类单独处理
                        List<Integer> pur = item.getPur();
                        pur.forEach(index -> {
                            char c = (patient.getPurpose()).charAt(index);
                            if (!String.valueOf(c).equals("0")) { //说明选择了
                                footResult.add(item.getMaterialType());
                            }
                        });
                    } else {
                        Map<String, Object> footMap = BeanUtil.beanToMap(foot);
                        Integer o = (Integer) footMap.get(symp);
                        if (o != null && o > 0) { //说明选择了
                            footResult.add(item.getMaterialType());
                        }
                    }
                }
            });
            result.put(foot.getFootType(), footResult);
        }
        return result;
    }

    private static Map<String, String> calculationFootLevel(Patient patient, String templateType) {
        List<Foot> foots = patient.getFoots();
        Map<String, Object> patientMap = BeanUtil.beanToMap(patient);
        Map<String, String> templateMap = new HashMap<>();//L,A
        for (Foot foot : foots) {
            Map<String, Integer> LevelPriority = new HashMap<>();//用于存放 A，1  只存A里面最大的优先级
            Map<String, Object> footMap = BeanUtil.beanToMap(foot);
            List<PriorityTemplate> priorityTemplates = PriorityConfig.tempaltePrioryityMap.get(templateType);
            for (PriorityTemplate priorityTemplate : priorityTemplates) {
                Set<Symptom> symptomSet = priorityTemplate.getSymptom();
                for (Symptom symptom : symptomSet) {
                    String symptomName = symptom.getSymptomName();
                    Object symptomValue = footMap.get(symptomName);
                    if (symptomValue == null) {//说明不是foot属性
                        Object patientValue = patientMap.get(symptomName);
                        if (patientValue != null) { //这个是真正的选择的属性
                            if (symptomName.equals(FootConst.PURPOSE)) { //用途
                                Set<Integer> level = symptom.getLevel();
                                for (Integer i : level) {
                                    char c = ((String) patientValue).charAt(i);
                                    if (!String.valueOf(c).equals("0")) {//说明选择了
                                        Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                        if (exitPriority == null) {
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        }
                                    }
                                }
                            } else if (symptomName.equals(FootConst.WEIGHT)) {//体重、承重
                                Set<Integer> level = symptom.getLevel();
                                Integer lgtype = symptom.getLgtype();

                                ArrayList<Integer> level_list = new ArrayList<>(level);
                                if (lgtype.equals(0)) {//大于
                                    if ((Double) patientValue >= level_list.get(0)) {
                                        Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                        if (exitPriority == null) {
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        }
                                    }
                                } else if (lgtype.equals(1)) {//小于
                                    if ((Double) patientValue <= level_list.get(0)) {
                                        Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                        if (exitPriority == null) {
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        }
                                    }
                                } else {
                                    if ((Double) patientValue >= level_list.get(0) && (Double) patientValue <= level_list.get(1)) {
                                        Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                        if (exitPriority == null) {
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        }
                                    }
                                }
                            } else if (symptomName.equals(FootConst.SELFCHECK)) {
                                Set<Integer> level = symptom.getLevel();
                                for (Integer i : level) {
                                    if (String.valueOf(patientValue).equals(i)) {//说明选择了
                                        Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                        if (exitPriority == null) {
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                            LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                        }

                                    }
                                }
                            }
                        }
                    } else {//foot属性
                        if (symptomName.equals(FootConst.BEARING)) {
                            Set<Integer> level = symptom.getLevel();
                            Integer lgtype = symptom.getLgtype();

                            ArrayList<Integer> level_list = new ArrayList<>(level);
                            if (lgtype.equals(0)) {//大于
                                if ((Integer) symptomValue >= level_list.get(0)) {
                                    Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                    if (exitPriority == null) {
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    }
                                }
                            } else if (lgtype.equals(1)) {//小于
                                if ((Integer) symptomValue <= level_list.get(0)) {
                                    Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                    if (exitPriority == null) {
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    }
                                }
                            } else {
                                if ((Integer) symptomValue >= level_list.get(0) && (Integer) symptomValue <= level_list.get(1)) {
                                    Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                    if (exitPriority == null) {
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    }
                                }
                            }


                        } else {
                            if (0 != (Integer) symptomValue) {//不是默认的0 代表用户选择了
                                Set<Integer> level = symptom.getLevel();
                                if (level.contains(symptomValue)) {//说明确实在这个等级里面
                                    Integer exitPriority = LevelPriority.get(priorityTemplate.getType());
                                    if (exitPriority == null) {
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    } else if (exitPriority != null && exitPriority > symptom.getPriority()) { //存最大的优先级 值越小优先级越高
                                        LevelPriority.put(priorityTemplate.getType(), symptom.getPriority());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Integer prio = 100;
            String abc = "A";
            for (Map.Entry<String, Integer> entry : LevelPriority.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (value < prio) { //把最大优先级赋给prio
                    prio = value;
                    abc = key;
                }
            }
            templateMap.put(foot.getFootType(), abc);
        }

        return templateMap;
    }


    public static void main(String[] args) {
        String a = "123456";
        char c = a.charAt(4);
        System.out.println(c);
    }


}

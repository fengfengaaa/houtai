package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
* 机器价格
* @author liufeng
* @date 2020/9/9 0009 16:54
 */
@Getter
@Setter
public class UserPrice implements Serializable {
    private Long id;//主键id
    private Integer project;//项目 0篮球 1足球 2跑步 3滑雪 4高尔夫 5骑行 6室内运动 7舒适 8皮鞋 9异形加高
    @NotNull(message = "基础价格不能为空")
    private BigDecimal price;//价格
    @NotNull(message = "进阶价格不能为空")
    private BigDecimal advancedPrice;//进阶价格
    @NotNull(message = "用户id不能为空")
    private Integer userId;//对应的机器

    public UserPrice(Integer project, Integer userId) {
        this.project = project;
        this.userId = userId;
    }

    public UserPrice() {
    }
}

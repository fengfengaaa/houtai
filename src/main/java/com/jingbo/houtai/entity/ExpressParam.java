package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ExpressParam {
    @NotNull
    private Integer patientId;
    @NotBlank(message = "收件人姓名不能为空")
    private String addressee;//收件人
    @NotBlank(message = "收件人电话不能为空")
    private String addresseePhone;//收件人电话
    @NotBlank(message = "收件人地址不能为空")
    private String toAddress;//收件地址
    @NotNull
    private Integer makeStatus;//状态
    @NotBlank(message = "快递单号不能为空")
    private String expressNumber;//快递单号
    private String expressRemark;//订单备注
}

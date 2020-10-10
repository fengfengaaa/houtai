package com.jingbo.houtai.constParam;

public enum AccountTypeEnum {
    SUPERADMIN(0),//超级账号
    OPERATE(1),//运营账号
    FINANCE(2),//财务账号
    MAKE(3),//制作账号
    EXPRESS(4),//快递账号
    SERVICE(5),//服务商账号
    ORDINARYROOT(6),//普通root
    MAKESERVICE(7);//制作服务商账号
    private Integer type;
    AccountTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType(){
        return this.type;
    }
}

package com.jingbo.houtai.constParam;

public enum LoginType {
    WEB("Web"),  MOBILE("Mobile");
    private String type;
    private LoginType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return this.type.toString();
    }

    public String getType(){
        return this.type;
    }
}

package com.jingbo.houtai.util;

import com.jingbo.houtai.util.AESUtil;

public class TestAESUtil {
    public static void main(String[] args) {
        String password = "hahahda";
        String encrypt = AESUtil.encrypt(password);
        System.out.println("==>加密密码："+ encrypt);

        String decrypt = AESUtil.decrypt(encrypt);

        System.out.println("==>解密密码："+ decrypt);
    }
}

package com.itheima.utils;

import java.util.Random;

/**
 * @Program: Itcast_health
 * @ClassName: ValidateCodeUtils
 * @Description: 随机生成验证码工具类
 * @Author: KyleSun
 **/
public class ValidateCodeUtils {

    /**
     * @description: //TODO 随机生成验证码 长度为4位或者6位
     * @param: [length]
     * @return: java.lang.Integer
     */
    public static Integer generateValidateCode(int length) {
        Integer code = null;
        if (length == 4) {
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if (code < 1000) {
                code = code + 1000;//保证随机数为4位数字
            }
        } else if (length == 6) {
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if (code < 100000) {
                code = code + 100000;//保证随机数为6位数字
            }
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * @description: //TODO 随机生成指定长度字符串验证码
     * @param: [length]
     * @return: java.lang.String
     */
    public static String generateValidateCode4String(int length) {
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }
}

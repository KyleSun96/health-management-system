package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static com.itheima.utils.SMSUtils.VALIDATE_CODE;

/**
 * @Program: Itcast_health
 * @ClassName: validateCode
 * @Description: 验证码操作管理
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * @description: //TODO 发送验证码: 用户体检预约
     * @param: [telephone]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        System.out.println("validateCode: " + validateCode);
        Jedis resource = null;
        try {
            // 给用户发送验证码 --> SMSUtils.sendShortMessage(验证码模板code,用户电话号码,验证码)
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
             /*
                若发送成功,将验证码保存到 Redis（5分钟） --> setex(存储的key名,Redis过期时间,存储的value值)
                    为避免Redis中存储的key名重复,选择拼接: 用户的号码 + Redis存储短信类型
             */
            resource = jedisPool.getResource();
            resource.setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, validateCode.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }


    /**
     * @description: //TODO 发送验证码: 用户快速登录
     * @param: [telephone]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        // 随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        System.out.println("validateCode: " + validateCode);
        Jedis resource = null;
        try {
            // 给用户发送验证码 --> SMSUtils.sendShortMessage(验证码模板code,用户电话号码,验证码)
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
             /*   若发送成功,将验证码保存到 Redis（5分钟） --> setex(存储的key名,Redis过期时间,存储的value值)
                    为避免Redis中存储的key名重复,选择拼接: 用户的号码 + Redis存储短信类型
             */
            resource = jedisPool.getResource();
            resource.setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 300, validateCode.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }
}

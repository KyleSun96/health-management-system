package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Program: Itcast_health
 * @ClassName: OrderController
 * @Description: 体检预约相关服务
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;


    /**
     * @description: //TODO 微信在线体检预约
     * @param: [map]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, Object> map) {
        // 1. 获取用户传入的数据
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String orderDate = (String) map.get("orderDate");

        // 2. 获取Redis缓存中的数据
        Jedis resource = jedisPool.getResource();
        String validateCodeInRedis = resource.get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        // 3. 将用户输入的验证码和Redis中保存的验证码进行比对
        if (validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            // 3.1 如果验证码比对成功，调用服务完成预约业务处理
            //  3.1.1 设置预约类型，分为微信预约、电话预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {
                //  3.1.2 通过Dubbo远程调用服务 实现 在线预约业务处理
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "Dubbo远程调用服务错误,预约失败");
            }
            if (result.isFlag()) {
                //  3.1.3 上一步调用服务成功后，预约成功: 可以为用户发送短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
                } catch (ClientException e) {
                    e.printStackTrace();
                    return new Result(false, "发送短信异常,预约失败");
                }
            }
            return result;
        } else {
            // 3.2 如果验证码比对失败(错误或超时)，返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }


    /**
     * @description: //TODO 根据预约ID,查询预约相关信息(体检人,预约日期,套餐名称,预约类型)
     * @param: [id]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map<String, Object> map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}

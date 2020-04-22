package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Program: Itcast_health
 * @ClassName: MemberController
 * @Description: 会员相关管理
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;


    /**
     * @description: //TODO 手机号快速登录
     * @param: [response, map]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map<String, Object> map) {
        // 获取前台传入的数据
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        // 从Redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {
            // 验证码输入正确,根据手机号查询当前用户是否为会员（查询会员表来确定）
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                // 不是会员,自动完成注册,将当前用户信息保存到会员表
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            /*
             如果是会员,
                向客户端浏览器写入cookie,内容为手机号, 实际的生产代码里,cookie不会是明文,而是一个可以反向解密串
                向服务器端写入redis,因为各个mobile之间session不能共享,所以需要存到公共的 redis中,也是基于cookie技术
             */
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            /*
                设置为"/",代表访问"/"所有路径下的页面都会将cookie带过去
                设置为"/pages",代表访问"/pages"路径下的页面才会将cookie带过去
             */
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);

            // 将会员信息: java对象序列化为json串,保存到 Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60 * 30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            // 验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }


}

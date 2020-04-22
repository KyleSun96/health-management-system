package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Program: Itcast_health
 * @ClassName: MemberServiceImpl
 * @Description: 会员相关管理服务
 * @Author: KyleSun
 **/
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;


    /**
     * @description: //TODO 根据手机号查询当前用户是否为会员
     * @param: [telephone]
     * @return: com.itheima.pojo.Member
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }


    /**
     * @description: //TODO 注册会员,保存会员信息
     * @param: [member]
     * @return: void
     */
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            // 不能向数据库中存明文密码: 使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);

    }


}

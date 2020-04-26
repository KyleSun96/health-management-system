package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * @Program: Itcast_health
 * @InterfaceName: MemberService
 * @Description: 会员相关管理接口
 * @Author: KyleSun
 **/
public interface MemberService {

    // 根据手机号查询当前用户是否为会员
    Member findByTelephone(String telephone);

    // 注册会员,保存会员信息
    void add(Member member);

    // 根据月份查询会员数量
    List<Integer> findMemberCountByMonths(List<String> months);
}

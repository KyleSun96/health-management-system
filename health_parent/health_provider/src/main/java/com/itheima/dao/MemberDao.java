package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;

import java.util.List;

public interface MemberDao {

    // 查询所有会员
    public List<Member> findAll();

    // 根据条件查询
    public Page<Member> selectByCondition(String queryString);

    // 新增会员
    public void add(Member member);

    // 删除会员
    public void deleteById(Integer id);

    // 根据id查询会员
    public Member findById(Integer id);

    // 根据电话查询会员
    public Member findByTelephone(String telephone);

    // 编辑会员
    public void edit(Member member);

    // 根据日期统计会员数，统计指定日期之前的会员数
    public Integer findMemberCountBeforeDate(String date);

    // 根据日期统计会员数
    public Integer findMemberCountByDate(String date);

    // 根据日期统计会员数，统计指定日期之后的会员数
    public Integer findMemberCountAfterDate(String date);

    // 总会员数
    public Integer findMemberTotalCount();
}

package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @program: Itcast_health
 * @InterfaceName: CheckGroupDao
 * @description:
 **/
public interface CheckGroupDao {

    // 新增检查组_1_插入检查组数据
    void add(CheckGroup checkGroup);

    // 新增检查组_2_设置检查组和检查项的多对多的关联关系
    void setRelOfGroupAndItem(Map<String, Integer> map);

    // 根据条件查询(用于分页查询中)
    Page<CheckGroup> selectByCondition(String queryString);

    // 根据ID查询检查组
    CheckGroup findById(Integer checkgroupId);

    // 根据检查组ID查询,检查组关联多少检查项ID
    List<Integer> findRelOfGroupAndItem(Integer checkgroupId);

    // 修改检查组基本信息 t_checkgroup
    void edit(CheckGroup checkGroup);

    // 清理当前检查组与检查项的关联关系
    void deleteRelation(Integer checkgroupId);

    // 查询所有检查组
    List<CheckGroup> findAll();

    // 删除检查组
}

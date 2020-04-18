package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @program: Itcast_health
 * @InterfaceName: CheckItemDao
 * @description:
 **/
public interface CheckItemDao {

    // 新增检查项
    void add(CheckItem checkItem);

    // 根据条件查询检查项
    Page<CheckItem> selectByCondition(String queryString);

    // 根据检查项Id统计: 检查项和检查组关系表中 已经关联该检查项的数据量
    long findGroupCountById(Integer id);

    // 根据检查项Id删除该检查项
    void deleteById(Integer id);

    // 编辑检查项
    void edit(CheckItem checkItem);

    // 根据检查项Id查询数据
    CheckItem findById(Integer id);

    // 查询所有检查项
    List<CheckItem> findAll();

    // 根据检查组Id找出: 检查项和检查组关系表中 已经关联该检查组的项目
    List<CheckItem> listItemsBycheckGroupId(Integer id);
}

package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * @program: Itcast_health
 * @InterfaceName: CheckGroupService
 * @description: 检查组务接口
 **/
public interface CheckGroupService {

    // 新增检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    // 分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    // 根据ID查询检查组
    CheckGroup findById(Integer checkgroupId);

    // 根据检查组ID查询,检查组关联多少检查项ID
    List<Integer> findRelOfGroupAndItem(Integer checkgroupId);

    // 编辑检查组
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    // 查询所有检查组
    List<CheckGroup> findAll();

    // 删除检查组
}

package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;

/**
 * @program: Itcast_health
 * @InterfaceName: SetmealService
 * @description: 体检套餐服务接口
 * @author: KyleSun
 **/

public interface SetmealService {

    // 新增套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    // 分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    // 查询所有套餐
    List<Setmeal> findAll();

    // 根据套餐的ID查询详细信息(包括套餐基本信息,套餐包含的检查组信息,检查组包含的检查项信息)
    Setmeal findById(int id);
}
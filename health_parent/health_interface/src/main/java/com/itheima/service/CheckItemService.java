package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @program: Itcast_health
 * @InterfaceName: CheckItemService
 * @description: 检查项服务接口
 **/
public interface CheckItemService {

    // 新增检查项
    void add(CheckItem checkItem);

    // 检查项分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    // 删除检查项
    void deleteById(Integer id);

    // 编辑检查项
    void edit(CheckItem checkItem);

    // 根据检查项Id查询数据
    CheckItem findById(Integer id);

    // 查询所有检查项
    List<CheckItem> findAll();
}

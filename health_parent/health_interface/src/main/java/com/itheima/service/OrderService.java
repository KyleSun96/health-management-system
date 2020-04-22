package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * @Program: Itcast_health
 * @InterfaceName: OrderService
 * @Description: 体检预约服务接口
 * @Author: KyleSun
 **/
public interface OrderService {

    // 体检预约
    Result order(Map<String, Object> map) throws Exception;

    // 根据预约ID,查询预约相关信息(体检人,预约日期,套餐名称,预约类型)
    Map<String, Object> findById(Integer id) throws Exception;
}

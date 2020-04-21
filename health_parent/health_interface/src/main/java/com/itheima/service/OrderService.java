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
}

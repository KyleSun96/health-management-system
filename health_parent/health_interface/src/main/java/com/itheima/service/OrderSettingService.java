package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @program: Itcast_health
 * @InterfaceName: OrderSettingService
 * @description: 预约设置服务接口
 * @author: KyleSun
 **/
public interface OrderSettingService {

    // 数据批量导入到数据库
    void add(List<OrderSetting> orderSettings);

    // 根据月份查询该月份内所有的预约数据
    List<Map> getOrderSettingByMonth(String date);
}

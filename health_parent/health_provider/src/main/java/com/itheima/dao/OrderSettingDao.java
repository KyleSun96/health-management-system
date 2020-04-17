package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: Itcast_health
 * @InterfaceName: OrderSettingDao
 * @description:
 * @author: KyleSun
 **/
public interface OrderSettingDao {

    // 根据指定预约日期查询数据库是否已存在
    long findCountByOrderDate(Date orderDate);

    // 预约设置数据批量导入到数据库: 数据库已有设置 --> 更新
    void editNumberByOrderDate(OrderSetting orderSetting);

    // 预约设置数据批量导入到数据库: 数据库没有设置 --> 添加
    void add(OrderSetting orderSetting);

    // 根据月份查询该月份内所有的预约数据
    List<Map> getOrderSettingByMonth(String date);

}

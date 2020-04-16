package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @program: Itcast_health
 * @ClassName: OrderSettingServiceImpl
 * @description: 预约设置服务
 * @author: KyleSun
 **/
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    /**
     * @Description: //TODO 数据批量导入到数据库
     * @Param: [orderSettings]
     * @return: void
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        if (orderSettingDao != null && orderSettings.size() > 0) {
            for (OrderSetting orderSetting : orderSettings) {
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    // 数据库已有设置 --> 更新
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    // 数据库没有设置 --> 添加
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }


    /**
     * @Description: //TODO 根据月份查询该月份内所有的预约数据
     * @Param: [date]
     * @return: java.util.List<java.util.Map>
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) { // yyyy-MM
        return orderSettingDao.getOrderSettingByMonth(date);
    }
}

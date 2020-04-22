package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Program: Itcast_health
 * @ClassName: OrderServiceImpl
 * @Description: 体检预约服务
 * @Author: KyleSun
 **/
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    /**
     * @description: //TODO 体检预约
     * @param: [map]
     * @return: com.itheima.entity.Result
     * <p>
     * 1. 检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
     * 2. 检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
     * 3. 检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
     * 4. 检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
     * 5. 预约成功，更新当日的已预约人数
     */
    @Override
    public Result order(Map<String, Object> map) throws Exception {
        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null) {
            // 指定日期没有进行预约设置，无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            // 若可预约人数 >= 已预约人数,说明已经约满，无法预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        // 检查当前用户是否为会员,如果是会员则直接完成预约
        if (member != null) {
            // 判断是否在重复预约
            Integer memberId = member.getId();  // 会员的id
            Date order_Date = DateUtils.parseString2Date(orderDate);  // 预约日期
            String setmealId = (String) map.get("setmealId"); // 页面提交的套餐id
            // 根据以上条件进行查询,是否已经预约过
            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                // 说明用户正在重复预约，无法完成再次预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            // 4、检查当前用户是否为会员,如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            // 自动完成会员注册
            memberDao.add(member);
        }
        // 5、预约成功，更新当日的已预约人数
        // 新增预约信息
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        // 更新已预约人数
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        // 返回预约的id.用于跳转到预约成功的界面,展示预约信息
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());

    }


    /**
     * @description: //TODO 根据预约ID,查询预约相关信息(体检人,预约日期,套餐名称,预约类型)
     * @param: [id]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    @Override
    public Map<String, Object> findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}

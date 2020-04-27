package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Program: Itcast_health
 * @ClassName: ReportServiceImpl
 * @Description: 运营数据统计服务
 * @Author: KyleSun
 **/
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * @description: //TODO 查询获取运营数据
     * @param: []
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {

        Map<String, Object> data = new HashMap<>();

        // 获取今日日期
        String today = DateUtils.parseDate2String(new Date());
        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        // 本日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        // 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        // 本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        // 本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        // 本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        // 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        // 热门套餐查询
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        data.put("reportDate", today);
        data.put("todayNewMember", todayNewMember);
        data.put("totalMember", totalMember);
        data.put("thisWeekNewMember", thisWeekNewMember);
        data.put("thisMonthNewMember", thisMonthNewMember);
        data.put("todayOrderNumber", todayOrderNumber);
        data.put("thisWeekOrderNumber", thisWeekOrderNumber);
        data.put("thisMonthOrderNumber", thisMonthOrderNumber);
        data.put("todayVisitsNumber", todayVisitsNumber);
        data.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        data.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        data.put("hotSetmeal", hotSetmeal);

        return data;
    }
}

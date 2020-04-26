package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Program: Itcast_health
 * @ClassName: ReportController
 * @Description: 报表操作
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    /**
     * @description: //TODO 会员数量折线图数据
     * @param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport() {

        Map<String, Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();

        // 获得日历对象，模拟时间就是当前时间
        Calendar calendar = Calendar.getInstance();
        // 当前时间往前推12个月
        calendar.add(Calendar.MONTH, -12);
        // 计算过去一年的12个月
        for (int i = 0; i < 12; i++) {
            // 当前calendar往后推一个月
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months", months);

        // 根据月份查询会员数量
        List<Integer> memberCount = memberService.findMemberCountByMonths(months);
        map.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }
}

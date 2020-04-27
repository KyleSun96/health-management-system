package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Program: Itcast_health
 * @ClassName: ReportController
 * @Description: 报表操作管理
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;

    /**
     * @description: //TODO 会员数量--折线图
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


    /**
     * @description: //TODO 套餐预约占比--饼形图
     * @param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport() {

        /*
            分析需要的数据,构造对应的容器

            "data":{
                    "setmealNames":['套餐1','套餐2','套餐3'],
                    "setmealCount":[
                        {"name":"套餐1","value":10},
                        {"name":"套餐2","value":20},
                        {"name":"套餐3","value":30}
                    ]
                  }

            重复数据,查询一次setmealCount,setmealNames再从setmealCount获取即可
         */

        // "data"
        Map<String, Object> data = new HashMap<>();

        try {
            // "setmealCount" --> 查询套餐预约占比数据
            List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
            data.put("setmealCount", setmealCount);

            // "setmealNames"
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }
            data.put("setmealNames", setmealNames);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

}

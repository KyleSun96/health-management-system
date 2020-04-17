package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: Itcast_health
 * @ClassName: OrderSettingController
 * @description: 预约设置管理
 * @author: KyleSun
 **/
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    /**
     * @Description: //TODO 上传文件,并将数据批量导入到数据库
     * @Param: [multipartFile]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("excelFile") MultipartFile multipartFile) {
        try {
            List<String[]> list = POIUtils.readExcel(multipartFile);
            if (list != null && list.size() > 0) {
                ArrayList<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] strings : list) {
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.valueOf(strings[1]));
                    orderSettings.add(orderSetting);
                }
                // 通过dubbo远程调用服务实现数据 批量导入到数据库
                orderSettingService.add(orderSettings);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    /**
     * @Description: //TODO 根据月份查询该月份内所有的预约数据
     * @Param: [date]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(String date) { // 从前端接受的date格式为 : yyyy-MM
        try {
            /*
                this.leftobj = [
                    { date: 1, number: 120, reservations: 1 },
                    { date: 3, number: 120, reservations: 119 },
                    { date: 4, number: 120, reservations: 120 },
                    { date: 6, number: 120, reservations: 1 },
                    { date: 8, number: 120, reservations: 1 }
                              ];
                 前台数据为数组类型的json,分析页面数据的结构,
                 可知我们使用 List<Map> 将每天的数据放入map中,再将每天放入list中 较好
             */
            List<Map> leftobjList = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, leftobjList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_FAIL);
    }


    /**
     * @Description: //TODO 根据日期设置最大可预约人数
     * @Param: [orderSetting]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        // 由springmvc.xml中的消息转换器 将从前台接收到的json类型数据,转换为Date的orderDate
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}

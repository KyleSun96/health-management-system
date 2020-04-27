package com.itheima.service;

import java.util.Map;

/**
 * @Program: Itcast_health
 * @InterfaceName: ReportService
 * @Description: 运营数据接口
 * @Author: KyleSun
 **/
public interface ReportService {

    // 查询获取运营数据
    Map<String, Object> getBusinessReportData() throws Exception;
}

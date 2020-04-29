package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
    @Reference
    private ReportService reportService;

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


    /**
     * @description: //TODO 运营数据统计
     * @param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/getBusinessReportData.do")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> data = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, data);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    /**
     * @description: //TODO 导出运营数据
     * @param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/exportBusinessReport.do")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            // 取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            // 先获取template文件夹的真实路径,再拼接上文件名
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            // 基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            // 读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);

            // 获得第三行,第六个单元格
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            // 热门套餐信息从第13行开始
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");

                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            /*
                使用输出流进行表格下载,基于浏览器作为客户端下载

                此处若使用new OutputStream --> 会在backend服务器中进行输出,即本地IO
                     使用response.getOutputStream --> 从响应对象中获取输出,才能向客户端进行输出,即网络IO
             */
            OutputStream out = response.getOutputStream();
            // 代表的是Excel文件类型
            response.setContentType("application/vnd.ms-excel");
            // 指定以附件形式进行下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            // 将 Excel以流读入内存中,再写入到输出流 out
            excel.write(out);
            out.flush();
            out.close();
            excel.close();

            return null;
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    /**
     * @description: //TODO 导出运营数据到PDF文件并提供客户端下载
     * @param: [request, response]
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //动态获取pdf模板文件绝对磁盘路径
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business.jasper";

            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperPath, result,
                            new JRBeanCollectionDataSource(hotSetmeal));

            //创建输出流，用于从服务器写数据到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            out.flush();
            out.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}

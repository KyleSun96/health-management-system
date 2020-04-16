package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: Itcast_health
 * @ClassName: POITest
 * @description: Apache POI功能测试, 与本项目主体无关
 * @author: KyleSun
 **/
public class POITest {

    /**
     * @Description: //TODO 使用POI读取Excel表格中的数据 -- 此方法企业中使用较少
     * @Param: []
     * @return: void
     */
    @Test
    public void test01() throws IOException {
        XSSFWorkbook excel = null;
        try {
            //加载指定文件 创建一个工作簿: excel对象
            excel = new XSSFWorkbook(this.getClass().getResource("/POITest.xlsx").getPath());
            // 读取excel文件的第一页: sheet1
            XSSFSheet sheet = excel.getSheetAt(0);
            // 遍历第一页,获得第一行
            for (Row row : sheet) {
                // 遍历行,获得每个单元格对象
                for (Cell cell : row) {
                    // 获取单元格对象的值
                    System.out.println(cell.getStringCellValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excel != null) {
                excel.close();
            }
        }
    }


    /**
     * @Description: //TODO 使用POI读取Excel表格中的数据 -- 此方法企业中使用多一些
     * @Param: []
     * @return: void
     */
    @Test
    public void test02() throws IOException {
        XSSFWorkbook excel = null;
        try {
            //加载指定文件 创建一个工作簿: excel对象
            excel = new XSSFWorkbook(this.getClass().getResource("/POITest.xlsx").getPath());
            // 读取excel文件的第一页: sheet1
            XSSFSheet sheet = excel.getSheetAt(0);
            // 获得当前工作表中最后一行的行号,行号索引从 0 开始
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
                // 获得当前行
                XSSFRow row = sheet.getRow(i);
                // 获得当前行最后一个单元格的索引,索引从 0 开始
                short lastCellNum = row.getLastCellNum();
                for (int j = 0; j < lastCellNum; j++) {
                    // 获得单元格对象
                    XSSFCell cell = row.getCell(j);
                    System.out.println(cell.getStringCellValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excel != null) {
                excel.close();
            }
        }
            /*
             此处表格有两行两列:
             由源码可知:
                lastRowNum = 1      --> 0,1 两行
                lastCellNum = 2     --> 0,1 两行 + 1 --> 1,2
                因此遍历时:
                    i <= lastRowNum
                    j < lastCellNum
            */
    }


    /**
     * @Description: //TODO 使用POI向Excel表格中的写入数据,并且通过输出流将创建的Excel文件保存到本地磁盘
     * @Param: []
     * @return: void
     */
    @Test
    public void test03() throws IOException {
        // 内存中创建一个工作簿
        XSSFWorkbook excel = new XSSFWorkbook();
        FileOutputStream os = null;
        try {
            XSSFSheet sheet = excel.createSheet("测试表单1");

            XSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("姓名");
            row1.createCell(1).setCellValue("成绩");
            row1.createCell(2).setCellValue("地址");

            XSSFRow row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("张三");
            row2.createCell(1).setCellValue("100");
            row2.createCell(2).setCellValue("北京");

            XSSFRow row3 = sheet.createRow(2);
            row3.createCell(0).setCellValue("李四");
            row3.createCell(1).setCellValue("99");
            row3.createCell(2).setCellValue("南京");

            // 指定文件输出的路径
            String path = this.getClass().getResource("/").getPath();

            // 创建一个输出流,通过输出流将内存中的excel文件写到磁盘的指定位置
            os = new FileOutputStream(new File(path + "POITest2.xlsx"));
            excel.write(os);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
            excel.close();
        }
    }
}

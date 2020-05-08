package com.fcant.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * ReasExcel
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 19:43 2020/5/8/0008
 */
public class ReadExcel {

    /**
     * 获取名单Excel
     *
     * @param filePath 名单Excel的文件路径
     * @return Workbook 返回Excel的对象
     * @author Fcant 上午 7:37 2020/5/8/0008
     */
    public Workbook getExcel(String filePath) {
        Workbook wb = null;
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("文件不存在");
            wb = null;
        } else {
            String fileType = filePath.substring(filePath.lastIndexOf("."));//获得后缀名
            try {
                InputStream is = new FileInputStream(filePath);
                if (".xls".equals(fileType)) {
                    wb = new HSSFWorkbook(is);
                } else if (".xlsx".equals(fileType)) {
                    wb = new XSSFWorkbook(is);
                } else {
                    System.out.println("格式不正确");
                    wb = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wb;
    }
}

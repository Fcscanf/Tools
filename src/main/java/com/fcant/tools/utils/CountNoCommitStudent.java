package com.fcant.tools.utils;

import com.fcant.tools.bean.Student;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * CountNoCommitStudent
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 23:02 2020/5/7/0007
 */
public class CountNoCommitStudent {

    /**
     * 名单数据路径
     */
    private static final String filePath = "C:\\Users\\fcsca\\OneDrive\\文档\\Excel\\软件161\\B软件161个人身份信息1.xlsx";

    /**
     * 对比查找的目录路径
     */
    public static final String dirPath = "D:\\LinkSpace\\Work\\WorkFiles\\B软件161\\B软件161-91job就业调查截图";

    /**
     * 文件名的分隔符
     */
    public static final String splitString = "-";

    /**
     * 学生名单集合
     */
    private static final List<Student> studentList = new ArrayList<>();

    /**
     * 保存提交文件的学生的学号
     */
    private static final List<String> commitFileStudentNumList = new ArrayList<>();

    public static void main(String[] args) {

        CountNoCommitStudent excelTest = new CountNoCommitStudent();

        Workbook wb = excelTest.getExcel(filePath);

        if (wb == null)
            System.out.println("文件读入出错");
        else {
            excelTest.getExcelData(wb);
        }
        getCommitFileStudentNum(dirPath);
        File file = new File(dirPath);
        System.out.println(file.getName() + "---------未完成名单");
        studentList.stream().forEach(student -> {
            if (!commitFileStudentNumList.contains(student.getStudentNum())) {
                System.out.println(student);
            }
        });
    }

    public static void getCommitFileStudentNum(String dirPath) {
        File file = new File(dirPath);
        Collection<File> files = FileUtils.listFiles(file, null, false);
        files.stream().forEach(f -> {
            String fileName = f.getName().toString();
            int i = fileName.lastIndexOf(".");
            String commitFileName = fileName.substring(0, i);
            String[] splitFileName = commitFileName.split(splitString);
            for (String s : splitFileName) {
                if (s.length() == 10) {
                    commitFileStudentNumList.add(s);
                }
            }
        });
    }

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

    public void getExcelData(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);//读取sheet(从0计数)
        int rowNum = sheet.getLastRowNum();//读取行数(从0计数)
        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);//获得行
            int colNum = row.getLastCellNum();//获得当前行的列数
            Student student = new Student();
            for (int j = 0; j < colNum; j++) {
                Cell cell = row.getCell(j);//获取单元格
                if (j == 0) {
                    String stuNum = cell.toString();
                    String newNum = "1" + stuNum.substring(2, 11);
                    student.setStudentNum(newNum.replaceAll("E", "0"));
                } else {
                    student.setStudentName(cell.toString());
                }
            }
            studentList.add(student);
        }
    }

}

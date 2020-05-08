package com.fcant.tools.utils;

import com.fcant.tools.bean.Student;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

/**
 * CompareTwoExcel
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 19:33 2020/5/8/0008
 */
public class CompareTwoExcel extends InitLoadStudentDataInfo {

    public static void main(String[] args) {
        getNoReportStudent();
    }

    /**
     * 获取未上报的学生名单
     *
     * @author Fcant 下午 20:51 2020/5/8/0008
     */
    public static void getNoReportStudent() {
        List<Student> studentsWorkedAll = readStudentCompanyInfo("D:\\LinkSpace\\Work\\WorkFiles\\B软件161\\信息学院\\就业名单.xlsx");
        List<Student> studentsWebReportAll = readStudentCompanyInfo("D:\\LinkSpace\\Work\\WorkFiles\\B软件161\\信息学院\\就业方案导出.xlsx");
        List<Student> studentsWorkedSoftware = new ArrayList<>();
        List<String> studentsWebReportSoftware = new ArrayList<>();
        initStudentInfo();
        System.out.println("B软件161工作学生名单：");
        studentsWorkedAll.stream().forEach(student -> {
            if (STUDENT_NAME_MAP.containsKey(student.getStudentName())) {
                studentsWorkedSoftware.add(student);
                System.out.println("姓名：" + student.getStudentName() + ", 公司：" + student.getWorkCompany());
            }
        });
        System.out.println("工作人数总计：" + studentsWorkedSoftware.size());
        System.out.println("\n--------------------------");
        System.out.println("B软件161在91JOB上报学生名单：");
        studentsWebReportAll.stream().forEach(student -> {
            if (STUDENT_NAME_MAP.containsKey(student.getStudentName())) {
                studentsWebReportSoftware.add(student.getStudentName());
                System.out.println("姓名：" + student.getStudentName() + ", 公司：" + student.getWorkCompany());
            }
        });
        System.out.println("91JOB上报人数总计：" + studentsWebReportSoftware.size());
        System.out.println("--------------------------\n");
        System.out.println("B软件161未上报学生名单：");
        studentsWorkedSoftware.stream().forEach(student -> {
            if (!studentsWebReportSoftware.contains(student.getStudentName())) {
                System.out.println("姓名：" + student.getStudentName() + ", 公司：" + student.getWorkCompany());
            }
        });

    }

    /**
     * 读取Excel中学生以及公司信息
     *
     * @return List<Student>
     * @author Fcant 下午 20:16 2020/5/8/0008
     */
    public static List<Student> readStudentCompanyInfo(String excelPath) {
        Workbook wb = readExcel.getExcel(excelPath);
        List<Student> studentCompany = new ArrayList<>();
        if (wb == null)
            System.out.println("文件读入出错");
        else {
            Sheet sheet = wb.getSheetAt(0);//读取sheet(从0计数)
            int rowNum = sheet.getLastRowNum();//读取行数(从0计数)
            for (int i = 0; i <= rowNum; i++) {
                Row row = sheet.getRow(i);//获得行
                int colNum = row.getLastCellNum();//获得当前行的列数
                Student student = new Student();
                for (int j = 0; j < colNum; j++) {
                    Cell cell = row.getCell(j);//获取单元格
                    if (j == 0) {
                        student.setStudentName(cell.toString());
                    } else {
                        student.setWorkCompany(cell.toString());
                    }
                }
                studentCompany.add(student);
            }
        }
        return studentCompany;
    }
}

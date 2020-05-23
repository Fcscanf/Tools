package com.fcant.tools.service.studentfilecount;

import com.fcant.tools.bean.Student;
import com.fcant.tools.utils.ReadExcel;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InitLoadStudentDataInfo
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 23:02 2020/5/7/0007
 */
@Data
public class InitLoadStudentDataInfo {

    /**
     * 名单数据路径
     */
    private static final String filePath = "C:\\Users\\fcsca\\OneDrive\\文档\\Excel\\软件161\\B软件161学号姓名名单表.xlsx";

    /**
     * 学生名单集合
     */
    public static final List<Student> STUDENT_LIST = new ArrayList<>();

    /**
     * 学生学号集合
     */
    public static final List<String> STUDENT_NUM_LIST = new ArrayList<>();

    /**
     * 学生学号为key，学生对象为值的学生表
     */
    public static final Map<String, Student> STUDENT_NUM_MAP = new HashMap<>();

    /**
     * 学生姓名为key，学生对象为值的学生表
     */
    public static final Map<String, Student> STUDENT_NAME_MAP = new HashMap<>();

    public static final ReadExcel readExcel = new ReadExcel();

    /**
     * 项目运行主程序
     *
     * @param args 参数
     * @author Fcant 上午 7:36 2020/5/8/0008
     */
    public static void main(String[] args) {
        initStudentInfo();
    }

    public static void initStudentInfo() {
        getExcelStudentInfoData(filePath);
    }

    /**
     * 获取Excel中的数据内容
     *
     * @param excelPath Excel文件路径
     * @author Fcant 上午 7:38 2020/5/8/0008
     */
    public static void getExcelStudentInfoData(String excelPath) {
        Workbook wb = readExcel.getExcel(excelPath);
        List<Student> studentCompany = new ArrayList<>();
        if (wb == null)
            System.out.println("文件读入出错");
        else {
            //读取sheet(从0计数)
            Sheet sheet = wb.getSheetAt(0);
            //读取行数(从0计数)
            int rowNum = sheet.getLastRowNum();
            for (int i = 0; i <= rowNum; i++) {
                //获得行
                Row row = sheet.getRow(i);
                //获得当前行的列数
                int colNum = row.getLastCellNum();
                Student student = new Student();
                for (int j = 0; j < colNum; j++) {
                    //获取单元格
                    Cell cell = row.getCell(j);
                    if (j == 0) {
                        String stuNum = cell.toString();
                        String newNum = "1" + stuNum.substring(2, 11);
                        student.setStudentNum(newNum.replaceAll("E", "0"));
                    } else {
                        student.setStudentName(cell.toString());
                    }
                }
                STUDENT_NUM_LIST.add(student.getStudentNum());
                STUDENT_NUM_MAP.put(student.getStudentNum(), student);
                STUDENT_NAME_MAP.put(student.getStudentName(), student);
                STUDENT_LIST.add(student);
            }
        }
    }
}

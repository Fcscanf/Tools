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
    private static final String filePath = "C:\\Users\\fcsca\\OneDrive\\文档\\Excel\\软件161\\B软件161学号姓名名单表.xlsx";

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
     * 学生学号集合
     */
    private static final List<String> studentNumList = new ArrayList<>();

    /**
     * 学生学号为key，学生对象为值的学生表
     */
    public static final Map<String, Student> STUDENT_NUM_MAP = new HashMap<>();

    /**
     * 学生姓名为key，学生对象为值的学生表
     */
    public static final Map<String, Student> STUDENT_NAME_MAP = new HashMap<>();

    /**
     * 保存提交文件的学生的学号
     */
    private static final List<String> commitFileStudentNumList = new ArrayList<>();

    /**
     * 保存提交文件的学生的姓名
     */
    private static final List<String> commitFileStudentNameList = new ArrayList<>();

    /**
     * 项目运行主程序
     *
     * @param args 参数
     * @author Fcant 上午 7:36 2020/5/8/0008
     */
    public static void main(String[] args) {

        CountNoCommitStudent excelRead = new CountNoCommitStudent();

        Workbook wb = excelRead.getExcel(filePath);

        if (wb == null)
            System.out.println("文件读入出错");
        else {
            excelRead.getExcelData(wb);
        }
        getCommitFileStudent(dirPath, true);
        getNoCommitFileStudent();
    }

    /**
     * 获取未提交文件的学生名单
     *
     * @author Fcant 上午 9:35 2020/5/8/0008
     */
    public static void getNoCommitFileStudent() {
        File file = new File(dirPath);
        System.out.println(file.getName() + "---------未完成名单");
        studentList.stream().forEach(student -> {
            boolean num = commitFileStudentNumList.contains(student.getStudentNum());
            boolean name = commitFileStudentNameList.contains(student.getStudentName());
            boolean b = name || num;
            if (!b) {
                System.out.println("学号：" + student.getStudentNum() + ", 姓名：" + student.getStudentName());
            }
        });
    }

    /**
     * 根据目录获取提交材料的学生名单
     *
     * @param dirPath 材料存放路径
     * @param isReName 是否重命名
     * @author Fcant 上午 7:36 2020/5/8/0008
     */
    public static void getCommitFileStudent(String dirPath, boolean isReName) {
        File file = new File(dirPath);
        Collection<File> files = FileUtils.listFiles(file, null, false);
        files.stream().forEach(f -> {
            String fileName = f.getName().toString();
            int i = fileName.lastIndexOf(".");
            String commitFileName = fileName.substring(0, i);
            String[] splitFileName = commitFileName.split(splitString);
            for (String spliceFileName : splitFileName) {
                String trimFileName = spliceFileName.trim();
                if (trimFileName.length() == 10) {
                    commitFileStudentNumList.add(trimFileName);
                    if (isReName) {
                        Student student = STUDENT_NUM_MAP.get(trimFileName);
                        renameFileName(f, student);
                    }
                } else if (trimFileName.length() == 2 || trimFileName.length() == 3) {
                    commitFileStudentNameList.add(trimFileName);
                    if (isReName) {
                        Student student = STUDENT_NAME_MAP.get(trimFileName);
                        renameFileName(f, student);
                    }
                }
            }
        });
    }

    public static void renameFileName(File f, Student student) {
        String fileType = f.getName().substring(f.getName().lastIndexOf("."));
        String newFileName = "B软件161-" + student.getStudentNum() + "-" + student.getStudentName() + fileType;
        if (!newFileName.equals(f.getName())) {
            f.renameTo(new File(f.getParent() + "/" + newFileName));
        }
    }

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

    /**
     * 获取Excel中的数据内容
     *
     * @param wb Excel对象
     * @author Fcant 上午 7:38 2020/5/8/0008
     */
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
            studentNumList.add(student.getStudentNum());
            STUDENT_NUM_MAP.put(student.getStudentNum(), student);
            STUDENT_NAME_MAP.put(student.getStudentName(), student);
            studentList.add(student);
        }
    }

}

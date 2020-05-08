package com.fcant.tools.utils;

import com.fcant.tools.bean.Student;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * StudentFileCommitCount-加载名单表，查看对应的目录下的缺少未交的名单和文件统一规范命名
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 23:40 2020/5/8/0008
 */
public class StudentFileCommitCount extends InitLoadStudentDataInfo {

    /**
     * 保存提交文件的学生的学号
     */
    private static final List<String> commitFileStudentNumList = new ArrayList<>();

    /**
     * 保存提交文件的学生的姓名
     */
    private static final List<String> commitFileStudentNameList = new ArrayList<>();

    /**
     * 对比查找的目录路径
     */
    private static final String dirPath = "D:\\LinkSpace\\Work\\WorkFiles\\B软件161\\B软件161-91job就业调查截图";

    public static void main(String[] args) {
        initStudentInfo();
        getCommitFileStudent(dirPath, false);
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
        STUDENT_LIST.stream().forEach(student -> {
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
            String[] splitFileName = new String[10];
            if (commitFileName.contains("-")) {
                splitFileName = LongStringToArrayUtil.stringSplit(commitFileName, "-");
            } else {
                splitFileName = LongStringToArrayUtil.stringSplit(commitFileName, "_");
            }
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
}

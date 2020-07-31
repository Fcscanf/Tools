package com.fcant.tools.service.nullfilescan;

import java.io.File;
import java.util.Arrays;

/**
 * MavenRepoNullFileScan
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 15:21 2020/7/31/0031
 */
public class MavenRepoNullFileScan {

    public static final String MAVEN_REPO_PATH_USER = "C:\\Users\\fcsca\\.m2\\repository";
    public static final String MAVEN_REPO_PATH = "D:\\LinkSpace\\Download\\DevelopPackage\\Maven\\apache-maven-3.6.1-bin\\apache-maven-3.6.1\\repo";

    public static void main(String[] args) {
        File file = new File(MAVEN_REPO_PATH_USER);
        checkFile(file);
    }

    public static void checkFile(File file) {
        if (file.exists()) {
            String fileName = file.getName();
            if (fileName.contains(".")) {
                taskFile(file);
            }

        }
        if (file.isDirectory()) {
            if (file.list().length > 0) {
                Arrays.stream(file.listFiles()).forEach(f -> checkFile(f));
            }

        }
    }

    public static void taskFile(File file) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("lastUpdated".equals(fileType)) {
            file.delete();
            String parent = file.getParent();
            File filePath = new File(parent);
            boolean flag = false;
            for (String f : filePath.list()) {
                String fType = f.substring(f.lastIndexOf(".") + 1);
                if ("jar".equals(fType)) {
                    flag = true;
                }
            }
            if (!flag) {
                filePath.delete();
            }
        }
    }

}

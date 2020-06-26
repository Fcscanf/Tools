package com.fcant.tools.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * LogToFile
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 上午 9:14 2020/5/24/0024
 */
public class LogToFile {

    /**
     * 将检测结果保存到HTML文件
     *
     * @param urlValue url
     * @param isConnect 连接结果
     * @param isDir 是否为书签目录
     * @author Fcant 上午 10:28 2020/5/23/0023
     */
    public static void saveResultToHTMLFile(String urlValue, boolean isConnect, boolean isDir) {
        File file = new File("./BookMarkURL.html");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isDir) {
            urlValue = "<h3 style=\"color:blue\">" + urlValue + "</h3>";
        }else if (isConnect) {
            urlValue = "<p><a style=\"color:green\" href=\"" + urlValue + "\">" + urlValue + "</a></p>";
        } else {
            urlValue = "<p><a style=\"color:red\" href=\"" + urlValue + "\">" + urlValue + "</a></p>";
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("./BookMarkURL.html", true);
            fileOutputStream.write(urlValue.getBytes());
            fileOutputStream.write("\r\n".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

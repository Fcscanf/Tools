package com.fcant.tools.service.bookmark;

import com.fcant.tools.utils.LongStringToArrayUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;

/**
 * BrowserBookMark-检测浏览器书签的有效性
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 上午 8:34 2020/5/22/0022
 */
public class BrowserBookMark {

    /**
     * 书签文件的路径
     */
    public static final String bookMarkFilePath = "D:\\LinkSpace\\Download\\Browser\\bookmarks_2020_5_23.html";

    /**
     * 书签目录的标记
     */
    public static final String bookMarkDir = "<DT><H3";

    /**
     * 书签条目的标记
     */
    public static final String bookMarkItem = "<DT><A";

    /**
     * 书签文件夹的结束标记
     */
    public static final String endPathTag = "</DL><p>";

    /**
     * 书签的目录的路径
     */
    public static String bookMarkRealPath = "";

    private static int NOT_USE_URL_NUM = 0;
    private static int RUN_URL_NUM = 0;
    private static int ALL_URL_NUM = 0;
    private static int ALL_BOOKMARK_DIR_NUM = 0;
    private static int FILTER_BOOKMARK_URL_NUM = 0;

    /**
     * 定义不访问过滤的URL的关键字
     */
    public static String[] IGNORE_URL_KEY = new String[]{
            "google",
            "github",
            "getbootstrap",
            "leetcode",
            "ycit",
            "javascript",
            "pdf",
            "aliyun",
            "huawei",
            "oracle",
            "spring"
    };


    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();
        getBookMarkAndCheck(bookMarkFilePath);
        System.out.println("------------完成扫描-----------");
        saveResultToFile("------------完成扫描-----------", false, true);
        System.out.println("有效的URL：" + RUN_URL_NUM + "个");
        saveResultToFile("有效的URL：" + RUN_URL_NUM + "个", false, true);
        System.out.println("无效的URL：" + NOT_USE_URL_NUM + "个");
        saveResultToFile("无效的URL：" + NOT_USE_URL_NUM + "个", false, true);
        System.out.println("过滤的URL：" + FILTER_BOOKMARK_URL_NUM + "个");
        saveResultToFile("过滤的URL：" + FILTER_BOOKMARK_URL_NUM + "个", false, true);
        System.out.println("一共有：" + ALL_URL_NUM + " 个URL");
        saveResultToFile("一共有：" + ALL_URL_NUM + " 个URL", false, true);
        System.out.println("一共有：" + ALL_BOOKMARK_DIR_NUM + " 个文件夹");
        saveResultToFile("一共有：" + ALL_BOOKMARK_DIR_NUM + " 个文件夹", false, true);
        Instant end = Instant.now();
        System.out.println("耗费时间为：" + Duration.between(start, end).toMinutes() + "分钟");
        saveResultToFile("耗费时间为：" + Duration.between(start, end).toMinutes() + "分钟", false, true);
    }

    /**
     * 获取书签文件并测试URL的连通性
     *
     * @param bookMarkFilePath 书签文件的路径
     * @exception IOException 文件读取异常
     * @author Fcant 上午 5:48 2020/5/23/0023
     */
    public static void getBookMarkAndCheck(String bookMarkFilePath) throws IOException {
        FileReader bookMarkFileReader = new FileReader(bookMarkFilePath);
        BufferedReader bufferedReader = new BufferedReader(bookMarkFileReader);
        String lineContent;
        while ((lineContent = bufferedReader.readLine()) != null) {
            getBookMarkItemPath(lineContent.trim());
            if (lineContent.contains(bookMarkItem)) {
                getBookMarkURLToConnect(lineContent);
            }
        }
    }

    /**
     * 获取书签的文件夹名称
     *
     * @param lineContent 解析目录行打印文件夹名称
     * @author Fcant 上午 5:47 2020/5/23/0023
     */
    public static void getBookMarkItemPath(String lineContent) {
        if (lineContent.contains(bookMarkDir)) {
            int last_modified = lineContent.indexOf("LAST_MODIFIED");
            String bookMarkPathTag = lineContent.substring(last_modified + 27);
            if (bookMarkPathTag.contains("true")) {
                bookMarkRealPath += "/" + bookMarkPathTag.substring(31, bookMarkPathTag.length() - 5);
            } else {
                bookMarkRealPath += "/" + bookMarkPathTag.substring(0, bookMarkPathTag.length() - 5);
            }
            System.out.println(bookMarkRealPath);
            ALL_BOOKMARK_DIR_NUM++;
            saveResultToFile(bookMarkRealPath, false, true);
        } else if (lineContent.contains(endPathTag)) {
            int lastIndexOf = bookMarkRealPath.lastIndexOf("/");
            bookMarkRealPath = bookMarkRealPath.substring(0, lastIndexOf);
            System.out.println(bookMarkRealPath);
            ALL_BOOKMARK_DIR_NUM++;
            saveResultToFile(bookMarkRealPath, false, true);
        }
    }

    /**
     * 解析书签的行内容，判断URL并连接
     *
     * @param lineContent 书签的行文本
     * @author Fcant 上午 5:46 2020/5/23/0023
     */
    public static void getBookMarkURLToConnect(String lineContent) {
        String[] lineSplice = LongStringToArrayUtil.stringSplit(lineContent, " ");
        for (String s : lineSplice) {
            if (s.contains("HREF")) {
                ALL_URL_NUM++;
                String realUrl = s.substring(6, s.length() - 1);
                if (filterUrl(realUrl) == 0) {
                    urlConnect(realUrl);
                    /*if (!openUrlByHttpClient(realUrl)) {
                        System.out.println("链接打不开：" + realUrl);
                    }*/
                } else {
                    FILTER_BOOKMARK_URL_NUM++;
                }
            }
        }
    }

    /**
     * 将检测结果保存到HTML文件
     *
     * @param urlValue url
     * @param isConnect 连接结果
     * @param isDir 是否为书签目录
     * @author Fcant 上午 10:28 2020/5/23/0023
     */
    public static void saveResultToFile(String urlValue, boolean isConnect, boolean isDir) {
        File file = new File("./URL.html");
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
            fileOutputStream = new FileOutputStream("./URL.html", true);
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

    /**
     * 判断URL中的字符在定义的过滤规则中存在的次数
     *
     * @param url URL
     * @return int
     * @author Fcant 上午 5:45 2020/5/23/0023
     */
    public static int filterUrl(String url) {
        int count = 0;
        for (int i = 0; i < IGNORE_URL_KEY.length; i++) {
            if (url.contains(IGNORE_URL_KEY[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * 使用Java底层Socket连接URL
     *
     * @param urlValue URL
     * @author Fcant 上午 5:44 2020/5/23/0023
     */
    public static void urlConnect(String urlValue) {
        URL url;
        try {
            url = new URL(urlValue);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(300);
            urlConnection.connect();
            System.out.println("CurrentURL：" + urlValue);
            RUN_URL_NUM++;
            saveResultToFile(urlValue, true, false);
        } catch (Exception e1) {
            url = null;
            System.out.println("链接打不开：" + urlValue);
            NOT_USE_URL_NUM++;
            saveResultToFile(urlValue, false, false);
        }
    }

    /**
     * 通过HttpClient测试URL的可用性
     *
     * @param urlValue URL
     * @return boolean
     * @author Fcant 上午 5:54 2020/5/23/0023
     */
    public static boolean openUrlByHttpClient(String urlValue) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000)
                .build();
        HttpGet httpGet = new HttpGet(urlValue);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        boolean isValid = false;
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200) {
                isValid = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isValid;
    }

}


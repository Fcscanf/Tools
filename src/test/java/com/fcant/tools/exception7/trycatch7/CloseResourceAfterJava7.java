package com.fcant.tools.exception7.trycatch7;

import com.fcant.tools.bean.Resource;

/**
 * CloseResourceAfterJava7
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 22:04 2020/6/26/0026
 */
public class CloseResourceAfterJava7 {

    public static void main(String[] args) {
        try {
            errorTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void errorTest() throws Exception {
        Resource resource = null;
        try {
            resource = new Resource();
            resource.sayHello();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }
}

package com.fcant.tools.bean;

/**
 * Resource
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 22:03 2020/6/26/0026
 */
public class Resource implements AutoCloseable {
    public void sayHello() throws Exception {
        throw new Exception("Resource throw Exception");
    }

    @Override
    public void close() throws Exception {
        throw new Exception("Close method throw Exception");
    }
}

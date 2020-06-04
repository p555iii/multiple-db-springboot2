package com.github.cyd.project.multiple.db;

/**
 * @Description
 * @Author changyandong
 * @Created Date: 2018/8/2 13:59
 * @ClassName DynamicDataSourceException
 * @Version: 1.0
 */
public class DynamicDataSourceException extends RuntimeException {
    /**
     * 自定义构造器，只保留一个，让其必须输入错误码及内容
     */
    public DynamicDataSourceException(String msg) {
        super(msg);
    }
}

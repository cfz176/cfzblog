package com.cfz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    /**
     * 生成文件名
     * @param fileName
     * @return
     */
    public static String generateFilePath(String fileName) {
        //根据日期生成路径
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String format = simpleDateFormat.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件一致
        int index = fileName.lastIndexOf(".");
        //test.jpg -> .jpg
        String fileType = fileName.substring(index);
        return new StringBuilder().append(format).append(uuid).append(fileType).toString();
    }

}

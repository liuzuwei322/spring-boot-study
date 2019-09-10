package com.baidu.springbootstudy.utils;

import com.baidu.springbootstudy.controller.RunShellController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    private static String pathJar = System.getProperty("java.class.path");
    private static final Logger logger = LoggerFactory.getLogger(RunShellController.class);

    /**
     * 获取服务器Jar包所在绝对路径
     * @return
     */
    public static String getJarDir() {
        if (pathJar.indexOf(":") >= 0) {
            String[] tmpArr = pathJar.split(":");
            pathJar = tmpArr[tmpArr.length - 1];
        }
        int firstIndex = pathJar.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = pathJar.lastIndexOf(File.separator) + 1;

        return pathJar.substring(firstIndex, lastIndex);
    }
}

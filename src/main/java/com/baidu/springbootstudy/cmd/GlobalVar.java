package com.baidu.springbootstudy.cmd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalVar {
    public static Map<String, ShellBase> map = new ConcurrentHashMap<>();
}

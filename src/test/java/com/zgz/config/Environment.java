package com.zgz.config;/**
 * @Author ZHANG
 * @PackageName Auto_ApiTest
 * @Package com.zgz.config
 * @Date 2022/11/8 23:19
 * @Version 1.0
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: Auto_ApiTest
 * @BelongsPackage: com.zgz.config
 * @Author: zgz
 * @CreateTime: 2022-11-08  23:19
 * @Description: 主要用于全局管理环境变量，模拟Jmeter变量存储操作
 * @Version: 1.0
 */
public class Environment {
    //声明并定义一个map（类似于JMeter的环境变量）
    public static Map<String,Object> envMap = new HashMap<String, Object>();
}

package com.zgz.config;

/**
 * @BelongsProject: Auto_ApiTest
 * @BelongsPackage: com.zgz.config
 * @Author: 项目常规信息配置类
 * @CreateTime: 2022-11-08  23:09
 * @Description: TODO
 * @Version: 1.0
 */
public class Contants {
    //项目访问地址
    public static final String PROJECT_URL="v.juhe.cn";
    //项目BASEURI地址
    public static final String BASE_URL="http://"+PROJECT_URL;
    //测试用例路径
    public static final String EXCEL_PATH="src\\test\\resources\\api_testcases.xls";
    //账号数据的Key，此处的key是自己申请聚合数据账号后得到的key
    public static final String KEY="xxxxx";

    //控制台日志输出开关（true->输出到控制台，false->不输出到控制台）
    public static final boolean SHOW_CONSOLE_LOG=false;
}

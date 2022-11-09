package com.zgz.common;/**
 * @Author ZHANG
 * @PackageName Auto_ApiTest
 * @Package com.zgz.common
 * @Date 2022/11/8 23:21
 * @Version 1.0
 */

import com.alibaba.fastjson.JSONObject;
import com.zgz.config.Contants;
import com.zgz.config.Environment;
import com.zgz.enties.CaseInfo;
import com.zgz.utils.JDBCUtils;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @BelongsProject: Auto_ApiTest
 * @BelongsPackage: com.zgz.common
 * @Author: zgz
 * @CreateTime: 2022-11-08  23:21
 * @Description: TODO
 * @Version: 1.0
 */
public class BaseTest {
    @BeforeSuite
    public void beforeMethod(){
        //把json小数的返回类型配置成BigDecimal类型，通过此配置，可以使得我们在断言小数类型的时候保持数据类型一致，避免了因数据类型不一致而导致断言不通过的情况
        RestAssured.config = RestAssured.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
        //REST-assured基础 baseurl设置
        RestAssured.baseURI= Contants.BASE_URL;
    }

    /**
     * 封装所有请求类型
     * @param caseInfo 测试用例对象
     * @return response响应对象
     */
    public static Response request(CaseInfo caseInfo){
        //读取测试用例的请求头
        String requestHeaders=caseInfo.getRequestHeader();
        //将请求头转为map类型数据
        Map requestHeadersMap= JSONObject.parseObject(requestHeaders);
        //读取测试用例的url
        String url=caseInfo.getUrl();
        //读取测试用例的body输入参数
        String params=caseInfo.getInputParams();
        //读取测试用例的请求方式
        String method=caseInfo.getMethod();
        //封装请求方法
        Response response=null;
        if ("get".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all()
                    .headers(requestHeadersMap)
                    .when().get(url)
                    .then().log().all()
                    .extract().response();
        }
        else if ("post".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all()
                    .headers(requestHeadersMap)
                    .body(params)
                    .when().post(url)
                    .then().log().all()
                    .extract().response();
        }
        else if ("put".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all()
                    .headers(requestHeadersMap)
                    .body(params)
                    .when().post(url)
                    .then().log().all()
                    .extract().response();
        }

        return response;
    }

    /**
     * 响应断言
     * @param res 实际响应结果
     * @param caseInfo 请求数据（实体类）
     */
    public void assertResponse(Response res,CaseInfo caseInfo){
        String expected = caseInfo.getExpected();
        if(expected != null) {
            //转成Map
            Map<String, Object> expectedMap = JSONObject.parseObject(expected);
            Set<String> allKeySet = expectedMap.keySet();
            for (String key : allKeySet) {
                //获取实际响应结果
                Object actualResult = res.jsonPath().get(key);
                //获取期望结果
                Object expectedResult = expectedMap.get(key);
                Assert.assertEquals(actualResult, expectedResult);
            }
        }
    }

    /**
     * 数据库断言统一封装
     * @param caseInfo 用例数据
     */
    public void assertDB(CaseInfo caseInfo){
        String dbAssertInfo = caseInfo.getDbAssert();
        if(dbAssertInfo != null) {
            Map<String, Object> mapDbAssert = JSONObject.parseObject(dbAssertInfo);
            Set<String> allKeys = mapDbAssert.keySet();
            for (String key : allKeys) {
                //key为对应要执行的sql语句
                Object dbActual = JDBCUtils.querySingleData(key);
                //根据数据库中读取实际返回类型做判断
                //1、Long类型
                if(dbActual instanceof Long){
                    Integer dbExpected = (Integer) mapDbAssert.get(key);
                    Long expected = dbExpected.longValue();
                    Assert.assertEquals(dbActual, expected);
                }else {
                    Object expected = mapDbAssert.get(key);
                    Assert.assertEquals(dbActual, expected);
                }
            }
        }
    }

    /**
     * 通过【提取表达式】将对应响应值保存到环境变量中
     * @param res 响应信息
     * @param caseInfo 实体类对象
     */
    public void extractToEnvironment(Response res, CaseInfo caseInfo){
        String extractStr = caseInfo.getExtractExper();
        if(extractStr != null) {
            //把提取表达式转成Map
            Map<String, Object> map = JSONObject.parseObject(extractStr);
            Set<String> allKeySets = map.keySet();
            for (String key : allKeySets) {
                //key为变量名，value是为提取的gpath表达式
                Object value = map.get(key);
                Object actualValue = res.jsonPath().get((String) value);
                //将对应的键和值保存到环境变量中
                Environment.envMap.put(key, actualValue);
            }
        }
    }

    /**
     * 正则替换功能，比如：
     * 原始字符串 {
     *   key=${key}
     * }
     * 替换为
     * {
     *   key=xxxx(自己账号生成的key)
     * }
     * xxxx 为环境变量中key变量名对应的变量值
     * @param orgStr 源字符串
     * @return
     */
    public String regexReplace(String orgStr){
        if(orgStr != null) {
            //匹配器
            Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
            //匹配对象
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            //循环遍历匹配对象
            while (matcher.find()) {
                //获取整个匹配正则的字符串 ${key}
                String allFindStr = matcher.group(0);
                //找到${XXX}内部的匹配的字符串 key
                String innerStr = matcher.group(1);
                //找到key：xxxx
                //具体的要替换的值（从环境变量中去找到的）
                Object replaceValue = Environment.envMap.get(innerStr);
                //要替换${key} --> xxxx
                result = result.replace(allFindStr, replaceValue + "");
            }
            return result;
        }else{
            return orgStr;
        }
    }

    /**
     * 整条用例数据的参数化替换，只要在对应的用例数据里面有${}包裹起来的数据,那么就会从环境变量中找，如果找到的话就去替换，否则不会
     * @param caseInfo
     */
    public CaseInfo paramsReplace(CaseInfo caseInfo){
        //1、请求头
        String requestHeader = caseInfo.getRequestHeader();
        caseInfo.setRequestHeader(regexReplace(requestHeader));
        //2、接口地址
        String url = caseInfo.getUrl();
        caseInfo.setUrl(regexReplace(url));
        //3、参数输入
        String inputParams = caseInfo.getInputParams();
        caseInfo.setInputParams(regexReplace(inputParams));
        //4、期望结果
        String expected = caseInfo.getExpected();
        caseInfo.setExpected(regexReplace(expected));
        return caseInfo;
    }

}

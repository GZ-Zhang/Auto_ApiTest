package com.zgz.common;/**
 * @Author ZHANG
 * @PackageName Auto_ApiTest
 * @Package com.zgz.common
 * @Date 2022/11/8 23:21
 * @Version 1.0
 */

import com.alibaba.fastjson.JSONObject;
import com.zgz.config.Contants;
import com.zgz.enties.CaseInfo;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

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
            response = RestAssured.given().log().all().headers(requestHeadersMap).when().get(url).then().log().all().extract().response();
        }
        else if ("post".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all().headers(requestHeadersMap).body(params).when().post(url).then().log().all().extract().response();
        }
        else if ("put".equalsIgnoreCase(method)) {
            response = RestAssured.given().log().all().headers(requestHeadersMap).body(params).when().post(url).then().log().all().extract().response();
        }

        return response;
    }
}

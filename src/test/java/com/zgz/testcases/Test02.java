package com.zgz.testcases;

import com.zgz.common.BaseTest;
import com.zgz.config.Contants;
import com.zgz.config.Environment;
import com.zgz.enties.CaseInfo;
import com.zgz.utils.EasyPoiExcelUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @BelongsProject: Auto_ApiTest
 * @BelongsPackage: com.zgz.testcases
 * @Author: zgz
 * @CreateTime: 2022-11-09  22:34
 * @Description: TODO
 * @Version: 1.0
 */
public class Test02 extends BaseTest {

    @BeforeClass
    public void setUp(){
        //向环境变量设置key
        Environment.envMap.put("key", Contants.KEY);
    }

    @Test(dataProvider = "readCases")
    public void test01(CaseInfo caseInfo){
        //将测试用例做整体替换，只要遇到${}数据，就替换为环境变量中的实际数据
        caseInfo=paramsReplace(caseInfo);
        //发起请求
        Response res = request(caseInfo);
        //断言请求
        assertResponse(res,caseInfo);
        //将测试用例的提取表达式保存到环境变量中
        extractToEnvironment(res,caseInfo);
    }

    @DataProvider
    //向测试用例提供Excel数据
    public Object[] readCases(){
        List<CaseInfo> listDatas = EasyPoiExcelUtil.readExcel(0);
        return listDatas.toArray();

    }


}

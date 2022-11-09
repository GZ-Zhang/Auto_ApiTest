package com.zgz.testcases;/**
 * @Author ZHANG
 * @PackageName Auto_ApiTest
 * @Package com.zgz.testcases
 * @Date 2022/11/8 23:14
 * @Version 1.0
 */

import com.zgz.enties.CaseInfo;
import com.zgz.utils.EasyPoiExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @BelongsProject: Auto_ApiTest
 * @BelongsPackage: com.zgz.testcases
 * @Author: zgz
 * @CreateTime: 2022-11-08  23:14
 * @Description: TODO
 * @Version: 1.0
 */
public class Test01 {
    @Test(dataProvider = "readCases")
    public void test01(CaseInfo caseInfo){
        System.out.println(caseInfo);
    }

    @DataProvider
    public Object[] readCases(){
        List<CaseInfo> listDatas = EasyPoiExcelUtil.readExcel(0);
        return listDatas.toArray();
    }
}

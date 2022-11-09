package com.zgz.utils;/**
 * @Author ZHANG
 * @PackageName Auto_ApiTest
 * @Package com.zgz.utils
 * @Date 2022/11/8 23:11
 * @Version 1.0
 */

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.zgz.config.Contants;
import com.zgz.enties.CaseInfo;

import java.io.File;
import java.util.List;

/**
 * @BelongsProject: Auto_ApiTest
 * @BelongsPackage: com.zgz.utils
 * @Author: zgz
 * @CreateTime: 2022-11-08  23:11
 * @Description: Excel解析工具类
 * @Version: 1.0
 */
public class EasyPoiExcelUtil {
    /**
     * 使用EasyPOI读取Excel数据
     * @return 用例list集合
     * 获取Excel里的所有行
     */
    public static List<CaseInfo> readExcel(int num){
        //读取测试用例
        File file=new File(Contants.EXCEL_PATH);
        //读取和导入Excel的参数配置
        ImportParams params=new ImportParams();
        params.setStartSheetIndex(num);
        //读取测试用例整合成每条用例对象集合
        List<CaseInfo> cases = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return cases;
    }


    /**
     * 使用EasyPOI读取Excel数据
     * @return 用例list集合
     * 获取Excel里的指定行
     */
    public static List<CaseInfo> readExcelPart(int num,int startNum,int readRows){
        //读取测试用例
        File file=new File(Contants.EXCEL_PATH);
        //读取和导入Excel的参数配置
        ImportParams params=new ImportParams();
        //读取指定页的Sheet
        params.setStartSheetIndex(num);
        //指定从第几行开始读取
        params.setStartRows(startNum);
        //指定读取几行数据
        params.setReadRows(readRows);
        //读取测试用例整合成每条用例对象集合
        List<CaseInfo> cases = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return cases;
    }

    /**
     * 使用EasyPOI读取Excel数据
     * @return 用例list集合
     * 从指定行开始读取下面全部用例
     */
    public static List<CaseInfo> readExcelPart(int num,int startNum){
        //读取测试用例
        File file=new File(Contants.EXCEL_PATH);
        //读取和导入Excel的参数配置
        ImportParams params=new ImportParams();
        //读取指定页的Sheet
        params.setStartSheetIndex(num);
        //指定从第几行开始读取
        params.setStartRows(startNum);
        //读取测试用例整合成每条用例对象集合
        List<CaseInfo> cases = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return cases;
    }
}

package com.uc.jtest.mocker;

import org.junit.Test;

import com.uc.jtest.data.JsonDataMocker;

public class JsonDataMockerTest {
    
    @Test
    public void test(){
        
        System.out.println(
        JsonDataMocker.jsonMocker()
        .addToRoot("status", "20000")
        .addToRoot("message", "Success")
        .addToData("service_ticket", "st580147052d4ae9b7dca9d938dc33da")
        .addToData("uid", "173772677")
        .addToData("nickname", "sandbox_zhangcl")
        .addToData(
                "avatar_uri",
                "http%3A%2F%2Fimg.taobaocdn.com%2Fsns_logo%2Fi4%2F10151014496000%2FTB2L04XXXXXXXXHXXXXXXXXXXXX_%21%213639980151-0-mytaobao.jpg")
        .getResponseString());
        
        System.out.println(
        JsonDataMocker
        .jsonMocker()
        .addToRoot("id","100")
        .addToRoot("encrypt","md5")
        .addToData("client", "caller", "secret.pp.client")
        .addListDataToData("content[0].packageName", "aaaa").addListDataToData("content[0].versionName", "ee")
        .addListDataToData("content[1].packageName", "aaaa").addListDataToData("content[1].versionName", "ee")
        .addListDataToData("test.versionName", "v")
        .getResponseString()
        );
    }

}

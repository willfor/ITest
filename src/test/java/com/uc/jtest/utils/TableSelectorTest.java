package com.uc.jtest.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.uc.jtest.table.TableCleaner;
import com.uc.jtest.table.TableInsertor;
import com.uc.jtest.table.TableSelector;


public class TableSelectorTest {

    @Ignore
    public void test() {
        TableInsertor.table("android_app").value("id","[1~5]").insert(5);
        TableSelector.select("android_app", "select * from android_app order by id asc")
                .assertTotalCount(5).index(1).assertSame("id", 2).index(0).assertSame("id", "1");
        assertEquals(String.valueOf(3),TableSelector.select("android_app").index(2).get("id"));
        assertEquals(String.valueOf(2),TableSelector.select("android_app").condition("id=2").get("id"));
        TableCleaner.deleteAll("android_app");
    }
    
    
    @Ignore
    public void test1(){
        Long appId = 36557L;
        Long versionId = 600056234L;
        String brand = "Meizu MX4";
        String content = "看小说挺好的";
        TableSelector.select("android_comment_data").index(0).assertSame("app_id", appId)
        .assertSame("appver_id", versionId).assertSame("user_id", -1)
        .assertSame("username", "游客").assertSame("status", 1)
        .assertSame("content", content).assertSame("stars", 10.0).assertSame("c_good", 0)
        .assertSame("c_bad", 0).assertSame("sec_id", 0).assertSame("p_id", 0)
        .assertSame("top_id", 0).assertSame("source", 0).assertSame("brand", brand)
        .assertSame("liked_count", 0).assertSame("account_type", 0)
        .assertSame("online_status", 1);
    }
    
    @Test
    public void testTrue(){
        Assert.assertTrue(true);
    }
    
}

package com.uc.jtest.utils;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.table.DBUtil;
import com.uc.jtest.table.TableCleaner;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.table.template.TableTemplateHandler;

public class DBUtilTest {


    @Test
    public void testDeleteAll() {
        assertTrue(TableCleaner.deleteAll("android_app"));
    }

    @Test
    public void testTruncateFailed() {
        // assertFalse(DBUtil.getInstance().deleteAll("android_app","test"));
    }

    @Test
    public void testTruncateSuccess() {
        assertTrue(TableCleaner.deleteAll("android_app", "android_apptag_app"));
    }

    @Test
    public void testInsertSuccess() {
        assertTrue(DBUtil
                .getInstance()
                .execute(
                        "insert into android_app ( catid,name,bundleId,versionCode,versionName,minSdkVersion,fileSize,status,filePath,artwork,posup,posids,up,down,comment_total,multiple_version,latest_vid,notupdate,verify,signature,md5,app_catid,sub_category,sub_catid,data_packet,is_bad_word,uc,is_check,total_size,set_bottom,updateDate) values( 5014,'PP Assistant','com.lanrenzhoumo.weekend',11,'1.0.1','2.3.2','7.38M',99,'/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk','/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png',0,0,0,0,0,0,500000088,1,1,'e8eb87439d3a3921db3881823c308993','22e0aa974022bef1dd9333333d2bc214',5014,'旅游,出行','616',0,0,0,1,7735069,0,'2015-01-29 14:07:19')"));
    }

    @Test
    public void testInsertFailed() {
        // assertFalse(DBUtil.getInstance().execute("insert into android_app ( catid,name,versionCode,versionName,minSdkVersion,fileSize,status,filePath,artwork,posup,posids,up,down,comment_total,multiple_version,latest_vid,notupdate,verify,signature,md5,app_catid,sub_category,sub_catid,data_packet,is_bad_word,uc,is_check,total_size,set_bottom,updateDate) values( 5014,'PP Assistant','com.lanrenzhoumo.weekend',11,'1.0.1','2.3.2','7.38M',99,'/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk','/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png',0,0,0,0,0,0,500000088,1,1,'e8eb87439d3a3921db3881823c308993','22e0aa974022bef1dd9333333d2bc214',5014,'旅游,出行','616',0,0,0,1,7735069,0,'2015-01-29 14:07:19')"));
    }

    @Test
    public void testTrue() {
        Assert.assertTrue(true);
    }

}

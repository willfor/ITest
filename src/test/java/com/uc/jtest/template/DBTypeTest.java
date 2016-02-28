package com.uc.jtest.template;

import org.junit.Assert;
import org.junit.Test;

import com.uc.jtest.table.template.DBType;

public class DBTypeTest {
	
	@Test
	 public  void testResolveType() {
			Assert.assertTrue(DBType.isBigInt("bigint(20)"));
			Assert.assertFalse(DBType.isInt("bigint(20)"));
			Assert.assertTrue(DBType.isStringRelatedType("varchar(512)"));
			Assert.assertTrue(DBType.isStringRelatedType("mediumtext"));
			Assert.assertTrue(DBType.isStringRelatedType("char(100)"));
			Assert.assertTrue(DBType.isStringRelatedType("text"));
			Assert.assertTrue(DBType.isInt("int(11)"));
			Assert.assertTrue(DBType.isInt("tinyint(1)"));
			Assert.assertTrue(DBType.isInt("smallint(5)"));
			Assert.assertTrue(DBType.isInt("mediumint(8)"));
			Assert.assertFalse(DBType.isBigInt("int(11)"));
			Assert.assertFalse(DBType.isBigInt("tinyint(1)"));
			Assert.assertFalse(DBType.isBigInt("smallint(5)"));
			Assert.assertFalse(DBType.isBigInt("mediumint(8)"));
			Assert.assertTrue(DBType.isDateTime("datetime"));
			
		}

}

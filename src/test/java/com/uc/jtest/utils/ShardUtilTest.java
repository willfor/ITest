package com.uc.jtest.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uc.jtest.table.ShardUtil;

public class ShardUtilTest {
	
	@Test
	public void testHashString(){
		//sync_app_check_safe_07
		assertEquals("sync_app_check_safe_07",ShardUtil.getShard(8, "sync_app_check_safe", "00541166d4c6ab96ba886809ec71ae49"));
		assertEquals("sync_app_check_safe_07",ShardUtil.getShard(8, "sync_app_check_safe", "00dceedfde6a4eb22853cecc2601ccfc"));
		assertEquals("sync_app_check_safe_07",ShardUtil.getShard(8, "sync_app_check_safe", "0290752fd6c1e98a51b31250e6fb27f4"));
		assertEquals("sync_app_check_safe_00",ShardUtil.getShard(8, "sync_app_check_safe", "0113b26231038a653976fabbf0af82f9"));
	}
	
	@Test
	public void testHashInt(){
		//sync_app_check_safe_07
		assertEquals("sync_package_history_00",ShardUtil.getShard(8, "sync_package_history", "6084440"));
		assertEquals("sync_package_history_01",ShardUtil.getShard(8, "sync_package_history", "23169"));
		assertEquals("sync_package_history_02",ShardUtil.getShard(8, "sync_package_history", "6084522"));
		assertEquals("sync_package_history_03",ShardUtil.getShard(8, "sync_package_history", "23099"));
		assertEquals("sync_package_history_04",ShardUtil.getShard(8, "sync_package_history", "6084492"));
	}

}

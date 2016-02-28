package com.uc.jtest.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RandomUtilTest {
    
    @Test
    public void testIncludeBothRangeAndComma(){
        System.out.println(RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[1~5,2~3]", 0));
    }
    /**
     * 测试输入字符为空
     * 期望：返回空
     * 不期望：抛NullPointer
     * 
     * */
    @Test
    public void testInputWasEmptyString() {
        Assert.assertNull(RandomUtil.getRangeValueBySequenceWithStartAndEndFlag(null, 0));
    }

    /**
     * 测试输入字符为常规字符，不包括约定的中括号
     * 期望：
     *   1.输出 = 输入
     *   2.与Index无关
     * */
    @Test
    public void testInputNotContainSquareBracket() {
        assertEquals("soft,game",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("soft,game", 0));
        assertEquals("soft,game",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("soft,game", 1));
    }

    @Test
    public void testInputContainPartSquareBracket1() {
        assertEquals("[soft,game",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[soft,game", 0));
        assertEquals("[soft,game",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[soft,game", 1));
    }

    @Test
    public void testInputContainPartSquareBracket2() {
        assertEquals("soft,game]",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("soft,game]", 0));
    }

    /**
     * 格式满足 ：带[] 和 ,
     * 在提供的值中，按照顺序循环
     * 
     * 
     * */
    @Test
    public void testInputContainPairSquareBracket() {
        assertEquals("soft",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[soft,game]", 0));
        assertEquals("game",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[soft,game]", 1));
        assertEquals("soft",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[soft,game]", 2));
        assertEquals("game",
                RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[soft,game]", 3));
    }

    /**
     * 格式满足：带[] 和 ～ 
     * 在提供的数字值中，按照范围取
     * */
    @Test
    public void testInputContainPairSquareBracketAndRange() {
        assertEquals("1", RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[1~3]", 0));
        assertEquals("2", RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[1~3]", 1));
        assertEquals("3", RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[1~3]", 2));
        //assertEquals("1", RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[1~3]", 3));
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testInputContainPairSquareBracketAndNotNumberRange() {
        exception.expect(UnsupportedOperationException.class);
        assertEquals("1", RandomUtil.getRangeValueBySequenceWithStartAndEndFlag("[x~y]", 0));
    }

    @Test
    public void testGetRangeValueBySenquence() {
        assertEquals("soft", RandomUtil.getRangeValueBySenquence("soft,game", 0));
        assertEquals("game", RandomUtil.getRangeValueBySenquence("soft,game", 1));
        assertEquals("soft", RandomUtil.getRangeValueBySenquence("soft,game", 2));
        assertEquals("game", RandomUtil.getRangeValueBySenquence("soft,game", 3));
        assertEquals("soft", RandomUtil.getRangeValueBySenquence("soft,game", 4));
    }

}

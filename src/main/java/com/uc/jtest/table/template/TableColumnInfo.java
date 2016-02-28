package com.uc.jtest.table.template;

import com.uc.jtest.utils.RandomUtil;
import com.uc.jtest.utils.ReflectionUtil;

public class TableColumnInfo {

    private String name;
    private String defaultValue;
    private String type;
    private String randomValue;
    private Object value;
    private boolean isUniqueNumber;
    private String uniqueSequenceNumber;
    private boolean isAutoIncrement;
    private String defaultMethod;
    private String nickName;

    @Override
    public String toString() {
        return " name:" + name + " defaultValue: " + defaultValue + " type:" + type
                + " randomValue:" + randomValue + " isUniqueNumber:" + isUniqueNumber
                + " uniqueSequenceNumber:" + uniqueSequenceNumber + " defaultMethod:"
                + defaultMethod + " value:" + value;
    }

    public TableColumnInfo() {

    }

    public TableColumnInfo(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public void setToExpectedValue(int i) {
        if (defaultValue != null && defaultValue != "") {
            this.setValue(defaultValue);
        } else if (randomValue != null && randomValue != "") {
            this.setValue(RandomUtil.getRandomValues(randomValue));
        } else if (isUniqueNumber()) {
            this.setValue(RandomUtil.getIntRandom(Integer.MIN_VALUE, Integer.MAX_VALUE));
        } else if (this.uniqueSequenceNumber != null && this.uniqueSequenceNumber != "") {
            this.setValue(RandomUtil.getRangeValueBySenquence(this.uniqueSequenceNumber, i));
        } else if (this.defaultMethod != null && this.defaultMethod != "") {
            this.setValue(ReflectionUtil.getMethodValue(this.defaultMethod));
            // PrintUtil.print("配置了defaultMethod：" + defaultMethod + " value: "+ this.getValue());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(String randomValue) {
        this.randomValue = randomValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isUniqueNumber() {
        return isUniqueNumber;
    }

    public void setUniqueNumber(boolean isUniqueNumber) {
        this.isUniqueNumber = isUniqueNumber;
    }

    public String getUniqueSequenceNumber() {
        return uniqueSequenceNumber;
    }

    public void setUniqueSequenceNumber(String uniqueSequenceNumber) {
        this.uniqueSequenceNumber = uniqueSequenceNumber;
    }

    public boolean isIgnoreColumn() {
        return this.isAutoIncrement() && this.getValue() == null;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getDefaultMethod() {
        return defaultMethod;
    }

    public void setDefaultMethod(String defaultMethod) {
        this.defaultMethod = defaultMethod;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}

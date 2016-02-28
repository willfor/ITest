package com.uc.jtest.table.template;

import static com.uc.jtest.config.JTestConstant.TEMPLATE_TABLE_FILE_NAME;
import static com.uc.jtest.table.template.TableTemplateNode.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.SAXParserFactory;

import org.junit.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.uc.jtest.config.JTestConstant;
import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.table.TableCleaner;
import com.uc.jtest.utils.JTestStringUtils;
import com.uc.jtest.utils.JTestTableGenerator;
import com.uc.jtest.utils.PrintUtil;

public class TableTemplateHandler extends DefaultHandler {

    private void writeTableNamesForTestCaseUsage() {
        Map<String, TableInfo> tableNameAndInfoMap = getTableInfos();
        StringBuffer sb = new StringBuffer();
        for (String tableName : tableNameAndInfoMap.keySet()) {
            for (String actualTableName : tableNameAndInfoMap.get(tableName).getAllTableNames()) {
                sb.append(actualTableName).append(",");
            }
        }
        String content = sb.toString();
        content = content.substring(0, content.length() - 1);
        FileOperateUtil.rewriteToFile(TEMPLATE_TABLE_FILE_NAME, content);
        PrintUtil.print("初始化清空模板配置的所有表数据", Level.INFO);
    }

    private static TableTemplateHandler handler;

    private TableTemplateHandler() {

    }

    public static TableTemplateHandler getInstance() {
        if (handler == null) {
            handler = new TableTemplateHandler();
            handler.parseXMLs();
        }
        return handler;
    }

    public void parseXMLs() {
        File folder = new File(FileOperateUtil.getCurrentProjectPath(false)+"test/resources/template/");
        if(!folder.exists()){
            folder = new File(FileOperateUtil.getCurrentProjectPath(true)+"test/resources/template/");
        }
        PrintUtil.print("开始解析 test/resources/template 下的所有文件", Level.INFO);
        File[] files = folder.listFiles();
        if(files==null || files.length ==0){
            
        }
        for (File file : files) {
            parseXML(file);
            PrintUtil.print("解析文件成功! " + file.getName(), Level.INFO);
        }
        writeTableNamesForTestCaseUsage();
        JTestTableGenerator.generateJTestTableJavaFile(this.getTableInfos());
    }

    public void parseXML(File file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            InputSource inputSource = new InputSource(file.getAbsolutePath());
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception throws when parse XML file!" + e.getMessage());
        }
    }

    private List<TableInfo> tableInfos = new ArrayList<TableInfo>();
    private List<TableGroup> tableGroups = new ArrayList<TableGroup>();
    private TableInfo tableInfo;
    private TableColumnInfo column;
    private TableGroup tableGroup;
    private TableRelation tableRelation;
    private Map<String, TableInfo> tableNameAndInfoMap;
    private Map<String, TableGroup> tableNameAndGroupMap;

    public TableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = getTableInfos().get(tableName);
        if (tableInfo == null) {
            PrintUtil.print("无法获取表：" + tableName
                    + "对应的模板信息，请检查:1.代码里面的表名是否正确2.模板是否配置 3.模板里面的表名是否正确！", Level.SEVERE);
            throw new UnsupportedOperationException("无法获取表：" + tableName
                    + "对应的模板信息，请检查:1.代码里面的表名是否正确2.模板是否配置 3.模板里面的表名是否正确！");
        }
        return tableInfo;
    }

    public Map<String, TableInfo> getTableInfos() {
        boolean isOneTableOccursManyTimes = false;
        if (tableNameAndInfoMap == null) {
            tableNameAndInfoMap = new HashMap<String, TableInfo>();
            for (TableInfo tableInfo : tableInfos) {
                if (tableNameAndInfoMap.get(tableInfo.getTableName()) != null) {
                    isOneTableOccursManyTimes = true;
                    throw new UnsupportedOperationException("表：" + tableInfo.getTableName()
                            + "在模板里面配置多次，请检查");
                }
                tableNameAndInfoMap.put(tableInfo.getTableName(), tableInfo);
            }
        }
        Assert.assertFalse(isOneTableOccursManyTimes);
        return tableNameAndInfoMap;
    }

    public Map<String, TableGroup> getTableGroups() {
        if (tableNameAndGroupMap == null) {
            tableNameAndGroupMap = new HashMap<String, TableGroup>();
            for (TableGroup group : tableGroups) {
                tableNameAndGroupMap.put(group.getGroupName(), group);
            }
        }
        return tableNameAndGroupMap;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (TableTemplateNode.isTable(qName)) {
            tableInfo = new TableInfo();
            tableInfos.add(tableInfo);
            tableInfo.setTableName(attributes.getValue(TableTemplateNode.name.name()));
            tableInfo.setDdlClass(attributes.getValue(TableTemplateNode.ddlClass.name()));
            tableInfo.setShardColumn(attributes.getValue(TableTemplateNode.shardColumn.name()));
            if (JTestStringUtils.isNotEmpty(attributes.getValue(TableTemplateNode.modebase.name()))) {
                tableInfo.setModebase(Integer.valueOf(
                        attributes.getValue(TableTemplateNode.modebase.name())).intValue());
            }
            
            
            // PrintUtil.print(tableInfo.toString());
        }

        if (TableTemplateNode.isColumn(qName)) {
            column = new TableColumnInfo();
            tableInfo.addToTableColumns(column);
            column.setName(attributes.getValue(TableTemplateNode.name.name()));
            column.setDefaultValue(attributes.getValue(defaultValue.name()));
            column.setType(attributes.getValue(type.name()));
            column.setRandomValue(attributes.getValue(random.name()));
            column.setUniqueNumber(JTestStringUtils.isNotEmpty(attributes.getValue(uniqueNumber
                    .name())));
            column.setUniqueSequenceNumber(attributes.getValue(uniqueSequenceNumber.name()));
            column.setAutoIncrement(JTestStringUtils.isNotEmpty(attributes.getValue(autoIncrement
                    .name())) && Boolean.valueOf(attributes.getValue(autoIncrement.name())));
            column.setDefaultMethod(attributes.getValue(defaultMethod.name()));
            column.setNickName(JTestStringUtils.isNotEmpty(attributes.getValue(nickName.name())) ? attributes
                    .getValue(nickName.name()) : column.getName());
        }

        if (TableTemplateNode.isTableGroup(qName)) {
            tableGroup = new TableGroup();
            tableGroups.add(tableGroup);
            tableGroup.setGroupName(attributes.getValue(TableTemplateNode.name.name()));
            tableGroup.setMainTableName(attributes.getValue(TableTemplateNode.mainTable.name()));
        }

        if (TableTemplateNode.isGTable(qName)) {
            tableRelation = new TableRelation();
            tableGroup.addToTableRelations(tableRelation);
            tableRelation.setTableName(attributes.getValue(TableTemplateNode.name.name()));
            tableRelation.setFk(attributes.getValue(TableTemplateNode.fk.name()));
            tableRelation
                    .setReferenceKey(attributes.getValue(TableTemplateNode.referencekey.name()));
        }

    }

}

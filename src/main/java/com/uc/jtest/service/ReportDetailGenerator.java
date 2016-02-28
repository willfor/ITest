package com.uc.jtest.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.uc.jtest.config.ConfigLoader;
import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.table.template.TableTemplateHandler;
import com.uc.jtest.utils.JTestStringUtils;
import com.uc.jtest.utils.PrintUtil;

public class ReportDetailGenerator {
    public static final String VO_JAVA_PACKAGE_NAME = ConfigLoader
            .getProperty("VO_JAVA_PACKAGE_NAME");
    public static final String VO_JAVA_FILE_PATH = ConfigLoader.getProperty("VO_JAVA_FILE_PATH");
    public static final String QUERY_SQL_FILE_PATH = ConfigLoader
            .getProperty("QUERY_SQL_FILE_PATH");

    public static void main(String[] args) {
        generateSelectedDetailClass("query_cpu_task.sql","CPUReportDetail");
    }

    public static void generateSelectedDetailClass(String fileName,String className) {
        List<String> columns = getColumnsBySqlQuery(FileOperateUtil.readFile(QUERY_SQL_FILE_PATH
                + fileName));
        generateTargetClass(columns, className);
    }

    public static List<String> getColumnsBySqlQuery(String querySql) {
        String selectkeyword = "select@space@";
        String fromKeyword = "@space@from";
        int selectIndex = querySql.indexOf(selectkeyword);
        int fromIndex = querySql.indexOf(fromKeyword);
        String selectedColumns = querySql
                .substring(selectIndex + selectkeyword.length(), fromIndex);
        String[] columns = selectedColumns.trim().split(",");
        List<String> finalColumns = new ArrayList<String>();
        for (String columnName : columns) {
            String finalColumnName = "";
            if (columnName.contains(" as ")) {
                finalColumnName = columnName.split(" as ")[1].trim();
            } else {
                finalColumnName = columnName.split("\\" + ".")[1].trim();
            }
            finalColumns.add(finalColumnName);
        }
        return finalColumns;
    }

    public static void generateTargetClass(List<String> columns, String className) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        File f = checkFile(VO_JAVA_FILE_PATH + className + ".java");
        try {
            fos = new FileOutputStream(f);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write("package " + VO_JAVA_PACKAGE_NAME + ";\t\n");
            bw.write("import com.uc.jtest.vo.TableDetailBase;\t\n");
            bw.write("\n");
            bw.write("public class " + className + " extends TableDetailBase {" + "\t\n");
            for (String column : columns) {
                bw.write("\n");
                bw.write("\t" + "private String " + column + " ;" + "\t\n");
                bw.write("\n");
                bw.write("\t" + "public String get" + JTestStringUtils.upperFirstLetter(column)
                        + "(){\t\n");
                bw.write("\t\t" + "return " + column + ";");
                bw.write("\n");
                bw.write("\t}");
                bw.write("\n");
                bw.write("\n\t" + "public void set" + JTestStringUtils.upperFirstLetter(column)
                        + "(String " + column + "){\t\n");
                bw.write("\t\t this." + column + " =" + column + ";");
                bw.write("\n");
                bw.write("\t}\t\n");
            }
            bw.write("\n}\t\n");
            PrintUtil.print("文件 " + VO_JAVA_FILE_PATH + className + ".java 生成成功！", Level.INFO);
        } catch (Exception e) {
            PrintUtil.print("exception throws when write content to " + className + e.getMessage(),
                    Level.SEVERE);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {

            }
        }
    }

    private static File checkFile(String filePath) {
        int lastSplitterIndex = filePath.lastIndexOf("//");
        String path = filePath.substring(0, lastSplitterIndex);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = null;
        try {
            f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            } else {
                // f.delete();
                // f.createNewFile();
            }
        } catch (Exception e) {
            PrintUtil.print("exception throws when create file!" + e.getMessage(), Level.SEVERE);
        }
        return f;
    }

    public static Map<String, TableInfo> getFilteredTableInfosByQuerySql(String querySql) {
        Map<String, TableInfo> tableNameAndInfos = TableTemplateHandler.getInstance()
                .getTableInfos();
        Map<String, TableInfo> queriedTableNameAndInfos = new HashMap<String, TableInfo>();
        for (String tableName : tableNameAndInfos.keySet()) {
            if (querySql.contains(tableName)) {
                queriedTableNameAndInfos.put(tableName, tableNameAndInfos.get(tableName));
            }
        }
        return queriedTableNameAndInfos;
    }
}

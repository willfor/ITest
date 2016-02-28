package com.uc.jtest.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.uc.jtest.config.ConfigLoader;
import com.uc.jtest.table.template.TableColumnInfo;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.table.template.TableTemplateHandler;

public class JTestTableGenerator {

    public static final String TABLE_JAVA_FILE_PATH = ConfigLoader
            .getProperty("TABLE_JAVA_FILE_PATH");
    public static final Boolean IS_REGENERATE_TABLE_JAVA_FILE = Boolean.valueOf(ConfigLoader
            .getProperty("IS_RE_GENERATE_TABLE_JAVA_FILE"));
    public static final String TABLE_PACKAGE_NAME = ConfigLoader.getProperty("TABLE_PACKAGE_NAME");
    
    public static void generate(){
        generateJTestTableJavaFile(TableTemplateHandler.getInstance().getTableInfos());
    }
    
    public static void generateDDLClass(){
        
    }
    
    

    public static void generateJTestTableJavaFile(Map<String, TableInfo>  tableNameAndInfoMap) {
        if (IS_REGENERATE_TABLE_JAVA_FILE) {
            try {
                FileOutputStream fos = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;
                File f = checkFile(TABLE_JAVA_FILE_PATH);
                try {
                    fos = new FileOutputStream(f);
                    osw = new OutputStreamWriter(fos, "UTF-8");
                    bw = new BufferedWriter(osw);
                    bw.write("package " + TABLE_PACKAGE_NAME + ";\t\n");
                    bw.write("public final class "+ f.getName().replace(".java", "") +" {" + "\t\n");
                    for (String tableName : tableNameAndInfoMap.keySet()) {
                        bw.write("\t" + "public static final class " + tableName + " {" + "\t\n");
                        bw.write("\t\t" + "public static final String TABLE_NAME=\"" + tableName + "\";\t\n");
                        for (TableColumnInfo columnInfo : tableNameAndInfoMap.get(tableName)
                                .getTableColumns()) {
                            bw.write("\t\t" + "public static final String " + columnInfo.getName()
                                    + " = \"" + columnInfo.getName() + "\";\t\n");
                        }
                        bw.write("\t}\t\n");
                    }
                    bw.write("}\t\n");
                    PrintUtil.print("文件 " + TABLE_JAVA_FILE_PATH + " 生成成功！", Level.INFO);
                } catch (Exception e) {
                    PrintUtil.print("exception throws when write content to "
                            + TABLE_JAVA_FILE_PATH + e.getMessage(), Level.SEVERE);
                } finally {
                    if (bw != null) {
                        bw.close();
                    }
                    if (osw != null) {
                        osw.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            } catch (Exception e) {
                PrintUtil.print("exception throws when close stream!" + e.getMessage(),
                        Level.SEVERE);
            }
        } else {
            PrintUtil.print("不会生成新的 " + TABLE_JAVA_FILE_PATH + " 文件", Level.INFO);
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
                f.delete();
                f.createNewFile();
            }
        } catch (Exception e) {
            PrintUtil.print("exception throws when create file!" + e.getMessage(), Level.SEVERE);
        }
        return f;
    }
}

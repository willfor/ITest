package com.uc.jtest.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.logging.Level;

import com.uc.jtest.utils.PrintUtil;

public class FileOperateUtil {
    
    public static void rewriteToFile(String filePath, String newFileContent){
        File dir = new File(filePath);
        if(dir.exists()){
            dir.delete();
        }
        writeToFileIfNotExist(filePath,newFileContent);
    }

    public static void writeToFileIfNotExist(String filePath, String newFileContent) {
        try {
            if (writeContentToFile(filePath, newFileContent)) {
                PrintUtil.print("write content to file :" + filePath + " successfully!",Level.INFO);
            }
        } catch (Exception e) {
            PrintUtil.print("Exception throws when write to file :",Level.SEVERE);
        }
    }
    
    public static boolean isExist(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            return true;
        }
        return false;
    }
    
    public static boolean deleteIfExist(String path){
        File dir = new File(path);
        boolean isDeleted = true;
        if (dir.exists()) {
            isDeleted = dir.delete();
        }
        return isDeleted;
    }

    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static boolean writeContentToFile(String filePath, String srcContent) throws Exception {
        int lastSplitterIndex = filePath.lastIndexOf("//");
        String path = filePath.substring(0, lastSplitterIndex);
        createDir(path);
        File f = null;
        try {
            f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            } else {
                PrintUtil.print("file " + f.getName() + " exists,将不会改动原文件!",Level.INFO);
                return false;
            }
        } catch (Exception e) {
            System.out.println("exception throws when create file!" + e.getMessage());
        }
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(f, true);
            writer.write(srcContent.getBytes("utf-8"));
            writer.close();
        } catch (Exception e) {
            System.out.println("exception throws when writeToFile !" + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();

            }
        }
        return true;
    }

    public static String readFile(String fileName) {
        BufferedReader existedContentReader = null;
        StringBuffer sb = new StringBuffer();
        try {
            try {

                String str = "";
                existedContentReader = new BufferedReader(new FileReader(fileName));
                while ((str = existedContentReader.readLine()) != null) {
                    sb.append(str).append("\n");
                }
                existedContentReader.close();
            } catch (Exception e) {
                PrintUtil.print("exception throws when read from file :" + fileName + " " + e.getMessage(),Level.SEVERE);
            } finally {
                if (existedContentReader != null) {
                    existedContentReader.close();
                }
            }
        } catch (Exception e) {
            PrintUtil.print("exception throws when read from file :" + fileName + " " + e.getMessage(),Level.SEVERE);
        }
        return sb.toString();
    }
    
    public static String getCurrentProjectPath(boolean error){
        String currentPath = System.getProperty("user.dir").replace("\\", "/") + "/";
        if(error){
           return currentPath + "/pppack/";
        }else{
            return currentPath;
        }
    }

}

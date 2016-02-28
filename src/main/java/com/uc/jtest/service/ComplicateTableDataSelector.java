package com.uc.jtest.service;

import static com.uc.jtest.service.ReportDetailGenerator.QUERY_SQL_FILE_PATH;
import static com.uc.jtest.service.ReportDetailGenerator.getColumnsBySqlQuery;
import static com.uc.jtest.service.ReportDetailGenerator.getFilteredTableInfosByQuerySql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.table.DBUtil;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.utils.MapConstructor;
import com.uc.jtest.utils.PrintUtil;
import com.uc.jtest.utils.ReflectionUtil;
import com.uc.jtest.vo.CPUReportDetail;

public class ComplicateTableDataSelector {
    public static void main(String[] args) {
       List<CPUReportDetail> cpuReportDetails =  getReportDetailsByTaskId(CPUReportDetail.class,"query_cpu_task.sql",MapConstructor
                .newMapConstructor().put("@main_task_id@", String.valueOf(4)).get());
    }
    
  
    
    public static <T extends Object> List<T> getReportDetailsByTaskId(Class<T> c,
            String querySqlFilePath, Map<String, String> toBeReplacedKeyValue) {
        String querySql = FileOperateUtil.readFile(QUERY_SQL_FILE_PATH + querySqlFilePath);
        List<String> columns = getColumnsBySqlQuery(querySql);
        querySql = querySql.replace("@space@", " ");
        for (String key : toBeReplacedKeyValue.keySet()) {
            querySql = querySql.replace(key, toBeReplacedKeyValue.get(key));
        }
        PrintUtil.print(querySql, Level.INFO);
        Map<String, TableInfo> queriedTableNameAndInfos = getFilteredTableInfosByQuerySql(querySql);
        List<Map<String, String>> expectedResults = DBUtil.getInstance().selectResult(querySql,
                queriedTableNameAndInfos, columns);
        PrintUtil.print(String.valueOf(expectedResults.size()), Level.INFO);
        List<T> reportDetails = new ArrayList<T>();
        for (Map<String, String> result : expectedResults) {
            try {
                T reportTask = (T) c.newInstance();
                reportDetails.add(reportTask);
                for (String column : result.keySet()) {
                    ReflectionUtil.setFieldValue(c, reportTask, column, result.get(column));
                }
            } catch (Exception e) {
                PrintUtil.print("exception throws when convert class", Level.SEVERE);
            }
        }
        return reportDetails;
    }
}

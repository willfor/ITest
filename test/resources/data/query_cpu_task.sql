select@space@ 
 mt.main_task_id,
 mt.package_task_id,
 mt.create_time as mainTask_createTime,
 mt.end_time as mainTask_endTime,
 st.sub_task_id,
 st.url as report_url,
 st.result,
 st.creatTime as subTask_createTime,
 st.endTime as subTask_endTime,
 st.status as subTask_status,
 tc.case_desc,
 cpu.cpu_percentage,
 cpu.process,
 pt.ppserver,
 pt.channel,
 pt.url as apk_download_url,
 pt.creatTime as pt_creatTime,
 pt.doneTime as pt_endTime,
 pt.status as pt_status,
 pt.apkname,
 pt.sign,
 pt.typestatus,
 t.type_name,
 d.device_brand
@space@from android_main_task mt, android_sub_task st,android_test_cpu cpu,android_test_case tc,packtask pt,andriod_test_type t,android_device_info d
@space@WHERE mt.main_task_id = st.main_task_id
@space@AND mt.main_task_id = @main_task_id@
@space@AND st.sub_task_id = cpu.sub_task_id
@space@AND cpu.test_case_id = tc.test_case_id
@space@AND  mt.package_task_id = pt.taskId
@space@AND st.test_type_id = t.type_id
@space@AND st.device_id = d.device_id

-- 更新后台文件. BP

rd /S/Q D:\jfinal-jflow\docs
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\BPMN
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\CN
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\DA
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Demo
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\DTS
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\En
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\FlowEvent
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\GPM
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Port
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Pub
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Rpt
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Sys
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Tools
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\Web
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\WF
rd /S/Q D:\jfinal-jflow\jflow-core\src\main\java\BP\XML

xcopy D:\JFlow\docs D:\jfinal-jflow\docs /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\BPMN  D:\jfinal-jflow\jflow-core\src\main\java\BP\BPMN  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\CN  D:\jfinal-jflow\jflow-core\src\main\java\BP\CN  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\DA  D:\jfinal-jflow\jflow-core\src\main\java\BP\DA  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Demo  D:\jfinal-jflow\jflow-core\src\main\java\BP\Demo  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\DTS  D:\jfinal-jflow\jflow-core\src\main\java\BP\DTS  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\En  D:\jfinal-jflow\jflow-core\src\main\java\BP\En 
xcopy D:\JFlow\jflow-core\src\main\java\BP\FlowEvent  D:\jfinal-jflow\jflow-core\src\main\java\BP\FlowEvent  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\GPM  D:\jfinal-jflow\jflow-core\src\main\java\BP\GPM  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Port  D:\jfinal-jflow\jflow-core\src\main\java\BP\Port  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Pub  D:\jfinal-jflow\jflow-core\src\main\java\BP\Pub  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Rpt  D:\jfinal-jflow\jflow-core\src\main\java\BP\Rpt  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Sys  D:\jfinal-jflow\jflow-core\src\main\java\BP\Sys  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Tools  D:\jfinal-jflow\jflow-core\src\main\java\BP\Tools  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\Web  D:\jfinal-jflow\jflow-core\src\main\java\BP\Web  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\WF  D:\jfinal-jflow\jflow-core\src\main\java\BP\WF  /e /a
xcopy D:\JFlow\jflow-core\src\main\java\BP\XML  D:\jfinal-jflow\jflow-core\src\main\java\BP\XML  /e /a

 

-- 更新 前台.

rd /S/Q D:\jfinal-jflow\jflow-web\src\main\resources\cn
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\DataUser 
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\GPM 
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\SDKFlowDemo 
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\WF
del /f/q D:\jfinal-jflow\jflow-web\src\main\webapp\index.htm 


xcopy D:\JFlow\jflow-web\src\main\resources\cn		D:\jfinal-jflow\jflow-web\src\main\resources\cn  /e /a
xcopy D:\JFlow\jflow-web\src\main\webapp\DataUser	D:\jfinal-jflow\jflow-web\src\main\webapp\DataUser  /e /a
xcopy D:\JFlow\jflow-web\src\main\webapp\GPM		D:\jfinal-jflow\jflow-web\src\main\webapp\GPM  /e /a
xcopy D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo	D:\jfinal-jflow\jflow-web\src\main\webapp\SDKFlowDemo  /e /a
xcopy D:\JFlow\jflow-web\src\main\webapp\WF			D:\jfinal-jflow\jflow-web\src\main\webapp\WF /e /a
copy /y D:\JFlow\jflow-web\src\main\webapp\index.htm	D:\jfinal-jflow\jflow-web\src\main\webapp\index.htm

pause;

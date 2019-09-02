@echo off
echo 更新说明：
echo 1.从JFlow更新后台文件到JFinal版本. BP
echo 2.从JFlow更新前台文件到JFinal版本.
pause

rem 更新后台文件. BP

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
xcopy D:\JFlow\jflow-core\src\main\java\BP\BPMN  D:\jfinal-jflow\jflow-core\src\main\java\BP\BPMN /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\CN  D:\jfinal-jflow\jflow-core\src\main\java\BP\CN /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\DA  D:\jfinal-jflow\jflow-core\src\main\java\BP\DA /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Demo  D:\jfinal-jflow\jflow-core\src\main\java\BP\Demo /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\DTS  D:\jfinal-jflow\jflow-core\src\main\java\BP\DTS /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\En  D:\jfinal-jflow\jflow-core\src\main\java\BP\En  /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\FlowEvent  D:\jfinal-jflow\jflow-core\src\main\java\BP\FlowEvent /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\GPM  D:\jfinal-jflow\jflow-core\src\main\java\BP\GPM /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Port  D:\jfinal-jflow\jflow-core\src\main\java\BP\Port /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Pub  D:\jfinal-jflow\jflow-core\src\main\java\BP\Pub /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Rpt  D:\jfinal-jflow\jflow-core\src\main\java\BP\Rpt /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Sys  D:\jfinal-jflow\jflow-core\src\main\java\BP\Sys /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Tools  D:\jfinal-jflow\jflow-core\src\main\java\BP\Tools /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\Web  D:\jfinal-jflow\jflow-core\src\main\java\BP\Web /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\WF  D:\jfinal-jflow\jflow-core\src\main\java\BP\WF /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP\XML  D:\jfinal-jflow\jflow-core\src\main\java\BP\XML /e /k /y
 

rem 更新 前台.

rd /S/Q D:\jfinal-jflow\jflow-web\src\main\resources\cn
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\DataUser 
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\GPM 
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\SDKFlowDemo 
rd /S/Q D:\jfinal-jflow\jflow-web\src\main\webapp\WF
del /f/q D:\jfinal-jflow\jflow-web\src\main\webapp\index.htm 


xcopy D:\JFlow\jflow-web\src\main\resources\cn		D:\jfinal-jflow\jflow-web\src\main\resources\cn /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\DataUser	D:\jfinal-jflow\jflow-web\src\main\webapp\DataUser /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\GPM		D:\jfinal-jflow\jflow-web\src\main\webapp\GPM /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo	D:\jfinal-jflow\jflow-web\src\main\webapp\SDKFlowDemo /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\WF			D:\jfinal-jflow\jflow-web\src\main\webapp\WF /e /k /y
copy /y D:\JFlow\jflow-web\src\main\webapp\index.htm	D:\jfinal-jflow\jflow-web\src\main\webapp\index.htm

pause;

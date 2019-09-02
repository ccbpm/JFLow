@echo off
echo 更新说明：
echo 1.从JFlow更新后台文件到Jeesite4版本. BP
echo 2.从JFlow更新前台文件到Jeesite4版本.
pause

rem 更新后台文件. BP

rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\BPMN
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\CN
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\DA
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Demo
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\DTS
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\En
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\FlowEvent
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\GPM
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Port
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Pub
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Rpt
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Sys
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Tools
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Web
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\WF
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\XML

xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\BPMN  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\BPMN\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\CN  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\CN\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\DA  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\DA\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Demo  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Demo\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Difference  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Difference\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\DTS  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\DTS\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\En  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\En\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\FlowEvent  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\FlowEvent\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\GPM  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\GPM\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Port  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Port\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Pub  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Pub\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Rpt  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Rpt\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Sys  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Sys\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Tools  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Tools\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\Web  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\Web\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\WF  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\WF\
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP\XML  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\XML\

rem 更新 前台.

rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\DataUser 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\GPM 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\SDKFlowDemo 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\WF

xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\DataUser	D:\jeesite4-jflow\web\src\main\webapp\DataUser\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\GPM		D:\jeesite4-jflow\web\src\main\webapp\GPM\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo	D:\jeesite4-jflow\web\src\main\webapp\SDKFlowDemo\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\WF			D:\jeesite4-jflow\web\src\main\webapp\WF\

pause;

@echo off
echo 更新说明：
echo 1.从JFlow更新后台文件到Jeesite4版本. BP
echo 2.从JFlow更新前台文件到Jeesite4版本.

pause

rem 更新后台文件. BP
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\

rem 更新 前台.

rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\GPM 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\SDKFlowDemo 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\WF

xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\GPM		D:\jeesite4-jflow\web\src\main\webapp\GPM\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo	D:\jeesite4-jflow\web\src\main\webapp\SDKFlowDemo\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\WF			D:\jeesite4-jflow\web\src\main\webapp\WF\

pause;

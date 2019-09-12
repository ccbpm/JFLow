@echo off
echo 更新说明:
echo 1.从JFlow更新后台文件到SpringBoot版本. BP
echo 2.从JFlow更新前台文件到SpringBoot版本.
pause;

rem 更新后台文件. BP
rd /S/Q D:\JFlowSpringBoot\docs
rd /S/Q D:\JFlowSpringBoot\jflow-core\src\main\java\BP

xcopy D:\JFlow\docs D:\jfinal-jflow\docs /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP  D:\JFlowSpringBoot\jflow-core\src\main\java\BP\  /e /k /y

rem 更新 前台.
rd /S/Q D:\JFlowSpringBoot\jflow-web\src\main\resources\cn
rd /S/Q D:\JFlowSpringBoot\jflow-web\src\main\webapp\DataUser 
rd /S/Q D:\JFlowSpringBoot\jflow-web\src\main\webapp\GPM 
rd /S/Q D:\JFlowSpringBoot\jflow-web\src\main\webapp\SDKFlowDemo 
rd /S/Q D:\JFlowSpringBoot\jflow-web\src\main\webapp\WF
del /f/q D:\JFlowSpringBoot\jflow-web\src\main\webapp\index.htm 

xcopy D:\JFlow\jflow-web\src\main\resources\cn		D:\JFlowSpringBoot\jflow-web\src\main\resources\cn\  /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\DataUser	D:\JFlowSpringBoot\jflow-web\src\main\webapp\DataUser\  /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\GPM		D:\JFlowSpringBoot\jflow-web\src\main\webapp\GPM\  /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo	D:\JFlowSpringBoot\jflow-web\src\main\webapp\SDKFlowDemo\  /e /k /y
xcopy D:\JFlow\jflow-web\src\main\webapp\WF			D:\JFlowSpringBoot\jflow-web\src\main\webapp\WF\ /e /k /y
copy /y D:\JFlow\jflow-web\src\main\webapp\index.htm	D:\JFlowSpringBoot\jflow-web\src\main\webapp\index.htm

pause;

@echo off
echo 更新说明:
echo 1.从JFlow更新后台文件到JFlowVue版本. BP
echo 2.从JFlow更新前台文件到JFlowVue版本.
pause;

rem 更新后台文件. BP
rd /S/Q D:\JFlowVue\docs
rd /S/Q D:\JFlowVue\jflow-core\src\main\java\BP

xcopy D:\JFlow\docs D:\jfinal-jflow\docs /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP  D:\JFlowVue\jflow-core\src\main\java\BP\  /e /k /y
 

pause;

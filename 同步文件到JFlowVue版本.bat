@echo off
echo ����˵��:
echo 1.��JFlow���º�̨�ļ���JFlowVue�汾. BP
echo 2.��JFlow����ǰ̨�ļ���JFlowVue�汾.
pause;

rem ���º�̨�ļ�. BP
rd /S/Q D:\JFlowVue\docs
rd /S/Q D:\JFlowVue\jflow-core\src\main\java\BP

xcopy D:\JFlow\docs D:\jfinal-jflow\docs /e /k /y
xcopy D:\JFlow\jflow-core\src\main\java\BP  D:\JFlowVue\jflow-core\src\main\java\BP\  /e /k /y
 

pause;

@echo off
echo ����˵����
echo 1.��JFlow���º�̨�ļ���Jeesite4�汾. BP
echo 2.��JFlow����ǰ̨�ļ���Jeesite4�汾.

pause

rem ���º�̨�ļ�. BP
rd /S/Q D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP
xcopy /e /k /y D:\JFlow\jflow-core\src\main\java\BP  D:\jeesite4-jflow\JFlow\jflow-core\src\main\java\BP\

rem ���� ǰ̨.

rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\GPM 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\SDKFlowDemo 
rd /S/Q D:\jeesite4-jflow\web\src\main\webapp\WF

xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\GPM		D:\jeesite4-jflow\web\src\main\webapp\GPM\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo	D:\jeesite4-jflow\web\src\main\webapp\SDKFlowDemo\
xcopy /e /k /y D:\JFlow\jflow-web\src\main\webapp\WF			D:\jeesite4-jflow\web\src\main\webapp\WF\

pause;

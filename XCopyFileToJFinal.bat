
-- 更新后台文件. BP

rd /s/q D:\jfinal-jflow\jflow-core\src\main\java\BP\DA

xcopy D:\JFlowForm\jflow-core\src\main\java\BP\DA  D:\jfinal-jflow\jflow-core\src\main\java\BP\DA /e /a

 

-- 更新 前台.

rd /s/q D:\jfinal-jflow\jflow-web\src\main\webapp\WF 
rd /s/q D:\jfinal-jflow\jflow-web\src\main\webapp\GPM 
rd /s/q D:\jfinal-jflow\jflow-web\src\main\webapp\SDKFlowDemo 

xcopy D:\JFlow\jflow-web\src\main\webapp\WF     D:\jfinal-jflow\jflow-web\src\main\webapp\WF /e
xcopy D:\JFlow\jflow-web\src\main\webapp\GPM    D:\jfinal-jflow\jflow-web\src\main\webapp\GPM  /e 
xcopy D:\JFlow\jflow-web\src\main\webapp\SDKFlowDemo    D:\jfinal-jflow\jflow-web\src\main\webapp\SDKFlowDemo  /e 

pause;

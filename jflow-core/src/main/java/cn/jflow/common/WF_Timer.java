package cn.jflow.common;

  
import org.springframework.scheduling.annotation.Scheduled;  
import org.springframework.stereotype.Component;

import BP.DA.Log;
import BP.Tools.StringHelper;
import BP.WF.DTS.AutoRunOverTimeFlow;  
  
/**   
 * @DL.D
 * createDate 2018-05-09
 * 定时任务  
 * 按约定处理逾期流程
 */  
  
@Component  
public class WF_Timer {  
  
   // @Scheduled(cron="0/5 * * * * ? ") //间隔5秒执行    
    @Scheduled(cron="0 0/1 * * * ?") //间隔1分钟执行
    public void startTask(){  
    	AutoRunOverTimeFlow  overTime = new AutoRunOverTimeFlow();
    	String info = "";
    	try {
			info = overTime.Do().toString();
		} catch (Exception e) {
			e.printStackTrace();			
		}
    	if (!StringHelper.isNullOrEmpty(info)) {
    		Log.DebugWriteInfo(info);
		}
    	
    }  
}  

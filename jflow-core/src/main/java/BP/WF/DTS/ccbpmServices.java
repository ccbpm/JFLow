package BP.WF.DTS;

import BP.En.Method;

public class ccbpmServices extends Method{

	 /// <summary>
    /// 不带有参数的方法
    /// </summary>
    public void ccbpmServices()
    {
        this.Title = "ccbpm服务";
        this.Help = "扫描并处理逾期的任务，按照节点配置的预期规则,自动发起流程.";
    }
    /// <summary>
    /// 设置执行变量
    /// </summary>
    /// <returns></returns>
    public  void Init()
    {
    	
    }
    /// <summary>
    /// 当前的操纵员是否可以执行这个方法
    /// </summary>
    public  boolean getIsCanDo()
    {
            return true;
    }
    /// <summary>
    /// 执行
    /// </summary>
    /// <returns>返回执行结果</returns>
    public  Object Do() throws Exception
    {
    	String info="";
    	
    	//执行触发方式发起的流程.
    	AutoRunWF_Task wf_task=new AutoRunWF_Task();
    	wf_task.Do();
    	
    	//执行触发方式发起的流程.
    	AutoRunStratFlows flws=new AutoRunStratFlows();
    	flws.Do();
    	
    	//处理超期的任务.
    	AutoRunOverTimeFlow overTimeFlow=new AutoRunOverTimeFlow();
    	overTimeFlow.Do();
    	
         
        return info;
    }
 
}


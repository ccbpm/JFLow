package BP.WF.DTS;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.Web.WebUser;
import BP.Web.Controls.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 Method 的摘要说明
*/
public class AutoRun_WhoExeIt extends Method
{
	/** 
	 不带有参数的方法
	*/
	public AutoRun_WhoExeIt()
	{
		this.Title = "执行节点的自动任务.";
		this.Help = "对于节点属性里配置的自动执行或者机器执行的节点上的任务自动发送下去。";
		this.GroupName = "流程自动执行定时任务";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		String info = "";
		String sql = "SELECT WorkID,FK_Emp,FK_Node,FK_Flow FROM WF_GenerWorkerList WHERE WhoExeIt!=0 AND IsPass=0 AND IsEnable=1 ORDER BY FK_Emp";
		DataTable dt = null;

		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "无任务";
		}


			///#region 自动启动流程B
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.get("WorkID").toString());
			String FK_Emp = dr.get("FK_Emp").toString();
			int paras = Integer.parseInt(dr.get("FK_Node").toString());
			String fk_flow = dr.get("FK_Flow").toString();

			if (WebUser.getNo().equals(FK_Emp) == false)
			{
				BP.WF.Dev2Interface.Port_Login(FK_Emp);
			}

			try
			{
				info += "发送成功:" + WebUser.getNo() + BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText();
			}
			catch (RuntimeException ex)
			{
				info += "err@发送错误:" + ex.getMessage().toString();
			}
		}

			///#endregion 自动启动流程

		if (WebUser.getNo().equals("admin") == false)
		{
			BP.WF.Dev2Interface.Port_Login("admin");
		}

		return info;
	}
}
package bp.wf.dts;

import bp.da.*;
import bp.en.*;
import bp.wf.*;

/** 
 Method 的摘要说明
*/
public class AutoRun_WhoExeIt extends Method
{
	/** 
	 不带有参数的方法
	*/
	public AutoRun_WhoExeIt()throws Exception
	{
		this.Title = "执行节点的自动任务.";
		this.Help = "对于节点属性里配置的自动执行或者机器执行的节点上的任务自动发送下去。";
		this.GroupName = "流程自动执行定时任务";
	}

	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()throws Exception
	{
		String info = "";
		String sql = "SELECT WorkID,FID,FK_Emp,FK_Node,FK_Flow FROM WF_GenerWorkerList WHERE WhoExeIt!=0 AND IsPass=0 AND IsEnable=1 ORDER BY FK_Emp";
		DataTable dt = null;

		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "无任务";
		}


			///#region 自动启动流程 whoExIt.
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			long fid = Long.parseLong(dr.getValue("FID").toString());
			String empNo = dr.getValue("FK_Emp").toString();
			int paras = Integer.parseInt(dr.getValue("FK_Node").toString());
			String fk_flow = dr.getValue("FK_Flow").toString();

			if (bp.web.WebUser.getNo().equals(empNo) == false)
			{
				Dev2Interface.Port_Login(empNo);
			}

			try
			{
				info += "发送成功:" + bp.web.WebUser.getNo() + Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText();
			}
			catch (RuntimeException ex)
			{
				info += "err@发送错误:" + ex.getMessage().toString();
			}
		}

			///#endregion 自动启动流程

		if (bp.web.WebUser.getNo().equals("admin") == false)
		{
			Dev2Interface.Port_Login("admin");
		}

		return info;
	}


		///#region 重写。
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
		if (bp.web.WebUser.getIsAdmin() == true)
		{
			return true;
		}
		return false;
	}

		///#endregion 重写。

}
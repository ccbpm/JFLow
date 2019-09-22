package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 Method 的摘要说明
*/
public class ClearDB extends Method
{
	/** 
	 不带有参数的方法
	*/
	public ClearDB()
	{
		this.Title = "清除流程运行的数据(此功能要在测试环境里运行)";
		this.Help = "清除所有流程运行的数据，包括待办工作。";
		this.Warning = "此功能要在测试环境里执行，确认是测试环境吗？";
		this.Icon = "<img src='/WF/Img/Btn/Delete.gif'  border=0 />";

		this.GroupName = "流程维护";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
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
	*/
	@Override
	public Object Do()
	{
		if (!BP.Web.WebUser.No.equals("admin"))
		{
			return "非法的用户执行。";
		}

		//DBAccess.RunSQL("DELETE FROM WF_CHOfFlow");

		DBAccess.RunSQL("DELETE FROM WF_Bill");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow");
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork");
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper");
		DBAccess.RunSQL("DELETE FROM WF_TransferCustom");
		DBAccess.RunSQL("DELETE FROM WF_RememberMe");
		DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB");
		DBAccess.RunSQL("DELETE FROM WF_CCList");
		DBAccess.RunSQL("DELETE FROM WF_CH"); //删除考核.

		Flows fls = new Flows();
		fls.RetrieveAll();
		for (Flow item : fls)
		{
			try
			{
				DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(item.No) + "Track");
			}
			catch (java.lang.Exception e)
			{
			}
		}

		Nodes nds = new Nodes();
		for (Node nd : nds)
		{
			try
			{
				Work wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.EnMap.PhysicsTable);
			}
			catch (java.lang.Exception e2)
			{
			}
		}

		MapDatas mds = new MapDatas();
		mds.RetrieveAll();
		for (MapData nd : mds)
		{
			try
			{
				DBAccess.RunSQL("DELETE FROM " + nd.PTable);
			}
			catch (java.lang.Exception e3)
			{
			}
		}

		MapDtls dtls = new MapDtls();
		dtls.RetrieveAll();
		for (MapDtl dtl : dtls)
		{
			try
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.PTable);
			}
			catch (java.lang.Exception e4)
			{
			}
		}
		return "执行成功...";
	}
}
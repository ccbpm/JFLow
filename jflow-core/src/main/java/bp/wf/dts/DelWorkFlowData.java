package bp.wf.dts;

import bp.da.*;
import bp.dts.DataIOEn;
import bp.dts.DoType;
import bp.wf.*;
import bp.web.WebUser;


public class DelWorkFlowData extends DataIOEn
{
	public DelWorkFlowData()throws Exception
	{
		this.HisDoType = DoType.UnName;
		this.Title = "<font color=red><b>清除流程数据</b></font>";

	}
	@Override
	public void Do() throws Exception
	{
		if (!WebUser.getNo().equals("admin"))
		{
			throw new RuntimeException("非法用户。");
		}

		DBAccess.RunSQL("DELETE FROM WF_Bill");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow");
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork");
		DBAccess.RunSQL("DELETE FROM WF_GECheckStand");
		DBAccess.RunSQL("DELETE FROM WF_GECheckMul");
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper");

		// 删除.
		CCLists ens = new CCLists();
		ens.ClearTable();

		Nodes nds = new Nodes();
		nds.RetrieveAll();

		String msg = "";
		for (Node nd : nds.ToJavaList())
		{

			Work wk = null;
			try
			{
				wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.getEnMap().getPhysicsTable());
			}
			catch (RuntimeException ex)
			{
				wk.CheckPhysicsTable();
				msg += "@" + ex.getMessage();
			}
		}

		if (!msg.equals(""))
		{
			throw new RuntimeException(msg);
		}
	}
}
package BP.WF.DTS;

import BP.DA.*;
import BP.Web.WebUser;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;

/** 
 升级ccflow6 要执行的调度
*/
public class Tool_Repair extends Method
{
	/** 
	 不带有参数的方法
	*/
	public Tool_Repair()
	{
		this.Title = "修复因显示不出来到达节点下拉框而导致的，发送不下去的bug引起的垃圾数据";
		this.Help = "此bug已经修复掉了,如果仍然出现类似的问题，有可能是其他问题引起的.";
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
	 * @throws Exception 
	*/
	@Override
	public boolean getIsCanDo() throws Exception
	{
		if (WebUser.getNo().equals("admin"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		String sql = "SELECT * FROM WF_GENERWORKFLOW WHERE WFState=2 ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String msg = "";
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			String todoEmps = dr.getValue("TODOEMPS").toString();
			String nodeID = dr.getValue("FK_NODE").toString();

			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.IsPass, 0);
			for (GenerWorkerList gwl : gwls.ToJavaList())
			{
				if (todoEmps.contains(gwl.getFK_Emp() + ",") == false)
				{
					if (nodeID.toString().endsWith("01") == true)
					{
						continue;
					}

					GenerWorkFlow gwf = new GenerWorkFlow(workid);
					msg += "<br>@流程:" + gwf.getFlowName() + "节点:" + gwf.getFK_Node() + "," + gwf.getNodeName() + " workid: " + workid + "title:" + gwf.getTitle() + " todoEmps:" + gwf.getTodoEmps();
					msg += "不包含:" + gwl.getFK_Emp() + "," + gwl.getFK_EmpText();

					gwf.setTodoEmps(gwf.getTodoEmps() + gwl.getFK_Emp() + "," + gwl.getFK_EmpText() + ";");
					gwf.Update();
				}
			}
		}
		return msg;
	}
}
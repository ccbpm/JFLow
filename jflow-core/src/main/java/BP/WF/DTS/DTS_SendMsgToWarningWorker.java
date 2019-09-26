package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import BP.WF.Port.WFEmp;

/** 
 向预期的工作人员发送提醒消息 的摘要说明
*/
public class DTS_SendMsgToWarningWorker extends Method
{
	/** 
	 向预期的工作人员发送提醒消息
	*/
	public DTS_SendMsgToWarningWorker()
	{
		this.Title = "向预期的工作人员发送提醒消息";
		this.Help = "该方法每天的8点自动执行";
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
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{

		/*查找一天预警1次的消息记录，并执行推送。*/
		String sql = "SELECT A.WorkID, A.Title, A.FlowName, A.TodoSta, B.FK_Emp, b.FK_EmpText, C.WAlertWay  FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Node C  ";
		sql += " WHERE A.WorkID=B.WorkID AND A.FK_Node=C.NodeID AND a.TodoSta=1 AND ( C.WAlertRole=1 OR C.WAlertRole=2 ) ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			CHAlertWay way = CHAlertWay.forValue(Integer.parseInt(dr.get("WAlertWay").toString())); //提醒方式.
			long workid = Long.parseLong(dr.get("WorkID").toString());
			String title = dr.get("Title").toString();
			String flowName = dr.get("FlowName").toString();
			String empNo = dr.get("FK_Emp").toString();
			String empName = dr.get("FK_EmpText").toString();

			WFEmp emp = new WFEmp(empNo);

			if (way == CHAlertWay.ByEmail)
			{
				String titleMail = "";
				String docMail = "";
				//  BP.WF.Dev2Interface.Port_SendEmail(emp.Email, titleMail, "");
			}

			if (way == CHAlertWay.BySMS)
			{
				String titleMail = "";
				String docMail = "";
				//BP.WF.Dev2Interface.Port_SendMsg(emp.Email, titleMail, "");
			}
		}
		return "执行成功...";
	}
}
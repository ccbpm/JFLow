package bp.wf.dts;

import bp.da.*;
import bp.en.*;
import bp.wf.*;

import java.util.Date;


/** 
 更新WF_GenerWorkerFlow.TodoSta状态. 
*/
public class DTS_GenerWorkFlowTodoSta extends Method
{
	/** 
	 更新WF_GenerWorkerFlow.TodoSta状态.
	*/
	public DTS_GenerWorkFlowTodoSta()throws Exception
	{
		this.Title = "更新WF_GenerWorkerFlow.TodoSta状态.";
		this.Help = "该方法每天的8点自动执行";
		this.GroupName = "流程自动执行定时任务";
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
		if (bp.web.WebUser.getIsAdmin() == true)
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()throws Exception
	{
		//系统期望的是，每一个人仅发一条信息.  "您有xx个预警工作，yy个预期工作，请及时处理。”

		DataTable dtEmps = new DataTable();
		dtEmps.Columns.Add("EmpNo", String.class);
		dtEmps.Columns.Add("WarningNum", Integer.class);
		dtEmps.Columns.Add("OverTimeNum", Integer.class);

		String timeDT = DataType.getCurrentDateByFormart("yyyy-MM-dd");
		String sql = "";

		sql = "SELECT * FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ORDER BY FK_Node,FK_Emp ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);


			///#region 向预警人员发消息.
		// 向预警的人员发消息.
		Node nd = new Node();
		bp.wf.port.WFEmp emp = new bp.wf.port.WFEmp();
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			int fk_node = Integer.parseInt(dr.getValue("FK_Node").toString());
			String fk_emp = dr.getValue("FK_Emp").toString();

			if (nd.getNodeID() != fk_node)
			{
				nd.setNodeID(fk_node);
				nd.Retrieve();
			}

			if (nd.getHisCHWay() != CHWay.ByTime)
			{
				continue; //非按照时效考核.
			}

			if (nd.getWAlertRole() == CHAlertRole.None)
			{
				continue;
			}

			//如果仅仅提醒一次.
		   // if (nd.WAlertRole == CHAlertRole.OneDayOneTime && isPM == true)
			if (nd.getWAlertRole() == CHAlertRole.OneDayOneTime && 1 == 1)
			{

			}
			else
			{
				continue;
			}

			if (!emp.getNo().equals(fk_emp))
			{
				emp.setNo(fk_emp);
				emp.Retrieve();
			}
		}

			///#endregion 向预警人员发消息.

		if (dt.Rows.size() >= 1)
		{
			//更新预警状态.
			sql = "UPDATE WF_GenerWorkFlow  SET TodoSta=1 ";
			sql += " WHERE WorkID IN (SELECT WorkID FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ) ";
			sql += " AND WF_GenerWorkFlow.WFState!=3 ";
			sql += " AND WF_GenerWorkFlow.TodoSta=0 ";
			int i = DBAccess.RunSQL(sql);
		}

		//更新逾期期状态.
		sql = "UPDATE WF_GenerWorkFlow  SET TodoSta=2 ";
		sql += " WHERE WorkID IN (SELECT WorkID FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ) ";
		sql += " AND WF_GenerWorkFlow.WFState!=3 ";
		sql += " AND WF_GenerWorkFlow.TodoSta=1 ";
		DBAccess.RunSQL(sql);

		return "时间戳修改成功";
	}
}
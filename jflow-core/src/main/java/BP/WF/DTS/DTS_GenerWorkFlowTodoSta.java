package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import java.time.*;

/** 
 更新WF_GenerWorkerFlow.TodoSta状态. 
*/
public class DTS_GenerWorkFlowTodoSta extends Method
{
	/** 
	 更新WF_GenerWorkerFlow.TodoSta状态.
	*/
	public DTS_GenerWorkFlowTodoSta()
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
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		//系统期望的是，每一个人仅发一条信息.  "您有xx个预警工作，yy个预期工作，请及时处理。"

		DataTable dtEmps = new DataTable();
		dtEmps.Columns.Add("EmpNo", String.class);
		dtEmps.Columns.Add("WarningNum", Integer.class);
		dtEmps.Columns.Add("OverTimeNum", Integer.class);

		String timeDT = LocalDateTime.now().toString("yyyy-MM-dd");
		String sql = "";

		//查询出预警的工作.
		//sql = " SELECT DISTINCT FK_Emp,COUNT(FK_Emp) as Num , 0 as DBType FROM WF_GenerWorkerlist A WHERE a.DTOfWarning =< '" + timeDT + "' AND a.SDT <= '" + timeDT + "' AND A.IsPass=0  ";
		//sql += "  UNION ";
		//sql += "SELECT DISTINCT FK_Emp,COUNT(FK_Emp) as Num , 1 as DBType FROM WF_GenerWorkerlist A WHERE  a.SDT >'" + timeDT + "' AND A.IsPass=0 ";

		sql = "SELECT * FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ORDER BY FK_Node,FK_Emp ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 向预警人员发消息.
		// 向预警的人员发消息.
		Node nd = new Node();
		WFEmp emp = new Port.WFEmp();
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.get("WorkID").toString());
			int fk_node = Integer.parseInt(dr.get("FK_Node").toString());
			String fk_emp = dr.get("FK_Emp").toString();

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

			if (!fk_emp.equals(emp.No))
			{
				emp.setNo (fk_emp;
				emp.Retrieve();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 向预警人员发消息.

		if (dt.Rows.size() >= 1)
		{
			//更新预警状态.
			sql = "UPDATE WF_GenerWorkFlow  SET TodoSta=1 ";
			sql += " WHERE WorkID IN (SELECT WorkID FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ) ";
			sql += " AND WF_GenerWorkFlow.WFState!=3 ";
			sql += " AND WF_GenerWorkFlow.TodoSta=0 ";
			int i = BP.DA.DBAccess.RunSQL(sql);
		}

		//更新逾期期状态.
		sql = "UPDATE WF_GenerWorkFlow  SET TodoSta=2 ";
		sql += " WHERE WorkID IN (SELECT WorkID FROM WF_GenerWorkerlist A WHERE a.DTOfWarning >'" + timeDT + "' AND a.SDT <'" + timeDT + "' AND A.IsPass=0 ) ";
		sql += " AND WF_GenerWorkFlow.WFState!=3 ";
		sql += " AND WF_GenerWorkFlow.TodoSta=1 ";
		BP.DA.DBAccess.RunSQL(sql);

		return "时间戳修改成功";
	}
}
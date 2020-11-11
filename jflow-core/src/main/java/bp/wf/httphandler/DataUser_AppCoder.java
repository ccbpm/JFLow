package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import java.util.*;

/** 
 页面功能实体
*/
public class DataUser_AppCoder extends WebContralBase
{
	/** 
	 构造函数
	*/
	public DataUser_AppCoder()
	{
	}




		///欢迎页面初始化.
	/** 
	 欢迎页面初始化-获得数量.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FlowDesignerWelcome_Init() throws Exception
	{
		String whereStr = "";
		String whereStrPuls = "";

		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			whereStr += " WHERE OrgNo = '" + WebUser.getOrgNo() + "'";
			whereStrPuls += " AND OrgNo = '" + WebUser.getOrgNo() + "'";
		}

		Hashtable ht = new Hashtable();
		ht.put("FlowNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM WF_Flow " + whereStr)); //流程数
		ht.put("NodeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(NodeID) FROM WF_Node " + whereStr)); //节点数据

		//表单数.
		ht.put("FromNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM Sys_MapData  WHERE FK_FormTree !=''" + whereStrPuls + " AND FK_FormTree IS NOT NULL ")); //表单数

		//所有的实例数量.
		ht.put("FlowInstaceNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState >1 " + whereStrPuls)); //实例数.

		//所有的待办数量.
		ht.put("TodolistNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=2 " + whereStrPuls));

		//退回数.
		ht.put("ReturnNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=5 " + whereStrPuls));

		//说有逾期的数量. 应该根据 WF_GenerWorkerList的 SDT 字段来求.
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE IsPass=0 AND STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now()"));

		}
		else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			//@lizhen 这里有问题.
			String sql = "SELECT COUNT(*) from (SELECT *  FROM WF_GenerWorkerList WHERE IsPass=0 AND   REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 ";
			sql += "UNION SELECT * FROM WF_GenerWorkerList WHERE  REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 )";

			ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt(sql));
		}
		else
		{
			ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE IsPass=0 AND convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120)"));
		}
		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 获得数量  流程饼图，部门柱状图，月份折线图.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FlowDesignerWelcome_DataSet() throws Exception
	{
		String whereStr = "";
		String whereStrPuls = "";

		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			whereStr += " WHERE OrgNo = '" + WebUser.getOrgNo() + "'";
			whereStrPuls += " AND OrgNo = '" + WebUser.getOrgNo() + "'";
		}

		DataSet ds = new DataSet();


			/// 实例分析
		//月份分组.
		String sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 " + whereStrPuls + " GROUP BY FK_NY ";
		DataTable FlowsByNY = DBAccess.RunSQLReturnTable(sql);
		FlowsByNY.TableName = "FlowsByNY";
		ds.Tables.add(FlowsByNY);

		//部门分组.
		sql = "SELECT DeptName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 " + whereStrPuls + "  GROUP BY DeptName ";
		DataTable FlowsByDept = DBAccess.RunSQLReturnTable(sql);
		FlowsByDept.TableName = "FlowsByDept";
		ds.Tables.add(FlowsByDept);

			/// 实例分析。


			///待办 分析
		//待办 - 部门分组.
		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT B.Name, count(a.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B WHERE A.FK_Dept=B.No GROUP BY B.Name";
		}
		else
		{
			sql = "SELECT B.Name, count(a.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B WHERE A.FK_Dept=B.No AND B.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY B.Name";
		}
		DataTable TodolistByDept = DBAccess.RunSQLReturnTable(sql);
		TodolistByDept.TableName = "TodolistByDept";
		ds.Tables.add(TodolistByDept);

		//待办的 - 流程分组.
		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT c.FlowName, count(a.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B, WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID AND A.IsPass=0 GROUP BY C.FlowName";
		}
		else
		{
			sql = "SELECT c.FlowName, count(a.WorkID) as Num FROM WF_GenerWorkerList A,Port_Dept B, WF_GenerWorkFlow C WHERE A.FK_Dept=B.No AND A.WorkID=C.WorkID AND A.IsPass=0 AND C.OrgNo='" + WebUser.getOrgNo() + "' GROUP BY C.FlowName";
		}

		//sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 GROUP BY FlowName";
		DataTable TodolistByFlow = DBAccess.RunSQLReturnTable(sql);
		TodolistByFlow.TableName = "TodolistByFlow";
		ds.Tables.add(TodolistByFlow);

			/// 待办。


			///逾期 分析.
		//逾期的 - 流程分组.
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() GROUP BY FlowName";

		}
		else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY FlowName ";
			sql += "UNION SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY FlowName";
		}
		else
		{
			sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) GROUP BY FlowName";
		}
		sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 GROUP BY FlowName";
		DataTable OverTimeByFlow = DBAccess.RunSQLReturnTable(sql);
		OverTimeByFlow.TableName = "OverTimeByFlow";
		ds.Tables.add(OverTimeByFlow);

		//逾期的 - 部门分组.

		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() GROUP BY DeptName";

		}
		else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY DeptName ";
			sql += "UNION SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY DeptName";
		}
		else
		{
			sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) GROUP BY DeptName";
		}
		//sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 GROUP BY DeptName";
		DataTable OverTimeByDept = DBAccess.RunSQLReturnTable(sql);
		OverTimeByDept.TableName = "OverTimeByDept";
		ds.Tables.add(OverTimeByDept);

			/// 逾期。


		return bp.tools.Json.ToJson(ds);
	}

		/// 欢迎页面初始化.

}
package bp.ccbill;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_CCBill_Portal extends WebContralBase
{

		///构造方法.
	/** 
	 构造函数
	*/
	public WF_CCBill_Portal()
	{
	}

		/// 构造方法.


		///欢迎页面初始化.
	/** 
	 欢迎页面初始化
	 
	 @return 
	*/
	public final String Default_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("FlowNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM WF_Flow")); //流程数
		ht.put("NodeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(NodeID) FROM WF_Node")); //节点数据
		ht.put("FromNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM Sys_MapData")); //表单数

		ht.put("FlowInstaceNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState >1 ")); //实例数.
		ht.put("TodolistNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=2 ")); //待办数
		ht.put("ReturnNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=5 ")); //退回数.

		ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=5 ")); //逾期数.

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 获得数据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Default_DataSet() throws Exception
	{
		DataSet ds = new DataSet();

		//月份分组.
		String sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 GROUP BY FK_NY ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "DTNY";
		ds.Tables.add(dt);

		//部门分组.
		sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 GROUP BY DeptName";
		DataTable deptNums = DBAccess.RunSQLReturnTable(sql);
		deptNums.TableName = "DeptNums";
		ds.Tables.add(deptNums);

		//流程分组.
		sql = "SELECT FlowName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 GROUP BY FlowName";
		DataTable flowNums = DBAccess.RunSQLReturnTable(sql);
		flowNums.TableName = "FlowNums";
		ds.Tables.add(flowNums);
		return bp.tools.Json.ToJson(ds);
	}

		/// 欢迎页面初始化.

}
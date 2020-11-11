package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.Glo;
import bp.wf.template.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_RptSearch extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_RptSearch()
	{
	}


		///流程分布.
	public final String DistributedOfMy_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//我发起的流程.
		Paras ps = new Paras();
		ps.SQL="select FK_Flow, FlowName,Count(WorkID) as Num FROM WF_GenerWorkFlow  WHERE Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter GROUP BY FK_Flow, FlowName ";
		ps.Add("Starter", WebUser.getNo());

		//string sql = "";
		//sql = "select FK_Flow, FlowName,Count(WorkID) as Num FROM WF_GenerWorkFlow  WHERE Starter='" + WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		dt.TableName = "Start";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("FK_FLOW").setColumnName("FK_Flow");
			dt.Columns.get("FLOWNAME").setColumnName("FlowName");
			dt.Columns.get("NUM").setColumnName("Num");
		}
		ds.Tables.add(dt);

		//待办.
		ps = new Paras();
		ps.SQL="select FK_Flow, FlowName,Count(WorkID) as Num FROM wf_empworks  WHERE FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp GROUP BY FK_Flow, FlowName ";
		ps.Add("FK_Emp", WebUser.getNo());
		//sql = "select FK_Flow, FlowName,Count(WorkID) as Num FROM wf_empworks  WHERE FK_Emp='" + WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dtTodolist = DBAccess.RunSQLReturnTable(ps);
		dtTodolist.TableName = "Todolist";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtTodolist.Columns.get("FK_FLOW").setColumnName("FK_Flow");
			dtTodolist.Columns.get("FLOWNAME").setColumnName("FlowName");
			dtTodolist.Columns.get("NUM").setColumnName("Num");
		}

		ds.Tables.add(dtTodolist);

		//正在运行的流程.
		DataTable dtRuning = bp.wf.Dev2Interface.DB_TongJi_Runing();
		dtRuning.TableName = "Runing";
		ds.Tables.add(dtRuning);


		//归档的流程.
		DataTable dtOK = bp.wf.Dev2Interface.DB_TongJi_FlowComplete();
		dtOK.TableName = "OK";
		ds.Tables.add(dtOK);

		//返回结果.
		return bp.tools.Json.ToJson(ds);
	}

		///



		///功能列表
	/** 
	 功能列表
	 
	 @return 
	*/
	public final String Default_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("MyStartFlow", "我发起的流程");
		ht.put("MyJoinFlow", "我审批的流程");



		//   ht.Add("MyDeptFlow", "我本部门发起的流程");
		//  ht.Add("MySubDeptFlow", "我本部门与子部门发起的流程");
		// ht.Add("AdvFlowsSearch", "高级查询");

		return bp.tools.Json.ToJsonEntitiesNoNameMode(ht);
	}

		///


		///执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" +CommonUtils.getRequest().getRequestURI());
	}

		/// 执行父类的重写方法.



		///xxx 界面 .

		/// xxx 界面方法.


		///KeySearch.htm
	/** 
	 功能列表
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String KeySearch_Query() throws NumberFormatException, Exception
	{
		String keywords = this.GetRequestVal("TB_KWds");
		//对输入的关键字进行验证
		keywords = Glo.CheckKeyWord(keywords);
		if (Glo.CheckKeyWordInSql(keywords))
		{
			return "@err:请输入正确字符！";
		}

		Paras ps = new Paras();
		ps.SQL="SELECT A.FlowName,A.NodeName,A.FK_Flow,A.FK_Node,A.WorkID,A.FID,A.Title,A.StarterName,A.RDT,A.WFSta,A.Emps, A.TodoEmps, A.WFState " + " FROM WF_GenerWorkFlow A " + " WHERE (A.Title LIKE '%" + keywords + "%' " + " or A.Starter LIKE '%" + keywords + "%' " + " or A.StarterName LIKE '%" + keywords + "%') " + " AND (A.Emps LIKE '@%" + WebUser.getNo() + "%' " + " or A.TodoEmps LIKE '%" + WebUser.getNo() + "%') " + " AND A.WFState!=0 ";

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		dt.TableName = "WF_GenerWorkFlow";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("FLOWNAME").setColumnName("FlowName");
			dt.Columns.get("FK_FLOW").setColumnName("FK_Flow");
			dt.Columns.get("FK_NODE").setColumnName("FK_Node");
			dt.Columns.get("NODENAME").setColumnName("NodeName");
			dt.Columns.get("WORKID").setColumnName("WorkID");
			dt.Columns.get("FID").setColumnName("FID");
			dt.Columns.get("TITLE").setColumnName("Title");
			dt.Columns.get("STARTERNAME").setColumnName("StarterName");
			dt.Columns.get("WFSTA").setColumnName("WFSta");
			dt.Columns.get("EMPS").setColumnName("Emps");
			dt.Columns.get("TODOEMPS").setColumnName("TodoEmps"); //处理人.
			dt.Columns.get("WFSTATE").setColumnName("WFState"); //处理人.
		}
		if (dt != null)
		{
			dt.Columns.Add("TDTime");
			for (DataRow dr : dt.Rows)
			{

				dr.setValue("TDTime", bp.wf.httphandler.CCMobile.GetTraceNewTime(dr.getValue("FK_Flow").toString(), Integer.parseInt(dr.getValue("WorkID").toString()), Integer.parseInt(dr.getValue("FID").toString())));
			}
		}
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 判断是否可以执行当前工作？
	 
	 @return 
	 * @throws Exception 
	*/
	public final String KeySearch_GenerOpenUrl() throws Exception
	{
		if (bp.wf.Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo()) == true)
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
	//打开表单.
	public final String KeySearch_OpenFrm() throws Exception
	{
	   bp.wf.httphandler.WF wf = new WF();
		return wf.Runing_OpenFrm();
	}

		///

}
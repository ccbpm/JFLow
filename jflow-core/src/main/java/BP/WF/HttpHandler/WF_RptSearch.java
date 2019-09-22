package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_RptSearch extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public WF_RptSearch()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 流程分布.
	public final String DistributedOfMy_Init()
	{
		DataSet ds = new DataSet();

		//我发起的流程.
		Paras ps = new Paras();
		ps.SQL = "select FK_Flow, FlowName,Count(WorkID) as Num FROM WF_GenerWorkFlow  WHERE Starter=" + SystemConfig.AppCenterDBVarStr + "Starter GROUP BY FK_Flow, FlowName ";
		ps.Add("Starter", BP.Web.WebUser.No);

		//string sql = "";
		//sql = "select FK_Flow, FlowName,Count(WorkID) as Num FROM WF_GenerWorkFlow  WHERE Starter='" + BP.Web.WebUser.No + "' GROUP BY FK_Flow, FlowName ";
		System.Data.DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		dt.TableName = "Start";
		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			dt.Columns["FK_FLOW"].ColumnName = "FK_Flow";
			dt.Columns["FLOWNAME"].ColumnName = "FlowName";
			dt.Columns["NUM"].ColumnName = "Num";
		}
		ds.Tables.Add(dt);

		//待办.
		ps = new Paras();
		ps.SQL = "select FK_Flow, FlowName,Count(WorkID) as Num FROM wf_empworks  WHERE FK_Emp=" + SystemConfig.AppCenterDBVarStr + "FK_Emp GROUP BY FK_Flow, FlowName ";
		ps.Add("FK_Emp", BP.Web.WebUser.No);
		//sql = "select FK_Flow, FlowName,Count(WorkID) as Num FROM wf_empworks  WHERE FK_Emp='" + BP.Web.WebUser.No + "' GROUP BY FK_Flow, FlowName ";
		System.Data.DataTable dtTodolist = BP.DA.DBAccess.RunSQLReturnTable(ps);
		dtTodolist.TableName = "Todolist";
		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			dtTodolist.Columns["FK_FLOW"].ColumnName = "FK_Flow";
			dtTodolist.Columns["FLOWNAME"].ColumnName = "FlowName";
			dtTodolist.Columns["NUM"].ColumnName = "Num";
		}

		ds.Tables.Add(dtTodolist);

		//正在运行的流程.
		System.Data.DataTable dtRuning = BP.WF.Dev2Interface.DB_TongJi_Runing();
		dtRuning.TableName = "Runing";
		ds.Tables.Add(dtRuning);


		//归档的流程.
		System.Data.DataTable dtOK = BP.WF.Dev2Interface.DB_TongJi_FlowComplete();
		dtOK.TableName = "OK";
		ds.Tables.Add(dtOK);

		//返回结果.
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 功能列表
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

		return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
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
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region xxx 界面 .
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion xxx 界面方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region KeySearch.htm
	/** 
	 功能列表
	 
	 @return 
	*/
	public final String KeySearch_Query()
	{
		String keywords = this.GetRequestVal("TB_KWds");
		//对输入的关键字进行验证
		keywords = Glo.CheckKeyWord(keywords);
		if (Glo.CheckKeyWordInSql(keywords))
		{
			return "@err:请输入正确字符！";
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT A.FlowName,A.NodeName,A.FK_Flow,A.FK_Node,A.WorkID,A.FID,A.Title,A.StarterName,A.RDT,A.WFSta,A.Emps, A.TodoEmps, A.WFState "
				+ " FROM WF_GenerWorkFlow A "
				+ " WHERE (A.Title LIKE '%" + keywords + "%' "
				+ " or A.Starter LIKE '%" + keywords + "%' "
				+ " or A.StarterName LIKE '%" + keywords + "%') "
				+ " AND (A.Emps LIKE '@%" + WebUser.No + "%' "
				+ " or A.TodoEmps LIKE '%" + WebUser.No + "%') "
				+ " AND A.WFState!=0 ";

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		dt.TableName = "WF_GenerWorkFlow";

		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			dt.Columns["FLOWNAME"].ColumnName = "FlowName";
			dt.Columns["FK_FLOW"].ColumnName = "FK_Flow";
			dt.Columns["FK_NODE"].ColumnName = "FK_Node";
			dt.Columns["NODENAME"].ColumnName = "NodeName";
			dt.Columns["WORKID"].ColumnName = "WorkID";
			dt.Columns["FID"].ColumnName = "FID";
			dt.Columns["TITLE"].ColumnName = "Title";
			dt.Columns["STARTERNAME"].ColumnName = "StarterName";
			dt.Columns["WFSTA"].ColumnName = "WFSta";
			dt.Columns["EMPS"].ColumnName = "Emps";
			dt.Columns["TODOEMPS"].ColumnName = "TodoEmps"; //处理人.
			dt.Columns["WFSTATE"].ColumnName = "WFState"; //处理人.
		}
		if (dt != null)
		{
			dt.Columns.Add("TDTime");
			for (DataRow dr : dt.Rows)
			{

				dr.set("TDTime", BP.WF.HttpHandler.CCMobile.GetTraceNewTime(dr.get("FK_Flow").toString(), Integer.parseInt(dr.get("WorkID").toString()), Integer.parseInt(dr.get("FID").toString())));
			}
		}
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 判断是否可以执行当前工作？
	 
	 @return 
	*/
	public final String KeySearch_GenerOpenUrl()
	{
		if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.No) == true)
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
	//打开表单.
	public final String KeySearch_OpenFrm()
	{
	   BP.WF.HttpHandler.WF wf = new WF();
		return wf.Runing_OpenFrm();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}
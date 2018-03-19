package BP.WF.HttpHandler;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.SystemConfig;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.Web.WebUser;

public class WF_RptSearch extends WebContralBase{
	/**
	 * 流程分布
	 * @return
	 */
	public final String DistributedOfMy_Init()
	{
		DataSet ds = new DataSet();

		//我发起的流程.
		String sql = "";
		sql = "select FK_Flow, FlowName,Count(WorkID) as Num FROM WF_GenerWorkFlow  WHERE Starter='" + BP.Web.WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Start";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("NUM").ColumnName = "Num";
		}
		ds.Tables.add(dt);

		//待办.
		sql = "select FK_Flow, FlowName,Count(WorkID) as Num FROM wf_empworks  WHERE FK_Emp='" + BP.Web.WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dtTodolist = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtTodolist.TableName = "Todolist";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dtTodolist.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dtTodolist.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dtTodolist.Columns.get("NUM").ColumnName = "Num";
		}

		ds.Tables.add(dtTodolist);

		//正在运行的流程.
		DataTable dtRuning = BP.WF.Dev2Interface.DB_TongJi_Runing();
		dtRuning.TableName = "Runing";
		ds.Tables.add(dtRuning);


		//归档的流程.
		DataTable dtOK = BP.WF.Dev2Interface.DB_TongJi_FlowComplete();
		dtOK.TableName = "OK";
		ds.Tables.add(dtOK);

		//返回结果.
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	
	public final String Default_Init()
	{
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("MyStartFlow", "我发起的流程");
		ht.put("MyJoinFlow", "我参与的流程");



		//   ht.Add("MyDeptFlow", "我本部门发起的流程");
		//  ht.Add("MySubDeptFlow", "我本部门与子部门发起的流程");
		// ht.Add("AdvFlowsSearch", "高级查询");

		return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
	}

	 //打开表单.
    public String KeySearch_OpenFrm()
    {
       BP.WF.HttpHandler.WF wf=new WF(this.context);
        return wf.Runing_OpenFrm();
    }

    /** 功能列表
	 
	 @return 
*/
	public final String KeySearch_Query()
	{
		String type = this.GetRequestVal("type");
		if (DataType.IsNullOrEmpty(type))
		{
			type = "ByTitle";
		}

		String keywords = this.GetValFromFrmByKey("TB_KeyWords");
		if (DataType.IsNullOrEmpty(keywords)==true)
		{
			keywords = this.GetRequestVal("TB_Key");
		}

		int myselft = this.GetRequestValInt("CHK_Myself");
		String sql = "";

//		switch (type)
//ORIGINAL LINE: case "ByWorkID":
		if (type.equals("ByWorkID"))
		{
				if (myselft == 1)
				{
					sql = "SELECT FlowName,NodeName,FK_Flow,FK_Node,WorkID,Title,Starter,RDT,WFSta,Emps,TodoEmps,IsCanDo FROM WF_GenerWorkFlow WHERE  WorkID=" + keywords + " AND Emps LIKE '@%" + WebUser.getNo() + "%'";
				}
				else
				{
					sql = "SELECT FlowName,NodeName,FK_Flow,FK_Node,WorkID,Title,Starter,RDT,WFSta,Emps,TodoEmps,IsCanDo FROM WF_GenerWorkFlow WHERE  WorkID=" + keywords;
				}
		}
//ORIGINAL LINE: case "ByTitle":
		else if (type.equals("ByTitle"))
		{
				sql = "SELECT A.FlowName,A.NodeName,A.FK_Flow,A.FK_Node,A.WorkID,A.Title,A.Starter,A.RDT,A.WFSta,A.Emps, A.TodoEmps, A.WFState ";
				sql += " FROM WF_GenerWorkFlow A ";
				sql += " WHERE A.Title LIKE '%" + keywords + "%' ";
				sql += " AND A.Title LIKE '%" + keywords + "%' ";
				sql += " AND A.Emps LIKE '@%" + WebUser.getNo() + "%' ";
				// sql += " AND ( A.Emps LIKE '@%" + WebUser.No + "%' OR B.FK_Emp='" + WebUser.No + "') ";
			  //  sql += " AND A.WorkID=B.WorkID ";
				sql += " AND A.WFState!=0 ";
				
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_GenerWorkFlow";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("WFSTA").ColumnName = "WFSta";

			dt.Columns.get("EMPS").ColumnName = "Emps";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps"; //处理人.
			dt.Columns.get("WFSTATE").ColumnName = "WFState"; //处理人.

			//  dt.Columns["ISCANDO"].ColumnName = "IsCanDo"; //是否可以处理？
		}

		///#region 加入当前用户信息.
		DataTable mydt = new DataTable();
		mydt.Columns.Add("No");
		mydt.Columns.Add("Name");
		mydt.Columns.Add("FK_Dept");

		DataRow dr = mydt.NewRow();
		dr.setValue2017("No", WebUser.getNo());
		dr.setValue2017("Name",WebUser.getName());
		dr.setValue2017("FK_Dept",WebUser.getFK_Dept());
		mydt.Rows.add(dr);
		mydt.TableName = "WebUser";
		///#endregion 加入当前用户信息.


		DataSet ds = new DataSet();
		ds.Tables.add(mydt);
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}
}

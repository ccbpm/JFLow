package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class CCMobile_RptSearch extends WebContralBase
{
	
	/**
	 * 构造函数
	 */
	public CCMobile_RptSearch()
	{
	
	}
	

	protected String DtlFieldUp(){
		return "执行成功.";
	}

	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Default_Init() throws Exception
	{
		DataSet ds = new DataSet();
		String sql = "";

		String tSpan = this.GetRequestVal("TSpan");
		if (tSpan.equals(""))
		{
			tSpan = null;
		}


			///#region 1、获取时间段枚举/总数.
		SysEnums ses = new SysEnums("TSpan");
		DataTable dtTSpan = ses.ToDataTableField();
		dtTSpan.TableName = "TSpan";
		ds.Tables.add(dtTSpan);

		if (this.getFK_Flow() == null)
		{
			sql = "SELECT  TSpan as No, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY TSpan";
		}
		else
		{
			sql = "SELECT  TSpan as No, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getFK_Flow() + "' AND Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY TSpan";
		}

		DataTable dtTSpanNum = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow drEnum : dtTSpan.Rows)
		{
			String no = drEnum.getValue("IntKey").toString();
			for (DataRow dr : dtTSpanNum.Rows)
			{
				if (dr.getValue("No").toString().equals(no))
				{
					drEnum.setValue("Lab",drEnum.getValue("Lab").toString() + "(" + dr.getValue("Num") + ")");
					break;
				}
			}
		}

			///#endregion


			///#region 2、处理流程类别列表.

		if (tSpan == null)
		{
			sql = "SELECT  FK_Flow as No, FlowName as Name, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE  Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY FK_Flow, FlowName";
		}
		else
		{
			sql = "SELECT  FK_Flow as No, FlowName as Name, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE TSpan=" + tSpan + " AND Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY FK_Flow, FlowName";
		}


		DataTable dtFlows = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dtFlows.Columns.get(0).ColumnName = "No";
			dtFlows.Columns.get(1).ColumnName = "Name";
			dtFlows.Columns.get(2).ColumnName = "Num";
		}
		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);

			///#endregion


			///#region 3、处理流程实例列表.

		GenerWorkFlows gwfs = new GenerWorkFlows();
		BP.En.QueryObject qo = new QueryObject(gwfs);
		qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", "%" + BP.Web.WebUser.getNo() + "%");

		if (tSpan != null)
		{
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.TSpan, tSpan);
		}

		if (this.getFK_Flow() != null)
		{
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.FK_Flow, this.getFK_Flow());
		}

		qo.setTop(50);


		DataTable mydt = null;
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			qo.DoQuery();
			mydt = gwfs.ToDataTableField("WF_GenerWorkFlow");
		}
		else
		{
			mydt = qo.DoQueryToTable();
			mydt.TableName = "WF_GenerWorkFlow";
		}

			///#endregion

		//foreach (DataRow dr in mydt.Rows)
		//{
		//    if (dr.getValue(GenerWorkFlowAttr.TodoEmps).ToString().Contains(WebUser.getNo()) == true)
		//    {
		//    }
		//}

		ds.Tables.add(mydt);

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Default_Search() throws Exception
	{
		String TSpan = this.GetRequestVal("TSpan");
		String FK_Flow = this.GetRequestVal("FK_Flow");

		GenerWorkFlows gwfs = new GenerWorkFlows();
		QueryObject qo = new QueryObject(gwfs);
		qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", "%" + BP.Web.WebUser.getNo() + "%");
		if (!DotNetToJavaStringHelper.isNullOrEmpty(TSpan))
		{
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.TSpan, this.GetRequestVal("TSpan"));
		}
		if (!DotNetToJavaStringHelper.isNullOrEmpty(FK_Flow))
		{
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.FK_Flow, this.GetRequestVal("FK_Flow"));
		}
		qo.setTop(50);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			qo.DoQuery();
			DataTable dt = gwfs.ToDataTableField("Ens");
			return BP.Tools.Json.ToJson(dt);
		}
		else
		{
			DataTable dt = qo.DoQueryToTable();
			return BP.Tools.Json.ToJson(dt);
		}
	}

	/** 
	 获取退回
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DB_GenerReturnWorks() throws Exception
	{
		CCMobile cc = new CCMobile();
		return cc.DB_GenerReturnWorks();
	}
}
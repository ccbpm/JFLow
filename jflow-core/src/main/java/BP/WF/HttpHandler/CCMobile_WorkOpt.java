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
public class CCMobile_WorkOpt extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public CCMobile_WorkOpt()
	{
	}
	/** 
	 打包下载
	 
	 @return 
	*/
	public final String Packup_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.Packup_Init();
	}
	/** 
	 选择接受人
	 
	 @return 
	*/
	public final String HuiQian_SelectEmps()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.HuiQian_SelectEmps();
	}


		///#region 审核组件.
	public final String WorkCheck_GetNewUploadedAths()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.WorkCheck_GetNewUploadedAths();
	}
	public final String WorkCheck_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.WorkCheck_Init();
	}
	public final String WorkCheck_Save()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.WorkCheck_Save();
	}

		///#endregion 审核组件


		///#region 会签.
	public final String HuiQian_AddEmps()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.HuiQian_AddEmps();
	}
	public final String HuiQian_Delete()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.HuiQian_Delete();
	}
	public final String HuiQian_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.HuiQian_Init();
	}
	public final String HuiQian_SaveAndClose()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.HuiQian_SaveAndClose();
	}

		///#endregion 会签


		///#region 接收人选择器(限定接受人范围的).
	public final String Accepter_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.Accepter_Init();
	}
	public final String Accepter_Save()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.Accepter_Save();
	}
	public final String Accepter_Send()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.Accepter_Send();
	}

		///#endregion 接收人选择器(限定接受人范围的).


		///#region 接收人选择器(通用).
	public final String AccepterOfGener_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.AccepterOfGener_Init();
	}
	public final String AccepterOfGener_AddEmps()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.AccepterOfGener_AddEmps();
	}
	public final String AccepterOfGener_Delete()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.AccepterOfGener_Delete();
	}
	public final String AccepterOfGener_Send()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.AccepterOfGener_Send();
	}

	//string flowNo, Int64 workID, int unSendToNode = 0
	public final String AccepterOfGener_UnSend()
	{
		return Dev2Interface.Flow_DoUnSend(this.GetRequestVal("flowNo"), Integer.parseInt(this.GetRequestVal("WorkID")));
	}

		///#endregion 接收人选择器(通用).


		///#region 选择人员(通用).
	/** 
	 将要去掉.
	 
	 @return 
	*/
	public final String SelectEmps()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.SelectEmps_Init();
	}
	public final String SelectEmps_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.SelectEmps_Init();
	}

		///#endregion 选择人员(通用).

	/** 
	 抄送初始化.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CC_Init() throws Exception
	{
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		Hashtable ht = new Hashtable();
		ht.put("Title", gwf.getTitle());

		//计算出来曾经抄送过的人.
		Paras ps = new Paras();
		ps.SQL = "SELECT CCToName FROM WF_CCList WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("FK_Node",this.getFK_Node());
		ps.Add("WorkID",this.getWorkID());
		//string sql = "SELECT CCToName FROM WF_CCList WHERE FK_Node=" + this.FK_Node + " AND WorkID=" + this.WorkID;

		DataTable mydt = DBAccess.RunSQLReturnTable(ps);
		String toAllEmps = "";
		for (DataRow dr : mydt.Rows)
		{
			toAllEmps += dr.get(0).toString() + ",";
		}

		ht.put("CCTo", toAllEmps);

		// 根据他判断是否显示权限组。
		if (BP.DA.DBAccess.IsExitsObject("GPM_Group") == true)
		{
			ht.put("IsGroup", "1");
		}
		else
		{
			ht.put("IsGroup", "0");
		}

		//返回流程标题.
		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	/** 
	 选择部门呈现信息.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CC_SelectDepts() throws Exception
	{
		BP.Port.Depts depts = new BP.Port.Depts();
		depts.RetrieveAll();
		return depts.ToJson();
	}
	/** 
	 选择部门呈现信息.
	 
	 @return 
	*/
	public final String CC_SelectStations()
	{
		//岗位类型.
		String sql = "SELECT NO,NAME FROM Port_StationType ORDER BY NO";
		DataSet ds = new DataSet();
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Port_StationType";
		ds.Tables.add(dt);

		//岗位.
		String sqlStas = "SELECT NO,NAME,FK_STATIONTYPE FROM Port_Station ORDER BY FK_STATIONTYPE,NO";
		DataTable dtSta = BP.DA.DBAccess.RunSQLReturnTable(sqlStas);
		dtSta.TableName = "Port_Station";
		ds.Tables.add(dtSta);
		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 抄送发送.
	 
	 @return 
	*/
	public final String CC_Send()
	{
		//人员信息. 格式 zhangsan,张三;lisi,李四;
		String emps = this.GetRequestVal("Emps");

		//岗位信息. 格式:  001,002,003,
		String stations = this.GetRequestVal("Stations");
		stations = stations.replace(";", ",");

		//权限组. 格式:  001,002,003,
		String groups = this.GetRequestVal("Groups");
		if (groups == null)
		{
			groups = "";
		}
		groups = groups.replace(";", ",");

		//部门信息.  格式: 001,002,003,
		String depts = this.GetRequestVal("Depts");
		//标题.
		String title = this.GetRequestVal("TB_Title");
		//内容.
		String doc = this.GetRequestVal("TB_Doc");

		//调用抄送接口执行抄送.
		String ccRec = BP.WF.Dev2Interface.Node_CC_WriteTo_CClist(this.getFK_Node(), this.getWorkID(), title, doc, emps, depts, stations, groups);

		if (ccRec.equals(""))
		{
			return "没有抄送到任何人。";
		}
		else
		{
			return "本次抄送给如下人员：" + ccRec;
		}

		//return "执行抄送成功.emps=(" + emps + ")  depts=(" + depts + ") stas=(" + stations + ") 标题:" + title + " ,抄送内容:" + doc;
	}

		///#region 退回.
	public final String Return_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.Return_Init();
	}
	//执行退回.
	public final String DoReturnWork()
	{
		WF_WorkOpt en = new WF_WorkOpt();
		return en.DoReturnWork();
	}

		///#endregion 退回.


		///#region xxx 界面 .

		///#endregion xxx 界面方法.
}
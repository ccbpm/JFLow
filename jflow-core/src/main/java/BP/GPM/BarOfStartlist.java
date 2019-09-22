package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 我发起的流程
*/
public class BarOfStartlist extends BarBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 系统属性.
	/** 
	 流程编号/流程标记.
	*/
	@Override
	public String getNo()
	{
		return this.toString();
	}
	/** 
	 名称
	*/
	@Override
	public String getName()
	{
		return "我发起的流程";
	}
	/** 
	 权限控制-是否可以查看
	*/
	@Override
	public boolean getIsCanView()
	{
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			return true;
		}
		return false;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 系统属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 外观行为.
	/** 
	 标题
	*/
	@Override
	public String getTitle()
	{
		return "我发起的流程";
	}
	/** 
	 更多连接
	*/
	@Override
	public String getMore()
	{
		return "<a href='/WF/Start.htm' target=_self >我要发起流程</a>";
	}
	/** 
	 内容信息
	*/
	@Override
	public String getDocuments()
	{
		Paras ps = new Paras();
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			ps.SQL = "SELECT top 9 Title,RDT,FK_Flow,WorkID,FK_Node,Sender FROM WF_GenerWorkFlow WHERE Starter=" + ps.DBStr + "FK_Emp ORDER BY WorkID ";
		}

		ps.AddFK_Emp();

		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		String html = "<table style='width:100%;'>";
		int idx = 0;
		for (DataRow dr : dt.Rows)
		{
			String fk_flow = dr.get("FK_Flow").toString();
			String workID = dr.get("WorkID").toString();
			String nodeID = dr.get("FK_Node").toString();
			String title = dr.get("Title").toString();
			String sender = dr.get("Sender").toString();
			String rdt = dr.get("RDT").toString();
			idx++;

			html += "<tr>";
			html += "<td>" + idx + "</td>";
			html += "<td><a href='../WF/WFRpt.htm?FK_Flow=" + fk_flow + "&WorkID=" + workID + "&FK_Node=" + nodeID + "&1=2' target=_blank >" + title + "</a></td>";
			html += "<td>" + sender + "</td>";
			html += "</tr>";
		}
		html += "</table>";
		return html;
	}
	/** 
	 宽度
	*/
	@Override
	public String getWidth()
	{
		return "300";
	}
	/** 
	 高度
	*/
	@Override
	public String getHeight()
	{
		return "200";
	}
	/** 
	 是否整行显示
	*/
	@Override
	public boolean getIsLine()
	{
		return true;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 外观行为.
}
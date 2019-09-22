package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 流程待办
*/
public class BarOfTodolist extends BarBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 系统属性.
	/** 
	 标记
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
		return "待办流程";
	}
	/** 
	 权限控制-是否可以查看
	*/
	@Override
	public boolean getIsCanView()
	{
		if (BP.Web.WebUser.No.equals("admin"))
		{
			return true; //任何人都可以看到.
		}
		else
		{
			return false;
		}
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
		return "待办";
	}
	/** 
	 更多连接
	*/
	@Override
	public String getMore()
	{
		return "<a href='/WF/Todolist.htm' target=_self >更多</a>";
	}
	/** 
	 内容信息
	*/
	@Override
	public String getDocuments()
	{
		String sql = "select A.WorkID, A.FK_Flow, A.FK_Node, A.Title , A.Sender, A.RDT FROM WF_GenerWorkFlow A , WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.IsPass=0 AND B.FK_Emp='" + Web.WebUser.No + "' ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.Count == 0)
		{
			return "无待办工作...";
		}

		String html = "<table>";

		int idx = 0;
		for (DataRow dr : dt.Rows)
		{
			if (idx == 8)
			{
				break;
			}

			String fk_flow = dr.get("FK_Flow").toString();
			String workID = dr.get("WorkID").toString();
			String nodeID = dr.get("FK_Node").toString();
			String title = dr.get("Title").toString();
			String sender = dr.get("Sender").toString();
			String rdt = dr.get("RDT").toString();

			idx++;
			html += "<tr>";
			html += "<td>" + idx + "</td>";
			html += "<td><a href='../../WF/MyFlow.htm?FK_Flow=" + fk_flow + "&WorkID=" + workID + "&FK_Node=" + nodeID + "&1=2'  target=_blank  >" + title + "</a></td>";
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
	@Override
	public boolean getIsLine()
	{
		return false;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 外观行为.
}
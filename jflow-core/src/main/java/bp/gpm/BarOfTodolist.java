package bp.gpm;
import bp.da.*;
import bp.web.WebUser;

/** 
 流程待办
*/
public class BarOfTodolist extends BarBase
{

		///系统属性.
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
	 * @throws Exception 
	*/
	@Override
	public boolean getIsCanView() throws Exception
	{
		if (WebUser.getNo().equals("admin") == true)
		{
			return true; //任何人都可以看到.
		}
		else
		{
			return false;
		}
	}

		/// 系统属性.


		///外观行为.
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
	 * @throws Exception 
	*/
	@Override
	public String getDocuments() throws Exception
	{
		String sql = "select A.WorkID, A.FK_Flow, A.FK_Node, A.Title , A.Sender, A.RDT FROM WF_GenerWorkFlow A , WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.IsPass=0 AND B.FK_Emp='" + WebUser.getNo() + "' ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
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

			String fk_flow = dr.getValue("FK_Flow").toString();
			String workID = dr.getValue("WorkID").toString();
			String nodeID = dr.getValue("FK_Node").toString();
			String title = dr.getValue("Title").toString();
			String sender = dr.getValue("Sender").toString();
			String rdt = dr.getValue("RDT").toString();

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

		/// 外观行为.
}
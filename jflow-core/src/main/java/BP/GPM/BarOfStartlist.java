package BP.GPM;

import BP.DA.*;

/**
 * 流程待办
 * 
 */
public class BarOfStartlist extends BarBase {
	/// #region 系统属性.
	/**
	 * 流程编号/流程标记.
	 * 
	 */
	@Override
	public String getNo() {
		return this.getClass().getName().toString();
	}

	/**
	 * 名称
	 * 
	 */
	@Override
	public String getName() {
		return "发起流程";
	}

	/**
	 * 权限控制-是否可以查看
	 * 
	 */
	@Override
	public boolean getIsCanView() {
		return true; // 任何人都可以看到.
	}

	/**
	 * 标题
	 * 
	 */
	@Override
	public String getTitle() {
		return "发起流程";
	}

	/**
	 * 更多连接
	 * @throws Exception 
	 * 
	 */
	@Override
	public String getMore() {
		try {
			return "<a href='/WF/Start.htm' target=_self >更多(" + BP.WF.Dev2Interface.getTodolist_EmpWorks() + ")</a>";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "<a href='/WF/Start.htm' target=_self >更多</a>";
		}
		
	}

	/**
	 * 内容信息
	 * 
	 */
	@Override
	public String getDocuments() {

		String sql="";
		try {
			sql = "select A.WorkID, A.FK_Flow, A.FK_Node, A.Title , A.Sender, A.RDT from WF_GenerWorkFlow A , WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.IsPass=0 AND B.FK_Emp='"
					+ BP.Web.WebUser.getNo() + "' ";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String html = "<ul>";
		int count = 0;
		for (DataRow dr : dt.Rows) {
			String fk_flow = dr.getValue("FK_Flow").toString();
			String workID = dr.getValue("WorkID").toString();
			String nodeID = dr.getValue("FK_Node").toString();
			String title = dr.getValue("Title").toString();
			String sender = dr.getValue("Sender").toString();
			String rdt = dr.getValue("RDT").toString();
			count++;
			if (count > 6) {
				html += "<li style='list-style-type:none'>更多....</li>";
			} else {
				html += "<li style='list-style-type:none'>" + count + ".<a href='MyFlow.htm?FK_Flow=" + fk_flow
						+ "&WorkID=" + workID + "&FK_Node=" + nodeID + "&1=2'>" + title + "</a></li>";
			}

		}
		html += "</ul>";
		return html;
	}

	/**
	 * 宽度
	 * 
	 */
	@Override
	public String getWidth() {
		return "300";
	}

	/**
	 * 高度
	 * 
	 */
	@Override
	public String getHeight() {
		return "200";
	}
	/// #endregion 外观行为.
}
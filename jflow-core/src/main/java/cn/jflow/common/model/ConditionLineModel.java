package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataTable;

public class ConditionLineModel extends BaseModel {

	public ConditionLineModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public final String getCondType() {
		return getParameter("CondType");
	}

	public final String getFK_Flow() {
		return getParameter("FK_Flow");
	}

	public final String getFK_MainNode() {
		return getParameter("FK_MainNode");
	}

	/*
	 * public String getFK_Node() { return getParameter("FK_Node"); }
	 */

	public String getFK_Attr() {
		return getParameter("FK_Attr");
	}

	public String getDirType() {
		return getParameter("DirType");
	}

	public final String getToNodeId() {
		return getParameter("ToNodeId");
	}

	public final DataTable Page_Load() {

		StringBuilder sql = new StringBuilder();
		//sunxd 
		//问题:不同类型的数据库执行以下语句所返回的字段名称不一样(oracle 返回的字段名称全部为大写)，导致前端获取时获取不到。
		//解决:为了保证满足所有不同类型的数据库统一，将别名加上“”号
		sql.append("SELECT wd.Node \"Node\",");
		sql.append("       wn2.Name \"NodeName\",");
		sql.append("       wd.ToNode \"ToNode\",");
		sql.append("       wn.Name  \"ToNodeName\",");
		sql.append("       wd.DirType \"DirType\"");
		sql.append("  FROM   WF_Direction wd");
		sql.append("       INNER JOIN WF_Node wn");
		sql.append("            ON  wn.NodeID = wd.ToNode");
		sql.append("       INNER JOIN WF_Node wn2");
		sql.append("            ON  wn2.NodeID = wd.Node");
		sql.append("  WHERE  wd.Node = " + getFK_Node());
		
		return  DBAccess.RunSQLReturnTable(sql.toString());
	}

}

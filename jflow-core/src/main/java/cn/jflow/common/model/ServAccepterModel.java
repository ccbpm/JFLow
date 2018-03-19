package cn.jflow.common.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Tools.StringHelper;
import BP.WF.Template.Selector;
import BP.Web.WebUser;

public class ServAccepterModel {
	StringBuilder result = new StringBuilder();
	StringBuilder sb = new StringBuilder();
	HttpServletRequest req;
	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletResponse getRes() {
		return res;
	}

	public void setRes(HttpServletResponse res) {
		this.res = res;
	}

	HttpServletResponse res;

	public ServAccepterModel() {

	}

	public ServAccepterModel(HttpServletRequest req, HttpServletResponse res,
			String FK_Station, String FK_Dept, String Name, String ToNode,
			String WorkID, String FK_Node, boolean IsReusable) {
		this.req = req;
		this.res = res;
		this.FK_Station = FK_Station;
		this.FK_Dept = FK_Dept;
		this.Name = Name;
		this.ToNode = ToNode;
		this.WorkID = WorkID;
		this.FK_Node = FK_Node;
		this.IsReusable = IsReusable;
	}

	public void OutHtml(String msg) throws IOException {
		// 组装ajax字符串格式,返回调用客户端
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setDateHeader("Expires", 0);
		res.getWriter().write(msg);
		res.flushBuffer();
		// MyContext.Response.End();
	}

	public String getUTF8ToString(String param)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(req.getParameter(param), "utf-8");
		// return HttpUtility.UrlDecode(MyContext.Request[param],
		// System.Text.Encoding.UTF8);
	}

	// 岗位ID
	public String FK_Station;
	// 部门ID
	public String FK_Dept;
	// 查询的名称
	public String Name;
	// 要到达的节点
	public String ToNode;
	public String getFK_Station() {
		return FK_Station;
	}

	public void setFK_Station(String fK_Station) {
		FK_Station = fK_Station;
	}

	public String getFK_Dept() {
		return FK_Dept;
	}

	public void setFK_Dept(String fK_Dept) {
		FK_Dept = fK_Dept;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getToNode() {
		return ToNode;
	}

	public void setToNode(String toNode) {
		ToNode = toNode;
	}

	public String getWorkID() {
		return WorkID;
	}

	public void setWorkID(String workID) {
		WorkID = workID;
	}

	public String getFK_Node() {
		return FK_Node;
	}

	public void setFK_Node(String fK_Node) {
		FK_Node = fK_Node;
	}

	public boolean isIsReusable() {
		return IsReusable;
	}

	public void setIsReusable(boolean isReusable) {
		IsReusable = isReusable;
	}

	public Selector getMySelector() {
		return MySelector;
	}

	public void setMySelector(Selector mySelector) {
		MySelector = mySelector;
	}

	// 工作ID
	public String WorkID;
	// 当前节点ID
	public String FK_Node;
	public boolean IsReusable;
	public Selector MySelector = null;
	private int page;
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	private int rows;

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String ProcessRequest(HttpServletResponse respone,String type) throws Exception {
		res = respone;
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/plain");
		String strResponse = null;
		int page=0;
			page = this.page; // 当前页码
		int rows = this.rows; // 传当前页
		String strtype = type;
		switch (Integer.parseInt(strtype)) {
		case 2:
			// 获取部门树
			strResponse = GetDeptTree();
			break;
		case 3:
			// 获取岗位树
			strResponse = GetStationTree();
			break;
		default:
			// 获取人员列表
			strResponse = GetEmp(strtype, page, rows);
			break;

		}
		res.getWriter().write(strResponse);
		return strResponse;
	}

	public String GetEmp(String type, int page, int rows) throws IOException {
		String val = "";
		// 第一次加载将不会加载人员列表，此处进行判断，避免抛出null值异常
		if (!StringHelper.isNullOrEmpty(this.ToNode)) {
			// 查询出要到达的节点的绑定规则
			MySelector = new Selector(Integer.parseInt(this.ToNode));

			// 此处根据节点属性绑定的规则，绑定数据
			switch (MySelector.getSelectorModel()) {
			// 按岗位查询
			case Station:
				// 点击岗位列表时
				if ("4".equals(type))
					val = GetEmpByStation("Station", this.FK_Station, page,
							rows);
				// 点击部门列表时
				if ("5".equals(type))
					val = GetEmpByDept("Station", this.FK_Dept, page, rows);
				// 点击人员查询时
				if ("6".equals(type))
					val = GetEmpByEmp("Station", this.Name, page, rows);
				break;
			// 按SQL语句查询
			case SQL:
				if ("4".equals(type))
					val = BindBySQL("Station", this.FK_Station, page, rows);
				if ("5".equals(type))
					val = BindBySQL("Dept", this.FK_Dept, page, rows);
				if ("6".equals(type))
					val = BindBySQL("Emp", this.Name, page, rows);
				break;
			// 按部门查询
			case Dept:
				if ("4".equals(type))
					val = GetEmpByStation("Dept", this.FK_Station, page, rows);
				if ("5".equals(type))
					val = GetEmpByDept("Dept", this.FK_Dept, page, rows);
				if ("6".equals(type))
					val = GetEmpByEmp("Dept", this.Name, page, rows);
				break;
			// 按接受人查询
			case Emp:
				if ("4".equals(type))
					val = GetEmpByStation("Emp", this.FK_Station, page, rows);
				if ("5".equals(type))
					val = GetEmpByDept("Emp", this.FK_Dept, page, rows);
				if ("6".equals(type))
					val = GetEmpByEmp("Emp", this.Name, page, rows);
				break;
			// 按URL查询
			case Url:
				if (MySelector.getSelectorP1().contains("?"))
					res.sendRedirect(MySelector.getSelectorP1() + "&WorkID="
							+ this.WorkID + "&FK_Node=" + this.FK_Node);
				else
					this.res.sendRedirect(MySelector.getSelectorP1()
							+ "?WorkID=" + this.WorkID + "&FK_Node="
							+ this.FK_Node);
				return "";
			default:
				break;
			}
		}
		return val;
	}

	// / <summary>
	// / 获取部门树
	// / </summary>
	// / <returns></returns>
	public String GetDeptTree() throws Exception {
		String sql = "select No,Name,ParentNo,'1' IsParent  from Port_Dept where Name not in('null','',' ')";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		String treeJson = GetTreeJsonByTable(dt, "No", "Name", "ParentNo", "0");
		return treeJson;
	}

	// / <summary>
	// / 获取岗位树
	// / </summary>
	// / <returns></returns>
	public String GetStationTree() throws Exception {
		String sql = "select No,Name,'0' ParentNo from Port_Station";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		String treeJson = GetTreeJsonByTable(dt, "No", "Name", "ParentNo", "0");
		return treeJson;
	}

	// / 按绑定的部门查询
	// / </summary>
	// / <param name="type">查询条件（岗位、部门、人员）</param>
	// / <param name="val">当前岗位、部门的编号</param>
	// / <param name="page">当前页数</param>
	// / <param name="rows">每页显示行数</param>
	// / <returns></returns>
	public String GetEmpByDept(String type, String val, int page, int rows) {
		String sql = "";
		String SqlCount = "";
		if (page == 0)
			page = 1;
		// 点击岗位时
		if ("Station".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station "
					+ " in(select FK_Station from port_empstation where FK_Station in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_Emp.FK_Dept='"
					+ val
					+ "' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station "
					+ " in(select FK_Station from port_empstation where FK_Station in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_Emp.FK_Dept='"
					+ val + "'  order by Port_Emp.No) order by Port_Emp.No";
			SqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station "
					+ " in(select FK_Station from port_empstation where FK_Station in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_Emp.FK_Dept='"
					+ val + "'";
		}
		// 点击部门时
		if ("Dept".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
					+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
					+ "(select No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_emp.FK_Dept='"
					+ val
					+ "' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_emp.No from Port_emp left join Port_dept "
					+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
					+ "(select No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_emp.FK_Dept='"
					+ val + "' order by Port_Emp.No) order by Port_Emp.No";
			SqlCount = "select distinct Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
					+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
					+ "(select No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_emp.FK_Dept='"
					+ val + "'";
		}
		// 点击人员查询时
		if ("Emp".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(SELECT FK_Emp FROM WF_NodeEmp where FK_Node='"
					+ this.ToNode
					+ "') and Port_Emp.No!='admin' and Port_dept.No='"
					+ val
					+ "' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(SELECT FK_Emp FROM WF_NodeEmp where FK_Node='"
					+ this.ToNode
					+ "') and Port_Emp.No!='admin' and Port_dept.No='" + val
					+ "' order by Port_Emp.No) order by Port_Emp.No";
			SqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(SELECT FK_Emp FROM WF_NodeEmp where FK_Node='"
					+ this.ToNode
					+ "') and Port_Emp.No!='admin' and Port_dept.No='"
					+ val
					+ "'";
		}
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		DataTable dte = BP.DA.DBAccess.RunSQLReturnTable(SqlCount);
		String gridJson = GetEmpJson(dt, dte.Rows.size());
		return gridJson;
	}

	// / <summary>
	// / 按绑定的人员查询
	// / </summary>
	// / <param name="type">查询条件（岗位、部门、人员）</param>
	// / <param name="val">当前岗位、部门的编号</param>
	// / <param name="page">当前页数</param>
	// / <param name="rows">每页显示行数</param>
	// / <returns></returns>
	public String GetEmpByEmp(String type, String val, int page, int rows) {
		String sql = "";
		String sqlCount = "";
		if (page == 0)
			page = 1;
		if (StringHelper.isNullOrEmpty(val)) {
			// 点击岗位时
			if ("Station".equals(type)) {
				sql = "select distinct top "
						+ rows
						+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
						+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no where Port_Emp.No!='admin' and Port_Emp.No in"
						+ "(select distinct FK_Emp from port_empstation where FK_Station in"
						+ "(select FK_STATION from WF_NodeStation where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No not in("
						+ "select distinct top "
						+ (page - 1)
						* rows
						+ " Port_Emp.No from Port_Emp left join port_empstation "
						+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no where Port_Emp.No in"
						+ "(select distinct FK_Emp from port_empstation where FK_Station in (select FK_STATION from WF_NodeStation where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
				sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
						+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no where Port_Emp.No in"
						+ "(select distinct FK_Emp from port_empstation where FK_Station in"
						+ "(select FK_STATION from WF_NodeStation where FK_Node='"
						+ this.ToNode + "') and Port_Emp.No!='admin')";
			}
			// 点击部门时
			if ("Dept".equals(type)) {
				sql = "select distinct top "
						+ rows
						+ " Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No!='admin' and Port_emp.No not in(select distinct top "
						+ (page - 1)
						* rows
						+ " Port_emp.No from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in(select distinct No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
				sqlCount = "select distinct Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
						+ this.ToNode + "') and Port_Emp.No!='admin')";
			}
			// 点击人员查询时
			if ("Emp".equals(type)) {
				sql = "select distinct top "
						+ rows
						+ " Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct FK_EMP from WF_NodeEmp where FK_Node='"
						+ this.ToNode
						+ "') and Port_Emp.No!='admin' and Port_emp.No not in("
						+ "select distinct top "
						+ (page - 1)
						* rows
						+ " Port_emp.No from Port_emp left join Port_dept on Port_emp.FK_Dept=Port_dept.no"
						+ " where Port_emp.No in(select distinct FK_EMP from WF_NodeEmp where FK_Node='"
						+ this.ToNode
						+ "') and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
				sqlCount = "select distinct Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct FK_EMP from WF_NodeEmp where FK_Node='"
						+ this.ToNode + "' and Port_Emp.No!='admin')";
			}
		} else {
			String strSql = "select No from Port_Emp where Name like'%" + Name
					+ "%'";
			DataTable dtl = BP.DA.DBAccess.RunSQLReturnTable(strSql);
			String emps = "";
			for (DataRow item : dtl.Rows) {
				emps += item.getValue("No") + "','";
			}
			// 点击岗位时
			if ("Station".equals(type)) {
				sql = "select distinct top "
						+ rows
						+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
						+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no where Port_Emp.No in"
						+ "(select distinct FK_Emp from port_empstation where FK_Station in"
						+ "(select FK_STATION from WF_NodeStation where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No!='admin' and Port_Emp.No in('"
						+ emps
						+ "') and Port_Emp.No not in("
						+ "select distinct top "
						+ (page - 1)
						* rows
						+ " Port_Emp.No from Port_Emp left join port_empstation "
						+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no where Port_Emp.No in"
						+ "(select distinct FK_Emp from port_empstation where FK_Station in (select FK_STATION from WF_NodeStation where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No in('"
						+ emps
						+ "') and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
				sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
						+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no where Port_Emp.No in"
						+ "(select distinct FK_Emp from port_empstation where FK_Station in"
						+ "(select FK_STATION from WF_NodeStation where FK_Node='"
						+ this.ToNode + "') and Port_Emp.No!='admin')";
			}
			// 点击部门时
			if ("Dept".equals(type)) {
				sql = "select distinct top "
						+ rows
						+ " Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No in('"
						+ emps
						+ "') and Port_Emp.No!='admin' and Port_emp.No not in(select distinct top "
						+ (page - 1)
						* rows
						+ " Port_emp.No from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in(select distinct No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
						+ this.ToNode
						+ "')) and Port_Emp.No!='admin' and Port_Emp.No in('"
						+ emps
						+ "') order by Port_Emp.No) order by Port_Emp.No";
				sqlCount = "select distinct Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
						+ this.ToNode + "') and Port_Emp.No!='admin')";
			}
			// 点击人员查询时
			if ("Emp".equals(type)) {
				sql = "select distinct top "
						+ rows
						+ " Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct FK_EMP from WF_NodeEmp where FK_Node='"
						+ this.ToNode
						+ "') and Port_Emp.No!='admin' and Port_Emp.No in('"
						+ emps
						+ "') and Port_emp.No not in("
						+ "select distinct top "
						+ (page - 1)
						* rows
						+ " Port_emp.No from Port_emp left join Port_dept on Port_emp.FK_Dept=Port_dept.no"
						+ " where Port_emp.No in(select distinct FK_EMP from WF_NodeEmp where FK_Node='"
						+ this.ToNode
						+ "') and Port_Emp.No!='admin' and Port_Emp.No in('"
						+ emps
						+ "') order by Port_Emp.No) order by Port_Emp.No";
				sqlCount = "select distinct Port_emp.No,Port_emp.Name,port_dept.Name as DeptName from Port_emp left join Port_dept "
						+ "on Port_emp.FK_Dept=Port_dept.no where Port_emp.No in"
						+ "(select distinct FK_EMP from WF_NodeEmp where FK_Node='"
						+ this.ToNode + "') and Port_Emp.No!='admin'";
			}
		}
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		DataTable dte = BP.DA.DBAccess.RunSQLReturnTable(sqlCount);
		String gridJson = GetEmpJson(dt, dte.Rows.size());
		return gridJson;
	}

	// / <summary>
	// / 按绑定的岗位查询
	// / </summary>
	// / <param name="type">查询条件（岗位、部门、人员）</param>
	// / <param name="val">当前岗位、部门的编号</param>
	// / <param name="page">当前页数</param>
	// / <param name="rows">每页显示行数</param>
	// / <returns></returns>
	public String GetEmpByStation(String type, String val, int page, int rows) {
		String sql = "";
		String sqlCount = "";
		if (page == 0)
			page = 1;
		// 如果点击岗位时
		if ("Station".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station='"
					+ val
					+ "' and port_empstation.FK_Station "
					+ " in(select FK_Station from port_empstation where FK_Station in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station='"
					+ val
					+ "' and port_empstation.FK_Station "
					+ " in(select FK_Station from port_empstation where FK_Station in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
			sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station='"
					+ val
					+ "' and port_empstation.FK_Station "
					+ " in(select FK_Station from port_empstation where FK_Station in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"
					+ this.ToNode + "')) and Port_Emp.No!='admin'";
		}
		// 如果点击部门时
		if ("Dept".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(select No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and port_empstation.FK_Station='"
					+ val
					+ "' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(select No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and port_empstation.FK_Station='"
					+ val + "' order by Port_Emp.No) order by Port_Emp.No";
			sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(select No from Port_emp where FK_Dept in(SELECT FK_Dept FROM WF_NodeDept where FK_Node='"
					+ this.ToNode
					+ "')) and Port_Emp.No!='admin' and port_empstation.FK_Station='"
					+ val + "'";
		}
		// 如果点击人员查询时
		if ("Emp".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(SELECT FK_Emp FROM WF_NodeEmp where FK_Node='"
					+ this.ToNode
					+ "') and Port_Emp.No!='admin' and port_empstation.FK_Station='"
					+ val
					+ "' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(SELECT FK_Emp FROM WF_NodeEmp where FK_Node='"
					+ this.ToNode
					+ "') and Port_Emp.No!='admin' and port_empstation.FK_Station='"
					+ val + "' order by Port_Emp.No) order by Port_Emp.No";
			sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No "
					+ " in(SELECT FK_Emp FROM WF_NodeEmp where FK_Node='"
					+ this.ToNode
					+ "') and Port_Emp.No!='admin' and port_empstation.FK_Station='"
					+ val + "'";
		}
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		DataTable dte = BP.DA.DBAccess.RunSQLReturnTable(sqlCount);
		String gridJson = GetEmpJson(dt, dte.Rows.size());
		return gridJson;
	}

	// / <summary>
	// / 按绑定的SQL语句查询
	// / </summary>
	// / <param name="type">查询条件（岗位、部门、人员）</param>
	// / <param name="val">当前岗位、部门的编号或者人员的名称</param>
	// / <returns></returns>
	public String BindBySQL(String type, String val, int page, int rows) {
		String BindBySQL = MySelector.getSelectorP1();
		BindBySQL = BindBySQL.replace("WebUser.No", WebUser.getNo());
		BindBySQL = BindBySQL.replace("@WebUser.Name", WebUser.getName());
		BindBySQL = BindBySQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		String sql = "";
		String sqlCount = "";
		if (page == 0)
			page = 1;
		// 如果点击岗位时
		if ("Station".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station='"
					+ val
					+ "' and Port_Emp.No in("
					+ BindBySQL
					+ ") and Port_Emp.No!='admin' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station='"
					+ val
					+ "' and Port_Emp.No in("
					+ BindBySQL
					+ ") and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
			sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where port_empstation.FK_Station='"
					+ val
					+ "' and Port_Emp.No in("
					+ BindBySQL
					+ ") and Port_Emp.No!='admin'";
		}
		// 如果点击部门时
		if ("Dept".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Dept.No='"
					+ val
					+ "' and Port_Emp.No!='admin' and Port_Emp.No in("
					+ BindBySQL
					+ ") and Port_Emp.No!='admin' and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Dept.No='"
					+ val
					+ "' and Port_Emp.No!='admin' and Port_Emp.No in("
					+ BindBySQL
					+ ") and Port_Emp.No!='admin' order by Port_Emp.No) order by Port_Emp.No";
			sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Dept.No='"
					+ val
					+ "' and Port_Emp.No!='admin' and Port_Emp.No in("
					+ BindBySQL + ") and Port_Emp.No!='admin'";
		}
		// 如果点击人员查询时
		if ("Emp".equals(type)) {
			sql = "select distinct top "
					+ rows
					+ " Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No!='admin' and Port_Emp.No in("
					+ BindBySQL
					+ ") and Port_Emp.No not in("
					+ "select distinct top "
					+ (page - 1)
					* rows
					+ " Port_Emp.No from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No!='admin' and Port_Emp.No in("
					+ BindBySQL
					+ ") order by Port_Emp.No) order by Port_Emp.No";
			sqlCount = "select distinct Port_Emp.No,Port_Emp.Name,port_dept.Name as DeptName from Port_Emp left join port_empstation "
					+ " on port_empstation.FK_Emp=Port_Emp.No left join Port_dept on Port_emp.FK_Dept=Port_dept.no "
					+ "where Port_Emp.No!='admin' and Port_Emp.No in("
					+ BindBySQL + ")";

		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		DataTable dte = BP.DA.DBAccess.RunSQLReturnTable(sqlCount);
		String gridJson = GetEmpJson(dt, dte.Rows.size());
		return gridJson;
	}

	// / 生成Json的通用方法
	// / </summary>
	// / <param name="dt">数据源</param>
	// / <returns></returns>
	public String GetEmpJson(DataTable dt, int count) {
		int cnt = count;

		StringBuilder sb = new StringBuilder();
		sb.append("{\"total\":" + Integer.toString(cnt) + ",\"rows\":[");
		for (DataRow row : dt.Rows) {
			sb.append("{\"Name\":\"" + row.getValue("Name")
					+ "\",\"DepartName\":\"" + row.getValue("DeptName")
					+ "\",\"UserName\":\"" + row.getValue("No") + "\"");
			sb.append("},");
		}

		sb = (cnt > 0) ? sb.delete(sb.length() - 1, 1) : sb;

		sb.append("]}");

		return sb.toString();
	}

	private String GetTreeJsonByTable(DataTable tabel, String idCol,
			String txtCol, String rela, Object pId) throws Exception {
		result.append(sb.toString());
		sb.delete(0, sb.length() - 1);
		if (tabel.Rows.size() > 0) {
			sb.append("[");
			// String filer = string.Format("{0}='{1}'", rela, pId);
			Map<String, Object> filer = new HashMap<String, Object>();
			filer.put(rela, pId);
			List<DataRow> rows = tabel.Select(filer);
			if (rows.size() > 0) {
				try {
					for (DataRow row : rows) {
						sb.append("{\"id\":\"" + row.getValue(idCol)
								+ "\",\"text\":\"" + row.getValue(txtCol)
								+ "\"");
						Map<String, Object> filer1 = new HashMap<String, Object>();
						filer1.put(rela, row.getValue(idCol));
						if (tabel.Select(filer1).size() > 0) {
							if ("0".equals(pId.toString())) {
								sb.append(",\"state\":\"open\",\"children\":");
							}
							// 点击展开
							else
								sb.append(",\"state\":\"closed\",\"children\":");
							GetTreeJsonByTable(tabel, idCol, txtCol, rela,
									row.getValue(idCol));
							result.append(sb.toString());
							sb.delete(0, sb.length() - 1);
						}
						result.append(sb.toString());
						sb.delete(0, sb.length() - 1);
						sb.append("},");
					}
					sb = sb.delete(sb.length() - 1, 1);
					sb.append("]");
					result.append(sb.toString());
					sb.delete(0, sb.length() - 1);
				} catch (Exception ex) {
					return ex.toString();
				}

			}
		}
		return result.toString();
	}
}

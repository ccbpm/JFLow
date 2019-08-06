package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import BP.DA.DataTable;
import BP.Difference.Handler.WebContralBase;

public class WF_Comm_DataUser_Test extends WebContralBase {
	
	
	/**
	 * 构造函数
	 */
	public WF_Comm_DataUser_Test()
	{
	
	}
	
	
	String sql = "";
	String doType = this.getRequest().getParameter("DoType");
			///#region 开窗返回值的demo.
		//获得部门列表, 开窗返回值json.
	public final String ReqDepts()
		{
			sql = "SELECT No,Name, ParentNo FROM Port_Dept ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
		}

		//获得查询来的人员信息, 开窗返回值json.
	public final String SearchEmps()
		{
			String key = this.getRequest().getParameter("Keyword");
			sql = "SELECT No,Name  FROM Port_Emp WHERE No like '%" + key + "%' OR Name like '%" + key + "%' ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
		}

		//获得部门列表, 开窗返回值json.
	public final String ReqEmpsByDeptNo()
		{
			String deptNo = this.getRequest().getParameter("DeptNo");
			sql = "SELECT No,Name  FROM Port_Emp WHERE FK_Dept='" + deptNo + "'";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
		}
		
		///#region 从表导入.
	public final String DtlImpSearchKey()
		{
			String key = this.getRequest().getParameter("Keyword");
			try {
				key = URLDecoder.decode(key, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sql = "SELECT No,Name  FROM Port_Emp WHERE No like '%" + key + "%' OR Name like '%" + key + "%' ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
		}
	public final String DtlImpReqAll()
		{
			String key = this.getRequest().getParameter("Keyword");
			sql = "select No,Name from Port_StationType";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
		}
	public final String DtlImpReq1()
		{
			sql = "SELECT No,Name  FROM Port_Emp WHERE  1=1 ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
		}
	public final String DtlImpReq2(){
		sql = "SELECT No,Name  FROM Port_Emp WHERE  1=1 ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		String json = BP.Tools.Json.ToJson(dt);
		this.OutInfo(json);
		return json;
	}
	public final String DtlImpReq3(){
		sql = "SELECT No,Name  FROM Port_Emp WHERE  1=1 ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		String json = BP.Tools.Json.ToJson(dt);
		this.OutInfo(json);
		return json;
	}
	public final String DtlImpFullData()
		{
			String key = this.getRequest().getParameter("Keyword");
			sql = "SELECT No,Name  FROM Port_Emp WHERE  FK_Duty='0" + key + "' ";
			if (key.equals("all"))
			{
				sql = "SELECT No,Name  FROM Port_Emp WHERE  1=1";
			}
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			String json = BP.Tools.Json.ToJson(dt);
			this.OutInfo(json);
			return json;
	}

	public final void OutInfo(String info)
	{
		//Response.ContentType = "text/plain";
		//myHttpContext.Response.Write(info);
	}

	public final boolean getIsReusable()
	{
		return false;
	}

}

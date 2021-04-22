package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.DataTableConvertJson;
import bp.tools.FileAccess;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

/** 
 页面功能实体
*/
public class GPMPage extends WebContralBase
{

		///构造函数
	/** 
	 构造函数
	*/
	public GPMPage()
	{
	}

		/// 构造函数


		///签名.
	/** 
	 图片签名初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Siganture_Init() throws Exception
	{
		if (WebUser.getNoOfRel() == null)
		{
			return "err@登录信息丢失";
		}
		Hashtable ht = new Hashtable();
		ht.put("No", WebUser.getNo());
		ht.put("Name",WebUser.getName());
		ht.put("FK_Dept", WebUser.getFK_Dept());
		ht.put("FK_DeptName", WebUser.getFK_DeptName());
		return bp.tools.Json.ToJson(ht);
	}

	/** 
	 签名保存
	 
	 @return 
	*/
	public final String Siganture_Save()
	{
		try {
			HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				String tempFilePath = SystemConfig.getPathOfWebApp() + "/DataUser/Siganture/" + this.getFK_Emp()
						+ ".jpg";
				File tempFile = new File(tempFilePath);
				if (tempFile.exists()) {
					tempFile.delete();
				}
				MultipartHttpServletRequest mrequest = CommonFileUtils.getMultipartHttpServletRequest(request);

				MultipartFile item = mrequest.getFile("file");

				// 获取文件名
				String fileName = item.getOriginalFilename();
				String fileExt = ",bpm,jpg,jpeg,png,gif,";
				// 扩展名
				String exts = FileAccess.getExtensionName(fileName).toLowerCase().replace(".", "");
				if (fileExt.indexOf(exts + ",") == -1) {
					return "err@上传的文件必须是以图片格式:" + fileExt + "类型, 现在类型是:" + exts;
				}

				MultipartFile multipartFile = mrequest.getFile("File_Upload");
				try {
					multipartFile.transferTo(tempFile);
				} catch (Exception e) {

				}

			}

		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}

		return "上传成功！";
	}

		///


		///组织结构维护.
	/** 
	 初始化组织结构部门表维护.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Organization_Init() throws Exception
	{

		bp.gpm.Depts depts = new bp.gpm.Depts();
		String parentNo = this.GetRequestVal("ParentNo");
		QueryObject qo = new QueryObject(depts);
		if(DataType.IsNullOrEmpty(parentNo)==false){
			if (parentNo.equals("0") == true) {
				qo.AddWhere(DeptAttr.ParentNo, parentNo);
				qo.addOr();
				qo.AddWhereInSQL(DeptAttr.ParentNo, "SELECT No From Port_Dept Where ParentNo='0'");
			}
			else
				qo.AddWhere(DeptAttr.ParentNo,parentNo);
		}
		qo.addOrderBy(bp.gpm.DeptAttr.Idx);
		qo.DoQuery();

		return depts.ToJson();

	}

	/** 
	 获取该部门的所有人员
	 
	 @return 
	 
	*/
	public final String LoadDatagridDeptEmp_Init()
	{
		String deptNo = this.GetRequestVal("deptNo");
		if (DataType.IsNullOrEmpty(deptNo))
		{
			return "{ total: 0, rows: [] }";
		}
		String orderBy = this.GetRequestVal("orderBy");


		String searchText = this.GetRequestVal("searchText");
		if (!DataType.IsNullOrEmpty(searchText))
		{
			searchText.trim();
		}
		String addQue = "";
		if (!DataType.IsNullOrEmpty(searchText))
		{
			addQue = "  AND (pe.No like '%" + searchText + "%' or pe.Name like '%" + searchText + "%') ";
		}

		String pageNumber = this.GetRequestVal("pageNumber");
		int iPageNumber = DataType.IsNullOrEmpty(pageNumber) ? 1 : Integer.parseInt(pageNumber);
		//每页多少行
		String pageSize = this.GetRequestVal("pageSize");
		int iPageSize = DataType.IsNullOrEmpty(pageSize) ? 9999 : Integer.parseInt(pageSize);

		String sql = "(select pe.*,pd.name FK_DutyText from port_emp pe left join port_duty pd on pd.setNo(pe.fk_duty where pe.no in (select fk_emp from Port_DeptEmp where fk_dept='" + deptNo + "') " + addQue + " ) dbSo ";


		return DBPaging(sql, iPageNumber, iPageSize, "No", orderBy);

	}

	/** 
	 以下算法只包含 oracle mysql sqlserver 三种类型的数据库 qin
	 
	 @param dataSource 表名
	 @param pageNumber 当前页
	 @param pageSize 当前页数据条数
	 @param key 计算总行数需要
	 @param orderKey 排序字段
	 @return 
	*/
	public final String DBPaging(String dataSource, int pageNumber, int pageSize, String key, String orderKey)
	{
		String sql = "";
		String orderByStr = "";

		if (!DataType.IsNullOrEmpty(orderKey))
		{
			orderByStr = " ORDER BY " + orderKey;
		}

		switch (DBAccess.getAppCenterDBType())
		{
			case Oracle:
			case KingBase:
				int beginIndex = (pageNumber - 1) * pageSize + 1;
				int endIndex = pageNumber * pageSize;

				sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN " + "FROM (SELECT * FROM  " + dataSource + orderByStr + ") A WHERE ROWNUM <= " + endIndex + " ) WHERE RN >=" + beginIndex;
				break;
			case MSSQL:
				sql = "SELECT TOP " + pageSize + " * FROM " + dataSource + " WHERE " + key + " NOT IN  (" + "SELECT TOP (" + pageSize + "*(" + pageNumber + "-1)) " + key + " FROM " + dataSource + " )" + orderByStr;
				break;
			case MySQL:
				pageNumber -= 1;
				sql = "select * from  " + dataSource + orderByStr + " limit " + pageNumber + "," + pageSize;
				break;
			default:
				throw new RuntimeException("暂不支持您的数据库类型.");
		}

		DataTable DTable = DBAccess.RunSQLReturnTable(sql);

		int totalCount = DBAccess.RunSQLReturnCOUNT("select " + key + " from " + dataSource);

		return DataTableConvertJson.DataTable2Json(DTable, totalCount);
	}

		///


		///获取菜单权限.
	/** 
	 获得菜单数据.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GPM_DB_Menus() throws Exception
	{
		String appNo = this.GetRequestVal("AppNo");

		String sql1 = "SELECT No,Name,FK_Menu,ParentNo,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx ";
		sql1 += " FROM V_GPM_EmpMenu ";
		sql1 += " WHERE FK_Emp = '" + WebUser.getNo() + "' ";
		sql1 += " AND MenuType = '3' ";
		sql1 += " AND FK_App = '" + appNo + "' ";
		sql1 += " UNION "; //加入不需要权限控制的菜单.
		sql1 += "SELECT No,Name, No as FK_Menu,ParentNo,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx";
		sql1 += " FROM GPM_Menu ";
		sql1 += " WHERE MenuCtrlWay=1 ";
		sql1 += " AND MenuType = '3' ";
		sql1 += " AND FK_App = '" + appNo + "' ORDER BY Idx ";
		DataTable dirs = DBAccess.RunSQLReturnTable(sql1);
		dirs.TableName = "Dirs"; //获得目录.

		String sql2 = "SELECT No,Name,FK_Menu,ParentNo,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx ";
		sql2 += " FROM V_GPM_EmpMenu ";
		sql2 += " WHERE FK_Emp = '" + WebUser.getNo() + "'";
		sql2 += " AND MenuType = '4' ";
		sql2 += " AND FK_App = '" + appNo + "' ";
		sql2 += " UNION "; //加入不需要权限控制的菜单.
		sql2 += "SELECT No,Name, No as FK_Menu,ParentNo,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx ";
		sql2 += " FROM GPM_Menu "; //加入不需要权限控制的菜单.
		sql2 += " WHERE MenuCtrlWay=1 ";
		sql2 += " AND MenuType = '4' ";
		sql2 += " AND FK_App = '" + appNo + "' ORDER BY Idx ";

		DataTable menus = DBAccess.RunSQLReturnTable(sql2);
		menus.TableName = "Menus"; //获得菜单.

		if (SystemConfig.getAppCenterDBType() == DBType.KingBase
				|| SystemConfig.getAppCenterDBType() == DBType.Oracle) {

			// 目录
			dirs.Columns.get("NO").ColumnName = "No";
			dirs.Columns.get("NAME").ColumnName = "Name";
			dirs.Columns.get("FK_MENU").ColumnName = "FK_Menu";
			dirs.Columns.get("PARENTNO").ColumnName = "ParentNo";
			dirs.Columns.get("URLEXT").ColumnName = "UrlExt";
			dirs.Columns.get("TAG1").ColumnName = "Tag1";
			dirs.Columns.get("TAG2").ColumnName = "Tag2";
			dirs.Columns.get("TAG3").ColumnName = "Tag3";
			dirs.Columns.get("WEBPATH").ColumnName = "WebPath";
			dirs.Columns.get("ICON").ColumnName = "Icon";
			dirs.Columns.get("IDX").ColumnName = "Idx";

			// 菜单.
			menus.Columns.get("NO").ColumnName = "No";
			menus.Columns.get("NAME").ColumnName = "Name";
			menus.Columns.get("FK_MENU").ColumnName = "FK_Menu";
			menus.Columns.get("PARENTNO").ColumnName = "ParentNo";
			menus.Columns.get("URLEXT").ColumnName = "UrlExt";
			menus.Columns.get("TAG1").ColumnName = "Tag1";
			menus.Columns.get("TAG2").ColumnName = "Tag2";
			menus.Columns.get("TAG3").ColumnName = "Tag3";
			menus.Columns.get("WEBPATH").ColumnName = "WebPath";
			menus.Columns.get("ICON").ColumnName = "Icon";
			menus.Columns.get("IDX").ColumnName = "Idx";

		}
		//组装数据.
		DataSet ds = new DataSet();
		ds.Tables.add(dirs);
		ds.Tables.add(menus);

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 获得OA菜单数据.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GPM_OA_Menus() throws Exception
	{
		String appNo = this.GetRequestVal("AppNo");

		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		ps.SQL="SELECT No FROM GPM_Menu WHERE MenuType=" + dbstr + "MenuType AND FK_App=" + dbstr + "FK_App";
		ps.Add("MenuType", 2);
		ps.Add("FK_App", appNo);

		String ParentNo = DBAccess.RunSQLReturnString(ps);

		if (DataType.IsNullOrEmpty(ParentNo))
		{
			return "[]";
		}

		String sql1 = "SELECT No,Name,FK_Menu,MenuType,ParentNo,Url,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx ";
		sql1 += " FROM v_gpm_empmenu ";
		sql1 += " WHERE FK_Emp = '" + WebUser.getNo() + "' ";
		sql1 += " AND ParentNo = '" + ParentNo + "' ";
		sql1 += " AND FK_App = '" + appNo + "' ";
		sql1 += " UNION "; //加入不需要权限控制的菜单.
		sql1 += "SELECT No,Name, No as FK_Menu,MenuType,ParentNo,Url,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx";
		sql1 += " FROM GPM_Menu ";
		sql1 += " WHERE MenuCtrlWay=1 ";
		sql1 += " AND ParentNo = '" + ParentNo + "' ";
		sql1 += " AND FK_App = '" + appNo + "' ORDER BY Idx ";
		DataTable dirs = DBAccess.RunSQLReturnTable(sql1);
		dirs.TableName = "Dirs"; //获得目录.

		String sql2 = "SELECT No,Name,FK_Menu,MenuType,ParentNo,Url,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx,openway ";
		sql2 += " FROM v_gpm_empmenu ";
		sql2 += " WHERE FK_Emp = '" + WebUser.getNo() + "'";
		sql2 += " AND ParentNo != '" + ParentNo + "'  ";
		sql2 += " AND FK_App = '" + appNo + "' ";
		sql2 += " UNION "; //加入不需要权限控制的菜单.
		sql2 += "SELECT No,Name, No as FK_Menu,MenuType,ParentNo,Url,UrlExt,Tag1,Tag2,Tag3,WebPath,Icon,Idx,openway ";
		sql2 += " FROM GPM_Menu "; //加入不需要权限控制的菜单.
		sql2 += " WHERE MenuCtrlWay=1 ";
		sql2 += " AND ParentNo != '" + ParentNo + "' ";
		sql2 += " AND FK_App = '" + appNo + "' ORDER BY Idx ";

		DataTable menus = DBAccess.RunSQLReturnTable(sql2);
		menus.TableName = "Menus"; //获得菜单.

		//组装数据.
		DataSet ds = new DataSet();
		ds.Tables.add(dirs);
		ds.Tables.add(menus);

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 是否可以执行当前工作
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GPM_IsCanExecuteFunction() throws Exception
	{
		DataTable dt = GPM_GenerFlagDB(); //获得所有的标记.
		String funcNo = this.GetRequestVal("FuncFlag");
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue(0).toString().equals(funcNo) == true)
			{
				return "1";
			}
		}
		return "0";
	}
	/** 
	 获得所有的权限标记.
	 
	 @return 
	 * @throws Exception 
	*/
	public final DataTable GPM_GenerFlagDB() throws Exception
	{
		String appNo = this.GetRequestVal("AppNo");
		String sql2 = "SELECT Flag,Idx";
		sql2 += " FROM V_GPM_EmpMenu ";
		sql2 += " WHERE FK_Emp = '" + WebUser.getNo() + "'";
		sql2 += " AND MenuType = '5' ";
		sql2 += " AND FK_App = '" + appNo + "' ";
		sql2 += " UNION "; //加入不需要权限控制的菜单.
		sql2 += "SELECT Flag,Idx ";
		sql2 += " FROM GPM_Menu "; //加入不需要权限控制的菜单.
		sql2 += " WHERE MenuCtrlWay=1 ";
		sql2 += " AND MenuType = '5' ";
		sql2 += " AND FK_App = '" + appNo + "' ORDER BY Idx ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql2);
		return dt;
	}
	/** 
	 获得所有权限的标记
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GPM_AutoHidShowPageElement() throws Exception
	{
		DataTable dt = GPM_GenerFlagDB(); //获得所有的标记.
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 组织结构查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GPM_Search() throws Exception
	{
		String searchKey = this.GetRequestVal("searchKey");
	
		String sql = "SELECT e.No AS No,e.Name AS Name,d.Name AS deptName,e.Email AS Email,e.Tel AS Tel from Port_Dept d,Port_Emp e " + "where d.No=e.FK_Dept AND (e.No LIKE '%" + searchKey + "%' or e.NAME LIKE '%" + searchKey + "%' or d.Name LIKE '%" + searchKey + "%' or e.Tel LIKE '%" + searchKey + "%')";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}

		///

}
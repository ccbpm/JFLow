package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.port.DeptEmp;
import bp.port.DeptEmpAttr;
import bp.port.DeptEmps;
import bp.port.Emp;
import bp.tools.FileAccess;
import bp.wf.port.*;
import bp.web.*;
import bp.sys.*;
import bp.en.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;

/** 
 页面功能实体
*/
public class GPMPage extends WebContralBase
{

		///#region 构造函数
	/** 
	 构造函数
	*/
	public GPMPage() throws Exception {
	}

		///#endregion 构造函数


		///#region 签名.
	/** 
	 图片签名初始化
	 
	 @return 
	*/
	public final String Siganture_Init() throws Exception {
		if (WebUser.getNoOfRel() == null)
		{
			return "err@登录信息丢失";
		}
		Hashtable ht = new Hashtable();
		ht.put("No", WebUser.getNo());
		ht.put("Name", WebUser.getName());
		ht.put("FK_Dept", WebUser.getFK_Dept());
		ht.put("FK_DeptName", WebUser.getFK_DeptName());
		return bp.tools.Json.ToJson(ht);
	}

	/** 
	 签名保存
	 
	 @return 
	*/
	public final String Siganture_Save() throws Exception {
		try {
			HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				String tempFilePath = SystemConfig.getPathOfWebApp() + "DataUser/Siganture/" + this.getFK_Emp()
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

		///#endregion


		///#region 组织结构维护.
	/** 
	 初始化组织结构部门表维护.
	 
	 @return 
	*/
	public final String Organization_Init() throws Exception {

		bp.port.Depts depts = new bp.port.Depts();
		String parentNo = this.GetRequestVal("ParentNo");
		if(DataType.IsNullOrEmpty(parentNo))
			parentNo = "0";
		QueryObject qo = new QueryObject(depts);
		if(SystemConfig.getCCBPMRunModel()==CCBPMRunModel.Single){
			if (parentNo.equals("0") == true) {
				qo.AddWhere(bp.port.DeptAttr.ParentNo, parentNo);
				qo.addOr();
				qo.AddWhereInSQL(bp.port.DeptAttr.ParentNo, "SELECT No From Port_Dept Where ParentNo='0'");
			}
			else
				qo.AddWhere(bp.port.DeptAttr.ParentNo,parentNo);
		}
		if(SystemConfig.getCCBPMRunModel()==CCBPMRunModel.GroupInc || SystemConfig.getCCBPMRunModel()==CCBPMRunModel.SAAS){
			qo.AddWhere(bp.port.DeptAttr.No, WebUser.getOrgNo());
		}

		qo.addOrderBy(bp.port.DeptAttr.Idx);
		qo.DoQuery();
		return depts.ToJson("dt");
	}
	public final String Organization_GetDeptsByParentNo() throws Exception {

		bp.port.Depts depts = new bp.port.Depts();
		String parentNo = GetRequestVal("ParentNo");
		QueryObject qo = new QueryObject(depts);
		qo.AddWhere(bp.port.DeptAttr.ParentNo, parentNo);
		qo.addOrderBy(bp.port.DeptAttr.Idx);
		qo.DoQuery();
		return depts.ToJson("dt");
	}
	/** 
	 获取本部门及人员信息
	 
	 @return 
	*/
	public final String DeptEmp_Init() throws Exception {

		bp.port.Depts depts = new bp.port.Depts();
		bp.port.Emps emps = new bp.port.Emps();
		String parentNo = this.GetRequestVal("ParentNo");
		QueryObject qo = new QueryObject(depts);
		if (DataType.IsNullOrEmpty(parentNo) == false)
		{
			if (parentNo.equals("0") == true)
			{
				emps.RetrieveIn(bp.port.EmpAttr.FK_Dept, "SELECT No From Port_Dept Where ParentNo='0'");
				qo.AddWhere(bp.port.DeptAttr.ParentNo, parentNo);
				qo.addOr();
				qo.AddWhereInSQL(bp.port.DeptAttr.ParentNo, "SELECT No From Port_Dept Where ParentNo='0'");

			}
			else
			{
				emps.Retrieve(bp.port.EmpAttr.FK_Dept, parentNo, null);
				qo.AddWhere(bp.port.DeptAttr.ParentNo, parentNo);
				qo.addOr();
				qo.AddWhere(bp.port.DeptAttr.No, parentNo);
			}


		}
		qo.addOrderBy(bp.port.DeptAttr.Idx);
		qo.DoQuery();
		DataSet ds = new DataSet();
		ds.Tables.add(depts.ToDataTableField("Depts"));
		ds.Tables.add(emps.ToDataTableField("Emps"));
		return bp.tools.Json.ToJson(ds);

	}

	/** 
	 获取该部门的所有人员
	 
	 @return         
	*/
	public final String LoadDatagridDeptEmp_Init() throws Exception {
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

		String sql = "(select pe.*,pd.name FK_DutyText from port_emp pe left join port_duty pd on pd.no = pe.fk_duty where pe.no in (select fk_emp from Port_DeptEmp where fk_dept='" + deptNo + "') " + addQue + " ) dbSo ";


		return DBPaging(sql, iPageNumber, iPageSize, "No", orderBy);

	}

	/** 
	 以下算法只包含 oracle mysql sqlserver 三种类型的数据库 qin
	 
	 param dataSource 表名
	 param pageNumber 当前页
	 param pageSize 当前页数据条数
	 param key 计算总行数需要
	 param orderKey 排序字段
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
			case KingBaseR3:
			case KingBaseR6:
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

		///#endregion


		///#region 获取菜单权限.
	/** 
	 获得菜单数据.
	 
	 @return 
	*/
	public final String GPM_DB_Menus() throws Exception {
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

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase) {
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
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase) {
			// 目录
			dirs.Columns.get("no").ColumnName = "No";
			dirs.Columns.get("name").ColumnName = "Name";
			dirs.Columns.get("fk_menu").ColumnName = "FK_Menu";
			dirs.Columns.get("parentno").ColumnName = "ParentNo";
			dirs.Columns.get("urlext").ColumnName = "UrlExt";
			dirs.Columns.get("tag1").ColumnName = "Tag1";
			dirs.Columns.get("tag2").ColumnName = "Tag2";
			dirs.Columns.get("tag3").ColumnName = "Tag3";
			dirs.Columns.get("webpath").ColumnName = "WebPath";
			dirs.Columns.get("icon").ColumnName = "Icon";
			dirs.Columns.get("idx").ColumnName = "Idx";

			// 菜单.
			menus.Columns.get("no").ColumnName = "No";
			menus.Columns.get("name").ColumnName = "Name";
			menus.Columns.get("fk_menu").ColumnName = "FK_Menu";
			menus.Columns.get("parentno").ColumnName = "ParentNo";
			menus.Columns.get("urlext").ColumnName = "UrlExt";
			menus.Columns.get("tag1").ColumnName = "Tag1";
			menus.Columns.get("tag2").ColumnName = "Tag2";
			menus.Columns.get("tag3").ColumnName = "Tag3";
			menus.Columns.get("webpath").ColumnName = "WebPath";
			menus.Columns.get("icon").ColumnName = "Icon";
			menus.Columns.get("idx").ColumnName = "Idx";

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
	*/
	public final String GPM_OA_Menus() throws Exception {
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
	*/
	public final String GPM_IsCanExecuteFunction() throws Exception {

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
	*/
	public final DataTable GPM_GenerFlagDB() throws Exception {
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
	*/
	public final String GPM_AutoHidShowPageElement() throws Exception {
		DataTable dt = GPM_GenerFlagDB(); //获得所有的标记.
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 组织结构查询
	 
	 @return 
	*/
	public final String GPM_Search() throws Exception {
		String searchKey = this.GetRequestVal("searchKey");
		String sql = "SELECT e.no AS \"No\",e.name AS \"Name\", d.No AS FK_Dept,d.Name AS deptName,e.Email AS Email,e.Tel AS Tel from Port_Dept d,Port_Emp e " + "where d.No=e.FK_Dept AND (e.No LIKE '%" + searchKey + "%' or e.NAME LIKE '%" + searchKey + "%' or d.Name LIKE '%" + searchKey + "%' or e.Tel LIKE '%" + searchKey + "%')";
		if (DataType.IsNullOrEmpty(WebUser.getOrgNo()) == false)
		{
			sql += " AND e.OrgNo='" + WebUser.getOrgNo() + "'";
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}

		///#endregion

	public final String Template_Save() throws Exception {
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			throw new RuntimeException("err@仅仅导入单组织版.");
		}
		//var files = HttpContextHelper.RequestFiles();
		//string ext = ".xls";
		//string fileName = System.IO.Path.GetFileName(files[0].FileName);
		//if (fileName.contains(".xlsx"))
		//    ext = ".xlsx";

		////设置文件名
		//string fileNewName = DateTime.Now.ToString("yyyyMMddHHmmssff") + ext;

		////文件存放路径
		//string filePath =  bp.difference.SystemConfig.getPathOfTemp() +  fileNewName;
		//HttpContextHelper.UploadFile(files[0], filePath);

		String filePath = "D:\\ccflow组织结构批量导入模板.xls";


			///#region 获得数据源.
		List sheetNameList = Arrays.asList(DBLoad.GenerTableNames(filePath));
		if (sheetNameList.size() < 3 || sheetNameList.contains("部门$") == false || sheetNameList.contains("岗位$") == false || sheetNameList.contains("人员$") == false)
		{
			throw new RuntimeException("excel不符合要求");
		}

		//获得部门数据.
		DataTable dtDept = DBLoad.ReadExcelFileToDataTable(filePath, sheetNameList.indexOf("部门$"));
		for (int i = 0; i < dtDept.Columns.size(); i++)
		{
			String name = dtDept.Columns.get(i).ColumnName;
			name = name.replace(" ", "");
			name = name.replace("*", "");
			dtDept.Columns.get(i).ColumnName = name;
		}

		//获得岗位数据.
		DataTable dtStation = DBLoad.ReadExcelFileToDataTable(filePath, sheetNameList.indexOf("岗位$"));
		for (int i = 0; i < dtStation.Columns.size(); i++)
		{
			String name = dtStation.Columns.get(i).ColumnName;
			name = name.replace(" ", "");
			name = name.replace("*", "");
			dtStation.Columns.get(i).ColumnName = name;
		}

		//获得人员数据.
		DataTable dtEmp = DBLoad.ReadExcelFileToDataTable(filePath, sheetNameList.indexOf("人员$"));
		for (int i = 0; i < dtEmp.Columns.size(); i++)
		{
			String name = dtEmp.Columns.get(i).ColumnName;
			name = name.replace(" ", "");
			name = name.replace("*", "");
			dtEmp.Columns.get(i).ColumnName = name;
		}



			///#endregion 获得数据源.


			///#region 检查是否有根目录为 0 的数据?
		//检查数据的完整性.
		//1.检查是否有根目录为0的数据?
		int num = 0;
		boolean isHave = false;
		for (DataRow dr : dtDept.Rows)
		{
			String str1 = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (DataType.IsNullOrEmpty(str1) == true)
			{
				continue;
			}

			num++;
			String str = dr.getValue(1) instanceof String ? (String)dr.getValue(1) : null;
			if (str == null )//|| str.equals(DBNull.Value))
			{
				return "err@导入出现数据错误:" + str1 + "的.上级部门名称-不能用空行的数据， 第[" + num + "]行数据.";
			}

			if (str.equals("0") == true || str.equals("root") == true)
			{
				isHave = true;
				break;
			}
		}
		if (isHave == false)
		{
			return "err@导入数据没有找到部门根目录节点.";
		}

			///#endregion 检查是否有根目录为0的数据


			///#region 检查部门名称是否重复?
		String deptStrs = "";
		for (DataRow dr : dtDept.Rows)
		{
			String deptName = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (DataType.IsNullOrEmpty(deptName) == true)
			{
				continue;
			}

			if (deptStrs.contains("," + deptName + ",") == true)
			{
				return "err@部门名称:" + deptName + "重复.";
			}

			//加起来..
			deptStrs += "," + deptName + ",";
		}

			///#endregion 检查部门名称是否重复?


			///#region 检查人员帐号是否重复?
		String emps = "";
		for (DataRow dr : dtEmp.Rows)
		{
			String empNo = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (DataType.IsNullOrEmpty(empNo) == true)
			{
				continue;
			}

			if (emps.contains("," + empNo + ",") == true)
			{
				return "err@人员帐号:" + empNo + "重复.";
			}

			//加起来..
			emps += "," + empNo + ",";
		}

			///#endregion 检查人员帐号是否重复?


			///#region 检查岗位名称是否重复?
		String staStrs = "";
		for (DataRow dr : dtStation.Rows)
		{
			String staName = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (DataType.IsNullOrEmpty(staName) == true)
			{
				continue;
			}

			if (staStrs.contains("," + staName + ",") == true)
			{
				return "err@岗位名称:" + staName + "重复.";
			}

			//加起来..
			staStrs += "," + staName + ",";
		}

			///#endregion 检查岗位名称是否重复?


			///#region 检查人员的部门名称是否存在于部门数据里?
		int idx = 0;
		for (DataRow dr : dtEmp.Rows)
		{
			String emp = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (DataType.IsNullOrEmpty(emp) == true)
			{
				continue;
			}

			idx++;
			//去的部门编号.
			String strs = dr.getValue("部门名称") instanceof String ? (String)dr.getValue("部门名称") : null;
			if (DataType.IsNullOrEmpty(strs) == true)
			{
				return "err@第[" + idx + "]行,人员[" + emp + "]部门不能为空:" + strs + ".";
			}

			String[] mystrs = strs.split("[,]", -1);
			for (String str : mystrs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				if (str.equals("0") || str.equals("root") == true)
				{
					continue;
				}

				//先看看数据是否有?
				Dept dept = new Dept();
				if (dept.Retrieve("Name", str) == 1)
				{
					continue;
				}

				//从xls里面判断.
				isHave = false;
				for (DataRow drDept : dtDept.Rows)
				{
					if (str.equals(drDept.get(0).toString()) == true)
					{
						isHave = true;
						break;
					}
				}
				if (isHave == false)
				{
					return "err@第[" + idx + "]行,人员[" + emp + "]部门名[" + str + "]，不存在模版里。";
				}
			}
		}

			///#endregion 检查人员的部门名称是否存在于部门数据里


			///#region 检查人员的岗位名称是否存在于岗位数据里?
		idx = 0;
		for (DataRow dr : dtEmp.Rows)
		{
			String emp = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (DataType.IsNullOrEmpty(emp) == true)
			{
				continue;
			}

			idx++;

			//岗位名称..
			String strs = dr.getValue("岗位名称") instanceof String ? (String)dr.getValue("岗位名称") : null;
			if (DataType.IsNullOrEmpty(strs) == true)
			{
				continue;
			}

			//判断岗位.
			String[] mystrs = strs.split("[,]", -1);
			for (String str : mystrs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				//先看看数据是否有?
			  bp.port.Station stationEn = new bp.port.Station();
				if (stationEn.Retrieve("Name", str) == 1)
				{
					continue;
				}

				//从 xls 判断.
				isHave = false;
				for (DataRow drSta : dtStation.Rows)
				{
					if (str.equals(drSta.get(0).toString()) == true)
					{
						isHave = true;
						break;
					}
				}
				if (isHave == false)
				{
					return "err@第[" + idx + "]行,人员[" + emp + "]岗位名称[" + str + "]，不存在模版里。";
				}
			}
		}

			///#endregion 检查人员的部门名称是否存在于部门数据里


			///#region 检查部门负责人是否存在于人员列表里?
		String empStrs = ",";
		for (DataRow item : dtEmp.Rows)
		{
			empStrs += item.get(0).toString() + ",";
		}
		idx = 0;
		for (DataRow dr : dtDept.Rows)
		{
			String empNo = dr.getValue(2) instanceof String ? (String)dr.getValue(2) : null;
			if (DataType.IsNullOrEmpty(empNo) == true)
			{
				continue;
			}

			idx++;
			if (empStrs.contains("," + empNo + ",") == false)
			{
				return "err@部门负责人[" + empNo + "]不存在与人员表里，第[" + idx + "]行.";
			}
		}

			///#endregion 检查部门负责人是否存在于人员列表里


			///#region 检查直属领导帐号是否存在于人员列表里?
		idx = 0;
		for (DataRow dr : dtEmp.Rows)
		{
			String empNo = dr.getValue(6) instanceof String ? (String)dr.getValue(6) : null;
			if (DataType.IsNullOrEmpty(empNo) == true)
			{
				continue;
			}

			idx++;
			if (empStrs.contains("," + empNo + ",") == false)
			{
				return "err@部门负责人[" + empNo + "]不存在与人员表里，第[" + idx + "]行.";
			}
		}

			///#endregion 检查部门负责人是否存在于人员列表里


			///#region 插入数据到 Port_StationType.
		idx = -1;
		for (DataRow dr : dtStation.Rows)
		{
			idx++;
			String str = dr.getValue(1) instanceof String ? (String)dr.getValue(1) : null;

			//判断是否是空.
			if (DataType.IsNullOrEmpty(str) == true)
			{
				continue;
			}

			if (str.equals("岗位类型") == true)
			{
				continue;
			}

			str = str.trim();

			//看看数据库是否存在.
			bp.port.StationType st = new bp.port.StationType();
			if (st.IsExit("Name", str) == false)
			{
				st.setName(str);
				st.setOrgNo(WebUser.getOrgNo());
				st.setNo(DBAccess.GenerGUID(0, null, null));
				st.Insert();
			}
		}

			///#endregion 插入数据到 Port_StationType.


			///#region 插入数据到 Port_Station.
		idx = -1;
		for (DataRow dr : dtStation.Rows)
		{
			idx++;
			String str = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;

			//判断是否是空.
			if (DataType.IsNullOrEmpty(str) == true)
			{
				continue;
			}

			if (str.equals("岗位名称") == true)
			{
				continue;
			}


			//获得类型的外键的编号.
			String stationTypeName = dr.getValue(1).toString().trim();
			bp.port.StationType st = new bp.port.StationType();
			if (st.Retrieve("Name", stationTypeName) == 0)
			{
				return "err@系统出现错误,没有找到岗位类型[" + stationTypeName + "]的数据.";
			}

			//看看数据库是否存在.
			bp.port.Station sta = new bp.port.Station();
			sta.setName(str);
			sta.setIdx(idx);

			//不存在就插入.
			if (sta.IsExit("Name", str) == false)
			{
				sta.setOrgNo(WebUser.getOrgNo());
				sta.setFK_StationType(st.getNo());
				sta.setNo(DBAccess.GenerGUID(0, null, null));
				sta.Insert();
			}
			else
			{
				//存在就更新.
				sta.setFK_StationType(st.getNo());
				sta.Update();
			}
		}

			///#endregion 插入数据到 Port_Station.


			///#region 插入数据到 Port_Dept.
		idx = -1;
		for (DataRow dr : dtDept.Rows)
		{
			//获得部门名称.
			String deptName = dr.getValue(0) instanceof String ? (String)dr.getValue(0) : null;
			if (deptName.equals("部门名称") == true)
			{
				continue;
			}

			String parentDeptName = dr.getValue(1) instanceof String ? (String)dr.getValue(1) : null;
			String leader = dr.getValue(2) instanceof String ? (String)dr.getValue(2) : null;

			//说明是根目录.
			if (parentDeptName.equals("0") == true || parentDeptName.equals("root") == true)
			{
				Dept root = new Dept();
				root.setNo(WebUser.getFK_Dept());
				if (root.RetrieveFromDBSources() == 0)
				{
					return "err@没有找到根目录节点，请联系管理员。";
				}

				root.setName(deptName);
				root.Update();
				continue;
			}


			//先求出来父节点.
		  Dept parentDept = new Dept();
			int i = parentDept.Retrieve("Name", parentDeptName);
			if (i == 0)
			{
				return "err@没有找到当前部门[" + deptName + "]的上一级部门[" + parentDeptName + "]";
			}

			Dept myDept = new Dept();

			//如果数据库存在.
			i = parentDept.Retrieve("Name", deptName);
			if (i >= 1)
			{
				continue;
			}

			//插入部门.
			myDept.setName(deptName);
		 //   myDept.OrgNo = bp.web.WebUser.getOrgNo();
			myDept.setNo(DBAccess.GenerGUID(0, null, null));
			myDept.setParentNo(parentDept.getNo());
			myDept.setLeader(leader); //领导.
			myDept.setIdx(idx);
			myDept.Insert();
		}

			///#endregion 插入数据到 Port_Dept.


			///#region 插入到 Port_Emp.
		idx = 0;
		for (DataRow dr : dtEmp.Rows)
		{
			String empNo = dr.getValue("人员帐号").toString();
			String empName = dr.getValue("人员姓名").toString();
			String deptNames = dr.getValue("部门名称").toString();
			String deptPaths = dr.getValue("部门路径").toString();

			String stationNames = dr.getValue("岗位名称").toString();
			String tel = dr.getValue("电话").toString();
			String email = dr.getValue("邮箱").toString();
			String leader = dr.getValue("直属领导").toString(); //部门领导.

			bp.port.Emp emp = new bp.port.Emp();
			int i = emp.Retrieve("No", empNo);
			if (i >= 1)
			{
				emp.setTel (tel);
				emp.setName (empName);
				emp.Update();
				continue;
			}

			//找到人员的部门.
			String[] myDeptStrs = deptNames.split("[,]", -1);
			Dept dept = new Dept();
			for (String deptName : myDeptStrs)
			{
				if (DataType.IsNullOrEmpty(deptName) == true)
				{
					continue;
				}

				i = dept.Retrieve("Name", deptName);
				if (i <= 0)
				{
					return "err@部门名称不存在." + deptName;
				}

				bp.port.DeptEmp de = new bp.port.DeptEmp();
				de.setFK_Dept(dept.getNo());
				de.setFK_Emp(empNo);
				de.setOrgNo(WebUser.getOrgNo());
				de.setMyPK(de.getFK_Dept() + "_" + de.getFK_Emp());
				de.Delete();
				de.Insert();
			}

			//插入岗位.
			String[] staNames = stationNames.split("[,]", -1);
		  bp.port.Station sta = new bp.port.Station();
			for (String staName : staNames)
			{
				if (DataType.IsNullOrEmpty(staName) == true)
				{
					continue;
				}

				i = sta.Retrieve("Name", staName);
				if (i == 0)
				{
					return "err@岗位名称不存在." + staName;
				}

				bp.port.DeptEmpStation des = new bp.port.DeptEmpStation();
				des.setFK_Dept(dept.getNo());
				des.setFK_Emp(empNo);
				des.setFK_Station(sta.getNo());
			 //   des.OrgNo = WebUser.getOrgNo();
				des.setMyPK(des.getFK_Dept() + "_" + des.getFK_Emp() + "_" + des.getFK_Station());
				des.Delete();
				des.Insert();
			}

			//插入到数据库.
			emp.setNo(empNo);
		 //   emp.setUserID (empNo;
			emp.setName (empName);
			emp.setFK_Dept  (dept.getNo());
		   // emp.setOrgNo( WebUser.getOrgNo();
			emp.setTel (tel);
			//emp.Email = email;
			//emp.Leader = leader;
			//emp.Idx = idx;

			emp.Insert();
		}

			///#endregion 插入到 Port_Emp.


		//删除临时文件
	  //  System.IO.File.Delete(filePath);

		return "执行完成.";
	}

	public String EnpDepts_Init() throws Exception {
		String empNo = this.getFK_Emp();
		if(DataType.IsNullOrEmpty(empNo)==true)
			return "err@参数FK_Emp不能为空";
		Emp emp = new Emp(empNo);
		DataSet ds = new DataSet();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		//获取当前人员所在的部门及兼职部门
		String sql = "SELECT B.No AS \'FK_Dept\',B.Name AS \'FK_DeptText\',A.MyPK AS \'MyPK\' From Port_DeptEmp A,Port_Dept B WHERE A.FK_Dept=B.No AND A.FK_Emp="+dbstr+"FK_Emp";
		if(SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			sql +=" B.OrgNo='"+WebUser.getOrgNo()+"'";
		Paras ps = new Paras();
		ps.SQL = sql;
		ps.Add("FK_Emp",empNo);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if(dt.Rows.size()==0){
			DeptEmp deptEmp =new DeptEmp();
			deptEmp.setFK_Dept(emp.getFK_Dept());
			deptEmp.setFK_Emp(emp.getNo());
			deptEmp.setMyPK(emp.getFK_Dept() + "_" + emp.getNo());
			deptEmp.Insert();
			DataRow dr = dt.NewRow();
			dr.setValue(0,emp.getFK_Dept());
			dr.setValue(1,emp.getFK_DeptText());
			dr.setValue(2,deptEmp.getMyPK());
			dt.Rows.AddRow(dr);
		}
		dt.TableName = "Port_DeptEmp";
		ds.Tables.add(dt);
		ps.clear();
		//获取岗位
		sql="SELECT B.No AS \'FK_Station\',B.Name AS \'FK_StationText\' ,A.FK_Dept From Port_DeptEmpStation A,Port_Station B WHERE A.FK_Station=B.No AND A.FK_Emp="+dbstr+"FK_Emp";
		if(SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			sql +=" B.OrgNo='"+WebUser.getOrgNo()+"'";

		ps.SQL = sql;
		ps.Add("FK_Emp",empNo);
		dt = DBAccess.RunSQLReturnTable(ps);
		dt.TableName = "Port_DeptEmpStation";
		ds.Tables.add(dt);
		return bp.tools.Json.ToJson(ds);
	}

	/**
	 取消人员部门岗位管理关系
	 @return
	 */
	public final String DeptEmpStation_Dele() throws Exception {
		String sql = "delete from Port_DeptEmpStation where FK_Emp='" + this.GetRequestVal("FK_Emp") + "' and FK_Dept='" + this.GetRequestVal("FK_Dept") + "'";
		DBAccess.RunSQL(sql);
		return "执行成功 ";
	}
}
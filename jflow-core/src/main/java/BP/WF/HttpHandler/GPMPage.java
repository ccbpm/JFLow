package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.CommonUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Tools.DataTableConvertJson;
import BP.Tools.FileAccess;
import BP.Web.*;
import BP.Port.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

/**
 * 页面功能实体
 */
public class GPMPage extends WebContralBase {

	/// #region 签名.
	/**
	 * 图片签名初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Siganture_Init() throws Exception {
		if (WebUser.getNoOfRel() == null) {
			return "err@登录信息丢失";
		}
		Hashtable ht = new Hashtable();
		ht.put("No", WebUser.getNo());
		ht.put("Name", WebUser.getName());
		ht.put("FK_Dept", WebUser.getFK_Dept());
		ht.put("FK_DeptName", WebUser.getFK_DeptName());
		return BP.Tools.Json.ToJson(ht);
	}

	/**
	 * 签名保存
	 * 
	 * @return
	 */
	public final String Siganture_Save() {
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

	/// #endregion

	/// #region 组织结构维护.
	/**
	 * 初始化组织结构部门表维护.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Organization_Init() throws Exception {

		BP.GPM.Depts depts = new BP.GPM.Depts();
		if (WebUser.getNo().equals("admin") == false) {
			depts.Retrieve("ParentNo", WebUser.getFK_Dept());
			depts.AddEntity(new Dept(WebUser.getFK_Dept()));
			return depts.ToJson();
		}

		depts.RetrieveAll();

		return depts.ToJson();
	}

	/**
	 * 获取该部门的所有人员
	 * 
	 * @return
	 * 
	 */
	public final String LoadDatagridDeptEmp_Init() {
		String deptNo = this.GetRequestVal("deptNo");
		if (DataType.IsNullOrEmpty(deptNo)) {
			return "{ total: 0, rows: [] }";
		}
		String orderBy = this.GetRequestVal("orderBy");

		String searchText = this.GetRequestVal("searchText");
		if (!DataType.IsNullOrEmpty(searchText)) {
			searchText.trim();
		}
		String addQue = "";
		if (!DataType.IsNullOrEmpty(searchText)) {
			addQue = "  AND (pe.No like '%" + searchText + "%' or pe.Name like '%" + searchText + "%') ";
		}

		String pageNumber = this.GetRequestVal("pageNumber");
		int iPageNumber = DataType.IsNullOrEmpty(pageNumber) ? 1 : Integer.parseInt(pageNumber);
		// 每页多少行
		String pageSize = this.GetRequestVal("pageSize");
		int iPageSize = DataType.IsNullOrEmpty(pageSize) ? 9999 : Integer.parseInt(pageSize);

		String sql = "(select pe.*,pd.name FK_DutyText from port_emp pe left join port_duty pd on pd.no = pe.fk_duty where pe.no in (select fk_emp from Port_DeptEmp where fk_dept='"
				+ deptNo + "') " + addQue + " ) dbSo ";

		return DBPaging(sql, iPageNumber, iPageSize, "No", orderBy);

	}

	/**
	 * 以下算法只包含 oracle mysql sqlserver 三种类型的数据库 qin
	 * 
	 * @param dataSource
	 *            表名
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            当前页数据条数
	 * @param key
	 *            计算总行数需要
	 * @param orderKey
	 *            排序字段
	 * @return
	 */
	public final String DBPaging(String dataSource, int pageNumber, int pageSize, String key, String orderKey) {
		String sql = "";
		String orderByStr = "";

		if (!DataType.IsNullOrEmpty(orderKey)) {
			orderByStr = " ORDER BY " + orderKey;
		}

		switch (DBAccess.getAppCenterDBType()) {
		case Oracle:
		case DM:
			int beginIndex = (pageNumber - 1) * pageSize + 1;
			int endIndex = pageNumber * pageSize;

			sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN " + "FROM (SELECT * FROM  " + dataSource + orderByStr
					+ ") A WHERE ROWNUM <= " + endIndex + " ) WHERE RN >=" + beginIndex;
			break;
		case MSSQL:
			sql = "SELECT TOP " + pageSize + " * FROM " + dataSource + " WHERE " + key + " NOT IN  (" + "SELECT TOP ("
					+ pageSize + "*(" + pageNumber + "-1)) " + key + " FROM " + dataSource + " )" + orderByStr;
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

	/// #endregion

	// #endregion 组织结构维护.

	public final String StationToDeptEmp_Init() {
		return "";
	}

	/**
	 * 处理系统编辑菜单.
	 * 
	 * @return
	 */
	public final String AppMenu_Init() {
		// BP.GPM.App app = new BP.GPM.App();
		// app.No = "CCFlowBPM";
		// if (app.RetrieveFromDBSources() == 0)
		// {
		// BP.GPM.App.InitBPMMenu();
		// app.Retrieve();
		// }
		return "";
	}

	/**
	 * 构造函数
	 */
	public GPMPage() {
	}

	/// #region 执行父类的重写方法.
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		switch (this.getDoType()) {
		case "DtlFieldUp": // 字段上移
			return "执行成功.";
		default:
			break;
		}

		// 找不不到标记就抛出异常.
		throw new RuntimeException(
				"@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + CommonUtils.getRequest().getRequestURI());
	}

	/// #endregion 执行父类的重写方法.

	/// #region xxx 界面 .

	/// #endregion xxx 界面方法.

}
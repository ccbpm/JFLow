package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.CommonUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.Cryptos;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Port.WFEmp;
import BP.WF.Port.WFEmpAttr;
import BP.WF.Template.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import java.io.*;

/**
 * 页面功能实体
 */
public class WF_Setting extends WebContralBase {
	/**
	 * 构造函数
	 */
	public WF_Setting() {
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

	public final String Default_Init() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		BP.Port.Emp emp = new Emp();
		emp.setNo(WebUser.getNo());
		emp.Retrieve();

		// 部门名称.
		ht.put("DeptName", emp.getFK_DeptText());

		if (SystemConfig.getOSModel() == OSModel.OneMore) {
			BP.GPM.DeptEmpStations des = new BP.GPM.DeptEmpStations();
			des.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Emp, WebUser.getNo());

			String depts = "";
			String stas = "";

			for (BP.GPM.DeptEmpStation item : des.ToJavaList()) {
				BP.Port.Dept dept = new Dept();
				dept.setNo(item.getFK_Dept());
				int count = dept.RetrieveFromDBSources();
				if (count != 0) {
					depts += dept.getName() + "、";
				}

				if (DataType.IsNullOrEmpty(item.getFK_Station()) == true) {
					continue;
				}

				if (DataType.IsNullOrEmpty(item.getFK_Dept()) == true) {
					// item.Delete();
					continue;
				}

				BP.Port.Station sta = new Station();
				sta.setNo(item.getFK_Station());
				count = sta.RetrieveFromDBSources();
				if (count != 0) {
					stas += sta.getName() + "、";
				}
			}

			ht.put("Depts", depts);
			ht.put("Stations", stas);
		}

		WFEmp wfemp = new WFEmp(WebUser.getNo());
		ht.put("Tel", wfemp.getTel());
		ht.put("Email", wfemp.getEmail());
		ht.put("Author", wfemp.getAuthor());

		return BP.Tools.Json.ToJson(ht);
	}

	/**
	 * 初始化
	 * 
	 * @return json数据
	 * @throws Exception
	 */
	public final String Author_Init() throws Exception {
		WFEmp emp = new WFEmp(WebUser.getNo());
		Hashtable ht = emp.getRow();
		ht.remove(WFEmpAttr.StartFlows); // 移除这一列不然无法形成json.
		return emp.ToJson();
	}

	public final String Author_Save() throws Exception {
		WFEmp emp = new WFEmp(WebUser.getNo());
		emp.setAuthor(this.GetRequestVal("Author"));
		emp.setAuthorDate(this.GetRequestVal("AuthorDate"));
		emp.setAuthorWay(this.GetRequestValInt("AuthorWay"));
		emp.Update();
		return "保存成功";
	}

	/// #region 图片签名.
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

	public final String Siganture_Save() {
		try {
			String empNo = this.GetRequestVal("EmpNo");
			if (DataType.IsNullOrEmpty(empNo) == true)
				empNo = WebUser.getNo();
			HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				String tempFilePath = BP.Sys.SystemConfig.getPathOfWebApp() + "/DataUser/Siganture/" + empNo + ".jpg";
				File tempFile = new File(tempFilePath);
				if (tempFile.exists()) {
					tempFile.delete();
				}
				CommonFileUtils.upload(request, "File_Upload", tempFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "err@执行失败";
		}

		return "文件上传成功";

	}

	public String HeadPic_Save() {

		try {
			String empNo = this.GetRequestVal("EmpNo");
			if (DataType.IsNullOrEmpty(empNo) == true)
				empNo = WebUser.getNo();
			HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				String tempFilePath = BP.Sys.SystemConfig.getPathOfWebApp() + "/DataUser/UserIcon/" + empNo + ".png";
				File tempFile = new File(tempFilePath);
				if (tempFile.exists()) {
					tempFile.delete();
				}
				CommonFileUtils.upload(request, "File_Upload", tempFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "err@执行失败";
		}

		return "文件上传成功";
	}

	/**
	 * 初始化切换部门.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String ChangeDept_Init() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT a.No,a.Name, NameOfPath, '0' AS  CurrentDept FROM Port_Dept A, Port_DeptEmp B WHERE A.No=B.FK_Dept AND B.FK_Emp="
				+ SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.Add("FK_Emp", WebUser.getNo());
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("CURRENTDEPT").ColumnName = "CurrentDept";
			dt.Columns.get("NAMEOFPATH").ColumnName = "NameOfPath";
		}

		// 设置当前的部门.
		for (DataRow dr : dt.Rows) {
			if (dr.get("No").toString().equals(WebUser.getFK_Dept())) {
				dr.setValue("CurrentDept", "1");
			}

			if (!dr.get("NameOfPath").toString().equals("")) {
				dr.setValue("Name", dr.get("NameOfPath"));
			}
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 提交选择的部门。
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String ChangeDept_Submit() throws Exception {
		String deptNo = this.GetRequestVal("DeptNo");
		BP.GPM.Dept dept = new BP.GPM.Dept(deptNo);

		WebUser.setFK_Dept(dept.getNo());
		WebUser.setFK_DeptName(dept.getName());
		WebUser.setFK_DeptNameOfFull(dept.getNameOfPath());

		WFEmp emp = new WFEmp(WebUser.getNo());
		emp.setStartFlows("");
		emp.Update();

		try {
			String sql = "UPDATE Port_Emp Set fk_dept='" + deptNo + "' WHERE no='" + WebUser.getNo() + "'";
			DBAccess.RunSQL(sql);
			BP.WF.Dev2Interface.Port_Login(WebUser.getNo());
		} catch (RuntimeException ex) {

		}

		return "@执行成功,已经切换到｛" + WebUser.getFK_DeptName() + "｝部门上。";
	}

	/// #endregion

	public final String UserIcon_Init() {
		return "";
	}

	public final String UserIcon_Save() {
		return "";
	}

	/// #region 修改密码.
	public final String ChangePassword_Init() {
		if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == true) {
			return "err@当前是组织结构集成模式，您不能修改密码，请在被集成的系统修改密码。";
		}

		return "";
	}

	/**
	 * 修改密码 .
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String ChangePassword_Submit() throws Exception {
		String oldPass = this.GetRequestVal("OldPass");
		String pass = this.GetRequestVal("Pass");

		BP.Port.Emp emp = new Emp(WebUser.getNo());
		if (emp.CheckPass(oldPass) == false) {
			return "err@旧密码错误.";
		}

		if (BP.Sys.SystemConfig.getIsEnablePasswordEncryption() == true) {
			pass = Cryptos.aesEncrypt(pass);
		}
		emp.setPass(pass);
		emp.Update();

		return "密码修改成功...";
	}

}
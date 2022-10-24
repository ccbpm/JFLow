package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.Encodes;
import bp.web.*;
import bp.port.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;

/** 
 页面功能实体
*/
public class WF_Setting extends WebContralBase
{
	/** 
	 清楚缓存
	 
	 @return 
	*/
	public final String Default_ClearCash() throws Exception {
		DBAccess.RunSQL("DELETE FROM Sys_UserRegedit WHERE FK_Emp='" + WebUser.getNo() + "' AND OrgNo='" + WebUser.getOrgNo() + "'");
		return "执行成功，请刷新菜单或者重新进入看看菜单权限是否有变化。";
	}
	public final String UpdateEmpNo() throws Exception {
		Emp emp = new Emp(WebUser.getNo());
		emp.setEmail(this.GetRequestVal("Email"));
		emp.setTel (this.GetRequestVal("Tel"));
		emp.setName (this.GetRequestVal("Name"));
		emp.Update();
		return "修改成功.";
	}
	/** 
	 构造函数
	*/
	public WF_Setting() throws Exception {
	}


		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod() throws Exception {
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + ContextHolderUtils.getRequest().getRequestURI());
	}

		///#endregion 执行父类的重写方法.


	public final String Default_Init() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		Emp emp = new Emp();
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			emp.setNo(WebUser.getOrgNo() + "_" + WebUser.getNo());
		}
		else
		{
			emp.setNo(WebUser.getNo());
		}
		emp.setUserID (WebUser.getNo());
		emp.Retrieve();

		//部门名称.
		ht.put("DeptName", emp.getFK_DeptText());


		DeptEmpStations des = new DeptEmpStations();
		des.Retrieve(DeptEmpStationAttr.FK_Emp, WebUser.getNo(), null);

		String depts = "";
		String stas = "";

		for (DeptEmpStation item : des.ToJavaList())
		{
			Dept dept = new Dept();
			dept.setNo(item.getFK_Dept());
			int count = dept.RetrieveFromDBSources();
			if (count != 0)
			{
				depts += dept.getName() + "、";
			}


			if (DataType.IsNullOrEmpty(item.getFK_Station()) == true)
			{
				continue;
			}

			if (DataType.IsNullOrEmpty(item.getFK_Dept()) == true)
			{
				//   item.Delete();
				continue;
			}

			Station sta = new Station();
			sta.setNo(item.getFK_Station());
			count = sta.RetrieveFromDBSources();
			if (count != 0)
			{
				stas += sta.getName() + "、";
			}
		}

		ht.put("Depts", depts);
		ht.put("Stations", stas);


		bp.wf.port.WFEmp wfemp = new bp.wf.port.WFEmp(WebUser.getNo());
		ht.put("Tel", emp.getTel());
		ht.put("Email", emp.getEmail());

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 初始化
	 
	 @return json数据
	*/
	public final String Author_Init() throws Exception {
		bp.wf.port.WFEmp emp = new bp.wf.port.WFEmp(WebUser.getNo());
		Hashtable ht = emp.getRow();
		ht.remove(bp.wf.port.WFEmpAttr.StartFlows); //移除这一列不然无法形成json.
		return emp.ToJson(true);
	}


		///#region 图片签名.
	public final String Siganture_Init() throws Exception {
		if (WebUser.getNoOfRel() == null)
		{
			return "err@登录信息丢失";
		}

		//首先判断是否存在，如果不存在就生成一个.
		//bp.wf.dts.GenerSiganture.GenerIt(WebUser.getNo(), WebUser.getName());


		Hashtable ht = new Hashtable();
		ht.put("No", WebUser.getNo());
		ht.put("Name", WebUser.getName());
		ht.put("FK_Dept", WebUser.getFK_Dept());
		ht.put("FK_DeptName", WebUser.getFK_DeptName());
		return bp.tools.Json.ToJson(ht);
	}
	public final String Siganture_Save() throws Exception {

		try {
			String empNo = this.GetRequestVal("EmpNo");
			if (DataType.IsNullOrEmpty(empNo) == true)
				empNo = WebUser.getNo();
			HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				String tempFilePath = SystemConfig.getPathOfWebApp() + "DataUser/Siganture/" + empNo + ".jpg";
				if (SystemConfig.getIsJarRun())
					tempFilePath = SystemConfig.getPhysicalPath()+"DataUser/Siganture/" + empNo + ".jpg";

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
		return "上传成功！";
	}

		///#endregion 图片签名.


		///#region 头像.
	public final String HeadPic_Save() throws Exception {
		try {
			String empNo = this.GetRequestVal("EmpNo");
			if (DataType.IsNullOrEmpty(empNo) == true)
				empNo = WebUser.getNo();
			HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				String tempFilePath = SystemConfig.getPathOfWebApp() + "DataUser/UserIcon/" + empNo + ".png";
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

		return "上传成功！";
	}

		///#endregion 头像.


		///#region 切换部门.
	/** 
	 初始化切换部门.
	 
	 @return 
	*/
	public final String ChangeDept_Init() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT a.No,a.Name, NameOfPath, '0' AS  CurrentDept FROM Port_Dept A, Port_DeptEmp B WHERE A.No=B.FK_Dept AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.Add("FK_Emp", WebUser.getNo(), false);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		if (dt.Rows.size() == 0)
		{
			String sql = "SELECT a.No,a.Name,B.NameOfPath, '1' as CurrentDept FROM ";
			sql += " Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No  AND A.No='" + WebUser.getNo() + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
		}

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("CURRENTDEPT").ColumnName = "CurrentDept";
			dt.Columns.get("NAMEOFPATH").ColumnName = "NameOfPath";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("currentdept").ColumnName = "CurrentDept";
			dt.Columns.get("nameofpath").ColumnName = "NameOfPath";
		}

		//设置当前的部门.
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("No").toString().equals(WebUser.getFK_Dept()))
			{
				dr.setValue("CurrentDept", "1");
			}

			if (!dr.getValue("NameOfPath").toString().equals(""))
			{
				dr.setValue("Name", dr.getValue("NameOfPath"));
			}
		}

		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 提交选择的部门。
	 
	 @return 
	*/
	public final String ChangeDept_Submit() throws Exception {
		String deptNo = this.GetRequestVal("DeptNo");
		Dept dept = new Dept(deptNo);

		WebUser.setFK_Dept(dept.getNo());
		WebUser.setFK_DeptName(dept.getName());
		WebUser.setFK_DeptNameOfFull(dept.getNameOfPath());
		bp.wf.port.WFEmp emp = new bp.wf.port.WFEmp(WebUser.getNo());
		emp.setStartFlows("");
		emp.Update();

		try
		{
			String sql = "";

			if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			{
				sql = "UPDATE Port_Emp SET fk_dept='" + deptNo + "' WHERE UserID='" + WebUser.getNo() + "' AND OrgNo='" + WebUser.getOrgNo() + "'";
			}
			else
			{
				sql = "UPDATE Port_Emp SET fk_dept='" + deptNo + "' WHERE No='" + WebUser.getNo() + "'";
			}


			DBAccess.RunSQL(sql);
			Dev2Interface.Port_Login(WebUser.getNo());
		}
		catch (RuntimeException ex)
		{

		}

		return "@执行成功,已经切换到｛" + WebUser.getFK_DeptName() + "｝部门上。";
	}

		///#endregion

	public final String UserIcon_Init() throws Exception {
		return "";
	}

	public final String UserIcon_Save() throws Exception {
		return "";
	}



		///#region 修改密码.
	public final String ChangePassword_Init() throws Exception {
		if (DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType( )) == true)
		{
			return "err@当前是组织结构集成模式，您不能修改密码，请在被集成的系统修改密码。";
		}

		return "";
	}
	/** 
	 修改密码 .
	 
	 @return 
	*/
	public final String ChangePassword_Submit() throws Exception {
		String oldPass = this.GetRequestVal("OldPass");
		String pass = this.GetRequestVal("Pass");

		Emp emp = new Emp(WebUser.getNo());
		if (emp.CheckPass(oldPass) == false)
		{
			return "err@旧密码错误.";
		}

		if (SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			pass = Encodes.encodeBase64(pass);
		}
		emp.setIsPass (pass);
		emp.Update();

		return "密码修改成功...";
	}

		///#endregion 修改密码.


	public final String SetUserTheme() throws Exception {
		String theme = this.GetRequestVal("Theme");
		bp.wf.port.WFEmp emp = new bp.wf.port.WFEmp(WebUser.getNo());
		emp.SetPara("Theme", theme);
		emp.Update();

		return "设置成功";
	}

}
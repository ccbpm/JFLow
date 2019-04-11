package BP.WF.HttpHandler;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.HttpHandler.Base.CommonFileUtils;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class WF_Setting extends WebContralBase
{
	
	/**
	 * 构造函数
	 */
	public WF_Setting()
	{
	
	}
	
	

		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		return "";
	}

	public final String Default_Init() throws Exception
	{
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		BP.Port.Emp emp = new Emp();
		emp.setNo(WebUser.getNo());
		emp.Retrieve();

		//部门名称.
		ht.put("DeptName", emp.getFK_DeptText());

		 
			BP.GPM.DeptEmpStations des = new BP.GPM.DeptEmpStations();
			des.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Emp, WebUser.getNo());

			String depts = "";
			String stas = "";

			for (BP.GPM.DeptEmpStation item : des.ToJavaList()
					)
			{
				BP.Port.Dept dept = new Dept();
				dept.setNo(item.getFK_Dept());
				int count = dept.RetrieveFromDBSources();
				if(count !=0)
					depts += dept.getName() + "、";


				BP.Port.Station sta = new Station();
				sta.setNo(item.getFK_Station());
				count = sta.RetrieveFromDBSources();
				if(count!=0)
					stas += sta.getName() + "、";
			}

			ht.put("Depts", depts);
			ht.put("Stations", stas);
		

		 

		BP.WF.Port.WFEmp wfemp = new BP.WF.Port.WFEmp(WebUser.getNo());
		ht.put("Tel", wfemp.getTel());
		ht.put("Email", wfemp.getEmail());

		return BP.Tools.Json.ToJson(ht);
	}

		///#region 图片签名.
	public final String Siganture_Init() throws Exception
	{
		if (BP.Web.WebUser.getNoOfRel() == null)
            return "err@登录信息丢失";

		java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("No", BP.Web.WebUser.getNo());
        ht.put("Name", BP.Web.WebUser.getName());
        ht.put("FK_Dept", BP.Web.WebUser.getFK_Dept());
        ht.put("FK_DeptName", BP.Web.WebUser.getFK_DeptName());
        return BP.Tools.Json.ToJson(ht);
	}
	

	public final String Siganture_Save()
	{
		try
		{
			String empNo = this.GetRequestVal("EmpNo");
	        if (DataType.IsNullOrEmpty(empNo) == true)
	             empNo = WebUser.getNo();
	        HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) { 				
				String tempFilePath = BP.Sys.SystemConfig.getPathOfWebApp() + "/DataUser/Siganture/" + empNo + ".jpg";
				File tempFile = new File(tempFilePath);
				if(tempFile.exists()){
					tempFile.delete();
				}
//				MultipartFile multipartFile = ((DefaultMultipartHttpServletRequest)request).getFile("File_Upload");
//				multipartFile.transferTo(tempFile);
				CommonFileUtils.upload(request, "File_Upload", tempFile);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return "err@执行失败";
		}
		
		return "文件上传成功";
    
	}

		///#endregion 图片签名.

	public final String UserIcon_Init()
	{
		return "";
	}

	public final String UserIcon_Save()
	{
		return "";
	}
	
	/** 
	 修改密码 @于庆海.
	 
	 @return 
	 * @throws Exception 
*/
	public final String ChangePassword_Submit() throws Exception
	{
		String oldPass = this.GetRequestVal("OldPass");
		String pass = this.GetRequestVal("Pass");

		BP.Port.Emp emp = new Emp(BP.Web.WebUser.getNo());
		if (emp.CheckPass(oldPass) == false)
		{
			return "err@旧密码错误.";
		}

		if (BP.Sys.SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			pass = Cryptography.EncryptString(pass);
		}
		emp.setPass(pass);
		emp.Update();

		return "密码修改成功...";
	}
	
	/** 初始化切换部门.
	 
	 @return 
	 * @throws Exception 
	 */
	public final String ChangeDept_Init() throws Exception
	{
		String sql = "SELECT a.No,a.Name, NameOfPath, '0' AS  CurrentDept FROM Port_Dept A, Port_DeptEmp B WHERE A.No=B.FK_Dept AND B.FK_Emp='" + BP.Web.WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("CURRENTDEPT").ColumnName = "CurrentDept";
			dt.Columns.get("NAMEOFPATH").ColumnName = "NameOfPath";
		}

		//设置当前的部门.
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("No").toString().equals(WebUser.getFK_Dept()))
			{
				dr.setValue("CurrentDept","1");
			}

			if (!dr.getValue("NameOfPath").toString().equals(""))
			{
				dr.setValue("Name", dr.getValue("NameOfPath"));
			}
		}

		return BP.Tools.Json.ToJson(dt);
	}
}
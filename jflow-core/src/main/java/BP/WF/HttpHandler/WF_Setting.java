package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class WF_Setting extends WebContralBase
{
	
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

	public final String Default_Init()
	{
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		BP.Port.Emp emp = new Emp();
		emp.setNo(WebUser.getNo());
		emp.Retrieve();

		//部门名称.
		ht.put("DeptName", emp.getFK_DeptText());

		if (SystemConfig.getOSModel() == OSModel.OneMore)
		{
			BP.GPM.DeptEmpStations des = new BP.GPM.DeptEmpStations();
			des.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Emp, WebUser.getNo());

			String depts = "";
			String stas = "";

			for (BP.GPM.DeptEmpStation item : des.ToJavaList()
					)
			{
				BP.Port.Dept dept = new Dept(item.getFK_Dept());
				depts += dept.getName() + "、";


				BP.Port.Station sta = new Station(item.getFK_Station());
				stas += sta.getName() + "、";
			}

			ht.put("Depts", depts);
			ht.put("Stations", stas);
		}

		if (SystemConfig.getOSModel() == OSModel.OneOne)
		{
			BP.Port.EmpStations des = new BP.Port.EmpStations();
			des.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Emp, WebUser.getNo());

			String depts = "";
			String stas = "";

			for (BP.Port.EmpStation item : des.ToJavaList())
			{
				BP.Port.Station sta = new Station(item.getFK_Station());
				stas += sta.getName() + "、";
			}

			ht.put("Depts", emp.getFK_DeptText());
			ht.put("Stations", stas);
		}

		BP.WF.Port.WFEmp wfemp = new BP.WF.Port.WFEmp(WebUser.getNo());
		ht.put("Tel", wfemp.getTel());
		ht.put("Email", wfemp.getEmail());

		return BP.Tools.Json.ToJson(ht);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 图片签名.
	public final String Siganture_Init()
	{
		return "sss";
	}
	public final String Siganture_Save()
	{
		return "";

		//FileUpload f = (FileUpload)this.FindControl("F");
		//if (f.HasFile == false)
		//    return "err@请上传文件.";

		////if (f.FileName.EndsW

		////判断文件类型.
		//string fileExt = ",bpm,jpg,jpeg,png,gif,";
		//string ext = f.FileName.Substring(f.FileName.LastIndexOf('.') + 1).ToLower();
		//if (fileExt.IndexOf(ext + ",") == -1)
		//{
		//    return "err@上传的文件必须是以图片格式:" + fileExt + "类型, 现在类型是:" + ext;
		//}

		//try
		//{
		//    string tempFile = BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/Siganture/T" + WebUser.No + ".jpg";
		//    if (System.IO.File.Exists(tempFile) == true)
		//        System.IO.File.Delete(tempFile);

		//    f.SaveAs(tempFile);
		//    System.Drawing.Image img = System.Drawing.Image.FromFile(tempFile);
		//    img.Dispose();
		//}
		//catch (Exception ex)
		//{
		//    return "err@"+ex.Message;
		//}

		//f.SaveAs(BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/Siganture/" + WebUser.No + ".jpg");
		//f.SaveAs(BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/Siganture/" + WebUser.Name + ".jpg");

		//f.PostedFile.InputStream.Close();
		//f.PostedFile.InputStream.Dispose();
		//f.Dispose();

		//   this.Response.Redirect(this.Request.RawUrl, true);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
*/
	public final String ChangePassword_Submit()
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
	 */
	public final String ChangeDept_Init()
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
package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.NetPlatformImpl.*;
import BP.WF.*;
import java.util.*;
import java.io.*;

/** 
 页面功能实体
*/
public class WF_Setting extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public WF_Setting()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.


	public final String Default_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.No);
		ht.put("UserName", WebUser.Name);

		BP.Port.Emp emp = new Emp();
		emp.No = WebUser.No;
		emp.Retrieve();

		//部门名称.
		ht.put("DeptName", emp.FK_DeptText);

		if (SystemConfig.OSModel == OSModel.OneMore)
		{
			BP.GPM.DeptEmpStations des = new BP.GPM.DeptEmpStations();
			des.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Emp, WebUser.No);

			String depts = "";
			String stas = "";

			for (BP.GPM.DeptEmpStation item : des)
			{
				BP.Port.Dept dept = new Dept();
				dept.No = item.FK_Dept;
				int count = dept.RetrieveFromDBSources();
				if (count != 0)
				{
					depts += dept.Name + "、";
				}


				if (DataType.IsNullOrEmpty(item.FK_Station) == true)
				{
					continue;
				}

				if (DataType.IsNullOrEmpty(item.FK_Dept) == true)
				{
					//   item.Delete();
					continue;
				}

				BP.Port.Station sta = new Station();
				sta.No = item.FK_Station;
				count = sta.RetrieveFromDBSources();
				if (count != 0)
				{
					stas += sta.Name + "、";
				}
			}

			ht.put("Depts", depts);
			ht.put("Stations", stas);
		}


		BP.WF.Port.WFEmp wfemp = new Port.WFEmp(WebUser.No);
		ht.put("Tel", wfemp.getTel());
		ht.put("Email", wfemp.getEmail());
		ht.put("Author", wfemp.getAuthor());

		return BP.Tools.Json.ToJson(ht);
	}
	/** 
	 初始化
	 
	 @return json数据
	*/
	public final String Author_Init()
	{
		BP.WF.Port.WFEmp emp = new Port.WFEmp(BP.Web.WebUser.No);
		Hashtable ht = emp.Row;
		ht.remove(BP.WF.Port.WFEmpAttr.StartFlows); //移除这一列不然无法形成json.
		return emp.ToJson();
	}
	public final String Author_Save()
	{
		BP.WF.Port.WFEmp emp = new Port.WFEmp(BP.Web.WebUser.No);
		emp.setAuthor(this.GetRequestVal("Author"));
		emp.setAuthorDate(this.GetRequestVal("AuthorDate"));
		emp.setAuthorWay(this.GetRequestValInt("AuthorWay"));
		emp.Update();
		return "保存成功";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 图片签名.
	public final String Siganture_Init()
	{
		if (BP.Web.WebUser.NoOfRel == null)
		{
			return "err@登录信息丢失";
		}

		Hashtable ht = new Hashtable();
		ht.put("No", BP.Web.WebUser.No);
		ht.put("Name", BP.Web.WebUser.Name);
		ht.put("FK_Dept", BP.Web.WebUser.FK_Dept);
		ht.put("FK_DeptName", BP.Web.WebUser.FK_DeptName);
		return BP.Tools.Json.ToJson(ht);
	}
	public final String Siganture_Save()
	{
		//HttpPostedFile f = context.Request.Files[0];
		String empNo = this.GetRequestVal("EmpNo");
		if (DataType.IsNullOrEmpty(empNo) == true)
		{
			empNo = WebUser.No;
		}
		try
		{
			String tempFile = BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/Siganture/" + empNo + ".jpg";
			if ((new File(tempFile)).isFile() == true)
			{
				(new File(tempFile)).delete();
			}

			//f.SaveAs(tempFile);
			HttpContextHelper.UploadFile(tempFile);
			System.Drawing.Image img = System.Drawing.Image.FromFile(tempFile);
			img.Dispose();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

		//f.SaveAs(BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/Siganture/" + WebUser.No + ".jpg");
		// f.SaveAs(BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/Siganture/" + WebUser.Name + ".jpg");

		//f.PostedFile.InputStream.Close();
		//f.PostedFile.InputStream.Dispose();
		//f.Dispose();

		//   this.Response.Redirect(this.Request.RawUrl, true);
		return "上传成功！";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 图片签名.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 头像.
	public final String HeadPic_Save()
	{
		//HttpPostedFile f = context.Request.Files[0];
		String empNo = this.GetRequestVal("EmpNo");

		if (DataType.IsNullOrEmpty(empNo) == true)
		{
			empNo = WebUser.No;
		}
		try
		{
			String tempFile = BP.Sys.SystemConfig.PathOfWebApp + "/DataUser/UserIcon/" + empNo + ".png";
			if ((new File(tempFile)).isFile() == true)
			{
				(new File(tempFile)).delete();
			}

			//f.SaveAs(tempFile);
			HttpContextHelper.UploadFile(tempFile);
			System.Drawing.Image img = System.Drawing.Image.FromFile(tempFile);
			img.Dispose();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

		return "上传成功！";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 头像.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 切换部门.
	/** 
	 初始化切换部门.
	 
	 @return 
	*/
	public final String ChangeDept_Init()
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT a.No,a.Name, NameOfPath, '0' AS  CurrentDept FROM Port_Dept A, Port_DeptEmp B WHERE A.No=B.FK_Dept AND B.FK_Emp=" + SystemConfig.AppCenterDBVarStr + "FK_Emp";
		ps.Add("FK_Emp", BP.Web.WebUser.No);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			dt.Columns["NO"].ColumnName = "No";
			dt.Columns["NAME"].ColumnName = "Name";
			dt.Columns["CURRENTDEPT"].ColumnName = "CurrentDept";
			dt.Columns["NAMEOFPATH"].ColumnName = "NameOfPath";
		}

		//设置当前的部门.
		for (DataRow dr : dt.Rows)
		{
			if (dr.get("No").toString().equals(WebUser.FK_Dept))
			{
				dr.set("CurrentDept", "1");
			}

			if (!dr.get("NameOfPath").toString().equals(""))
			{
				dr.set("Name", dr.get("NameOfPath"));
			}
		}

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 提交选择的部门。
	 
	 @return 
	*/
	public final String ChangeDept_Submit()
	{
		String deptNo = this.GetRequestVal("DeptNo");
		BP.GPM.Dept dept = new GPM.Dept(deptNo);

		BP.Web.WebUser.FK_Dept = dept.No;
		BP.Web.WebUser.FK_DeptName = dept.Name;
		BP.Web.WebUser.FK_DeptNameOfFull = dept.NameOfPath;

		////重新设置cookies.
		//string strs = "";
		//strs += "@No=" + WebUser.No;
		//strs += "@Name=" + WebUser.Name;
		//strs += "@FK_Dept=" + WebUser.FK_Dept;
		//strs += "@FK_DeptName=" + WebUser.FK_DeptName;
		//strs += "@FK_DeptNameOfFull=" + WebUser.FK_DeptNameOfFull;
		//BP.Web.WebUser.SetValToCookie(strs);

		BP.WF.Port.WFEmp emp = new Port.WFEmp(WebUser.No);
		emp.setStartFlows("");
		emp.Update();

		try
		{
			String sql = "UPDATE Port_Emp Set fk_dept='" + deptNo + "' WHERE no='" + WebUser.No + "'";
			DBAccess.RunSQL(sql);
			BP.WF.Dev2Interface.Port_Login(WebUser.No);
		}
		catch (RuntimeException ex)
		{

		}

		return "@执行成功,已经切换到｛" + BP.Web.WebUser.FK_DeptName + "｝部门上。";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String UserIcon_Init()
	{
		return "";
	}

	public final String UserIcon_Save()
	{
		return "";
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 修改密码.
	public final String ChangePassword_Init()
	{
		if (BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.AppCenterDBType) == true)
		{
			return "err@当前是组织结构集成模式，您不能修改密码，请在被集成的系统修改密码。";
		}

		return "";
	}
	/** 
	 修改密码 .
	 
	 @return 
	*/
	public final String ChangePassword_Submit()
	{
		String oldPass = this.GetRequestVal("OldPass");
		String pass = this.GetRequestVal("Pass");

		BP.Port.Emp emp = new Emp(BP.Web.WebUser.No);
		if (emp.CheckPass(oldPass) == false)
		{
			return "err@旧密码错误.";
		}

		if (BP.Sys.SystemConfig.IsEnablePasswordEncryption == true)
		{
			pass = BP.Tools.Cryptography.EncryptString(pass);
		}
		emp.Pass = pass;
		emp.Update();

		return "密码修改成功...";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 修改密码.

}
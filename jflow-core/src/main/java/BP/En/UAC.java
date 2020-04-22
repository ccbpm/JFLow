package BP.En;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Sys.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 访问控制
*/
public class UAC
{

		///#region 常用方法
	/** 
	 从权限管理系统里装载数据.
	 * @throws Exception 
	*/
	public final void LoadRightFromCCGPM(Entity en) throws Exception
	{
		String sql = "SELECT Tag1  FROM V_GPM_EmpMenu WHERE  FK_Emp='" + WebUser.getNo() + "'  AND Url LIKE '%" + en.toString() + "%'  ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			String tag = dr.getValue(0).toString();

			if (tag.contains("Insert") == true)
			{
				this.IsInsert = true;
			}
			if (tag.contains("Update") == true)
			{
				this.IsUpdate = true;
			}
			if (tag.contains("Delete") == true)
			{
				this.IsDelete = true;
			}
		}

	}
	public final void Readonly()
	{
		this.IsUpdate = false;
		this.IsDelete = false;
		this.IsInsert = false;
		this.IsAdjunct = false;
		this.IsView = true;
	}
	/** 
	 全部开放
	*/
	public final void OpenAll()
	{
		this.IsUpdate = true;
		this.IsDelete = true;
		this.IsInsert = true;
		this.IsAdjunct = false;
		this.IsView = true;
	}
	/** 
	 为一个岗位设置全部的权限
	 
	 @param fk_station
	 * @throws Exception 
	*/
	public final void OpenAllForStation(String fk_station) throws Exception
	{
		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo());
		ps.Add("FK_Station", fk_station);

		boolean bl;

			bl = DBAccess.IsExits("SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND FK_Station=" + SystemConfig.getAppCenterDBVarStr() + "FK_Station", ps);

		if (bl)
		{
			this.OpenAll();
		}
		else
		{
			this.Readonly();
		}
	}
	/** 
	 仅仅对管理员
	 * @throws Exception 
	*/
	public final UAC OpenForSysAdmin() throws Exception
	{
		if (WebUser.getNo().equals("admin"))
		{
			this.OpenAll();
		}

		return this;
	}
	public final UAC OpenForAppAdmin() throws Exception
	{
		if (WebUser.getNo() != null && WebUser.getNo().contains("admin") == true)
		{
			this.OpenAll();
		}
		return this;
	}
	public final UAC OpenForSetAdmin() throws Exception
	{
		String adminNos= SystemConfig.getAdmins();
		if (adminNos.contains(WebUser.getNo()) == true)
		{
			this.OpenAll();
		}
		return this;
	}
		///#endregion


		///#region 控制属性
	/** 
	 是否插入
	*/
	public boolean IsInsert = false;
	/** 
	 是否删除
	*/
	public boolean IsDelete = false;
	/** 
	 是否更新
	*/
	public boolean IsUpdate = false;
	/** 
	 是否查看
	*/
	public boolean IsView = true;
	/** 
	 附件
	*/
	public boolean IsAdjunct = false;
	/** 
	 是否可以导出
	 <p>注意：要启用导出权限控制，请使用uac.IsExp = UserRegedit.HaveRoleForExp(this.ToString());</p>
	*/
	public boolean IsExp = false;
	/** 
	 是否可以导入
	 <p>注意：要启用导入权限控制，请使用uac.IsImp = UserRegedit.HaveRoleForImp(this.ToString());</p>
	*/
	public boolean IsImp = false;

		///#endregion


		///#region 构造
	/** 
	 用户访问
	*/
	public UAC()
	{

	}

		///#endregion
}
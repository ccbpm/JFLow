package BP.WF.Port;

import BP.DA.DBAccess;
import BP.En.Entities;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.UAC;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.WF.Flow;
import BP.WF.Flows;

/** 
 Emp 的摘要说明。
*/
public class Emp extends EntityNoName
{


		
	/** 
	 主要的部门。
	*/
	public final Dept getHisDept()
	{

		try
		{
			return new Dept(this.getFK_Dept());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取操作员" + this.getNo() + "部门[" + this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@" + ex.getMessage());
		}
	}
	/** 
	 工作岗位集合。
	*/
	public final Stations getHisStations()
	{
		EmpStations sts = new EmpStations();
		Stations mysts = sts.GetHisStations(this.getNo());
		return mysts;
			//return new Station(this.FK_Station);
	}
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}
	public final String getFK_DeptText()
	{
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}
	/** 
	 密码
	*/
	public final String getPass()
	{
		return this.GetValStrByKey(EmpAttr.Pass);
	}
	public final void setPass(String value)
	{
		this.SetValByKey(EmpAttr.Pass, value);
	}

	public final boolean CheckPass(String pass)
	{
		if (this.getPass().equals(pass))
		{
			return true;
		}
		return false;
	}
	/** 
	 工作人员
	*/
	public Emp()
	{
	}
	/** 
	 工作人员编号
	 @param _No No
	*/
	public Emp(String no)
	{
		this.setNo(no.trim());
		if (this.getNo().length() == 0)
		{
			throw new RuntimeException("@要查询的操作员编号为空。");
		}
		try
		{
			this.Retrieve();
		}
		catch (RuntimeException ex1)
		{
			int i = this.RetrieveFromDBSources();
			if (i == 0)
			{
				throw new RuntimeException("@用户或者密码错误：[" + no + "]，或者帐号被停用。@技术信息(从内存中查询出现错误)：ex1=" + ex1.getMessage());
			}
		}
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Emp", "用户");

			//关于字段属性的增加 
		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 20, 100);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 100, 100);
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 20, 10);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new BP.Port.Depts(), true);
		map.AddTBString(EmpAttr.SID, null, "SID", false, false, 0, 20, 10);

		map.AddSearchAttr(EmpAttr.FK_Dept); //查询条件.

			//增加点对多属性 一个操作员的部门查询权限与岗位权限.
		map.getAttrsOfOneVSM().Add(new EmpStations(), new Stations(), EmpStationAttr.FK_Emp, EmpStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "岗位权限");

		RefMethod rm = new RefMethod();
		rm.Title = "禁用";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoDisableIt";
		map.AddRefMethod(rm);
		rm = new RefMethod();
		rm.Title = "启用";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoEnableIt";
		map.AddRefMethod(rm);



		rm = new RefMethod();
		rm.Title = "更改登录帐号";
		rm.Warning = "您确定要处理吗？如果确定，该用户当前的待办信息一起其他的流程信息都会被重置到新编号里。";
		rm.getHisAttrs().AddTBString("FieldNew", null, "新帐号", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeUserNo";
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 重置当前用户编号
	 
	 @param userNo 当前用户编号
	 @return 返回重置信息
	*/
	public final String DoChangeUserNo(String userNo)
	{
		if ( ! BP.Web.WebUser.getNo().equals("admin"))
		{
			return "非超级管理员，不能执行。";
		}

		String msg = "";
		int i = 0;
		//更新待办.
		String sql = "update wf_generworkerlist set fk_emp='"+userNo+"' where fk_emp='"+this.getNo()+"'";
		i= BP.DA.DBAccess.RunSQL(sql);
		if (i != 0)
		{
			msg += "@待办更新[" + i + "]个。";
		}

		sql = "UPDATE WF_GENERWORKFLOW  SET STARTER='"+userNo+"'  WHERE STARTER='"+this.getNo()+"'";
		i = BP.DA.DBAccess.RunSQL(sql);
		if (i != 0)
		{
			msg += "@流程注册更新[" + i + "]个。";
		}


		//更换流程信息的数据表
		BP.WF.Flows fls = new Flows();
		fls.RetrieveAll();
		for (Flow fl : fls.ToJavaList())
		{
			sql = "UPDATE " + fl.getPTable() + " SET FlowEnder='" + userNo + "' WHERE FlowEnder='" + this.getNo() + "'";
			i = DBAccess.RunSQL(sql);

			if (i != 0)
			{
				msg += "@流程注册更新[" + i + "]个。";
			}

			sql = "UPDATE  " + fl.getPTable() + "  SET FlowStarter='" + userNo + "' WHERE FlowStarter='" + this.getNo() + "'";
			i = DBAccess.RunSQL(sql);
			if (i != 0)
			{
				msg += "@流程业务表发起人，更新了[" + i + "]个。";
			}


			sql = "UPDATE  " + fl.getPTable() + "  SET Rec='" + userNo + "' WHERE Rec='" + this.getNo() + "'";
			i = DBAccess.RunSQL(sql);
			if (i != 0)
			{
				msg += "@流程业务表记录人，更新了[" + i + "]个。";
			}

			String trackTable = "ND" + Integer.parseInt(fl.getNo()) + "Track";
			sql = "UPDATE  " + trackTable + "  SET EmpFrom='" + userNo + "' WHERE EmpFrom='" + this.getNo() + "'";
			i = DBAccess.RunSQL(sql);
			if (i != 0)
			{
				msg += "@轨迹表 EmpFrom，更新了[" + i + "]个。";
			}


			sql = "UPDATE  " + trackTable + "  SET EmpTo='" + userNo + "' WHERE EmpTo='" + this.getNo() + "'";
			i = DBAccess.RunSQL(sql);
			if (i != 0)
			{
				msg += "@轨迹表 EmpTo，更新了[" + i + "]个。";
			}


			sql = "UPDATE  " + trackTable + "  SET Exer='" + userNo + "' WHERE Exer='" + this.getNo() + "'";
			i = DBAccess.RunSQL(sql);
			if (i != 0)
			{
				msg += "@轨迹表 Exer，更新了[" + i + "]个。";
			}
		}


		//更新其他字段.
		BP.Sys.MapAttrs attrs = new MapAttrs();
		attrs.RetrieveAll();
		for (BP.Sys.MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getDefValReal().contains("WebUser.No") == true)
			{
				try
				{
					BP.Sys.MapData md = new MapData(attr.getFK_MapData());
					sql = "UPDATE " + md.getPTable() + " SET " + attr.getKeyOfEn() + "='" + userNo + "' WHERE " + attr.getKeyOfEn() + "='" + this.getNo() + "'";
					i = DBAccess.RunSQL(sql);
					if (i != 0)
					{
						msg += "@表[" + md.getName() + "],[" + md.getPTable() + "] [" + attr.getKeyOfEn() + "]，更新了[" + i + "]个。";
					}
				}
				catch (java.lang.Exception e)
				{

				}
			}
		}
		//人员主表信息-手动修改
		return msg;
	}
	/** 
	 执行禁用
	*/
	public final String DoDisableIt()
	{
		WFEmp emp = new WFEmp(this.getNo());
		emp.setUseSta(0);
		emp.Update();
		return "已经执行(禁用)成功";
	}
	/** 
	 执行启用
	*/
	public final String DoEnableIt()
	{
		WFEmp emp = new WFEmp(this.getNo());
		emp.setUseSta(1);
		emp.Update();
		return "已经执行(启用)成功";
	}

	@Override
	protected boolean beforeUpdate()
	{
		WFEmp emp = new WFEmp(this.getNo());
		emp.Update();
		return super.beforeUpdate();
	}
	@Override
	public Entities getGetNewEntities()
	{
		return new Emps();
	}
}
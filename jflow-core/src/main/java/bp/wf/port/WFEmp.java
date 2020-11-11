package bp.wf.port;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.port.*;
import bp.web.*;
import bp.wf.*;
import java.util.*;
import java.time.*;

/** 
 操作员
*/
public class WFEmp extends EntityNoName
{

		///基本属性
	public final String getHisAlertWayT() throws Exception
	{
		return this.GetValRefTextByKey(WFEmpAttr.AlertWay);
	}
	public final AlertWay getHisAlertWay()throws Exception
	{
		return AlertWay.forValue(this.GetValIntByKey(WFEmpAttr.AlertWay));
	}
	public final void setHisAlertWay(AlertWay value)throws Exception
	{
		SetValByKey(WFEmpAttr.AlertWay, value.getValue());
	}
	/** 
	 用户状态
	*/
	public final int getUseSta()throws Exception
	{
		return this.GetValIntByKey(WFEmpAttr.UseSta);
	}
	public final void setUseSta(int value) throws Exception
	{
		SetValByKey(WFEmpAttr.UseSta, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.FK_Dept, value);
	}
	/** 
	 风格文件
	*/
	public final String getStyle()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.Style);
	}
	public final void setStyle(String value) throws Exception
	{
		this.SetValByKey(WFEmpAttr.Style, value);
	}

	/** 
	 电话
	*/
	public final String getTel()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.Tel);
	}
	public final void setTel(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.Tel, value);
	}
	public final int getIdx()throws Exception
	{
		return this.GetValIntByKey(WFEmpAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		SetValByKey(WFEmpAttr.Idx, value);
	}

	public final String getEmail()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.Email);
	}
	public final void setEmail(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.Email, value);
	}
	public final String getAuthor()throws Exception
	{
		return this.GetValStrByKey(WFEmpAttr.Author);
	}
	public final void setAuthor(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.Author, value);
	}
	public final String getAuthorDate()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.AuthorDate);
	}
	public final void setAuthorDate(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.AuthorDate, value);
	}
	public final String getAuthorToDate()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.AuthorToDate);
	}
	public final void setAuthorToDate(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.AuthorToDate, value);
	}
	/** 
	 授权的流程
	*/
	public final String getAuthorFlows()throws Exception
	{
		String s = this.GetValStringByKey(WFEmpAttr.AuthorFlows);
		s = s.replace(",", "','");
		return "('" + s + "')";
	}
	public final void setAuthorFlows(String value) throws Exception
	{
			//授权流程为空时的bug  解决
		if (!DataType.IsNullOrEmpty(value))
		{
			SetValByKey(WFEmpAttr.AuthorFlows, value.substring(1));
		}
		else
		{
			SetValByKey(WFEmpAttr.AuthorFlows, "");
		}
			//SetValByKey(WFEmpAttr.AuthorFlows, value.Substring(1));
	}
	/** 
	 发起流程.
	*/
	public final String getStartFlows()throws Exception
	{
		return this.GetValStrByKey(WFEmpAttr.StartFlows);
	}
	public final void setStartFlows(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.StartFlows, value);
	}
	/** 
	 图片签名密码
	*/
	public final String getSPass()throws Exception
	{
		return this.GetValStringByKey(WFEmpAttr.SPass);
	}
	public final void setSPass(String value) throws Exception
	{
		SetValByKey(WFEmpAttr.SPass, value);
	}

	/** 
	 授权方式
	*/
	public final AuthorWay getHisAuthorWay()throws Exception
	{
		return AuthorWay.forValue(this.getAuthorWay());
	}
	/** 
	 授权方式
	*/
	public final int getAuthorWay()throws Exception
	{
		return this.GetValIntByKey(WFEmpAttr.AuthorWay);
	}
	public final void setAuthorWay(int value) throws Exception
	{
		SetValByKey(WFEmpAttr.AuthorWay, value);
	}
	public final boolean getAuthorIsOK() throws Exception
	{
		int b = this.GetValIntByKey(WFEmpAttr.AuthorWay);
		if (b == 0)
		{
			return false; //不授权.
		}

			// if (DataType.IsNullOrEmpty(this.Author) == true)
			//  return false;

		if (this.getAuthorToDate().length() < 4)
		{
			return true; //没有填写时间,当做无期限
		}

		Date dt = DataType.ParseSysDateTime2DateTime(this.getAuthorToDate());
		if (dt.compareTo(new Date()) < 0)
		{
			return false;
		}

		return true;
	}

		///


		///构造函数
	/** 
	 操作员
	*/
	public WFEmp()
	{
	}
	/** 
	 操作员
	 
	 @param no
	 * @throws Exception 
	*/
	public WFEmp(String no) throws Exception
	{
		this.setNo( no);
		try
		{
			if (this.RetrieveFromDBSources() == 0)
			{
				Emp emp = new Emp(no);
				this.Copy(emp);
				this.Insert();
			}
		}
		catch (java.lang.Exception e)
		{
			this.CheckPhysicsTable();
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Emp", "操作员");

		map.AddTBStringPK(WFEmpAttr.No, null, "No", true, true, 1, 50, 36);
		map.AddTBString(WFEmpAttr.Name, null, "Name", true, false, 0, 50, 20);
		map.AddTBInt(WFEmpAttr.UseSta, 1, "用户状态0禁用,1正常.", true, true);

		map.AddTBString(WFEmpAttr.Tel, null, "Tel", true, true, 0, 50, 20);
		map.AddTBString(WFEmpAttr.FK_Dept, null, "FK_Dept", true, true, 0, 100, 36);
		map.AddTBString(WFEmpAttr.Email, null, "Email", true, true, 0, 50, 20);

		map.AddDDLSysEnum(WFEmpAttr.AlertWay, 3, "收听方式", true, true, WFEmpAttr.AlertWay);
		map.AddTBString(WFEmpAttr.Author, null, "授权人", true, true, 0, 50, 20);
		map.AddTBString(WFEmpAttr.AuthorDate, null, "授权日期", true, true, 0, 50, 20);

			//0不授权， 1完全授权，2，指定流程范围授权. 
		map.AddTBInt(WFEmpAttr.AuthorWay, 0, "授权方式", true, true);
		map.AddTBDate(WFEmpAttr.AuthorToDate, null, "授权到日期", true, true);

		map.AddTBString(WFEmpAttr.AuthorFlows, null, "可以执行的授权流程", true, true, 0, 3900, 0);

		map.AddTBString(WFEmpAttr.Stas, null, "岗位s", true, true, 0, 3000, 20);
		map.AddTBString(WFEmpAttr.Depts, null, "Deptss", true, true, 0, 100, 36);

		map.AddTBString(WFEmpAttr.Msg, null, "Msg", true, true, 0, 4000, 20);
		map.AddTBString(WFEmpAttr.Style, null, "Style", true, true, 0, 4000, 20);

			//map.AddTBStringDoc(WFEmpAttr.StartFlows, null, "可以发起的流程", true, true);

			//隶属组织.
		map.AddTBString("OrgNo", null, "OrgNo", true, true, 0, 100, 20);

		map.AddTBString(WFEmpAttr.SPass, null, "图片签名密码", true, true, 0, 200, 20);

		map.AddTBInt(WFEmpAttr.Idx, 0, "Idx", false, false);

		map.AddTBAtParas(3500); //增加字段.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///


		///方法
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		String msg = "";
		//if (this.Email.Length == 0)
		//{
		//    if (this.HisAlertWay == AlertWay.SMSAndEmail || this.HisAlertWay == AlertWay.Email)
		//        msg += "错误：您设置了用e-mail接收信息，但是您没有设置e-mail。";
		//}

		//if (this.Tel.Length == 0)
		//{
		//    if (this.HisAlertWay == AlertWay.SMSAndEmail || this.HisAlertWay == AlertWay.SMS)
		//        msg += "错误：您设置了用短信接收信息，但是您没有设置手机号。";
		//}

		//EmpStations ess = new EmpStations();
		//ess.Retrieve(EmpStationAttr.FK_Emp, this.getNo());
		//string sts = "";
		//foreach (EmpStation es in ess)
		//{
		//    sts += es.FK_StationT + " ";
		//}
		//this.Stas = sts;

		if (!msg.equals(""))
		{
			throw new RuntimeException(msg);
		}

		return super.beforeUpdate();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setUseSta(1);
		return super.beforeInsert();
	}

		///

	public static void DTSData() throws Exception
	{
		String sql = "select No from Port_Emp where No not in (select No from WF_Emp)";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			bp.port.Emp emp1 = new bp.port.Emp(dr.getValue("No").toString());
			bp.wf.port.WFEmp empWF = new bp.wf.port.WFEmp();
			empWF.Copy(emp1);
			try
			{
				empWF.setUseSta(1);
				empWF.DirectInsert();
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	public final void DoUp() throws Exception
	{
		this.DoOrderUp("FK_Dept", this.getFK_Dept(), "Idx");
		return;
	}
	public final void DoDown() throws Exception
	{
		this.DoOrderDown("FK_Dept", this.getFK_Dept(), "Idx");
		return;
	}
}
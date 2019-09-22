package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 操作员
*/
public class WFEmp extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	public final String getHisAlertWayT()
	{
		return this.GetValRefTextByKey(WFEmpAttr.AlertWay);
	}
	public final AlertWay getHisAlertWay()
	{
		return AlertWay.forValue(this.GetValIntByKey(WFEmpAttr.AlertWay));
	}
	public final void setHisAlertWay(AlertWay value)
	{
		SetValByKey(WFEmpAttr.AlertWay, value.getValue());
	}
	/** 
	 用户状态
	*/
	public final int getUseSta()
	{
		return this.GetValIntByKey(WFEmpAttr.UseSta);
	}
	public final void setUseSta(int value)
	{
		SetValByKey(WFEmpAttr.UseSta, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(WFEmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(WFEmpAttr.FK_Dept, value);
	}
	/** 
	 风格文件
	*/
	public final String getStyle()
	{
		return this.GetValStringByKey(WFEmpAttr.Style);
	}
	public final void setStyle(String value)
	{
		this.SetValByKey(WFEmpAttr.Style, value);
	}

	/** 
	 电话
	*/
	public final String getTel()
	{
		return this.GetValStringByKey(WFEmpAttr.Tel);
	}
	public final void setTel(String value)
	{
		SetValByKey(WFEmpAttr.Tel, value);
	}
	public final int getIdx()
	{
		return this.GetValIntByKey(WFEmpAttr.Idx);
	}
	public final void setIdx(int value)
	{
		SetValByKey(WFEmpAttr.Idx, value);
	}
	public final String getTelHtml()
	{
		if (this.getTel().length() == 0)
		{
			return "未设置";
		}
		else
		{
			return "<a href=\"javascript:WinOpen('./Msg/SMS.aspx?Tel=" + this.getTel() + "');\"  ><img src='/WF/Img/SMS.gif' border=0/>" + this.getTel() + "</a>";
		}
	}
	public final String getEmailHtml()
	{
		if (this.getEmail() == null || this.getEmail().length() == 0)
		{
			return "未设置";
		}
		else
		{
			return "<a href='Mailto:" + this.getEmail() + "' ><img src='/WF/Img/SMS.gif' border=0/>" + this.getEmail() + "</a>";
		}
	}
	public final String getEmail()
	{
		return this.GetValStringByKey(WFEmpAttr.Email);
	}
	public final void setEmail(String value)
	{
		SetValByKey(WFEmpAttr.Email, value);
	}
	public final String getAuthor()
	{
		return this.GetValStrByKey(WFEmpAttr.Author);
	}
	public final void setAuthor(String value)
	{
		SetValByKey(WFEmpAttr.Author, value);
	}
	public final String getAuthorDate()
	{
		return this.GetValStringByKey(WFEmpAttr.AuthorDate);
	}
	public final void setAuthorDate(String value)
	{
		SetValByKey(WFEmpAttr.AuthorDate, value);
	}
	public final String getAuthorToDate()
	{
		return this.GetValStringByKey(WFEmpAttr.AuthorToDate);
	}
	public final void setAuthorToDate(String value)
	{
		SetValByKey(WFEmpAttr.AuthorToDate, value);
	}
	/** 
	 授权的流程
	*/
	public final String getAuthorFlows()
	{
		String s = this.GetValStringByKey(WFEmpAttr.AuthorFlows);
		s = s.replace(",", "','");
		return "('" + s + "')";
	}
	public final void setAuthorFlows(String value)
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
	public final String getStartFlows()
	{
		return this.GetValStrByKey(WFEmpAttr.StartFlows);
	}
	public final void setStartFlows(String value)
	{
		SetValByKey(WFEmpAttr.StartFlows, value);
	}
	/** 
	 图片签名密码
	*/
	public final String getSPass()
	{
		return this.GetValStringByKey(WFEmpAttr.SPass);
	}
	public final void setSPass(String value)
	{
		SetValByKey(WFEmpAttr.SPass, value);
	}

	/** 
	 授权方式
	*/
	public final AuthorWay getHisAuthorWay()
	{
		return getAuthorWay().forValue(this.getAuthorWay());
	}
	/** 
	 授权方式
	*/
	public final int getAuthorWay()
	{
		return this.GetValIntByKey(WFEmpAttr.AuthorWay);
	}
	public final void setAuthorWay(int value)
	{
		SetValByKey(WFEmpAttr.AuthorWay, value);
	}
	public final boolean getAuthorIsOK()
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

		LocalDateTime dt = DataType.ParseSysDateTime2DateTime(this.getAuthorToDate());
		if (dt.compareTo(LocalDateTime.now()) < 0)
		{
			return false;
		}

		return true;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 操作员
	*/
	public WFEmp()
	{
	}
	/** 
	 操作员
	 
	 @param no
	*/
	public WFEmp(String no)
	{
		this.No = no;
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
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
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

		map.AddTBString(WFEmpAttr.SPass, null, "图片签名密码", true, true, 0, 200, 20);

		map.AddTBInt(WFEmpAttr.Idx, 0, "Idx", false, false);

		map.AddTBAtParas(3500); //增加字段.

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	@Override
	protected boolean beforeUpdate()
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
		//ess.Retrieve(EmpStationAttr.FK_Emp, this.No);
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
	protected boolean beforeInsert()
	{
		this.setUseSta(1);
		return super.beforeInsert();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public static void DTSData()
	{
		String sql = "select No from Port_Emp where No not in (select No from WF_Emp)";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			BP.Port.Emp emp1 = new BP.Port.Emp(dr.get("No").toString());
			BP.WF.Port.WFEmp empWF = new BP.WF.Port.WFEmp();
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
	public final void DoUp()
	{
		this.DoOrderUp("FK_Dept", this.getFK_Dept(), "Idx");
		return;
	}
	public final void DoDown()
	{
		this.DoOrderDown("FK_Dept", this.getFK_Dept(), "Idx");
		return;
	}
}
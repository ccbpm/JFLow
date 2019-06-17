package BP.WF.Port;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.Tools.StringHelper;

/** 
 操作员
*/
public class WFEmp extends EntityNoName
{

		
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
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(WFEmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(WFEmpAttr.FK_Dept, value);
	}
	public final String getStyle()
	{
		return this.GetValStringByKey(WFEmpAttr.Style);
	}
	public final void setStyle(String value)
	{
		this.SetValByKey(WFEmpAttr.Style, value);
	}
	public final String getTM()
	{
		return this.GetValStringByKey(WFEmpAttr.TM);
	}
	public final void setTM(String value)
	{
		this.SetValByKey(WFEmpAttr.TM, value);
	}
	/** 
	 微信号的OpenID.
	*/
	public final String getOpenID()
	{
		return this.GetValStringByKey(WFEmpAttr.TM);
	}
	public final void setOpenID(String value)
	{
		this.SetValByKey(WFEmpAttr.TM, value);
	}
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
			return "<a href=\"javascript:WinOpen('./Msg/SMS.jsp?Tel=" + this.getTel() + "');\"  ><img src='../WF/Img/sms.gif' border=0/>" + this.getTel() + "</a>";
		}
	}
	public final String getEmailHtml()
	{
		if (this.getEmail()==null || this.getEmail().length() == 0)
		{
			return "未设置";
		}
		else
		{
			return "<a href='Mailto:" + this.getEmail() + "' ><img src='/WF/Img/sms.gif' border=0/>" + this.getEmail() + "</a>";
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
		String s= this.GetValStringByKey(WFEmpAttr.AuthorFlows);
		s = s.replace(",", "','");
		return "('"+s+"')";
	}
	public final void setAuthorFlows(String value)
	{
			//授权流程为空时的bug  解决
		if (!StringHelper.isNullOrEmpty(value))
		{
			SetValByKey(WFEmpAttr.AuthorFlows, value.substring(1));
		}
		else
		{
			SetValByKey(WFEmpAttr.AuthorFlows, "");
		}
	}
	public final String getFtpUrl()
	{
		return this.GetValStringByKey(WFEmpAttr.FtpUrl);
	}
	public final void setFtpUrl(String value)
	{
		SetValByKey(WFEmpAttr.FtpUrl, value);
	}
	public final String getStas() throws Exception
	{
		String s= this.GetValStringByKey(WFEmpAttr.Stas);
		if (s.equals(""))
		{
			EmpStations ess = new EmpStations();
			ess.Retrieve(EmpStationAttr.FK_Emp, this.getNo());
			for (EmpStation es : ess.ToJavaList())
			{
				s += es.getFK_StationT() + ",";
			}

			if (ess.size() != 0)
			{
				  //  this.Stas = s;
				this.Update();
					//this.Update(WFEmpAttr.Stas, s);
			}
			return s;
		}
		else
		{
			return s;
		}
	}
	public final void setStas_(String value)
	{
		SetValByKey(WFEmpAttr.Stas, value);
	}

	/** 
	 授权方式
	*/
	public final AuthorWay getHisAuthorWay()
	{
		return AuthorWay.forValue(this.getAuthorWay());
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
		int b= this.GetValIntByKey(WFEmpAttr.AuthorWay);
		if (b == 0)
		{
			return false;
		}

		if (this.getAuthorToDate().length() < 4)
		{
			return true; //没有填写时间,当做无期限
		}

		java.util.Date dt = DataType.ParseSysDateTime2DateTime(this.getAuthorToDate());
		if (dt.compareTo(new java.util.Date()) < 0)
		{
			return false;
		}

		return true;
	}
	
	/** 发起流程.
	 
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
		this.setNo(no);
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
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Emp", "操作员");

		map.AddTBStringPK(WFEmpAttr.No, null, "No", true, true, 1, 50, 36);
		map.AddTBString(WFEmpAttr.Name, null, "Name", true,false, 0, 50, 20);
		map.AddTBInt(WFEmpAttr.UseSta, 1, "用户状态0禁用,1正常.", true, true);

		map.AddTBString(WFEmpAttr.Tel, null, "Tel", true, true, 0, 50, 20);
		map.AddTBString(WFEmpAttr.FK_Dept, null, "FK_Dept", true, true, 0, 100, 36);
		map.AddTBString(WFEmpAttr.Email, null, "Email", true, true, 0, 50, 20);
		map.AddTBString(WFEmpAttr.TM, null, "即时通讯号", true, true, 0, 50, 20);

		map.AddDDLSysEnum(WFEmpAttr.AlertWay, 3, "收听方式", true, true, WFEmpAttr.AlertWay);
		map.AddTBString(WFEmpAttr.Author, null, "授权人", true, true, 0, 50, 20);
		map.AddTBString(WFEmpAttr.AuthorDate, null, "授权日期", true, true, 0, 50, 20);

	    //0不授权， 1完全授权，2，指定流程范围授权. 
		map.AddTBInt(WFEmpAttr.AuthorWay, 0, "授权方式", true, true);
		map.AddTBDate(WFEmpAttr.AuthorToDate, null, "授权到日期", true, true);
		map.AddTBString(WFEmpAttr.AuthorFlows, null, "可以执行的授权流程", true, true, 0, 1000, 20);

		map.AddTBString(WFEmpAttr.Stas, null, "岗位s", true, true, 0, 3000, 20);
		map.AddTBString(WFEmpAttr.Depts, null, "Deptss", true, true, 0, 100, 36);

		map.AddTBString(WFEmpAttr.FtpUrl, null, "FtpUrl", true, true, 0, 50, 20);
		map.AddTBString(WFEmpAttr.Msg, null, "Msg", true, true, 0, 4000, 20);
		map.AddTBString(WFEmpAttr.Style, null, "Style", true, true, 0, 4000, 20);
		map.AddTBString(WFEmpAttr.StartFlows, null, "可以发起的流程", true, true, 0, 4000, 20);
		
		map.AddTBInt(WFEmpAttr.Idx, 0, "Idx", false, false);
		
        map.AddTBAtParas(3500); //增加字段.
        
		
		this.set_enMap(map);
		return this.get_enMap();
	}
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
	protected boolean beforeInsert() throws Exception
	{
		this.setUseSta(1);
		return super.beforeInsert();
	}
	public static void DTSData() throws Exception
	{
		String sql = "select No from Port_Emp where No not in (select No from WF_Emp)";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			BP.Port.Emp emp1 = new BP.Port.Emp(dr.getValue("No").toString());
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
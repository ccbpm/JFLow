package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 个人设置
*/
public class PerSetting extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 系统
	*/
	public final String getFK_App()
	{
		return this.GetValStringByKey(PerSettingAttr.FK_App);
	}
	public final void setFK_App(String value)
	{
		this.SetValByKey(PerSettingAttr.FK_App, value);
	}
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(PerSettingAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(PerSettingAttr.FK_Emp, value);
	}
	public final String getUserNo()
	{
		return this.GetValStringByKey(PerSettingAttr.UserNo);
	}
	public final void setUserNo(String value)
	{
		this.SetValByKey(PerSettingAttr.UserNo, value);
	}
	public final String getUserPass()
	{
		return this.GetValStringByKey(PerSettingAttr.UserPass);
	}
	public final void setUserPass(String value)
	{
		this.SetValByKey(PerSettingAttr.UserPass, value);
	}
	public final int getIdx()
	{
		return this.GetValIntByKey(PerSettingAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(PerSettingAttr.Idx, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 个人设置
	*/
	public PerSetting()
	{
	}
	/** 
	 个人设置
	 
	 @param mypk
	*/
	public PerSetting(String no)
	{
		this.setMyPK( no;
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_PerSetting");
		map.DepositaryOfEntity = Depositary.None;
		map.DepositaryOfMap = Depositary.Application;
		map.EnDesc = "个人设置";
		map.EnType = EnType.Sys;
		map.AddMyPK();

		map.AddTBString(PerSettingAttr.FK_Emp, null, "人员", true, false, 0, 200, 20);
		map.AddTBString(PerSettingAttr.FK_App, null, "系统", true, false, 0, 200, 20);

		map.AddTBString(PerSettingAttr.UserNo, null, "UserNo", true, false, 0, 200, 20, true);
		map.AddTBString(PerSettingAttr.UserPass, null, "UserPass", true, false, 0, 200, 20, true);
		map.AddTBInt(PerSettingAttr.Idx, 0, "显示顺序", false, true);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setMyPK( this.getFK_Emp() + "_" + this.getFK_App();
		return super.beforeUpdateInsertAction();
	}
}
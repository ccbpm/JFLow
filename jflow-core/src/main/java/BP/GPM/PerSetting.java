package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 个人设置
 
*/
public class PerSetting extends EntityMyPK
{

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
	 * @throws Exception 
	*/
	public PerSetting(String no) throws Exception
	{
		this.setMyPK(no);
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
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("个人设置");
		map.setEnType(EnType.Sys);
		map.AddMyPK();

		map.AddTBString(PerSettingAttr.FK_Emp, null, "人员", true, false, 0, 200, 20);
		map.AddTBString(PerSettingAttr.FK_App, null, "系统", true, false, 0, 200, 20);

		map.AddTBString(PerSettingAttr.UserNo, null, "UserNo", true, false, 0, 200, 20, true);
		map.AddTBString(PerSettingAttr.UserPass, null, "UserPass", true, false, 0, 200, 20, true);
		map.AddTBInt(PerSettingAttr.Idx, 0, "显示顺序", false, true);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFK_Emp() + "_" + this.getFK_App());
		return super.beforeUpdateInsertAction();
	}
}
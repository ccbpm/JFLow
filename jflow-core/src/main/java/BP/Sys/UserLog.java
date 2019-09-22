package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import java.util.*;

/** 
 用户日志
*/
public class UserLog extends EntityMyPK
{
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 用户日志信息键值列表
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	public final String getIP()
	{
		return this.GetValStringByKey(UserLogAttr.IP);
	}
	public final void setIP(String value)
	{
		this.SetValByKey(UserLogAttr.IP, value);
	}
	/** 
	 日志标记键
	*/
	public final String getLogFlag()
	{
		return this.GetValStringByKey(UserLogAttr.LogFlag);
	}
	public final void setLogFlag(String value)
	{
		this.SetValByKey(UserLogAttr.LogFlag, value);
	}
	/** 
	 FK_Emp
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(UserLogAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(UserLogAttr.FK_Emp, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(UserLogAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(UserLogAttr.RDT, value);
	}

	public final String getDocs()
	{
		return this.GetValStringByKey(UserLogAttr.Docs);
	}
	public final void setDocs(String value)
	{
		this.SetValByKey(UserLogAttr.Docs, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 用户日志
	*/
	public UserLog()
	{
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
		Map map = new Map("Sys_UserLogT", "用户日志");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();

		map.AddTBString(UserLogAttr.FK_Emp, null, "用户", true, false, 0, 30, 20);
		map.AddTBString(UserLogAttr.IP, null, "IP", true, false, 0, 200, 20);
		map.AddTBString(UserLogAttr.LogFlag, null, "标识", true, false, 0, 300, 20);
		map.AddTBString(UserLogAttr.Docs, null, "说明", true, false, 0, 300, 20);
		map.AddTBString(UserLogAttr.RDT, null, "记录日期", true, false, 0, 20, 20);

		map.GetAttrByKey(this.getPK()).setUIVisible(false);

		map.DTSearchKey = UserLogAttr.RDT;
		map.DTSearchWay = DTSearchWay.ByDate;

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写
	@Override
	public Entities getGetNewEntities()
	{
		return new UserLogs();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 重写
}
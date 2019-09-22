package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import java.util.*;
import java.time.*;

/** 
 用户注册表
*/
public class UserRegedit extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 用户注册表信息键值列表
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 是否使用自动的MyPK,即FK_Emp + CfgKey
	*/
	private boolean AutoMyPK;
	public final boolean getAutoMyPK()
	{
		return AutoMyPK;
	}
	public final void setAutoMyPK(boolean value)
	{
		AutoMyPK = value;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 是否显示图片
	*/
	public final boolean getIsPic()
	{
		return this.GetValBooleanByKey(UserRegeditAttr.IsPic);
	}
	public final void setIsPic(boolean value)
	{
		this.SetValByKey(UserRegeditAttr.IsPic, value);
	}
	/** 
	 数值键
	*/
	public final String getNumKey()
	{
		return this.GetValStringByKey(UserRegeditAttr.NumKey);
	}
	public final void setNumKey(String value)
	{
		this.SetValByKey(UserRegeditAttr.NumKey, value);
	}
	/** 
	 参数
	*/
	public final String getParas()
	{
		return this.GetValStringByKey(UserRegeditAttr.Paras);
	}
	public final void setParas(String value)
	{
		this.SetValByKey(UserRegeditAttr.Paras, value);
	}
	/** 
	 产生的sql
	*/
	public final String getGenerSQL()
	{
		String GenerSQL = this.GetValStringByKey(UserRegeditAttr.GenerSQL);
		GenerSQL = GenerSQL.replace("~", "'");
		return GenerSQL;
	}
	public final void setGenerSQL(String value)
	{
		this.SetValByKey(UserRegeditAttr.GenerSQL, value);
	}
	/** 
	 排序方式
	*/
	public final String getOrderWay()
	{
		return this.GetValStringByKey(UserRegeditAttr.OrderWay);
	}
	public final void setOrderWay(String value)
	{
		this.SetValByKey(UserRegeditAttr.OrderWay, value);
	}
	public final String getOrderBy()
	{
		return this.GetValStringByKey(UserRegeditAttr.OrderBy);
	}
	public final void setOrderBy(String value)
	{
		this.SetValByKey(UserRegeditAttr.OrderBy, value);
	}
	/** 
	 FK_Emp
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(UserRegeditAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(UserRegeditAttr.FK_Emp, value);
	}
	/** 
	 查询时间从
	*/
	public final String getDTFrom_Data()
	{
		String s = this.GetValStringByKey(UserRegeditAttr.DTFrom);
		if (DataType.IsNullOrEmpty(s) || 1 == 1)
		{
			LocalDateTime dt = LocalDateTime.now().plusDays(-14);
			return dt.toString(DataType.getSysDataFormat());
		}
		return s.substring(0, 10);
	}
	public final void setDTFrom_Data(String value)
	{
		this.SetValByKey(UserRegeditAttr.DTFrom, value);
	}
	/** 
	 到
	*/
	public final String getDTTo_Data()
	{
		String s = this.GetValStringByKey(UserRegeditAttr.DTTo);
		if (DataType.IsNullOrEmpty(s) || 1 == 1)
		{
			LocalDateTime dt = LocalDateTime.now();
			return dt.toString(DataType.getSysDataFormat());
		}
		return s.substring(0, 10);
	}
	public final void setDTTo_Data(String value)
	{
		this.SetValByKey(UserRegeditAttr.DTTo, value);
	}

	/** 
	 查询时间从
	*/
	public final String getDTFrom()
	{
		return this.GetValStringByKey(UserRegeditAttr.DTFrom);
			//string s = this.GetValStringByKey(UserRegeditAttr.DTFrom);
			//if (DataType.IsNullOrEmpty(s) || 1==1)
			//{
			//    DateTime dt = DateTime.Now.AddDays(-14);
			//    return dt.ToString(DataType.SysDataFormat);
			//}
			//return s.Substring(0, 10);
	}
	public final void setDTFrom(String value)
	{
		this.SetValByKey(UserRegeditAttr.DTFrom, value);
	}
	/** 
	 到
	*/
	public final String getDTTo()
	{
		return this.GetValStringByKey(UserRegeditAttr.DTTo);
			//string s = this.GetValStringByKey(UserRegeditAttr.DTTo);
			//if (DataType.IsNullOrEmpty(s) || 1 == 1 )
			//{
			//    DateTime dt = DateTime.Now;
			//    return dt.ToString(DataType.SysDataFormat);
			//}
			//return s.Substring(0, 10);
	}
	public final void setDTTo(String value)
	{
		this.SetValByKey(UserRegeditAttr.DTTo, value);
	}
	public final String getDTFrom_Datatime()
	{
		String s = this.GetValStringByKey(UserRegeditAttr.DTFrom);
		if (DataType.IsNullOrEmpty(s))
		{
			LocalDateTime dt = LocalDateTime.now().plusDays(-14);
			return dt.toString(DataType.getSysDataTimeFormat());
		}
		return s;
	}
	public final void setDTFrom_Datatime(String value)
	{
		this.SetValByKey(UserRegeditAttr.DTFrom, value);
	}
	/** 
	 到
	*/
	public final String getDTTo_Datatime()
	{
		String s = this.GetValStringByKey(UserRegeditAttr.DTTo);
		if (DataType.IsNullOrEmpty(s))
		{
			LocalDateTime dt = LocalDateTime.now();
			return dt.toString(DataType.getSysDataTimeFormat());
		}
		return s;
	}
	public final void setDTTo_Datatime(String value)
	{
		this.SetValByKey(UserRegeditAttr.DTTo, value);
	}
	/** 
	 CfgKey
	*/
	public final String getCfgKey()
	{
		return this.GetValStringByKey(UserRegeditAttr.CfgKey);
	}
	public final void setCfgKey(String value)
	{
		this.SetValByKey(UserRegeditAttr.CfgKey, value);
	}
	public final String getSearchKey()
	{
		return this.GetValStringByKey(UserRegeditAttr.SearchKey);
	}
	public final void setSearchKey(String value)
	{
		this.SetValByKey(UserRegeditAttr.SearchKey, value);
	}
	/** 
	 Vals
	*/
	public final String getVals()
	{
		return this.GetValStringByKey(UserRegeditAttr.Vals);
	}
	public final void setVals(String value)
	{
		this.SetValByKey(UserRegeditAttr.Vals, value);
	}
	public final String getMVals()
	{
		return this.GetValStringByKey(UserRegeditAttr.MVals);
	}
	public final void setMVals(String value)
	{
		this.SetValByKey(UserRegeditAttr.MVals, value);
	}
	  
	/** 
	 用户注册表
	*/
	public UserRegedit()
	{
		setAutoMyPK(true);
	}
	/** 
	 用户注册表
	 
	 @param fk_emp 人员
	 @param cfgkey 配置
	*/
	public UserRegedit(String fk_emp, String cfgkey)
	{
		this();
		this.setMyPK(fk_emp + cfgkey);
		this.setCfgKey(cfgkey);
		this.setFK_Emp(fk_emp);
		int i = this.RetrieveFromDBSources();
		if (i == 0)
		{
			this.setCfgKey(cfgkey);
			this.setFK_Emp(fk_emp);
			this.DirectInsert();
			// this.DirectInsert();
		}
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
		Map map = new Map("Sys_UserRegedit", "用户注册表");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(UserRegeditAttr.FK_Emp, null, "用户", false, false, 0, 30, 20);
		map.AddTBString(UserRegeditAttr.CfgKey, null, "键", true, false, 0, 200, 20);
		map.AddTBString(UserRegeditAttr.Vals, null, "值", true, false, 0, 2000, 20);
		map.AddTBString(UserRegeditAttr.GenerSQL, null, "GenerSQL", true, false, 0, 2000, 20);
		map.AddTBString(UserRegeditAttr.Paras, null, "Paras", true, false, 0, 2000, 20);
		map.AddTBString(UserRegeditAttr.NumKey, null, "分析的Key", true, false, 0, 300, 20);
		map.AddTBString(UserRegeditAttr.OrderBy, null, "OrderBy", true, false, 0, 300, 20);
		map.AddTBString(UserRegeditAttr.OrderWay, null, "OrderWay", true, false, 0, 300, 20);
		map.AddTBString(UserRegeditAttr.SearchKey, null, "SearchKey", true, false, 0, 300, 20);
		map.AddTBString(UserRegeditAttr.MVals, null, "MVals", true, false, 0, 2000, 20);
		map.AddBoolean(UserRegeditAttr.IsPic, false, "是否图片", true, false);

		map.AddTBString(UserRegeditAttr.DTFrom, null, "查询时间从", true, false, 0, 20, 20);
		map.AddTBString(UserRegeditAttr.DTTo, null, "到", true, false, 0, 20, 20);

			//增加属性.
		map.AddTBAtParas(4000);

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
		return new UserRegedits();
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		return super.beforeUpdateInsertAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 重写

	/** 
	 获取键/值对集合
	 
	 @return 
	*/
	public final HashMap<String, String> GetVals()
	{
		if (DataType.IsNullOrEmpty(this.getVals()))
		{
			return new HashMap<String, String>();
		}

		String[] arr = null;
		String[] strs = this.getVals().split("@".toCharArray(), StringSplitOptions.RemoveEmptyEntries);
		int idx = -1;
		HashMap<String, String> kvs = new HashMap<String, String>();

		for (String str : strs)
		{
			idx = str.indexOf('=');

			if (idx == -1)
			{
				continue;
			}

			kvs.put(str.substring(0, idx), idx == str.length() - 1 ? "" : str.substring(idx + 1));
		}

		return kvs;
	}

	/** 
	 获取当前用户是否具有导入数据的权限
	 <p>added by liuxc,2017-04-30</p>
	 注意：此权限数据保存于Sys_Regedit.Paras字段中，为@ImpEmpNos=liyan,liping,ligen格式
	 
	 @param ensName 集合类全名，如BP.Port.Emps
	 @return 
	*/
	public static boolean HaveRoleForImp(String ensName)
	{
		//获取可导入权限
		UserRegedit ur = new UserRegedit("admin", ensName + "_SearchAttrs");
		String impEmps = (new AtPara(ur.getParas())).GetValStrByKey("ImpEmpNos");

		if (DataType.IsNullOrEmpty(impEmps))
		{
			return true;
		}
		else
		{
			return WebUser.getNo().equals("admin") || ("," + impEmps + ",").indexOf("," + WebUser.getNo() + ",") != -1;
		}
	}

	/** 
	 获取当前用户是否具有导出数据的权限
	 <p>added by liuxc,2017-04-30</p>
	 注意：此权限数据保存于Sys_Regedit.Paras字段中，为@ExpEmpNos=liyan,liping,ligen格式
	 
	 @param ensName 集合类全名，如BP.Port.Emps
	 @return 
	*/
	public static boolean HaveRoleForExp(String ensName)
	{
		//获取可导入权限
		UserRegedit ur = new UserRegedit("admin", ensName + "_SearchAttrs");
		String impEmps = (new AtPara(ur.getParas())).GetValStrByKey("ExpEmpNos");
		if (DataType.IsNullOrEmpty(impEmps))
		{
			return true;
		}
		else
		{
			return WebUser.getNo().equals("admin") || ("," + impEmps + ",").indexOf("," + WebUser.getNo() + ",") != -1;
		}
	}
}
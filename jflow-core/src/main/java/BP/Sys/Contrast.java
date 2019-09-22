package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 对比状态存储
*/
public class Contrast extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性

	/** 
	 属性
	*/
	public final String getContrastKey()
	{
		String s = this.GetValStringByKey(ContrastAttr.ContrastKey);
		if (s == null || s.equals(""))
		{
			s = "FK_NY";
		}

		return s;
	}
	public final void setContrastKey(String value)
	{
		this.SetValByKey(ContrastAttr.ContrastKey, value);
	}

	public final String getKeyVal1()
	{
		return this.GetValStringByKey(ContrastAttr.KeyVal1);
	}
	public final void setKeyVal1(String value)
	{
		this.SetValByKey(ContrastAttr.KeyVal1, value);
	}
	public final String getKeyVal2()
	{
		return this.GetValStringByKey(ContrastAttr.KeyVal2);
	}
	public final void setKeyVal2(String value)
	{
		this.SetValByKey(ContrastAttr.KeyVal2, value);
	}

	public final String getSortBy()
	{
		return this.GetValStringByKey(ContrastAttr.SortBy);
	}
	public final void setSortBy(String value)
	{
		this.SetValByKey(ContrastAttr.SortBy, value);
	}

	public final String getKeyOfNum()
	{
		return this.GetValStringByKey(ContrastAttr.KeyOfNum);
	}
	public final void setKeyOfNum(String value)
	{
		this.SetValByKey(ContrastAttr.KeyOfNum, value);
	}

	public final int getGroupWay()
	{
		return this.GetValIntByKey(ContrastAttr.GroupWay);
	}
	public final void setGroupWay(int value)
	{
		this.SetValByKey(ContrastAttr.GroupWay, value);
	}
	public final String getOrderWay()
	{
		return this.GetValStringByKey(ContrastAttr.OrderWay);
	}
	public final void setOrderWay(String value)
	{
		this.SetValByKey(ContrastAttr.OrderWay, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 对比状态存储
	*/
	public Contrast()
	{
	}

	/** 
	 map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_UserRegedit", "对比状态存储");
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.AddMyPK();

		map.AddTBString(ContrastAttr.ContrastKey, null, "对比项目", false, true, 0, 20, 10);
		map.AddTBString(ContrastAttr.KeyVal1, null, "KeyVal1", false, true, 0, 20, 10);
		map.AddTBString(ContrastAttr.KeyVal2, null, "KeyVal2", false, true, 0, 20, 10);

		map.AddTBString(ContrastAttr.SortBy, null, "SortBy", false, true, 0, 20, 10);
		map.AddTBString(ContrastAttr.KeyOfNum, null, "KeyOfNum", false, true, 0, 20, 10);

		map.AddTBInt(ContrastAttr.GroupWay, 1, "求什么?SumAvg", false, true);
		map.AddTBString(ContrastAttr.OrderWay, "", "OrderWay", false, true, 0, 10, 10);

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	public Entities getGetNewEntities()
	{
		return new Contrasts();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
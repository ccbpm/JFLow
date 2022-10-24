package bp.sys;

import bp.en.*;
import bp.en.Map;
import java.math.*;

/** 
 全局变量
*/
public class GloVar extends EntityNoName
{

		///#region 属性
	public final Object getValOfObject()  throws Exception
	{
		return this.GetValByKey(GloVarAttr.Val);
	}
	public final void setValOfObject(Object value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final String getVal()  throws Exception
	{
		return this.GetValStringByKey(GloVarAttr.Val);
	}
	public final void setVal(String value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final float getValOfFloat()throws Exception
	{
		try
		{
			return this.GetValFloatByKey(GloVarAttr.Val);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	public final void setValOfFloat(float value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final int getValOfInt()throws Exception
	{
		try
		{
			return this.GetValIntByKey(GloVarAttr.Val);
		}
		catch (RuntimeException ex)
		{
			return 0;
		}
	}
	public final void setValOfInt(int value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final BigDecimal getValOfDecimal()throws Exception
	{
		try
		{
			return this.GetValDecimalByKey(GloVarAttr.Val,0);
		}
		catch (java.lang.Exception e)
		{
			return new BigDecimal(0);
		}
	}
	public final void setValOfDecimal(BigDecimal value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final boolean getValOfBoolen()  throws Exception
	{
		return this.GetValBooleanByKey(GloVarAttr.Val);
	}
	public final void setValOfBoolen(boolean value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	/** 
	 note
	*/
	public final String getNote()  throws Exception
	{
		return this.GetValStringByKey(GloVarAttr.Note);
	}
	public final void setNote(String value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Note, value);
	}
	public final int getIdx()  throws Exception
	{
		return this.GetValIntByKey(GloVarAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Idx, value);
	}
	/** 
	 分组值
	*/
	public final String getGroupKey()  throws Exception
	{
		return this.GetValStringByKey(GloVarAttr.GroupKey);
	}
	public final void setGroupKey(String value) throws Exception
	{
		this.SetValByKey(GloVarAttr.GroupKey, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 全局变量
	*/
	public GloVar()
	{
	}
	/** 
	 全局变量
	 
	 param no
	*/
	public GloVar(String no) throws Exception {
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 键值
	 
	 param key key
	 param isNullAsVal
	*/
	public GloVar(String key, Object isNullAsVal) throws Exception {
		try
		{
			this.setNo(key);
			this.Retrieve();
		}
		catch (java.lang.Exception e)
		{
			if (this.RetrieveFromDBSources() == 0)
			{
				this.setVal(isNullAsVal.toString());
				this.Insert();
			}
		}
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_GloVar", "全局变量");

		map.AddTBStringPK(GloVarAttr.No, null, "键", true, false, 1, 50, 20);
		map.AddTBString(GloVarAttr.Name, null, "名称", true, false, 0, 120, 20);
		map.AddTBString(GloVarAttr.Val, null, "值", true, false, 0, 4000, 20, true);
		map.AddTBString(GloVarAttr.GroupKey, null, "分组值", true, false, 0, 120, 20, true);
		map.AddTBStringDoc(GloVarAttr.Note, null, "说明", true, false, true);
		map.AddTBInt(GloVarAttr.Idx, 0, "顺序号", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 公共属性.
	/** 
	 公共假期
	*/
	private static String _Holidays = null;
	/** 
	 一个月份的假期.
	*/
	public static String getHolidays()throws Exception
	{
		if (_Holidays != null)
		{
			return _Holidays;
		}
		GloVar en = new GloVar();
		en.setNo("Holiday");
		int i = en.RetrieveFromDBSources();
		if (i == 0)
		{
			_Holidays = "";
		}
		else
		{
			_Holidays = en.getVal();
		}
		return _Holidays;
	}
	public static void setHolidays(String value) throws Exception
	{
		_Holidays = value;
	}

		///#endregion

}
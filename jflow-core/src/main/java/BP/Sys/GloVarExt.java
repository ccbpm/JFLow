package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;
import java.math.*;

/** 
 全局变量
*/
public class GloVarExt extends EntityNoName
{

		///#region 属性
	public final Object getValOfObject() throws Exception
	{
		return this.GetValByKey(GloVarAttr.Val);
	}
	public final void setValOfObject(Object value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final String getVal() throws Exception
	{
		return this.GetValStringByKey(GloVarAttr.Val);
	}
	public final void setVal(String value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final float getValOfFloat()
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
	public final int getValOfInt() throws Exception
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
	public final BigDecimal getValOfDecimal() throws Exception
	{
		try
		{
			return this.GetValDecimalByKey(GloVarAttr.Val);
		}
		catch (java.lang.Exception e)
		{
			throw new RuntimeException("@" + this.getName() + ", 没有设置默认值." + this.getVal());
		}
	}
	public final void setValOfDecimal(BigDecimal value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	public final boolean getValOfBoolen() throws Exception
	{
		return this.GetValBooleanByKey(GloVarAttr.Val);
	}
	public final void setValOfBoolen(boolean value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Val, value);
	}
	/** 
	 note
	 * @throws Exception 
	*/
	public final String getNote() throws Exception
	{
		return this.GetValStringByKey(GloVarAttr.Note);
	}
	public final void setNote(String value) throws Exception
	{
		this.SetValByKey(GloVarAttr.Note, value);
	}
	/** 
	 分组值
	 * @throws Exception 
	*/
	public final String getGroupKey() throws Exception
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
	public GloVarExt()
	{
	}
	/** 
	 全局变量
	 
	 @param mypk
	 * @throws Exception 
	*/
	public GloVarExt(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 全局变量s
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_GloVar", "全局变量");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(GloVarAttr.No, null, "键", true, false, 1, 50, 20);
		map.AddTBString(GloVarAttr.Name, null, "名称", true, false, 0, 120, 20);
		map.AddTBString(GloVarAttr.Val, null, "值/表达式", true, false, 0, 2000, 20, true);
		map.AddTBString(GloVarAttr.GroupKey, null, "分组值", false, false, 0, 120, 20, false);
		map.AddTBStringDoc(GloVarAttr.Note, null, "备注", true, false, true);
		map.AddTBInt(GloVarAttr.Idx, 0, "顺序号", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}
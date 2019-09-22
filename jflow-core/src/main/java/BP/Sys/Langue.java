package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 语言
*/
public class Langue extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 模块：比如 ccform.
	*/
	public final String getModel()
	{
		return this.GetValStringByKey(LangueAttr.Model);
	}
	public final void setModel(String value)
	{
		this.SetValByKey(LangueAttr.Model, value);
	}
	/** 
	 类别：比如Label,Field
	*/
	public final String getSort()
	{
		return this.GetValStringByKey(LangueAttr.Sort);
	}
	public final void setSort(String value)
	{
		this.SetValByKey(LangueAttr.Sort, value);
	}
	/** 
	 关联的主键: 比如:LabelID, KeyOfEn
	*/
	public final String getSortKey()
	{
		return this.GetValStringByKey(LangueAttr.SortKey);
	}
	public final void setSortKey(String value)
	{
		this.SetValByKey(LangueAttr.SortKey, value);
	}
	/** 
	 语言
	*/
	public final String getLang()
	{
		return this.GetValStringByKey(LangueAttr.Langue);
	}
	public final void setLang(String value)
	{
		this.SetValByKey(LangueAttr.Langue, value);
	}
	/** 
	 值
	*/
	public final String getVal()
	{
		return this.GetValStringByKey(LangueAttr.Val);
	}
	public final void setVal(String value)
	{
		this.SetValByKey(LangueAttr.Val, value);
	}
	public final String getModelKey()
	{
		return this.GetValStringByKey(LangueAttr.ModelKey);
	}
	public final void setModelKey(String value)
	{
		this.SetValByKey(LangueAttr.ModelKey, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public Langue()
	{
	}
	public Langue(String pk)
	{
		super(pk);
	}
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_Langue", "语言定义");
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.AddMyPK();

		map.AddTBString(LangueAttr.Langue, null, "语言ID", true, true, 0, 20, 20);

		map.AddTBString(LangueAttr.Model, null, "模块", true, true, 0, 20, 20);
		map.AddTBString(LangueAttr.ModelKey, null, "模块实例", true, true, 0, 200, 20);

		map.AddTBString(LangueAttr.Sort, null, "类别", true, true, 0, 20, 20);
		map.AddTBString(LangueAttr.SortKey, null, "类别PK", true, true, 0, 100, 20);

		map.AddTBString(LangueAttr.Val, null, "语言值", true, true, 0, 3999, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setMyPK(this.getLang() + "_" + this.getModel() + "_" + this.getModelKey() + "_" + this.getSort() + "_" + this.getSortKey());
		return super.beforeUpdateInsertAction();
	}
}
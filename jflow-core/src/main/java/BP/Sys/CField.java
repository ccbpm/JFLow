package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 列选择
*/
public class CField extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 列选择
	*/
	public final String getAttrs()
	{
		return this.GetValStringByKey(CFieldAttr.Attrs);
	}
	public final void setAttrs(String value)
	{
		this.SetValByKey(CFieldAttr.Attrs, value);
	}
	/** 
	 操作员ID
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(CFieldAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(CFieldAttr.FK_Emp, value);
	}
	/** 
	 属性
	*/
	public final String getEnsName()
	{
		return this.GetValStringByKey(CFieldAttr.EnsName);
	}
	public final void setEnsName(String value)
	{
		this.SetValByKey(CFieldAttr.EnsName, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 列选择
	*/
	public CField()
	{
	}
	/** 
	 列选择
	 
	 @param FK_Emp 工作人员ID
	 @param className 类名称
	 @param attrKey 属性
	 @param Attrs 值
	*/
	public CField(String FK_Emp, String className)
	{
		int i = this.Retrieve(CFieldAttr.FK_Emp, FK_Emp, CFieldAttr.EnsName, className);
		if (i == 0)
		{
			this.setEnsName(className);
			this.setFK_Emp(FK_Emp);
			this.Insert();
		}
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
		Map map = new Map("Sys_UserRegedit", "列选择");

		map.Java_SetEnType(EnType.Sys);
		map.Java_SetDepositaryOfEntity(Depositary.None);

		map.AddMyPK();
		map.AddTBString(CFieldAttr.EnsName, null, "实体类名称", false, true, 0, 100, 10);
		map.AddTBString(CFieldAttr.FK_Emp, null, "工作人员", false, true, 0, 100, 10);
		map.AddTBStringDoc(CFieldAttr.Attrs, null, "属性s", true, false);
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setMyPK(this.getEnsName() + "_" + this.getFK_Emp());
		return super.beforeUpdateInsertAction();
	}

	public static Attrs GetMyAttrs(Entities ens, Map map)
	{
		String vals = SystemConfig.GetConfigXmlEns("ListAttrs", ens.toString());
		if (vals == null)
		{
			return map.getAttrs();
		}
		
		Attrs attrs = new Attrs();
		for (Attr attr : map.getAttrs())
		{
			if (vals.contains("," + attr.getKey() + ","))
			{
				attrs.Add(attr);
			}
		}
		return attrs;
	}
}
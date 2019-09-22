package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 映射基础
*/
public class MapDataTemplate extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 映射基础
	*/
	public MapDataTemplate()
	{
	}
	/** 
	 映射基础
	 
	 @param no 映射编号
	*/
	public MapDataTemplate(String no)
	{
		super(no);
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

		Map map = new Map("Sys_MapData", "表单模版表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基础信息.
		map.AddTBStringPK(MapDataTemplateAttr.No, null, "编号", true, false, 1, 150, 100);
		map.AddTBString(MapDataTemplateAttr.Name, null, "描述", true, false, 0, 200, 20);
		map.AddTBString(MapDataTemplateAttr.PTable, null, "物理表", true, false, 0, 100, 20);
		map.AddTBInt("IsTemplate", 0, "是否是表单模版", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基础信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 设计者信息.
			//增加参数字段.
		map.AddTBAtParas(4000);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法

}
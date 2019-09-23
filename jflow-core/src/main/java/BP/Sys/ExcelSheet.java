package BP.Sys;

import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 ExcelSheet
*/
public class ExcelSheet extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 获取或设置Excel模板
	*/
	public final String getFK_ExcelFile()
	{
		return this.GetValStrByKey(ExcelSheetAttr.FK_ExcelFile);
	}
	public final void setFK_ExcelFile(String value)
	{
		this.SetValByKey(ExcelSheetAttr.FK_ExcelFile, value);
	}

	/** 
	 获取或设置Sheet索引
	*/
	public final int getSheetIndex()
	{
		return this.GetValIntByKey(ExcelSheetAttr.SheetIndex);
	}
	public final void setSheetIndex(int value)
	{
		this.SetValByKey(ExcelSheetAttr.SheetIndex, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public ExcelSheet()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 权限控制

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region EnMap
	/** 
	 ExcelSheetMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_ExcelSheet");
		map.setEnDesc("ExcelSheet");

		map.AddTBStringPK(ExcelSheetAttr.No, null, "No", true, true, 1, 36, 200);
		map.AddTBString(ExcelSheetAttr.Name, null, "Sheet名称", true, false, 1, 50, 100);
		map.AddDDLEntities(ExcelSheetAttr.FK_ExcelFile, null, "Excel模板", new ExcelFiles(), true);
		map.AddTBInt(ExcelSheetAttr.SheetIndex, 0, "Sheet索引", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion EnMap

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写事件
	/** 
	 记录添加前事件
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setNo(UUID.randomUUID().toString());
		return super.beforeInsert();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 重写事件
}
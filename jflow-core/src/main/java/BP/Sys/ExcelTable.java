package BP.Sys;

import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/** 
Excel数据表

*/
public class ExcelTable extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
	/** 
	 获取或设置Excel模板
	 
	*/
	public final String getFK_ExcelFile()
	{
		return this.GetValStrByKey(ExcelTableAttr.FK_ExcelFile);
	}
	public final void setFK_ExcelFile(String value)
	{
		this.SetValByKey(ExcelTableAttr.FK_ExcelFile, value);
	}

	/** 
	 获取或设置是否明细表
	 
	*/
	public final boolean getIsDtl()
	{
		return this.GetValBooleanByKey(ExcelTableAttr.IsDtl);
	}
	public final void setIsDtl(boolean value)
	{
		this.SetValByKey(ExcelTableAttr.IsDtl, value);
	}

	/** 
	 获取或设置数据表说明
	 
	*/
	public final String getNote()
	{
		return this.GetValStrByKey(ExcelTableAttr.Note);
	}
	public final void setNote(String value)
	{
		this.SetValByKey(ExcelTableAttr.Note, value);
	}

	/** 
	 获取或设置同步到表
	 
	*/
	public final String getSyncToTable()
	{
		return this.GetValStrByKey(ExcelTableAttr.SyncToTable);
	}
	public final void setSyncToTable(String value)
	{
		this.SetValByKey(ExcelTableAttr.SyncToTable, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	public ExcelTable()
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
	 Excel数据表Map
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_ExcelTable");
		map.setEnDesc("Excel数据表");

		map.AddTBStringPK(ExcelTableAttr.No, null, "编号", true, true, 1, 36, 200);
		map.AddTBString(ExcelTableAttr.Name, null, "数据表名", true, false, 1, 50, 100);
		map.AddDDLEntities(ExcelTableAttr.FK_ExcelFile, null, "Excel模板", new ExcelFiles(), true);
		map.AddBoolean(ExcelTableAttr.IsDtl, false, "是否明细表", true, false);
		map.AddTBStringDoc(ExcelTableAttr.Note, null, "数据表说明", true, false, true);
		map.AddTBString(ExcelTableAttr.SyncToTable, null, "同步到表", true, false, 1, 100, 100);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion EnMap

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 重写事件
	/** 
	 记录添加前事件
	 
	*/
	@Override
	protected boolean beforeInsert()
	{
		return super.beforeInsert();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 重写事件
}

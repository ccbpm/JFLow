package BP.Sys;

import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 ExcelSheet
*/
public class ExcelSheet extends EntityNoName
{

		///#region 属性
	/** 
	 获取或设置Excel模板
	 * @throws Exception 
	*/
	public final String getFK_ExcelFile() throws Exception
	{
		return this.GetValStrByKey(ExcelSheetAttr.FK_ExcelFile);
	}
	public final void setFK_ExcelFile(String value) throws Exception
	{
		this.SetValByKey(ExcelSheetAttr.FK_ExcelFile, value);
	}

	/** 
	 获取或设置Sheet索引
	 * @throws Exception 
	*/
	public final int getSheetIndex() throws Exception
	{
		return this.GetValIntByKey(ExcelSheetAttr.SheetIndex);
	}
	public final void setSheetIndex(int value) throws Exception
	{
		this.SetValByKey(ExcelSheetAttr.SheetIndex, value);
	}


		///#endregion 属性


		///#region 构造方法
	public ExcelSheet()
	{
	}

		///#endregion 构造方法


		///#region 权限控制
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}

		///#endregion 权限控制


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

		///#endregion EnMap


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


		///#endregion 重写事件
}
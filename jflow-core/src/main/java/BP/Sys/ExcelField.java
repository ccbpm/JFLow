package BP.Sys;

import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 Excel字段
*/
public class ExcelField extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 获取或设置单元格名称
	 * @throws Exception 
	*/
	public final String getCellName() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.CellName);
	}
	public final void setCellName(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.CellName, value);
	}

	/** 
	 获取或设置行号
	 * @throws Exception 
	*/
	public final int getCellRow() throws Exception
	{
		return this.GetValIntByKey(ExcelFieldAttr.CellRow);
	}
	public final void setCellRow(int value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.CellRow, value);
	}

	/** 
	 获取或设置列号
	 * @throws Exception 
	*/
	public final int getCellColumn() throws Exception
	{
		return this.GetValIntByKey(ExcelFieldAttr.CellColumn);
	}
	public final void setCellColumn(int value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.CellColumn, value);
	}

	/** 
	 获取或设置ExcelSheet
	 * @throws Exception 
	*/
	public final String getFK_ExcelSheet() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.FK_ExcelSheet);
	}
	public final void setFK_ExcelSheet(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.FK_ExcelSheet, value);
	}

	/** 
	 获取或设置存储字段名
	 * @throws Exception 
	*/
	public final String getField() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.Field);
	}
	public final void setField(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.Field, value);
	}

	/** 
	 获取或设置存储数据表
	 * @throws Exception 
	*/
	public final String getFK_ExcelTable() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.FK_ExcelTable);
	}
	public final void setFK_ExcelTable(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.FK_ExcelTable, value);
	}

	/** 
	 获取或设置值类型
	 * @throws Exception 
	*/
	public final ExcelFieldDataType getDataType() throws Exception
	{
		return ExcelFieldDataType.forValue(this.GetValIntByKey(ExcelFieldAttr.DataType, 0));
	}
	public final void setDataType(ExcelFieldDataType value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.DataType, value.getValue());
	}

	/** 
	 获取或设置数据源表/枚举
	 * @throws Exception 
	*/
	public final String getUIBindKey() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.UIBindKey);
	}
	public final void setUIBindKey(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.UIBindKey, value);
	}

	/** 
	 获取或设置数据源表No
	 * @throws Exception 
	*/
	public final String getUIRefKey() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.UIRefKey);
	}
	public final void setUIRefKey(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.UIRefKey, value);
	}

	/** 
	 获取或设置数据源表Name
	 * @throws Exception 
	*/
	public final String getUIRefKeyText() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.UIRefKeyText);
	}
	public final void setUIRefKeyText(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.UIRefKeyText, value);
	}

	/** 
	 获取或设置校验器
	 * @throws Exception 
	*/
	public final String getValidators() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.Validators);
	}
	public final void setValidators(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.Validators, value);
	}

	/** 
	 获取或设置Excel模板
	 * @throws Exception 
	*/
	public final String getFK_ExcelFile() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.FK_ExcelFile);
	}
	public final void setFK_ExcelFile(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.FK_ExcelFile, value);
	}

	/** 
	 获取或设置
	 * @throws Exception 
	*/
	public final String getAtPara() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.AtPara);
	}
	public final void setAtPara(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.AtPara, value);
	}

	/** 
	 获取或设置单元格确认方式
	 * @throws Exception 
	*/
	public final ConfirmKind getConfirmKind() throws Exception
	{
		return ConfirmKind.forValue(this.GetValIntByKey(ExcelFieldAttr.ConfirmKind, 0));
	}
	public final void setConfirmKind(ConfirmKind value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.ConfirmKind, value.getValue());
	}

	/** 
	 获取或设置单元格确认方向移动量
	 * @throws Exception 
	*/
	public final int getConfirmCellCount() throws Exception
	{
		return this.GetValIntByKey(ExcelFieldAttr.ConfirmCellCount, 1);
	}
	public final void setConfirmCellCount(int value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.ConfirmCellCount, value);
	}

	/** 
	 获取或设置对应单元格值
	 * @throws Exception 
	*/
	public final String getConfirmCellValue() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.ConfirmCellValue);
	}
	public final void setConfirmCellValue(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.ConfirmCellValue, value);
	}

	/** 
	 获取或设置对应单元格值重复选定次序
	 * @throws Exception 
	*/
	public final int getConfirmRepeatIndex() throws Exception
	{
		return this.GetValIntByKey(ExcelFieldAttr.ConfirmRepeatIndex);
	}
	public final void setConfirmRepeatIndex(int value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.ConfirmRepeatIndex, value);
	}

	/** 
	 获取或设置不计非空
	*/
	public final boolean getSkipIsNull()
	{
		return this.GetValBooleanByKey(ExcelFieldAttr.SkipIsNull, false);
	}
	public final void setSkipIsNull(boolean value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.SkipIsNull, value);
	}

	/** 
	 获取或设置同步到字段
	 * @throws Exception 
	*/
	public final String getSyncToField() throws Exception
	{
		return this.GetValStrByKey(ExcelFieldAttr.SyncToField);
	}
	public final void setSyncToField(String value) throws Exception
	{
		this.SetValByKey(ExcelFieldAttr.SyncToField, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public ExcelField()
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
	 Excel字段Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_ExcelField");
		map.setEnDesc("Excel字段");

		map.AddTBStringPK(ExcelFieldAttr.No, null, "编号", true, true, 1, 36, 200);
		map.AddTBString(ExcelFieldAttr.Name, null, "名称", true, false, 1, 50, 100);
		map.AddTBString(ExcelFieldAttr.CellName, null, "单元格名称", true, false, 1, 50, 100);
		map.AddTBInt(ExcelFieldAttr.CellRow, 0, "行号", true, false);
		map.AddTBInt(ExcelFieldAttr.CellColumn, 0, "列号", true, false);
		map.AddDDLEntities(ExcelFieldAttr.FK_ExcelSheet, null, "所属ExcelSheet表", new ExcelSheets(), false);
		map.AddTBString(ExcelFieldAttr.Field, null, "存储字段名", true, false, 1, 50, 100);
		map.AddDDLEntities(ExcelFieldAttr.FK_ExcelTable, null, "存储数据表", new ExcelTables(), false);
		map.AddDDLSysEnum(ExcelFieldAttr.DataType, 0, "值类型", true, true, ExcelFieldAttr.DataType, "@0=字符串@1=整数@2=浮点数@3=日期@4=日期时间@5=外键@6=枚举");
		map.AddTBString(ExcelFieldAttr.UIBindKey, null, "外键表/枚举", true, false, 1, 100, 100);
		map.AddTBString(ExcelFieldAttr.UIRefKey, null, "外键表No", true, false, 1, 30, 100);
		map.AddTBString(ExcelFieldAttr.UIRefKeyText, null, "外键表Name", true, false, 1, 30, 100);
		map.AddTBString(ExcelFieldAttr.Validators, null, "校验器", true, false, 1, 4000, 100);
		map.AddDDLEntities(ExcelFieldAttr.FK_ExcelFile, null, "所属Excel模板", new ExcelFiles(), false);
		map.AddTBStringDoc(ExcelFieldAttr.AtPara, null, "参数", true, false, true);
		map.AddDDLSysEnum(ExcelFieldAttr.ConfirmKind, 0, "单元格确认方式", true, true, ExcelFieldAttr.ConfirmKind, "@0=当前单元格@1=左方单元格@2=上方单元格@3=右方单元格@4=下方单元格");
		map.AddTBInt(ExcelFieldAttr.ConfirmCellCount, 1, "单元格确认方向移动量", true, false);
		map.AddTBString(ExcelFieldAttr.ConfirmCellValue, null, "对应单元格值", true, false, 1, 200, 100);
		map.AddTBInt(ExcelFieldAttr.ConfirmRepeatIndex, 0, "对应单元格值重复选定次序", true, false);
		map.AddBoolean(ExcelFieldAttr.SkipIsNull, false, "不计非空", true, true);
		map.AddTBString(ExcelFieldAttr.SyncToField, null, "同步到字段", true, false, 1, 100, 100);

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
		//检测单元格是否已经使用过
		if (this.IsExit(ExcelFieldAttr.CellName, this.getCellName(), ExcelFieldAttr.FK_ExcelSheet, this.getFK_ExcelSheet(), ExcelFieldAttr.FK_ExcelTable, this.getFK_ExcelTable()))
		{
			throw new RuntimeException("单元格 " + this.getCellName() + " 已经使用，不能重复使用！");
		}

		this.setNo(UUID.randomUUID().toString());
		return super.beforeInsert();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 重写事件
}
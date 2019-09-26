package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 Excel字段集合
*/
public class ExcelFields extends EntitiesNoName
{

		///#region 属性
	/** 
	 生成Excel字段实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExcelField();
	}

		///#endregion 属性


		///#region 构造方法
	public ExcelFields()
	{
	}

	public ExcelFields(String fk_excelfile) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(ExcelFieldAttr.FK_ExcelFile, fk_excelfile);
		qo.addOrderBy(ExcelFieldAttr.CellRow, ExcelFieldAttr.CellColumn);
		qo.DoQuery();
	}

		///#endregion 构造方法
}
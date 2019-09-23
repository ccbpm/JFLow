package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 Excel字段集合
*/
public class ExcelFields extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 生成Excel字段实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExcelField();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法
}
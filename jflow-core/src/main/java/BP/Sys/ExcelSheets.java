package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 ExcelSheet集合
*/
public class ExcelSheets extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 生成ExcelSheet实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExcelSheet();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public ExcelSheets()
	{
	}

	public ExcelSheets(String fk_excelfile)
	{
		this.Retrieve(ExcelSheetAttr.FK_ExcelFile, fk_excelfile);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法
}
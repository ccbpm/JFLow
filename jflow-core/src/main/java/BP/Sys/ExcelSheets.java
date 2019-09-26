package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 ExcelSheet集合
*/
public class ExcelSheets extends EntitiesNoName
{

		///#region 属性
	/** 
	 生成ExcelSheet实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExcelSheet();
	}

		///#endregion 属性


		///#region 构造方法
	public ExcelSheets()
	{
	}

	public ExcelSheets(String fk_excelfile) throws Exception
	{
		this.Retrieve(ExcelSheetAttr.FK_ExcelFile, fk_excelfile);
	}

		///#endregion 构造方法
}
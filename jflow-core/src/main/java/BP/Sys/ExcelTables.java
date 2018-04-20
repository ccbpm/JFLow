package BP.Sys;

import java.util.List;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
Excel数据表集合

*/
public class ExcelTables extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
	/** 
	 生成Excel数据表实体
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExcelTable();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	public ExcelTables()
	{
	}

	public ExcelTables(String fk_excelfile) throws Exception
	{
		this.Retrieve(ExcelTableAttr.FK_ExcelFile, fk_excelfile, ExcelTableAttr.Name);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 构造方法
	public List<ExcelTable> Tojavalist(){
		return (List<ExcelTable>)(Object)this;
	}
}

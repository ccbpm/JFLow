package BP.Sys;

import java.util.List;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
Excel数据表集合

*/
public class ExcelTables extends EntitiesNoName
{

	///#region 属性
	/** 
	 生成Excel数据表实体
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExcelTable();
	}

	///#endregion 属性


	///#region 构造方法
	public ExcelTables()
	{
	}

	public ExcelTables(String fk_excelfile) throws Exception
	{
		this.Retrieve(ExcelTableAttr.FK_ExcelFile, fk_excelfile, ExcelTableAttr.Name);
	}

	///#endregion 构造方法
	public List<ExcelTable> Tojavalist(){
		return (List<ExcelTable>)(Object)this;
	}
}

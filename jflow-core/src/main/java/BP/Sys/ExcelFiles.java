package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 Excel模板集合
*/
public class ExcelFiles extends EntitiesNoName
{

		///#region 属性
	/** 
	 生成Excel模板实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExcelFile();
	}

		///#endregion 属性


		///#region 构造方法
	public ExcelFiles()
	{
	}

		///#endregion 构造方法
}
package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 Excel模板集合
*/
public class ExcelFiles extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 生成Excel模板实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExcelFile();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public ExcelFiles()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法
}
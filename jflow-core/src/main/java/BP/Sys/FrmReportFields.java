package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 表单报表设置数据存储表s
*/
public class FrmReportFields extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 表单报表s
	*/
	public FrmReportFields()
	{
	}
	/** 
	 表单报表s
	 
	 @param fk_mapdata s
	*/
	public FrmReportFields(String fk_mapdata)
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve(FrmReportFieldAttr.FK_MapData, fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash(FrmReportFieldAttr.FK_MapData, (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmReportField();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmReportField> ToJavaList()
	{
		return (List<FrmReportField>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmReportField> Tolist()
	{
		ArrayList<FrmReportField> list = new ArrayList<FrmReportField>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmReportField)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
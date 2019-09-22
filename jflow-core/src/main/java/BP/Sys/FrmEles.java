package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 表单元素扩展s
*/
public class FrmEles extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 表单元素扩展s
	*/
	public FrmEles()
	{
	}
	/** 
	 表单元素扩展s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmEles(String fk_mapdata) throws Exception
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash(FrmLineAttr.FK_MapData, (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmEle();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmEle> ToJavaList()
	{
		return (List<FrmEle>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmEle> Tolist()
	{
		ArrayList<FrmEle> list = new ArrayList<FrmEle>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmEle)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
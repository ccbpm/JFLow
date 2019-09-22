package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 标签s
*/
public class FrmLabs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 标签s
	*/
	public FrmLabs()
	{
	}
	/** 
	 标签s
	 
	 @param fk_mapdata s
	*/
	public FrmLabs(String fk_mapdata)
	{
	   this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmLab();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmLab> ToJavaList()
	{
		return (List<FrmLab>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmLab> Tolist()
	{
		ArrayList<FrmLab> list = new ArrayList<FrmLab>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmLab)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
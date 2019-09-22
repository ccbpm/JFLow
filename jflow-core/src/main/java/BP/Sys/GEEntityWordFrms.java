package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 通用实体s
*/
public class GEEntityWordFrms extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载基类方法
	@Override
	public String toString()
	{
		//if (this.FK_MapData == null)
		//    throw new Exception("@没有能 FK_MapData 给值。");
		return this.FK_MapData;
	}
	/** 
	 主键
	*/
	public String FK_MapData = null;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
			//if (this.FK_MapData == null)
			//    throw new Exception("@没有能 FK_MapData 给值。");

		if (this.FK_MapData == null)
		{
			return new GEEntity();
		}
		return new GEEntity(this.FK_MapData);
	}
	/** 
	 通用实体ID
	*/
	public GEEntityWordFrms()
	{
	}
	/** 
	 通用实体ID
	 
	 @param fk_mapdtl
	*/
	public GEEntityWordFrms(String fk_mapdata)
	{
		this.FK_MapData = fk_mapdata;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GEEntityWordFrm> ToJavaList()
	{
		return (List<GEEntityWordFrm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEEntityWordFrm> Tolist()
	{
		ArrayList<GEEntityWordFrm> list = new ArrayList<GEEntityWordFrm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEEntityWordFrm)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
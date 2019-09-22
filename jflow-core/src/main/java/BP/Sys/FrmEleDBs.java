package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 表单元素扩展DBs
*/
public class FrmEleDBs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 表单元素扩展DBs
	*/
	public FrmEleDBs()
	{
	}
	/** 
	 表单元素扩展DBs
	 
	 @param fk_mapdata
	 @param pkval
	*/
	public FrmEleDBs(String fk_mapdata, String pkval)
	{
		this.Retrieve(FrmEleDBAttr.FK_MapData, fk_mapdata, FrmEleDBAttr.EleID, pkval);
	}
	/** 
	 表单元素扩展DBs
	 
	 @param fk_mapdata s
	*/
	public FrmEleDBs(String fk_mapdata)
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
		return new FrmEleDB();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmEleDB> ToJavaList()
	{
		return (List<FrmEleDB>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmEleDB> Tolist()
	{
		ArrayList<FrmEleDB> list = new ArrayList<FrmEleDB>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmEleDB)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
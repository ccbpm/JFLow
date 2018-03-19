package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 超连接s
 
*/
public class FrmLinks extends EntitiesMyPK
{

		
	/** 
	 超连接s
	 
	*/
	public FrmLinks()
	{
	}
	/** 
	 超连接s
	 
	 @param fk_mapdata s
	*/
	public FrmLinks(String fk_mapdata)
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
		return new FrmLink();
	}
	public static ArrayList<FrmLink> convertFrmLinks(Object obj)
	{
		return (ArrayList<FrmLink>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmLink> ToJavaList()
	{
		return (java.util.List<FrmLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmLink> Tolist()
	{
		java.util.ArrayList<FrmLink> list = new java.util.ArrayList<FrmLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmLink)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
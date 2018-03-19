package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 M2M数据存储
 
*/
public class M2Ms extends EntitiesMyPK
{
	/** 
	 M2M数据存储s
	 
	*/
	public M2Ms()
	{
	}
	/** 
	 M2M数据存储s
	 
	 @param FK_MapData
	 @param EnOID
	*/
	public M2Ms(String FK_MapData, long EnOID)
	{
		this.Retrieve(M2MAttr.FK_MapData, FK_MapData, M2MAttr.EnOID, (new Long(EnOID)).toString());
	}
	/** 
	 M2M数据存储 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new M2M();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<M2M> ToJavaList()
	{
		return (java.util.List<M2M>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<M2M> Tolist()
	{
		java.util.ArrayList<M2M> list = new java.util.ArrayList<M2M>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((M2M)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
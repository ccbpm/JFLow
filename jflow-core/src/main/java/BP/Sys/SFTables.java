package BP.Sys;

import BP.En.*;

/** 
 用户自定义表s
 
*/
public class SFTables extends EntitiesNoName
{

		
	/** 
	 用户自定义表s
	 
	*/
	public SFTables()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SFTable();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<SFTable> ToJavaList()
	{
		return (java.util.List<SFTable>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<SFTable> Tolist()
	{
		java.util.ArrayList<SFTable> list = new java.util.ArrayList<SFTable>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SFTable)this.get(i));
		}
		return list;
	}
}
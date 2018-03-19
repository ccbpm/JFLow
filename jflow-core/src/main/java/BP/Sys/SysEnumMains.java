package BP.Sys;

import BP.En.*;

/** 
 纳税人集合 
 
*/
public class SysEnumMains extends EntitiesNoName
{
	/** 
	 SysEnumMains
	 
	*/
	public SysEnumMains()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SysEnumMain();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<SysEnumMain> ToJavaList()
	{
		return (java.util.List<SysEnumMain>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<SysEnumMain> Tolist()
	{
		java.util.ArrayList<SysEnumMain> list = new java.util.ArrayList<SysEnumMain>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysEnumMain)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
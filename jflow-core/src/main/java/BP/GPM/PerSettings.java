package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 个人设置s
 
*/
public class PerSettings extends EntitiesMyPK
{
		///#region 构造
	/** 
	 个人设置s
	 
	*/
	public PerSettings()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new PerSetting();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<PerSetting> ToJavaList()
	{
		return (java.util.List<PerSetting>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<PerSetting> Tolist()
	{
		java.util.ArrayList<PerSetting> list = new java.util.ArrayList<PerSetting>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((PerSetting)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
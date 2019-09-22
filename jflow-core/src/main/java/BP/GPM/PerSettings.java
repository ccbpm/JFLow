package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 个人设置s
*/
public class PerSettings extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	public Entity getNewEntity()
	{
		return new PerSetting();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<PerSetting> ToJavaList()
	{
		return (List<PerSetting>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<PerSetting> Tolist()
	{
		ArrayList<PerSetting> list = new ArrayList<PerSetting>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((PerSetting)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
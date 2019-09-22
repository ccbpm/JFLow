package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SysEnumMain> ToJavaList()
	{
		return (List<SysEnumMain>)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SysEnumMain> Tolist()
	{
		ArrayList<SysEnumMain> list = new ArrayList<SysEnumMain>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((SysEnumMain)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
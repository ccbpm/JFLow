package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
实体版本号s
*/
public class EnVers extends EntitiesMyPK
{
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new EnVer();
	}
	/** 
	 实体版本号集合
	*/
	public EnVers()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EnVer> ToJavaList()
	{
		return (List<EnVer>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EnVer> Tolist()
	{
		ArrayList<EnVer> list = new ArrayList<EnVer>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EnVer)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
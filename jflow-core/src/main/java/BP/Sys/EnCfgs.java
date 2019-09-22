package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import java.util.*;

/** 
 实体集合
*/
public class EnCfgs extends EntitiesNo
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 配置信息
	*/
	public EnCfgs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new EnCfg();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EnCfg> ToJavaList()
	{
		return (List<EnCfg>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EnCfg> Tolist()
	{
		ArrayList<EnCfg> list = new ArrayList<EnCfg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EnCfg)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
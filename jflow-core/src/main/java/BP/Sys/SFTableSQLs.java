package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 用户自定义表s
*/
public class SFTableSQLs extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 用户自定义表s
	*/
	public SFTableSQLs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SFTableSQL();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SFTableSQL> ToJavaList()
	{
		return (List<SFTableSQL>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SFTableSQL> Tolist()
	{
		ArrayList<SFTableSQL> list = new ArrayList<SFTableSQL>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SFTableSQL)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
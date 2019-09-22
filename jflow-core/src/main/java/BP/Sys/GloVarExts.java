package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.math.*;

/** 
 全局变量s
*/
public class GloVarExts extends EntitiesNoName
{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 全局变量s
	*/
	public GloVarExts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GloVarExt();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GloVarExt> ToJavaList()
	{
		return (List<GloVarExt>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GloVarExt> Tolist()
	{
		ArrayList<GloVarExt> list = new ArrayList<GloVarExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GloVarExt)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
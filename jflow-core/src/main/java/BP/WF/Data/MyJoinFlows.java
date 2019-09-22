package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 我参与的流程s
*/
public class MyJoinFlows extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyJoinFlow();
	}
	/** 
	 我参与的流程集合
	*/
	public MyJoinFlows()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyJoinFlow> ToJavaList()
	{
		return (List<MyJoinFlow>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyJoinFlow> Tolist()
	{
		ArrayList<MyJoinFlow> list = new ArrayList<MyJoinFlow>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((MyJoinFlow)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.WF.Data;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 工作质量评价s BP.Port.FK.Evals
*/
public class Evals extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Eval();
	}
	/** 
	 工作质量评价s
	*/
	public Evals()
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
	public final List<Eval> ToJavaList()
	{
		return (List<Eval>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Eval> Tolist()
	{
		ArrayList<Eval> list = new ArrayList<Eval>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((Eval)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
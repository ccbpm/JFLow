package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.*;
import java.util.*;

/** 
 审核组件s
*/
public class FrmWorkChecks extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 审核组件s
	*/
	public FrmWorkChecks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmWorkCheck();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmWorkCheck> ToJavaList()
	{
		return (List<FrmWorkCheck>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmWorkCheck> Tolist()
	{
		ArrayList<FrmWorkCheck> list = new ArrayList<FrmWorkCheck>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmWorkCheck)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
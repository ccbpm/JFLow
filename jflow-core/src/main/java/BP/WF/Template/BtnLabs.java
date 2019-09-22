package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点按钮权限s
*/
public class BtnLabs extends Entities
{
	/** 
	 节点按钮权限s
	*/
	public BtnLabs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BtnLab();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<BtnLab> ToJavaList()
	{
		return (List<BtnLab>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<BtnLab> Tolist()
	{
		ArrayList<BtnLab> list = new ArrayList<BtnLab>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BtnLab)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
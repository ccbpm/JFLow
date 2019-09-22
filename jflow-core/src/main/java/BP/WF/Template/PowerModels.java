package BP.WF.Template;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 权限模型
*/
public class PowerModels extends EntitiesMyPK
{
	/** 
	 权限模型
	*/
	public PowerModels()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new PowerModel();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<PowerModel> ToJavaList()
	{
		return (List<PowerModel>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<PowerModel> Tolist()
	{
		ArrayList<PowerModel> list = new ArrayList<PowerModel>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((PowerModel)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
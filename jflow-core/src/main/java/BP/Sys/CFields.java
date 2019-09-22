package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 列选择s
*/
public class CFields extends EntitiesMyPK
{
	/** 
	 列选择s
	*/
	public CFields()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CField();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CField> ToJavaList()
	{
		return (List<CField>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CField> Tolist()
	{
		ArrayList<CField> list = new ArrayList<CField>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CField)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
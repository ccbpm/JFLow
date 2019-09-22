package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 表单方法
*/
public class Methods extends EntitiesMyPK
{
	/** 
	 表单方法
	*/
	public Methods()
	{
	}
	/** 
	 表单方法
	 
	 @param nodeid 方法IDID
	*/
	public Methods(int nodeid)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MethodAttr.MethodID, nodeid);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Method();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Method> ToJavaList()
	{
		return (List<Method>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Method> Tolist()
	{
		ArrayList<Method> list = new ArrayList<Method>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((Method)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
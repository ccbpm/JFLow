package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import java.util.*;

/** 
 用户日志s
*/
public class UserLogs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	public UserLogs()
	{
	}
	/** 
	 
	 
	 @param emp
	*/
	public UserLogs(String emp)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(UserLogAttr.FK_Emp, emp);
		qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new UserLog();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<UserLog> Tolist()
	{
		ArrayList<UserLog> list = new ArrayList<UserLog>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((UserLog)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
	/** 
	 转化成list
	 
	 @return List
	*/
	public final List<UserLog> ToJavaList()
	{
		return (List<UserLog>)this;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。

}
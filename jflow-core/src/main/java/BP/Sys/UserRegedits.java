package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import java.util.*;
import java.time.*;

/** 
 用户注册表s
*/
public class UserRegedits extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	public UserRegedits()
	{
	}
	/** 
	 
	 
	 @param emp
	*/
	public UserRegedits(String emp)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(UserRegeditAttr.FK_Emp, emp);
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
		return new UserRegedit();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<UserRegedit> ToJavaList()
	{
		return (List<UserRegedit>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<UserRegedit> Tolist()
	{
		ArrayList<UserRegedit> list = new ArrayList<UserRegedit>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((UserRegedit)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
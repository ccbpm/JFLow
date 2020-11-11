package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import bp.*;
import java.util.*;

/** 
 用户日志s
*/
public class UserLogs extends EntitiesMyPK
{

		///构造
	public UserLogs()
	{
	}
	/** 
	 
	 
	 @param emp
	 * @throws Exception 
	*/
	public UserLogs(String emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(UserLogAttr.FK_Emp, emp);
		qo.DoQuery();
	}

		///


		///重写
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new UserLog();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<UserLog> Tolist()
	{
		ArrayList<UserLog> list = new ArrayList<UserLog>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((UserLog)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.


		///为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.List<UserLog> ToJavaList()
	{
		return (java.util.List<UserLog>)(Object)this;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。

}
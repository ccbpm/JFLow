package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 用户注册表s
*/
public class UserRegedits extends EntitiesMyPK
{

		///#region 构造
	public UserRegedits()
	{
	}
	/** 
	 
	 
	 @param emp
	 * @throws Exception 
	*/
	public UserRegedits(String emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(UserRegeditAttr.FK_Emp, emp);
		qo.DoQuery();
	}

	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new UserRegedit();
	}


	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<UserRegedit> ToJavaList()
	{
		return (List<UserRegedit>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<UserRegedit> Tolist()
	{
		ArrayList<UserRegedit> list = new ArrayList<UserRegedit>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((UserRegedit)this.get(i));
		}
		return list;
	}

}
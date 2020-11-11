package bp.sys;
import java.util.ArrayList;

import bp.en.*;
/** 
 用户注册表s
*/
public class UserRegedits extends EntitiesMyPK
{
	private static final long serialVersionUID = 1L;
	///构造
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

		///


		///重写
	/** 
	 得到它的 Entity
	 * @throws Exception 
	*/
	@Override
	public Entity getGetNewEntity() throws Exception
	{
		return new UserRegedit();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成IList, c#代码调用会出错误。
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<UserRegedit> ToJavaList()
	{
		return (java.util.List<UserRegedit>)(Object)this;
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
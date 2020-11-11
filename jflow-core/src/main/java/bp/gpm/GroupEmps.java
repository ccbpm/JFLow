package bp.gpm;

import bp.en.*;
import java.util.*;

/** 
 权限组人员s
*/
public class GroupEmps extends EntitiesMM
{
	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 权限组s
	*/
	public GroupEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GroupEmp();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GroupEmp> ToJavaList()
	{
		return (List<GroupEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GroupEmp> Tolist()
	{
		ArrayList<GroupEmp> list = new ArrayList<GroupEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupEmp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
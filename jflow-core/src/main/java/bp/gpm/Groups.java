package bp.gpm;

import bp.da.*;
import bp.en.*;
import java.util.*;

/** 
 权限组s
*/
public class Groups extends EntitiesNoName
{

		///构造
	/** 
	 权限组s
	*/
	public Groups()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Group();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Group> ToJavaList()
	{
		return (List<Group>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Group> Tolist()
	{
		ArrayList<Group> list = new ArrayList<Group>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Group)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
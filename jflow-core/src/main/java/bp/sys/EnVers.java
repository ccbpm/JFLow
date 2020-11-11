package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.*;
import java.util.*;

/** 
实体版本号s
*/
public class EnVers extends EntitiesMyPK
{
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new EnVer();
	}
	/** 
	 实体版本号集合
	*/
	public EnVers()
	{
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<EnVer> ToJavaList()
	{
		return (java.util.List<EnVer>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EnVer> Tolist()
	{
		ArrayList<EnVer> list = new ArrayList<EnVer>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EnVer)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
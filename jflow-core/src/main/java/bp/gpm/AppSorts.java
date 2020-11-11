package bp.gpm;

import bp.en.*;
import java.util.*;

/** 
 系统类别s
*/
public class AppSorts extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
		///构造
	/** 
	 系统类别s
	*/
	public AppSorts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new AppSort();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<AppSort> ToJavaList()
	{
		return (List<AppSort>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<AppSort> Tolist()
	{
		ArrayList<AppSort> list = new ArrayList<AppSort>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((AppSort)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
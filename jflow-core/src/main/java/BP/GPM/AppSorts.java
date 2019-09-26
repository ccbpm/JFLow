package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 系统类别s
*/
public class AppSorts extends EntitiesNoName
{

		///#region 构造
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
	public Entity getNewEntity()
	{
		return new AppSort();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
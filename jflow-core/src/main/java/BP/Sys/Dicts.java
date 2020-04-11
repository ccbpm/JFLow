package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import java.util.*;

/** 
 实体集合
*/
public class Dicts extends EntitiesNo
{

		///#region 构造
	/** 
	 配置信息
	*/
	public Dicts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Dict();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Dict> ToJavaList()
	{
		return (List<Dict>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Dict> Tolist()
	{
		ArrayList<Dict> list = new ArrayList<Dict>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Dict)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
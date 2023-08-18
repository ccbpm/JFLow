package bp.ta;

import bp.en.EntitiesNoName;
import bp.en.Entity;

import java.util.*;

/** 
 項目s
*/
public class Templates extends EntitiesNoName
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Template();
	}
	/** 
	 項目
	*/
	public Templates()
	{
	}

	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Template> ToJavaList()
	{
		return (java.util.List<Template>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Template> Tolist()
	{
		ArrayList<Template> list = new ArrayList<Template>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Template)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}

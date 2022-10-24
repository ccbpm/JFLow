package bp.sys;

import bp.en.*;
import java.util.*;

/** 
 语言 
*/
public class Langues extends EntitiesMyPK
{
	/**
	 * 实体集合
	 */
	public Langues() throws Exception {
	}

	/**
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Langue();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Langue> ToJavaList()throws Exception
	{
		return (java.util.List<Langue>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Langue> Tolist()throws Exception
	{
		ArrayList<Langue> list = new ArrayList<Langue>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Langue)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
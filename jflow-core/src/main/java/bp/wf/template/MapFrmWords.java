package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 Word表单属性s
*/
public class MapFrmWords extends EntitiesNoName
{

		///构造
	/** 
	 Word表单属性s
	*/
	public MapFrmWords()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFrmWord();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrmWord> ToJavaList()
	{
		return (List<MapFrmWord>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrmWord> Tolist()
	{
		ArrayList<MapFrmWord> list = new ArrayList<MapFrmWord>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmWord)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
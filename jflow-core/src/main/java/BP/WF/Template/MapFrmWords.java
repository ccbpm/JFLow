package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 Word表单属性s
*/
public class MapFrmWords extends EntitiesNoName
{

		///#region 构造
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
	public Entity getNewEntity()
	{
		return new MapFrmWord();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
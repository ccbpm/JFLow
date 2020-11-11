package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 自由表单属性s
*/
public class MapFrmNodes extends EntitiesNoName
{

		///构造
	/** 
	 自由表单属性s
	*/
	public MapFrmNodes()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFrmNode();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrmNode> ToJavaList()
	{
		return (List<MapFrmNode>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrmNode> Tolist()
	{
		ArrayList<MapFrmNode> list = new ArrayList<MapFrmNode>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmNode)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
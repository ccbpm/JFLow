package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import java.util.*;

/** 
 映射基础s
*/
public class MapDataTemplates extends EntitiesNoName
{

		///#region 构造
	/** 
	 映射基础s
	*/
	public MapDataTemplates() throws Exception {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new MapDataTemplate();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapDataTemplate> ToJavaList() {
		return (java.util.List<MapDataTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDataTemplate> Tolist()  {
		ArrayList<MapDataTemplate> list = new ArrayList<MapDataTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataTemplate)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
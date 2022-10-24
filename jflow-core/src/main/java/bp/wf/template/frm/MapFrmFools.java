package bp.wf.template.frm;

import bp.en.*;
import java.util.*;

/** 
 傻瓜表单属性s
*/
public class MapFrmFools extends EntitiesNoName
{

		///#region 构造
	/** 
	 傻瓜表单属性s
	*/
	public MapFrmFools() throws Exception {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new MapFrmFool();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapFrmFool> ToJavaList() {
		return (java.util.List<MapFrmFool>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrmFool> Tolist()  {
		ArrayList<MapFrmFool> list = new ArrayList<MapFrmFool>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmFool)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
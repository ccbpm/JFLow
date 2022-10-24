package bp.sys.frmui;

import bp.en.*;
import java.util.*;

/** 
 实体属性s
*/
public class MapAttrDataViews extends EntitiesMyPK
{

		///#region 构造
	/** 
	 实体属性s
	*/
	public MapAttrDataViews() {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new MapAttrDataView();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapAttrDataView> ToJavaList() {
		return (java.util.List<MapAttrDataView>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrDataView> Tolist()  {
		ArrayList<MapAttrDataView> list = new ArrayList<MapAttrDataView>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrDataView)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
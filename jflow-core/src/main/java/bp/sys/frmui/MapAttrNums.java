package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;
import java.util.*;
import java.math.*;

/** 
 实体属性s
*/
public class MapAttrNums extends EntitiesMyPK
{

		///#region 构造
	/** 
	 实体属性s
	*/
	public MapAttrNums() throws Exception {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new MapAttrNum();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapAttrNum> ToJavaList() {
		return (java.util.List<MapAttrNum>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrNum> Tolist()  {
		ArrayList<MapAttrNum> list = new ArrayList<MapAttrNum>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrNum)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
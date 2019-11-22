package BP.Sys.FrmUI;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

import java.util.ArrayList;
import java.util.List;

/** 
 地图s
*/
public class ExtMaps extends EntitiesMyPK
{

		///#region 构造
	/**
	 地图s
	*/
	public ExtMaps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExtMap();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 @return List
	*/
	public final List<ExtMap> ToJavaList()
	{
		return (List<ExtMap>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtMap> Tolist()
	{
		ArrayList<ExtMap> list = new ArrayList<ExtMap>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtMap)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
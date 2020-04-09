package BP.Sys.FrmUI;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

import java.util.ArrayList;
import java.util.List;

/** 
 实体属性s
*/
public class MapAttrFlowBBSs extends EntitiesMyPK
{

		///#region 构造
	/**
	 实体属性s
	*/
	public MapAttrFlowBBSs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MapAttrFlowBBS();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrFlowBBS> ToJavaList()
	{
		return (List<MapAttrFlowBBS>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrFlowBBS> Tolist()
	{
		ArrayList<MapAttrFlowBBS> list = new ArrayList<MapAttrFlowBBS>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrFlowBBS)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.Sys.FrmUI;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

import java.util.ArrayList;
import java.util.List;

/** 
 实体属性s
*/
public class MapAttrCards extends EntitiesMyPK
{

		///#region 构造
	/**
	 实体属性s
	*/
	public MapAttrCards()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MapAttrCard();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrCard> ToJavaList()
	{
		return (List<MapAttrCard>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrCard> Tolist()
	{
		ArrayList<MapAttrCard> list = new ArrayList<MapAttrCard>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrCard)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 表单元素扩展s
 
*/
public class FrmEles extends EntitiesMyPK
{
	/** 
	 表单元素扩展s
	 
	*/
	public FrmEles()
	{
	}
	/** 
	 表单元素扩展s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmEles(String fk_mapdata) throws Exception
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash(FrmLineAttr.FK_MapData, (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmEle();
	}
	public static ArrayList<FrmEle> convertFrmEles(Object obj)
	{
		return (ArrayList<FrmEle>) obj;
	}

	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmEle> ToJavaList()
	{
		return (java.util.List<FrmEle>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmEle> Tolist()
	{
		java.util.ArrayList<FrmEle> list = new java.util.ArrayList<FrmEle>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmEle)this.get(i));
		}
		return list;
	}
}
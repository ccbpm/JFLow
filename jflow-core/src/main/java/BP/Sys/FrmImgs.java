package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 图片s
 
*/
public class FrmImgs extends EntitiesMyPK
{

		
	/** 
	 图片s
	 
	*/
	public FrmImgs()
	{
	}
	/** 
	 图片s
	 
	 @param fk_mapdata s
	*/
	public FrmImgs(String fk_mapdata)
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
		return new FrmImg();
	}
	public static ArrayList<FrmImg> convertFrmImgs(Object obj)
	{
		return (ArrayList<FrmImg>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmImg> ToJavaList()
	{
		return (java.util.List<FrmImg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmImg> Tolist()
	{
		java.util.ArrayList<FrmImg> list = new java.util.ArrayList<FrmImg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImg)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 图片附件s
 
*/
public class FrmImgAths extends EntitiesMyPK
{

		
	/** 
	 图片附件s
	 
	*/
	public FrmImgAths()
	{
	}
	/** 
	 图片附件s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmImgAths(String fk_mapdata) throws Exception
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
		return new FrmImgAth();
	}
	public static ArrayList<FrmImgAth> convertFrmImgAths(Object obj)
	{
		return (ArrayList<FrmImgAth>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmImgAth> ToJavaList()
	{
		return (java.util.List<FrmImgAth>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmImgAth> Tolist()
	{
		java.util.ArrayList<FrmImgAth> list = new java.util.ArrayList<FrmImgAth>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImgAth)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
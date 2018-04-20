package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 剪切图片附件数据存储s
 
*/
public class FrmImgAthDBs extends EntitiesMyPK
{

		
	/** 
	 剪切图片附件数据存储s
	 
	*/
	public FrmImgAthDBs()
	{
	}
	/** 
	 剪切图片附件数据存储s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmImgAthDBs(String fk_mapdata, String pkval) throws Exception
	{
		this.Retrieve(FrmImgAthDBAttr.FK_MapData, fk_mapdata, FrmImgAthDBAttr.RefPKVal, pkval);
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmImgAthDB();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmImgAthDB> ToJavaList()
	{
		return (java.util.List<FrmImgAthDB>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmImgAthDB> Tolist()
	{
		java.util.ArrayList<FrmImgAthDB> list = new java.util.ArrayList<FrmImgAthDB>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImgAthDB)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
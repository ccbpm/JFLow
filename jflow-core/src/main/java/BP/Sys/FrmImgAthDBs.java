package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 剪切图片附件数据存储s
*/
public class FrmImgAthDBs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 剪切图片附件数据存储s
	*/
	public FrmImgAthDBs()
	{
	}

	public FrmImgAthDBs(String fk_mapdata)
	{
		this.Retrieve(FrmImgAthDBAttr.FK_MapData, fk_mapdata);
	}
	/** 
	 剪切图片附件数据存储s
	 
	 @param fk_mapdata s
	*/
	public FrmImgAthDBs(String fk_mapdata, String pkval)
	{
		this.Retrieve(FrmImgAthDBAttr.FK_MapData, fk_mapdata, FrmImgAthDBAttr.RefPKVal, pkval);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmImgAthDB();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmImgAthDB> ToJavaList()
	{
		return (List<FrmImgAthDB>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmImgAthDB> Tolist()
	{
		ArrayList<FrmImgAthDB> list = new ArrayList<FrmImgAthDB>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImgAthDB)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
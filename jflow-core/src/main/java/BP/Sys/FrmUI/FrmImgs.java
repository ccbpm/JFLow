package BP.Sys.FrmUI;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.Sys.FrmLineAttr;
import BP.Sys.SystemConfig;

/** 
装饰图片s
*/
public class FrmImgs extends EntitiesMyPK
{
	//#region 构造
	/** 
	 装饰图片s
	*/
	public FrmImgs()
	{
	}
	/** 
	 装饰图片s
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmImgs(String fk_mapdata) throws Exception
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
	//#endregion

	//#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmImg> ToJavaList()
	{
		return (List<FrmImg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmImg> Tolist()
	{
		ArrayList<FrmImg> list = new ArrayList<FrmImg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImg)this.get(i));
		}
		return list;
	}
	//#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}

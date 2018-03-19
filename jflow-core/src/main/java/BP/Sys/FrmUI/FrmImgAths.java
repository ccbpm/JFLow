package BP.Sys.FrmUI;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.Sys.FrmLineAttr;
import BP.Sys.SystemConfig;

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
	*/
	public FrmImgAths(String fk_mapdata)
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

	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmImgAth> ToJavaList()
	{
		return (List<FrmImgAth>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmImgAth> Tolist()
	{
		ArrayList<FrmImgAth> list = new ArrayList<FrmImgAth>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImgAth)this.get(i));
		}
		return list;
	}
}


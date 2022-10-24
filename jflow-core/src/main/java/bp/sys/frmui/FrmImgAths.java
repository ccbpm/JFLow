package bp.sys.frmui;

import bp.en.*;
import bp.sys.*;
import java.util.*;

/** 
 图片附件s
*/
public class FrmImgAths extends EntitiesMyPK
{

		///#region 构造
	/** 
	 图片附件s
	*/
	public FrmImgAths() {
	}
	/** 
	 图片附件s
	 
	 param fk_mapdata s
	*/
	public FrmImgAths(String fk_mapdata) throws Exception {
		if (bp.difference.SystemConfig.getIsDebug())
		{
			this.Retrieve(MapAttrAttr.FK_MapData, fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash(MapAttrAttr.FK_MapData, (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new FrmImgAth();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmImgAth> ToJavaList() {
		return (java.util.List<FrmImgAth>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmImgAth> Tolist()  {
		ArrayList<FrmImgAth> list = new ArrayList<FrmImgAth>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImgAth)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
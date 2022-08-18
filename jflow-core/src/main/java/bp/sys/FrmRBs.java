package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import java.util.*;

/** 
 单选框s
*/
public class FrmRBs extends EntitiesMyPK
{

		///#region 构造
	/** 
	 单选框s
	*/
	public FrmRBs() throws Exception {
	}
	/** 
	 单选框s
	 
	 param fk_mapdata s
	*/
	public FrmRBs(String fk_mapdata) throws Exception {
		this.Retrieve(MapAttrAttr.FK_MapData, fk_mapdata);

	}
	/** 
	 单选框s
	 
	 param fk_mapdata 表单ID
	 param keyOfEn 字段
	*/
	public FrmRBs(String fk_mapdata, String keyOfEn) throws Exception {
		this.Retrieve(FrmRBAttr.FK_MapData, fk_mapdata, FrmRBAttr.KeyOfEn, keyOfEn);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new FrmRB();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmRB> ToJavaList() {
		return (java.util.List<FrmRB>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmRB> Tolist()  {
		ArrayList<FrmRB> list = new ArrayList<FrmRB>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmRB)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
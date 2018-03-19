package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 单选框s
 
*/
public class FrmRBs extends EntitiesMyPK
{

		
	/** 
	 单选框s
	 
	*/
	public FrmRBs()
	{
	}
	/** 
	 单选框s
	 
	 @param fk_mapdata s
	*/
	public FrmRBs(String fk_mapdata)
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
	 单选框s
	 
	 @param fk_mapdata 表单ID
	 @param keyOfEn 字段
	*/
	public FrmRBs(String fk_mapdata, String keyOfEn)
	{
		this.Retrieve(FrmRBAttr.FK_MapData, fk_mapdata, FrmRBAttr.KeyOfEn, keyOfEn);
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmRB();
	}
	public static ArrayList<FrmRB> convertFrmRBs(Object obj)
	{
		return (ArrayList<FrmRB>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmRB> ToJavaList()
	{
		return (java.util.List<FrmRB>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmRB> Tolist()
	{
		java.util.ArrayList<FrmRB> list = new java.util.ArrayList<FrmRB>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmRB)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
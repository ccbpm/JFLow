package BP.Sys;

import java.util.ArrayList;

import BP.DA.*;
import BP.En.*;

/** 
 按钮s
 
*/
public class FrmBtns extends EntitiesMyPK
{
	//#region 构造
	/** 
	 按钮s
	 
	*/
	public FrmBtns()
	{
	}
	/** 
	 按钮s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmBtns(String fk_mapdata) throws Exception
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
		return new FrmBtn();
	}
	public static ArrayList<FrmBtn> convertFrmBtns(Object obj)
	{
		return (ArrayList<FrmBtn>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<FrmBtn> ToJavaList()
	{
		return (java.util.List<FrmBtn>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmBtn> Tolist()
	{
		java.util.ArrayList<FrmBtn> list = new java.util.ArrayList<FrmBtn>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmBtn)this.get(i));
		}
		return list;
	}
	//#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
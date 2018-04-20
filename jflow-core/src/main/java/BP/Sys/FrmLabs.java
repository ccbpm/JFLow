package BP.Sys;

import java.util.ArrayList;

import BP.DA.*;
import BP.En.*;

/** 
 标签s
*/
public class FrmLabs extends EntitiesMyPK
{

		
	/** 
	 标签s
	*/
	public FrmLabs()
	{
	}
	/** 
	 标签s
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmLabs(String fk_mapdata) throws Exception
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
		return new FrmLab();
	}
	public static ArrayList<FrmLab> convertFrmLabs(Object obj)
	{
		return (ArrayList<FrmLab>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<FrmLab> ToJavaList()
	{
		return (java.util.List<FrmLab>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<FrmLab> Tolist()
	{
		java.util.ArrayList<FrmLab> list = new java.util.ArrayList<FrmLab>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmLab)this.get(i));
		}
		return list;
	}
}
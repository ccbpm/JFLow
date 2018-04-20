package BP.Sys;

import java.util.ArrayList;
import BP.En.*;

/** 
 线s
*/
public class FrmLines extends EntitiesMyPK
{
	/** 
	 线s
	*/
	public FrmLines()
	{
	}
	/** 
	 线s
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmLines(String fk_mapdata) throws Exception
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
		return new FrmLine();
	}
	
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public static ArrayList<FrmLine> convertFrmLines(Object obj)
	{
		return (ArrayList<FrmLine>) obj;
	}
	public final java.util.List<FrmLine> ToJavaList()
	{
		return (java.util.List<FrmLine>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<FrmLine> Tolist()
	{
		java.util.ArrayList<FrmLine> list = new java.util.ArrayList<FrmLine>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmLine)this.get(i));
		}
		return list;
	}
}
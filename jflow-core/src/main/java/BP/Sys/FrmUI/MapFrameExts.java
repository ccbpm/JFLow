package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;

/** 
 框架s
 
*/
public class MapFrameExts extends EntitiesMyPK
{

		///#region 构造
	/** 
	 框架s
	 
	*/
	public MapFrameExts()
	{
	}
	
	 /// <summary>
    /// 框架s
    /// </summary>
    /// <param name="frmID">表单ID</param>
    public MapFrameExts(String frmID) throws Exception
    {
        this.Retrieve(MapFrameAttr.FK_MapData, frmID);
    }
    
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFrameExt();
	}
	
	
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrameExt> ToJavaList()
	{
		return (List<MapFrameExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrameExt> Tolist()
	{
		ArrayList<MapFrameExt> list = new ArrayList<MapFrameExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrameExt)this.get(i));
		}
		return list;
	}
	  
}
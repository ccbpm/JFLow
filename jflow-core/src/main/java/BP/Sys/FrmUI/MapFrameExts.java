package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

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
	/** 
	 框架s
	 
	 @param frmID 表单ID
	 * @throws Exception 
	*/
	public MapFrameExts(String frmID) throws Exception
	{
		this.Retrieve(MapFrameAttr.FK_MapData, frmID);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MapFrameExt();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
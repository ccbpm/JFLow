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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	*/
	public MapFrameExts(String frmID)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrameExt> ToJavaList()
	{
		return (List<MapFrameExt>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrameExt> Tolist()
	{
		ArrayList<MapFrameExt> list = new ArrayList<MapFrameExt>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((MapFrameExt)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
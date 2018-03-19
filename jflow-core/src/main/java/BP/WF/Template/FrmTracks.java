package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SystemConfig;

/** 
 轨迹图标组件s
 
*/
public class FrmTracks extends Entities
{

		
	/** 
	 轨迹图标组件s
	 
	*/
	public FrmTracks()
	{
	}
	/** 
	 轨迹图标组件s
	 
	 @param fk_mapdata s
	*/
	public FrmTracks(String fk_mapdata)
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve("No", fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash("No", (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmTrack();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmTrack> ToJavaList()
	{
		return (java.util.List<FrmTrack>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmTrack> Tolist()
	{
		java.util.ArrayList<FrmTrack> list = new java.util.ArrayList<FrmTrack>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmTrack)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
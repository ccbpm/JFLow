package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SystemConfig;

/** 
 流转自定义组件s
 
*/
public class FrmTransferCustoms extends Entities
{

		
	/** 
	 流转自定义组件s
	 
	*/
	public FrmTransferCustoms()
	{
	}
	/** 
	 流转自定义组件s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmTransferCustoms(String fk_mapdata) throws Exception
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
		return new FrmTransferCustom();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmTransferCustom> ToJavaList()
	{
		return (java.util.List<FrmTransferCustom>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmTransferCustom> Tolist()
	{
		java.util.ArrayList<FrmTransferCustom> list = new java.util.ArrayList<FrmTransferCustom>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmTransferCustom)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
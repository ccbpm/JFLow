package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SystemConfig;

/** 
 父子流程s
 
*/
public class FrmSubFlows extends Entities
{

		
	/** 
	 父子流程s
	 
	*/
	public FrmSubFlows()
	{
	}
	/** 
	 父子流程s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmSubFlows(String fk_mapdata) throws Exception
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
		return new FrmSubFlow();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmSubFlow> ToJavaList()
	{
		return (java.util.List<FrmSubFlow>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmSubFlow> Tolist()
	{
		java.util.ArrayList<FrmSubFlow> list = new java.util.ArrayList<FrmSubFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmSubFlow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
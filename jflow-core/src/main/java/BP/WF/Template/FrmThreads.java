package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SystemConfig;

/** 
 子线程组件s
 
*/
public class FrmThreads extends Entities
{

		
	/** 
	 子线程组件s
	 
	*/
	public FrmThreads()
	{
	}
	/** 
	 子线程组件s
	 
	 @param fk_mapdata s
	*/
	public FrmThreads(String fk_mapdata)
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
		return new FrmThread();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmThread> ToJavaList()
	{
		return (java.util.List<FrmThread>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmThread> Tolist()
	{
		java.util.ArrayList<FrmThread> list = new java.util.ArrayList<FrmThread>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmThread)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
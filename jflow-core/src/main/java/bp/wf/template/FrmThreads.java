package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 子线程组件s
*/
public class FrmThreads extends Entities
{

		///构造
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
	public FrmThreads(String fk_mapdata) throws Exception
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmThread> ToJavaList()
	{
		return (List<FrmThread>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmThread> Tolist()
	{
		ArrayList<FrmThread> list = new ArrayList<FrmThread>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmThread)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
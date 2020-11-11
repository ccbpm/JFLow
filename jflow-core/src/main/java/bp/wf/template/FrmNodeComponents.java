package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;

/** 
 节点表单组件s
*/
public class FrmNodeComponents extends Entities
{

		///构造
	/** 
	 节点表单组件s
	*/
	public FrmNodeComponents()
	{
	}
	/** 
	 节点表单组件s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmNodeComponents(String fk_mapdata) throws Exception
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
		return new FrmNodeComponent();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmNodeComponent> ToJavaList()
	{
		return (List<FrmNodeComponent>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNodeComponent> Tolist()
	{
		ArrayList<FrmNodeComponent> list = new ArrayList<FrmNodeComponent>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNodeComponent)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
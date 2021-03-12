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
 流转自定义组件s
*/
public class FrmTransferCustoms extends Entities
{

		///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmTransferCustom> ToJavaList()
	{
		return (List<FrmTransferCustom>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmTransferCustom> Tolist()
	{
		ArrayList<FrmTransferCustom> list = new ArrayList<FrmTransferCustom>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmTransferCustom)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
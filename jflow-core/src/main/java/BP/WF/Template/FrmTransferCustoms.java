package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 流转自定义组件s
*/
public class FrmTransferCustoms extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 流转自定义组件s
	*/
	public FrmTransferCustoms()
	{
	}
	/** 
	 流转自定义组件s
	 
	 @param fk_mapdata s
	*/
	public FrmTransferCustoms(String fk_mapdata)
	{
		if (SystemConfig.IsDebug)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmTransferCustom> ToJavaList()
	{
		return (List<FrmTransferCustom>)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmTransferCustom> Tolist()
	{
		ArrayList<FrmTransferCustom> list = new ArrayList<FrmTransferCustom>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmTransferCustom)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
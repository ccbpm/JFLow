package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.*;
import java.util.*;

/** 
 节点表单组件s
*/
public class FrmNodeComponents extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 节点表单组件s
	*/
	public FrmNodeComponents()
	{
	}
	/** 
	 节点表单组件s
	 
	 @param fk_mapdata s
	*/
	public FrmNodeComponents(String fk_mapdata)
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
		return new FrmNodeComponent();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmNodeComponent> ToJavaList()
	{
		return (List<FrmNodeComponent>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNodeComponent> Tolist()
	{
		ArrayList<FrmNodeComponent> list = new ArrayList<FrmNodeComponent>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmNodeComponent)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.GPM.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Sys.*;
import java.util.*;

/** 
 单据模版s
*/
public class FrmTemplates extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 单据模版s
	*/
	public FrmTemplates()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmTemplate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmTemplate> ToJavaList()
	{
		return (List<FrmTemplate>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmTemplate> Tolist()
	{
		ArrayList<FrmTemplate> list = new ArrayList<FrmTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmTemplate)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
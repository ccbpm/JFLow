package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Sys.*;
import java.util.*;
import java.time.*;

/** 
 实体表单s
*/
public class FrmDicts extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 实体表单s
	*/
	public FrmDicts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmDict();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmDict> ToJavaList()
	{
		return (List<FrmDict>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmDict> Tolist()
	{
		ArrayList<FrmDict> list = new ArrayList<FrmDict>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmDict)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
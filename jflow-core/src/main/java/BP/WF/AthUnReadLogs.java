package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.Data.*;
import java.util.*;

/** 
 附件未读日志s 
*/
public class AthUnReadLogs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 附件未读日志s
	*/
	public AthUnReadLogs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new AthUnReadLog();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<AthUnReadLog> ToJavaList()
	{
		return (List<AthUnReadLog>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<AthUnReadLog> Tolist()
	{
		ArrayList<AthUnReadLog> list = new ArrayList<AthUnReadLog>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((AthUnReadLog)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
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
	public Entity getNewEntity()
	{
		return new AthUnReadLog();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<AthUnReadLog> ToJavaList()
	{
		return (List<AthUnReadLog>)(Object)this;
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
			list.add((AthUnReadLog)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
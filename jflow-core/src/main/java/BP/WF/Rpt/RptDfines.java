package BP.WF.Rpt;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;

/** 
 报表定义s
*/
public class RptDfines extends EntitiesNoName
{

		///#region 构造
	/** 
	 报表定义s
	*/
	public RptDfines()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new RptDfine();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<RptDfine> ToJavaList()
	{
		return (List<RptDfine>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<RptDfine> Tolist()
	{
		ArrayList<RptDfine> list = new ArrayList<RptDfine>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptDfine)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
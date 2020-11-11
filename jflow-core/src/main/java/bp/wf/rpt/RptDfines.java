package bp.wf.rpt;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.data.*;
import bp.wf.*;
import java.util.*;

/** 
 报表定义s
*/
public class RptDfines extends EntitiesNoName
{

		///构造
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
	public Entity getGetNewEntity()
	{
		return new RptDfine();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
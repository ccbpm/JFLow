package bp.wf.rpt;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 报表人员 
*/
public class RptEmps extends Entities
{

		///构造
	/** 
	 报表与人员集合
	*/
	public RptEmps()
	{
	}

		///


		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RptEmp();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<RptEmp> ToJavaList()
	{
		return (List<RptEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<RptEmp> Tolist()
	{
		ArrayList<RptEmp> list = new ArrayList<RptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptEmp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
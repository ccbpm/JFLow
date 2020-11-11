package bp.wf.rpt;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 报表部门 
*/
public class RptDepts extends Entities
{

		///构造
	/** 
	 报表与部门集合
	*/
	public RptDepts()
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
		return new RptDept();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<RptDept> ToJavaList()
	{
		return (List<RptDept>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<RptDept> Tolist()
	{
		ArrayList<RptDept> list = new ArrayList<RptDept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptDept)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
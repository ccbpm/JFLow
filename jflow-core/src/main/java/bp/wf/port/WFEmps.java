package bp.wf.port;

import bp.en.*;
import java.util.*;

/** 
 操作员s 
*/
public class WFEmps extends EntitiesNoName
{

		///构造
	/** 
	 操作员s
	*/
	public WFEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new WFEmp();
	}

	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll("FK_Dept", "Idx");
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<WFEmp> ToJavaList()
	{
		return (List<WFEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<WFEmp> Tolist()
	{
		ArrayList<WFEmp> list = new ArrayList<WFEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((WFEmp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}
package bp.gpm;

import bp.en.*;
import java.util.*;

/** 
 操作员s
*/
// </summary>
public class GPMEmps extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
	///构造方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GPMEmp();
	}
	/** 
	 操作员s
	*/
	public GPMEmps()
	{
	}
	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll("Name");
	}

		/// 构造方法


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GPMEmp> ToJavaList()
	{
		return (List<GPMEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GPMEmp> Tolist()
	{
		ArrayList<GPMEmp> list = new ArrayList<GPMEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GPMEmp)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
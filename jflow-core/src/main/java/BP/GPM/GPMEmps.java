package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import java.util.*;

/** 
 操作员s
*/
// </summary>
public class GPMEmps extends EntitiesNoName
{

		///#region 构造方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
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

		///#endregion 构造方法


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package bp.gpm;

import bp.en.*;
import java.util.*;

/** 
 部门岗位对应 
*/
public class DeptStations extends Entities
{
	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 工作部门岗位对应
	*/
	public DeptStations()
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
		return new DeptStation();
	}

		///



		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<DeptStation> ToJavaList()
	{
		return (java.util.List<DeptStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DeptStation> Tolist()
	{
		ArrayList<DeptStation> list = new ArrayList<DeptStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptStation)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
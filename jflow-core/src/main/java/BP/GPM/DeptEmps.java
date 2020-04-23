package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 部门人员信息 
*/
public class DeptEmps extends EntitiesMyPK
{

		///#region 构造
	/** 
	 工作部门人员信息
	*/
	public DeptEmps()
	{
	}

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new DeptEmp();
	}

	/**
	 * 删除不存在Port_Emp中的数据
	 */
	public String DelteNotInEmp()
	{
		String sql = "DELETE FROM Port_DeptEmp WHERE FK_Emp NOT IN (SELECT No FROM Port_Emp)";
		DBAccess.RunSQL(sql);
		return "";
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<DeptEmp> ToJavaList()
	{
		return (List<DeptEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DeptEmp> Tolist()
	{
		ArrayList<DeptEmp> list = new ArrayList<DeptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptEmp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}
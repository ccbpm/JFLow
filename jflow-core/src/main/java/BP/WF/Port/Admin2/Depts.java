package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
部门集合
*/
public class Depts extends EntitiesTree
{
	/** 
	 查询全部。
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			return super.RetrieveAll();
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptAttr.No, " = ", BP.Web.WebUser.getFK_Dept());
		qo.addOr();
		qo.AddWhere(DeptAttr.ParentNo, " = ", BP.Web.WebUser.getFK_Dept());
		return qo.DoQuery();
	}
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Dept();
	}
	/** 
	 create ens
	*/
	public Depts()
	{
	}

	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Dept> ToJavaList()
	{
		return (List<Dept>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Dept> Tolist()
	{
		ArrayList<Dept> list = new ArrayList<Dept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Dept)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
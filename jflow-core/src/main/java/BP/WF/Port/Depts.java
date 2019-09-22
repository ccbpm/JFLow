package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
部门集合
*/
public class Depts extends EntitiesNoName
{
	/** 
	 查询全部。
	 
	 @return 
	*/
	@Override
	public int RetrieveAll()
	{
		if (BP.Web.WebUser.No.equals("admin"))
		{
			return super.RetrieveAll();
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(DeptAttr.No, " = ", BP.Web.WebUser.FK_Dept);
		qo.addOr();
		qo.AddWhere(DeptAttr.ParentNo, " = ", BP.Web.WebUser.FK_Dept);
		return qo.DoQuery();
	}
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Dept();
	}
	/** 
	 create ens
	*/
	public Depts()
	{

	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Dept> ToJavaList()
	{
		return (List<Dept>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Dept> Tolist()
	{
		ArrayList<Dept> list = new ArrayList<Dept>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((Dept)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
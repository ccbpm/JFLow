package BP.WF.Data;

import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 报表集合
*/
public class MyDeptEmps extends BP.En.EntitiesNoName
{
	/** 
	 报表集合
	*/
	public MyDeptEmps()
	{
	}

	@Override
	public Entity getGetNewEntity()
	{
		return new MyDeptEmp();
	}
	@Override
	public int RetrieveAll()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyDeptEmpAttr.FK_Dept, BP.Web.WebUser.FK_Dept);
		return qo.DoQuery();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyDeptEmp> ToJavaList()
	{
		return (List<MyDeptEmp>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyDeptEmp> Tolist()
	{
		ArrayList<MyDeptEmp> list = new ArrayList<MyDeptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptEmp)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}
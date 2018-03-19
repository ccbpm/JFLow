package BP.WF.Data;

import BP.En.Entities;
import BP.En.Entity;

/** 
 我部门的待办s
*/
public class MyDeptTodolists extends Entities
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyDeptTodolist();
	}
	/** 
	 我部门的待办集合
	*/
	public MyDeptTodolists()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<MyDeptTodolist> ToJavaList()
	{
		return (java.util.List<MyDeptTodolist>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<MyDeptTodolist> Tolist()
	{
		java.util.ArrayList<MyDeptTodolist> list = new java.util.ArrayList<MyDeptTodolist>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptTodolist)this.get(i));
		}
		return list;
	}
}
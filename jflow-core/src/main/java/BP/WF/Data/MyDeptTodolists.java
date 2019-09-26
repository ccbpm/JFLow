package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 我部门的待办s
*/
public class MyDeptTodolists extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MyDeptTodolist();
	}
	/** 
	 我部门的待办集合
	*/
	public MyDeptTodolists()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyDeptTodolist> ToJavaList()
	{
		return (List<MyDeptTodolist>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyDeptTodolist> Tolist()
	{
		ArrayList<MyDeptTodolist> list = new ArrayList<MyDeptTodolist>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptTodolist)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
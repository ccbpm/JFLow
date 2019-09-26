package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 我部门的流程s
*/
public class MyDeptFlows extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MyDeptFlow();
	}
	/** 
	 我部门的流程集合
	*/
	public MyDeptFlows()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyDeptFlow> ToJavaList()
	{
		return (List<MyDeptFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyDeptFlow> Tolist()
	{
		ArrayList<MyDeptFlow> list = new ArrayList<MyDeptFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptFlow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
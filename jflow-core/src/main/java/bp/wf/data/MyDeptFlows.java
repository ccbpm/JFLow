package bp.wf.data;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import java.util.*;

/** 
 我部门的流程s
*/
public class MyDeptFlows extends Entities
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyDeptFlow();
	}
	/** 
	 我部门的流程集合
	*/
	public MyDeptFlows()
	{
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
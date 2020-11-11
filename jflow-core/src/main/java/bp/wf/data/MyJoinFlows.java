package bp.wf.data;

import bp.da.*;
import bp.wf.*;
import bp.port.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.*;
import java.util.*;

/** 
 我参与的流程s
*/
public class MyJoinFlows extends Entities
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyJoinFlow();
	}
	/** 
	 我参与的流程集合
	*/
	public MyJoinFlows()
	{
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyJoinFlow> ToJavaList()
	{
		return (List<MyJoinFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyJoinFlow> Tolist()
	{
		ArrayList<MyJoinFlow> list = new ArrayList<MyJoinFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyJoinFlow)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
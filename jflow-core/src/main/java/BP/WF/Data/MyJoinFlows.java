package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 我参与的流程s
*/
public class MyJoinFlows extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MyJoinFlow();
	}
	/** 
	 我参与的流程集合
	*/
	public MyJoinFlows()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.WF.Data;

import BP.En.Entities;
import BP.En.Entity;

public class MyJoinFlows extends Entities
{

	///#region 方法
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

	///#endregion


	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MyJoinFlow> ToJavaList()
	{
		return (java.util.List<MyJoinFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MyJoinFlow> Tolist()
	{
		java.util.ArrayList<MyJoinFlow> list = new java.util.ArrayList<MyJoinFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyJoinFlow)this.get(i));
		}
		return list;
	}

	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}



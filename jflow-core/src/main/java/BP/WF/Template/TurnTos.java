package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 条件s
 
*/
public class TurnTos extends Entities
{

		
	/** 
	 条件
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TurnTo();
	}
	/** 
	 条件.
	 
	*/
	public final boolean getIsAllPassed()
	{
		if (this.size() == 0)
		{
			throw new RuntimeException("@没有要判断的集合.");
		}

		for (TurnTo en : this.ToJavaList())
		{
			if (en.getIsPassed() == false)
			{
				return false;
			}
		}
		return true;
	}
	/** 
	 是否通过
	 
	*/
	public final boolean getIsPass()
	{
		if (this.size() == 1)
		{
			if (this.getIsOneOfTurnToPassed())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	public final String getMsgOfDesc()
	{
		String msg = "";
		for (TurnTo c : this.ToJavaList())
		{
			msg += "@" + c.MsgOfTurnTo;
		}
		return msg;
	}
	/** 
	 是不是其中的一个passed. 
	 
	*/
	public final boolean getIsOneOfTurnToPassed()
	{
		for (TurnTo en : this.ToJavaList())
		{
			if (en.getIsPassed() == true)
			{
				return true;
			}
		}
		return false;
	}
	/** 
	 取出其中一个的完成条件。. 
	 
	*/
	public final TurnTo getGetOneOfTurnToPassed()
	{
		for (TurnTo en : this.ToJavaList())
		{
			if (en.getIsPassed() == true)
			{
				return en;
			}
		}
		throw new RuntimeException("@没有完成条件。");
	}
	/** 
	 节点ID
	 
	*/
	public int NodeID = 0;

		///#endregion


		
	/** 
	 条件
	 
	*/
	public TurnTos()
	{
	}
	/** 
	 条件
	 
	*/
	public TurnTos(String fk_flow)
	{
		this.Retrieve(TurnToAttr.FK_Flow, fk_flow);
	}
	/** 
	 条件
	 
	 @param ct 类型
	 @param nodeID 节点
	*/
	public TurnTos(TurnToType ct, int nodeID, long workid)
	{
		this.NodeID = nodeID;
		this.Retrieve(TurnToAttr.FK_Node, nodeID, TurnToAttr.TurnToType, ct.getValue());

		for (TurnTo en : this.ToJavaList())
		{
			en.WorkID = workid;
		}
	}
	/** 
	 描述
	 
	*/
	public final String getTurnToitionDesc()
	{
		return "";
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<TurnTo> ToJavaList()
	{
		return (java.util.List<TurnTo>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<TurnTo> Tolist()
	{
		java.util.ArrayList<TurnTo> list = new java.util.ArrayList<TurnTo>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TurnTo)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
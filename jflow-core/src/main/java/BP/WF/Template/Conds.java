package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Data.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 条件s
*/
public class Conds extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 获得Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Cond();
	}
	/** 
	 在这里面的所有条件是不是都符合.
	*/
	public final boolean getIsAllPassed()
	{
		if (this.Count == 0)
		{
			throw new RuntimeException("@没有要判断的集合.");
		}

		for (Cond en : this)
		{
			if (en.getIsPassed() == false)
			{
				return false;
			}
		}
		return true;
	}
	public final CondOrAnd getCondOrAnd()
	{
		for (Cond item : this)
		{
			return item.getCondOrAnd();
		}

		return CondOrAnd.ByAnd;
	}
	/** 
	 是否通过
	*/
	public final boolean getIsPass()
	{
		if (this.getCondOrAnd() == CondOrAnd.ByAnd)
		{
			return this.getIsPassAnd();
		}
		else
		{
			return this.getIsPassOr();
		}
	}
	/** 
	 是否通过  
	*/
	public final boolean getIsPassAnd()
	{
			// 判断  and. 的关系。
		for (Cond en : this)
		{
			if (en.getIsPassed() == false)
			{
				return false;
			}
		}
		return true;
	}
	public final boolean getIsPassOr()
	{
			// 判断  and. 的关系。
		for (Cond en : this)
		{
			if (en.getIsPassed() == true)
			{
				return true;
			}
		}
		return false;
	}
	/** 
	 描述
	*/
	public final String getMsgOfDesc()
	{
		String msg = "";
		for (Cond c : this)
		{
			msg += "@" + c.MsgOfCond;
		}
		return msg;
	}
	/** 
	 是不是其中的一个passed. 
	*/
	public final boolean getIsOneOfCondPassed()
	{
		for (Cond en : this)
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
	public final Cond getGetOneOfCondPassed()
	{
		for (Cond en : this)
		{
			if (en.getIsPassed() == true)
			{
				return en;
			}
		}
		throw new RuntimeException("@没有完成条件。");
	}
	public int NodeID = 0;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 条件
	*/
	public Conds()
	{
	}
	/** 
	 条件
	 
	 @param fk_flow 流程编号
	*/
	public Conds(String fk_flow)
	{
		this.Retrieve(CondAttr.FK_Flow, fk_flow);
	}
	/** 
	 条件
	 
	 @param ct 类型
	 @param nodeID 节点
	*/
	public Conds(CondType ct, int nodeID, long workid, GERpt enData)
	{
		this.NodeID = nodeID;
		this.Retrieve(CondAttr.NodeID, nodeID, CondAttr.CondType, ct.getValue(), CondAttr.PRI);
		for (Cond en : this)
		{
			en.setWorkID(workid);
			en.en = enData;
		}
	}
	/** 
	 条件 - 配置信息
	 
	 @param ct
	 @param nodeID
	*/
	public Conds(CondType ct, int nodeID)
	{
		this.Retrieve(CondAttr.NodeID, nodeID, CondAttr.CondType, ct.getValue(), CondAttr.PRI);
	}
	public final String getConditionDesc()
	{
		return "";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Cond> ToJavaList()
	{
		return (List<Cond>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Cond> Tolist()
	{
		ArrayList<Cond> list = new ArrayList<Cond>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((Cond)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.WF.Template;

import BP.En.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;

/** 
 条件s
*/
public class Conds extends Entities
{

		///#region 属性
	/** 
	 获得Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Cond();
	}
	/** 
	 在这里面的所有条件是不是都符合.
	 * @throws Exception 
	*/
	public final boolean getIsAllPassed() throws Exception
	{
		if (this.size() == 0)
		{
			throw new RuntimeException("@没有要判断的集合.");
		}

		for (Cond en : this.ToJavaList())
		{
			if (en.getIsPassed() == false)
			{
				return false;
			}
		}
		return true;
	}
	public final CondOrAnd getCondOrAnd() throws Exception
	{
		for (Cond item : this.ToJavaList())
		{
			return item.getCondOrAnd();
		}

		return CondOrAnd.ByAnd;
	}
	/** 
	 是否通过
	 * @throws Exception 
	*/
	public final boolean getIsPass() throws Exception
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
	 * @throws Exception 
	*/
	public final boolean getIsPassAnd() throws Exception
	{
			// 判断  and. 的关系。
		for (Cond en : this.ToJavaList())
		{
			if (en.getIsPassed() == false)
			{
				return false;
			}
		}
		return true;
	}
	public final boolean getIsPassOr() throws Exception
	{
			// 判断  and. 的关系。
		for (Cond en : this.ToJavaList())
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
		for (Cond c : this.ToJavaList())
		{
			msg += "@" + c.MsgOfCond;
		}
		return msg;
	}
	/** 
	 是不是其中的一个passed. 
	 * @throws Exception 
	*/
	public final boolean getIsOneOfCondPassed() throws Exception
	{
		for (Cond en : this.ToJavaList())
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
	 * @throws Exception 
	*/
	public final Cond getGetOneOfCondPassed() throws Exception
	{
		for (Cond en : this.ToJavaList())
		{
			if (en.getIsPassed() == true)
			{
				return en;
			}
		}
		throw new RuntimeException("@没有完成条件。");
	}
	public int NodeID = 0;

		///#endregion


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
	 * @throws Exception 
	*/
	public Conds(String fk_flow) throws Exception
	{
		this.Retrieve(CondAttr.FK_Flow, fk_flow);
	}
	/** 
	 条件
	 
	 @param ct 类型
	 @param nodeID 节点
	 * @throws Exception 
	*/
	public Conds(CondType ct, int nodeID, long workid, GERpt enData) throws Exception
	{
		this.NodeID = nodeID;
		this.Retrieve(CondAttr.NodeID, nodeID, CondAttr.CondType, ct.getValue(), CondAttr.PRI);
		for (Cond en : this.ToJavaList())
		{
			en.setWorkID(workid);
			en.en = enData;
		}
	}
	/** 
	 条件 - 配置信息
	 
	 @param ct
	 @param nodeID
	 * @throws Exception 
	*/
	public Conds(CondType ct, int nodeID) throws Exception
	{
		this.Retrieve(CondAttr.NodeID, nodeID, CondAttr.CondType, ct.getValue(), CondAttr.PRI);
	}
	public final String getConditionDesc()
	{
		return "";
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Cond> ToJavaList()
	{
		return (List<Cond>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Cond> Tolist()
	{
		ArrayList<Cond> list = new ArrayList<Cond>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Cond)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
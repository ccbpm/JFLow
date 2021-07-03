package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.wf.data.*;
import java.util.*;

/** 
 条件s
*/
public class Conds extends Entities
{

		///属性
	public final String getConditionDesc()
	{
		return "";
	}
	/** 
	 获得Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Cond();
	}
	/** 
	 执行计算
	 
	 @param runModel 模式
	 @return 
	 * @throws Exception 
	*/

	public final boolean GenerResult() throws Exception
	{
		return GenerResult(null);
	}


//ORIGINAL LINE: public bool GenerResult(GERpt en = null)
	public final boolean GenerResult(GERpt en) throws Exception
	{
		if (this.size() == 0)
		{
			throw new RuntimeException("err@没有要计算的条件，无法计算.");
		}

		//给条件赋值.
		if (en != null)
		{
			for (Cond cd : this.ToJavaList())
			{
				cd.setWorkID(en.getOID());
				cd.en = en;
			}
		}


			///首先计算简单的.
		//如果只有一个条件,就直接范围该条件的执行结果.
		if (this.size() == 1)
		{
			Cond cond = this.get(0) instanceof Cond ? (Cond)this.get(0) : null;
			return cond.getIsPassed();
		}



			/// 首先计算简单的.


			///处理混合计算。
		String exp = "";
		for (Cond item : this.ToJavaList())
		{
			if (item.getHisDataFrom() == ConnDataFrom.CondOperator)
			{
				exp += " " + item.getOperatorValue();
				continue;
			}

			if (item.getIsPassed())
			{
				exp += " 1=1 ";
			}
			else
			{
				exp += " 1=2 ";
			}
		}

		//如果是混合计算.
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				sql = " SELECT TOP 1 No FROM WF_Emp WHERE " + exp;
				break;
			case MySQL:
				sql = " SELECT No FROM WF_Emp WHERE " + exp + "   limit 1 ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = " SELECT No FROM WF_Emp WHERE " + exp + "   limit 1 ";
				break;
			case DM:
				sql = " SELECT No FROM WF_Emp WHERE " + exp + "   rownum <=1 ";
				break;
			default:
				throw new RuntimeException("err@没有做的数据库类型判断.");
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return false;
		}
		return true;

			/// 处理混合计算。
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
	public int NodeID = 0;

		///


		///构造
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
		this.Retrieve(CondAttr.FK_Node, nodeID, CondAttr.CondType, ct.getValue(), CondAttr.Idx);
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
		this.Retrieve(CondAttr.FK_Node, nodeID, CondAttr.CondType, ct.getValue(), CondAttr.Idx);
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
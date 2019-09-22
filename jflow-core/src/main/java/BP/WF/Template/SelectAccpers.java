package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 选择接受人
*/
public class SelectAccpers extends EntitiesMyPK
{
	/** 
	 是否记忆下次选择
	*/
	public final boolean getIsSetNextTime()
	{
		if (this.size() == 0)
		{
			return false;
		}

		for (SelectAccper item : this)
		{
			if (item.getIsRemember() == true)
			{
				return item.getIsRemember();
			}
		}
		return false;
	}
	/** 
	 查询接收人,如果没有设置就查询历史记录设置的接收人.
	 
	 @param fk_node
	 @param Rec
	 @return 
	*/
	public final int QueryAccepter(int fk_node, String rec, long workid)
	{
		//查询出来当前的数据.
		int i = this.Retrieve(SelectAccperAttr.FK_Node, fk_node, SelectAccperAttr.WorkID, workid);
		if (i != 0)
		{
			return i; //如果没有就找最大的workid.
		}

		//找出最近的工作ID
		int maxWorkID = BP.DA.DBAccess.RunSQLReturnValInt("SELECT Max(WorkID) FROM WF_SelectAccper WHERE Rec='" + rec + "' AND FK_Node=" + fk_node, 0);
		if (maxWorkID == 0)
		{
			return 0;
		}

		//查询出来该数据.
		this.Retrieve(SelectAccperAttr.FK_Node, fk_node, SelectAccperAttr.WorkID, maxWorkID);

		//返回查询结果.
		return this.size();
	}
	/** 
	 查询上次的设置
	 
	 @param fk_node 节点编号
	 @param rec 当前人员
	 @param workid 工作ID
	 @return 
	*/
	public final int QueryAccepterPriSetting(int fk_node)
	{
		//找出最近的工作ID.
		int maxWorkID = BP.DA.DBAccess.RunSQLReturnValInt("SELECT Max(WorkID) FROM WF_SelectAccper WHERE " + SelectAccperAttr.IsRemember + "=1 AND Rec='" + BP.Web.WebUser.No + "' AND FK_Node=" + fk_node, 0);
		if (maxWorkID == 0)
		{
			return 0;
		}

		//查询出来该数据.
		this.Retrieve(SelectAccperAttr.FK_Node, fk_node, SelectAccperAttr.WorkID, maxWorkID);

		//返回查询结果.
		return this.size();
	}
	/** 
	 他的到人员
	*/
	public final Emps getHisEmps()
	{
		Emps ens = new Emps();
		for (SelectAccper ns : this)
		{
			ens.AddEntity(new Emp(ns.getFK_Emp()));
		}
		return ens;
	}
	/** 
	 他的工作节点
	*/
	public final Nodes getHisNodes()
	{
		Nodes ens = new Nodes();
		for (SelectAccper ns : this)
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;
	}
	/** 
	 选择接受人
	*/
	public SelectAccpers()
	{
	}
	/** 
	 查询出来选择的人员
	 
	 @param fk_flow
	 @param workid
	*/
	public SelectAccpers(long workid)
	{
		BP.En.QueryObject qo = new QueryObject(this);
		qo.AddWhere(SelectAccperAttr.WorkID, workid);
		qo.addOrderByDesc(SelectAccperAttr.FK_Node,SelectAccperAttr.Idx);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SelectAccper();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SelectAccper> ToJavaList()
	{
		return (List<SelectAccper>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SelectAccper> Tolist()
	{
		ArrayList<SelectAccper> list = new ArrayList<SelectAccper>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SelectAccper)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
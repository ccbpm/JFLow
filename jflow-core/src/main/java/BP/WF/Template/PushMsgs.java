package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 消息推送
 
*/
public class PushMsgs extends EntitiesMyPK
{
	/** 
	 消息推送
	 
	*/
	public PushMsgs()
	{
	}
	/** 
	 消息推送
	 
	 @param fk_flow
	*/
	public PushMsgs(String fk_flow)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(PushMsgAttr.FK_Node, "SELECT NodeID FROM WF_Node WHERE FK_Flow='" + fk_flow + "'");
		qo.DoQuery();
	}
	/** 
	 消息推送
	 
	 @param nodeid 节点ID
	*/
	public PushMsgs(int nodeid)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(PushMsgAttr.FK_Node, nodeid);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new PushMsg();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<PushMsg> ToJavaList()
	{
		return (java.util.List<PushMsg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<PushMsg> Tolist()
	{
		java.util.ArrayList<PushMsg> list = new java.util.ArrayList<PushMsg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((PushMsg)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
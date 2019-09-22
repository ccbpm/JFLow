package BP.WF.Template;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

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
	public Entity getNewEntity()
	{
		return new PushMsg();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<PushMsg> ToJavaList()
	{
		return (List<PushMsg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<PushMsg> Tolist()
	{
		ArrayList<PushMsg> list = new ArrayList<PushMsg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((PushMsg)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
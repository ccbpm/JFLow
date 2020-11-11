package bp.wf.template;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.*;
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
	 
	 @param flowNo 流程编号
	 * @throws Exception 
	*/
	public PushMsgs(String flowNo) throws Exception
	{
		this.Retrieve(PushMsgAttr.FK_Flow, flowNo);
	}
	/** 
	 消息推送
	 
	 @param nodeid 节点ID
	 * @throws Exception 
	*/
	public PushMsgs(int nodeid) throws Exception
	{
		this.Retrieve(PushMsgAttr.FK_Node, nodeid);
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new PushMsg();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
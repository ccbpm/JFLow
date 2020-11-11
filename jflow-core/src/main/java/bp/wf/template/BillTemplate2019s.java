package bp.wf.template;

import bp.en.*;
import bp.wf.*;
import java.util.*;

/** 
 单据模板s
*/
public class BillTemplate2019s extends EntitiesNoName
{

		///#region 构造
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BillTemplate2019();
	}
	/** 
	 单据模板
	*/
	public BillTemplate2019s()
	{
	}

		///#endregion


		///#region 查询与构造
	/** 
	 按节点查询
	 
	 @param nd
	 * @throws Exception 
	*/
	public BillTemplate2019s(Node nd) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BillTemplate2019Attr.NodeID, nd.getNodeID());
		if (nd.getIsStartNode())
		{
			qo.addOr();
			qo.AddWhere("No", "SLHZ");
		}
		qo.DoQuery();
	}
	/** 
	 按流程查询
	 
	 @param fk_flow 流程编号
	 * @throws Exception 
	*/
	public BillTemplate2019s(String fk_flow) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(BillTemplate2019Attr.NodeID, "SELECT NodeID FROM WF_Node WHERE fk_flow='" + fk_flow + "'");
		qo.DoQuery();
	}
	/** 
	 按节点查询
	 
	 @param fk_node 节点ID
	 * @throws Exception 
	*/
	public BillTemplate2019s(int fk_node) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(BillTemplate2019Attr.NodeID, fk_node);
		qo.DoQuery();
	}

		///#endregion 查询与构造


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<BillTemplate2019> ToJavaList()
	{
		return (List<BillTemplate2019>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<BillTemplate2019> Tolist()
	{
		ArrayList<BillTemplate2019> list = new ArrayList<BillTemplate2019>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BillTemplate2019)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
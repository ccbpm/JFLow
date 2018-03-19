package BP.WF.MS;

import BP.En.EntityMM;
import BP.En.Map;

/** 
 制度章节
 节点的章节编号有两部分组成.	 
 记录了从一个节点调用其他的多个节点.
 也记录了调用这个节点的其他的节点.
*/
public class NodeDtl extends EntityMM
{
		
	/** 
	节点
	*/
	public final String getFK_Node()
	{
		return this.GetValStringByKey(NodeDtlAttr.FK_Node);
	}
	public final void setFK_Node(String value)
	{
		this.SetValByKey(NodeDtlAttr.FK_Node, value);
	}
	/** 
	 章节编号
	*/
	public final String getFK_ZhiDuDtl()
	{
		return this.GetValStringByKey(NodeDtlAttr.FK_ZhiDuDtl);
	}
	public final void setFK_ZhiDuDtl(String value)
	{
		this.SetValByKey(NodeDtlAttr.FK_ZhiDuDtl, value);
	}
	public final String getFK_ZhiDuDtlT()
	{
		return this.GetValRefTextByKey(NodeDtlAttr.FK_ZhiDuDtl);
	}
		///#endregion

		
	/** 
	 制度章节
	*/
	public NodeDtl()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("MS_NodeDtl", "制度章节");


		map.AddTBIntPK(NodeDtlAttr.FK_Node, 0, "节点", true, true);
		map.AddTBStringPK(NodeDtlAttr.FK_ZhiDuDtl, null, "节点", true, true,1,100,100);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}
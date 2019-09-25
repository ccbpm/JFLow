package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import java.util.*;

/**
 * 节点集合
 */
public class Nodes extends EntitiesOID {

	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getNewEntity() {
		return new Node();
	}

	/**
	 * 节点集合
	 */
	public Nodes() {
	}

	/**
	 * 节点集合.
	 * 
	 * @param FlowNo
	 * @throws Exception
	 */
	public Nodes(String fk_flow) throws Exception {
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		return;
	}

	/**
	 * RetrieveAll
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public int RetrieveAll() throws Exception {
		Object tempVar = Cash.GetObj(this.toString(), Depositary.Application);
		Nodes nds = tempVar instanceof Nodes ? (Nodes) tempVar : null;
		if (nds == null) {
			nds = new Nodes();
			QueryObject qo = new QueryObject(nds);
			qo.AddWhereInSQL(NodeAttr.NodeID, " SELECT Node FROM WF_Direction ");
			qo.addOr();
			qo.AddWhereInSQL(NodeAttr.NodeID, " SELECT ToNode FROM WF_Direction ");
			qo.DoQuery();

			Cash.AddObj(this.toString(), Depositary.Application, nds);
			Cash.AddObj(this.getNewEntity().toString(), Depositary.Application, nds);
		}

		this.clear();
		this.AddEntities(nds);
		return this.size();
	}

	/**
	 * 转化成 java list,C#不能调用.
	 * 
	 * @return List
	 */
	public final List<Node> ToJavaList() {
		return (List<Node>) (Object) this;
	}

	/**
	 * 转化成list 为了翻译成java的需要
	 * 
	 * @return List
	 */
	public final ArrayList<BP.WF.Node> Tolist() {
		ArrayList<BP.WF.Node> list = new ArrayList<BP.WF.Node>();
		for (int i = 0; i < this.size(); i++) {
			list.add((BP.WF.Node) this.get(i));
		}
		return list;
	}

}
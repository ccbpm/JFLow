package bp.wf.template.ccen;

import bp.en.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 抄送部门
*/
public class CCDepts extends EntitiesMM
{

	/** 
	 他的工作节点
	*/
	public final Nodes getHisNodes() throws Exception {
		Nodes ens = new Nodes();
		for (CCDept ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;
	}
	/** 
	 抄送部门
	*/
	public CCDepts()  {
	}
	/** 
	 抄送部门
	 
	 param NodeID 节点ID
	*/
	public CCDepts(int NodeID) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCDeptAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 抄送部门
	 
	 param StationNo StationNo
	*/
	public CCDepts(String StationNo) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCDeptAttr.FK_Dept, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new CCDept();
	}
	/** 
	 取到一个工作部门集合能够访问到的节点s
	 
	 param sts 工作部门集合
	 @return 
	*/
	public final Nodes GetHisNodes(Stations sts) throws Exception {
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Station st : sts.ToJavaList())
		{
			tmp = this.GetHisNodes(st.getNo());
			for (Node nd : tmp.ToJavaList())
			{
				if (nds.contains(nd))
				{
					continue;
				}
				nds.AddEntity(nd);
			}
		}
		return nds;
	}

	/** 
	 工作部门对应的节点
	 
	 param stationNo 工作部门编号
	 @return 节点s
	*/
	public final Nodes GetHisNodes(String stationNo) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCDeptAttr.FK_Dept, stationNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (CCDept en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<CCDept> ToJavaList() {
		return (java.util.List<CCDept>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CCDept> Tolist()  {
		ArrayList<CCDept> list = new ArrayList<CCDept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CCDept)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}
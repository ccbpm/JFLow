package cn.jflow.model.designer;

import java.util.HashMap;

import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.ConnDataFrom;



public class ConditionModel {
	public ConditionModel()
	{
		
	}
	public ConditionModel(String CurrentCond,String CondType,String FK_Flow,String FK_MainNode,String FK_Node,String FK_Attr,String DirType,String ToNodeId)
	{
		this.CurrentCond=CurrentCond;
		this.CondType=CondType;
		this.FK_Flow=FK_Flow;
		this.FK_MainNode=FK_MainNode;
		this.FK_Node=FK_Node;
		this.FK_Attr=FK_Attr;
		this.DirType=DirType;
		this.ToNodeID=ToNodeId;
	}
	private String CondType;
//	 public string CondType
//     {
//         get { return Request.QueryString["CondType"]; }
//     }

     public String getCondType() {
		return CondType;
	}
	public void setCondType(String condType) {
		CondType = condType;
	}
	private String FK_Flow;
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
//	public string FK_Flow
//     {
//         get { return Request.QueryString["FK_Flow"]; }
//     }

	private String FK_MainNode;
     public String getFK_MainNode() {
		return FK_MainNode;
	}
	public void setFK_MainNode(String fK_MainNode) {
		FK_MainNode = fK_MainNode;
	}
//	public string FK_MainNode
//     {
//         get { return Request.QueryString["FK_MainNode"]; }
//     }

	private String FK_Node;
     public String getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(String fK_Node) {
		FK_Node = fK_Node;
	}
//	public string FK_Node
//     {
//         get { return Request.QueryString["FK_Node"]; }
//     }

	private String FK_Attr;
     public String getFK_Attr() {
		return FK_Attr;
	}
	public void setFK_Attr(String fK_Attr) {
		FK_Attr = fK_Attr;
	}
//	public string FK_Attr
//     {
//         get { return Request.QueryString["FK_Attr"]; }
//     }

	private String DirType;
     public String getDirType() {
		return DirType;
	}
	public void setDirType(String dirType) {
		DirType = dirType;
	}
//	public string DirType
//     {
//         get { return Request.QueryString["DirType"]; }
//     }

	private String ToNodeID;
     public String getToNodeId() {
		return ToNodeID;
	}
	public void setToNodeId(String toNodeId) {
		ToNodeID = toNodeId;
	}
//	public string ToNodeId
//     {
//         get { return Request.QueryString["ToNodeId"]; }
//     }
	private HashMap<ConnDataFrom,String> DataFrom=new HashMap<ConnDataFrom,String>();
    public HashMap<ConnDataFrom, String> getDataFrom() {
    	DataFrom.put(ConnDataFrom.Depts, "CondDept");
    	DataFrom.put(ConnDataFrom.NodeForm, "Cond");
    	DataFrom.put(ConnDataFrom.Paras, "CondByPara");
    	DataFrom.put(ConnDataFrom.SQL, "CondBySQL");
    	DataFrom.put(ConnDataFrom.Stas, "CondStation");
    	DataFrom.put(ConnDataFrom.Url, "CondByUrl");
		return DataFrom;
	}
	public void setDataFrom(HashMap<ConnDataFrom, String> dataFrom) {
		DataFrom = dataFrom;
	}

//     private Dictionary<ConnDataFrom, string> DataFrom = new Dictionary<ConnDataFrom, string>()
//                                                             {
//                                                                 {ConnDataFrom.Depts, "CondDept"},
//                                                                 {ConnDataFrom.Form, "Cond"},
//                                                                 {ConnDataFrom.Paras, "CondByPara"},
//                                                                 {ConnDataFrom.SQL, "CondBySQL"},
//                                                                 {ConnDataFrom.Stas, "CondStation"},
//                                                                 {ConnDataFrom.Url, "CondByUrl"}
//                                                             };

     /// <summary>
     /// 当前使用的条件类型
     /// </summary>
     public String CurrentCond;

     public String getCurrentCond() {
		return CurrentCond;
	}
	public void setCurrentCond(String currentCond) {
		CurrentCond = currentCond;
	}
	public void init()
     {
//		System.out.println(this.CondType);
         Cond cond = new Cond();
         if(cond.Retrieve(CondAttr.NodeID, this.FK_Node, CondAttr.ToNodeID, this.ToNodeID) != 0)
         {
             CurrentCond = this.getDataFrom().get(cond.getHisDataFrom());//[cond.HisDataFrom];
         }
     }
}

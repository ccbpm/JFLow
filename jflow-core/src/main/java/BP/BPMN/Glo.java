package BP.BPMN;

import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.DataStoreModel;
import BP.WF.Template.Direction;

public class Glo {
	/** 
	   创建一个流程.
	 @param flowSort 流程类别
	 @return string
	 * @throws Exception 
	*/
	public static String NewFlow(String flowSort, String flowName, DataStoreModel dsm, String ptable, String flowMark, String flowVer) throws Exception {
		//执行保存.
		BP.WF.Flow fl = new BP.WF.Flow();
		
		//修改类型为CCBPMN
		fl.setDType(StringHelper.isNullOrEmpty(flowVer) ? 1 : Integer.parseInt(flowVer));
		String flowNo = fl.DoNewFlow(flowSort, flowName, dsm, ptable, flowMark);
		fl.setNo(flowNo);

		//如果为CCFlow模式则不进行写入Json串
		if (flowVer.equals("0")) {
			return flowNo;
		}

		
		//创建连线
		Direction drToNode = new Direction();
		drToNode.setFK_Flow (flowNo);
		drToNode.setNode(Integer.parseInt(Integer.parseInt(flowNo) + "01"));
		drToNode.setToNode(Integer.parseInt(Integer.parseInt(flowNo) + "02"));
		drToNode.Insert();

		return flowNo;
	}
	/** 
	 创建一个节点
	 
	 @param flowNo
	 @param x
	 @param y
	 @return 
	 * @throws Exception 
	*/
	public static int NewNode(String flowNo, int x, int y) throws Exception {
		BP.WF.Flow fl = new Flow(flowNo);
		BP.WF.Node nd = fl.DoNewNode(x, y);
		return nd.getNodeID();
	}
	/** 
	 删除节点.
	 
	 @param nodeid
	 * @throws Exception 
	*/
	public static void DeleteNode(int nodeid) throws Exception {
		BP.WF.Node nd = new Node(nodeid);
		nd.Delete();
	}
	
	  
	 
	
	
     // #endregion

}

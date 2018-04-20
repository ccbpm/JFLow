package BP.BPMN;

import java.io.File;

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

		//确定模板
		String tempFile = BP.Sys.SystemConfig.getCCFlowAppPath() + "/WF/Data/Templete/NewFlow.json";
		if (flowVer.equals("1")) { //CCBPM_DType.CCBPM
			tempFile = BP.Sys.SystemConfig.getCCFlowAppPath() + "/WF/Data/Templete/ccbpm.json";
		}
		//将流程模版保存到数据库里.
		try {
			fl.SaveFileToDB("FlowJson", tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//替换流程模板中的默认节点编号
		Flow fl_BPMN = new Flow(flowNo);
		String flowJson = fl_BPMN.getFlowJson();
		//开始节点
		flowJson = flowJson.replace("@UserTask01@", Integer.parseInt(flowNo) + "01");
		//第二节点
		flowJson = flowJson.replace("@UserTask02@", Integer.parseInt(flowNo) + "02");
		fl_BPMN.setFlowJson(flowJson);
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
	/** 
	 获得流程JSON数据.
	 
	 @param flowNo 流程编号
	 @return json格式的数据
	 * @throws Exception 
	*/
	public static String GenerFlowJsonFromDB(String flowNo) throws Exception {
		Flow fl = new Flow(flowNo);
		return fl.getFlowJson();
	}
	public static String GenerFlowJsonFromFile(String flowNo) throws Exception {
		String tempFile = BP.Sys.SystemConfig.getPathOfDataUser() + "FlowDesc/" + flowNo + ".json";
		/*if (System.IO.File.Exists(tempFile) == false) {
			String json = GenerFlowJsonFromDB(flowNo);
		}*/
		File  f=new File(tempFile);
		if(f.exists()==false){
			String json = GenerFlowJsonFromDB(flowNo);
		}
		Flow fl = new Flow(flowNo);
		return fl.getFlowJson();
	}
	/** 
	 保存流程数据.
	 
	 @param flowNo 流程编号
	 @param json json格式的数据.
	 * @throws Exception 
	*/
	public static String SaveGraphData(String flowNo, String json) throws Exception {
		// 保存到临时文件.
		String tempFile = BP.Sys.SystemConfig.getPathOfTemp() + "/" + flowNo + ".json";
		BP.DA.DataType.WriteFile(tempFile, json);

			///#region 第1步骤:进行必要的安全检查.
			///#endregion 进行必要的安全检查.

			///#region 第2步骤:进行相关的到ccbpm存储.
			///#endregion 进行相关的到ccbpm存储.

		//执行保存.
		Flow fl = new Flow(flowNo);
		try {
			fl.SaveFileToDB("FlowJson", tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

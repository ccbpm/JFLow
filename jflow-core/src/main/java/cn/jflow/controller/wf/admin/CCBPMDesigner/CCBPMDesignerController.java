package cn.jflow.controller.wf.admin.CCBPMDesigner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DBAccess;
import BP.DA.DataSet;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Entity.CCBPM_DType;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.LabNote;
import BP.WF.Template.LabNoteAttr;
import BP.WF.Template.NodeAttr;
import cn.jflow.common.util.JsonPluginsUtil;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin/CCBPMDesignerController")
@Scope("request")
public class CCBPMDesignerController extends BaseController {
	///#region 全局变量IRequiresSessionState
	/**
	 * 流程编号
	 */
	public String getFK_Flow() {
		return this.getParamter("FK_Flow");
	}

	/**
	 * 节点编号
	 */
	public int getFK_Node() {
		String fk_node = this.getParamter("FK_Node");
		if (!isNullOrEmpty(fk_node)) {
			return Integer.parseInt(fk_node);
		}
		return 0;
	}

	/**
	 * http请求
	 */
	/*private HttpContext private_Context;
	public final HttpContext get_Context()
	{
		return private_Context;
	}
	public final void set_Context(HttpContext value)
	{
		private_Context = value;
	}*/

	/**
	 * 公共方法获取值
	 * @param param 参数名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	/**
	 * 公共方法获取值
	 * 
	 * @param param 参数名
	 * @return
	 */
	public final String getUTF8ToString(String param) {
		String str=this.getRequest().getParameter(param);
		if(!"".equals(str)&&str!=null && !"null".equals(str)){
			try {
				return java.net.URLDecoder.decode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return "";
		}
	}

	///#endregion
	@ResponseBody
	@RequestMapping(value = "/ProcessRequest", method = RequestMethod.POST)
	public String ProcessRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*set_Context(context);

		if (get_Context() == null)
		{
			return;
		}*/

		String action = "";
		//返回值
		String s_responsetext = "";
		if (!isNullOrEmpty(request.getParameter("action").toString())) {
			action = request.getParameter("action").toString();
		}

		//			switch (action)
		//ORIGINAL LINE: case "load":
		if (action.equals("load")) //获取流程图表数据
		{
			s_responsetext = Flow_LoadFlowJsonData();
		}
		//ORIGINAL LINE: case "save":
		else if (action.equals("save")) //保存流程图
		{
			s_responsetext = Flow_Save();
		}
		//ORIGINAL LINE: case "saveAs":
		else if (action.equals("saveAs")) //另存为流程
		{
			s_responsetext = Flow_SaveAs();
		}
		//ORIGINAL LINE: case "genernodeid":
		else if (action.equals("genernodeid")) //创建节点获取节点编号
		{
			s_responsetext = Node_Create_GenerNodeID();
		}
		//ORIGINAL LINE: case "deletenode":
		else if (action.equals("deletenode")) //删除流程节点
		{
			s_responsetext = Node_DeleteNodeOfNodeID();
		}
		//ORIGINAL LINE: case "editnodename":
		else if (action.equals("editnodename")) //修改节点名称
		{
			s_responsetext = Node_EditNodeName();
		}
		//ORIGINAL LINE: case "ccbpm_flow_elements":
		else if (action.equals("ccbpm_flow_elements")) //流程所有元素集合
		{
			s_responsetext = Flow_AllElements_ResponseJson();
		}
		//ORIGINAL LINE: case "changenoderunmodel":
		else if (action.equals("changenoderunmodel")) //修改节点运行模式
		{
			s_responsetext = Node_ChangeRunModel();
		}
		else if( action.equals("ccbpm_flow_resetversion"))//重置流程版本为1.0
		{
             s_responsetext = Flow_ResetFlowVersion();
		}
		if (isNullOrEmpty(s_responsetext)) {
			s_responsetext = "";
		}
		return s_responsetext;
	}

	///#region 流程相关 Flow
	/**
	 * 加载流程图数据
	 * @return
	 * @throws IOException
	 */
	private String Flow_LoadFlowJsonData() throws IOException {
		String diagramId = this.getParamter("diagramId");//转换UTF-8
		Flow fl = new Flow(diagramId);
		System.out.println(fl.getFlowJson());
		return fl.getFlowJson();
	}

	/**
	 * 保存流程图信息
	 * @return
	 * @throws Exception
	 */
	private String Flow_Save() throws Exception {
		//流程格式.
		String diagram = this.getParamter("diagram");/*getUTF8ToString("diagram")*/;
		//流程图.
		String png = this.getParamter("png");
		// 流程编号.
		String flowNo = this.getParamter("diagramId");
		//节点到节点关系
		String direction = this.getParamter("direction");
		//直接保存流程图信息
		Flow fl = new Flow(flowNo);
		//修改版本
		fl.setDType(CCBPM_DType.BPMN.equals(fl.getDType()) ? CCBPM_DType.BPMN.getValue() : CCBPM_DType.CCBPM.getValue());
		//直接保存了.
		fl.Update();
		fl.setFlowJson(diagram);

		//节点方向
		String[] dir_Nodes = direction.split("@");
		Direction drToNode = new Direction();
		drToNode.Delete(DirectionAttr.FK_Flow, flowNo);
		for (String item : dir_Nodes) {
			if (isNullOrEmpty(item)) {
				continue;
			}
			String[] nodes = item.split(":");
			if (nodes.length == 2) {
				if(!(isNullOrEmpty(nodes[0])||isNullOrEmpty(nodes[1]))){
					drToNode = new Direction();
					drToNode.setFK_Flow(flowNo);
					drToNode.setNode(Integer.parseInt(nodes[0]));
					drToNode.setToNode(Integer.parseInt(nodes[1]));
					drToNode.Insert();
				}
			}
		}
		
		/**************裴孝峰 2016-7-29 Add Start*****************************************************************/
		try{
			//清空标签
			LabNote labelNode = new LabNote();
			labelNode.Delete(LabNoteAttr.FK_Flow, flowNo);
			//流程格式转第一级map
			Map firstMap=JsonPluginsUtil.jsonToMap(diagram);
			//获取key等于s的map数据
			String firstS=String.valueOf(firstMap.get("s"));
			//通过获取的s的数据转换为第二级map
			Map secondMap=JsonPluginsUtil.jsonToMap(firstS);
			//通过第二级map获取key等于figures的数据
			String figures=String.valueOf(secondMap.get("figures"));
			//把figures的数据转换成数组数据
			String[] figuresArr=JsonPluginsUtil.jsonToStringArray(figures);
			//循环figuresArr数组
			for(int i=0;i<figuresArr.length;i++){
				//把figuresArr数组循环转换成第三级map
				Map thirdMap=JsonPluginsUtil.jsonToMap(figuresArr[i]);
				String CCBPM_Shape="";//定义
				if(thirdMap!=null){//判断map是否为空
					CCBPM_Shape=String.valueOf(thirdMap.get("CCBPM_Shape"));
					//为空时继续
					if(StringHelper.isNullOrEmpty(CCBPM_Shape)){
						continue;
					}
					if("Node".equals(CCBPM_Shape)){
						//节点坐标处理
						BP.WF.Node node = new BP.WF.Node();
						node.RetrieveByAttr(NodeAttr.NodeID, String.valueOf(thirdMap.get("CCBPM_OID")));
						if (!StringHelper.isNullOrEmpty(node.getName())) {
							//获取key等于rotationCoords的map数据
							String rotationCoords=String.valueOf(thirdMap.get("rotationCoords"));
							//把rotationCoords的数据转换成数组数据
							String[] rotationCoordsArr=JsonPluginsUtil.jsonToStringArray(rotationCoords);
							if(rotationCoordsArr.length>0){
								//把rotationCoordsArr数组循环转换成第四级map
								Map fourMap=JsonPluginsUtil.jsonToMap(rotationCoordsArr[0]);
								// map数据不为空更新x.y数据
								if(fourMap!=null){
									int x=Integer.parseInt(String.valueOf(fourMap.get("x")));
									int y=Integer.parseInt(String.valueOf(fourMap.get("y")));
									node.setX(x);
									node.setY(y);
									node.DirectUpdate();
								}
							}
						}
					}else if("Text".equals(CCBPM_Shape)){
						//获取key等于primitives的map数据
						String primitives=String.valueOf(thirdMap.get("primitives"));
						//把primitives的数据转换成数组数据
						String[] primitivesArr=JsonPluginsUtil.jsonToStringArray(primitives);
						if(primitivesArr.length>0){
							//把primitivesArr数组循环转换成第五级map
							Map fiveMap=JsonPluginsUtil.jsonToMap(primitivesArr[0]);
							
							//获取key等于vector的map数据
							String vector=String.valueOf(fiveMap.get("vector"));
							//把vector的数据转换成数组数据
							String[] vectorArr=JsonPluginsUtil.jsonToStringArray(vector);
							
							if(vectorArr.length>0){
								//把vectorArr数组循环转换成第六级map
								Map sixMap=JsonPluginsUtil.jsonToMap(vectorArr[0]);
								// map数据不为空更新x.y数据
								if(sixMap!=null){
									labelNode = new LabNote();
									labelNode.setFK_Flow(flowNo);
									labelNode.setName(String.valueOf(fiveMap.get("str")));
									int x=Integer.parseInt(String.valueOf(sixMap.get("x")));
									int y=Integer.parseInt(String.valueOf(sixMap.get("y")));
									labelNode.setX(x);
									labelNode.setY(y);
									labelNode.Insert();
								}
							}
						}
					}
				}		
			}
			return "true";
		}
		catch (RuntimeException ex){
		}
		/**************裴孝峰 2016-7-29 Add  END*****************************************************************/
		return "true";
	}

	/**
	 * 另存为流程图
	 * @return
	 */
	private String Flow_SaveAs() {
		return "";
	}
	/**
	 *  重置流程版本为1.0
	 */
    
    private String Flow_ResetFlowVersion()
    {
        DBAccess.RunSQL("UPDATE WF_FLOW SET DTYPE=0,FLOWJSON='' WHERE NO='" + this.getFK_Flow() + "'");
        return "true";
    }

	/**
	 * 获取流程所有元素
	 * @return json data
	 */
	private String Flow_AllElements_ResponseJson() {
		try {
			BP.WF.Flow flow = new BP.WF.Flow();
			flow.setNo(this.getFK_Flow());
			flow.RetrieveFromDBSources();
			if (flow.getDType() == 0) {
				//获取所有节点
				//Environment.NewLine
				String sqls = "SELECT NODEID,NAME,X,Y,RUNMODEL FROM WF_NODE WHERE FK_FLOW='" + this.getFK_Flow() + "';" + ""
						+ "SELECT NODE,TONODE FROM WF_DIRECTION WHERE FK_FLOW='" + this.getFK_Flow() + "';" + ""
						+ "SELECT MYPK,NAME,X,Y FROM WF_LABNOTE WHERE FK_FLOW='" + this.getFK_Flow() + "';";
				DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);
				ds.Tables.get(0).setTableName("Nodes");
				ds.Tables.get(1).setTableName("Direction");
				ds.Tables.get(2).setTableName("LabNote");

				String str = "{\"success\":true,\"msg\":\"\",\"data\":\"{";
				str=str+"\\\""+ds.Tables.get(0).TableName+"\\\":"+JSONArray.fromObject(ds.Tables.get(0).Rows).toString().replaceAll("\"", "\\\\\"");
				str=str+",\\\""+ds.Tables.get(1).TableName+"\\\":"+JSONArray.fromObject(ds.Tables.get(1).Rows).toString().replaceAll("\"", "\\\\\"");
				str=str+",\\\""+ds.Tables.get(2).TableName+"\\\":"+JSONArray.fromObject(ds.Tables.get(2).Rows).toString().replaceAll("\"", "\\\\\"");
				str+="}\"}";
				return str;
				//JSONArray jsonArray = JSONArray.fromObject(ds);
				//return jsonArray.toString();
			}
			return "{}";
		} catch (RuntimeException ex) {
			return "{\"success\":false,\"msg\":"+ex.getMessage()+"}";
		}
	}

	///#endregion end Flow

	///#region 节点相关 Nodes
	/**
	 * 创建流程节点并返回编号
	 * @return
	 */
	private String Node_Create_GenerNodeID() {
		try {
			String FK_Flow = this.getParamter("FK_Flow");
			String figureName = this.getParamter("FigureName");
			String x = this.getParamter("x");
			String y = this.getParamter("y");
			int iX = 0;
			int iY = 0;
			if (!isNullOrEmpty(x)) {
				iX = (int) Double.parseDouble(x);
			}
			if (!isNullOrEmpty(y)) {
				iY = (int) Double.parseDouble(y);
			}
			//aaaa
			int nodeId = BP.BPMN.Glo.NewNode(FK_Flow, iX, iY);
			BP.WF.Node node = new BP.WF.Node(nodeId);
			node.setHisRunModel(Node_GetRunModelByFigureName(figureName));
			node.Update();
			return "{\"success\":true,\"msg\":\"\",\"data\":{\"NodeID\":" + nodeId + "," + "\"text\":\"" + node.getName() + "\"}}";
		} catch (RuntimeException ex) {
			return "{\"success\":false,\"msg\":" + ex.getMessage() + ",\"data\":\"\"}";
		}
	}

	/**
	 * gen
	 * @param figureName
	 * @return
	 */
	private BP.WF.RunModel Node_GetRunModelByFigureName(String figureName) {
		BP.WF.RunModel runModel = BP.WF.RunModel.Ordinary;
		//			switch (figureName)
		//ORIGINAL LINE: case "NodeOrdinary":
		if (figureName.equals("NodeOrdinary")) {
			runModel = BP.WF.RunModel.Ordinary;
		}
		//ORIGINAL LINE: case "NodeFL":
		else if (figureName.equals("NodeFL")) {
			runModel = BP.WF.RunModel.FL;
		}
		//ORIGINAL LINE: case "NodeHL":
		else if (figureName.equals("NodeHL")) {
			runModel = BP.WF.RunModel.HL;
		}
		//ORIGINAL LINE: case "NodeFHL":
		else if (figureName.equals("NodeFHL")) {
			runModel = BP.WF.RunModel.FHL;
		}
		//ORIGINAL LINE: case "NodeSubThread":
		else if (figureName.equals("NodeSubThread")) {
			runModel = BP.WF.RunModel.SubThread;
		} else {
			runModel = BP.WF.RunModel.Ordinary;
		}
		return runModel;
	}

	/**
	 * 根据节点编号删除流程节点
	 * @return 执行结果
	 */
	private String Node_DeleteNodeOfNodeID() {
		try {
			int delResult = 0;
			String FK_Node = this.getParamter("FK_Node");
			if (isNullOrEmpty(FK_Node)) {
				return "true";
			}

			BP.WF.Node node = new BP.WF.Node(Integer.parseInt(FK_Node));
			if (node.getIsExits() == false) {
				return "true";
			}

			if (node.getIsStartNode() == true) {
				return "开始节点不允许被删除。";
			}
			delResult = node.Delete();

			if (delResult > 0) {
				return "true";
			}

			return "Delete Error.";
		} catch (RuntimeException ex) {
			return ex.getMessage();
		}
	}

	/**
	 * 修改节点名称
	 * @return
	 */
	private String Node_EditNodeName() {
		String FK_Node = this.getParamter("NodeID");

		String NodeName = this.getParamter("NodeName");
		try {
			NodeName = URLDecoder.decode(NodeName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		BP.WF.Node node = new BP.WF.Node();
		node.setNodeID(Integer.parseInt(FK_Node));
		int iResult = node.RetrieveFromDBSources();
		if (iResult > 0) {
			node.setName(NodeName);
			node.Update();
			return "true";
		}
		return "false";
	}

	/**
	 * 修改节点运行模式
	 * @return
	 */
	private String Node_ChangeRunModel() {
		String runModel = this.getParamter("RunModel");
		BP.WF.Node node = new BP.WF.Node(this.getFK_Node());
		//节点运行模式
		//			switch (runModel)
		//ORIGINAL LINE: case "NodeOrdinary":
		if (runModel.equals("NodeOrdinary")) {
			node.setHisRunModel(BP.WF.RunModel.Ordinary);
		}
		//ORIGINAL LINE: case "NodeFL":
		else if (runModel.equals("NodeFL")) {
			node.setHisRunModel(BP.WF.RunModel.FL);
		}
		//ORIGINAL LINE: case "NodeHL":
		else if (runModel.equals("NodeHL")) {
			node.setHisRunModel(BP.WF.RunModel.HL);
		}
		//ORIGINAL LINE: case "NodeFHL":
		else if (runModel.equals("NodeFHL")) {
			node.setHisRunModel(BP.WF.RunModel.FHL);
		}
		//ORIGINAL LINE: case "NodeSubThread":
		else if (runModel.equals("NodeSubThread")) {
			node.setHisRunModel(BP.WF.RunModel.SubThread);
		}
		node.Update();
		return JsonPluginsUtil.beanToJson(node);
	}

	///#endregion end Node

	public final boolean getIsReusable() {
		return false;
	}

	//----------------------------------------------------------------------------------------
	//		Copyright © 2006 - 2010 Tangible Software Solutions Inc.
	//		This class can be used by anyone provided that the copyright notice remains intact.
	//
	//		This class is used to simulate some .NET string functions in Java.
	//----------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'IsNullOrEmpty'.
	//------------------------------------------------------------------------------------
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (2 parameter version).
	//------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (4 parameter version).
	//------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray, int startindex, int count) {
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length && index - startindex < count; index++) {
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimEnd'.
	//------------------------------------------------------------------------------------
	public static String trimEnd(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					lengthToKeep = index;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimStart'.
	//------------------------------------------------------------------------------------
	public static String trimStart(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					startingIndex = index + 1;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(startingIndex);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Trim' when arguments are used.
	//------------------------------------------------------------------------------------
	public static String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	//------------------------------------------------------------------------------------
	//	This method is used for string equality comparisons when the option
	//	'Use helper 'stringsEqual' method to handle null strings' is selected
	//	(The Java String 'equals' method can't be called on a null instance).
	//------------------------------------------------------------------------------------
	public static boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}

}

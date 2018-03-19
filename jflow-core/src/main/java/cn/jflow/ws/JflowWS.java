package cn.jflow.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

@WebService
public interface JflowWS {
	@WebMethod(operationName = "Flow_DoPress")
	@WebResult(name = "result")
	public String Flow_DoPress(@WebParam(name="workid", mode = Mode.IN) long workid, @WebParam(name="msg")String msg, @WebParam(name="userNo")String userNo);

	@WebMethod(operationName = "Port_SigOut")
	@WebResult(name = "result")
	public void Port_SigOut(String userNo);

	@WebMethod(operationName = "Port_Menu")
	@WebResult(name = "result")
	public String Port_Menu(String userNo);

	@WebMethod(operationName = "Port_ChangePassword")
	@WebResult(name = "result")
	public String Port_ChangePassword(String userNo, String oldPass,
			String newPass);

	@WebMethod(operationName = "Port_SMS")
	@WebResult(name = "result")
	public String Port_SMS(String userNo, String lastTime);

	@WebMethod(operationName = "Port_SMS_DB")
	@WebResult(name = "result")
	public String Port_SMS_DB(String userNo, String lastTime);

	@WebMethod(operationName = "DB_MobileMenu")
	@WebResult(name = "result")
	public String DB_MobileMenu(String userNo);

	@WebMethod(operationName = "Address_List")
	@WebResult(name = "result")
	public String Address_List();

	@WebMethod(operationName = "UserInfoByNo")
	@WebResult(name = "result")
	public String UserInfoByNo(String userNo);

	@WebMethod(operationName = "UserInfoChange")
	@WebResult(name = "result")
	public String UserInfoChange(String userNo, String userName, String tel,
			String email);

	@WebMethod(operationName = "WriteUserMsg")
	@WebResult(name = "result")
	public String WriteUserMsg(String userNo, String msg);

	@WebMethod(operationName = "FileUploadImage")
	@WebResult(name = "result")
	public String FileUploadImage(String userNo, String bytestr,
			String smaller, String byImg);

	@WebMethod(operationName = "DB_GenerWillReturnNodes")
	@WebResult(name = "result")
	public String DB_GenerWillReturnNodes(int nodeID, long workid, long fid,
			String userNo);

	@WebMethod(operationName = "DB_FlowTree")
	@WebResult(name = "result")
	public String DB_FlowTree(String userNo, String sid);

	@WebMethod(operationName = "DB_FlowCompleteGroup")
	@WebResult(name = "result")
	public String DB_FlowCompleteGroup(String userNo, String sid);

	@WebMethod(operationName = "DB_FlowComplete")
	@WebResult(name = "result")
	public String DB_FlowComplete(String userNo, String sid, String fk_flow,
			int pageSize, int pageIdx);

	@WebMethod(operationName = "DataTable_DB_GenerWillReturnNodes")
	@WebResult(name = "result")
	public String DataTable_DB_GenerWillReturnNodes(int nodeID, long workid,
			long fid, String userNo);

	@WebMethod(operationName = "DB_TaskPool")
	@WebResult(name = "result")
	public String DB_TaskPool(String userNo);

	@WebMethod(operationName = "DB_TaskPoolOfMyApply")
	@WebResult(name = "result")
	public String DB_TaskPoolOfMyApply(String userNo);

	@WebMethod(operationName = "DB_GenerCanStartFlowsOfDataTable")
	@WebResult(name = "result")
	public String DB_GenerCanStartFlowsOfDataTable(String userNo);

	@WebMethod(operationName = "DataTable_DB_GenerCanStartFlowsOfDataTable")
	@WebResult(name = "result")
	public String DataTable_DB_GenerCanStartFlowsOfDataTable(String userNo);

	@WebMethod(operationName = "DataTable_DB_GenerEmpWorksOfDataTable")
	@WebResult(name = "result")
	public String DataTable_DB_GenerEmpWorksOfDataTable(String userNo);

	@WebMethod(operationName = "DB_GenerEmpWorksOfDataTable")
	@WebResult(name = "result")
	public String DB_GenerEmpWorksOfDataTable(String userNo);

	@WebMethod(operationName = "DB_CCList")
	@WebResult(name = "result")
	public String DB_CCList(String userNo);

	@WebMethod(operationName = "Node_DoCCCheckNote")
	@WebResult(name = "result")
	public String Node_DoCCCheckNote(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid, String msge);

	@WebMethod(operationName = "DB_Truck")
	@WebResult(name = "result")
	public String DB_Truck(String flowNo, long WorkID, long FID);

	@WebMethod(operationName = "DB_GenerHungUpList")
	@WebResult(name = "result")
	public String DB_GenerHungUpList(String userNo);

	@WebMethod(operationName = "DB_GenerRuning")
	@WebResult(name = "result")
	public String DB_GenerRuning(String userNo);

	@WebMethod(operationName = "Node_CreateBlankWork")
	@WebResult(name = "result")
	public String Node_CreateBlankWork(String flowNo, String starter,
			String title);

	@WebMethod(operationName = "Node_CC_DoDel")
	@WebResult(name = "result")
	public String Node_CC_DoDel(String mypk);

	@WebMethod(operationName = "Node_CC_SetRead")
	@WebResult(name = "result")
	public String Node_CC_SetRead(String mypks);

	@WebMethod(operationName = "Node_CC")
	@WebResult(name = "result")
	public String Node_CC(String userNo, String sid, String fk_flow,
			int fk_node, long workID, String toEmpNos, String msgTitle,
			String msgDoc, String pFlowNo, long pWorkID);

	@WebMethod(operationName = "Node_SetDraft")
	@WebResult(name = "result")
	public void Node_SetDraft(String fk_flow, long workID);

	@WebMethod(operationName = "Node_UnHungUpWork")
	@WebResult(name = "result")
	public void Node_UnHungUpWork(String fk_flow, long workid, String msg);

	@WebMethod(operationName = "Flow_DoUnSend")
	@WebResult(name = "result")
	public String Flow_DoUnSend(String fk_flow, long workid, String userNo);

	@WebMethod(operationName = "Node_HungUpWork")
	@WebResult(name = "result")
	public String Node_HungUpWork(String fk_flow, long workid, int wayInt,
			String reldata, String hungNote);

	@WebMethod(operationName = "Node_TaskPoolTakebackOne")
	@WebResult(name = "result")
	public String Node_TaskPoolTakebackOne(long workID, String userNo);

	@WebMethod(operationName = "Node_TaskPoolPutOne")
	@WebResult(name = "result")
	public String Node_TaskPoolPutOne(long workID, String userNo);

	@WebMethod(operationName = "Node_Shift")
	@WebResult(name = "result")
	public String Node_Shift(String flowNo, int nodeID, long workID, long fid,
			String toEmp, String msg, String userNo, String sid);

	@WebMethod(operationName = "Un_Node_Shift")
	@WebResult(name = "result")
	public String Un_Node_Shift(String userNo, String sid, String fk_flow,
			long workID);

	@WebMethod(operationName = "Node_ReturnWork")
	@WebResult(name = "result")
	public String Node_ReturnWork(String fk_flow, long workID, long fid,
			int currentNodeID, int returnToNodeID, String returnToEmp,
			String msg, boolean isBackToThisNode, String userNo, String sid);

	@WebMethod(operationName = "Port_Login")
	@WebResult(name = "result")
	public String Port_Login(String userNo, String pass);

	@WebMethod(operationName = "DoIt")
	@WebResult(name = "result")
	public String DoIt(String flag, String userNo, String sid, String fk_flow,
			String workID, String msg, String delModel, String val4, String val5);

	@WebMethod(operationName = "GenerFlowTrack_Josn")
	@WebResult(name = "result")
	public String GenerFlowTrack_Josn(String fk_flow, long workID, long fid,
			String userNo, String sid);

	@WebMethod(operationName = "GenerWorkNode_JSON")
	@WebResult(name = "result")
	public String GenerWorkNode_JSON(String fk_flow, int fk_node, long workID,
			long fid, String userNo, String sid);

	@WebMethod(operationName = "GenerWorkNode_JSONV2")
	@WebResult(name = "result")
	public String GenerWorkNode_JSONV2(String fk_flow, int fk_node,
			long workID, long fid, boolean isCc, float srcWidth,
			float srcHeight, String userNo, String sid);

	@WebMethod(operationName = "GetFlowTemplete")
	@WebResult(name = "result")
	public String GetFlowTemplete(String fk_flow, String fk_node, String ver);

	@WebMethod(operationName = "WorkOpt_GetToNodes")
	@WebResult(name = "result")
	public String WorkOpt_GetToNodes(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid);

	@WebMethod(operationName = "WorkOpt_SendToNodes")
	@WebResult(name = "result")
	public String WorkOpt_SendToNodes(String userNo, String sid,
			String fk_flow, int fk_node, long workID, long fid, String to_node);

	@WebMethod(operationName = "WorkOpt_AccepterDB")
	@WebResult(name = "result")
	public String WorkOpt_AccepterDB(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid);

	@WebMethod(operationName = "WorkOpt_SetAccepter")
	@WebResult(name = "result")
	public String WorkOpt_SetAccepter(String userNo, String sid, int fk_node,
			long workID, long fid, String emps, boolean isNextTime);

	@WebMethod(operationName = "Flow_DoDeleteFlowByReal")
	@WebResult(name = "result")
	public String Flow_DoDeleteFlowByReal(String userNo, String sid,
			String fk_flow, long workID, boolean isDelSubFlow);

	@WebMethod(operationName = "Node_AskforReply")
	@WebResult(name = "result")
	public String Node_AskforReply(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid, String replyNote);

	@WebMethod(operationName = "GenerFlowTemplete_Json")
	@WebResult(name = "result")
	public String GenerFlowTemplete_Json(String fk_flow);

	@WebMethod(operationName = "Node_SaveWork")
	@WebResult(name = "result")
	public String Node_SaveWork(String fk_flow, int fk_node, long workID,
			String userNo, String dsXml);

	@WebMethod(operationName = "Node_SaveWork_Json")
	@WebResult(name = "result")
	public String Node_SaveWork_Json(String fk_flow, int fk_node, long workID,
			long fid, String userNo, String sid, String jsonStr);

	@WebMethod(operationName = "Node_SendWork_Json")
	@WebResult(name = "result")
	public String Node_SendWork_Json(String fk_flow, int fk_node, long workID,
			long fid, String userNo, String sid, String jsonStr);

	@WebMethod(operationName = "Node_SendWork")
	@WebResult(name = "result")
	public String Node_SendWork(String fk_flow, int fk_node, long workID,
			String dsXml, String currUserNo);

	@WebMethod(operationName = "Node_Askfor")
	@WebResult(name = "result")
	public String Node_Askfor(String userNo, String sid, long workID,
			int _askforHelpSta, String toEmpNo, String note);

	@WebMethod(operationName = "GetCheckInfo")
	@WebResult(name = "result")
	public String GetCheckInfo(String userNo, String sid, String fk_flow,
			int fk_node, long workID);

	@WebMethod(operationName = "WriteTrackWorkCheck")
	@WebResult(name = "result")
	public String WriteTrackWorkCheck(String userNo, String sid, String flowNo,
			int nodeFrom, long workid, long fid, String msg, String optionName);

	@WebMethod(operationName = "CCFrom_DelFrmAttachment")
	@WebResult(name = "result")
	public String CCFrom_DelFrmAttachment(String userNo, String sid, String mypk);

	@WebMethod(operationName = "CCForm_UploadFrmAttachment")
	@WebResult(name = "result")
	public String CCForm_UploadFrmAttachment(String userNo, String sid,
			String fk_frmath, byte[] intoBuffer);

	@WebMethod(operationName = "CCForm_FrmTemplete")
	@WebResult(name = "result")
	public String CCForm_FrmTemplete(String fk_mapdata);

	@WebMethod(operationName = "CCFlow_FlowTemplete")
	@WebResult(name = "result")
	public String CCFlow_FlowTemplete(String fk_flow);

	// / <summary>
	// / 让他登录
	// / </summary>
	// / <param name="user"></param>
	// / <param name="sid"></param>
	@WebMethod(operationName = "LetUserLogin")
	@WebResult(name = "result")
	public void LetUserLogin(String user, String sid);
}

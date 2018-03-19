package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;

public class TransferCustomModel extends BaseModel{

	public TransferCustomModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public String ClientID;
	public int FK_Node;
	public String FK_Flow;
	public long WorkID;
	public long FID;
	public String fk_NodeText;
	public String fk_CCText;	
	public String fk_CC;
	public String toDoListModel;
	public String idx;
	public String worker;
	public String subFlowNo;
	public String subFlowName;
	public String workerText;
	public String getClientID() {
		return ClientID;
	}
	public void setClientID(String clientID) {
		ClientID = clientID;
	}
	public int getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
	public long getWorkID() {
		return WorkID;
	}
	public void setWorkID(long workID) {
		WorkID = workID;
	}
	public long getFID() {
		return FID;
	}
	public void setFID(long fID) {
		FID = fID;
	}
	public String getFk_NodeText() {
		return fk_NodeText;
	}
	public void setFk_NodeText(String fk_NodeText) {
		this.fk_NodeText = fk_NodeText;
	}
	public String getFk_CCText() {
		return fk_CCText;
	}
	public void setFk_CCText(String fk_CCText) {
		this.fk_CCText = fk_CCText;
	}
	public String getFk_CC() {
		return fk_CC;
	}
	public void setFk_CC(String fk_CC) {
		this.fk_CC = fk_CC;
	}
	public String getToDoListModel() {
		return toDoListModel;
	}
	public void setToDoListModel(String toDoListModel) {
		this.toDoListModel = toDoListModel;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getSubFlowNo() {
		return subFlowNo;
	}
	public void setSubFlowNo(String subFlowNo) {
		this.subFlowNo = subFlowNo;
	}
	public String getSubFlowName() {
		return subFlowName;
	}
	public void setSubFlowName(String subFlowName) {
		this.subFlowName = subFlowName;
	}
	public String getWorkerText() {
		return workerText;
	}
	public void setWorkerText(String workerText) {
		this.workerText = workerText;
	}
	
	
}

package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.XML.EventList;
import BP.WF.XML.EventLists;
import BP.WF.XML.EventSource;
import BP.WF.XML.EventSources;
import cn.jflow.common.model.BaseModel;

public class ActionModel {

	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Pub2 = new StringBuilder();

	private String basePath;
	private HttpServletRequest _request = null;

	// private HttpServletResponse _response = null;
	public ActionModel(HttpServletRequest request,
			HttpServletResponse response, String basePath) {
		this.basePath = basePath;
		this._request = request;
		// this._response = response;
	}

	public String getEvent() {
		if (_request.getParameter("Event") == null)
			return "";
		return _request.getParameter("Event");
	}

	private String Title = "";

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}
	public final String getShowType() {
		if (!"".equals(this.getNodeID())&&!this.getNodeID().equals("0")) {
			return "Node";
		}


		if (this.getNodeID().equals("0") && StringHelper.isNullOrEmpty(this.getFK_Flow()) == false && this.getFK_Flow().length() >= 3) {
			return "Flow";
		}

		if (this.getNodeID().equals("0") && StringHelper.isNullOrEmpty(this.getFK_MapData())==false) {
			return "Frm";
		}

		return "Node";
	}

	/**
	 * 当前事件设置的名称
	 */
	private String CurrentEvent;

	public String getCurrentEvent() {
		return CurrentEvent;
	}

	public void setCurrentEvent(String value) {
		this.CurrentEvent = value;
	}

	/**
	 * 当前事件所属事件源名称
	 */
	private String CurrentEventGroup;

	public String getCurrentEventGroup() {
		return CurrentEventGroup;
	}

	public void setCurrentEventGroup(String value) {
		this.CurrentEventGroup = value;
	}

	private boolean HaveMsg;

	public boolean getHaveMsg() {
		return HaveMsg;
	}

	public void setHaveMsg(boolean value) {
		this.HaveMsg = value;
	}

	public String getNodeID() {
		if (_request.getParameter("NodeID") == null)
			return "";
		return _request.getParameter("NodeID");
	}

	public String getFK_MapData() {
		String fk_mapdata = _request.getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata)) {
			fk_mapdata = "ND" + this.getNodeID();
		}
		return fk_mapdata;
	}

	public String getDoType() {
		if (_request.getParameter("DoType") == null)
			return "";
		return _request.getParameter("DoType");
	}

	public String getFK_Flow() {
		if (_request.getParameter("FK_Flow") == null)
			return "";
		return _request.getParameter("FK_Flow");
	}

	public String getMyPK() {
		if (_request.getParameter("MyPK") == null)
			return "";
		return _request.getParameter("MyPK");
	}

	public void init() {
		if (!"".equals(this.getDoType()) && "Del".equals(this.getDoType())) {
			FrmEvent delFE = new FrmEvent();
			delFE.setMyPK(this.getFK_MapData() + "_"
					+ _request.getParameter("RefXml"));
			delFE.Delete();
		}

		FrmEvents ndevs = new FrmEvents();
		ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());

		EventLists xmls = new EventLists();
		xmls.RetrieveAll();

		EventSources ess = new EventSources();
		ess.RetrieveAll();

		String myEvent = this.getEvent();
		EventList myEnentXml = null;

		
		// /#region //生成事件列表

		for (EventSource item : ess.ToJavaList()) {
			
			if (!this.getShowType().equals(item.getNo())) {
				continue;
			}

			this.Pub1
					.append(String
							.format("<div title='%1$s' style='padding:10px; overflow:auto' data-options=''>",
									item.getName()));
			this.Pub1.append(BaseModel.AddUL("class='navlist'"));
			String msg = "";

			for (EventList xml : xmls.ToJavaList()) {
				if (!xml.getEventType().equals(item.getNo())) {
					continue;
				}
				msg = "";
				if (xml.getIsHaveMsg() == true) {
					msg = "<img src='"+Glo.getCCFlowAppPath()+"WF/Img/Msg.png' />";
				}


				Object tempVar = ndevs.GetEntityByKey(FrmEventAttr.FK_Event,
						xml.getNo());
				FrmEvent nde = (FrmEvent) ((tempVar instanceof FrmEvent) ? tempVar
						: null);

				if (nde == null) {
					if (myEvent != null && myEvent.equals(xml.getNo())) {
						this.setCurrentEventGroup(item.getName());
						myEnentXml = xml;
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div style='font-weight:bold'><a href='javascript:void(0)'><span class='nav'><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Event.png' border=0/>" + msg + "%1$s</span></a></div>%2$s",
												xml.getName(), "\r\n")));
					} else {
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div><a href='"
												+ basePath
												+ "WF/Admin/AttrNode/Action.jsp?NodeID=%1$s&Event=%2$s&FK_Flow=%3$s&tk=%6$s&FK_MapData=%7$s'><span class='nav'><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Event.png' border=0/>" + msg + "%4$s</span></a></div>%5$s",
												this.getNodeID(), xml.getNo(),
												this.getFK_Flow(), xml
														.getName(), "\r\n",
												new java.util.Random().nextDouble(),
												this.getFK_MapData())));
					}
				} else {
					if (myEvent != null && myEvent.equals(xml.getNo())) {
						setCurrentEventGroup(item.getName());
						myEnentXml = xml;
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div style='font-weight:bold'><a href='javascript:void(0)'><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Event.png' border=0/>" + msg + "%1$s</span></a></div>%2$s",
												xml.getName(), "\r\n")));
					} else {
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div><a href='"
												+ basePath
												+ "WF/Admin/AttrNode/Action.jsp?NodeID=%1$s&Event=%2$s&FK_Flow=%3$s&MyPK=%4$s&tk=%7$s&FK_MapData=%8$s'><span class='nav'><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Event.png' border=0/>" + msg + "%5$s</span></a></div>%6$s",
												this.getNodeID(), xml.getNo(),
												this.getFK_Flow(), nde
														.getMyPK(), xml
														.getName(), "\r\n",
												new java.util.Random()
														.nextDouble(),
														this.getFK_MapData())));
					}
				}
			}

			this.Pub1.append(BaseModel.AddULEnd());
			this.Pub1.append(BaseModel.AddDivEnd());
		}
		
		// /#endregion

		if (myEnentXml == null) {
			this.setCurrentEvent("帮助");

			this.Pub2
					.append("<div style='width:100%; text-align:left' data-options='noheader:true'>");
			this.Pub2.append(BaseModel.AddH2("事件是ccbpm与您的应用程序接口"));
			
			
			
			
			
			if (!"0".equals(this.getNodeID())) {
				this.Pub2.append(BaseModel.AddFieldSet("节点事件"));
				this.Pub2.append(BaseModel.AddUL());
				this.Pub2.append(BaseModel.AddLi("流程在运动过程中，有许多的事件，比如节点发送前、发送成功后、发送失败后、退回前、退回后、撤销发送前、这小发送后、流程结束前、结束后、删除前删除后。"));
				this.Pub2.append(BaseModel.AddLi("ccbpm把事件分为流程事件与节点事件，流程属性里定义流程事件，节点属性里定义节点事件。"));
				this.Pub2.append(BaseModel.AddLi("在这些事件里ccbpm允许调用您编写的业务逻辑，完成与界面交互、与其他系统交互、与其他流程参与人员交互。"));
				this.Pub2.append(BaseModel.AddLi("按照事件发生的类型，ccbpm把事件分为：节点、表单、流程三类的事件。"));
				this.Pub2.append(BaseModel.AddLi("在BPMN2.0规范里没有定义节点事件表单事件，这是ccbpm特有的概念与元素。"));
				this.Pub2.append(BaseModel.AddULEnd());
				this.Pub2.append(BaseModel.AddFieldSetEnd());
			}

			if (StringHelper.isNullOrEmpty(this.getFK_Flow()) == false && "0".equals(this.getNodeID())) {
				this.Pub2.append(BaseModel.AddFieldSet("流程事件"));
				this.Pub2.append(BaseModel.AddUL());
				this.Pub2.append(BaseModel.AddLi("流程在运动过程中，有许多的事件，比如节点发送前、发送成功后、发送失败后、退回前、退回后、撤销发送前、这小发送后、流程结束前、结束后、删除前删除后。"));
				this.Pub2.append(BaseModel.AddLi("ccbpm把事件分为流程事件与节点事件，流程属性里定义流程事件，节点属性里定义节点事件。"));
				this.Pub2.append(BaseModel.AddLi("在这些事件里ccbpm允许调用您编写的业务逻辑，完成与界面交互、与其他系统交互、与其他流程参与人员交互。"));
				this.Pub2.append(BaseModel.AddLi("按照事件发生的类型，ccbpm把事件分为：节点、表单、流程三类的事件。"));
				this.Pub2.append(BaseModel.AddLi("在BPMN2.0规范里定义了，流程发起事件，流程发起错误事件。在ccbpm里取消了这些概念，取而代之的是开始节点的发送前、发送失败时、发送成功时的事件与之对应。"));
				this.Pub2.append(BaseModel.AddULEnd());
				this.Pub2.append(BaseModel.AddFieldSetEnd());
			}

			if (this.getFK_MapData() != null && StringHelper.isNullOrEmpty(this.getFK_MapData())==false) {
				this.Pub2.append(BaseModel.AddFieldSet("表单事件"));
				this.Pub2.append(BaseModel.AddUL());
				this.Pub2.append(BaseModel.AddLi("流程在运动过程中，有许多的事件，比如节点发送前、发送成功后、发送失败后、退回前、退回后、撤销发送前、这小发送后、流程结束前、结束后、删除前删除后。"));
				this.Pub2.append(BaseModel.AddLi("ccbpm把事件分为流程事件与节点事件，流程属性里定义流程事件，节点属性里定义节点事件。"));
				this.Pub2.append(BaseModel.AddLi("在这些事件里ccbpm允许调用您编写的业务逻辑，完成与界面交互、与其他系统交互、与其他流程参与人员交互。"));
				this.Pub2.append(BaseModel.AddLi("按照事件发生的类型，ccbpm把事件分为：节点、表单、流程三类的事件。"));
				this.Pub2.append(BaseModel.AddLi("在BPMN2.0规范里定义了，流程发起事件，流程发起错误事件。在ccbpm里取消了这些概念，取而代之的是开始节点的发送前、发送失败时、发送成功时的事件与之对应。"));
				this.Pub2.append(BaseModel.AddULEnd());
				this.Pub2.append(BaseModel.AddFieldSetEnd());
			}



			/*this.Pub2.append(BaseModel.AddUL());
			this.Pub2.append(BaseModel
					.AddLi("流程在运动的过程中会产生很多的事件，比如：节点发送前、发送成功时、发送失败时、退回前、退后后。"));
			this.Pub2
					.append(BaseModel
							.AddLi("在这些事件里ccflow允许调用您编写的业务逻辑，完成与界面交互、与其他系统交互、与其他流程参与人员交互。"));
			this.Pub2.append(BaseModel
					.AddLi("按照事件发生的类型，ccflow把事件分为：节点、表单、流程三类的事件。"));
			this.Pub2.append(BaseModel.AddULEnd());*/

			this.Pub2.append(BaseModel.AddDivEnd());
			return;
		}

		Object tempVar2 = ndevs.GetEntityByKey(FrmEventAttr.FK_Event, myEvent);
		FrmEvent mynde = (FrmEvent) ((tempVar2 instanceof FrmEvent) ? tempVar2
				: null);
		if (mynde == null) {
			mynde = new FrmEvent();
			mynde.setFK_Event(myEvent);
		}

		this.setTitle("");
		this.setCurrentEvent(myEnentXml == null ? "" : myEnentXml.getName());
		int col = 50;

		this.Pub2
				.append("<div id='tabMain' class='easyui-tabs' data-options='fit:true'>");

		this.Pub2.append("<div title='事件接口' style='padding:5px'>" + "\r\n");
		this.Pub2
				.append("<iframe id='src1' frameborder='0' src='' style='width:100%;height:100%' scrolling='auto'></iframe>");
		this.Pub2.append("</div>" + "\n");

		/*  该模块jflow暂时不翻译，注释掉  by fanleiwei 20160531
		if (myEnentXml != null && myEnentXml.getIsHaveMsg()) {
			setHaveMsg(true);
			this.Pub2.append("<div title='向当事人推送消息' style='padding:5px'>"
					+ "\r\n");
			this.Pub2
					.append("<iframe id='src2' frameborder='0' src='' style='width:100%;height:100%' scrolling='auto'></iframe>");
			this.Pub2.append("</div>" + "\n");

			this.Pub2.append("<div title='向其他指定的人推送消息' style='padding:5px'>"
					+ "\r\n");
			this.Pub2
					.append("<iframe id='src3' frameborder='0' src='' style='width:100%;height:100%' scrolling='auto'></iframe>");
			this.Pub2.append("</div>" + "\n");
		}
		 */
		
		// BP.WF.Dev2Interface.Port_Login("zhoupeng");

		// BP.WF.Dev2Interface.Port_SigOut();

		this.Pub2.append("</div>");
	}
}

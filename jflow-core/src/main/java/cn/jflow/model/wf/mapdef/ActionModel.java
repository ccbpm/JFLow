package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.Sys.SystemConfig;
import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Sys.MapDtl;
import BP.WF.Glo;
import BP.WF.XML.EventListDtl;
import BP.WF.XML.EventListDtls;
import BP.XML.XmlEn;

/**
 * 
 * @author ly
 * @date 20150430 New Add 
 */
public class ActionModel extends BaseModel {

	public StringBuilder pub3;
	public StringBuilder pub1;
	public StringBuilder pub2;
	public String title;

	public ActionModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		pub3 = new StringBuilder();
		pub1 = new StringBuilder();
		pub2 = new StringBuilder();
	}

	public final String getEvent() {
		return getParameter("Event");
	}

	public final String getFK_MapData() {
		return getParameter("FK_MapData");
	}

	
	public final void page_Load() {
		
		String mypk = getParameter("MyPK");
		String doType = getParameter("DoType");
		String refXml = getParameter("RefXml");
		String fk_MapData = getParameter("FK_MapData");
		
		// if (doType.equals("Del")
		if ("Del".equals(doType)) {
			FrmEvent delFE = new FrmEvent();
			delFE.setMyPK(fk_MapData + "_" + refXml);
			delFE.Delete();
		}
		
		MapDtl dtl = new MapDtl(fk_MapData);
		pub3.append(AddCaptionLeft("从表:" + dtl.getName()));
		
		title = "设置:从表事件";
		
		FrmEvents ndevs = new FrmEvents();
		ndevs.Retrieve(FrmEventAttr.FK_MapData, fk_MapData);
		
		EventListDtls xmls = new EventListDtls();
		xmls.RetrieveAll();
		
		String myEvent = this.getEvent();
		EventListDtl myEnentXml = null;
		
		this.pub1.append("<a href='http://ccflow.org' target=_blank ><img src='"+Glo.getCCFlowAppPath()+"DataUser/ICON/"
						+ SystemConfig.getCompanyID() + "/LogBiger.png' /></a>");
		this.pub1.append(AddUL());
		
		for (XmlEn obj : xmls.ToJavaList()) {
			
			EventListDtl xml = (EventListDtl) obj;
			Object tempVar = ndevs.GetEntityByKey(FrmEventAttr.FK_Event, xml.getNo());
			FrmEvent nde = (FrmEvent) ((tempVar instanceof FrmEvent) ? tempVar : null);
			
			if (nde == null) {
				if (xml.getNo().equals(myEvent)) {
					myEnentXml = xml;
					this.pub1.append(AddLi("<font color=green><b>" + xml.getName() + "</b></font>"));
				} else {
					this.pub1.append(AddLi("Action.jsp?FK_MapData=" + fk_MapData + "&Event=" + xml.getNo(), xml.getName()));
				}
				
			} else {
				if (xml.getNo().equals(myEvent)) {
					myEnentXml = xml;
					this.pub1.append(AddLi("<font color=green><b>" + xml.getName() + "</b></font>"));
				} else {
					this.pub1.append(AddLi("Action.jsp?FK_MapData=" + this.getFK_MapData() + "&Event=" + xml.getNo() + "&MyPK=" + nde.getMyPK(), "<b>" + xml.getName() + "</b>"));
				}
			}
		}
		
		this.pub1.append(AddULEnd());

		if (myEnentXml == null) {
			this.pub2.append(AddFieldSet("帮助"));
			this.pub2.append(AddH2("事件是ccflow与您的应用程序接口，"));
			this.pub2.append(AddFieldSetEnd());
			return;
		}
		
		Object tempVar2 = ndevs.GetEntityByKey(FrmEventAttr.FK_Event, myEvent);
		FrmEvent mynde = (FrmEvent) ((tempVar2 instanceof FrmEvent) ? tempVar2 : null);
		if (mynde == null) {
			mynde = new FrmEvent();
		}
		this.pub2.append(AddFieldSet(myEnentXml.getName()));
		this.pub2.append("要执行的内容<br>");
		
		TextBox tb = new TextBox();
		tb.setId("TB_Doc");
		tb.setCols(70);
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setRows(5);
		tb.setText(mynde.getDoDoc());
		
		this.pub2.append(tb);
		this.pub2.append(AddHR());
		this.pub2.append("内容类型:");
		
		DDL ddl = new DDL();
		ddl.BindSysEnum("EventDoType");
		ddl.setId("DDL_EventDoType");
		ddl.SetSelectItem(mynde.getHisDoType().getValue());
		
		this.pub2.append(ddl);
		this.pub2.append(AddHR());
		
		tb = new TextBox();
		tb.setId("TB_MsgOK");
		tb.setCols(70);
		tb.setText(mynde.getMsgOKString());
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setRows(3);
		
		this.pub2.append("执行成功信息提示<br>");
		this.pub2.append(tb);
		this.pub2.append(AddHR());
		this.pub2.append("执行失败信息提示<br>");
		
		tb = new TextBox();
		tb.setId("TB_MsgErr");
		tb.setCols(70);
		tb.setText(mynde.getMsgErrorString());
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setRows(3);
		
		this.pub2.append(tb);
		this.pub2.append(AddFieldSetEnd());
		
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		btn.setText("  Save  ");
		btn.addAttr("onclick", "btn_Click()");
		
		this.pub2.append(btn);
		
		if (mypk != null) {
			this.pub2.append("&nbsp;&nbsp;<a href=\"javascript:DoDel('" + fk_MapData + "','" + this.getEvent() + "')\"><img src='../Img/Btn/Delete.gif' />删除</a>");
		}
	}
}

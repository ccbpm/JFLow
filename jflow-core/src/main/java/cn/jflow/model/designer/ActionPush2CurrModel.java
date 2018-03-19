package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class ActionPush2CurrModel extends BaseModel{

	public StringBuffer Pub1=null;

	public ActionPush2CurrModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuffer();
	}

	public String Event;

	public String getEvent() {
		return this.get_request().getParameter("Event");
	}

	public void setEvent(String event) {
		Event = event;
	}


	public void init()
	{
		FrmEvents ndevs = new FrmEvents();
		ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());

		FrmEvent mynde = (FrmEvent)ndevs.GetEntityByKey(FrmEventAttr.FK_Event, this.getEvent());

		if (mynde == null)
		{
			mynde = new FrmEvent();
			mynde.setFK_Event(this.Event);
		}

		this.Pub1.append(AddTable("class='Table' cellspacing='1' cellpadding='1' border='1' style='width:100%'"));

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("控制方式"));
		DDL ddl = new DDL();
		ddl.BindSysEnum("MsgCtrl");
		ddl.setId("DDL_" + FrmEventAttr.MsgCtrl);
		ddl.setName("DDL_" + FrmEventAttr.MsgCtrl);
		ddl.SetSelectItem(mynde.getMsgCtrl().getValue());
		this.Pub1.append(AddTD(ddl));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD(""));
		CheckBox cb = new CheckBox();
		cb.setId("CB_" + FrmEventAttr.MailEnable);
		cb.setName("CB_" + FrmEventAttr.MailEnable);
		cb.setText("是否启用邮件通知？");
		cb.setChecked(mynde.getMailEnable());
		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("邮件标题模版"));
		TextBox tb = new TextBox();
		tb.setId("TB_" + FrmEventAttr.MailTitle);
		tb.setName("TB_" + FrmEventAttr.MailTitle);
		tb.setText(mynde.getMailTitle_Real());
		tb.addAttr("width", "99%");
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("邮件内容模版:"));
		tb = new TextBox();
		tb.setId("TB_" + FrmEventAttr.MailDoc);
		tb.setName("TB_" + FrmEventAttr.MailDoc);
		tb.setText(mynde.getMailDoc_Real());
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setCols(50);
		tb.setRows(4);
		tb.addAttr("width", "99%");
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTREnd());

		//手机短信....
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("默认:不启用"));
		cb = new CheckBox();
		cb.setId("CB_" + FrmEventAttr.SMSEnable);
		cb.setName("CB_" + FrmEventAttr.SMSEnable);
		cb.setText("是否启用手机短信通知？");
		cb.setChecked(mynde.getSMSEnable());
		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTREnd());

		tb = new TextBox();
		tb.setId("TB_" + FrmEventAttr.SMSDoc);
		tb.setName("TB_" + FrmEventAttr.SMSDoc);
		tb.setText(mynde.getSMSDoc_Real());
		tb.addAttr("width", "99%");
		tb.setRows(2);
		this.Pub1.append(AddTR());
		if (StringHelper.isNullOrEmpty(tb.getText()) == true)
			this.Pub1.append(AddTD("短信模版:"));
		else
			this.Pub1.append(AddTD("短信模版"));

		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("默认:启用"));
		cb = new CheckBox();
		cb.setId("CB_" + FrmEventAttr.MobilePushEnable);
		cb.setName("CB_" + FrmEventAttr.MobilePushEnable);
		cb.setText("是否启用手机应用，平板应用信息推送？");
		cb.setChecked(mynde.getMobilePushEnable());
		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());

		Pub1.append(AddBR());
		Pub1.append(AddSpace(1));

		LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
		btn.addAttr("onclick", "onSave()");
		//btn.Click += new EventHandler(btn_Click);
		Pub1.append(btn);
	}

//	void btn_Click(object sender, EventArgs e)
//	{
//		FrmEvent fe = new FrmEvent();
//		fe.MyPK = this.FK_MapData + "_" + this.Event;
//		fe.RetrieveFromDBSources();
//
//		fe = (FrmEvent)this.Pub1.Copy(fe);
//		fe.Save();
//
//		//var pm = new PushMsg();
//		//pm.Retrieve(PushMsgAttr.FK_Event, this.Event, PushMsgAttr.FK_Node, this.NodeID);
//
//		this.Response.Redirect("ActionPush2Curr.aspx?NodeID=" + this.NodeID + "&MyPK=" + fe.MyPK + "&Event=" + this.Event + "&tk=" + new Random().NextDouble(), true);
//	}
}

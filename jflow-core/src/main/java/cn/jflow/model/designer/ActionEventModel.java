package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class ActionEventModel {

	//private String basePath;
	private HttpServletRequest _request = null;
	//private HttpServletResponse _response = null;
	
	public UiFatory Pub1 = null;
	public ActionEventModel(HttpServletRequest request,
			HttpServletResponse response , String basePath) {
		//this.basePath = basePath;
		this._request = request;
		// this._response = response;
		
		this.Pub1 = new UiFatory();
	}

	public String getEvent() {
		if (_request.getParameter("Event") == null)
			return "";
		return _request.getParameter("Event");
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
		FrmEvents ndevs = new FrmEvents();
		ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());

		Object tempVar = ndevs.GetEntityByKey(FrmEventAttr.FK_Event,
				this.getEvent());
		FrmEvent mynde = (FrmEvent) ((tempVar instanceof FrmEvent) ? tempVar
				: null);
		if (mynde == null) {
			mynde = new FrmEvent();
			mynde.setFK_Event(this.getEvent());
		}

		this.Pub1
				.append(BaseModel
						.AddTable("class='Table' cellspacing='1' cellpadding='1' border='1' style='width:100%'"));

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("内容类型:"));
		DDL ddl = this.Pub1.creatDDL("DDL_EventDoType");
		ddl.BindSysEnum("EventDoType");
		ddl.SetSelectItem(mynde.getHisDoType().getValue());
		this.Pub1.append("\n<TD nowrap = 'nowrap'>");
		this.Pub1.append(ddl);
		this.Pub1.append("</TD>");
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDBegin("colspan=2"));
		this.Pub1.append("&nbsp;要执行的内容<br>");
		TextBox tb1 = this.Pub1.creatTextBox("TB_Doc");
		tb1.setCols(50);
		tb1.addAttr("style", "width:99%");
		tb1.setTextMode(TextBoxMode.MultiLine);
		tb1.setRows(4);
		tb1.setText(mynde.getDoDoc());
		this.Pub1.append(tb1);
		this.Pub1.append(BaseModel.AddTDEnd());
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDBegin("colspan=2"));
		TextBox tb2 = this.Pub1.creatTextBox("TB_MsgOK");
		tb2.addAttr("style", "width:99%");
		tb2.setText(mynde.getMsgOKString());
		this.Pub1.append("执行成功信息提示(可为空)<br>");
		this.Pub1.append(tb2);
		this.Pub1.append(BaseModel.AddTDEnd());
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDBegin("colspan=2"));
		this.Pub1.append("执行失败信息提示(可为空)<br>");
		TextBox tb3 = this.Pub1.creatTextBox("TB_MsgErr");
		tb3.addAttr("style", "width:99%");
		tb3.setText(mynde.getMsgErrorString());
		this.Pub1.append(tb3);
		this.Pub1.append(BaseModel.AddTDEnd());
		this.Pub1.append(BaseModel.AddTREnd());
		this.Pub1.append(BaseModel.AddTableEnd());
		this.Pub1.append(BaseModel.AddBR());
		this.Pub1.append(BaseModel.AddSpace(1));

		LinkButton btn = this.Pub1.creatLinkButton(false,NamesOfBtn.Save.getCode(),"保存");
//		btn.setText("保存");
		btn.addAttr("onclick", "onSave()");
		this.Pub1.append(btn);

		if (!StringHelper.isNullOrEmpty(this.getMyPK())) {
			this.Pub1.append(BaseModel.AddSpace(1));
			this.Pub1
					.append(String
							.format("<a href='javascript:void(0)' onclick=\"DoDel('%1$s','%2$s')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-delete'\">删除</a>",
									this.getNodeID(), this.getEvent()));
		}
	}
}

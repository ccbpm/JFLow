package cn.jflow.model.wf.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.Flows;
import BP.WF.GetTask;
import BP.WF.GetTasks;
import BP.WF.Template.NodeAttr;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;

public class GetTaskModel extends BaseModel{
	public UiFatory Pub1 = null;
	public GetTaskModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.Pub1 = new UiFatory();
	}
	public final int getFK_Node() {
		String step= get_request().getParameter("FK_Node");
		if("".equals(step)||step==null){
			return 0;
		}else{
			return Integer.parseInt(step);
		}
	}
	public final int getStep() {
		String step= get_request().getParameter("Step");
		if("".equals(step)||step==null){
			return 1;
		}else{
			return Integer.parseInt(step);
		}
	}

	public void page_load(){
		if (this.getRefNo() == null) {
			this.BindList();
			return;
		}

		if (this.getStep() == 1) {
			this.BindStep1();
			return;
		}

		if (this.getStep() == 2) {
			this.BindStep1();
			return;
		}

		if (this.getStep() == 3) {
			this.BindStep3();
			return;
		}

	}
	public final void BindStep1() {
		this.Pub1.append(AddTable("width='98%'"));
		this.Pub1.append(AddCaptionLeft("设置流程条跳转审批规则:<a href='GetTask.jsp'>返回</a> "));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("序"));
		this.Pub1.append(AddTDTitle("节点编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("步骤"));

		this.Pub1.append(AddTDTitle("可跳转审核的节点"));

		this.Pub1.append(AddTDTitle("操作"));
		this.Pub1.append(AddTREnd());

		BP.WF.GetTasks jcs = new GetTasks();
		jcs.Retrieve(NodeAttr.FK_Flow, this.getRefNo());

		int idx = 0;
		for	(int i=0;i<jcs.size();i++){
			GetTask jc=(GetTask) jcs.get(i);
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD(jc.getNodeID()));
			this.Pub1.append(AddTD(jc.getName()));
			this.Pub1.append(AddTD(jc.getStep()));
			this.Pub1.append(AddTD(jc.getCheckNodes()));

			if (jc.getStep() == 1 || jc.getStep() == 2) {
				this.Pub1.append(AddTD());
			}
			else {
				switch (jc.getHisDeliveryWay()) {
					case ByBindEmp:
					case ByStation:
						if (jc.getCheckNodes().length()> 2) {
							this.Pub1.append(AddTD("<a href=\"javascript:EditIt('" + this.getRefNo() + "','" + jc.getNodeID() + "');\" >编辑</a>"));
						}
						else {
							this.Pub1.append(AddTD("<a href=\"javascript:EditIt('" + this.getRefNo() + "','" + jc.getNodeID() + "');\" >创建</a>"));
						}
						break;
					default:
						this.Pub1.append(AddTD("访问规则非按人员或按部门"));
						break;
				}
			}
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());
	}
	public final void BindStep3() {
		this.Pub1.append(AddTable("width='98%'"));
		this.Pub1.append(AddCaptionLeft("选择可以跳转审核的节点"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("序"));
		this.Pub1.append(AddTDTitle("节点编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("步骤"));
		this.Pub1.append(AddTDTitle("选择"));
		this.Pub1.append(AddTREnd());

		BP.WF.GetTasks jcs = new GetTasks();
		jcs.Retrieve(NodeAttr.FK_Flow, this.getRefNo());

		BP.WF.GetTask myjc = new GetTask(this.getFK_Node());
		String nodes = myjc.getCheckNodes();

		int idx = 0;
		for	(int i=0;i<jcs.size();i++){
			GetTask jc=(GetTask) jcs.get(i);
			if (jc.getStep() == 1) {
				continue;
			}

			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD(jc.getNodeID()));
			this.Pub1.append(AddTD(jc.getName()));
			this.Pub1.append(AddTD(jc.getStep()));

			CheckBox cb = new CheckBox();
			cb.setId("CB_" + jc.getNodeID());
			cb.setValue(String.valueOf(jc.getNodeID()));
			cb.setText("选择");
			if (nodes.contains(String.valueOf(jc.getNodeID()))) {
				cb.setChecked(true);
				cb.setText("<font color=green>" + jc.getName() + "</font>");
			}
			else {
				cb.setChecked(false);
			}

			this.Pub1.append(AddTD(cb));
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTR());
		this.Pub1.append(Add("<TD colspan=5>"));

		Button btn = new Button();
		btn.setText("保存配置");
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "btn_Click('ok')");
		this.Pub1.append(Add(btn));
		
		btn = new Button();
		btn.setText("删除配置");
		btn.setId("Btn_Del");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "btn_Click('del')");
		this.Pub1.append(Add(btn));

		btn = new Button();
		btn.setText("关闭");
		btn.setId("Btn_Cancel");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "btn_Click('cancel')");
		this.Pub1.append(Add(btn));

		this.Pub1.append(Add("</TD>"));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEndWithHR());

	}


	public final void BindList() {
		Flows fls = new Flows();
		fls.RetrieveAll();
		this.Pub1.append(AddTable("width='99%'"));
		this.Pub1.append(AddCaptionLeft("设置流程条跳转审批规则"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("序"));
		this.Pub1.append(AddTDTitle("流程类别"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("流程图"));
		this.Pub1.append(AddTDTitle("描述"));
		this.Pub1.append(AddTREnd());
		int i = 0;
		boolean is1 = false;
		String fk_sort = "";
		for (int j=0;j<fls.size();j++) {
			Flow fl=(Flow) fls.get(j);
			if (fl.getFlowAppType() == FlowAppType.DocFlow) {
				continue;
			}
			i++;
			//is1 = ;
			this.Pub1.append(AddTR(is1));
			this.Pub1.append(AddTDIdx(i));
			if (fk_sort.equals(fl.getFK_FlowSort())) {
				this.Pub1.append(AddTD());
			}
			else {
				this.Pub1.append(AddTDB(fl.getFK_FlowSortText()));
			}

			fk_sort = fl.getFK_FlowSort();
			this.Pub1.append(AddTD("<a href='GetTask.jsp?RefNo=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01' >" + fl.getName() + "</a>"));

			this.Pub1.append(AddTD("<a href=\"javascript:WinOpen('../Admin/CCFlowDesigner/Truck.html?FK_Flow=" + fl.getNo() + "&DoType=Chart','sd');\"  >打开</a>"));
			this.Pub1.append(AddTD(fl.getNote()));
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTRSum());
		this.Pub1.append(AddTD("colspan=" + 5, "&nbsp;"));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
	}

}

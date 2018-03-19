package cn.jflow.model.wf.rpt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.GenerWorkFlow;
import BP.WF.HungUpWay;
import BP.WF.WFState;
import BP.WF.Template.HungUp;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TB;
import cn.jflow.system.ui.core.TextBoxMode;

public class HungUpModel extends BaseModel {

	public HungUpModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public UiFatory Pub1 = null;
	public final String getFK_Flow() {
		return get_request().getParameter("FK_Flow");
	}
	public final int getFK_Node() {
		return Integer.parseInt(get_request().getParameter("FK_Node"));
	}
	public final long getWorkID() {
		return Long.parseLong(get_request().getParameter("WorkID"));
	}
	public final long getFID() {
		try {
			return Long.parseLong(get_request().getParameter("FID"));
		}
		catch (java.lang.Exception e) {
			return 0;
		}
	}


	public void init(){
		Pub1=new UiFatory();
		HungUp hu = new HungUp();
		hu.setMyPK(this.getFK_Node() + "_" + this.getWorkID());
		int i = hu.RetrieveFromDBSources();

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		if (gwf.RetrieveFromDBSources() == 0) {
			this.Pub1.append(AddFieldSet("错误","当前是开始节点，或者工作不存在."));
			return;
		}

		this.Pub1.append(AddFieldSet("对工作<b>(" + gwf.getTitle() + ")</b>挂起方式"));
		RadioButton rb = new RadioButton();
		rb.setGroupName("s");
		rb.setText("永久挂起");
		rb.setValue("RB_HungWay0");
		rb.setId( "RB_HungWay0");
		if (hu.getHungUpWay().getValue() == 0) {
			rb.setChecked(true);
		}
		else {
			rb.setChecked(false);
		}

		this.Pub1.append(Add(rb));
		this.Pub1.append(AddBR());

		rb = new RadioButton();
		rb.setGroupName("s");
		rb.setText("在指定的日期自动解除挂起.<br>");
		rb.setValue("RB_HungWay1");
		rb.setId ("RB_HungWay1");
		if (hu.getHungUpWay().getValue() == HungUpWay.SpecDataRel.getValue()) {
			rb.setChecked(true);
		}
		else {
			rb.setChecked(false);
		}
		this.Pub1.append(Add(rb));

		this.Pub1.append(Add("&nbsp;&nbsp;&nbsp;&nbsp;解除流程挂起的日期:"));
		TB tb = new TB();
		tb.setReadOnly(false);
		tb.setId("TB_RelData");
		if (hu.getDTOfUnHungUpPlan().length() == 0) {
			Date dNow = new Date();   //当前时间
			Date dBefore = new Date();
			Calendar calendar = Calendar.getInstance(); //得到日历
			calendar.setTime(dNow);//把当前时间赋给日历
			calendar.add(Calendar.DAY_OF_MONTH, 7); 
			dBefore = calendar.getTime(); 
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //设置时间格式
			String defaultStartDate = sdf.format(dBefore);    //格式化前一天
			hu.setDTOfUnHungUpPlan(defaultStartDate); 
		}
		tb.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
		tb.setText(hu.getDTOfUnHungUpPlan());
		this.Pub1.append(Add(tb));

		this.Pub1.append(AddBR());
		this.Pub1.append(Add("挂起原因(可以为空):"));

		tb = new TB();
		tb.setId ("TB_Note");
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.setColumns(140);
		tb.setHeight(60);
		tb.setText(hu.getNote());
		this.Pub1.append(Add(tb));
		this.Pub1.append(AddFieldSetEnd());

		this.Pub1.append(Add("&nbsp;&nbsp;&nbsp;&nbsp;"));
		Button btn = new Button();
		btn.setId("Btn_OK");

		if (gwf.getWFState().getValue()== WFState.HungUp.getValue()) {
			btn.setText(" 取消挂起 ");
		}
		else {
			btn.setText(" 挂起 ");
		}

		btn.addAttr("onclick", " hungUpSubmit(this);");
		this.Pub1.append(Add(btn));

		btn = new Button();
		btn.setId("Btn_Cancel");
		btn.setText(" 返回 ");
		btn.addAttr("onclick", "hungUpSubmit(this);");
		this.Pub1.append(Add(btn));

	}
	
}

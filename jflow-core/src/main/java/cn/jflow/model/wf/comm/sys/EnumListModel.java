package cn.jflow.model.wf.comm.sys;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMains;
import BP.Sys.SysEnums;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;

public class EnumListModel extends BaseModel {

	private StringBuffer ucSys1;
	
	public StringBuffer getUcSys1() {
		return ucSys1;
	}
	
	
	public String getDoType() {
		if (StringHelper.isNullOrEmpty(getParameter("DoType")))
			return "";
		return getParameter("DoType");
	}


	public EnumListModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
		ucSys1 = new StringBuffer();
	}
	
	
	public final void BindRefNo() {
		SysEnumMain sem = new SysEnumMain(this.getRefNo());

		ucSys1.append(AddTableNormal());
		ucSys1.append(AddTRGroupTitle(3, "<a href='EnumList.jsp?T=" + new java.util.Date()+ "'>列表</a> - <a href='EnumList.jsp?DoType=New&T=" + new java.util.Date()+ "'>新建</a> - 编辑：<b>" + sem.getName() + "</b>"));

		ucSys1.append(AddTR());
		Button btn = new Button();
		btn.setText("保存");
		btn.setId(NamesOfBtn.Save.getCode());
		btn.setCssClass("easyui-linkbutton");
		btn.addAttr("onclick","btn_Click()");
		//btn.Click += new EventHandler(btn_Click);
		ucSys1.append(AddTDGroupTitle1("colspan=3", btn.toString()));
		ucSys1.append(AddTREnd());

		ucSys1.append(AddTR());
		// ucSys1.append(AddTDTitle("序");
		ucSys1.append(AddTDGroupTitle("项目"));
		ucSys1.append(AddTDGroupTitle("采集"));
		ucSys1.append(AddTDGroupTitle("说明"));
		ucSys1.append(AddTREnd());

		SysEnums ses = new SysEnums();
		ses.Retrieve(SysEnumAttr.EnumKey, this.getRefNo(), SysEnumAttr.IntKey);

		ucSys1.append(AddTRSum());
		ucSys1.append(AddTD("style='text-align:right; width:60px'", "编号"));
		TextBox tb = new TextBox();
		tb.setId("TB_No");
		tb.setText(this.getRefNo());
		tb.setEnabled(false);
		tb.addAttr("readonly","readonly");
		ucSys1.append(AddTD(tb));
		ucSys1.append(AddTD("不可修改"));
		ucSys1.append(AddTREnd());

		ucSys1.append(AddTRSum());
		ucSys1.append(AddTD("style='text-align:right; width:60px'", "名称"));
		tb = new TextBox();
		tb.setId("TB_Name");
		tb.setText(sem.getName());
		ucSys1.append(AddTD(tb));
		ucSys1.append(AddTD(""));
		ucSys1.append(AddTREnd());

		int myNum = 0;
		//for (SysEnum se : ses) {
		for (int i=0;i<ses.size();i++) {
			SysEnum se =(SysEnum) ses.get(i);
			ucSys1.append(AddTR());
			ucSys1.append(AddTD(se.getIntKey()));
			tb = new TextBox();
			tb.setId("TB_" + se.getIntKey());
			tb.setText(se.getLab());
			tb.setColumns(50);
			ucSys1.append(AddTD(tb));
			ucSys1.append(AddTD(""));
			ucSys1.append(AddTREnd());
			myNum = se.getIntKey();
		}

		myNum++;
		//每次追加10个
		for (int i = myNum; i < 10 + myNum; i++) {
			ucSys1.append(AddTR());
			ucSys1.append(AddTD(i));
			tb = new TextBox();
			tb.setId("TB_" + i);
			tb.setColumns(50);
			ucSys1.append(AddTD(tb));
			ucSys1.append(AddTD(""));
			ucSys1.append(AddTREnd());
		}
		ucSys1.append(AddTableEnd());
	}
	
	
	public final void BindNew() {
		ucSys1.append(AddTableNormal());
		ucSys1.append(AddTRGroupTitle(3, "<a href='EnumList.jsp?T=" + new java.util.Date()+ "'>列表</a> - <b>新建</b>"));

		ucSys1.append(AddTR());
		Button btn = new Button();
		btn.setText("保存");
		btn.setId(NamesOfBtn.Save.getCode());
		btn.setCssClass("easyui-linkbutton");
		btn.addAttr("onclick","btn_New_Click()");
		//btn.Click += new EventHandler(btn_New_Click);
		ucSys1.append(AddTDGroupTitle1("colspan=3", btn.toString()));
		ucSys1.append(AddTREnd());

		ucSys1.append(AddTR());
		ucSys1.append(AddTDGroupTitle("项目"));
		ucSys1.append(AddTDGroupTitle("采集"));
		ucSys1.append(AddTDGroupTitle("说明"));
		ucSys1.append(AddTREnd());

		ucSys1.append(AddTRSum());
		ucSys1.append(AddTD("style='text-align:right; width:60px'", "编号"));
		TextBox tb = new TextBox();
		tb.setId("TB_No");
		tb.setColumns(50);
		ucSys1.append(AddTD(tb));
		ucSys1.append(AddTD("编号系统唯一并且以字母或下划线开头"));
		ucSys1.append(AddTREnd());

		ucSys1.append(AddTRSum());
		ucSys1.append(AddTD("style='text-align:right; width:60px'", "名称"));
		tb = new TextBox();
		tb.setId("TB_Name");
		tb.setColumns(50);
		ucSys1.append(AddTD(tb));
		ucSys1.append(AddTD("不能为空"));
		ucSys1.append(AddTREnd());
		for (int i = 0; i < 20; i++) {
			ucSys1.append(AddTR());
			ucSys1.append(AddTD(i));
			tb = new TextBox();
			tb.setId ("TB_" + i);
			tb.setColumns(50);
			ucSys1.append(AddTD(tb));
			ucSys1.append(AddTD(""));
			ucSys1.append(AddTREnd());
		}
		ucSys1.append(AddTableEnd());
	}

	
	public void Page_Load(HttpServletRequest request,HttpServletResponse response) {
		//this.Title = "枚举值编辑";
		if ("Del".equals(this.getDoType())) {
			MapAttrs attrs = new MapAttrs();
			attrs.Retrieve(MapAttrAttr.UIBindKey, this.getRefNo());

			ucSys1.append(AddEasyUiPanelInfoBegin("删除确认", "icon-delete",2));

			if (attrs.size() != 0) {
				ucSys1.append(Add("<b>此枚举值已经被其它的字段所引用，您不能删除它。</b>"));
				ucSys1.append(Add("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-back'\" href='EnumList.jsp?T=" + new java.util.Date() + "' >返回列表</a>"));
				ucSys1.append(AddEasyUiPanelInfoEnd());
				return;
			}

			SysEnumMain m = new SysEnumMain(this.getRefNo());
			ucSys1.append(AddEasyUiLinkButton("确定删除：" + m.getName() + "？", "EnumList.jsp?RefNo=" + this.getRefNo() + "&DoType=DelReal&T=" + new java.util.Date(), "icon-delete",false));
			ucSys1.append(AddSpace(1));
			ucSys1.append(AddEasyUiLinkButton("取消", "EnumList.jsp?T=" + new java.util.Date(), "icon-undo",false));
			ucSys1.append(AddEasyUiPanelInfoEnd());
			return;
		}

		if ("DelReal".equals(this.getDoType())) {
			SysEnumMain m = new SysEnumMain();
			m.setNo(this.getRefNo());
			m.Delete();
			SysEnums ses = new SysEnums();
			ses.Delete(SysEnumAttr.EnumKey, this.getRefNo());
			//this.Response.Redirect("EnumList.jsp", true);
			try {
				response.sendRedirect("EnumList.jsp?T=" + new java.util.Date());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if ("New".equals(this.getDoType())) {
			this.BindNew();
			return;
		}

		if (this.getRefNo() != null) {
			this.BindRefNo();
			return;
		}

		ucSys1.append(AddTableNormal());
		ucSys1.append(AddTRGroupTitle(5, "<b>列表</b> - <a href='EnumList.jsp?DoType=New&T=" + new java.util.Date() + "'>新建</a>"));
		ucSys1.append(AddTR());
		ucSys1.append(AddTDGroupTitleCenter("序"));
		ucSys1.append(AddTDGroupTitle("编号"));
		ucSys1.append(AddTDGroupTitle("名称"));
		ucSys1.append(AddTDGroupTitle("信息"));
		ucSys1.append(AddTDGroupTitle("操作"));
		ucSys1.append(AddTREnd());

		SysEnumMains sems = new SysEnumMains();
		sems.RetrieveAll();
		int i = 0;
		for (int j=0;j<sems.size();j++) {
			SysEnumMain se = (SysEnumMain) sems.get(j);
			i++;
			ucSys1.append(AddTR(i % 2 == 0));
			ucSys1.append(AddTDIdx(i));
			ucSys1.append(AddTD(se.getNo()));
			ucSys1.append(AddTDA("EnumList.jsp?RefNo=" + se.getNo() + "&T=" + new java.util.Date(), se.getName()));
			ucSys1.append(AddTD(se.getCfgVal()));
			ucSys1.append(AddTDBegin());
			ucSys1.append(AddEasyUiLinkButton("删除", "EnumList.jsp?RefNo=" + se.getNo() + "&DoType=Del&T=" + new java.util.Date(), "icon-delete",false));
			ucSys1.append(AddTDEnd());
			ucSys1.append(AddTREnd());
		}
		ucSys1.append(AddTableEnd());
	}

}

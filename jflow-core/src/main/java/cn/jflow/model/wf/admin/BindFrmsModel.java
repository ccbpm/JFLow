package cn.jflow.model.wf.admin;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import BP.En.QueryObject;
import BP.Sys.AppType;
import BP.Sys.FrmType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.SystemConfig;
import BP.WF.Node;
import BP.WF.RunModel;
import BP.WF.StartWorkAttr;
import BP.WF.WorkAttr;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTreeAttr;
import BP.WF.Template.SysFormTrees;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;

public class BindFrmsModel extends BaseModel {

	private StringBuffer Pub1;

	public String getPub1() {
		return Pub1.toString();
	}

	public BindFrmsModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public String getEnsName() {
		return this.get_request().getParameter("EnsName");
	}

	// /#region 属性.
	public  String getFK_MapData() {
		return getParameter("FK_MapData");
	}

	public  String getFK_Flow() {
		return getParameter("FK_Flow");
	}

	/**
	 * 节点
	 */
	public  int getFK_Node() {
		try {
			return Integer.parseInt(getParameter("FK_Node"));
		} catch (java.lang.Exception e) {
			return Integer.parseInt(getParameter("FK_Flow"));
		}
	}

	// /#endregion 属性.

	public  void loadPage() {
		// switch (this.DoType)
		if (this.getDoType().equals("Up")) {
			FrmNode fnU = new FrmNode(this.getMyPK());
			fnU.DoUp();
			this.BindList();
		} else if (this.getDoType().equals("Down")) {
			FrmNode fnD = new FrmNode(this.getMyPK());
			fnD.DoDown();
			this.BindList();
		} else if (this.getDoType().equals("SelectedFrm")) {
			this.SelectedFrm();
		} else {
			this.BindList();
		}
	}

	// /#region 绑定表单.
	public  void SelectedFrm() {
		Pub1 = new StringBuffer();
		Node nd = new Node(this.getFK_Node());

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		Pub1.append(AddTable("align=left"));
		Pub1.append(AddCaption("设置节点:(" + nd.getName() + ")绑定的表单"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("Idx"));
		Pub1.append(AddTDTitle("表单编号"));
		Pub1.append(AddTDTitle("名称"));
		Pub1.append(AddTDTitle("表/视图"));
		Pub1.append(AddTREnd());

		MapDatas mds = new MapDatas();
		QueryObject obj_mds = new QueryObject(mds);
		obj_mds.AddWhere(MapDataAttr.AppType, AppType.Application.getValue());
		obj_mds.addOrderBy(MapDataAttr.Name);
		obj_mds.DoQuery();

		SysFormTrees formTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(formTrees);
		objInfo.AddWhere(SysFormTreeAttr.ParentNo, "0");
		objInfo.addOrderBy(SysFormTreeAttr.Name);
		objInfo.DoQuery();

		int idx = 0;
		for (SysFormTree fs : formTrees.ToJavaList()) {
			idx++;
			Pub1.append(AddTRSum());
			Pub1.append(AddTDIdx(idx));
			Pub1.append(AddTD("colspan=4", fs.getName()));
			Pub1.append(AddTREnd());
			for (MapData md : mds.ToJavaList()) {
				if (!md.getFK_FormTree().equals(fs.getNo())) {
					continue;
				}
				idx++;
				Pub1.append(AddTR());
				Pub1.append(AddTDIdx(idx));

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + md.getNo());
				cb.setText(md.getNo());
				cb.setChecked(fns.Contains(FrmNodeAttr.FK_Frm, md.getNo()));

				Pub1.append(AddTD(cb));
				if (cb.getChecked()) {
					Pub1.append(AddTDB("<a href=\"javascript:WinOpen('../MapDef/CCForm/Frm.jsp?FK_MapData="
							+ md.getNo()
							+ "&FK_Flow="+ this.getFK_Flow()
							+ "&UserNo="+BP.Web.WebUser.getNo()
							+ "&SID="+BP.Web.WebUser.getSID()+"');\" ><b>" + md.getName() + "</b></a>"));
					Pub1.append(AddTDB(md.getPTable()));
				} else {
					Pub1.append(AddTD("<a href=\"javascript:WinOpen('../MapDef/CCForm/Frm.jsp?FK_MapData="
							+ md.getNo()
							+ "&FK_Flow=" + this.getFK_Flow()
							+ "&UserNo="+BP.Web.WebUser.getNo()
							+ "&SID="+BP.Web.WebUser.getSID()+"');\" >" + md.getName() + "</a>"));
					Pub1.append(AddTD(md.getPTable()));
				}
				Pub1.append(AddTREnd());
			}
			AddChildNode(fs.getNo(), mds, fns);
		}
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setText("保存并设置绑定方案属性");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "SaveFlowFrmsClick();");
		Pub1.append(AddTR());
		Pub1.append(AddTD("colspan=4", btn));
		Pub1.append(AddTREnd());
		Pub1.append(AddTableEnd());
	}

	private void AddChildNode(String parentNo, MapDatas mds, FrmNodes fns) {
		SysFormTrees formTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(formTrees);
		objInfo.AddWhere(SysFormTreeAttr.ParentNo, parentNo);
		objInfo.addOrderBy(SysFormTreeAttr.Name);
		objInfo.DoQuery();

		int idx = 0;
		for (SysFormTree fs : formTrees.ToJavaList()) {
			idx++;
			for (MapData md : mds.ToJavaList()) {
				if (md.getFK_FormTree().equals(fs.getNo())) {
					continue;
				}
				idx++;
				Pub1.append(AddTR());
				Pub1.append(AddTDIdx(idx));

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + md.getNo());
				cb.setText(md.getNo());
				cb.setChecked(fns.Contains(FrmNodeAttr.FK_Frm, md.getNo()));

				Pub1.append(AddTD(cb));
				Pub1.append(AddTD(md.getName()));
				Pub1.append(AddTD(md.getPTable()));
				Pub1.append(AddTREnd());
			}
			//AddChildNode(fs.getNo(), mds, fns);
		}
	}

	// /#endregion 绑定表单.

	// /#region 设置方案.
	public  void BindList() {
		Pub1 = new StringBuffer();
		String text = "";
		Node nd = new Node(this.getFK_Node());
		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		if (fns.size() == 0) {
			text = "<ul>";
			text += "<li>1，当前没有任何独立表单绑定到该节点上。";
			text += "<li>2，请您执行绑定表单：<a href=\"javascript:BindFrms('" + this.getFK_Node() + "','" + this.getFK_Flow() + "');\"  >绑定表单</a></li>";
			text += "</ul>";
			Pub1.append(AddFieldSet("提示", text));
			return;
		}
		String tfModel = (String) SystemConfig.getAppSettings().get("TreeFrmModel");
		
		Pub1.append(AddTable("width=100%"));
		Pub1.append(AddCaption("设置节点:(" + nd.getName() + ")绑定表单"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("Idx"));
		Pub1.append(AddTDTitle("表单编号"));
		Pub1.append(AddTDTitle("名称"));
		if (tfModel!=null&&tfModel.equals("1"))
		{
			Pub1.append(AddTDTitle("关键字段"));
		}

		Pub1.append(AddTDTitle("启用规则"));
		Pub1.append(AddTDTitle("可编辑否？"));
		Pub1.append(AddTDTitle("可打印否？"));
		Pub1.append(AddTDTitle("是否启用<br>装载填充事件"));
		Pub1.append(AddTDTitle("权限控制<br>方案"));
		Pub1.append(AddTDTitle("表单元素<br>自定义设置"));
		Pub1.append(AddTDTitle("谁是主键？"));
		Pub1.append(AddTDTitle("文件模版"));

		if (nd.getHisRunModel().equals(RunModel.FL) || nd.getHisRunModel() .equals(RunModel.FHL))
		{
		   Pub1.append(AddTDTitle("是否1变N")); //add by zhoupeng 2016.03.25 for hainan.
		}

		if (nd.getHisRunModel().equals(RunModel.SubThread))
		{
			Pub1.append(AddTDTitle("数据汇总")); //add by zhoupeng 2016.03.25 for hainan.
		}

		Pub1.append(AddTDTitle("顺序"));
		Pub1.append(AddTDTitle(""));
		Pub1.append(AddTDTitle(""));
		Pub1.append(AddTREnd());

		int idx = 1;
		for (FrmNode fn : fns.ToJavaList()) {
			Pub1.append(AddTR());
			Pub1.append(AddTDIdx(idx++));
			Pub1.append(AddTD(fn.getFK_Frm()));

			MapData md = new MapData();
			md.setNo(fn.getFK_Frm());
			try {
				md.Retrieve();
			} catch (java.lang.Exception e) {
				// 说明该表单不存在了，就需要把这个删除掉.
				fn.Delete();
			}

			Pub1.append(AddTD("<a href=\"javascript:WinOpen('../MapDef/CCForm/Frm.jsp?FK_MapData="
					+ md.getNo()
					+ "&FK_Flow="
					+ this.getFK_Flow()
					+ "&UserNo="+BP.Web.WebUser.getNo()
					+ "&SID="+BP.Web.WebUser.getSID()+"');\" >"
					+ md.getName() + "</a>"));

			DDL ddl = new DDL();
			//ddl.ID = "DDL_FrmType_" + fn.FK_Frm;
			//ddl.BindSysEnum("FrmType", (int)fn.HisFrmType);
			//this.Pub1.AddTD(fn.HisFrmTypeText);

			//获取当前独立表单中的所有字段     add by 海南  zqp
			if (tfModel!=null&&tfModel.equals("1"))
			{
				//获取它的字段集合
				MapAttrs attrs = new MapAttrs();
				attrs.Retrieve(MapAttrAttr.FK_MapData, md.getNo());

				MapAttrs attrNs = new MapAttrs();
				//去掉一些基础字段
				for (MapAttr attr : attrs.ToJavaList())
				{
					//switch (attr.KeyOfEn)
					//ORIGINAL LINE: case "Title":
					if (attr.getKeyOfEn().equals("Title") || attr.getKeyOfEn().equals("FK_Emp") || attr.getKeyOfEn().equals("MyNum") || attr.getKeyOfEn().equals("FK_NY") || attr.getKeyOfEn() == WorkAttr.Emps || attr.getKeyOfEn() == WorkAttr.OID || attr.getKeyOfEn() == StartWorkAttr.Rec || attr.getKeyOfEn() == StartWorkAttr.FID)
					{
							continue;
					}
					else
					{
					}
					attrNs.AddEntity(attr);
				}
				//添加到页面中
				DDL myddl = new DDL();
				myddl.setId("DDL_Attr_" + md.getNo());
				myddl.BindEntities(attrNs, MapAttrAttr.KeyOfEn, MapAttrAttr.Name);
				myddl.SetSelectItem(fn.getGuanJianZiDuan());
				Pub1.append(myddl);
			}

			//为了扩充的需要，把下拉框的模式去掉了.
			//ddl = new DDL();
			//ddl.ID = "DDL_FrmEnableRole_" + md.No;
			//ddl.Items.Add(new ListItem("始终启用", "0"));
			//ddl.Items.Add(new ListItem("有数据时启用", "1"));
			//ddl.Items.Add(new ListItem("有参数时启用", "2"));
			//ddl.Items.Add(new ListItem("按表单字段条件", "3"));
			//ddl.Items.Add(new ListItem("按SQL表达式", "4"));
			//ddl.SetSelectItem(fn.FrmEnableRoleInt); //设置权限控制方案.
			//this.Pub1.AddTD(ddl);
			Pub1.append("<TD><a href=\"javascript:WinOpen('./FlowFrm/FrmEnableRole.jsp?FK_Node="+fn.getFK_Node()+"&FK_MapData="+fn.getFK_Frm()+"')\">设置(" + fn.getFrmEnableRole() + ")</a></TD>");

			CheckBox cb = new CheckBox();
			cb.setId("CB_IsEdit_" + md.getNo());
			cb.setText("可编辑否？");
			cb.setChecked(fn.getIsEdit());
			Pub1.append(AddTD(cb));
			
			cb = new CheckBox();
			cb.setId("CB_IsPrint_" + md.getNo());
			cb.setText("打印否？");
			cb.setChecked(fn.getIsPrint());
			Pub1.append(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsEnableLoadData_" + md.getNo());
			cb.setText("启用否？");
			cb.setChecked(fn.getIsEnableLoadData());
			Pub1.append(AddTD(cb));
			
			ddl = new DDL();
			ddl.setId("DDL_Sln_" + md.getNo());
			ddl.Items.add(new ListItem("默认方案", "0"));
			ddl.Items.add(new ListItem("自定义", this.getFK_Node()+""));
			ddl.SetSelectItem(fn.getFrmSln()); //设置权限控制方案.
			Pub1.append(AddTD(ddl));
			
			Pub1.append(AddTDBegin());
			Pub1.append("<a href=\"javascript:WinField('" + md.getNo() + "','" + this.getFK_Node() + "','" + this.getFK_Flow() + "')\" >字段</a>");
			Pub1.append("-<a href=\"javascript:WinFJ('" + md.getNo() + "','" + this.getFK_Node() + "','" + this.getFK_Flow() + "')\" >附件</a>");
			Pub1.append("-<a href=\"javascript:WinDtl('" + md.getNo() + "','" + this.getFK_Node() + "','" + this.getFK_Flow() + "')\" >从表</a>");

			if (md.getHisFrmType().equals(FrmType.ExcelFrm))
			{
				Pub1.append("-<a href=\"javascript:ToolbarExcel('" + md.getNo() + "','" + this.getFK_Node() + "','" + this.getFK_Flow() + "')\" >ToolbarExcel</a>");
			}

			if (md.getHisFrmType().equals(FrmType.WordFrm))
			{
				Pub1.append("-<a href=\"javascript:ToolbarWord('" + md.getNo() + "','" + this.getFK_Node() + "','" + this.getFK_Flow() + "')\" >ToolbarWord</a>");
			}

			Pub1.append(AddTDEnd());

			ddl = new DDL();
			ddl.setId("DDL_WhoIsPK_" + md.getNo());
			ddl.BindSysEnum("WhoIsPK");
			ddl.SetSelectItem(fn.getWhoIsPK().getValue()); //谁是主键？.
			Pub1.append(AddTD(ddl));

			if (md.getHisFrmType().equals(FrmType.ExcelFrm) || md.getHisFrmType().equals(FrmType.WordFrm))
			{
				ddl = new DDL();
				ddl.setId("DDL_File_" + md.getNo());
				File files = new File(BP.Sys.SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/");
				FileUtils.listFiles(files, new String[]{"xls"}, true);
				File[] fs = files.listFiles();
				//String[] files = Directory.GetFiles(BP.Sys.SystemConfig.getPathOfDataUser() + "\\FrmOfficeTemplate\\", md.getNo() + "*.xls");
				for(int i=0;i<fs.length;i++){
					//System.IO.FileInfo info=new System.IO.FileInfo(
					if(md.getNo().equals(fs[i].getName())){
						ddl.Items.add(new ListItem(fs[i].getName().substring(fs[i].getName().lastIndexOf(md.getNo())), fs[i].getName()));
					}
				}
				Pub1.append(AddTD(ddl));
			}
			else
			{
				Pub1.append(AddTD("无效"));
			}

			if (nd.getHisRunModel().equals(RunModel.FL) || nd.getHisRunModel().equals(RunModel.FHL))
			{
				cb = new CheckBox();
				cb.setId("CB_Is1ToN_" + md.getNo());
				cb.setText("是否1变N？");
				cb.setChecked(fn.getIs1ToN());
				Pub1.append(AddTD(cb)); //add by zhoupeng 2016.03.25 for hainan.
			}

			if (nd.getHisRunModel().equals(RunModel.SubThread))
			{
				ddl = new DDL();
				ddl.setId("DDL_HuiZong_" + md.getNo());
				ddl.Items.add(new ListItem("不汇总数据", "0"));

				BP.WF.Template.FrmNodes myfns = new FrmNodes();
				myfns.Retrieve(FrmNodeAttr.FK_Flow, nd.getFK_Flow());

				//组合这个字符串.
				String strs = ";"+fn.getFK_Frm()+";";
				for (BP.WF.Template.FrmNode myfrn : myfns.ToJavaList())
				{
					if (strs.contains(";" + myfrn.getFK_Frm() + ";") == true)
					{
						continue;
					}

					strs += ";" + myfrn.getFK_Frm() + ";";

					//检查该frm 是否有dtl.
					MapDtls dtls = new MapDtls(myfrn.getFK_Frm());
					if (dtls.size() == 0)
					{
						continue;
					}

					for (MapDtl dtl : dtls.ToJavaList())
					{
						ddl.Items.add(new ListItem("汇总到:"+myfrn.getHisFrm().getName()+"-"+dtl.getName(), myfrn.getHisFrm().getNo()+"@"+dtl.getNo()));
					}
				}
				ddl.SetSelectItem(fn.getHuiZong()); //设置汇总..
				Pub1.append(AddTD(ddl));
			}

			TextBox tb = new TextBox();
			tb.setId("TB_Idx_" + md.getNo());
			tb.setText(fn.getIdx()+"");
			tb.setColumns(5);
			Pub1.append(AddTD(tb));

			Pub1.append(AddTDA("BindFrms.jsp?ShowType=EditPowerOrder&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&MyPK=" + fn.getMyPK() + "&DoType=Up", "上移"));
			Pub1.append(AddTDA("BindFrms.jsp?ShowType=EditPowerOrder&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&MyPK=" + fn.getMyPK() + "&DoType=Down", "下移"));

			Pub1.append(AddTREnd());
		}

		Pub1.append(AddTableEnd());

		  text = "<input type=button onclick=\"javascript:BindFrms('" + this.getFK_Node() + "','" + this.getFK_Flow() + "');\" value='修改表单绑定'  class=Btn />";
		Pub1.append(text);

		Button btn = new Button();
		btn.setId("Save");
		btn.setText("保存设置");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "btn_SavePowerOrders_Click();");
		Pub1.append(btn);

		text = "<input type=button onclick=\"javascript:window.close();\" value='关闭'  class=Btn />";
		Pub1.append(text);
	}

}

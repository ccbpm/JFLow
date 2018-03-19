package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.En.QueryObject;
import BP.Sys.AppType;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Node;
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

public class FlowFrmsModel extends BaseModel {
	public FlowFrmsModel(HttpServletRequest request,
			HttpServletResponse response,String FK_Flow,String FK_MapData,String ShowType,int FK_Node) {
		super(request, response);
		Pub1 = new StringBuilder();
		Left = new StringBuilder();
		this.FK_Flow = FK_Flow;
		this.FK_MapData = FK_MapData;
		this.ShowType = ShowType;
		this.FK_Node = FK_Node;
//		this.Title = Title;
//		this.IsPostBack = IsPostBack;
	}

	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Left = new StringBuilder();
	public String ShowType;
	public String FK_MapData;
	public String FK_Flow;
	public int FK_Node;
	public String Title;
	public boolean IsPostBack;

	public void init() {
		if (this.IsPostBack == false) {
			if (this.getDoType() != null && this.getDoType().equals("Up")) {
				FrmNode fnU = new FrmNode(this.getMyPK());
				fnU.DoUp();
			} else if (this.getDoType() != null
					&& this.getDoType().equals("Down")) {
				FrmNode fnD = new FrmNode(this.getMyPK());
				fnD.DoDown();
			} else if (this.getDoType() != null
					&& this.getDoType().equals("DelFrm")) {
				FrmNodes fnsR = new FrmNodes();
				if (fnsR.Retrieve(FrmNodeAttr.FK_Frm, this.getFK_MapData()) != 0) {
					this.Alert("此表单已经被多个流程节点(" + fnsR.size() + ")绑定，所以您不能删除它。");
				} else {
					MapData md = new MapData();
					md.setNo(this.getFK_MapData());
					md.Delete();
				}
			} else if (this.getDoType() != null
					&& this.getDoType().equals("Del")) {
				FrmNodes fns = new FrmNodes(this.getFK_Flow(),
						this.getFK_Node());
				for (FrmNode fn : fns.ToJavaList()) {
					if (this.getFK_MapData().equals(fn.getFK_Frm())) {
						fn.Delete();
						break;
					}
				}
			}
			// ORIGINAL LINE: case "Add":
			else if (this.getDoType() != null && this.getDoType().equals("Add")) {
				FrmNode fnN = new FrmNode();
				fnN.setFK_Frm(this.getFK_MapData());
				fnN.setFK_Node(this.getFK_Node());
				fnN.setFK_Flow(this.getFK_Flow());
				fnN.Save();
			} else {
			}
		}
		
		// /#endregion 执行功能.

		// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
		// string member and was converted to Java 'if-else' logic:
		// switch (this.ShowType)
		// ORIGINAL LINE: case "Frm":
		if (ShowType != null && ShowType.equals("Frm")) {
			this.BindFrm();
			this.Title = "表单";
		}
		// ORIGINAL LINE: case "FrmLib":
		else if (ShowType != null &&( ShowType.equals("FrmLib")
				|| ShowType.equals("FrmLab"))) {
			this.BindFrmLib();
			this.Title = "表单库";
		}
		// ORIGINAL LINE: case "FlowFrms":
		else if (ShowType != null && ShowType.equals("FlowFrms")) {
			this.BindFlowFrms();
			this.Title = "流程表单";
		}
		// ORIGINAL LINE: case "FrmSorts":
		else if (ShowType != null && ShowType.equals("FrmSorts")) {
			this.BindFrmSorts();
			this.Title = "流程类别";
		}
		// ORIGINAL LINE: case "EditPowerOrder":
		else if (ShowType != null && ShowType.equals("EditPowerOrder")) // 编辑权限与顺序.
		{
			this.BindEditPowerOrder();
		} else {
		}

		this.BindLeft();
	}

	/**
	 * 编辑权限与顺序
	 */
	public void BindEditPowerOrder() {
		this.Pub1.append(AddH2("表单权限与显示顺序"));
		this.Pub1.append(AddHR());
		this.Pub1.append(AddTable("align=left"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("IDX"));
		this.Pub1.append(AddTDTitle("编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("显示方式"));
		this.Pub1.append(AddTDTitle("是否可编辑？"));
		this.Pub1.append(AddTDTitle("是否可打印"));
		this.Pub1.append(AddTDTitle("权限控制方案"));
		this.Pub1.append(AddTDTitle("自定义"));
		this.Pub1.append(AddTDTitle("谁是主键？"));
		this.Pub1.append(AddTDTitle("顺序"));
		this.Pub1.append(AddTDTitle(""));
		this.Pub1.append(AddTDTitle(""));
		this.Pub1.append(AddTREnd());

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		int idx = 1;
		for (FrmNode fn : fns.ToJavaList()) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			this.Pub1.append(AddTD(fn.getFK_Frm()));

			MapData md = new MapData(fn.getFK_Frm());
			md = new MapData(fn.getFK_Frm());
			// this.Pub1.AddTD(md.Name);
			this.Pub1.append(AddTDA("FlowFrms.jsp?ShowType=Frm&FK_MapData="
					+ md.getNo() + "&FK_Node=" + this.getFK_Node(),
					md.getName()));

			DDL ddl = new DDL();
			ddl.setId("DDL_FrmType_" + fn.getFK_Frm());
			ddl.setName("DDL_FrmType_" + fn.getFK_Frm());
			ddl.BindSysEnum("FrmType",
					fn.getHisFrmType().getValue());
			this.Pub1.append(AddTD(ddl));

			CheckBox cb = new CheckBox();
			cb.setId("CB_IsEdit_" + md.getNo());
			cb.setName("CB_IsEdit_" + md.getNo());
			cb.setText("是否可编辑？");
			cb.setChecked(fn.getIsEdit());
			this.Pub1.append(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsPrint_" + md.getNo());
			cb.setName("CB_IsPrint_" + md.getNo());
			cb.setText("是否可打印");
			cb.setChecked(fn.getIsPrint());
			this.Pub1.append(AddTD(cb));

			ddl = new DDL();
			ddl.setId("DDL_Sln_" + md.getNo());
			ddl.setName("DDL_Sln_" + md.getNo());
			// ddl.BindAtParas(md.Slns);
			ddl.Items.add(new ListItem("默认方案", "0"));
			ddl.Items.add(new ListItem("自定义", (new Integer(this.getFK_Node()))
					.toString()));
			ddl.SetSelectItem(fn.getFrmSln()); // 设置权限控制方案.
			this.Pub1.append(AddTD(ddl));

			this.Pub1.append(AddTD("<a href=\"javascript:WinField('"
					+ md.getNo() + "','" + this.getFK_Node() + "','"
					+ this.getFK_Flow()
					+ "')\" >字段</a>--<a href=\"javascript:WinFJ('" + md.getNo()
					+ "','" + this.getFK_Node() + "','" + this.getFK_Flow()
					+ "')\" >附件</a>--<a href=\"javascript:WinDtl('" + md.getNo() + "','" + this.getFK_Node() + "','" + this.getFK_Flow() + "')\" >从表</a>"));

			ddl = new DDL();
			ddl.setId("DDL_WhoIsPK_" + md.getNo());
			ddl.setName("DDL_WhoIsPK_" + md.getNo());
			ddl.BindSysEnum("WhoIsPK");
			ddl.SetSelectItem(fn.getWhoIsPK().getValue()); // 谁是主键？.
			this.Pub1.append(AddTD(ddl));

			TextBox tb = new TextBox();
			tb.setId("TB_Idx_" + md.getNo());
			tb.setName("TB_Idx_" + md.getNo());
			tb.setText(String.valueOf(fn.getIdx()));
			tb.setCols(5);
			this.Pub1.append(AddTD(tb));

			this.Pub1.append(AddTDA(
					"FlowFrms.jsp?ShowType=EditPowerOrder&FK_Node="
							+ this.getFK_Node() + "&FK_Flow="
							+ this.getFK_Flow() + "&MyPK=" + fn.getMyPK()
							+ "&DoType=Up", "上移"));
			this.Pub1.append(AddTDA(
					"FlowFrms.jsp?ShowType=EditPowerOrder&FK_Node="
							+ this.getFK_Node() + "&FK_Flow="
							+ this.getFK_Flow() + "&MyPK=" + fn.getMyPK()
							+ "&DoType=Down", "下移"));

			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTR());
		Button btn = new Button();
		btn.setId("Save");
		btn.setText("  Save  ");
		btn.setCssClass("Btn");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_SavePowerOrders_Click);
		btn.addAttr("onclick", "btn_SavePowerOrders_Click()");
		this.Pub1.append(AddTD("colspan=12", btn));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
	}

	public void BindFrmSorts() {
		SysFormTrees fss = new SysFormTrees();
		fss.RetrieveAll();
		this.Pub1.append(AddH2("表单类别维护"));
		this.Pub1.append(AddHR());

		this.Pub1.append(AddTable("align=left"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("序号"));
		this.Pub1.append(AddTDTitle("类别编号"));
		this.Pub1.append(AddTDTitle("类别名称"));
		this.Pub1.append(AddTREnd());

		for (int i = 1; i <= 15; i++) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(i));

			TextBox tb = new TextBox();
			// tb.setText((new Integer(i)).toString().PadLeft(2, '0'));
			tb.setText(StringUtils.leftPad((new Integer(i)).toString(), 2, '0'));
			Object tempVar = fss.GetEntityByKey(SysFormTreeAttr.No,
					tb.getText());
			SysFormTree fs = (SysFormTree) ((tempVar instanceof SysFormTree) ? tempVar
					: null);

			tb.setId("TB_No_" + i);
			tb.setCols(5);
			tb.setReadOnly(true);
			this.Pub1.append(AddTD(tb.getText()));

			tb = new TextBox();
			tb.setId("TB_Name_" + i);
			tb.setName("TB_Name_" + i);
			tb.setCols(40);
			if (fs != null) {
				tb.setText(fs.getName());
			}

			this.Pub1.append(AddTD(tb));
			this.Pub1.append(AddTREnd());
		}

		Button btn = new Button();
		btn.setText("Save");
		btn.setCssClass("Btn");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_SaveFrmSort_Click);
		btn.addAttr("onclick", "btn_SaveFrmSort_Click()");

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("colspan=2", btn));
		this.Pub1.append(AddTD("要删除类别，请把文本框数据清空保存即可。"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTableEndWithHR());
	}

	public void BindFlowFrms() {
		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		this.Pub1.append(AddH2("流程表单绑定"));
		this.Pub1.append(AddTable("align=left"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("表单编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("物理表"));
		this.Pub1.append(AddTDTitle("权限"));
		this.Pub1.append(AddTREnd());

		Node nd = new Node(this.getFK_Node());

		MapDatas mds = new MapDatas();
		QueryObject obj_mds = new QueryObject(mds);
		obj_mds.AddWhere(MapDataAttr.AppType,
				AppType.Application.getValue());
		
		obj_mds.addOrderBy(MapDataAttr.Name);
		obj_mds.DoQuery();
		// FrmSorts fss = new FrmSorts();
		// fss.RetrieveAll();
		SysFormTrees formTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(formTrees);
		objInfo.AddWhere(SysFormTreeAttr.ParentNo, "0");
		objInfo.addOrderBy(SysFormTreeAttr.Name);
		objInfo.DoQuery();

		int idx = 0;
		for (SysFormTree fs : formTrees.ToJavaList()) {
			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTDB("colspan=4", fs.getName()));
			this.Pub1.append(AddTREnd());
			for (MapData md : mds.ToJavaList()) {
				if (!md.getFK_FormTree().equals(fs.getNo())) {
					continue;
				}
				idx++;
				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDIdx(idx));

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + md.getNo());
				cb.setName("CB_" + md.getNo());
				cb.setText(md.getNo());
				cb.setChecked(fns.Contains(FrmNodeAttr.FK_Frm, md.getNo()));

				this.Pub1.append(AddTD(cb));
				this.Pub1
						.append(AddTD("<a href='../MapDef/CCForm/Frm.jsp?FK_MapData="
								+ md.getNo()
								+ "&FK_Flow="
								+ this.getFK_Flow()
								+ "'  target=_blank>" + md.getName() + "</a>"));
				this.Pub1.append(AddTD(md.getPTable()));

				if (cb.getChecked()) {
					this.Pub1.append(AddTD("<a href=\"javascript:WinField('"
							+ md.getNo() + "','" + this.getFK_Node() + "','"
							+ this.getFK_Flow()
							+ "')\">字段</a>|<a href=\"javascript:WinFJ('"
							+ md.getNo() + "','" + this.getFK_Node() + "','"
							+ this.getFK_Flow() + "')\">附件</a>"));
				} else {
					this.Pub1.append(AddTD());
				}
				// this.Pub1.AddTD(md.Designer);
				// this.Pub1.AddTD(md.DesignerUnit);
				// this.Pub1.AddTD(md.DesignerContact);
				this.Pub1.append(AddTREnd());
			}
			AddChildNode(fs.getNo(), mds, fns);
		}
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setText("Save");
		btn.setCssClass("Btn");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_SaveFlowFrms_Click);
		btn.addAttr("onclick", "btn_SaveFlowFrms_Click()");
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("colspan=5", btn));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
	}

	public void AddChildNode(String parentNo, MapDatas mds, FrmNodes fns) {
		SysFormTrees formTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(formTrees);
		objInfo.AddWhere(SysFormTreeAttr.ParentNo, parentNo);
		objInfo.addOrderBy(SysFormTreeAttr.Name);
		objInfo.DoQuery();

		int idx = 0;
		for (SysFormTree fs : formTrees.ToJavaList()) {
			idx++;
			for (MapData md : mds.ToJavaList()) {
				if (!md.getFK_FormTree().equals(fs.getNo())) {
					continue;
				}
				idx++;
				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDIdx(idx));

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + md.getNo());
				cb.setText(md.getNo());
				cb.setChecked(fns.Contains(FrmNodeAttr.FK_Frm, md.getNo()));

				this.Pub1.append(AddTD(cb));
				this.Pub1.append(AddTD(md.getName()));
				this.Pub1.append(AddTD(md.getPTable()));
				this.Pub1.append(AddTREnd());
			}
			AddChildNode(fs.getNo(), mds, fns);
		}
	}

	public void BindFrmLib() {
		this.Pub1.append(AddH2("表单库"));

		this.Pub1.append(AddTable("width=100% align=left"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("表单编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("物理表"));

		// this.Pub1.AddTDTitle("设计者");
		// this.Pub1.AddTDTitle("设计单位");
		// this.Pub1.AddTDTitle("联系方式");
		this.Pub1.append(AddTREnd());

		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.AppType,
				AppType.Application.getValue());

		SysFormTrees fss = new SysFormTrees();
		fss.RetrieveAll();
		int idx = 0;
		for (SysFormTree fs : fss.ToJavaList()) {
			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD("colspan=6", "<b>" + fs.getName() + "</b>"));
			this.Pub1.append(AddTREnd());
			for (MapData md : mds.ToJavaList()) {
				if (md.getFK_FrmSort().equals(fs.getNo())) {
					continue;
				}
				idx++;
				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDIdx(idx));
				this.Pub1.append(AddTD(md.getNo()));
				this.Pub1.append(AddTDA("FlowFrms.jsp?ShowType=Frm&FK_MapData="
						+ md.getNo() + "&FK_Node=" + this.getFK_Node()
						+ "&FK_Flow=" + this.getFK_Flow(), md.getName()));
				this.Pub1.append(AddTD(md.getPTable()));
				// this.Pub1.AddTD(md.Designer);
				// this.Pub1.AddTD(md.DesignerUnit);
				// this.Pub1.AddTD(md.DesignerContact);
				this.Pub1.append(AddTREnd());

				// this.Pub1.AddTR();
				// this.Pub1.AddTD();
				// this.Pub1.AddTD();
				// this.Pub1.AddTDBegin("colspan=5");
				// this.Pub1.AddTDEnd();
				// this.Pub1.AddTREnd();
			}
		}
		this.Pub1.append(AddTableEnd());
	}

	public void BindFrm() {
		MapData md = new MapData();
		if (StringHelper.isNullOrEmpty(FK_MapData) == false) {
			md.setNo(this.getFK_MapData());
			md.RetrieveFromDBSources();
			this.Pub1.append(AddH2("表单属性" + md.getName()));
			this.Pub1.append(AddHR());
		} else {
			this.Pub1.append(AddH2("新建表单"));
			this.Pub1.append(AddHR());
		}

		this.Pub1.append(AddTable("align=left width='100%' "));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("属性"));
		this.Pub1.append(AddTDTitle("采集"));
		this.Pub1.append(AddTDTitle("描述"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("表单名称"));
		TextBox tb = new TextBox();
		tb.setId("TB_Name");
		tb.setName("TB_Name");
		tb.setText(md.getName());
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("描述"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("表单编号"));
		tb = new TextBox();
		tb.setId("TB_No");
		tb.setName("TB_No");
		tb.setText(md.getNo());
		if (StringHelper.isNullOrEmpty(md.getNo()) == false) {
			// tb.Attributes["readonly"] = "true";
			tb.addAttr("readonly", "true");
		}

		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("也是表单ID."));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("表单类型"));
		DDL ddl = new DDL();
		ddl.setId("DDL_FrmType");
		ddl.setName("DDL_FrmType");
		ddl.BindSysEnum(MapDataAttr.FrmType,
				md.getHisFrmType().getValue());
		this.Pub1.append(AddTD(ddl));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("物理表/视图"));
		tb = new TextBox();
		tb.setId("TB_PTable");
		tb.setName("TB_PTable");
		tb.setText(md.getNo());
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("多个表单可以对应同一个表或视图<br>如果表不存在,ccflow会自动创建."));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("类别"));
		ddl = new DDL();
		ddl.setId("DDL_FK_FrmSort");
		ddl.setName("DDL_FK_FrmSort");
		SysFormTrees fss = new SysFormTrees();
		fss.RetrieveAll();

		ddl.Bind1(fss, md.getFK_FrmSort());

		this.Pub1.append(AddTD(ddl));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		// this.Pub1.AddTR();
		// this.Pub1.AddTD("设计单位");
		// tb = new TextBox();
		// tb.ID = "TB_" + MapDataAttr.DesignerUnit;
		// tb.Text = md.DesignerUnit;
		// if (string.IsNullOrEmpty(tb.Text))
		// tb.Text = BP.SystemConfig.DeveloperName;

		// tb.Columns = 60;
		// this.Pub1.AddTD("colspan=2", tb);
		// this.Pub1.AddTREnd();

		// this.Pub1.AddTR();
		// this.Pub1.AddTD("联系方式");
		// tb = new TextBox();
		// tb.ID = "TB_" + MapDataAttr.DesignerContact;
		// tb.Text = md.DesignerContact;
		// if (string.IsNullOrEmpty(tb.Text))
		// tb.Text = BP.SystemConfig.ServiceTel + "," +
		// BP.SystemConfig.ServiceMail;
		// tb.Columns = 60;
		// this.Pub1.AddTD("colspan=2", tb);
		// this.Pub1.AddTREnd();

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTDBegin());
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setName("Btn_Save");
		btn.setText("Save");
		btn.setCssClass("Btn");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_SaveFrm_Click);
		btn.addAttr("onclick", "btn_SaveFrm_Click('Btn_Save')");
		this.Pub1.append(btn);

		if (StringHelper.isNullOrEmpty(md.getNo()) == false) {
			btn = new Button();
			btn.setId("Btn_Delete");
			btn.setName("Btn_Delete");
			btn.setText("Delete");
			btn.setCssClass("Btn");
			// btn.Attributes["onclick"] = "return window.confirm('您确定要删除吗？')";
			// C#-style event wireups:
			// btn.Click += new EventHandler(btn_SaveFrm_Click);
			 btn.addAttr("onclick", "btn_SaveFrm_Click('Btn_Delete')");
			this.Pub1.append(btn);
		}

		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		if (StringHelper.isNullOrEmpty(md.getNo()) == false) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDBegin("colspan=3"));
			// this.Pub1.Add("<a href='FlowFrms.aspx?ShowType=FrmLib&DoType=DelFrm&FK_Node="
			// + FK_Node + "&FK_MapData=" + md.No +
			// "'  ><img src='./Img/Btn/Delete.gif' border=0 />删除</a>");
//			this.Pub1
//					.append("<a href='../MapDef/ViewFrm.jsp?DoType=Column4Frm&FK_MapData="
//							+ md.getNo()
//							+ "&FK_Flow="
//							+ this.getFK_Flow()
//							+ "' target=_blank  ><img src='../Img/Btn/View.gif' border=0 />傻瓜表单预览</a>");
//			this.Pub1
//					.append("<a href='../CCForm/Frm.jsp?FK_MapData="
//							+ md.getNo()
//							+ "&FK_Flow="
//							+ this.getFK_Flow()
//							+ "&IsTest=1&WorkID=0' target=_blank  ><img src='../Img/Btn/View.gif' border=0 />自由表单预览</a>");
//			this.Pub1
//					.append("<a href='../MapDef/ViewFrm.jsp?DoType=dd&FK_MapData="
//							+ md.getNo()
//							+ "&FK_Flow="
//							+ this.getFK_Flow()
//							+ "' target=_blank  ><img src='../Img/Btn/View.gif' border=0 />手机表单预览</a>");
			this.Pub1
					.append("<a href='../CCForm/Frm.jsp?FK_MapData="
							+ md.getNo()
							+ "&FK_Flow="
							+ this.getFK_Flow()
							+ "' target=_blank  ><img src='../Img/Btn/View.gif' border=0 />启动自由表单设计器</a>");
//			this.Pub1
//					.append("<a href='../MapDef/MapDef.jsp?PK="
//							+ md.getNo()
//							+ "&FK_Flow="
//							+ this.getFK_Flow()
//							+ "' target=_blank  ><img src='../Img/Btn/View.gif' border=0 />启动傻瓜表单设计器</a>");
			this.Pub1.append(AddTDEnd());
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());
	}

	public void BindLeft() {

		this.Left
				.append("<a href='http://ccflow.org' target=_blank ><img src='../../DataUser/ICON/"
						+ SystemConfig.getCompanyID()
						+ "/LogBiger.png' border=0/></a>");
		this.Left.append(AddHR());

		this.Left.append(AddUL());
		// //if (this.FK_Node == 0)
		// //{
		// this.Left.AddLi("<a href=\"FlowFrms.aspx?ShowType=FrmLib&FK_Node=" +
		// this.FK_Node + "&FK_MapData=" + this.FK_MapData + "&FK_Flow=" +
		// this.FK_Flow + "\"><b>表单库</b></a>");
		// this.Left.Add("查看，修改，设计，表单。<br><br>");

		// this.Left.AddLi("<a href=\"FlowFrms.aspx?ShowType=FrmSorts&FK_Node="
		// + this.FK_Node + "&FK_MapData=" + this.FK_MapData + "&FK_Flow=" +
		// this.FK_Flow + "\"><b>类别维护</b></a>");
		// this.Left.Add("维护表单类别。<br><br>");

		// this.Left.AddLi("<a href=\"FlowFrms.aspx?ShowType=Frm&FK_Node=" +
		// this.FK_Node + "&FK_Flow=" + this.FK_Flow + "\"><b>新建表单</b></a>");
		// this.Left.Add("新建表单。<br><br>");
		// //}
		// else
		// {
		this.Left
				.append(AddLi("<a href=\"FlowFrms.jsp?ShowType=FrmLib&FK_Node="
						+ this.getFK_Node()
						+ "&FK_MapData="
						+ this.getFK_MapData()
						+ "&FK_Flow="
						+ this.getFK_Flow()
						+ "\"><b>表单库</b></a>-<a href=\"FlowFrms.jsp?ShowType=FrmSorts&FK_Node="
						+ this.getFK_Node()
						+ "&FK_MapData="
						+ this.getFK_MapData()
						+ "\"><b>类别维护</b></a>-<a href=\"FlowFrms.jsp?ShowType=Frm&FK_Node="
						+ this.getFK_Node() + "\"><b>新建表单</b></a>"));
		this.Left.append("表单库维护，类别维护，新建表单<br><br>");

		this.Left
				.append(AddLi("<a href=\"FlowFrms.jsp?ShowType=FlowFrms&FK_Node="
						+ this.getFK_Node()
						+ "&FK_MapData="
						+ this.getFK_MapData()
						+ "&FK_Flow="
						+ this.getFK_Flow() + "\"><b>增加移除流程表单绑定</b></a>"));
		this.Left.append("增加或移除查询结果集合中的列内容。<br><br>");

		this.Left
				.append(AddLi("<a href=\"FlowFrms.jsp?ShowType=EditPowerOrder&FK_Node="
						+ this.getFK_Node()
						+ "&FK_MapData="
						+ this.getFK_MapData()
						+ "&FK_Flow="
						+ this.getFK_Flow() + "\"><b>表单权限与显示顺序</b></a>"));
		this.Left.append("表单在该节点的权限与显示顺序控制。<br><br>");
		// }
		this.Left.append(AddULEnd());
	}

}

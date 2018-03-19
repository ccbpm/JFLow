package cn.jflow.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.util.ConvertTools;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.WF.Glo;
import BP.WF.Template.*;
import BP.WF.Node;

// 1 public class WF_Admin_BillSet extends BP.Web.WebPage {
/**
 * /WF/Admin/Bill.jsp
 * @author ly
 *
 */
public class BillModel extends BaseModel {
	public String Title;
	public StringBuilder Ucsys1;
	public StringBuilder UCSys2;
	 
	public BillModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Ucsys1=new StringBuilder();
		UCSys2=new StringBuilder();
	}
	
	public StringBuilder getUCSys2() {
		return UCSys2;
	}
	public void setUCSys2(StringBuilder uCSys2) {
		UCSys2 = uCSys2;
	}
	public StringBuilder getUcsys1() {
		return Ucsys1;
	}
	public void setUcsys1(StringBuilder ucsys1) {
		Ucsys1 = ucsys1;
	}
/*	继承 BaseModel 里面有getNodeId()和getFK_Flow() 的方法
 * public final int getNodeID() {
		//2 return Integer.parseInt(this.Request.QueryString["NodeID"]);
		return Integer.parseInt(this.request.getParameter("NodeID"));
	}*/
	/** 
	 流程编号
	*/
	/*public final String getFK_Flow() {
		//3 return this.Request.QueryString["FK_Flow"];
		return this.request.getParameter("FK_Flow");
	}*/
	
	public final void DoNew(BillTemplate bill) {
		Node nd=new Node(getNodeID());
		this.Ucsys1.append(AddTable());
		this.Ucsys1.append(AddCaptionLeft("<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "' >" + "返回" + "</a> - <a href=Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=New ><img  border=0 src='../Img/Btn/New.gif' />新建</a>"));
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTDTitle("项目"));
		this.Ucsys1.append(AddTDTitle("输入"));
		this.Ucsys1.append(AddTDTitle("备注"));
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTD("单据类型"));//单据  单据名称
		DDL ddl=new DDL();
		ddl.setId("DDL_BillType");
		BP.WF.Data.BillTypes ens = new BP.WF.Data.BillTypes();
		ens.RetrieveAllFromDBSource();

		if (ens.size() == 0) {
			BP.WF.Data.BillType enB = new BP.WF.Data.BillType();
			enB.setName("新建类型" + "1");
			enB.setFK_Flow(this.getFK_Flow());
			enB.setNo("01");
			enB.Insert();
			ens.AddEntity(enB);
		}

		ddl.BindEntities(ens);
		ddl.SetSelectItem(bill.getFK_BillType());

		this.Ucsys1.append(AddTD(ddl));
		this.Ucsys1.append(AddTD("<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=EditType'><img src='../Img/Btn/Edit.gif' border=0/>类别维护</a>"));
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTD("编号"));
		TextBox tb=new  TextBox();
		tb.setId("TB_No");
		tb.setText(bill.getNo());
		tb.setEnabled(false);
		if (tb.getText().equals("")) {
			tb.setText("系统自动生成");
		}

		this.Ucsys1.append(AddTD(tb));
		this.Ucsys1.append(AddTD(""));
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTD("名称"));//单据 单据名称

		tb=new TextBox();
		tb.setId("TB_Name");
		tb.setText(bill.getName());
		tb.setCols(40);

		this.Ucsys1.append(AddTD("colspan=2",tb));
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTD("生成的文件类型"));//单据 单据名称
		
		ddl = new DDL();
		ddl.setId("DDL_BillFileType");;
		ddl.BindSysEnum("BillFileType");
		ddl.SetSelectItem(bill.getHisBillFileType().getValue());
	
		this.Ucsys1.append(AddTD(ddl));
		this.Ucsys1.append(AddTD("目前不支持excel,html格式."));
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTD("单据模板"));
		
// 		开始 FileUpload file = new FileUpload(); 下面替换
		TextBox file = new TextBox();
		file.setTextMode(TextBoxMode.Files);
		//结束
		file.setId("f");
		file.addAttr("width", "100%");
		
		this.Ucsys1.append(AddTD("colspan=2",file));
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTRSum());
		this.Ucsys1.append("<TD class=TD colspan=3 align=center>");


		Button btn = new Button();
		btn.setCssClass("Btn");
		btn.setId("Btn_Save");
		btn.setText("保存");
		
//		btn.Click += new EventHandler(btn_Click);增加单机事件
		btn.addAttr("onclick", "btn_Click();");
	
		this.Ucsys1.append(btn);
		if(bill.getNo().length()>1){
			btn = new Button();
			btn.setId("Btn_Del") ;
			btn.setCssClass("Btn");
			btn.setText("删除"); // "删除单据";
			//this.Ucsys1.Add(btn);
		//	btn.addAttr("onclick", "return confirm('您确认吗？');");
			//btn.Attributes["onclick"] += "return confirm('您确认吗？'); ";

			//  btn.Click += new EventHandler(btn_Del_Click);下面是替代的
			btn.addAttr("onclick", "btn_Del_Click()");
			this.Ucsys1.append(btn);
			
		}
		String url = "";
		String fileType = "";
		if (bill.getHisBillFileType() == BillFileType.RuiLang) {
			fileType = "grf";
		}
		else {
			fileType = "rtf";
		}

		//if (this.RefNo != null) {
		if(this.getRefNo()!=null){
			url = "<a href='../../DataUser/CyclostyleFile/" + bill.getUrl() + "." + fileType + "'><img src='../Img/Btn/Save.gif' border=0/> 模板下载</a>";
		}

		/*this.Ucsys1.Add(url + "</TD>");
		this.Ucsys1.AddTREnd();
		this.Ucsys1.AddTable();*/
		this.Ucsys1.append(url+"</TD>");
		this.Ucsys1.append(AddTREnd());
		this.Ucsys1.append(AddTable());
	}
	/*private void btn_Gener_Click() {
		String url = "Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=Edit&RefNo=" + this.getRefNo();
		//this.Response.Redirect(url, true);
		this.response.sendRedirect(url);
	}*/
	/*private void btn_Click(Object sender, EventArgs e) {
		//Object tempVar = this.Ucsys1.FindControl("f");
		
				
		//HtmlInputFile file = (HtmlInputFile)((tempVar instanceof HtmlInputFile) ? tempVar : null);
		FileUpload file =(FileUpload)((tempVar instanceof FileUpload) ? tempVar : null) ;		
		BillTemplate bt = new BillTemplate();
		//bt.NodeID = this.getNodeID();
		bt.setNodeID(this.getNodeID());
		//BP.WF.Node nd = new BP.WF.Node(this.getNodeID());
		Node nd = new Node(this.getNodeID());
		if(this.getRefNo()!=null){
	//	if (this.RefNo != null) {
			//bt.No = this.RefNo;
			bt.setNo(this.getRefNo());
			bt.Retrieve();
			Object tempVar2 = this.Ucsys1.Copy(bt);
			bt = (BillTemplate)((tempVar2 instanceof BillTemplate) ? tempVar2 : null);
			//bt.NodeID = this.getNodeID();
			bt.setNodeID(this.getNodeID());
			//this.Ucsys1.GetDDLByID 没有
			bt.FK_BillType = this.Ucsys1.GetDDLByID("DDL_BillType").SelectedItemStringVal;
			
			//file 可以getName吗???
			if (file.getName() == null || file.getName().trim().equals("")) {
				bt.Update();
				this.Alert("保存成功");
				return;
			}

			if(bt.getHisBillFileType()==BillFileType.RuiLang){
		//	if (bt.HisBillFileType == BillFileType.RuiLang) {
				if (file.getName().toLowerCase().contains(".grf") == false) {
					this.Alert("@错误，非法的 grf 格式文件。");
					return;
				}
			}
			else {
				if (file.getName().toLowerCase().contains(".rtf") == false) {
					this.Alert("@错误，非法的 rtf 格式文件。");
					return;
				}
			}
			String temp = "";
			String tempName = "";
			if (bt.getHisBillFileType() == BillFileType.RuiLang) {
				tempName = "Temp.grf";
				temp = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "\\Temp.grf";
				file.PostedFile.SaveAs(temp);
				
			}
			else {
				tempName = "Temp.rtf";
				temp = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "\\Temp.rtf";
				file.PostedFile.SaveAs(temp);
			}



			//检查文件是否正确。
			try {
				String[] paras = BP.DA.Cash.GetBillParas_Gener(tempName, nd.getHisFlow().getHisGERpt().getEnMap().getAttrs());
			}
			catch (RuntimeException ex) {
				this.UCSys2.append(this.AddMsgOfWarning("错误信息", ex.getMessage()));
			//	this.Ucsys2.AddMsgOfWarning("错误信息", ex.getMessage());
				return;
			}
			String fullFile = FileFullPath(file.PostedFile.FileName, bt); //BP.Sys.SystemConfig.PathOfCyclostyleFile + "\\" + bt.No + ".rtf";
			System.IO.File.Copy(temp, fullFile, true);
			bt.Update();
			return;
		}
		Object tempVar3 = this.Ucsys1.Copy(bt);
		bt = (BillTemplate)((tempVar3 instanceof BillTemplate) ? tempVar3 : null);

		if (file.getName() != null) {
			if (bt.getHisBillFileType() == BillFileType.RuiLang) {
				if (file.getName().toLowerCase().contains(".grf") == false) {
					this.Alert("@错误，非法的 grf 格式文件。");
					// this.Alert("@错误，非法的 rtf 格式文件。");
					return;
				}
			}
			else {
				if (file.getName().toLowerCase().contains(".rtf") == false) {
					this.Alert("@错误，非法的 rtf 格式文件。");
					// this.Alert("@错误，非法的 rtf 格式文件。");
					return;
				}
			}

		}
		else {
			this.Alert("请上传文件。");
			// this.Alert("@错误，非法的 rtf 格式文件。");
			return;

		}

		// 如果包含这二个字段。
		String fileName = file.PostedFile.FileName;
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		if (bt.getName().equals("")) {

			bt.setName(fileName.replace(".rtf", ""));
			bt.setName(fileName.replace(".grf", ""));
		}

		try {
			bt.setNo( BP.Tools.chs2py.convert(bt.getName())) ;
			if (bt.IsExits) {
				bt.No = bt.No + "." + BP.DA.DBAccess.GenerOID().toString();
			}
			if(bt.getIsExits()){
				bt.setNo(bt.getNo()+"."+Integer.valueOf(DBAccess.GenerOID()).toString());
			}
		}
		catch (java.lang.Exception e1) {
			//bt.No = BP.DA.DBAccess.GenerOID().toString();
			bt.setNo(Integer.valueOf(DBAccess.GenerOID()).toString());
		}
		String tmp = "";
		String tmpName = "";
		if (bt.getHisBillFileType() == BillFileType.RuiLang) {
			tmpName = "Temp.grf";
			//tmp = BP.Sys.SystemConfig.PathOfCyclostyleFile + "\\Temp.grf";
			tmp=SystemConfig.getPathOfCyclostyleFile()+ "\\Temp.grf";
			
			file.PostedFile.SaveAs(tmp);
		}
		else {
			tmpName = "Temp.rtf";
			tmp =SystemConfig.getPathOfCyclostyleFile() + "\\Temp.rtf";
			
			file.PostedFile.SaveAs(tmp);
		}


		//检查文件是否正确。
		try {
			String[] paras1 = BP.DA.Cash.GetBillParas_Gener(tmpName, nd.getHisFlow().getHisGERpt().getEnMap().getAttrs());
		}
		catch (RuntimeException ex) {
			//this.Ucsys2.AddMsgOfWarning("Error:", ex.getMessage());
			this.UCSys2.append(this.AddMsgOfWarning("Error:", ex.getMessage()));
			return;
		}

		String fullFile1 = FileFullPath(fileName, bt); //BP.Sys.SystemConfig.PathOfCyclostyleFile + "\\" + bt.No + ".rtf";
		System.IO.File.Copy(tmp, fullFile1, true);
		//Ucsys1.GetDDLByID getddlbyid 弄不错俩
		  bt.FK_BillType = this.Ucsys1.GetDDLByID("DDL_BillType").SelectedItemStringVal;
		
		bt.Insert();

		///#region 更新节点信息。
		String Billids = "";
		BillTemplates tmps = new BillTemplates(nd);
		for (BillTemplate Btmp : tmps) {
			Billids += "@" + Btmp.getNo();
		}
		nd.setHisBillIDs(Billids);
		nd.Update();
		///#endregion 更新节点信息。

		this.response.sendRedirect("Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID());
	}*/
/*	private void btn_Del_Click(Object sender, EventArgs e) {
		BillTemplate t = new BillTemplate();
		//t.No = this.RefNo;
		t.setNo(this.getRefNo());
		t.Delete();

		///#region 更新节点信息。
	//	BP.WF.Node nd = new BP.WF.Node(this.getNodeID());
		Node nd =new Node(this.getNodeID());
		String Billids = "";
		BillTemplates tmps = new BillTemplates(nd);
		for (BillTemplate tmp : tmps) {
			Billids += "@" + tmp.getNo();
		}
		//nd.getHisBillIDs() = Billids;
		nd.setHisBillIDs(Billids);
		nd.Update();
		///#endregion 更新节点信息。
		this.Response.Redirect("Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID(), true);
	}*/
	/** 
	 类别修改
	 
	*/
	public final void EditTypes() {

		this.Ucsys1.append(AddTable());
		this.Ucsys1.append(AddCaptionLeft("<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "'>返回</a> -单据类别维护"));
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTDTitle("类别编号"));
		this.Ucsys1.append(AddTDTitle("类别名称"));
		this.Ucsys1.append(AddTREnd());
		
		BillTypes ens = new BillTypes();
		ens.RetrieveAll();
		for (int i = 1; i < 18; i++) {
			//this.Ucsys1.AddTD((new Integer(i)).toString().PadLeft(2, '0'));
			//列子 en.setNo(ConvertTools.padLeft(String.valueOf(i), 2, "0"));
			//this.Ucsys1.append(ConvertTools.padLeft(String.valueOf(i), 2, "0"));
			this.Ucsys1.append(AddTD(ConvertTools.padLeft(String.valueOf(i), 2,"0")));
			TextBox tb = new TextBox();
			tb.setId("TB_" + i) ;
			tb.setCols(50);
			try {	
				//BillType en = (BillType)((ens[i - 1] instanceof BillType) ? ens[i - 1] : null);
				BillType en = (BillType)((ens.get(i-1) instanceof BillType) ? ens.get(i-1) : null);
				tb.setText(en.getName());
				//this.Ucsys1.AddTD(tb);
				
				this.Ucsys1.append(AddTD(tb));
			}
			catch (java.lang.Exception e) {
				//this.Ucsys1.AddTD(tb);
				this.Ucsys1.append(AddTD(tb));
			}
			//this.Ucsys1.AddTREnd();
			this.Ucsys1.append(AddTREnd());
		}

		//this.Ucsys1.AddTableEndWithHR();
		this.Ucsys1.append(AddTableEndWithHR());
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setText("Save");
		btn.setCssClass("Btn");

		//btn.Click += new EventHandler(btn_SaveTypes_Click);下面是踢地啊
		btn.addAttr("onclick", "btn_SaveTypes_Click()");
		//this.Ucsys1.Add(btn);
		this.Ucsys1.append(btn);
	}
	/*protected final void btn_SaveTypes_Click(Object sender, EventArgs e) {
		BillTypes ens = new BillTypes();
		ens.RetrieveAll();
		ens.Delete();
		for (int i = 1; i < 18; i++) {
			String name = this.Ucsys1.GetTextBoxByID("TB_" + i).getText();
			if (StringHelper.isNullOrEmpty(name)) {
				continue;
			}

			BillType en = new BillType();
			en.No = (new Integer(i)).toString().PadLeft(2, '0');
			
			en.setName(name);
			//en.FK_Flow = this.getFK_Flow();
			en.setFK_Flow(this.getFK_Flow());
			en.Insert();
		}
		this.Alert("保存成功.");
	}*/
	public void loadPage() throws IOException {
		this.Title = "节点单据设计"; //"节点单据设计";
		if ("Edit".equals(this.getDoType())) {
				BillTemplate bk1 = new BillTemplate(this.getRefNo());
				//bk1.NodeID = this.getNodeID();
				bk1.setNodeID(this.getNodeID());
				this.DoNew(bk1);
				return;
		}
		else if ("New".equals(this.getDoType())) {
				BillTemplate bk = new BillTemplate();
				//bk.NodeID = this.RefOID;
				bk.setNodeID(this.getRefOID());
				this.DoNew(bk);
				return;
		}
		else if ("EditType".equals(this.getDoType())) {
				EditTypes();
				return;
		}
		else {
		}
	
		BillTemplates Bills = new BillTemplates(this.getNodeID());
		if (Bills.size() == 0) {
			sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=New");
			return;
		}

		Node nd =new Node(this.getNodeID());
		this.Title = nd.getName() + " - " + "单据管理"; //单据管理
	
		this.Ucsys1.append(AddTable());
		if (this.getRefNo() == null) {
			this.Ucsys1.append(AddCaptionLeft("<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=New'><img src='../Img/Btn/New.gif' border=0/>新建</a> -<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=EditType'><img src='../Img/Btn/Edit.gif' border=0/>类别维护</a>"));
		}
	
		this.Ucsys1.append(AddTR());
		this.Ucsys1.append(AddTDTitle("IDE"));
		this.Ucsys1.append(AddTDTitle("编号"));
		this.Ucsys1.append(AddTDTitle("名称"));
		this.Ucsys1.append(AddTDTitle("操作"));
		this.Ucsys1.append(AddTREnd());
		
		int i = 0;
	
		for (BillTemplate Bill : Bills.ToJavaList()) {
			i++;
		
			this.Ucsys1.append(AddTR());
			this.Ucsys1.append(AddTDIdx(i));
			this.Ucsys1.append(AddTD(Bill.getNo()));
			String fileUrl = "";
			//../WorkOpt/GridEdit.jsp?grf=" +Bill.Url + ".grf&t="+DateTime.Now.ToString("yyMMddhh:mm:ss")+" target='_blank'
			if (Bill.getHisBillFileType() == BillFileType.RuiLang) {
				String name = Bill.getUrl();

				name = name.replace('\\', '-');

				//if (name.Split('\\').Count() > 2)
				//{
				//    string tempName = "";
				//    foreach (string single in name.Split('\\'))
				//    {
				//        tempName += single + "-";
				//    }
				//    name = tempName.substing(0, tempName.Length - 1);
				//}
				fileUrl = "<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=Edit&RefNo=" + Bill.getNo() + "'><img src='../Img/Btn/Edit.gif' border=0/编辑/a>|<a href='../../../DataUser/CyclostyleFile/" + Bill.getUrl() + ".grf'><img src='../Img/Btn/Save.gif' border=0/> 模板下载</a>|<a href='javascript:openEidt(\"" + name + "\")'  ><img src='../Img/Btn/Edit.gif' />编辑模版</a>";
			}
			else {
				fileUrl = "<a href='Bill.jsp?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID() + "&DoType=Edit&RefNo=" + Bill.getNo() + "'><img src='../Img/Btn/Edit.gif' border=0/编辑/a>|<a href='../../DataUser/CyclostyleFile/" + Bill.getUrl() + ".rtf'  ><img src='../Img/Btn/Save.gif' border=0/> 模板下载</a>";
			}
			this.Ucsys1.append(AddTD("<img src='../Img/Btn/Word.gif' >" + Bill.getName()));
			this.Ucsys1.append(AddTD(fileUrl));
			this.Ucsys1.append(AddTREnd());
		}
		this.Ucsys1.append(AddTableEnd());
	}
}
	


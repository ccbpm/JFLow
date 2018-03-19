package cn.jflow.common.model;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.XML.WorkOptDtlXml;
import BP.WF.XML.WorkOptDtlXmls;
import BP.Web.WebPage;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.FileUpload;
import cn.jflow.system.ui.core.ListItem;

public class DtlOptModel extends BaseModel {

	
	public StringBuffer Pub1=null;

	public StringBuilder Pub2 = null;
	
	public DtlOptModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuffer();
		Pub2=new StringBuilder();
	}
	public final long getWorkID()
	{
		return Long.parseLong(this.get_request().getParameter("WorkID"));
	}
	public final String getFK_MapDtl()
	{
		return this.get_request().getParameter("FK_MapDtl");
	}
	public  void init()
	{
		//this.Page.Title = "明细选项";

		WorkOptDtlXmls xmls = new WorkOptDtlXmls();
		xmls.RetrieveAll();
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		this.Pub1.append("\t\n<div id='tabsJ'  align='center'>");
		this.Pub1.append("\t\n<ul>");
		for (WorkOptDtlXml item : xmls.ToJavaList())
		{

//			switch (item.getNo())
			if (item.getNo().equals("UnPass"))
			{
					if (!dtl.getIsEnablePass())
					{
						continue;
					}
			}
			else if (item.getNo().equals("SelectItems"))
			{
					////if (!dtl.getIsEnableSelectImp())
					//{
						//.dtl..dtl.continue;
					//}
			}
			else
			{
			}
			String url = item.getURL() + "?DoType=" + item.getNo() + "&WorkID=" + this.getWorkID() + "&FK_MapDtl=" + this.getFK_MapDtl();
			url = url.replace(".aspx", ".jsp");
			this.Pub1.append(BaseModel.AddLi("<a href=\"" + url + "\" ><span>" + item.getName() + "</span></a>"));
		}
		this.Pub1.append("\t\n</ul>");
		this.Pub1.append("\t\n</div>");


//		switch (this.getDoType())
		if (this.getDoType().equals("UnPass"))
		{
				this.BindUnPass();
		}
//ORIGINAL LINE: case "ExpImp":
		else if (this.getDoType().equals("ExpImp"))
		{
			this.BindExpImp();//
		}
		else
		{
			this.BindExpImp();
		}
	}
	private void BindExpImp()
	{
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		String flag = StringHelper.isEmpty(getParameter("Flag"), "");
		boolean isfiles = false;
		if (flag.equals("ExpTemplete"))
		{
			String filepath = this.get_request().getSession().getServletContext().getRealPath("/") + "\\DataUser\\DtlTemplete\\" + this.getFK_MapDtl() + ".xlsx";
			File file1 = new File(filepath);
			if (file1.exists())
			{
				isfiles = true;
				try {
					BP.Sys.PubClass.OpenExcel(filepath, dtl.getName() + ".xls");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String file = this.get_request().getSession().getServletContext().getRealPath("/") + "\\DataUser\\DtlTemplete\\" + this.getFK_MapDtl() + ".xls";
			File fileSys = new File(file);
			if (fileSys.exists())
			{
				isfiles = true;
				try {
					BP.Sys.PubClass.OpenExcel(file, dtl.getName() + ".xls");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(!isfiles){
				this.winCloseWithMsg("设计错误：流程设计人员没有把该导入的从表模版放入" + file);
				return;
			}
			this.WinClose();
			return;
		}

		if (flag.equals("ExpData"))
		{
			GEDtls dtls = new GEDtls(this.getFK_MapDtl());
			dtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
			String filepath = WebPage.ExportDGToExcelV2(dtls, dtl.getName() + ".xls");
			try {
				BP.Sys.PubClass.OpenExcel(filepath,dtl.getName() + ".xls");
			} catch (IOException e) {
				//e.printStackTrace();
			}
			this.WinClose();
			return;
		}

		if (dtl.getIsExp())
		{
			this.Pub1.append(BaseModel.AddFieldSet("数据导出"));
			this.Pub1.append("点下面的连接进行本从表的导出，您可以根据列的需要增减列。");
			String urlExp = "DtlOpt.jsp?DoType=" + this.getDoType() + "&WorkID=" + this.getWorkID() + "&FK_MapDtl=" + this.getFK_MapDtl() + "&Flag=ExpData";
			this.Pub1.append("<a href='" + urlExp + "' target=_blank ><img src='../Img/FileType/xls.gif' border=0 /><b>导出数据</b></a>");
			this.Pub1.append(BaseModel.AddFieldSetEnd());
		}

		if (dtl.getImpModel()!=0)
		{
			this.Pub1.append(BaseModel.AddFieldSet("通过Excel导入:" + dtl.getName()));
			this.Pub1.append("下载数据模版:利用数据模板导出一个数据模板，您可以在此基础上进行数据编辑，把编辑好的信息<br>在通过下面的功能导入进来，以提高工作效率。");
			String url = "DtlOpt.jsp?DoType=" + this.getDoType() + "&WorkID=" + this.getWorkID() + "&FK_MapDtl=" + this.getFK_MapDtl() + "&Flag=ExpTemplete";
			this.Pub1.append("<a href='" + url + "' target=_blank ><img src='../Img/FileType/xls.gif' border=0 />数据模版</a>");
			this.Pub1.append("<br>");

			this.Pub1.append("格式数据文件:");
			FileUpload fu = new FileUpload();
			fu.setId("fup");
			this.Pub1.append(fu);

			DDL ddl = new DDL();
			ddl.Items.add(new ListItem("选择导入方式", "all"));
			ddl.Items.add(new ListItem("清空方式", "0"));
			ddl.Items.add(new ListItem("追加方式", "1"));
			ddl.setId("DDL_ImpWay");
			ddl.setName("DDL_ImpWay");
			this.Pub1.append(ddl);

			Button btn = new Button();
			btn.setType("button");
			btn.setText("导入");
			btn.addAttr("onclick", "DtlOptImport('"+dtl.getNo()+"')");
			btn.setCssClass("Btn");
			btn.setId("Btn_" + dtl.getNo());

			this.Pub1.append(btn);
			this.Pub1.append(BaseModel.AddFieldSetEnd());
		}

		 
	}
//	private void btn_Exp_Click(Object sender, EventArgs e)
//	{
//		Button btn = (Button)((sender instanceof Button) ? sender : null);
//		String id = btn.ID.Replace("Btn_Exp", "");
//
//		MapDtl dtl = new MapDtl(id);
//		GEDtls dtls = new GEDtls(id);
//		this.ExportDGToExcelV2(dtls, dtl.getName() + ".xls");
//		return;
//	}
	private void BindUnPass()
	{
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		Node nd = new Node(dtl.getFK_MapData());
		MapData md = new MapData(dtl.getFK_MapData());

		String starter = "SELECT Rec FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID();
		starter = BP.DA.DBAccess.RunSQLReturnString(starter);

		GEDtls geDtls = new GEDtls(dtl.getNo());
		geDtls.Retrieve(GEDtlAttr.Rec, starter, "IsPass", "0");

		MapAttrs attrs = new MapAttrs(dtl.getNo());
		this.Pub1.append(BaseModel.AddTable());
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("IDX"));

		if (geDtls.size() > 0)
		{
			String str1 = "<INPUT id='checkedAll' onclick='selectAll()' type='checkbox' name='checkedAll'>";
			this.Pub1.append(BaseModel.AddTDTitle(str1));
		}
		else
		{
			this.Pub1.append(BaseModel.AddTDTitle());
		}

		String spField = ",Checker,Check_RDT,Check_Note,";

		for (MapAttr attr : attrs.ToJavaList())
		{
			if (!attr.getUIVisible() && !spField.contains("," + attr.getKeyOfEn() + ","))
			{
				continue;
			}

			this.Pub1.append(BaseModel.AddTDTitle(attr.getName()));
		}
		this.Pub1.append(BaseModel.AddTREnd());
		int idx = 0;
		for (GEDtl item : geDtls.ToJavaList())
		{
			idx++;
			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddTDIdx(idx));
			CheckBox cb = new CheckBox();
			cb.setId("CB_" + item.getOID());
			this.Pub1.append(BaseModel.AddTD(cb));
			for (MapAttr attr : attrs.ToJavaList())
			{
				if (!attr.getUIVisible() && !spField.contains("," + attr.getKeyOfEn() + ","))
				{
					continue;
				}

				if (attr.getMyDataType() == BP.DA.DataType.AppBoolean)
				{
					this.Pub1.append(BaseModel.AddTD(item.GetValBoolStrByKey(attr.getKeyOfEn())));
					continue;
				}

				switch (attr.getUIContralType())
				{
					case DDL:
						this.Pub1.append(BaseModel.AddTD(item.GetValRefTextByKey(attr.getKeyOfEn())));
						continue;
					default:
						this.Pub1.append(BaseModel.AddTD(item.GetValStrByKey(attr.getKeyOfEn())));
						continue;
				}
			}
			this.Pub1.append(BaseModel.AddTREnd());
		}
		this.Pub1.append(BaseModel.AddTableEndWithHR());

		if (geDtls.size() == 0)
		{
			return;
		}

		if (!nd.getIsStartNode())
		{
			return;
		}

		Button btn = new Button();
		btn.setId("Btn_Delete");
		btn.setCssClass("Btn");
		btn.setText("批量删除");
		btn.addAttr("onclick"," return confirm('您确定要执行吗？');");
		//btn.Click += new EventHandler(btn_DelUnPass_Click);
		this.Pub1.append(btn);

		btn = new Button();
		btn.setId("Btn_Imp");
		btn.setCssClass("Btn");
		btn.setText("导入并重新编辑(追加方式)");

		//btn.Click += new EventHandler(btn_Imp_Click);
		this.Pub1.append(btn);

		btn = new Button();
		btn.setId("Btn_ImpClear");
		btn.setCssClass("Btn");
		btn.setText("导入并重新编辑(清空方式)");
		//btn.Click += new EventHandler(btn_Imp_Click);
		this.Pub1.append(btn);
	}


}

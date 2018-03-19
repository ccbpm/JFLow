package cn.jflow.common.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.Button;
import BP.DA.DataType;
import BP.En.Entities;
import BP.Sys.GEEntity;
import BP.WF.Data.Bill;
import BP.WF.Template.*;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Work;
import BP.WF.Works;
import BP.Web.WebUser;

public class PrintDocModel extends BaseModel {
	
	public StringBuilder Pub1 = new StringBuilder();

	public final String getFK_Bill() {
		return get_request().getParameter("FK_Bill");
	}

	private boolean isRuiLang = false;

	public final boolean getIsRuiLang() {
		return isRuiLang;
	}

	public final void setIsRuiLang(boolean value) {
		isRuiLang = value;
	}

	public PrintDocModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	public final void loadData()
	{
		BillTemplates templetes = new BillTemplates();
		templetes.Retrieve(BillTemplateAttr.NodeID, this.getFK_Node());
		if (templetes.size() == 0)
		{
			winCloseWithMsg("当前节点上没有绑定单据模板。");
			return;
		}

		if (templetes.size() == 1)
		{
		   PrintDocV3((BillTemplate)((templetes.get(0) instanceof BillTemplate) ? templetes.get(0) : null));
			return;
		}

		appendPub1(AddTable());
		appendPub1(AddCaptionLeft("请选择要打印的单据"));
		appendPub1(AddTR());
		appendPub1(AddTDTitle("单据编号"));
		appendPub1(AddTDTitle("单据名称"));
		appendPub1(AddTDTitle("打印"));
		appendPub1(AddTREnd());

		for (BillTemplate en : templetes.ToJavaList())
		{
			appendPub1(AddTR());
			appendPub1(AddTD(en.getNo()));
			appendPub1(AddTD(en.getName()));
			appendPub1(AddTD("<a href='PrintDoc.jsp?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Bill=" + en.getNo() + "&FK_Flow="+this.getFK_Flow()+"' >打印</a>"));
			appendPub1(AddTREnd());
		}
		appendPub1(AddTableEnd());

		if (this.getFK_Bill() != null)
		{
			BillTemplate templete = new BillTemplate(this.getFK_Bill());

			if (templete.getHisBillFileType() == BillFileType.RuiLang)
			{
				this.PrintDocV4(templete);
			}
			else
			{
				this.PrintDocV2(templete);
			}
		}
	}
	
	/** 
	 瑞郎
	 
	 @param func
	*/
	public final void PrintDocV4(BillTemplate func)
	{
		setIsRuiLang(true);

		Button button = new Button();
		button.attributes.put("onclick", "return btnPreview_onclick('"+func.getUrl()+"')");

		button.setText("预览 '"+func.getName()+"'");

		appendPub1(button.toString());
	}

	public final void PrintDocV3(BillTemplate funcs)
	{

		switch (funcs.getHisBillFileType())
			{
				case Word:
				case Excel:
					PrintDocV2(funcs);
					break;
				case RuiLang:
					PrintDocV4(funcs);
					break;
				default:
					break;
			}


	}
	/** 
	 打印单据
	 
	 @param func
	*/
	public final void PrintDocV2(BillTemplate func)
	{
		String billInfo = "";
		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();
		wk.ResetDefaultVal();

		String file = new Date().getYear() + "_" + WebUser.getFK_Dept() + "_" + func.getNo() + "_" + getWorkID() + ".doc";
		BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();

		Works works;
		String[] paths;
		String path;
		try
		{
			// region 生成单据
			rtf.getHisEns().clear();
			rtf.getEnsDataDtls().clear();
			if (func.getNodeID() == 0)
			{

			}
			else
			{
				//WorkNodes wns = new WorkNodes();
				//if (nd.HisRunModel == RunModel.FL
				//    || nd.HisRunModel == RunModel.FHL
				//    || nd.HisRunModel == RunModel.HL)
				//    wns.GenerByFID(nd.HisFlow, this.WorkID);
				//else
				//    wns.GenerByWorkID(nd.HisFlow, this.WorkID);

				//把流程主表数据放入里面去.
				GEEntity ndxxRpt = new GEEntity("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt");
				ndxxRpt.setPKVal(this.getWorkID());
				ndxxRpt.Retrieve();
				ndxxRpt.Copy(wk);

				//把数据赋值给wk.
				wk.setRow(ndxxRpt.getRow());
				rtf.HisGEEntity = wk;

				//加入他的明细表.
				ArrayList<Entities> al = wk.GetDtlsDatasOfList();
				for (Entities ens : al)
				{
					rtf.AddDtlEns(ens);
				}

				//rtf.AddEn(wk);
				////if (wns.Count == 0)
				////    works = nd.HisWorks;
				////else
				////    works = wns.GetWorks;
				//foreach (Work mywk in works)
				//{
				//    if (mywk.OID == 0)
				//        continue;
				//    rtf.AddEn(mywk);
				//    rtf.ensStrs += ".ND" + mywk.NodeID;
				//}

			}

			paths = file.split("[_]", -1);
			path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";

			String billUrl = getBasePath()+ "DataUser/Bill/" + path + file;

			if (func.getHisBillFileType() == BillFileType.PDF)
			{
				billUrl = billUrl.replace(".doc", ".pdf");
				billInfo += "<img src='"+getBasePath()+"WF/Img/FileType/PDF.gif' /><a href='" + billUrl + "' target=_blank >" + func.getName() + "</a>";
			} else {
				billInfo += "<img src='"+getBasePath()+"WF/Img/FileType/doc.gif' /><a href='" + billUrl + "' target=_blank >" + func.getName() + "</a>";
			}

			path = BP.WF.Glo.getFlowFileBill() + new Date().getYear() + "/" + WebUser.getFK_Dept() + "/" + func.getNo() + "/";
			File pathFile = new File(path);
			
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
//			path = Server.MapPath(path);
//			if (System.IO.Directory.Exists(path) == false)
//			{
//				System.IO.Directory.CreateDirectory(path);
//			}

			rtf.MakeDoc(func.getUrl() + ".rtf", path, file, func.getReplaceVal(), false);
			// endregion

			// region 转化成pdf.
			if (func.getHisBillFileType() == BillFileType.PDF)
			{
				String rtfPath = path + file;
				String pdfPath = rtfPath.replace(".doc", ".pdf");
				try
				{
					BP.WF.Glo.Rtf2PDF(rtfPath, pdfPath);
				}
				catch (RuntimeException ex)
				{
					billInfo = ex.getMessage();
					//this.addMsg("RptError", "产生报表数据错误:" + ex.Message);
				}
			}
			// endregion

			// region 保存单据
			Bill bill = new Bill();
			bill.setMyPK(wk.getFID() + "_" + wk.getOID() + "_" + nd.getNodeID() + "_" + func.getNo());
			bill.setFID(wk.getFID());
			bill.setWorkID(wk.getOID());
			bill.setFK_Node(wk.getNodeID());
			bill.setFK_Dept(WebUser.getFK_Dept());
			bill.setFK_Emp(WebUser.getNo());
			bill.setUrl(billUrl);
			bill.setRDT(DataType.getCurrentDataTime());
			bill.setFullPath(path + file);
			bill.setFK_NY(DataType.getCurrentYearMonth());
			bill.setFK_Flow(nd.getFK_Flow());
			bill.setFK_BillType(func.getFK_BillType());
			bill.setEmps(rtf.HisGEEntity.GetValStrByKey("Emps"));
			bill.setFK_Starter(rtf.HisGEEntity.GetValStrByKey("Rec"));
			bill.setStartDT(rtf.HisGEEntity.GetValStrByKey("RDT"));
			bill.setTitle(rtf.HisGEEntity.GetValStrByKey("Title"));
			bill.setFK_Dept(rtf.HisGEEntity.GetValStrByKey("FK_Dept"));
			try {
				bill.Insert();
			} catch (Exception e)
			{
				bill.Update();
			}
			// endregion
		}
		catch (RuntimeException ex)
		{
			BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
			dir.Do();
			path = Glo.getIntallPath() + "DataUser/Bill/" + new Date().getYear() + "/" + WebUser.getFK_Dept() + "/" + func.getNo() + "/";
			
			String msgErr = "@" + String.format("生成单据失败，请让管理员检查目录设置") + "[" + BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file + " @Path:" + path;
			billInfo += "@<font color=red>" + msgErr + "</font>";
			throw new RuntimeException(msgErr + "@其它信息:" + ex.getMessage());
		}

		billInfo = "<h3>&nbsp;&nbsp;&nbsp;&nbsp;打印单据</h3><ul><li>" + billInfo + "</li></ul>";

		appendPub1(billInfo);
		return;
	}

	public final void PrintDoc(BillTemplate en)
	{
		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();
		String msg = "";
		String file = DataType.getCurrentYear() + "_" + WebUser.getFK_Dept() + "_" + en.getNo() + "_" + this.getWorkID() + ".doc";
		BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();
		//        Works works;
		String[] paths;
		String path;
		try
		{
			// region 生成单据
			rtf.getHisEns().clear();
			rtf.getEnsDataDtls().clear();
			rtf.AddEn(wk);
			rtf.ensStrs += ".ND" + wk.getNodeID();
			ArrayList<Entities> al = wk.GetDtlsDatasOfArrayList();
			for (Entities ens : al)
			{
				rtf.AddDtlEns(ens);
			}

			BP.Sys.GEEntity ge = new BP.Sys.GEEntity("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt");
			ge.Copy(wk);
			rtf.HisGEEntity = ge;

			paths = file.split("[_]", -1);
			path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";

			path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "/" + WebUser.getFK_Dept() + "/" + en.getNo() + "/";
			File pathFile = new File(path);
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
			
//			if (System.IO.Directory.Exists(path) == false)
//			{
//				System.IO.Directory.CreateDirectory(path);
//			}
			// rtf.ensStrs = ".ND";
			rtf.MakeDoc(en.getUrl() + ".rtf", path, file, en.getReplaceVal(), false);
			// endregion

			// region 转化成pdf.
			if (en.getHisBillFileType() == BillFileType.PDF)
			{
				String rtfPath = path + file;
				String pdfPath = rtfPath.replace(".doc", ".pdf");
				try
				{
					BP.WF.Glo.Rtf2PDF(rtfPath, pdfPath);

					file = file.replace(".doc", ".pdf");
					new File(rtfPath).delete();
					//System.IO.File.Delete(rtfPath);

					//file = file.replace(".doc", ".pdf");
					//System.IO.File.Delete(rtfPath);
				}
				catch (RuntimeException ex)
				{
					msg += ex.getMessage();
				}
			}
			// endregion

			String url = getBasePath() + "DataUser/Bill/" + DataType.getCurrentYear() + "/" + WebUser.getFK_Dept() + "/" + en.getNo() + "/" + file;
			try {
				get_response().sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//         BP.Sys.PubClass.OpenWordDocV2( path+file, en.Name);
		}
		catch (RuntimeException ex)
		{
			BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
			dir.Do();
			path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "/" + WebUser.getFK_Dept() + "/" + en.getNo() + "/";
			String msgErr = "@生成单据失败，请让管理员检查目录设置 [" + BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file + " @Path:" + path;
			throw new RuntimeException(msgErr + "@其它信息:" + ex.getMessage());
		}
	}

	
	private void appendPub1(String str){
		Pub1.append(str);
	}


}

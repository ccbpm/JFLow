package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.WF.Data.Bill;
import BP.WF.Data.BillAttr;
import BP.WF.Data.Bills;
import BP.WF.Node;

public class AthModel extends BaseModel{

	public StringBuilder Pub1 = null;
	private String basePath;
	private String FK_Flow;
	private int OID;
	private int FID;
	private int FK_Node;
	public AthModel(HttpServletRequest request, HttpServletResponse response,String basePath,String FK_Flow,int OID,int FID,int FK_Node) {
		super(request, response);
		Pub1 = new StringBuilder();
		this.basePath=basePath;
		this.FK_Flow=FK_Flow;
		this.OID=OID;
		this.FID=FID;
		this.FK_Node=FK_Node;
	}

	public void init(){
		String sql = "SELECT * FROM Sys_FrmAttachmentDB WHERE FK_FrmAttachment IN (SELECT MyPK FROM Sys_FrmAttachment WHERE  "+ BP.WF.Glo.MapDataLikeKey(this.FK_Flow, "FK_MapData") + "  AND IsUpload=1) AND RefPKVal='" + this.OID + "' ORDER BY RDT";
		
		//string sql = "SELECT * FROM Sys_FrmAttachmentDB WHERE FK_FrmAttachment IN (SELECT MyPK FROM Sys_FrmAttachment WHERE  FK_MapData ='ND"+FK_Node+"'  AND IsUpload=1) AND RefPKVal='" + this.OID + "' ORDER BY RDT";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (dt.Rows.size() > 0)
		{

			Pub1.append(this.AddTable("class='am-table am-table-striped am-table-hover table-main' cellpadding='0' cellspacing='0' border='0' style='width: 100%'"));
			Pub1.append(this.AddTR());
			Pub1.append(this.AddTDGroupTitle("style='text-align:center'", "序"));
			Pub1.append(this.AddTDGroupTitle("附件编号"));
			Pub1.append(this.AddTDGroupTitle("节点"));
			Pub1.append(this.AddTDGroupTitle("名称"));
			Pub1.append(this.AddTDGroupTitle("大小(kb)"));
			Pub1.append(this.AddTDGroupTitle("上传人"));
			Pub1.append(this.AddTDGroupTitle("上传日期"));
			Pub1.append(this.AddTREnd());
			int i = 0;

			for(DataRow dr:dt.Rows)
			{
				i++;
				Pub1.append(this.AddTR());
				Pub1.append(this.AddTDIdx(i));
				Pub1.append(this.AddTD(dr.getValue("FK_FrmAttachment").toString()));
				String nodeName = "";
				try
				{
					int nodeID = Integer.parseInt(dr.getValue("NodeID").toString());
					Node node = new Node(nodeID);
					nodeName = node.getName();

				}
				catch (Exception ex)
				{ 
					ex.printStackTrace();
				}

				Pub1.append(this.AddTD(nodeName));
				Pub1.append(this.AddTD("<a href="+basePath+"WF/CCForm/AttachmentUpload.jsp?DoType=Down&MyPK=" + dr.getValue("MyPK") + " target=_sd ><img src="+basePath+"WF/Img/FileType/" + dr.getValue("FileExts") + ".gif onerror=\"this.src='"+basePath+"WF/Img/FileType/Undefined.gif'\" border=0/>" + dr.getValue("FileName").toString() + "</a>"));
				Pub1.append(this.AddTD(dr.getValue("FileSize").toString()));
				Pub1.append(this.AddTD(dr.getValue("RecName").toString()));
				Pub1.append(this.AddTD(dr.getValue("RDT").toString()));
				Pub1.append(this.AddTREnd());
			}
			Pub1.append(this.AddTableEnd());
		}

		Bills bills = new Bills();
		bills.Retrieve(BillAttr.WorkID, this.OID);
		if (bills.size() > 0)
		{
			Pub1.append(this.AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width: 100%'"));
			Pub1.append(this.AddTR());
			Pub1.append(this.AddTDGroupTitle("style='text-align:center'", "序"));
			Pub1.append(this.AddTDGroupTitle("名称"));
			Pub1.append(this.AddTDGroupTitle("节点"));
			Pub1.append(this.AddTDGroupTitle("打印人"));
			Pub1.append(this.AddTDGroupTitle("日期"));
			Pub1.append(this.AddTDGroupTitle("功能"));
			Pub1.append(this.AddTREnd());
			int idx = 0;
			for (Bill bill: bills.ToJavaList())
			{
				idx++;
				Pub1.append(this.AddTR());//.AddTR();
				
				Pub1.append(this.AddTDIdx(idx));//.AddTDIdx(idx);

				Pub1.append(this.AddTD(bill.getFK_BillTypeT()));//.AddTD(bill.getFK_BillTypeT());

				Pub1.append(this.AddTD(bill.getFK_NodeT()));//.AddTD(bill.FK_NodeT);
				Pub1.append(this.AddTD(bill.getFK_EmpT()));//.AddTD(bill.FK_EmpT);
				Pub1.append(this.AddTD(bill.getRDT()));//.AddTD(bill.RDT);
				Pub1.append(this.AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-print'\" href='" + basePath+ "WF/Rpt/bill.jsp?MyPK=" + bill.getMyPK() + "&DoType=Print' >打印</a>"));//.AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-print'\" href='" + basePath+ "WF/Rpt/Bill.aspx?MyPK=" + bill.getMyPK() + "&DoType=Print' >打印</a>");
				Pub1.append(this.AddTREnd());
			}
			Pub1.append(this.AddTableEnd());
		}

		int num = bills.size() + dt.Rows.size();
		if (num == 0)
		{
			
			Pub1.append(this.AddEasyUiPanelInfo("提示", "<h3>当前流程没有数据，或者该流程没有附件或者单据。</h3>"));
		}

	}
}

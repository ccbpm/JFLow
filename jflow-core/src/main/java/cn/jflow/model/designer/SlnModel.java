package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataTable;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachments;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtlAttr;
import BP.Sys.MapDtls;
import BP.WF.WorkAttr;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.TextBox;

public class SlnModel extends BaseModel{
	private String FK_MapData;
	private String FK_Flow;
	private String FK_Node;
	private String KeyOfEn;
	private String Ath;
	private String Title;
	public DataTable dtNodes;
	public StringBuilder Pub1=null;
	

	public SlnModel(HttpServletRequest request, HttpServletResponse response,String FK_MapData,String FK_Flow,String FK_Node,String
			KeyOfEn,String Ath,DataTable dtNodes) {
		super(request, response);
		this.FK_MapData=FK_MapData;
		this.FK_Flow=FK_Flow;
		this.FK_Node=FK_Node;
		this.KeyOfEn=KeyOfEn;
		this.Ath=Ath;
		this.dtNodes=dtNodes;
	}
		///#endregion 属性.

	public void init()
	{
		this.Pub1 = new StringBuilder();
		///#region 功能执行.
		if (this.getDoType().equals("DeleteFJ"))
		{
			FrmAttachment ath1 = new FrmAttachment();
			ath1.setMyPK(this.getFK_MapData() + "_" + this.getAth() + "_" + FK_Node);
			ath1.Delete();
			WinClose();
			return;
		}
		// 删除
		if (this.getDoType().equals("DeleteDtl"))
        {
            MapDtl dtl = new MapDtl();
            dtl.setNo(getParameter("FK_MapDtl") + "_" + FK_Node);
            dtl.Delete();
            WinClose();
            return;
        }
		///#endregion 功能执行.


//		MapData md = new MapData(this.getFK_MapData());
		FrmField sln = new FrmField();
		sln.CheckPhysicsTable();
		if (this.getDoType().equals("FJ")) // 附件方案.
		{
			this.Title = "表单附件权限";
			BindFJ();
		} else if (this.getDoType().equals("Dtl")) {
			this.Title = "从表权限";
			this.BindDtl();
		} else // 字段方案.
		{
			this.Title = "表单字段权限";
			this.BindSln();
		}
	}

	  public void BindDtl() {
		MapDtls dtls = new MapDtls();
		dtls.Retrieve(MapDtlAttr.FK_MapData, this.FK_MapData);

		this.Pub1.append(AddTable("width='100%'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("原始属性"));
		this.Pub1.append(AddTDTitle("编辑"));
		this.Pub1.append(AddTDTitle("删除"));
		this.Pub1.append(AddTREnd());

		int idx = 0;
		for (MapDtl item : dtls.ToJavaList())
		{
			if (item.getFK_Node() != 0)
			{
				continue;
			}

			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD(item.getNo()));
			this.Pub1.append(AddTD(item.getName()));
			this.Pub1.append(AddTD("<a href=\"javascript:EditDtlYuanShi('" + this.getFK_MapData() + "','" + item.getNo() + "')\">原始属性</a>"));
			this.Pub1.append(AddTD("<a href=\"javascript:EditDtl('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNo() + "')\">编辑</a>"));

			MapDtl en = new MapDtl();
			en.setNo(item.getNo() + "_" + this.getFK_Node());
			if (en.RetrieveFromDBSources() == 0)
			{
				this.Pub1.append(AddTD());
			}
			else
			{
				this.Pub1.append(AddTD("<a href=\"javascript:DeleteDtl('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNo() + "')\">删除</a>"));
			}

			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());

	}

	public final void BindFJ()
	{
		FrmAttachments fas = new FrmAttachments();
		fas.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData());

		this.Pub1.append(AddTable("width='100%'"));
		this.Pub1.append(AddCaptionLeft("表单附件权限."));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("类型"));
		this.Pub1.append(AddTDTitle("编辑"));
		this.Pub1.append(AddTDTitle("删除"));
		this.Pub1.append(AddTREnd());

		int idx = 0;
		for (FrmAttachment item : fas.ToJavaList())
		{
			if (item.getFK_Node() != 0)
			{
				continue;
			}

			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD(item.getNoOfObj()));
			this.Pub1.append(AddTD(item.getName()));
			this.Pub1.append(AddTD(item.getUploadTypeT()));
			this.Pub1.append(AddTD("<a href=\"javascript:EditFJ('"+this.getFK_Node()+"','"+this.getFK_MapData()+"','"+item.getNoOfObj()+"')\">编辑</a>"));

			FrmAttachment en = new FrmAttachment();
			en.setMyPK(this.getFK_MapData() + "_" + item.getNoOfObj() + "_" + this.getFK_Node());
			if (en.RetrieveFromDBSources()==0)
			{
				this.Pub1.append(AddTD());
			}
			else
			{
				this.Pub1.append(AddTD("<a href=\"javascript:DeleteFJ('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">删除</a>"));
			}

			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());
	}
	/** 
	 绑定方案
	 
	*/
	public final void BindSln()
	{
		// 查询出来解决方案.
		FrmFields fss = new FrmFields(this.getFK_MapData(),this.getFK_Node());

		// 处理好.
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());

		this.Pub1.append(AddTable("80%"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("字段"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("类型"));

		this.Pub1.append(AddTDTitle("width='90px'","可见？"));
		this.Pub1.append(AddTDTitle("<input type='checkbox' id='s' onclick=\"CheckAll('UIIsEnable')\" />可用？"));

		this.Pub1.append(AddTDTitle("是否是签名？"));
		this.Pub1.append(AddTDTitle("默认值"));

		this.Pub1.append(AddTDTitle("<input type='checkbox' id='s' onclick=\"CheckAll('IsNotNull')\" />检查必填？"));
		this.Pub1.append(AddTDTitle("正则表达式"));

		this.Pub1.append(AddTDTitle("<input type='checkbox' id='s' onclick=\"CheckAll('IsWriteToFlowTable')\" />写入流程数据表？"));
		this.Pub1.append(AddTDTitle(""));
		this.Pub1.append(AddTREnd());

		CheckBox cb = new CheckBox();
		TextBox tb = new TextBox();

		int idx = 0;
		for (MapAttr attr : attrs.ToJavaList())
		{
			if(attr.getKeyOfEn().equals(WorkAttr.RDT)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.FID)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.OID)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.Rec)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.MyNum)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.MD5)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.Emps)){
				 continue;
			}else if(attr.getKeyOfEn().equals(WorkAttr.CDT)){
				 continue;
			}

			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD(attr.getKeyOfEn()));
			this.Pub1.append(AddTD(attr.getName()));
			this.Pub1.append(AddTD(attr.getLGTypeT()));

			Object tempVar = fss.GetEntityByKey(FrmFieldAttr.KeyOfEn, attr.getKeyOfEn());
			FrmField sln = (FrmField)((tempVar instanceof FrmField) ? tempVar : null);
			if (sln == null)
			{
				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setChecked(attr.getUIVisible());
				cb.setText("可见？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setChecked(attr.getUIIsEnable());
				cb.setText("可用？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setName("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setChecked(attr.getIsSigan());
				cb.setText("是否数字签名？");
				this.Pub1.append(AddTD("width=150px", cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setName("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setText(attr.getDefValReal());
				this.Pub1.append(AddTD(tb));


				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsNotNull);
				cb.setName("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsNotNull);
			   // cb.Checked = attr.IsNotNull;
				cb.setChecked(false);
				cb.setText("检查必填？");
				this.Pub1.append(AddTD(cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.RegularExp);
				tb.setName("TB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.RegularExp);
				//tb.Text = attr.RegularExp;
			  //  tb.Columns = 150;
				this.Pub1.append(AddTD(tb));


				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_"+FrmFieldAttr.IsWriteToFlowTable);
				cb.setName("CB_" + attr.getKeyOfEn() + "_"+FrmFieldAttr.IsWriteToFlowTable);
				cb.setChecked(false);
				cb.setText("是否写入流程表？");
				this.Pub1.append(AddTD(cb));

				this.Pub1.append(AddTD());
				//this.Pub2.AddTD("<a href=\"javascript:EditSln('" + this.FK_MapData + "','" + this.SlnString + "','" + attr.KeyOfEn + "')\" >Edit</a>");
			}
			else
			{
				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setChecked(sln.getUIVisible());
				cb.setText("可见？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setChecked(sln.getUIIsEnable());
				cb.setText("可用？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setName("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setChecked(sln.getIsSigan());
				cb.setText("是否数字签名？");
				this.Pub1.append(AddTD("width=150px", cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setName("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setText(sln.getDefVal());
				this.Pub1.append(AddTD(tb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_"+FrmFieldAttr.IsNotNull);
				cb.setChecked(sln.getIsNotNull());
				cb.setText("必填？");
				this.Pub1.append(AddTD(cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_RegularExp");
				tb.setName("TB_" + attr.getKeyOfEn() + "_RegularExp");
				tb.setText(sln.getRegularExp());
				this.Pub1.append(AddTD(tb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsWriteToFlowTable);
				cb.setName("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsWriteToFlowTable);
				cb.setChecked(sln.getIsWriteToFlowTable());
				cb.setText("写入流程数据表？");
				this.Pub1.append(AddTD(cb));

				this.Pub1.append(AddTD("<a href=\"javascript:DelSln('" + this.getFK_MapData() + "','"+this.getFK_Flow()+"','"+this.getFK_Node()+"','" + this.getFK_Node() + "','" + attr.getKeyOfEn() + "')\" ><img src='../Img/Btn/Delete.gif' border=0/>Delete</a>"));
			}
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());

		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setName("Btn_Save");
		//btn.Click += new EventHandler(btn_Field_Click);
		btn.addAttr("onclick", "btn_Field_Click('Btn_Save')");
		btn.setText(" Save ");
		this.Pub1.append(btn); //保存.

		if (fss.size() != 0)
		{
			btn = new Button();
			btn.setId("Btn_Del");
			btn.setName("Btn_Del");
			//btn.Click += new EventHandler(btn_Field_Click);
			btn.addAttr("onclick", "btn_Field_Click('Btn_Del')");
			btn.setText(" Delete All ");
//			btn.Attributes["onclick"] = "return confirm('Are you sure?');";
			this.Pub1.append(btn); //删除定义..
		}

		if (dtNodes.Rows.size() >= 1)
		{
			btn = new Button();
			btn.setId("Btn_Copy");
			btn.setName("Btn_Copy");
			//btn.Click += new EventHandler(btn_Field_Click);
			btn.addAttr("onclick", "btn_Field_Click('Btn_Copy')");
			btn.setText(" Copy From Node ");
//			btn.Attributes["onclick"] = "CopyIt('" + this.getFK_MapData() + "','" + this.getFK_Node() + "')";
			this.Pub1.append(btn); //删除定义..
		}
	}

	


}

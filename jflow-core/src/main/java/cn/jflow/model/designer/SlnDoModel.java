package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.TextBox;

public class SlnDoModel extends BaseModel{
	public StringBuilder Pub1=new StringBuilder();
	public String KeyOfEn;
	public String FK_Node;

	public SlnDoModel(HttpServletRequest request, HttpServletResponse response, String KeyOfEn, String FK_Node) {
		super(request, response);
		this.Pub1=new StringBuilder();
		this.KeyOfEn=KeyOfEn;
		this.FK_Node=FK_Node;
	}
	public  void init()
	{

//		switch (this.DoType)
//ORIGINAL LINE: case "DelSln":
		if (this.getDoType().equals("DelSln")) //删除sln.
		{
				FrmField sln = new FrmField();
				sln.Delete(FrmFieldAttr.FK_MapData, this.getFK_MapData(), FrmFieldAttr.KeyOfEn, KeyOfEn, FrmFieldAttr.FK_Flow, this.getFK_Flow(), FrmFieldAttr.FK_Node, FK_Node);
				this.WinClose();
				return;
		}
//ORIGINAL LINE: case "EditSln":
		else if (this.getDoType().equals("EditSln")) //编辑sln.
		{
				this.EditSln();
				return;
		}
//ORIGINAL LINE: case "Copy":
		else if (this.getDoType().equals("Copy")) //编辑sln.
		{
				this.Copy();
				return;
		}
//ORIGINAL LINE: case "CopyIt":
		else if (this.getDoType().equals("CopyIt")) //编辑sln.
		{
				FrmFields fss = new FrmFields();
				try {
					fss.Delete(FrmFieldAttr.FK_MapData, this.getFK_MapData(), FrmFieldAttr.FK_Flow, this.getFK_Flow(), FrmFieldAttr.FK_Node, FK_Node);
				} catch (Exception e) {
					e.printStackTrace();
				}

				fss = new FrmFields(this.getFK_MapData(),Integer.parseInt(this.get_request().getParameter("FromSln")));
				//fss.Retrieve(FrmFieldAttr.FK_MapData, this.FK_MapData,
				//    FrmFieldAttr.FK_Node, this.Request.QueryString["FromSln"]);

				for (FrmField sl : fss.ToJavaList())
				{
					sl.setFK_Node(Integer.valueOf(FK_Node));
					sl.setFK_Flow(this.getFK_Flow());
					sl.setMyPK(this.getFK_MapData() + "_" +this.getFK_Flow()+"_"+ this.getFK_Node() + "_" + sl.getKeyOfEn());
					sl.Insert();
				}
				this.WinClose();
				return;
		}
		else
		{
		}
	}
	/** 
	 执行复制.
	 
	*/
	public final void Copy()
	{
		String sql = "SELECT NodeID, Name, Step FROM WF_Node WHERE NodeID IN (SELECT FK_Node FROM Sys_FrmSln WHERE FK_MapData='" + this.getFK_MapData() + "' )";
		DataTable dtNodes = BP.DA.DBAccess.RunSQLReturnTable(sql);

		this.Pub1.append(AddFieldSet("请选择要copy的节点."));

		this.Pub1.append(AddUL());
		for (DataRow dr : dtNodes.Rows)
		{
			String name = "步骤:" + dr.getValue(2) + ",节点ID:" + dr.getValue(0) + ":" + dr.getValue(1).toString();
			String no = dr.getValue(0).toString();
			if (no!=null&&FK_Node.equals(no))
			{
				continue;
			}
			else
			{
				this.Pub1.append(AddLi("<a href='SlnDo.jsp?FK_MapData=" + this.getFK_MapData() + "&FromSln=" + no +"&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + FK_Node + "&DoType=CopyIt' >" + name + "</a>"));
			}
		}
		this.Pub1.append(AddULEnd());

		this.Pub1.append(AddFieldSetEnd());

	}
	public final void EditSln()
	{
		FrmField sln = new FrmField();
		int num = sln.Retrieve(FrmFieldAttr.FK_MapData, this.getFK_MapData(), FrmFieldAttr.KeyOfEn, KeyOfEn, FrmFieldAttr.FK_Node, FK_Node);

		MapAttr attr = new MapAttr();
		attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, KeyOfEn);

		if (num == 0)
		{
			sln.setUIIsEnable(attr.getUIIsEnable());
			sln.setUIVisible(attr.getUIVisible());
			sln.setIsSigan(attr.getIsSigan());
			sln.setDefVal(attr.getDefValReal());
		}

		this.Pub1.append(AddTable());
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("项目"));
		this.Pub1.append(AddTDTitle("信息"));
		this.Pub1.append(AddTDTitle("备注"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("字段"));
		this.Pub1.append(AddTD(attr.getKeyOfEn()));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("中文名"));
		this.Pub1.append(AddTD(attr.getName()));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD());
		CheckBox cb = new CheckBox();
		cb.setId("CB_Visable");
		cb.setName("CB_Visable");
		cb.setText("是否可见?");
		cb.setChecked(sln.getUIVisible());

		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTD("在该方案中是否可见？"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD());
		cb = new CheckBox();
		cb.setId("CB_Readonly");
		cb.setName("CB_Readonly");
		cb.setText("是否只读?");
		cb.setChecked(sln.getUIIsEnable());
		this.Pub1.append(AddTD(cb));
		this.Pub1.append(AddTD("在该方案中是否只读？"));
		this.Pub1.append(AddTREnd());

		if (attr.getMyDataType()== DataType.AppString)
		{
			//只读，并且是String. 
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD());
			cb = new CheckBox();
			cb.setId("CB_IsSigan");
			cb.setName("CB_IsSigan");
			cb.setText("是否是数字签名?");
			cb.setChecked(sln.getIsSigan());
			this.Pub1.append(AddTD(cb));
			this.Pub1.append(AddTD("如果是，并且需要在当前方案显示当前人员的签名：<br>请在默认值里输入WebUser.No"));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("默认值"));
		TextBox tb = new TextBox();
		tb.setId("TB_DefVal");
		tb.setName("TB_DefVal");
		tb.setText(sln.getDefVal());
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD("支持ccflow的全局变量."));
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());

		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setName("Btn_Save");
		btn.setText("Save");

		//btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onClick", "btn_Click()");
		this.Pub1.append(btn);

		this.Pub1.append(AddFieldSet("流程表单中的数字签名设置方法"));
		this.Pub1.append(AddBR("应用概述:"));
		this.Pub1.append(AddBR("1, 一个流程表单上会有多出数字签名. "));
		this.Pub1.append(AddBR("2, 这些数字签名有时会读取以前的签名，有时需要当前的数字签名。"));
		this.Pub1.append(AddBR("3, 如果当前方案需要读取以前的数字签名，那就清除默认值信息，否则就设置WebUser.No 获取当前操作员的数字签名。"));
		this.Pub1.append(AddFieldSetEnd());
	}

}

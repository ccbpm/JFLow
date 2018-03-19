package cn.jflow.common.model;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.En.QueryObject;
import BP.Sys.GENoName;
import BP.Sys.GENoNames;
import BP.Sys.SFTable;
import BP.Tools.StringHelper;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.TextBox;

public class SFTableEditDataModel extends BaseModel{

	public SFTableEditDataModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	public StringBuilder Pub1=new StringBuilder();
	public StringBuilder Pub2=new StringBuilder();
	public StringBuilder Pub3=new StringBuilder();
	
	public String IDX;
	public String Title;
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public void setIDX(String iDX) {
		IDX = iDX;
	}
	public String getIDX()
	{
		return get_request().getParameter("IDX");
	}
	public String getRefNo() {
		String s = this.get_request().getParameter("FK_SFTable");
		if(StringUtils.isEmpty(s))
			s = this.get_request().getParameter("RefNo");
		return s;
	}
	public void init()
	{
		if (get_request().getParameter("EnPK") != null)
		{
			GENoName en = new GENoName(this.getRefNo(), "");
			en.setNo(get_request().getParameter("EnPK"));
			en.Delete();
		}

		this.setTitle("编辑表数据");
		this.BindSFTable();
	}

	public final void BindSFTable()
	{
		SFTable sf = new SFTable(this.getRefNo());

		/*
		 * warning var canEdit = sf.getFK_SFDBSrc().equals("local"); //todo:此处判断不准确，需更加精确的判断??
*/
		boolean canEdit=false;
		if(sf.getFK_SFDBSrc().equals("local")){
			canEdit=true;
		}
		this.Title = (canEdit ? "编辑:" : "查看:") + sf.getName();
		this.Pub1.append(AddTable("class='table' cellpadding='1' cellspacing='1' border='1' style='width:100%'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDGroupTitle("style='width:80px;text-align:center' class=\"GroupTitle\"", "编号"));
		this.Pub1.append(AddTDGroupTitle("名称"));

		if (canEdit)
		{
			this.Pub1.append(AddTDGroupTitle("style='width:80px' class=\"GroupTitle\"", "操作"));
		}

		this.Pub1.append(AddTREnd());

		GENoNames ens = new GENoNames(sf.getNo(), sf.getName());
		QueryObject qo = new QueryObject(ens);
		try
		{
			BindPageIdxEasyUi(Pub2,qo.GetCount(), "SFTableEditData.jsp?RefNo=" + this.getRefNo(), this.getPageIdx(),10,
					"'first','prev','sep','manual','sep','next','last'", false);
		}
		catch (java.lang.Exception e)
		{
			sf.CheckPhysicsTable();
			BindPageIdxEasyUi(Pub2,qo.GetCount(), "SFTableEditData.jsp?RefNo=" + this.getRefNo(), this.getPageIdx(),10,
					"'first','prev','sep','manual','sep','next','last'", false);
		}

		qo.DoQuery("No", 10, this.getPageIdx(), false);

		for (GENoName en : ens.ToJavaList())
		{
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(en.getNo()));
			TextBox tb = new TextBox();
			tb.setId("TB_" + en.getNo());
			tb.setName("TB_" + en.getNo());
			tb.setText(en.getName());
			tb.addAttr("style", "width:99%");
			tb.setReadOnly(!canEdit);

			this.Pub1.append(AddTD(tb));

			if(canEdit)
			{
				this.Pub1.append(AddTD("<a href=\"javascript:Del('" + this.getRefNo() + "','" + this.getPageIdx() + "','" + en.getNo() + "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-delete'\" >删除</a>"));
			}

			this.Pub1.append(AddTREnd());
		}

		if (canEdit)
		{
			GENoName newen = new GENoName(sf.getNo(), sf.getName());
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx("新记录"));
			TextBox tb1 = new TextBox();
			tb1.setId("TB_Name");
			tb1.setName("TB_Name");
			tb1.setText(newen.getName());
			tb1.addAttr("style", "width:99%");

			this.Pub1.append(AddTD(tb1));

//			LinkBtn btn = new LinkBtn(false, NamesOfBtn.Save, "保存");
			Button btn = new Button();
			btn.setId("Btn_Save");
			btn.setName("Btn_Save");
			btn.setText("保存");
			btn.setCssClass("Btn");

//			btn.Click += new EventHandler(btn_Click);
			btn.addAttr("onclick", "btn_Click()");

			this.Pub1.append(AddTD(btn));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTableEnd());

		//this.Pub3.AddTable();
		//this.Pub3.AddTRSum();
		//this.Pub3.AddTD("编号");
		//this.Pub3.AddTD("名称");
		//this.Pub3.AddTD("");
		//this.Pub3.AddTREnd();

		//GENoName newen = new GENoName(sf.No, sf.Name);
		//this.Pub3.AddTRSum();
		//this.Pub3.AddTD(newen.GenerNewNo);
		//TextBox tbn = new TextBox();
		//tbn.ID = "TB_Name";

		//this.Pub3.AddTD(tbn);
		//Button btn = new Button();
		//btn.Text = "增加";
		//btn.Click += new EventHandler(btn_Click);
		//this.Pub3.AddTD(btn);
		//this.Pub3.AddTREnd();
		//this.Pub3.AddTableEnd();
	}
}

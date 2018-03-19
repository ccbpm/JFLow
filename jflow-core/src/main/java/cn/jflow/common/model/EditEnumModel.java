package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import BP.En.EditType;
import BP.En.UIContralType;
import BP.Sys.SysEnumMain;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapExtXmlList;
import BP.Tools.StringHelper;

public class EditEnumModel extends BaseModel{

	public EditEnumModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	public String Title;
	public UiFatory Pub1=new UiFatory();
	/** 
	 GroupField
	 
	*/
	public final int getGroupField()
	{
        String s = get_request().getParameter("GroupField");
        if (StringHelper.isNullOrEmpty(s))
            return 0;
        return Integer.parseInt(s);
	}
	/** 
	 执行类型
	 
	*/
	public final String getDoType()
	{
		return get_request().getParameter("DoType");
	}
	public final String getFType()
	{
		return get_request().getParameter("FType");
	}
	public final String getIDX()
	{
		return get_request().getParameter("IDX");
	}
	public final void init()
	{
		//this.Response.Write(this.Request.RawUrl);
		this.Title = "编辑枚举类型"; // "编辑枚举类型";
		MapAttr attr = null;

		if (this.getRefNo() == null)
		{
			attr = new MapAttr();
			String enumKey = get_request().getParameter("EnumKey");
			if (enumKey != null)
			{
				SysEnumMain se = new SysEnumMain(enumKey);
				attr.setKeyOfEn(enumKey);
				attr.setUIBindKey(enumKey);
				attr.setName(se.getName());
				attr.setName(se.getName());
			}
		}
		else
		{
			attr = new MapAttr(this.getRefNo());
		}
		BindEnum(attr);
	}
	private int idx = 1;
	public final void BindEnum(MapAttr mapAttr)
	{
		this.Pub1.append(AddTable("width='100%'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("ID"));
		this.Pub1.append(AddTDTitle("项目"));
		this.Pub1.append(AddTDTitle("采集"));
		this.Pub1.append(AddTDTitle("说明"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段中文名"));
		TextBox tb = Pub1.creatTextBox("TB_Name");
//		tb.ID = "TB_Name";
		tb.setText(mapAttr.getName());
		tb.addAttr("style", "width:100%");
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段英文名"));
//		 tb = new TB();
		if (this.getRefNo() != null)
		{
			this.Pub1.append(AddTD(mapAttr.getKeyOfEn()));
		}
		else
		{
			tb = Pub1.creatTextBox("TB_KeyOfEn");
//			tb.ID = "TB_KeyOfEn";
			tb.setText(mapAttr.getKeyOfEn());
			this.Pub1.append(AddTD(tb));
		}

		if (StringHelper.isNullOrEmpty(mapAttr.getKeyOfEn()))
		{
			this.Pub1.append(AddTD("字母/数字/下划线组合"));
		}
		else
		{
			this.Pub1.append(AddTD("<a href=\"javascript:clipboardData.setData('Text','" + mapAttr.getKeyOfEn() + "');alert('已经copy到粘帖版上');\" ><img src='../Img/Btn/Copy.GIF' class='ICON' />复制字段名</a></TD>"));
		}

		// this.Pub1.AddTDTitle("&nbsp;");
		//this.Pub1.AddTD("不要以数字开头、不要中文。");
		this.Pub1.append(AddTREnd());



		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("默认值"));

		DDL ddl = Pub1.creatDDL("DDL");
//		ddl.ID = "DDL";
		ddl.BindSysEnum(mapAttr.getUIBindKey());
		ddl.SetSelectItem(mapAttr.getDefVal());
		this.Pub1.append(AddTD(ddl));
		this.Pub1.append(AddTD("<a href='SysEnum.jsp?RefNo=" + mapAttr.getUIBindKey()+ "'>编辑</a>"));
		this.Pub1.append(AddTREnd());


		///#region 是否可编辑
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否可编辑"));
		this.Pub1.append(AddTDBegin());
		RadioButton rb = Pub1.creatRadioButton("RB_UIIsEnable_0");
//		rb.ID = "RB_UIIsEnable_0";
		rb.setText("不可编辑");
		rb.setGroupName("s");
		if (mapAttr.getUIIsEnable())
		{
			rb.setChecked(false);
		}
		else
		{
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		rb = Pub1.creatRadioButton("RB_UIIsEnable_1");
//		rb.ID = "RB_UIIsEnable_1";
		rb.setText("可编辑");
		rb.setGroupName("s");

		if (mapAttr.getUIIsEnable())
		{
			rb.setChecked(true);
		}
		else
		{
			rb.setChecked(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());
		///#endregion 是否可编辑


		///#region 展示控件
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("控件类型"));
		this.Pub1.append(AddTDBegin());
		 rb = Pub1.creatRadioButton("RB_Ctrl_0");
//		 rb.ID = "RB_Ctrl_0";
		rb.setText("下拉框");
		rb.setGroupName("Ctrl");
		if (mapAttr.getUIContralType() == UIContralType.DDL)
		{
			rb.setChecked(true);
		}
		else
		{
			rb.setChecked(false);
		}

		this.Pub1.append(rb);
		rb = Pub1.creatRadioButton("RB_Ctrl_1");
//		rb.ID = "RB_Ctrl_1";
		rb.setText("单选按钮");
		rb.setGroupName("Ctrl");

		if (mapAttr.getUIContralType() == UIContralType.DDL)
		{
			rb.setChecked(false);
		}
		else
		{
			rb.setChecked(true);
		}
		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());
		///#endregion 展示控件


		///#region 是否可界面可见
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否界面可见")); //是否界面可见
		this.Pub1.append(AddTDBegin());
		rb = Pub1.creatRadioButton("RB_UIVisible_0");
//		rb.ID = "RB_UIVisible_0";
		rb.setText("不可见"); // 界面不可见
		rb.setGroupName("sa3");
		if (mapAttr.getUIVisible())
		{
			rb.setChecked(false);
		}
		else
		{
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		if (mapAttr.getIsTableAttr())
		{
			rb.setEnabled(false);
		}

		rb = Pub1.creatRadioButton("RB_UIVisible_1");
//		rb.ID = "RB_UIVisible_1";
		rb.setText("界面可见"); // 界面可见;
		rb.setGroupName("sa3");

		if (mapAttr.getUIVisible())
		{
			rb.setChecked(true);
		}
		else
		{
			rb.setChecked(false);
		}

		if (mapAttr.getIsTableAttr())
		{
			rb.setEnabled(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD("不可见则为隐藏字段."));
		this.Pub1.append(AddTREnd());
		///#endregion 是否可界面可见


		///#region 合并单元格数
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("合并单元格数"));
		ddl = Pub1.creatDDL("DDL_ColSpan");
//		ddl.ID = "DDL_ColSpan";
		for (int i = 1; i < 12; i++)
		{
			ddl.Items.add(new ListItem((new Integer(i)).toString(),(new Integer(i)).toString()));
		}
		ddl.SetSelectItem(String.valueOf(mapAttr.getColSpan()));
		this.Pub1.append(AddTD(ddl));
		this.Pub1.append(AddTD("对傻瓜表单有效"));
		this.Pub1.append(AddTREnd());
		///#endregion 合并单元格数


		///#region 字段分组
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段分组"));
		DDL ddlGroup = Pub1.creatDDL("DDL_GroupID");
//		ddlGroup.ID = "DDL_GroupID";
		GroupFields gfs = new GroupFields(mapAttr.getFK_MapData());
		ddlGroup.Bind(gfs, GroupFieldAttr.OID, GroupFieldAttr.Lab);
		if (mapAttr.getGroupID() == 0)
		{
			mapAttr.setGroupID(this.getGroupField());
		}

		ddlGroup.SetSelectItem(mapAttr.getGroupID());
		this.Pub1.append(AddTD(ddlGroup));
		this.Pub1.append(AddTD("修改隶属分组"));
		this.Pub1.append(AddTREnd());
		///#endregion 字段分组

		if (this.getRefNo() != null) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx++));
			String html = "<a href=\"javascript:WinOpen('./MapExt/DDLFullCtrl.jsp?FK_MapData=" + mapAttr.getFK_MapData() + "&RefNo=" + mapAttr.getKeyOfEn() + "&MyPK=" + mapAttr.getFK_MapData() + "_" + MapExtXmlList.DDLFullCtrl + "_" + mapAttr.getKeyOfEn() + "')\">下拉框自动完成</a>";

			html += " - <a href=\"javascript:WinOpen('./MapExt/RadioBtns.htm?FK_MapData=" + mapAttr.getFK_MapData() + "&KeyOfEn=" + mapAttr.getKeyOfEn() + "&MyPK=" + mapAttr.getFK_MapData() + "_" + MapExtXmlList.DDLFullCtrl + "_" + mapAttr.getKeyOfEn() + "')\">高级JS设置</a>";

			this.Pub1.append(AddTD(html));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTRSum());
		this.Pub1.append("<TD colspan=4 >");
		Button btn = Pub1.creatButton("Btn_Save");
//		btn.ID = "Btn_Save";
		btn.setCssClass("Btn");
		btn.setText(" 保存 ");

//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_Save')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndClose");
//		btn.ID = "Btn_SaveAndClose";
		btn.setCssClass("Btn");
		btn.setText("保存并关闭");

//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_SaveAndClose')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndNew");
//		btn.ID = "Btn_SaveAndNew";
		btn.setCssClass("Btn");
		btn.setText("保存并新建");

//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_SaveAndNew')");
		this.Pub1.append(btn);
		if (this.getRefNo() != null)
		{
			btn = Pub1.creatButton("Btn_AutoFull");
//			btn.ID = "Btn_AutoFull";
			btn.setCssClass("Btn");
			btn.setText("扩展设置");
			//  btn.Click += new EventHandler(btn_Save_Click);
//			btn.Attributes["onclick"] = "javascript:WinOpen('AutoFull.aspx?RefNo=" + this.RefNo + "&FK_MapData=" + mapAttr.FK_MapData + "',''); return false;";
			btn.addAttr("onclick", "javascript:WinOpen('AutoFullNew.jsp?RefNo=" + this.getRefNo() + "&FK_MapData=" + mapAttr.getFK_MapData() + "',''); return false;");
			this.Pub1.append(btn);

			if (mapAttr.getHisEditType() == EditType.Edit)
			{
				btn = Pub1.creatButton("Btn_Del");
//				btn.ID = "Btn_Del";
				btn.setCssClass("Btn");
				btn.setText("删除");

//				btn.Click += new EventHandler(btn_Save_Click);
				btn.addAttr("onclick", "btn_Save_Click('Btn_Del')");
//				btn.Attributes["onclick"] = " return confirm('您确认吗？');";
				this.Pub1.append(btn);
			}

			String myUrl = "EleBatch.jsp?KeyOfEn=" + mapAttr.getKeyOfEn() + "&FK_MapData=" + mapAttr.getFK_MapData() + "&EleType=MapAttr";
			this.Pub1.append("<a href='" + myUrl + "' target='M"+mapAttr.getKeyOfEn()+"' ><img src='../Img/Btn/Apply.gif' border=0>批处理</a>");
		}

		String url = "Do.jsp?DoType=AddF&MyPK=" + mapAttr.getFK_MapData() + "&IDX=" + mapAttr.getIdx();
		this.Pub1.append("<a href='" + url + "'><img src='../Img/Btn/New.gif' border=0>新建</a></TD>");


		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEndWithBR());


	}
	public final String getGetCaption()
	{
		if (this.getDoType().equals("Add"))
		{
			return "增加新字段向导 - <a href='Do.jsp?DoType=ChoseFType&GroupField=" + this.getGroupField() + "'>选择类型</a>";
		}
		else
		{
			return " <a href='Do.jsp?DoType=ChoseFType&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo() + "&GroupField=" + this.getGroupField() + "'>编辑</a>";
		}
	}


}

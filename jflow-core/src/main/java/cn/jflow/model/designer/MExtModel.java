package cn.jflow.model.designer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.ExpFucnXml;
import BP.Sys.ExpFucnXmls;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.M2MType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXml;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExtXmls;
import BP.Sys.MapExts;
import BP.Sys.MapM2M;
import BP.Sys.MapM2Ms;
import BP.Sys.ToolbarExcel;
import BP.Sys.XML.RegularExpressions;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.FileUpload;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class MExtModel extends BaseModel{

	
	public StringBuffer Left=null;
	
	public StringBuffer Pub2=null;
	
	UiFatory uf=null;
	
	private String basePath;
	
	public MExtModel(HttpServletRequest request, HttpServletResponse response,String basePath) {
		super(request, response);
		Left=new StringBuffer();
		Pub2=new StringBuffer();
		uf=new UiFatory();
		this.basePath=basePath;
	}
	

    public String Lab = null;
    public void ShowExtMenu()
    {
        Pub2.append("\t\n<div id='tabsJ'  align='center'>");
        MapExtXmls fss = new MapExtXmls();
        fss.RetrieveAll();

        Pub2.append(AddUL());
        for (MapExtXml fs: fss.ToJavaList())
        {
            if (this.getExtType() == fs.getNo())
            {
               // this.Lab = fs.Name;
            	Pub2.append(AddLiB(basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo(), "<span>" + fs.getName() + "</span>"));
            }
            else
            	Pub2.append(AddLi(basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo(), "<span>" + fs.getName() + "</span>"));
        }
        Pub2.append(AddLi("<a href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "'><span>帮助</span></a>"));
        Pub2.append(AddULEnd());
        Pub2.append(AddDivEnd());  
    }
//	
	//#region 属性。
//    public string FK_MapData
//    {
//        get
//        {
//            return this.Request.QueryString["FK_MapData"];
//        }
//    }

    public String getOperAttrKey()
    {
            return this.get_request().getParameter("OperAttrKey");
    }
    public String getExtType()
    {
            String s = this.get_request().getParameter("ExtType");
            if ("".equals(s))
                s = null;
            return s;
    }

    //#endregion 属性。

    /// <summary>
    /// BindLeft
    /// </summary>
    public void BindLeft()
    {
        if (this.getExtType() == MapExtXmlList.StartFlow)
            return;

        MapExtXmls fss = new MapExtXmls();
        fss.RetrieveAll();

        //this.Left.Add("<a href='http://ccflow.org' target=_blank  ><img src='../../DataUser/ICON/" + SystemConfig.CompanyID + "/LogBiger.png' style='width:180px;' /></a><hr>");
        //this.Left.AddUL();
        this.Left.append(AddUL("class='navlist'"));

        for (MapExtXml fs: fss.ToJavaList())
        {
            if (this.getExtType() == fs.getNo())
            {
                this.Lab = fs.getName();
                //this.Left.AddLiB(fs.URL + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.No + "&RefNo=" + this.getRefNo(), "<span>" + fs.Name + "</span>");
                if(this.getRefNo()!=null && !this.getRefNo().equals("")){
                	this.Left.append("<li style=\"font-weight:bold\"><div><a href=\""+basePath+"WF/MapDef/" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo().substring(0,this.getRefNo().indexOf(".")) + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
                }else{
                	this.Left.append("<li style=\"font-weight:bold\"><div><a href=\""+basePath+"WF/MapDef/" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
                }
            }
            else
            {
                //this.Left.AddLi(fs.URL + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.No + "&RefNo=" + this.getRefNo(), "<span>" + fs.Name + "</span>");
            	if(this.getRefNo()!=null && !this.getRefNo().equals("")){
            		if(this.getRefNo().indexOf(".")!=-1){
            			this.Left.append("<li><div><a href=\""+basePath+"WF/MapDef/" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo().substring(0,this.getRefNo().indexOf(".")) + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
            		}
            		else{
            			this.Left.append("<li><div><a href=\""+basePath+"WF/MapDef/" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
            		}
            	}else{
            		this.Left.append("<li><div><a href=\""+basePath+"WF/MapDef/" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
            	}
            }
        }
        if(this.getRefNo()!=null && !this.getRefNo().equals("")){
        	if(this.getRefNo().indexOf(".")!=-1){
        		this.Left.append("<li" + (StringHelper.isNullOrEmpty(this.Lab) ? " style='font-weight:bold'" : "") + "><div><a href=\""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo().substring(0,this.getRefNo().indexOf(".")) + "\"><span class=\"nav\">帮助</span></a></div></li>");
        	}else{
        		this.Left.append("<li" + (StringHelper.isNullOrEmpty(this.Lab) ? " style='font-weight:bold'" : "") + "><div><a href=\""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">帮助</span></a></div></li>");
        	}
        }else{
        	this.Left.append("<li" + (StringHelper.isNullOrEmpty(this.Lab) ? " style='font-weight:bold'" : "") + "><div><a href=\""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">帮助</span></a></div></li>");
        }
        //this.Left.AddLi("<a href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'><span>帮助</span></a>");
        if (StringHelper.isNullOrEmpty(this.Lab))
            this.Lab = "帮助";

        this.Left.append(AddULEnd());
    }

    public void BindLeftV1()
    {
        this.Left.append("\t\n<div id='tabsJ'  align='center'>");
        MapExtXmls fss = new MapExtXmls();
        fss.RetrieveAll();

        this.Left.append(AddUL());
        for (MapExtXml fs: fss.ToJavaList())
        {
            if (this.getExtType() == fs.getNo())
            {
                this.Lab = fs.getName();
                this.Left.append(AddLiB(fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo(), "<span>" + fs.getName() + "</span>"));
            }
            else
                this.Left.append(AddLi(fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo(), "<span>" + fs.getName() + "</span>"));
        }
        this.Left.append(AddLi("<a href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'><span>帮助</span></a>"));
        this.Left.append(AddULEnd());
        this.Left.append(AddDivEnd());
    }
    /// <summary>
    /// 新建文本框自动完成
    /// </summary>
    public void EditAutoFullM2M_TB()
    {
        MapExt myme = new MapExt(this.getMyPK());
        MapM2Ms m2ms = new MapM2Ms(myme.getFK_MapData());

        //this.Pub2.append(AddH2("设置自动填充从表. <a href='?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a>"));
        if (m2ms.size() == 0)
        {
//            this.Pub2.clear();
//            this.Pub2.append(AddFieldSet("设置自动填充从表. <a href='?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a>"));
//            this.Pub2.append("该表单下没有从表，所以您不能为从表设置自动填充。");
//            this.Pub2.append(AddFieldSetEnd());
            return;
        }
        Pub2.append(AddTableNormal());
        Pub2.append(AddTRGroupTitle("设置自动填充从表. <a href='"+basePath+"WF/MapDef/MapExt.jsp?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a>"));

        String[] strs = myme.getTag2().split("$");
//        boolean is1 = false;
        boolean isHaveM2M = false;
        boolean isHaveM2MM = false;
        for (MapM2M m2m: m2ms.ToJavaList())
        {
            if (m2m.getHisM2MType() == M2MType.M2M)
                isHaveM2M = true;
            if (m2m.getHisM2MType() == M2MType.M2MM)
                isHaveM2MM = true;

            TextBox tb = new TextBox();
            tb.setId("TB_" + m2m.getNoOfObj());
            tb.setName("TB_" + m2m.getNoOfObj());
            tb.setCols(70);
            tb.attributes.put("style", "width:100%");
            tb.setRows(5);
            tb.setTextMode(TextBoxMode.MultiLine);
            for(String s:strs)
            {
                if (s == null)
                    continue;

                if (s.contains(m2m.getNoOfObj() + ":") == false)
                    continue;

                String[] ss = s.split(":");
                tb.setText(ss[1]);
            }
            //this.Pub2.AddFieldSet("编号:" + m2m.NoOfObj + ",名称:" + m2m.Name);
            Pub2.append(AddTR());
            Pub2.append(AddTDBegin());
            Pub2.append("编号:" + m2m.getNoOfObj() + ",名称:" + m2m.getName());
            Pub2.append(tb);
            Pub2.append(AddTDEnd());
            Pub2.append(AddTREnd());
            //this.Pub2.AddFieldSetEnd();
        }
        //this.Pub2.AddHR();
        Pub2.append(AddTableEnd());
        Pub2.append(AddBR());

        Button mybtn = new Button();
        mybtn.setId("Btn_Save");
        mybtn.setName("Btn_Save");
        mybtn.setCssClass("Btn");
        mybtn.setText("保存");
        mybtn.addAttr("onclick", "mybtn_SaveAutoFullM2M_Click()");
        Pub2.append(mybtn);
        Pub2.append(AddSpace(1));

        mybtn = new Button();
        mybtn.setCssClass("Btn");
        mybtn.setId("Btn_Cancel");
        mybtn.setName("Btn_Cancel");
        mybtn.setText("取消");
        mybtn.addAttr("onclick", "mybtn_CancelAutoFullDtl_Click()");
        Pub2.append(mybtn);
        Pub2.append(AddBR());
        //this.Pub2.AddFieldSetEnd();
        
        if (isHaveM2M)
        {
//            this.Pub2.append(AddFieldSet("帮助:一对多"));
        	this.Pub2.append(AddEasyUiPanelInfoBegin("帮助:一对多", "icon-help"));
            this.Pub2.append("在主表相关数据发生变化后，一对多数据要发生变化，变化的格式为：");
            this.Pub2.append(AddBR("实例：SELECT No,Name FROM WF_Emp WHERE FK_Dept='@Key' "));
            this.Pub2.append(AddBR("相关内容的值发生改变时而自动填充checkbox。"));
            this.Pub2.append(AddBR("注意:"));
            this.Pub2.append(AddBR("1，@Key 是主表字段传递过来的变量。"));
            this.Pub2.append(AddBR("2，必须并且仅有No,Name两个列，顺序不要颠倒。"));
//            this.Pub2.append(AddFieldSetEnd());
            this.Pub2.append(AddEasyUiPanelInfoEnd());
        }

        if (isHaveM2MM)
        {
//            this.Pub2.append(AddFieldSet("帮助:一对多多"));
        	this.Pub2.append(AddEasyUiPanelInfoBegin("帮助:一对多多", "icon-help"));
            this.Pub2.append("在主表相关数据发生变化后，一对多多数据要发生变化，变化的格式为：");
            this.Pub2.append(AddBR("实例：SELECT a.FK_Emp M1ID, a.FK_Station as M2ID, b.Name as M2Name FROM Port_EmpStation a, Port_Station b WHERE  A.FK_Station=B.No and a.FK_Emp='@Key'"));
            this.Pub2.append(AddBR("相关内容的值发生改变时而自动填充checkbox。"));
            this.Pub2.append(AddBR("注意:"));
            this.Pub2.append(AddBR("1，@Key 是主表字段传递过来的变量。"));
            this.Pub2.append(AddBR("2，必须并且仅有3个列 M1ID,M2ID,M2Name，顺序不要颠倒。第1列的ID对应列表的ID，第2，3列对应的是列表数据源的ID与名称。"));
//            this.Pub2.append(AddFieldSetEnd());
            this.Pub2.append(AddEasyUiPanelInfoEnd());
        }
    }
    /// <summary>
    /// 新建文本框自动完成
    /// </summary>
    public void EditAutoFullDtl_TB()
    {
        MapExt myme = new MapExt(this.getMyPK());
        MapDtls dtls = new MapDtls(myme.getFK_MapData());

//        this.Pub2.append(AddH2("设置自动填充从表. <a href='?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a>"));
        if (dtls.size() == 0)
        {
//            this.Pub2.Clear();
//            this.Pub2.append(AddFieldSet("设置自动填充从表. <a href='?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a>"));
//            this.Pub2.append("该表单下没有从表，所以您不能为从表设置自动填充。");
//            this.Pub2.append(AddFieldSetEnd());
        	this.Pub2.append(AddEasyUiPanelInfo("设置自动填充从表", "<p>该表单下没有从表，所以您不能为从表设置自动填充。<a href='?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a></p>"));
            return;
        }

        Pub2.append(AddTableNormal());
        Pub2.append(AddTRGroupTitle("设置自动填充从表. <a href='"+basePath+"WF/MapDef/MapExt.jsp?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a>"));

        String[] strs = myme.getTag1().split("$");
        boolean is1 = false;
        for (MapDtl dtl: dtls.ToJavaList())
        {
            TextBox tb = new TextBox();
            tb.setId("TB_" + dtl.getNo());
            tb.setName("TB_" + dtl.getNo());
            tb.setCols(70);
            tb.attributes.put("style", "width:100%");
            tb.setRows(5);
            tb.setTextMode(TextBoxMode.MultiLine);
            for(String s:strs)
            {
                if (s == null)
                    continue;

                if (s.contains(dtl.getNo() + ":") == false)
                    continue;

                String[] ss = s.split(":");
                tb.setText(ss[1]);
            }
//            this.Pub2.append(AddFieldSet("编号:" + dtl.getNo() + ",名称:" + dtl.getName()));
//            this.Pub2.append(tb);
            
            //this.Pub2.AddFieldSet("编号:" + dtl.No + ",名称:" + dtl.Name);
            Pub2.append(AddTR());
            Pub2.append(AddTDBegin());
            Pub2.append("编号:" + dtl.getNo() + ",名称:" + dtl.getName());
            Pub2.append(AddBR());
            Pub2.append(tb);
            Pub2.append(AddBR());
            
            String fs = "可填充的字段:";
            MapAttrs attrs = new MapAttrs(dtl.getNo());
            for (MapAttr item: attrs.ToJavaList())
            {
                if (item.getKeyOfEn().equals("OID") || item.getKeyOfEn().equals("RefPKVal"))
                    continue;
                fs += item.getKeyOfEn() + ",";
            }
            this.Pub2.append(fs.substring(0, fs.length() - 1));
            //this.Pub2.AddFieldSetEnd();
            Pub2.append(AddTDEnd());
            Pub2.append(AddTREnd());
        }

        //this.Pub2.AddHR();
        Pub2.append(AddTableEnd());
        Pub2.append(AddBR());

        //Button mybtn = new Button();
        Button mybtn = new Button();
        mybtn.setId("Btn_Save");
        mybtn.setName("Btn_Save");
        mybtn.setCssClass("Btn");
        mybtn.setText("保存");
        mybtn.addAttr("onclick", "mybtn_SaveAutoFullDtl_Click()");
        this.Pub2.append(mybtn);
        this.Pub2.append(AddSpace(1));

        mybtn = new Button();
        mybtn.setId("Btn_Cancel");
        mybtn.setName("Btn_Cancel");
        mybtn.setText("取消");
        mybtn.addAttr("onclick", "mybtn_CancelAutoFullDtl_Click()");
        this.Pub2.append(mybtn);
        this.Pub2.append(AddBR());
        //this.Pub2.AddFieldSetEnd();

//        this.Pub2.append(AddFieldSet("帮助:"));
        this.Pub2.append(AddEasyUiPanelInfoBegin("帮助:一对多多", "icon-help"));
        this.Pub2.append("在这里您需要设置一个查询语句");
        this.Pub2.append(AddBR("例如：SELECT XLMC AS suozaixianlu, bustype as V_BusType FROM [V_XLVsBusType] WHERE jbxx_htid='@Key'"));
        this.Pub2.append(AddBR("这个查询语句要与从表的列对应上就可以在文本框的值发生改变时而自动填充。"));
        this.Pub2.append(AddBR("注意:"));
        this.Pub2.append(AddBR("1，@Key 是主表字段传递过来的变量。"));
        this.Pub2.append(AddBR("2，从表列字段字名，与填充sql列字段大小写匹配。"));
//        this.Pub2.append(AddFieldSetEnd());
        this.Pub2.append(AddEasyUiPanelInfoEnd());
    }
    /// <summary>
    /// 新建文本框自动完成
    /// </summary>
    public void EditAutoFullDtl_DDL()
    {
        this.Pub2.append(AddFieldSet("<a href='"+basePath+"WF/MapDef/MapExt.jsp?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a> -设置自动填充从表"));
        MapExt myme = new MapExt(this.getMyPK());
        MapDtls dtls = new MapDtls(myme.getFK_MapData());
        String[] strs = myme.getTag1().split("$");
//        this.Pub2.append(AddTable("border=0  align=left "));
        if (dtls.size() == 0)
        {
            this.Pub2 = new StringBuffer();
            Pub2.append(AddEasyUiPanelInfo("设置自动填充从表", "<p>该表单下没有从表，所以您不能为从表设置自动填充。<a href='"+basePath+"WF/MapDef/MapExt.jsp?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a></p>"));
            return;
        }

        //this.Pub2.AddTable("border=0  align=left ");
        Pub2.append(AddTableNormal());
        Pub2.append(AddTRGroupTitle("<a href='"+basePath+"WF/MapDef/MapExt.jsp?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a> - 设置自动填充从表"));

        boolean is1 = false;
        for (MapDtl dtl : dtls.ToJavaList())
        {
//            is1 = this.AddTR(""+is1+"");
            TextBox tb = new TextBox();
            tb.setId("TB_" + dtl.getNo());
            tb.setName("TB_" + dtl.getNo());
            tb.setCols(80);
            tb.attributes.put("style", "width:100%");
            tb.setRows(3);
            tb.setTextMode(TextBoxMode.MultiLine);
            for(String s:strs)
            {
                if (s == null)
                    continue;

                if (s.contains(dtl.getNo() + ":") == false)
                    continue;
                String[] ss = s.split(":");
                tb.setText(ss[1]);
            }

            this.Pub2.append(AddTDBegin());
            this.Pub2.append(AddB("&nbsp;&nbsp;" + dtl.getName() + "-从表"));
            this.Pub2.append(AddBR());
            this.Pub2.append(tb);
            this.Pub2.append(AddTDEnd());
            this.Pub2.append(AddTREnd());
        }
      //this.Pub2.AddTableEndWithHR();
        Pub2.append(AddTableEnd());
        Pub2.append(AddBR());
        
        Button mybtn = new Button();
        mybtn.setId("Btn_Save");
        mybtn.setName("Btn_Save");
        mybtn.setCssClass("Btn");
        mybtn.setText("保存");
        mybtn.addAttr("onclick", "mybtn_SaveAutoFullDtl_Click()");
//        mybtn.Click += new EventHandler(mybtn_SaveAutoFullDtl_Click);
        this.Pub2.append(mybtn);
        this.Pub2.append(AddSpace(1));

        mybtn = new Button();
        mybtn.setId("Btn_Cancel");
        mybtn.setName("Btn_Cancel");
        mybtn.setText("取消");
        mybtn.addAttr("onclick", "mybtn_CancelAutoFullDtl_Click()");
//        mybtn.Click += new EventHandler(mybtn_SaveAutoFullDtl_Click);
        this.Pub2.append(mybtn);
//        this.Pub2.append(AddFieldSetEnd());
    }

    public void EditAutoJL()
    {
        MapExt myme = new MapExt(this.getMyPK());
        MapAttrs attrs = new MapAttrs(myme.getFK_MapData());
        String[] strs = myme.getTag().split("$");
        this.Pub2.append(AddTableNormal());
        this.Pub2.append(AddTRGroupTitle("<a href='"+basePath+"WF/MapDef/MapExt.jsp?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a> - 设置级连菜单"));

//        this.Pub2.append(AddTable("border=0 width='70%' align=left"));
//        this.Pub2.append(AddCaptionLeft("<a href='?ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "'>返回</a> -设置级连菜单"));
        boolean is1 = false;
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getLGType() == FieldTypeS.Normal)
                continue;
            if (attr.getUIIsEnable() == false)
                continue;

            TextBox tb = new TextBox();
            tb.setId("TB_" + attr.getKeyOfEn());
            tb.setName("TB_" + attr.getKeyOfEn());
            tb.addAttr("width","100%");
            tb.setCols(90);
            tb.attributes.put("style", "width:100%");
            tb.setRows(4);
            tb.setTextMode(TextBoxMode.MultiLine);

            for(String s:strs)
            {
                if (s == null)
                    continue;

                if (s.contains(attr.getKeyOfEn() + ":") == false)
                    continue;

                String[] ss = s.split(":");
                tb.setText(ss[1]);
            }

            this.Pub2.append(AddTR());
            this.Pub2.append(AddTD("" + attr.getName() + "  " + attr.getKeyOfEn() + " 字段<br>"));
            this.Pub2.append(AddTREnd());

            this.Pub2.append(AddTR());
            this.Pub2.append(AddTD(tb));
            this.Pub2.append(AddTREnd());
        }
//        this.Pub2.append(AddTR());
//        this.Pub2.append(AddTDBegin());
        Pub2.append(AddTableEnd());
        Pub2.append(AddBR());

        Button mybtn = new Button();
        mybtn.setId("Btn_Save");
        mybtn.setName("Btn_Save");
        mybtn.setCssClass("Btn");
        mybtn.setText("保存");
        mybtn.addAttr("onclick", "mybtn_SaveAutoFullJilian_Click()");
//        mybtn.Click += new EventHandler(mybtn_SaveAutoFullJilian_Click);
        this.Pub2.append(mybtn);
        Pub2.append(AddSpace(1));

        mybtn = new Button();
        mybtn.setCssClass("Btn");
        mybtn.setId("Btn_Cancel");
        mybtn.setName("Btn_Cancel");
        mybtn.setText("取消");
        mybtn.addAttr("onclick", "mybtn_CancelAutoFullDtl_Click()");
//        mybtn.Click += new EventHandler(mybtn_SaveAutoFullJilian_Click);
        this.Pub2.append(mybtn);

//        this.Pub2.append(AddTDEnd());
//        this.Pub2.append(AddTREnd());
//
//        this.Pub2.append(AddTableEnd());
    }
    public void init() throws IOException
    {
        this.BindLeft();
//        this.Page.Title = "表单扩展设置";
        if(this.getDoType().equals("Del")){
        	 MapExt mm = new MapExt();
             mm.setMyPK(this.getMyPK());
             if (MapExtXmlList.InputCheck.equals(this.getExtType()))
                 mm.Retrieve();

             mm.Delete();
             this.get_response().sendRedirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + this.getRefNo());
             return;
        }
        else if(this.getDoType().equals("EditAutoJL")){
        	this.EditAutoJL();
            return;
        }
        else{
        	
        }
//        switch (this.getDoType())
//        {
//            case "Del":
//                MapExt mm = new MapExt();
//                mm.MyPK = this.getMyPK();
//                if (this.getExtType() == MapExtXmlList.InputCheck)
//                    mm.Retrieve();
//
//                mm.Delete();
//                this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + this.getRefNo(), true);
//                return;
//            case "EditAutoJL":
//                this.EditAutoJL();
//                return;
//            default:
//                break;
//        }

        if (StringHelper.isNullOrEmpty(this.getExtType()))
        {
            this.Pub2.append(AddEasyUiPanelInfoBegin("帮助","1"));
            this.Pub2.append("<p style='line-height:24px;font-weight:bold'>");
            this.Pub2.append("所有技术资料都整理在，《驰骋工作流程引擎-流程开发说明书.doc》与《驰骋工作流程引擎-表单设计器操作说明书.doc》两个文件中。<br />");

            this.Pub2.append("这两个文件位于:D:\\ccflow\\Documents下面.<br />");
            this.Pub2.append("<a href='http://ccflow.org/Help.jsp' target=_blank>官网帮助</a>");
            this.Pub2.append(AddEasyUiPanelInfoEnd());

            //this.Pub2.AddFieldSet("Help");
            //this.Pub2.AddH3("所有技术资料都整理在，《驰骋工作流程引擎-流程开发说明书.doc》与《驰骋工作流程引擎-表单设计器操作说明书.doc》两个文件中。");
            //this.Pub2.AddH3("<br>这两个文件位于:D:\\ccflow\\Documents下面.");
            //this.Pub2.AddH3("<a href='http://ccflow.org/Help.jsp' target=_blank>官网帮助..</a>");
            //this.Pub2.AddFieldSetEnd();
            return;
        }

        MapExts mes = new MapExts();
        
        if(this.getExtType()==MapExtXmlList.WordFrm){
        	this.FrmWord();
        }
        else if(this.getExtType()==MapExtXmlList.ExcelFrm){
        	this.FrmExcel();
        }
        else if(this.getExtType().equals(MapExtXmlList.Link)){
        	if (!StringHelper.isNullOrEmpty(this.getMyPK()) || "New".equals(this.getDoType()))
            {
                this.BindLinkEdit();
                return;
            }
            this.BindLinkList();
        }
        else if(this.getExtType().equals(MapExtXmlList.RegularExpression)){
        	if ("templete".equals(this.getDoType()))//选择模版
            {
                this.BindReTemplete();
                return;
            }
            if (!StringHelper.isNullOrEmpty(this.getMyPK()) || "New".equals(this.getDoType()))
            {
                this.BindRegularExpressionEdit();
                return;
            }
            this.BindRegularExpressionList();
        }
        else if(this.getExtType().equals(MapExtXmlList.PageLoadFull)){
        	this.BindPageLoadFull();
        }
        else if(this.getExtType().equals(MapExtXmlList.StartFlow)){
        	this.BindPageLoadFull();
        }
        else if(this.getExtType().equals(MapExtXmlList.AutoFullDLL)){
        	this.BindAutoFullDDL();
        }
        else if(this.getExtType().equals(MapExtXmlList.ActiveDDL)){
        	if (!StringHelper.isNullOrEmpty(this.getMyPK()) || !StringHelper.isNullOrEmpty(this.getOperAttrKey()) || "New".equals(this.getDoType()))
            {
                Edit_ActiveDDL();
                return;
            }
            mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                MapExtAttr.FK_MapData, this.getFK_MapData());
            this.MapExtList(mes);
        }
        else if(this.getExtType().equals(MapExtXmlList.TBFullCtrl)){
        	if ("EditAutoFullDtl".equals(this.getDoType()))
            {
                this.EditAutoFullDtl_TB();
                return;
            }
            if ("EditAutoFullM2M".equals(this.getDoType()))
            {
                this.EditAutoFullM2M_TB();
                return;
            }

            if (!StringHelper.isNullOrEmpty(this.getMyPK()) || "New".equals(this.getDoType()))
            {
                this.EditAutoFull_TB();
                return;
            }
            mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                MapExtAttr.FK_MapData, this.getFK_MapData());
            this.MapExtList(mes);
        }
        else if(this.getExtType().equals(MapExtXmlList.DDLFullCtrl)){
        	 if ("EditAutoFullDtl".equals(this.getDoType()))
             {
                 this.EditAutoFullDtl_DDL();
                 return;
             }
             if (!StringHelper.isNullOrEmpty(this.getMyPK()) || "New".equals(this.getDoType()))
             {
                 this.EditAutoFull_DDL();
                 return;
             }
             mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                 MapExtAttr.FK_MapData, this.getFK_MapData());
             this.MapExtList(mes);
        }
        else if(this.getExtType().equals(MapExtXmlList.InputCheck)){
        	if (!StringHelper.isNullOrEmpty(this.getMyPK()) || "New".equals(this.getDoType()))
            {
                Edit_InputCheck();
                return;
            }
            mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                MapExtAttr.FK_MapData, this.getFK_MapData());
            this.MapJS(mes);
        }
        else if(this.getExtType().equals(MapExtXmlList.PopVal)){
        	 if (!StringHelper.isNullOrEmpty(this.getMyPK()) || "New".equals(this.getDoType()))
             {
                 Edit_PopVal();
                 return;
             }
             mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                 MapExtAttr.FK_MapData, this.getFK_MapData());
             this.MapExtList(mes);
        }
        else if(this.getExtType().equals(MapExtXmlList.Func)){
        	this.BindExpFunc();
        }
        else{
        	
        }
        
//        switch (this.getExtType())
//        {
//            case MapExtXmlList.WordFrm: //word模版。
//                this.FrmWord();
//                break;
//            case MapExtXmlList.ExcelFrm: //ExcelFrm。
//                this.FrmExcel();
//                break;
//            case MapExtXmlList.Link: //字段连接。
//                if (this.getMyPK() != null || this.getDoType() == "New")
//                {
//                    this.BindLinkEdit();
//                    return;
//                }
//                this.BindLinkList();
//                break;
//            case MapExtXmlList.RegularExpression: //正则表达式。
//                if (this.getDoType() == "templete")//选择模版
//                {
//                    this.BindReTemplete();
//                    return;
//                }
//                if (this.getMyPK() != null || this.getDoType() == "New")
//                {
//                    this.BindRegularExpressionEdit();
//                    return;
//                }
//                this.BindRegularExpressionList();
//                break;
//            case MapExtXmlList.PageLoadFull: //表单装载填充。
//            case MapExtXmlList.StartFlow: //表单装载填充。
//                this.BindPageLoadFull();
//                break;
//            case MapExtXmlList.AutoFullDLL: //动态的填充下拉框。
//                this.BindAutoFullDDL();
//                break;
//            case MapExtXmlList.ActiveDDL: //联动菜单.
//                if (this.getMyPK() != null || this.getOperAttrKey() != null || this.getDoType() == "New")
//                {
//                    Edit_ActiveDDL();
//                    return;
//                }
//                mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
//                    MapExtAttr.FK_MapData, this.getFK_MapData());
//                this.MapExtList(mes);
//                break;
//            case MapExtXmlList.TBFullCtrl:  //自动完成.
//                if (this.getDoType() == "EditAutoFullDtl")
//                {
//                    this.EditAutoFullDtl_TB();
//                    return;
//                }
//                if (this.getDoType() == "EditAutoFullM2M")
//                {
//                    this.EditAutoFullM2M_TB();
//                    return;
//                }
//
//                if (this.getMyPK() != null || this.getDoType() == "New")
//                {
//                    this.EditAutoFull_TB();
//                    return;
//                }
//                mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
//                    MapExtAttr.FK_MapData, this.getFK_MapData());
//                this.MapExtList(mes);
//                break;
//            case MapExtXmlList.DDLFullCtrl:  //DDL自动完成.
//                if (this.getDoType() == "EditAutoFullDtl")
//                {
//                    this.EditAutoFullDtl_DDL();
//                    return;
//                }
//                if (this.getMyPK() != null || this.getDoType() == "New")
//                {
//                    this.EditAutoFull_DDL();
//                    return;
//                }
//                mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
//                    MapExtAttr.FK_MapData, this.getFK_MapData());
//                this.MapExtList(mes);
//                break;
//            case MapExtXmlList.InputCheck: //输入检查.
//                if (this.getMyPK() != null || this.getDoType() == "New")
//                {
//                    Edit_InputCheck();
//                    return;
//                }
//                mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
//                    MapExtAttr.FK_MapData, this.getFK_MapData());
//                this.MapJS(mes);
//                break;
//            case MapExtXmlList.PopVal: //联动菜单.
//                if (this.getMyPK() != null || this.getDoType() == "New")
//                {
//                    Edit_PopVal();
//                    return;
//                }
//                mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
//                    MapExtAttr.FK_MapData, this.getFK_MapData());
//                this.MapExtList(mes);
//                break;
//            case MapExtXmlList.Func: //联动菜单.
//                this.BindExpFunc();
//                break;
//            default:
//                break;
//        }
    }

    //#region 正则表达式.
    public void BindReTemplete()
    {
        //this.Pub2.AddFieldSet("使用事件模版：" + this.OperAttrKey);
        this.Pub2.append(AddEasyUiPanelInfoBegin("使用事件模版：" + this.getOperAttrKey(),"1"));
        //this.Pub2.AddTable("align=left width=100%");
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("colspan=2", "<b color='blue'>使用事件模版,能够帮助您快速的定义表单字段事件</b>"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=2", "事件模版-点击名称选用它"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        ListBox lb = new ListBox();
        lb.addAttr("width","100%");
//        lb.AutoPostBack = false;
        lb.setId("LBReTemplete");
        lb.setName("LBReTemplete");

        RegularExpressions res = new RegularExpressions();
        res.RetrieveAll();
        for (BP.Sys.XML.RegularExpression item: res.ToJavaList())
        {
            ListItem li = new ListItem(item.getName() + "->" + item.getNote(), item.getNo());
            lb.Items.add(li);
        }
        this.Pub2.append(AddTD("colspan=2", lb));
        this.Pub2.append(AddTREnd());

        //this.Pub2.AddTRSum();
        //Button btn = new Button();
        //btn.ID = "BtnSave";
        //btn.CssClass = "Btn";
        //btn.Text = "Save";
        this.Pub2.append(AddTR());
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
//        btn.Click += new EventHandler(btn_SaveReTemplete_Click);
        btn.setHref("btn_SaveReTemplete_Click()");
        this.Pub2.append(AddTDBegin("colspan=2"));
        //this.Pub2.AddTD("colspan=1 width='80'", btn);
        this.Pub2.append(btn);
        this.Pub2.append(AddSpace(2));
        this.Pub2.append("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-back'\" href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&OperAttrKey=" + this.getOperAttrKey() + "&DoType=New'>返回</a>");
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTableEnd());
        this.Pub2.append(AddEasyUiPanelInfoEnd());
        //this.Pub2.AddFieldSetEnd();
    }
//    public void btn_SaveReTemplete_Click(object sender, EventArgs e)
//    {
//        ListBox lb = this.Pub2.FindControl("LBReTemplete") as ListBox;
//        if (lb == null && lb.SelectedItem.Value == null) return;
//
//        string newMyPk = "";
//        BP.XML.RegularExpressionDtls reDtls = new BP.XML.RegularExpressionDtls();
//        reDtls.RetrieveAll();
//
//        //删除现有的逻辑.
//        BP.Sys.MapExts exts = new BP.Sys.MapExts();
//        exts.Delete(MapExtAttr.AttrOfOper, this.OperAttrKey,
//            MapExtAttr.ExtType, BP.Sys.MapExtXmlList.RegularExpression);
//
//        // 开始装载.
//        foreach (BP.XML.RegularExpressionDtl dtl in reDtls)
//        {
//            if (dtl.ItemNo != lb.SelectedItem.Value)
//                continue;
//
//            BP.Sys.MapExt ext = new BP.Sys.MapExt();
//            ext.MyPK = this.getFK_MapData() + "_" + this.OperAttrKey + "_" + MapExtXmlList.RegularExpression + "_" + dtl.ForEvent;
//            ext.FK_MapData = this.getFK_MapData();
//            ext.AttrOfOper = this.OperAttrKey;
//            ext.Doc = dtl.Exp; //表达公式.
//            ext.Tag = dtl.ForEvent; //时间.
//            ext.Tag1 = dtl.Msg;  //消息
//            ext.ExtType = MapExtXmlList.RegularExpression; // 表达公式 .
//            ext.Insert();
//            newMyPk = ext.MyPK;
//        }
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + newMyPk + "&OperAttrKey=" + this.OperAttrKey + "&DoType=New", true);
//    }
    public void BindRegularExpressionList()
    {
        MapExts mes = new MapExts();
        mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                   MapExtAttr.FK_MapData, this.getFK_MapData());
        //this.Pub2.AddTable("align=left width=100%");
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        //this.Pub2.AddCaptionLeftTX("正则表达式");
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan='3'", "正则表达式"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "序"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "字段"));
        //this.Pub2.AddTDTitle("表达公式");
        //this.Pub2.AddTDTitle("提示信息");
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "操作"));
        this.Pub2.append(AddTREnd());
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddhhmmss");
        String tKey = sdf.format(new Date());

        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        int idx = 0;
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            //if (attr.UIIsEnable == false)
            //    continue;

            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDIdx(idx++));
            this.Pub2.append(AddTD(attr.getKeyOfEn() + "-" + attr.getName()));
            MapExt me = (MapExt)mes.GetEntityByKey(MapExtAttr.AttrOfOper, attr.getKeyOfEn());
            if (me == null)
                this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-config'\" href='"+basePath+"WF/MapDef/MapExt.jsp?s=" + tKey + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&OperAttrKey=" + attr.getKeyOfEn() + "&DoType=New'>设置</a>"));
            else
                this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-edit'\" href='"+basePath+"WF/MapDef/MapExt.jsp?s=" + tKey + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + me.getMyPK() + "&OperAttrKey=" + attr.getKeyOfEn() + "'>修改</a>"));
            this.Pub2.append(AddTREnd());
        }
        this.Pub2.append(AddTableEnd());
    }

    public void BindRegularExpressionEdit()
    {
        //this.Pub2.AddTable();
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan='4'",
                                  "正则表达式 - <a href=\""+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" +
                                  this.getExtType() + "&OperAttrKey=" + this.getOperAttrKey() +
                                  "&DoType=templete\">加载模版</a>- <a href='"+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() +
                                  "&ExtType=" + this.getExtType() + "' >返回</a>"));
        this.Pub2.append(AddTREnd());
        //this.Pub2.AddCaption("正则表达式 - <a href=\""+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&OperAttrKey=" + this.OperAttrKey + "&DoType=templete\">加载模版</a>- <a href='"+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "' >返回</a>");

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "序"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "事件"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "事件内容"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "提示信息"));
        this.Pub2.append(AddTREnd());

        //#region 绑定事件
        int idx = 1;
        idx = BindRegularExpressionEditExt(idx, "onblur");
        idx = BindRegularExpressionEditExt(idx, "onchange");

        idx = BindRegularExpressionEditExt(idx, "onclick");
        idx = BindRegularExpressionEditExt(idx, "ondblclick");

        idx = BindRegularExpressionEditExt(idx, "onkeypress");
        idx = BindRegularExpressionEditExt(idx, "onkeyup");
        idx = BindRegularExpressionEditExt(idx, "onsubmit");
        
        this.Pub2.append(AddTableEnd());

        //Button btn = new Button();
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        //btn.ID = "Btn_Save";
        //btn.Text = "Save";
        btn.setHref("BindRegularExpressionEdit_Click()");
//        btn.Click += new EventHandler(BindRegularExpressionEdit_Click);
        this.Pub2.append(btn);

    }
    public int BindRegularExpressionEditExt(int idx, String myEvent)
    {
        // 查询.
        MapExt me = new MapExt();
        me.setFK_MapData(this.getFK_MapData());
        me.setTag(myEvent);
        me.setAttrOfOper(this.getOperAttrKey());
        me.setMyPK(me.getFK_MapData() + "_" + this.getOperAttrKey() + "_" + this.getExtType() + "_" + myEvent);
        me.RetrieveFromDBSources();

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDIdx(idx));
        this.Pub2.append(AddTD("style='font-size:12px'", myEvent));

        TextBox tb = new TextBox();
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setId("TB_Doc_" + myEvent);
        tb.setName("TB_Doc_" + myEvent);
        tb.setText(me.getDoc());
        tb.setCols(50);
        tb.setRows(3);
        tb.addAttr("width", "99%");

        this.Pub2.append(AddTD(tb));

        tb = new TextBox();
        tb.setId("TB_Tag1_" + myEvent);
        tb.setName("TB_Tag1_" + myEvent);
        tb.setText(me.getTag1());
        tb.setCols(50);
        tb.setRows(3);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD(tb));
        this.Pub2.append(AddTREnd());
        idx = idx + 1;
        return idx;
    }

//    void BindRegularExpressionEdit_Click(object sender, EventArgs e)
//    {
//        
//        BindRegularExpressionEdit_ClickSave("onblur");
//        BindRegularExpressionEdit_ClickSave("onchange");
//
//        BindRegularExpressionEdit_ClickSave("onclick");
//
//        BindRegularExpressionEdit_ClickSave("ondblclick");
//
//        BindRegularExpressionEdit_ClickSave("onkeypress");
//        BindRegularExpressionEdit_ClickSave("onkeyup");
//        BindRegularExpressionEdit_ClickSave("onsubmit");
//        #endregion
//
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType(), true);
//    }
    


   
    public void BindLinkList()
    {
        MapExts mes = new MapExts();
        mes.Retrieve(MapExtAttr.ExtType, this.getExtType(),
                   MapExtAttr.FK_MapData, this.getFK_MapData());

        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan='5'", "字段超连接"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "序"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "字段"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "连接"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "窗口"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "操作"));
        this.Pub2.append(AddTREnd());

        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        int idx = 0;
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            if (attr.getUIIsEnable() == true)
                continue;

            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDIdx(idx++));
            this.Pub2.append(AddTD(attr.getKeyOfEn() + "-" + attr.getName()));
            MapExt me = (MapExt)mes.GetEntityByKey(MapExtAttr.AttrOfOper, attr.getKeyOfEn());
            if (me == null)
            {
                this.Pub2.append(AddTD("-"));
                this.Pub2.append(AddTD("-"));
                this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-config'\" href='"+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&OperAttrKey=" + attr.getKeyOfEn() + "&DoType=New'>设置</a>"));
            }
            else
            {
                this.Pub2.append(AddTD(me.getTag()));
                this.Pub2.append(AddTD(me.getTag1()));
                this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-edit'\" href='"+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + me.getMyPK() + "&OperAttrKey=" + attr.getKeyOfEn() + "'>修改</a>"));
            }
            this.Pub2.append(AddTREnd());
        }
        this.Pub2.append(AddTableEnd());
    }
    public void BindLinkEdit()
    {
        MapExt me = new MapExt();
        if (this.getMyPK() != null)
        {
            me.setMyPK(this.getMyPK());
            me.RetrieveFromDBSources();
        }
        else
        {
            me.setFK_MapData(this.getFK_MapData());
            me.setAttrOfOper(this.getOperAttrKey());
            me.setTag("http://ccflow.org");
            me.setTag1("_" + this.getOperAttrKey());
        }

        this.Pub2.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("字段超连接 - <a href='"+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "' >返回</a>"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("字段英文名"));
        this.Pub2.append(AddTD(this.getOperAttrKey()));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("字段中文名"));
        MapAttr ma = new MapAttr(this.getFK_MapData(), this.getOperAttrKey());
        this.Pub2.append(AddTD(ma.getName()));
        this.Pub2.append(AddTREnd());
        TextBox tb = new TextBox();
        tb.setId("TB_Tag");
        tb.setName("TB_Tag");
        tb.setText(me.getTag());
        tb.setCols(50);
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("Url"));
        this.Pub2.append(AddTD(tb));
        this.Pub2.append(AddTREnd());

        tb = new TextBox();
        tb.setId("TB_Tag1");
        tb.setName("TB_Tag1");
        tb.setText(me.getTag1());
        tb.setCols(50);
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("窗口"));
        this.Pub2.append(AddTD(tb));
        this.Pub2.append(AddTREnd());

        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("BindLinkEdit_Click()");
        //        btn.Click += new EventHandler(BindLinkEdit_Click);
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD(btn));

        if (this.getMyPK() != null)
        {
            btn = new LinkButton(false, NamesOfBtn.Delete.toString(), "删除");
//            btn.Click += new EventHandler(BindLinkEdit_Click);
            btn.setHref("BindLinkDelete_Click()");
            btn.addAttr("onclick","return window.confirm('您确定要删除吗？');");
            this.Pub2.append(AddTD(btn));
        }
        else
        {
            this.Pub2.append(AddTD());
        }

        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());
    }
//    void BindLinkEdit_Click(object sender, EventArgs e)
//    {
//        MapExt me = new MapExt();
//        Button btn = sender as Button;
//        if (btn.ID == NamesOfBtn.Delete)
//        {
//            me.MyPK = this.getMyPK();
//            me.Delete();
//            this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType(), true);
//            return;
//        }
//
//        me = (MapExt)this.Pub2.Copy(me);
//        me.FK_MapData = this.getFK_MapData();
//        me.AttrOfOper = this.OperAttrKey;
//        //me.Tag = this.Pub2.GetTextBoxByID("TB_Tag").Text;
//        //me.Tag1 = this.Pub2.GetTextBoxByID("TB_Tag1").Text;
//        me.ExtType = this.getExtType();
//        if (this.getMyPK() == null)
//            me.MyPK = me.FK_MapData + "_" + me.AttrOfOper + "_" + this.getExtType();
//        else
//            me.MyPK = this.getMyPK();
//        me.Save();
//
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType(), true);
//    }
//    #endregion
    /// <summary>
    /// BindPageLoadFull
    /// </summary>
    public void BindPageLoadFull()
    {
        MapExt me = new MapExt();
        me.setMyPK(this.getFK_MapData() + "_" + this.getExtType());
        me.RetrieveFromDBSources();

        this.Pub2.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("主表SQL设置"));
        this.Pub2.append(AddTREnd());

        TextBox tb = new TextBox();
        tb.setId("TB_" + MapExtAttr.Tag);
        tb.setName("TB_" + MapExtAttr.Tag);
        tb.setText(me.getTag());
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(10);
        tb.setCols(70);
        tb.addAttr("width", "99%");

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDBegin());
        this.Pub2.append(tb);
        this.Pub2.append(AddBR());
        this.Pub2.append("说明:填充主表的sql,表达式里支持@变量与约定的公用变量。 <br>比如: SELECT No,Name,Tel FROM Port_Emp WHERE No='@WebUser.No' , 如果列名与开始表单字段名相同，就会自动给值。");
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());

        MapDtls dtls = new MapDtls(this.getFK_MapData());
        if (dtls.size() != 0)
        {
            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDGroupTitle("明细表SQL设置"));
            this.Pub2.append(AddTREnd());

            String[] sqls = null;
            if(!StringHelper.isNullOrEmpty(me.getTag1())){
            	if(me.getTag1().startsWith("*")){
            		sqls=new String[1];
            		sqls[0]=me.getTag1().substring(1);
            	}else{
            		sqls=new String[2];
            		sqls=me.getTag1().split("*");
            	}
            }
            for (MapDtl dtl: dtls.ToJavaList())
            {
                this.Pub2.append(AddTR());
				this.Pub2.append(AddTD("明细表:[" + dtl.getNo() + "]&nbsp;" + dtl.getName()));
                this.Pub2.append(AddTREnd());
                tb = new TextBox();
                tb.setId("TB_" + dtl.getNo());
                tb.setName("TB_" + dtl.getNo());
                if(sqls!=null){
                	for (String sql:sqls)
                	{
                		if (StringHelper.isNullOrEmpty(sql))
                			continue;
                		String key = sql.substring(0, sql.indexOf('='));
                		if (key.equals(dtl.getNo()))
                		{
                			tb.setText(sql.substring(sql.indexOf('=') + 1));
                			break;
                		}
                	}
                }

                tb.setTextMode(TextBoxMode.MultiLine);
                tb.setRows(10);
                tb.setCols(70);
                tb.addAttr("width", "99%");

                this.Pub2.append(AddTR());
                this.Pub2.append(AddTDBegin());
                this.Pub2.append(tb);
                this.Pub2.append(AddBR());
                this.Pub2.append("说明:结果集合填充从表");
                this.Pub2.append(AddTREnd());
            }
        }

        //Button btn = new Button();
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("btn_SavePageLoadFull_Click()");
        //        btn.Click += new EventHandler(btn_SavePageLoadFull_Click);

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD(btn));
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());
        return;
    }
    /// <summary>
    /// 保存它
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
//    void btn_SavePageLoadFull_Click(object sender, EventArgs e)
//    {
//        MapExt me = new MapExt();
//        me.MyPK = this.getFK_MapData() + "_" + this.getExtType();
//        me.FK_MapData = this.getFK_MapData();
//        me.ExtType = this.getExtType();
//        me.RetrieveFromDBSources();
//
//        me.Tag = this.Pub2.GetTextBoxByID("TB_" + MapExtAttr.Tag).Text;
//        string sql = "";
//        MapDtls dtls = new MapDtls(this.getFK_MapData());
//        foreach (MapDtl dtl in dtls)
//        {
//            sql += "*" + dtl.No + "=" + this.Pub2.GetTextBoxByID("TB_" + dtl.No).Text;
//        }
//        me.Tag1 = sql;
//
//        me.MyPK = this.getFK_MapData() + "_" + this.getExtType();
//
//        string info = me.Tag1 + me.Tag;
//        if (string.IsNullOrEmpty(info))
//            me.Delete();
//        else
//            me.Save();
//    }
//
//    #region 保存word模版属性.
    /// <summary>
    /// Word属性.
    /// </summary>
    public void FrmWord()
    {
        MapData ath = new MapData(this.getFK_MapData());

        //#region WebOffice控制方式.
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=3", "WebOffice控制方式."));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        CheckBox cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableWF);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableWF);
        cb.setText("是否启用weboffice？");
//        cb.setChecked(ath.IsWoEnableWF);
        this.Pub2.append(AddTD(cb));

        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableSave);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableSave);
        cb.setText("是否启用保存？");
//        cb.setChecked(ath.IsWoEnableSave);
        this.Pub2.append(AddTD(cb));

        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableReadonly);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableReadonly);
        cb.setText("是否只读？");
//        cb.setChecked(ath.IsWoEnableReadonly);
        this.Pub2.append(AddTD(cb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableRevise);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableRevise);
        cb.setText("是否启用修订？");
//        cb.setChecked(ath.IsWoEnableRevise);
        this.Pub2.append(AddTD(cb));

        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark);
        cb.setText("是否查看用户留痕？");
//        cb.setChecked(ath.IsWoEnableViewKeepMark);
        this.Pub2.append(AddTD(cb));

        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnablePrint);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnablePrint);
        cb.setText("是否打印？");
//        cb.setChecked(ath.IsWoEnablePrint);
        this.Pub2.append(AddTD(cb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableOver);
        cb.setText("是否启用套红？");
//        cb.setChecked(ath.IsWoEnableOver);
        this.Pub2.append(AddTD(cb));

        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableSeal);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableSeal);
        cb.setText("是否启用签章？");
//        cb.setChecked(ath.IsWoEnableSeal);
        this.Pub2.append(AddTD(cb));

        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableTemplete);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableTemplete);
        cb.setText("是否启用模板文件？");
//        cb.setChecked(ath.IsWoEnableTemplete);
        this.Pub2.append(AddTD(cb));

        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTR());
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableCheck);
        cb.setText( "是否记录节点信息？");
//        cb.setChecked(ath.IsWoEnableCheck);
        this.Pub2.append(AddTD(cb));
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow);
        cb.setText("是否启用插入流程？");
//        cb.setChecked(ath.IsWoEnableInsertFlow);
        this.Pub2.append(AddTD(cb));
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian);
        cb.setText("是否启用插入风险点？");
//        cb.setChecked(ath.IsWoEnableInsertFengXian);
        this.Pub2.append(AddTD(cb));
        this.Pub2.append(AddTR());
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableMarks);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableMarks);
        cb.setText("是否进入留痕模式？");
//        cb.setChecked(ath.IsWoEnableMarks);
        this.Pub2.append(AddTD(cb));
        cb = new CheckBox();
        cb.setId("CB_" + FrmAttachmentAttr.IsWoEnableDown);
        cb.setName("CB_" + FrmAttachmentAttr.IsWoEnableDown);
        cb.setText("是否启用下载？");
//        cb.setChecked(ath.IsWoEnableDown);
        this.Pub2.append(AddTD(cb));
        this.Pub2.append(AddTD(""));
        this.Pub2.append(AddTREnd());
        //#endregion WebOffice控制方式.

        //确定模板文件
//        var moduleFile = getModuleFile(new[] { ".doc", ".docx" });

        Pub2.append(AddTR());
        Pub2.append(AddTDBegin("colspan='3'"));
        this.Pub2.append("模版文件(必须是*.doc/*.docx文件):");

//        var lit = new Literal();
//        lit.ID = "litInfo";
//
//        if (!StringHelper.isNullOrEmpty(moduleFile))
//        {
//            lit.Text = "[<span style='color:green'>已上传Word表单模板:<a href='" + moduleFile +
//                       "' target='_blank' title='下载或打开模版'>" + moduleFile +
//                       "</a></span>]<br /><br />";
//
//            this.Pub2.Add(lit);
//        }
//        else
//        {
//            lit.Text = "[<span style='color:red'>还未上传Word表单模板</span>]<br /><br />";
//            this.Pub2.Add(lit);
//        }
//
//        FileUpload fu = new FileUpload();
//        fu.ID = "FU";
//        fu.Width = 300;
//        this.Pub2.Add(fu);
//        this.Pub2.AddSpace(2);
//        var btn = new LinkBtn(false, NamesOfBtn.Save, "保存");
//        btn.Click += new EventHandler(btn_SaveWordFrm_Click);
//        this.Pub2.Add(btn);
//
//        this.Pub2.AddTDEnd();
//        this.Pub2.AddTREnd();
//        this.Pub2.AddTableEnd();
    }

    /// <summary>
    /// 获取上传的模板文件相对路径
    /// </summary>
    /// <returns></returns>
    private String getModuleFile(String[] extArr)
    {
        String dir = this.get_request().getSession().getServletContext().getRealPath("/DataUser/FrmOfficeTemplate/");
        File[] files = new File(dir).listFiles();//DirectoryInfo(dir).GetFiles(this.getFK_MapData() + ".*");
        List<File> list=new ArrayList<File>();
        for(File f:files){
        	if(f.getName().startsWith(this.getFK_MapData())){
        		list.add(f);
        	}
        }
        String moduleFile = null;

        for(File file:list)
        {
        	String f=file.getName().substring(file.getName().lastIndexOf(".")+1);
            for(String s:extArr){
            	if (s.contains(f))
                {
                    moduleFile = file.getAbsolutePath().replace("\\", "/");
                    moduleFile = moduleFile.substring(moduleFile.indexOf("/DataUser/"));
                    break;
                }
            }
        }

        return moduleFile;
    }

//    void btn_SaveWordFrm_Click(object sender, EventArgs e)
//    {
//        MapData ath = new MapData(this.getFK_MapData());
//        //ath.IsWoEnableWF = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableWF).Checked;
//        //ath.IsWoEnableSave = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableSave).Checked;
//        //ath.IsWoEnableReadonly = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableReadonly).Checked;
//        //ath.IsWoEnableRevise = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableRevise).Checked;
//        //ath.IsWoEnableViewKeepMark = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark).Checked;
//        //ath.IsWoEnablePrint = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnablePrint).Checked;
//        //ath.IsWoEnableSeal = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableSeal).Checked;
//        //ath.IsWoEnableOver = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableOver).Checked;
//        //ath.IsWoEnableTemplete = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableTemplete).Checked;
//        //ath.IsWoEnableCheck = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableCheck).Checked;
//        //ath.IsWoEnableInsertFengXian = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian).Checked;
//        //ath.IsWoEnableInsertFlow = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow).Checked;
//        //ath.IsWoEnableMarks = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableMarks).Checked;
//        //ath.IsWoEnableDown = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableDown).Checked;
//        //ath.Update();
//
//        FileUpload fu = this.Pub2.FindControl("FU") as FileUpload;
//        if (fu.FileName != null)
//        {
//            var extArr = new[] { ".doc", ".docx" };
//            var ext = Path.GetExtension(fu.FileName).ToLower();
//            if (!extArr.Contains(ext))
//            {
//                Response.Write("<script>alert('Word表单模板只能上传*.doc/*.docx两种格式的文件！');history.back();</script>");
//                return;
//            }
//
//            var moduleFile = getModuleFile(extArr);
//
//            if (!string.IsNullOrEmpty(moduleFile))
//                File.Delete(Server.MapPath(moduleFile));
//
//            moduleFile = SystemConfig.PathOfDataUser + "FrmOfficeTemplate\\" + this.getFK_MapData() +
//                         Path.GetExtension(fu.FileName);
//
//            fu.SaveAs(moduleFile);
//            moduleFile = moduleFile.substing(moduleFile.IndexOf("\\DataUser\\")).Replace("\\", "/");
//            var lit = this.Pub2.FindControl("litInfo") as Literal;
//
//            if (lit != null)
//            {
//                lit.Text = "[<span style='color:green'>已上传Word表单模板:<a href='" + moduleFile +
//                           "' target='_blank' title='下载或打开模版'>" + moduleFile +
//                           "</a></span>]<br /><br />";
//            }
//        }
//    }
//    #endregion 保存word模版属性.
//
//    #region 保存Excel模版属性.
    /// <summary>
    /// Word属性.
    /// </summary>
    public void FrmExcel()
    {
        ToolbarExcel en = new ToolbarExcel(this.getFK_MapData());

        //this.Pub2.AddH2("编辑Excel表单属性.");
        //this.Pub2.Add("<a href=\"javascript:WinOpen('/WF/Comm/RefFunc/UIEn.jsp?EnName=BP.Sys.ToolbarExcel&No=" + this.getFK_MapData() + "')\" >Excel配置项</a>");

        //确定模板文件
        String moduleFile = getModuleFile(new String[] { ".xls", ".xlsx" });
        this.Pub2.append(AddEasyUiPanelInfoBegin("Excel表单属性","1"));
        this.Pub2.append("模版文件(必须是*.xls/*.xlsx文件):");

       TextBox lit=new TextBox();
        lit.setId("litInfo");
        lit.setTextMode(TextBoxMode.Files);
        if (!StringHelper.isNullOrEmpty(moduleFile))
        {
            lit.setText("[<span style='color:green'>已上传Excel表单模板:<a href='" + moduleFile +
                       "' target='_blank' title='下载或打开模版'>" + moduleFile +
                       "</a></span>]<br /><br />");

            this.Pub2.append(lit);
        }
        else
        {
            lit.setText("[<span style='color:red'>还未上传Excel表单模板</span>]<br /><br />");
            this.Pub2.append(lit);
        }

        FileUpload fu = new FileUpload();
        fu.setId("FU");
        fu.setName("FU");
//        fu.Width = 300;
        this.Pub2.append(fu);
        this.Pub2.append(AddSpace(2));
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
//        btn.Click += new EventHandler(btn_SaveExcelFrm_Click);
        this.Pub2.append(btn);
        this.Pub2.append(AddSpace(2));
        this.Pub2.append("<a href=\"javascript:OpenEasyUiDialog('/WF/Comm/RefFunc/UIEn.jsp?EnName=BP.Sys.ToolbarExcel&No="+this.getFK_MapData()+"','eudlgframe','Excel配置顶',800,495,'icon-config')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-config'\">Excel配置项</a>");
        this.Pub2.append(AddEasyUiPanelInfoEnd());
    }

//    void btn_SaveExcelFrm_Click(object sender, EventArgs e)
//    {
//        //ath.IsWoEnableWF = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableWF).Checked;
//        //ath.IsWoEnableSave = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableSave).Checked;
//        //ath.IsWoEnableReadonly = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableReadonly).Checked;
//        //ath.IsWoEnableRevise = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableRevise).Checked;
//        //ath.IsWoEnableViewKeepMark = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark).Checked;
//        //ath.IsWoEnablePrint = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnablePrint).Checked;
//        //ath.IsWoEnableSeal = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableSeal).Checked;
//        //ath.IsWoEnableOver = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableOver).Checked;
//        //ath.IsWoEnableTemplete = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableTemplete).Checked;
//        //ath.IsWoEnableCheck = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableCheck).Checked;
//        //ath.IsWoEnableInsertFengXian = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian).Checked;
//        //ath.IsWoEnableInsertFlow = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow).Checked;
//        //ath.IsWoEnableMarks = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableMarks).Checked;
//        //ath.IsWoEnableDown = this.Pub2.GetCBByID("CB_" + FrmAttachmentAttr.IsWoEnableDown).Checked;
//        //ath.Update();
//
//        FileUpload fu = this.Pub2.FindControl("FU") as FileUpload;
//        if (fu.FileName != null)
//        {
//            var extArr = new[] { ".xls", ".xlsx" };
//            var ext = Path.GetExtension(fu.FileName).ToLower();
//            if (!extArr.Contains(ext))
//            {
//                Response.Write("<script>alert('Excel表单模板只能上传*.xls/*.xlsx两种格式的文件！');history.back();</script>");
//                return;
//            }
//
//            var moduleFile = getModuleFile(extArr);
//
//            if (!string.IsNullOrEmpty(moduleFile))
//                File.Delete(Server.MapPath(moduleFile));
//
//            moduleFile = SystemConfig.PathOfDataUser + "FrmOfficeTemplate\\" + this.getFK_MapData() +
//                         Path.GetExtension(fu.FileName);
//
//            fu.SaveAs(moduleFile);
//            moduleFile = moduleFile.substing(moduleFile.IndexOf("\\DataUser\\")).Replace("\\", "/");
//            var lit = this.Pub2.FindControl("litInfo") as Literal;
//
//            if (lit != null)
//            {
//                lit.Text = "[<span style='color:green'>已上传Excel表单模板:<a href='" + moduleFile +
//                           "' target='_blank' title='下载或打开模版'>" + moduleFile +
//                           "</a></span>]<br /><br />";
//            }
//        }
//    }
//    #endregion 保存word模版属性.

    public void BindAutoFullDDL()
    {
        if ("Del".equals(this.getDoType()))
        {
            MapExt me = new MapExt();
            me.setMyPK(this.get_request().getParameter("FK_MapExt"));
            me.Delete();
        }

        MapAttrs attrs = new MapAttrs();
        attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(),
            MapAttrAttr.UIContralType, BP.En.UIContralType.DDL.getValue(),
            MapAttrAttr.UIVisible, 1, MapAttrAttr.UIIsEnable, 1);

        if (attrs.size() == 0)
        {
            this.Pub2.append(AddEasyUiPanelInfo("提示","此表单中没有可设置的自动填充内容。<br />只有满足，可见，被启用，是外键字段，才可以设置。"));
            return;
        }

        MapExts mes = new MapExts();
        mes.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL);
        //this.Pub2.AddTable("align=left width='60%'");
        this.Pub2.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));
        //this.Pub2.AddCaptionLeft(this.Lab);
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan='5'", this.Lab));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "序号"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "字段"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "中文名"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "绑定源"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "操作"));
        this.Pub2.append(AddTREnd());
        String fk_attr = this.get_request().getParameter("FK_Attr");
        int idx = 0;
        MapAttr attrOper = null;
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getKeyOfEn().equals(fk_attr))
                attrOper = attr;

            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDIdx(idx++));
            this.Pub2.append(AddTD(attr.getKeyOfEn()));
            this.Pub2.append(AddTD(attr.getName()));
            this.Pub2.append(AddTD(attr.getUIBindKey()));
            MapExt me = (MapExt)mes.GetEntityByKey(MapExtAttr.AttrOfOper, attr.getKeyOfEn());
            if (me == null)
                this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-config'\" href='?FK_MapData=" + this.getFK_MapData() + "&FK_Attr=" + attr.getKeyOfEn() + "&ExtType=" + MapExtXmlList.AutoFullDLL + "' >设置</a>"));
            else
                this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-edit'\" href='?FK_MapData=" + this.getFK_MapData() + "&FK_Attr=" + attr.getKeyOfEn() + "&ExtType=" + MapExtXmlList.AutoFullDLL + "' >编辑</a> - <a class='easyui-linkbutton' data-options=\"iconCls:'icon-delete'\" href=\"javascript:DoDel('" + me.getMyPK() + "','" + this.getFK_MapData() + "','" + MapExtXmlList.AutoFullDLL + "')\" >删除</a>"));
            this.Pub2.append(AddTREnd());
        }

        if (fk_attr != null)
        {
            MapExt me = new MapExt();
            me.setMyPK(MapExtXmlList.AutoFullDLL + "_" + this.getFK_MapData() + "_" + fk_attr);
            me.RetrieveFromDBSources();
            this.Pub2.append(AddTR());
            this.Pub2.append(AddTDBegin("colspan=5"));
            this.Pub2.append(AddEasyUiPanelInfoBegin("设置:(" + attrOper.getKeyOfEn() + " - " + attrOper.getName() + ")运行时自动填充数据","xi"));
            //this.Pub2.AddFieldSet("设置:(" + attrOper.KeyOfEn + " - " + attrOper.Name + ")运行时自动填充数据");
            TextBox tb = new TextBox();
            tb.setTextMode(TextBoxMode.MultiLine);
            tb.setCols(80);
            tb.setId("TB_Doc");
            tb.setName("TB_Doc");
            tb.setRows(4);
            tb.addAttr("width", "99%");
            tb.setText(me.getDoc().replace("~", "'"));
            this.Pub2.append(tb);
            this.Pub2.append(AddBR());
            this.Pub2.append(AddBR());
            //Button btn = new Button();
            LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
            //btn.ID = "Btn_Save_AutoFullDLL";
            //btn.CssClass = "Btn";
            //btn.Text = " 保 存 ";
            btn.setHref("btn_Save_AutoFullDLL_Click()");
//            btn.Click += new EventHandler(btn_Save_AutoFullDLL_Click);
            this.Pub2.append(btn);
            this.Pub2.append("<br />事例:SELECT No,Name FROM Port_Emp WHERE FK_Dept LIKE '@WebUser.FK_Dept%' <br />您可以用@符号取本表单中的字段变量，或者全局变量，更多的信息请参考说明书。");
            this.Pub2.append("<br />数据源必须具有No,Name两个列。");

            //this.Pub2.AddFieldSetEnd();
            this.Pub2.append(AddEasyUiPanelInfoEnd());
            this.Pub2.append(AddTDEnd());
            this.Pub2.append(AddTREnd());
            this.Pub2.append(AddTableEnd());
        }
        else
        {
            this.Pub2.append(AddTableEnd());
        }
    }
//    void btn_Save_AutoFullDLL_Click(object sender, EventArgs e)
//    {
//        string attr = this.Request.QueryString["FK_Attr"];
//        MapExt me = new MapExt();
//        me.MyPK = MapExtXmlList.AutoFullDLL + "_" + this.getFK_MapData() + "_" + attr;
//        me.RetrieveFromDBSources();
//        me.FK_MapData = this.getFK_MapData();
//        me.AttrOfOper = attr;
//        me.ExtType = MapExtXmlList.AutoFullDLL;
//        me.Doc = this.Pub2.GetTextBoxByID("TB_Doc").Text.Replace("'", "~");
//
//        try
//        {
//            DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(me.Doc);
//        }
//        catch(Exception e)
//        {
//            this.Alert("SQL不能被正确的执行，拼写有问题，请检查。");
//            return;
//        }
//
//        me.Save();
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + MapExtXmlList.AutoFullDLL, true);
//    }
    /// <summary>
    /// 功能执行
    /// </summary>
    public void BindExpFunc()
    {
        ExpFucnXmls xmls = new ExpFucnXmls();
        xmls.RetrieveAll();

        //this.Pub2.AddFieldSet("导出");
        this.Pub2.append(AddEasyUiPanelInfoBegin("导出","1"));
        this.Pub2.append(AddUL("class='navlist'"));
        for (ExpFucnXml item: xmls.ToJavaList())
        {
            //this.Pub2.AddLi(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&DoType=" + item.No + "&RefNo=" + this.getRefNo(),item.Name);
            this.Pub2.append("<li><div><a href=\""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&DoType=" + item.getNo() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">" + item.getName() + "</span></a></div></li>");
        }
        this.Pub2.append(AddULEnd());
        //this.Pub2.AddFieldSetEnd();
        this.Pub2.append(AddEasyUiPanelInfoEnd());
    }
//    void mybtn_SaveAutoFullDtl_Click(object sender, EventArgs e)
//    {
//        var btn = sender as LinkBtn;
//        if (btn.ID.Contains("Cancel"))
//        {
//            this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//            return;
//        }
//
//        MapExt myme = new MapExt(this.getMyPK());
//        MapDtls dtls = new MapDtls(myme.FK_MapData);
//        string info = "";
//        string error = "";
//        foreach (MapDtl dtl in dtls)
//        {
//            TextBox tb = this.Pub2.GetTextBoxByID("TB_" + dtl.No);
//            if (tb.Text.Trim() == "")
//                continue;
//            try
//            {
//                //DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(tb.Text);
//                //MapAttrs attrs = new MapAttrs(dtl.No);
//                //string err = "";
//                //foreach (DataColumn dc in dt.Columns)
//                //{
//                //    if (attrs.IsExits(MapAttrAttr.KeyOfEn, dc.ColumnName) == false)
//                //    {
//                //        err += "<br>列" + dc.ColumnName + "不能与从表 属性匹配.";
//                //    }
//                //}
//                //if (err != "")
//                //{
//                //    error += "在为("+dtl.Name+")检查sql设置时出现错误:"+err;
//                //}
//            }
//            catch (Exception ex)
//            {
//                this.Alert("SQL ERROR: " + ex.Message);
//                return;
//            }
//            info += "$" + dtl.No + ":" + tb.Text;
//        }
//
//        if (error != "")
//        {
//            this.Pub2.AddEasyUiPanelInfo("错误", "设置错误,请更正:<br />" + error, "icon-no");
//            //this.Pub2.AddMsgOfWarning("设置错误,请更正:", error);
//            return;
//        }
//        myme.Tag1 = info;
//        myme.Update();
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//    }
//    void mybtn_SaveAutoFullM2M_Click(object sender, EventArgs e)
//    {
//        //Button btn = sender as Button;
//        var btn = sender as LinkBtn;
//        if (btn.ID.Contains("Cancel"))
//        {
//            this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//            return;
//        }
//
//        MapExt myme = new MapExt(this.getMyPK());
//        MapM2Ms m2ms = new MapM2Ms(myme.FK_MapData);
//        string info = "";
//        string error = "";
//        foreach (MapM2M m2m in m2ms)
//        {
//            TextBox tb = this.Pub2.GetTextBoxByID("TB_" + m2m.NoOfObj);
//            if (tb.Text.Trim() == "")
//                continue;
//            try
//            {
//                DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(tb.Text);
//                string err = "";
//                if (dt.Columns[0].ColumnName != "No")
//                    err += "第1列不是No.";
//                if (dt.Columns[1].ColumnName != "Name")
//                    err += "第2列不是Name.";
//
//                if (err != "")
//                {
//                    error += "在为(" + m2m.Name + ")检查sql设置时出现错误：请确认列的顺序是否正确为大小写是否匹配。" + err;
//                }
//            }
//            catch (Exception ex)
//            {
//                this.Alert("SQL ERROR: " + ex.Message);
//                return;
//            }
//            info += "$" + m2m.NoOfObj + ":" + tb.Text;
//        }
//
//        if (error != "")
//        {
//            this.Pub2.AddEasyUiPanelInfo("错误", "设置错误,请更正:<br />" + error, "icon-no");
//            //this.Pub2.AddMsgOfWarning("设置错误,请更正:", error);
//            return;
//        }
//        myme.Tag2 = info;
//        myme.Update();
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//    }
//    void mybtn_SaveAutoFullJilian_Click(object sender, EventArgs e)
//    {
//        //Button btn = sender as Button;
//        var btn = sender as LinkBtn;
//        if (btn.ID.Contains("Cancel"))
//        {
//            this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//            return;
//        }
//
//        MapExt myme = new MapExt(this.getMyPK());
//        MapAttrs attrs = new MapAttrs(myme.FK_MapData);
//        string info = "";
//        foreach (MapAttr attr in attrs)
//        {
//            if (attr.LGType == FieldTypeS.Normal)
//                continue;
//
//            if (attr.UIIsEnable == false)
//                continue;
//
//            TextBox tb = this.Pub2.GetTextBoxByID("TB_" + attr.KeyOfEn);
//            if (tb.Text.Trim() == "")
//                continue;
//
//            try
//            {
//                DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(tb.Text);
//                if (tb.Text.Contains("@Key") == false)
//                    throw new Exception("缺少@Key参数。");
//
//                if (dt.Columns.Contains("No") == false || dt.Columns.Contains("Name") == false)
//                    throw new Exception("在您的sql表单公式中必须有No,Name两个列，来绑定下拉框。");
//            }
//            catch (Exception ex)
//            {
//                this.Alert("SQL ERROR: " + ex.Message);
//                return;
//            }
//            info += "$" + attr.KeyOfEn + ":" + tb.Text;
//        }
//        myme.Tag = info;
//        myme.Update();
//        this.Alert("保存成功.");
//        //   this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//    }
    public void Edit_PopVal()
    {
        //this.Pub2.AddTable("border=0");
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        MapExt me = null;
        if (this.getMyPK() == null)
        {
            me = new MapExt();
            //this.Pub2.AddCaptionLeft("新建:" + this.Lab + "-帮助请详见驰骋表单设计器说明书");
            this.Pub2.append(AddTRGroupTitle(3, "新建:" + this.Lab + "-帮助请详见驰骋表单设计器说明书"));
        }
        else
        {
            me = new MapExt(this.getMyPK());
            //this.Pub2.AddCaptionLeft("编辑:" + this.Lab + "-帮助请详见驰骋表单设计器说明书");
            this.Pub2.append(AddTRGroupTitle(3, "编辑:" + this.Lab + "-帮助请详见驰骋表单设计器说明书"));
        }

        me.setFK_MapData(this.getFK_MapData());
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "项目"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "采集"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "说明"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("作用字段："));
        DDL ddl = new DDL();
        ddl.setId("DDL_Oper");
        ddl.setName("DDL_Oper");
        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            if (attr.getUIIsEnable() == false)
                continue;

            if (attr.getUIContralType() == UIContralType.TB)
            {
                ddl.Items.add(new ListItem(attr.getKeyOfEn() + " - " + attr.getName(), attr.getKeyOfEn()));
                continue;
            }
        }
        ddl.SetSelectItem(me.getAttrOfOper());
        this.Pub2.append(AddTD(ddl));
        this.Pub2.append(AddTD("处理pop窗体的字段."));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("设置类型："));
        this.Pub2.append(AddTDBegin());

        RadioButton rb = new RadioButton();
        rb.setText("自定义URL");
        rb.setId("RB_Tag_0");
        rb.setName("RB_Tag_0");
        rb.setGroupName("sd");
        rb.setValue("RB_Tag_0");
        if (me.getPopValWorkModel() == 0)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);
        rb = new RadioButton();
        rb.setId("RB_Tag_1");
        rb.setName("RB_Tag_1");
        rb.setText("ccform内置");
        rb.setGroupName("sd");
        if (me.getPopValWorkModel() == 1)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTD("如果是自定义URL,仅填写URL字段."));
        this.Pub2.append(AddTREnd());


        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("URL："));
        TextBox tb = new TextBox();
        tb.setId("TB_" + MapExtAttr.Doc);
        tb.setId("TB_" + MapExtAttr.Doc);
        tb.setText(me.getDoc());
        tb.setCols(50);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD("colspan=2", tb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("colspan=3", "URL填写说明:请输入一个弹出窗口的url,当操作员关闭后返回值就会被设置在当前控件中<br />测试URL:http://localhost/Flow/SDKFlowDemo/PopSelectVal.jsp."));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("数据分组SQL："));
        tb = new TextBox();
        tb.setId("TB_" + MapExtAttr.Tag1);
        tb.setName("TB_" + MapExtAttr.Tag1);
        tb.setText(me.getTag1());
        tb.setCols(50);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD("colspan=2", tb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("数据源SQL："));
        tb = new TextBox();
        tb.setId("TB_" + MapExtAttr.Tag2);
        tb.setName("TB_" + MapExtAttr.Tag2);
        tb.setText(me.getTag2());
        tb.setCols(50);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD("colspan=2", tb));
        this.Pub2.append(AddTREnd());
        //this.Pub2.AddTREnd();

        //#region 选择方式
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("选择方式："));
        this.Pub2.append(AddTDBegin());

        rb = new RadioButton();
        rb.setText("多项选择");
        rb.setId("RB_Tag3_0");
        rb.setName("RB_Tag3_0");
        rb.setValue("RB_Tag3_0");
        rb.setGroupName("dd");
        if (me.getPopValSelectModel() == 0)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);

        rb = new RadioButton();
        rb.setId("RB_Tag3_1");
        rb.setName("RB_Tag3_1");
        rb.setText("单项选择");
        rb.setGroupName("dd");
        if (me.getPopValSelectModel() == 1)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTD(""));
        this.Pub2.append(AddTREnd());
        //#endregion 选择方式

        //#region 呈现方式
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("数据源呈现方式："));
        this.Pub2.append(AddTDBegin());

        rb = new RadioButton();
        rb.setText("表格方式");
        rb.setId("RB_Tag4_0");
        rb.setGroupName("dsd");
        rb.setValue("RB_Tag4_0");
        if (me.getPopValShowModel() == 0)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);

        rb = new RadioButton();
        rb.setId("RB_Tag4_1");
        rb.setText("目录方式");
        rb.setGroupName("dsd");
        if (me.getPopValShowModel() == 1)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTD(""));
        this.Pub2.append(AddTREnd());
        //#endregion 呈现方式

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("返回值格式："));
        ddl = new DDL();
        ddl.setId("DDL_PopValFormat");
        ddl.setName("DDL_PopValFormat");
        ddl.BindSysEnum("PopValFormat");

        ddl.SetSelectItem(me.getPopValFormat());

        this.Pub2.append(AddTD("colspan=2", ddl));
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTRSum());
        //Button btn = new Button();
        //btn.ID = "BtnSave";
        //btn.CssClass = "Btn";
        //btn.Text = "Save";
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("btn_SavePopVal_Click()");
        //        btn.Click += new EventHandler(btn_SavePopVal_Click);
        this.Pub2.append(AddTD("colspan=3", btn));
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());
    }
    public String EventName()
    {
            String s = this.get_request().getParameter("EventName");
            return s;
    }
    String temFile = "s@xa";
    public void Edit_InputCheck()
    {
        MapExt me = null;
        if (StringHelper.isNullOrEmpty(this.getMyPK()))
        {
            me = new MapExt();
            //this.Pub2.AddFieldSet("新建:" + this.Lab);
            this.Pub2.append(AddEasyUiPanelInfoBegin("新建:" + this.Lab, "icon-new"));
        }
        else
        {
            me = new MapExt(this.getMyPK());
            //this.Pub2.AddFieldSet("编辑:" + this.Lab);
            this.Pub2.append(AddEasyUiPanelInfoBegin("编辑:" + this.Lab, "icon-edit"));
        }
        me.setFK_MapData(this.getFK_MapData());
        temFile = me.getTag();

        //this.Pub2.AddTable("border=0  width='70%' align=left ");
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        MapAttr attr = new MapAttr(this.getRefNo());
        //this.Pub2.AddCaptionLeft(attr.KeyOfEn + " - " + attr.Name);
        this.Pub2.append(AddTRGroupTitle(2, attr.getKeyOfEn() + " - " + attr.getName()));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("函数库来源:"));
        this.Pub2.append(AddTDBegin());
        RadioButton rb = uf.creatRadioButton("RB_0");
        rb.setName("RB_MExt");
        rb.setText("ccflow系统js函数库.");
//        rb.setAutoPostBack(true);
        if (me.getDoWay() == 0)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        rb.setGroupName("s");
        rb.addAttr("onclick", "rb_CheckedChanged()");
        this.Pub2.append(rb);

        rb = uf.creatRadioButton("RB_1");
        rb.addAttr("onclick", "rb_CheckedChanged()");
        rb.setName("RB_MExt");
//        rb.setAutoPostBack(true);
        rb.setText("我自定义的函数库.");
//        rb.CheckedChanged += new EventHandler(rb_CheckedChanged);
        rb.setGroupName("s");
//        rb.setAutoPostBack(true);
        if (me.getDoWay() == 1)
            rb.setChecked(true);
        else
            rb.setChecked(false);
        this.Pub2.append(rb);
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=2", "函数列表"));
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTR());

        ListBox lb = uf.createListBox("LB1");
        lb.addAttr("width","100%");
        rb_CheckedChanged();
//        lb.AutoPostBack = false;
        this.Pub2.append(AddTD("colspan=2", lb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTRSum());
        //Button btn = new Button();
        //btn.ID = "BtnSave";
        //btn.CssClass = "Btn";
        //btn.Text = "Save";
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
//        btn.Click += new EventHandler(btn_SaveInputCheck_Click);
        btn.setHref("btn_SaveInputCheck_Click()");

        this.Pub2.append(AddTD(btn));
        this.Pub2.append(AddTD("<a class='easyui-linkbutton' data-options=\"iconCls:'icon-back'\" href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "'>返回</a>"));
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());
        //this.Pub2.AddFieldSetEnd();
        this.Pub2.append(AddEasyUiPanelInfoEnd());
    }
    private void rb_CheckedChanged()
    {
        String path = BP.Sys.SystemConfig.getPathOfData() + "JSLib/";
        
       RadioButton rb = (RadioButton) uf.GetUIByID("RB_0"); // sender as System.Web.UI.WebControls.RadioButton;
        if (rb.getChecked() == false)
            path = BP.Sys.SystemConfig.getPathOfDataUser() + "JSLib/";

        ListBox lb = (ListBox)uf.GetUIByID("LB1");
        lb.Items.clear();
//        lb.setAutoPostBack(false);
        lb.setSelectionMode(ListSelectionMode.Multiple);
//        lb.setRows(10);
        //lb.SelectedIndexChanged += new EventHandler(lb_SelectedIndexChanged);
        String file = temFile;
        if (!StringHelper.isNullOrEmpty(temFile))
        {
            file = file.substring(file.lastIndexOf('/') + 4);
            file = file.replace(".js", "");
        }
        else
        {
            file = "!!!";
        }

        MapExts mes = new MapExts();
        mes.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(),
            MapExtAttr.AttrOfOper, this.getOperAttrKey(),
            MapExtAttr.ExtType, this.getExtType());

        
        File[] dics = new File(path).listFiles();
        
//        String[] dirs = System.IO.Directory.GetDirectories(path);
        for(File f:dics){
        	String ph=f.getPath();
        	File[] ff=new File(f.getPath()).listFiles();
        	for(File dir:ff)
            {
        		String str=dir.getPath();
//                String[] strs = Directory.GetFiles(dir);
//                for(String s:strs)
//                {
                    if (!str.contains(".js"))
                        continue;

                    ListItem li = new ListItem(str.replace(path, "").replace(".js", ""), str);
                    if (str.contains(file))
                        li.setSelected(true);
                    
                    lb.Items.add(li);
//                }
            }
        }
    }
    public void EditAutoFull_TB()
    {
        MapExt me = null;
        if (StringHelper.isNullOrEmpty(this.getMyPK()))
            me = new MapExt();
        else
            me = new MapExt(this.getMyPK());

        me.setFK_MapData(this.getFK_MapData());

        //this.Pub2.AddTable("border=0");
        this.Pub2.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        //this.Pub2.AddCaptionLeft("新建:" + this.Lab);
        this.Pub2.append(AddTRGroupTitle(2, "新建:" + this.Lab));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "项目"));
        this.Pub2.append(AddTDGroupTitle("style='text-align:center'", "采集"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("下拉框"));
        DDL ddl = new DDL();
        ddl.setId("DDL_Oper");
        ddl.setName("DDL_Oper");
        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            if (attr.getUIIsEnable() == false)
                continue;

            if (attr.getUIContralType() == UIContralType.TB)
            {
                ddl.Items.add(new ListItem(attr.getKeyOfEn() + " - " + attr.getName(), attr.getKeyOfEn()));
                continue;
            }
        }
        ddl.SetSelectItem(me.getAttrOfOper());
        this.Pub2.append(AddTD(ddl));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=2", "自动填充SQL:"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        TextBox tb = new TextBox();
        tb.setId("TB_Doc");
        tb.setName("TB_Doc");
        tb.setText( me.getDoc());
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(5);
        tb.setCols(80);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD("colspan=2", tb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=2", "关键字查询的SQL:"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        tb = new TextBox();
        tb.setId("TB_Tag");
        tb.setName("TB_Tag");
        tb.setText(me.getTag());
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(5);
        tb.setCols(80);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD("colspan=2", tb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTRSum());
        this.Pub2.append(AddTDBegin("colspan=2"));

        //Button btn = new Button();
        //btn.CssClass = "Btn";
        //btn.ID = "BtnSave";
        //btn.Text = "保存";
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("btn_SaveAutoFull_Click()");
//        btn.Click += new EventHandler(btn_SaveAutoFull_Click);
        this.Pub2.append(btn);

        if (StringHelper.isNullOrEmpty(this.getMyPK()))
        {
        }
        else
        {
        	 this.Pub2.append(AddSpace(1));
             this.Pub2.append("<a class='easyui-linkbutton' href=\""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo = " + this.getRefNo() + "&ExtType=" + this.getExtType() + "&DoType=EditAutoJL\" >级连下拉框</a>");
             this.Pub2.append(AddSpace(1));
             this.Pub2.append("<a class='easyui-linkbutton' href=\""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + this.getRefNo() + "&DoType=EditAutoFullDtl\" >填充从表</a>");
             this.Pub2.append(AddSpace(1));
             this.Pub2.append("<a class='easyui-linkbutton' href=\""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + this.getRefNo() + "&DoType=EditAutoFullM2M\" >填充一对多</a>");
        }
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());
        //#region 输出事例

        //this.Pub2.AddFieldSet("帮助");
        this.Pub2.append(AddEasyUiPanelInfoBegin("帮助", "icon-help"));
        this.Pub2.append(AddB("For oracle:"));
        String sql = "自动填充SQL:<br />SELECT No as ~No~ , Name as ~Name~, Name as ~mingcheng~ FROM WF_Emp WHERE No LIKE '@Key%' AND ROWNUM<=15";
        sql += "<br />关键字查询SQL:<br>SELECT No as ~No~ , Name as ~Name~, Name as ~mingcheng~ FROM WF_Emp WHERE No LIKE '@Key%'  ";
        this.Pub2.append(AddBR(sql.replace("~", "\"")));

        this.Pub2.append(AddB("<br />For sqlserver:"));
        sql = "自动填充SQL:<br />SELECT TOP 15 No, Name , Name as mingcheng FROM WF_Emp WHERE No LIKE '@Key%'";
        sql += "<br />关键字查询SQL:<br>SELECT  No, Name , Name as mingcheng FROM WF_Emp WHERE No LIKE '@Key%'";
        this.Pub2.append(AddBR(sql.replace("~", "\"")));

        this.Pub2.append(AddB("<br />注意:"));
        this.Pub2.append(AddBR("1,文本框自动完成填充事例: 必须有No,Name两列，它用于显示下列出的提示列表。"));
        this.Pub2.append(AddBR("2,设置合适的记录数量，能够改善系统执行效率。"));
        this.Pub2.append(AddBR("3,@Key 是系统约定的关键字，就是当用户输入一个字符后ccform就会传递此关键字到数据库查询把结果返回给用户。"));
        this.Pub2.append(AddBR("4,其它的列与本表单的字段名相同则可自动填充，要注意大小写匹配。"));
        this.Pub2.append(AddBR("5,关键字查询sql是用来，双点文本框时弹出的查询语句，如果为空就按自动填充的sql计算。"));

        //this.Pub2.AddFieldSetEnd();
        this.Pub2.append(AddEasyUiPanelInfoEnd());
        //#endregion 输出事例
    }
    public void EditAutoFull_DDL()
    {
        MapExt me = null;
        if (this.getMyPK() == null)
            me = new MapExt();
        else
            me = new MapExt(this.getMyPK());

        me.setFK_MapData(this.getFK_MapData());

        //this.Pub2.AddTable("align=left");
        this.Pub2.append(AddTableNormal());
        //this.Pub2.AddCaptionLeft("新建:" + this.Lab);
        this.Pub2.append(AddTRGroupTitle(3, "新建:" + this.Lab));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitleCenter("项目"));
        this.Pub2.append(AddTDGroupTitleCenter("采集"));
        this.Pub2.append(AddTDGroupTitleCenter("说明"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("下拉框："));
        DDL ddl = new DDL();
        ddl.setId("DDL_Oper");
        ddl.setName("DDL_Oper");
        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            if (attr.getUIIsEnable() == false)
                continue;

            if (attr.getUIContralType() == UIContralType.DDL)
            {
                ddl.Items.add(new ListItem(attr.getKeyOfEn() + " - " + attr.getName(), attr.getKeyOfEn()));
                continue;
            }
        }
        ddl.SetSelectItem(me.getAttrOfOper());

        this.Pub2.append(AddTD(ddl));
        this.Pub2.append(AddTD("输入项"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=3", "自动填充SQL："));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        TextBox tb = new TextBox();
        tb.setId("TB_Doc");
        tb.setName("TB_Doc");
        tb.setText(me.getDoc());
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(5);
        tb.setCols(80);
        tb.addAttr("width", "99%");
        this.Pub2.append(AddTD("colspan=3", tb));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTRSum());
        //Button btn = new Button();
        //btn.CssClass = "Btn";
        //btn.ID = "BtnSave";
        //btn.Text = "保存";
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("btn_SaveAutoFull_Click()");
//        btn.Click += new EventHandler(btn_SaveAutoFull_Click);
        //this.Pub2.AddTD("colspan=2", btn);
        this.Pub2.append(AddTDBegin("colspan=3"));
        this.Pub2.append(btn);

        if (!StringHelper.isNullOrEmpty(this.getMyPK()))
        {
            //this.Pub2.AddTD("<a href=\""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo = " + this.getRefNo() + "&ExtType=" + this.getExtType() + "&DoType=EditAutoJL\" >级连下拉框</a>-<a href=\""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + this.getRefNo() + "&DoType=EditAutoFullDtl\" >填充从表</a>");
        	this.Pub2.append(AddSpace(1));
        	this.Pub2.append(AddEasyUiLinkButton("级连下拉框",
                                     ""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&RefNo = " +
                                     this.getRefNo() + "&ExtType=" + this.getExtType() + "&DoType=EditAutoJL",null,false));
        	this.Pub2.append(AddSpace(1));
        	this.Pub2.append(AddEasyUiLinkButton("填充从表",
                                     ""+basePath+"WF/MapDef/MapExt.jsp?MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" +
                                     this.getExtType() + "&RefNo=" + this.getRefNo() + "&DoType=EditAutoFullDtl",null,false));

        }
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());

        //#region 输出事例
        //this.Pub2.AddTRSum();
        //this.Pub2.Add("\n<TD class='BigDoc' valign=top colspan=3>");
        this.Pub2.append(AddTR());
        //his.Pub2.AddFieldSet("填充事例:");
        this.Pub2.append(AddEasyUiPanelInfoBegin("填充事例：","1"));
        String sql = "SELECT dizhi as Addr, fuzeren as Manager FROM Prj_Main WHERE No = '@Key'";
        this.Pub2.append(sql.replace("~", "\""));
        this.Pub2.append(AddBR("<hr><b>说明：</b>根据用户当前选择下拉框的实例（比如:选择一个工程）把相关此实例的其它属性放在控件中"));
        this.Pub2.append("（比如：工程的地址，负责人。）");
        this.Pub2.append(AddBR("<b>备注：</b><br />1.只有列名与本表单中字段名称匹配才能自动填充上去。<br>2.sql查询出来的是一行数据，@Key 是当前选择的值。"));
        //this.Pub2.AddFieldSetEnd();
        this.Pub2.append(AddEasyUiPanelInfoEnd());

        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());
        //#endregion 输出事例
    }
    public void Edit_ActiveDDL()
    {
        MapExt me = null;
        if (this.getMyPK() == null)
        {
            me = new MapExt();
            //this.Pub2.AddFieldSet("新建:" + this.Lab);
            this.Pub2.append(AddEasyUiPanelInfoBegin("新建:" + this.Lab,"icon-new"));
        }
        else
        {
            me = new MapExt(this.getMyPK());
            //this.Pub2.AddFieldSet("编辑:" + this.Lab);
            this.Pub2.append(AddEasyUiPanelInfoBegin("编辑:" + this.Lab,"icon-edit"));
        }
        me.setFK_MapData(this.getFK_MapData());

        //this.Pub2.AddTable("border=0  width='300px' align=left ");
        this.Pub2.append(AddTableNormal());
        //this.Pub2.AddCaptionLeft(this.Lab);
        this.Pub2.append(AddTRGroupTitle(3, this.Lab));

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitleCenter("项目"));
        this.Pub2.append(AddTDGroupTitleCenter("采集"));
        this.Pub2.append(AddTDGroupTitleCenter("说明"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("主菜单："));
        DDL ddl = new DDL();
        ddl.setId("DDL_Oper");
        ddl.setName("DDL_Oper");
        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        int num = 0;
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            if (attr.getUIIsEnable() == false)
                continue;

            if (attr.getUIContralType() == UIContralType.DDL)
            {
                num++;
                ddl.Items.add(new ListItem(attr.getKeyOfEn() + " - " + attr.getName(), attr.getKeyOfEn()));
                continue;
            }
        }
        ddl.SetSelectItem(me.getAttrOfOper());

        this.Pub2.append(AddTD(ddl));
        this.Pub2.append(AddTD("输入项"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTD("联动项："));
        ddl = new DDL();
        ddl.setId("DDL_Attr");
        ddl.setName("DDL_Attr");
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            if (attr.getUIIsEnable() == false)
                continue;

            if (attr.getUIContralType() != UIContralType.DDL)
                continue;

            ddl.Items.add(new ListItem(attr.getKeyOfEn() + " - " + attr.getName(), attr.getKeyOfEn()));
        }
        ddl.SetSelectItem(me.getAttrsOfActive());
        this.Pub2.append(AddTD(ddl));
        this.Pub2.append(AddTD("要实现联动效果的菜单"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitle("colspan=3", "联动方式"));
        this.Pub2.append(AddTREnd());

        this.Pub2.append(AddTR());
        //this.Pub2.Add("<TD class=BigDoc width='100%' colspan=3>");
        this.Pub2.append(AddTDBegin("colspan='3'"));
        RadioButton rb = new RadioButton();
        rb.setName("RB_0");
        rb.setText("通过sql获取联动");
        rb.setGroupName("sdr");
        rb.setId("RB_0");
        if (me.getDoWay() == 0)
            rb.setChecked(true);

        //this.Pub2.AddFieldSet(rb);
        
        this.Pub2.append("在下面文本框中输入一个SQL,具有编号，标签列，用来绑定下从动下拉框。<br />");
        this.Pub2.append("比如:SELECT No, Name FROM CN_City WHERE FK_SF = '@Key' ");
        this.Pub2.append(AddBR());
        TextBox tb = new TextBox();
        tb.setId("TB_Doc");
        tb.setName("TB_Doc");
        tb.setText(me.getDoc());
        tb.setCols(80);
        tb.setCssClass("TH");
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(7);
        tb.addAttr("width", "99%");
        this.Pub2.append(tb);
        this.Pub2.append(AddBR());
        this.Pub2.append("说明:@Key是ccflow约定的关键字，是主下拉框传递过来的值");
        //this.Pub2.AddFieldSetEnd();

        rb = new RadioButton();
        rb.setText("通过编码标识获取");
        rb.setGroupName("sdr");
        rb.setEnabled(false);
        rb.setId("RB_1");
        if (me.getDoWay() == 1)
            rb.setChecked(true);

        //this.Pub2.AddFieldSet(rb);
        this.Pub2.append("主菜单是编号的是从动菜单编号的前几位，不必联动内容。<br />");
        this.Pub2.append("比如: 主下拉框是省份，联动菜单是城市。");
        //this.Pub2.AddFieldSetEnd();

        //this.Pub2.Add("</TD>");
        this.Pub2.append(AddTDEnd());
        this.Pub2.append(AddTREnd());

        //this.Pub2.AddTRSum();
        this.Pub2.append(AddTR());
        //Button btn = new Button();
        //btn.CssClass = "Btn";
        //btn.ID = "BtnSave";
        //btn.Text = "Save";
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("btn_SaveJiLian_Click()");
//        btn.Click += new EventHandler(btn_SaveJiLian_Click);
        this.Pub2.append(AddTD("colspan=3", btn));
        this.Pub2.append(AddTREnd());
        this.Pub2.append(AddTableEnd());

        //this.Pub2.AddFieldSetEnd();
        this.Pub2.append(AddEasyUiPanelInfoEnd());
    }
////    void btn_SaveJiLian_Click(object sender, EventArgs e)
////    {
////        MapExt me = new MapExt();
////        me.MyPK = this.getMyPK();
////        if (me.MyPK.Length > 2)
////            me.RetrieveFromDBSources();
////        me = (MapExt)this.Pub2.Copy(me);
////        me.ExtType = this.getExtType();
////        me.Doc = this.Pub2.GetTextBoxByID("TB_Doc").Text;
////        me.AttrOfOper = this.Pub2.GetDDLByID("DDL_Oper").SelectedItemStringVal;
////        me.AttrsOfActive = this.Pub2.GetDDLByID("DDL_Attr").SelectedItemStringVal;
////        if (me.AttrsOfActive == me.AttrOfOper)
////        {
////            this.Alert("两个项目不能相同.");
////            return;
////        }
////        try
////        {
////            if (this.Pub2.GetRadioButtonByID("RB_1").Checked)
////                me.DoWay = 1;
////            else
////                me.DoWay = 0;
////        }
////        catch (Exception ex)
////        {
////            me.DoWay = 0;
////        }
////
////        me.FK_MapData = this.getFK_MapData();
////        try
////        {
////            me.MyPK = this.getFK_MapData() + "_" + me.ExtType + "_" + me.AttrOfOper + "_" + me.AttrsOfActive;
////
////            if (me.Doc.Contains("No") == false || me.Doc.Contains("Name") == false)
////                throw new Exception("在您的sql表达式里，必须有No,Name 还两个列。");
////            //DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(me.Doc);
////            //if (dt.Columns.Contains("Name") == false || dt.Columns.Contains("No") == false)
////            //    throw new Exception("在您的sql表达式里，必须有No,Name 还两个列。");
////            me.Save();
////        }
////        catch (Exception ex)
////        {
////            this.Alert(ex.Message);
////            return;
////        }
////        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo = " + this.getRefNo(), true);
////    }
////    void btn_SaveInputCheck_Click(object sender, EventArgs e)
////    {
////        ListBox lb = this.Pub2.FindControl("LB1") as ListBox;
////
////        // 检查路径. 没有就创建它。
////        string pathDir = BP.Sys.SystemConfig.PathOfDataUser + "\\JSLibData\\";
////        if (Directory.Exists(pathDir) == false)
////            Directory.CreateDirectory(pathDir);
////
////        // 删除已经存在的数据.
////        MapExt me = new MapExt();
////        me.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(),
////            MapExtAttr.ExtType, this.getExtType(),
////            MapExtAttr.AttrOfOper, this.OperAttrKey);
////
////        foreach (ListItem li in lb.Items)
////        {
////            if (li.Selected == false)
////                continue;
////
////            me = (MapExt)this.Pub2.Copy(me);
////            me.ExtType = this.getExtType();
////
////            // 操作的属性.
////            me.AttrOfOper = this.OperAttrKey;
////            //this.Pub2.GetDDLByID("DDL_Oper").SelectedItemStringVal;
////
////            int doWay = 0;
////            if (this.Pub2.GetRadioButtonByID("RB_0").Checked == false)
////                doWay = 1;
////
////            me.DoWay = doWay;
////            me.Doc = BP.DA.DataType.ReadTextFile(li.Value);
////            FileInfo info = new FileInfo(li.Value);
////            me.Tag2 = info.Directory.Name;
////
////            //获取函数的名称.
////            string func = me.Doc;
////            func = me.Doc.substing(func.IndexOf("function") + 8);
////            func = func.substing(0, func.IndexOf("("));
////            me.Tag1 = func.Trim();
////
////            // 检查路径,没有就创建它.
////            FileInfo fi = new FileInfo(li.Value);
////            me.Tag = li.Value;
////            me.FK_MapData = this.getFK_MapData();
////            me.ExtType = this.getExtType();
////            me.MyPK = this.getFK_MapData() + "_" + me.ExtType + "_" + me.AttrOfOper + "_" + me.Tag1;
////            try
////            {
////                me.Insert();
////            }
////            catch
////            {
////                me.Update();
////            }
////        }
////
////        #region 把所有的js 文件放在一个文件里面。
////        MapExts mes = new MapExts();
////        mes.Retrieve(MapExtAttr.FK_MapData, this.getFK_MapData(),
////            MapExtAttr.ExtType, this.getExtType());
////
////        string js = "";
////        foreach (MapExt me1 in mes)
////        {
////            js += "\r\n" + BP.DA.DataType.ReadTextFile(me1.Tag);
////        }
////
////        if (File.Exists(pathDir + "\\" + this.getFK_MapData() + ".js"))
////            File.Delete(pathDir + "\\" + this.getFK_MapData() + ".js");
////
////        BP.DA.DataType.WriteFile(pathDir + "\\" + this.getFK_MapData() + ".js", js);
////        #endregion 把所有的js 文件放在一个文件里面。
////
////
////        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo = " + this.getRefNo(), true);
////    }
////    void btn_SavePopVal_Click(object sender, EventArgs e)
////    {
//        MapExt me = new MapExt();
//        me.MyPK = this.getMyPK();
//        if (me.MyPK.Length > 2)
//            me.RetrieveFromDBSources();
//        me = (MapExt)this.Pub2.Copy(me);
//        me.ExtType = this.getExtType();
//        me.Doc = this.Pub2.GetTextBoxByID("TB_Doc").Text;
//        me.AttrOfOper = this.Pub2.GetDDLByID("DDL_Oper").SelectedItemStringVal;
//        me.SetPara("PopValFormat", this.Pub2.GetDDLByID("DDL_PopValFormat").SelectedItemStringVal);
//
//        RadioButton rb = this.Pub2.GetRadioButtonByID("RB_Tag_0");
//        if (rb.Checked)
//            me.PopValWorkModel = 0;
//        else
//            me.PopValWorkModel = 1;
//
//        rb = this.Pub2.GetRadioButtonByID("RB_Tag3_0");
//        if (rb.Checked)
//            me.PopValSelectModel = 0;
//        else
//            me.PopValSelectModel = 1;
//
//        rb = this.Pub2.GetRadioButtonByID("RB_Tag4_0");
//        if (rb.Checked)
//            me.PopValShowModel = 0;
//        else
//            me.PopValShowModel = 1;
//
//
//        me.FK_MapData = this.getFK_MapData();
//        me.MyPK = this.getFK_MapData() + "_" + me.ExtType + "_" + me.AttrOfOper;
//        me.Save();
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo = " + this.getRefNo(), true);
////    }
////    void btn_SaveAutoFull_Click(object sender, EventArgs e)
////    {
//        MapExt me = new MapExt();
//        me.MyPK = this.getMyPK();
//        if (me.MyPK.Length > 2)
//            me.RetrieveFromDBSources();
//
//        me = (MapExt)this.Pub2.Copy(me);
//        me.ExtType = this.getExtType();
//        me.Doc = this.Pub2.GetTextBoxByID("TB_Doc").Text;
//        me.AttrOfOper = this.Pub2.GetDDLByID("DDL_Oper").SelectedItemStringVal;
//        me.FK_MapData = this.getFK_MapData();
//        me.MyPK = this.getFK_MapData() + "_" + me.ExtType + "_" + me.AttrOfOper;
//
//        try
//        {
//            //DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(me.Doc);
//            //if (string.IsNullOrEmpty(me.Tag) == false)
//            //{
//            //    dt = BP.DA.DBAccess.RunSQLReturnTable(me.Tag);
//            //    if (dt.Columns.Contains("Name") == false || dt.Columns.Contains("No") == false)
//            //        throw new Exception("在您的sql表达式里，必须有No,Name 还两个列。");
//            //}
//
//            //if (this.getExtType() == MapExtXmlList.TBFullCtrl)
//            //{
//            //    if (dt.Columns.Contains("Name") == false || dt.Columns.Contains("No") == false)
//            //        throw new Exception("在您的sql表达式里，必须有No,Name 还两个列。");
//            //}
//
//            //MapAttrs attrs = new MapAttrs(this.getFK_MapData());
//            //foreach (DataColumn dc in dt.Columns)
//            //{
//            //    if (dc.ColumnName.ToLower() == "no" || dc.ColumnName.ToLower() == "name")
//            //        continue;
//
//            //    if (attrs.Contains(MapAttrAttr.KeyOfEn, dc.ColumnName) == false)
//            //        throw new Exception("@系统没有找到您要匹配的列(" + dc.ColumnName + ")，注意:您要指定的列名区分大小写。");
//            //}
//            me.Save();
//        }
//        catch (Exception ex)
//        {
//            //this.Alert(ex.Message);
//            this.AlertMsg_Warning("SQL错误", ex.Message);
//            return;
//        }
//        this.Response.Redirect(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + this.getRefNo(), true);
////    }
    public void MapExtList(MapExts ens)
    {
        //this.Pub2.AddTable("border=0 width='80%' align=left");
        this.Pub2.append(AddTableNormal());
        //this.Pub2.AddCaptionLeft("<a href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&DoType=New&RefNo=" + this.getRefNo() + "' ><img src='../Img/Btn/New.gif' border=0/>新建:" + this.Lab + "</a>");
        this.Pub2.append(AddCaptionLeft(
                             "<a href='"+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() +
                             "&DoType=New&RefNo=" + this.getRefNo() + "' ><img src='../Img/Btn/New.gif' border=0 />新建:" +this.Lab + "</a>"));
        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitleCenter("类型"));
        this.Pub2.append(AddTDGroupTitleCenter("描述"));
        this.Pub2.append(AddTDGroupTitleCenter("字段"));
        this.Pub2.append(AddTDGroupTitleCenter("删除"));
        this.Pub2.append(AddTREnd());

        for (MapExt en: ens.ToJavaList())
        {
            MapAttr ma = new MapAttr();
            ma.setMyPK(this.getFK_MapData() + "_" + en.getAttrOfOper());
            if (ma.RetrieveFromDBSources() == 0)
            {
                ma.Delete();
                continue;
            }

            this.Pub2.append(AddTR());
            this.Pub2.append(AddTD(en.getExtType()));
            this.Pub2.append(AddTDA(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + en.getMyPK() + "&RefNo=" + this.getRefNo(), en.getExtDesc()));

            this.Pub2.append(AddTD(en.getAttrOfOper() + " " + ma.getName()));

            //this.Pub2.AddTD("<a href=\"javascript:DoDel('" + en.MyPK + "','" + this.getFK_MapData() + "','" + this.getExtType() + "');\" >删除</a>");
            this.Pub2.append(AddTDBegin());
            this.Pub2.append(AddEasyUiLinkButton("删除",
                                          "javascript:DoDel('" + en.getMyPK() + "','" + this.getFK_MapData() + "','" +
                                          this.getExtType() + "');", "icon-delete",false));
            this.Pub2.append(AddTDEnd());
            this.Pub2.append(AddTREnd());
        }

        this.Pub2.append(AddTableEndWithBR());
    }
    public void MapJS(MapExts ens)
    {
        //this.Pub2.AddTable("border=0 width=90% align=left");
        this.Pub2.append(AddTableNormal());
        //this.Pub2.AddCaptionLeft("脚本验证");
        this.Pub2.append(AddTRGroupTitle(5, "脚本验证"));

        this.Pub2.append(AddTR());
        this.Pub2.append(AddTDGroupTitleCenter("字段"));
        this.Pub2.append(AddTDGroupTitleCenter("类型"));
        this.Pub2.append(AddTDGroupTitleCenter("验证函数中文名"));
        this.Pub2.append(AddTDGroupTitleCenter("显示"));
        this.Pub2.append(AddTDGroupTitleCenter("操作"));
        this.Pub2.append(AddTREnd());

        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        for (MapAttr attr: attrs.ToJavaList())
        {
            if (attr.getUIVisible() == false)
                continue;

            MapExt myEn = null;
            for (MapExt en: ens.ToJavaList())
            {
                if (en.getAttrOfOper().equals( attr.getKeyOfEn()))
                {
                    myEn = en;
                    break;
                }
            }

            if (myEn == null)
            {
                this.Pub2.append(AddTRTX());
                this.Pub2.append(AddTD(attr.getKeyOfEn() + "-" + attr.getName()));
                this.Pub2.append(AddTD("无"));
                this.Pub2.append(AddTD("无"));
                this.Pub2.append(AddTD("无"));
                this.Pub2.append(AddTDBegin());
                this.Pub2.append(AddEasyUiLinkButton("编辑",
                                              ""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() +
                                              "&RefNo=" + attr.getMyPK() + "&OperAttrKey=" + attr.getKeyOfEn() + "&DoType=New",
                                              "icon-edit",false));
                this.Pub2.append(AddTDEnd());
                //this.Pub2.AddTDA(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&RefNo=" + attr.MyPK + "&OperAttrKey=" + attr.KeyOfEn + "&DoType=New", "<img src='../Img/Btn/Edit.gif' border=0/>编辑");
                this.Pub2.append(AddTREnd());
            }
            else
            {
                this.Pub2.append(AddTRTX());
                this.Pub2.append(AddTD(attr.getKeyOfEn() + "-" + attr.getName()));

                if (myEn.getDoWay() == 0)
                    this.Pub2.append(AddTD("系统函数"));
                else
                    this.Pub2.append(AddTD("自定义函数"));

                String file = myEn.getTag();
                file = file.substring(file.lastIndexOf('\\') + 4);
                file = file.replace(".js", "");

                this.Pub2.append(AddTDA(""+basePath+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + myEn.getMyPK() + "&RefNo=" + attr.getMyPK() + "&OperAttrKey=" + attr.getKeyOfEn(), file));

                this.Pub2.append(AddTD(myEn.getTag2() + "=" + myEn.getTag1() + "(this);"));

                //this.Pub2.AddTD("<a href=\"javascript:DoDel('" + myEn.MyPK + "','" + this.getFK_MapData() + "','" + this.getExtType() + "');\" ><img src='../Img/Btn/Delete.gif' border=0/>删除</a>");
                this.Pub2.append(AddTDBegin());
                this.Pub2.append(AddEasyUiLinkButton("删除",
                                              "javascript:DoDel('" + myEn.getMyPK() + "','" + this.getFK_MapData() + "','" +
                                              this.getExtType() + "');", "icon-delete",false));
                this.Pub2.append(AddTDEnd());
                this.Pub2.append(AddTREnd());
            }
        }
        this.Pub2.append(AddTableEnd());
    }
	
}

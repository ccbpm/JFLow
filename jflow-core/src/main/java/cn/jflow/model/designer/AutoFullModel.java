package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.FieldTypeS;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXml;
import BP.Sys.MapExtXmls;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class AutoFullModel extends BaseModel{
	
	public static final String RB_WAY_GROUP_NAME = "RB_Way_Group";
	public static final String RB_FK_GROUP_NAME = "RB_FK_Group";
	
	public StringBuffer Left=null;
	
	public StringBuffer Pub1=null;
	
	public AutoFullModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Left=new StringBuffer();
		Pub1=new StringBuffer();
	}
	
	//#region 属性
    /// <summary>
    /// 执行类型
    /// </summary>
//    public String getDoType()
//    {
//            return this.get_request().getParameter("DoType");
//    }
    public int getFType()
    {
            return Integer.parseInt(this.get_request().getParameter("FType"));
    }
    public String getIDX()
    {
            return getParameter("IDX");
    }

    //#region 属性
    public void BindTop()
    {
        // this.Pub1.Add("\t\n<div id='tabsJ'  align='center'>");
        MapExtXmls fss = new MapExtXmls();
        fss.RetrieveAll();

        //this.Left.Add("<a href='http://ccflow.org' target=_blank  ><img src='../../DataUser/ICON/" + BP.Sys.SystemConfig.CompanyID + "/LogBiger.png' style='width:180px;' /></a><hr>");

        //this.Left.Add("<a href='http://ccflow.org' target=_blank ><img src='../../DataUser/LogBiger.png' /></a><hr>");

        this.Left.append(AddUL("class='navlist'"));
        for (MapExtXml fs: fss.ToJavaList())
        {
            if (this.getPageID().equals(fs.getNo()))
            {
                //this.Left.AddLiB(fs.URL + "&FK_MapData=" + this.FK_MapData + "&ExtType=" + fs.No + "&RefNo=" + this.RefNo, "<span>" + fs.Name + "</span>");
                this.Left.append("<li style=\"font-weight:bold\"><div><a href=\"" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
            }
            else
            {
                //this.Left.AddLi(fs.URL + "&FK_MapData=" + this.FK_MapData + "&ExtType=" + fs.No + "&RefNo=" + this.RefNo, "<span>" + fs.Name + "</span>");
                this.Left.append("<li><div><a href=\"" + fs.getURL() + "&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + fs.getNo() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">" + fs.getName() + "</span></a></div></li>");
            }
        }

        //this.Left.AddLi("<a href='MapExt.aspx?FK_MapData=" + this.FK_MapData + "&RefNo=" + this.RefNo + "'><span>帮助</span></a>");

        this.Left.append("<li><div><a href=\"MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&RefNo=" + this.getRefNo() + "\"><span class=\"nav\">帮助</span></a></div></li>");

        this.Left.append(AddULEnd());
    }
    //#endregion 属性

    public void init()
    {
        this.BindTop();

        if (StringHelper.isNullOrEmpty(this.getRefNo()))
        {
            /*请选择要设置的字段*/
            MapAttrs mattrs = new MapAttrs();
            mattrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData());

            //this.Pub1.AddFieldSet("请选择要设置的字段");
            this.Pub1.append(AddEasyUiPanelInfoBegin("1","选择要设置的字段"));
            this.Pub1.append(AddUL("class='navlist'"));

            for (MapAttr en: mattrs.ToJavaList())
            {

                if (en.getUIVisible() == false && en.getUIIsEnable() == false)
                    continue;

                //this.Pub1.AddLi("?FK_MapData=" + this.FK_MapData + "&RefNo=" + en.MyPK + "&ExtType=AutoFull", en.KeyOfEn + " - " + en.Name);
                this.Pub1.append("<li style=\"font-weight:bold\"><div><a href=\"?FK_MapData=" + this.getFK_MapData() +
                              "&RefNo=" + en.getMyPK() + "&ExtType=AutoFull\"><span class=\"nav\">" + en.getKeyOfEn() + " - " +
                              en.getName() + "</span></a></div></li>");
                ;
            }

            this.Pub1.append(AddULEnd());
            //this.Pub1.AddFieldSetEnd();
            this.Pub1.append(AddEasyUiPanelInfoEnd());
            return;
        }

        MapAttr mattr = new MapAttr(this.getRefNo());
        Attr attr = mattr.getHisAttr();
//        this.Title()="为[" + mattr.getKeyOfEn() + "][" + mattr.getName() + "]设置自动完成"; // this.ToE("GuideNewField");

        switch (attr.getMyDataType())
        {
            case BP.DA.DataType.AppRate:
            case BP.DA.DataType.AppMoney:
            case BP.DA.DataType.AppInt:
            case BP.DA.DataType.AppFloat:
                BindNumType(mattr);
                break;
            case BP.DA.DataType.AppString:
                BindStringType(mattr);
                break;
            default:
                BindStringType(mattr);
                break;
        }

    }
    public void BindStringType(MapAttr mattr)
    {
        BindNumType(mattr);
    }
    public void BindNumType(MapAttr mattr_del)
    {
        MapExt me = new MapExt();
        me.setMyPK(this.getRefNo() + "_AutoFull");
        me.RetrieveFromDBSources();

        //this.Pub1.AddTable("align=left");
        this.Pub1.append(AddTable("class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%'"));
        //this.Pub1.AddCaptionLeft("数据获取 - 当一个字段值需要从其它表中得到时，请设置此功能。");
        this.Pub1.append(AddTRGroupTitle(5,"数据获取 - 当一个字段值需要从其它表中得到时，请设置此功能。"));
        this.Pub1.append(AddTR());
        //this.Pub1.Add("<TD>");
        this.Pub1.append(AddTDBegin());

        RadioButton rb = new RadioButton();
        rb.setGroupName(RB_WAY_GROUP_NAME);
        rb.setText("方式0：不做任何设置。");
        rb.setId("RB_Way_0");
        rb.setValue(rb.getId());
        if (me.getTag().equals("0"))
            rb.setChecked(true);

        //this.Pub1.AddFieldSet(rb);
        //this.Pub1.Add(  "不做任何设置。");
        //this.Pub1.AddFieldSetEnd();

        this.Pub1.append(rb);
        this.Pub1.append(AddBR());
        this.Pub1.append("不做任何设置。");

        this.Pub1.append(AddTDEnd());
        this.Pub1.append(AddTREnd());

        this.Pub1.append(AddTR());
        //this.Pub1.Add("<TD>");
        this.Pub1.append(AddTDBegin());

        rb = new RadioButton();
        rb.setGroupName(RB_WAY_GROUP_NAME);
        rb.setText("方式1：本表单中数据计算。"); //"";
        rb.setId("RB_Way_1");
        rb.setValue(rb.getId());
        if (me.getTag().equals("1"))
            rb.setChecked(true);

        //this.Pub1.AddFieldSet(rb);
        //this.Pub1.Add( "比如:@单价*@数量");
        //this.Pub1.AddBR();
        this.Pub1.append(rb);
        this.Pub1.append(AddBR());
        this.Pub1.append("比如:@单价*@数量。");
        this.Pub1.append(AddBR());

        TextBox tb = new TextBox();
        tb.setId("TB_JS");
        //tb.Width = 450;
        tb.addAttr("width", "99%");
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(5);
        if (me.getTag().equals("1"))
            tb.setText(me.getDoc());

        this.Pub1.append(tb);
        //this.Pub1.AddFieldSetEnd();
        this.Pub1.append(AddTDEnd());
        this.Pub1.append(AddTREnd());

        // 方式2 利用SQL自动填充
        this.Pub1.append(AddTR());
        //this.Pub1.Add("<TD>");
        this.Pub1.append(AddTDBegin());

        rb = new RadioButton();
        rb.setGroupName(RB_WAY_GROUP_NAME);
        rb.setText("方式2：利用SQL自动填充(此功能已经在ccflow5中取消，需要此功能的请把逻辑放入的节点或者表单事件里完成)。");
        rb.setId("RB_Way_2");
        rb.setValue(rb.getId());
        rb.setEnabled(false);

        if (me.getTag().equals("2"))
            rb.setChecked(true);

       // if (mattr.HisAutoFull == AutoFullWay.Way2_SQL)

        //this.Pub1.AddFieldSet(rb);
        this.Pub1.append(rb);
        this.Pub1.append(AddBR());
        this.Pub1.append( "比如:Select Addr From 商品表 WHERE No=@FK_Pro  FK_Pro是本表中的任意字段名<BR>");

        tb = new TextBox();
        tb.setId("TB_SQL");
        //tb.Width = 450;
        tb.addAttr("width", "99%");
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setRows(5);
        if (me.getTag().equals("2"))
            tb.setText(me.getDoc());

        this.Pub1.append(tb);

        //this.Pub1.AddFieldSetEnd();
        this.Pub1.append(AddTDEnd());
        this.Pub1.append(AddTREnd());

        // 方式3 本表单中外键列
        this.Pub1.append(AddTR());
        //this.Pub1.Add("<TD>");
        this.Pub1.append(AddTDBegin());

        rb = new RadioButton();
        rb.setGroupName(RB_WAY_GROUP_NAME);
        rb.setText("方式3：本表单中外键列。") ;
        // rb.getText( = "方式3：本表单中外键列</font></b>";
        rb.setId("RB_Way_3");
        rb.setValue(rb.getId());
        if (me.getTag().equals("3"))
            rb.setChecked(true);

        //if (mattr.HisAutoFull == AutoFullWay.Way3_FK)

        //this.Pub1.AddFieldSet(rb);

        // 让它等于外键表的一个值。
        Attrs attrs = null;
        MapData md = new MapData();
        md.setNo(this.getFK_MapData());
        if (md.RetrieveFromDBSources() == 0)
        {
            attrs = md.GenerHisMap().getHisFKAttrs();
        }
        else
        {
            MapDtl mdtl = new MapDtl();
            mdtl.setNo(this.getFK_MapData());
            attrs = mdtl.GenerMap().getHisFKAttrs();
        }
        String msge = "";

        if (attrs.size() > 0)
        {
        }
        else
        {
            rb.setEnabled(false);
            if (rb.getChecked())
                rb.setChecked(false);
            msge = "@本表没有外键字段。";
        }
        
        this.Pub1.append(rb.toString());
        this.Pub1.append(AddBR());
        this.Pub1.append("比如:表单中有商品编号列,需要填充商品地址、供应商电话。");
        this.Pub1.append(AddBR());
        
        this.Pub1.append(msge);

        for(Attr attr:attrs)
        {
            if (attr.getIsRefAttr())
                continue;

            rb = new RadioButton();
            rb.setText(attr.getDesc());
            rb.setId("RB_FK_" + attr.getKey());
            rb.setGroupName(RB_FK_GROUP_NAME);
            rb.setValue(rb.getId());
            if (me.getDoc().contains(attr.getKey()))
                rb.setChecked(true);

            this.Pub1.append(rb);
            DDL ddl = new DDL();
            ddl.setId("DDL_" + attr.getKey());

            String sql = "";
            switch (BP.Sys.SystemConfig.getAppCenterDBType())
            {
                case Oracle:
                case Informix:
                    continue;
                   // sql = "Select fname as No ,fDesc as Name FROM Sys_FieldDesc WHERE tableName='" + attr.getHisFKEn().getEnMap().getPhysicsTable() + "'";
                    //break;
                case MySQL:
//                    sql = "Select COLUMN_NAME as No,COLUMN_NAME as Name from information_schema.COLUMNS WHERE TABLE_NAME='" + attr.getHisFKEn().getEnMap().getPhysicsTable() + "'";
                    break;
                default:
                    sql = "Select name as No ,Name as Name from syscolumns WHERE ID=OBJECT_ID('" + attr.getHisFKEn().getEnMap().getPhysicsTable() + "')";
                    break;
            }

            //  string sql = "Select fname as 'No' ,fDesc as 'Name' FROM Sys_FieldDesc WHERE tableName='" + attr.HisFKEn.getEnMap().getPhysicsTable() + "'";
            //string sql = "Select NO , NAME  FROM Port_Emp ";

            DataTable dt = DBAccess.RunSQLReturnTable(sql);
            for(DataRow dr:dt.Rows)
            {
                //  ddl.Items.Add(new ListItem(this.ToE("Field") + dr.getValue(0).ToString() + " " + this.ToE("Desc") + " " + dr.getValue(1).ToString(), dr.getValue(0).ToString()));
                ListItem li = new ListItem(dr.get("no").toString() + "；" + dr.get("name").toString(), dr.get("no").toString());
                if (me.getDoc().contains(dr.get("no").toString()))
                    li.setSelected(true);

                ddl.Items.add(li);
            }

            this.Pub1.append(ddl);
            this.Pub1.append(AddBR());
        }

        //this.Pub1.AddFieldSetEnd();
        this.Pub1.append(AddTDEnd());
        this.Pub1.append(AddTREnd());

        // 方式3 本表单中外键列
        this.Pub1.append(AddTR());
        //this.Pub1.Add("<TD>");
        this.Pub1.append(AddTDBegin());

        rb = new RadioButton();
        rb.setGroupName(RB_WAY_GROUP_NAME);
        rb.setText("方式4：对一个从表的列求值。");
        rb.setId("RB_Way_4");
        rb.setValue(rb.getId());
        if (me.getTag().equals("4"))
            rb.setChecked(true);

        //this.Pub1.AddFieldSet(rb);
        this.Pub1.append(rb);
        this.Pub1.append( "比如:对从表中的列求值。");
        this.Pub1.append(AddBR());

        // 让它对一个从表求和、求平均、求最大、求最小值。
        MapDtls dtls = new MapDtls(this.getFK_MapData());
        if (dtls.size() > 0)
        {
        }
        else
        {
            rb.setEnabled(false);
            if (rb.getChecked())
                rb.setChecked(false);
            // this.Pub1.Add("@没有从表。");
        }
        for (MapDtl dtl: dtls.ToJavaList())
        {
            DDL ddlF = new DDL();
            ddlF.setId("DDL_" + dtl.getNo() + "_F");
            MapAttrs mattrs1 = new MapAttrs(dtl.getNo());
            int count = 0;
            for (MapAttr mattr1: mattrs1.ToJavaList())
            {
                if (mattr1.getLGType() != FieldTypeS.Normal)
                    continue;

                if (mattr1.getKeyOfEn().equals(MapAttrAttr.MyPK))
                    continue;

                if (mattr1.getIsNum() == false)
                    continue;
                String keyofEn = mattr1.getKeyOfEn();
                if(keyofEn.equals("OID") || keyofEn.equals("RefOID")|| keyofEn.equals("FID")){
                	 continue;
                }
                count++;
                ListItem li = new ListItem(mattr1.getName(), mattr1.getKeyOfEn());
                if (me.getTag().equals("4"))
                    if (me.getDoc().contains("=" + mattr1.getKeyOfEn()))
                        li.setSelected(true);
                ddlF.Items.add(li);
            }
            if (count == 0)
                continue;

            rb = new RadioButton();
            rb.setText(dtl.getName());
            rb.setId("RB_" + dtl.getNo());
            rb.setGroupName("dtl");
            if (me.getDoc().contains(dtl.getNo()))
                rb.setChecked(true);

            this.Pub1.append(rb);

            DDL ddl = new DDL();
            ddl.setId("DDL_" + dtl.getNo() + "_Way");
            ddl.Items.add(new ListItem("求合计", "SUM"));
            ddl.Items.add(new ListItem("求平均", "AVG"));
            ddl.Items.add(new ListItem("求最大", "MAX"));
            ddl.Items.add(new ListItem("求最小", "MIN"));
            this.Pub1.append(ddl);

            if (me.getTag().equals("4"))
            {
                if (me.getDoc().contains("SUM"))
                    ddl.SetSelectItem("SUM");
                if (me.getDoc().contains("AVG"))
                    ddl.SetSelectItem("AVG");
                if (me.getDoc().contains("MAX"))
                    ddl.SetSelectItem("MAX");
                if (me.getDoc().contains("MIN"))
                    ddl.SetSelectItem("MIN");
            }

            this.Pub1.append(ddlF);
            this.Pub1.append(AddBR());
        }

        //this.Pub1.AddFieldSetEnd();
        this.Pub1.append(AddTDEnd());
        this.Pub1.append(AddTREnd());


        //#region 方式5
        //this.Pub1.AddTD();
        //this.Pub1.AddTR();

        //this.Pub1.AddFieldSet(rb);
        //this.Pub1.Add(this.ToE("Way2D", "嵌入的JS"));
        //tb = new TextBox();
        //tb.ID = "TB_JS";
        //tb.Width = 450;
        //tb.getText(Mode = TextBoxMode.MultiLine;
        //tb.Rows = 5;
        //if (mattr.HisAutoFull == AutoFullWay.Way5_JS)
        //    tb.getText( = mattr.AutoFullDoc;
        //this.Pub1.Add(tb);
        //this.Pub1.AddFieldSetEnd();

        //this.Pub1.AddTDEnd();
        //this.Pub1.AddTREnd();
        //#endregion 方式5
        
        this.Pub1.append(AddTableEnd());
        this.Pub1.append(AddBR());
        this.Pub1.append(AddBR());

        //this.Pub1.AddTRSum();
        //this.Pub1.AddTDBegin("aligen=center");
        //Button btn = new Button();
        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.setHref("void(0);");
        btn.attributes.put("onclick", "save_data();");
        
        this.Pub1.append(btn);
        this.Pub1.append(AddSpace(1));

        btn = new LinkButton(false, NamesOfBtn.SaveAndClose.toString(), "保存并关闭");
        btn.setHref("void(0);");
        btn.attributes.put("onclick", "save_data_cancel();");
        this.Pub1.append(btn);
        //this.Pub1.AddTREnd();
        //this.Pub1.AddTableEnd();
        //return;
    }
    /// <summary>
    /// 
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
//    void btn_Click(object sender, EventArgs e)
//    {
//        MapAttr mattrNew = new MapAttr(this.RefNo);
//
//        MapExt me = new MapExt();
//        me.MyPK =   this.RefNo + "_AutoFull";
//        me.RetrieveFromDBSources();
//        me.FK_MapData = this.FK_MapData;
//        me.AttrOfOper = mattrNew.KeyOfEn;
//        me.ExtType = MapExtXmlList.AutoFull;
//        if (this.Pub1.GetRadioButtonByID("RB_Way_0").Checked)
//        {
//            me.Tag = "0";
//        }
//
//        // JS 方式。
//        if (this.Pub1.GetRadioButtonByID("RB_Way_1").Checked)
//        {
//            me.Tag = "1";
//            me.Doc = this.Pub1.GetTextBoxByID("TB_JS").getText(;
//
//            /*检查字段是否填写正确.*/
//            MapAttrs attrsofCheck = new MapAttrs(this.FK_MapData);
//            string docC = me.Doc;
//            foreach (MapAttr attrC in attrsofCheck)
//            {
//                if (attrC.IsNum == false)
//                    continue;
//                docC = docC.Replace("@" + attrC.KeyOfEn, "");
//                docC = docC.Replace("@" + attrC.Name, "");
//            }
//
//            if (docC.Contains("@"))
//            {
//                this.Alert("您填写的表达公式不正确，导致一些数值类型的字段没有被正确的替换。" + docC);
//                return;
//            }
//        }
//
//        // 外键方式。
//        if (this.Pub1.GetRadioButtonByID("RB_Way_2").Checked)
//        {
//            me.Tag = "2";
//            me.Doc = this.Pub1.GetTextBoxByID("TB_SQL").getText(;
//
//            //mattr.HisAutoFull = AutoFullWay.Way2_SQL;
//            //mattr.AutoFullDoc = this.Pub1.GetTextBoxByID("TB_SQL").getText(;
//        }
//
//        // 本表单中外键列。
//        string doc = "";
//        if (this.Pub1.GetRadioButtonByID("RB_Way_3").Checked)
//        {
//            me.Tag = "3";
//
//           // mattr.HisAutoFull = AutoFullWay.Way3_FK;
//            MapData md = new MapData(this.FK_MapData);
//            Attrs attrs = md.GenerHisMap().HisFKAttrs;
//            foreach (Attr attr in attrs)
//            {
//                if (attr.IsRefAttr)
//                    continue;
//
//                if (this.Pub1.GetRadioButtonByID("RB_FK_" + attr.Key).Checked == false)
//                    continue;
//                // doc = " SELECT " + this.Pub1.GetDDLByID("DDL_" + attr.Key).SelectedValue + " FROM " + attr.HisFKEn.getEnMap().getPhysicsTable() + " WHERE NO=@" + attr.Key;
//                doc = "@AttrKey=" + attr.Key + "@Field=" + this.Pub1.GetDDLByID("DDL_" + attr.Key).SelectedValue + "@Table=" + attr.HisFKEn.getEnMap().getPhysicsTable();
//            }
//            me.Doc = doc;
//        }
//
//        // 本表单中从表列。
//        if (this.Pub1.GetRadioButtonByID("RB_Way_4").Checked)
//        {
//            me.Tag = "4";
//
//            MapDtls dtls = new MapDtls(this.FK_MapData);
//         //   mattr.HisAutoFull = AutoFullWay.Way4_Dtl;
//            foreach (MapDtl dtl in dtls)
//            {
//                try
//                {
//                    if (this.Pub1.GetRadioButtonByID("RB_" + dtl.No).Checked == false)
//                        continue;
//                }
//                catch
//                {
//                    continue;
//                }
//                //  doc = "SELECT " + this.Pub1.GetDDLByID( "DDL_"+dtl.No + "_Way").SelectedValue + "(" + this.Pub1.GetDDLByID("DDL_"+dtl.No+"_F").SelectedValue + ") FROM " + dtl.No + " WHERE REFOID=@OID";
//                doc = "@Table=" + dtl.No + "@Field=" + this.Pub1.GetDDLByID("DDL_" + dtl.No + "_F").SelectedValue + "@Way=" + this.Pub1.GetDDLByID("DDL_" + dtl.No + "_Way").SelectedValue;
//            }
//            me.Doc = doc;
//        }
//
//        try
//        {
//            me.Save();
//        }
//        catch (Exception ex)
//        {
//            this.ResponseWriteRedMsg(ex);
//            return;
//        }
//
//        this.Alert("保存成功");
//        this.Pub1.Clear();
//        //Button btn = sender as Button;
//        var btn = sender as LinkBtn;
//        if (btn.ID.Contains("Close"))
//        {
//            this.WinClose();
//            return;
//        }
//        else
//        {
//            this.Response.Redirect(this.Request.RawUrl, true);
//        }
//    }
    public void BindStringType()
    {
    }
    public String GetCaption()
    {
            if (this.getDoType().equals("Add"))
                return "新增向导" + " - <a href='Do.jsp?DoType=ChoseFType'>选择类型</a> - " + "编辑";
            else
                return "<a href='Do.jsp?DoType=ChoseFType&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo() + "'>选择类型</a> - " + "编辑";
    }
}

package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.GPM.Dept;
import BP.GPM.Depts;
import BP.GPM.Emps;
import BP.Port.Station;
import BP.Port.Stations;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.OSModel;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.PushMsg;
import BP.WF.Template.PushMsgAttr;
import BP.WF.Template.PushMsgs;
import BP.WF.Template.PushWay;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;


public class ActionPush2SpecModel extends BaseModel{

	public StringBuffer Pub1=null;
	
	public ActionPush2SpecModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuffer();
	}
	

    private String Event;


    public String getEvent() {
		return this.get_request().getParameter("Event");
	}

	public void setEvent(String event) {
		Event = event;
	}




    public String ThePushWay;


    public String getThePushWay() {
		return ThePushWay;
	}

	public void setThePushWay(String thePushWay) {
		ThePushWay = thePushWay;
	}

	public void init()
    {
        PushMsgs msgs = new PushMsgs(this.getFK_Flow());
        PushMsg msg = (PushMsg)msgs.GetEntityByKey(PushMsgAttr.FK_Event, this.getEvent(), PushMsgAttr.FK_Node, this.getNodeID());

        if (msg == null)
        {
            msg = new PushMsg();
            msg.setFK_Event(this.getEvent());
            msg.setFK_Node(this.getNodeID());
        }

        if (!StringHelper.isNullOrEmpty(this.ThePushWay))
        {
            if (!this.getThePushWay().equals(""+msg.getPushWay()))
            {
                msg.setPushDoc("");
                msg.setTag("");
            }

            msg.setPushWay(Integer.parseInt(this.getThePushWay()));
        }

        this.Pub1.append(AddTable("class='Table' cellspacing='1' cellpadding='1' border='1' style='width:100%'"));

        this.Pub1.append(AddTR());
        this.Pub1.append(AddTD("style='width:100px'", "推送设置方式："));
        DDL ddl = new DDL();
        ddl.BindSysEnum(PushMsgAttr.PushWay);
        ddl.setId("DDL_" + PushMsgAttr.PushWay);
        ddl.setName("DDL_" + PushMsgAttr.PushWay);
        ddl.SetSelectItem((int)msg.getPushWay());
//        ddl.setAutoPostBack(true);
//        ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
        this.Pub1.append(AddTD(ddl));
        this.Pub1.append(AddTREnd());

        int i=msg.getPushWay();
        if(i==PushWay.ByParas.getValue()){
        	Pub1.append(AddTR());
            Pub1.append(AddTD("输入参数名："));
            Pub1.append(AddTDBegin());

            RadioButton rad = new RadioButton();
            rad.setGroupName("Para");
            rad.setId("RB_0");
            rad.setName("RB_0");
            rad.setText("系统参数");
            rad.setChecked("0".equals(msg.getPushDoc()));

            Pub1.append(rad);

            TextBox tb = new TextBox();
            tb.setId("TB_" + PushMsgAttr.Tag);
            tb.setName("TB_" + PushMsgAttr.Tag);
            if (msg.getPushDoc().equals("0"))
                tb.setText(msg.getTag());
            else
                tb.setText("NoticeTo");

            Pub1.append(tb);

            Pub1.append("&nbsp;默认为NoticeTo");
            Pub1.append(AddBR());

            rad = new RadioButton();
            rad.setGroupName("Para");
            rad.setId("RB_1");
            rad.setName("RB_1");
            rad.setText("表单字段参数");
            rad.setChecked("1".equals(msg.getPushDoc()));

            Pub1.append(rad);

            MapAttrs attrs = new MapAttrs();
            attrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + this.getNodeID());

            MapAttrs attrNs = new MapAttrs();

            for (MapAttr attr: attrs.ToJavaList())
            {
                if (attr.getIsBigDoc())
                    continue;
                
				// String a=attr.getKeyOfEn();
				//
				// if(a.equals("Title")){
				// }
				// else if(a.equals("FK_Emp")){
				// }
				// else if(a.equals("MyNum")){
				// }
				// else if(a.equals("FK_NY")){
				// }
				// else if(a.equals(WorkAttr.Emps)){
				//
				// }
				// else if(a.equals(WorkAttr.OID)){
				// }
				// else if(a.equals(StartWorkAttr.Rec)){
				// }
				// else if(a.equals(StartWorkAttr.FID)){
				// }

                attrNs.AddEntity(attr);
            }

            ddl = new DDL();
            ddl.setId("DDL_" + PushMsgAttr.Tag);
            ddl.setName("DDL_" + PushMsgAttr.Tag);
            ddl.BindEntities(attrNs, MapAttrAttr.MyPK, MapAttrAttr.Name);
//            ddl.setAutoPostBack(false);

            if ("1".equals(msg.getPushDoc()))
                ddl.SetSelectItem(msg.getTag());

            Pub1.append(ddl);
            Pub1.append(AddTREnd());
        }
        else if(i==PushWay.NodeWorker.getValue()){
        	//#region 按照指定结点的工作人员

            Pub1.append(AddTR());
            Pub1.append(AddTDBegin("colspan='2'"));

            Pub1.append("请选择要推送到的节点工作人员：<br />");
            Nodes nds = new Nodes(this.getFK_Flow());
            CheckBox cb = null;

            for(Node nd:nds.ToJavaList())
            {
                if (nd.getNodeID() == this.getNodeID())
                    continue;

                cb = new CheckBox();
                cb.setId("CB_" + nd.getNodeID());
                cb.setName("CB_" + nd.getNodeID());
                cb.setText(nd.getNodeID() + " &nbsp;" + nd.getName());
                cb.setChecked(msg.getPushDoc().contains("@" + nd.getNodeID() + "@"));
                Pub1.append(cb);
                Pub1.append(AddBR());
            }

            Pub1.append(AddTDEnd());
            Pub1.append(AddTREnd());
        }
        else if(i==PushWay.SpecDepts.getValue()){
        	//#region 按照指定的部门

            Pub1.append(AddTR());
            Pub1.append(AddTDBegin("colspan='2'"));

            this.Pub1.append(AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
            this.Pub1.append(AddTR());
            this.Pub1.append(AddTD("colspan='3' class='GroupTitle'", "部门选择"));
            this.Pub1.append(AddTREnd());

            //NodeDepts ndepts = new NodeDepts(int.Parse(this.NodeID));
            Depts depts = new Depts();
            depts.RetrieveAll();
            int iii = 0;

            //foreach (NodeDept dept in ndepts)
            for (Dept dept: depts.ToJavaList())
            {
                iii++;

                if (iii == 4)
                    iii = 1;

                if (iii == 1)
                    Pub1.append(AddTR());

                CheckBox cb = new CheckBox();
                //cb.ID = "CB_" + dept.FK_Dept;
                //cb.Text = (depts.GetEntityByKey(dept.FK_Dept) as Dept).Name;
                cb.setId("CB_" + dept.getNo());
                cb.setName("CB_" + dept.getNo());
                cb.setText(dept.getName());

                //if (msg.PushDoc.Contains("@" + dept.FK_Dept + "@"))
                if (msg.getPushDoc().contains("@" + dept.getNo() + "@"))
                    cb.setChecked(true);

                this.Pub1.append(AddTD(cb));

                if (iii == 3)
                    Pub1.append(AddTREnd());
            }

            switch (iii)
            {
                case 1:
                    Pub1.append(AddTD());
                    Pub1.append(AddTD());
                    Pub1.append(AddTREnd());
                    break;
                case 2:
                    Pub1.append(AddTD());
                    Pub1.append(AddTREnd());
                    break;
                default:
                    break;
            }

            this.Pub1.append(AddTableEnd());
            Pub1.append(AddTDEnd());
            Pub1.append(AddTREnd());
        }
        else if(i==PushWay.SpecEmps.getValue()){
        	//#region 按照指定的人员

            Pub1.append(AddTR());
            //Pub1.AddTDBegin("colspan='2'");

            Pub1.append(AddTD("选择人员："));
            Pub1.append(AddTDBegin());

            TextBox tb = new TextBox();
            tb.setId("TB_Users");
            tb.setName("TB_Users");
            tb.setTextMode(TextBoxMode.MultiLine);
            tb.addAttr("width", "99%");
            tb.setRows(4);
            tb.setReadOnly(true);

            TextBox hf = new TextBox();
            hf.setTextMode(TextBoxMode.Hidden);
            hf.setId("HID_Users");
            hf.setName("HID_Users");
            //加载已经选择的人员
            if (!StringHelper.isNullOrEmpty(msg.getPushDoc()))
            {
                hf.setText(msg.getPushDoc().replace("@@", ","));

                Emps emps = new Emps();
                emps.RetrieveAll();

//                tb.setText(
//                    hf.Value.Split(',').Select(o => (emps.GetEntityByKey(o) as Emp).Name).Aggregate(
//                        string.Empty, (curr, next) => curr + next + ",").TrimEnd(','));
            }

            Pub1.append(tb);
            Pub1.append(hf);
            Pub1.append(AddBR());
            Pub1.append(AddBR());

            Pub1.append(
                "<a class='easyui-linkbutton' data-options=\"iconCls:'icon-user'\" href='javascript:void(0)' onclick=\"showWin('../Comm/Port/SelectUser_Jq.aspx','" +
                tb.getId()+ "','" + hf.getId() + "');\">选择人员...</a>");
            Pub1.append(AddTDEnd());
            //Pub1.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'");
            //depts = new Depts();
            //depts.RetrieveAll();
            //var emps = new Emps();
            //emps.RetrieveAll();
            //var empDepts = new EmpDepts();
            //empDepts.RetrieveAll();
            //var nemps = new NodeEmps(int.Parse(this.NodeID));

            //Emp emp = null;

            //foreach (Dept dept in depts)
            //{
            //    this.Pub1.AddTR();
            //    var mycb = new CheckBox();
            //    mycb.Text = dept.Name;
            //    mycb.ID = "CB_D_" + dept.No;
            //    this.Pub1.AddTD("colspan='3' class='GroupTitle'", mycb);
            //    this.Pub1.AddTREnd();

            //    i = 0;
            //    string ctlIDs = "";

            //    foreach (EmpDept ed in empDepts)
            //    {
            //        if (ed.FK_Dept != dept.No)
            //            continue;

            //        //排除非当前结点绑定的人员
            //        if (nemps.GetEntityByKey(NodeEmpAttr.FK_Emp, ed.FK_Emp) == null)
            //            continue;

            //        i++;

            //        if (i == 4)
            //            i = 1;

            //        if (i == 1)
            //            Pub1.AddTR();

            //        emp = emps.GetEntityByKey(ed.FK_Emp) as Emp;

            //        cb = new CheckBox();
            //        cb.ID = "CB_E_" + emp.No;
            //        ctlIDs += cb.ID + ",";
            //        cb.Text = emp.Name;
            //        if (msg.PushDoc.Contains("@" + emp.No + "@"))
            //            cb.Checked = true;

            //        Pub1.AddTD(cb);

            //        if (i == 3)
            //            Pub1.AddTREnd();
            //    }

            //    mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";

            //    switch (i)
            //    {
            //        case 1:
            //            Pub1.AddTD();
            //            Pub1.AddTD();
            //            Pub1.AddTREnd();
            //            break;
            //        case 2:
            //            Pub1.AddTD();
            //            Pub1.AddTREnd();
            //            break;
            //        default:
            //            break;
            //    }
            //}

            //Pub1.AddTableEnd();

            //Pub1.AddTDEnd();
            Pub1.append(AddTREnd());
        }
        if(i==PushWay.SpecSQL.getValue()){
        	 //#region 按照指定的SQL查询语句

            Pub1.append(AddTR());

            this.Pub1.append(AddTDBegin("colspan='2'"));
            this.Pub1.append("SQL查询语句：<br />");
            TextBox tb = new TextBox();
            tb.setId("TB_" + PushMsgAttr.PushDoc);
            tb.setName("TB_" + PushMsgAttr.PushDoc);
            tb.setCols(50);
            tb.addAttr("width", "99%");
            tb.setTextMode(TextBoxMode.MultiLine);
            tb.setRows(4);
            tb.setText(msg.getPushDoc());
            this.Pub1.append(tb);
            this.Pub1.append(AddTDEnd());
            Pub1.append(AddTREnd());
        }
        else if(i==PushWay.SpecStations.getValue()){
        	//#region 按照指定的岗位

            Pub1.append(AddTR());
            Pub1.append(AddTDBegin("colspan='2'"));

            if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
            {
                SysEnums ses = new SysEnums("StaGrade");
                Stations sts = new Stations();
                sts.RetrieveAll();

                String sql = "SELECT No,Name FROM Port_Station WHERE StaGrade  NOT IN (SELECT IntKey FROM Sys_Enum WHERE EnumKey='StaGrade')";
                DataTable dt = DBAccess.RunSQLReturnTable(sql);
                if (dt.Rows.size() != 0)
                {
                    if (ses.size() == 0)
                    {
                        SysEnum se = new SysEnum();
                        se.setEnumKey("StaGrade");
                        se.setLab("普通岗");
                        se.setIntKey(0);
                        se.Insert();

                        ses.AddEntity(se);
                    }

                    for (Station st: sts.ToJavaList())
                    {
                        st.setStaGrade(0);
                        st.Save();
                    }
                }

                this.Pub1.append(AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));

                for (SysEnum se: ses.ToJavaList())
                {
                    this.Pub1.append(AddTR());
                    CheckBox mycb = new CheckBox();
                    mycb.setText(se.getLab());
                    mycb.setId("CB_SG_" + se.getIntKey());
                    mycb.setName("CB_SG_" + se.getIntKey());
                    this.Pub1.append(AddTD("colspan='3' class='GroupTitle'", mycb));
                    this.Pub1.append(AddTREnd());

                    i = 0;
                    String ctlIDs = "";

                    for (Station st: sts.ToJavaList())
                    {
                        if (st.getStaGrade()!= se.getIntKey())
                            continue;

                        i++;

                        if (i == 4)
                            i = 1;

                        if (i == 1)
                            Pub1.append(AddTR());

                        CheckBox cb = new CheckBox();
                        cb.setId("CB_S_" + st.getNo());
                        cb.setName("CB_S_" + st.getNo());
                        ctlIDs += cb.getId() + ",";
                        cb.setText(st.getName());

                        if (msg.getPushDoc().contains("@" + st.getNo() + "@"))
                            cb.setChecked(true);

                        Pub1.append(AddTD(cb));

                        if (i == 3)
                            Pub1.append(AddTREnd());
                    }

                    mycb.addAttr("onclick", "onSelected");
//                    mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";

                    switch (i)
                    {
                        case 1:
                            Pub1.append(AddTD());
                            Pub1.append(AddTD());
                            Pub1.append(AddTREnd());
                            break;
                        case 2:
                            Pub1.append(AddTD());
                            Pub1.append(AddTREnd());
                            break;
                        default:
                            break;
                    }
                }

                this.Pub1.append(AddTableEnd());
            }
            else
            {
                /*BPM 模式*/
                BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
                tps.RetrieveAll();

                BP.GPM.Stations sts = new BP.GPM.Stations();
                sts.RetrieveAll();

                String sql = "SELECT No,Name FROM Port_Station WHERE FK_StationType NOT IN (SELECT No FROM Port_StationType)";
                DataTable dt = DBAccess.RunSQLReturnTable(sql);
                if (dt.Rows.size() != 0)
                {
                    if (tps.size() == 0)
                    {
                    	BP.GPM.StationType stp = new BP.GPM.StationType();// { No = "01", Name = "普通岗" };
                    	stp.setNo("01");
                    	stp.setName("普通岗");
                    	stp.Save();

                        tps.AddEntity(stp);
                    }

                    //更新所有对不上岗位类型的岗位，岗位类型为01或第一个
                    for (BP.GPM.Station st: sts.ToJavaList())
                    {
                        st.setFK_StationType(tps.convertStationTypes(tps).get(0).getNo());
                        st.Update();
                    }
                }

                this.Pub1.append(AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));

                for (BP.GPM.StationType tp: tps.ToJavaList())
                {
                    this.Pub1.append(AddTR());
                    CheckBox mycb = new CheckBox();
                    mycb.setText(tp.getName());
                    mycb.setId("CB_ST_" + tp.getNo());
                    mycb.setName("CB_ST_" + tp.getNo());
                    this.Pub1.append(AddTD("colspan='3' class='GroupTitle'", mycb));
                    this.Pub1.append(AddTREnd());

                    i = 0;
                    String ctlIDs = "";

                    for (BP.GPM.Station st: sts.ToJavaList())
                    {
                        if (st.getFK_StationType() != tp.getNo())
                            continue;

                        i++;

                        if (i == 4)
                            i = 1;

                        if (i == 1)
                        {
                            Pub1.append(AddTR());
                        }

                        CheckBox cb = new CheckBox();
                        cb.setId("CB_S_" + st.getNo());
                        cb.setName("CB_S_" + st.getNo());
                        ctlIDs += cb.getId() + ",";
                        cb.setText(st.getName());

                        if (msg.getPushDoc().contains("@" + st.getNo() + "@"))
                            cb.setChecked(true);

                        this.Pub1.append(AddTD(cb));

                        if (i == 3)
                            Pub1.append(AddTREnd());
                    }

                    mycb.addAttr("onclick", "onSelected1");//.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";

                    switch (i)
                    {
                        case 1:
                            Pub1.append(AddTD());
                            Pub1.append(AddTD());
                            Pub1.append(AddTREnd());
                            break;
                        case 2:
                            Pub1.append(AddTD());
                            Pub1.append(AddTREnd());
                            break;
                        default:
                            break;
                    }
                }

                this.Pub1.append(AddTableEnd());
            }

            //#region 原逻辑，只考虑了一种模式，停用
            //Pub1.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'");
            //SysEnums ses = new SysEnums("StaGrade");
            //Stations sts = new Stations();
            //NodeStations nsts = new NodeStations(int.Parse(this.NodeID));
            //sts.RetrieveAll();

            //foreach (SysEnum se in ses)
            //{
            //    this.Pub1.AddTR();
            //    var mycb = new CheckBox();
            //    mycb.Text = se.Lab;
            //    mycb.ID = "CB_SG_" + se.IntKey;
            //    this.Pub1.AddTD("colspan=3 class='GroupTitle'", mycb);
            //    this.Pub1.AddTREnd();

            //    i = 0;
            //    string ctlIDs = "";

            //    foreach (Station st in sts)
            //    {
            //        if (st.StaGrade != se.IntKey)
            //            continue;

            //        //排除非当前结点的岗位
            //        if (nsts.GetEntityByKey(NodeStationAttr.FK_Station, st.No) == null)
            //            continue;

            //        i++;

            //        if (i == 4)
            //            i = 1;

            //        if (i == 1)
            //            Pub1.AddTR();

            //        cb = new CheckBox();
            //        cb.ID = "CB_S_" + st.No;
            //        ctlIDs += cb.ID + ",";
            //        cb.Text = st.Name;
            //        if (msg.PushDoc.Contains("@" + st.No + "@"))
            //            cb.Checked = true;

            //        Pub1.AddTD(cb);

            //        if (i == 3)
            //            Pub1.AddTREnd();
            //    }

            //    mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";

            //    switch (i)
            //    {
            //        case 1:
            //            Pub1.AddTD();
            //            Pub1.AddTD();
            //            Pub1.AddTREnd();
            //            break;
            //        case 2:
            //            Pub1.AddTD();
            //            Pub1.AddTREnd();
            //            break;
            //        default:
            //            break;
            //    }
            //}
            //Pub1.AddTableEnd();

            Pub1.append(AddTDEnd());
            Pub1.append(AddTREnd());
        }
        
//        switch (msg.getPushWay())
//        {
//        
//            case PushWay.ByParas.getValue():
//
//                //#region 按照系统指定参数
//
//                Pub1.append(AddTR());
//                Pub1.append(AddTD("输入参数名："));
//                Pub1.append(AddTDBegin());
//
//                RadioButton rad = new RadioButton();
//                rad.setGroupName("Para");
//                rad.setId("RB_0");
//                rad.setText("系统参数");
//                rad.setChecked(msg.getPushDoc() == "0");
//
//                Pub1.append(rad);
//
//                TextBox tb = new TextBox();
//                tb.setId("TB_" + PushMsgAttr.Tag);
//
//                if (msg.getPushDoc().equals("0"))
//                    tb.setText(msg.getTag());
//                else
//                    tb.setText("NoticeTo");
//
//                Pub1.append(tb);
//
//                Pub1.append("&nbsp;默认为NoticeTo");
//                Pub1.append(AddBR());
//
//                rad = new RadioButton();
//                rad.setGroupName("Para");
//                rad.setId("RB_1");
//                rad.setText("表单字段参数");
//                rad.setChecked(msg.getPushDoc() == "1");
//
//                Pub1.append(rad);
//
//                MapAttrs attrs = new MapAttrs();
//                attrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + this.getNodeID());
//
//                MapAttrs attrNs = new MapAttrs();
//
//                for (MapAttr attr: attrs.ToJavaList())
//                {
//                    if (attr.getIsBigDoc())
//                        continue;
//                    
//                    String a=attr.getKeyOfEn();
//                    if(a.equals("Title")){
//                    }
//                    else if(a.equals("FK_Emp")){
//                    }
//                    else if(a.equals("MyNum")){
//                    }
//                    else if(a.equals("FK_NY")){
//                    }
//                    else if(a.equals(WorkAttr.Emps)){
//                    }
//                    else if(a.equals(WorkAttr.OID)){
//                    }
//                    else if(a.equals(StartWorkAttr.Rec)){
//                    }
//                    else if(a.equals(StartWorkAttr.FID)){
//                    }
//
//                    attrNs.AddEntity(attr);
//                }
//
//                ddl = new DDL();
//                ddl.setId("DDL_" + PushMsgAttr.Tag);
//                ddl.BindEntities(attrNs, MapAttrAttr.MyPK, MapAttrAttr.Name);
////                ddl.setAutoPostBack(false);
//
//                if (msg.getPushDoc() == "1")
//                    ddl.SetSelectItem(msg.getTag());
//
//                Pub1.append(ddl);
//                Pub1.append(AddTREnd());
//                break;
//            case PushWay.NodeWorker.getValue():
//
//                //#region 按照指定结点的工作人员
//
//                Pub1.append(AddTR());
//                Pub1.append(AddTDBegin("colspan='2'"));
//
//                Pub1.append("请选择要推送到的节点工作人员：<br />");
//                Nodes nds = new Nodes(this.getFK_Flow());
//                CheckBox cb = null;
//
//                for (Node nd: nds.ToJavaList())
//                {
//                    if (nd.getNodeID() == this.getNodeID())
//                        continue;
//
//                    cb = new CheckBox();
//                    cb.setId("CB_" + nd.getNodeID());
//                    cb.setText(nd.getNodeID() + " &nbsp;" + nd.getName());
//                    cb.setChecked(msg.getPushDoc().contains("@" + nd.getNodeID() + "@"));
//                    Pub1.append(cb);
//                    Pub1.append(AddBR());
//                }
//
//                Pub1.append(AddTDEnd());
//                Pub1.append(AddTREnd());
//            case PushWay.SpecDepts.getValue():
//
//                //#region 按照指定的部门
//
//                Pub1.append(AddTR());
//                Pub1.append(AddTDBegin("colspan='2'"));
//
//                this.Pub1.append(AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
//                this.Pub1.append(AddTR());
//                this.Pub1.append(AddTD("colspan='3' class='GroupTitle'", "部门选择"));
//                this.Pub1.append(AddTREnd());
//
//                //NodeDepts ndepts = new NodeDepts(int.Parse(this.NodeID));
//                Depts depts = new Depts();
//                depts.RetrieveAll();
//                int i = 0;
//
//                //foreach (NodeDept dept in ndepts)
//                for (Dept dept: depts.ToJavaList())
//                {
//                    i++;
//
//                    if (i == 4)
//                        i = 1;
//
//                    if (i == 1)
//                        Pub1.append(AddTR());
//
//                    cb = new CheckBox();
//                    //cb.ID = "CB_" + dept.FK_Dept;
//                    //cb.Text = (depts.GetEntityByKey(dept.FK_Dept) as Dept).Name;
//                    cb.setId("CB_" + dept.getNo());
//                    cb.setText(dept.getName());
//
//                    //if (msg.PushDoc.Contains("@" + dept.FK_Dept + "@"))
//                    if (msg.getPushDoc().contains("@" + dept.getNo() + "@"))
//                        cb.setChecked(true);
//
//                    this.Pub1.append(AddTD(cb));
//
//                    if (i == 3)
//                        Pub1.append(AddTREnd());
//                }
//
//                switch (i)
//                {
//                    case 1:
//                        Pub1.append(AddTD());
//                        Pub1.append(AddTD());
//                        Pub1.append(AddTREnd());
//                        break;
//                    case 2:
//                        Pub1.append(AddTD());
//                        Pub1.append(AddTREnd());
//                        break;
//                    default:
//                        break;
//                }
//
//                this.Pub1.append(AddTableEnd());
//                Pub1.append(AddTDEnd());
//                Pub1.append(AddTREnd());
//
//            case PushWay.SpecEmps.getValue():
//
//                //#region 按照指定的人员
//
//                Pub1.append(AddTR());
//                //Pub1.AddTDBegin("colspan='2'");
//
//                Pub1.append(AddTD("选择人员："));
//                Pub1.append(AddTDBegin());
//
//                tb = new TextBox();
//                tb.setId("TB_Users");
//                tb.setTextMode(TextBoxMode.MultiLine);
//                tb.addAttr("width", "99%");
//                tb.setRows(4);
//                tb.setReadOnly(true);
//
//                TextBox hf = new TextBox();
//                hf.setTextMode(TextBoxMode.Hidden);
//                hf.setId("HID_Users");
//
//                //加载已经选择的人员
//                if (!StringHelper.isNullOrEmpty(msg.getPushDoc()))
//                {
//                    hf.setText(msg.getPushDoc().replace("@@", ","));
//
//                    Emps emps = new Emps();
//                    emps.RetrieveAll();
//
////                    tb.setText(
////                        hf.Value.Split(',').Select(o => (emps.GetEntityByKey(o) as Emp).Name).Aggregate(
////                            string.Empty, (curr, next) => curr + next + ",").TrimEnd(','));
//                }
//
//                Pub1.append(tb);
//                Pub1.append(hf);
//                Pub1.append(AddBR());
//                Pub1.append(AddBR());
//
//                Pub1.append(
//                    "<a class='easyui-linkbutton' data-options=\"iconCls:'icon-user'\" href='javascript:void(0)' onclick=\"showWin('../Comm/Port/SelectUser_Jq.aspx','" +
//                    tb.getId()+ "','" + hf.getId() + "');\">选择人员...</a>");
//                Pub1.append(AddTDEnd());
//                //Pub1.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'");
//                //depts = new Depts();
//                //depts.RetrieveAll();
//                //var emps = new Emps();
//                //emps.RetrieveAll();
//                //var empDepts = new EmpDepts();
//                //empDepts.RetrieveAll();
//                //var nemps = new NodeEmps(int.Parse(this.NodeID));
//
//                //Emp emp = null;
//
//                //foreach (Dept dept in depts)
//                //{
//                //    this.Pub1.AddTR();
//                //    var mycb = new CheckBox();
//                //    mycb.Text = dept.Name;
//                //    mycb.ID = "CB_D_" + dept.No;
//                //    this.Pub1.AddTD("colspan='3' class='GroupTitle'", mycb);
//                //    this.Pub1.AddTREnd();
//
//                //    i = 0;
//                //    string ctlIDs = "";
//
//                //    foreach (EmpDept ed in empDepts)
//                //    {
//                //        if (ed.FK_Dept != dept.No)
//                //            continue;
//
//                //        //排除非当前结点绑定的人员
//                //        if (nemps.GetEntityByKey(NodeEmpAttr.FK_Emp, ed.FK_Emp) == null)
//                //            continue;
//
//                //        i++;
//
//                //        if (i == 4)
//                //            i = 1;
//
//                //        if (i == 1)
//                //            Pub1.AddTR();
//
//                //        emp = emps.GetEntityByKey(ed.FK_Emp) as Emp;
//
//                //        cb = new CheckBox();
//                //        cb.ID = "CB_E_" + emp.No;
//                //        ctlIDs += cb.ID + ",";
//                //        cb.Text = emp.Name;
//                //        if (msg.PushDoc.Contains("@" + emp.No + "@"))
//                //            cb.Checked = true;
//
//                //        Pub1.AddTD(cb);
//
//                //        if (i == 3)
//                //            Pub1.AddTREnd();
//                //    }
//
//                //    mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";
//
//                //    switch (i)
//                //    {
//                //        case 1:
//                //            Pub1.AddTD();
//                //            Pub1.AddTD();
//                //            Pub1.AddTREnd();
//                //            break;
//                //        case 2:
//                //            Pub1.AddTD();
//                //            Pub1.AddTREnd();
//                //            break;
//                //        default:
//                //            break;
//                //    }
//                //}
//
//                //Pub1.AddTableEnd();
//
//                //Pub1.AddTDEnd();
//                Pub1.append(AddTREnd());
//
////                break;
//            case PushWay.SpecSQL.getValue():
//
//                //#region 按照指定的SQL查询语句
//
//                Pub1.append(AddTR());
//
//                this.Pub1.append(AddTDBegin("colspan='2'"));
//                this.Pub1.append("SQL查询语句：<br />");
//                tb = new TextBox();
//                tb.setId("TB_" + PushMsgAttr.PushDoc);
//                tb.setCols(50);
//                tb.addAttr("width", "99%");
//                tb.setTextMode(TextBoxMode.MultiLine);
//                tb.setRows(4);
//                tb.setText(msg.getPushDoc());
//                this.Pub1.append(tb);
//                this.Pub1.append(AddTDEnd());
//                Pub1.append(AddTREnd());
//
//                break;
//            case PushWay.SpecStations.getValue():
//
//                //#region 按照指定的岗位
//
//                Pub1.append(AddTR());
//                Pub1.append(AddTDBegin("colspan='2'"));
//
//                if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
//                {
//                    SysEnums ses = new SysEnums("StaGrade");
//                    Stations sts = new Stations();
//                    sts.RetrieveAll();
//
//                    String sql = "SELECT No,Name FROM Port_Station WHERE StaGrade  NOT IN (SELECT IntKey FROM Sys_Enum WHERE EnumKey='StaGrade')";
//                    DataTable dt = DBAccess.RunSQLReturnTable(sql);
//                    if (dt.Rows.size() != 0)
//                    {
//                        if (ses.size() == 0)
//                        {
//                            SysEnum se = new SysEnum();
//                            se.setEnumKey("StaGrade");
//                            se.setLab("普通岗");
//                            se.setIntKey(0);
//                            se.Insert();
//
//                            ses.AddEntity(se);
//                        }
//
//                        for (Station st: sts.ToJavaList())
//                        {
//                            st.setStaGrade(0);
//                            st.Save();
//                        }
//                    }
//
//                    this.Pub1.append(AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));
//
//                    for (SysEnum se: ses.ToJavaList())
//                    {
//                        this.Pub1.append(AddTR());
//                        CheckBox mycb = new CheckBox();
//                        mycb.setText(se.getLab());
//                        mycb.setId("CB_SG_" + se.getIntKey());
//                        this.Pub1.append(AddTD("colspan='3' class='GroupTitle'", mycb));
//                        this.Pub1.append(AddTREnd());
//
//                        i = 0;
//                        String ctlIDs = "";
//
//                        for (Station st: sts.ToJavaList())
//                        {
//                            if (st.getStaGrade()!= se.getIntKey())
//                                continue;
//
//                            i++;
//
//                            if (i == 4)
//                                i = 1;
//
//                            if (i == 1)
//                                Pub1.append(AddTR());
//
//                            cb = new CheckBox();
//                            cb.setId("CB_S_" + st.getNo());
//                            ctlIDs += cb.getId() + ",";
//                            cb.setText(st.getName());
//
//                            if (msg.getPushDoc().contains("@" + st.getNo() + "@"))
//                                cb.setChecked(true);
//
//                            Pub1.append(AddTD(cb));
//
//                            if (i == 3)
//                                Pub1.append(AddTREnd());
//                        }
//
//                        mycb.addAttr("onclick", "onSelected");
////                        mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";
//
//                        switch (i)
//                        {
//                            case 1:
//                                Pub1.append(AddTD());
//                                Pub1.append(AddTD());
//                                Pub1.append(AddTREnd());
//                                break;
//                            case 2:
//                                Pub1.append(AddTD());
//                                Pub1.append(AddTREnd());
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//
//                    this.Pub1.append(AddTableEnd());
//                }
//                else
//                {
//                    /*BPM 模式*/
//                    BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
//                    tps.RetrieveAll();
//
//                    BP.GPM.Stations sts = new BP.GPM.Stations();
//                    sts.RetrieveAll();
//
//                    String sql = "SELECT No,Name FROM Port_Station WHERE FK_StationType NOT IN (SELECT No FROM Port_StationType)";
//                    DataTable dt = DBAccess.RunSQLReturnTable(sql);
//                    if (dt.Rows.size() != 0)
//                    {
//                        if (tps.size() == 0)
//                        {
//                        	BP.GPM.StationType stp = new BP.GPM.StationType();// { No = "01", Name = "普通岗" };
//                        	stp.setNo("01");
//                        	stp.setName("普通岗");
//                        	stp.Save();
//
//                            tps.AddEntity(stp);
//                        }
//
//                        //更新所有对不上岗位类型的岗位，岗位类型为01或第一个
//                        for (BP.GPM.Station st: sts.ToJavaList())
//                        {
//                            st.setFK_StationType(tps.convertStationTypes(tps).get(0).getNo());
//                            st.Update();
//                        }
//                    }
//
//                    this.Pub1.append(AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));
//
//                    for (BP.GPM.StationType tp: tps.ToJavaList())
//                    {
//                        this.Pub1.append(AddTR());
//                        CheckBox mycb = new CheckBox();
//                        mycb.setText(tp.getName());
//                        mycb.setId("CB_ST_" + tp.getNo());
//                        this.Pub1.append(AddTD("colspan='3' class='GroupTitle'", mycb));
//                        this.Pub1.append(AddTREnd());
//
//                        i = 0;
//                        String ctlIDs = "";
//
//                        for (BP.GPM.Station st: sts.ToJavaList())
//                        {
//                            if (st.getFK_StationType() != tp.getNo())
//                                continue;
//
//                            i++;
//
//                            if (i == 4)
//                                i = 1;
//
//                            if (i == 1)
//                            {
//                                Pub1.append(AddTR());
//                            }
//
//                            cb = new CheckBox();
//                            cb.setId("CB_S_" + st.getNo());
//                            ctlIDs += cb.getId() + ",";
//                            cb.setText(st.getName());
//
//                            if (msg.getPushDoc().contains("@" + st.getNo() + "@"))
//                                cb.setChecked(true);
//
//                            this.Pub1.append(AddTD(cb));
//
//                            if (i == 3)
//                                Pub1.append(AddTREnd());
//                        }
//
//                        mycb.addAttr("onclick", "onSelected1");//.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";
//
//                        switch (i)
//                        {
//                            case 1:
//                                Pub1.append(AddTD());
//                                Pub1.append(AddTD());
//                                Pub1.append(AddTREnd());
//                                break;
//                            case 2:
//                                Pub1.append(AddTD());
//                                Pub1.append(AddTREnd());
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//
//                    this.Pub1.append(AddTableEnd());
//                }
//
//                //#region 原逻辑，只考虑了一种模式，停用
//                //Pub1.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'");
//                //SysEnums ses = new SysEnums("StaGrade");
//                //Stations sts = new Stations();
//                //NodeStations nsts = new NodeStations(int.Parse(this.NodeID));
//                //sts.RetrieveAll();
//
//                //foreach (SysEnum se in ses)
//                //{
//                //    this.Pub1.AddTR();
//                //    var mycb = new CheckBox();
//                //    mycb.Text = se.Lab;
//                //    mycb.ID = "CB_SG_" + se.IntKey;
//                //    this.Pub1.AddTD("colspan=3 class='GroupTitle'", mycb);
//                //    this.Pub1.AddTREnd();
//
//                //    i = 0;
//                //    string ctlIDs = "";
//
//                //    foreach (Station st in sts)
//                //    {
//                //        if (st.StaGrade != se.IntKey)
//                //            continue;
//
//                //        //排除非当前结点的岗位
//                //        if (nsts.GetEntityByKey(NodeStationAttr.FK_Station, st.No) == null)
//                //            continue;
//
//                //        i++;
//
//                //        if (i == 4)
//                //            i = 1;
//
//                //        if (i == 1)
//                //            Pub1.AddTR();
//
//                //        cb = new CheckBox();
//                //        cb.ID = "CB_S_" + st.No;
//                //        ctlIDs += cb.ID + ",";
//                //        cb.Text = st.Name;
//                //        if (msg.PushDoc.Contains("@" + st.No + "@"))
//                //            cb.Checked = true;
//
//                //        Pub1.AddTD(cb);
//
//                //        if (i == 3)
//                //            Pub1.AddTREnd();
//                //    }
//
//                //    mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";
//
//                //    switch (i)
//                //    {
//                //        case 1:
//                //            Pub1.AddTD();
//                //            Pub1.AddTD();
//                //            Pub1.AddTREnd();
//                //            break;
//                //        case 2:
//                //            Pub1.AddTD();
//                //            Pub1.AddTREnd();
//                //            break;
//                //        default:
//                //            break;
//                //    }
//                //}
//                //Pub1.AddTableEnd();
//
//                Pub1.append(AddTDEnd());
//                Pub1.append(AddTREnd());
//
//                break;
//        }

        Pub1.append(AddTableEnd());

        Pub1.append(AddBR());
        Pub1.append(AddSpace(1));

        LinkButton btn = new LinkButton(false, NamesOfBtn.Save.toString(), "保存");
        btn.addAttr("onclick", "onSave()");
        //        btn.Click += new EventHandler(btn_Click);
        Pub1.append(btn);

        if (!StringHelper.isNullOrEmpty(msg.getMyPK()))
        {
            Pub1.append(AddSpace(1));
            btn = new LinkButton(false, NamesOfBtn.Delete.toString(), "删除");
            btn.addAttr("onclick", "onDelete()");//.Click += new EventHandler(btn_Delete_Click);
            btn.addAttr("onclick", btn.attributes.get("onclick")+"return confirm('你确定要删除此消息推送设置吗？');");
            Pub1.append(btn);
        }
    }

//    void btn_Delete_Click(object sender, EventArgs e)
//    {
//        var pm = new PushMsg();
//        pm.Retrieve(PushMsgAttr.FK_Event, this.Event, PushMsgAttr.FK_Node, this.NodeID);
//        pm.Delete();
//
//        Response.Redirect(string.Format("ActionPush2Spec.aspx?NodeID={0}&MyPK={1}&Event={2}&FK_Flow={3}&tk={4}", NodeID, MyPK, Event,
//                                        FK_Flow, new Random().NextDouble()), true);
//    }
//
//    void btn_Click(object sender, EventArgs e)
//    {
//        var pm = new PushMsg();
//        pm.Retrieve(PushMsgAttr.FK_Event, this.Event, PushMsgAttr.FK_Node, this.NodeID);
//
//        if (!StringHelper.isNullOrWhiteSpace(pm.MyPK))
//            pm.Delete();
//
//        pm.FK_Event = this.Event;
//        pm.FK_Node = int.Parse(this.NodeID);
//
//        var ddl = Pub1.GetDDLByID("DDL_" + PushMsgAttr.PushWay);
//        pm.PushWay = ddl.SelectedItemIntVal;
//        pm.PushDoc = string.Empty;
//
//        switch ((PushWay)pm.PushWay)
//        {
//            case PushWay.ByParas:
//
//                #region 按照系统指定参数
//
//                var rb = Pub1.GetRadioBtnByID("RB_0");
//
//                if (rb.Checked)
//                {
//                    pm.PushDoc = "0";
//                    pm.Tag = Pub1.GetTBByID("TB_" + PushMsgAttr.Tag).Text;
//                }
//                else
//                {
//                    rb = Pub1.GetRadioBtnByID("RB_1");
//
//                    if (rb.Checked)
//                        pm.PushDoc = "1";
//
//                    pm.Tag = Pub1.GetDDLByID("DDL_" + PushMsgAttr.Tag).SelectedItemStringVal;
//                }
//
//                #endregion
//
//                break;
//            case PushWay.NodeWorker:
//
//                #region 按照指定结点的工作人员
//
//                CheckBox cb = null;
//
//                foreach (var ctrl in Pub1.Controls)
//                {
//                    cb = ctrl as CheckBox;
//                    if (cb == null || !cb.ID.StartsWith("CB_") || !cb.Checked) continue;
//
//                    pm.PushDoc += "@" + cb.ID.substing(3) + "@";
//                }
//
//                #endregion
//
//                break;
//            case PushWay.SpecDepts:
//
//                #region 按照指定的部门
//
//                foreach (var ctrl in Pub1.Controls)
//                {
//                    cb = ctrl as CheckBox;
//                    if (cb == null || !cb.ID.StartsWith("CB_") || !cb.Checked) continue;
//
//                    pm.PushDoc += "@" + cb.ID.substing(3) + "@";
//                }
//
//                #endregion
//
//                break;
//            case PushWay.SpecEmps:
//
//                #region 按照指定的人员
//
//                var hid = Pub1.FindControl("HID_Users") as HiddenField;
//
//                if(!StringHelper.isNullOrWhiteSpace(hid.Value))
//                {
//                    pm.PushDoc = hid.Value.Split(',').Select(o => "@" + o + "@").Aggregate(string.Empty,
//                                                                                           (curr, next) =>
//                                                                                           curr + next);
//                }
//                //foreach (var ctrl in Pub1.Controls)
//                //{
//                //    cb = ctrl as CheckBox;
//                //    if (cb == null || !cb.ID.StartsWith("CB_E_") || !cb.Checked) continue;
//
//                //    pm.PushDoc += "@" + cb.ID.substing(5) + "@";
//                //}
//
//                #endregion
//
//                break;
//            case PushWay.SpecSQL:
//
//                #region 按照指定的SQL查询语句
//
//                pm.PushDoc = Pub1.GetTBByID("TB_" + PushMsgAttr.PushDoc).Text;
//
//                #endregion
//
//                break;
//            case PushWay.SpecStations:
//
//                #region 按照指定的岗位
//
//                foreach (var ctrl in Pub1.Controls)
//                {
//                    cb = ctrl as CheckBox;
//                    if (cb == null || !cb.ID.StartsWith("CB_S_") || !cb.Checked) continue;
//
//                    pm.PushDoc += "@" + cb.ID.substing(5) + "@";
//                }
//
//                #endregion
//
//                break;
//        }
//
//        pm.Save();
//        Response.Redirect(string.Format("ActionPush2Spec.aspx?NodeID={0}&MyPK={1}&Event={2}&FK_Flow={3}&tk={4}", NodeID, MyPK, Event,
//                                        FK_Flow, new Random().NextDouble()), true);
//    }
//
//    void ddl_SelectedIndexChanged(object sender, EventArgs e)
//    {
//        Response.Redirect(string.Format("ActionPush2Spec.aspx?NodeID={0}&MyPK={1}&Event={2}&FK_Flow={3}&ThePushWay={4}&tk={5}", NodeID, MyPK, Event,
//                                        FK_Flow, (sender as DDL).SelectedItemIntVal, new Random().NextDouble()), true);
//    }
}



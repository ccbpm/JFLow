package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Difference.SystemConfig;
import BP.En.Attr;
import BP.Port.Emp;
import BP.Sys.GEEntity;
import BP.WF.*;
import BP.WF.Template.FlowExt;
import BP.Web.WebUser;

public class WF_Admin_TestingContainer extends WebContralBase {
    /// <summary>
    /// 构造函数
    /// </summary>
    public WF_Admin_TestingContainer()
    {
    }

    /// <summary>
    /// 左侧的树刷新
    /// </summary>
    /// <returns></returns>
    public String Left_Init()
    {
        return "";
    }
    /// <summary>
    /// 测试页面初始化
    /// </summary>
    /// <returns></returns>
    public String Default_Init() throws Exception
    {
        String userNo = this.GetRequestVal("UserNo");
        if (WebUser.getNo().equals(userNo) == false)
            BP.WF.Dev2Interface.Port_Login(userNo);

        long workid = BP.WF.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), userNo);
        return String.valueOf(workid);
    }


    /// <summary>
    /// 数据库信息
    /// </summary>
    /// <returns></returns>
    public String DBInfo_Init()throws Exception
    {
        //数据容器，用于盛放数据，并返回json.
        DataSet ds = new DataSet();

        //流程引擎控制表.
        GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
        ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

        //流程引擎人员列表.
        GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
        gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.RDT);
        ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));


        //获得Track数据.
        String table = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
        String sql = "SELECT * FROM " + table + " WHERE WorkID=" + this.getWorkID();
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        dt.TableName = "Track";
        //把列大写转化为小写.
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            Track tk = new Track();
            for (Attr attr : tk.getEnMap().getAttrs())
            {
                if (dt.Columns.contains(attr.getKey().toUpperCase()) == true)
                {
                    dt.Columns.get(attr.getKey().toUpperCase()).ColumnName = attr.getKey();

                }
            }
        }
        ds.Tables.add(dt);

        //获得NDRpt表
        String rpt = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
        GEEntity en = new GEEntity(rpt);
        en.setPKVal(this.getWorkID());
        en.RetrieveFromDBSources();
        ds.Tables.add(en.ToDataTableField("NDRpt"));

        //转化为json ,返回出去.
        return BP.Tools.Json.ToJson(ds);
    }
    /// <summary>
    /// 让adminer登录.
    /// </summary>
    /// <returns></returns>
    public String Default_LetAdminerLogin() throws Exception
    {
        String adminer = this.GetRequestVal("Adminer");
        String SID = this.GetRequestVal("SID");
        BP.WF.Dev2Interface.Port_LoginBySID(adminer,SID);

        return "登录成功.";
        //Int64 workid = BP.WF.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), userNo);
        //return workid.toString();
    }
    /// <summary>
    /// 切换用户
    /// </summary>
    /// <returns></returns>
    public String SelectOneUser_ChangUser()
    {

        String adminer = this.GetRequestVal("Adminer");
        String SID = this.GetRequestVal("SID");

        try
        {
            BP.WF.Dev2Interface.Port_Login(this.getFK_Emp());
            return "登录成功.";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }

    /// <summary>
    /// 发起.
    /// </summary>
    /// <returns></returns>
    public String TestFlow2020_StartIt() throws Exception
    {
        String sid = this.GetRequestVal("SID");
        if (WebUser.getIsAdmin() == false)
            return "err@非管理员无法测试,关闭后重新登录。";

        //用户编号.
        String userNo = this.GetRequestVal("UserNo");

        //判断是否可以测试该流程？
        BP.Port.Emp myEmp = new BP.Port.Emp();
        int i = myEmp.Retrieve("SID", sid);
        if (i == 0)
            throw new Exception("err@非法的SID，SID不正确.");
        //组织url发起该流程.
        String url = "Default.html?RunModel=1&FK_Flow=" + this.getFK_Flow() + "&SID=" + sid + "&UserNo=" + userNo;
        return url;
    }
    /// <summary>
    /// 初始化
    /// </summary>
    /// <returns></returns>
    public String TestFlow2020_Init() throws Exception
    {
        //清除缓存.
        SystemConfig.DoClearCash();

        if (BP.Web.WebUser.getIsAdmin() == false)
            return "err@您不是管理员，无法执行该操作.";


        FlowExt fl = new FlowExt(this.getFK_Flow());

        if (1 == 2 && BP.Web.WebUser.getNo() != "admin" && fl.getTester().length() <= 1)
        {
            String msg = "err@二级管理员[" + BP.Web.WebUser.getName() + "]您好,您尚未为该流程配置测试人员.";
            msg += "您需要在流程属性里的底部[设置流程发起测试人]的属性里，设置可以发起的测试人员,多个人员用逗号分开.";
            return msg;
        }

        int nodeid = Integer.parseInt(this.getFK_Flow() + "01");
        DataTable dt = null;
        String sql = "";
        BP.WF.Node nd = new BP.WF.Node(nodeid);
        if (nd.getIsGuestNode())
        {
            /*如果是 guest 节点，就让其跳转到 guest登录界面，让其发起流程。*/
            //这个地址需要配置.
            return "url@/SDKFlowDemo/GuestApp/Login.htm?FK_Flow=" + this.getFK_Flow();
        }
           // #endregion 测试人员.


           // #region 从配置里获取-测试人员.
        /* 检查是否设置了测试人员，如果设置了就按照测试人员身份进入
         * 设置测试人员的目的是太多了人员影响测试效率.
         * */
        if (fl.getTester().length() > 2)
        {
            // 构造人员表.
            DataTable dtEmps = new DataTable();
            dtEmps.Columns.Add("No");
            dtEmps.Columns.Add("Name");
            dtEmps.Columns.Add("FK_DeptText");

            String[] strs = fl.getTester().split(",");
            for (String str : strs)
            {
                if (DataType.IsNullOrEmpty(str) == true)
                    continue;

                Emp emp = new Emp();
                emp.SetValByKey("No", str);
                int i = emp.RetrieveFromDBSources();
                if (i == 0)
                    continue;

                DataRow dr = dtEmps.NewRow();
                dr.setValue("No",emp.getNo());
                dr.setValue("Name",emp.getName());
                dr.setValue("FK_DeptText",emp.getFK_DeptText());
                dtEmps.Rows.add(dr);
            }
            return BP.Tools.Json.ToJson(dtEmps);
        }
            //endregion 测试人员.

        //fl.DoCheck();

            //region 从设置里获取-测试人员.
        try
        {
            switch (nd.getHisDeliveryWay())
            {
                case ByStation:
                case ByStationOnly:
                    if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
                        sql = "SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.No) join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();
                    else
                        sql = "SELECT Port_Emp.No FROM Port_Emp WHERE OrgNo='" + BP.Web.WebUser.getOrgNo() + "' LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.No) join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();

                    // emps.RetrieveInSQL_Order("select fk_emp from Port_Empstation WHERE fk_station in (select fk_station from WF_NodeStation WHERE FK_Node=" + nodeid + " )", "FK_Dept");
                    break;
                case ByDept:
                    sql = "SELECT No,Name FROM Port_Emp A, WF_NodeDept B WHERE A.FK_Dept=B.FK_Dept AND B.FK_Node=" + nodeid;
                    break;
                case ByBindEmp:
                    sql = "SELECT No,Name from Port_Emp WHERE No in (select FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') ";
                    //emps.RetrieveInSQL("select fk_emp from wf_NodeEmp WHERE fk_node=" + int.Parse(this.FK_Flow + "01") + " ");
                    break;
                case ByDeptAndStation:

                    sql = "SELECT pdes.FK_Emp AS No"
                            + " FROM   Port_DeptEmpStation pdes"
                            + "        INNER JOIN WF_NodeDept wnd"
                            + "             ON  wnd.FK_Dept = pdes.FK_Dept"
                            + "             AND wnd.FK_Node = " + nodeid
                            + "        INNER JOIN WF_NodeStation wns"
                            + "             ON  wns.FK_Station = pdes.FK_Station"
                            + "             AND wnd.FK_Node =" + nodeid
                            + " ORDER BY"
                            + "        pdes.FK_Emp";

                    break;
                case BySelected: //所有的人员多可以启动, 2016年11月开始约定此规则.

                    if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
                        sql = "SELECT c.No, c.Name, B.Name as DeptName FROM Port_DeptEmp A, Port_Dept B, Port_Emp C WHERE A.FK_Dept=B.No AND A.FK_Emp=C.No ";
                    else
                        sql = "SELECT c.No, c.Name, B.Name as DeptName FROM Port_DeptEmp A, Port_Dept B, Port_Emp C WHERE A.FK_Dept=B.No AND B.OrgNo='" + BP.Web.WebUser.getOrgNo() + "' AND A.FK_Emp=C.No ";

                    dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

                    if (dt.Rows.size() > 300 && 1==2)
                    {
                        if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.MSSQL)
                            sql = "SELECT top 300 No as FK_Emp FROM Port_Emp ";

                        if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.Oracle)
                            sql = "SELECT  No as FK_Emp FROM Port_Emp WHERE ROWNUM <300 ";

                        if (SystemConfig.getAppCenterDBType() == BP.DA.DBType.MySQL)
                            sql = "SELECT  No as FK_Emp FROM Port_Emp   limit 0,300 ";
                    }
                    break;
                case BySQL:
                    if (DataType.IsNullOrEmpty(nd.getDeliveryParas()))
                        return "err@您设置的按SQL访问开始节点，但是您没有设置sql.";
                    break;
                default:
                    break;
            }

            dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
            if (dt.Rows.size() == 0)
                return "err@您按照:" + nd.getHisDeliveryWay() + "的方式设置的开始节点的访问规则，但是开始节点没有人员。";

            if (dt.Rows.size() > 2000)
                return "err@可以发起开始节点的人员太多，会导致系统崩溃变慢，您需要在流程属性里设置可以发起的测试用户.";

            // 构造人员表.
            DataTable dtMyEmps = new DataTable();
            dtMyEmps.Columns.Add("No");
            dtMyEmps.Columns.Add("Name");
            dtMyEmps.Columns.Add("FK_DeptText");

            //处理发起人数据.
            String emps = "";
            for (DataRow dr : dt.Rows)
            {
                String myemp = dr.getValue(0).toString();
                if (emps.contains("," + myemp + ",") == true)
                    continue;

                emps += "," + myemp + ",";
                BP.Port.Emp emp = new Emp(myemp);

                DataRow drNew = dtMyEmps.NewRow();
                drNew.setValue("No",emp.getNo());
                drNew.setValue("Name",emp.getName());
                drNew.setValue("FK_DeptText",emp.getFK_DeptText());

                dtMyEmps.Rows.add(drNew);
            }

            //检查物理表,避免错误.
            Nodes nds = new Nodes(this.getFK_Flow());
            for (Node mynd : nds.ToJavaList())
            {
                mynd.getHisWork().CheckPhysicsTable();
            }

            //返回数据源.
            return BP.Tools.Json.ToJson(dtMyEmps);
                //endregion 从设置里获取-测试人员.

        }
        catch (Exception ex)
        {
            return "err@<h2>您没有正确的设置开始节点的访问规则，这样导致没有可启动的人员，<a href='http://bbs.ccflow.org/showtopic-4103.aspx' target=_blank ><font color=red>点击这查看解决办法</font>.</a>。</h2> 系统错误提示:" + ex.getMessage() + "<br><h3>也有可能你你切换了OSModel导致的，什么是OSModel,请查看在线帮助文档 <a href='http://ccbpm.mydoc.io' target=_blank>http://ccbpm.mydoc.io</a>  .</h3>";
        }
    }

}

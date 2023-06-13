package bp.cloud;

import bp.da.*;
import bp.web.*;
import bp.wf.*;
import bp.difference.*;
import bp.wf.port.admin2group.*;
import bp.wf.admin.*;

/**
 * 云的公共类
 */
public class Dev2Interface {
    /**
     * 创建组织
     *
     * @param orgNo
     * @param orgName
     * @return
     */
    public static String Port_CreateOrg(String orgNo, String orgName) throws Exception {

        try {

            ///#region 1.检查完整性.
            bp.cloud.port.Org org = new bp.cloud.port.Org();
            org.setNo(orgNo);
            if (org.RetrieveFromDBSources() == 1) {
                return "err@组织编号已经存在";
            }

            bp.cloud.port.Dept dept = new bp.cloud.port.Dept();
            dept.setNo(orgNo);
            if (dept.RetrieveFromDBSources() == 1) {
                return "err@部门编号已经存在";
            }

            bp.cloud.port.Emp emp = new bp.cloud.port.Emp();
            emp.setNo(orgNo + "_" + orgNo);
            if (emp.RetrieveFromDBSources() == 1) {
                return "err@人员编号已经存在";
            }

            ///#endregion 检查完整性.


            ///#region 3.创建部门.
            dept.setParentNo("100");
            dept.setName(orgName);
            dept.setOrgNo(orgNo);
            dept.Insert();

            dept.setNo(DBAccess.GenerGUID());
            dept.setParentNo(orgNo);
            dept.setName("部门1");
            dept.setOrgNo(orgNo);
            dept.Insert();

            dept.setNo(DBAccess.GenerGUID());
            dept.setParentNo(orgNo);
            dept.setName("部门2");
            dept.setOrgNo(orgNo);
            dept.Insert();

            ///#endregion 创建部门.


            ///#region 2.创建组织+ 人员
            //创建组织.
            org.setName(orgName);
            org.setAdminer(orgNo); //创始人.
            org.setAdminerName(orgName);
            org.Insert();

            emp.setName(orgName);
            emp.setFK_Dept(orgNo);
            emp.setOrgNo(orgNo);
            emp.setUserID(orgNo);
            emp.Insert();

            ///#endregion 创建组织.


            ///#region 4. 设置管理员.
            OrgAdminer oa = new OrgAdminer();
            oa.setMyPK(orgNo + "_" + orgNo);
            oa.setFK_Emp(orgNo);
            oa.setOrgNo(orgNo);
            oa.setEmpName(orgName);
            oa.Insert();

            ///#endregion 设置管理员.


            ///#region 5. 增加流程树.
            FlowSort fs = new FlowSort();
            fs.setOrgNo(orgNo);
            fs.setNo(orgNo);
            fs.setParentNo("100");
            fs.setName("流程树" + orgNo);
            fs.Insert();

            fs = new FlowSort();
            fs.setOrgNo(orgNo);
            fs.setNo(DBAccess.GenerGUID());
            fs.setParentNo(orgNo);
            fs.setName("类型1");
            fs.Insert();

            fs = new FlowSort();
            fs.setOrgNo(orgNo);
            fs.setNo(DBAccess.GenerGUID());
            fs.setParentNo(orgNo);
            fs.setName("类型2");
            fs.Insert();


            ///#endregion   增加流程树.


            ///#region 6. 增加表单树.
            FrmSort f1s = new FrmSort();
            f1s.setOrgNo(orgNo);
            f1s.setNo(orgNo);
            f1s.setParentNo("100");
            f1s.setName(orgName);
            f1s.Insert();

            f1s = new FrmSort();
            f1s.setOrgNo(orgNo);
            f1s.setNo(DBAccess.GenerGUID());
            f1s.setParentNo(orgNo);
            f1s.setName("类型1");
            f1s.Insert();

            f1s = new FrmSort();
            f1s.setOrgNo(orgNo);
            f1s.setNo(DBAccess.GenerGUID());
            f1s.setParentNo(orgNo);
            f1s.setName("类型2");
            f1s.Insert();

            ///#endregion   增加流程树.

            return "info@创建成功.";
        } catch (RuntimeException ex) {
            String sql = "DELETE FROM Port_Org WHERE No='" + orgNo + "';";
            sql += "DELETE FROM Port_OrgAdminer WHERE OrgNo='" + orgNo + "'; ";
            sql += "DELETE FROM Port_Dept WHERE OrgNo='" + orgNo + "'; ";
            sql += "DELETE FROM Port_DeptEmp WHERE OrgNo='" + orgNo + "'; ";
            sql += "DELETE FROM Port_Emp WHERE OrgNo='" + orgNo + "'; ";
            sql += "DELETE FROM WF_FlowSort WHERE OrgNo='" + orgNo + "'; ";
            sql += "DELETE FROM Sys_FormTree WHERE OrgNo='" + orgNo + "'; ";
            DBAccess.RunSQLs(sql);
            return "info@创建失败:" + ex.getMessage();
        }
    }

    /**
     * 多组织登录接口.
     *
     * @param userID
     * @param orgNo
     */
    public static String Port_Login(String userID, String orgNo) throws Exception {
        bp.cloud.port.Emp emp = new bp.cloud.port.Emp();
        int i = emp.Retrieve("UserID", userID, "OrgNo", orgNo);
        if (i == 0) {
            throw new RuntimeException("err@用户名[" + userID + "],OrgNo[" + orgNo + "]不存在.");
        }

        //调用登录.
        String str = Port_Login(emp);

        return bp.wf.Dev2Interface.Port_GenerToken("PC");
    }

    /**
     * 登录
     */
    public static String Port_Login(bp.cloud.port.Emp emp) {
        // cookie操作，为适应不同平台，统一使用HttpContextHelper
        java.util.HashMap<String, String> cookieValues = new java.util.HashMap<String, String>();

        ContextHolderUtils.addCookie("No", emp.getUserID());
        ContextHolderUtils.addCookie("Name", emp.getName());

        ContextHolderUtils.addCookie("FK_Dept", emp.getFK_Dept());
        ContextHolderUtils.addCookie("FK_DeptName", emp.getFK_DeptText());

        ContextHolderUtils.addCookie("OrgNo", emp.getOrgNo());
        ContextHolderUtils.addCookie("OrgName", emp.getOrgName());

        String token = "";

        ContextHolderUtils.addCookie("Tel", emp.getTel());
        ContextHolderUtils.addCookie("Lang", "CH");
        ContextHolderUtils.addCookie("Token", WebUser.getToken());

        ContextHolderUtils.getSession().setAttribute("No", emp.getUserID());
        ContextHolderUtils.getSession().setAttribute("Name", emp.getName());
        ContextHolderUtils.getSession().setAttribute("FK_Dept", emp.getFK_Dept());
        ContextHolderUtils.getSession().setAttribute("FK_DeptText", emp.getFK_DeptText());
        ContextHolderUtils.getSession().setAttribute("OrgNo", emp.getOrgNo());
        ContextHolderUtils.getSession().setAttribute("OrgName", emp.getOrgName());

//        BP.Difference.HttpContextHelper.ResponseCookieAdd(cookieValues, null, "CCS");
//
//        //给 session 赋值.
//        BP.Difference.HttpContextHelper.Current.Session["No"] = emp.getUserID();
//        BP.Difference.HttpContextHelper.Current.Session["Name"] = emp.getName();
//        BP.Difference.HttpContextHelper.Current.Session["FK_Dept"] = emp.getFK_Dept();
//        BP.Difference.HttpContextHelper.Current.Session["FK_DeptText"] = emp.getFK_DeptText();
//        BP.Difference.HttpContextHelper.Current.Session["OrgNo"] = emp.getOrgNo();
//        BP.Difference.HttpContextHelper.Current.Session["OrgName"] = emp.getOrgName();

        return token;
    }

    
//ORIGINAL LINE: public static DataTable DB_StarFlows(string userNo, string domain = null)
    public static DataTable DB_StarFlows(String userNo, String domain) {
        String sql = "SELECT A.ICON, A.No,A.Name,a.IsBatchStart,";
        sql += " a.FK_FlowSort,B.Name AS FK_FlowSortText,B.Domain,A.IsStartInMobile, A.Idx,";
        sql += " a.WorkModel,"; // 0=内部流程1=外部流程2=实体台账3=表单.
        sql += " a.PTable as FrmID "; // 表单ID,为实体台账的时候存储的表单ID.
        sql += " FROM WF_Flow A, WF_FlowSort B  ";
        sql += " WHERE   A.IsCanStart=1 AND A.FK_FlowSort=B.No  AND A.OrgNo='" + WebUser.getOrgNo() + "' ";
        sql += " ORDER BY A.Idx ";
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        return dt;
    }

    /**
     * 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
     *
     * @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
     */

//ORIGINAL LINE: public static DataTable DB_GenerRuning(string userNo = null, bool isContainFuture = false, string domain = null)
    public static DataTable DB_GenerRuning(String userNo, boolean isContainFuture, String domain) {
        if (userNo == null) {
            userNo = WebUser.getNo();
        }

        DataTable dt = DB_GenerRuning(userNo, null, false, null, isContainFuture);

        //暂时屏蔽type的拼接，拼接后转json会报错 于庆海修改
//            dt.Columns.Add("Type");
//            foreach (DataRow row in dt.Rows)
//            {
//                row["Type", "RUNNING";
//            }

//        dt.DefaultView.Sort = "RDT DESC";
//        return dt.DefaultView.ToTable();
        return dt;
    }

    /**
     * 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
     * 该接口为在途菜单提供数据,在在途工作中，可以执行撤销发送。
     *
     * @param userNo      操作员
     * @param fk_flow     流程编号
     * @param isMyStarter 是否仅仅查询我发起的在途流程
     * @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
     */

//ORIGINAL LINE: public static DataTable DB_GenerRuning(string userNo, string fk_flow, bool isMyStarter = false, string domain = null, bool isContainFuture = false)
    public static DataTable DB_GenerRuning(String userNo, String fk_flow, boolean isMyStarter, String domain, boolean isContainFuture) {
        String dbStr = SystemConfig.getAppCenterDBVarStr();
        Paras ps = new Paras();

        String domainSQL = "";
        if (domain == null) {
            domainSQL = " AND Domain='" + domain + "' ";
        }
        //获取用户当前所在的节点
        String currNode = "";
        switch (DBAccess.getAppCenterDBType()) {
            case Oracle:
                currNode = "(SELECT FK_Node FROM (SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND  OrgNo='" + WebUser.getOrgNo() + "'  Order by RDT DESC ) WHERE RowNum=1)";
                break;
            case MySQL:
            case PostgreSQL:
            case KingBaseR3:
            case KingBaseR6:
            case UX:
                currNode = "(SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND  OrgNo='" + WebUser.getOrgNo() + "' Order by RDT DESC LIMIT 1)";
                break;
            case MSSQL:
                currNode = "(SELECT TOP 1 FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND  OrgNo='" + WebUser.getOrgNo() + "' Order by RDT DESC)";
                break;
            default:
                break;
        }


        //授权模式.
        String sql = "";
        String futureSQL = "";
        if (isContainFuture == true) {
            switch (DBAccess.getAppCenterDBType()) {
                case MySQL:
                    futureSQL = " UNION SELECT A.WorkID,A.StarterName,A.Title,A.DeptName,D.Name AS NodeName,A.RDT,B.FK_Node,A.FK_Flow,A.FID,A.FlowName,C.EmpName AS TodoEmps," + currNode + " AS CurrNode ,1 AS RunType FROM WF_GenerWorkFlow A, WF_SelectAccper B," + "(SELECT GROUP_CONCAT(B.EmpName SEPARATOR ';') AS EmpName, B.WorkID,B.FK_Node FROM WF_GenerWorkFlow A, WF_SelectAccper B WHERE A.WorkID = B.WorkID  group By B.FK_Node) C,WF_Node D" + " WHERE A.WorkID = B.WorkID AND B.WorkID = C.WorkID AND B.FK_Node = C.FK_Node AND A.FK_Node = D.NodeID AND B.FK_Emp = '" + WebUser.getNo() + "'" + " AND B.FK_Node Not in(Select DISTINCT FK_Node From WF_GenerWorkerlist G where G.WorkID = B.WorkID)AND A.WFState != 3";
                    break;
                case MSSQL:
                    futureSQL = " UNION SELECT A.WorkID,A.StarterName,A.Title,A.DeptName,D.Name AS NodeName,A.RDT,B.FK_Node,A.FK_Flow,A.FID,A.FlowName,C.EmpName AS TodoEmps ," + currNode + " AS CurrNode ,1 AS RunType FROM WF_GenerWorkFlow A, WF_SelectAccper B," + "(SELECT EmpName=STUFF((Select ';'+FK_Emp+','+EmpName From WF_SelectAccper t Where t.FK_Node=B.FK_Node FOR xml path('')) , 1 , 1 , '') , B.WorkID,B.FK_Node FROM WF_GenerWorkFlow A, WF_SelectAccper B WHERE A.WorkID = B.WorkID  group By B.FK_Node,B.WorkID) C,WF_Node D" + " WHERE A.WorkID = B.WorkID AND B.WorkID = C.WorkID AND B.FK_Node = C.FK_Node AND A.FK_Node = D.NodeID AND B.FK_Emp = '" + WebUser.getNo() + "'" + " AND B.FK_Node Not in(Select DISTINCT FK_Node From WF_GenerWorkerlist G where G.WorkID = B.WorkID)AND A.WFState != 3";
                    break;
                default:
                    break;

            }
        }


        //非授权模式，

        if (DataType.IsNullOrEmpty(fk_flow)) {
            if (isMyStarter == true) {
                sql = "SELECT DISTINCT a.WorkID,a.StarterName,a.Title,a.DeptName,a.NodeName,a.RDT,a.FK_Node,a.FK_Flow,a.FID ,a.FlowName,a.TodoEmps," + currNode + " AS CurrNode,0 AS RunType FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.TodoEmps  not like '%" + WebUser.getNo() + ",%' AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < -1) AND  A.Starter=" + dbStr + "Starter  AND  OrgNo='" + WebUser.getOrgNo() + "'";
                if (isContainFuture == true) {
                    sql += futureSQL;
                }
                ps.SQL = sql;
                ps.Add("FK_Emp", userNo);
                ps.Add("Starter", userNo);
            } else {
                sql = "SELECT DISTINCT a.WorkID,a.StarterName,a.Title,a.DeptName,a.NodeName,a.RDT,a.FK_Node,a.FK_Flow,a.FID ,a.FlowName,a.TodoEmps ," + currNode + " AS CurrNode,0 AS RunType FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.TodoEmps  not like '%" + WebUser.getNo() + ",%' AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < -1)  AND  OrgNo='" + WebUser.getOrgNo() + "'";
                if (isContainFuture == true) {
                    sql += futureSQL;
                }
                ps.SQL = sql;
                ps.Add("FK_Emp", userNo);
            }
        } else {
            if (isMyStarter == true) {
                sql = "SELECT DISTINCT a.WorkID,a.StarterName,a.Title,a.DeptName,a.NodeName,a.RDT,a.FK_Node,a.FK_Flow,a.FID ,a.FlowName,a.TodoEmps ," + currNode + " AS CurrNode,0 AS RunType FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.TodoEmps  not like '%" + WebUser.getNo() + ",%' AND A.FK_Flow=" + dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < -1 ) AND  A.Starter=" + dbStr + "Starter  AND  OrgNo='" + WebUser.getOrgNo() + "'";
                if (isContainFuture == true) {
                    sql += futureSQL;
                }
                ps.SQL = sql;
                ps.Add("FK_Flow", fk_flow);
                ps.Add("FK_Emp", userNo);
                ps.Add("Starter", userNo);
            } else {
                sql = "SELECT DISTINCT a.WorkID,a.StarterName,a.Title,a.DeptName,a.NodeName,a.RDT,a.FK_Node,a.FK_Flow,a.FID ,a.FlowName,a.TodoEmps ," + currNode + " AS CurrNode,0 AS RunType  FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.TodoEmps  not like '%" + WebUser.getNo() + ",%' AND A.FK_Flow=" + dbStr + "FK_Flow  AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND (B.IsPass=1 or B.IsPass < -1 )  AND  OrgNo='" + WebUser.getOrgNo() + "'";
                if (isContainFuture == true) {
                    sql += futureSQL;
                }
                ps.SQL = sql;
                ps.Add("FK_Flow", fk_flow);
                ps.Add("FK_Emp", userNo);
            }
        }

        //获得sql.
        DataTable dt = DBAccess.RunSQLReturnTable(ps);
        if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase) {
            dt.Columns.get("WORKID").ColumnName = "WorkID";
            dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
            dt.Columns.get("TITLE").ColumnName = "Title";
            dt.Columns.get("NODENAME").ColumnName = "NodeName";
            dt.Columns.get("RDT").ColumnName = "RDT";
            dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
            dt.Columns.get("FID").ColumnName = "FID";
            dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
            dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
            dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
            dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
            dt.Columns.get("CURRNODE").ColumnName = "CurrNode";
            dt.Columns.get("RUNTYPE").ColumnName = "RunType";
        }

        if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase) {
            dt.Columns.get("workid").ColumnName = "WorkID";
            dt.Columns.get("startername").ColumnName = "StarterName";
            dt.Columns.get("title").ColumnName = "Title";
            dt.Columns.get("nodename").ColumnName = "NodeName";
            dt.Columns.get("rdt").ColumnName = "RDT";
            dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
            dt.Columns.get("fid").ColumnName = "FID";
            dt.Columns.get("fk_node").ColumnName = "FK_Node";
            dt.Columns.get("flowname").ColumnName = "FlowName";
            dt.Columns.get("deptname").ColumnName = "DeptName";
            dt.Columns.get("todoemps").ColumnName = "TodoEmps";
            dt.Columns.get("currnode").ColumnName = "CurrNode";
            dt.Columns.get("runtype").ColumnName = "RunType";
        }

        return dt;
    }

    public static DataTable DB_GenerRuningNotMyStart(String userNo) {
        String dbStr = SystemConfig.getAppCenterDBVarStr();
        Paras ps = new Paras();

        //获取用户当前所在的节点
        String currNode = "";
        switch (DBAccess.getAppCenterDBType()) {
            case Oracle:
                currNode = "(SELECT FK_Node FROM (SELECT G.FK_Node FROM WF_GenerWorkFlow A,WF_GenerWorkerlist G WHERE G.WorkID = A.WorkID AND G.FK_Emp='" + WebUser.getNo() + "' Order by G.RDT DESC ) WHERE RowNum=1)";
                break;
            case MySQL:
            case PostgreSQL:
            case KingBaseR3:
            case KingBaseR6:
            case UX:
                currNode = "(SELECT  FK_Node FROM WF_GenerWorkerlist G WHERE   G.WorkID = A.WorkID AND FK_Emp='" + WebUser.getNo() + "' Order by RDT DESC LIMIT 1)";
                break;
            case MSSQL:
                currNode = "(SELECT TOP 1 FK_Node FROM WF_GenerWorkerlist G WHERE  G.WorkID = A.WorkID AND FK_Emp='" + WebUser.getNo() + "' Order by RDT DESC)";
                break;
            default:
                break;
        }

        String sql = "SELECT DISTINCT a.WorkID,a.StarterName,a.Title,a.DeptName,a.NodeName,a.RDT," + "a.FK_Node,a.FK_Flow,a.FID ,a.FlowName,a.TodoEmps ," + currNode + " AS CurrNode,0 AS RunType FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.TodoEmps  not like '%" + WebUser.getNo() + ",%' AND A.WorkID=B.WorkID AND B.FK_Emp=" + dbStr + "FK_Emp AND B.IsEnable=1 AND  (B.IsPass=1 or B.IsPass < -1)  AND  OrgNo='" + WebUser.getOrgNo() + "' AND A.Starter!=" + dbStr + "Starter";

        ps.SQL = sql;
        ps.Add("FK_Emp", userNo);
        ps.Add("Starter", userNo);

        //获得sql.
        DataTable dt = DBAccess.RunSQLReturnTable(ps);
        if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None) {
            dt.Columns.get(0).ColumnName = "WorkID";
            dt.Columns.get(1).ColumnName = "StarterName";
            dt.Columns.get(2).ColumnName = "Title";
            dt.Columns.get(3).ColumnName = "DeptName";
            dt.Columns.get(4).ColumnName = "NodeName";
            dt.Columns.get(5).ColumnName = "RDT";
            dt.Columns.get(6).ColumnName = "FK_Node";
            dt.Columns.get(7).ColumnName = "FK_Flow";
            dt.Columns.get(8).ColumnName = "FID";
            dt.Columns.get(9).ColumnName = "FlowName";
            dt.Columns.get(10).ColumnName = "TodoEmps";
            dt.Columns.get(11).ColumnName = "CurrNode";
            dt.Columns.get(12).ColumnName = "RunType";
        }
        return dt;
    }

    /**
     * 获取某一个人已完成的流程
     *
     * @param userNo 用户编码
     * @return
     */
    public static DataTable DB_FlowCompleteNotMyStart(String userNo) {
        Paras ps = new Paras();
        String dbstr = SystemConfig.getAppCenterDBVarStr();
        ps.SQL = "SELECT 'RUNNING' AS Type, T.* FROM WF_GenerWorkFlow T WHERE T.Starter!=" + dbstr + "Starter AND (T.Emps LIKE '%@" + userNo + "@%' OR  T.Emps LIKE '%@" + userNo + ",%') AND T.FID=0 AND T.WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
        ps.Add("Starter", userNo);
        DataTable dt = DBAccess.RunSQLReturnTable(ps);

        //需要翻译.
        if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase) {
            dt.Columns.get("TYPE").ColumnName = "Type";
            dt.Columns.get("WORKID").ColumnName = "WorkID";
            dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
            dt.Columns.get("SYSTYPE").ColumnName = "SysType";
            dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
            dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
            dt.Columns.get("TITLE").ColumnName = "Title";
            dt.Columns.get("WFSTA").ColumnName = "WFSta";
            dt.Columns.get("WFSTATE").ColumnName = "WFState";
            dt.Columns.get("STARTER").ColumnName = "Starter";
            dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
            dt.Columns.get("SENDER").ColumnName = "Sender";
            dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
            dt.Columns.get("NODENAME").ColumnName = "NodeName";
            dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
            dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
            dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
            dt.Columns.get("SDTOFFLOW").ColumnName = "SDTOfFlow";
            dt.Columns.get("PFLOWNO").ColumnName = "PflowNo";
            dt.Columns.get("PWORKID").ColumnName = "PWorkID";
            dt.Columns.get("PNODEID").ColumnName = "PNodeID";
            dt.Columns.get("PEMP").ColumnName = "PEmp";
            dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
            dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
            dt.Columns.get("BILLNO").ColumnName = "BillNo";
            dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
            dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
            dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
            dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
            dt.Columns.get("ATPARA").ColumnName = "AtPara";
            dt.Columns.get("EMPS").ColumnName = "Emps";
            dt.Columns.get("DOMAIN").ColumnName = "Domain";
            dt.Columns.get("SENDDT").ColumnName = "SendDT";
            dt.Columns.get("WEEKNUM").ColumnName = "WeekNum";
        }
        if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase) {
            dt.Columns.get("type").ColumnName = "Type";
            dt.Columns.get("workid").ColumnName = "WorkID";
            dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
            dt.Columns.get("systype").ColumnName = "SysType";
            dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
            dt.Columns.get("flowname").ColumnName = "FlowName";
            dt.Columns.get("title").ColumnName = "Title";
            dt.Columns.get("wfsta").ColumnName = "WFSta";
            dt.Columns.get("wfstate").ColumnName = "WFState";
            dt.Columns.get("starter").ColumnName = "Starter";
            dt.Columns.get("startername").ColumnName = "StarterName";
            dt.Columns.get("sender").ColumnName = "Sender";
            dt.Columns.get("fk_node").ColumnName = "FK_Node";
            dt.Columns.get("nodename").ColumnName = "NodeName";
            dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
            dt.Columns.get("deptname").ColumnName = "DeptName";
            dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
            dt.Columns.get("sdtofflow").ColumnName = "SDTOfFlow";
            dt.Columns.get("pflowno").ColumnName = "PflowNo";
            dt.Columns.get("pworkid").ColumnName = "PWorkID";
            dt.Columns.get("pnodeid").ColumnName = "PNodeID";
            dt.Columns.get("pemp").ColumnName = "PEmp";
            dt.Columns.get("guestno").ColumnName = "GuestNo";
            dt.Columns.get("guestname").ColumnName = "GuestName";
            dt.Columns.get("billno").ColumnName = "BillNo";
            dt.Columns.get("flownote").ColumnName = "FlowNote";
            dt.Columns.get("todoemps").ColumnName = "TodoEmps";
            dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
            dt.Columns.get("tasksta").ColumnName = "TaskSta";
            dt.Columns.get("atpara").ColumnName = "AtPara";
            dt.Columns.get("emps").ColumnName = "Emps";
            dt.Columns.get("domain").ColumnName = "Domain";
            dt.Columns.get("senddt").ColumnName = "SendDT";
            dt.Columns.get("weeknum").ColumnName = "WeekNum";
        }
        return dt;
    }

    /**
     * 待办工作数量
     */
    public static int getTodolistEmpWorks() throws Exception {
        Paras ps = new Paras();
        String dbstr = SystemConfig.getAppCenterDBVarStr();

        //不是授权状态
        if (bp.wf.Glo.isEnableTaskPool() == true) {
            ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE FK_Emp=" + dbstr + "FK_Emp AND OrgNo=" + dbstr + "OrgNo AND TaskSta!=1 ";
        } else {
            ps.SQL = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp=" + dbstr + "FK_Emp AND OrgNo=" + dbstr + "OrgNo";
        }

        ps.Add("FK_Emp", bp.web.WebUser.getNo());
        ps.Add("OrgNo", bp.web.WebUser.getOrgNo());

        //  Log.DebugWriteInfo(ps.SQL);
        return DBAccess.RunSQLReturnValInt(ps);
    }

    /**
     * 抄送数量
     */
    public static int getTodolist_CCWorks() {
        Paras ps = new Paras();

        ps.SQL = "SELECT count(MyPK) as Num FROM WF_CCList WHERE CCTo=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Emp AND OrgNo=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OrgNo";
        ps.Add("FK_Emp", bp.web.WebUser.getNo());
        ps.Add("OrgNo", bp.web.WebUser.getOrgNo());
        return DBAccess.RunSQLReturnValInt(ps, 0);
    }


    /**
     * 退回给当前用户的数量
     */
    public static int getTodolist_ReturnNum() {
        String sql = "SELECT  COUNT(WorkID) AS Num from WF_GenerWorkFlow where WFState=5 and (TodoEmps like '%" + WebUser.getNo() + ",%' OR TodoEmps like '%" + WebUser.getNo() + ";%') AND OrgNo='" + WebUser.getOrgNo() + "' AND WorkID in (SELECT distinct WorkID FROM WF_ReturnWork WHERE ReturnToEmp='" + bp.web.WebUser.getNo() + "')";

        return DBAccess.RunSQLReturnValInt(sql);
    }

    /**
     * 待办逾期的数量
     */
    public static int getTodolist_OverWorkNum() throws Exception {
        String sql = "";
        String whereSQL = "AND convert(varchar(100),A.SDT,120)<CONVERT(varchar(100), GETDATE(), 120) AND A.WFState=2 AND A.FK_Node NOT like '%01' AND ListType=0";


        if (bp.wf.Glo.isEnableTaskPool() == true) {
            sql = "SELECT  Count(*) FROM WF_EmpWorks A WHERE  TaskSta=0 AND A.FK_Emp='" + WebUser.getNo() + "' " + whereSQL;
        } else {
            sql = "SELECT Count(*) FROM WF_EmpWorks A WHERE  A.FK_Emp='" + WebUser.getNo() + "' " + whereSQL;
        }

        //获得授权信息.
        Auths aths = new Auths();
        aths.Retrieve(AuthAttr.AutherToEmpNo, WebUser.getNo());
        for (Auth ath : aths.ToJavaList()) {

            String todata = ath.getTakeBackDT().replace("-", "");
            if (!DataType.IsNullOrEmpty(ath.getTakeBackDT())) {
                int mydt = Integer.parseInt(todata);
                int nodt = Integer.parseInt(DataType.getCurrentDateByFormart("yyyyMMdd"));
                if (mydt < nodt) {
                    continue;
                }
                sql += " UNION ";

                if (ath.getAuthType() == AuthorWay.SpecFlows) {
                    sql += "SELECT Count(*) FROM WF_EmpWorks A WHERE  FK_Emp='" + ath.getAuther() + "' AND FK_Flow='" + ath.getFlowNo() + "' " + whereSQL;
                } else {
                    sql += "SELECT Count(*) FROM WF_EmpWorks A WHERE  FK_Emp='" + ath.getAuther() + "' " + whereSQL;
                }


            }
        }

        //string sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE OrgNo='" + WebUser.getOrgNo() + "' AND FK_Emp='" + WebUser.getNo() + "' AND A.FK_Node=B.FK_Node  AND  convert(varchar(100),A.SDT,120)>CONVERT(varchar(100), GETDATE(), 120)";

        return DBAccess.RunSQLReturnValInt(sql);
    }
}
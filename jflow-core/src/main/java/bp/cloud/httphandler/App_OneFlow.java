package bp.cloud.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.en.FieldTypeS;
import bp.en.QueryObject;
import bp.sys.MapAttr;
import bp.sys.MapAttrAttr;
import bp.sys.MapAttrs;
import bp.web.WebUser;
import bp.wf.Dev2Interface;
import bp.wf.WFState;
import bp.wf.data.MyJoinFlows;
import bp.wf.data.MyStartFlowAttr;
import bp.wf.data.MyStartFlows;

/**
 页面功能实体

 */
public class App_OneFlow extends WebContralBase
{
    /**
     构造函数

     */
    public App_OneFlow()
    {

    }
    /**
     获得菜单

     @return
     */
    public final String Default_Menu() throws Exception {
        if (WebUser.getNo() == null)
        {
            return "err@/App/index.htm";
        }
        //类别.
        DataTable dtSort = new DataTable("Sorts");
        dtSort.Columns.Add("No");
        dtSort.Columns.Add("Name");
        dtSort.Columns.Add("ICON");

        //菜单.
        DataTable dtMenu = new DataTable("Menus");
        dtMenu.Columns.Add("No");
        dtMenu.Columns.Add("Name");
        dtMenu.Columns.Add("SortNo");
        dtMenu.Columns.Add("ICON");
        dtMenu.Columns.Add("Url");
     ///#region 类别.
        DataRow dr = dtSort.NewRow();
        dr.setValue("No","01");
        dr.setValue("Name","本流程操作");
        dr.setValue("ICON","FlowCenter.png");
        dtSort.Rows.add(dr);

        dr = dtSort.NewRow();
        dr.setValue("No","02");
        dr.setValue("Name","本流程业务数据");
        dr.setValue("ICON","FlowSearch.png");
        dtSort.Rows.add(dr);
        dr = dtMenu.NewRow();
        dr.setValue("No","Todolist");
        dr.setValue("Name","待办");
        dr.setValue("SortNo","01");
        dr.setValue("Url","/App/OneFlow/Todolist.htm");
        dr.setValue("ICON","Todolist.png");
        dtMenu.Rows.add(dr);

        dr = dtMenu.NewRow();
        dr.setValue("No","Runing");
        dr.setValue("Name","未完成");
        dr.setValue("SortNo","01");
        dr.setValue("Url","/App/OneFlow/Runing.htm");
        dr.setValue("ICON","Runing.png");
        dtMenu.Rows.add(dr);

        dr = dtMenu.NewRow();
        dr.setValue("No","CC");
        dr.setValue("Name","抄送");
        dr.setValue("SortNo","01");
        dr.setValue("Url","/App/OneFlow/CC.htm");
        dr.setValue("ICON","CC.png");
        dtMenu.Rows.add(dr);

        dr = dtMenu.NewRow();
        dr.setValue("No","Draf");
        dr.setValue("Name","草稿");
        dr.setValue("SortNo","01");
        dr.setValue("Url","/App/OneFlow/Draf.htm");
        dr.setValue("ICON","Draft.png");
        dtMenu.Rows.add(dr);
        ///#endregion 流程中心-菜单.
        ///#region 流程查询-菜单.
        dr = dtMenu.NewRow();
        dr.setValue("No","MyStartFlows");
        dr.setValue("Name","我发起的");
        dr.setValue("SortNo","02");
        dr.setValue("Url","/App/OneFlow/RptSearch.htm?SearchType=My");
        dr.setValue("ICON","SearchMy.png");
        dtMenu.Rows.add(dr);



        dr = dtMenu.NewRow();
        dr.setValue("No","MyJoinFlows");
        dr.setValue("Name","我审批的");
        dr.setValue("SortNo","02");
        dr.setValue("Url","/App/OneFlow/RptSearch.htm?SearchType=MyJoin");
        dr.setValue("ICON","SearchMyCheck.png");
        dtMenu.Rows.add(dr);

        dr = dtMenu.NewRow();
        dr.setValue("No","Org");
        dr.setValue("Name","部门数据概况");
        dr.setValue("SortNo","02");
        dr.setValue("Url","/App/OneFlow/DataPanelDept.htm");
        dr.setValue("ICON","Organization.png");
        dtMenu.Rows.add(dr);

        dr = dtMenu.NewRow();
        dr.setValue("No","Org");
        dr.setValue("Name","组织数据概况");
        dr.setValue("SortNo","02");
        dr.setValue("Url","/App/OneFlow/DataPanelOrg.htm");
        dr.setValue("ICON","Organization.png");
        dtMenu.Rows.add(dr);



        if (WebUser.getIsAdmin() == true)
        {
            dr = dtMenu.NewRow();
            dr.setValue("No","MyDeptFlows");
            dr.setValue("Name","我部门发起的");
            dr.setValue("SortNo","02");
            dr.setValue("Url","/App/OneFlow/RptSearch.htm?SearchType=MyDept");
            dr.setValue("ICON","SearchMyDept.png");
            dtMenu.Rows.add(dr);

            dr = dtMenu.NewRow();
            dr.setValue("No","FX");
            dr.setValue("Name","综合分析");
            dr.setValue("SortNo","02");
            dr.setValue("Url","/WF/Comm/Group.htm?EnsName=BP.Cloud.GWFAdmins");
            dr.setValue("ICON","ZongHeFenXi.png");
            dtMenu.Rows.add(dr);

            dr = dtMenu.NewRow();
            dr.setValue("No","FlowDatas");
            dr.setValue("Name","数据运维");
            dr.setValue("SortNo","02");
            dr.setValue("Url","/WF/Comm/Search.htm?EnsName=BP.Cloud.GWFAdmins");
            dr.setValue("ICON","YunWei.png");
            dtMenu.Rows.add(dr);
        }
        ///#endregion 流程查询-菜单.
        ///#endregion 系统管理-菜单.

        //组装数据.
        DataSet ds = new DataSet();
        ds.Tables.add(dtSort);
        ds.Tables.add(dtMenu);

        //返回数据.
        return bp.tools.Json.ToJson(ds);
    }
    /**
     初始化Home

     @return
     */
    public final String Default_Init() throws Exception {
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("UserNo", WebUser.getNo());
        ht.put("UserName", WebUser.getName());

        //系统名称.
        ht.put("SysName", SystemConfig.getSysName());
        ht.put("CustomerName", SystemConfig.getCustomerName());

        ht.put("Todolist_EmpWorks", bp.wf.Dev2Interface.getTodolistEmpWorks());
        ht.put("Todolist_Runing", bp.wf.Dev2Interface.getTodolistRuning());
        ht.put("Todolist_Sharing", bp.wf.Dev2Interface.getTodolistSharing());
        ht.put("Todolist_CCWorks", bp.wf.Dev2Interface.getTodolistCCWorks());
        ht.put("Todolist_Apply", bp.wf.Dev2Interface.getTodolistApply()); //申请下来的任务个数.
        ht.put("Todolist_Draft", bp.wf.Dev2Interface.getTodolistDraft()); //草稿数量.
        ht.put("Todolist_Complete", bp.wf.Dev2Interface.getTodolistComplete()); //完成数量.
        ht.put("UserDeptName", WebUser.getFK_DeptName());

        //我发起
        MyStartFlows myStartFlows = new MyStartFlows();
        QueryObject obj = new QueryObject(myStartFlows);
        obj.AddWhere(MyStartFlowAttr.Starter, WebUser.getNo());
        obj.addAnd();
        //运行中\已完成\挂起\退回\转发\加签\批处理\
        obj.addLeftBracket();
        obj.AddWhere("WFState=2 or WFState=3 or WFState=4 or WFState=5 or WFState=6 or WFState=8 or WFState=10");
        obj.addRightBracket();
        obj.DoQuery();
        ht.put("Todolist_MyStartFlow", myStartFlows.size());

        //我参与
        MyJoinFlows myFlows = new MyJoinFlows();
        obj = new QueryObject(myFlows);
        obj.AddWhere("Emps like '%" + WebUser.getNo() + "%'");
        obj.DoQuery();
        ht.put("Todolist_MyFlow", myFlows.size());

        return bp.tools.Json.ToJsonEntityModel(ht);
    }

    /**
     初始化待办.

     @return
     */
    public final String Todolist_Init() throws Exception {
        String fk_node = this.GetRequestVal("FK_Node");
        String showWhat = this.GetRequestVal("ShowWhat");
        DataTable dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(),this.getFK_Node(),showWhat,this.getDomain());
        return bp.tools.Json.ToJson(dt);
    }

    /**
     在途

     @return 运行中的流程
     */
    public final String Runing_Init() throws Exception {
        DataTable dt = null;
        boolean isContainFuture = this.GetRequestValBoolen("IsContainFuture");
        dt = bp.wf.Dev2Interface.DB_GenerRuning(WebUser.getNo(),this.getFK_Flow(),false,this.getDomain(), isContainFuture); //获得指定域的在途.
        return bp.tools.Json.ToJson(dt);
    }

    /**
     抄送

     @return
     */
    public final String CC_Init() throws Exception {
        String sta = this.GetRequestVal("Sta");
        if (DataType.IsNullOrEmpty(sta))
        {
            sta = "-1";
        }


        DataTable dt = null;
        if (sta.equals("-1"))
        {
            dt = Dev2Interface.DB_CCList("",this.getFK_Flow());
        }

        if (sta.equals("0"))
        {
            dt = bp.wf.Dev2Interface.DB_CCList_UnRead(WebUser.getNo(),null,this.getFK_Flow());
        }

        if (sta.equals("1"))
        {
            dt = bp.wf.Dev2Interface.DB_CCList_Read(null,this.getFK_Flow());
        }

        if (sta.equals("2"))
        {
            dt = bp.wf.Dev2Interface.DB_CCList_Delete(null,this.getFK_Flow());
        }

        return bp.tools.Json.ToJson(dt);
    }

    public final String Draf_Init()
    {
        DataTable dt = bp.wf.Dev2Interface.DB_GenerDraftDataTable(this.getFK_Flow());
        return bp.tools.Json.ToJson(dt);
    }

    /**
     获取本部门的数据统计

     @return
     */
    public final String DataPanelDept_GetGenerWorksByDept()
    {
        java.util.Hashtable ht = new java.util.Hashtable();
        String dbStr = SystemConfig.getAppCenterDBVarStr();
        Paras ps = new Paras();
        ps.SQL = "SELECT COUNT(*) AS count,WFState FROM WF_GenerWorkFlow Where WFState >1 AND FID=0 AND FK_Flow=" + dbStr + "FK_Flow AND FK_Dept=" + dbStr + "FK_Dept GROUP BY WFState";
        ps.Add("FK_Flow", this.getFK_Flow());
        ps.Add("FK_Dept", WebUser.getFK_Dept());
        DataTable dt = DBAccess.RunSQLReturnTable(ps);
        int allCount = 0;
        int count = 0;
        int wfstate = 0;
        for(DataRow dr : dt.Rows)
        {
            count = Integer.parseInt(dr.getValue(0).toString());
            wfstate = Integer.parseInt(dr.getValue(1).toString());
            allCount += count;
            if(wfstate == WFState.Complete.getValue())
            {
                ht.put("Dept_GWF_CompleteCounts", count);
            }
            if (wfstate == WFState.ReturnSta.getValue())
            {
                ht.put("Dept_GWF_ReturnCounts", count);
            }
            if (wfstate == WFState.Delete.getValue())
            {
                ht.put("Dept_GWF_DeleteCounts", count);
            }

        }
        //本部门的查询显示数量
        ps.SQL = "Select Count(DISTINCT WorkID) From(" + "SELECT DISTINCT G.WorkID From WF_GenerWorkFlow G,WF_GenerWorkerlist L Where  G.WorkID=L.WorkID AND G.WFState=2 AND G.FK_Flow=" + dbStr + "FK_Flow AND L.IsPass=0 AND L.FK_Dept=" + dbStr + "FK_Dept" +" UNION SELECT DISTINCT WorkID FROM WF_GenerWorkFlow Where WFState > 1 AND WFState!=3 AND FK_Flow =" + dbStr + "FK_Flow AND FK_Dept=" + dbStr + "FK_Dept) AS A";

        ps.Add("FK_Flow", this.getFK_Flow());
        ps.Add("FK_Dept", WebUser.getFK_Dept());
        int runingCount = DBAccess.RunSQLReturnValInt(ps);
        ht.put("Dept_GWF_RuningCounts", runingCount);
        ht.put("Dept_GWF_Counts", allCount);
        switch(SystemConfig.getAppCenterDBType()){
            case MySQL:
            case UX:
            case PostgreSQL:
                ht.put("Dept_GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE  IsPass=0 AND FK_Flow='"+this.getFK_Flow()+"' AND FK_Dept='" +WebUser.getFK_Dept() + "' AND  STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') <  NOW()"));
                break;
            case Oracle:
            case KingBaseR3:
            case KingBaseR6:
                ht.put("Dept_GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE  IsPass=0 AND FK_Flow='"+this.getFK_Flow()+"' AND FK_Dept='" +WebUser.getFK_Dept() + "' AND  to_char(to_date(SDT,'yyyy-mm-dd,hh24:mi'),'yyyymmdd HH:mm')  < to_char(sysdate ,'yyyymmdd HH:mm')"));
                break;
            case MSSQL:
                ht.put("Dept_GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE  IsPass=0 AND FK_Flow='"+this.getFK_Flow()+"' AND FK_Dept='" +WebUser.getFK_Dept() + "' AND  convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120)"));
                break;
            default:
                throw new RuntimeException(SystemConfig.getAppCenterDBType()+"还没增加该数据库类型的解析");
        }

        //个人的查询显示数量
        ps.clear();
        ps.SQL = "SELECT COUNT(DISTINCT WorkID) AS count,WFState FROM WF_GenerWorkFlow Where WFState >1 AND FK_Flow=" + dbStr + "FK_Flow AND (Emps like '%@"+WebUser.getNo()+",%'OR TodoEmps like '%"+WebUser.getNo()+",%')  GROUP BY WFState ";
        ps.Add("FK_Flow", this.getFK_Flow());
        //ps.Add("FK_Dept", WebUser.getFK_Dept());
        dt = DBAccess.RunSQLReturnTable(ps);
        allCount = 0;
        for (DataRow dr : dt.Rows)
        {
            count = Integer.parseInt(dr.getValue(0).toString());
            wfstate = Integer.parseInt(dr.getValue(1).toString());
            allCount += count;
            if (wfstate ==WFState.Runing.getValue())
            {
                ht.put("My_GWF_RuningCounts", count);
            }
            if (wfstate ==WFState.Complete.getValue())
            {
                ht.put("My_GWF_CompleteCounts", count);
            }
            if (wfstate == WFState.ReturnSta.getValue())
            {
                ht.put("My_GWF_ReturnCounts", count);
            }
            if (wfstate ==WFState.Delete.getValue())
            {
                ht.put("My_GWF_DeleteCounts", count);
            }

        }
        ht.put("My_GWF_Counts", allCount);
        switch(SystemConfig.getAppCenterDBType()){
            case MySQL:
            case UX:
            case PostgreSQL:
                ht.put("My_GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE  IsPass=0 AND FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "' AND  STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') <  NOW()"));
                break;
            case Oracle:
            case KingBaseR3:
            case KingBaseR6:
                ht.put("My_GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE  IsPass=0 AND FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow()+ "' AND  convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120)"));
                break;
            case MSSQL:
                ht.put("My_GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_GenerWorkerList WHERE  IsPass=0 AND FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "' AND  convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120)"));
                break;
            default:
                throw new RuntimeException(SystemConfig.getAppCenterDBType()+"还没增加该数据库类型的解析");
        }

        return bp.tools.Json.ToJson(ht);
    }

    /**
     获取Rpt表的分析项

     @return
     */
    public final String DataPanelDept_GetAnalyseGroupByRpt() throws Exception {
        //获取的是系统字段WFState和表单中Int类型的字段
        MapAttrs mapattrs = new MapAttrs();
        QueryObject qo = new QueryObject(mapattrs);
        qo.AddWhere(MapAttrAttr.FK_MapData, "ND" + Integer.parseInt(this.getFK_Flow()) + "01");
        qo.addAnd();
        qo.AddWhereNotIn(MapAttrAttr.MyDataType, "1,4,6,7");
        qo.addAnd();
        qo.AddWhere(MapAttrAttr.UIVisible, true);
        qo.addAnd();
        qo.AddWhere(MapAttrAttr.LGType, "!=",FieldTypeS.Enum.getValue());

        qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
        qo.DoQuery();
        //增加系统表的状态
        MapAttr attr = new MapAttr("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt_WFState");
        mapattrs.AddEntity(attr);
        return bp.tools.Json.ToJson(mapattrs.ToDataTableField());
    }

    public final String DataPanelDept_GetAnalyseBySpecifyField_DataSet() throws Exception {
        DataSet ds = new DataSet();
        String field = this.GetRequestVal("KeyOfEn");
        String groupBy = this.GetRequestVal("FK_NY");
        if (DataType.IsNullOrEmpty(field) || DataType.IsNullOrEmpty(groupBy))
        {
            throw new RuntimeException("分析条件，分析项不能为空");
        }

        DataTable dt;
        //流程状态，只分析发起数量和完成数量
        if (field.equals("WFState") == true)
        {
            String sql = "SELECT SUBSTRING(FK_NY, 6, 2) AS FK_NY, WFState, count(OID) as Num FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt WHERE FK_Dept = 'ccs1'  AND FK_NY> DateName(year, GetDate()) + '-00' GROUP BY FK_NY ,WFState";
            dt = DBAccess.RunSQLReturnTable(sql);
        }
        else
        {
            //按照申请人的区分
            String sql = "SELECT SUBSTRING(FK_NY, 6, 2) AS FK_NY, FlowStarter, sum(" + field+") as Num FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt WHERE FK_Dept = 'ccs1'  AND FK_NY> DateName(year, GetDate()) + '-00' GROUP BY FK_NY ,FlowStarter";
            dt = DBAccess.RunSQLReturnTable(sql);

        }
        return bp.tools.Json.ToJson(ds);
    }
    /**
     获取单个流程的本部门的数据分析

     @return
     */
    public final String DataPanelDept_GetAnalyseByFlowNoDept_DataSet() throws Exception {
        DataSet ds = new DataSet();
        //按照月份（统计的内容有发起数，完成数量，业务字段）
        //发起数量
        String dbSQL="";
        String sql = "SELECT SUBSTRING(FK_NY, 6, 2) AS FK_NY, count(WorkID) AS Num FROM WF_GenerWorkFlow WHERE WFState >1 AND FK_Dept='" + WebUser.getFK_Dept()+"' AND FK_Flow='"+this.getFK_Flow()+ "'";
        switch(SystemConfig.getAppCenterDBType()){
            case MySQL:
            case PostgreSQL:
            case UX:
                dbSQL=" AND FK_NY>YEAR(CURDATE())+'-00' ";
                break;
            case Oracle:
            case KingBaseR3:
            case KingBaseR6:
                dbSQL=" AND FK_NY>to_char(sysdate, 'yyyy' )+'-00' ";
                break;
            case MSSQL:
                dbSQL=" AND FK_NY>DateName(year,GetDate())+'-00' ";
                break;
            default:
                throw new RuntimeException(SystemConfig.getAppCenterDBType()+"还没增加该数据库类型的解析");
        }
         sql+=dbSQL+ " GROUP BY FK_NY ";
        DataTable FlowStartByNY = DBAccess.RunSQLReturnTable(sql);
        FlowStartByNY.TableName = "FlowStartByNY";
        ds.Tables.add(FlowStartByNY);
        //完成数量
        sql = "SELECT SUBSTRING(FK_NY, 6, 2) AS FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND FK_Dept='" + WebUser.getFK_Dept() + "' AND FK_Flow='" + this.getFK_Flow() + "'"+dbSQL+" GROUP BY FK_NY ";

        DataTable FlowCompleteByNY = DBAccess.RunSQLReturnTable(sql);
        FlowCompleteByNY.TableName = "FlowCompleteByNY";
        ds.Tables.add(FlowCompleteByNY);


        //按照人员
        //发起数量
        sql = "SELECT StarterName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 AND FK_Dept='" + WebUser.getFK_Dept() + "' AND FK_Flow='" + this.getFK_Flow() + "' GROUP BY StarterName ";
        DataTable FlowStartByEmp = DBAccess.RunSQLReturnTable(sql);
        FlowStartByEmp.TableName = "FlowStartByEmp";
        ds.Tables.add(FlowStartByEmp);
        //完成数量
        sql = "SELECT StarterName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND FK_Dept='" + WebUser.getFK_Dept() + "' AND FK_Flow='" + this.getFK_Flow() + "' GROUP BY StarterName ";
        DataTable FlowCompleteByEmp = DBAccess.RunSQLReturnTable(sql);
        FlowCompleteByEmp.TableName = "FlowCompleteByEmp";
        ds.Tables.add(FlowCompleteByEmp);

        return bp.tools.Json.ToJson(ds);
    }


    /**
     获取指定流程的数据统计

     @return
     */
    public final String DataPanelOrg_GetGenerWorksByFlowNo()
    {
        java.util.Hashtable ht = new java.util.Hashtable();
        String dbStr = SystemConfig.getAppCenterDBVarStr();
        Paras ps = new Paras();
        ps.SQL = "SELECT COUNT(*) AS count,WFState FROM WF_GenerWorkFlow Where WFState >1 AND FID=0 AND FK_Flow=" + dbStr + "FK_Flow GROUP BY WFState";
        ps.Add("FK_Flow", this.getFK_Flow());
        DataTable dt = DBAccess.RunSQLReturnTable(ps);
        int allCount = 0;
        int count = 0;
        int wfstate = 0;
        int returnCount = 0;
        for (DataRow dr : dt.Rows)
        {
            count = Integer.parseInt(dr.getValue(0).toString());
            wfstate = Integer.parseInt(dr.getValue(1).toString());
            allCount += count;
            if (wfstate == WFState.Complete.getValue())
            {
                ht.put("GWF_CompleteCounts", count);
            }
            if (wfstate == WFState.ReturnSta.getValue())
            {
                returnCount = count;
                ht.put("GWF_ReturnCounts", count);
            }

            if (wfstate == WFState.Delete.getValue())
            {
                ht.put("GWF_DeleteCounts", count);
            }

        }
        //本部门的查询显示数量
        ps.SQL = "SELECT Count(DISTINCT G.WorkID) From WF_GenerWorkFlow G,WF_GenerWorkerlist L Where  G.WorkID=L.WorkID AND G.WFState=2 AND G.FK_Flow=" + dbStr + "FK_Flow AND L.IsPass=0";
        ps.Add("FK_Flow", this.getFK_Flow());
        int runingCount = DBAccess.RunSQLReturnValInt(ps);
        ht.put("GWF_RuningCounts", runingCount + returnCount);
        ht.put("GWF_Counts", allCount);
        ht.put("GWF_OverCounts", DBAccess.RunSQLReturnValInt("SELECT COUNT(DISTINCT A.WorkID) FROM WF_GenerWorkFlow A,WF_GenerWorkerList B WHERE A.WorkID=B.WorkID AND A.WFState>1 AND  B.IsPass=0 AND B.FK_Flow='" + this.getFK_Flow() + "'  AND  convert(varchar(100),B.SDT,120) < CONVERT(varchar(100), GETDATE(), 120)"));

        return bp.tools.Json.ToJson(ht);
    }


    /**
     获取单个流程的数据分析

     @return
     */
    public final String DataPanelOrg_GetAnalyseByFlowNo_DataSet() throws Exception {
        DataSet ds = new DataSet();
        //按照月份（统计的内容有发起数，完成数量，业务字段）
        //发起数量
        String sql = "SELECT SUBSTRING(FK_NY, 6, 2) AS FK_NY, count(WorkID) AS Num FROM WF_GenerWorkFlow WHERE WFState >1  AND FK_Flow='" + this.getFK_Flow() + "' AND FK_NY>DateName(year,GetDate())+'-00' GROUP BY FK_NY ";
        DataTable FlowStartByNY = DBAccess.RunSQLReturnTable(sql);
        FlowStartByNY.TableName = "FlowStartByNY";
        ds.Tables.add(FlowStartByNY);
        //完成数量
        sql = "SELECT SUBSTRING(FK_NY, 6, 2) AS FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3 AND  FK_Flow='" + this.getFK_Flow() + "' AND FK_NY>DateName(year,GetDate())+'-00' GROUP BY FK_NY ";
        DataTable FlowCompleteByNY = DBAccess.RunSQLReturnTable(sql);
        FlowCompleteByNY.TableName = "FlowCompleteByNY";
        ds.Tables.add(FlowCompleteByNY);


        //按照部门
        //发起数量
        sql = "SELECT DeptName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1  AND FK_Flow='" + this.getFK_Flow() + "' GROUP BY DeptName ";
        DataTable FlowStartByEmp = DBAccess.RunSQLReturnTable(sql);
        FlowStartByEmp.TableName = "FlowStartByDept";
        ds.Tables.add(FlowStartByEmp);
        //完成数量
        sql = "SELECT DeptName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState=3  AND FK_Flow='" + this.getFK_Flow() + "' GROUP BY DeptName ";
        DataTable FlowCompleteByEmp = DBAccess.RunSQLReturnTable(sql);
        FlowCompleteByEmp.TableName = "FlowCompleteByDept";
        ds.Tables.add(FlowCompleteByEmp);

        return bp.tools.Json.ToJson(ds);
    }

}
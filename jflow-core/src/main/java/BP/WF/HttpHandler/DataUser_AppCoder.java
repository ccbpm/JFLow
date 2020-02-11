package BP.WF.HttpHandler;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.Difference.Handler.WebContralBase;
import BP.Difference.SystemConfig;

import java.util.Hashtable;

public class DataUser_AppCoder extends WebContralBase {
    //#region 欢迎页面初始化.
    /// <summary>
    /// 欢迎页面初始化-获得数量.
    /// </summary>
    /// <returns></returns>
    public String FlowDesignerWelcome_Init()
    {
        Hashtable ht = new Hashtable();
        ht.put("FlowNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM WF_Flow")); //流程数
        ht.put("NodeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(NodeID) FROM WF_Node")); //节点数据
        //表单数.
        ht.put("FromNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM Sys_MapData  WHERE FK_FormTree !='' AND FK_FormTree IS NOT NULL ")); //表单数

        //所有的实例数量.
        ht.put("FlowInstaceNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState >1 ")); //实例数.

        //所有的待办数量.
        ht.put("TodolistNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=2 "));

        //退回数.
        ht.put("ReturnNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=5 "));

        //说有逾期的数量. 应该根据 WF_GenerWorkerList的 SDT 字段来求.
        if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
        {
            ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_EMPWORKS where STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now()"));

        }
        else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            String sql = "SELECT COUNT(*) from (SELECT *  FROM WF_EMPWORKS WHERE  REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0";

            sql += "UNION SELECT* FROM WF_EMPWORKS WHERE  REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 )";

            ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt(sql));
        }
        else
        {
            ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_EMPWORKS where convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120)"));
        }

        return BP.Tools.Json.ToJson(ht);
    }
    /// <summary>
    /// 获得数量  流程饼图，部门柱状图，月份折线图.
    /// </summary>
    /// <returns></returns>
    public String FlowDesignerWelcome_DataSet()
    {
        DataSet ds = new DataSet();

            //#region  实例分析
        //月份分组.
        String sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 GROUP BY FK_NY ";
        DataTable FlowsByNY = DBAccess.RunSQLReturnTable(sql);
        FlowsByNY.TableName = "FlowsByNY";
        ds.Tables.add(FlowsByNY);

        //部门分组.
        sql = "SELECT DeptName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 GROUP BY DeptName ";
        DataTable FlowsByDept = DBAccess.RunSQLReturnTable(sql);
        FlowsByDept.TableName = "FlowsByDept";
        ds.Tables.add(FlowsByDept);
           // #endregion 实例分析。


           // #region 待办 分析
            //待办 - 部门分组.
            sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 GROUP BY DeptName";
        DataTable TodolistByDept = DBAccess.RunSQLReturnTable(sql);
        TodolistByDept.TableName = "TodolistByDept";
        ds.Tables.add(TodolistByDept);

        //待办的 - 流程分组.
        sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 GROUP BY FlowName";
        DataTable TodolistByFlow = DBAccess.RunSQLReturnTable(sql);
        TodolistByFlow.TableName = "TodolistByFlow";
        ds.Tables.add(TodolistByFlow);
           // #endregion 待办。


            //#region 逾期 分析.
        //逾期的 - 流程分组.
        if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
        {
            sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() GROUP BY FlowName";

        }
        else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY FlowName ";
            sql += "UNION SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY FlowName";
        }
        else
        {
            sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) GROUP BY FlowName";
        }
        sql = "SELECT FlowName as name, count(WorkID) as value FROM WF_EmpWorks WHERE WFState >1 GROUP BY FlowName";
        DataTable OverTimeByFlow = DBAccess.RunSQLReturnTable(sql);
        OverTimeByFlow.TableName = "OverTimeByFlow";
        ds.Tables.add(OverTimeByFlow);

        //逾期的 - 部门分组.

        if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
        {
            sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() GROUP BY DeptName";

        }
        else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 GROUP BY DeptName ";
            sql += "UNION SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 GROUP BY DeptName";
        }
        else
        {
            sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) GROUP BY DeptName";
        }
        //sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 GROUP BY DeptName";
        DataTable OverTimeByDept = DBAccess.RunSQLReturnTable(sql);
        OverTimeByDept.TableName = "OverTimeByDept";
        ds.Tables.add(OverTimeByDept);
            //#endregion 逾期。


        return BP.Tools.Json.ToJson(ds);
    }
       // #endregion 欢迎页面初始化.


}

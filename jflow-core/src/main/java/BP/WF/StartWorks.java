package BP.WF;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataTable;
import BP.En.QueryObject;

/** 
 工作流程采集信息的基类 集合
*/
public abstract class StartWorks extends Works
{
	/** 
	 信息采集基类
	*/
	public StartWorks()
	{
	}
	/** 
	 查询到我的任务.
	 @return 
	*/
	public final DataTable RetrieveMyTask_del(String flow)
	{
		QueryObject qo = new QueryObject(this);
		//qo.Top=50;
		qo.AddWhere(StartWorkAttr.OID, " in ", " ( SELECT WorkID FROM V_WF_Msg  WHERE  FK_Flow='" + flow + "' AND FK_Emp='" + BP.Web.WebUser.getNo() + "' )");
		return qo.DoQueryToTable();
	}
	/** 
	 查询到我的任务.
	 @return 
	*/
	public final DataTable RetrieveMyTask(String flow)
	{
		//string sql="SELECT OID AS WORKID, TITLE, ";
		QueryObject qo = new QueryObject(this);
		//qo.Top=50;
		if (BP.Sys.SystemConfig.getAppCenterDBType()==DBType.Oracle)
		{
			qo.AddWhere(StartWorkAttr.OID, " in ", " (  SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + BP.Web.WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID ) )");
		}
		else
		{
			qo.AddWhere(StartWorkAttr.OID, " in ", " (  SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + BP.Web.WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID ) )");
		}
		return qo.DoQueryToTable();
	}
	/** 
	 查询到我的任务.
	 @return 
	*/
	public final DataTable RetrieveMyTaskV2(String flow)
	{
		String sql = "SELECT OID, TITLE, RDT, Rec FROM  " + this.getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE OID IN (   SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + BP.Web.WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID )  )";
		return DBAccess.RunSQLReturnTable(sql);
//            
//			QueryObject qo = new QueryObject(this);
//            //qo.Top=50;
//			qo.AddWhere(StartWorkAttr.OID," in ", " ( SELECT WorkID FROM V_WF_Msg  WHERE  FK_Flow='"+flow+"' AND FK_Emp="+Web.WebUser.getNo()+")" );
//			return qo.DoQueryToTable();
//			
	}
	/** 
	 查询到我的任务.
	 @return 
	*/
	public final DataTable RetrieveMyTask(String flow, String flowSort)
	{
		QueryObject qo = new QueryObject(this);
		//qo.Top=50;
		qo.AddWhere(StartWorkAttr.OID, " IN ", " ( SELECT WorkID FROM V_WF_Msg  WHERE  (FK_Flow='" + flow + "' AND FK_Emp='" + BP.Web.WebUser.getNo() + "' ) AND ( FK_Flow in ( SELECT No from WF_Flow WHERE FK_FlowSort='" + flowSort + "' )) )");
		return qo.DoQueryToTable();
	}
}
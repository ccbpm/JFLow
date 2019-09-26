package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;

/** 
 工作流程采集信息的基类 集合
*/
public abstract class StartWorks extends Works
{

		///#region 构造方法
	/** 
	 信息采集基类
	*/
	public StartWorks()
	{
	}

		///#endregion


		///#region 公共查询方法
	/** 
	 查询到我的任务.
	 		 
	 @return 
	*/
	public final DataTable RetrieveMyTask_del(String flow)
	{
		QueryObject qo = new QueryObject(this);
		//qo.Top=50;
		qo.AddWhere(StartWorkAttr.OID, " in ", " ( SELECT WorkID FROM V_WF_Msg  WHERE  FK_Flow='" + flow + "' AND FK_Emp='" + WebUser.getNo() + "' )");
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
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			qo.AddWhere(StartWorkAttr.OID, " in ", " (  SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID ) )");
		}
		else
		{
			qo.AddWhere(StartWorkAttr.OID, " in ", " (  SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID ) )");
		}
		return qo.DoQueryToTable();
	}
	/** 
	 查询到我的任务.
	 		 
	 @return 
	*/
	public final DataTable RetrieveMyTaskV2(String flow)
	{
		String sql = "SELECT OID, TITLE, RDT, Rec FROM  " + this.GetNewEntity.EnMap.PhysicsTable + " WHERE OID IN (   SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID )  )";
		return DBAccess.RunSQLReturnTable(sql);
		/*
		QueryObject qo = new QueryObject(this);
		//qo.Top=50;
		qo.AddWhere(StartWorkAttr.OID," in ", " ( SELECT WorkID FROM V_WF_Msg  WHERE  FK_Flow='"+flow+"' AND FK_Emp="+Web.WebUser.getNo()+")" );
		return qo.DoQueryToTable();
		*/
	}
	/** 
	 查询到我的任务.
	 		 
	 @return 
	*/
	public final DataTable RetrieveMyTask(String flow, String flowSort)
	{
		QueryObject qo = new QueryObject(this);
		//qo.Top=50;
		qo.AddWhere(StartWorkAttr.OID, " IN ", " ( SELECT WorkID FROM V_WF_Msg  WHERE  (FK_Flow='" + flow + "' AND FK_Emp='" + WebUser.getNo() + "' ) AND ( FK_Flow in ( SELECT No FROM WF_Flow WHERE FK_FlowSort='" + flowSort + "' )) )");
		return qo.DoQueryToTable();
	}

		///#endregion
}
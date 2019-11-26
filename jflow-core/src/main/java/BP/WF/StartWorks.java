package BP.WF;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Web.WebUser;

/**
 * 工作流程采集信息的基类 集合
 */
public abstract class StartWorks extends Works {
	/**
	 * 信息采集基类
	 */
	public StartWorks() {
	}

	/**
	 * 查询到我的任务.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final DataTable RetrieveMyTask_del(String flow) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StartWorkAttr.OID, " in ",
				" ( SELECT WorkID FROM V_WF_Msg  WHERE  FK_Flow='" + flow + "' AND FK_Emp='" + WebUser.getNo() + "' )");
		return qo.DoQueryToTable();
	}

	/**
	 * 查询到我的任务.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final DataTable RetrieveMyTask(String flow) throws Exception {
		QueryObject qo = new QueryObject(this);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			qo.AddWhere(StartWorkAttr.OID, " in ",
					" (  SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='"
							+ WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID ) )");
		} else {
			qo.AddWhere(StartWorkAttr.OID, " in ",
					" (  SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='"
							+ WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID ) )");
		}
		return qo.DoQueryToTable();
	}

	/**
	 * 查询到我的任务.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final DataTable RetrieveMyTaskV2(String flow) throws Exception {
		String sql = "SELECT OID, TITLE, RDT, Rec FROM  " + this.getNewEntity().getEnMap().getPhysicsTable()
				+ " WHERE OID IN (   SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Node IN ( SELECT FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='"
				+ WebUser.getNo() + "' AND FK_Flow='" + flow + "' AND WORKID=WF_GenerWorkFlow.WORKID )  )";
		return DBAccess.RunSQLReturnTable(sql);

	}

	/**
	 * 查询到我的任务.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final DataTable RetrieveMyTask(String flow, String flowSort) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StartWorkAttr.OID, " IN ",
				" ( SELECT WorkID FROM V_WF_Msg  WHERE  (FK_Flow='" + flow + "' AND FK_Emp='" + WebUser.getNo()
						+ "' ) AND ( FK_Flow in ( SELECT No FROM WF_Flow WHERE FK_FlowSort='" + flowSort + "' )) )");
		return qo.DoQueryToTable();
	}
}
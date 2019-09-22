package BP.WF.DTS;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.DTS.*;
import BP.WF.*;
import java.io.*;
import java.time.*;

public class OutputSQLs extends DataIOEn
{
	/** 
	 流程时效考核
	*/
	public OutputSQLs()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "OutputSQLs for produces DTSCHofNode";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do()
	{
		String sql = this.GenerSqls();
	}
	public final String GenerSqls()
	{
		Log.DefaultLogWriteLine(LogType.Info, BP.Web.WebUser.Name + "开始调度考核信息:" + LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		String infoMsg = "", errMsg = "";

		Nodes nds = new Nodes();
		nds.RetrieveAll();

		String fromDateTime = LocalDateTime.now().getYear() + "-01-01";
		fromDateTime = "2004-01-01 00:00";
		//string fromDateTime=DateTime.Now.Year+"-01-01 00:00";
		//string fromDateTime=DateTime.Now.Year+"-01-01 00:00";
		String insertSql = "";
		String delSQL = "";
		String updateSQL = "";

		String sqls = "";
		int i = 0;
		for (Node nd : nds)
		{
			if (nd.getIsPCNode()) // 如果是计算机节点.
			{
				continue;
			}
			i++;
			Map map = nd.getHisWork().EnMap;
			delSQL = "\n DELETE FROM " + map.PhysicsTable + " WHERE  OID  NOT IN (SELECT WORKID FROM WF_GenerWorkFlow ) AND WFState= " + WFState.Runing.getValue();

			sqls += "\n\n\n -- NO:" + i + "、" + nd.getFK_Flow() + nd.getFlowName() + " :  " + map.EnDesc + " \n" + delSQL + "; \n" + insertSql + "; \n" + updateSQL + ";";
		}
		Log.DefaultLogWriteLineInfo(sqls);
		return sqls;
	}
}
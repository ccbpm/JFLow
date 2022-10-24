package bp.wf.dts;

import bp.da.*;
import bp.dts.DataIOEn;
import bp.dts.DoType;
import bp.dts.RunTimeType;
import bp.en.*;
import bp.wf.*;
import bp.tools.DateUtils;
import bp.web.WebUser;
import java.util.Date;

public class OutputSQLs extends DataIOEn
{
	/**
	 流程时效考核
	 */
	public OutputSQLs()throws Exception
	{
		this.HisDoType = DoType.UnName;
		this.Title = "OutputSQLs for produces DTSCHofNode";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do() throws Exception
	{
		String sql = this.GenerSqls();
	}
	public final String GenerSqls() throws Exception
	{
		Log.DefaultLogWriteLine(LogType.Info, WebUser.getName() + "开始调度考核信息:" + DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		String infoMsg = "", errMsg = "";

		Nodes nds = new Nodes();
		nds.RetrieveAll();

		String fromDateTime = DataType.getCurrentYear() + "-01-01";
		fromDateTime = "2004-01-01 00:00";
		String insertSql = "";
		String delSQL = "";
		String updateSQL = "";

		String sqls = "";
		int i = 0;
		for (Node nd : nds.ToJavaList())
		{
			if (nd.isPCNode()) // 如果是计算机节点.
			{
				continue;
			}
			i++;
			Map map = nd.getHisWork().getEnMap();
			delSQL = "\n DELETE FROM " + map.getPhysicsTable() + " WHERE  OID  NOT IN (SELECT WORKID FROM WF_GenerWorkFlow ) AND WFState= " + WFState.Runing.getValue();

			sqls += "\n\n\n -- NO:" + i + "、" + nd.getFK_Flow() + nd.getFlowName() + " :  " + map.getEnDesc() + " \n" + delSQL + "; \n" + insertSql + "; \n" + updateSQL + ";";
		}
		Log.DefaultLogWriteLineInfo(sqls);
		return sqls;
	}
}
package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Difference.SystemConfig;
import BP.Sys.CCBPMRunModel;
import BP.Web.WebUser;


public class WF_Admin_AttrNode_FrmSln extends WebContralBase
{
	/**
	 构造函数
	*/
	public WF_Admin_AttrNode_FrmSln()
	{

	}


	public String RefOneFrmTreeFrms_Init() throws Exception
	{
		String sql = "";
		//单机模式下
		if (BP.WF.Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql += "SELECT  b.NAME AS SortName, a.No, A.Name,";
			sql += "A.PTable,";
			sql += "A.OrgNo ";
			sql += "FROM ";
			sql += "Sys_MapData A, ";
			sql += "Sys_FormTree B ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.NO ";
			sql += "ORDER BY B.IDX,A.IDX";

		}

		// 云服务器环境下
		if (BP.WF.Glo.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			sql += "SELECT  b.NAME AS SortName, a.No, A.Name, ";
			sql += "A.PTable, ";
			sql += "A.OrgNo ";
			sql += "FROM ";
			sql += "Sys_MapData A, ";
			sql += "Sys_FormTree B ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.NO ";
			sql += " AND B.OrgNo = '" + WebUser.getOrgNo() + "' ";
			sql += "ORDER BY B.IDX,A.IDX";
		}

		//集团模式下
		if (BP.WF.Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			sql += "select * from (SELECT  b.NAME AS SortName, a.No, A.Name,";
			sql += "A.PTable,";
			sql += "A.OrgNo ,b.idx as idx1,a.idx as idx2 ";
			sql += "FROM ";
			sql += "Sys_MapData A, ";
			sql += "Sys_FormTree B ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.NO ";
			sql += " AND B.OrgNo = '" + WebUser.getOrgNo() + "') t1 ";

			sql += " UNION ";

			sql += "select * from (SELECT  b.NAME AS SortName, a.No, A.Name,";
			sql += "A.PTable,A.OrgNo ,b.idx as idx1,a.idx as idx2 ";
			sql += " FROM ";
			sql += "Sys_MapData A, Sys_FormTree B, WF_FrmOrg C ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.No ";
			sql += " AND B.OrgNo = C.OrgNo ";
			sql += " AND B.OrgNo = '" + WebUser.getOrgNo() + "' ) t2 ";
			sql += "ORDER BY idx1,idx2";

		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//#warning 需要判断不同的数据库类型
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("SORTNAME").ColumnName = "SortName";
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PTABLE").ColumnName = "PTable";
			dt.Columns.get("ORGNO").ColumnName = "OrgNo";
		}

		return BP.Tools.Json.ToJson(dt);
	}

}
package BP.WF;

/** 
 审核工作节点
*/
public class WorkCheck
{
	/** 
	 工作ID
	*/
	public long WorkID = 0;
	public long FID = 0;
	/** 
	 节点ID
	*/
	public int NodeID = 0;
	/** 
	 流程编号
	*/
	public String FlowNo = null;
	public WorkCheck(String flowNo, int nodeID, long workid, long fid)
	{
		this.FlowNo = flowNo;
		this.NodeID = nodeID;
		this.WorkID = workid;
		this.FID = fid;
	}
	/** 
	 获取主键32位
	 
	 @return 
	*/
	public final int GetMyPK32()
	{
		try
		{
			int newPK = Integer.parseInt(String.valueOf(this.WorkID)) + this.NodeID + Integer.parseInt(this.FlowNo);
			String myPk = "";
			String sql = "SELECT TOP 1 RDT FROM WF_GenerWorkerlist WHERE WorkID={0} AND FK_Node={1} AND FK_Flow='{2}' ORDER BY RDT DESC";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(String.format(sql, this.WorkID, this.NodeID, this.FlowNo));
			if (dt != null && dt.Rows.Count > 0)
			{
				myPk = dt.Rows[0]["RDT"].toString();
				myPk = myPk.replace("-", "").replace(":", "").replace(" ", "");
				myPk = myPk.substring(4);
				newPK = Integer.parseInt(String.valueOf(this.WorkID)) + this.NodeID + Integer.parseInt(this.FlowNo) + Integer.parseInt(myPk);
			}
			return newPK;
		}
		catch (RuntimeException ex)
		{
			return 0;
		}
	}
	/** 
	 获取主键
	 
	 @return 
	*/
	public final long GetMyPK()
	{
		try
		{
			long newPK = Long.parseLong(String.valueOf(this.WorkID)) + this.NodeID + Long.parseLong(this.FlowNo);
			String myPk = "";
			String sql = "SELECT TOP 1 RDT FROM WF_GenerWorkerlist WHERE WorkID={0} AND FK_Node={1} AND FK_Flow='{2}' ORDER BY RDT DESC";


			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(String.format(sql, this.WorkID, this.NodeID, this.FlowNo));
			if (dt != null && dt.Rows.Count > 0)
			{
				myPk = dt.Rows[0]["RDT"].toString();
				myPk = myPk.replace("-", "").replace(":", "").replace(" ", "");
				myPk = myPk.substring(2);
				newPK = Long.parseLong(String.valueOf(this.WorkID)) + this.NodeID + Long.parseLong(this.FlowNo) + Long.parseLong(myPk);
			}
			return newPK;
		}
		catch (RuntimeException ex)
		{
			return 0;
		}
	}
	public final Tracks getHisWorkChecks()
	{
		if (_HisWorkChecks == null)
		{
			_HisWorkChecks = new Tracks();
			BP.En.QueryObject qo = new En.QueryObject(_HisWorkChecks);

			if (this.FID != 0)
			{
				qo.AddWhereIn(TrackAttr.WorkID, "(" + this.WorkID + "," + this.FID + ")");
			}
			else
			{
				qo.AddWhere(TrackAttr.WorkID, this.WorkID);

				if (this.WorkID != 0)
				{
					qo.addOr();
					qo.AddWhere(TrackAttr.FID, this.WorkID);
				}
			}

			qo.addOrderBy(TrackAttr.RDT);

			String sql = qo.SQL;
			sql = sql.replace("WF_Track", "ND" + Integer.parseInt(this.FlowNo) + "Track");
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql, qo.MyParas);

			dt.DefaultView.Sort = "RDT desc";

			BP.En.Attrs attrs = _HisWorkChecks.getGetNewEntity().EnMap.Attrs;
			for (DataRow dr : dt.Rows)
			{
				Track en = new Track();
				for (BP.En.Attr attr : attrs)
				{
					en.Row.SetValByKey(attr.Key, dr.get(attr.Key));
				}

				_HisWorkChecks.AddEntity(en);
			}
		}
		return _HisWorkChecks;
	}
	private Tracks _HisWorkChecks = null;
	public final Tracks getHisWorkChecks_New()
	{
		if (_HisWorkChecks == null)
		{
			_HisWorkChecks = new Tracks();

			BP.DA.Paras ps = new BP.DA.Paras();
			String sql = "SELECT * FROM ND" + Integer.parseInt(this.FlowNo) + "Track WHERE ";
			String dbstr = BP.Sys.SystemConfig.AppCenterDBVarStr;
			if (this.FID == 0)
			{
					// 为了兼容多种数据库，所以使用了两个相同的参数.
				sql += " WorkID=" + dbstr + "WorkID OR FID=" + dbstr + "FID ORDER BY RDT DESC ";
				ps.Add("WorkID", this.WorkID);
				ps.Add("FID", this.WorkID);
			}
			else
			{
					//sql += " WorkID=" + dbstr + "WorkID OR WorkID=" + dbstr + "FID ORDER BY RDT DESC ";
					// ps.Add("WorkID", this.WorkID);
					// ps.Add("FID", this.FID);
				 //   qo.AddWhereIn(TrackAttr.WorkID, "(" + this.WorkID + "," + this.FID + ")");

				sql += " WorkID=" + dbstr + "WorkID OR WorkID=" + dbstr + "FID ORDER BY RDT DESC ";
				ps.Add("WorkID", this.WorkID);
				ps.Add("FID", this.FID);
			}

			ps.SQL = sql;

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

				//获得Attrs
			BP.En.Attrs attrs = _HisWorkChecks.getGetNewEntity().EnMap.Attrs;
			 for (DataRow dr : dt.Rows)
			 {
				Track en = new Track();
				for (BP.En.Attr attr : attrs)
				{
					en.Row.SetValByKey(attr.Key, dr.get(attr.Key));
				}

				_HisWorkChecks.AddEntity(en);
			 }
		}
		return _HisWorkChecks;
	}
}
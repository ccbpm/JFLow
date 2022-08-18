package bp.wf;

import bp.da.*;
import bp.*;

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
	public final int GetMyPK32() throws Exception {
		try
		{
			int newPK = Integer.parseInt(String.valueOf(this.WorkID)) + this.NodeID + Integer.parseInt(this.FlowNo);
			String myPk = "";
			String sql = "SELECT TOP 1 RDT FROM WF_GenerWorkerlist WHERE WorkID=%1$s AND FK_Node=%2$s AND FK_Flow='%3$s' ORDER BY RDT DESC";
			DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql, this.WorkID, this.NodeID, this.FlowNo));
			if (dt != null && dt.Rows.size() > 0)
			{
				myPk = dt.Rows.get(0).getValue("RDT").toString();
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
	public final long GetMyPK() throws Exception {
		try
		{
			long newPK = Long.parseLong(String.valueOf(this.WorkID)) + this.NodeID + Long.parseLong(this.FlowNo);
			String myPk = "";
			String sql = "SELECT TOP 1 RDT FROM WF_GenerWorkerlist WHERE WorkID=%1$s AND FK_Node=%2$s AND FK_Flow='%3$s' ORDER BY RDT DESC";


			DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql, this.WorkID, this.NodeID, this.FlowNo));
			if (dt != null && dt.Rows.size() > 0)
			{
				myPk = dt.Rows.get(0).getValue("RDT").toString();
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
		public final Tracks getHisWorkChecks() throws Exception {
		if (_HisWorkChecks == null)
		{
			_HisWorkChecks = new Tracks();
			bp.en.QueryObject qo = new bp.en.QueryObject(_HisWorkChecks);

			if (this.FID != 0)
			{
				qo.AddWhere(TrackAttr.WorkID, this.FID);
				qo.addOr();
				qo.AddWhere(TrackAttr.WorkID, this.WorkID);
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

			qo.addOrderByDesc(TrackAttr.RDT);

			String sql = qo.getSQL();
			sql = sql.replace("WF_Track", "ND" + Integer.parseInt(this.FlowNo) + "Track");
			DataTable dt = null;

				//修复track 表.
			try
			{
				dt = DBAccess.RunSQLReturnTable(sql, qo.getMyParas());
			}
			catch (RuntimeException ex)
			{
				Track.CreateOrRepairTrackTable(this.FlowNo);
				dt = DBAccess.RunSQLReturnTable(sql, qo.getMyParas());
			}

			//dt.DefaultView.Sort = "RDT desc";

			//放入到track里面.
			bp.en.QueryObject.InitEntitiesByDataTable(_HisWorkChecks, dt, null);
		}
		return _HisWorkChecks;
	}
	private Tracks _HisWorkChecks = null;
}
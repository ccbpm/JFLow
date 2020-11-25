package bp.wf;

import bp.da.DBAccess;
import bp.da.DataTable;
import bp.difference.SystemConfig;
import bp.en.QueryObject;

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
		this.WorkID=workid;
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
	public final long GetMyPK()
	{
		try
		{
			long newPK = Long.parseLong(String.valueOf(this.WorkID)) + this.NodeID + Long.parseLong(this.FlowNo);
			String myPk = "";
			String sql = "SELECT TOP 1 RDT FROM WF_GenerWorkerlist WHERE WorkID={0} AND FK_Node={1} AND FK_Flow='{2}' ORDER BY RDT DESC";


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
	public final Tracks getHisWorkChecks() throws Exception
	{
		if (_HisWorkChecks == null)
		{
			Long workIdStr;
			if (this.FID != 0)
			{ 
				workIdStr = this.FID;
			}
			else
			{
				workIdStr = this.WorkID;
			}
			_HisWorkChecks = new Tracks();

			String sql ="SELECT MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,"
			+"EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan,Msg,NodeData,Tag,Exer"
			+ " FROM ND" + Integer.parseInt(this.FlowNo) + "Track t1, port_emp t2, port_dept t3  "
			+ "WHERE ( t1.WorkID = " + workIdStr + " OR t1.FID = " + workIdStr + " )"
			+ " AND t1.empfrom = t2.NO AND t3.NO = t2.FK_Dept ORDER BY t3.idx,t2.idx";
			DataTable dt = null;
			try
			{
				dt = DBAccess.RunSQLReturnTable(sql);
			}
			catch (RuntimeException ex)
			{
				Track.CreateOrRepairTrackTable(this.FlowNo);
				dt = DBAccess.RunSQLReturnTable(sql);
			}

			QueryObject.InitEntitiesByDataTable(_HisWorkChecks, dt, null);
			
			
		}
		return _HisWorkChecks;

		
		/*QueryObject qo = new QueryObject(_HisWorkChecks);

		if (this.FID != 0)
		{
			qo.AddWhere(TrackAttr.WorkID, this.FID);
			qo.addOr();
			qo.AddWhere(TrackAttr.FID, this.FID);
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

		String sql = qo.getSQL();
		sql = sql.replace("WF_Track", "ND" + Integer.parseInt(this.FlowNo) + "Track");
		
*/
			//修复track 表.
	}
	private Tracks _HisWorkChecks = null;
}
package BP.WF;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.WF.Template.*;
import BP.Web.WebUser;
import java.time.*;

/** 
 轨迹
*/
public class Track extends BP.En.Entity
{

		///#region 基本属性.
	/** 
	 表单数据
	*/
	public String FrmDB = null;
	/** 
	 主键值
	 * @throws Exception 
	*/
	public final String getMyPK() throws Exception
	{
		return this.GetValStrByKey(TrackAttr.MyPK);
	}
	public final void setMyPK(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.MyPK, value);
	}
	@Override
	public String getPK()
	{
		return "MyPK";
	}

	@Override
	public String getPKField()
	{
		return "MyPK";
	}
	public String FK_Flow = null;
	/** 
	 节点从
	*/
	public final int getNDFrom() throws Exception
	{
		return this.GetValIntByKey(TrackAttr.NDFrom);
	}
	public final void setNDFrom(int value) throws Exception
	{
		this.SetValByKey(TrackAttr.NDFrom, value);
	}
	/** 
	 节点到
	*/
	public final int getNDTo() throws Exception
	{
		return this.GetValIntByKey(TrackAttr.NDTo);
	}
	public final void setNDTo(int value) throws Exception
	{
		this.SetValByKey(TrackAttr.NDTo, value);
	}
	/** 
	 从人员
	*/
	public final String getEmpFrom() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.EmpFrom);
	}
	public final void setEmpFrom(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.EmpFrom, value);
	}
	/** 
	 到人员
	*/
	public final String getEmpTo() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.EmpTo);
	}
	public final void setEmpTo(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.EmpTo, value);
	}
	/** 
	 参数数据.
	*/
	public final String getTag() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.Tag);
	}
	public final void setTag(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.Tag, value);
	}
	/** 
	 记录日期
	*/
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.RDT, value);
	}
	/** 
	 fid
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(TrackAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		this.SetValByKey(TrackAttr.FID, value);
	}
	/** 
	 工作ID
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(TrackAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		this.SetValByKey(TrackAttr.WorkID, value);
	}
	/** 
	 CWorkID
	*/
	public final long getCWorkID() throws Exception
	{
		return this.GetValInt64ByKey(TrackAttr.CWorkID);
	}
	public final void setCWorkID(long value) throws Exception
	{
		this.SetValByKey(TrackAttr.CWorkID, value);
	}
	/** 
	 活动类型
	*/
	public final ActionType getHisActionType() throws Exception
	{
		return ActionType.forValue(this.GetValIntByKey(TrackAttr.ActionType));
	}
	public final void setHisActionType(ActionType value) throws Exception
	{
		this.SetValByKey(TrackAttr.ActionType, value.getValue());
	}
	/** 
	 获取动作文本
	 
	 @param at
	 @return 
	*/
	public static String GetActionTypeT(ActionType at)
	{
		switch (at)
		{
			case Forward:
				return "发送";
			case Return:
				return "退回";
			case ReturnAndBackWay:
				return "退回并原路返回";
			case Shift:
				return "移交";
			case UnShift:
				return "撤消移交";
			case Start:
				return "发起";
			case UnSend:
				return "撤消发送";
			case ForwardFL:
				return " -前进(分流点)";
			case ForwardHL:
				return " -向合流点发送";
			case FlowOver:
				return "流程结束";
			case CallChildenFlow:
				return "子流程调用";
			case StartChildenFlow:
				return "子流程发起";
			case SubThreadForward:
				return "线程前进";
			case RebackOverFlow:
				return "恢复已完成的流程";
			case FlowOverByCoercion:
				return "强制结束流程";
			case HungUp:
				return "挂起";
			case UnHungUp:
				return "取消挂起";
			case Press:
				return "催办";
			case CC:
				return "抄送";
			case WorkCheck:
				return "审核";
			case ForwardAskfor:
				return "加签发送";
			case AskforHelp:
				return "加签";
			case Skip:
				return "跳转";
			case HuiQian:
				return "主持人执行会签";
			case DeleteFlowByFlag:
				return "逻辑删除";
			case Order:
				return "队列发送";
			case FlowBBS:
				return "评论";
			case TeampUp:
				return "协作";
			default:
				return "信息" + at.toString();
		}
	}
	/** 
	 活动名称
	*/
	public final String getActionTypeText() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.ActionTypeText);
	}
	public final void setActionTypeText(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.ActionTypeText, value);
	}
	/** 
	 节点数据
	*/
	public final String getNodeData() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.NodeData);
	}
	public final void setNodeData(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.NodeData, value);
	}
	/** 
	 实际执行人
	*/
	public final String getExer() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.Exer);
	}
	public final void setExer(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.Exer, value);
	}
	/** 
	 审核意见
	*/
	public final String getMsg() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.Msg);
	}
	public final void setMsg(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.Msg, value);
	}
	/** 
	 消息
	*/
	public final String getMsgHtml() throws Exception
	{
		return this.GetValHtmlStringByKey(TrackAttr.Msg);
	}
	/** 
	 人员到
	*/
	public final String getEmpToT() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.EmpToT);
	}
	public final void setEmpToT(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.EmpToT, value);
	}
	/** 
	 人员从
	*/
	public final String getEmpFromT() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.EmpFromT);
	}
	public final void setEmpFromT(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.EmpFromT, value);
	}
	/** 
	 节点从
	*/
	public final String getNDFromT() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.NDFromT);
	}
	public final void setNDFromT(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.NDFromT, value);
	}
	/** 
	 节点到
	*/
	public final String getNDToT() throws Exception
	{
		return this.GetValStringByKey(TrackAttr.NDToT);
	}
	public final void setNDToT(String value) throws Exception
	{
		this.SetValByKey(TrackAttr.NDToT, value);
	}

		///#endregion attrs


		///#region 构造.
	public String RptName = null;
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Track", "轨迹表");


			///#region 字段
			//增加一个自动增长的列.
		map.AddTBIntPK(TrackAttr.MyPK, 0, "MyPK", true, false);

		map.AddTBInt(TrackAttr.ActionType, 0, "类型", true, false);
		map.AddTBString(TrackAttr.ActionTypeText, null, "类型(名称)", true, false, 0, 30, 100);
		map.AddTBInt(TrackAttr.FID, 0, "流程ID", true, false);
		map.AddTBInt(TrackAttr.WorkID, 0, "工作ID", true, false);
			//  map.AddTBInt(TrackAttr.CWorkID, 0, "CWorkID", true, false);

		map.AddTBInt(TrackAttr.NDFrom, 0, "从节点", true, false);
		map.AddTBString(TrackAttr.NDFromT, null, "从节点(名称)", true, false, 0, 300, 100);

		map.AddTBInt(TrackAttr.NDTo, 0, "到节点", true, false);
		map.AddTBString(TrackAttr.NDToT, null, "到节点(名称)", true, false, 0, 999, 900);

		map.AddTBString(TrackAttr.EmpFrom, null, "从人员", true, false, 0, 50, 100);
		map.AddTBString(TrackAttr.EmpFromT, null, "从人员(名称)", true, false, 0, 30, 100);

		map.AddTBString(TrackAttr.EmpTo, null, "到人员", true, false, 0, 2000, 100);
		map.AddTBString(TrackAttr.EmpToT, null, "到人员(名称)", true, false, 0, 2000, 100);

		map.AddTBString(TrackAttr.RDT, null, "日期", true, false, 0, 20, 100);
		map.AddTBFloat(TrackAttr.WorkTimeSpan, 0, "时间跨度(天)", true, false);
		map.AddTBStringDoc(TrackAttr.Msg, null, "消息", true, false);
		map.AddTBStringDoc(TrackAttr.NodeData, null, "节点数据(日志信息)", true, false);
		map.AddTBString(TrackAttr.Tag, null, "参数", true, false, 0, 300, 3000);
		map.AddTBString(TrackAttr.Exer, null, "执行人", true, false, 0, 200, 100);
			//   map.AddTBString(TrackAttr.InnerKey, null, "内部的Key,用于防止插入重复", true, false, 0, 200, 100);

			///#endregion 字段

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 轨迹
	*/
	public Track()
	{
	}
	/** 
	 轨迹
	 
	 @param flowNo 流程编号
	 @param mypk 主键
	*/
	public Track(String flowNo, String mypk) throws Exception
	{
		String sql = "SELECT * FROM ND" + Integer.parseInt(flowNo) + "Track WHERE MyPK='" + mypk + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@日志数据丢失.." + sql);
		}
		this.getRow().LoadDataTable(dt, dt.Rows.get(0));
	}
	/** 
	 创建track.
	 
	 @param fk_flow 流程编号
	 * @throws Exception 
	*/
	public static void CreateOrRepairTrackTable(String fk_flow) throws Exception
	{
		String ptable = "ND" + Integer.parseInt(fk_flow) + "Track";
		if (DBAccess.IsExitsObject(ptable) == true)
		{
			return;
		}

		//删除主键.
		DBAccess.DropTablePK(ptable);

		// 删除主键.
		DBAccess.DropTablePK("WF_Track");

		//创建表.
		Track tk = new Track();
		try
		{
			tk.CheckPhysicsTable();
		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DebugWriteError(ex.getMessage() + " @可以容忍的异常....");
		}

		// 删除主键.
		DBAccess.DropTablePK("WF_Track");

		String sqlRename = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				sqlRename = "EXEC SP_RENAME WF_Track, " + ptable;
				break;
			case Informix:
				sqlRename = "RENAME TABLE WF_Track TO " + ptable;
				break;
			case Oracle:
				sqlRename = "ALTER TABLE WF_Track RENAME to " + ptable;
				break;
			case PostgreSQL:
				sqlRename = "ALTER TABLE WF_Track RENAME to " + ptable;
				break;
			case MySQL:
				sqlRename = "ALTER TABLE WF_Track RENAME to " + ptable;
				break;
			default:
				sqlRename = "ALTER TABLE WF_Track RENAME to " + ptable;
				break;
		}

		//重命名.
		DBAccess.RunSQL(sqlRename);

		//删除主键.
		DBAccess.DropTablePK(ptable);

		//创建主键.
		DBAccess.CreatePK(ptable, TrackAttr.MyPK, tk.getEnMap().getEnDBUrl().getDBType());
	}
	/** 
	 插入
	 
	 @param mypk
	 * @throws Exception 
	*/
	public final void DoInsert(long mypk) throws Exception
	{
		String ptable = "ND" + Integer.parseInt(this.FK_Flow) + "Track";
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		String sql = "INSERT INTO " + ptable;
		sql += "(";
		sql += "" + TrackAttr.MyPK + ",";
		sql += "" + TrackAttr.ActionType + ",";
		sql += "" + TrackAttr.ActionTypeText + ",";
		sql += "" + TrackAttr.FID + ",";
		sql += "" + TrackAttr.WorkID + ",";
		sql += "" + TrackAttr.NDFrom + ",";
		sql += "" + TrackAttr.NDFromT + ",";
		sql += "" + TrackAttr.NDTo + ",";
		sql += "" + TrackAttr.NDToT + ",";
		sql += "" + TrackAttr.EmpFrom + ",";
		sql += "" + TrackAttr.EmpFromT + ",";
		sql += "" + TrackAttr.EmpTo + ",";
		sql += "" + TrackAttr.EmpToT + ",";
		sql += "" + TrackAttr.RDT + ",";
		sql += "" + TrackAttr.WorkTimeSpan + ",";
		sql += "" + TrackAttr.Msg + ",";
		sql += "" + TrackAttr.NodeData + ",";
		sql += "" + TrackAttr.Tag + ",";

		sql += "" + TrackAttr.Exer + "";
		sql += ") VALUES (";
		sql += dbstr + TrackAttr.MyPK + ",";
		sql += dbstr + TrackAttr.ActionType + ",";
		sql += dbstr + TrackAttr.ActionTypeText + ",";
		sql += dbstr + TrackAttr.FID + ",";
		sql += dbstr + TrackAttr.WorkID + ",";
		sql += dbstr + TrackAttr.NDFrom + ",";
		sql += dbstr + TrackAttr.NDFromT + ",";
		sql += dbstr + TrackAttr.NDTo + ",";
		sql += dbstr + TrackAttr.NDToT + ",";
		sql += dbstr + TrackAttr.EmpFrom + ",";
		sql += dbstr + TrackAttr.EmpFromT + ",";
		sql += dbstr + TrackAttr.EmpTo + ",";
		sql += dbstr + TrackAttr.EmpToT + ",";
		sql += dbstr + TrackAttr.RDT + ",";
		sql += dbstr + TrackAttr.WorkTimeSpan + ",";
		sql += dbstr + TrackAttr.Msg + ",";
		sql += dbstr + TrackAttr.NodeData + ",";
		sql += dbstr + TrackAttr.Tag + ",";
		sql += dbstr + TrackAttr.Exer + "";
		sql += ")";

		//如果这里是空的，就认为它是，从系统里面取出。
		if (DataType.IsNullOrEmpty(this.getActionTypeText()))
		{
			this.setActionTypeText(Track.GetActionTypeT(this.getHisActionType()));
		}

		if (mypk == 0)
		{
			this.SetValByKey(TrackAttr.MyPK, DBAccess.GenerOIDByGUID());

		}
		else
		{
			DBAccess.RunSQL("DELETE  FROM " + ptable + " WHERE MyPK=" + mypk);
			this.SetValByKey(TrackAttr.MyPK, mypk);
		}

		this.setRDT(DataType.getCurrentDataTimess());


			///#region 执行保存
		try
		{
			Paras ps = SqlBuilder.GenerParas(this, null);
			ps.SQL = sql;

			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					this.RunSQL(ps);
					break;
				case Access:
					this.RunSQL(ps);
					break;
				case MySQL:
				case Informix:
				default:
					ps.SQL = ps.SQL.replace("[", "").replace("]", "");
					this.RunSQL(ps); // 运行sql.
					break;
			}
		}
		catch (RuntimeException ex)
		{
			// 写入日志.
			Log.DefaultLogWriteLineError(ex.getMessage());

			//创建track.
			//Track.CreateOrRepairTrackTable(this.FK_Flow);
			throw ex;
		}

		//把frm日志写入到数据里.
		if (this.FrmDB != null)
		{
			BP.DA.DBAccess.SaveBigTextToDB(this.FrmDB, ptable, "MyPK", this.getMyPK(), "FrmDB");
		}


			///#endregion 执行保存

		//解决流程的开始日期计算错误的问题.
		if (this.getHisActionType() == ActionType.Start || this.getHisActionType() == ActionType.StartChildenFlow)
		{
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerlist SET RDT=" + SystemConfig.getAppCenterDBVarStr() + "RDT WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID ";
			ps.Add("RDT", this.getRDT());
			ps.Add("WorkID", this.getWorkID());
			DBAccess.RunSQL(ps);

			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkFlow SET RDT=" + SystemConfig.getAppCenterDBVarStr() + "RDT WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID ";
			ps.Add("RDT", this.getRDT());
			ps.Add("WorkID", this.getWorkID());
			DBAccess.RunSQL(ps);
		}
	}
	/** 
	 增加授权人
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (WebUser.getNo().equals("Guest"))
		{
			this.setExer(BP.Web.GuestUser.getNo() + "," + BP.Web.GuestUser.getName());
		}
		else
		{
			if (WebUser.getIsAuthorize())
			{
				this.setExer(BP.WF.Glo.DealUserInfoShowModel(WebUser.getAuth(), WebUser.getAuthName()));
			}
			else
			{
				this.setExer(BP.WF.Glo.DealUserInfoShowModel(WebUser.getNo(), WebUser.getName()));
			}
		}

		this.setRDT(BP.DA.DataType.getCurrentDataTimess());

		this.DoInsert(0);
		return false;
	}

		///#endregion 构造.
}
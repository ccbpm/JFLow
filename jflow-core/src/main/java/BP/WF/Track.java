package BP.WF;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.Map;
import BP.En.SqlBuilder;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;

/** 
 轨迹
 
*/
public class Track extends BP.En.Entity
{
	/** 
	 表单数据
	 
	*/
	public String FrmDB = null;
	/** 
	 主键值
	 
	*/
	public final String getMyPK()
	{
		return this.GetValStrByKey(TrackAttr.MyPK);
	}
	public final void setMyPK(String value)
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


		///#region attrs

	/** 
	 节点从
	 
	*/
	public final int getNDFrom()
	{
		return this.GetValIntByKey(TrackAttr.NDFrom);
	}
	public final void setNDFrom(int value)
	{
		this.SetValByKey(TrackAttr.NDFrom, value);
	}

	/** 
	 节点到
	 
	*/
	public final int getNDTo()
	{
		return this.GetValIntByKey(TrackAttr.NDTo);
	}
	public final void setNDTo(int value)
	{
		this.SetValByKey(TrackAttr.NDTo, value);
	}
	/** 
	 从人员
	 
	*/
	public final String getEmpFrom()
	{
		return this.GetValStringByKey(TrackAttr.EmpFrom);
	}
	public final void setEmpFrom(String value)
	{
		this.SetValByKey(TrackAttr.EmpFrom, value);
	}
	///// <summary>
	///// 内部的PK.
	///// </summary>
	//public string InnerKey_del
	//{
	//    get
	//    {
	//        return this.GetValStringByKey(TrackAttr.InnerKey);
	//    }
	//    set
	//    {
	//        this.SetValByKey(TrackAttr.InnerKey, value);
	//    }
	//}
	/** 
	 到人员
	 
	*/
	public final String getEmpTo()
	{
		return this.GetValStringByKey(TrackAttr.EmpTo);
	}
	public final void setEmpTo(String value)
	{
		this.SetValByKey(TrackAttr.EmpTo, value);
	}
	public final String getTag()
	{
		return this.GetValStringByKey(TrackAttr.Tag);
	}
	public final void setTag(String value)
	{
		this.SetValByKey(TrackAttr.Tag, value);
	}
	/** 
	 记录日期
	 
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(TrackAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(TrackAttr.RDT, value);
	}

	/** 
	 fid
	 
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(TrackAttr.FID);
	}
	public final void setFID(long value)
	{
		this.SetValByKey(TrackAttr.FID, value);
	}
	/** 
	 工作ID
	 
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(TrackAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		this.SetValByKey(TrackAttr.WorkID, value);
	}
	/** 
	 CWorkID
	 
	*/
	public final long getCWorkID()
	{
		return this.GetValInt64ByKey(TrackAttr.CWorkID);
	}
	public final void setCWorkID(long value)
	{
		this.SetValByKey(TrackAttr.CWorkID, value);
	}
	/** 
	 活动类型
	 
	*/
	public final ActionType getHisActionType()
	{
		return ActionType.forValue(this.GetValIntByKey(TrackAttr.ActionType));
	}
	public final void setHisActionType(ActionType value)
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
				return "前进";
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
				return "撤消发起";
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
			case Info:
				return "信息";
			case DeleteFlowByFlag:
				return "逻辑删除";
			case Order:
				return "队列发送";
			case FlowBBS:
				return "评论";
			default:
				return "未知";
		}
	}

	public final String getActionTypeText()
	{
		return this.GetValStringByKey(TrackAttr.ActionTypeText);
	}
	public final void setActionTypeText(String value)
	{
		this.SetValByKey(TrackAttr.ActionTypeText, value);
	}

	/** 
	 节点数据
	 
	*/
	public final String getNodeData()
	{
		return this.GetValStringByKey(TrackAttr.NodeData);
	}
	public final void setNodeData(String value)
	{
		this.SetValByKey(TrackAttr.NodeData, value);
	}
	/** 
	 实际执行人
	 
	*/
	public final String getExer()
	{
		return this.GetValStringByKey(TrackAttr.Exer);
	}
	public final void setExer(String value)
	{
		this.SetValByKey(TrackAttr.Exer, value);
	}
	/** 
	 审核意见
	 
	*/
	public final String getMsg()
	{
		return this.GetValStringByKey(TrackAttr.Msg);
	}
	public final void setMsg(String value)
	{
		this.SetValByKey(TrackAttr.Msg, value);
	}

	public final String getMsgHtml()
	{
		return this.GetValHtmlStringByKey(TrackAttr.Msg);
	}

	public final String getEmpToT()
	{
		return this.GetValStringByKey(TrackAttr.EmpToT);
	}
	public final void setEmpToT(String value)
	{
		this.SetValByKey(TrackAttr.EmpToT, value);
	}

	public final String getEmpFromT()
	{
		return this.GetValStringByKey(TrackAttr.EmpFromT);
	}
	public final void setEmpFromT(String value)
	{
		this.SetValByKey(TrackAttr.EmpFromT, value);
	}

	public final String getNDFromT()
	{
		return this.GetValStringByKey(TrackAttr.NDFromT);
	}
	public final void setNDFromT(String value)
	{
		this.SetValByKey(TrackAttr.NDFromT, value);
	}

	public final String getNDToT()
	{
		return this.GetValStringByKey(TrackAttr.NDToT);
	}
	public final void setNDToT(String value)
	{
		this.SetValByKey(TrackAttr.NDToT, value);
	}
	
	 
	public final void setNDFrom(String value)
	{
		this.SetValByKey(TrackAttr.NDFrom, value);
	}
	

		///#endregion attrs


		

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

		map.AddTBString(TrackAttr.EmpFrom, null, "从人员", true, false, 0, 20, 100);
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

	public String FK_Flow = null;

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
	public Track(String flowNo, String mypk)
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
		catch(RuntimeException ex)
		{
			BP.DA.Log.DebugWriteError(ex.getMessage()+" @可以容忍的异常....");
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
				sqlRename = "ALTER TABLE WF_Track rename to " + ptable;
				break;

			case MySQL:
				sqlRename = "ALTER TABLE WF_Track rename to " + ptable;
				break;
			default:
				throw new RuntimeException("@未涉及到此类型.");
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
		if (StringHelper.isNullOrEmpty(this.getActionTypeText()))
		{
			this.setActionTypeText(Track.GetActionTypeT(this.getHisActionType()));
		}

		if (mypk == 0)
		{
			this.SetValByKey(TrackAttr.MyPK, DBAccess.GenerOIDByGUID());
			//this.SetValByKey(TrackAttr.MyPK, DBAccess.GenerGUID());
		}
		else
		{
			DBAccess.RunSQL("DELETE  FROM " + ptable + " WHERE MyPK=" + mypk);
			this.SetValByKey(TrackAttr.MyPK, mypk);
		}

		this.setRDT(DataType.getCurrentDataTime());

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
					//  this.RunSQL(sql.replace("[", "").replace("]", ""), SqlBuilder.GenerParas(this, null));
					break;
			}
		}
		catch (RuntimeException ex)
		{
			// 写入日志.
			Log.DefaultLogWriteLineError(ex.getMessage());

			//创建track.
			Track.CreateOrRepairTrackTable(this.FK_Flow);
			throw ex;
		}

		//把frm日志写入到数据里.
		if (this.FrmDB != null) 
		{
			BP.DA.DBAccess.SaveBigTextToDB(this.FrmDB, ptable, "MyPK", this.getMyPK(), "FrmDB");
		}


			///#endregion 执行保存
	}

	/** 
	 增加授权人
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (BP.Web.WebUser.getNo().equals("Guest"))
		{
			this.setExer(BP.Web.GuestUser.getNo() +","+ BP.Web.GuestUser.getName());
		}
		else
		{
			if (BP.Web.WebUser.getIsAuthorize())
			{
				this.setExer(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getAuth(), BP.Web.WebUser.getName()));
			}
			else
			{
				this.setExer(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getNo(), BP.Web.WebUser.getName()));
			}
		}

		this.setRDT(BP.DA.DataType.getCurrentDataTime());//getCurrentDataTimess

		this.DoInsert(0);
		return false;
	}
	public void setFK_Flow(String flowNo) {
		
		this.SetValByKey("FK_Flow", flowNo);
		
		this.FK_Flow=flowNo;
		
		
	}


		///#endregion 属性
}
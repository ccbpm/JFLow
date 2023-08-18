package bp.ta;


import bp.en.EntityMyPK;
import bp.en.Map;

/**
 轨迹
*/
public class Track extends EntityMyPK
{
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 属性
	public final int getUseHH()
	{
		return this.GetValIntByKey(TrackAttr.UseHH);
	}
	public final void setUseHH(int value)
	{
		this.SetValByKey(TrackAttr.UseHH, value);
	}
	public final int getUseMM()
	{
		return this.GetValIntByKey(TrackAttr.UseMM);
	}
	public final void setUseMM(int value)
	{
		this.SetValByKey(TrackAttr.UseMM, value);
	}
	public final int getWCL()
	{
		return this.GetValIntByKey(TrackAttr.WCL);
	}
	public final void setWCL(int value)
	{
		this.SetValByKey(TrackAttr.WCL, value);
	}
	public final int getTaskID()
	{
		return this.GetValIntByKey(TrackAttr.TaskID);
	}
	public final void setTaskID(long value)
	{
		this.SetValByKey(TrackAttr.TaskID, value);
	}
	public final String getPrjNo()
	{
		return this.GetValStringByKey(TrackAttr.PrjNo);
	}
	public final void setPrjNo(String value)
	{
		this.SetValByKey(TrackAttr.PrjNo, value);
	}
	public final String getEmpNo()
	{
		return this.GetValStringByKey(TrackAttr.EmpNo);
	}
	public final void setEmpNo(String value)
	{
		this.SetValByKey(TrackAttr.EmpNo, value);
	}
	public final String getEmpName()
	{
		return this.GetValStringByKey(TrackAttr.EmpName);
	}
	public final void setEmpName(String value)
	{
		this.SetValByKey(TrackAttr.EmpName, value);
	}
	public final String getActionType()
	{
		return this.GetValStringByKey(TrackAttr.ActionType);
	}
	public final void setActionType(String value)
	{
		this.SetValByKey(TrackAttr.ActionType, value);
	}
	public final String getActionName()
	{
		return this.GetValStringByKey(TrackAttr.ActionName);
	}
	public final void setActionName(String value)
	{
		this.SetValByKey(TrackAttr.ActionName, value);
	}
	public final String getDocs()
	{
		return this.GetValStringByKey(TrackAttr.Docs);
	}
	public final void setDocs(String value)
	{
		this.SetValByKey(TrackAttr.Docs, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(TrackAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(TrackAttr.RDT, value);
	}
	public final String getRecNo()
	{
		return this.GetValStringByKey(TrackAttr.RecNo);
	}
	public final void setRecNo(String value)
	{
		this.SetValByKey(TrackAttr.RecNo, value);
	}
	public final String getRecName()
	{
		return this.GetValStringByKey(TrackAttr.RecName);
	}
	public final void setRecName(String value)
	{
		this.SetValByKey(TrackAttr.RecName, value);
	}
		///#endregion 属性

		///#region 构造函数
	/** 
	 轨迹
	*/
	public Track()
	{
	}
	/** 
	 轨迹
	 
	 @param mypk
	*/
	public Track(String mypk) throws Exception {
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("TA_Track", "轨迹");

		map.AddMyPK(true);
		map.AddTBInt(TrackAttr.TaskID, 0, "TaskID", true, true);
		map.AddTBString(TrackAttr.PrjNo, null, "项目编号", true, false, 0, 50, 300);
		map.AddTBString(TrackAttr.EmpNo, null, "人员编号", true, false, 0, 50, 300);
		map.AddTBString(TrackAttr.EmpName, null, "人员名称", true, false, 0, 50, 300);

		map.AddTBString(TrackAttr.ActionType, null, "活动类型", true, false, 0, 50, 300);
		map.AddTBString(TrackAttr.ActionName, null, "活动名称", true, false, 0, 50, 300);
		map.AddTBString(TrackAttr.Docs, null, "内容", true, false, 0, 4000, 300);

		map.AddTBString(TrackAttr.RDT, null, "记录日期", true, false, 0, 50, 300);
		map.AddTBString(TrackAttr.RecNo, null, "记录人编号", true, false, 0, 50, 300);
		map.AddTBString(TrackAttr.RecName, null, "记录人名称", true, false, 0, 50, 300);


		map.AddTBInt(TrackAttr.WCL, 0, "完成率", true, true);
		map.AddTBInt(TrackAttr.UseHH, 0, "UseHH", true, true);
		map.AddTBInt(TrackAttr.UseMM, 0, "UseMM", true, true);


		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}

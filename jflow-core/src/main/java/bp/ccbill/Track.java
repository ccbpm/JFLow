package bp.ccbill;

import bp.da.*;
import bp.en.*;

/** 
 轨迹
*/
public class Track extends EntityMyPK
{

		///#region 字段属性.
	/** 
	 参数数据.
	*/
	public final String getTag()
	{
		return this.GetValStringByKey(TrackAttr.Tag);
	}
	public final void setTag(String value)
	 {
		this.SetValByKey(TrackAttr.Tag, value);
	}
	/** 
	 表单ID
	*/
	public final String getFrmID()
	{
		return this.GetValStringByKey(TrackAttr.FrmID);
	}
	public final void setFrmID(String value)
	 {
		this.SetValByKey(TrackAttr.FrmID, value);
	}
	/** 
	 表单名称
	*/
	public final String getFrmName()
	{
		return this.GetValStringByKey(TrackAttr.FrmName);
	}
	public final void setFrmName(String value)
	 {
		this.SetValByKey(TrackAttr.FrmName, value);
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
	public final String getWorkID()
	{
		return this.GetValStringByKey(TrackAttr.WorkID);
	}
	public final void setWorkID(String value)
	 {
		this.SetValByKey(TrackAttr.WorkID, value);
	}
	public final String getActionType()
	{
		return this.GetValStringByKey(TrackAttr.ActionType);
	}
	public final void setActionType(String value)
	 {
		this.SetValByKey(TrackAttr.ActionType, value);
	}
	/** 
	 获取动作文本
	 
	 param at
	 @return 
	*/
	public static String GetActionTypeT(String at)
	{
		switch (at)
		{
			case bp.ccbill.FrmActionType.Save:
				return "保存";
			case bp.ccbill.FrmActionType.Create:
				return "提交";
			case bp.ccbill.FrmActionType.BBS:
				return "评论";
			case bp.ccbill.FrmActionType.View:
				return "打开";
			default:
				return "信息" + at.toString();
		}
	}
	/** 
	 活动名称
	*/
	public final String getActionTypeText()
	{
		return this.GetValStringByKey(TrackAttr.ActionTypeText);
	}
	public final void setActionTypeText(String value)
	 {
		this.SetValByKey(TrackAttr.ActionTypeText, value);
	}
	/** 
	 记录人
	*/
	public final String getRec()
	{
		return this.GetValStringByKey(TrackAttr.Rec);
	}
	public final void setRec(String value)
	 {
		this.SetValByKey(TrackAttr.Rec, value);
	}
	/** 
	 记录人名字
	*/
	public final String getRecName()
	{
		return this.GetValStringByKey(TrackAttr.RecName);
	}
	public final void setRecName(String value)
	 {
		this.SetValByKey(TrackAttr.RecName, value);
	}
	/** 
	 消息
	*/
	public final String getMsg()
	{
		return this.GetValStringByKey(TrackAttr.Msg);
	}
	public final void setMsg(String value)
	 {
		this.SetValByKey(TrackAttr.Msg, value);
	}
	/** 
	 消息
	*/
	public final String getMsgHtml()
	{
		return this.GetValHtmlStringByKey(TrackAttr.Msg);
	}
	public final long getWorkIDOfFlow()
	{
		return this.GetValInt64ByKey(TrackAttr.WorkIDOfFlow);
	}
	public final void setWorkIDOfFlow(long value)
	 {
		this.SetValByKey(TrackAttr.WorkIDOfFlow, value);
	}

		///#endregion attrs


		///#region 流程属性.
	/** 
	 流程编号
	*/
	public final String getFlowNo()
	{
		return this.GetValStringByKey(TrackAttr.FlowNo);
	}
	public final void setFlowNo(String value)
	 {
		this.SetValByKey(TrackAttr.FlowNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStringByKey(TrackAttr.FlowName);
	}
	public final void setFlowName(String value)
	 {
		this.SetValByKey(TrackAttr.FlowName, value);
	}
	public final int getNodeID()
	{
		return this.GetValIntByKey(TrackAttr.NodeID);
	}
	public final void setNodeID(int value)
	 {
		this.SetValByKey(TrackAttr.NodeID, value);
	}
	/** 
	 节点名称
	*/
	public final String getNodeName()
	{
		return this.GetValStringByKey(TrackAttr.NodeName);
	}
	public final void setNodeName(String value)
	 {
		this.SetValByKey(TrackAttr.NodeName, value);
	}
	public final String getDeptNo()
	{
		return this.GetValStringByKey(TrackAttr.DeptNo);
	}
	public final void setDeptNo(String value)
	 {
		this.SetValByKey(TrackAttr.DeptNo, value);
	}
	public final String getDeptName()
	{
		return this.GetValStringByKey(TrackAttr.DeptName);
	}
	public final void setDeptName(String value)
	 {
		this.SetValByKey(TrackAttr.DeptName, value);
	}

		///#endregion 流程属性.



		///#region 构造.
	/** 
	 表单轨迹表
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Frm_Track", "表单轨迹表");


			///#region 基本字段.
		map.AddMyPK(true); //组合主键.

		map.AddTBString(TrackAttr.FrmID, null, "表单ID", true, false, 0, 50, 200);
		map.AddTBString(TrackAttr.FrmName, null, "表单名称(可以为空)", true, false, 0, 200, 200);

			// map.AddTBInt(TrackAttr.ActionType, 0, "类型", true, false);
		map.AddTBString(TrackAttr.ActionType, null, "类型", true, false, 0, 30, 100);
		map.AddTBString(TrackAttr.ActionTypeText, null, "类型(名称)", true, false, 0, 30, 100);

		  //  map.AddTBInt(TrackAttr.WorkID, 0, "工作ID/OID", true, false);
		map.AddTBString(TrackAttr.WorkID, null, "工作ID/OID", true, false, 0, 100, 100);

		map.AddTBString(TrackAttr.Msg, null, "消息", true, false, 0, 300, 3000);

		map.AddTBString(TrackAttr.Rec, null, "记录人", true, false, 0, 200, 100);
		map.AddTBString(TrackAttr.RecName, null, "名称", true, false, 0, 200, 100);
		map.AddTBDateTime(TrackAttr.RDT, null, "记录日期时间", true, false);

		map.AddTBString(TrackAttr.DeptNo, null, "部门编号", true, false, 0, 200, 100);
		map.AddTBString(TrackAttr.DeptName, null, "名称", true, false, 0, 200, 100);

			///#endregion 基本字段


			///#region 流程信息.
			//流程信息.
		map.AddTBInt(TrackAttr.FID, 0, "FID", true, false);

		map.AddTBString(TrackAttr.FlowNo, null, "流程ID", true, false, 0, 20, 20);
		map.AddTBString(TrackAttr.FlowName, null, "流程名称", true, false, 0, 200, 200);

		map.AddTBInt(TrackAttr.NodeID, 0, "节点ID", true, false);
		map.AddTBString(TrackAttr.NodeName, null, "节点名称", true, false, 0, 200, 200);
		map.AddTBInt(TrackAttr.WorkIDOfFlow, 0, "工作ID/OID", true, false);


			///#endregion 流程信息.

		map.AddTBAtParas(3999); //参数.

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 轨迹
	*/
	public Track()  {
	}
	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getMyPK()) == true)
		{
			this.setMyPK(DBAccess.GenerGUID(0, null, null));
		}
		return super.beforeInsert();
	}

		///#endregion 构造.
}
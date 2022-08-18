package bp.wf.data.admingroup;

import bp.en.*;
import bp.wf.data.*;

/** 
 时效考核
*/
public class CHExt extends EntityMyPK
{

		///#region 基本属性
	/** 
	 考核状态
	*/
	public final CHSta getCHSta() throws Exception {
		return CHSta.forValue(this.GetValIntByKey(CHAttr.CHSta));
	}
	public final void setCHSta(CHSta value)  throws Exception
	 {
		this.SetValByKey(CHAttr.CHSta, value.getValue());
	}
	/** 
	 时间到
	*/
	public final String getDTTo() throws Exception
	{
		return this.GetValStringByKey(CHAttr.DTTo);
	}
	public final void setDTTo(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.DTTo, value);
	}
	/** 
	 时间从
	*/
	public final String getDTFrom() throws Exception
	{
		return this.GetValStringByKey(CHAttr.DTFrom);
	}
	public final void setDTFrom(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.DTFrom, value);
	}
	/** 
	 应完成日期
	*/
	public final String getSDT() throws Exception
	{
		return this.GetValStringByKey(CHAttr.SDT);
	}
	public final void setSDT(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.SDT, value);
	}
	/** 
	 流程标题
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(CHAttr.Title);
	}
	public final void setTitle(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.Title, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_Flow, value);
	}
	/** 
	 流程
	*/
	public final String getFK_FlowT() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_FlowT);
	}
	public final void setFK_FlowT(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_FlowT, value);
	}
	/** 
	 限期
	*/
	public final int getTimeLimit() throws Exception
	{
		return this.GetValIntByKey(CHAttr.TimeLimit);
	}
	public final void setTimeLimit(int value)  throws Exception
	 {
		this.SetValByKey(CHAttr.TimeLimit, value);
	}
	/** 
	 操作人员
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_Emp, value);
	}
	/** 
	 人员
	*/
	public final String getFK_EmpT() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_EmpT);
	}
	public final void setFK_EmpT(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_EmpT, value);
	}
	/** 
	 部门
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_Dept, value);
	}
	/** 
	 部门名称
	*/
	public final String getFK_DeptT() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_DeptT);
	}
	public final void setFK_DeptT(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_DeptT, value);
	}
	/** 
	 年月
	*/
	public final String getFkNy() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_NY);
	}
	public final void setFk_Ny(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_NY, value);
	}
	/** 
	 工作ID
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(CHAttr.WorkID);
	}
	public final void setWorkID(long value)  throws Exception
	 {
		this.SetValByKey(CHAttr.WorkID, value);
	}
	/** 
	 流程ID
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(CHAttr.FID);
	}
	public final void setFID(long value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FID, value);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(CHAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_Node, value);
	}
	/** 
	 节点名称
	*/
	public final String getFK_NodeT() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_NodeT);
	}
	public final void setFK_NodeT(String value)  throws Exception
	 {
		this.SetValByKey(CHAttr.FK_NodeT, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = false;
		uac.IsView = true;
		return uac;
	}
	/** 
	 时效考核
	*/
	public CHExt()  {
	}
	/** 
	 
	 
	 param pk
	*/
	public CHExt(String pk) throws Exception {
		super(pk);
	}

		///#endregion


		///#region Map
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_CH", "时效考核");
		map.AddMyPK(true);
		map.AddDDLEntities(CHAttr.OrgNo, null, "组织", new bp.wf.port.admingroup.Orgs(), false);
		map.AddTBString(CHAttr.FK_DeptT, null, "部门", true, true, 0, 900, 5, true);
			//map.AddDDLEntities(CHAttr.FK_Dept, null, "隶属部门", new bp.port.Depts(), false);
		   // map.AddDDLEntities(CHAttr.FK_Emp, null, "当事人", new bp.port.Emps(), false);
		   // map.AddTBString(CHAttr.Title, null, "标题", true, true, 0, 900, 5, true);
		map.AddTBString(CHAttr.FK_EmpT, null, "当事人", true, true, 0, 900, 5, true);

		map.AddTBString(CHAttr.Title, null, "标题", true, true, 0, 900, 5, true);
		map.AddTBString(CHAttr.FK_Flow, null, "流程编号", true, true, 0, 900, 5, false);
		map.AddTBString(CHAttr.FK_FlowT, null, "名称", true, true, 0, 900, 5, false);
		map.AddTBString(CHAttr.FK_NodeT, null, "节点名称", true, true, 0, 50, 5);

		map.AddTBString(CHAttr.DTFrom, null, "时间从", true, true, 0, 50, 5);
		map.AddTBString(CHAttr.DTTo, null, "到", true, true, 0, 50, 5);
		map.AddTBString(CHAttr.SDT, null, "应完成日期", true, true, 0, 50, 5);

		map.AddTBString(CHAttr.TimeLimit, null, "限期", true, true, 0, 50, 5);

		map.AddTBFloat(CHAttr.UseDays, 0, "实际使用天", false, true);
		map.AddTBFloat(CHAttr.OverDays, 0, "逾期天", false, true);

		map.AddDDLSysEnum(CHAttr.CHSta, 0, "状态", true, true, CHAttr.CHSta, "@0=及时完成@1=按期完成@2=逾期完成@3=超期完成");

		map.AddTBString(CHAttr.FK_NY, null, "月份", true, true, 0, 50, 5);
		map.AddTBInt(CHAttr.WorkID, 0, "工作ID", true, true);

			//查询条件.
		map.AddSearchAttr(CHAttr.CHSta, 130);
		map.AddSearchAttr(CHAttr.OrgNo, 130);

			//方法.
		RefMethod rm = new RefMethod();
		rm.Title = "详情";
		rm.ClassMethodName = this.toString() + ".DoOpen";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	public final String DoOpen() throws Exception {
		return "../../MyView.htm?FK_Flow" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&OID=" + this.getWorkID();
	}
}
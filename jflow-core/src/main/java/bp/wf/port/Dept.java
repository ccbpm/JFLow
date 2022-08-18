package bp.wf.port;

import bp.en.*;

/** 
 部门
*/
public class Dept extends EntityNoName
{

		///#region 属性
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(DeptAttr.Idx);
	}
	public final void setIdx(int value)  throws Exception
	 {
		this.SetValByKey(DeptAttr.Idx, value);
	}
	/** 
	 父节点编号
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value)  throws Exception
	 {
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(DeptAttr.OrgNo, value);
	}
	public final String getLeader() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.Leader);
	}
	public final void setLeader(String value)  throws Exception
	 {
		this.SetValByKey(DeptAttr.Leader, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 部门
	*/
	public Dept()  {
	}
	/** 
	 部门
	 
	 param no 编号
	*/
	public Dept(String no)
	{
		super(no);
	}

		///#endregion


		///#region 重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Dept", "部门");

		map.setAdjunctType(AdjunctType.None);

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null,"名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		map.AddTBString(DeptAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 250);

		map.AddTBString(DeptAttr.Leader, null, "Leader", true, false, 0, 50, 250);
		map.AddTBInt(DeptAttr.Idx, 0, "Leader", true, false);

			//map.AddTBString(DeptAttr.FK_Unit, "1", "隶属单位", false, false, 0, 50, 10);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}
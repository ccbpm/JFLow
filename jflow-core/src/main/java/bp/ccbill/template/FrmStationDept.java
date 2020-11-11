package bp.ccbill.template;

import bp.en.*;
import bp.en.Map;

/** 
 单据查询岗位
 单据查询岗位有两部分组成.	 
*/
public class FrmStationDept extends EntityMM
{
	private static final long serialVersionUID = 1L;
		///基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	单据
	 * @throws Exception 
	*/
	public final String getFK_Frm() throws Exception
	{
		return this.GetValStringByKey(FrmStationDeptAttr.FK_Frm);
	}
	public final void setFK_Frm(String value) throws Exception
	{
		this.SetValByKey(FrmStationDeptAttr.FK_Frm, value);
	}

	/** 
	 工作岗位
	 * @throws Exception 
	*/
	public final String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(FrmStationDeptAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		this.SetValByKey(FrmStationDeptAttr.FK_Station, value);
	}
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(FrmStationDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		this.SetValByKey(FrmStationDeptAttr.FK_Dept, value);
	}


		///


		///构造方法
	/** 
	 单据工作岗位
	*/
	public FrmStationDept()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Frm_StationDept", "单据岗位部门");

		map.AddTBStringPK(FrmStationDeptAttr.FK_Frm, null, "单据编号", false, false, 1, 190, 20);

		map.AddDDLEntitiesPK(FrmStationDeptAttr.FK_Station, null, "工作岗位", new bp.port.Stations(), true);

		map.AddDDLEntitiesPK(FrmStationDeptAttr.FK_Dept, null, "部门", new bp.gpm.Depts(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}



		///

}
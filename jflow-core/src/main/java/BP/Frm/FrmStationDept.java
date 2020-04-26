package BP.Frm;

import BP.En.Map;
import BP.En.UAC;

public class FrmStationDept extends BP.En.EntityMM{
    //#region 基本属性
    /// <summary>
    /// UI界面上的访问控制
    /// </summary>
	@Override
    public UAC getHisUAC()
    {
        
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
        
    }

    /// <summary>
    ///单据
    /// </summary>
    public String getFK_Frm() throws Exception
    {

		return this.GetValStringByKey(FrmStationDeptAttr.FK_Frm);
        
    }
	public final void setFK_Frm(String value) throws Exception
	{
		this.SetValByKey(FrmStationDeptAttr.FK_Frm, value);
	}

    /// <summary>
    /// 工作岗位
    /// </summary>
	public String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(FrmStationDeptAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		this.SetValByKey(FrmStationDeptAttr.FK_Station, value);
	}
    public String getFK_Dept() throws Exception
    {
    	return this.GetValStringByKey(FrmStationDeptAttr.FK_Dept);
    }
    public final void setFK_Dept(String value) throws Exception
    {
    	this.SetValByKey(FrmStationDeptAttr.FK_Dept, value);
    }
    
   

        //#endregion

        //#region 构造方法
    /// <summary>
    /// 单据工作岗位
    /// </summary>
    public FrmStationDept() { }
    /// <summary>
    /// 重写基类方法
    /// </summary>
	@Override
    public Map getEnMap()
    {
        
			if (this.get_enMap() != null)
			{
				return this.get_enMap();
			}
            Map map = new Map("Frm_StationDept", "单据岗位部门");

            map.AddTBStringPK(FrmStationDeptAttr.FK_Frm, null, "单据编号", false, false, 1, 190, 20);

            map.AddDDLEntitiesPK(FrmStationDeptAttr.FK_Station, null, "工作岗位", new BP.Port.Stations(), true);

            map.AddDDLEntitiesPK(FrmStationDeptAttr.FK_Dept, null, "部门", new BP.GPM.Depts(), true);

            this.set_enMap(map);
			return this.get_enMap();
    }

     
        //#endregion




}

package BP.Port;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/**
 * 岗位
 */
public class Station extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 实现基本的方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	public String FK_StationType;
	
	public String getFK_StationType()
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	
	public void setFK_StationType(String fK_StationType)
	{
		this.SetValByKey(StationAttr.FK_StationType, fK_StationType);
	}
	
	// {
	// get
	// {
	// return this.GetValStrByKey(StationAttr.FK_StationType);
	// }
	// set
	// {
	// this.SetValByKey(StationAttr.FK_StationType, value);
	// }
	// }
	public String getName()
	{
		return this.GetValStrByKey("Name");
	}
	
	public final int getGrade()
	{
		return this.getNo().length() / 2;
	}
	 
	// 构造方法
	/**
	 * 岗位
	 */
	public Station()
	{
	}
	
	/**
	 * 岗位
	 * 
	 * @param _No
	 */
	public Station(String _No)
	{
		super(_No);
	}
	
	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("Port_Station");
		map.setEnDesc("岗位"); // "岗位";
		map.setEnType(EnType.Admin);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.Application);
		map.setCodeStruct("2222222"); // 最大级别是 7.
		
		map.AddTBStringPK(StationAttr.No, null, "编号", true, false, 1, 20, 100);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 0, 100, 100);
		map.AddTBString(StationAttr.OrgNo, "0", "组织", true, false, 0, 100, 100);
		 
		/*map.AddDDLSysEnum(StationAttr.StaGrade, 0, "类型", true, true,
				StationAttr.StaGrade, "@1=高层岗@2=中层岗@3=执行岗");*/
		
		map.getAttrsOfOneVSM().Add(new EmpStations(), new Emps(),
				EmpStationAttr.FK_Station, EmpStationAttr.FK_Emp,
				DeptAttr.Name, DeptAttr.No, "人员");
		
		// 不带有参数的方法.
		// xiaozhoupeng 注释，原因不需要注销岗位 start
		// RefMethod rm = new RefMethod();
		// rm.Title = "注销岗位";
		// rm.Warning= "是否确认注销岗位？";
		// rm.ClassMethodName = this.toString() + ".DoZhuXiao";
		// map.AddRefMethod(rm);
		// xiaozhoupeng 注释，原因不需要注销岗位 start
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/**
	 * 无参数的方法:注销岗位 说明：都要返回string类型.
	 * 
	 * @return
	 */
	public final void DoZhuXiao()
	{
		this.Delete();
	}
}
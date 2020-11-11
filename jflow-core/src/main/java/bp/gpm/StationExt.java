package bp.gpm;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.StationAttr;
import bp.port.StationTypes;

/** 
 岗位
*/
public class StationExt extends EntityNoName
{
	private static final long serialVersionUID = 1L;
	///属性
	public final String getFK_StationExtType()throws Exception
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationExtType(String value) throws Exception
	{
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

		///


		///实现基本的方方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

	/** 
	 岗位
	*/
	public StationExt()
	{
	}
	/** 
	 岗位
	 
	 @param _No
	 * @throws Exception 
	*/
	public StationExt(String _No) throws Exception
	{
		super(_No);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Station","岗位");

		map.setDepositaryOfMap(Depositary.Application);
		map.setCodeStruct("4");

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 4, 4, 36);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 0, 100, 200);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "类型", new StationTypes(), true);
		map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 250);
		map.AddSearchAttr(StationAttr.FK_StationType);

			//岗位绑定菜单
		map.getAttrsOfOneVSM().AddBranches(new StationMenus(), new bp.gpm.Menus(), bp.gpm.StationMenuAttr.FK_Station, bp.gpm.StationMenuAttr.FK_Menu, "绑定菜单", EmpAttr.Name, EmpAttr.No, "0");


		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}
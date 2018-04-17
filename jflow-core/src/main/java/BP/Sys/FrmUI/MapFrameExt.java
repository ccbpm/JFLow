package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;

/** 
 框架
 
*/
public class MapFrameExt extends EntityMyPK
{
  
		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 框架
	 
	*/
	public MapFrameExt()
	{

	}
	/** 
	 框架
	 
	 @param mypk
	*/
	public MapFrameExt(String mypk)
	{
		this.setMyPK( mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FrmEle", "框架");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(FrmEleAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
		map.AddTBString(FrmEleAttr.URL, null, "URL(支持ccbpm的表达式)", true, false, 0, 3900, 20, true);
		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "所在分组", "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'", true);

		this.set_enMap( map);
		return map;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
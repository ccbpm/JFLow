package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
  用户组类型
*/
public class TeamType extends EntityNoName
{
		///#region 属性
		///#endregion

		///#region 实现基本的方方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
		///#endregion

		///#region 构造方法
	/** 
	 用户组类型
	*/
	public TeamType()
	{
	}
	/** 
	 用户组类型
	 
	 @param _No
	*/
	public TeamType(String _No) throws Exception
	{
		super(_No);
	}
		///#endregion

	/** 
	 用户组类型Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_TeamType", "用户组类型");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(TeamTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(TeamTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(TeamTypeAttr.Idx, 0, "顺序", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
}
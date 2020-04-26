package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
  用户组类型
*/
public class GroupType extends EntityNoName
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
	public GroupType()
	{
	}
	/** 
	 用户组类型
	 
	 @param _No
	*/
	public GroupType(String _No) throws Exception
	{
		super(_No);
	}
		///#endregion

	/** 
	 用户组类型Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupType", "用户组类型");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(GroupTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(GroupTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(GroupTypeAttr.Idx, 0, "顺序", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
}
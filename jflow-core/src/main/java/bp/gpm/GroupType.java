package bp.gpm;

import bp.da.*;
import bp.en.*;
import bp.en.Map;

/**
 * 用户组类型
 */
public class GroupType extends EntityNoName {
	private static final long serialVersionUID = 1L;

	/// 实现基本的方方法
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

	///

	/// 构造方法
	/**
	 * 用户组类型
	 */
	public GroupType() {
	}

	/**
	 * 用户组类型
	 * 
	 * @param _No
	 * @throws Exception
	 */
	public GroupType(String _No) throws Exception {
		super(_No);
	}

	///

	/**
	 * 用户组类型Map
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}
		Map map = new Map("GPM_GroupType", "用户组类型");
		map.setCodeStruct("2");

		map.setDepositaryOfEntity(Depositary.Application);
		map.setDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(GroupTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(GroupTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(GroupTypeAttr.Idx, 0, "顺序", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
}
package bp.port;

import bp.en.*;
import bp.en.Map;

/**
 * 用户组类型
 */
public class TeamType extends EntityNoName {

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
	public TeamType() {
	}

	/**
	 * 用户组类型
	 * 
	 * @param _No
	 * @throws Exception 
	 */
	public TeamType(String _No) throws Exception {
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
		Map map = new Map("Port_TeamType", "用户组类型");
		map.setCodeStruct("2");

		map.AddTBStringPK(TeamTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(TeamTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(TeamTypeAttr.Idx, 0, "顺序", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
}
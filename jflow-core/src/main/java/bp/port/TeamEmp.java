package bp.port;

import bp.da.*;
import bp.en.*;
import bp.en.Map;

/**
 * 用户组人员
 */
public class TeamEmp extends EntityMM {

	private static final long serialVersionUID = 1L;

	/// 属性
	public final String getFK_Emp()throws Exception {
		return this.GetValStringByKey(TeamEmpAttr.FK_Emp);
	}

	public final void setFK_Emp(String value) throws Exception{
		this.SetValByKey(TeamEmpAttr.FK_Emp, value);
	}

	public final String getFK_Team()throws Exception {
		return this.GetValStringByKey(TeamEmpAttr.FK_Team);
	}

	public final void setFK_Team(String value) throws Exception{
		this.SetValByKey(TeamEmpAttr.FK_Team, value);
	}

	///

	/// 构造方法
	/**
	 * 用户组人员
	 */
	public TeamEmp() {
	}

	/**
	 * 用户组人员
	 * 
	 * @param mypk
	 * @throws Exception 
	 */
	public TeamEmp(String no) throws Exception {
		this.Retrieve();
	}

	/**
	 * 用户组人员
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}
		Map map = new Map("Port_TeamEmp");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("用户组人员");
		map.setEnType(EnType.App);

		map.AddTBStringPK(TeamEmpAttr.FK_Team, null, "用户组", true, false, 0, 50, 20);
		map.AddDDLEntitiesPK(TeamEmpAttr.FK_Emp, null, "人员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

	///
}
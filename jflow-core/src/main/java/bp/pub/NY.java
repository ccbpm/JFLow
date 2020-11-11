package bp.pub;

import bp.da.DBUrl;
import bp.da.DBUrlType;
import bp.da.Depositary;
import bp.en.*;

/**
 * 年月
 */
public class NY extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NY()
	{
	}
	
	public NY(String _No) throws Exception
	{
		super(_No);
	}

	@Override
	public Map getEnMap() throws Exception {

		if (this.get_enMap() != null) {
			return this.get_enMap();
		}
		Map map = new Map("Pub_NY", "年月");

		//#region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
		map.setAdjunctType(AdjunctType.AllType);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsCheckNoLength(false);
		map.setEnType(EnType.App);
		map.setCodeStruct("4");
		//#endregion

		//#region 字段
		map.AddTBStringPK(EntityNoNameAttr.No, null, "编号", true, false, 0, 50, 50);
		map.AddTBString(EntityNoNameAttr.Name, null, "名称", true, false, 0, 50, 200);
		//#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}
}
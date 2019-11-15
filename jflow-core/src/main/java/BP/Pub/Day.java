package BP.Pub;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.*;

public class Day extends EntityNoName
{
	// 构造方法
	public Day()
	{
	}
	
	/**
	 * _No
	 * 
	 * @param _No
	 * @throws Exception 
	 */
	public Day(String _No) throws Exception
	{
		super(_No);
	}

	@Override
	public Map getEnMap() {

		if (this.get_enMap() != null) {
			return this.get_enMap();
		}
		Map map = new Map("Pub_ND", "年度");

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
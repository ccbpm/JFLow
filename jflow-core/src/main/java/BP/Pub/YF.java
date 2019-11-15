package BP.Pub;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.*;

/**
 * 月份
 */
public class YF extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 实现基本的方方法
	
	/**
	 * 物理表
	 */
	public String getPhysicsTable()
	{
		return "Pub_YF";
	}
	
	/**
	 * 描述
	 */
	public String getDesc()
	{
		return "月份"; // "月份";
	}
	
	// 构造方法
	public YF()
	{
	}
	
	/**
	 * _No
	 * 
	 * @param _No
	 * @throws Exception 
	 */
	public YF(String _No) throws Exception
	{
		super(_No);
	}

	/// <summary>
	/// Map
	/// </summary>
	@Override
	public Map getEnMap() {

		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Pub_YF", "月份");

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


		this.set_enMap(map);
		return this.get_enMap();
	}
}
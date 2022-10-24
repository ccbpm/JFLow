package bp.pub;

import bp.da.*;
import bp.en.*;
import bp.en.Map;

/** 
 年月
*/
public class NY extends EntityNoName
{

		///#region 基本属性

		///#endregion


		///#region 构造函数
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 年月
	*/
	public NY()
	{
	}
	public NY(String no) throws Exception 
	{
		super(no);
	}


	/** 
	 Map
	*/
	@Override
	public bp.en.Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Pub_NY", "年月");


			///#region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
		map.setAdjunctType(AdjunctType.AllType);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsCheckNoLength(false);
		map.setEnType(EnType.App);
		map.setCodeStruct("4");

			///#endregion


			///#region 字段
		map.AddTBStringPK(NYAttr.No, null, "编号", true, false, 0, 50, 50);
		map.AddTBString(NYAttr.Name, null, "名称", true, false, 0, 50, 200);

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	public Entities getGetNewEntities()
	{
		return new NYs();
	}

		///#endregion
}
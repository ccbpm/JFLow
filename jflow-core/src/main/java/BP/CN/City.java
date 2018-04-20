package BP.CN;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.AdjunctType;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

public class City extends EntityNoName
{
	// 基本属性
	public final String getNames()
	{
		return this.GetValStrByKey(CityAttr.Names);
	}
	
	public final String getFK_PQ()
	{
		return this.GetValStrByKey(CityAttr.FK_PQ);
	}
	
	public final String getFK_SF()
	{
		return this.GetValStrByKey(CityAttr.FK_SF);
	}
	
	// 构造函数
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * 城市
	 */
	public City()
	{
	}
	
	public City(String no) throws Exception
	{
		super(no);
	}
	
	/**
	 * Map
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map();
		
		// 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
		map.setPhysicsTable("CN_City");
		map.setAdjunctType(AdjunctType.AllType);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsCheckNoLength(false);
		map.setEnDesc("城市");
		map.setEnType(EnType.App);
		map.setCodeStruct("4");
		
		// 字段
		map.AddTBStringPK(CityAttr.No, null, "编号", true, false, 0, 50, 50);
		map.AddTBString(CityAttr.Name, null, "名称", true, false, 0, 50, 200);
		map.AddTBString(CityAttr.Names, null, "小名", true, false, 0, 50, 200);
		map.AddTBInt(CityAttr.Grade, 0, "Grade", false, false);
		
		map.AddDDLEntities(CityAttr.FK_SF, null, "省份", new SFs(), true);
		map.AddDDLEntities(CityAttr.FK_PQ, null, "片区", new PQs(), true);
		
		map.AddSearchAttr(CityAttr.FK_SF);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}

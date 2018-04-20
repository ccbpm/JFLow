package BP.CN;

import BP.DA.DBAccess;
import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.AdjunctType;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/**
 * 城市编码
 */
public class Area extends EntityNoName
{
	// 基本属性
	public final String getNames()
	{
		return this.GetValStrByKey(CityAttr.Names);
	}
	
	public final String getFK_PQ()
	{
		return this.GetValStrByKey(AreaAttr.FK_PQ);
	}
	
	public final String getFK_SF()
	{
		return this.GetValStrByKey(AreaAttr.FK_SF);
	}
	
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * 城市编码
	 */
	public Area()
	{
	}
	
	public Area(String no) throws Exception
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
		map.setPhysicsTable("CN_Area");
		map.setAdjunctType(AdjunctType.AllType);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsCheckNoLength(false);
		map.setEnDesc("城市编码");
		map.setEnType(EnType.App);
		map.setCodeStruct("4");
		
		// 字段
		map.AddTBStringPK(AreaAttr.No, null, "编号", true, false, 0, 50, 50);
		map.AddTBString(AreaAttr.Name, null, "名称", true, false, 0, 50, 200);
		map.AddTBString(AreaAttr.Names, null, "小名", true, false, 0, 50, 200);
		map.AddTBInt(AreaAttr.Grade, 0, "Grade", false, false);
		
		map.AddDDLEntities(AreaAttr.FK_SF, null, "省份", new SFs(), true);
		map.AddDDLEntities(AreaAttr.FK_PQ, null, "片区", new PQs(), true);
		
		map.AddSearchAttr(AreaAttr.FK_SF);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	public static String GenerAreaNoByName(String name1, String name2,
			String oldcity)
	{
		String fk_city1 = BP.CN.Area.GenerAreaNoByName(name1, "");
		String fk_city2 = BP.CN.Area.GenerAreaNoByName(name2, "");
		String fk_city = null;
		
		if (fk_city1.length() >= 4)
		{
			fk_city = fk_city1;
		}
		
		if (fk_city1.length() == 2)
		{
			if (fk_city2.contains(fk_city1))
			{
				fk_city = fk_city2;
			} else
			{
				fk_city = fk_city1;
			}
		}
		return fk_city;
	}
	
	public static String GenerAreaNoByName(String name, String oldcity)
	{
		// 进行模糊匹配地区，先找区县。
		String sql = "SELECT NO FROM CN_Area WHERE indexof('" + name
				+ "', names ) >0 ORDER BY GRADE DESC ";
		String val = DBAccess.RunSQLReturnString(sql);
		if (val != null)
		{
			return val;
		} else
		{
			return oldcity;
		}
	}
}

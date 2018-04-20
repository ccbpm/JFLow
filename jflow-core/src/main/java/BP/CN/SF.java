package BP.CN;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.AdjunctType;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/**
 * 省份
 */
public class SF extends EntityNoName
{
	// 基本属性
	/**
	 * 片区编号
	 */
	public final String getFK_PQ()
	{
		return this.GetValStrByKey(SFAttr.FK_PQ);
	}
	
	public final void setFK_PQ(String value)
	{
		this.SetValByKey(SFAttr.FK_PQ, value);
	}
	
	/**
	 * 片区名称
	 */
	public final String getFK_PQT()
	{
		return this.GetValRefTextByKey(SFAttr.FK_PQ);
	}
	
	/**
	 * 小名称
	 */
	public final String getNames()
	{
		return this.GetValStrByKey(SFAttr.Names);
	}
	
	public final void setNames(String value)
	{
		this.SetValByKey(SFAttr.Names, value);
	}
	
	/**
	 * 简称
	 */
	public final String getJC()
	{
		return this.GetValStrByKey(SFAttr.JC);
	}
	
	// 构造函数
	/**
	 * 访问权限.
	 * @throws Exception 
	 */
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * 省份
	 */
	public SF()
	{
	}
	
	/**
	 * 省份
	 * 
	 * @param no
	 * @throws Exception 
	 */
	public SF(String no) throws Exception
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
		map.setPhysicsTable("CN_SF");
		map.setAdjunctType(AdjunctType.AllType);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsCheckNoLength(false);
		map.setEnDesc("省份");
		map.setEnType(EnType.App);
		map.setCodeStruct("4");
		
		// 字段
		map.AddTBStringPK(SFAttr.No, null, "编号", true, false, 2, 2, 2);
		map.AddTBString(SFAttr.Name, null, "名称", true, false, 0, 50, 200);
		map.AddTBString(SFAttr.Names, null, "小名称", true, false, 0, 50, 200);
		map.AddTBString(SFAttr.JC, null, "简称", true, false, 0, 50, 200);
		map.AddDDLEntities(SFAttr.FK_PQ, null, "片区", new PQs(), true);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}

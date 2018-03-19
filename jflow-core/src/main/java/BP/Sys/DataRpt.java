package BP.Sys;

import java.math.BigDecimal;

import BP.DA.Depositary;
import BP.En.EntityMyPK;
import BP.En.Map;

/**
 * 报表数据存储模版
 */
public class DataRpt extends EntityMyPK
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//  构造方法
	/**
	 * 报表数据存储模版
	 */
	public DataRpt()
	{
	}
	
	/**
	 * 报表数据存储模版
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_DataRpt");
		map.setEnDesc("报表数据存储模版");
		map.setDepositaryOfMap(Depositary.Application);
		
		map.AddMyPK();
		map.AddTBString(DataRptAttr.ColCount, null, "列", true, true, 0, 50, 20);
		map.AddTBString(DataRptAttr.RowCount, null, "行", true, true, 0, 50, 20);
		map.AddTBDecimal(DataRptAttr.Val, 0, "值", true, false);
		map.AddTBDecimal(DataRptAttr.RefOID, 0, "关联的值", true, false);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}
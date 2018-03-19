package BP.WF.Data;

import BP.DA.Depositary;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.SimpleNoNameAttr;
import BP.En.UAC;

/**
 * 单据类型
 */
public class BillType extends EntityNoName
{
	// 属性.
	/**
	 * 流程编号
	 */
	public final String getFK_Flow()
	{
		return this.GetValStrByKey("FK_Flow");
	}
	
	public final void setFK_Flow(String value)
	{
		this.SetValByKey("FK_Flow", value);
	}
	
	/**
	 * UI界面上的访问控制
	 */
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	// 构造方法
	/**
	 * 单据类型
	 */
	public BillType()
	{
	}
	
	/**
	 * 单据类型
	 * 
	 * @param _No
	 */
	public BillType(String _No)
	{
		super(_No);
	}
	
	/**
	 * 单据类型Map
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_BillType");
		map.setEnDesc("单据类型");
		map.setCodeStruct("2");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setIsAutoGenerNo(true);
		
		map.AddTBStringPK(SimpleNoNameAttr.No, null, "编号", true, true, 2, 2, 2);
		map.AddTBString(SimpleNoNameAttr.Name, null, "名称", true, false, 1, 50,
				50);
		map.AddTBString("FK_Flow", null, "流程", true, false, 1, 50, 50);
		
		map.AddTBInt("IDX", 0, "IDX", false, false);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
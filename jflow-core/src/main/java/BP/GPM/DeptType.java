package BP.GPM;

import BP.DA.Depositary;
import BP.En.EntityNoName;
import BP.En.Map;

/** 
  部门类型
*/
public class DeptType extends EntityNoName
{

	/** 
	 部门类型
	*/
	public DeptType()
	{
	}
	/** 
	 部门类型
	 @param _No
	*/
	public DeptType(String _No)
	{
		super(_No);
	}

	/** 
	 部门类型Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_DeptType");
		map.setEnDesc("部门类型");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(DeptTypeAttr.No, null, "编号", true, true, 2, 2, 2);
		map.AddTBString(DeptTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
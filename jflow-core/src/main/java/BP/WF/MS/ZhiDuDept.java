package BP.WF.MS;

import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;

/** 
 制度
*/
public class ZhiDuDept extends EntityNoName
{
		
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(ZhiDuDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(ZhiDuDeptAttr.FK_Dept, value);
	}
	/** 
	 制度编号
	*/
	public final String getZDNo()
	{
		return this.GetValStringByKey(ZhiDuDeptAttr.ZDNo);
	}
	public final void setZDNo(String value)
	{
		this.SetValByKey(ZhiDuDeptAttr.ZDNo, value);
	}
	/** 
	 最大值
	*/
	public final int getZDMax()
	{
		return this.GetValIntByKey(ZhiDuDeptAttr.ZDMax);
	}
	public final void setZDMax(int value)
	{
		this.SetValByKey(ZhiDuDeptAttr.ZDMax, value);
	}

		///#endregion

		
	/** 
	 Main
	*/
	public ZhiDuDept()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("MS_ZhiDuDept", "制度");
		map.Java_SetEnType(EnType.Admin);

		map.AddTBStringPK(ZhiDuDeptAttr.No, null, "编号", true, true, 5, 5, 5);
		map.AddTBString(ZhiDuDeptAttr.Name, null, "名称", true, true, 0, 200, 5);
		map.AddTBString(ZhiDuDeptAttr.FK_Dept, null, "部门", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuDeptAttr.ZDMax, null, "最大值", true, true, 0, 400, 4);
		map.AddTBString(ZhiDuDeptAttr.ZDNo, null, "制度编号", true, true, 0, 200, 4);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}
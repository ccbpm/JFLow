package bp.wf.port;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.web.*;
import bp.wf.*;
import java.util.*;

/** 
 部门
*/
public class Dept extends EntityNoName
{

		///属性
	/** 
	 父节点编号
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}

 
	/** 
	 部门
	*/
	public Dept()
	{
	}
	/** 
	 部门
	 
	 @param no 编号
	*/
	public Dept(String no) throws Exception
	{
		super(no);
	}

		///


		///重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Dept", "部门");

		map.setAdjunctType(AdjunctType.None);

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null,"名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		map.AddTBString(DeptAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 250);

			//map.AddTBString(DeptAttr.FK_Unit, "1", "隶属单位", false, false, 0, 50, 10);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}
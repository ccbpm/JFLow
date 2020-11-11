package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 这里存放每个接受人规则的信息.	 
*/
public class AccepterRole extends EntityOID
{

		///基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 节点编号
	 * @throws Exception 
	*/
	public final String getFK_Node() throws Exception
	{
		return this.GetValStringByKey(AccepterRoleAttr.FK_Node);
	}
	public final void setFK_Node(String value) throws Exception
	{
		SetValByKey(AccepterRoleAttr.FK_Node, value);
	}

		///


		///构造函数
	/** 
	 接受人规则
	*/
	public AccepterRole()
	{
	}
	/** 
	 接受人规则
	 
	 @param oid 接受人规则ID	
	 * @throws Exception 
	*/
	public AccepterRole(int oid) throws Exception
	{
		this.setOID(oid);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_AccepterRole", "接受人规则");

		map.AddTBIntPKOID();

		map.AddTBString(AccepterRoleAttr.Name, null, null, true, false, 0, 200, 10, true);
		map.AddTBString(AccepterRoleAttr.FK_Node, null, "节点", false, true, 0, 100, 10);
		map.AddTBInt(AccepterRoleAttr.FK_Mode, 0, "模式类型", false, true);

		map.AddTBString(AccepterRoleAttr.Tag0, null, "Tag0", false, true, 0, 999, 10);
		map.AddTBString(AccepterRoleAttr.Tag1, null, "Tag1", false, true, 0, 999, 10);
		map.AddTBString(AccepterRoleAttr.Tag2, null, "Tag2", false, true, 0, 999, 10);
		map.AddTBString(AccepterRoleAttr.Tag3, null, "Tag3", false, true, 0, 999, 10);
		map.AddTBString(AccepterRoleAttr.Tag4, null, "Tag4", false, true, 0, 999, 10);
		map.AddTBString(AccepterRoleAttr.Tag5, null, "Tag5", false, true, 0, 999, 10);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

}
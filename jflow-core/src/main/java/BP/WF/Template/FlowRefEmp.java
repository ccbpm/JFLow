package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程关联人员.	 
*/
public class FlowRefEmp extends EntityMyPK
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 流程关联人员的事务编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.FK_Flow, value);
	}
	/** 
	 类型
	 * @throws Exception 
	*/
	public final String getFlowRefEmpType() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.FlowRefEmpType);
	}
	public final void setFlowRefEmpType(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.FlowRefEmpType, value);
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(FlowRefEmpAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.FK_Node, value);
	}
	/** 
	 字段存储0
	 * @throws Exception 
	*/
	public final String getTag0() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag0);
	}
	public final void setTag0(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag0, value);
	}
	/** 
	 字段存储1
	 * @throws Exception 
	*/
	public final String getTag1() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag1);
	}
	public final void setTag1(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag1, value);
	}
	/** 
	 字段存储2
	 * @throws Exception 
	*/
	public final String getTag2() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag2);
	}
	public final void setTag2(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag2, value);
	}
	/** 
	 字段存储3
	 * @throws Exception 
	*/
	public final String getTag3() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag3);
	}
	public final void setTag3(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag3, value);
	}
	/** 
	 字段存储4
	 * @throws Exception 
	*/
	public final String getTag4() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag4);
	}
	public final void setTag4(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag4, value);
	}
	/** 
	 字段存储5
	 * @throws Exception 
	*/
	public final String getTag5() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag5);
	}
	public final void setTag5(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag5, value);
	}
	/** 
	 字段存储6
	 * @throws Exception 
	*/
	public final String getTag6() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag6);
	}
	public final void setTag6(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag6, value);
	}
	/** 
	 字段存储7
	 * @throws Exception 
	*/
	public final String getTag7() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag7);
	}
	public final void setTag7(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag7, value);
	}
	/** 
	 字段存储8
	 * @throws Exception 
	*/
	public final String getTag8() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag8);
	}
	public final void setTag8(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag8, value);
	}
	/** 
	 字段存储9
	 * @throws Exception 
	*/
	public final String getTag9() throws Exception
	{
		return this.GetValStringByKey(FlowRefEmpAttr.Tag9);
	}
	public final void setTag9(String value) throws Exception
	{
		SetValByKey(FlowRefEmpAttr.Tag9, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 流程关联人员
	*/
	public FlowRefEmp()
	{
	}
	/** 
	 流程关联人员
	 
	 @param _oid 流程关联人员ID	
	 * @throws Exception 
	*/
	public FlowRefEmp(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
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

		Map map = new Map("WF_Part", "流程关联人员");

		map.AddMyPK();

		map.AddTBString(PartAttr.FK_Flow, null, "流程编号", false, true, 0, 100, 10);
		map.AddTBInt(PartAttr.FK_Node, 0, "节点ID", false, false);
		map.AddTBString(PartAttr.PartType, null, "类型", false, true, 0, 100, 10);

		map.AddTBString(FlowRefEmpAttr.Tag0, null, "Tag0", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag1, null, "Tag1", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag2, null, "Tag2", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag3, null, "Tag3", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag4, null, "Tag4", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag5, null, "Tag5", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag6, null, "Tag6", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag7, null, "Tag7", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag8, null, "Tag8", false, true, 0, 2000, 10);
		map.AddTBString(FlowRefEmpAttr.Tag9, null, "Tag9", false, true, 0, 2000, 10);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}
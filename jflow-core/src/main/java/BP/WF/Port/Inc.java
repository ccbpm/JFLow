package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 独立组织
*/
public class Inc extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 父节点编号
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(IncAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(IncAttr.ParentNo, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 独立组织
	*/
	public Inc()
	{
	}
	/** 
	 独立组织
	 
	 @param no 编号
	 * @throws Exception 
	*/
	public Inc(String no) throws Exception
	{
		super(no);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写方法
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Inc", "独立组织");

		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.setAdjunctType(AdjunctType.None);
		map.setEnType(EnType.View); //独立组织是一个视图.

		map.AddTBStringPK(IncAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(IncAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		map.AddTBString(IncAttr.Name, null,"名称", true, false, 0, 60, 200,true);

		RefMethod rm = new RefMethod();
		rm.Title = "设置二级管理员";
		rm.Warning = "设置为子公司后，系统就会在流程树上分配一个目录节点.";
		rm.ClassMethodName = this.toString() + ".SetSubInc";
		rm.getHisAttrs().AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String SetSubInc(String userNo) throws Exception
	{
		BP.WF.Port.SubInc.Dept dept = new BP.WF.Port.SubInc.Dept(this.getNo());
		return dept.SetSubInc(userNo);
	}

}
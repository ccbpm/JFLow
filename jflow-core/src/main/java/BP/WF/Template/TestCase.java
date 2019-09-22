package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程测试.
*/
public class TestCase extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 流程测试的事务编号
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(TestCaseDtlAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(TestCaseDtlAttr.FK_Node, value);
	}
	public final String getParaType()
	{
		return this.GetValStringByKey(TestCaseDtlAttr.ParaType);
	}
	public final void setParaType(String value)
	{
		SetValByKey(TestCaseDtlAttr.ParaType, value);
	}
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(TestCaseDtlAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(TestCaseDtlAttr.FK_Flow, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 流程测试
	*/
	public TestCase()
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

		Map map = new Map("WF_TestCase", "自定义流程测试");

		map.AddMyPK();
		map.AddTBString(TestCaseDtlAttr.FK_Flow, null, "流程编号", true, false, 0, 100, 100, true);
		map.AddTBString(TestCaseDtlAttr.ParaType, null, "参数类型", true, false, 0, 100, 100, true);
		map.AddTBString(TestCaseDtlAttr.Vals, null, "值s", true, false, 0, 500, 300, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
package BP.WF.Data;

import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 报表
*/
public class MyDeptEmp extends BP.En.EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region attrs - attrs
	public String RptName = null;
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Emp", "流程数据");
		map.Java_SetEnType(EnType.View);

		map.AddTBStringPK(MyDeptEmpAttr.No, null, "编号", false, false, 0, 100, 100);
		map.AddTBString(MyDeptEmpAttr.Name, null, "名称", false, false, 0, 100, 100);
		map.AddTBString(MyDeptEmpAttr.FK_Dept, null, "部门", false, false, 0, 100, 100);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion attrs

}
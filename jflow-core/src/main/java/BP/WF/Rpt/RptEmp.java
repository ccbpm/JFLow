package BP.WF.Rpt;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 RptEmp 的摘要说明。
*/
public class RptEmp extends Entity
{

	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.No.equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 报表ID
	*/
	public final String getFK_Rpt()
	{
		return this.GetValStringByKey(RptEmpAttr.FK_Rpt);
	}
	public final void setFK_Rpt(String value)
	{
		SetValByKey(RptEmpAttr.FK_Rpt, value);
	}
	public final String getFK_EmpT()
	{
		return this.GetValRefTextByKey(RptEmpAttr.FK_Emp);
	}
	/** 
	人员
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(RptEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		SetValByKey(RptEmpAttr.FK_Emp, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 报表人员
	*/
	public RptEmp()
	{
	}
	/** 
	 报表人员对应
	 
	 @param _empoid 报表ID
	 @param wsNo 人员编号 	
	*/
	public RptEmp(String _empoid, String wsNo)
	{
		this.setFK_Rpt(_empoid);
		this.setFK_Emp(wsNo);
		if (this.Retrieve() == 0)
		{
			this.Insert();
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Sys_RptEmp", "报表人员对应信息");
		map.Java_SetEnType(EnType.Dot2Dot);

		map.AddTBStringPK(RptEmpAttr.FK_Rpt, null, "报表", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(RptEmpAttr.FK_Emp, null, "人员", new Emps(), true);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
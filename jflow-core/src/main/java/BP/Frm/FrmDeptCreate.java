package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的部门
 单据的部门有两部分组成.	 
 记录了从一个单据到其他的多个单据.
 也记录了到这个单据的其他的单据.
*/
public class FrmDeptCreate extends EntityMM
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
		uac.OpenAll();
		return uac;
	}
	/** 
	单据
	*/
	public final String getFrmID()
	{
		return this.GetValStringByKey(FrmDeptCreateAttr.FrmID);
	}
	public final void setFrmID(String value)
	{
		this.SetValByKey(FrmDeptCreateAttr.FrmID, value);
	}
	/** 
	 部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(FrmDeptCreateAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(FrmDeptCreateAttr.FK_Dept, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 单据可创建的部门
	*/
	public FrmDeptCreate()
	{
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

		Map map = new Map("Frm_DeptCreate", "单据部门");

		map.AddTBStringPK(FrmDeptCreateAttr.FrmID, null, "表单", true, true, 1, 100, 100);
		map.AddDDLEntitiesPK(FrmDeptCreateAttr.FK_Dept, null, "可以创建部门", new BP.GPM.Depts(), true);
		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}
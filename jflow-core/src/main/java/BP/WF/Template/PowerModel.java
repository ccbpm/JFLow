package BP.WF.Template;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 权限模型
*/
public class PowerModel extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 流程编号
	*/
	public final String getFlowNo()
	{
		return this.GetValStringByKey(PowerModelAttr.FlowNo);
	}
	public final void setFlowNo(String value)
	{
		this.SetValByKey(PowerModelAttr.FlowNo, value);
	}
	/** 
	 权限标记
	*/
	public final String getPowerFlag()
	{
		return this.GetValStringByKey(PowerModelAttr.PowerFlag);
	}
	public final void setPowerFlag(String value)
	{
		this.SetValByKey(PowerModelAttr.PowerFlag, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 权限模型
	*/
	public PowerModel()
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

		Map map = new Map("WF_PowerModel", "权限模型");

		map.AddMyPK();

			//比如： FlowData , FrmData
		map.AddTBString(PowerModelAttr.Model, null, "模块", true, false, 0, 100, 10);

			//权限标记: FlowDataDelete
		map.AddTBString(PowerModelAttr.PowerFlag, null, "权限标识", true, false, 0, 100, 10);
			//权限名称: 流程删除
		map.AddTBString(PowerModelAttr.PowerFlagName, null, "权限标记名称", true, false, 0, 100, 10);

		map.AddDDLSysEnum(PowerModelAttr.PowerCtrlType, 0, "控制类型", true, false, PowerModelAttr.PowerCtrlType, "@0=岗位@1=人员");

		map.AddTBString(PowerModelAttr.EmpNo, null, "人员编号", true, false, 0, 100, 10);
		map.AddTBString(PowerModelAttr.EmpName, null, "人员名称", true, false, 0, 100, 10);

		map.AddTBString(PowerModelAttr.StaNo, null, "岗位编号", true, false, 0, 100, 10);
		map.AddTBString(PowerModelAttr.StaName, null, "岗位名称", true, false, 0, 100, 10);

			//Model标记.
		map.AddTBString(PowerModelAttr.FlowNo, null, "流程编号", true, false, 0, 100, 10);
		  //  map.AddTBInt(PowerModelAttr.NodeID, 0, "节点", true, false);
		map.AddTBString(PowerModelAttr.FrmID, null, "表单ID", true, false, 0, 100, 10);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
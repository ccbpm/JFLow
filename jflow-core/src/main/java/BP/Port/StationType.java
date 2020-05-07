package BP.Port;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;
import BP.Sys.CCBPMRunModel;

import java.util.*;

/** 
  岗位类型
*/
public class StationType extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 组织编码
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(StationAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(StationAttr.OrgNo, value);
	}
	public final String getFK_StationType() throws Exception
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value) throws Exception
	{
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

	public final String getFK_StationTypeText() throws Exception
	{
		return this.GetValRefTextByKey(StationAttr.FK_StationType);
	}

		///#endregion

		///#region 实现基本的方方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
		///#endregion

		///#region 构造方法
	/** 
	 岗位类型
	*/
	public StationType()
	{
	}
	/** 
	 岗位类型
	 
	 @param _No
	*/
	public StationType(String _No) throws Exception
	{
		super(_No);
	}
		///#endregion

	/** 
	 岗位类型Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_StationType", "岗位类型");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(StationTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(StationTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(StationTypeAttr.Idx, 0, "顺序", true, false);
		map.AddTBString(StationTypeAttr.OrgNo, null, "组织机构编号", true, false, 0, 50, 20);

		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			map.AddHidden(StationTypeAttr.OrgNo, "=", BP.Web.WebUser.getOrgNo());
		}

		this.set_enMap(map);
		return this.get_enMap();
	}
}
package bp.port;

import bp.da.DataType;
import bp.en.*;
import bp.sys.*;
import bp.difference.*;

/** 
  岗位类型
*/
public class StationType extends EntityNoName
{

		///#region 属性
	/** 
	 组织编码
	*/
	public final String getOrgNo()
	{
		return this.GetValStrByKey(StationAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	 {
		this.SetValByKey(StationAttr.OrgNo, value);
	}
	public final String getFK_StationType()
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value)
	 {
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

	public final String getFK_StationTypeText()
	{
		return this.GetValRefTextByKey(StationAttr.FK_StationType);
	}


		///#endregion


		///#region 实现基本的方方法
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

		///#endregion


		///#region 构造方法
	/** 
	 岗位类型
	*/
	public StationType() {
	}
	/** 
	 岗位类型
	 
	 param _No
	*/
	public StationType(String _No)
	{
		super(_No);
	}

		///#endregion

	/** 
	 岗位类型Map
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_StationType", "岗位类型");
		map.setCodeStruct("2");

		map.AddTBStringPK(StationTypeAttr.No, null, "编号", true, true, 1, 40, 40);
		map.AddTBString(StationTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(StationTypeAttr.Idx, 0, "顺序", true, false);

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, true, 0, 50, 250);
			map.AddHidden(StationAttr.OrgNo, "=", bp.web.WebUser.getOrgNo()); //加隐藏条件.
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, true, 0, 50, 250);

			if (SystemConfig.getGroupStationModel() == 0)
			{
				map.AddHidden(StationAttr.OrgNo, "=", bp.web.WebUser.getOrgNo()); //每个组织都有自己的岗责体系的时候. 加隐藏条件.
			}
		}

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (DataType.IsNullOrEmpty(this.getName()) == true)
			throw new RuntimeException("请输入岗位类型名称");
		if (DataType.IsNullOrEmpty(this.getOrgNo()) == true){
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single) {
				this.setOrgNo(bp.web.WebUser.getOrgNo());
			}
		}
		return super.beforeUpdateInsertAction();
	}

}
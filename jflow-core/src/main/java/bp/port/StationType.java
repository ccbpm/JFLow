package bp.port;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;

/**
 * 岗位类型
 */
public class StationType extends EntityNoName {

	private static final long serialVersionUID = 1L;

	/// 属性
	/**
	 * 组织编码
	 */
	public final String getOrgNo()throws Exception {
		return this.GetValStrByKey(StationAttr.OrgNo);
	}

	public final void setOrgNo(String value) throws Exception {
		this.SetValByKey(StationAttr.OrgNo, value);
	}

	public final String getFK_StationType() throws Exception{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}

	public final void setFK_StationType(String value) throws Exception {
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

	public final String getFK_StationTypeText()throws Exception {
		return this.GetValRefTextByKey(StationAttr.FK_StationType);
	}

	///

	/// 实现基本的方方法
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

	///

	/// 构造方法
	/**
	 * 岗位类型
	 */
	public StationType() {
	}

	/**
	 * 岗位类型
	 * 
	 * @param _No
	 * @throws Exception 
	 */
	public StationType(String _No) throws Exception {
		super(_No);
	}

	///

	/**
	 * 岗位类型Map
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}
		Map map = new Map("Port_StationType", "岗位类型");
		map.setCodeStruct("2");

		map.AddTBStringPK(StationTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(StationTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(StationTypeAttr.Idx, 0, "顺序", true, false);
		map.AddTBString(StationTypeAttr.OrgNo, null, "组织机构编号", true, false, 0, 50, 20);

		try {
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
				map.AddHidden(StationTypeAttr.OrgNo, "=", WebUser.getOrgNo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		this.set_enMap(map);
		return this.get_enMap();
	}
}
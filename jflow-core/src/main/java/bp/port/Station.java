package bp.port;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.web.WebUser;


/**
 * 岗位
 */
public class Station extends EntityNoName {
	private static final long serialVersionUID = 1L;

	/// 属性
	public final String getFK_StationType() throws Exception{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}

	public final void setFK_StationType(String value) throws Exception {
		this.SetValByKey(StationAttr.FK_StationType, value);
	}

	/**
	 * 组织编码
	 */
	public final String getOrgNo() throws Exception{
		return this.GetValStrByKey(StationAttr.OrgNo);
	}

	public final void setOrgNo(String value) throws Exception{
		this.SetValByKey(StationAttr.OrgNo, value);
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
	 * 岗位
	 */
	public Station() {
	}

	/**
	 * 岗位
	 * 
	 * @param _No
	 * @throws Exception 
	 */
	public Station(String _No) throws Exception {
		super(_No);
	}

	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("Port_Station", "岗位");
		map.setCodeStruct("3");
		map.setIsAutoGenerNo(true);

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 1, 50, 200);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 0, 100, 200);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "类型", new StationTypes(), true);
		map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 250);
		map.AddSearchAttr(StationAttr.FK_StationType);

		if (SystemConfig.getCCBPMRunModel() == bp.sys.CCBPMRunModel.Single) {

		} else {
			try {
				map.AddHidden(StationTypeAttr.OrgNo, "=", WebUser.getOrgNo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.set_enMap(map);
		return this.get_enMap();
	}


}
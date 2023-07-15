package bp.port;

import bp.da.DataType;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.CCBPMRunModel;

/** 
  用户组类型
*/
public class TeamType extends EntityNoName
{

		///#region 属性

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
	 用户组类型
	*/
	public TeamType()  {
	}
	/** 
	 用户组类型
	 
	 param _No
	*/
	public TeamType(String _No)
	{
		super(_No);
	}

	/** 
	 用户组类型Map
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
			return this.get_enMap();
		Map map = new Map("Port_TeamType", "用户组类型");
		map.setCodeStruct("2");

		map.AddTBStringPK(TeamTypeAttr.No, null, "编号", true, true, 1, 5, 5);
		map.AddTBString(TeamTypeAttr.Name, null, "名称", true, false, 1, 50, 20);
		map.AddTBInt(TeamTypeAttr.Idx, 0, "顺序", true, false);

		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, true, 0, 50, 250);
			map.AddHidden(StationAttr.OrgNo, "=", "@WebUser.OrgNo"); //加隐藏条件.
		}

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {

		if (DataType.IsNullOrEmpty(this.getName()) == true)
			throw new RuntimeException("请输入名称");

		if (DataType.IsNullOrEmpty(this.GetValByKey("OrgNo")) == true){
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single) {
				this.SetValByKey("OrgNo",bp.web.WebUser.getOrgNo());
			}
		}

		if (DataType.IsNullOrEmpty(this.getName()) == true)
			throw new RuntimeException("请输入名称");

		return super.beforeUpdateInsertAction();
	}
}
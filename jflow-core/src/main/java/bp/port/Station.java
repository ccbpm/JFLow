package bp.port;

import bp.da.DBAccess;
import bp.da.DataType;
import bp.en.*;
import bp.sys.*;
import bp.difference.*;
import bp.web.WebUser;

/** 
 岗位
*/
public class Station extends EntityNoName
{

		///#region 属性
	public final String getFKStationType()
	{
		return this.GetValStrByKey(StationAttr.FK_StationType);
	}
	public final void setFK_StationType(String value)
	 {
		this.SetValByKey(StationAttr.FK_StationType, value);
	}
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
	public final int getIdx()
	{
		return this.GetValIntByKey(StationAttr.Idx);
	}
	public final void setIdx(int value)
	 {
		this.SetValByKey(StationAttr.Idx, value);
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
	 岗位
	*/
	public Station()  {
	}
	/** 
	 岗位
	 
	 param _No
	*/
	public Station(String _No) throws Exception {
		super(_No);
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Station", "岗位");
	//	map.setCodeStruct("3");
		//map.setIsAutoGenerNo(true);

		map.AddTBStringPK(StationAttr.No, null, "编号", true, true, 1, 50, 200);
		map.AddTBString(StationAttr.Name, null, "名称", true, false, 0, 100, 200);
		map.AddDDLEntities(StationAttr.FK_StationType, null, "类型", new StationTypes(), true);
		map.AddTBString(StationAttr.OrgNo, null, "隶属组织", true, true, 0, 50, 250);
        //#region 根据组织结构类型不同.
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			map.AddHidden(StationAttr.OrgNo, "=", bp.web.WebUser.getOrgNo()); //加隐藏条件.

		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
			if (bp.difference.SystemConfig.getGroupStationModel() == 0)
				map.AddHidden(StationAttr.OrgNo, "=", bp.web.WebUser.getOrgNo());//每个组织都有自己的岗责体系的时候. 加隐藏条件.

		map.AddTBInt(StationAttr.Idx, 0, "顺序号", true, false);
			//根据组织结构类型不同.
		map.AddSearchAttr(StationAttr.FK_StationType);

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getNo())) this.setNo(DBAccess.GenerGUID());
		return super.beforeInsert();
	}

	///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if(DataType.IsNullOrEmpty(this.getName())==true)
			throw new RuntimeException("请输入岗位名称");
		if(DataType.IsNullOrEmpty(this.getFKStationType())==true)
			throw new RuntimeException("请选择岗位类型");
		if (DataType.IsNullOrEmpty(this.getOrgNo()) == true) {
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
				this.setOrgNo(bp.web.WebUser.getOrgNo());
		}
		return super.beforeUpdateInsertAction();
	}


}
package bp.sys;

import bp.da.*;
import bp.sys.base.*;
import bp.en.*;
import bp.pub.*;
import java.io.*;
import java.nio.file.*;


import bp.wf.RefObject;

/** 
 映射基础
*/
public class MapData extends EntityNoName
{

		///#region entity 相关属性(参数属性)
	/** 
	 属性ens
	*/
	public final String getEnsName()
	{
		return this.GetValStringByKey(MapDataAttr.EnsName);
	}
	public final void setEnsName(String value)
	{this.SetPara(MapDataAttr.EnsName, value);
	}

	public final void setFK_FrmSort(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FrmSort, value);
	}
	/** 
	 是不是加密，为铁路局增加.
	*/
	public final boolean isJM()  {
		return this.GetParaBoolen(MapDataAttr.IsJM);
	}
	public final void setJM(boolean value)
	{this.SetPara(MapDataAttr.IsJM, value);
	}

		///#endregion entity 相关操作.


		///#region 报表属性(参数方式存储).
	/** 
	 是否关键字查询
	*/
	public final boolean isSearchKey()  {
		return this.GetParaBoolen(MapDataAttr.IsSearchKey, true);
	}
	public final void setSearchKey(boolean value)
	{this.SetPara(MapDataAttr.IsSearchKey, value);
	}

	/**
	 是否关键字查询
	 */
	public final boolean getRptIsSearchKey()throws Exception
	{
		return this.GetParaBoolen(MapDataAttr.RptIsSearchKey, true);
	}
	public final void setRptIsSearchKey(boolean value) throws Exception
	{
		this.SetPara(MapDataAttr.RptIsSearchKey, value);
	}

	/** 
	 时间段查询方式
	*/
	public final DTSearchWay getDTSearchWay()  {
		return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.DTSearchWay));
	}
	public final void setDTSearchWay(DTSearchWay value)
	{this.SetPara(MapDataAttr.DTSearchWay, value.getValue());
	}
	/** 
	 查询外键枚举字段
	*/
	public final String getRptSearchKeys()  {
		return this.GetParaString(MapDataAttr.RptSearchKeys, "*");
	}
	public final void setRptSearchKeys(String value)
	{this.SetPara(MapDataAttr.RptSearchKeys, value);
	}
	/** 
	 查询key.
	*/
	public final String getDTSearchKey()  {
		return this.GetParaString(MapDataAttr.DTSearchKey);
	}
	public final void setDTSearchKey(String value)
	{this.SetPara(MapDataAttr.DTSearchKey, value);
	}

		///#endregion 报表属性(参数方式存储).


		///#region 外键属性
	/** 
	 版本号
	*/
	public final int getVer2022()  {
		return this.GetParaInt(MapDataAttr.Ver, 1);
	}
	public final void setVer2022(int value)
	{this.SetPara(MapDataAttr.Ver, value);
	}
	public final String getOrgNo()
	{
		return this.GetValStringByKey(MapDataAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	 {
		this.SetValByKey(MapDataAttr.OrgNo, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(MapDataAttr.Idx);
	}
	public final void setIdx(int value)
	 {
		this.SetValByKey(MapDataAttr.Idx, value);
	}

	/** 
	 框架
	*/
	public final MapFrames getMapFrames() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new MapFrames(), MapExtAttr.FK_MapData, this.getNo());
		return ens instanceof MapFrames ? (MapFrames)ens : null;

	}
	/** 
	 分组字段
	*/
	public final GroupFields getGroupFields() throws Exception {
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = tempVar instanceof GroupFields ? (GroupFields)tempVar : null;
		if (obj == null)
		{
			obj = new GroupFields(this.getNo());
			this.SetRefObject("GroupFields", obj);
		}
		return obj;
	}
	/** 
	 逻辑扩展
	*/
	public final MapExts getMapExts() throws Exception {
//		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new MapExts(), MapExtAttr.FK_MapData, this.getNo());
//		return ens instanceof MapExts ? (MapExts)ens : null;

			//MapExts obj = this.GetRefObject("MapExts") as MapExts;
			//if (obj == null)
			//{
			//    obj = new MapExts(this.No);
			//    this.SetRefObject("MapExts", obj);
			//}
			//return obj;
		Object tempVar = this.GetRefObject("MapExts");
		MapExts obj = tempVar instanceof MapExts ? (MapExts)tempVar : null;
		if (obj == null)
		{
			obj = new MapExts(this.getNo());
			this.SetRefObject("MapExts", obj);
		}
		return obj;
	}
	/** 
	 事件:
	 1.该事件与Node,Flow,MapDtl,MapData一样的算法.
	 2.如果一个业务逻辑有变化，其他的也要变化.
	*/
	public final FrmEvents getFrmEvents() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new FrmEvents(), FrmEventAttr.FK_MapData, this.getNo());
		return ens instanceof FrmEvents ? (FrmEvents)ens : null;
	}
	/** 
	 从表原始属性的获取
	*/
	public final MapDtls getOrigMapDtls() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new MapDtls(), MapDtlAttr.FK_MapData, this.getNo(), MapDtlAttr.FK_Node, 0);
		return ens instanceof MapDtls ? (MapDtls)ens : null;
	}
	/** 
	 查询给MapData下的所有从表数据
	*/
	public final MapDtls getMapDtls() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new MapDtls(), MapDtlAttr.FK_MapData, this.getNo());
		return ens instanceof MapDtls ? (MapDtls)ens : null;

			//MapDtls obj = this.GetRefObject("MapDtls") as MapDtls;
			//if (obj == null)
			//{
			//    obj = new MapDtls(this.No);
			//    this.SetRefObject("MapDtls", obj);
			//}
			//return obj;
	}
	/** 
	 枚举值
	*/
	public final SysEnums getSysEnums() throws Exception {
		Object tempVar = this.GetRefObject("SysEnums");
		SysEnums obj = tempVar instanceof SysEnums ? (SysEnums)tempVar : null;
		obj = null;
		if (obj == null)
		{
			obj = new SysEnums();
			if (bp.difference.SystemConfig.getAppCenterDBType() == DBType.MySQL)
			{
				String strs = "";
				Paras ps = new Paras();
				ps.SQL = "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData=" + ps.getDBStr() + "FK_MapData AND LGType=1";
				ps.Add("FK_MapData", this.getNo());

				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				for (DataRow dr : dt.Rows)
				{
					strs += "'" + dr.getValue(0).toString() + "',";
				}

				if (dt.Rows.size() >= 1)
				{
					strs += "'ssss'";
					obj.RetrieveInOrderBy("EnumKey", strs, SysEnumAttr.IntKey);
				}
			}
			else
			{
				if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
				{

					String enumKeySQL = "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData = '" + this.getNo() + "' AND LGType = 1 ";
					String sqlWhere = " EnumKey IN (" + enumKeySQL + ") AND OrgNo='" + bp.web.WebUser.getOrgNo() + "'";
					String sqlEnum = "SELECT * FROM "+bp.sys.base.Glo.SysEnum()+" WHERE " + sqlWhere;
					sqlEnum += " UNION ";
					sqlEnum += "SELECT * FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey IN (" + enumKeySQL + ") AND EnumKey NOT IN (SELECT EnumKey FROM "+bp.sys.base.Glo.SysEnum()+" WHERE " + sqlWhere + ") AND (OrgNo Is Null Or OrgNo='')";
					sqlEnum += "Order By IntKey";
					DataTable dt = DBAccess.RunSQLReturnTable(sqlEnum);
					QueryObject.InitEntitiesByDataTable(obj, dt, null);
				}
				else
				{
					obj.RetrieveInSQL(SysEnumAttr.EnumKey, "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' AND LGType=1 ", SysEnumAttr.IntKey);
				}
			}
			this.SetRefObject("SysEnums", obj);

		}
		return obj;
	}


	/** 
	 图片
	*/
	public final FrmImgs getFrmImgs() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new FrmImgs(), FrmImgAttr.FK_MapData, this.getNo());
		return ens instanceof FrmImgs ? (FrmImgs)ens : null;
	}
	/** 
	 附件
	*/
	public final FrmAttachments getFrmAttachments() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new FrmAttachments(), FrmAttachmentAttr.FK_MapData, this.getNo());
		return ens instanceof FrmAttachments ? (FrmAttachments)ens : null;
	}
	/** 
	 图片附件
	*/
	public final FrmImgAths getFrmImgAths() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new FrmImgAths(), FrmImgAthAttr.FK_MapData, this.getNo());
		return ens instanceof FrmImgAths ? (FrmImgAths)ens : null;

	}

	/** 
	 单选按钮
	*/
	public final FrmRBs getFrmRBs() throws Exception {
		Entities ens =this.GetEntitiesAttrFromAutoNumCash(new FrmRBs(), FrmRBAttr.FK_MapData, this.getNo());
		return ens instanceof FrmRBs ? (FrmRBs)ens : null;
	}
	/** 
	 属性
	*/
	public final MapAttrs getMapAttrs() throws Exception {
		Object tempVar = this.GetRefObject("MapAttrs");
		MapAttrs obj = tempVar instanceof MapAttrs ? (MapAttrs)tempVar : null;
		if (obj == null)
		{
			obj = new MapAttrs(this.getNo());
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}

		///#endregion


		///#region 缓存方法.
	public final void CleanObject()  {
		this.getRow().SetValByKey("FrmEles", null);
		this.getRow().SetValByKey("MapFrames", null);
		this.getRow().SetValByKey("GroupFields", null);
		this.getRow().SetValByKey("MapExts", null);
		this.getRow().SetValByKey("FrmEvents", null);
		this.getRow().SetValByKey("MapDtls", null);
		this.getRow().SetValByKey("SysEnums", null);
		this.getRow().SetValByKey("FrmRpts", null);
		this.getRow().SetValByKey("FrmLinks", null);
		this.getRow().SetValByKey("FrmBtns", null);
		this.getRow().SetValByKey("FrmEles", null);
		// this.Row.SetValByKey("FrmLines", null);
		// this.Row.SetValByKey("FrmLabs", null);
		this.getRow().SetValByKey("FrmAttachments", null);
		this.getRow().SetValByKey("FrmImgAthDBs", null);
		this.getRow().SetValByKey("FrmRBs", null);
		this.getRow().SetValByKey("MapAttrs", null);
		return;
	}
	/** 
	 清空缓存
	*/
	public final void ClearCash()  {
		CashFrmTemplate.Remove(this.getNo());
		Cash.SetMap(this.getNo(), null);
		CleanObject();
		Cash.getSQL_Cash().remove(this.getNo());
	}

		///#endregion 缓存方法.


		///#region 基本属性.
	/** 
	 事件实体
	*/
	public final String getFormEventEntity()
	{
		return this.GetValStringByKey(MapDataAttr.FormEventEntity);
	}
	public final void setFormEventEntity(String value)
	 {
		this.SetValByKey(MapDataAttr.FormEventEntity, value);
	}

	public static boolean isEditDtlModel()  {
		String s = bp.web.WebUser.GetSessionByKey("IsEditDtlModel", "0");
		if (s.equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public static void setEditDtlModel(boolean value)
	{bp.web.WebUser.SetSessionByKey("IsEditDtlModel", "1");
	}

	public final String getICON()
	{
		return this.GetValStringByKey(MapDataAttr.Icon);
	}

		///#endregion 基本属性.


		///#region 属性
	/** 
	 物理表
	*/
	public final String getPTable()  {
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			return this.getNo();
		}
		return s.trim();
	}
	public final void setPTable(String value)
	 {
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 表存储模式0=自定义表,1,指定的表,2=指定的表不能修改表结构.
	 @周朋
	*/
	public final int getPTableModel()
	{
		return this.GetValIntByKey(MapDataAttr.PTableModel);
	}
	public final void setPTableModel(int value)
	 {
		this.SetValByKey(MapDataAttr.PTableModel, value);
	}


	public final String getUrlExt()
	{
		return this.GetValStrByKey(MapDataAttr.UrlExt);
	}
	public final void setUrlExt(String value)
	 {
		this.SetValByKey(MapDataAttr.UrlExt, value);
	}
	public final DBUrlType getHisDBUrl()  {
		return DBUrlType.AppCenterDSN;
	}
	public final int getHisFrmTypeInt()
	{
		return this.GetValIntByKey(MapDataAttr.FrmType);
	}
	public final void setHisFrmTypeInt(int value)
	 {
		this.SetValByKey(MapDataAttr.FrmType, value);
	}
	public final FrmType getHisFrmType()  {
		return FrmType.forValue(this.GetValIntByKey(MapDataAttr.FrmType));
	}
	public final void setHisFrmType(FrmType value)
	 {
		this.SetValByKey(MapDataAttr.FrmType, value.getValue());
	}


	public final int getHisEntityType()
	{
		return this.GetValIntByKey(MapDataAttr.EntityType);
	}
	public final void setHisEntityType(int value)
	 {
		this.SetValByKey(MapDataAttr.EntityType, value);
	}
	/** 
	 表单类型名称
	*/
	public final String getHisFrmTypeText()  {
		return this.getHisFrmType().toString();
	}
	/** 
	 备注
	*/
	public final String getNote()
	{
		return this.GetValStrByKey(MapDataAttr.Note);
	}
	public final void setNote(String value)
	 {
		this.SetValByKey(MapDataAttr.Note, value);
	}
	/** 
	 是否有CA.
	*/
	public final boolean isHaveCA()  {
		return this.GetParaBoolen("IsHaveCA", false);

	}
	public final void setHaveCA(boolean value)
	{this.SetPara("IsHaveCA", value);
	}

	/** 
	是否启用装载填充
	*/
	public final boolean isPageLoadFull()  {
		return this.GetParaBoolen("IsPageLoadFull", false);

	}
	public final void setPageLoadFull(boolean value)
	{this.SetPara("IsPageLoadFull", value);
	}

	/** 
	 数据源
	*/
	public final String getDBSrc()
	{
		return this.GetValStrByKey(MapDataAttr.DBSrc);
	}
	public final void setDBSrc(String value)
	 {
		this.SetValByKey(MapDataAttr.DBSrc, value);
	}

	/** 
	 类别，可以为空.
	*/
	public final String getFK_FormTree()
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)
	 {
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 类别名称
	*/
	public final String getFK_FormTreeText()  {
		return DBAccess.RunSQLReturnStringIsNull("SELECT Name FROM Sys_FormTree WHERE No='" + this.getFK_FormTree() + "'", "目录错误");
	}
	/** 
	 从表集合.
	*/
	public final String getDtls()
	{
		return this.GetValStrByKey(MapDataAttr.Dtls);
	}
	public final void setDtls(String value)
	 {
		this.SetValByKey(MapDataAttr.Dtls, value);
	}
	/** 
	 主键
	*/
	public final String getEnPK()  {
		String s = this.GetValStrByKey(MapDataAttr.EnPK);
		if (DataType.IsNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value)
	 {
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	private Entities _HisEns = null;
	public final Entities getHisEns()  {
		if (_HisEns == null)
		{
			_HisEns = ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn() throws Exception {
		return this.getHisEns().getGetNewEntity();
	}
	public final float getFrmW()
	{
		return this.GetValFloatByKey(MapDataAttr.FrmW);
	}
	public final void setFrmW(float value)
	 {
		this.SetValByKey(MapDataAttr.FrmW, value);
	}
	public final float getFrmH()
	{
		return this.GetValFloatByKey(MapDataAttr.FrmH);
	}
	public final void setFrmH(float value)
	 {
		this.SetValByKey(MapDataAttr.FrmH, value);
	}
	/** 
	 应用类型.  0独立表单.1节点表单
	*/
	public final String getAppType()
	{
		return this.GetValStrByKey(MapDataAttr.AppType);
	}
	public final void setAppType(String value)
	 {
		this.SetValByKey(MapDataAttr.AppType, value);
	}
	/** 
	 表单body属性.
	*/
	public final String getBodyAttr()  {
		String str = this.GetValStrByKey(MapDataAttr.BodyAttr);
		str = str.replace("~", "'");
		return str;
	}
	public final void setBodyAttr(String value)
	 {
		this.SetValByKey(MapDataAttr.BodyAttr, value);
	}
	/** 
	 流程控件s.
	*/
	public final String getFlowCtrls()
	{
		return this.GetValStrByKey(MapDataAttr.FlowCtrls);
	}
	public final void setFlowCtrls(String value)
	 {
		this.SetValByKey(MapDataAttr.FlowCtrls, value);
	}

	public final int getTableCol()
	{
		return this.GetValIntByKey(MapDataAttr.TableCol);
	}
	public final void setTableCol(int value)
	 {
		this.SetValByKey(MapDataAttr.TableCol, value);
	}
	/** 
	 实体表单类型.@0=独立表单@1=单据@2=编号名称实体@3=树结构实体
	*/
	public final EntityType getEntityType() throws Exception {
		return EntityType.forValue(this.GetValIntByKey(MapDataAttr.EntityType));
	}
	public final void setEntityType(EntityType value) throws Exception {
		this.SetValByKey(MapDataAttr.EntityType, value.getValue());
	}

		///#region 构造方法
	public final Map GenerHisMap() throws Exception {
		MapAttrs mapAttrs = this.getMapAttrs();
		if (mapAttrs.size() == 0)
		{
			this.RepairMap();
			mapAttrs = this.getMapAttrs();
		}

		Map map = new Map(this.getPTable(), this.getName());

		Attrs attrs = new Attrs();
		for (MapAttr mapAttr : mapAttrs.ToJavaList())
		{
			map.AddAttr(mapAttr.getHisAttr());
		}

		// 产生从表。
		MapDtls dtls = this.getMapDtls(); // new MapDtls(this.No);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			GEDtls dtls1 = new GEDtls(dtl.getNo());
			map.AddDtl(dtls1, "RefPK");
		}


			///#region 查询条件.
		map.IsShowSearchKey = this.isSearchKey(); //是否启用关键字查询.
		// 按日期查询.
		map.DTSearchWay = this.getDTSearchWay(); //日期查询方式.
		map.DTSearchKey = this.getDTSearchKey(); //日期字段.

		//是否是加密 
		map.IsJM = this.isJM();

		//加入外键查询字段.
		String[] keys = this.getRptSearchKeys().split("[*]", -1);
		for (String key : keys)
		{
			if (DataType.IsNullOrEmpty(key))
			{
				continue;
			}

			if (map.getAttrs().contains(key) == false)
			{
				continue;
			}

			map.AddSearchAttr(key);
		}

			///#endregion 查询条件.

		return map;
	}
	private GEEntity _HisEn = null;
	public final GEEntity getHisGEEn()  {
		if (this._HisEn == null)
		{
			_HisEn = new GEEntity(this.getNo());
		}
		return _HisEn;
	}
	/** 
	 生成实体
	 
	 param ds
	 @return 
	*/
	public final GEEntity GenerGEEntityByDataSet(DataSet ds) throws Exception {
		// New 它的实例.
		GEEntity en = this.getHisGEEn();

		// 它的table.
		DataTable dt = ds.GetTableByName(this.getNo());

		//装载数据.
		en.getRow().LoadDataTable(dt, dt.Rows.get(0));

		// dtls.
		MapDtls dtls = this.getMapDtls();
		for (MapDtl item : dtls.ToJavaList())
		{
			DataTable dtDtls = ds.GetTableByName(item.getNo());
			GEDtls dtlsEn = new GEDtls(item.getNo());
			for (DataRow dr : dtDtls.Rows)
			{
				// 产生它的Entity data.
				GEDtl dtl = (GEDtl)dtlsEn.getGetNewEntity();
				dtl.getRow().LoadDataTable(dtDtls, dr);

				//加入这个集合.
				dtlsEn.AddEntity(dtl);
			}

			//加入到他的集合里.
			en.getDtls().add(dtDtls);
		}
		return en;
	}
	/** 
	 生成map.
	 
	 param no
	 @return 
	*/
	public static Map GenerHisMap(String no) throws Exception {
		if (bp.difference.SystemConfig.getIsDebug())
		{
			MapData md = new MapData();
			md.setNo(no);
			md.Retrieve();
			return md.GenerHisMap();
		}
		else
		{
			Map map = Cash.GetMap(no);
			if (map == null)
			{
				MapData md = new MapData();
				md.setNo(no);
				md.Retrieve();
				map = md.GenerHisMap();
				Cash.SetMap(no, map);
			}
			return map;
		}
	}
	/** 
	 映射基础
	*/
	public MapData()  {
	}
	/** 
	 映射基础
	 
	 param no 映射编号
	*/
	public MapData(String no) throws Exception {
		this.setNo(no);
		this.Retrieve();
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

		Map map = new Map("Sys_MapData", "表单注册表");


			///#region 基础信息.
		map.AddTBStringPK(MapDataAttr.No, null, "编号", true, false, 1, 200, 100);
		map.AddTBString(MapDataAttr.Name, null, "描述", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.FormEventEntity, null, "事件实体", true, true, 0, 100, 20, true);

		map.AddTBString(MapDataAttr.EnPK, null, "实体主键", true, false, 0, 200, 20);
		map.AddTBString(MapDataAttr.PTable, null, "物理表", true, false, 0, 500, 20);

			//@周朋 表存储格式0=自定义表,1=指定表,可以修改字段2=执行表不可以修改字段.
		map.AddTBInt(MapDataAttr.PTableModel, 0, "表存储模式", true, true);


		map.AddTBString(MapDataAttr.UrlExt, null, "连接(对嵌入式表单有效)", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.Dtls, null, "从表", true, false, 0, 500, 20);

			//格式为: @1=方案名称1@2=方案名称2@3=方案名称3
			//map.AddTBString(MapDataAttr.Slns, null, "表单控制解决方案", true, false, 0, 500, 20);

		map.AddTBInt(MapDataAttr.FrmW, 900, "FrmW", true, true);
		map.AddTBInt(MapDataAttr.FrmH, 1200, "FrmH", true, true);

			// @0=4列, @1=6 列.
		map.AddTBInt(MapDataAttr.TableCol, 0, "傻瓜表单显示的列", true, true);

			//Tag
		map.AddTBString(MapDataAttr.Tag, null, "Tag", true, false, 0, 500, 20);

			// 可以为空这个字段。
			//map.AddTBString(MapDataAttr.FK_FrmSort, null, "表单类别", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.FK_FormTree, null, "表单树类别", true, false, 0, 500, 20);

			// enumFrmType  @自由表单，@傻瓜表单，@嵌入式表单.  
		map.AddDDLSysEnum(MapDataAttr.FrmType, bp.sys.FrmType.FoolForm.getValue(), "表单类型", true, false, MapDataAttr.FrmType);

		map.AddTBInt(MapDataAttr.FrmShowType, 0, "表单展示方式", true, true);

		map.AddDDLSysEnum(MapDataAttr.EntityType, 0, "业务类型", true, false, MapDataAttr.EntityType, "@0=独立表单@1=单据@2=编号名称实体@3=树结构实体");
		map.SetHelperAlert(MapDataAttr.EntityType, "该实体的类型,@0=单据@1=编号名称实体@2=树结构实体.");

		map.AddBoolean("IsEnableJs", false, "是否启用自定义js函数？", true, true, true);

			// 应用类型.  0独立表单.1节点表单
		map.AddTBInt(MapDataAttr.AppType, 0, "应用类型", true, false);
		map.AddTBString(MapDataAttr.DBSrc, "local", "数据源", true, false, 0, 100, 20);
		map.AddTBString(MapDataAttr.BodyAttr, null, "表单Body属性", true, false, 0, 100, 20);

			///#endregion 基础信息.


			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Note, null, "备注", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);

		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", true, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, false, 0, 128, 20);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, false, 0, 30, 20);

		map.AddTBString(MapDataAttr.Icon, null, "Icon", true, false, 0, 500, 20, true);

			//流程控件.
		map.AddTBString(MapDataAttr.FlowCtrls, null, "流程控件", true, true, 0, 200, 20);

		//表单
		//map.AddTBString(MapDataAttr.HtmlTemplateFile, null, "表单", true, true, 0, 200, 20);

			//增加参数字段.
		map.AddTBAtParas(4000);

			///#endregion

		map.AddTBString(MapDataAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 执行复制.
	 
	 param copyToFrmID
	 param frmName
	 param
	 @return 
	*/
	public final String DoCopy(String copyToFrmID, String frmName) throws Exception {
		bp.sys.CCFormAPI.CopyFrm(this.getNo(), copyToFrmID, frmName, this.getFK_FormTree());
		return "执行成功";
	}

	/** 
	 上移
	*/
	public final void DoUp() throws Exception {
		this.DoOrderUp(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
	}
	/** 
	 下移
	*/
	public final void DoOrderDown() throws Exception {
		this.DoOrderDown(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
	}

	//检查表单
	public final void CheckPTableSaveModel(String filed) throws Exception {
		if (this.getPTableModel() == 2)
		{
			/*如果是存储格式*/
			if (DBAccess.IsExitsTableCol(this.getPTable(), filed) == false)
			{
				throw new RuntimeException("@表单的表存储模式不允许您创建不存在的字段(" + filed + ")，不允许修改表结构.");
			}
		}
	}
	/** 
	 获得PTableModel=2模式下的表单，没有被使用的字段集合.
	 
	 param frmID
	 @return 
	*/
	public static DataTable GetFieldsOfPTableMode2(String frmID) throws Exception {
		String pTable = "";

		MapDtl dtl = new MapDtl();
		dtl.setNo(frmID);
		if (dtl.RetrieveFromDBSources() == 1)
		{
			pTable = dtl.getPTable();
		}
		else
		{
			MapData md = new MapData();
			md.setNo(frmID);
			md.RetrieveFromDBSources();
			pTable = md.getPTable();
		}

		//获得原始数据.
		DataTable dt = DBAccess.GetTableSchema(pTable);

		//创建样本表结构.
		DataTable mydt = DBAccess.GetTableSchema(pTable);
		mydt.Rows.clear();

		//获得现有的列..
		MapAttrs attrs = new MapAttrs(frmID);

		String flowFiels = ",GUID,PRI,PrjNo,PrjName,PEmp,AtPara,FlowNote,WFSta,PNodeID,FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum,PWorkID,PFlowNo,BillNo,ProjNo,";

		//排除已经存在的列. 把所有的列都输出给前台，让前台根据类型分拣.
		for (DataRow dr : dt.Rows)
		{
			String key = dr.getValue("FName").toString();
			if (attrs.contains(MapAttrAttr.KeyOfEn, key) == true)
			{
				continue;
			}

			if (flowFiels.contains("," + key + ",") == true)
			{
				continue;
			}

			DataRow mydr = mydt.NewRow();
			mydr.setValue("FName", dr.getValue("FName"));
			mydr.setValue("FType", dr.getValue("FType"));
			mydr.setValue("FLen", dr.getValue("FLen"));
			mydr.setValue("FDesc", dr.getValue("FDesc"));
			mydt.Rows.add(mydr);
		}
		return mydt;
	}



		///#endregion


		///#region 常用方法.
	private FormEventBase _HisFEB = null;
	public final FormEventBase getHisFEB() throws Exception {
		if (this.getFormEventEntity().equals(""))
		{
			return null;
		}

		if (_HisFEB == null)
		{
			_HisFEB = bp.sys.base.Glo.GetFormEventBaseByEnName(this.getNo());
		}

		return _HisFEB;
	}
	/** 
	 升级逻辑.
	*/
	public final void Upgrade() throws Exception {
		String sql = "";

			///#region 升级ccform控件.
		//if (DBAccess.IsExitsObject("Sys_FrmLine") == true)
		//{
		//    //重命名.
		//    BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
		//    dbsrc.Rename("Table", "Sys_FrmLine", "Sys_FrmLineBak");
		//}
		if (DBAccess.IsExitsObject("Sys_FrmLab") == true)
		{
			//重命名.
			bp.sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLab", "Sys_FrmLabBak");


		}
		if (DBAccess.IsExitsObject("Sys_FrmBtn") == true)
		{
			//重命名.
			bp.sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLab", "Sys_FrmLabBak");
		}

			///#endregion 升级ccform控件.
	}
	/** 
	 导入数据
	 
	 param ds
	 param
	 @return 
	*/
	public static MapData ImpMapData(DataSet ds) throws Exception {
		String errMsg = "";
		if (ds.contains("WF_Flow") == true)
		{
			errMsg += "@此模板文件为流程模板。";
		}

		if (ds.contains("Sys_MapAttr") == false)
		{
			errMsg += "@缺少表:Sys_MapAttr";
		}

		if (ds.contains("Sys_MapData") == false)
		{
			errMsg += "@缺少表:Sys_MapData";
		}

		if (!errMsg.equals(""))
		{
			throw new RuntimeException(errMsg);
		}

		DataTable dt = ds.GetTableByName("Sys_MapData");
		String fk_mapData = dt.Rows.get(0).getValue("No").toString();
		MapData md = new MapData();
		md.setNo(fk_mapData);
		if (md.getIsExits())
		{
			throw new RuntimeException("err@已经存在(" + fk_mapData + ")的表单ID，所以您不能导入。");
		}

		//执行删除操作.
		md.Delete();

		//导入.
		return ImpMapData(fk_mapData, ds);
	}
	/** 
	 设置表单为只读属性
	 
	 param fk_mapdata 表单ID
	*/
	public static void SetFrmIsReadonly(String fk_mapdata) throws Exception {
		//把主表字段设置为只读.
		MapAttrs attrs = new MapAttrs(fk_mapdata);
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getDefValReal().contains("@"))
			{
				attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
				attr.setDefValReal(""); //清空默认值.
				attr.SetValByKey("ExtDefVal", ""); //设置默认值.
				attr.Update();
				continue;
			}

			if (attr.getUIIsEnable() == true)
			{
				attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
				attr.Update();
				continue;
			}
		}

		//把从表字段设置为只读.
		MapDtls dtls = new MapDtls(fk_mapdata);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.setIsInsert(false);
			dtl.setIsUpdate(false);
			dtl.setIsDelete(false);
			dtl.Update();

			attrs = new MapAttrs(dtl.getNo());
			for (MapAttr attr : attrs.ToJavaList())
			{
				if (attr.getDefValReal().contains("@"))
				{
					attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
					attr.setDefValReal(""); //清空默认值.
					attr.SetValByKey("ExtDefVal", ""); //设置默认值.
					attr.Update();
				}

				if (attr.getUIIsEnable() == true)
				{
					attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
					attr.Update();
					continue;
				}
			}
		}

		//把附件设置为只读.
		FrmAttachments aths = new FrmAttachments(fk_mapdata);
		for (FrmAttachment item : aths.ToJavaList())
		{
			item.setIsUpload(false);
			item.setHisDeleteWay(AthDeleteWay.DelSelf);

			//如果是从开始节点表单导入的,就默认为, 按照主键PK的方式显示.
			if (fk_mapdata.indexOf("ND") == 0)
			{
				item.setDataRefNoOfObj("AttachM1");
			}
			item.Update();
		}
	}


	/** 
	 导入表单
	 
	 param specFrmID 指定的表单ID
	 param ds 表单数据
	 param
	 @return 
	*/
	public static MapData ImpMapData(String specFrmID, DataSet ds) throws Exception {

		if (DataType.IsNullOrEmpty(specFrmID) == true)
		{
			ImpMapData(ds);
		}

		//    throw new Exception("err@指定的表单ID - specFrmID 是空.");


			///#region 检查导入的数据是否完整.
		String errMsg = "";
		//if (ds.Tables.get(0).TableName != "Sys_MapData")
		//    errMsg += "@非表单模板。";

		if (ds.contains("WF_Flow") == true)
		{
			errMsg += "@此模板文件为流程模板。";
		}

		if (ds.contains("Sys_MapAttr") == false)
		{
			errMsg += "@缺少表:Sys_MapAttr";
		}

		if (ds.contains("Sys_MapData") == false)
		{
			errMsg += "@缺少表:Sys_MapData";
		}

		DataTable dtCheck = ds.GetTableByName("Sys_MapAttr");
		boolean isHave = false;
		for (DataRow dr : dtCheck.Rows)
		{
			if (dr.getValue("KeyOfEn").toString().equals("OID"))
			{
				isHave = true;
				break;
			}
		}

		if (isHave == false)
		{
			errMsg += "@表单模版缺少列:OID";
		}

		if (!errMsg.equals(""))
		{
			throw new RuntimeException("@以下错误不可导入，可能的原因是非表单模板文件:" + errMsg);
		}

			///#endregion

		// 定义在最后执行的sql.
		String endDoSQL = "";

		MapData mdOld = new MapData();
		mdOld.setNo(specFrmID);
		int count = mdOld.RetrieveFromDBSources();
		if (count == 1)
		{
			mdOld.Delete();
		}


		// 求出dataset的map.
		String oldMapID = "";
		DataTable dtMap = ds.GetTableByName("Sys_MapData");
		if (dtMap.Rows.size() == 1)
		{
			oldMapID = dtMap.Rows.get(0).getValue("No").toString();
		}
		else
		{
			// 求旧的表单ID.
			for (DataRow dr : dtMap.Rows)
			{
				oldMapID = dr.getValue("No").toString();
			}

			if (DataType.IsNullOrEmpty(oldMapID) == true)
			{
				oldMapID = dtMap.Rows.get(0).getValue("No").toString();
			}
		}

		//现在表单的类型
		FrmType frmType = mdOld.getHisFrmType();
		//业务类型
		int entityType = mdOld.getHisEntityType();

		//mdOld.Delete();

		String timeKey = DataType.getCurrentDateByFormart("MMddHHmmss");


			///#region 表单元素
		for (DataTable dt : ds.Tables)
		{
			int idx = 0;
			switch (dt.TableName)
			{
				case "Sys_MapDtl":
					for (DataRow dr : dt.Rows)
					{
						MapDtl dtl = new MapDtl();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							//如果是节点表单，是从表，则从表的名字不修改了.
							if (dc.ColumnName.equals("PTable") == true && val.toString().indexOf("ND") == 0)
							{
								dtl.SetValByKey(dc.ColumnName, val.toString());
							}
							else
							{
								dtl.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
							}
							//编号列.
							String colName = dc.ColumnName.toLowerCase();

							if (colName.equals("no") || colName.equals("name") || colName.equals("fk_mapdata"))
								dtl.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
							else
								dtl.SetValByKey(dc.ColumnName, val.toString());
						}
						dtl.Insert();
					}
					break;
				case "Sys_MapData":
					for (DataRow dr : dt.Rows)
					{
						MapData md = new MapData();
						String htmlCode = "";
						for (DataColumn dc : dt.Columns)
						{
							if (dc.ColumnName.equals("HtmlTemplateFile"))
							{
								htmlCode = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
								continue;
							}
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							String colName = dc.ColumnName.toLowerCase();
							if (colName.equals("no") == true || colName.equals("name")==true)
								md.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
							else
								md.SetValByKey(dc.ColumnName, val.toString());
							//md.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}

						//表单类别编号不为空，则用原表单类别编号
						md.setFK_FormTree(mdOld.getFK_FormTree());

						if (DataType.IsNullOrEmpty(mdOld.getPTable()) == false)
						{
							md.setPTable(mdOld.getPTable());
						}
						//如果物理表为空，则使用编号为物理数据表
						if (DataType.IsNullOrEmpty(md.getPTable().trim()) == true)
						{
							md.setPTable(md.getNo());
						}

						if (DataType.IsNullOrEmpty(mdOld.getName()) == false)
						{
							md.setName(mdOld.getName());
						}

						if (count == 1)
						{
							md.setHisFrmType(mdOld.getHisFrmType());
						}
						else
						{
							frmType = md.getHisFrmType();
						}


						if (entityType != md.getHisEntityType())
						{
							md.setHisEntityType(entityType);
						}

						//表单应用类型保持不变
						md.setAppType(mdOld.getAppType());
						if (md.DirectUpdate() == 0)
						{
							md.DirectInsert();
						}
						Cash2019.UpdateRow(md.toString(), md.getNo().toString(), md.getRow());

						//如果是开发者表单，赋值HtmlTemplateFile数据库的值并保存到DataUser下
						if (frmType == FrmType.Develop)
						{
							// string htmlCode = DBAccess.GetBigTextFromDB("Sys_MapData", "No", oldMapID, "HtmlTemplateFile");
							if (DataType.IsNullOrEmpty(htmlCode) == false)
							{
								htmlCode = htmlCode.replace(oldMapID, specFrmID);
								//保存到数据库，存储html文件
								//保存到DataUser/CCForm/HtmlTemplateFile/文件夹下
								String filePath = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/";
								if ((new File(filePath)).isDirectory() == false)
								{
									(new File(filePath)).mkdirs();
								}
								filePath = filePath + md.getNo() + ".htm";
								//写入到html 中
								DataType.WriteFile(filePath, htmlCode);
								// HtmlTemplateFile 保存到数据库中
								DBAccess.SaveBigTextToDB(htmlCode, "Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
							}
							else
							{
								//如果htmlCode是空的需要删除当前节点的html文件
								String filePath = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/" + md.getNo() + ".htm";
								if ((new File(filePath)).isFile() == true)
								{
									(new File(filePath)).delete();
								}
								DBAccess.SaveBigTextToDB("", "Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
							}
						}
					}
					break;
				case "Sys_FrmBtn":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmBtn en = new FrmBtn();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}

						//en.setMyPK("Btn_" + idx + "_" + fk_mapdata;
						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
					break;
				case "Sys_FrmImg":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}
						if (DataType.IsNullOrEmpty(en.getKeyOfEn()) == true)
						{
							en.setMyPK(DBAccess.GenerGUID());
						}

						en.Insert();
					}
					break;
				case "Sys_FrmImgAth":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImgAth en = new FrmImgAth();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}

						if (DataType.IsNullOrEmpty(en.getCtrlID()))
						{
							en.setCtrlID("ath" + idx);
						}

						en.Insert();
					}
					break;
				case "Sys_FrmRB":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmRB en = new FrmRB();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}

						try
						{
							en.Save();
						}
						catch (java.lang.Exception e)
						{
						}
					}
					break;
				case "Sys_FrmAttachment":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmAttachment en = new FrmAttachment();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}
						en.setMyPK(specFrmID + "_" + en.GetValByKey("NoOfObj"));


						try
						{
							en.Insert();
						}
						catch (java.lang.Exception e2)
						{
						}
					}
					break;
				case "Sys_MapFrame":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapFrame en = new MapFrame();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}


							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}
						en.DirectInsert();
					}
					break;
				case "Sys_MapExt":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapExt en = new MapExt();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							if (DataType.IsNullOrEmpty(val.toString()) == true)
							{
								continue;
							}
							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}

						//执行保存，并统一生成PK的规则.
						en.InitPK();
						en.DirectSave();
					}
					break;
				case "Sys_MapAttr":
					for (DataRow dr : dt.Rows)
					{
						MapAttr en = new MapAttr();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
						}

						en.setMyPK(en.getFK_MapData() + "_" + en.getKeyOfEn());

						//直接插入.
						try
						{
							en.DirectInsert();
							//判断该字段是否是大文本 例如注释、说明
							if (en.getUIContralType() == UIContralType.BigText)
							{
								//判断原文件是否存在
								String file = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/BigNoteHtmlText/" + oldMapID + ".htm";
								//若文件存在，则复制                                  
								if ((new File(file)).isFile() == true)
								{
									String newFile = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/BigNoteHtmlText/" + specFrmID + ".htm";
									if ((new File(newFile)).isFile() == true)
									{
										(new File(newFile)).delete();
									}
									Files.copy(Paths.get(file), Paths.get(newFile), StandardCopyOption.COPY_ATTRIBUTES);
								}

							}
						}
						catch (java.lang.Exception e3)
						{
						}
					}
					break;
				case "Sys_GroupField":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						GroupField en = new GroupField();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							try
							{
								en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, specFrmID));
							}
							catch (java.lang.Exception e4)
							{
								throw new RuntimeException("val:" + val.toString() + "oldMapID:" + oldMapID + "fk_mapdata:" + specFrmID);
							}
						}
						long beforeID = en.getOID();
						en.setOID(0);
						en.DirectInsert();
						endDoSQL += "@UPDATE Sys_MapAttr SET GroupID=" + en.getOID() + " WHERE FK_MapData='" + specFrmID + "' AND GroupID='" + beforeID + "'";
					}
					break;
				case "Sys_Enum":
				case "Sys_Enums":
					for (DataRow dr : dt.Rows)
					{
						SysEnum se = new bp.sys.SysEnum();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							se.SetValByKey(dc.ColumnName, val);
						}
						if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
						{
							se.setOrgNo(bp.web.WebUser.getOrgNo());
							//  se.setRefPK(se.OrgNo + "_" + se.EnumKey;
							se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey() + "_" + se.getOrgNo());
							if (se.getIsExits())
							{
								continue;
							}
						}
						else
						{
							se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
							if (se.getIsExits())
							{
								continue;
							}
						}
						se.Insert();
					}
					break;
				case "Sys_EnumMain":
					for (DataRow dr : dt.Rows)
					{
						SysEnumMain sem = new bp.sys.SysEnumMain();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							sem.SetValByKey(dc.ColumnName, val);
						}

						if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
						{
							sem.setOrgNo(bp.web.WebUser.getOrgNo());
							sem.setNo(sem.getOrgNo() + "_" + sem.getEnumKey());
							if (sem.getIsExits())
							{
								continue;
							}
						}
						else
						{
							if (sem.getIsExits())
							{
								continue;
							}
						}
						sem.Insert();
					}
					break;
				case "WF_Node":
					if (dt.Rows.size() > 0)
					{
						endDoSQL += "@UPDATE WF_Node SET FWCSta=2" + ",FWC_X=" + dt.Rows.get(0).getValue("FWC_X") + ",FWC_Y=" + dt.Rows.get(0).getValue("FWC_Y") + ",FWC_H=" + dt.Rows.get(0).getValue("FWC_H") + ",FWC_W=" + dt.Rows.get(0).getValue("FWC_W") + ",FWCType=" + dt.Rows.get(0).getValue("FWCType") + " WHERE NodeID=" + specFrmID.replace("ND", "");
					}
					break;
				default:
					break;
			}
		}

			///#endregion

		//执行最后结束的sql.
		DBAccess.RunSQLs(endDoSQL);

		MapData mdNew = new MapData(specFrmID);
		mdNew.RepairMap();

		if (mdNew.getNo().indexOf("ND") == 0)
		{
			mdNew.setFK_FormTree("");
		}

		mdNew.Update();
		return mdNew;
	}
	/** 
	 修复map.
	*/
	public final void RepairMap() throws Exception {
		GroupFields gfs = new GroupFields(this.getNo());
		if (gfs.size() == 0)
		{
			GroupField gf = new GroupField();
			gf.setFrmID(this.getNo());
			gf.setLab(this.getName());
			gf.Insert();

			String sqls = "";
			sqls += "@UPDATE Sys_MapDtl SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			//sqls += "@UPDATE Sys_MapFrame SET GroupID=" + gf.OID + " WHERE FK_MapData='" + this.No + "'";
			sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			DBAccess.RunSQLs(sqls);
		}
		else
		{
			if (bp.difference.SystemConfig.getAppCenterDBType() != DBType.Oracle)
			{
				GroupField gfFirst = gfs.get(0) instanceof GroupField ? (GroupField)gfs.get(0) : null;

				String sqls = "";
				//   sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gfFirst.OID + "       WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_MapAttr       WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.No + "') or GroupID is null) AS X) AND FK_MapData='" + this.No + "' ";
				sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gfFirst.getOID() + " WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_FrmAttachment WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.getNo() + "')) AS X) AND FK_MapData='" + this.getNo() + "' ";


///#warning 这些sql 对于Oracle 有问题，但是不影响使用.
				try
				{
					DBAccess.RunSQLs(sqls);
				}
				catch (java.lang.Exception e)
				{
				}
			}
		}

		bp.sys.MapAttr attr = new bp.sys.MapAttr();
		if (this.getEnPK().equals("OID"))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, this.getNo()) == false)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn("OID");
				attr.setName("OID");
				attr.setMyDataType(DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.SetValByKey(MapAttrAttr.UIVisible, false);
				attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
				attr.setDefVal("0");
				attr.setHisEditType(EditType.Readonly);
				attr.Insert();
			}
		}
		if (this.getEnPK().equals("No") || this.getEnPK().equals("MyPK"))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getEnPK(), MapAttrAttr.FK_MapData, this.getNo()) == false)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn(this.getEnPK());
				attr.setName(this.getEnPK());
				attr.setMyDataType(DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.SetValByKey(MapAttrAttr.UIVisible, false);
				attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
				attr.setDefVal("0");
				attr.setHisEditType(EditType.Readonly);
				attr.Insert();
			}
		}

		if (attr.IsExit(MapAttrAttr.KeyOfEn, "RDT", MapAttrAttr.FK_MapData, this.getNo()) == false)
		{
			attr = new bp.sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setEditType(EditType.UnDel);
			attr.setKeyOfEn("RDT");
			attr.setName("更新时间");
			attr.setGroupID(0);
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.SetValByKey(MapAttrAttr.UIVisible, false);
			attr.SetValByKey(MapAttrAttr.UIIsEnable, false);
			attr.setDefVal("@RDT");
			attr.setTag("1");
			attr.Insert();
		}

		//检查特殊UIBindkey丢失的问题.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, this.getNo());
		for (MapAttr item : attrs.ToJavaList())
		{
			if (item.getLGType() == FieldTypeS.Enum || item.getLGType() == FieldTypeS.FK)
			{
				if (DataType.IsNullOrEmpty(item.getUIBindKey()) == true)
				{
					item.setLGType(FieldTypeS.Normal);
					item.setUIContralType(UIContralType.TB);
					item.Update();
				}
			}
		}

	}
	@Override
	protected boolean beforeInsert() throws Exception {
		if (this.getHisFrmType() == FrmType.Url || this.getHisFrmType() == FrmType.Entity)
		{

		}
		else
		{
			this.setPTable(PubClass.DealToFieldOrTableNames(this.getPTable()));
		}


		//写入日志.
		if (this.getNo().startsWith("ND") == false)
		{
			Glo.WriteUserLog("新建表单：" + this.getNo() + " - " + this.getName(),null,null);
		}

		this.setVer2022(1);
		return super.beforeInsert();
	}
	/** 
	 创建MapData后插入一条版本数据
	*/
	@Override
	protected void afterInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getFK_FormTree()) == true)
		{
			super.afterInsert();
			return;
		}

		MapDataVer ver = new MapDataVer();
		ver.setMyPK(this.getNo() + ".1");

		ver.setVer(1); //设置当前为主版本.
		ver.setFrmID(this.getNo()); //设置表单ID.
		ver.setRel(1); //设置为主版本.

		ver.setRec(bp.web.WebUser.getNo());
		ver.setRecName(bp.web.WebUser.getName());
		ver.setRDT(DataType.getCurrentDateTime());

		//设置数量.
		ver.setAttrsNum(0);
		ver.setAthsNum(0);
		ver.setDtlsNum(0);
		ver.setExtsNum(0);
		ver.Insert();
		super.afterInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (this.getHisFrmType() == FrmType.Url || this.getHisFrmType() == FrmType.Entity)
		{
			return super.beforeUpdateInsertAction();
		}

		//clear外键实体数量的缓存.
		this.ClearAutoNumCash(false);

		this.setPTable(PubClass.DealToFieldOrTableNames(this.getPTable()));

		//修改2021-09-04 注释，不知道这个代码的作用
		//MapAttrs.Retrieve(MapAttrAttr.FK_MapData, PTable);

		//设置OrgNo. 如果是管理员，就设置他所在的部门编号。
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}

		//判断是否有多个主键字段?
		//  string sql = "SELECT ";

		//检查主键.
		CheckPKFields(this.getNo(), this.getName());

		//清除缓存.
		this.ClearCash();

		return super.beforeUpdateInsertAction();
	}

	public static String CheckPKFields(String frmID, String name)
	{
		String sql = "SELECT KeyOfEn, Name FROM Sys_MapAttr WHERE FK_MapData='" + frmID + "' ";
		sql += "  AND ( ";
		sql += "  KeyOfEn='OID'   ";
		sql += "  OR KeyOfEn='No'   ";
		sql += "  OR KeyOfEn='MyPK'   ";
		sql += "  OR KeyOfEn='NodeID'   ";
		sql += "  OR KeyOfEn='WorkID'   ";
		sql += "  ) ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (dt.Rows.size() == 1)
		{
			return "数据正确.";
		}

		if (dt.Rows.size() == 0)
		{
			return "err@表单缺少主键." + frmID + "," + name;
		}

		String msg = "err@FrmID=" + frmID + name + "主键不明确.";
		for (DataRow dr : dt.Rows)
		{
			msg += "@" + dr.getValue(0) + "," + dr.getValue(1).toString();
		}
		return msg;
	}
	/** 
	 更新版本
	*/
	public final void UpdateVer() throws Exception {
		String sql = "UPDATE Sys_MapData SET VER='" + DataType.getCurrentDateTimess() + "' WHERE No='" + this.getNo() + "'";
		DBAccess.RunSQL(sql);
	}
	@Override
	protected boolean beforeDelete() throws Exception {

			///#region 判断是否是节点表单？如果是，判断节点是否被删除了.
		//if (this.No.StartsWith("ND") == true)
		//{
		//    string frmID = this.No.Replace("ND", "");
		//    if (bp.da.DataType.IsNumStr(frmID) == true)
		//    {
		//        int nodeID = int.Parse(frmID);
		//        int count = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) as NUM FROM WF_Node WHERE NodeID=" + nodeID);
		//        if (count == 1)
		//            throw new Exception("err@删除节点表单前，需要删除节点数据."+this.No+" - " +this.Name);
		//    }
		//}

			///#endregion 判断是否是节点表单？如果是，判断节点是否被删除了.


			///#region 检查完整性.
		String sql = "";
		//如果存在版本就不能删除
		sql = "SELECT count(*) From Sys_MapDataVer Where FrmID='" + this.getNo() + "' AND Ver!=" + this.getVer2022();

		try
		{
			if (DBAccess.RunSQLReturnValInt(sql) > 0)
			{
				throw new RuntimeException("表单存在其他的版本,请删除其他版本后再删除表单");
			}
		}
		catch (RuntimeException ex)
		{
			MapDataVer mv = new MapDataVer();
			mv.CheckPhysicsTable();
			if (DBAccess.RunSQLReturnValInt(sql) > 0)
			{
				throw new RuntimeException("表单存在其他的版本,请删除其他版本后再删除表单");
			}
		}

		//如果当前版本是主版本，需要删除主版本的信息
		sql = "SELECT Ver From Sys_MapDataVer WHERE FrmID='" + this.getNo() + "' AND IsRel=1";
		int ver = DBAccess.RunSQLReturnValInt(sql, 0);
		if (ver == this.getVer2022())
		{
			//删除主版本的信息
			MapData md = new MapData();
			md.setNo(this.getNo() + "." + ver);
			if (md.RetrieveFromDBSources() == 1)
			{
				md.Delete();
			}
			DBAccess.RunSQL("DELETE FROM Sys_MapDataVer WHERE MyPK='" + md.getNo() + "'");

		}

		sql = "";
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='" + this.getNo() + "'";
		DataTable Sys_MapDtl = DBAccess.RunSQLReturnTable(sql);

		String whereFK_MapData = "FK_MapData= '" + this.getNo() + "' ";
		String whereEnsName = "FrmID= '" + this.getNo() + "' ";
		String whereNo = "No='" + this.getNo() + "' ";

		for (DataRow dr : Sys_MapDtl.Rows)
		{
			// ids += ",'" + dr["No"] + "'";
			whereFK_MapData += " OR FK_MapData='" + dr.getValue("No") + "' ";
			whereEnsName += " OR FrmID='" + dr.getValue("No") + "' ";
			whereNo += " OR No='" + dr.getValue("No") + "' ";
		}

			///#endregion 检查完整性.


			///#region 删除相关的数据。
		sql = "DELETE FROM Sys_MapDtl WHERE FK_MapData='" + this.getNo() + "'";
		//  sql += "@DELETE FROM Sys_FrmLine WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmEvent WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmBtn WHERE " + whereFK_MapData;
		// sql += "@DELETE FROM Sys_FrmLab WHERE " + whereFK_MapData;
		//sql += "@DELETE FROM Sys_FrmLink WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmImg WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmImgAth WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmRB WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmAttachment WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_MapFrame WHERE " + whereFK_MapData;

		if (this.getNo().contains("BP.") == false)
		{
			sql += "@DELETE FROM Sys_MapExt WHERE " + whereFK_MapData;
		}

		sql += "@DELETE FROM Sys_MapAttr WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_GroupField WHERE " + whereEnsName;
		sql += "@DELETE FROM Sys_MapData WHERE " + whereNo;
		// sql += "@DELETE FROM Sys_M2M WHERE " + whereFK_MapData;
		sql += "@DELETE FROM WF_FrmNode WHERE FK_Frm='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmSln WHERE " + whereFK_MapData;
		//sql += "@DELETE FROM Sys_MapDataVer WHERE " + whereFK_MapData;

		DBAccess.RunSQLs(sql);

			///#endregion 删除相关的数据。


			///#region 删除物理表。
		//如果存在物理表.
		if (DBAccess.IsExitsObject(this.getPTable()) && this.getPTable().indexOf("ND") == 0)
		{
			//如果其他表单引用了该表，就不能删除它.
			sql = "SELECT COUNT(No) AS NUM  FROM Sys_MapData WHERE PTable='" + this.getPTable() + "' OR ( PTable='' AND No='" + this.getPTable() + "')";
			if (DBAccess.RunSQLReturnValInt(sql, 0) > 1)
			{
				/*说明有多个表单在引用.*/
			}
			else
			{
				// edit by zhoupeng 误删已经有数据的表.
				if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + this.getPTable() + " WHERE 1=1 ") == 0)
				{
					DBAccess.RunSQL("DROP TABLE " + this.getPTable());
				}
			}
		}
		MapDtls dtls = new MapDtls(this.getNo());
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.Delete();
		}


			///#endregion


			///#region 删除注册到的外检表.
		SFTables sfs = new SFTables();
		sfs.Retrieve(SFTableAttr.SrcTable, this.getPTable());
		for (SFTable item : sfs.ToJavaList())
		{
			if (item.IsCanDelete() == null)
			{
				item.Delete();
			}
		}

			///#endregion 删除注册到的外检表.


			///#region 属性.

			///#endregion 属性.

		//写入日志.
		if (this.getNo().startsWith("ND") == false)
		{
			Glo.WriteUserLog("删除表单：" + this.getNo() + " - " + this.getName(),null,null);
		}

		return super.beforeDelete();
	}

		///#endregion 常用方法.


		///#region 与Excel相关的操作 .
	/** 
	 获得Excel文件流
	 
	 param
	 @return 
	*/

//ORIGINAL LINE: public bool ExcelGenerFile(string pkValue, ref byte[] bytes, string saveTo)
	public final boolean ExcelGenerFile(String pkValue, RefObject<byte[]> bytes, String saveTo)
	{
		try
		{

//ORIGINAL LINE: byte[] by = DBAccess.GetByteFromDB(this.PTable, this.EnPK, pkValue, saveTo);
			byte[] by = DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), pkValue, saveTo);
			if (by != null)
			{
				bytes.argvalue = by;
				return true;
			}
			else //说明当前excel文件没有生成.
			{
				String tempExcel = bp.difference.SystemConfig.getPathOfDataUser() + "FrmVSTOTemplate/" + this.getNo() + ".xlsx";
				if ((new File(tempExcel)).isFile() == true)
				{
					bytes.argvalue = DataType.ConvertFileToByte(tempExcel);
					return false;
				}
				else //模板文件也不存在时
				{
					throw new RuntimeException("@没有找到模版文件." + tempExcel + " 请确认表单配置.");
				}
			}
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError("读取excel失败：" + ex.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/** 
	 保存excel文件
	 
	 param
	 param bty
	*/

//ORIGINAL LINE: public void ExcelSaveFile(string pkValue, byte[] bty, string saveTo)
	public final void ExcelSaveFile(String pkValue, byte[] bty, String saveTo) throws Exception {
		DBAccess.SaveBytesToDB(bty, this.getPTable(), this.getEnPK(), pkValue, saveTo);
	}

		///#endregion 与Excel相关的操作 .


		///#region 与Word相关的操作 .
	/** 
	 获得Excel文件流
	 
	 param
	 @return 
	*/

//ORIGINAL LINE: public void WordGenerFile(string pkValue, ref byte[] bytes, string saveTo)
	public final void WordGenerFile(String pkValue, RefObject<byte[]> bytes, String saveTo) throws Exception {

//ORIGINAL LINE: byte[] by = DBAccess.GetByteFromDB(this.PTable, this.EnPK, pkValue, saveTo);
		byte[] by = DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), pkValue, saveTo);
		if (by != null)
		{
			bytes.argvalue = by;
			return;
		}
		else //说明当前excel文件没有生成.
		{
			String tempExcel = bp.difference.SystemConfig.getPathOfDataUser() + "FrmVSTOTemplate/" + this.getNo() + ".docx";

			if ((new File(tempExcel)).isFile() == false)
			{
				tempExcel = bp.difference.SystemConfig.getPathOfDataUser() + "FrmVSTOTemplate/NDxxxRpt.docx";
			}

			bytes.argvalue = DataType.ConvertFileToByte(tempExcel);
			return;
		}
	}
	/** 
	 保存excel文件
	 
	 param
	 param bty
	*/

//ORIGINAL LINE: public void WordSaveFile(string pkValue, byte[] bty, string saveTo)
	public final void WordSaveFile(String pkValue, byte[] bty, String saveTo) throws Exception {
		DBAccess.SaveBytesToDB(bty, this.getPTable(), this.getEnPK(), pkValue, saveTo);
	}

		///#endregion 与Excel相关的操作 .

	/** 
	 创建版本
	*/
	public final String CreateMapDataVer() throws Exception {
		//创建版本之前先判断当前版本是不是有数据
		if (DBAccess.IsExitsObject(this.getPTable()) == false)
		{
			//MapData md = new MapData(this.No);
			GEEntity ge = new GEEntity(this.getNo());
			ge.CheckPhysicsTable();
		}

		//获得最大的版本数.
		int count = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) From " + this.getPTable() + " WHERE AtPara NOT LIKE '%@FrmVer=%'");
		if (count == 0)
		{
			return "表单" + this.getName() + "版本" + this.getVer2022() + "还没有使用，不用创建新的版本。";
		}

		MapDataVer ver = new MapDataVer();

		MapDataVers vers = new MapDataVers();
		vers.Retrieve("FrmID", this.getNo());
		if (vers.size() == 0)
		{
			ver.setMyPK(this.getNo() + ".1");

			ver.setVer(1); //设置当前为主版本.
			ver.setFrmID(this.getNo()); //设置表单ID.
			ver.setRel(1); //设置为主版本.

			ver.setRec(bp.web.WebUser.getNo());
			ver.setRecName(bp.web.WebUser.getName());
			ver.setRDT(DataType.getCurrentDateTime());

			//设置数量.
			ver.setAttrsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "'"));
			ver.setAthsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_Frmattachment WHERE FK_MapData='" + this.getNo() + "'"));
			ver.setDtlsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapDtl WHERE FK_MapData='" + this.getNo() + "'"));
			ver.setExtsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapExt WHERE FK_MapData='" + this.getNo() + "'"));
			ver.Insert();

			this.setVer2022(ver.getVer()); //更新当前的版本.
			this.Update();

			//执行复制,表单.
			bp.sys.CCFormAPI.CopyFrm(this.getNo(), this.getNo() + "." + this.getVer2022(), this.getName() + "." + ver.getVer(), this.getFK_FormTree());

			return "创建成功." + ver.GetValByKey(MapDataVerAttr.Ver);
		}

		//缺少AtPara字段
		MapAttr mattr = new MapAttr();
		mattr.setMyPK(this.getNo() + "_AtPara");
		if (mattr.RetrieveFromDBSources() == 0)
		{
			mattr.setFK_MapData(this.getNo());
			mattr.setHisEditType(EditType.UnDel);
			mattr.setKeyOfEn("AtPara");
			mattr.setName("参数"); // 单据编号
			mattr.setMyDataType(DataType.AppString);
			mattr.setUIContralType(UIContralType.TB);
			mattr.setLGType(FieldTypeS.Normal);
			mattr.setUIVisible(false);
			mattr.setUIIsEnable(false);
			mattr.setUIIsLine(false);
			mattr.setMinLen(0);
			mattr.setMaxLen(4000);
			mattr.setIdx(-100);
			mattr.Insert();
			GEEntity en = new GEEntity(this.getNo());
			en.CheckPhysicsTable();

		}
		//设置所有的版本为 0 .
		String sql = "UPDATE Sys_MapDataVer SET IsRel=0 WHERE FrmID='" + this.getNo() + "'";
		DBAccess.RunSQL(sql);


			///#region 1. 创建新版本 执行复制,表单.

		int maxVer = DBAccess.RunSQLReturnValInt("SELECT MAX(ver) FROM Sys_MapDataVer WHERE FrmID='" + this.getNo() + "'", 0);
		//执行复制,表单，创建新版本.
		ver.setVer(maxVer + 1);
		ver.setMyPK(this.getNo() + "." + ver.getVer());
		ver.setFrmID(this.getNo()); //设置表单ID.
		ver.setRel(1); //设置为主版本.
		ver.setRec(bp.web.WebUser.getNo());
		ver.setRecName(bp.web.WebUser.getName());
		ver.setRDT(DataType.getCurrentDateTime());

		//设置数量.
		ver.setAttrsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "'"));
		ver.setAthsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_Frmattachment WHERE FK_MapData='" + this.getNo() + "'"));
		ver.setDtlsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapDtl WHERE FK_MapData='" + this.getNo() + "'"));
		ver.setExtsNum(DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapExt WHERE FK_MapData='" + this.getNo() + "'"));
		ver.Insert(); //创建新版本.

		//生成新的表单.
		bp.sys.CCFormAPI.CopyFrm(this.getNo(), this.getNo() + "." + ver.getVer(), this.getName() + "(Ver" + ver.getVer() + ".0)", this.getFK_FormTree());
		//把版本的FK_FormTree清空
		MapData md = new MapData(this.getNo() + "." + ver.getVer());
		md.setFK_FormTree("");
		md.Update();


			///#endregion 1. 创建新版本 执行复制,表单.


			///#region 2. 覆盖旧版本.
		String currVer = this.getNo() + "." + String.valueOf(this.getVer2022()); // this.No + "." + vers.size();
		md = new MapData();
		md.setNo(currVer);
		if (md.RetrieveFromDBSources() == 1)
		{
			md.Delete();
		}
		//把表单属性的FK_FormTree清空
		bp.sys.CCFormAPI.CopyFrm(this.getNo(), currVer, this.getName() + "(Ver" + String.valueOf(this.getVer2022()) + ".0)", this.getFK_FormTree());
		md.Retrieve();
		md.setFK_FormTree("");
		md.setPTable(this.getPTable());
		md.Update();
		//修改从表的存储表
		MapDtls dtls = md.getMapDtls();
		for (MapDtl dtl : dtls.ToJavaList())
		{
			if (dtl.getPTable().equals(dtl.getNo()) == true)
			{
				dtl.setPTable(dtl.getPTable().replace(currVer, this.getNo()));
				dtl.Update();
				continue;
			}
		}
		//把当前表单对应数据改成当前的版本
		DBAccess.RunSQL("UPDATE " + md.getPTable() + " SET AtPara=CONCAT(AtPara,'@FrmVer=" + this.getVer2022() + "') WHERE AtPara NOT LIKE '%@FrmVer=%'");


			///#endregion 2. 覆盖旧版本.


			///#region 3. 更新当前版本号.
		this.setVer2022(ver.getVer()); //更新当前的版本.
		this.Update();

			///#endregion 3. 更新当前版本号.

		return "创建成功，版本号:" + ver.getVer();

	}
}
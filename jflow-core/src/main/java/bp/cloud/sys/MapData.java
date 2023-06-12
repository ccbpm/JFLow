package bp.cloud.sys;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.sys.base.*;
import bp.wf.RefObject;

import java.io.File;

/**
 * 映射基础
 */
public class MapData extends EntityNoName {

    ///#region entity 相关属性(参数属性)

    /**
     * 属性ens
     */
    public final String getEnsName() {
        return this.GetValStringByKey(MapDataAttr.EnsName);
    }

    public final void setEnsName(String value) {
        this.SetPara(MapDataAttr.EnsName, value);
    }

    ///#endregion entity 相关操作.


    ///#region 自动计算属性.
    public final float getMaxLeft() {
        return this.GetParaFloat(MapDataAttr.MaxLeft);
    }

    public final void setMaxLeft(float value) {
        this.SetPara(MapDataAttr.MaxLeft, value);
    }

    public final float getMaxRight() {
        return this.GetParaFloat(MapDataAttr.MaxRight);
    }

    public final void setMaxRight(float value) {
        this.SetPara(MapDataAttr.MaxRight, value);
    }

    public final float getMaxTop() {
        return this.GetParaFloat(MapDataAttr.MaxTop);
    }

    public final void setMaxTop(float value) {
        this.SetPara(MapDataAttr.MaxTop, value);
    }

    public final float getMaxEnd() {
        return this.GetParaFloat(MapDataAttr.MaxEnd);
    }

    public final void setMaxEnd(float value) {
        this.SetPara(MapDataAttr.MaxEnd, value);
    }

    ///#endregion 自动计算属性.


    ///#region 报表属性(参数方式存储).

    /**
     * 是否关键字查询
     */
    public final boolean getIsSearchKey() {
        return this.GetParaBoolen(MapDataAttr.IsSearchKey, true);
    }

    public final void setIsSearchKey(boolean value) {
        this.SetPara(MapDataAttr.IsSearchKey, value);
    }

    /**
     * 时间段查询方式
     */
    public final DTSearchWay getDTSearchWay() {
        return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.DTSearchWay));
    }

    public final void setDTSearchWay(DTSearchWay value) {
        this.SetPara(MapDataAttr.DTSearchWay, value.getValue());
    }

    /**
     * 时间字段
     */
    public final String getDTSearchKey() {
        return this.GetParaString(MapDataAttr.DTSearchKey);
    }

    public final void setDTSearchKey(String value) {
        this.SetPara(MapDataAttr.DTSearchKey, value);
    }

    /**
     * 查询外键枚举字段
     */
    public final String getRptSearchKeys() {
        return this.GetParaString(MapDataAttr.RptSearchKeys, "*");
    }

    public final void setRptSearchKeys(String value) {
        this.SetPara(MapDataAttr.RptSearchKeys, value);
    }

    ///#endregion 报表属性(参数方式存储).


    ///#region 外键属性

    /**
     * 版本号.
     */
    public final String getVer() {
        return this.GetValStringByKey(MapDataAttr.Ver);
    }

    public final void setVer(String value) {
        this.SetValByKey(MapDataAttr.Ver, value);
    }

    public final String getOrgNo() {
        return this.GetValStringByKey(MapDataAttr.OrgNo);
    }

    public final void setOrgNo(String value) {
        this.SetValByKey(MapDataAttr.OrgNo, value);
    }

    /**
     * 顺序号
     */
    public final int getIdx() {
        return this.GetValIntByKey(MapDataAttr.Idx);
    }

    public final void setIdx(int value) {
        this.SetValByKey(MapDataAttr.Idx, value);
    }

    /**
     * 框架
     */
    public final MapFrames getMapFrames() throws Exception {
        Object tempVar = this.GetRefObject("MapFrames");
        MapFrames obj = (MapFrames) ((tempVar instanceof MapFrames) ? tempVar : null);
        if (obj == null) {
            obj = new MapFrames(this.getNo());
            this.SetRefObject("MapFrames", obj);
        }
        return obj;
    }

    /**
     * 分组字段
     */
    public final GroupFields getGroupFields() throws Exception {
        Object tempVar = this.GetRefObject("GroupFields");
        GroupFields obj = (GroupFields) ((tempVar instanceof GroupFields) ? tempVar : null);
        if (obj == null) {
            obj = new GroupFields(this.getNo());
            this.SetRefObject("GroupFields", obj);
        }
        return obj;
    }

    /**
     * 逻辑扩展
     */
    public final MapExts getMapExts() throws Exception {
        Object tempVar = this.GetRefObject("MapExts");
        MapExts obj = (MapExts) ((tempVar instanceof MapExts) ? tempVar : null);
        if (obj == null) {
            obj = new MapExts(this.getNo());
            this.SetRefObject("MapExts", obj);
        }
        return obj;
    }

    /**
     * 事件
     */
    public final FrmEvents getFrmEvents() throws Exception {
        Object tempVar = this.GetRefObject("FrmEvents");
        FrmEvents obj = (FrmEvents) ((tempVar instanceof FrmEvents) ? tempVar : null);
        if (obj == null) {
            obj = new FrmEvents(this.getNo());
            this.SetRefObject("FrmEvents", obj);
        }
        return obj;
    }

    /**
     * 从表原始属性的获取
     */
    public final MapDtls getOrigMapDtls() throws Exception {
        Object tempVar = this.GetRefObject("MapDtls");
        MapDtls obj = (MapDtls) ((tempVar instanceof MapDtls) ? tempVar : null);
        if (obj == null) {
            obj = new MapDtls();
            obj.Retrieve(MapDtlAttr.FK_MapData, this.getNo(), MapDtlAttr.FK_Node, 0);
            this.SetRefObject("MapDtls", obj);
        }
        return obj;
    }

    /**
     * 查询给MapData下的所有从表数据
     */
    public final MapDtls getMapDtls() throws Exception {
        Object tempVar = this.GetRefObject("MapDtls");
        MapDtls obj = (MapDtls) ((tempVar instanceof MapDtls) ? tempVar : null);
        if (obj == null) {
            obj = new MapDtls(this.getNo());
            this.SetRefObject("MapDtls", obj);
        }
        return obj;
    }

    /**
     * 枚举值
     */
    public final SysEnums getSysEnums() throws Exception {
        Object tempVar = this.GetRefObject("SysEnums");
        SysEnums obj = (SysEnums) ((tempVar instanceof SysEnums) ? tempVar : null);
        if (obj == null) {
            obj = new SysEnums();
            if (bp.difference.SystemConfig.getAppCenterDBType() == DBType.MySQL) {
                String strs = "";

                DataTable dt = DBAccess.RunSQLReturnTable("SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' AND LGType=1  ");

                for (DataRow dr : dt.Rows) {
                    strs += "'" + dr.getValue(0).toString() + "',";
                }

                if (dt.Rows.size() >= 1) {
                    strs += "'ssss'";
                    obj.RetrieveInOrderBy("EnumKey", strs, SysEnumAttr.IntKey);
                }


            } else {
                obj.RetrieveInSQL(SysEnumAttr.EnumKey, "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' AND LGType=1 ", SysEnumAttr.IntKey);
            }
            this.SetRefObject("SysEnums", obj);

        }
        return obj;
    }

    /**
     * 图片
     */
    public final FrmImgs getFrmImgs() throws Exception {
        Object tempVar = this.GetRefObject("FrmImgs");
        FrmImgs obj = (FrmImgs) ((tempVar instanceof FrmImgs) ? tempVar : null);
        if (obj == null) {
            obj = new FrmImgs(this.getNo());
            this.SetRefObject("FrmImgs", obj);
        }
        return obj;
    }

    /**
     * 附件
     */
    public final FrmAttachments getFrmAttachments() throws Exception {
        Object tempVar = this.GetRefObject("FrmAttachments");
        FrmAttachments obj = (FrmAttachments) ((tempVar instanceof FrmAttachments) ? tempVar : null);
        if (obj == null) {
            obj = new FrmAttachments(this.getNo());
            this.SetRefObject("FrmAttachments", obj);
        }
        return obj;
    }

    /**
     * 图片附件
     */
    public final FrmImgAths getFrmImgAths() throws Exception {
        Object tempVar = this.GetRefObject("FrmImgAths");
        FrmImgAths obj = (FrmImgAths) ((tempVar instanceof FrmImgAths) ? tempVar : null);
        if (obj == null) {
            obj = new FrmImgAths(this.getNo());
            this.SetRefObject("FrmImgAths", obj);
        }
        return obj;
    }

    // <summary>

    /**
     * 图片附件记录
     */
    public final FrmImgAthDBs getFrmImgAthDB() throws Exception {
        Object tempVar = this.GetRefObject("FrmImgAthDBs");
        FrmImgAthDBs obj = (FrmImgAthDBs) ((tempVar instanceof FrmImgAthDBs) ? tempVar : null);
        if (obj == null) {
            obj = new FrmImgAthDBs(this.getNo());
            this.SetRefObject("FrmImgAthDBs", obj);
        }
        return obj;
    }

    /**
     * 单选按钮
     */
    public final FrmRBs getFrmRBs() throws Exception {
        Object tempVar = this.GetRefObject("FrmRBs");
        FrmRBs obj = (FrmRBs) ((tempVar instanceof FrmRBs) ? tempVar : null);
        if (obj == null) {
            obj = new FrmRBs(this.getNo());
            this.SetRefObject("FrmRBs", obj);
        }
        return obj;
    }

    /**
     * 属性
     */
    public final MapAttrs getMapAttrs() throws Exception {
        Object tempVar = this.GetRefObject("MapAttrs");
        MapAttrs obj = (MapAttrs) ((tempVar instanceof MapAttrs) ? tempVar : null);
        if (obj == null) {
            obj = new MapAttrs(this.getNo());
            this.SetRefObject("MapAttrs", obj);
        }
        return obj;
    }

    ///#endregion

    public final void CleanObject() {
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
        //  this.getRow().SetValByKey("FrmLines", null);
        //  this.getRow().SetValByKey("FrmLabs", null);
        this.getRow().SetValByKey("FrmAttachments", null);
        this.getRow().SetValByKey("FrmImgAthDBs", null);
        this.getRow().SetValByKey("FrmRBs", null);
        this.getRow().SetValByKey("MapAttrs", null);
        return;
    }

    /**
     * 清空缓存
     */
    public final void ClearCash() {
        bp.da.CashFrmTemplate.Remove(this.getNo());
        bp.da.Cash.SetMap(this.getNo(), null);
        CleanObject();
        bp.da.Cash.getSQL_Cash().remove(this.getNo());
    }


    ///#region 基本属性.

    /**
     * 事件实体
     */
    public final String getFormEventEntity() {
        return this.GetValStringByKey(MapDataAttr.FormEventEntity);
    }

    public final void setFormEventEntity(String value) {
        this.SetValByKey(MapDataAttr.FormEventEntity, value);
    }

    public static boolean getIsEditDtlModel() {
        String s = bp.web.WebUser.GetSessionByKey("IsEditDtlModel", "0");
        if (s.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    public static void setIsEditDtlModel(boolean value) {
        bp.web.WebUser.SetSessionByKey("IsEditDtlModel", "1");
    }

    ///#endregion 基本属性.


    ///#region 属性

    /**
     * 物理表
     */
    public final String getPTable() {
        String s = this.GetValStrByKey(MapDataAttr.PTable);
        if (DataType.IsNullOrEmpty(s) == true) {
            return this.getNo();
        }
        return s;
    }

    public final void setPTable(String value) {
        this.SetValByKey(MapDataAttr.PTable, value);
    }

    /**
     * URL
     */
    public final String getUrl() {
        return this.GetValStrByKey(MapDataAttr.Url);
    }

    public final void setUrl(String value) {
        this.SetValByKey(MapDataAttr.Url, value);
    }

    public final int getHisFrmTypeInt() {
        return this.GetValIntByKey(MapDataAttr.FrmType);
    }

    public final void setHisFrmTypeInt(int value) {
        this.SetValByKey(MapDataAttr.FrmType, value);
    }

    public final FrmType getHisFrmType() {
        return FrmType.forValue(this.GetValIntByKey(MapDataAttr.FrmType));
    }

    public final void setHisFrmType(FrmType value) {
        this.SetValByKey(MapDataAttr.FrmType, value.getValue());
    }


    public final int getHisEntityType() {
        return this.GetValIntByKey(MapDataAttr.EntityType);
    }

    public final void setHisEntityType(int value) {
        this.SetValByKey(MapDataAttr.EntityType, value);
    }

    /**
     * 表单类型名称
     */
    public final String getHisFrmTypeText() {
        return this.getHisFrmType().toString();

        //  SysEnum se = new SysEnum("FrmType", this.HisFrmTypeInt);
        // return se.Lab;
    }

    /**
     * 备注
     */
    public final String getNote() {
        return this.GetValStrByKey(MapDataAttr.Note);
    }

    public final void setNote(String value) {
        this.SetValByKey(MapDataAttr.Note, value);
    }

    /**
     * 是否有CA.
     */
    public final boolean getIsHaveCA() {
        return this.GetParaBoolen("IsHaveCA", false);

    }

    public final void setIsHaveCA(boolean value) {
        this.SetPara("IsHaveCA", value);
    }

    /**
     * 类别，可以为空.
     */
    public final String getFK_FrmSort() {
        return this.GetValStrByKey(MapDataAttr.FK_FrmSort);
    }

    public final void setFK_FrmSort(String value) {
        this.SetValByKey(MapDataAttr.FK_FrmSort, value);
    }

    /**
     * 数据源
     */
    public final String getDBSrc() {
        return this.GetValStrByKey(MapDataAttr.DBSrc);
    }

    public final void setDBSrc(String value) {
        this.SetValByKey(MapDataAttr.DBSrc, value);
    }

    /**
     * 类别，可以为空.
     */
    public final String getFK_FormTree() {
        return this.GetValStrByKey(MapDataAttr.FK_FormTree);
    }

    public final void setFK_FormTree(String value) {
        this.SetValByKey(MapDataAttr.FK_FormTree, value);
    }

    /**
     * 类别名称
     */
    public final String getFK_FormTreeText() {
        return DBAccess.RunSQLReturnStringIsNull("SELECT Name FROM Sys_FormTree WHERE No='" + this.getFK_FormTree() + "'", "目录错误");
    }

    /**
     * 从表集合.
     */
    public final String getDtls() {
        return this.GetValStrByKey(MapDataAttr.Dtls);
    }

    public final void setDtls(String value) {
        this.SetValByKey(MapDataAttr.Dtls, value);
    }

    /**
     * 主键
     */
    public final String getEnPK() {
        String s = this.GetValStrByKey(MapDataAttr.EnPK);
        if (DataType.IsNullOrEmpty(s)) {
            return "OID";
        }
        return s;
    }

    public final void setEnPK(String value) {
        this.SetValByKey(MapDataAttr.EnPK, value);
    }

    private Entities _HisEns = null;

    public final Entities getHisEns() {
        if (_HisEns == null) {
            _HisEns = bp.en.ClassFactory.GetEns(this.getNo());
        }
        return _HisEns;
    }

    public final Entity getHisEn() {
        return this.getHisEns().getGetNewEntity();
    }

    public final float getFrmW() {
        return this.GetValFloatByKey(MapDataAttr.FrmW);
    }

    public final void setFrmW(float value) {
        this.SetValByKey(MapDataAttr.FrmW, value);
    }

    /**
     * 应用类型.  0独立表单.1节点表单
     */
    public final String getAppType() {
        return this.GetValStrByKey(MapDataAttr.AppType);
    }

    public final void setAppType(String value) {
        this.SetValByKey(MapDataAttr.AppType, value);
    }

    /**
     * 表单body属性.
     */
    public final String getBodyAttr() {
        String str = this.GetValStrByKey(MapDataAttr.BodyAttr);
        str = str.replace("~", "'");
        return str;
    }

    public final void setBodyAttr(String value) {
        this.SetValByKey(MapDataAttr.BodyAttr, value);
    }

    /**
     * 流程控件s.
     */
    public final String getFlowCtrls() {
        return this.GetValStrByKey(MapDataAttr.FlowCtrls);
    }

    public final void setFlowCtrls(String value) {
        this.SetValByKey(MapDataAttr.FlowCtrls, value);
    }

    public final int getTableCol() {
        return this.GetValIntByKey(MapDataAttr.TableCol);
    }

    public final void setTableCol(int value) {
        this.SetValByKey(MapDataAttr.TableCol, value);
    }


    ///#endregion


    ///#region 构造方法
    public final Map GenerHisMap() throws Exception {
        MapAttrs mapAttrs = this.getMapAttrs();
        if (mapAttrs.size() == 0) {
            this.RepairMap();
            mapAttrs = this.getMapAttrs();
        }

        Map map = new Map(this.getPTable(), this.getName());
        //  map.EnDBUrl = new DBUrl(this.HisDBUrl);

        Attrs attrs = new Attrs();
        for (MapAttr mapAttr : mapAttrs.ToJavaList()) {
            map.AddAttr(mapAttr.getHisAttr());
        }

        // 产生从表。
        MapDtls dtls = this.getMapDtls(); // new MapDtls(this.getNo());
        for (MapDtl dtl : dtls.ToJavaList()) {
            GEDtls dtls1 = new GEDtls(dtl.getNo());
            map.AddDtl(dtls1, "RefPK");
        }


        ///#region 查询条件.
        map.IsShowSearchKey = this.getIsSearchKey(); //是否启用关键字查询.
        // 按日期查询.
        map.DTSearchWay = this.getDTSearchWay(); //日期查询方式.
        map.DTSearchKey = this.getDTSearchKey(); //日期字段.

        //加入外键查询字段.
        String[] keys = this.getRptSearchKeys().split("[*]", -1);
        for (String key : keys) {
            if (DataType.IsNullOrEmpty(key)) {
                continue;
            }

            if (map.getAttrs().contains(key) == false) {
                continue;
            }

            map.AddSearchAttr(key);
        }

        ///#endregion 查询条件.

        return map;
    }

    private GEEntity _HisEn = null;

    public final GEEntity getHisGEEn() {
        if (this._HisEn == null) {
            _HisEn = new GEEntity(this.getNo());
        }
        return _HisEn;
    }

    /**
     * 生成实体
     *
     * @param ds
     * @return
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
        for (MapDtl item : dtls.ToJavaList()) {
            DataTable dtDtls = ds.GetTableByName(item.getNo());
            GEDtls dtlsEn = new GEDtls(item.getNo());
            for (DataRow dr : dtDtls.Rows) {
                // 产生它的Entity data.
                GEDtl dtl = (GEDtl) dtlsEn.getGetNewEntity();
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
     * 生成map.
     *
     * @param no
     * @return
     */
    public static Map GenerHisMap(String no) throws Exception {
        if (bp.difference.SystemConfig.getIsDebug()) {
            MapData md = new MapData();
            md.setNo(no);
            md.Retrieve();
            return md.GenerHisMap();
        } else {
            Map map = bp.da.Cash.GetMap(no);
            if (map == null) {
                MapData md = new MapData();
                md.setNo(no);
                md.Retrieve();
                map = md.GenerHisMap();
                bp.da.Cash.SetMap(no, map);
            }
            return map;
        }
    }

    /**
     * 映射基础
     */
    public MapData() {
    }

    /**
     * 映射基础
     *
     * @param no 映射编号
     */
    public MapData(String no) {
        super(no);
    }

    /**
     * EnMap
     */
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null) {
            return this.get_enMap();
        }

        Map map = new Map("Sys_MapData", "表单注册表");


        ///#region 基础信息.
        map.AddTBStringPK(MapDataAttr.No, null, "编号", true, false, 1, 200, 100);
        map.AddTBString(MapDataAttr.Name, null, "描述", true, false, 0, 500, 20);
        map.AddTBString(MapDataAttr.FormEventEntity, null, "事件实体", true, true, 0, 100, 20, true);

        map.AddTBString(MapDataAttr.EnPK, null, "实体主键", true, false, 0, 200, 20);
        map.AddTBString(MapDataAttr.PTable, null, "物理表", true, false, 0, 500, 20);

        map.AddTBString(MapDataAttr.FrmID, null, "短名称", true, false, 0, 500, 20);


        //@周朋 表存储格式0=自定义表,1=指定表,可以修改字段2=执行表不可以修改字段.
        map.AddTBInt(MapDataAttr.PTableModel, 0, "表存储模式", true, true);


        map.AddTBString(MapDataAttr.Url, null, "连接(对嵌入式表单有效)", true, false, 0, 500, 20);
        map.AddTBString(MapDataAttr.Dtls, null, "从表", true, false, 0, 500, 20);

        //格式为: @1=方案名称1@2=方案名称2@3=方案名称3
        //map.AddTBString(MapDataAttr.Slns, null, "表单控制解决方案", true, false, 0, 500, 20);

        map.AddTBInt(MapDataAttr.FrmW, 900, "FrmW", true, true);

        map.AddTBInt(MapDataAttr.TableCol, 0, "傻瓜表单显示的列", true, true);

        //Tag
        map.AddTBString(MapDataAttr.Tag, null, "Tag", true, false, 0, 500, 20);

        // 可以为空这个字段。
        map.AddTBString(MapDataAttr.FK_FrmSort, null, "表单类别", true, false, 0, 500, 20);
        map.AddTBString(MapDataAttr.FK_FormTree, null, "表单树类别", true, false, 0, 500, 20);


        // enumFrmType  @自由表单，@傻瓜表单，@嵌入式表单.  
        map.AddDDLSysEnum(MapDataAttr.FrmType, bp.sys.FrmType.FoolForm.getValue(), "表单类型", true, false, MapDataAttr.FrmType);

        map.AddTBInt(MapDataAttr.FrmShowType, 0, "表单展示方式", true, true);

        map.AddDDLSysEnum(MapDataAttr.EntityType, 0, "业务类型", true, false, MapDataAttr.EntityType, "@0=独立表单@1=单据@2=编号名称实体@3=树结构实体");
        map.SetHelperAlert(MapDataAttr.EntityType, "该实体的类型,@0=单据@1=编号名称实体@2=树结构实体.");

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

        //流程控件.
        map.AddTBString(MapDataAttr.FlowCtrls, null, "流程控件", true, true, 0, 200, 20);

        //增加参数字段.
        map.AddTBAtParas(4000);

        ///#endregion

        map.AddTBString(MapDataAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 20);

        this.set_enMap(map);
        return this.get_enMap();
    }

    /**
     * 上移
     */
    public final void DoUp() {
        this.DoOrderUp(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
    }

    /**
     * 下移
     */
    public final void DoOrderDown() {
        this.DoOrderDown(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
    }

    /**
     * 获得PTableModel=2模式下的表单，没有被使用的字段集合.
     *
     * @param frmID
     * @return
     */
    public static DataTable GetFieldsOfPTableMode2(String frmID) throws Exception {
        String pTable = "";

        MapDtl dtl = new MapDtl();
        dtl.setNo(frmID);
        if (dtl.RetrieveFromDBSources() == 1) {
            pTable = dtl.getPTable();
        } else {
            MapData md = new MapData();
            md.setNo(frmID);
            md.RetrieveFromDBSources();
            pTable = md.getPTable();
        }

        //获得原始数据.
        DataTable dt = bp.da.DBAccess.GetTableSchema(pTable);

        //创建样本表结构.
        DataTable mydt = bp.da.DBAccess.GetTableSchema(pTable);
        mydt.Rows.clear();

        //获得现有的列..
        MapAttrs attrs = new MapAttrs(frmID);

        String flowFiels = ",GUID,PRI,PrjNo,PrjName,PEmp,AtPara,FlowNote,WFSta,PNodeID,FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum,PWorkID,PFlowNo,BillNo,ProjNo,";

        //排除已经存在的列. 把所有的列都输出给前台，让前台根据类型分拣.
        for (DataRow dr : dt.Rows) {
            String key = dr.getValue("FName").toString();
            if (attrs.contains(MapAttrAttr.KeyOfEn, key) == true) {
                continue;
            }

            if (flowFiels.contains("," + key + ",") == true) {
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

    public final FormEventBase getHisFEB() {
        if (this.getFormEventEntity().equals("")) {
            return null;
        }

        if (_HisFEB == null) {
            _HisFEB = bp.sys.base.Glo.GetFormEventBaseByEnName(this.getNo());
        }

        return _HisFEB;
    }

    /**
     * 导入数据
     *
     * @param ds
     * @return
     */
    public static MapData ImpMapData(DataSet ds) throws Exception {
        String errMsg = "";
        if (ds.Tables.contains("WF_Flow") == true) {
            errMsg += "@此模板文件为流程模板。";
        }

        if (ds.Tables.contains("Sys_MapAttr") == false) {
            errMsg += "@缺少表:Sys_MapAttr";
        }

        if (ds.Tables.contains("Sys_MapData") == false) {
            errMsg += "@缺少表:Sys_MapData";
        }

        if (!errMsg.equals("")) {
            throw new RuntimeException(errMsg);
        }

        DataTable dt = ds.GetTableByName("Sys_MapData");
        String fk_mapData = dt.Rows.get(0).getValue("No").toString();
        MapData md = new MapData();
        md.setNo(fk_mapData);
        if (md.getIsExits()) {
            throw new RuntimeException("@已经存在(" + fk_mapData + ")的表单ID，所以您不能导入。");
        }

        //导入.
        return ImpMapData(fk_mapData, ds);
    }

    /**
     * 设置表单为只读属性
     *
     * @param fk_mapdata 表单ID
     */
    public static void SetFrmIsReadonly(String fk_mapdata) throws Exception {
        //把主表字段设置为只读.
        MapAttrs attrs = new MapAttrs(fk_mapdata);
        for (MapAttr attr : attrs.ToJavaList()) {
            if (attr.getDefValReal().contains("@")) {
                attr.setUIIsEnable(false);
                attr.setDefValReal(""); //清空默认值.
                attr.SetValByKey("ExtDefVal", ""); //设置默认值.
                attr.Update();
                continue;
            }

            if (attr.getUIIsEnable() == true) {
                attr.setUIIsEnable(false);
                attr.Update();
                continue;
            }
        }

        //把从表字段设置为只读.
        MapDtls dtls = new MapDtls(fk_mapdata);
        for (MapDtl dtl : dtls.ToJavaList()) {
            dtl.setIsInsert(false);
            dtl.setIsUpdate(false);
            dtl.setIsDelete(false);
            dtl.Update();

            attrs = new MapAttrs(dtl.getNo());
            for (MapAttr attr : attrs.ToJavaList()) {
                if (attr.getDefValReal().contains("@")) {
                    attr.setUIIsEnable(false);
                    attr.SetValByKey("ExtDefVal", ""); //设置默认值.
                    attr.Update();
                }

                if (attr.getUIIsEnable() == true) {
                    attr.setUIIsEnable(false);
                    attr.Update();
                    continue;
                }
            }
        }

        //把附件设置为只读.
        FrmAttachments aths = new FrmAttachments(fk_mapdata);
        for (FrmAttachment item : aths.ToJavaList()) {
            item.setIsUpload(false);
            item.setHisDeleteWay(AthDeleteWay.DelSelf);

            //如果是从开始节点表单导入的,就默认为, 按照主键PK的方式显示.
            if (fk_mapdata.indexOf("ND") == 0) {
                item.setHisCtrlWay(AthCtrlWay.PK);
                item.setDataRefNoOfObj("AttachM1");
            }
            item.Update();
        }
    }


    /**
     * 导入表单
     *
     * @param fk_mapdata 表单ID
     * @param ds         表单数据
     * @return
     */
    public static MapData ImpMapData(String fk_mapdata, DataSet ds) throws Exception {


        ///#region 检查导入的数据是否完整.
        String errMsg = "";
        //if (ds.Tables[0].TableName != "Sys_MapData")
        //    errMsg += "@非表单模板。";

        if (ds.Tables.contains("WF_Flow") == true) {
            errMsg += "@此模板文件为流程模板。";
        }

        if (ds.Tables.contains("Sys_MapAttr") == false) {
            errMsg += "@缺少表:Sys_MapAttr";
        }

        if (ds.Tables.contains("Sys_MapData") == false) {
            errMsg += "@缺少表:Sys_MapData";
        }

        DataTable dtCheck = ds.GetTableByName("Sys_MapAttr");
        boolean isHave = false;
        for (DataRow dr : dtCheck.Rows) {
            if (dr.getValue("KeyOfEn").toString().equals("OID")) {
                isHave = true;
                break;
            }
        }

        if (isHave == false) {
            errMsg += "@表单模版缺少列:OID";
        }

        if (!errMsg.equals("")) {
            throw new RuntimeException("@以下错误不可导入，可能的原因是非表单模板文件:" + errMsg);
        }

        ///#endregion

        // 定义在最后执行的sql.
        StringBuilder endDoSQL = new StringBuilder();

        //检查是否存在OID字段.
        MapData mdOld = new MapData();
        mdOld.setNo(fk_mapdata);
        mdOld.RetrieveFromDBSources();

        //现在表单的类型
        FrmType frmType = mdOld.getHisFrmType();

        //业务类型
        int entityType = mdOld.getHisEntityType();

        mdOld.Delete();

        // 求出dataset的map.
        String oldMapID = "";
        DataTable dtMap = ds.GetTableByName("Sys_MapData");
        if (dtMap.Rows.size() == 1) {
            oldMapID = dtMap.Rows.get(0).getValue("No").toString();
        } else {
            // 求旧的表单ID.
            for (DataRow dr : dtMap.Rows) {
                oldMapID = dr.getValue("No").toString();
            }

            if (DataType.IsNullOrEmpty(oldMapID) == true) {
                oldMapID = dtMap.Rows.get(0).getValue("No").toString();
            }
        }
        String timeKey = DataType.getCurrentDateByFormart("MMddHHmmss");


        ///#region 表单元素
        for (DataTable dt : ds.Tables) {
            int idx = 0;

//			switch (dt.TableName)
//ORIGINAL LINE: case "Sys_MapDtl":
            if (dt.TableName.equals("Sys_MapDtl")) {

                for (DataRow dr : dt.Rows) {

                    MapDtl dtl = new MapDtl();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        dtl.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));

                    }
                    dtl.Insert();
                }
            }
//ORIGINAL LINE: case "Sys_MapData":
            else if (dt.TableName.equals("Sys_MapData")) {
                for (DataRow dr : dt.Rows) {
                    MapData md = new MapData();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        md.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }

                    //如果物理表为空，则使用编号为物理数据表
                    if (DataType.IsNullOrEmpty(md.getPTable().trim()) == true) {
                        md.setPTable(md.getNo());
                    }

                    //表单类别编号不为空，则用原表单类别编号
                    if (DataType.IsNullOrEmpty(mdOld.getFK_FormTree()) == false) {
                        md.setFK_FormTree(mdOld.getFK_FormTree());
                    }

                    //表单类别编号不为空，则用原表单类别编号
                    if (DataType.IsNullOrEmpty(mdOld.getFK_FrmSort()) == false) {
                        md.setFK_FrmSort(mdOld.getFK_FrmSort());
                    }

                    if (DataType.IsNullOrEmpty(mdOld.getPTable()) == false) {
                        md.setPTable(mdOld.getPTable());
                    }
                    if (DataType.IsNullOrEmpty(mdOld.getName()) == false) {
                        md.setName(mdOld.getName());
                    }

                    md.setHisFrmType(mdOld.getHisFrmType());
                    if (frmType == FrmType.Develop) {
                        md.setHisFrmType(FrmType.Develop);
                    }

                    if (entityType != md.getHisEntityType()) {
                        md.setHisEntityType(entityType);
                    }

                    //表单应用类型保持不变
                    md.setAppType(mdOld.getAppType());
                    md.DirectInsert();
                    Cash2019.UpdateRow(md.toString(), md.getNo().toString(), md.getRow());

                    //如果是开发者表单，赋值HtmlTemplateFile数据库的值并保存到DataUser下
                    if (frmType == FrmType.Develop) {
                        String htmlCode = bp.da.DBAccess.GetBigTextFromDB("Sys_MapData", "No", oldMapID, "HtmlTemplateFile");
                        if (DataType.IsNullOrEmpty(htmlCode) == false) {
                            //保存到数据库，存储html文件
                            //保存到DataUser/CCForm/HtmlTemplateFile/文件夹下
                            String filePath = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/";
                            File directory = new File(filePath);
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }
                            filePath = filePath + md.getNo() + ".htm";
                            //写入到html 中
                            bp.da.DataType.WriteFile(filePath, htmlCode);
                            // HtmlTemplateFile 保存到数据库中
                            bp.da.DBAccess.SaveBigTextToDB(htmlCode, "Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
                        } else {
                            //如果htmlCode是空的需要删除当前节点的html文件
                            String filePath = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/" + md.getNo() + ".htm";
                            File file = new File(filePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            bp.da.DBAccess.SaveBigTextToDB("", "Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
                        }
                    }

                }
            }
//ORIGINAL LINE: case "Sys_FrmBtn":
            else if (dt.TableName.equals("Sys_FrmBtn")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    FrmBtn en = new FrmBtn();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }

                    //en.setMyPK("Btn_" + idx + "_" + fk_mapdata;
                    en.setMyPK(DBAccess.GenerGUID());
                    en.Insert();
                }

            }
//ORIGINAL LINE: case "Sys_FrmImg":
            else if (dt.TableName.equals("Sys_FrmImg")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    FrmImg en = new FrmImg();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }
                    if (DataType.IsNullOrEmpty(en.getKeyOfEn()) == true) {
                        en.setMyPK(DBAccess.GenerGUID());
                    }

                    en.Insert();
                }
            }
//ORIGINAL LINE: case "Sys_FrmImgAth":
            else if (dt.TableName.equals("Sys_FrmImgAth")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    FrmImgAth en = new FrmImgAth();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }

                    if (DataType.IsNullOrEmpty(en.getCtrlID())) {
                        en.setCtrlID("ath" + idx);
                    }

                    en.Insert();
                }
            }
//ORIGINAL LINE: case "Sys_FrmRB":
            else if (dt.TableName.equals("Sys_FrmRB")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    FrmRB en = new FrmRB();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }


                    try {
                        en.Save();
                    } catch (java.lang.Exception e) {
                    }
                }
            }
//ORIGINAL LINE: case "Sys_FrmAttachment":
            else if (dt.TableName.equals("Sys_FrmAttachment")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    FrmAttachment en = new FrmAttachment();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }
                    en.setMyPK(fk_mapdata + "_" + en.GetValByKey("NoOfObj"));


                    try {
                        en.Insert();
                    } catch (java.lang.Exception e2) {
                    }
                }
            }
//ORIGINAL LINE: case "Sys_MapFrame":
            else if (dt.TableName.equals("Sys_MapFrame")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    MapFrame en = new MapFrame();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }


                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }
                    en.DirectInsert();
                }
            }
//ORIGINAL LINE: case "Sys_MapExt":
            else if (dt.TableName.equals("Sys_MapExt")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    MapExt en = new MapExt();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }
                        if (DataType.IsNullOrEmpty(val.toString()) == true) {
                            continue;
                        }
                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }

                    //执行保存，并统一生成PK的规则.
                    en.InitPK();
                    en.Save();
                }
            }
//ORIGINAL LINE: case "Sys_MapAttr":
            else if (dt.TableName.equals("Sys_MapAttr")) {
                for (DataRow dr : dt.Rows) {
                    MapAttr en = new MapAttr();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }

                        en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                    }

                    en.setMyPK(en.getFK_MapData() + "_" + en.getKeyOfEn());

                    //直接插入.
                    try {
                        en.DirectInsert();
                        //判断该字段是否是大文本 例如注释、说明
                        if (en.getUIContralType() == UIContralType.BigText) {
                            //判断原文件是否存在
                            String file = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/BigNoteHtmlText/" + oldMapID + ".htm";
                            File f = new File(file);
                            //若文件存在，则复制                                  
                            if (f.exists()) {
                                String newFile = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/BigNoteHtmlText/" + fk_mapdata + ".htm";
                                f = new File(newFile);
                                if (f.exists()) {
                                    f.delete();
                                }
                                DBAccess.copyDirectory(file, newFile);
                            }

                        }
                    } catch (java.lang.Exception e3) {
                    }
                }
            }
//ORIGINAL LINE: case "Sys_GroupField":
            else if (dt.TableName.equals("Sys_GroupField")) {
                for (DataRow dr : dt.Rows) {
                    idx++;
                    GroupField en = new GroupField();
                    for (DataColumn dc : dt.Columns) {
                        Object val = (Object) ((dr.getValue(dc.ColumnName) instanceof Object) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }
                        try {
                            en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
                        } catch (java.lang.Exception e4) {
                            throw new RuntimeException("val:" + val.toString() + "oldMapID:" + oldMapID + "fk_mapdata:" + fk_mapdata);
                        }
                    }
                    int beforeID = (int) en.getOID();
                    en.setOID(0);
                    en.DirectInsert();
                    endDoSQL.append("@UPDATE Sys_MapAttr SET GroupID=").append(en.getOID()).append(" WHERE FK_MapData='").append(fk_mapdata).append("' AND GroupID='").append(beforeID).append("'");
                }
            }
//ORIGINAL LINE: case "Sys_Enum":
            else if (dt.TableName.equals("Sys_Enum")) {
                for (DataRow dr : dt.Rows) {
                    SysEnum se = new bp.cloud.sys.SysEnum();
                    for (DataColumn dc : dt.Columns) {
                        String val = (String) ((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
                        se.SetValByKey(dc.ColumnName, val);
                    }
                    se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
                    if (se.getIsExits()) {
                        continue;
                    }
                    se.Insert();
                }
            }
//ORIGINAL LINE: case "Sys_EnumMain":
            else if (dt.TableName.equals("Sys_EnumMain")) {
                for (DataRow dr : dt.Rows) {
                    SysEnumMain sem = new bp.cloud.sys.SysEnumMain();
                    for (DataColumn dc : dt.Columns) {
                        String val = (String) ((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
                        if (val == null) {
                            continue;
                        }
                        sem.SetValByKey(dc.ColumnName, val);
                    }
                    if (sem.getIsExits()) {
                        continue;
                    }
                    sem.Insert();
                }
            }
//ORIGINAL LINE: case "WF_Node":
            else if (dt.TableName.equals("WF_Node")) {
                if (dt.Rows.size() > 0) {
                    endDoSQL.append("@UPDATE WF_Node SET FWCSta=2" + ",FWC_X=")
                            .append(dt.Rows.get(0).getValue("FWC_X"))
                            .append(",FWC_Y=").append(dt.Rows.get(0).getValue("FWC_Y"))
                            .append(",FWC_H=").append(dt.Rows.get(0).getValue("FWC_H"))
                            .append(",FWC_W=").append(dt.Rows.get(0).getValue("FWC_W"))
                            .append(",FWCType=").append(dt.Rows.get(0).getValue("FWCType"))
                            .append(" WHERE NodeID=").append(fk_mapdata.replace("ND", ""));
                }
            }
        }

        ///#endregion

        //执行最后结束的sql.
        DBAccess.RunSQLs(endDoSQL.toString());

        MapData mdNew = new MapData(fk_mapdata);
        mdNew.RepairMap();

        if (mdNew.getNo().indexOf("ND") == 0) {
            mdNew.setFK_FrmSort("");
            mdNew.setFK_FormTree("");
        }

        mdNew.Update();
        return mdNew;
    }

    /**
     * 修复map.
     */
    public final void RepairMap() throws Exception {
        GroupFields gfs = new GroupFields(this.getNo());
        if (gfs.size() == 0) {
            GroupField gf = new GroupField();
            gf.setFrmID(this.getNo());
            gf.setLab(this.getName());
            gf.Insert();

            String sqls = "";
            sqls += "@UPDATE Sys_MapDtl SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
            sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
            //sqls += "@UPDATE Sys_MapFrame SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
            sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
            DBAccess.RunSQLs(sqls);
        } else {
            if (bp.difference.SystemConfig.getAppCenterDBType() != DBType.Oracle) {
                GroupField gfFirst = (GroupField) ((gfs.get(0) instanceof GroupField) ? gfs.get(0) : null);

                String sqls = "";
                //   sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gfFirst.getOID() + "       WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_MapAttr       WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.getNo() + "') or GroupID is null) AS X) AND FK_MapData='" + this.getNo() + "' ";
                sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gfFirst.getOID() + " WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_FrmAttachment WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.getNo() + "')) AS X) AND FK_MapData='" + this.getNo() + "' ";


///#warning 这些sql 对于Oracle 有问题，但是不影响使用.
                try {
                    DBAccess.RunSQLs(sqls);
                } catch (java.lang.Exception e) {
                }
            }
        }

        bp.sys.MapAttr attr = new bp.sys.MapAttr();
        if (this.getEnPK().equals("OID")) {
            if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, this.getNo()) == false) {
                attr.setFK_MapData(this.getNo());
                attr.setKeyOfEn("OID");
                attr.setName("OID");
                attr.setMyDataType(bp.da.DataType.AppInt);
                attr.setUIContralType(UIContralType.TB);
                attr.setLGType(FieldTypeS.Normal);
                attr.setUIVisible(false);
                attr.setUIIsEnable(false);
                attr.setDefVal("0");
                attr.setEditType(EditType.Readonly);
                attr.Insert();
            }
        }
        if (this.getEnPK().equals("No") || this.getEnPK().equals("MyPK")) {
            if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getEnPK(), MapAttrAttr.FK_MapData, this.getNo()) == false) {
                attr.setFK_MapData(this.getNo());
                attr.setKeyOfEn(this.getEnPK());
                attr.setName(this.getEnPK());
                attr.setMyDataType(bp.da.DataType.AppInt);
                attr.setUIContralType(UIContralType.TB);
                attr.setLGType(FieldTypeS.Normal);
                attr.setUIVisible(false);
                attr.setUIIsEnable(false);
                attr.setDefVal("0");
                attr.setEditType(EditType.Readonly);
                attr.Insert();
            }
        }

        if (attr.IsExit(MapAttrAttr.KeyOfEn, "RDT", MapAttrAttr.FK_MapData, this.getNo()) == false) {
            attr = new bp.sys.MapAttr();
            attr.setFK_MapData(this.getNo());
            attr.setEditType(EditType.UnDel);
            attr.setKeyOfEn("RDT");
            attr.setName("更新时间");
            attr.setGroupID(0);
            attr.setMyDataType(DataType.AppDateTime);
            attr.setUIContralType(UIContralType.TB);
            attr.setLGType(FieldTypeS.Normal);
            attr.setUIVisible(false);
            attr.setUIIsEnable(false);
            attr.setDefVal("@RDT");
            attr.setTag("1");
            attr.Insert();
        }

        //检查特殊UIBindkey丢失的问题.
        MapAttrs attrs = new MapAttrs();
        attrs.Retrieve(MapAttrAttr.FK_MapData, this.getNo());
        for (MapAttr item : attrs.ToJavaList()) {
            if (item.getLGType() == FieldTypeS.Enum || item.getLGType() == FieldTypeS.FK) {
                if (DataType.IsNullOrEmpty(item.getUIBindKey()) == true) {
                    item.setLGType(FieldTypeS.Normal);
                    item.setUIContralType(UIContralType.TB);
                    item.Update();
                }
            }
        }

    }

    @Override
    protected boolean beforeInsert() throws Exception {
        if (this.getHisFrmType() == FrmType.Url || this.getHisFrmType() == FrmType.Entity) {

        } else {
            this.setPTable(bp.pub.PubClass.DealToFieldOrTableNames(this.getPTable()));
        }
        return super.beforeInsert();
    }


    @Override
    protected boolean beforeUpdateInsertAction() throws Exception {
        if (this.getHisFrmType() == FrmType.Url || this.getHisFrmType() == FrmType.Entity) {
            return super.beforeUpdateInsertAction();
        }

        this.setPTable(bp.pub.PubClass.DealToFieldOrTableNames(this.getPTable()));
        getMapAttrs().Retrieve(MapAttrAttr.FK_MapData, getPTable());

        //更新版本号.
        this.setVer(DataType.getCurrentDateTimess());

        //设置OrgNo. 如果是管理员，就设置他所在的部门编号。
        this.setOrgNo(bp.web.WebUser.getFK_Dept());


        ///#region  检查是否有ca认证设置.
        boolean isHaveCA = false;
        for (MapAttr item : this.getMapAttrs().ToJavaList()) {
            if (item.getSignType() == SignType.CA) {
                isHaveCA = true;
                break;
            }
        }
        this.setIsHaveCA(isHaveCA);
        if (getIsHaveCA() == true) {
            //就增加隐藏字段.
            //MapAttr attr = new bp.sys.MapAttr();
            // attr.setMyPK(this.getNo() + "_SealData";
            // attr.setFK_MapData(this.getNo());
            // attr.setEditType(EditType.UnDel);
            //attr.setKeyOfEn("SealData";
            // attr.setName("SealData";
            // attr.MyDataType = bp.da.DataType.AppString;
            // attr.setUIContralType(UIContralType.TB);
            //  attr.setLGType(FieldTypeS.Normal);
            // attr.setUIVisible(false);
            // attr.setUIIsEnable(false);
            // attr.setMaxLen(4000;
            // attr.setMinLen(0);
            // attr.Save();
        }

        ///#endregion  检查是否有ca认证设置.

        //清除缓存.
        this.ClearCash();

        return super.beforeUpdateInsertAction();
    }

    /**
     * 更新版本
     */
    public final void UpdateVer() {
        String sql = "UPDATE Sys_MapData SET VER='" + bp.da.DataType.getCurrentDateTimess() + "' WHERE No='" + this.getNo() + "'";
        bp.da.DBAccess.RunSQL(sql);
    }

    @Override
    protected boolean beforeDelete() throws Exception {
        String sql = "";
        sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='" + this.getNo() + "'";
        DataTable Sys_MapDtl = DBAccess.RunSQLReturnTable(sql);

        StringBuilder whereFK_MapData = new StringBuilder("FK_MapData= '" + this.getNo() + "' ");
        StringBuilder whereEnsName = new StringBuilder("FrmID= '" + this.getNo() + "' ");
        StringBuilder whereNo = new StringBuilder("No='" + this.getNo() + "' ");

        for (DataRow dr : Sys_MapDtl.Rows) {
            // ids += ",'" + dr.setValue("No"] + "'";
            whereFK_MapData.append(" OR FK_MapData='").append(dr.getValue("No")).append("' ");
            whereEnsName.append(" OR FrmID='").append(dr.getValue("No")).append("' ");
            whereNo.append(" OR No='").append(dr.getValue("No")).append("' ");
        }

        //	string where = " FK_MapData IN (" + ids + ")";


        ///#region 删除相关的数据。
        sql = "DELETE FROM Sys_MapDtl WHERE FK_MapData='" + this.getNo() + "'";
        //  sql += "@DELETE FROM Sys_FrmLine WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmEle WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmEvent WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmBtn WHERE " + whereFK_MapData;
        // sql += "@DELETE FROM Sys_FrmLab WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmLink WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmImg WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmImgAth WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmRB WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_FrmAttachment WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_MapFrame WHERE " + whereFK_MapData;

        if (this.getNo().contains("BP.") == false) {
            sql += "@DELETE FROM Sys_MapExt WHERE " + whereFK_MapData;
        }

        sql += "@DELETE FROM Sys_MapAttr WHERE " + whereFK_MapData;
        sql += "@DELETE FROM Sys_GroupField WHERE " + whereEnsName;
        sql += "@DELETE FROM Sys_MapData WHERE " + whereNo;
        // sql += "@DELETE FROM Sys_M2M WHERE " + whereFK_MapData;
        sql += "@DELETE FROM WF_FrmNode WHERE FK_Frm='" + this.getNo() + "'";
        sql += "@DELETE FROM Sys_FrmSln WHERE " + whereFK_MapData;
        DBAccess.RunSQLs(sql);

        ///#endregion 删除相关的数据。


        ///#region 删除物理表。
        //如果存在物理表.
        if (DBAccess.IsExitsObject(this.getPTable()) && this.getPTable().indexOf("ND") == 0) {
            //如果其他表单引用了该表，就不能删除它.
            sql = "SELECT COUNT(No) AS NUM  FROM Sys_MapData WHERE PTable='" + this.getPTable() + "' OR ( PTable='' AND No='" + this.getPTable() + "')";
            if (DBAccess.RunSQLReturnValInt(sql, 0) > 1) {
                //说明有多个表单在引用.
            } else {
                // edit by zhoupeng 误删已经有数据的表.
                if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + this.getPTable() + " WHERE 1=1 ") == 0) {
                    DBAccess.RunSQL("DROP TABLE " + this.getPTable());
                }
            }
        }

        MapDtls dtls = new MapDtls(this.getNo());
        for (MapDtl dtl : dtls.ToJavaList()) {
            dtl.Delete();
        }


        ///#endregion


        ///#region 删除注册到的外检表.
        SFTables sfs = new SFTables();
        sfs.Retrieve(SFTableAttr.SrcTable, this.getPTable());
        for (SFTable item : sfs.ToJavaList()) {
            if (item.IsCanDelete() == null) {
                item.Delete();
            }
        }

        ///#endregion 删除注册到的外检表.

        return super.beforeDelete();
    }

    ///#endregion 常用方法.


    ///#region 与Excel相关的操作 .

    /**
     * 获得Excel文件流
     *
     * @param pkValue
     * @param bytes
     * @param saveTo
     * @return
     */
    public final boolean ExcelGenerFile(String pkValue, RefObject<byte[]> bytes, String saveTo) {
        try {
            byte[] by = bp.da.DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), pkValue, saveTo);
            if (by != null) {
                bytes.argvalue = by;
                return true;
            } else //说明当前excel文件没有生成.
            {
                String tempExcel = bp.difference.SystemConfig.getPathOfDataUser() + "FrmVSTOTemplate/" + this.getNo() + ".xlsx";
                File f = new File(tempExcel);
                if (f.exists()) {
                    bytes.argvalue = bp.da.DataType.ConvertFileToByte(tempExcel);
                    return false;
                } else //模板文件也不存在时
                {
                    throw new RuntimeException("@没有找到模版文件." + tempExcel + " 请确认表单配置.");
                }
            }
        } catch (Exception ex) {
            bp.da.Log.DebugWriteError("读取excel失败：" + ex.getMessage());
            //  bp.da.Log.DebugWriteError();
            return false;
        }
    }

    /**
     * 保存excel文件
     *
     * @param pkValue
     * @param bty
     * @param saveTo
     */
    public final void ExcelSaveFile(String pkValue, byte[] bty, String saveTo) throws Exception {
        bp.da.DBAccess.SaveBytesToDB(bty, this.getPTable(), this.getEnPK(), pkValue, saveTo);
    }

    ///#endregion 与Excel相关的操作 .


    ///#region 与Word相关的操作 .

    /**
     * 获得Excel文件流
     *
     * @param pkValue
     * @param bytes
     * @param saveTo
     * @return
     */
    public final void WordGenerFile(String pkValue, RefObject<byte[]> bytes, String saveTo) throws Exception {
        byte[] by = bp.da.DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), pkValue, saveTo);
        if (by != null) {
            bytes.argvalue = by;
        } else //说明当前excel文件没有生成.
        {
            String tempExcel = bp.difference.SystemConfig.getPathOfDataUser() + "FrmVSTOTemplate/" + this.getNo() + ".docx";
            File f = new File(tempExcel);
            if (f.exists()) {
                tempExcel = bp.difference.SystemConfig.getPathOfDataUser() + "FrmVSTOTemplate/NDxxxRpt.docx";
            }

            bytes.argvalue = bp.da.DataType.ConvertFileToByte(tempExcel);
        }
    }

    /**
     * 保存excel文件
     *
     * @param pkValue
     * @param bty
     * @param saveTo
     */
    public final void WordSaveFile(String pkValue, byte[] bty, String saveTo) throws Exception {
        bp.da.DBAccess.SaveBytesToDB(bty, this.getPTable(), this.getEnPK(), pkValue, saveTo);
    }

    ///#endregion 与Excel相关的操作 .

}
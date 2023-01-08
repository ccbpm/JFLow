package bp.wf.template.frm;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.*;
import bp.sys.CCFormAPI;
import bp.sys.base.FormEventBaseDtl;
import bp.wf.*;
import bp.wf.template.*;
import java.util.*;

/** 
 明细
*/
public class MapDtlExt extends EntityNoName
{

		///#region 导入导出属性.
	/** 
	 用户访问.
	*/
	@Override
	public UAC getHisUAC()  {

		UAC uac = new UAC();
			/* uac.OpenForAppAdmin();
			 uac.IsInsert = false;*/

		if (bp.web.WebUser.getIsAdmin() == false)
		{
			throw new RuntimeException("err@管理员登录用户信息丢失,当前会话[" + bp.web.WebUser.getNo() + "," + bp.web.WebUser.getName() + "]");
		}

		uac.IsUpdate = true;
		uac.IsDelete = true;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 是否可以导出
	*/
	public final boolean isImp() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsImp);
	}
	public final void setImp(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsImp, value);
	}
	/** 
	 是否可以导入
	*/
	public final boolean isExp() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsExp);
	}
	public final void setExp(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsExp, value);
	}
	/** 
	 查询sql
	*/
	public final String getImpSQLInit() throws Exception
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLInit).replace("~", "'");
	}
	public final void setImpSQLInit(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.ImpSQLInit, value);
	}
	/** 
	 搜索sql
	*/
	public final String getImpSQLSearch() throws Exception
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLSearch).replace("~", "'");
	}
	public final void setImpSQLSearch(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.ImpSQLSearch, value);
	}
	/** 
	 填充数据一行数据
	*/
	public final String getImpSQLFullOneRow() throws Exception
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLFullOneRow).replace("~", "'");
	}
	public final void setImpSQLFullOneRow(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.ImpSQLFullOneRow, value);
	}

		///#endregion


		///#region 基本设置
	/** 
	 工作模式
	*/
	public final DtlModel getDtlModel() throws Exception {
		return DtlModel.forValue(this.GetValIntByKey(MapDtlAttr.Model));
	}
	public final void setDtlModel(DtlModel value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.Model, value.getValue());
	}
	/** 
	 是否启用行锁定.
	*/
	public final boolean isRowLock() throws Exception {
		return this.GetParaBoolen(MapDtlAttr.IsRowLock, false);
	}
	public final void setRowLock(boolean value)throws Exception
	{this.SetPara(MapDtlAttr.IsRowLock, value);
	}

		///#endregion 基本设置


		///#region 参数属性
	/** 
	 记录增加模式
	*/
	public final DtlAddRecModel getDtlAddRecModel() throws Exception {
		return DtlAddRecModel.forValue(this.GetParaInt(MapDtlAttr.DtlAddRecModel, 0));
	}
	public final void setDtlAddRecModel(DtlAddRecModel value)throws Exception
	{this.SetPara(MapDtlAttr.DtlAddRecModel, value.getValue());
	}
	/** 
	 保存方式
	*/
	public final DtlSaveModel getDtlSaveModel() throws Exception {
		return DtlSaveModel.forValue(this.GetParaInt(MapDtlAttr.DtlSaveModel, 0));
	}
	public final void setDtlSaveModel(DtlSaveModel value)throws Exception
	{this.SetPara(MapDtlAttr.DtlSaveModel, value.getValue());
	}
	/** 
	 是否启用Link,在记录的右边.
	*/
	public final void setIsEnableLink(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnableLink, value);
		this.SetPara(MapDtlAttr.IsEnableLink, value);
	}

	public final boolean isEnableLink() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableLink, false);
	}
	public final void setEnableLink(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsEnableLink, value);
		this.SetPara(MapDtlAttr.IsEnableLink, value);
	}
	public final String getLinkLabel() throws Exception {
		String s = this.GetValStrByKey(MapDtlAttr.LinkLabel);
		if (DataType.IsNullOrEmpty(s))
		{
			return "详细";
		}
		return s;
	}
	public final void setLinkLabel(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.LinkLabel, value);
		this.SetPara(MapDtlAttr.LinkLabel, value);
	}
	public final String getLinkUrl() throws Exception {
		String s = this.GetValStrByKey(MapDtlAttr.LinkUrl);
		if (DataType.IsNullOrEmpty(s))
		{
			return "http://ccport.org";
		}

		s = s.replace("*", "@");
		return s;
	}
	public final void setLinkUrl(String value)throws Exception
	{String val = value;
		val = val.replace("@", "*");
		this.SetValByKey(MapDtlAttr.LinkUrl, val);
		this.SetPara(MapDtlAttr.LinkUrl, val);
	}
	public final String getLinkTarget() throws Exception {
		String s = this.GetValStrByKey(MapDtlAttr.LinkTarget);
		if (DataType.IsNullOrEmpty(s))
		{
			return "_blank";
		}
		return s;
	}
	public final void setLinkTarget(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.LinkTarget, value);
		this.SetPara(MapDtlAttr.LinkTarget, value);
	}
	/** 
	 子线程处理人字段(用于分流节点的明细表分配子线程任务).
	*/
	public final String getSubThreadWorker() throws Exception {
		String s = this.GetParaString(MapDtlAttr.SubThreadWorker);
		if (DataType.IsNullOrEmpty(s))
		{
			return "";
		}
		return s;
	}
	public final void setSubThreadWorker(String value)throws Exception
	{this.SetPara(MapDtlAttr.SubThreadWorker, value);
	}
	/** 
	 子线程分组字段(用于分流节点的明细表分配子线程任务)
	*/
	public final String getSubThreadGroupMark() throws Exception {
		String s = this.GetParaString(MapDtlAttr.SubThreadGroupMark);
		if (DataType.IsNullOrEmpty(s))
		{
			return "";
		}
		return s;
	}
	public final void setSubThreadGroupMark(String value)throws Exception
	{this.SetPara(MapDtlAttr.SubThreadGroupMark, value);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(MapDtlAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.FK_Node, value);
	}

		///#endregion 参数属性


		///#region 外键属性
	/** 
	 框架
	*/
	public final MapFrames getMapFrames() throws Exception {
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = tempVar instanceof MapFrames ? (MapFrames)tempVar : null;
		if (obj == null)
		{
			obj = new MapFrames(this.getNo());
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	/** 
	 分组字段
	*/
	public final GroupFields getGroupFieldsDel() throws Exception {
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
	 事件
	*/
	public final FrmEvents getFrmEvents() throws Exception {
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = tempVar instanceof FrmEvents ? (FrmEvents)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEvents(this.getNo());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
	/** 
	 从表
	*/
	public final MapDtls getMapDtls() throws Exception {
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = tempVar instanceof MapDtls ? (MapDtls)tempVar : null;
		if (obj == null)
		{
			obj = new MapDtls(this.getNo());
			this.SetRefObject("MapDtls", obj);
		}
		return obj;
	}

	/** 
	 图片
	*/
	public final FrmImgs getFrmImgs() throws Exception {
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 附件
	*/
	public final FrmAttachments getFrmAttachments() throws Exception {
		Object tempVar = this.GetRefObject("FrmAttachments");
		FrmAttachments obj = tempVar instanceof FrmAttachments ? (FrmAttachments)tempVar : null;
		if (obj == null)
		{
			obj = new FrmAttachments(this.getNo());
			this.SetRefObject("FrmAttachments", obj);
		}
		return obj;
	}
	/** 
	 图片附件
	*/
	public final FrmImgAths getFrmImgAths() throws Exception {
		Object tempVar = this.GetRefObject("FrmImgAths");
		FrmImgAths obj = tempVar instanceof FrmImgAths ? (FrmImgAths)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgAths(this.getNo());
			this.SetRefObject("FrmImgAths", obj);
		}
		return obj;
	}
	/** 
	 单选按钮
	*/
	public final FrmRBs getFrmRBs() throws Exception {
		Object tempVar = this.GetRefObject("FrmRBs");
		FrmRBs obj = tempVar instanceof FrmRBs ? (FrmRBs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmRBs(this.getNo());
			this.SetRefObject("FrmRBs", obj);
		}
		return obj;
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


		///#region 属性
	public GEDtls HisGEDtls_temp = null;
	/** 
	 是否显示数量
	*/
	public final boolean isShowSum() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowSum);
	}
	public final void setShowSum(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsShowSum, value);
	}
	/** 
	 是否显示Idx
	*/
	public final boolean isShowIdx() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowIdx);
	}
	public final void setShowIdx(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsShowIdx, value);
	}
	/** 
	 是否显示标题
	*/
	public final boolean isShowTitle() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowTitle);
	}
	public final void setShowTitle(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsShowTitle, value);
	}
	/** 
	 是否是合流汇总数据
	*/
	public final boolean isHLDtl() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsHLDtl);
	}
	public final void setHLDtl(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsHLDtl, value);
	}
	/** 
	 是否是分流
	*/
	public final boolean isFLDtl() throws Exception {
		return this.GetParaBoolen(MapDtlAttr.IsFLDtl);
	}
	public final void setFLDtl(boolean value)throws Exception
	{this.SetPara(MapDtlAttr.IsFLDtl, value);
	}
	public int _IsReadonly = 2;
	public final boolean isReadonly() throws Exception {
		if (_IsReadonly != 2)
		{
			if (_IsReadonly == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		if (this.isDelete() || this.isInsert() || this.isUpdate())
		{
			_IsReadonly = 0;
			return false;
		}
		_IsReadonly = 1;
		return true;
	}
	/** 
	 是否可以删除？
	*/
	public final boolean isDelete() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsDelete);
	}
	public final void setDelete(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsDelete, value);
	}
	/** 
	 是否可以新增？
	*/
	public final boolean isInsert() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsInsert);
	}
	public final void setInsert(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsInsert, value);
	}
	/** 
	 是否可见
	*/
	public final boolean isView() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsView);
	}
	public final void setView(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsView, value);
	}
	/** 
	 是否可以更新？
	*/
	public final boolean isUpdate() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsUpdate);
	}
	public final void setUpdate(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsUpdate, value);
	}
	/** 
	 是否启用多附件
	*/
	public final boolean isEnableAthM() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableAthM);
	}
	public final void setEnableAthM(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsEnableAthM, value);
	}
	/** 
	 是否启用分组字段
	*/
	public final boolean isEnableGroupField() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableGroupField);
	}
	public final void setEnableGroupField(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsEnableGroupField, value);
	}
	/** 
	 是否起用审核连接
	*/
	public final boolean isEnablePass() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnablePass);
	}
	public final void setEnablePass(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsEnablePass, value);
	}



	/** 
	 是否copy数据？
	*/
	public final boolean isCopyNDData() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsCopyNDData);
	}
	public final void setCopyNDData(boolean value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.IsCopyNDData, value);
	}

	/** 
	 是否启用一对多多
	*/

	public boolean IsUse = false;
	/** 
	 是否检查人员的权限
	*/
	public final DtlOpenType getDtlOpenType() throws Exception {
		return DtlOpenType.forValue(this.GetValIntByKey(MapDtlAttr.DtlOpenType));
	}
	public final void setDtlOpenType(DtlOpenType value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.DtlOpenType, value.getValue());
	}
	/** 
	 分组字段
	*/
	public final String getGroupField() throws Exception
	{
		return this.GetValStrByKey(MapDtlAttr.GroupField);
	}
	public final void setGroupField(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.GroupField, value);
	}
	/** 
	 表单ID
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(MapDtlAttr.FK_MapData);
	}
	public final void setFKMapData(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.FK_MapData, value);
	}
	/** 
	 事件类.
	*/
	public final String getFEBD() throws Exception
	{
		return this.GetValStrByKey(MapDtlAttr.FEBD);
	}
	public final void setFEBD(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.FEBD, value);
	}

	/** 
	 数量
	*/
	public final int getRowsOfList() throws Exception {
			//如果不可以插入，就让其返回0.
		if (this.isInsert() == false)
		{
			return 0;
		}

		return this.GetValIntByKey(MapDtlAttr.RowsOfList);
	}
	public final void setRowsOfList(int value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.RowsOfList, value);
	}
	/** 
	 物理表
	*/
	public final String getPTable()  {
		String s = this.GetValStrByKey(MapDtlAttr.PTable);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			s = this.getNo();
			if (s.substring(0, 1).equals("0"))
			{
				return "T" + this.getNo();
			}
			else
			{
				return s;
			}
		}
		else
		{
			if (s.substring(0, 1).equals("0"))
			{
				return "T" + this.getNo();
			}
			else
			{
				return s;
			}
		}
	}
	public final void setPTable(String value)  throws Exception
	 {
		this.SetValByKey(MapDtlAttr.PTable, value);
	}
	/** 
	 多表头
	*/
	//public string MTR
	//{
	//    get
	//    {
	//        string s = this.GetValStrByKey(MapDtlAttr.MTR);
	//        s = s.Replace("《", "<");
	//        s = s.Replace("》", ">");
	//        s = s.Replace("‘", "'");
	//        return s;
	//    }
	//    set
	//    {
	//        string s = value;
	//        s = s.Replace("<", "《");
	//        s = s.Replace(">", "》");
	//        s = s.Replace("'", "‘");
	//        this.SetValByKey(MapDtlAttr.MTR, value);
	//    }
	//}

		///#endregion


		///#region 构造方法
	/** 
	 明细
	*/
	public MapDtlExt()  {
	}
	public MapDtlExt(String mypk)throws Exception
	{
		this.setNo(mypk);
		this._IsReadonly = 2;
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_MapDtl", "明细");

		map.setDepositaryOfEntity( Depositary.Application);
		map.IndexField = MapDtlAttr.FK_MapData;


			///#region 基础信息.
		map.AddGroupAttr("基本属性");
		map.AddTBStringPK(MapDtlAttr.No, null, "编号", true, false, 1, 100, 20);
		map.AddTBString(MapDtlAttr.Name, null, "名称", true, false, 1, 200, 20);
		map.AddTBString(MapDtlAttr.Alias, null, "别名", true, false, 0, 100, 20, false);
		map.SetHelperAlert(MapDtlAttr.Alias, "用于Excel表单有效.");

		map.AddTBString(MapDtlAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
		map.AddTBString(MapDtlAttr.PTable, null, "存储表", true, false, 0, 200, 20);
		map.SetHelperAlert(MapDtlAttr.PTable, "默认与编号为同一个存储表.");

		map.AddTBString(MapDtlAttr.FEBD, null, "事件类实体类", true, true, 0, 100, 20, false);

		map.AddDDLSysEnum(MapDtlAttr.Model, 0, "工作模式", true, true, MapDtlAttr.Model, "@0=普通@1=固定行");
		map.AddDDLSysEnum(MapDtlAttr.DtlVer, 0, "使用版本", true, true, MapDtlAttr.DtlVer, "@0=2017传统版");


			//map.AddTBString(MapDtlAttr.ImpFixTreeSql, null, "固定列树形SQL", true, false, 0, 500, 20);
			//map.AddTBString(MapDtlAttr.ImpFixDataSql, null, "固定列数据SQL", true, false, 0, 500, 20);

			//map.AddTBInt(MapDtlAttr.RowsOfList, 6, "初始化行数", true, false);
			//map.SetHelperAlert(MapDtlAttr.RowsOfList, "对第1个版本有效.");

		map.AddBoolean(MapDtlAttr.IsEnableGroupField, false, "是否启用分组字段", true, true);

		map.AddBoolean(MapDtlAttr.IsShowSum, true, "是否显示合计？", true, true);
		map.AddBoolean(MapDtlAttr.IsShowIdx, true, "是否显示序号？", true, true);

		map.AddBoolean(MapDtlAttr.IsReadonly, false, "是否只读？", true, true);
		map.AddBoolean(MapDtlAttr.IsShowTitle, true, "是否显示标题？", true, true);
		map.AddBoolean(MapDtlAttr.IsView, true, "是否可见？", true, true);

		map.AddBoolean(MapDtlAttr.IsInsert, true, "是否可以插入行？", true, true);
		map.AddBoolean(MapDtlAttr.IsDelete, true, "是否可以删除行？", true, true);
		map.AddBoolean(MapDtlAttr.IsUpdate, true, "是否可以更新？", true, true);
		map.AddBoolean(MapDtlAttr.IsEnableAthM, false, "是否启用多附件", true, true);
		map.AddBoolean(MapDtlAttr.IsImp, false, "是否可以导出？", true, true);
		map.AddBoolean(MapDtlAttr.IsCopyFirstData, false, "是否复制第一行数据？", true, true);


		map.AddTBString(MapDtlAttr.InitDBAttrs, null, "行初始化字段", true, false, 0, 40, 20, false);
		map.SetHelperAlert(MapDtlAttr.InitDBAttrs, "格式为:F1,F2,按照枚举外键字段的合集初始化行数据，一般用于不让其新增行。比如：为每个房间都要初始化一笔观测数据。.");


		map.AddDDLSysEnum(MapDtlAttr.WhenOverSize, 0, "超出行数", true, true, MapDtlAttr.WhenOverSize, "@0=不处理@1=向下顺增行@2=次页显示");

			// 为浙商银行设置从表打开.翻译.
		map.AddDDLSysEnum(MapDtlAttr.ListShowModel, 0, "列表数据显示格式", true, true, MapDtlAttr.ListShowModel, "@0=表格@1=卡片@2=自定义Url");

		map.AddDDLSysEnum(MapDtlAttr.EditModel, 0, "编辑数据方式", true, true, MapDtlAttr.EditModel, "@0=表格模式@1=傻瓜表单@2=开发者表单");
		map.SetHelperAlert(MapDtlAttr.EditModel, "格式为:第1种类型就要新建行,其他类型新建的时候弹出卡片.");
		map.AddTBString(MapDtlAttr.UrlDtl, null, "自定义Url", true, false, 0, 200, 20, true);


		map.AddTBInt(MapDtlAttr.NumOfDtl, 0, "最小从表集合", true, false);
		map.SetHelperAlert(MapDtlAttr.NumOfDtl, "用于控制输入的行数据最小值，比如：从表不能为空，就是用这个模式。");

			//用于控制傻瓜表单.
		map.AddTBFloat(MapDtlAttr.H, 350, "高度", true, false);
		map.SetHelperAlert(MapDtlAttr.H, "对傻瓜表单有效");

			//移动端数据显示方式
		map.AddDDLSysEnum(MapDtlAttr.MobileShowModel, 0, "移动端数据显示方式", true, true, MapDtlAttr.MobileShowModel, "@0=新页面显示模式@1=列表模式");
		map.AddTBString(MapDtlAttr.MobileShowField, null, "移动端列表显示字段", true, false, 0, 100, 20, false);

			//map.AddTBFloat(MapDtlAttr.X, 5, "距左", false, false);
			//map.AddTBFloat(MapDtlAttr.Y, 5, "距上", false, false);
			//map.AddTBFloat(MapDtlAttr.W, 200, "宽度", true, false);

			//map.AddTBFloat(MapDtlAttr.FrmW, 900, "表单宽度", true, true);
			//map.AddTBFloat(MapDtlAttr.FrmH, 1200, "表单高度", true, true);

			//对显示的结果要做一定的限制.
		map.AddTBString(MapDtlAttr.FilterSQLExp, null, "过滤数据SQL表达式", true, false, 0, 200, 20, true);
		map.SetHelperUrl(MapDtlAttr.FilterSQLExp, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=4711478&doc_id=31094");
			// map.SetHelperAlert(MapDtlAttr.FilterSQLExp, "格式为:WFState=1 过滤WFState=1的数据");

			//对显示的结果要做一定的限制.
		map.AddTBString(MapDtlAttr.OrderBySQLExp, null, "排序字段", true, false, 0, 200, 20, true);
		map.SetHelperAlert(MapDtlAttr.OrderBySQLExp, "格式1: MyFile1,MyField2 ,格式2: MyFile1 DESC  就是SQL语句的 Ordery By 后面的字符串，默认按照 OID (输入的顺序)排序.");

			//列自动计算表达式.
			//map.AddTBString(MapDtlAttr.ColAutoExp, null, "列自动计算", true, false, 0, 200, 20, true);
			//map.SetHelperAlert(MapDtlAttr.ColAutoExp, "格式为:@XiaoJi:Sum@NingLing:Avg 要对小计求合计,对年龄求平均数.不配置不显示.");

			//要显示的列.
		map.AddTBString(MapDtlAttr.ShowCols, null, "显示的列", true, false, 0, 500, 20, true);
		map.SetHelperAlert(MapDtlAttr.ShowCols, "默认为空,全部显示,如果配置了就按照配置的计算,格式为:field1,field2");

		map.AddTBString(MapDtlAttr.GUID, null, "GUID", false, false, 0, 128, 20);

			///#endregion 基础信息.


			///#region 导入导出填充. 此部分的功能
			// 2014-07-17 for xinchang bank.
		map.AddBoolean(MapDtlAttr.IsExp, true, "是否可以导入？", false, true);

			//导入模式.
		map.AddDDLSysEnum(MapDtlAttr.ImpModel, 0, "导入方式", false, false, MapDtlAttr.ImpModel, "@0=不导入@1=按配置模式导入@2=按照xls文件模版导入");
			//string strs = "如果是按照xls导入,请做一个从表ID.xls的模版文件放在:/DataUser/DtlTemplate/ 下面. 目前仅仅支持xls文件.";
			// map.SetHelperAlert(MapDtlAttr.ImpModel, strs);

		map.AddTBStringDoc(MapDtlAttr.ImpSQLInit, null, "初始化SQL(初始化表格的时候的SQL数据,可以为空)", false, false, true, 10);
		map.AddTBStringDoc(MapDtlAttr.ImpSQLSearch, null, "查询SQL(SQL里必须包含@Key关键字.)", false, false, true, 10);
		map.AddTBStringDoc(MapDtlAttr.ImpSQLFullOneRow, null, "数据填充一行数据的SQL(必须包含@Key关键字,为选择的主键)", false, false, true, 10);
		map.AddTBString(MapDtlAttr.ImpSQLNames, null, "列的中文名称", false, false, 0, 900, 20, true);


			///#endregion 导入导出填充.


			///#region 超链接.
		map.AddGroupAttr("超链接");
		map.AddBoolean(MapDtlAttr.IsEnableLink, false, "相关功能1", true, true);
		map.AddTBString(MapDtlAttr.LinkLabel, "", "超连接/功能标签", true, false, 0, 50, 100);
		map.AddDDLSysEnum(MapDtlAttr.ExcType, 0, "执行类型", true, true, "ExcType", "@0=超链接@1=函数");
		map.AddTBString(MapDtlAttr.LinkTarget, null, "LinkTarget", true, false, 0, 10, 100);
		map.AddTBString(MapDtlAttr.LinkUrl, null, "连接/函数", true, false, 0, 200, 200, true);


		map.AddBoolean(MapDtlAttr.IsEnableLink2, false, "相关功能2", true, true);
		map.AddTBString(MapDtlAttr.LinkLabel2, "", "超连接/功能标签", true, false, 0, 50, 100);
		map.AddDDLSysEnum(MapDtlAttr.ExcType2, 0, "执行类型", true, true, "ExcType", "@0=超链接@1=函数");
		map.AddTBString(MapDtlAttr.LinkTarget2, null, "LinkTarget", true, false, 0, 10, 100);
		map.AddTBString(MapDtlAttr.LinkUrl2, null, "连接/函数", true, false, 0, 200, 200, true);

			///#endregion 超链接.


			///#region 工作流相关.
		map.AddGroupAttr("工作流相关");
			//add 2014-02-21.
		map.AddGroupAttr("工作流相关");
		map.AddBoolean(MapDtlAttr.IsCopyNDData, true, "是否允许copy节点数据", true, true);
		map.AddTBInt(MapDtlAttr.FK_Node, 0, "节点(用户独立表单权限控制)", false, false);
		map.AddBoolean(MapDtlAttr.IsHLDtl, false, "是否是合流汇总", true, true);
		String sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='@No' AND  ( (MyDataType =1 and UIVisible=1 ) or (UIContralType=1))";
		map.AddDDLSQL(MapDtlAttr.SubThreadWorker, null, "子线程处理人字段", sql, true);
		map.AddBoolean(MapDtlAttr.IsEnablePass, false, "是否启用通过审核功能?", true, true);
		map.AddDDLSysEnum(MapDtlAttr.DtlOpenType, 1, "数据开放类型", true, true, MapDtlAttr.DtlOpenType, "@0=操作员@1=WorkID-流程ID@2=FID-干流程ID@3=PWorkID-父流程WorkID");

			///#endregion 工作流相关.


			///#region 相关方法.
		map.AddGroupMethod("基本功能");
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "隐藏字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".HidAttr";
			//rm.Icon = "../Img/Setting.png";
		rm.Icon = "icon-ghost";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "导入其它数据源字段"; // "设计表单";
		rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
		rm.ClassMethodName = this.toString() + ".ImpFields";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Icon = "icon-arrow-down-circle";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "icon-arrow-down-circle";
		rm.Title = "导入其它从表字段"; // "设计表单";
		rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
		rm.ClassMethodName = this.toString() + ".ImpFromDtlID";
		rm.getHisAttrs().AddTBString("ID", null, "请输入要导入的从表ID", true, false, 0, 100, 100);
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "icon-credit-card";
		rm.Title = "多表头"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoMultiTitle";
			// rm.Icon = "../Img/AttachmentM.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设计傻瓜表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DFoolFrm";
			//  rm.Icon = "../Img/Setting.png";
		rm.Icon = "icon-screen-desktop";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "从表附件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".OpenAthAttr";
			//  rm.Icon = "../Img/AttachmentM.png";
		rm.Icon = "icon-paper-clip";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "生成英文字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".GenerAttrs";
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "生成英文字段列，方便字段数据copy使用.";
		rm.Icon = "icon-heart";
		map.AddRefMethod(rm);

			///#endregion 相关方法.


			///#region 实验中的功能.
		map.AddGroupMethod("实验中的功能");
		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "列自动计算"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".ColAutoExp";
			// rm.Icon = "../Img/Setting.png";
		rm.Icon = "icon-pin";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "数据导入"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DtlImp";
			//rm.Icon = "../Img/Setting.png";
		rm.Icon = "icon-action-redo";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "数据导入v2019"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DtlImpV2019";
			//rm.Icon = "../Img/Setting.png";
		rm.Icon = "icon-action-redo";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoAction";
			//  rm.Icon = "../Img/Setting.png";
		rm.Icon = "icon-energy";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

			///#endregion 实验中的功能.

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception {
		//调用frmEditAction, 完成其他的操作.
		CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}
	/** 
	 导入其他从表字段
	 
	 @return 
	*/
	public final String ImpFromDtlID(String dtlId) throws Exception {
		MapDtl dtl = new MapDtl();
		dtl.setNo(dtlId);
		if (dtl.RetrieveFromDBSources() == 0)
		{
			return "err@" + dtlId + "输入错误.";
		}

		//MapDtl dtlOfThis = new MapDtl(this.No);
		//dtlOfThis.Copy(dtl);
		//dtlOfThis.setFK_MapData(this.FK_MapData);
		//dtlOfThis.Update();

		//删除当前从表Attrs.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.getNo());

		//查询出来要导入的.
		attrs.Retrieve(MapAttrAttr.FK_MapData, dtlId, null);

		//执行字段导入.
		for (MapAttr item : attrs.ToJavaList())
		{
			item.setFK_MapData(this.getNo());
			item.Save();
		}

		//删除当前从表 exts .
		MapExts exts = new MapExts();
		exts.Delete(MapAttrAttr.FK_MapData, this.getNo());

		//查询出来要导入的.
		exts.Retrieve(MapAttrAttr.FK_MapData, dtlId, null);

		//执行字段导入.
		for (MapExt item : exts.ToJavaList())
		{
			item.setFK_MapData(this.getNo());
			item.Save();
		}

		return "导入成功.";
	}
	/** 
	 打开从表附件属性.
	 
	 @return 
	*/
	public final String OpenAthAttr() throws Exception {
		String url = "../../Admin/FoolFormDesigner/DtlSetting/AthMDtl.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + bp.web.WebUser.getNo() + "&Token=" + bp.web.WebUser.getToken() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + bp.difference.SystemConfig.getCustomerNo();

		// string url = "../../Comm/RefFunc/En.htm?EnName=BP.Sys.FrmUI.FrmAttachmentExt&PKVal=" + this.No + "_AthMDtl";
		// string url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" + this.No + "&FromDtl=1&IsFirst=1&UserNo=" + bp.web.WebUser.getNo() + "&Token=" + bp.web.WebUser.SID + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + bp.difference.SystemConfig.getCustomerNo();
		return url;
	}

	/** 
	 导入
	 
	 @return 
	*/
	public final String DtlImp() throws Exception {
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + bp.web.WebUser.getNo() + "&Token=" + bp.web.WebUser.getToken() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + bp.difference.SystemConfig.getCustomerNo();
		// string url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp/Default.htm?FK_MapDtl=" + this.No + "&FromDtl=1";
		return url;
	}
	/** 
	 导入V2019
	 
	 @return 
	*/
	public final String DtlImpV2019() throws Exception {
		//string url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" + this.No + "&FromDtl=1&IsFirst=1&UserNo=" + bp.web.WebUser.getNo() + "&Token=" + bp.web.WebUser.SID + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + bp.difference.SystemConfig.getCustomerNo();
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp/Default.htm?FK_MapDtl=" + this.getNo() + "&FromDtl=1";
		return url;
	}
	/** 
	 列自动计算
	 
	 @return 
	*/
	public final String ColAutoExp() throws Exception {
		String url = "../../Admin/FoolFormDesigner/DtlSetting/ColAutoExp.htm?FK_MapData=" + this.getNo();
		return url;
	}
	/** 
	 导入其他表字段
	 
	 @return 
	*/
	public final String ImpFields() throws Exception {

		//  http://localhost:18272/WF/Admin/FoolFormDesigner/ImpTableField.htm?FK_MapData=CCFrm_CZBankBXDtl1&reset=true
		String url = "../../Admin/FoolFormDesigner/ImpTableField.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + bp.web.WebUser.getNo() + "&Token=" + bp.web.WebUser.getToken() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + bp.difference.SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DFoolFrm() throws Exception {
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + bp.web.WebUser.getNo() + "&Token=" + bp.web.WebUser.getToken() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + bp.difference.SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 多表头
	 
	 @return 
	*/
	public final String DoMultiTitle() throws Exception {
		return "../../Comm/Sys/MultiTitle.htm?EnsName=" + this.getNo() + "&DoType=Dtl";
	}


	public final String GenerAttrs() throws Exception {
		String strs = "";
		MapAttrs attrs = new MapAttrs(this.getNo());
		for (MapAttr item : attrs.ToJavaList())
		{
			strs += "\t\n " + item.getKeyOfEn() + ",";
		}
		return strs;
	}
	public final String DoAction() throws Exception {
		return "../../Admin/FoolFormDesigner/ActionForDtl.htm?DoType=Edit&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDate();
	}

	public final String HidAttr() throws Exception {
		return "../../Admin/FoolFormDesigner/HidAttr.htm?DoType=Edit&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDate();
	}


		///#region 基本属性.

	public final float getH() throws Exception
	{
		return this.GetValFloatByKey(MapDtlAttr.H);
	}
	public final float getFrmW() throws Exception
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmW);
	}
	public final float getFrmH() throws Exception
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmH);
	}

		///#endregion 基本属性.


	/** 
	 初始化自定义字段属性
	 
	 @return 返回执行结果
	*/
	public final String InitAttrsOfSelf() throws Exception {
		if (this.getFK_Node() == 0)
		{
			return "err@该从表属性不是自定义属性.";
		}

		if (this.getNo().contains("_" + this.getFK_Node()) == false)
		{
			return "err@该从表属性不是自定义属性.";
		}

		//求从表ID.
		String refDtl = this.getNo().replace("_" + this.getFK_Node(), "");

		//处理属性问题.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.getNo());
		attrs.Retrieve(MapAttrAttr.FK_MapData, refDtl, null);
		for (MapAttr attr : attrs.ToJavaList())
		{
			attr.setMyPK(this.getNo() + "_" + attr.getKeyOfEn());
			attr.setFK_MapData(this.getNo());
			attr.Insert();
		}

		//处理mapExt 的问题.
		MapExts exts = new MapExts();
		exts.Delete(MapAttrAttr.FK_MapData, this.getNo()); //先删除，后查询.
		exts.Retrieve(MapAttrAttr.FK_MapData, refDtl, null);
		MapExt mapExt = null;
		for (MapExt ext : exts.ToJavaList())
		{
			mapExt = new MapExt();
			mapExt = ext;
			mapExt.setMyPK(ext.getExtType() + "_" + this.getNo() + "_" + ext.getAttrOfOper());
			mapExt.setFK_MapData(this.getNo());
			mapExt.Insert();
		}

		//处理附件问题
		/* 如果启用了多附件*/
		if (this.isEnableAthM() == true)
		{
			FrmAttachment athDesc = new FrmAttachment();
			//获取原始附件的属性

			athDesc.setMyPK(this.getNo() + "_AthMDtl");
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				//获取原来附件的属性
				FrmAttachment oldAthDesc = new FrmAttachment();
				oldAthDesc.setMyPK(refDtl + "_AthMDtl");
				if (oldAthDesc.RetrieveFromDBSources() == 0)
				{
					return "原始从表的附件属性不存在，请联系管理员";
				}
				athDesc = oldAthDesc;
				athDesc.setMyPK(this.getNo()+ "_AthMDtl");
				athDesc.setFK_MapData(this.getNo());
				athDesc.setNoOfObj("AthMDtl");
				athDesc.setName( this.getName());
				athDesc.setIsVisable(false);
				athDesc.DirectInsert();
				//增加分组
				GroupField group = new GroupField();
				group.setLab(athDesc.getName());
				group.setFrmID(this.getFK_MapData());
				group.setCtrlType("Ath");
				group.setCtrlID(athDesc.getMyPK());
				group.setIdx(10);
				group.Insert();
			}

			//判断是否有隐藏的AthNum 字段
			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getNo() + "_AthNum");
			int count = attr.RetrieveFromDBSources();
			if (count == 0)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn("AthNum");
				attr.setName("附件数量");
				attr.setDefVal( "0");
				attr.setUIContralType(UIContralType.TB);
				attr.setMyDataType(DataType.AppInt);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.DirectInsert();
			}


		}

		return "执行成功";
	}

	@Override
	protected boolean beforeUpdate() throws Exception {
		MapDtl dtl = new MapDtl(this.getNo());
		//启用审核
		dtl.setIsEnablePass(this.isEnablePass());
		//超链接
		dtl.setIsEnableLink(this.isEnablePass());
		dtl.setLinkLabel(this.getLinkLabel());
		dtl.setLinkUrl(this.getLinkUrl());
		dtl.setLinkTarget(this.getLinkTarget());
		dtl.Update();

		//判断是否启用多附件
		if (this.isEnableAthM() == true)
		{
			bp.sys.FrmAttachment athDesc = new bp.sys.FrmAttachment();
			athDesc.setMyPK(this.getNo() + "_AthMDtl");
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				athDesc.setFK_MapData(this.getNo());
				athDesc.setNoOfObj("AthMDtl");
				athDesc.setName(this.getName());
				athDesc.setIsVisable(false);
				athDesc.DirectInsert();
				//增加分组
				GroupField group = new GroupField();
				group.setLab(athDesc.getName());
				group.setFrmID(this.getNo());
				group.setCtrlType("Ath");
				group.setCtrlID(athDesc.getMyPK());
				group.setIdx(10);
				group.Insert();
			}


			//判断是否有隐藏的AthNum 字段
			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getNo() + "_AthNum");
			int count = attr.RetrieveFromDBSources();
			if (count == 0)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn("AthNum");
				attr.setName("附件数量");
				attr.setDefVal( "0");
				attr.setUIContralType(UIContralType.TB);
				attr.setMyDataType(DataType.AppInt);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.DirectInsert();
				String a = "13";
			}
		}


		//获得事件实体.
		FormEventBaseDtl febd = bp.sys.base.Glo.GetFormDtlEventBaseByEnName(this.getNo());
		if (febd == null)
		{
			this.setFEBD("");
		}
		else
		{
			this.setFEBD(febd.toString());
		}



			///#region 检查填充的SQL是否符合要求.

			///#endregion

		//更新分组标签.  @fanleiwei. 代码有变化.
		GroupField gf = new GroupField();
		int i = gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.getNo());
		if (i == 0 && this.getFK_Node() == 0)
		{
			gf.setCtrlID(this.getNo());
			gf.setCtrlType("Dtl");
			gf.setFrmID(this.getFK_MapData());
			gf.Insert();
		}

		if (i > 1)
		{
			gf.Delete();
			i = gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.getNo());
		}

		if (i == 1 && gf.getLab().equals(this.getName()) == false)
		{
			gf.setLab(this.getName());
			gf.Update();
		}

		return super.beforeUpdate();
	}

	@Override
	protected void afterDelete() throws Exception {
		MapDtl dtl = new MapDtl();
		dtl.setNo(this.getNo());
		dtl.setFK_MapData(this.getFK_MapData());
		dtl.Delete();

		//删除分组
		GroupFields gfs = new GroupFields();
		gfs.RetrieveByLike(GroupFieldAttr.CtrlID, this.getNo() + "%");
		gfs.Delete();

		//如果启用了附件也需要删除
		if (this.isEnableAthM() == true)
		{
			FrmAttachment ath = new FrmAttachment();
			ath.Delete(FrmAttachmentAttr.MyPK, this.getNo() + "_AthMDtl");
		}


		//执行清空缓存到的AutoNum.
		MapData md = new MapData(this.getFK_MapData());
		md.ClearAutoNumCash(true); //更新缓存.

		super.afterDelete();
	}
	/** 
	 获取个数
	 
	 param fk_val
	 @return 
	*/
	public final int GetCountByFK(int workID) throws Exception {
		return DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE WorkID=" + workID);
	}
	public final int GetCountByFK(String field, String val)
	{
		return DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE " + field + "='" + val + "'");
	}
	public final int GetCountByFK(String field, long val)
	{
		return DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE " + field + "=" + val);
	}
	public final int GetCountByFK(String f1, long val1, String f2, String val2)
	{
		return DBAccess.RunSQLReturnValInt("SELECT COUNT(OID) from " + this.getPTable() + " WHERE " + f1 + "=" + val1 + " AND " + f2 + "='" + val2 + "'");
	}

		///#endregion

}
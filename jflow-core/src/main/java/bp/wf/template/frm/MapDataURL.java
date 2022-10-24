package bp.wf.template.frm;

import bp.da.*;
import bp.en.*;
import bp.sys.*;

/** 
 表单属性
*/
public class MapDataURL extends EntityNoName
{

		///#region 权限控制.
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		if (bp.web.WebUser.getNo().equals("admin") == true)
		{
			uac.IsDelete = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		return uac;
	}

		///#endregion 权限控制.


		///#region 自动计算属性.
	/** 
	 左边界.
	*/
	public final float getMaxLeft() throws Exception {
		return this.GetParaFloat(MapDataAttr.MaxLeft, 0);
	}
	public final void setMaxLeft(float value)throws Exception
	{this.SetPara(MapDataAttr.MaxLeft, value);
	}
	/** 
	 右边界
	*/
	public final float getMaxRight() throws Exception {
		return this.GetParaFloat(MapDataAttr.MaxRight, 0);
	}
	public final void setMaxRight(float value)throws Exception
	{this.SetPara(MapDataAttr.MaxRight, value);
	}
	/** 
	 最高top
	*/
	public final float getMaxTop() throws Exception {
		return this.GetParaFloat(MapDataAttr.MaxTop, 0);
	}
	public final void setMaxTop(float value)throws Exception
	{this.SetPara(MapDataAttr.MaxTop, value);
	}
	/** 
	 最低
	*/
	public final float getMaxEnd() throws Exception {
		return this.GetParaFloat(MapDataAttr.MaxEnd, 0);
	}
	public final void setMaxEnd(float value)throws Exception
	{this.SetPara(MapDataAttr.MaxEnd, value);
	}

		///#endregion 自动计算属性.


		///#region 报表属性(参数方式存储).
	/** 
	 是否关键字查询
	*/
	public final boolean isSearchKey() throws Exception {
		return this.GetParaBoolen(MapDataAttr.IsSearchKey, true);
	}
	public final void setSearchKey(boolean value)throws Exception
	{this.SetPara(MapDataAttr.IsSearchKey, value);
	}
	/** 
	 时间段查询方式
	*/
	public final DTSearchWay getDTSearchWay() throws Exception {
		return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.DTSearchWay, 0));
	}
	public final void setDTSearchWay(DTSearchWay value)throws Exception
	{this.SetPara(MapDataAttr.DTSearchWay, value.getValue());
	}
	/** 
	 时间字段
	*/
	public final String getDTSearchKey() throws Exception {
		return this.GetParaString(MapDataAttr.DTSearchKey);
	}
	public final void setDTSearchKey(String value)throws Exception
	{this.SetPara(MapDataAttr.DTSearchKey, value);
	}
	/** 
	 查询外键枚举字段
	*/
	public final String getRptSearchKeys() throws Exception {
		return this.GetParaString(MapDataAttr.RptSearchKeys, "*");
	}
	public final void setRptSearchKeys(String value)throws Exception
	{this.SetPara(MapDataAttr.RptSearchKeys, value);
	}

		///#endregion 报表属性(参数方式存储).


		///#region 外键属性
	public final String getVer() throws Exception
	{
		return this.GetValStringByKey(MapDataAttr.Ver);
	}
	public final void setVer(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.Ver, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.Idx);
	}
	public final void setIdx(int value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.Idx, value);
	}


		///#endregion

	public static boolean isEditDtlModel() throws Exception {
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
	public static void setEditDtlModel(boolean value)throws Exception
	{bp.web.WebUser.SetSessionByKey("IsEditDtlModel", "1");
	}


		///#region 属性
	/** 
	 物理表
	*/
	public final String getPTable() throws Exception {
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (DataType.IsNullOrEmpty(s) == true)
		{
			return this.getNo();
		}
		return s;
	}
	public final void setPTable(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 URL
	*/
	public final String getUrlExt() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.UrlExt);
	}
	public final void setUrlExt(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.UrlExt, value);
	}
	public final DBUrlType getHisDBUrl() throws Exception {
		return DBUrlType.AppCenterDSN;
	}
	public final AppType getHisAppType() throws Exception {
		return AppType.forValue(this.GetValIntByKey(MapDataAttr.AppType));
	}
	public final void setHisAppType(AppType value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.AppType, value.getValue());
	}
	/** 
	 备注
	*/
	public final String getNote() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Note);
	}
	public final void setNote(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.Note, value);
	}
	/** 
	 是否有CA.
	*/
	public final boolean isHaveCA() throws Exception {
		return this.GetParaBoolen("IsHaveCA", false);

	}
	public final void setHaveCA(boolean value)throws Exception
	{this.SetPara("IsHaveCA", value);
	}

	/** 
	 类别，可以为空.
	*/
	public final String getFKFormTree() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 从表集合.
	*/
	public final String getDtls() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Dtls);
	}
	public final void setDtls(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.Dtls, value);
	}
	/** 
	 主键
	*/
	public final String getEnPK() throws Exception {
		String s = this.GetValStrByKey(MapDataAttr.EnPK);
		if (DataType.IsNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	private Entities _HisEns = null;
	public final Entities getHisEns() throws Exception {
		if (_HisEns == null)
		{
			_HisEns = ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn() throws Exception {
		return this.getHisEns().getGetNewEntity();
	}
	public final float getFrmW() throws Exception
	{
		return this.GetValFloatByKey(MapDataAttr.FrmW);
	}
	public final void setFrmW(float value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.FrmW, value);
	}
	///// <summary>
	///// 表单控制方案
	///// </summary>
	//public string Slns
	//{
	//    get
	//    {
	//        return this.GetValStringByKey(MapDataAttr.Slns);
	//    }
	//    set
	//    {
	//        this.SetValByKey(MapDataAttr.Slns, value);
	//    }
	//}
	public final float getFrmH() throws Exception
	{
		return this.GetValFloatByKey(MapDataAttr.FrmH);
	}
	public final void setFrmH(float value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.FrmH, value);
	}
	/** 
	 表格显示的列
	*/
	public final int getTableCol() throws Exception {
		int i = this.GetValIntByKey(MapDataAttr.TableCol);
		if (i == 0 || i == 1)
		{
			return 4;
		}
		return i;
	}
	public final void setTableCol(int value)  throws Exception
	 {
		this.SetValByKey(MapDataAttr.TableCol, value);
	}


		///#endregion


		///#region 构造方法
	/** 
	 表单属性
	*/
	public MapDataURL()  {
	}
	/** 
	 表单属性
	 
	 param no 映射编号
	*/
	public MapDataURL(String no)
	{
		super(no);
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

		Map map = new Map("Sys_MapData", "URL表单属性");


			///#region 基本属性.
		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, false, 1, 200, 20);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.PTable, null, "存储表", false, false, 0, 500, 20);

		map.AddTBString(MapDataAttr.UrlExt, null, "URL连接", true, false, 0, 500, 20, true);

			//表单的运行类型.
		map.AddDDLSysEnum(MapDataAttr.FrmType, FrmType.FoolForm.getValue(), "表单类型",true, false, MapDataAttr.FrmType);

			//数据源.
		   // map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);

			///#endregion 基本属性.


			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapDataAttr.Note, null, "备注", true, false, true, 10);

			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", false, false);

			///#endregion 设计者信息.

			//RefMethod rm = new RefMethod();
			//rm.Title = "打开URL"; // "设计表单";
			//rm.GroupName = "基本功能";
			//rm.ClassMethodName = this.ToString() + ".DoOpenUrl";
			//rm.Icon = "../../WF/Img/FullData.png";
			//rm.Visable = true;
			//rm.refMethodType = RefMethodType.LinkeWinOpen;
			//rm.Target = "_blank";
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoOpenUrl() throws Exception {
		return this.getUrlExt();
	}
}
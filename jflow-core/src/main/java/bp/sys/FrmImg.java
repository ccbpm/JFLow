package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.sys.frmui.*;
import bp.difference.*;

/** 
 图片
*/
public class FrmImg extends EntityMyPK
{

		///#region 属性
	/** 
	 中文名称
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(FrmImgAttr.Name);
	}
	public final void setName(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.Name, value);
	}
	/** 
	 对应字段名称
	*/
	public final String getKeyOfEn() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)  throws Exception
	 {
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 英文名称
	*/
	public final String getEnPK() throws Exception
	{
		return this.GetValStringByKey(FrmImgAttr.EnPK);
	}
	public final void setEnPK(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.EnPK, value);
	}
	public final int getIsEdit() throws Exception
	{
		return this.GetValIntByKey(FrmImgAttr.IsEdit);
	}
	public final void setIsEdit(int value) throws Exception
	{
		this.SetValByKey(FrmImgAttr.IsEdit, value);
	}
	/** 
	 应用类型
	*/
	public final ImgAppType getHisImgAppType() throws Exception {
		return ImgAppType.forValue(this.GetValIntByKey(FrmImgAttr.ImgAppType));
	}
	public final void setHisImgAppType(ImgAppType value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.ImgAppType, value.getValue());
	}
	/** 
	 数据来源
	*/
	public final int getImgSrcType() throws Exception
	{
		return this.GetValIntByKey(FrmImgAttr.ImgSrcType);
	}
	public final void setImgSrcType(int value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.ImgSrcType, value);
	}

	public final String getTag0() throws Exception
	{
		return this.GetValStringByKey(FrmImgAttr.Tag0);
	}
	public final void setTag0(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.Tag0, value);
	}

	public final String getLinkTarget() throws Exception
	{
		return this.GetValStringByKey(FrmImgAttr.LinkTarget);
	}
	public final void setLinkTarget(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.LinkTarget, value);
	}
	/** 
	 URL
	*/
	public final String getLinkURL() throws Exception
	{
		return this.GetValStringByKey(FrmImgAttr.LinkURL);
	}
	public final void setLinkURL(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.LinkURL, value);
	}
	public final String getImgPath() throws Exception {
		String src = this.GetValStringByKey(FrmImgAttr.ImgPath);
		if (DataType.IsNullOrEmpty(src))
		{
			if (DataType.IsNullOrEmpty(src)) {
				src =  "DataUser/ICON/" + SystemConfig.getCustomerNo() + "/LogBiger.png";
			}
		}
		return src;
	}
	public final void setImgPath(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.ImgPath, value);
	}
	public final String getImgURL() throws Exception {
		String src = this.GetValStringByKey(FrmImgAttr.ImgURL);
		if (DataType.IsNullOrEmpty(src) || src.contains("component/Img"))
		{
			src =  "DataUser/ICON/" + SystemConfig.getCustomerNo() + "/LogBiger.png";
		}
		return src;
	}
	public final void setImgURL(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.ImgURL, value);
	}

	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmImgAttr.FK_MapData);
	}
	public final void setFKMapData(String value)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.FK_MapData, value);
	}
	public final void setFK_MapData(String val)  throws Exception
	 {
		this.SetValByKey(FrmImgAttr.FK_MapData, val);
	}
	public final float getUIWidth() throws Exception
	{
		return this.GetValFloatByKey(MapAttrAttr.UIWidth);
	}
	public final void setUIWidth(float value)  throws Exception
	 {
		this.SetValByKey(MapAttrAttr.UIWidth, value);
	}
	/** 
	 X
	*/
	public final float getUIHeight() throws Exception
	{
		return this.GetValFloatByKey(MapAttrAttr.UIHeight);
	}
	public final void setUIHeight(float value)  throws Exception
	 {
		this.SetValByKey(MapAttrAttr.UIHeight, value);
	}

	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmImgAttr.X, value);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmImgAttr.Y, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 图片
	*/
	public FrmImg()  {
	}
	/** 
	 图片
	 
	 param mypk
	*/
	public FrmImg(String mypk)throws Exception
	{
		this.setMyPK(mypk);
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
		Map map = new Map("Sys_FrmImg", "图片");
		map.IndexField = FrmImgAttr.FK_MapData;

		map.AddMyPK();

		map.AddTBString(FrmImgAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "对应字段", true, false, 1, 100, 20);

		map.AddTBInt(FrmImgAttr.ImgAppType, 0, "应用类型", false, false);

		map.AddTBFloat(MapAttrAttr.X, 5, "X", true, false);
		map.AddTBFloat(MapAttrAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(MapAttrAttr.UIWidth, 200, "H", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 160, "H", true, false);

		map.AddTBString(FrmImgAttr.ImgURL, null, "ImgURL", true, false, 0, 200, 20);
		map.AddTBString(FrmImgAttr.ImgPath, null, "ImgPath", true, false, 0, 200, 20);

		map.AddTBString(FrmImgAttr.LinkURL, null, "LinkURL", true, false, 0, 200, 20);
		map.AddTBString(FrmImgAttr.LinkTarget, "_blank", "LinkTarget", true, false, 0, 200, 20);

		map.AddTBString(FrmImgAttr.GUID, null, "GUID", true, false, 0, 128, 20);

			//如果是 seal 就是岗位集合。
		map.AddTBString(FrmImgAttr.Tag0, null, "参数", true, false, 0, 500, 20);
		map.AddTBInt(FrmImgAttr.ImgSrcType, 0, "图片来源0=本地,1=URL", true, false);
		map.AddTBInt(FrmImgAttr.IsEdit, 0, "是否可以编辑", true, false);
		map.AddTBString(FrmImgAttr.Name, null, "中文名称", true, false, 0, 500, 20);
		map.AddTBString(FrmImgAttr.EnPK, null, "英文名称", true, false, 0, 500, 20);
		map.AddTBInt(MapAttrAttr.ColSpan, 0, "单元格数量", false, true);
		map.AddTBInt(MapAttrAttr.LabelColSpan, 1, "文本单元格数量", false, true);
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", false, true);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getKeyOfEn()) == false)
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
		}
		return super.beforeInsert();
	}


}
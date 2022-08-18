package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import bp.en.Map;

import java.util.*;

/** 
 单选框
*/
public class FrmRB extends EntityMyPK
{

		///#region 属性
	/** 
	 提示
	*/
	public final String getTip() throws Exception
	{
		return this.GetValStringByKey(FrmRBAttr.Tip);
	}
	public final void setTip(String value)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.Tip, value);
	}

	/** 
	 字段设置值
	*/
	public final String getSetVal() throws Exception
	{
		return this.GetValStringByKey(FrmRBAttr.SetVal);
	}
	public final void setVal(String value)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.SetVal, value);
	}
	/** 
	 要执行的脚本
	*/
	public final String getScript() throws Exception
	{
		return this.GetValStringByKey(FrmRBAttr.Script);
	}
	public final void setScript(String value)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.Script, value);
	}

	/** 
	 字段-配置信息
	*/
	public final String getFieldsCfg() throws Exception
	{
		return this.GetValStringByKey(FrmRBAttr.FieldsCfg);
	}
	public final void setFieldsCfg(String value)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.FieldsCfg, value);
	}
	public final String getLab() throws Exception
	{
		return this.GetValStringByKey(FrmRBAttr.Lab);
	}

	public final void setLab(String val)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.Lab, val);
	}
	public final String getKeyOfEn() throws Exception
	{
		return this.GetValStringByKey(FrmRBAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.KeyOfEn, value);
	}

	public final int getIntKey() throws Exception
	{
		return this.GetValIntByKey(FrmRBAttr.IntKey);
	}
	public final void setIntKey(int val)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.IntKey, val);
	}

	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmRBAttr.FK_MapData);
	}
	public final void setFK_MapData(String val)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.FK_MapData, val);
	}
	public final String getEnumKey() throws Exception
	{
		return this.GetValStrByKey(FrmRBAttr.EnumKey);
	}
	public final void setEnumKey(String val)  throws Exception
	 {
		this.SetValByKey(FrmRBAttr.EnumKey, val);
	}
	public final int getFontSize() throws Exception {
		return this.GetParaInt(FrmRBAttr.FontSize, 12);
	}
	public final void setFontSize(int value)throws Exception
	{this.SetPara(FrmRBAttr.FontSize, value);
	}


	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmRBAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmRBAttr.X, value);
	}

	/**
	 Y
	 */
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmRBAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmRBAttr.Y, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 单选框
	*/
	public FrmRB()  {
	}
	public FrmRB(String mypk)throws Exception
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
		Map map = new Map("Sys_FrmRB", "单选框");
			//      map.EnDBUrl = new DBUrl(DBUrlType.DBAccessOfMSSQL1);

		map.IndexField = FrmImgAthDBAttr.FK_MapData;

		map.AddMyPK();
		map.AddTBString(FrmRBAttr.FK_MapData, null, "表单ID", true, false, 0, 300, 20);
		map.AddTBString(FrmRBAttr.KeyOfEn, null, "字段", true, false, 0, 300, 20);
		map.AddTBString(FrmRBAttr.EnumKey, null, "枚举值", true, false, 0, 30, 20);
		map.AddTBString(FrmRBAttr.Lab, null, "标签", true, false, 0, 500, 20);

		map.AddTBInt(FrmRBAttr.IntKey, 0, "IntKey", true, false);


		map.AddTBInt(MapAttrAttr.UIIsEnable, 0, "是否启用", true, false);

		map.AddTBFloat(FrmRBAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmRBAttr.Y, 5, "Y", false, false);

			//要执行的脚本.
		map.AddTBString(FrmRBAttr.Script, null, "要执行的脚本", true, false, 0, 4000, 20);
		map.AddTBString(FrmRBAttr.FieldsCfg, null, "配置信息@FieldName=Sta", true, false, 0, 4000, 20);
		map.AddTBString(FrmRBAttr.SetVal, null, "设置的值", true, false, 0, 200, 20);

		map.AddTBString(FrmRBAttr.Tip, null, "选择后提示的信息", true, false, 0, 1000, 20);
		map.AddTBString(FrmRBAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		map.AddTBAtParas(500);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn() + "_" + this.getIntKey());
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn() + "_" + this.getIntKey());
		return super.beforeUpdateInsertAction();
	}
}
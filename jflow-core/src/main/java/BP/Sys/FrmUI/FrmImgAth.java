package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 图片附件
*/
public class FrmImgAth extends EntityMyPK
{

		///#region 属性
	/** 
	 名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(FrmImgAthAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.Name, value);
	}
	/** 
	 控件ID
	 * @throws Exception 
	*/
	public final String getCtrlID() throws Exception
	{
		return this.GetValStringByKey(FrmImgAthAttr.CtrlID);
	}
	public final void setCtrlID(String value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.CtrlID, value);
	}
	/** 
	 Y
	 * @throws Exception 
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAthAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.Y, value);
	}
	/** 
	 X
	 * @throws Exception 
	*/
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAthAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.X, value);
	}
	/** 
	 H
	 * @throws Exception 
	*/
	public final float getH() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAthAttr.H);
	}
	public final void setH(float value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.H, value);
	}
	/** 
	 W
	 * @throws Exception 
	*/
	public final float getW() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAthAttr.W);
	}
	public final void setW(float value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.W, value);
	}
	/** 
	 FK_MapData
	 * @throws Exception 
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmImgAthAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.FK_MapData, value);
	}
	/** 
	 是否可编辑
	 * @throws Exception 
	*/
	public final boolean getIsEdit() throws Exception
	{
		return this.GetValBooleanByKey(FrmImgAthAttr.IsEdit);
	}
	public final void setIsEdit(boolean value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.IsEdit, value);
	}
	/** 
	 是否必填，2016-11-1
	 * @throws Exception 
	*/
	public final boolean getIsRequired() throws Exception
	{
		return this.GetValBooleanByKey(FrmImgAthAttr.IsRequired);
	}
	public final void setIsRequired(boolean value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.IsRequired, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	*/

	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 图片附件
	*/
	public FrmImgAth()
	{
	}
	/** 
	 图片附件
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmImgAth(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmImgAth", "图片附件");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.IndexField = MapAttrAttr.FK_MapData;

		map.AddMyPK();

		map.AddTBString(FrmImgAthAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
		map.AddTBString(FrmImgAthAttr.CtrlID, null, "控件ID", true, true, 0, 200, 20);
		map.AddTBString(FrmImgAthAttr.Name, null, "中文名称", true, false, 0, 200, 20);

		map.AddTBFloat(FrmImgAthAttr.X, 5, "X", false, false);
		map.AddTBFloat(FrmImgAthAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(FrmImgAthAttr.H, 200, "H", true, false);
		map.AddTBFloat(FrmImgAthAttr.W, 160, "W", true, false);

		map.AddBoolean(FrmImgAthAttr.IsEdit, true, "是否可编辑", true, true);
			//map.AddTBInt(FrmImgAthAttr.IsEdit, 1, "是否可编辑", true, true);
		map.AddBoolean(FrmImgAthAttr.IsRequired, false, "是否必填项", true, true);
			//map.AddTBInt(FrmImgAthAttr.IsRequired, 0, "是否必填项", true, true);
			//map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, true, 0, 128, 20);
		//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);
		map.AddTBInt(MapAttrAttr.ColSpan, 0, "单元格数量", false, true);

		//跨单元格
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString",
				"@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");
		//跨行
		map.AddDDLSysEnum(MapAttrAttr.RowSpan, 1, "行数", true, true, "RowSpanAttrString",
				"@1=跨1个行@2=跨2行@3=跨3行");
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		//this.setMyPK( this.FK_MapData + "_" + this.CtrlID;
		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		BP.Sys.FrmImgAth imgAth = new BP.Sys.FrmImgAth();
		imgAth.setMyPK(this.getMyPK());
		imgAth.RetrieveFromDBSources();
		imgAth.Update();

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/** 
	 删除后清缓存
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		//把相关的字段也要删除.
		MapAttrString attr = new MapAttrString();
		attr.setMyPK(this.getMyPK());
		attr.setFK_MapData(this.getFK_MapData());
		attr.Delete();
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}

}
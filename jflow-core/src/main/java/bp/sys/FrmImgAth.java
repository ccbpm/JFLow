package bp.sys;
import bp.en.*;
import bp.en.Map;

/** 
 图片附件
*/
public class FrmImgAth extends EntityMyPK
{
	private static final long serialVersionUID = 1L;

	///属性
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
	*/
	public final boolean getIsRequired() throws Exception
	{
		return this.GetValBooleanByKey(FrmImgAthAttr.IsRequired);
	}
	public final void setIsRequired(boolean value) throws Exception
	{
		this.SetValByKey(FrmImgAthAttr.IsRequired, value);
	}

		///


		///构造方法
	/** 
	 图片附件
	*/
	public FrmImgAth()
	{
	}
	/** 
	 图片附件
	 
	 @param mypk
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
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmImgAth", "图片附件");
		map.IndexField = FrmImgAttr.FK_MapData;

		map.AddMyPK();

		map.AddTBString(FrmImgAthAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmImgAthAttr.CtrlID, null, "控件ID", true, false, 0, 200, 20);
		map.AddTBString(FrmImgAthAttr.Name, null, "中文名称", true, false, 0, 200, 20);


		map.AddTBFloat(FrmImgAthAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmImgAthAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(FrmImgAthAttr.H, 200, "H", true, false);
		map.AddTBFloat(FrmImgAthAttr.W, 160, "W", false, false);

		map.AddTBInt(FrmImgAthAttr.IsEdit, 1, "是否可编辑", true, true);
		map.AddTBInt(FrmImgAthAttr.IsRequired, 0, "是否必填项", true, true);
		map.AddTBString(MapAttrAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFK_MapData() + "_" + this.getCtrlID());
		MapAttr attr = new MapAttr(this.getMyPK());
		attr.setName(this.getName());
		attr.Update();
		return super.beforeUpdateInsertAction();
	}
}
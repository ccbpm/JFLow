package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.WebUser;

import java.util.*;

/** 
 框架
*/
public class MapFrame extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsDelete = true;
			uac.IsUpdate = true;
		}
		return uac;
	}
	/** 
	 是否自适应大小
	 * @throws Exception 
	*/
	public final boolean getIsAutoSize() throws Exception
	{
		return this.GetValBooleanByKey(MapFrameAttr.IsAutoSize);
	}
	public final void setIsAutoSize(boolean value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.IsAutoSize, value);
	}
	/** 
	 名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStrByKey(MapFrameAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.Name, value);
	}
	/** 
	 EleID
	 * @throws Exception 
	*/
	public final String getFrmID() throws Exception
	{
		return this.GetValStrByKey(MapFrameAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.FrmID, value);
	}
	/** 
	 EleType
	 * @throws Exception 
	*/
	public final String getEleType() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleType);
	}
	public final void setEleType(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleType, value);
	}
	/** 
	 连接
	 * @throws Exception 
	*/
	public final String getURL() throws Exception
	{
		String s = this.GetValStrByKey(MapFrameAttr.FrameURL);
		if (DataType.IsNullOrEmpty(s))
		{
			return "http://ccflow.org";
		}
		return s;
	}
	public final void setURL(String value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.FrameURL, value);
	}
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmEleAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.X, value);
	}
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmEleAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Y, value);
	}
	/** 
	 高度
	 * @throws Exception 
	*/
	public final float getH() throws Exception
	{
		return this.GetValFloatByKey(MapFrameAttr.H, 700);

	}
	public final void setH(float value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.H, value);
	}
	/** 
	 宽度
	 * @throws Exception 
	*/
	public final float getW() throws Exception
	{
		return this.GetValFloatByKey(MapFrameAttr.W);
	}
	public final void setW(float value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.W, value);
	}
	public boolean IsUse = false;
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(MapFrameAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(MapFrameAttr.FK_MapData, value);
	}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 框架
	*/
	public MapFrame()
	{
	}
	/** 
	 框架
	 
	 @param no
	 * @throws Exception 
	*/
	public MapFrame(String mypk) throws Exception
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
		Map map = new Map("Sys_MapFrame", "框架");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(MapFrameAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
		map.AddTBString(MapFrameAttr.Name, null, "名称", true, false, 0, 200, 20, true);
		map.AddTBString(MapFrameAttr.URL, null, "URL", true, false, 0, 3000, 20, true);
		map.AddTBString(MapFrameAttr.FrameURL, null, "FrameURL", true, false, 0, 3000, 20, true);

		map.AddTBInt(MapFrameAttr.UrlSrcType, 0, "URL来源", false, false);


		  //  map.AddDDLSysEnum(MapFrameAttr.UrlSrcType, 0, "URL来源", true, true, MapFrameAttr.UrlSrcType,
			//"@0=自定义@1=地图@2=流程轨迹表@3=流程轨迹图");

		map.AddTBString(FrmEleAttr.Y, null, "Y", true, false, 0, 20, 20);
		map.AddTBString(FrmEleAttr.X, null, "x", true, false, 0, 20, 20);

		map.AddTBString(FrmEleAttr.W, null, "宽度", true, false, 0, 20, 20);
		map.AddTBString(FrmEleAttr.H, null, "高度", true, false, 0, 20, 20);

		map.AddBoolean(MapFrameAttr.IsAutoSize, true, "是否自动设置大小", false, false);

		map.AddTBString(FrmEleAttr.EleType, null, "类型", false, false, 0, 50, 20, true);

		   // map.AddTBInt(MapFrameAttr.RowIdx, 99, "位置", false, false);
		   // map.AddTBInt(MapFrameAttr.GroupID, 0, "GroupID", false, false);

		map.AddTBString(FrmEleAttr.GUID, null, "GUID", false, false, 0, 128, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 插入之后增加一个分组.
	 * @throws Exception 
	*/
	@Override
	protected void afterInsert() throws Exception
	{
		GroupField gf = new GroupField();

		gf.setFrmID(this.getFK_MapData());
		gf.setCtrlID(this.getMyPK());
		gf.setCtrlType("Frame");
		gf.setLab(this.getName());
		gf.setIdx(0);
		gf.Insert(); //插入.

		super.afterInsert();
	}
	/** 
	 删除之后的操作
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		GroupField gf = new GroupField();
		gf.Delete(GroupFieldAttr.CtrlID, this.getMyPK());

		super.afterDelete();
	}
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		GroupField gf = new GroupField();
		gf.Retrieve(GroupFieldAttr.CtrlID, this.getMyPK());
		gf.setLab(this.getName());
		gf.Update();

		return super.beforeUpdate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
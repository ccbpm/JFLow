package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.pub.*;
import bp.*;
import java.util.*;

/** 
 按钮
*/
public class FrmBtn extends EntityMyPK
{

		///#region 属性
	/** 
	 所在的分组
	*/
	public final int getGroupID() throws Exception
	{
		return this.GetValIntByKey(FrmBtnAttr.GroupID);
	}
	public final void setGroupID(long value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.GroupID, value);
	}
	public final String getMsgOK() throws Exception
	{
		return this.GetValStringByKey(FrmBtnAttr.MsgOK);
	}
	public final void setMsgOK(String value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.MsgOK, value);
	}
	public final String getMsgErr() throws Exception
	{
		return this.GetValStringByKey(FrmBtnAttr.MsgErr);
	}
	public final void setMsgErr(String value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.MsgErr, value);
	}
	/** 
	 EventContext
	*/
	public final String getEventContext() throws Exception
	{
		return this.GetValStringByKey(FrmBtnAttr.EventContext).replace("#", "@");
			//return this.GetValStringByKey(FrmBtnAttr.EventContext);
	}
	public final void setEventContext(String value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.EventContext, value);
	}

	public final String getUACContext() throws Exception
	{
		return this.GetValStringByKey(FrmBtnAttr.UACContext);
	}
	public final void setUACContext(String value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.UACContext, value);
	}
	/** 
	 IsEnable
	*/
	public final boolean isEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmBtnAttr.IsEnable);
	}
	public final void setEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.IsEnable, value);
	}
	/** 
	 Y
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmBtnAttr.Y);
	}
	public final void setY(float value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.Y, value);
	}
	/** 
	 X
	*/
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmBtnAttr.X);
	}
	public final void setX(float value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.X, value);
	}
	public final BtnEventType getHisBtnEventType() throws Exception {
		return BtnEventType.forValue(this.GetValIntByKey(FrmBtnAttr.EventType));
	}
	/** 
	 BtnType
	*/
	public final int getEventType() throws Exception
	{
		return this.GetValIntByKey(FrmBtnAttr.EventType);
	}
	public final void setEventType(int value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.EventType, value);
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmBtnAttr.FK_MapData);
	}
	public final void setFKMapData(String value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.FK_MapData, value);
	}
	/** 
	 Text
	*/
	public final String getLab() throws Exception
	{
		return this.GetValStrByKey(FrmBtnAttr.Lab);
	}
	public final void setLab(String value)  throws Exception
	 {
		this.SetValByKey(FrmBtnAttr.Lab, value);
	}
	public final String getTextHtml() throws Exception {
			//if (this.EventType)
			//    return "<b>" + this.GetValStrByKey(FrmBtnAttr.Text).Replace("@","<br>") + "</b>";
			//else
			return this.GetValStrByKey(FrmBtnAttr.Lab).replace("@", "<br>");
	}

		///#endregion


		///#region 构造方法
	/** 
	 按钮
	*/
	public FrmBtn()  {
	}
	/** 
	 按钮
	 
	 param mypk
	*/
	public FrmBtn(String mypk)throws Exception
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

		Map map = new Map("Sys_FrmBtn", "按钮");
		map.IndexField = FrmBtnAttr.FK_MapData;

		map.AddMyPK();
		map.AddTBString(FrmBtnAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmBtnAttr.Lab, null, "标签", true, false, 0, 3900, 20);

		map.AddTBFloat(FrmBtnAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmBtnAttr.Y, 5, "Y", false, false);

		map.AddTBInt(FrmBtnAttr.IsView, 0, "是否可见", false, false);
		map.AddTBInt(FrmBtnAttr.IsEnable, 0, "是否起用", false, false);

			//map.AddTBInt(FrmBtnAttr.BtnType, 0, "类型", false, false);

		map.AddTBInt(FrmBtnAttr.UAC, 0, "控制类型", false, false);
		map.AddTBString(FrmBtnAttr.UACContext, null, "控制内容", true, false, 0, 3900, 20);

		map.AddTBInt(FrmBtnAttr.EventType, 0, "事件类型", false, false);
		map.AddTBString(FrmBtnAttr.EventContext, null, "事件内容", true, false, 0, 3900, 20);

		map.AddTBString(FrmBtnAttr.MsgOK, null, "运行成功提示", true, false, 0, 500, 20);
		map.AddTBString(FrmBtnAttr.MsgErr, null, "运行失败提示", true, false, 0, 500, 20);

		map.AddTBString(FrmBtnAttr.BtnID, null, "按钮ID", true, false, 0, 128, 20);

		map.AddTBInt(FrmBtnAttr.GroupID, 0, "所在分组", false, false);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}
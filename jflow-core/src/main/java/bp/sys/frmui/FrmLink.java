package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.pub.*;
import bp.*;
import bp.sys.*;
import bp.web.WebUser;

import java.util.*;

/** 
 超连接
*/
public class FrmLink extends EntityMyPK
{

		///属性
	/** 
	 FontStyle
	 * @throws Exception 
	*/
	public final String getFontStyle() throws Exception
	{
		return this.GetValStringByKey(FrmLinkAttr.FontStyle);
	}
	public final void setFontStyle(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.FontStyle, value);
	}
	public final String getFontColorHtml() throws Exception
	{
		return PubClass.ToHtmlColor(this.getFontColor());
	}
	/** 
	 FontColor
	*/
	public final String getFontColor() throws Exception
	{
		return this.GetValStringByKey(FrmLinkAttr.FontColor);
	}
	public final void setFontColor(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.FontColor, value);
	}
	public final String getURL() throws Exception
	{
		return this.GetValStringByKey(FrmLinkAttr.URLExt).replace("#", "@");
	}
	public final void setURL(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.URLExt, value);
	}
	/** 
	 Font
	*/
	public final String getFontName() throws Exception
	{
		return this.GetValStringByKey(FrmLinkAttr.FontName);
	}
	public final void setFontName(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.FontName, value);
	}
	/** 
	 Y
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmLinkAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.Y, value);
	}
	/** 
	 X
	*/
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmLinkAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.X, value);
	}
	/** 
	 FontSize
	*/
	public final int getFontSize() throws Exception
	{
		return this.GetValIntByKey(FrmLinkAttr.FontSize);
	}
	public final void setFontSize(int value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.FontSize, value);
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmLinkAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.FK_MapData, value);
	}
	/** 
	 Text
	*/
	public final String getLab() throws Exception
	{
		return this.GetValStrByKey(FrmLinkAttr.Lab);
	}
	public final void setLab(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.Lab, value);
	}
	public final String getTarget() throws Exception
	{
		return this.GetValStringByKey(FrmLinkAttr.Target);
	}
	public final void setTarget(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.Target, value);
	}
	public final boolean getIsBold() throws Exception
	{
		return this.GetValBooleanByKey(FrmLabAttr.IsBold);
	}
	public final void setIsBold(boolean value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.IsBold, value);
	}
	public final boolean getIsItalic() throws Exception
	{
		return this.GetValBooleanByKey(FrmLabAttr.IsItalic);
	}
	public final void setIsItalic(boolean value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.IsItalic, value);
	}

		///


		///构造方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.Readonly();
		if (WebUser.getNo().equals("admin") == true)
		{
			uac.IsUpdate = true;
		}
		return uac;
	}
	/** 
	 超连接
	*/
	public FrmLink()
	{
	}
	/** 
	 超连接
	 
	 @param mypk
	*/
	public FrmLink(String mypk) throws Exception
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
		Map map = new Map("Sys_FrmLink", "超连接");
		map.IndexField = MapAttrAttr.FK_MapData;


			//连接目标.
		map.AddMyPK();
		map.AddTBString(FrmLinkAttr.Target, "_blank", "连接目标(_blank,_parent,_self)", true, false, 0, 20, 20);
		map.AddTBString(FrmLinkAttr.Lab, "New Link", "标签", true, false, 0, 500, 20, true);
		map.AddTBString(FrmLinkAttr.URLExt, null, "URL", true, false, 0, 500, 20, true);
		map.AddTBString(FrmLinkAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData' AND (CtrlType IS NULL OR CtrlType='')  ", true);


		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{


		bp.sys.FrmLink frmLink = new bp.sys.FrmLink();
		frmLink.setMyPK(this.getMyPK());
		frmLink.RetrieveFromDBSources();
		frmLink.Update();

		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/** 
	 删除后清缓存
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}

		///
}
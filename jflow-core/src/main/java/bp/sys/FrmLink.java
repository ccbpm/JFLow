package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.pub.*;
import bp.*;
import java.util.*;

/** 
 超连接
*/
public class FrmLink extends EntityMyPK
{

		///属性
	/** 
	 FontStyle
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
	public final String getURLExt() throws Exception
	{
		return this.GetValStringByKey(FrmLinkAttr.URLExt).replace("#","@");
	}
	public final void setURLExt(String value) throws Exception
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
	public final String getText() throws Exception
	{
		return this.GetValStrByKey(FrmLinkAttr.Text);
	}
	public final void setText(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.Text, value);
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
		map.IndexField = FrmImgAthDBAttr.FK_MapData;

		map.AddMyPK();

		map.AddTBString(FrmLinkAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmLinkAttr.Text, "New Link", "Label", true, false, 0, 500, 20);

		map.AddTBString(FrmLinkAttr.URLExt, null, "URLExt", true, false, 0, 500, 20);

		map.AddTBString(FrmLinkAttr.Target, "_blank", "Target", true, false, 0, 20, 20);

		map.AddTBFloat(FrmLinkAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmLinkAttr.Y, 5, "Y", false, false);

		map.AddTBInt(FrmLinkAttr.FontSize, 12, "FontSize", false, false);
		map.AddTBString(FrmLinkAttr.FontColor, "black", "FontColor", true, false, 0, 50, 20);
		map.AddTBString(FrmLinkAttr.FontName, null, "FontName", true, false, 0, 50, 20);
		map.AddTBString(FrmLinkAttr.FontStyle, "normal", "FontStyle", true, false, 0, 50, 20);

		map.AddTBInt(FrmLabAttr.IsBold, 0, "IsBold", false, false);
		map.AddTBInt(FrmLabAttr.IsItalic, 0, "IsItalic", false, false);

		map.AddTBString(FrmLabAttr.GUID, null, "GUID", true, false, 0, 128, 20);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}
package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 超连接
*/
public class FrmLink extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 FontStyle
	*/
	public final String getFontStyle()
	{
		return this.GetValStringByKey(FrmLinkAttr.FontStyle);
	}
	public final void setFontStyle(String value)
	{
		this.SetValByKey(FrmLinkAttr.FontStyle, value);
	}
	public final String getFontColorHtml()
	{
		return PubClass.ToHtmlColor(this.getFontColor());
	}
	/** 
	 FontColor
	*/
	public final String getFontColor()
	{
		return this.GetValStringByKey(FrmLinkAttr.FontColor);
	}
	public final void setFontColor(String value)
	{
		this.SetValByKey(FrmLinkAttr.FontColor, value);
	}
	public final String getURL()
	{
		return this.GetValStringByKey(FrmLinkAttr.URL).replace("#","@");
	}
	public final void setURL(String value)
	{
		this.SetValByKey(FrmLinkAttr.URL, value);
	}
	/** 
	 Font
	*/
	public final String getFontName()
	{
		return this.GetValStringByKey(FrmLinkAttr.FontName);
	}
	public final void setFontName(String value)
	{
		this.SetValByKey(FrmLinkAttr.FontName, value);
	}
	/** 
	 Y
	*/
	public final float getY()
	{
		return this.GetValFloatByKey(FrmLinkAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(FrmLinkAttr.Y, value);
	}
	/** 
	 X
	*/
	public final float getX()
	{
		return this.GetValFloatByKey(FrmLinkAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(FrmLinkAttr.X, value);
	}
	/** 
	 FontSize
	*/
	public final int getFontSize()
	{
		return this.GetValIntByKey(FrmLinkAttr.FontSize);
	}
	public final void setFontSize(int value)
	{
		this.SetValByKey(FrmLinkAttr.FontSize, value);
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmLinkAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmLinkAttr.FK_MapData, value);
	}
	/** 
	 Text
	*/
	public final String getText()
	{
		return this.GetValStrByKey(FrmLinkAttr.Text);
	}
	public final void setText(String value)
	{
		this.SetValByKey(FrmLinkAttr.Text, value);
	}
	public final String getTarget()
	{
		return this.GetValStringByKey(FrmLinkAttr.Target);
	}
	public final void setTarget(String value)
	{
		this.SetValByKey(FrmLinkAttr.Target, value);
	}
	public final boolean getIsBold()
	{
		return this.GetValBooleanByKey(FrmLabAttr.IsBold);
	}
	public final void setIsBold(boolean value)
	{
		this.SetValByKey(FrmLabAttr.IsBold, value);
	}
	public final boolean getIsItalic()
	{
		return this.GetValBooleanByKey(FrmLabAttr.IsItalic);
	}
	public final void setIsItalic(boolean value)
	{
		this.SetValByKey(FrmLabAttr.IsItalic, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
	public FrmLink(String mypk)
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
		Map map = new Map("Sys_FrmLink", "超连接");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.IndexField = FrmImgAthDBAttr.FK_MapData;


		map.AddMyPK();

		map.AddTBString(FrmLinkAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmLinkAttr.Text, "New Link", "Label", true, false, 0, 500, 20);

		map.AddTBString(FrmLinkAttr.URL, null, "URL", true, false, 0, 500, 20);

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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.pub.*;
import bp.*;
import java.util.*;

/** 
 标签
*/
public class FrmLab extends EntityMyPK
{

		///属性
	/** 
	 FontStyle
	*/
	public final String getFontStyle() throws Exception
	{
		return this.GetValStringByKey(FrmLabAttr.FontStyle);
	}
	public final void setFontStyle(String value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.FontStyle, value);
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
		return this.GetValStringByKey(FrmLabAttr.FontColor);
	}
	public final void setFontColor(String value) throws Exception
	{
		switch (value)
		{
			case "#FF000000":
				this.SetValByKey(FrmLabAttr.FontColor, "Red");
				return;
			default:
				break;
		}
		this.SetValByKey(FrmLabAttr.FontColor, value);
	}
	public final String getFontWeight() throws Exception
	{
		return this.GetValStringByKey(FrmLabAttr.FontWeight);
	}
	public final void setFontWeight(String value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.FontWeight, value);
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
	/** 
	 FontName
	*/
	public final String getFontName() throws Exception
	{
		return this.GetValStringByKey(FrmLabAttr.FontName);
	}
	public final void setFontName(String value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.FontName, value);
	}
	/** 
	 Y
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmLabAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.Y, value);
	}
	/** 
	 X
	*/
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmLabAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.X, value);
	}
	/** 
	 FontSize
	*/
	public final int getFontSize() throws Exception
	{
		return this.GetValIntByKey(FrmLabAttr.FontSize);
	}
	public final void setFontSize(int value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.FontSize, value);
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmLabAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.FK_MapData, value);
	}
	/** 
	 Text
	*/
	public final String getLab() throws Exception
	{
		return this.GetValStrByKey(FrmLabAttr.Lab);
	}
	public final void setLab(String value) throws Exception
	{
		this.SetValByKey(FrmLabAttr.Lab, value);
	}
	public final String getTextHtml() throws Exception
	{
		if (this.getIsBold())
		{
			return "<b>" + this.GetValStrByKey(FrmLabAttr.Lab).replace("@","<br>") + "</b>";
		}
		else
		{
			return this.GetValStrByKey(FrmLabAttr.Lab).replace("@", "<br>");
		}
	}

		///


		///构造方法
	/** 
	 标签
	*/
	public FrmLab()
	{
	}
	/** 
	 标签
	 
	 @param mypk
	*/
	public FrmLab(String mypk) throws Exception
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
		Map map = new Map("Sys_FrmLab", "标签");


		map.IndexField = FrmImgAthDBAttr.FK_MapData;


		map.AddMyPK();
		map.AddTBString(FrmLabAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmLabAttr.Lab, "New Label", "Label", true, false, 0, 3900, 20);

		map.AddTBFloat(FrmLabAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmLabAttr.Y, 5, "Y", false, false);

		map.AddTBInt(FrmLabAttr.FontSize, 12, "字体大小", false, false);
		map.AddTBString(FrmLabAttr.FontColor, "black", "颜色", true, false, 0, 50, 20);
		map.AddTBString(FrmLabAttr.FontName, null, "字体名称", true, false, 0, 50, 20);
		map.AddTBString(FrmLabAttr.FontStyle, "normal", "字体风格", true, false, 0, 200, 20);
		map.AddTBString(FrmLabAttr.FontWeight, "normal", "字体宽度", true, false, 0, 50, 20);

		map.AddTBInt(FrmLabAttr.IsBold, 0, "是否粗体", false, false);
		map.AddTBInt(FrmLabAttr.IsItalic, 0, "是否斜体", false, false);
		map.AddTBString(FrmLabAttr.GUID, null, "GUID", true, false, 0, 128, 20);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	/** 
	 是否存在相同的数据?
	 
	 @return 
	*/
	public final boolean IsExitGenerPK() throws Exception
	{
		String sql = "SELECT COUNT(*) FROM " + this.get_enMap().getPhysicsTable() + " WHERE FK_MapData='" + this.getFK_MapData() + "' AND X=" + this.getX() + " AND Y=" + this.getY() + "  and Lab='" + this.getLab() + "'";
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			return false;
		}
		return true;
	}

}
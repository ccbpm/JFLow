package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 标签
 
*/
public class FrmLab extends EntityMyPK
{

		
	/** 
	 FontStyle
	 
	*/
	public final String getFontStyle()
	{
		return this.GetValStringByKey(FrmLabAttr.FontStyle);
	}
	public final void setFontStyle(String value)
	{
		this.SetValByKey(FrmLabAttr.FontStyle, value);
	}
	public final String getFontColorHtml()
	{
		try {
			return PubClass.ToHtmlColor(this.getFontColor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 FontColor
	 
	*/
	public final String getFontColor()
	{
		return this.GetValStringByKey(FrmLabAttr.FontColor);
	}
	public final void setFontColor(String value)
	{

//		switch (value)
//ORIGINAL LINE: case "#FF000000":
		if (value.equals("#FF000000"))
		{
				this.SetValByKey(FrmLabAttr.FontColor, "Red");
				return;
		}
		else
		{
		}
		this.SetValByKey(FrmLabAttr.FontColor, value);
	}
	public final String getFontWeight()
	{
		return this.GetValStringByKey(FrmLabAttr.FontWeight);
	}
	public final void setFontWeight(String value)
	{
		this.SetValByKey(FrmLabAttr.FontWeight, value);
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
	/** 
	 FontName
	 
	*/
	public final String getFontName()
	{
		return this.GetValStringByKey(FrmLabAttr.FontName);
	}
	public final void setFontName(String value)
	{
		this.SetValByKey(FrmLabAttr.FontName, value);
	}
	/** 
	 Y
	 
	*/
	public final float getY()
	{
		return this.GetValFloatByKey(FrmLabAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(FrmLabAttr.Y, value);
	}
	/** 
	 X
	 
	*/
	public final float getX()
	{
		return this.GetValFloatByKey(FrmLabAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(FrmLabAttr.X, value);
	}
	/** 
	 FontSize
	 
	*/
	public final int getFontSize()
	{
		return this.GetValIntByKey(FrmLabAttr.FontSize);
	}
	public final void setFontSize(int value)
	{
		this.SetValByKey(FrmLabAttr.FontSize, value);
	}
	/** 
	 FK_MapData
	 
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmLabAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmLabAttr.FK_MapData, value);
	}
	/** 
	 Text
	 
	*/
	public final String getText()
	{
		return this.GetValStrByKey(FrmLabAttr.Text);
	}
	public final void setText(String value)
	{
		this.SetValByKey(FrmLabAttr.Text, value);
	}
	public final String getTextHtml()
	{
		if (this.getIsBold())
		{
			return "<b>" + this.GetValStrByKey(FrmLabAttr.Text).replace("@","<br>") + "</b>";
		}
		else
		{
			return this.GetValStrByKey(FrmLabAttr.Text).replace("@", "<br>");
		}
	}

		///#endregion


		
	/** 
	 标签
	 
	*/
	public FrmLab()
	{
	}
	/** 
	 标签
	 
	 @param mypk
	 * @throws Exception 
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
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmLab", "标签");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(FrmLabAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmLabAttr.Text, "New Label", "Label", true, false, 0, 3900, 20);

		map.AddTBFloat(FrmLabAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmLabAttr.Y, 5, "Y", false, false);

		map.AddTBInt(FrmLabAttr.FontSize, 12, "字体大小", false, false);
		map.AddTBString(FrmLabAttr.FontColor, "black", "颜色", true, false, 0, 50, 20);
		map.AddTBString(FrmLabAttr.FontName, null, "字体名称", true, false, 0, 50, 20);
		map.AddTBString(FrmLabAttr.FontStyle, "normal", "字体风格", true, false, 0, 200, 20);
		map.AddTBString(FrmLabAttr.FontWeight, "normal", "字体宽度", true, false, 0, 50, 20);

		map.AddTBInt(FrmLabAttr.IsBold, 0, "是否粗体", false, false);
		map.AddTBInt(FrmLabAttr.IsItalic, 0, "是否斜体", false, false);
		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


	/** 
	 是否存在相同的数据?
	 
	 @return 
	*/
	public final boolean IsExitGenerPK()
	{
		String sql = "SELECT COUNT(*) FROM " + this.getEnMap().getPhysicsTable() + " WHERE FK_MapData='" + this.getFK_MapData() + "' AND X=" + this.getX() + " AND Y=" + this.getY() + "  and Text='" + this.getText()+"'";
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			return false;
		}
		return true;
	}

}
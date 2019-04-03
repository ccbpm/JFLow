package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.DA.Log;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.FrmLabAttr;
import BP.Sys.FrmLinkAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.PubClass;

/** 
超连接
*/
public class FrmLink extends EntityMyPK
{
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
		try {
			return PubClass.ToHtmlColor(this.getFontColor());
		} catch (Exception e) {
			Log.DebugWriteError("frmLink getFontColorHtml:"+e.getMessage());
			e.printStackTrace();
		}
		return "";
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
	
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.Readonly();
		//if (BP.Web.WebUser.getNo().equals("admin"))
		//{
			uac.IsUpdate = true;
		//}
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
	 * @throws Exception 
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

		map.AddMyPK();

		map.AddTBString(FrmLinkAttr.Target, "_blank", "连接目标", true, false, 0, 20, 20);
		map.SetHelperAlert(FrmLinkAttr.Target, "可以输入:_blank,_parent,_self");

		map.AddTBString(FrmLinkAttr.Text, "New Link", "标签", true, false, 0, 500, 20, true);
		map.AddTBString(FrmLinkAttr.URL, null, "URL", true, false, 0, 500, 20, true);
		map.AddTBString(FrmLinkAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);


			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "所在分组", MapAttrString.SQLOfGroupAttr(), true);


		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	 protected  void afterInsertUpdateAction() throws Exception
     {
         BP.Sys.FrmLink frmLink = new BP.Sys.FrmLink();
         frmLink.setMyPK(this.getMyPK());
         frmLink.RetrieveFromDBSources();
         frmLink.Update();
         super.afterInsertUpdateAction();
     }
}

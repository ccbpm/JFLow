package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
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
		return this.GetValStringByKey(FrmLinkAttr.URL).replace("#", "@");
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
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		if (BP.Web.WebUser.getNo().equals("admin"))
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
		map.IndexField = MapAttrAttr.FK_MapData;


			//连接目标.
		map.AddMyPK();
		map.AddTBString(FrmLinkAttr.Target, "_blank", "连接目标(_blank,_parent,_self)", true, false, 0, 20, 20);
		map.AddTBString(FrmLinkAttr.Text, "New Link", "标签", true, false, 0, 500, 20, true);
		map.AddTBString(FrmLinkAttr.URL, null, "URL", true, false, 0, 500, 20, true);
		map.AddTBString(FrmLinkAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData' AND (CtrlType IS NULL OR CtrlType='')  ", true);


		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		BP.Sys.FrmLink frmLink = new BP.Sys.FrmLink();
		frmLink.setMyPK(this.getMyPK());
		frmLink.RetrieveFromDBSources();
		frmLink.Update();

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
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Web.WebUser;

import java.util.*;

/** 
 超连接
*/
public class FrmLink extends EntityMyPK
{

		///#region 属性
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
	 * @throws Exception 
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
		return this.GetValStringByKey(FrmLinkAttr.URLExt).replace("#", "@");
	}
	public final void setURLExt(String value) throws Exception
	{
		this.SetValByKey(FrmLinkAttr.URLExt, value);
	}
	/** 
	 Font
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
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
		///#endregion

		///#region 构造方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.Readonly();
		if (WebUser.getNo().equals("admin"))
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

		///#endregion
}
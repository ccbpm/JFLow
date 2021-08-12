package bp.gpm.home;

import bp.da.DBAccess;
import bp.da.DataType;
import bp.da.Depositary;
import bp.en.EnType;
import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.UAC;

/** 
信息块
*/
public class WindowTemplate extends EntityNoName
{
///#region 权限控制.
	/** 
	 控制权限
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (bp.web.WebUser.getIsAdmin() == true)
		{
			uac.OpenAll();
		}
		else
		{
			uac.IsView = false;
		}
		return uac;
	}

/** 
 文件内容
*/
public final WinDocType getWinDocType() throws Exception{
	return WinDocType.forValue(this.GetValIntByKey(WindowTemplateAttr.WinDocType));
}

public final void setWinDocType(WinDocType value) throws Exception{
	this.SetValByKey(WindowTemplateAttr.WinDocType, value.getValue());
}
/** 
窗口模式
*/
public final String getWinDocModel()throws Exception
{
	return this.GetValStringByKey(WindowTemplateAttr.WinDocModel);
}
public final void setWinDocModel(String value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.WinDocModel, value);
}

/** 
 更多的URL
*/
public final String getMoreUrl()throws Exception
{
	return this.GetValStrByKey(WindowTemplateAttr.MoreUrl);
}
public final void setMoreUrl(String value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.MoreUrl, value);
}
/** 
 更多标签
*/
public final String getMoreLab()throws Exception
{
	return this.GetValStrByKey(WindowTemplateAttr.MoreLab);
}
public final void setMoreLab(String value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.MoreLab, value);
}
public final int getPopW()throws Exception
{
	return this.GetValIntByKey(WindowTemplateAttr.PopW);
}
public final void setPopW(int value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.PopW, value);
}
public final int getPopH()throws Exception
{
	return this.GetValIntByKey(WindowTemplateAttr.PopH);
}
public final void setPopH(int value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.PopH, value);
}
public final int getColSpan()throws Exception
{
	return this.GetValIntByKey(WindowTemplateAttr.ColSpan);
}
public final void setColSpan(int value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.ColSpan, value);
}
public final int getMoreLinkModel()throws Exception
{
	return this.GetValIntByKey(WindowTemplateAttr.MoreLinkModel);
}
public final void setMoreLinkModel(int value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.MoreLinkModel, value);
}
/** 
 标题
*/

/** 
 用户是否可以删除
*/
public final boolean getIsDel()throws Exception
{
	return this.GetValBooleanByKey(WindowTemplateAttr.IsDel);
}
public final void setIsDel(boolean value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.IsDel, value);
}
/** 
 是否禁用?
*/
public final boolean getIsEnable()throws Exception
{
	return this.GetValBooleanByKey(WindowTemplateAttr.IsEnable);
}
public final void setIsEnable(boolean value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.IsEnable, value);
}

/** 
 打开方式
*/
public final int getOpenWay()throws Exception
{
	return this.GetValIntByKey(WindowTemplateAttr.OpenWay);
}
public final void setOpenWay(int value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.OpenWay, value);
}
/** 
 顺序号
*/
public final int getIdx()throws Exception
{
	return this.GetValIntByKey(WindowTemplateAttr.Idx);
}
public final void setIdx(int value)throws Exception
{
	this.SetValByKey(WindowTemplateAttr.Idx, value);
}
public final String getDocGenerRDT()throws Exception
{
	return this.GetValStrByKey(WindowTemplateAttr.DocGenerRDT);
}
public final void setDocGenerRDT(String value) throws Exception
{
	this.SetValByKey(WindowTemplateAttr.DocGenerRDT, value);
}

public final String getDocs() throws Exception
{
	return this.GetValStrByKey(WindowTemplateAttr.Docs);
}
public final void setDocs(String value) throws Exception
{
	this.SetValByKey(WindowTemplateAttr.Docs, value);
}
public final String getPageID() throws Exception
{
	return this.GetValStrByKey(WindowTemplateAttr.PageID);
}
public final void setPageID(String value) throws Exception
{
	this.SetValByKey(WindowTemplateAttr.PageID, value);
}

/** 
 信息块
*/
public WindowTemplate()
{
}
	/** 
	 信息块
	 
	 @param mypk
	 * @throws Exception 
	*/
	public WindowTemplate(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 EnMap
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_WindowTemplate");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("信息块");
		map.setEnType(EnType.Sys);

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本信息.
		map.AddTBStringPK(WindowTemplateAttr.No, null, "编号", true, true, 1, 40, 200);
		map.AddTBInt(WindowTemplateAttr.ColSpan, 1, "占的列数", true, false);
		map.SetHelperAlert(WindowTemplateAttr.ColSpan, "画布按照4列划分布局，输入的输在在1=4之间.");
		map.AddTBString(WindowTemplateAttr.Name, null, "标题", true, false, 0, 300, 20, true);
		map.AddTBString(WindowTemplateAttr.WinDocModel, null, "内容类型", true, false, 0, 300, 20, true);
		map.AddTBString(WindowTemplateAttr.Icon, null, "Icon", true, false, 0, 100, 20, true);
		map.AddTBString(WindowTemplateAttr.PageID, null, "页面ID", true, true, 0, 40, 20, false);
		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本信息.

					// map.AddDDLSysEnum(WindowTemplateAttr.ColSpan, 1, "占的列数", true, true, WindowTemplateAttr.ColSpan,
					//  "@1=1列@2=2列@3=覆盖新窗口");
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 更多的信息定义.
		map.AddTBString(WindowTemplateAttr.MoreLab, null, "更多标签", true, false, 0, 300, 20, true);
		map.AddTBString(WindowTemplateAttr.MoreUrl, null, "更多链接", true, false, 0, 300, 20, true);
		map.AddDDLSysEnum(WindowTemplateAttr.MoreLinkModel, 0, "打开方式", true, true, WindowTemplateAttr.MoreLinkModel, "@0=新窗口@1=本窗口@2=覆盖新窗口");
		map.AddTBInt(WindowTemplateAttr.PopW, 500, "Pop宽度", false, true);
		map.AddTBInt(WindowTemplateAttr.PopH, 400, "Pop高度", false, true);
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 更多的信息定义.


	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 内容定义.
					// 0=Html,   1=SQL列表, 2=折线图, 3=柱状图, 4=饼图.
					// map.AddTBInt(WindowTemplateAttr.WinDocType, 0, "内容类型", true, true);

		map.AddDDLSysEnum(WindowTemplateAttr.WinDocType, 0, "内容类型", true, true, WindowTemplateAttr.WinDocType, "@0=Html@1=系统内置@2=SQL列表@3=折线图@4=柱状图@5=饼图");

		map.AddTBStringDoc(WindowTemplateAttr.Docs, null, "内容表达式", true, false);
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 内容定义.

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限定义.
					// 0=Html , 1=SQL列表
					//  map.AddTBInt(WindowTemplateAttr.WinDocType, 0, "内容类型", false, true);
					// map.AddTBString(WindowTemplateAttr.Docs, null, "内容", true, false, 0, 4000, 20);
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 权限定义.


	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他
		map.AddTBInt(WindowTemplateAttr.Idx, 0, "默认的排序", true, false);
		map.AddBoolean(WindowTemplateAttr.IsDel, true, "用户是否可删除", true, true);
		map.AddBoolean(WindowTemplateAttr.IsEnable, false, "是否禁用?", true, true);
		map.AddTBString(WindowTemplateAttr.OrgNo, null, "OrgNo", false, false, 0, 50, 20);
		map.AddDDLSysEnum(WindowTemplateAttr.WindCtrlWay, 0, "控制方式", true, true, WindowTemplateAttr.WindCtrlWay, "@0=任何人都可以使用@1=按照设置的控制@2=Admin用户可以使用");
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他
		//region 扇形图

        map.AddTBString(WindowTemplateAttr.LabOfFZ, null, "分子标签", true, false, 0, 100, 20);
        map.AddTBStringDoc(WindowTemplateAttr.SQLOfFZ, null, "分子表达式", true, false, true);

        map.AddTBString(WindowTemplateAttr.LabOfFM, null, "分母标签", true, false, 0, 100, 20);
        map.AddTBStringDoc(WindowTemplateAttr.SQLOfFM, null, "分子表达式", true, false, true);
        map.AddTBString(WindowTemplateAttr.LabOfRate, null, "率标签", true, false, 0, 100, 20);
        //endregion 扇形图
		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setNo(DBAccess.GenerGUID());
		if (DataType.IsNullOrEmpty(this.getPageID()) == true)
		{
			this.setPageID ("Home");
		}

		return super.beforeInsert();
	}

	@Override
	protected void afterDelete() throws Exception
	{
		//删除它的实例.
		Windows ens = new Windows();
		ens.Delete(WindowAttr.WindowTemplateNo, this.getNo());

		super.afterDelete();
	}
}
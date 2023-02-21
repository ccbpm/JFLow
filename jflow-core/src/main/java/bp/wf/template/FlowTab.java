package bp.wf.template;

import bp.en.*;
import bp.sys.*;

/** 
 抄送
*/
public class FlowTab extends EntityMyPK
{

		///#region 属性
	public final String getName() throws Exception
	{
		return this.GetValStrByKey(FlowTabAttr.Name);
	}
	public final void setName(String value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.Name, value);
	}
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStrByKey(FlowTabAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.FK_Flow, value);
	}
	public final boolean isEnable() throws Exception
	{
		return this.GetValBooleanByKey(FlowTabAttr.IsEnable);
	}
	public final void setEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.IsEnable, value);
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(FlowTabAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.OrgNo, value);
	}
	public final String getTip() throws Exception
	{
		return this.GetValStrByKey(FlowTabAttr.Tip);
	}
	public final void setTip(String value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.Tip, value);
	}
	public final String getUrlExt() throws Exception
	{
		return this.GetValStrByKey(FlowTabAttr.UrlExt);
	}
	public final void setUrlExt(String value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.UrlExt, value);
	}
	public final String getMark() throws Exception
	{
		return this.GetValStrByKey(FlowTabAttr.Mark);
	}
	public final void setMark(String value)  throws Exception
	 {
		this.SetValByKey(FlowTabAttr.Mark, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 抄送设置
	*/
	public FlowTab()  {
	}
	/** 
	 抄送设置
	 
	 param mypk
	*/
	public FlowTab(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FlowTab", "流程功能");

		map.AddMyPK(true);
		map.AddTBString(FlowTabAttr.Name, null, "标签", true, true, 0, 100, 10, false);
		map.AddTBString(FlowTabAttr.FK_Flow, null, "流程编号", false, false, 0, 4, 10);
		map.AddTBString(FlowTabAttr.Mark, null, "标记", false, false, 0, 50, 10);
		map.AddTBString(FlowTabAttr.Tip, null, "Tip", false, false, 0, 200, 10);

		map.AddTBInt(FlowTabAttr.isEnable, 1, "isEnable", true, true);
		map.AddTBString(FlowTabAttr.UrlExt, null, "url链接", false, false, 0, 300, 10);
		map.AddTBString(FlowTabAttr.Icon, null, "Icon", false, false, 0, 50, 10);
		map.AddTBString(FlowTabAttr.OrgNo, null, "OrgNo", false, false, 0, 50, 10);
		map.AddTBInt(FlowTabAttr.Idx, 0, "Idx", true, true);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
		}
		else
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}
		return super.beforeInsert();
	}
}
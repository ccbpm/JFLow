package bp.wf.template;


import bp.en.*;

/** 
 流程对应组织
*/
public class FlowOrg extends EntityMM
{

		///#region 基本属性
	/** 
	流程
	*/
	public final String getFlowNo() throws Exception
	{
		return this.GetValStringByKey(FlowOrgAttr.FlowNo);
	}
	public final void setFlowNo(String value)  throws Exception
	 {
		this.SetValByKey(FlowOrgAttr.FlowNo, value);
	}
	/** 
	 组织
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(FlowOrgAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(FlowOrgAttr.OrgNo, value);
	}
	public final String getOrgNoT() throws Exception
	{
		return this.GetValRefTextByKey(FlowOrgAttr.OrgNo);
	}

		///#endregion


		///#region 构造方法
	/** 
	 流程对应组织
	*/
	public FlowOrg()  {
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FlowOrg", "流程对应组织");
		map.IndexField = FlowOrgAttr.FlowNo;

		map.AddTBStringPK(FlowOrgAttr.FlowNo,null,"流程",true,true,1,100,100);
		map.AddDDLEntitiesPK(FlowOrgAttr.OrgNo, null, "到组织", new bp.wf.port.admin2group.Orgs(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}
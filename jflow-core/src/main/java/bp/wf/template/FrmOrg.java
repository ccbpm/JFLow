package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

/** 
 表单对应组织
*/
public class FrmOrg extends EntityMM
{

		///基本属性
	/** 
	表单
	*/
	public final String getFrmID() throws Exception
	{
		return this.GetValStringByKey(FrmOrgAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		this.SetValByKey(FrmOrgAttr.FrmID,value);
	}
	/** 
	 组织
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(FrmOrgAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(FrmOrgAttr.OrgNo,value);
	}
	public final String getOrgNoT() throws Exception
	{
		return this.GetValRefTextByKey(FrmOrgAttr.OrgNo);
	}

		///


		///构造方法
	/** 
	 表单对应组织
	*/
	public FrmOrg()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FrmOrg", "表单对应组织");
		map.IndexField = FrmOrgAttr.FrmID;

		map.AddTBStringPK(FrmOrgAttr.FrmID,null,"表单",true,true,1,100,100);
		map.AddDDLEntitiesPK(FrmOrgAttr.OrgNo, null, "到组织", new bp.wf.port.admin2.Orgs(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}
package bp.wf.port.admin2group;

import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.template.*;


/**
 组织管理员
*/
public class OrgAdminer extends EntityMyPK
{

		///#region 属性
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(OrgAdminerAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)  throws Exception
	 {
		this.SetValByKey(OrgAdminerAttr.FK_Emp, value);
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(OrgAdminerAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(OrgAdminerAttr.OrgNo, value);
	}

		///#endregion


		///#region 构造方法
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
			//uac.IsDelete = false;
		   // uac.IsInsert = false;
		return uac;
	}
	/** 
	 组织管理员
	*/
	public OrgAdminer()  {
	}
	/** 
	 组织管理员
	 
	 param mypk
	*/
	public OrgAdminer(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 组织管理员
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Port_OrgAdminer", "组织管理员");
		map.AddMyPK(true);
		map.AddTBString(OrgAdminerAttr.OrgNo, null, "组织", true, false, 0, 50, 20);
		map.AddDDLEntities(OrgAdminerAttr.FK_Emp, null, "管理员", new Emps(), false);

		map.AddTBStringDoc("FlowSorts", null, "管理的流程目录", true, true, true, 10);
		map.AddTBStringDoc("FrmTrees", null, "管理的表单目录", true, true, true, 10);

		map.getAttrsOfOneVSM().AddGroupPanelModel(new OAFlowSorts(), new bp.wf.template.FlowSorts(), OAFlowSortAttr.RefOrgAdminer, OAFlowSortAttr.FlowSortNo, "流程目录权限", null, "Name", "No");

		map.getAttrsOfOneVSM().AddGroupPanelModel(new OAFrmTrees(), new bp.sys.FrmTrees(), OAFrmTreeAttr.RefOrgAdminer, OAFrmTreeAttr.FrmTreeNo, "表单目录权限", null, "Name", "No");

		map.AddHidden("OrgNo", " = ", "@WebUser.OrgNo");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		String str = "";
		bp.wf.template.FlowSorts ens = new FlowSorts();
		ens.RetrieveInSQL("SELECT FlowSortNo FROM Port_OrgAdminerFlowSort WHERE  FK_Emp='" + this.getFK_Emp() + "' AND OrgNo='" + this.getOrgNo() + "'");
		for (FlowSort item : ens.ToJavaList())
		{
			str += "(" + item.getNo() + ")" + item.getName() + ";";
		}
		this.SetValByKey("FlowSorts", str);

		str = "";
		bp.sys.FrmTrees enTrees = new bp.sys.FrmTrees();
		enTrees.RetrieveInSQL("SELECT FrmTreeNo FROM Port_OrgAdminerFrmTree WHERE  FK_Emp='" + this.getFK_Emp() + "' AND OrgNo='" + this.getOrgNo() + "'");
		for (FrmTree item : enTrees.ToJavaList())
		{
			str += "(" + item.getNo() + ")" + item.getName() + ";";
		}
		this.SetValByKey("FrmTrees", str);

		return super.beforeUpdateInsertAction();
	}
}
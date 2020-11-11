package bp.ccbill.template;
import bp.en.*;
import bp.en.Map;

/** 
 控制模型
*/
public class CtrlModelDtl extends EntityMyPK
{
	private static final long serialVersionUID = 1L;
	///基本属性.
	/** 
	 表单ID
	*/
	public final String getFrmID() throws Exception
	{
		return this.GetValStringByKey(CtrlModelDtlAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		SetValByKey(CtrlModelDtlAttr.FrmID, value);
	}

	/** 
	 控制权限
	 * @throws Exception 
	*/
	public final String getCtrlObj() throws Exception
	{
		return this.GetValStringByKey(CtrlModelDtlAttr.CtrlObj);
	}
	public final void setCtrlObj(String value) throws Exception
	{
		SetValByKey(CtrlModelDtlAttr.CtrlObj, value);
	}


	/** 
	 组织类型
	 * @throws Exception 
	*/
	public final String getOrgType() throws Exception
	{
		return this.GetValStringByKey(CtrlModelDtlAttr.OrgType);
	}
	public final void setOrgType(String value) throws Exception
	{
		SetValByKey(CtrlModelDtlAttr.OrgType, value);
	}
	public final String getIDs() throws Exception
	{
		return this.GetValStringByKey(CtrlModelDtlAttr.IDs);
	}
	public final void setIDs(String value) throws Exception
	{
		SetValByKey(CtrlModelDtlAttr.IDs, value);
	}

		/// 基本属性.


		///构造.

		/// 构造方法
	public String RptName = null;
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Frm_CtrlModelDtl", "控制模型表Dtl");


			///字段
		map.AddMyPK(); //增加一个自动增长的列.

		map.AddTBString(CtrlModelDtlAttr.FrmID, null, "表单ID", true, false, 0, 300, 100);
			//BtnNew,BtnSave,BtnSubmit,BtnDelete,BtnSearch
		map.AddTBString(CtrlModelDtlAttr.CtrlObj, null, "控制权限", true, false, 0, 20, 100);
			//Station,Dept,User
		map.AddTBString(CtrlModelDtlAttr.OrgType, null, "组织类型", true, false, 0, 300, 100);
		map.AddTBString(CtrlModelDtlAttr.IDs, null, "IDs", true, false, 0, 1000, 100);


			/// 字段.

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 控制模型
	*/
	public CtrlModelDtl()
	{
	}

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(this.getFrmID() + "_" + this.getCtrlObj() + "_" + this.getOrgType());
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFrmID() + "_" + this.getCtrlObj() + "_" + this.getOrgType());
		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		//修改CtrlModel中的数据
		CtrlModel ctrlM = new CtrlModel();
		ctrlM.setMyPK(this.getFrmID() + "_" + this.getCtrlObj());
		if (ctrlM.RetrieveFromDBSources() == 0)
		{
			ctrlM.setFrmID(this.getFrmID());
			ctrlM.setCtrlObj(this.getCtrlObj());
			if (this.getOrgType().equals("Station"))
			{
				ctrlM.setIDOfStations(this.getIDs());
			}
			if (this.getOrgType().equals("Dept"))
			{
				ctrlM.setIDOfDepts(this.getIDs());
			}
			if (this.getOrgType().equals("User"))
			{
				ctrlM.setIDOfUsers(this.getIDs());
			}
			ctrlM.Insert();

		}
		else
		{
			if (this.getOrgType().equals("Station"))
			{
				ctrlM.setIDOfStations(this.getIDs());
			}
			if (this.getOrgType().equals("Dept"))
			{
				ctrlM.setIDOfDepts(this.getIDs());
			}
			if (this.getOrgType().equals("User"))
			{
				ctrlM.setIDOfUsers(this.getIDs());
			}
			ctrlM.Update();
		}
		 super.afterInsertUpdateAction();


	}

}
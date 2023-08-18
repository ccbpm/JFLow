package bp.ta;

import bp.en.*;


/** 
  模板节点
*/
public class Node extends EntityTree
{
	///#region 属性.
	/** 
	 组织编号
	*/
	public final String getFZREmpNos()
	{
		return this.GetValStrByKey(NodeAttr.FZREmpNos);
	}
	public final void setFZREmpNos(String value)
	{
		this.SetValByKey(NodeAttr.FZREmpNos, value);
	}
	public final int getDWayFZR()
	{
		return this.GetValIntByKey(NodeAttr.DWayFZR);
	}
	public final void setDWayFZR(int value)
	{
		this.SetValByKey(NodeAttr.DWayFZR, value);
	}
	public final String getStaNos()
	{
		return this.GetValStrByKey(NodeAttr.StaNos);
	}
	public final void setStaNos(String value)
	{
		this.SetValByKey(NodeAttr.StaNos, value);
	}
	public final String getDeptNos()
	{
		return this.GetValStrByKey(NodeAttr.DeptNos);
	}
	public final void setDeptNos(String value)
	{
		this.SetValByKey(NodeAttr.DeptNos, value);
	}
	public final String getEmpNos()
	{
		return this.GetValStrByKey(NodeAttr.EmpNos);
	}
	public final void setEmpNos(String value)
	{
		this.SetValByKey(NodeAttr.EmpNos, value);
	}
	///#endregion 属性.

	///#region 构造方法
	/** 
	 模板节点
	*/
	public Node()
	{
	}
	/** 
	 模板节点
	 
	 @param _No
	*/
	public Node(String _No) throws Exception {
		super(_No);
	}
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
		///#endregion

	/** 
	 模板节点Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("TA_Node", "模板节点");

		map.AddTBStringPK(NodeAttr.No, null, "编号", false, false, 1, 100, 20);
		map.AddTBString(NodeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);

		map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 200, 30, true);
		map.AddTBString(NodeAttr.TemplateNo, null, "模板编号", true, false, 0, 200, 30, true);

		//负责人生成方式
		map.AddTBInt(NodeAttr.DWayFZR, 0, "负责人生成方式", true, true);
		map.AddTBString(NodeAttr.FZRDeptNos, null, "岗位编号", true, false, 0, 50, 10, false);
		map.AddTBString(NodeAttr.FZRStaNos, null, "岗位编号", true, false, 0, 50, 10, false);
		map.AddTBString(NodeAttr.FZREmpNos, null, "人员编号", true, false, 0, 50, 10, false);

		//协助人生成方式.
		//0=手工选择.1=按岗位2=按部门3=按人员
		map.AddTBInt(NodeAttr.DWayXieZhu, 0, "协助人员生成方式", true, true);
		map.AddTBString(NodeAttr.StaNos, null, "岗位编号", true, false, 0, 50, 10, false);
		map.AddTBString(NodeAttr.DeptNos, null, "部门编号", true, false, 0, 50, 10, false);
		map.AddTBString(NodeAttr.EmpNos, null, "人员编号", true, false, 0, 50, 10, false);

		map.AddTBInt(NodeAttr.Idx, 0, "Idx", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 生成负责人
	 
	 @return 
	*/
	public final String GenerFZR()
	{
		return this.getFZREmpNos();
	}

	/** 
	 创建的时候，给他增加一个OrgNo。
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdate() throws Exception {
		////更新流程引擎控制表.
		//string sql = "UPDATE WF_GenerWorkFlow SET Domain="" + this.Domain + "" WHERE FK_Node="" + this.No + """;
		//DBAccess.RunSQL(sql);

		//// sql = "UPDATE WF_Flow SET Domain="" + this.Domain + "" WHERE FK_Node="" + this.No + """;
		////DBAccess.RunSQL(sql);

		//if (Glo.CCBPMRunModel == CCBPMRunModel.Single)
		//    sql = "UPDATE WF_Emp SET StartFlows="" ";
		//else
		//    sql = "UPDATE WF_Emp SET StartFlows="" ";
	   // DBAccess.RunSQL(sql);
		return super.beforeUpdate();
	}
	/** 
	 删除之前的逻辑
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete() throws Exception {
		////检查是否有流程？
		//Paras ps = new Paras();
		//ps.SQL = "SELECT COUNT(*) FROM TA_Template WHERE Node=" + BP.Difference.SystemConfig.AppCenterDBVarStr + "fk_Node";
		//ps.Add("NodeNo", this.No);
		////string sql = "SELECT COUNT(*) FROM WF_Flow WHERE FK_Node="" + fk_Node + """;
		//if (DBAccess.RunSQLReturnValInt(ps) != 0)
		//    throw new Exception("err@该目录下有流程，您不能删除。");

		////检查是否有子目录？
		//ps = new Paras();
		//ps.SQL = "SELECT COUNT(*) FROM WF_Node WHERE ParentNo=" + BP.Difference.SystemConfig.AppCenterDBVarStr + "ParentNo";
		//ps.Add("ParentNo", this.No);
		////sql = "SELECT COUNT(*) FROM WF_Node WHERE ParentNo="" + fk_Node + """;
		//if (DBAccess.RunSQLReturnValInt(ps) != 0)
		//    throw new Exception("err@该目录下有子目录，您不能删除...");

		return super.beforeDelete();
	}
}

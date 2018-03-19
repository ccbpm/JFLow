package BP.WF.Template;

import BP.DA.Depositary;
import BP.En.EntityMultiTree;
import BP.En.Map;
import BP.WF.Glo;

/** 
 独立表单树
 
*/
public class FlowFormTree extends EntityMultiTree
{

	/** 
	 节点类型
	 
	*/
	private String privateNodeType;
	public final String getNodeType()
	{
		return privateNodeType;
	}
	public final void setNodeType(String value)
	{
		privateNodeType = value;
	}
	/** 
	 是否可编辑
	 
	*/
	private String privateIsEdit;
	public final String getIsEdit()
	{
		return privateIsEdit;
	}
	public final void setIsEdit(String value)
	{
		privateIsEdit = value;
	}
	/** 
	 Url
	 
	*/
	private String privateUrl;
	public final String getUrl()
	{
		return privateUrl;
	}
	public final void setUrl(String value)
	{
		privateUrl = value;
	}

		///#endregion

		
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(FrmNodeAttr.FK_Flow, value);
	}

		///#endregion 属性


		
	/** 
	 独立表单树
	 
	*/
	public FlowFormTree()
	{
	}
	/** 
	 独立表单树
	 
	 @param _No
	*/
	public FlowFormTree(String _No)
	{
		super(_No);
	}

		///#endregion

	/** 
	 分组字段
	 
	*/
	@Override
	public String getRefObjField()
	{
		return FlowFormTreeAttr.FK_Flow;
	}
	/** 
	 独立表单树Map
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FlowFormTree", "独立表单树");
		map.Java_SetCodeStruct("2");
		map.Java_SetDepositaryOfEntity(Depositary.Application);


		map.AddTBStringPK(FlowFormTreeAttr.No, null, "编号", true, true, 1, 10, 20);
		map.AddTBString(FlowFormTreeAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(FlowFormTreeAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);
		map.AddTBString(FlowFormTreeAttr.TreeNo, null, "TreeNo", false, false, 0, 100, 30);
		map.AddTBInt(FlowFormTreeAttr.Idx, 0, "Idx", false, false);
		map.AddTBInt(FlowFormTreeAttr.IsDir, 0, "是否是目录?", false, false);

			// 隶属的流程编号.
		map.AddTBString(FlowFormTreeAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
	//public void WritToGPM()
	//{
	//    if (BP.WF.Glo.OSModel == BP.Sys.OSModel.OneOne)
	//        return;

	//    string pMenuNo = "";
	//    #region 检查系统是否存在，并返回系统菜单编号
	//    string sql = "SELECT * FROM GPM_App where No='" + SystemConfig.getSysNo() + "'";

	//    DataTable dt = DBAccess.RunSQLReturnTable(sql);
	//    if (dt != null && dt.Rows.Count == 0)
	//    {
	//        //系统类别
	//        sql = "SELECT No FROM GPM_Menu WHERE ParentNo=0";
	//        string sysRootNo = DBAccess.RunSQLReturnStringIsNull(sql, "0");
	//        // 取得该功能菜单的主键编号.
	//        pMenuNo = DBAccess.GenerOID("BP.GPM.Menu").ToString();
	//        string url = Glo.HostURL;
	//        /*如果没有系统，就插入该系统菜单.*/
	//        sql = "INSERT INTO GPM_Menu(No,Name,ParentNo,IsDir,MenuType,FK_App,IsEnable,Flag)";
	//        sql += " VALUES('{0}','{1}','{2}',1,2,'{3}',1,'{4}')";
	//        sql = string.Format(sql, pMenuNo, SystemConfig.SysName, sysRootNo, SystemConfig.getSysNo(), "FlowFormTree" + SystemConfig.getSysNo());
	//        DBAccess.RunSQL(sql);
	//        /*如果没有系统，就插入该系统.*/
	//        sql = "INSERT INTO GPM_App(No,Name,AppModel,FK_AppSort,Url,RefMenuNo,MyFileName)";
	//        sql += " VALUES('{0}','{1}',0,'01','{2}','{3}','admin.gif')";

	//        sql = string.Format(sql, SystemConfig.getSysNo(), SystemConfig.SysName, url, pMenuNo);
	//        DBAccess.RunSQL(sql);
	//    }
	//    else
	//    {
	//        pMenuNo = dt.Rows[0]["RefMenuNo"].ToString();
	//    }
	//    #endregion

	//    try
	//    {
	//        sql = "SELECT * FROM GPM_Menu WHERE Flag='FlowFormTree" + this.No + "'";
	//        dt = DBAccess.RunSQLReturnTable(sql);
	//        if (dt.Rows.Count >= 1)
	//            DBAccess.RunSQL("DELETE FROM GPM_Menu WHERE Flag='FlowFormTree" + this.No + "' AND FK_App='" + SystemConfig.getSysNo() + "' ");
	//    }
	//    catch
	//    {
	//    }

	//    // 组织数据。
	//    // 获取他的ParentNo
	//    string parentNo = "";//this.ParentNo
	//    if (!string.IsNullOrEmpty(this.ParentNo))
	//    {
	//        sql = "SELECT * FROM GPM_Menu WHERE Flag='FlowFormTree" + this.ParentNo + "'";
	//        dt = DBAccess.RunSQLReturnTable(sql);
	//        if (dt.Rows.Count >= 1)
	//        {
	//            pMenuNo = dt.Rows[0]["No"].ToString();
	//        }
	//    }

	//    string menuNo = DBAccess.GenerOID("BP.GPM.Menu").ToString();
	//    // 执行插入.
	//    sql = "INSERT INTO GPM_Menu(No,Name,ParentNo,IsDir,MenuType,FK_App,IsEnable,Flag)";
	//    sql += " VALUES('{0}','{1}','{2}',{3},{4},'{5}',{6},'{7}')";
	//    sql = string.Format(sql, menuNo, this.Name, pMenuNo, 1, 3, SystemConfig.getSysNo(), 1, "FlowFormTree" + this.No);
	//    DBAccess.RunSQL(sql);
	//}
	//protected override bool beforeInsert()
	//{
	//    this.WritToGPM();
	//    return base.beforeInsert();
	//}


	//protected override bool beforeDelete()
	//{
	//    try
	//    {
	//        //删除权限管理
	//        if (BP.WF.Glo.OSModel == BP.Sys.OSModel.OneMore)
	//            DBAccess.RunSQL("DELETE FROM GPM_Menu WHERE Flag='FlowFormTree" + this.No + "' AND FK_App='" + Glo.getCCFlowAppPath() + "'");
	//    }
	//    catch
	//    {
	//    }
	//    return base.beforeDelete();
	//}
	//protected override bool beforeUpdate()
	//{
	//    //修改权限管理
	//    if (BP.WF.Glo.OSModel == BP.Sys.OSModel.OneMore)
	//    {
	//        DBAccess.RunSQL("UPDATE  GPM_Menu SET Name='" + this.Name + "' WHERE Flag='FlowFormTree" + this.No + "' AND FK_App='" + SystemConfig.getSysNo() + "'");
	//    }
	//    return base.beforeUpdate();
	//}
}
package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 累加表单方案
*/
public class FoolTruckNodeFrm extends EntityMyPK
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	节点
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FK_Node, value);
	}
	/** 
	 表单ID
	*/
	public final String getFKFrm() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Frm);
	}
	public final void setFKFrm(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FK_Frm, value);
	}
	/** 
	 对应的解决方案
	 0=默认方案.节点编号= 自定义方案, 1=不可编辑.
	*/
	public final int getFrmSln() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.FrmSln);
	}
	public final void setFrmSln(int value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmSln, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FK_Flow, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 累加表单方案
	*/
	public FoolTruckNodeFrm()  {
	}
	/** 
	 累加表单方案
	 
	 param mypk
	*/
	public FoolTruckNodeFrm(String mypk)throws Exception
	{
		super(mypk);
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

		Map map = new Map("WF_FrmNode", "累加表单方案");
		map.AddMyPK(true);

		map.AddTBInt(FrmNodeAttr.FK_Node, 0, "要作用的节点ID", false, true);
		map.AddTBString(FrmNodeAttr.FK_Frm, null, "表单ID", false, true, 1, 100, 200);

			//   map.AddDDLSysEnum(FrmNodeAttr.FrmSln, 0, "表单控制方案", true, true, FrmNodeAttr.FrmSln,
			//    "@0=默认方案@1=只读方案@2=自定义方案");

		map.AddTBString(FrmNodeAttr.FK_Flow, null, "流程编号", true, true, 1, 4, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 修改前的操作
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception {
		//表单方案如果是只读或者默认方案时，删除对应的设置的权限
		if (this.getFrmSln() == 0 || this.getFrmSln() == 1)
		{
			String sql = "";
			sql += "@DELETE FROM Sys_FrmSln WHERE FK_MapData='" + this.getFKFrm() + "' AND FK_Node='" + this.getFK_Node() + "'";
			sql += "@DELETE FROM Sys_FrmAttachment WHERE FK_MapData='" + this.getFKFrm() + "' AND FK_Node='" + this.getFK_Node() + "'";
			sql += "@DELETE FROM Sys_MapDtl WHERE FK_MapData='" + this.getFKFrm() + "' AND FK_Node='" + this.getFK_Node() + "'";
			DBAccess.RunSQLs(sql);
		}
		return super.beforeUpdate();
	}
}
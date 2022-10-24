package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.*;
import java.util.*;

/** 
 分组
*/
public class GroupField extends EntityOID
{

		///#region 权限控制
	/** 
	 权限控制.
	*/
	@Override
	public UAC getHisUAC() 
	{
		UAC uac = new UAC();
		if (bp.web.WebUser.getNo().equals("admin") == true || bp.web.WebUser.getIsAdmin())
		{
			uac.IsDelete = true;
			uac.IsInsert = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		uac.IsView = false;
		return uac;
	}

		///#endregion 权限控制


		///#region 属性
	public boolean IsUse = false;
	/** 
	 表单ID
	*/
	public final String getFrmID()  throws Exception
	{
		return this.GetValStrByKey(GroupFieldAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		this.SetValByKey(GroupFieldAttr.FrmID, value);
	}
	public final String getEnName()  throws Exception
	{
		return this.GetValStrByKey(GroupFieldAttr.FrmID);
	}
	public final void setEnName(String value) throws Exception
	{
		this.SetValByKey(GroupFieldAttr.FrmID, value);
	}
	/** 
	 标签
	*/
	public final String getLab()  throws Exception
	{
		return this.GetValStrByKey(GroupFieldAttr.Lab);
	}
	public final void setLab(String value) throws Exception
	{
		this.SetValByKey(GroupFieldAttr.Lab, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx()  throws Exception
	{
		return this.GetValIntByKey(GroupFieldAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(GroupFieldAttr.Idx, value);
	}
	/** 
	 控件类型
	*/
	public final String getCtrlType()  throws Exception
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlType);
	}
	public final void setCtrlType(String value) throws Exception
	{
		this.SetValByKey(GroupFieldAttr.CtrlType, value);
	}
	/** 
	 控件ID
	*/
	public final String getCtrlID()  throws Exception
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlID);
	}
	public final void setCtrlID(String value) throws Exception
	{
		this.SetValByKey(GroupFieldAttr.CtrlID, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 GroupField
	*/
	public GroupField()
	{
	}
	public GroupField(int oid) throws Exception {
		super(oid);
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_GroupField", "傻瓜表单分组");


			///#region 字段.
		map.AddTBIntPKOID();
		map.AddTBString(GroupFieldAttr.Lab, null, "标签", true, false, 0, 500, 20, true);
		map.AddTBString(GroupFieldAttr.FrmID, null, "表单ID", true, true, 0, 200, 20);

		map.AddTBString(GroupFieldAttr.CtrlType, null, "控件类型", true, true, 0, 50, 20);
		map.AddTBString(GroupFieldAttr.CtrlID, null, "控件ID", true, true, 0, 500, 20);

			//map.AddBoolean(GroupFieldAttr.IsZDPC, false, "是否折叠(PC)", true, true);
		map.AddBoolean(GroupFieldAttr.IsZDMobile, false, "是否折叠(Mobile)", true, true);
		map.AddDDLSysEnum(GroupFieldAttr.ShowType, 0, "分组显示模式", true, true, GroupFieldAttr.ShowType, "@0=显示@1=PC折叠@2=隐藏");

		map.AddTBInt(GroupFieldAttr.Idx, 99, "顺序号", true, false);
		map.AddTBString(MapAttrAttr.GUID, null, "GUID", true, true, 0, 128, 20, true);
		map.AddTBAtParas(3000);

			///#endregion 字段.


			///#region 方法.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "删除隶属分组的字段";
		rm.Warning = "您确定要删除该分组下的所有字段吗？";
		rm.ClassMethodName = this.toString() + ".DoDelAllField";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "调整字段顺序";
		rm.ClassMethodName = this.toString() + ".DoGroupFieldIdx";
		rm.refMethodType = RefMethodType.RightFrameOpen;
			// map.AddRefMethod(rm);

			///#endregion 方法.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 方法.
	/** 
	 外部调用的
	 
	 @return 
	*/
	public final String AddGroup()throws Exception
	{
		this.InsertAsNew();
		return "执行成功.";
	}

	/** 
	 删除所有隶属该分组的字段.
	 
	 @return 
	*/
	public final String DoDelAllField()throws Exception
	{
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getFrmID() + "' AND GroupID=" + this.getOID() + " AND KeyOfEn NOT IN ('OID','RDT','REC','RefPK','FID')";
		int i = DBAccess.RunSQL(sql);
		return "删除字段{" + i + "}个，被删除成功.";
	}
	/** 
	 分组内的字段顺序调整
	 
	 @return 
	*/
	public final String DoGroupFieldIdx()throws Exception
	{
		return "../../Admin/FoolFormDesigner/GroupFieldIdx.htm?FrmID=" + this.getFrmID() + "&GroupField=" + this.getOID();
	}
	@Override
	protected boolean beforeUpdate() throws Exception 
	{
		String sql = "UPDATE Sys_GroupField SET LAB='" + this.getLab() + "' WHERE OID=" + this.getOID();
		DBAccess.RunSQL(sql);
		return super.beforeUpdate();
	}
	public final String DoDown() throws Exception 
	{
		this.DoOrderDown(GroupFieldAttr.FrmID, this.getFrmID(), GroupFieldAttr.Idx);
		return "执行成功";
	}
	public final String DoUp() throws Exception 
	{
		this.DoOrderUp(GroupFieldAttr.FrmID, this.getFrmID(), GroupFieldAttr.Idx);
		return "执行成功";
	}
	@Override
	protected boolean beforeInsert() throws Exception 
	{

		try
		{
			String sql = "SELECT MAX(Idx) FROM " + this.getEnMap().getPhysicsTable() + " WHERE FrmID='" + this.getFrmID() + "'";
			this.setIdx(DBAccess.RunSQLReturnValInt(sql, 0) + 1);
		}
		catch (java.lang.Exception e)
		{
			this.setIdx(1);
		}
		return super.beforeInsert();
	}

		///#endregion 方法.
}
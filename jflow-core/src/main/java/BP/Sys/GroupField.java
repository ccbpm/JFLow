package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 GroupField
*/
public class GroupField extends EntityOID
{
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin") || BP.Web.WebUser.getIsAdmin())
		{
				/* */
			uac.IsDelete = true;
			uac.IsInsert = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		uac.IsView = false;
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public boolean IsUse = false;
	/** 
	 表单ID
	*/
	public final String getFrmID()
	{
		return this.GetValStrByKey(GroupFieldAttr.FrmID);
	}
	public final void setFrmID(String value)
	{
		this.SetValByKey(GroupFieldAttr.FrmID, value);
	}
	public final String getEnName()
	{
		return this.GetValStrByKey(GroupFieldAttr.FrmID);
	}
	public final void setEnName(String value)
	{
		this.SetValByKey(GroupFieldAttr.FrmID, value);
	}
	/** 
	 标签
	*/
	public final String getLab()
	{
		return this.GetValStrByKey(GroupFieldAttr.Lab);
	}
	public final void setLab(String value)
	{
		this.SetValByKey(GroupFieldAttr.Lab, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(GroupFieldAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(GroupFieldAttr.Idx, value);
	}
	/** 
	 控件类型
	*/
	public final String getCtrlType()
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlType);
	}
	public final void setCtrlType(String value)
	{
		this.SetValByKey(GroupFieldAttr.CtrlType, value);
	}
	/** 
	 控件ID
	*/
	public final String getCtrlID()
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlID);
	}
	public final void setCtrlID(String value)
	{
		this.SetValByKey(GroupFieldAttr.CtrlID, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 GroupField
	*/
	public GroupField()
	{
	}
	public GroupField(int oid)
	{
		super(oid);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_GroupField", "傻瓜表单分组");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBIntPKOID();
		map.AddTBString(GroupFieldAttr.Lab, null, "标签", true, false, 0, 500, 20, true);
		map.AddTBString(GroupFieldAttr.FrmID, null, "表单ID", true, true, 0, 200, 20);

		map.AddTBString(GroupFieldAttr.CtrlType, null, "控件类型", true, true, 0, 50, 20);
		map.AddTBString(GroupFieldAttr.CtrlID, null, "控件ID", true, true, 0, 500, 20);
		map.AddTBInt(GroupFieldAttr.Idx, 99, "顺序号", true, false);
		map.AddTBString(MapAttrAttr.GUID, null, "GUID", true, true, 0, 128, 20, true);
		map.AddTBAtParas(3000);


		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "删除隶属分组的字段";
		rm.Warning = "您确定要删除该分组下的所有字段吗？";
		rm.ClassMethodName = this.toString() + ".DoDelAllField";
		rm.RefMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "调整字段顺序";
		rm.ClassMethodName = this.toString() + ".DoGroupFieldIdx";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 外部调用的
	 
	 @return 
	*/
	public final String AddGroup()
	{
		this.InsertAsNew();
		return "执行成功.";
	}


	/** 
	 删除所有隶属该分组的字段.
	 
	 @return 
	*/
	public final String DoDelAllField()
	{
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getFrmID() + "' AND GroupID=" + this.getOID() + " AND KeyOfEn NOT IN ('OID','RDT','REC','RefPK','FID')";
		int i = BP.DA.DBAccess.RunSQL(sql);
		return "删除字段{" + i + "}个，被删除成功.";
	}
	/** 
	 分组内的字段顺序调整
	 
	 @return 
	*/
	public final String DoGroupFieldIdx()
	{
		return "../../Admin/FoolFormDesigner/GroupFieldIdx.htm?FrmID=" + this.getFrmID() + "&GroupField=" + this.getOID();
	}
	@Override
	protected boolean beforeUpdate()
	{
		String sql = "UPDATE Sys_GroupField SET LAB='" + this.getLab() + "' WHERE OID=" + this.getOID();
		BP.DA.DBAccess.RunSQL(sql);
		return super.beforeUpdate();
	}
	public final String DoDown()
	{
		this.DoOrderDown(GroupFieldAttr.FrmID, this.getFrmID(), GroupFieldAttr.Idx);
		return "执行成功";
	}
	public final String DoUp()
	{
		this.DoOrderUp(GroupFieldAttr.FrmID, this.getFrmID(), GroupFieldAttr.Idx);
		return "执行成功";
	}
	@Override
	protected boolean beforeInsert()
	{

		try
		{
			String sql = "SELECT MAX(IDX) FROM " + this.getEnMap().getPhysicsTable() + " WHERE FrmID='" + this.getFrmID() + "'";
			this.setIdx(DBAccess.RunSQLReturnValInt(sql, 0) + 1);
		}
		catch (java.lang.Exception e)
		{
			this.setIdx(1);
		}
		return super.beforeInsert();
	}
}
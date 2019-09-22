package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.GPM.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Sys.*;
import java.util.*;

/** 
 单据模版
*/
public class FrmTemplate extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.No.equals("admin"))
		{
			uac.IsDelete = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 权限控制.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 物理表
	*/
	public final String getPTable()
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (s.equals("") || s == null)
		{
			return this.No;
		}
		return s;
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 实体类型：@0=单据@1=编号名称实体@2=树结构实体
	*/
	public final EntityType getEntityType()
	{
		return EntityType.forValue(this.GetValIntByKey(FrmTemplateAttr.EntityType));
	}
	public final void setEntityType(EntityType value)
	{
		this.SetValByKey(FrmTemplateAttr.EntityType, value.getValue());
	}
	/** 
	 表单类型 (0=傻瓜，2=自由 ...)
	*/
	public final FrmType getFrmType()
	{
		return (FrmType)this.GetValIntByKey(MapDataAttr.FrmType);
	}
	public final void setFrmType(FrmType value)
	{
		this.SetValByKey(MapDataAttr.FrmType, (int)value);
	}
	/** 
	 表单树
	*/
	public final String getFK_FormTree()
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)
	{
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 新建模式 @0=表格模式@1=卡片模式@2=不可用
	*/
	public final int getBtnNewModel()
	{
		return this.GetValIntByKey(FrmTemplateAttr.BtnNewModel);
	}
	public final void setBtnNewModel(int value)
	{
		this.SetValByKey(FrmTemplateAttr.BtnNewModel, value);
	}

	/** 
	 单据格式
	*/
	public final String getBillNoFormat()
	{
		String str = this.GetValStrByKey(FrmTemplateAttr.BillNoFormat);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "{LSH4}";
		}
		return str;
	}
	public final void setBillNoFormat(String value)
	{
		this.SetValByKey(FrmTemplateAttr.BillNoFormat, value);
	}
	/** 
	 单据编号生成规则
	*/
	public final String getTitleRole()
	{
		String str = this.GetValStrByKey(FrmTemplateAttr.TitleRole);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "@WebUser.FK_DeptName @WebUser.Name @RDT";
		}
		return str;
	}
	public final void setTitleRole(String value)
	{
		this.SetValByKey(FrmTemplateAttr.BillNoFormat, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 单据模版
	*/
	public FrmTemplate()
	{
	}
	/** 
	 单据模版
	 
	 @param no 映射编号
	*/
	public FrmTemplate(String no)
	{
		super(no);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}
		Map map = new Map("Sys_MapData", "单据模版");
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, true, 1, 190, 20);
		map.SetHelperAlert(MapDataAttr.No, "也叫表单ID,系统唯一.");

		map.AddDDLSysEnum(MapDataAttr.FrmType, 0, "表单类型", true, true, "BillFrmType", "@0=傻瓜表单@1=自由表单");
		map.AddTBString(MapDataAttr.PTable, null, "存储表", true, false, 0, 500, 20, true);
		map.SetHelperAlert(MapDataAttr.PTable, "存储的表名,如果您修改一个不存在的系统将会自动创建一个表.");

		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 200, 20, true);
		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), false);

		map.AddDDLSysEnum(FrmAttr.RowOpenModel, 0, "行记录打开模式", true, true, FrmAttr.RowOpenModel, "@0=新窗口打开@1=弹出窗口打开,关闭后刷新列表@2=弹出窗口打开,关闭后不刷新列表");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 单据模版.
		map.AddDDLSysEnum(FrmTemplateAttr.EntityType, 0, "业务类型", true, false, FrmTemplateAttr.EntityType, "@0=独立表单@1=单据@2=编号名称实体@3=树结构实体");
		map.SetHelperAlert(FrmTemplateAttr.EntityType, "该实体的类型,@0=单据@1=编号名称实体@2=树结构实体.");

		map.AddDDLSysEnum(FrmAttr.EntityShowModel, 0, "展示模式", true, true, FrmAttr.EntityShowModel, "@0=表格@1=树干模式");

		map.AddTBString(FrmTemplateAttr.BillNoFormat, null, "实体编号规则", true, false, 0, 100, 20, true);
		map.SetHelperAlert(FrmTemplateAttr.BillNoFormat, "\t\n实体编号规则: \t\n 2标识:01,02,03等, 3标识:001,002,003,等..");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 单据模版.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 实体属性
		map.AddTBInt(FrmTemplateAttr.EntityEditModel, 0, "编辑模式", true, false);
			//map.AddDDLSysEnum(FrmAttr.EntityEditModel, 0, "编辑模式", true, true, FrmAttr.EntityEditModel, "@0=只读列表模式@1=Table编辑模式");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 实体属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 可以创建的权限.
			//平铺模式.
		map.AttrsOfOneVSM.AddGroupPanelModel(new StationCreates(), new BP.WF.Port.Stations(), StationCreateAttr.FrmID, StationCreateAttr.FK_Station, "可以创建的岗位", StationAttr.FK_StationType);

		map.AttrsOfOneVSM.AddGroupListModel(new StationCreates(), new BP.WF.Port.Stations(), StationCreateAttr.FrmID, StationCreateAttr.FK_Station, "可以创建的岗位AddGroupListModel", StationAttr.FK_StationType);

			//节点绑定部门. 节点绑定部门.
		map.AttrsOfOneVSM.AddBranches(new FrmDeptCreates(), new BP.Port.Depts(), FrmDeptCreateAttr.FrmID, FrmDeptCreateAttr.FK_Dept, "可以创建的部门AddBranches", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.AttrsOfOneVSM.AddBranchesAndLeaf(new EmpCreates(), new BP.Port.Emps(), EmpCreateAttr.FrmID, EmpCreateAttr.FK_Emp, "可以创建的人员", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 可以创建的权限

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}
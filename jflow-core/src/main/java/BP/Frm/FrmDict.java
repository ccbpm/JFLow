package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Sys.*;
import java.util.*;
import java.time.*;

/** 
 实体表单
*/
public class FrmDict extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
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
		return EntityType.forValue(this.GetValIntByKey(FrmDictAttr.EntityType));
	}
	public final void setEntityType(EntityType value)
	{
		this.SetValByKey(FrmDictAttr.EntityType, value.getValue());
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
		this.SetValByKey(MapDataAttr.FrmType, value.getValue());
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
		return this.GetValIntByKey(FrmDictAttr.BtnNewModel);
	}
	public final void setBtnNewModel(int value)
	{
		this.SetValByKey(FrmDictAttr.BtnNewModel, value);
	}

	/** 
	 单据格式
	*/
	public final String getBillNoFormat()
	{
		String str = this.GetValStrByKey(FrmDictAttr.BillNoFormat);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "{LSH4}";
		}
		return str;
	}
	public final void setBillNoFormat(String value)
	{
		this.SetValByKey(FrmDictAttr.BillNoFormat, value);
	}
	/** 
	 单据编号生成规则
	*/
	public final String getTitleRole()
	{
		String str = this.GetValStrByKey(FrmDictAttr.TitleRole);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "@WebUser.getFK_Dept()Name @WebUser.getName() @RDT";
		}
		return str;
	}
	public final void setTitleRole(String value)
	{
		this.SetValByKey(FrmDictAttr.BillNoFormat, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 实体表单
	*/
	public FrmDict()
	{
	}
	/** 
	 实体表单
	 
	 @param no 映射编号
	*/
	public FrmDict(String no)
	{
		super(no);
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
		Map map = new Map("Sys_MapData", "实体表单");
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 外观.
		map.AddDDLSysEnum(FrmAttr.RowOpenModel, 0, "行记录打开模式", true, true, "RowOpenMode", "@0=新窗口打开@1=在本窗口打开@2=弹出窗口打开,关闭后不刷新列表@3=弹出窗口打开,关闭后刷新列表");
		map.AddTBInt(FrmAttr.PopHeight, 500, "弹窗高度", true, false);
		map.AddTBInt(FrmAttr.PopWidth, 760, "弹窗宽度", true, false);

		map.AddDDLSysEnum(MapDataAttr.TableCol, 0, "表单显示列数", true, true, "傻瓜表单显示方式", "@0=4列@1=6列@2=上下模式3列");

		map.AddDDLSysEnum(FrmAttr.EntityEditModel, 0, "编辑模式", true, true, FrmAttr.EntityEditModel, "@0=表格@1=行编辑");
		map.SetHelperAlert(FrmAttr.EntityEditModel,"用什么方式打开实体列表进行编辑0=只读查询模式SearchDict.htm,1=行编辑模式SearchEditer.htm");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 外观.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 实体表单.
		map.AddDDLSysEnum(FrmDictAttr.EntityType, 0, "业务类型", true, false, FrmDictAttr.EntityType, "@0=独立表单@1=单据@2=编号名称实体@3=树结构实体");
		map.SetHelperAlert(FrmDictAttr.EntityType, "该实体的类型,@0=单据@1=编号名称实体@2=树结构实体.");

		map.AddTBString(FrmDictAttr.BillNoFormat, null, "实体编号规则", true, false, 0, 100, 20, true);
		map.SetHelperAlert(FrmDictAttr.BillNoFormat, "\t\n实体编号规则: \t\n 2标识:01,02,03等, 3标识:001,002,003,等..");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 实体表单.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region MyBill - 按钮权限.
		map.AddTBString(FrmDictAttr.BtnNewLable, "新建", "新建", true, false, 0, 50, 20);
		map.AddDDLSysEnum(FrmDictAttr.BtnNewModel, 0, "新建模式", true, true, FrmDictAttr.BtnNewModel, "@0=表格模式@1=卡片模式@2=不可用");

		map.AddTBString(FrmDictAttr.BtnSaveLable, "保存", "保存", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnSaveEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnSaveAndCloseLable, "保存并关闭", "保存并关闭", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnSaveAndCloseEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnDelLable, "删除", "删除", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnDelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnSearchLabel, "列表", "列表", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnSearchEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnGroupLabel, "分析", "分析", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnGroupEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnPrintHtml, "打印Html", "打印Html", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnPrintHtmlEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnPrintPDF, "打印PDF", "打印PDF", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnPrintPDFEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnPrintRTF, "打印RTF", "打印RTF", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnPrintRTFEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnPrintCCWord, "打印CCWord", "打印CCWord", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnPrintCCWordEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnExpZip, "导出zip文件", "导出zip文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnExpZipEnable, false, "是否可用？", true, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 按钮权限.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询按钮权限.
		map.AddTBString(FrmDictAttr.BtnImpExcel, "导入Excel文件", "导入Excel文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnImpExcelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnExpExcel, "导出Excel文件", "导出Excel文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnExpExcelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnGroupLabel, "分析", "分析", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnGroupEnable, true, "是否可用？", true, true);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 查询按钮权限.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapDataAttr.Note, null, "备注", true, false, true);
		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", false, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 设计者信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 扩展参数.
		map.AddTBAtParas(3000); //参数属性.
		map.AddTBString(FrmDictAttr.Tag0, null, "Tag0", false, false, 0, 500, 20);
		map.AddTBString(FrmDictAttr.Tag1, null, "Tag1", false, false, 0, 4000, 20);
		map.AddTBString(FrmDictAttr.Tag2, null, "Tag2", false, false, 0, 500, 20);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 扩展参数.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本功能.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设计表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDesigner";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		  //  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "单据url的API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoAPI";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开数据(表格)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoOpenBillDict";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开数据(行编辑)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoOpenBillEditer";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "绑定到菜单目录"; // "设计表单";
		rm.getHisAttrs().AddDDLSQL("MENUNo", null, "选择菜单目录", "SELECT No,Name FROM GPM_Menu WHERE MenuType=3");
		rm.getHisAttrs().AddTBString("Name", "@Name", "菜单名称", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoBindMenu";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.Func;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoEvent";
		rm.Icon = "../../WF/Img/Event.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行方法"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoMethod";
		rm.Icon = "../../WF/Img/Event.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 权限规则.
		rm = new RefMethod();
		rm.Title = "创建规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoCreateRole";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "编辑规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSaveRole";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDeleteRole";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "查询权限"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSearchRole";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 报表定义.
		rm = new RefMethod();
		rm.GroupName = "报表定义";
		rm.Title = "设置显示的列"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoRpt_ColsChose";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "报表定义";
		rm.Title = "列的顺序"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoRpt_ColsIdxAndLabel";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
			//   map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "报表定义";
		rm.Title = "查询条件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoRpt_SearchCond";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 报表定义.

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 检查enittyNoName类型的实体
	*/
	public final void CheckEnityTypeAttrsFor_EntityNoName()
	{
		//取出来全部的属性.
		MapAttrs attrs = new MapAttrs(this.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 补充上流程字段到 NDxxxRpt.
		if (attrs.Contains(this.No + "_" + GERptAttr.OID) == false)
		{
			/* WorkID */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.KeyOfEn = "OID";
			attr.Name = "主键ID";
			attr.MyDataType = BP.DA.DataType.AppInt;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.DefVal = "0";
			attr.HisEditType = BP.En.EditType.Readonly;
			attr.Insert();
		}
		if (attrs.Contains(this.No + "_" + GERptAttr.BillNo) == false)
		{
			/* 单据编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.BillNo;
			attr.Name = "编号"; //  单据编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 100;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(this.No + "_" + GERptAttr.Title) == false)
		{
			/* 名称 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.Title; // "FlowEmps";
			attr.Name = "名称"; //   单据模式， ccform的模式.
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 400;
			attr.Idx = -90;
			attr.Insert();
		}
		if (attrs.Contains(this.No + "_BillState") == false)
		{
			/* 单据状态 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "BillState"; // "FlowEmps";
			attr.Name = "单据状态";
			attr.MyDataType = DataType.AppInt;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 10;
			attr.Idx = -98;
			attr.Insert();
		}

		if (attrs.Contains(this.No + "_Starter") == false)
		{
			/* 发起人 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "Starter";
			attr.Name = "创建人";
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;

			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.MinLen = 0;
			attr.MaxLen = 32;
			attr.Idx = -1;
			attr.Insert();
		}
		if (attrs.Contains(this.No + "_StarterName") == false)
		{
			/* 创建人名称 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "StarterName";
			attr.Name = "创建人名称";
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;

			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.MinLen = 0;
			attr.MaxLen = 32;
			attr.Idx = -1;
			attr.Insert();
		}


		if (attrs.Contains(this.No + "_" + GERptAttr.AtPara) == false)
		{
			/* 参数 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.AtPara;
			attr.Name = "参数"; // 单据编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 4000;
			attr.Idx = -99;
			attr.Insert();
		}

		if (attrs.Contains(this.No + "_RDT") == false)
		{
			/* MyNum */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = this.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "RDT"; // "FlowStartRDT";
			attr.Name = "创建时间";
			attr.MyDataType = DataType.AppDateTime;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.Idx = -97;
			attr.Insert();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 补充上流程字段。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 注册到外键表.
		SFTable sf = new SFTable();
		sf.No = this.No;
		if (sf.RetrieveFromDBSources() == 0)
		{
			sf.Name = this.Name;
			sf.SrcType = SrcType.SQL;
			sf.SrcTable = this.getPTable();
			sf.ColumnValue = "BillNo";
			sf.ColumnText = "Title";
			sf.SelectStatement = "SELECT BillNo AS No, Title as Name FROM " + this.getPTable();
			sf.Insert();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 注册到外键表
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 报表定义
	/** 
	 选择显示的列
	 
	 @return 
	*/
	public final String DoRpt_ColsChose()
	{
		return "../../CCBill/Admin/ColsChose.htm?FrmID=" + this.No;
	}
	/** 
	 列的顺序
	 
	 @return 
	*/
	public final String DoRpt_ColsIdxAndLabel()
	{
		return "../../CCBill/Admin/ColsIdxAndLabel.htm?FrmID=" + this.No;
	}
	/** 
	 查询条件
	 
	 @return 
	*/
	public final String DoRpt_SearchCond()
	{
		return "../../CCBill/Admin/SearchCond.htm?FrmID=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 报表定义.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	public final String DoSaveRole()
	{
		return "../../CCBill/Admin/CreateRole.htm?s=34&FrmID=" " + this.getNo()+ " "&ExtType=PageLoadFull&RefNo=";
	}
	public final String DoCreateRole()
	{
		return "../../CCBill/Admin/CreateRole.htm?s=34&FrmID=" " + this.getNo()+ " "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 查询权限
	 
	 @return 
	*/
	public final String DoSearchRole()
	{
		return "../../CCBill/Admin/SearchRole.htm?s=34&FrmID=" " + this.getNo()+ " "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 删除规则.
	 
	 @return 
	*/
	public final String DoDeleteRole()
	{
		return "../../CCBill/Admin/DeleteRole.htm?s=34&FrmID=" " + this.getNo()+ " "&ExtType=PageLoadFull&RefNo=";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 权限控制.

	public final String DoMethod()
	{
		return "../../CCBill/Admin/Method.htm?s=34&FrmID=" " + this.getNo()+ " "&ExtType=PageLoadFull&RefNo=";
	}
	public final String DoPageLoadFull()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData=" " + this.getNo()+ " "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	*/
	public final String DoEvent()
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData=" " + this.getNo()+ " "&T=sd&FK_Node=0";
	}
	/** 
	 绑定菜单树
	 
	 @return 返回执行结果.
	*/
	public final String DoBindMenu(String menumDirNo, String menuName)
	{
		String sql = "SELECT FK_App FROM GPM_Menu WHERE No='" + menumDirNo + "'";
		String app = DBAccess.RunSQLReturnString(sql);

		String guid = DBAccess.GenerGUID();

		String url = "../WF/CCBill/Search.htm?FrmID=" + this.No;
		sql = "INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, Url, OpenWay,Icon,MenuCtrlWay) VALUES ('" + guid + "', '" + menuName + "', '" + menumDirNo + "', 1, 4, '" + app + "', '" + url + "',  0,'',1)";
		DBAccess.RunSQL(sql);
		return "加入成功,如何<a href='En.htm?EnName=BP.GPM.Menu&No=" + guid + "'>控制权限请转GPM.</a>";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 业务逻辑.
	public final String CreateBlankWorkID()
	{
		return String.valueOf(BP.Frm.Dev2Interface.CreateBlankDictID(this.No, WebUser.getNo(), null));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 业务逻辑.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法操作.
	/** 
	 打开单据
	 
	 @return 
	*/
	public final String DoOpenBillDict()
	{
		return "../../CCBill/SearchDict.htm?FrmID=" " + this.getNo()+ " "&t=" + LocalDateTime.now().toString("yyyyMMddHHmmssffffff");
	}
	public final String DoOpenBillEditer()
	{
		return "../../CCBill/SearchEditer.htm?FrmID=" " + this.getNo()+ " "&t=" + LocalDateTime.now().toString("yyyyMMddHHmmssffffff");
	}
	public final String DoAPI()
	{
		return "../../Admin/FoolFormDesigner/Bill/API.htm?FrmID=" " + this.getNo()+ " "&t=" + LocalDateTime.now().toString("yyyyMMddHHmmssffffff");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法操作.

}
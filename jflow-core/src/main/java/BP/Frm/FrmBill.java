package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Web.WebUser;
import BP.Sys.*;
import java.util.*;
import java.time.*;

/** 
 单据属性
*/
public class FrmBill extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	@Override
	public UAC getHisUAC() throws Exception
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
			return this.getNo();
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
		return EntityType.forValue(this.GetValIntByKey(FrmBillAttr.EntityType));
	}
	public final void setEntityType(EntityType value)
	{
		this.SetValByKey(FrmBillAttr.EntityType, value.getValue());
	}
	/** 
	 表单类型 (0=傻瓜，2=自由 ...)
	*/
	public final FrmType getFrmType()
	{
		return FrmType.forValue( this.GetValIntByKey(MapDataAttr.FrmType));
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
	 单据格式
	*/
	public final String getBillNoFormat()
	{
		String str = this.GetValStrByKey(FrmBillAttr.BillNoFormat);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "{LSH4}";
		}
		return str;
	}
	public final void setBillNoFormat(String value)
	{
		this.SetValByKey(FrmBillAttr.BillNoFormat, value);
	}
	/** 
	 单据编号生成规则
	*/
	public final String getTitleRole()
	{
		String str = this.GetValStrByKey(FrmBillAttr.TitleRole);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "@WebUser.getFK_DeptName @WebUser.getName() @RDT";
		}
		return str;
	}
	public final void setTitleRole(String value)
	{
		this.SetValByKey(FrmBillAttr.BillNoFormat, value);
	}

	 public final String getSortColumns()
	 {
		 return this.GetValStrByKey(FrmBillAttr.SortColumns);
	 }
	 public final void setSortColumns(String value)
	 {
		 this.SetValByKey(FrmBillAttr.SortColumns, value);
	 }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 单据属性
	*/
	public FrmBill()
	{
	}
	/** 
	 单据属性
	 
	 @param no 映射编号
	*/
	public FrmBill(String no)
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
		
		Map map = new Map("Sys_MapData", "单据属性");
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, true, 1, 190, 20);
		map.AddDDLSysEnum(MapDataAttr.FrmType, 0, "表单类型", true, true, "BillFrmType", "@0=傻瓜表单@1=自由表单");
			//map.AddDDLSysEnum(MapDataAttr.FrmModel, 0, "单据模板", true, true, "BillFrmModel", "@0=系统预置@1=用户新增");
		map.AddTBString(MapDataAttr.PTable, null, "存储表", true, false, 0, 500, 20, true);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20, true);
		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);


		map.AddDDLSysEnum(MapDataAttr.TableCol, 0, "表单显示列数", true, true, "傻瓜表单显示方式", "@0=4列@1=6列@2=上下模式3列");

		map.AddDDLSysEnum(FrmAttr.RowOpenModel, 0, "行记录打开模式", true, true, "RowOpenMode", "@0=新窗口打开@1=在本窗口打开@2=弹出窗口打开,关闭后不刷新列表@3=弹出窗口打开,关闭后刷新列表");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 单据属性.
			//map.AddDDLSysEnum(FrmBillAttr.FrmBillWorkModel, 0, "工作模式", true, false, FrmBillAttr.FrmBillWorkModel,
			//    "@0=独立表单@1=单据工作模式");

		map.AddDDLSysEnum(FrmBillAttr.EntityType, 0, "业务类型", true, false, FrmBillAttr.EntityType, "@0=独立表单@1=单据@2=编号名称实体@3=树结构实体");
		map.SetHelperAlert(FrmBillAttr.EntityType, "该实体的类型,@0=单据@1=编号名称实体@2=树结构实体.");

			//map.AddDDLSysEnum(MapDataAttr.FrmType, 0, "表单类型", true, true, "", "@0=独立表单@1=单据工作模式@2=流程工作模式");

		map.AddTBString(FrmBillAttr.BillNoFormat, null, "单号规则", true, false, 0, 100, 20, true);
		map.AddTBString(FrmBillAttr.TitleRole, null, "标题生成规则", true, false, 0, 100, 20, true);
		map.AddTBString(FrmBillAttr.SortColumns, null, "排序字段", true, false, 0, 100, 20, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 单据属性.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 按钮权限.
		map.AddTBString(FrmBillAttr.BtnNewLable, "新建", "新建", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnNewModel, true, "新建模式？", true, true);

		map.AddTBString(FrmBillAttr.BtnSaveLable, "保存", "保存", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnSaveEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnDelLable, "删除", "删除", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnDelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnSearchLabel, "列表", "列表", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnSearchEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnGroupLabel, "分析", "分析", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnGroupEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnPrintHtml, "打印Html", "打印Html", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnPrintHtmlEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnPrintPDF, "打印PDF", "打印PDF", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnPrintPDFEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnPrintRTF, "打印RTF", "打印RTF", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnPrintRTFEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnPrintCCWord, "打印CCWord", "打印CCWord", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnPrintCCWordEnable, false, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnExpZip, "导出zip文件", "导出zip文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnExpZipEnable, false, "是否可用？", true, true);


		map.AddTBString(FrmBillAttr.BtnRefBill, "关联单据", "关联单据", true, false, 0, 50, 20);

		map.AddDDLSysEnum(FrmAttr.RefBillRole, 0, "关联单据工作模式", true, true, "RefBillRole", "@0=不启用@1=非必须选择关联单据@2=必须选择关联单据");

		map.AddTBString(FrmBillAttr.RefBill, null, "关联单据ID", true, false, 0, 100, 20, true);
		map.SetHelperAlert(FrmBillAttr.RefBill, "请输入单据编号,多个单据编号用逗号分开.\t\n比如:Bill_Sale,Bill_QingJia");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 按钮权限.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询按钮权限.
		map.AddTBString(FrmBillAttr.BtnImpExcel, "导入Excel文件", "导入Excel文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnImpExcelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnExpExcel, "导出Excel文件", "导出Excel文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnExpExcelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmBillAttr.BtnGroupLabel, "分析", "分析", true, false, 0, 50, 20);
		map.AddBoolean(FrmBillAttr.BtnGroupEnable, true, "是否可用？", true, true);

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
		map.AddTBString(FrmDictAttr.Tag0, null, "Tag0", false, false, 0, 500, 20);
		map.AddTBString(FrmDictAttr.Tag1, null, "Tag1", false, false, 0, 4000, 20);
		map.AddTBString(FrmDictAttr.Tag2, null, "Tag2", false, false, 0, 500, 20);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 扩展参数.


		map.AddTBAtParas(800); //参数属性.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本功能.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设计表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDesigner";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "单据url的API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoAPI";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开单据数据"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoOpenBill";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "绑定到菜单目录"; // "设计表单";
		rm.getHisAttrs().AddDDLSQL("MENUNo", null, "选择菜单目录", "SELECT No,Name FROM GPM_Menu WHERE MenuType=3",true);
		rm.getHisAttrs().AddTBString("Name", "@Name", "菜单名称", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoBindMenu";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.Func;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "装载填充"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoPageLoadFull";
		rm.Icon = "../../WF/Img/FullData.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoEvent";
		rm.Icon = "../../WF/Img/Event.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行方法"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoMethod";
		rm.Icon = "../../WF/Img/Event.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
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
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDeleteRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "查询权限"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSearchRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
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
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "报表定义";
		rm.Title = "列的顺序"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoRpt_ColsIdxAndLabel";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
			//   map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "报表定义";
		rm.Title = "查询条件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoRpt_SearchCond";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 报表定义.

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	/** 
	 创建权限
	 
	 @return 
	*/
	public final String DoCreateRole()
	{
		return "../../CCBill/Admin/CreateRole.htm?s=34&FrmID= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 查询权限
	 
	 @return 
	*/
	public final String DoSearchRole()
	{
		return "../../CCBill/Admin/SearchRole.htm?s=34&FrmID= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 删除规则.
	 
	 @return 
	*/
	public final String DoDeleteRole()
	{
		return "../../CCBill/Admin/DeleteRole.htm?s=34&FrmID= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 权限控制.


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

	public final String DoMethod()
	{
		return "../../CCBill/Admin/Method.htm?s=34&FrmID= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	public final String DoPageLoadFull()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	*/
	public final String DoEvent()
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData= " + this.getNo()+ " &T=sd&FK_Node=0";
	}
	/** 
	 设计表单
	 
	 @return 
	*/
	public final String DoDesigner()
	{
		if (this.getFrmType() == Sys.FrmType.FreeFrm)
		{
			return "";
		}
		return "";
	}
	/** 
	 检查检查实体类型
	*/
	public final void CheckEnityTypeAttrsFor_Bill()
	{
		//取出来全部的属性.
		MapAttrs attrs = new MapAttrs(this.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 补充上流程字段到 NDxxxRpt.
		if (attrs.Contains(this.getNo() + "_" + GERptAttr.Title) == false)
		{
			/* 标题 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.Title; // "FlowEmps";
			attr.setName("标题"; //   单据模式， ccform的模式.
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 400;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(this.getNo() + "_" + GERptAttr.OID) == false)
		{
			/* WorkID */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.KeyOfEn = "OID";
			attr.setName("主键ID";
			attr.MyDataType = BP.DA.DataType.AppInt;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.DefVal = "0";
			attr.HisEditType = BP.En.EditType.Readonly;
			attr.Insert();
		}
		if (attrs.Contains(this.getNo() + "_" + GERptAttr.BillNo) == false)
		{
			/* 单据编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType( EditType.UnDel);
			attr.KeyOfEn = GERptAttr.BillNo;

			attr.setName("单据编号"); //  单据编号
			attr.setMyDataType( DataType.AppString);
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

		if (attrs.Contains(this.getNo() + "_" + GERptAttr.AtPara) == false)
		{
			/* 参数 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.AtPara;
			attr.setName("参数"; // 单据编号
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

		if (attrs.Contains(this.No + "_BillState") == false)
		{
			/* 单据状态 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "BillState"; // "FlowEmps";
			attr.setName("单据状态";
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
			attr.setFK_MapData(this.getNo());
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "Starter";
			attr.setName("创建人";
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
			attr.setFK_MapData(this.getNo());
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "StarterName";
			attr.setName("创建人名称";
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

		if (attrs.Contains(this.No + "_RDT") == false)
		{
			/* MyNum */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "RDT"; // "FlowStartRDT";
			attr.setName("创建时间";
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
		return String.valueOf(BP.Frm.Dev2Interface.CreateBlankBillID(this.No, WebUser.getNo(), null));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 业务逻辑.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法操作.
	/** 
	 打开单据
	 
	 @return 
	*/
	public final String DoOpenBill()
	{
		return "../../CCBill/Search.htm?FrmID= " + this.getNo()+ " &t=" + LocalDateTime.now().toString("yyyyMMddHHmmssffffff");
	}
	public final String DoAPI()
	{
		return "../../Admin/FoolFormDesigner/Bill/API.htm?FrmID= " + this.getNo()+ " &t=" + LocalDateTime.now().toString("yyyyMMddHHmmssffffff");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法操作.

}
package bp.ccbill;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.ccbill.template.*;
import java.util.*;
import java.time.*;

/** 
 实体表单
*/
public class FrmDict extends EntityNoName
{

		///权限控制.
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin") == true)
		{
			uac.IsDelete = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		return uac;
	}

		/// 权限控制.


		///属性
	/** 
	 物理表
	 * @throws Exception 
	*/
	public final String getPTable() throws Exception
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (DataType.IsNullOrEmpty(s)==true)
		{
			return this.getNo();
		}
		return s;
	}
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 实体类型：@0=单据@1=编号名称实体@2=树结构实体
	 * @throws Exception 
	*/
	public final EntityType getEntityType() throws Exception
	{
		return EntityType.forValue(this.GetValIntByKey(FrmDictAttr.EntityType));
	}
	public final void setEntityType(EntityType value) throws Exception
	{
		this.SetValByKey(FrmDictAttr.EntityType, value.getValue());
	}
	/** 
	 表单类型 (0=傻瓜，2=自由 ...)
	 * @throws Exception 
	*/
	public final FrmType getFrmType() throws Exception
	{
		return FrmType.forValue( this.GetValIntByKey(MapDataAttr.FrmType));
	}
	public final void setFrmType(FrmType value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FrmType, value.getValue());
	}
	/** 
	 表单树
	 * @throws Exception 
	*/
	public final String getFK_FormTree() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 新建模式 @0=表格模式@1=卡片模式@2=不可用
	 * @throws Exception 
	*/
	public final int getBtnNewModel() throws Exception
	{
		return this.GetValIntByKey(FrmDictAttr.BtnNewModel);
	}
	public final void setBtnNewModel(int value) throws Exception
	{
		this.SetValByKey(FrmDictAttr.BtnNewModel, value);
	}

	/** 
	 单据格式
	 * @throws Exception 
	*/
	public final String getBillNoFormat() throws Exception
	{
		String str = this.GetValStrByKey(FrmDictAttr.BillNoFormat);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "{LSH4}";
		}
		return str;
	}
	public final void setBillNoFormat(String value) throws Exception
	{
		this.SetValByKey(FrmDictAttr.BillNoFormat, value);
	}
	/** 
	 单据编号生成规则
	 * @throws Exception 
	*/
	public final String getTitleRole() throws Exception
	{
		String str = this.GetValStrByKey(FrmDictAttr.TitleRole);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "@WebUser.FK_DeptName @WebUser.Name @RDT";
		}
		return str;
	}
	public final void setTitleRole(String value) throws Exception
	{
		this.SetValByKey(FrmDictAttr.BillNoFormat, value);
	}

		///


		///构造方法
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
	public FrmDict(String no) throws Exception
	{
		super(no);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("Sys_MapData", "实体表单");

		map.setCodeStruct("4");


			///基本属性.
		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, true, 1, 190, 20);
		map.SetHelperAlert(MapDataAttr.No, "也叫表单ID,系统唯一.");

		map.AddDDLSysEnum(MapDataAttr.FrmType, 0, "表单类型", true, true, "BillFrmType", "@0=傻瓜表单@1=自由表单@8=开发者表单");
		map.AddTBString(MapDataAttr.PTable, null, "存储表", true, false, 0, 500, 20, true);
		map.SetHelperAlert(MapDataAttr.PTable, "存储的表名,如果您修改一个不存在的系统将会自动创建一个表.");

		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 200, 20, true);
		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), false);

			/// 基本属性.


			///外观.
		map.AddDDLSysEnum(FrmAttr.RowOpenModel, 0, "行记录打开模式", true, true, "RowOpenMode", "@0=新窗口打开@1=在本窗口打开@2=弹出窗口打开,关闭后不刷新列表@3=弹出窗口打开,关闭后刷新列表");
		map.AddTBInt(FrmAttr.PopHeight, 500, "弹窗高度", true, false);
		map.AddTBInt(FrmAttr.PopWidth, 760, "弹窗宽度", true, false);

		map.AddDDLSysEnum(MapDataAttr.TableCol, 0, "表单显示列数", true, true, "傻瓜表单显示方式", "@0=4列@1=6列@2=上下模式3列");

		map.AddDDLSysEnum(FrmAttr.EntityEditModel, 0, "编辑模式", true, true, FrmAttr.EntityEditModel, "@0=表格@1=行编辑");
		map.SetHelperAlert(FrmAttr.EntityEditModel,"用什么方式打开实体列表进行编辑0=只读查询模式SearchDict.htm,1=行编辑模式SearchEditer.htm");

			/// 外观.


			///实体表单.
		map.AddDDLSysEnum(FrmDictAttr.EntityType, 0, "业务类型", true, false, FrmDictAttr.EntityType, "@0=独立表单@1=单据@2=编号名称实体@3=树结构实体");
		map.SetHelperAlert(FrmDictAttr.EntityType, "该实体的类型,@0=单据@1=编号名称实体@2=树结构实体.");

		map.AddTBString(FrmDictAttr.BillNoFormat, null, "实体编号规则", true, false, 0, 100, 20, true);
		map.SetHelperAlert(FrmDictAttr.BillNoFormat, "\t\n实体编号规则: \t\n 2标识:01,02,03等, 3标识:001,002,003,等..");
		map.AddTBString(FrmBillAttr.SortColumns, null, "排序字段", true, false, 0, 100, 20, true);
		map.AddTBString(FrmBillAttr.ColorSet, null, "颜色设置", true, false, 0, 100, 20, true);
		map.AddTBString(FrmBillAttr.FieldSet, null, "字段求和求平均设置", true, false, 0, 100, 20, true);


			/// 实体表单.


			///MyBill - 按钮权限.
		map.AddTBString(FrmDictAttr.BtnNewLable, "新建", "新建", true, false, 0, 50, 20);
		map.AddDDLSysEnum(FrmDictAttr.BtnNewModel, 0, "新建模式", true, true, FrmDictAttr.BtnNewModel, "@0=表格模式@1=卡片模式@2=不可用",true);


		map.AddTBString(FrmDictAttr.BtnSaveLable, "保存", "保存", true, false, 0, 50, 20);
			//map.AddBoolean(FrmDictAttr.BtnSaveEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnSubmitLable, "提交", "提交", true, false, 0, 50, 20);
			//map.AddBoolean(FrmDictAttr.BtnSubmitEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnDelLable, "删除", "删除", true, false, 0, 50, 20);
		   // map.AddBoolean(FrmDictAttr.BtnDelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnSearchLabel, "列表", "列表", true, false, 0, 50, 20);
			//map.AddBoolean(FrmDictAttr.BtnSearchEnable, true, "是否可用？", true, true);

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

			/// 按钮权限.


			///查询按钮权限.
		map.AddTBString(FrmDictAttr.BtnImpExcel, "导入Excel文件", "导入Excel文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnImpExcelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnExpExcel, "导出Excel文件", "导出Excel文件", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnExpExcelEnable, true, "是否可用？", true, true);

		map.AddTBString(FrmDictAttr.BtnGroupLabel, "分析", "分析", true, false, 0, 50, 20);
		map.AddBoolean(FrmDictAttr.BtnGroupEnable, true, "是否可用？", true, true);


			/// 查询按钮权限.


			///设计者信息.
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapDataAttr.Note, null, "备注", true, false, true);
		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", false, false);

			/// 设计者信息.


			///扩展参数.
		map.AddTBAtParas(3000); //参数属性.
		map.AddTBString(FrmDictAttr.Tag0, null, "Tag0", false, false, 0, 500, 20);
		map.AddTBString(FrmDictAttr.Tag1, null, "Tag1", false, false, 0, 4000, 20);
		map.AddTBString(FrmDictAttr.Tag2, null, "Tag2", false, false, 0, 500, 20);

			/// 扩展参数.


			///基本功能.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设计表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDesigner";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		  //  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "单据url的API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoAPI";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开数据(表格)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoOpenBillDict";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开数据(行编辑)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoOpenBillEditer";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "绑定到菜单目录"; // "设计表单";
		rm.getHisAttrs().AddDDLSQL("MENUNo", null, "选择菜单目录", "SELECT No,Name FROM GPM_Menu WHERE MenuType=3");
		rm.getHisAttrs().AddTBString("Name", "@Name", "菜单名称", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoBindMenu";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.Func;
		rm.Target = "_blank";
			//rm.GroupName = "开发接口";
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

			/// 基本功能.


			///权限规则.
		rm = new RefMethod();
		rm.Title = "创建规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoCreateRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
		rm.RefAttrKey = FrmDictAttr.BtnNewLable;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "保存规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSaveRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
		rm.RefAttrKey = FrmDictAttr.BtnSaveLable;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "提交规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSubmitRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
		rm.RefAttrKey = FrmDictAttr.BtnSubmitLable;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除规则"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoDeleteRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
		rm.RefAttrKey = FrmDictAttr.BtnDelLable;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "查询权限"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSearchRole";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
		rm.RefAttrKey = FrmDictAttr.BtnSearchLabel;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "数据查询权限规则";
		rm.ClassMethodName = this.toString() + ".DoSearchDataRole()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "权限规则";
		map.AddRefMethod(rm);



			///


			///报表定义.
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

			/// 报表定义.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	protected final void InsertCtrlModel() throws Exception
	{
		//保存权限表
		CtrlModel ctrl = new CtrlModel();
		ctrl.setFrmID(this.getNo());
		ctrl.setCtrlObj("BtnNew");
		ctrl.setMyPK(ctrl.getFrmID() + "_" + ctrl.getCtrlObj());
		ctrl.setIsEnableAll(true);
		if (ctrl.RetrieveFromDBSources() == 0)
		{
			ctrl.Insert();
		}



		ctrl = new CtrlModel();
		ctrl.setFrmID(this.getNo());
		ctrl.setCtrlObj("BtnSave");
		ctrl.setIsEnableAll(true);
		ctrl.setMyPK(ctrl.getFrmID() + "_" + ctrl.getCtrlObj());
		if (ctrl.RetrieveFromDBSources() == 0)
		{
			ctrl.Insert();
		}

		ctrl = new CtrlModel();
		ctrl.setFrmID(this.getNo());
		ctrl.setCtrlObj("BtnSubmit");
		ctrl.setIsEnableAll(true);
		ctrl.setMyPK(ctrl.getFrmID() + "_" + ctrl.getCtrlObj());
		if (ctrl.RetrieveFromDBSources() == 0)
		{
			ctrl.Insert();
		}

		ctrl = new CtrlModel();
		ctrl.setFrmID(this.getNo());
		ctrl.setCtrlObj("BtnDelete");
		ctrl.setIsEnableAll(true);
		ctrl.setMyPK(ctrl.getFrmID() + "_" + ctrl.getCtrlObj());
		if (ctrl.RetrieveFromDBSources() == 0)
		{
			ctrl.Insert();
		}

		ctrl = new CtrlModel();
		ctrl.setFrmID(this.getNo());
		ctrl.setCtrlObj("BtnSearch");
		ctrl.setIsEnableAll(true);
		ctrl.setMyPK(ctrl.getFrmID() + "_" + ctrl.getCtrlObj());
		if (ctrl.RetrieveFromDBSources() == 0)
		{
			ctrl.Insert();
		}

	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		InsertCtrlModel();
		CheckEnityTypeAttrsFor_EntityNoName();

		super.afterInsertUpdateAction() ;
	}

	/** 
	 检查enittyNoName类型的实体
	 * @throws Exception 
	*/
	public final void CheckEnityTypeAttrsFor_EntityNoName() throws Exception
	{
		//取出来全部的属性.
		MapAttrs attrs = new MapAttrs(this.getNo());


			///补充上流程字段到 NDxxxRpt.
		if (attrs.Contains(this.getNo()+ "_" + GERptAttr.OID) == false)
		{
			/* WorkID */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("主键ID");
			attr.setMyDataType(bp.da.DataType.AppInt);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType( EditType.Readonly); 
			attr.Insert();
		}
		if (attrs.Contains(this.getNo()+ "_" + GERptAttr.BillNo) == false)
		{
			/* 单据编号 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn( GERptAttr.BillNo);
			attr.setName("编号"); //  单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen( 0);
			attr.setMaxLen( 100);
			attr.setIdx( -100);
			attr.Insert();
		}

		if (attrs.Contains(this.getNo()+ "_" + GERptAttr.Title) == false)
		{
			/* 名称 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(  GERptAttr.Title); // "FlowEmps";
			attr.setName("名称"); //   单据模式， ccform的模式.
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( true);
			attr.setUIIsEnable(true);
			attr.setUIIsLine(true);
			attr.setMinLen( 0);
			attr.setMaxLen( 400);
			attr.setIdx( -90);
			attr.Insert();
		}
		if (attrs.Contains(this.getNo()+ "_BillState") == false)
		{
			/* 单据状态 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("BillState"); // "FlowEmps";
			attr.setName("单据状态");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen( 0);
			attr.setMaxLen( 10);
			attr.setIdx( -98);
			attr.Insert();
		}

		if (attrs.Contains(this.getNo()+ "_Starter") == false)
		{
			/* 发起人 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("Starter");
			attr.setName("创建人");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);

			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setMinLen( 0);
			attr.setMaxLen( 32);
			attr.setIdx( -1);
			attr.Insert();
		}
		if (attrs.Contains(this.getNo()+ "_StarterName") == false)
		{
			/* 创建人名称 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType( EditType.UnDel);
			attr.setKeyOfEn("StarterName");
			attr.setName("创建人名称");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);

			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setMinLen( 0);
			attr.setMaxLen( 32);
			attr.setIdx( -1);
			attr.Insert();
		}


		if (attrs.Contains(this.getNo()+ "_" + GERptAttr.AtPara) == false)
		{
			/* 参数 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType( EditType.UnDel);
			attr.setKeyOfEn( GERptAttr.AtPara);
			attr.setName("参数"); // 单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine( false);
			attr.setMinLen(0);
			attr.setMaxLen( 4000);
			attr.setIdx( -99);
			attr.Insert();
		}

		if (attrs.Contains(this.getNo()+ "_RDT") == false)
		{
			/* MyNum */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("RDT"); // "FlowStartRDT";
			attr.setName("创建时间");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx( -97);
			attr.Insert();
		}
		if (attrs.Contains(this.getNo()+ "_FK_Dept") == false)
		{
			/* 创建人部门 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("FK_Dept");
			attr.setName("创建人部门");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);

			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen( 32);
			attr.setIdx( -1);
			attr.Insert();
		}
		if (attrs.Contains(this.getNo()+ "OrgNo") == false)
		{
			/* 创建人名称 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("OrgNo");
			attr.setName("创建人所在的组织");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);

			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setMinLen( 0);
			attr.setMaxLen( 32);
			attr.setIdx( -1);
			attr.Insert();
		}

			/// 补充上流程字段。


			///注册到外键表.
		SFTable sf = new SFTable();
		sf.setNo(this.getNo());
		if (sf.RetrieveFromDBSources() == 0)
		{
			sf.setName( this.getName());
			sf.setSrcType(SrcType.SQL);
			sf.setSrcTable(this.getPTable());
			sf.setColumnValue("BillNo");
			sf.setColumnText("Title");
			sf.setSelectStatement("SELECT BillNo AS No, Title as Name FROM " + this.getPTable());
			sf.Insert();
		}


			/// 注册到外键表
	}


		///报表定义
	/** 
	 选择显示的列
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRpt_ColsChose() throws Exception
	{
		return "../../CCBill/Admin/ColsChose.htm?FrmID=" + this.getNo();
	}
	/** 
	 列的顺序
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRpt_ColsIdxAndLabel() throws Exception
	{
		return "../../CCBill/Admin/ColsIdxAndLabel.htm?FrmID=" + this.getNo();
	}
	/** 
	 查询条件
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRpt_SearchCond() throws Exception
	{
		return "../../CCBill/Admin/SearchCond.htm?FrmID=" + this.getNo();
	}

		/// 报表定义.


		///权限控制.
	/** 
	 保存权限规则
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSaveRole() throws Exception
	{
		return "../../CCBill/Admin/BillRole.htm?s=34&FrmID=" + this.getNo()+ "&CtrlObj=BtnSave";
	}
	/** 
	 提交权限规则
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSubmitRole() throws Exception
	{
		return "../../CCBill/Admin/BillRole.htm?s=34&FrmID=" + this.getNo()+ "&CtrlObj=BtnSubmit";
	}

	/** 
	 新增权限规则
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoCreateRole() throws Exception
	{
		return "../../CCBill/Admin/BillRole.htm?s=34&FrmID=" + this.getNo()+ "&CtrlObj=BtnNew";
	}
	/** 
	 删除权限规则
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDeleteRole() throws Exception
	{
		return "../../CCBill/Admin/BillRole.htm?s=34&FrmID=" + this.getNo()+ "&CtrlObj=BtnDelete";
	}

	/** 
	 查询权限
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearchRole() throws Exception
	{
		return "../../CCBill/Admin/BillRole.htm?s=34&FrmID=" + this.getNo()+ "&CtrlObj=BtnSearch";
	}


	/** 
	 数据查询权限规则
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearchDataRole() throws Exception
	{
		return "../../CCBill/Admin/SearchDataRole.htm?s=34&FrmID=" + this.getNo();
	}


		/// 权限控制.

	public final String DoMethod() throws Exception
	{
		return "../../CCBill/Admin/Method.htm?s=34&FrmID=" + this.getNo()+ "&ExtType=PageLoadFull&RefNo=";
	}
	public final String DoPageLoadFull() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData=" + this.getNo()+ "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoEvent() throws Exception
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData=" + this.getNo()+ "&T=sd&FK_Node=0";
	}
	/** 
	 绑定菜单树
	 
	 @return 返回执行结果.
	 * @throws Exception 
	*/
	public final String DoBindMenu(String menumDirNo, String menuName) throws Exception
	{
		String sql = "SELECT FK_App FROM GPM_Menu WHERE No='" + menumDirNo + "'";
		String app = DBAccess.RunSQLReturnString(sql);

		String guid = DBAccess.GenerGUID();

		String url = "../WF/CCBill/Search.htm?FrmID=" + this.getNo();
		sql = "INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, Url, OpenWay,Icon,MenuCtrlWay) VALUES ('" + guid + "', '" + menuName + "', '" + menumDirNo + "', 1, 4, '" + app + "', '" + url + "',  0,'',1)";
		DBAccess.RunSQL(sql);
		return "加入成功,如何<a href='En.htm?EnName=bp.gpm.Menu&No=" + guid + "'>控制权限请转GPM.</a>";
	}


		///业务逻辑.
	public final String CreateBlankWorkID() throws Exception
	{
		return String.valueOf(bp.ccbill.Dev2Interface.CreateBlankDictID(this.getNo(), WebUser.getNo(), null));
	}

		/// 业务逻辑.


		///方法操作.
	/** 
	 打开单据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoOpenBillDict() throws Exception
	{
		return "../../CCBill/SearchDict.htm?FrmID=" + this.getNo()+ "&t=" + DataType.getCurrentDateByFormart("yyyyMMddHHmmssffffff");
	}
	public final String DoOpenBillEditer() throws Exception
	{
		return "../../CCBill/SearchEditer.htm?FrmID=" + this.getNo()+ "&t=" + DataType.getCurrentDateByFormart("yyyyMMddHHmmssffffff");
	}
	public final String DoAPI() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Bill/API.htm?FrmID=" + this.getNo()+ "&t=" + DataType.getCurrentDateByFormart("yyyyMMddHHmmssffffff");
	}

		/// 方法操作.

}
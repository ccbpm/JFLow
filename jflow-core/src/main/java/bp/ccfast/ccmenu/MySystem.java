package bp.ccfast.ccmenu;

import bp.ccbill.template.Method;
import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.ccbill.template.*;
import bp.sys.CCFormAPI;
import bp.wf.*;
import bp.wf.template.FlowTabAttr;

import java.io.*;

/** 
 系统
*/
public class MySystem extends EntityNoName
{

		///#region 属性
	/** 
	 打开方式
	*/
	public final String getOpenWay() {
		int openWay = 0;

		switch (openWay)
		{
			case 0:
				return "_blank";
			case 1:
				return this.getNo();
			default:
				return "";
		}
	}
	/** 
	 路径
	*/
	public final String getWebPath()
	{
		return this.GetValStringByKey("WebPath");
	}
	/** 
	 是否启用
	*/
	public final boolean isEnable()
	{
		return this.GetValBooleanByKey(MySystemAttr.IsEnable);
	}
	public final void setEnable(boolean value)
	 {
		this.SetValByKey(MySystemAttr.IsEnable, value);
	}
	/** 
	 顺序
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(MySystemAttr.Idx);
	}
	public final void setIdx(int value)
	 {
		this.SetValByKey(MySystemAttr.Idx, value);
	}
	/** 
	 Icon
	*/
	public final String getIcon()
	{
		return this.GetValStrByKey(MySystemAttr.Icon);
	}
	public final void setIcon(String value)
	 {
		this.SetValByKey(MySystemAttr.Icon, value);
	}


	public final String getOrgNo()
	{
		return this.GetValStringByKey(MySystemAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	 {
		this.SetValByKey(MySystemAttr.OrgNo, value);
	}
	public final String getRefMenuNo()
	{
		return this.GetValStringByKey(MySystemAttr.RefMenuNo);
	}
	public final void setRefMenuNo(String value)
	 {
		this.SetValByKey(MySystemAttr.RefMenuNo, value);
	}

		///#endregion


		///#region 按钮权限控制
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}

		///#endregion


		///#region 构造方法
	/** 
	 系统
	*/
	public MySystem(){
	}
	/** 
	 系统
	 
	 param no
	*/
	public MySystem(String no)
	{
		this.setNo(no);
		try {
			this.Retrieve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_System", "系统");
		map.setDepositaryOfEntity( Depositary.None);

		map.AddTBStringPK(MySystemAttr.No, null, "编号", true, false, 2, 100, 100);
		map.AddTBString(MySystemAttr.Name, null, "名称", true, false, 0, 300, 150, true);
		map.AddBoolean(MySystemAttr.IsEnable, true, "启用?", true, true);
		map.AddTBString(MySystemAttr.Icon, null, "图标", true, false, 0, 50, 150, true);

		map.AddTBString(MenuAttr.OrgNo, null, "组织编号", true, false, 0, 50, 20);
		map.AddTBInt(MySystemAttr.Idx, 0, "显示顺序", true, false);

		RefMethod rm = new RefMethod();
		rm.Title = "导出应用模板";
		rm.ClassMethodName = this.toString() + ".DoExpAppModel";
			//rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 导出
	 
	 @return 
	*/
	public final String DoExp() throws Exception {
		String path = bp.difference.SystemConfig.getPathOfWebApp() + "CCFast/SystemTemplete/" + this.getName() + "/";
		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		//系统属性.
		DataSet ds = new DataSet();
		ds.Tables.add(this.ToDataTableField("MySystem"));

		//模块.
		Modules ens = new Modules();
		ens.Retrieve(ModuleAttr.SystemNo, this.getNo(), null);
		ds.Tables.add(ens.ToDataTableField("Modules"));

		//菜单.
		Menus menus = new Menus();
		menus.Retrieve(MenuAttr.SystemNo, this.getNo(), null);
		ds.Tables.add(menus.ToDataTableField("Menus"));

		String file = path + "Menus.xml"; //默认的页面.
		ds.WriteXml(file,XmlWriteMode.IgnoreSchema,ds);

		//遍历菜单.
		for (Menu en : menus.ToJavaList())
		{
			////常规的功能，不需要备份.
			//if (en.Mark.Equals("WorkRec") == true
			//    || en.Mark.Equals("Calendar") == true
			//    || en.Mark.Equals("Notepad") == true)
			//    continue;
			switch (en.getMenuModel())
			{
				case "WorkRec":
				case "Calendar":
				case "Notepad":
				case "Task":
				case "KnowledgeManagement":
					break;
				case "Dict": //如果是实体.
					Dict(en, path);
					break;
				case "DictTable": //如果是字典.
					DictTable(en, path);
				case "StandAloneFlow":
					StandAloneFlow(en,path);
					break;
				default:
					//    throw new Exception("err@没有判断的应用类型:" + en.Mark);
					break;
			}
		}

		return "执行成功. 导出到：" + path;
	}
	public final String DictTable(Menu en, String path) throws Exception {
		DataSet ds = new DataSet();

		SFTable sf = new SFTable(en.getUrlExt());

		ds.Tables.add(sf.ToDataTableField("SFTable"));

		DataTable dt = sf.GenerHisDataTable(null);
		dt.TableName = "Data";
		ds.Tables.add(dt);

		ds.WriteXml(path + en.getUrlExt() + ".xml",XmlWriteMode.IgnoreSchema,ds);
		return "";
	}
	/** 
	 导出字典.
	 
	 @return 
	*/
	public final String Dict(Menu en, String path) throws Exception {
		//获得表单的ID.
		String frmID = en.getUrlExt();

		DataSet ds = CCFormAPI.GenerHisDataSet_AllEleInfo(frmID);
		String file = path + "\\" + frmID + ".xml"; //实体方法.
		ds.WriteXml(file,XmlWriteMode.WriteSchema, ds);


			///#region 导出实体的方法 .
		//获得方法分组
		GroupMethods ensGroup = new GroupMethods();
		ensGroup.Retrieve(MethodAttr.FrmID, frmID, null);

		//获得方法.
		Methods ens = new Methods();
		ens.Retrieve(MethodAttr.FrmID, frmID, null);

		//保存方法.
		ds = new DataSet();
		ds.Tables.add(ensGroup.ToDataTableField("GroupMethods"));
		ds.Tables.add(ens.ToDataTableField("Methods"));

		file = path + frmID + "_GroupMethods.xml"; //实体方法.
		ds.WriteXml(file,XmlWriteMode.IgnoreSchema,ds);

		//循环单实体方法集合.
		for (Method method : ens.ToJavaList())
		{
			switch (method.getMethodModel())
			{
				case "FlowEtc": //流程
					Flow f2l1 = new Flow(method.getMethodID());
					f2l1.DoExpFlowXmlTemplete(path + method.getMethodID() + "_Flow");
					break;
				case "FlowBaseData": //流程
					Flow fl1 = new Flow(method.getMethodID());
					fl1.DoExpFlowXmlTemplete(path + method.getMethodID() + "_Flow");
					break;
				case "Func": //功能导出？
					break;
				default:
					break;
			}
		}

			///#endregion 导出实体的方法 .


			///#region 导出集合 .
		//获得方法分组
		bp.ccbill.template.Collections ensCollts = new bp.ccbill.template.Collections();
		ensCollts.Retrieve(CollectionAttr.FrmID, frmID, null);

		//保存方法.
		ds = new DataSet();
		ds.Tables.add(ensCollts.ToDataTableField("Collections"));

		file = path + "/" + frmID + "_Collections.xml"; //实体方法.
		ds.WriteXml(file,XmlWriteMode.WriteSchema, ds);

		//循环单实体方法集合.
		for (Collection method : ensCollts.ToJavaList())
		{
			switch (method.getMethodModel())
			{
				case "FlowEntityBatchStart": //流程
					Flow fC1 = new Flow(method.getFlowNo());
					fC1.DoExpFlowXmlTemplete(path + method.getFlowNo() + "_Flow");
					break;
				case "FlowNewEntity": //流程
					Flow fc2 = new Flow(method.getFlowNo());
					fc2.DoExpFlowXmlTemplete(path + method.getFlowNo() + "_Flow");
					break;
				default:
					break;
			}
		}

			///#endregion 导出实体的方法 .

		return "实体导出成功";
	}
	public final String StandAloneFlow(Menu en, String path) throws Exception {
		String file;
		String newMark = en.getTag1()+"_"+en.getMark();
		Flow f1 = new Flow(en.getTag1());

		f1.DoExpFlowXmlTemplete(path +newMark + "_Flow");

		//获得功能页
		bp.wf.template.FlowTabs flowTabs = new bp.wf.template.FlowTabs();
		flowTabs.Retrieve(FlowTabAttr.FK_Flow,en.getTag1());
		DataSet ds=new DataSet();
		ds.Tables.add(flowTabs.ToDataTableField("FlowTab"));

		file = path + "/" +newMark + "_FlowTab.xml"; //实体方法.
		ds.WriteXml(file,XmlWriteMode.WriteSchema, ds);


		return "独立运行流程导出成功";
	}
	/** 
	 导出应用模板
	 
	 @return 
	*/
	public final String DoExpAppModel()  {
		return "../../GPM/PowerCenter.htm?CtrlObj=System&CtrlPKVal=" + this.getNo() + "&CtrlGroup=System";
	}

	/** 
	 业务处理.
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			this.setNo(DBAccess.GenerGUID(10, null, null));
		}

		this.setOrgNo(bp.web.WebUser.getOrgNo());
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeDelete() throws Exception {
		Modules ens = new Modules();
		ens.Retrieve(ModuleAttr.SystemNo, this.getNo(), null);
		if (ens.size() != 0)
		{
			throw new RuntimeException("err@该系统下有子模块，您不能删除。");
		}

		//看看这个类别下是否有表单，如果有就删除掉.
		String sql = "SELECT COUNT(No) AS No FROM Sys_MapData WHERE FK_FormTree='" + this.getNo() + "'";
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			DBAccess.RunSQL("DELETE FROM Sys_FormTree WHERE No='" + this.getNo() + "' ");
		}

		//看看这个类别下是否有流程，如果有就删除掉.
		sql = "SELECT COUNT(No) AS No FROM WF_Flow WHERE FK_FlowSort='" + this.getNo() + "'";
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			DBAccess.RunSQL("DELETE FROM WF_FlowSort WHERE No='" + this.getNo() + "' ");
		}

		return super.beforeDelete();
	}


		///#region 移动方法.
	/** 
	 向上移动
	*/
	public final void DoUp() throws Exception {
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.DoOrderUp(MySystemAttr.OrgNo, this.getOrgNo(), MySystemAttr.Idx);
		}
		else
		{
			this.DoOrderUp(MySystemAttr.Idx);
		}
	}
	/** 
	 向下移动
	*/
	public final void DoDown()  {
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.DoOrderDown(MySystemAttr.OrgNo, this.getOrgNo(), MySystemAttr.Idx);
		}
		else
		{
			this.DoOrderDown(MySystemAttr.Idx);
		}
	}

		///#endregion 移动方法.

}
package BP.WF.Rpt;

import BP.DA.Depositary;
import BP.En.EntityNoName;
import BP.En.FieldTypeS;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.En.UIContralType;
import BP.Port.DeptAttr;
import BP.Port.Depts;
import BP.Port.Emps;
import BP.Port.Stations;
import BP.Sys.DTSearchWay;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.WF.Flow;
import BP.WF.Data.GERptAttr;

/**
 * 报表定义
 * 
 */
public class RptDfine extends EntityNoName {

	/// #region 属性
	/**
	 * 本部门流程查询权限定义
	 * 
	 */
	public final int getMyDeptRole() {
		return this.GetValIntByKey(RptDfineAttr.MyDeptRole);
	}

	public final void setMyDeptRole(int value) {
		this.SetValByKey(RptDfineAttr.MyDeptRole, value);
	}

	/// #endregion

	/// #region 构造方法
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		if (BP.Web.WebUser.getIsAdmin()) {
			uac.IsUpdate = true;
			uac.IsDelete = false;
			uac.IsView = true;
			uac.IsInsert = false;
		} else {
			uac.IsView = false;
		}
		return uac;
	}

	/**
	 * 报表定义
	 * 
	 */
	public RptDfine() {
	}

	/**
	 * 报表定义
	 * 
	 * @param no
	 *            映射编号
	 * @throws Exception 
	 */
	public RptDfine(String no) throws Exception {
		this.setNo(no);
		this.Retrieve();
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

		Map map = new Map("WF_Flow", "报表定义");

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("4");
		;


			///#region 基本属性.
		map.AddTBStringPK(RptDfineAttr.No, null, "编号", true, false, 1, 200, 20);
		map.AddTBString(RptDfineAttr.Name, null, "流程名称", true, false, 0, 500, 20);

		map.AddDDLSysEnum(RptDfineAttr.MyDeptRole, 0, "本部门发起的流程", true, true,
				RptDfineAttr.MyDeptRole,
				"@0=仅部门领导可以查看@1=部门下所有的人都可以查看@2=本部门里指定岗位的人可以查看", true);

			//map.AddTBString(RptDfineAttr.PTable, null, "物理表", true, false, 0, 500, 20);
			//map.AddTBString(RptDfineAttr.Note, null, "备注", true, false, 0, 500, 20);

			///#endregion 基本属性.


			///#region 绑定的关联关系.
		map.getAttrsOfOneVSM().Add(new RptStations(), new Stations(), RptStationAttr.FK_Rpt, RptStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "岗位权限");
		map.getAttrsOfOneVSM().Add(new RptDepts(), new Depts(), RptDeptAttr.FK_Rpt, RptDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "部门权限");
		map.getAttrsOfOneVSM().Add(new RptEmps(), new Emps(), RptEmpAttr.FK_Rpt, RptEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "人员权限");

			///#endregion


			///#region 我发起的流程.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyStartFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyStartFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyStartFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_MyStartFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyStartFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?如果确定，以前配置将清空。";
		rm.ClassMethodName = this.toString() + ".DoReset_MyStartFlow()";
		rm.refMethodType = RefMethodType.Func;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

			///#endregion 我发起的流程.


			///#region 我参与的流程.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我参与的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我参与的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我参与的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我参与的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我参与的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_MyJoinFlow()";
		rm.refMethodType = RefMethodType.Func;
		rm.GroupName = "我参与的流程";
		map.AddRefMethod(rm);

			///#endregion 我发起的流程.


			///#region 我部门发起的流程.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyDeptFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyDeptFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyDeptFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_MyDeptFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyDeptFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_MyDeptFlow()";
		rm.refMethodType = RefMethodType.Func;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

			///#endregion 我部门发起的流程.


			///#region 高级查询.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_AdminerFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_AdminerFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_AdminerFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_AdminerFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_AdminerFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_AdminerFlow()";
		rm.refMethodType = RefMethodType.Func;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

			///#endregion 高级查询.

		this.set_enMap(map);
		return this.get_enMap();
	}

	/// #endregion

	/// #region 通用方法.
	/**
	 * 选择的列
	 * 
	 * @param rptMark
	 * @return
	 */
	public final String DoColsChose(String rptMark) {
		return "../../Admin/RptDfine/S2ColsChose.htm?FK_Flow=" + this.getNo() + "&RptNo=ND"
				+ Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}

	/**
	 * 列的次序
	 * 
	 * @param rptMark
	 * @return
	 */
	public final String DoColsOrder(String rptMark) {
		return "../../Admin/RptDfine/S3ColsLabel.htm?FK_Flow=" + this.getNo() + "&RptNo=ND"
				+ Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}

	/**
	 * 查询条件设置
	 * 
	 * @param rptMark
	 * @return
	 */
	public final String DoSearchCond(String rptMark) {
		return "../../Admin/RptDfine/S5SearchCond.htm?FK_Flow=" + this.getNo() + "&RptNo=ND"
				+ Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}

	/**
	 * 导出模版设置
	 * 
	 * @param rptMark
	 * @return
	 */
	public final String DoRptExportTemplate(String rptMark) {
		return "../../Admin/RptDfine/S8_RptExportTemplate.htm?FK_Flow=" + this.getNo() + "&RptNo=ND"
				+ Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}

	/** 
	 重置设置.
	 * @throws Exception 
	 
	*/
	public final String DoReset(String rptMark, String rptName) throws Exception
	{
		MapData md = new MapData();
		md.setNo("ND" + Integer.parseInt(this.getNo()) + "Rpt" + rptMark);
		if (md.RetrieveFromDBSources() == 0)
		{
			md.setName(rptName);
			md.Insert();
		}

		md.setRptIsSearchKey(true); //按关键查询.
		md.setRptDTSearchWay(DTSearchWay.None); //按日期查询.
		md.setRptDTSearchKey("");

		//设置查询条件.
//		switch (rptMark)
//ORIGINAL LINE: case "My":
		if (rptMark.equals("My") || rptMark.equals("MyJoin") || rptMark.equals("MyDept"))
		{
				md.setRptSearchKeys("*WFSta*FK_NY*"); //查询条件.
		}
//ORIGINAL LINE: case "Adminer":
		else if (rptMark.equals("Adminer"))
		{
				md.setRptSearchKeys("*WFSta*FK_NY*"); //查询条件.
		}
		else
		{
		}

		Flow fl = new Flow(this.getNo());
		md.setPTable(fl.getPTable());
		md.Update();

		String keys = ",OID,FK_Dept,FlowStarter,WFState,Title,FlowStarter,FlowStartRDT,FlowEmps,FlowDaySpan,FlowEnder,FlowEnderRDT,FK_NY,FlowEndNode,WFSta,";

		//查询出来所有的字段.
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(this.getNo()) + "Rpt");
		attrs.Delete(MapAttrAttr.FK_MapData, md.getNo()); // 删除已经有的字段。
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (keys.contains("," + attr.getKeyOfEn() + ",") == false)
			{
				continue;
			}
			attr.setFK_MapData(md.getNo());
			attr.setUIIsEnable(false);

			///#region 判断特殊的字段.
			if (attr.getKeyOfEn()==GERptAttr.FK_Dept){
				attr.setUIBindKey("BP.Port.Depts");
				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.FK);
				attr.setUIVisible(true);
				attr.setDefVal("");
				attr.setMaxLen(100);
				attr.Update();
				}
			else if(attr.getKeyOfEn()==GERptAttr.FK_NY){
				attr.setUIBindKey("BP.Pub.NYs");
				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.FK);
				attr.setUIVisible(true);
				attr.setUIIsEnable(false);
				//attr.GroupID = groupID;
				attr.Update();
			}else if(attr.getKeyOfEn()==GERptAttr.Title){
				attr.setUIWidth(120);
				}
			else if (attr.getKeyOfEn()==GERptAttr.FlowStarter){
				attr.setUIIsEnable(false);
				//attr.setLGType(FieldTypeS.FK;
				//attr.setUIBindKey("BP.Port.Emps";
				//attr.setUIContralType(UIContralType.DDL;
				//attr.setUIWidth(120;
				//break;
			}
			else if(attr.getKeyOfEn()==GERptAttr.FlowEndNode){
				//attr.setLGType(FieldTypeS.FK;
				//attr.setUIBindKey("BP.WF.Template.NodeExts";
				//attr.setUIContralType(UIContralType.DDL;
				//break;
			}
			else if("FK_Emp".equals(attr.getKeyOfEn())){
				//break;
			}
			else{
				//break;
			}
			///#endregion

			attr.Insert();
		}
		return"标记为: "+rptMark+"的报表，重置成功...";

	}

	/// #endregion

	/// #region 我发起的流程
	/** 
	 设置选择的列
	 
	 @return 
	*/
	public final String DoColsChoseOf_MyStartFlow()
	{
		return this.DoColsChose("My");
	}

	/**
	 * 列的次序
	 * 
	 * @return
	 */
	public final String DoColsOrder_MyStartFlow() {
		return DoColsOrder("My");
	}

	/**
	 * 查询条件
	 * 
	 * @return
	 */
	public final String DoSearchCond_MyStartFlow() {
		return DoSearchCond("My");
	}

	/**
	 * 导出模版.
	 * 
	 * @return
	 */
	public final String DoRptExportTemplate_MyStartFlow() {
		return DoRptExportTemplate("My");
	}

	/**
	 * 重置
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DoReset_MyStartFlow() throws Exception {
		return DoReset("My", "我发起的流程");
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public final String DoSearch_MyStartFlow() {
		return "../../RptDfine/FlowSearch.htm?SearchType=My&FK_Flow=" + this.getNo();
	}

	/// #endregion

	/// #region 我参与的流程
	/**
	 * 设置选择的列
	 * 
	 * @return
	 */
	public final String DoColsChoseOf_MyJoinFlow() {
		return this.DoColsChose("MyJoin");
	}

	/**
	 * 列的次序
	 * 
	 * @return
	 */
	public final String DoColsOrder_MyJoinFlow() {
		return DoColsOrder("MyJoin");
	}

	/**
	 * 查询条件
	 * 
	 * @return
	 */
	public final String DoSearchCond_MyJoinFlow() {
		return DoSearchCond("MyJoin");
	}

	/**
	 * 导出模版.
	 * 
	 * @return
	 */
	public final String DoRptExportTemplate_MyJoinFlow() {
		return DoRptExportTemplate("MyJoin");
	}

	/**
	 * 重置
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DoReset_MyJoinFlow() throws Exception {
		return DoReset("MyJoin", "我参与的流程");
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public final String DoSearch_MyJoinFlow() {
		return "../../RptDfine/FlowSearch.htm?SearchType=MyJoin&FK_Flow=" + this.getNo();
	}

	/// #endregion 我参与的流程

	/// #region 本部门发起的流程
	/**
	 * 设置选择的列
	 * 
	 * @return
	 */
	public final String DoColsChoseOf_MyDeptFlow() {
		return this.DoColsChose("MyDept");
	}

	/**
	 * 列的次序
	 * 
	 * @return
	 */
	public final String DoColsOrder_MyDeptFlow() {
		return DoColsOrder("MyDept");
	}

	/**
	 * 查询条件
	 * 
	 * @return
	 */
	public final String DoSearchCond_MyDeptFlow() {
		return DoSearchCond("MyDept");
	}

	/**
	 * 导出模版.
	 * 
	 * @return
	 */
	public final String DoRptExportTemplate_MyDeptFlow() {
		return DoRptExportTemplate("MyDept");
	}

	/**
	 * 重置
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DoReset_MyDeptFlow() throws Exception {
		return DoReset("MyDept", "本部门发起的流程");
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public final String DoSearch_MyDeptFlow() {
		return "../../RptDfine/FlowSearch.htm?SearchType=MyDept&FK_Flow=" + this.getNo();
	}

	/// #endregion 本部门发起的流程

	/// #region 高级查询
	/**
	 * 设置选择的列
	 * 
	 * @return
	 */
	public final String DoColsChoseOf_AdminerFlow() {
		return this.DoColsChose("Adminer");
	}

	/**
	 * 列的次序
	 * 
	 * @return
	 */
	public final String DoColsOrder_AdminerFlow() {
		return DoColsOrder("Adminer");
	}

	/**
	 * 查询条件
	 * 
	 * @return
	 */
	public final String DoSearchCond_AdminerFlow() {
		return DoSearchCond("Adminer");
	}

	/**
	 * 导出模版.
	 * 
	 * @return
	 */
	public final String DoRptExportTemplate_AdminerFlow() {
		return DoRptExportTemplate("Adminer");
	}

	/**
	 * 重置
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DoReset_AdminerFlow() throws Exception {
		return DoReset("Adminer", "本部门发起的流程");
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public final String DoSearch_AdminerFlow() {
		return "../../RptDfine/FlowSearch.htm?SearchType=Adminer&FK_Flow=" + this.getNo();
	}

	/// #endregion 高级查询

}
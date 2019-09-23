package BP.WF.Rpt;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;

/** 
 报表定义
*/
public class RptDfine extends EntityNoName
{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 本部门流程查询权限定义
	*/
	public final int getMyDeptRole()
	{
		return this.GetValIntByKey(RptDfineAttr.MyDeptRole);
	}
	public final void setMyDeptRole(int value)
	{
		this.SetValByKey(RptDfineAttr.MyDeptRole, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (WebUser.getIsAdmin())
		{
			uac.IsUpdate = true;
			uac.IsDelete = false;
			uac.IsView = true;
			uac.IsInsert = false;
		}
		else
		{
			uac.IsView = false;
		}
		return uac;
	}
	/** 
	 报表定义
	*/
	public RptDfine()
	{
	}
	/** 
	 报表定义
	 
	 @param no 映射编号
	*/
	public RptDfine(String no)
	{
		this.No = no;
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBStringPK(RptDfineAttr.No, null, "编号", true, false, 1, 200, 20);
		map.AddTBString(RptDfineAttr.Name, null, "流程名称", true, false, 0, 500, 20);

		map.AddDDLSysEnum(RptDfineAttr.MyDeptRole, 0, "本部门发起的流程", true, true, RptDfineAttr.MyDeptRole, "@0=仅部门领导可以查看@1=部门下所有的人都可以查看@2=本部门里指定岗位的人可以查看", true);

			//map.AddTBString(RptDfineAttr.PTable, null, "物理表", true, false, 0, 500, 20);
			//map.AddTBString(RptDfineAttr.Note, null, "备注", true, false, 0, 500, 20);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 绑定的关联关系.
		map.getAttrsOfOneVSM().Add(new RptStations(), new Stations(), RptStationAttr.FK_Rpt, RptStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "岗位权限");
		map.getAttrsOfOneVSM().Add(new RptDepts(), new Depts(), RptDeptAttr.FK_Rpt, RptDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "部门权限");
		map.getAttrsOfOneVSM().Add(new RptEmps(), new Emps(), RptEmpAttr.FK_Rpt, RptEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "人员权限");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 我发起的流程.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyStartFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyStartFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyStartFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_MyStartFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyStartFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);
		rm = new RefMethod();

		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_MyStartFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?如果确定，以前配置将清空。";
		rm.ClassMethodName = this.toString() + ".DoReset_MyStartFlow()";
		rm.RefMethodType = RefMethodType.Func;
		rm.GroupName = "我发起的流程";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 我发起的流程.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 我审批的流程.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyJoinFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyJoinFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyJoinFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "设置导出模板";
			//rm.Icon = "../../WF/Img/Guide.png";
			//rm.ClassMethodName = this.ToString() + ".DoRptExportTemplate_MyJoinFlow()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.GroupName = "我参与的流程";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyJoinFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_MyJoinFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_MyJoinFlow()";
		rm.RefMethodType = RefMethodType.Func;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 我发起的流程.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 我部门发起的流程.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_MyDeptFlow()";
		rm.RefMethodType = RefMethodType.Func;
		rm.GroupName = "本部门发起的流程";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 我部门发起的流程.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 高级查询.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_AdminerFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_AdminerFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_AdminerFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置导出模板";
		rm.Icon = "../../WF/Img/Guide.png";
		rm.ClassMethodName = this.toString() + ".DoRptExportTemplate_AdminerFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_AdminerFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_AdminerFlow()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "查询权限";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoReset_AdminerFlowRight()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_AdminerFlow()";
		rm.RefMethodType = RefMethodType.Func;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 高级查询.

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 通用方法.
	/** 
	 选择的列
	 
	 @param rptMark
	 @return 
	*/
	public final String DoColsChose(String rptMark)
	{
		return "../../Admin/RptDfine/S2ColsChose.htm?FK_Flow= " + this.getNo()+ " &RptNo=ND" + Integer.parseInt(this.No) + "Rpt" + rptMark;
	}
	/** 
	 列的次序
	 
	 @param rptMark
	 @return 
	*/
	public final String DoColsOrder(String rptMark)
	{
		return "../../Admin/RptDfine/S3ColsLabel.htm?FK_Flow= " + this.getNo()+ " &RptNo=ND" + Integer.parseInt(this.No) + "Rpt" + rptMark;
	}
	/** 
	 查询条件设置
	 
	 @param rptMark
	 @return 
	*/
	public final String DoSearchCond(String rptMark)
	{
		return "../../Admin/RptDfine/S5SearchCond.htm?FK_Flow= " + this.getNo()+ " &RptNo=ND" + Integer.parseInt(this.No) + "Rpt" + rptMark;
	}
	/** 
	 导出模版设置
	 
	 @param rptMark
	 @return 
	*/
	public final String DoRptExportTemplate(String rptMark)
	{
		return "../../Admin/RptDfine/S8_RptExportTemplate.htm?FK_Flow= " + this.getNo()+ " &RptNo=ND" + Integer.parseInt(this.No) + "Rpt" + rptMark;
	}
	/** 
	 重置设置.
	*/
	public final String DoReset(String rptMark, String rptName)
	{
		MapData md = new MapData();
		md.No = "ND" + Integer.parseInt(this.No) + "Rpt" + rptMark;
		if (md.RetrieveFromDBSources() == 0)
		{
			md.Name = rptName;
			md.Insert();
		}

		md.RptIsSearchKey = true; //按关键查询.
		md.RptDTSearchWay = DTSearchWay.None; //按日期查询.
		md.RptDTSearchKey = "";

		//设置查询条件.
		switch (rptMark)
		{
			case "My":
			case "MyJoin":
			case "MyDept":
				//md.RptSearchKeys = "*WFSta*FK_NY*"; //查询条件.
				break;
			case "Adminer":
			   // md.RptSearchKeys = "*WFSta*FK_NY*"; //查询条件.
				break;
			default:
				break;
		}

		Flow fl = new Flow(this.No);
		md.PTable = fl.getPTable();
		md.Update();

		String keys = ",OID,FK_Dept,FlowStarter,WFState,Title,FlowStarter,FlowStartRDT,FlowEmps,FlowDaySpan,FlowEnder,FlowEnderRDT,FK_NY,FlowEndNode,WFSta,";

		//string keys = ",OID,Title,WFSta,";

		//查询出来所有的字段.
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(this.No) + "Rpt");
		attrs.Delete(MapAttrAttr.FK_MapData, md.No); // 删除已经有的字段。
		for (MapAttr attr : attrs)
		{
			if (keys.contains("," + attr.KeyOfEn + ",") == false)
			{
				continue;
			}

			attr.FK_MapData = md.No;
			attr.setUIIsEnable(false;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 判断特殊的字段.
			switch (attr.KeyOfEn)
			{
				case GERptAttr.FK_Dept:
					attr.UIBindKey = "BP.Port.Depts";
					attr.setUIContralType (UIContralType.DDL;
					attr.setLGType(FieldTypeS.FK;
					attr.setUIVisible(true;
					attr.setDefVal("";
					attr.setMaxLen(100;
					attr.Update();
					break;
				case GERptAttr.FK_NY:
					attr.UIBindKey = "BP.Pub.NYs";
					attr.setUIContralType (UIContralType.DDL;
					attr.setLGType(FieldTypeS.FK;
					attr.setUIVisible(true;
					attr.setUIIsEnable(false;
					//attr.GroupID = groupID;
					attr.Update();
					break;
				case GERptAttr.Title:
					attr.UIWidth = 120;
					break;
				case GERptAttr.FlowStarter:
					attr.setUIIsEnable(false;
					//attr.setLGType(FieldTypeS.FK;
					//attr.UIBindKey = "BP.Port.Emps";
					//attr.setUIContralType (UIContralType.DDL;
					//attr.UIWidth = 120;
					break;
				case GERptAttr.FlowEndNode:
					//attr.setLGType(FieldTypeS.FK;
					//attr.UIBindKey = "BP.WF.Template.NodeExts";
					//attr.setUIContralType (UIContralType.DDL;
					break;
				case "FK_Emp":
					break;
				default:
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			attr.Insert();
		}
		return "标记为: " + rptMark + "的报表，重置成功...";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 我发起的流程
	/** 
	 设置选择的列
	 
	 @return 
	*/
	public final String DoColsChoseOf_MyStartFlow()
	{
		return this.DoColsChose("My");
	}
	/** 
	 列的次序
	 
	 @return 
	*/
	public final String DoColsOrder_MyStartFlow()
	{
		return DoColsOrder("My");
	}
	/** 
	 查询条件
	 
	 @return 
	*/
	public final String DoSearchCond_MyStartFlow()
	{
		return DoSearchCond("My");
	}
	/** 
	 导出模版.
	 
	 @return 
	*/
	public final String DoRptExportTemplate_MyStartFlow()
	{
		return DoRptExportTemplate("My");
	}
	/** 
	 重置
	 
	 @return 
	*/
	public final String DoReset_MyStartFlow()
	{
		return DoReset("My", "我发起的流程");
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch_MyStartFlow()
	{
		return "../../RptDfine/Search.htm?SearchType=My&FK_Flow=" + this.No;
	}

	/** 
	 分析
	 
	 @return 
	*/
	public final String DoGroup_MyStartFlow()
	{
		return "../../RptDfine/Group.htm?GroupType=My&FK_Flow=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 我参与的流程
	/** 
	 设置选择的列
	 
	 @return 
	*/
	public final String DoColsChoseOf_MyJoinFlow()
	{
		return this.DoColsChose("MyJoin");
	}
	/** 
	 列的次序
	 
	 @return 
	*/
	public final String DoColsOrder_MyJoinFlow()
	{
		return DoColsOrder("MyJoin");
	}
	/** 
	 查询条件
	 
	 @return 
	*/
	public final String DoSearchCond_MyJoinFlow()
	{
		return DoSearchCond("MyJoin");
	}
	/** 
	 导出模版.
	 
	 @return 
	*/
	public final String DoRptExportTemplate_MyJoinFlow()
	{
		return DoRptExportTemplate("MyJoin");
	}
	/** 
	 重置
	 
	 @return 
	*/
	public final String DoReset_MyJoinFlow()
	{
		return DoReset("MyJoin", "我审批的流程");
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch_MyJoinFlow()
	{
		return "../../RptDfine/Search.htm?SearchType=MyJoin&FK_Flow=" + this.No;
	}

	public final String DoGroup_MyJoinFlow()
	{
		return "../../RptDfine/Group.htm?GroupType=MyJoin&FK_Flow=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 我审批的流程

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 本部门发起的流程
	/** 
	 设置选择的列
	 
	 @return 
	*/
	public final String DoColsChoseOf_MyDeptFlow()
	{
		return this.DoColsChose("MyDept");
	}
	/** 
	 列的次序
	 
	 @return 
	*/
	public final String DoColsOrder_MyDeptFlow()
	{
		return DoColsOrder("MyDept");
	}
	/** 
	 查询条件
	 
	 @return 
	*/
	public final String DoSearchCond_MyDeptFlow()
	{
		return DoSearchCond("MyDept");
	}
	/** 
	 导出模版.
	 
	 @return 
	*/
	public final String DoRptExportTemplate_MyDeptFlow()
	{
		return DoRptExportTemplate("MyDept");
	}
	/** 
	 重置
	 
	 @return 
	*/
	public final String DoReset_MyDeptFlow()
	{
		return DoReset("MyDept", "本部门发起的流程");
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch_MyDeptFlow()
	{
		return "../../RptDfine/Search.htm?SearchType=MyDept&FK_Flow=" + this.No;
	}

	/** 
	 分析
	 
	 @return 
	*/
	public final String DoGroup_MyDeptFlow()
	{
		return "../../RptDfine/Group.htm?GroupType=MyDept&FK_Flow=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 本部门发起的流程

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 高级查询
	/** 
	 设置选择的列
	 
	 @return 
	*/
	public final String DoColsChoseOf_AdminerFlow()
	{
		return this.DoColsChose("Adminer");
	}
	/** 
	 列的次序
	 
	 @return 
	*/
	public final String DoColsOrder_AdminerFlow()
	{
		return DoColsOrder("Adminer");
	}
	/** 
	 查询条件
	 
	 @return 
	*/
	public final String DoSearchCond_AdminerFlow()
	{
		return DoSearchCond("Adminer");
	}
	/** 
	 导出模版.
	 
	 @return 
	*/
	public final String DoRptExportTemplate_AdminerFlow()
	{
		return DoRptExportTemplate("Adminer");
	}
	/** 
	 重置
	 
	 @return 
	*/
	public final String DoReset_AdminerFlow()
	{
		return DoReset("Adminer", "本部门发起的流程");
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch_AdminerFlow()
	{
		return "../../RptDfine/Search.htm?SearchType=Adminer&FK_Flow=" + this.No;
	}

	/** 
	 分析
	 
	 @return 
	*/
	public final String DoGroup_AdminerFlow()
	{
		return "../../RptDfine/Search.htm?GroupType=Adminer&FK_Flow=" + this.No;
	}

	public final String DoReset_AdminerFlowRight()
	{
		return "../../Admin/RptDfine/AdvSearchRight.htm?FK_Flow=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 高级查询

}
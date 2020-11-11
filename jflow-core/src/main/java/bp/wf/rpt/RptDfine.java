package bp.wf.rpt;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.wf.data.*;
import bp.wf.*;
import java.util.*;

/** 
 报表定义
*/
public class RptDfine extends EntityNoName
{


		///属性
	/** 
	 本部门流程查询权限定义
	*/
	public final int getMyDeptRole()throws Exception
	{
		return this.GetValIntByKey(RptDfineAttr.MyDeptRole);
	}
	public final void setMyDeptRole(int value) throws Exception
	{
		this.SetValByKey(RptDfineAttr.MyDeptRole, value);
	}

		///


		///构造方法
	@Override
	public UAC getHisUAC() throws Exception
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
	public RptDfine(String no) throws Exception
	{
		this.setNo( no);
		this.Retrieve();
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

		Map map = new Map("WF_Flow", "报表定义");
		map.setCodeStruct("4");


			///基本属性.
		map.AddTBStringPK(RptDfineAttr.No, null, "编号", true, false, 1, 200, 20);
		map.AddTBString(RptDfineAttr.Name, null, "流程名称", true, false, 0, 500, 20);

		map.AddDDLSysEnum(RptDfineAttr.MyDeptRole, 0, "本部门发起的流程", true, true, RptDfineAttr.MyDeptRole, "@0=仅部门领导可以查看@1=部门下所有的人都可以查看@2=本部门里指定岗位的人可以查看", true);

			//map.AddTBString(RptDfineAttr.PTable, null, "物理表", true, false, 0, 500, 20);
			//map.AddTBString(RptDfineAttr.Note, null, "备注", true, false, 0, 500, 20);

			/// 基本属性.


			///绑定的关联关系.
		map.getAttrsOfOneVSM().Add(new RptStations(), new Stations(), RptStationAttr.FK_Rpt, RptStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "岗位权限");
		map.getAttrsOfOneVSM().Add(new RptDepts(), new Depts(), RptDeptAttr.FK_Rpt, RptDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "部门权限");
		map.getAttrsOfOneVSM().Add(new RptEmps(), new Emps(), RptEmpAttr.FK_Rpt, RptEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "人员权限");

			///


			///我发起的流程.
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

		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_MyStartFlow()";
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

			/// 我发起的流程.


			///我审批的流程.
		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SelectCols.png";
		rm.ClassMethodName = this.toString() + ".DoColsChoseOf_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示列次序";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Order.png";
		rm.ClassMethodName = this.toString() + ".DoColsOrder_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置查询条件";
		rm.Icon = "../../WF/Admin/RptDfine/Img/SearchCond.png";
		rm.ClassMethodName = this.toString() + ".DoSearchCond_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "设置导出模板";
			//rm.Icon = "../../WF/Img/Guide.png";
			//rm.ClassMethodName = this.ToString() + ".DoRptExportTemplate_MyJoinFlow()";
			//rm.refMethodType = RefMethodType.RightFrameOpen;
			//rm.GroupName = "我参与的流程";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoSearch_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_MyJoinFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "恢复设置";
		rm.Icon = "../../WF/Admin/RptDfine/Img/Reset.png";
		rm.Warning = "您确定要执行吗?";
		rm.ClassMethodName = this.toString() + ".DoReset_MyJoinFlow()";
		rm.refMethodType = RefMethodType.Func;
		rm.GroupName = "我审批的流程";
		map.AddRefMethod(rm);

			/// 我发起的流程.


			///我部门发起的流程.
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
		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_MyDeptFlow()";
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

			/// 我部门发起的流程.


			///高级查询.
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
		rm.Title = "执行分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoGroup_AdminerFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级查询";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "查询权限";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoReset_AdminerFlowRight()";
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

			/// 高级查询.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///


		///通用方法.
	/** 
	 选择的列
	 
	 @param rptMark
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsChose(String rptMark) throws NumberFormatException, Exception
	{
		return "../../Admin/RptDfine/S2ColsChose.htm?FK_Flow=" + this.getNo()+ "&RptNo=ND" + Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}
	/** 
	 列的次序
	 
	 @param rptMark
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsOrder(String rptMark) throws NumberFormatException, Exception
	{
		return "../../Admin/RptDfine/S3ColsLabel.htm?FK_Flow=" + this.getNo()+ "&RptNo=ND" + Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}
	/** 
	 查询条件设置
	 
	 @param rptMark
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoSearchCond(String rptMark) throws NumberFormatException, Exception
	{
		return "../../Admin/RptDfine/S5SearchCond.htm?FK_Flow=" + this.getNo()+ "&RptNo=ND" + Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
	}
	/** 
	 导出模版设置
	 
	 @param rptMark
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoRptExportTemplate(String rptMark) throws NumberFormatException, Exception
	{
		return "../../Admin/RptDfine/S8_RptExportTemplate.htm?FK_Flow=" + this.getNo()+ "&RptNo=ND" + Integer.parseInt(this.getNo()) + "Rpt" + rptMark;
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
			md.setName( rptName);
			md.Insert();
		}

		md.setRptIsSearchKey(true); //按关键查询.
		md.setRptDTSearchWay(DTSearchWay.None); //按日期查询.
		md.setRptDTSearchKey("");

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

		Flow fl = new Flow(this.getNo());
		md.setPTable(fl.getPTable());
		md.Update();

		String keys = ",OID,FK_Dept,FlowStarter,WFState,Title,FlowStarter,FlowStartRDT,FlowEmps,FlowDaySpan,FlowEnder,FlowEnderRDT,FK_NY,FlowEndNode,WFSta,";

		//string keys = ",OID,Title,WFSta,";

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


				///判断特殊的字段.
			switch (attr.getKeyOfEn())
			{
				case GERptAttr.FK_Dept:
					attr.setUIBindKey("bp.port.Depts");
					attr.setUIContralType( UIContralType.DDL);
					attr.setLGType( FieldTypeS.FK);
					attr.setUIVisible( true);
					attr.setDefVal("");
					attr.setMaxLen( 100);
					attr.Update();
					break;
				case GERptAttr.FK_NY:
					attr.setUIBindKey("bp.pub.NYs");
					attr.setUIContralType( UIContralType.DDL);
					attr.setLGType( FieldTypeS.FK);
					attr.setUIVisible( true);
					attr.setUIIsEnable(false);
					//attr.GroupID = groupID;
					attr.Update();
					break;
				case GERptAttr.Title:
					attr.setUIWidth(120);
					break;
				case GERptAttr.FlowStarter:
					attr.setUIIsEnable(false);
				
					break;
				case GERptAttr.FlowEndNode:
					
					break;
				case "FK_Emp":
					break;
				default:
					break;
			}

				///

			attr.Insert();
		}
		return "标记为: " + rptMark + "的报表，重置成功...";
	}

		///


		///我发起的流程
	/** 
	 设置选择的列
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsChoseOf_MyStartFlow() throws NumberFormatException, Exception
	{
		return this.DoColsChose("My");
	}
	/** 
	 列的次序
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsOrder_MyStartFlow() throws NumberFormatException, Exception
	{
		return DoColsOrder("My");
	}
	/** 
	 查询条件
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoSearchCond_MyStartFlow() throws NumberFormatException, Exception
	{
		return DoSearchCond("My");
	}
	/** 
	 导出模版.
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoRptExportTemplate_MyStartFlow() throws NumberFormatException, Exception
	{
		return DoRptExportTemplate("My");
	}
	/** 
	 重置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoReset_MyStartFlow() throws Exception
	{
		return DoReset("My", "我发起的流程");
	}
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearch_MyStartFlow() throws Exception
	{
		return "../../RptDfine/Search.htm?SearchType=My&FK_Flow=" + this.getNo();
	}

	/** 
	 分析
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoGroup_MyStartFlow() throws Exception
	{
		return "../../RptDfine/Group.htm?GroupType=My&FK_Flow=" + this.getNo();
	}

		///


		///我参与的流程
	/** 
	 设置选择的列
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsChoseOf_MyJoinFlow() throws NumberFormatException, Exception
	{
		return this.DoColsChose("MyJoin");
	}
	/** 
	 列的次序
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsOrder_MyJoinFlow() throws NumberFormatException, Exception
	{
		return DoColsOrder("MyJoin");
	}
	/** 
	 查询条件
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoSearchCond_MyJoinFlow() throws NumberFormatException, Exception
	{
		return DoSearchCond("MyJoin");
	}
	/** 
	 导出模版.
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoRptExportTemplate_MyJoinFlow() throws NumberFormatException, Exception
	{
		return DoRptExportTemplate("MyJoin");
	}
	/** 
	 重置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoReset_MyJoinFlow() throws Exception
	{
		return DoReset("MyJoin", "我审批的流程");
	}
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearch_MyJoinFlow() throws Exception
	{
		return "../../RptDfine/Search.htm?SearchType=MyJoin&FK_Flow=" + this.getNo();
	}

	public final String DoGroup_MyJoinFlow() throws Exception
	{
		return "../../RptDfine/Group.htm?GroupType=MyJoin&FK_Flow=" + this.getNo();
	}

		/// 我审批的流程


		///本部门发起的流程
	/** 
	 设置选择的列
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsChoseOf_MyDeptFlow() throws NumberFormatException, Exception
	{
		return this.DoColsChose("MyDept");
	}
	/** 
	 列的次序
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsOrder_MyDeptFlow() throws NumberFormatException, Exception
	{
		return DoColsOrder("MyDept");
	}
	/** 
	 查询条件
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoSearchCond_MyDeptFlow() throws NumberFormatException, Exception
	{
		return DoSearchCond("MyDept");
	}
	/** 
	 导出模版.
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoRptExportTemplate_MyDeptFlow() throws NumberFormatException, Exception
	{
		return DoRptExportTemplate("MyDept");
	}
	/** 
	 重置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoReset_MyDeptFlow() throws Exception
	{
		return DoReset("MyDept", "本部门发起的流程");
	}
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearch_MyDeptFlow() throws Exception
	{
		return "../../RptDfine/Search.htm?SearchType=MyDept&FK_Flow=" + this.getNo();
	}

	/** 
	 分析
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoGroup_MyDeptFlow() throws Exception
	{
		return "../../RptDfine/Group.htm?GroupType=MyDept&FK_Flow=" + this.getNo();
	}

		/// 本部门发起的流程


		///高级查询
	/** 
	 设置选择的列
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsChoseOf_AdminerFlow() throws NumberFormatException, Exception
	{
		return this.DoColsChose("Adminer");
	}
	/** 
	 列的次序
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoColsOrder_AdminerFlow() throws NumberFormatException, Exception
	{
		return DoColsOrder("Adminer");
	}
	/** 
	 查询条件
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoSearchCond_AdminerFlow() throws NumberFormatException, Exception
	{
		return DoSearchCond("Adminer");
	}
	/** 
	 导出模版.
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String DoRptExportTemplate_AdminerFlow() throws NumberFormatException, Exception
	{
		return DoRptExportTemplate("Adminer");
	}
	/** 
	 重置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoReset_AdminerFlow() throws Exception
	{
		return DoReset("Adminer", "本部门发起的流程");
	}
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearch_AdminerFlow() throws Exception
	{
		return "../../RptDfine/Search.htm?SearchType=Adminer&FK_Flow=" + this.getNo();
	}

	/** 
	 分析
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoGroup_AdminerFlow() throws Exception
	{
		return "../../RptDfine/Group.htm?GroupType=Adminer&FK_Flow=" + this.getNo();
	}

	public final String DoReset_AdminerFlowRight() throws Exception
	{
		return "../../Admin/RptDfine/AdvSearchRight.htm?FK_Flow=" + this.getNo();
	}

		/// 高级查询

}
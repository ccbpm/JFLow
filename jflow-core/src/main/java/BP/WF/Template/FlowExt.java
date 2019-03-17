package BP.WF.Template;

import java.io.IOException;
import java.util.Date;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.Web.*;
import BP.Tools.ContextHolderUtils;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.FlowDeleteRole;
import BP.WF.FlowRunWay;
import BP.WF.Flows;
import BP.WF.GenerWorkFlow;
import BP.WF.GenerWorkFlowAttr;
import BP.WF.GenerWorkFlows;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerListAttr;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.ReturnWork;
import BP.WF.TrackAttr;
import BP.WF.WFState;
import BP.WF.WorkAttr;
import BP.WF.WorkNode;
import BP.WF.Work;
import BP.WF.Works;
import BP.WF.Data.*;

/** 
 流程
 
*/
public class FlowExt extends EntityNoName
{

	/** 
	 流程类别
	 
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStringByKey(FlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		this.SetValByKey(FlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别（第2级流程树节点编号）f
	 
	*/
	public final String getSysType()
	{
		return this.GetValStringByKey(FlowAttr.SysType);
	}
	public final void setSysType(String value)
	{
		this.SetValByKey(FlowAttr.SysType, value);
	}
	/** 
	 流程事件实体
	 
	*/
	public final String getFlowEventEntity()
	{
		return this.GetValStringByKey(FlowAttr.FlowEventEntity);
	}
	public final void setFlowEventEntity(String value)
	{
		this.SetValByKey(FlowAttr.FlowEventEntity, value);
	}
	/** 
	 流程标记
	 
	*/
	public final String getFlowMark()
	{
		String str = this.GetValStringByKey(FlowAttr.FlowMark);
		if (str.equals(""))
		{
			return this.getNo();
		}
		return str;
	}
	public final void setFlowMark(String value)
	{
		this.SetValByKey(FlowAttr.FlowMark, value);
	}


		///#region   前置导航
	/** 
	 前置导航方式
	 
	*/
	public final StartGuideWay getStartGuideWay()
	{
		return StartGuideWay.forValue(this.GetValIntByKey(FlowAttr.StartGuideWay));

	}
	public final void setStartGuideWay(StartGuideWay value)
	{
		this.SetValByKey(FlowAttr.StartGuideWay, value.getValue());
	}

	/** 
	 前置导航参数1
	 
	*/

	public final String getStartGuidePara1()
	{
		return this.GetValStringByKey(FlowAttr.StartGuidePara1);
	}
	public final void setStartGuidePara1(String value)
	{
		this.SetValByKey(FlowAttr.StartGuidePara1, value);
	}

	/** 
	 前置导航参数2
	 
	*/

	public final String getStartGuidePara2()
	{
		return this.GetValStringByKey(FlowAttr.StartGuidePara2);
	}
	public final void setStartGuidePara2(String value)
	{
		this.SetValByKey(FlowAttr.StartGuidePara2, value);
	}

	/** 
	 前置导航参数3
	 
	*/

	public final String getStartGuidePara3()
	{
		return this.GetValStringByKey(FlowAttr.StartGuidePara3);
	}
	public final void setStartGuidePara3(String value)
	{
		this.SetValByKey(FlowAttr.StartGuidePara3, value);
	}


	/** 
	 启动方式
	 
	*/
	public final FlowRunWay getFlowRunWay()
	{
		return FlowRunWay.forValue(this.GetValIntByKey(FlowAttr.FlowRunWay));

	}
	public final void setFlowRunWay(FlowRunWay value)
	{
		this.SetValByKey(FlowAttr.FlowRunWay, value.getValue());
	}


	/** 
	 运行内容
	 
	*/

	public final String getRunObj()
	{
		return this.GetValStringByKey(FlowAttr.RunObj);
	}
	public final void setRunObj(String value)
	{
		this.SetValByKey(FlowAttr.RunObj, value);
	}


	/** 
	 是否启用开始节点数据重置按钮
	 
	*/

	public final boolean getIsResetData()
	{
		return this.GetValBooleanByKey(FlowAttr.IsResetData);
	}
	public final void setIsResetData(boolean value)
	{
		this.SetValByKey(FlowAttr.IsResetData, value);
	}

	/** 
	 是否自动装载上一笔数据
	 
	*/
	public final boolean getIsLoadPriData()
	{
		return this.GetValBooleanByKey(FlowAttr.IsLoadPriData);
	}
	public final void setIsLoadPriData(boolean value)
	{
		this.SetValByKey(FlowAttr.IsLoadPriData, value);
	}

		///#endregion
	/** 
	 设计者编号
	 
	*/
	public final String getDesignerNo()
	{
		return this.GetValStringByKey(FlowAttr.DesignerNo);
	}
	public final void setDesignerNo(String value)
	{
		this.SetValByKey(FlowAttr.DesignerNo, value);
	}
	/** 
	 设计者名称
	 
	*/
	public final String getDesignerName()
	{
		return this.GetValStringByKey(FlowAttr.DesignerName);
	}
	public final void setDesignerName(String value)
	{
		this.SetValByKey(FlowAttr.DesignerName, value);
	}
	/** 
	 编号生成格式
	 
	*/
	public final String getBillNoFormat()
	{
		return this.GetValStringByKey(FlowAttr.BillNoFormat);
	}
	public final void setBillNoFormat(String value)
	{
		this.SetValByKey(FlowAttr.BillNoFormat, value);
	}

		///#endregion 属性.


		
	/** 
	 UI界面上的访问控制
	 
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate=true;
		return uac;
		
		//Var   i=0;
		
		/*
		if (BP.Web.WebUser.getNo().equals("admin") || this.getDesignerNo().equals(WebUser.getNo()))
		{
			uac.IsUpdate = true;
		} 
			 
		return uac;
		*/
	}
	/** 
	 流程
	 
	*/
	public FlowExt()
	{
	}
	/** 
	 流程
	 
	 @param _No 编号
	 * @throws Exception 
	*/
	public FlowExt(String _No) throws Exception
	{
		this.setNo(_No);
		if (SystemConfig.getIsDebug())
		{
			int i = this.RetrieveFromDBSources();
			if (i == 0)
			{
				throw new RuntimeException("流程编号不存在");
			}
		}
		else
		{
			this.Retrieve();
		}
	}
	/** 
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Flow", "流程");



       // #region 基本属性。
        map.AddTBStringPK(FlowAttr.No, null, "编号", true, true, 1, 10, 3);
        map.SetHelperUrl(FlowAttr.No, "http://ccbpm.mydoc.io/?v=5404&t=17023"); //使用alert的方式显示帮助信息.

        map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), true);
        map.SetHelperUrl(FlowAttr.FK_FlowSort, "http://ccbpm.mydoc.io/?v=5404&t=17024");
        map.AddTBString(FlowAttr.Name, null, "名称", true, false, 0, 50, 10, true);

        // add 2013-02-14 唯一确定此流程的标记
        map.AddTBString(FlowAttr.FlowMark, null, "流程标记", true, false, 0, 150, 10);
        map.AddTBString(FlowAttr.FlowEventEntity, null, "流程事件实体", true, true, 0, 150, 10);
        map.SetHelperUrl(FlowAttr.FlowMark, "http://ccbpm.mydoc.io/?v=5404&t=16847");
        map.SetHelperUrl(FlowAttr.FlowEventEntity, "http://ccbpm.mydoc.io/?v=5404&t=17026");

        // add 2013-02-05.
        map.AddTBString(FlowAttr.TitleRole, null, "标题生成规则", true, false, 0, 150, 10, true);
        map.SetHelperUrl(FlowAttr.TitleRole, "http://ccbpm.mydoc.io/?v=5404&t=17040");


        map.AddBoolean(FlowAttr.IsCanStart, true, "可以独立启动否？(独立启动的流程可以显示在发起流程列表里)", true, true, true);
        map.SetHelperUrl(FlowAttr.IsCanStart, "http://ccbpm.mydoc.io/?v=5404&t=17027");

        map.AddBoolean(FlowAttr.IsFullSA, false, "是否自动计算未来的处理人？", true, true, true);
        map.SetHelperUrl(FlowAttr.IsFullSA, "http://ccbpm.mydoc.io/?v=5404&t=17034");

        map.AddDDLSysEnum(FlowAttr.IsAutoSendSubFlowOver, 0, "为子流程时结束规则", true, true,
            FlowAttr.IsAutoSendSubFlowOver, "@0=不处理@1=让父流程自动运行下一步@2=结束父流程");

        map.AddBoolean(FlowAttr.IsGuestFlow, false, "是否外部用户参与流程(非组织结构人员参与的流程)", true, true, false);
        map.SetHelperUrl(FlowAttr.IsGuestFlow, "http://ccbpm.mydoc.io/?v=5404&t=17039");

        map.AddDDLSysEnum(FlowAttr.FlowAppType, FlowAppType.Normal.getValue(), "流程应用类型",
          true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
        map.SetHelperUrl(FlowAttr.FlowAppType, "http://ccbpm.mydoc.io/?v=5404&t=17035");
        map.AddDDLSysEnum(FlowAttr.TimelineRole, TimelineRole.ByNodeSet.getValue(), "时效性规则",
         true, true, FlowAttr.TimelineRole, "@0=按节点(由节点属性来定义)@1=按发起人(开始节点SysSDTOfFlow字段计算)");
        map.SetHelperUrl(FlowAttr.TimelineRole, "http://ccbpm.mydoc.io/?v=5404&t=17036");

        // 草稿
        map.AddDDLSysEnum(FlowAttr.Draft, DraftRole.None.getValue(), "草稿规则",
       true, true, FlowAttr.Draft, "@0=无(不设草稿)@1=保存到待办@2=保存到草稿箱");
        map.SetHelperUrl(FlowAttr.Draft, "http://ccbpm.mydoc.io/?v=5404&t=17037");

        // add for 华夏银行.
        map.AddDDLSysEnum(FlowAttr.FlowDeleteRole, FlowDeleteRole.AdminOnly.getValue(), "流程实例删除规则",
    true, true, FlowAttr.FlowDeleteRole,
    "@0=超级管理员可以删除@1=分级管理员可以删除@2=发起人可以删除@3=节点启动删除按钮的操作员");


        map.AddDDLSysEnum(FlowAttr.FlowAppType, FlowAppType.Normal.getValue(), "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
        map.AddTBString(FlowAttr.HelpUrl, null, "帮助文档", true, false, 0, 300, 10, true);


        //为 莲荷科技增加一个系统类型, 用于存储当前所在流程树的第2级流程树编号.
        map.AddTBString(FlowAttr.SysType, null, "系统类型", false, false, 0, 100, 10, false);
        map.AddTBString(FlowAttr.Tester, null, "发起测试人", true, false, 0, 300, 10, true);

        String sql = "SELECT No,Name FROM Sys_EnumMain WHERE No LIKE 'Flow_%' ";
        map.AddDDLSQL("NodeAppType", null, "业务类型枚举(可为Null)", sql, true);

        // add 2014-10-19.
        map.AddDDLSysEnum(FlowAttr.ChartType, FlowChartType.Icon.getValue(), "节点图形类型", true, true,
            "ChartType", "@0=几何图形@1=肖像图片");
   	 //运行主机. 这个流程运行在那个子系统的主机上.
        map.AddTBString("HostRun", null, "运行主机(IP+端口)", true, false, 0, 40, 10, true);
      //  #endregion 基本属性。

       // #region 表单数据.

        //批量发起 add 2013-12-27. 
        map.AddBoolean(FlowAttr.IsBatchStart, false, "是否可以批量发起流程？(如果是就要设置发起的需要填写的字段,多个用逗号分开)", true, true, true);
        map.AddTBString(FlowAttr.BatchStartFields, null, "发起字段s", true, false, 0, 500, 10, true);
        map.SetHelperUrl(FlowAttr.IsBatchStart, "http://ccbpm.mydoc.io/?v=5404&t=17047");

        //add 2013-05-22.
        map.AddTBString(FlowAttr.HistoryFields, null, "历史查看字段", true, false, 0, 500, 10, true);

        //移动到这里 by zhoupeng 2016.04.08.
        map.AddBoolean(FlowAttr.IsResetData, false, "是否启用开始节点数据重置按钮？", true, true, true);
        map.AddBoolean(FlowAttr.IsLoadPriData, false, "是否自动装载上一笔数据？", true, true, true);
        map.AddBoolean(FlowAttr.IsDBTemplate, true, "是否启用数据模版？", true, true, true);
        map.AddBoolean(FlowAttr.IsStartInMobile, true, "是否可以在手机里启用？(如果发起表单特别复杂就不要在手机里启用了)", true, true, true);
        map.SetHelperAlert(FlowAttr.IsStartInMobile, "用于控制手机端流程发起列表.");

        map.AddBoolean(FlowAttr.IsMD5, false, "是否是数据加密流程(MD5数据加密防篡改)", true, true, true);
        map.SetHelperUrl(FlowAttr.IsMD5, "http://ccbpm.mydoc.io/?v=5404&t=17028");

        // 数据存储.
        map.AddDDLSysEnum(FlowAttr.DataStoreModel, DataStoreModel.ByCCFlow.getValue(), "数据存储", true, true, FlowAttr.DataStoreModel, "@0=数据轨迹模式@1=数据合并模式");
        map.SetHelperUrl(FlowAttr.DataStoreModel, "http://ccbpm.mydoc.io/?v=5404&t=17038");

        map.AddTBString(FlowAttr.PTable, null, "流程数据存储表", true, false, 0, 30, 10);
        map.SetHelperUrl(FlowAttr.PTable, "http://ccbpm.mydoc.io/?v=5404&t=17897");


        //map.SetHelperBaidu(FlowAttr.HistoryFields, "ccflow 历史查看字段");
        map.AddTBString(FlowAttr.FlowNoteExp, null, "备注的表达式", true, false, 0, 500, 10, true);
        map.SetHelperUrl(FlowAttr.FlowNoteExp, "http://ccbpm.mydoc.io/?v=5404&t=17043");

        //add  2013-08-30.
        map.AddTBString(FlowAttr.BillNoFormat, null, "单据编号格式", true, false, 0, 50, 10, false);
        map.SetHelperUrl(FlowAttr.BillNoFormat, "http://ccbpm.mydoc.io/?v=5404&t=17041");
     //   #endregion 表单数据.

       // #region 开发者信息.
        //map.AddTBString("NodeAppType", null, "业务类型枚举", true, false, 0, 50, 10, true);
        map.AddTBString(FlowAttr.DesignerNo, null, "设计者编号", true, false, 0, 50, 10, false);
        map.AddTBString(FlowAttr.DesignerName, null, "设计者名称", true, false, 0, 50, 10, false);
        map.AddTBStringDoc(FlowAttr.Note, null, "流程描述", true, false, true);
       // #endregion 开发者信息.
		
		
 
			///#region 基本功能.
			//map.AddRefMethod(rm);
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "调试运行"; // "设计检查报告";
			//rm.ToolTip = "检查流程设计的问题。";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/Run.png";
		rm.ClassMethodName = this.toString() + ".DoRunIt";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "检查报告"; // "设计检查报告";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/CheckRpt.png";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设计报表"; // "报表运行";
		rm.Icon = "../../WF/Img/Btn/Rpt.gif";
		rm.ClassMethodName = this.toString() + ".DoOpenRpt()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "自动发起";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/AutoStart.png";
		rm.ClassMethodName = this.toString() + ".DoSetStartFlowDataSources()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发起限制规则";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/limit.png";
		rm.ClassMethodName = this.toString() + ".DoLimit()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发起前置导航";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/StartGuide.png";
		rm.ClassMethodName = this.toString() + ".DoStartGuide()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);
 

		rm = new RefMethod();
		rm.Title = "流程事件&消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon =   "../../WF/Img/Event.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
        rm.Title = "版本管理";
        rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
        rm.ClassMethodName = this.toString() + ".DoVer()";
        rm.refMethodType = RefMethodType.RightFrameOpen;
        // rm.GroupName = "实验中的功能";
        map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程轨迹表单";
		rm.Icon =  "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoBindFlowExt()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量设置节点";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoNodeAttrs()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "轨迹查看权限";
		rm.Icon = "../../WF/Img/Setting.png";
		rm.ClassMethodName = this.toString() + ".DoTruckRight()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "数据源管理(如果新增数据源后需要关闭重新打开)";
		rm.ClassMethodName = this.toString() + ".DoDBSrc";
		rm.Icon =  "../../WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSDBSrc;
		rm.RefAttrLinkLabel = "数据源管理";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "一键设置审核组件工作模式";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoSetFWCModel()";
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 第2个节点以后的节点表单都指向第2个节点表单.  \t\n  2, 结束节点都设置为只读模式. ";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除NDxxxRpt表,多余字段.";
		rm.ClassMethodName = this.toString() + ".DoDeleteFields()";
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 表NDxxxRpt是自动创建的.  \t\n  2, 在设置流程过程中有些多余的字段会生成到NDxxRpt表里. \t\n 3,这里是删除数据字段为null 并且是多余的字段.";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除NDxxxRpt表,数据为null的多余字段.";
		rm.ClassMethodName = this.toString() + ".DoDeleteFieldsIsNull()";
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 表NDxxxRpt是自动创建的.  \t\n  2, 在设置流程过程中有些多余的字段会生成到NDxxxRpt表里. \t\n 3,这里是删除数据字段为null 并且是多余的字段.";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);
		
		
			///#endregion 实验中的功能


			///#region 流程模版管理.
		rm = new RefMethod();
		rm.Title = "模版导入";
		rm.Icon = "../../WF/Img/redo.png";
		rm.ClassMethodName = this.toString() + ".DoImp()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程模版";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "模版导出";
		rm.Icon = "../../WF/Img/undo.png";
		rm.ClassMethodName = this.toString() + ".DoExps()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程模版";
		map.AddRefMethod(rm);

			///#endregion 流程模版管理.


			///#region 开发接口.


		rm = new RefMethod();
		rm.Title = "与业务表数据同步";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/DTS.png";

		rm.ClassMethodName = this.toString() + ".DoDTSBTable()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "URL调用接口";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/URL.png";
		rm.ClassMethodName = this.toString() + ".DoAPI()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "SDK开发接口";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/API.png";
		rm.ClassMethodName = this.toString() + ".DoAPICode()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "代码事件开发接口";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/API.png";
		rm.ClassMethodName = this.toString() + ".DoAPICodeFEE()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

			///#endregion 开发接口.


			///#region 报表设计.
//		rm = new RefMethod();
//		rm.Title = "报表设计";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/DesignRpt.png";
//		rm.ClassMethodName = this.toString() + ".DoDRpt()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "报表设计";
//		map.AddRefMethod(rm);
//
//		rm = new RefMethod();
//		rm.Title = "流程查询";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
//		rm.ClassMethodName = this.toString() + ".DoDRptSearch()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "报表设计";
//		map.AddRefMethod(rm);
//
//		rm = new RefMethod();
//		rm.Title = "自定义查询";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/SQL.png";
//		rm.ClassMethodName = this.toString() + ".DoDRptSearchAdv()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "报表设计";
//		map.AddRefMethod(rm);
//
//		rm = new RefMethod();
//		rm.Title = "分组分析";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
//		rm.ClassMethodName = this.toString() + ".DoDRptGroup()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "报表设计";
//		map.AddRefMethod(rm);
//
//		rm = new RefMethod();
//		rm.Title = "交叉报表";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/D3.png";
//		rm.ClassMethodName = this.toString() + ".DoDRptD3()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "报表设计";
//		map.AddRefMethod(rm);
//
//		rm = new RefMethod();
//		rm.Title = "对比分析";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Contrast.png";
//		rm.ClassMethodName = this.toString() + ".DoDRptContrast()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "报表设计";
//		map.AddRefMethod(rm);

			///#endregion 报表设计.


			///#region 流程运行维护.
		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Title = "重生成报表数据"; // "删除数据";
		rm.Warning = "您确定要执行吗? 注意:此方法耗费资源。"; // "您确定要执行删除流程数据吗？";
		rm.ClassMethodName = this.toString() + ".DoReloadRptData";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重生成流程标题";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoGenerTitle()";
			//设置相关字段.
			//rm.RefAttrKey = FlowAttr.TitleRole;
		rm.RefAttrLinkLabel = "重新生成流程标题";
		rm.refMethodType = RefMethodType.Func;
		rm.Target = "_blank";
		rm.Warning = "您确定要根据新的规则重新产生标题吗？";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重生成FlowEmps字段";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoGenerFlowEmps()";
		rm.RefAttrLinkLabel = "补充修复emps字段，包括wf_generworkflow,NDxxxRpt字段.";
		rm.refMethodType = RefMethodType.Func;
		rm.Target = "_blank";
		rm.Warning = "您确定要重新生成吗？";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

			//带有参数的方法.
		rm = new RefMethod();
		rm.GroupName = "流程维护";
		rm.Title = "重命名节点表单字段";
			//  rm.Warning = "您确定要处理吗？";
		rm.getHisAttrs().AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddBoolen("thisFlowOnly", true, "仅仅当前流程");
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "节点表单字段视图";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
		rm.ClassMethodName = this.toString() + ".DoFlowFields()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "删除全部流程数据"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.Warning = "您确定要执行删除流程数据吗? \t\n该流程的数据删除后，就不能恢复，请注意删除的内容。"; // "您确定要执行删除流程数据吗？";
		rm.ClassMethodName = this.toString() + ".DoDelData";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "按工作ID删除"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.GroupName = "流程维护";
		rm.ClassMethodName = this.toString() + ".DoDelDataOne";
		rm.getHisAttrs().AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.getHisAttrs().AddTBString("beizhu", null, "删除备注", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

			//带有参数的方法.
		rm = new RefMethod();
		rm.GroupName = "流程维护";
		rm.Title = "删除指定日期范围内的流程";
		rm.Warning = "您确定要删除吗？";
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.getHisAttrs().AddTBDateTime("DTFrom", null, "时间从", true, false);
		rm.getHisAttrs().AddTBDateTime("DTTo", null, "时间到", true, false);
		rm.getHisAttrs().AddBoolen("thisFlowOnly", true, "仅仅当前流程");
		rm.ClassMethodName = this.toString() + ".DoDelFlows";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "回滚流程";
		rm.Icon = "../../WF/Img/Btn/Back.png";
		rm.ClassMethodName = this.toString() + ".DoRebackFlowData()";
			// rm.Warning = "您确定要回滚它吗？";
		rm.getHisAttrs().AddTBInt("workid", 0, "请输入要会滚WorkID", true, false);
		rm.getHisAttrs().AddTBInt("nodeid", 0, "回滚到的节点ID", true, false);
		rm.getHisAttrs().AddTBString("note", null, "回滚原因", true, false, 0, 600, 200);
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

			///#endregion 流程运行维护.


			///#region 流程监控.

		/*rm = new RefMethod();
		rm.Title = "监控面板";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Monitor.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Welcome()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);*/

//		rm = new RefMethod();
//		rm.Title = "流程查询";
//		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
//		rm.ClassMethodName = this.toString() + ".DoDataManger()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "流程监控";
//		map.AddRefMethod(rm);

		/*rm = new RefMethod();
		rm.Title = "节点列表";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/flows.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Nodes()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);*/

		rm = new RefMethod();
		rm.Title = "综合查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Search()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "综合分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Group()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);
		
		  rm = new RefMethod();
			rm.Title = "删除日志";
			rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/log.png";
			rm.ClassMethodName = this.toString() + ".DoDataManger_DeleteLog()";
			rm.refMethodType = RefMethodType.RightFrameOpen;
			rm.GroupName = "流程监控";
			map.AddRefMethod(rm);
		

		/*rm = new RefMethod();
		rm.Title = "实例增长分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Grow.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_InstanceGrowOneFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "逾期未完成实例";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/warning.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_InstanceWarning()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "逾期已完成实例";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/overtime.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_InstanceOverTimeOneFlow()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除日志";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/log.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_DeleteLog()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "数据订阅-实验中";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/RptOrder.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_RptOrder()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);*/

			///#endregion 流程监控.


			//rm = new RefMethod();
			//rm.Title = "执行流程数据表与业务表数据手工同步"; 
			//rm.ClassMethodName = this.ToString() + ".DoBTableDTS";
			//rm.Icon = BP.WF.Glo.CCFlowAppPath + "WF/Img/Btn/DTS.gif";
			//rm.Warning = "您确定要执行吗？如果执行了可能会对业务表数据造成错误。";
			////设置相关字段.
			//rm.RefAttrKey = FlowAttr.DTSSpecNodes;
			//rm.RefAttrLinkLabel = "业务表字段同步配置";
			//rm.RefMethodType = RefMethodType.Func;
			//rm.Target = "_blank";
			////  map.AddRefMethod(rm);


			//rm = new RefMethod();
			//rm.Title = "设置自动发起"; // "报表运行";
			//rm.Icon = "/WF/Img/Btn/View.gif";
			//rm.ClassMethodName = this.ToString() + ".DoOpenRpt()";
			////rm.Icon = "/WF/Img/Btn/Table.gif"; 
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = this.ToE("Event", "事件"); // "报表运行";
			//rm.Icon = "/WF/Img/Btn/View.gif";
			//rm.ClassMethodName = this.ToString() + ".DoOpenRpt()";
			////rm.Icon = "/WF/Img/Btn/Table.gif";
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = this.ToE("FlowExtDataOut", "数据转出定义");  //"数据转出定义";
			////  rm.Icon = "/WF/Img/Btn/Table.gif";
			//rm.ToolTip = "在流程完成时间，流程数据转储存到其它系统中应用。";
			//rm.ClassMethodName = this.ToString() + ".DoExp";
			//map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 流程监控.
	/*public final String DoDataManger_Welcome()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/CCBPMDesigner/App/OneFlow/Welcome.jsp?FK_Flow=" + this.getNo();
	}*/
	/*public final String DoDataManger_Nodes()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/CCBPMDesigner/App/OneFlow/Nodes.jsp?FK_Flow=" + this.getNo();
	}*/
	public final String DoDataManger_Search()
	{
		return Glo.getCCFlowAppPath() + "WF/Comm/Search.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.getNo() + "&WFSta=all";
	}
	public final String DoDataManger_Group()
	{
		return Glo.getCCFlowAppPath() + "WF/Comm/Group.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.getNo() + "&WFSta=all";
	}

	public final String DoDataManger_InstanceGrowOneFlow()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FlowDB/InstanceGrowOneFlow.jsp?anaTime=mouth&FK_Flow=" + this.getNo();
	}

	public final String DoDataManger_InstanceWarning()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FlowDB/InstanceWarning.jsp?anaTime=mouth&FK_Flow=" + this.getNo();
	}

	public final String DoDataManger_InstanceOverTimeOneFlow()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FlowDB/InstanceOverTimeOneFlow.jsp?anaTime=mouth&FK_Flow=" + this.getNo();
	}
	public final String DoDataManger_DeleteLog()
	{
		return Glo.getCCFlowAppPath() + "WF/Comm/Search.htm?EnsName=BP.WF.WorkFlowDeleteLogs&FK_Flow=" + this.getNo() + "&WFSta=all";
	}

	/** 
	 数据订阅
	 
	 @return 
	*/
	public final String DoDataManger_RptOrder()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/CCBPMDesigner/App/RptOrder.jsp?anaTime=mouth&FK_Flow=" + this.getNo();
	}

		///#endregion 流程监控.


		///#region 报表设计.
	public final String DoDRpt()
	{

		/**WF/Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Rpt.MapRptExts&PK=ND185MyRpt;
		*/
		//return Glo.getCCFlowAppPath() + "WF/Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Rpt.MapRptExts&PK=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
		return Glo.getCCFlowAppPath() +"WF/Comm/En.htm?EnName=BP.WF.Rpt.MapRptExt&PKVal=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
		//  UIEn.jsp?EnsName=BP.WF.Rpt.MapRptExts&PK=ND185MyRpt
		//return Glo.getCCFlowAppPath() + "WF/Rpt/OneFlow.jsp?FK_Flow=" + this.getNo() + "&FK_MapData=ND" + int.Parse(this.getNo()) + "MyRpt";
	}
	public final String DoDRptSearch()
	{
		return Glo.getCCFlowAppPath() + "WF/Rpt/Search.jsp?FK_Flow=" + this.getNo() + "&RptNo=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
	}
	public final String DoDRptSearchAdv()
	{
		return Glo.getCCFlowAppPath() + "WF/Rpt/SearchAdv.htm?FK_Flow=" + this.getNo() + "&RptNo=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
	}
	public final String DoDRptGroup()
	{
		return Glo.getCCFlowAppPath() + "WF/RptDfine/Group.jsp?FK_Flow=" + this.getNo() + "&RptNo=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
	}
	public final String DoDRptD3()
	{
		return Glo.getCCFlowAppPath() + "WF/Rpt/D3.jsp?FK_Flow=" + this.getNo() + "&RptNo=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
	}
	public final String DoDRptContrast()
	{
		return Glo.getCCFlowAppPath() + "WF/Rpt/Contrast.htm?FK_Flow=" + this.getNo() + "&RptNo=ND" + Integer.parseInt(this.getNo()) + "MyRpt";
	}

		///#endregion 报表设计.


		///#region 开发接口.
	/** 
	 执行删除指定日期范围内的流程
	 
	 @param dtFrom 日期从
	 @param dtTo 日期到
	 @param isOk 仅仅删除当前流程？1=删除当前流程, 0=删除全部流程.
	 @return 
	 * @throws Exception 
	*/
	public final String DoDelFlows(String dtFrom, String dtTo, String isDelCurrFlow) throws Exception
	{
		if ( ! BP.Web.WebUser.getNo().equals("admin"))
		{
			return "非admin用户，不能删除。";
		}

		String sql = "";
		if (isDelCurrFlow.equals("1"))
		{
			sql = "SELECT WorkID, FK_Flow FROM WF_GenerWorkFlow  WHERE RDT >= '" + dtFrom + "' AND RDT <= '" + dtTo + "'  AND FK_Flow='" + this.getNo() + "' ";
		}
		else
		{
			sql = "SELECT WorkID, FK_Flow FROM WF_GenerWorkFlow  WHERE RDT >= '" + dtFrom + "' AND RDT <= '" + dtTo + "' ";
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		String msg = "如下流程ID被删除:";
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			String fk_flow = dr.getValue("FK_Flow").toString();
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow, workid, false);
			msg += " " + workid;
		}
		return msg;
	}
	/** 
	 批量重命名字段.
	 
	 @param FieldOld
	 @param FieldNew
	 @param FieldNewName
	 @return 
	 * @throws Exception 
	*/
	public final String DoChangeFieldName(String fieldOld, String fieldNew, String FieldNewName, String thisFlowOnly) throws Exception
	{

		if (thisFlowOnly.equals("1"))
		{
			return DoChangeFieldNameOne(this, fieldOld, fieldNew, FieldNewName);
		}

		FlowExts fls = new FlowExts();
		fls.RetrieveAll();

		String resu = "";
		for (FlowExt item : fls.ToJavaList())
		{
			resu += "   ====   " + DoChangeFieldNameOne(item, fieldOld, fieldNew, FieldNewName);

		}
		return resu;
	}
	public final String DoChangeFieldNameOne(FlowExt flow, String fieldOld, String fieldNew, String FieldNewName) throws Exception
	{
		String result = "开始执行对字段:" + fieldOld + " ，进行重命名。";
		result += "<br> ===============================================================   ";
		Nodes nds = new Nodes(flow.getNo());
		for (Node nd : nds.ToJavaList())
		{
			result += " @ 执行节点:" + nd.getName() + " 结果如下. <br>";
			result += "<br> ------------------------------------------------------------------------ ";
			MapDataExt md = new MapDataExt("ND" + nd.getNodeID());
			result += "\t\n @" + md.DoChangeFieldName(fieldOld, fieldNew, FieldNewName);
		}

		result += "@ 执行Rpt结果如下. <br>";
		MapDataExt rptMD = new MapDataExt("ND" + Integer.parseInt(flow.getNo()) + "Rpt");
			result += "\t\n@ " + rptMD.DoChangeFieldName(fieldOld, fieldNew, FieldNewName);

		result += "@ 执行MyRpt结果如下. <br>";
		rptMD = new MapDataExt("ND" + Integer.parseInt(flow.getNo()) + "MyRpt");

		if (rptMD.Retrieve() > 0)
		{
			  result += "\t\n@ " + rptMD.DoChangeFieldName(fieldOld, fieldNew, FieldNewName);
		}

		return result;
	}
	/** 
	 字段视图
	 
	 @return 
	*/
	public final String DoFlowFields()
	{
		return "../../WF/Admin/AttrFlow/FlowFields.htm?FK_Flow=" + this.getNo();
	}
	/** 
	 与业务表数据同步
	 
	 @return 
	*/
	public final String DoDTSBTable()
	{
		return "../../Admin/AttrFlow/DTSBTable.htm?FK_Flow=" + this.getNo();
	}
	public final String DoAPI()
	{
		return "../../Admin/AttrFlow/API.htm?FK_Flow=" + this.getNo();
	}
	public final String DoAPICode()
	{
		return "../../Admin/AttrFlow/APICode.htm?FK_Flow=" + this.getNo();
	}
	public final String DoAPICodeFEE()
	{
		return "../../Admin/AttrFlow/APICodeFEE.htm?FK_Flow=" + this.getNo();
	}

		///#endregion 开发接口


		///#region  基本功能
	/** 
	 事件
	 
	 @return 
	*/
	public final String DoAction()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/AttrFlow/Action.htm?FK_Flow=" + this.getNo() + "&tk=" + new java.util.Random().nextDouble();
	}
	public final String DoDBSrc()
	{
		return Glo.getCCFlowAppPath() + "WF/Comm/Sys/SFDBSrcNewGuide.jsp";
	}
	public final String DoBTable()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/AttrFlow/DTSBTable.htm?s=d34&ShowType=FlowFrms&FK_Node=" + Integer.parseInt(this.getNo()) + "01&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=" + DataType.getCurrentDataTime();
	}
	/** 
	 批量修改节点属性
	 
	 @return 
	*/
	public final String DoNodeAttrs()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/AttrFlow/NodeAttrs.htm?NodeID=0&FK_Flow=" + this.getNo() + "&tk=" + new java.util.Random().nextDouble();
	}
	public final String DoBindFlowExt()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/Sln/BindFrms.htm?s=d34&ShowType=FlowFrms&FK_Node=0&FK_Flow=" + this.getNo() + "&ExtType=StartFlow";
	}
	/** 
	 轨迹查看权限
	 
	 @return 
	*/
	public final String DoTruckRight()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/AttrFlow/TruckViewPower.htm?FK_Flow=" + this.getNo();
	}
	/** 
	 批量发起字段
	 
	 @return 
	*/
	public final String DoBatchStartFields()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/AttrNode/BatchStartFields.jsp?s=d34&FK_Flow=" + this.getNo() + "&ExtType=StartFlow";
	}
	/** 
	 执行流程数据表与业务表数据手工同步
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoBTableDTS() throws Exception
	{
		Flow fl = new Flow(this.getNo());
		return fl.DoBTableDTS();

	}
	/** 
	 恢复已完成的流程数据到指定的节点，如果节点为0就恢复到最后一个完成的节点上去.
	 
	 @param workid 要恢复的workid
	 @param backToNodeID 恢复到的节点编号，如果是0，标示回复到流程最后一个节点上去.
	 @param note
	 @return 
	 * @throws Exception 
	*/
	public final String DoRebackFlowData(long workid, int backToNodeID, String note) throws Exception
	{
		if (note.length() <= 2)
		{
			return "请填写恢复已完成的流程原因.";
		}

		Flow fl = new Flow(this.getNo());
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
		rpt.setOID(workid);
		int i = rpt.RetrieveFromDBSources();
		if (i == 0)
		{
			throw new RuntimeException("@错误，流程数据丢失。");
		}
		if (backToNodeID == 0)
		{
			backToNodeID = rpt.getFlowEndNode();
		}

		Emp empStarter = new Emp(rpt.getFlowStarter());

		// 最后一个节点.
		Node endN = new Node(backToNodeID);
		GenerWorkFlow gwf = null;
		boolean isHaveGener = false;
		try
		{

				///#region 创建流程引擎主表数据.
			gwf = new GenerWorkFlow();
			gwf.setWorkID(workid);
			if (gwf.RetrieveFromDBSources() == 1)
			{
				isHaveGener = true;
				//判断状态
				if (gwf.getWFState() != WFState.Complete)
				{
					throw new RuntimeException("@当前工作ID为:" + workid + "的流程没有结束,不能采用此方法恢复。");
				}
			}

			gwf.setFK_Flow(this.getNo());
			gwf.setFlowName(this.getName());
			gwf.setWorkID(workid);
			gwf.setPWorkID(rpt.getPWorkID());
			gwf.setPFlowNo(rpt.getPFlowNo());
			gwf.setPNodeID(rpt.getPNodeID());
			gwf.setPEmp(rpt.getPEmp());


			gwf.setFK_Node(backToNodeID);
			gwf.setNodeName(endN.getName());

			gwf.setStarter(rpt.getFlowStarter());
			gwf.setStarterName(empStarter.getName());
			gwf.setFK_FlowSort(fl.getFK_FlowSort());
			gwf.setSysType(fl.getSysType());

			gwf.setTitle(rpt.getTitle());
			gwf.setWFState(WFState.ReturnSta); //设置为退回的状态
			gwf.setFK_Dept(rpt.getFK_Dept());

			Dept dept = new Dept(empStarter.getFK_Dept());

			gwf.setDeptName(dept.getName());
			gwf.setPRI(1);

			Date date = DateUtils.addDay(new Date(), 3);
			String dttime = DateUtils.format(date, DateUtils.YMDHMS_PATTERN);

			gwf.setSDTOfNode(dttime);
			gwf.setSDTOfFlow(dttime);
			if (isHaveGener)
			{
				gwf.Update();
			}
			else
			{
				gwf.Insert(); //插入流程引擎数据.
			}


				///#endregion 创建流程引擎主表数据
			String ndTrack = "ND" + Integer.parseInt(this.getNo()) + "Track";
			String actionType = ActionType.Forward.getValue() + "," + ActionType.FlowOver.getValue() + "," + ActionType.ForwardFL.getValue() + "," + ActionType.ForwardHL.getValue();
			String sql = "SELECT  * FROM " + ndTrack + " WHERE   ActionType IN (" + actionType + ")  and WorkID=" + workid + " ORDER BY RDT DESC, NDFrom ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@工作ID为:" + workid + "的数据不存在.");
			}

			String starter = "";
			boolean isMeetSpecNode = false;
			GenerWorkerList currWl = new GenerWorkerList();
			for (DataRow dr : dt.Rows)
			{
				int ndFrom = Integer.parseInt(dr.getValue("NDFrom").toString());
				Node nd = new Node(ndFrom);

				String ndFromT = dr.getValue("NDFromT").toString();
				String EmpFrom = dr.getValue(TrackAttr.EmpFrom).toString();
				String EmpFromT = dr.getValue(TrackAttr.EmpFromT).toString();

				// 增加上 工作人员的信息.
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.setWorkID(workid);
				gwl.setFK_Flow(this.getNo());

				gwl.setFK_Node(ndFrom);
				gwl.setFK_NodeText(ndFromT);

				if (gwl.getFK_Node() == backToNodeID)
				{
					gwl.setIsPass(false);
					currWl = gwl;
				}else
					gwl.setIsPass(true);

				gwl.setFK_Emp(EmpFrom);
				gwl.setFK_EmpText(EmpFromT);
				if (gwl.getIsExits())
				{
					continue; //有可能是反复退回的情况.
				}

				Emp emp = new Emp(gwl.getFK_Emp());
				gwl.setFK_Dept(emp.getFK_Dept());

				gwl.setRDT(dr.getValue("RDT").toString());
				gwl.setSDT(dr.getValue("RDT").toString());
				gwl.setDTOfWarning(gwf.getSDTOfNode());
				//gwl.setWarningHour(nd.getWarningHour());
				gwl.setIsEnable(true);
				gwl.setWhoExeIt(nd.getWhoExeIt());
				gwl.Insert();
			}


				///#region 加入退回信息, 让接受人能够看到退回原因.
			ReturnWork rw = new ReturnWork();
			rw.setWorkID(workid);
			rw.setReturnNode(backToNodeID);
			rw.setReturnNodeName(endN.getName());
			rw.setReturner(WebUser.getNo());
			rw.setReturnerName(WebUser.getName());

			rw.setReturnToNode(currWl.getFK_Node());
			rw.setReturnToEmp(currWl.getFK_Emp());
			rw.setBeiZhu(note);
			rw.setRDT(DataType.getCurrentDataTime());
			rw.setIsBackTracking(false);
			rw.setMyPK(BP.DA.DBAccess.GenerGUID());

				///#endregion   加入退回信息, 让接受人能够看到退回原因.

			//更新流程表的状态.
			rpt.setFlowEnder(currWl.getFK_Emp());
			rpt.setWFState(WFState.ReturnSta); //设置为退回的状态
			rpt.setFlowEndNode(currWl.getFK_Node());
			rpt.Update();

			// 向接受人发送一条消息.
			BP.WF.Dev2Interface.Port_SendMsg(currWl.getFK_Emp(), "工作恢复:" + gwf.getTitle(), "工作被:" + WebUser.getNo() + " 恢复." + note, "ReBack" + workid, BP.WF.SMSMsgType.SendSuccess, this.getNo(), Integer.parseInt(this.getNo() + "01"), workid, 0);

			//写入该日志.
			WorkNode wn = new WorkNode(workid, currWl.getFK_Node());
			wn.AddToTrack(ActionType.RebackOverFlow, currWl.getFK_Emp(), currWl.getFK_EmpText(), currWl.getFK_Node(), currWl.getFK_NodeText(), note);

			return "@已经还原成功,现在的流程已经复原到(" + currWl.getFK_NodeText() + "). @当前工作处理人为(" + currWl.getFK_Emp() + " , " + currWl.getFK_EmpText() + ")  @请通知他处理工作.";
		}
		catch (RuntimeException ex)
		{
			//此表的记录删除已取消
			//gwf.Delete();
			GenerWorkerList wl = new GenerWorkerList();
			wl.Delete(GenerWorkerListAttr.WorkID, workid);

			String sqls = "";
			sqls += "@UPDATE " + fl.getPTable() + " SET WFState=" + WFState.Complete.getValue() + " WHERE OID=" + workid;
			DBAccess.RunSQLs(sqls);
			return "<font color=red>会滚期间出现错误</font><hr>" + ex.getMessage();
		}
	}
	/** 
	 重新产生标题，根据新的规则.
	 * @throws Exception 
	 
	*/
	public final String DoGenerFlowEmps() throws Exception
	{
		if ( ! WebUser.getNo().equals("admin"))
		{
			return "非admin用户不能执行。";
		}

		Flow fl = new Flow(this.getNo());

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.getNo());

		for (GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			String emps = "";
			String sql = "SELECT EmpFrom FROM ND" + Integer.parseInt(this.getNo()) + "Track  WHERE WorkID=" + gwf.getWorkID();

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				if (emps.contains("," + dr.getValue(0).toString() + ","))
				{
					continue;
				}
			}

			sql = "UPDATE " + fl.getPTable() + " SET FlowEmps='" + emps + "' WHERE OID=" + gwf.getWorkID();
			DBAccess.RunSQL(sql);

			sql = "UPDATE WF_GenerWorkFlow SET Emps='" + emps + "' WHERE WorkID=" + gwf.getWorkID();
			DBAccess.RunSQL(sql);
		}

		Node nd = fl.getHisStartNode();
		Works wks = nd.getHisWorks();
		wks.RetrieveAllFromDBSource(WorkAttr.Rec);
		String table = nd.getHisWork().getEnMap().getPhysicsTable();
		String tableRpt = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		MapData md = new MapData(tableRpt);
		for (Work wk : wks.ToJavaList())
		{
			if (!wk.getRec().equals(WebUser.getNo()))
			{
				BP.Web.WebUser.Exit();
				try
				{
					Emp emp = new Emp(wk.getRec());
					BP.Web.WebUser.SignInOfGener(emp);
				}
				catch (java.lang.Exception e)
				{
					continue;
				}
			}
			String sql = "";
			String title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);
			Paras ps = new Paras();
			ps.Add("Title", title);
			ps.Add("OID", wk.getOID());
			ps.SQL = "UPDATE " + table + " SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE " + md.getPTable() + " SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			 
		}
		Emp emp1 = new Emp("admin");
		BP.Web.WebUser.SignInOfGener(emp1);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}

	/** 
	 重新产生标题，根据新的规则.
	 * @throws Exception 
	 
	*/
	public final String DoGenerTitle() throws Exception
	{
		if ( ! WebUser.getNo().equals("admin"))
		{
			return "非admin用户不能执行。";
		}
		Flow fl = new Flow(this.getNo());
		Node nd = fl.getHisStartNode();
		Works wks = nd.getHisWorks();
		wks.RetrieveAllFromDBSource(WorkAttr.Rec);
		String table = nd.getHisWork().getEnMap().getPhysicsTable();
		String tableRpt = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		MapData md = new MapData(tableRpt);
		for (Work wk : wks.ToJavaList())
		{

			if (!wk.getRec().equals(WebUser.getNo()))
			{
				BP.Web.WebUser.Exit();
				try
				{
					Emp emp = new Emp(wk.getRec());
					BP.Web.WebUser.SignInOfGener(emp);
				}
				catch (java.lang.Exception e)
				{
					continue;
				}
			}
			String sql = "";
			String title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);
			Paras ps = new Paras();
			ps.Add("Title", title);
			ps.Add("OID", wk.getOID());
			ps.SQL = "UPDATE " + table + " SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE " + md.getPTable() + " SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

		 
		}
		Emp emp1 = new Emp("admin");
		BP.Web.WebUser.SignInOfGener(emp1);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}
	/** 
	 流程监控
	 
	 @return 
	*/
	public final String DoDataManger()
	{
		return "../../WF/Comm/Search.htm?s=d34&EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=";
	}
	/** 
	 绑定独立表单
	 
	 @return 
	*/
	public final String DoFlowFormTree()
	{
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),SystemConfig.getCCFlowWebPath() +"WF/Admin/FlowFormTree.jsp?s=d34&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=" + DataType.getCurrentDataTime(), 700, 500);
		} catch (IOException e) {
			Log.DebugWriteError("FlowExt DoFlowFormTree()" + e);
		}
		return null;
	}
	/** 
	 定义报表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoAutoStartIt() throws Exception
	{
		Flow fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		return fl.DoAutoStartIt();
	}
	/** 
	 删除流程
	 
	 @param workid
	 @param sd
	 @return 
	 * @throws Exception 
	*/
	public final String DoDelDataOne(int workid, String note) throws Exception
	{
		try
		{
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getNo(), workid, true);
			return "删除成功 workid=" + workid + "  理由:" + note;
		}
		catch (RuntimeException ex)
		{
			return "删除失败:" + ex.getMessage();
		}
	}
	/** 
	 设置发起数据源
	 
	 @return 
	*/
	public final String DoSetStartFlowDataSources()
	{
		String flowID = Integer.parseInt(this.getNo()) + "01";
		return "../../Admin/AttrFlow/AutoStart.htm?s=d34&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=";
		//return Glo.CCFlowAppPath + "WF/Admin/FoolFormDesigner/MapExt.jsp?s=d34&FK_MapData=ND" + flowID + "&ExtType=StartFlow&RefNo=";
	}
	public final String DoCCNode()
	{
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),SystemConfig.getCCFlowWebPath() + "WF/Admin/CCNode.jsp?FK_Flow=" + this.getNo(), 400, 500);
		} catch (IOException e) {
			Log.DebugWriteError("FlowExt DoCCNode()" + e);
		}
		return null;
	}
	/** 
	 执行运行
	 @return 
	*/
	public final String DoRunIt()
	{
		return  "../../Admin/TestFlow.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 执行检查
	 @return 
	*/
	public final String DoCheck()
	{
		return  "../../Admin/AttrFlow/CheckFlow.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}

	/** 
	 启动限制规则
	 @return 返回URL
	*/
	public final String DoLimit()
	{
		return   "../../Admin/AttrFlow/Limit.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 设置发起前置导航
	 @return 
	*/
	public final String DoStartGuide()
	{
		return   "../../Admin/AttrFlow/StartGuide.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 执行数据同步
	 @return 
	*/
	public final String DoDTS()
	{
		return   "../../Admin/AttrFlow/DTSBTable.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 导入
	 @return 
	*/
	public final String DoImp()
	{
		return "../../Admin/AttrFlow/Imp.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 导出
	 @return 
	*/
	public final String DoExps()
	{
		return  "../../Admin/AttrFlow/Exp.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/**
	 * 版本管理
	 * @return
	 */
	public String DoVer()
    {
        return "../../Admin/AttrFlow/Ver.htm?FK_Flow=" + this.getNo();
    }
	/** 
	 执行重新装载数据
	 @return 
	 * @throws Exception 
	*/
	public final String DoReloadRptData() throws Exception
	{
		Flow fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		return fl.DoReloadRptData();
	}
	/** 
	 删除数据.
	 @return 
	 * @throws Exception 
	*/
	public final String DoDelData() throws Exception
	{
		Flow fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		return fl.DoDelData();
	}
	/** 
	 运行报表
	 @return 
	*/
	public final String DoOpenRpt()
	{
		return "../../Admin/RptDfine/Default.htm?FK_Flow=" + this.getNo() + "&DoType=Edit&FK_MapData=ND" + Integer.parseInt(this.getNo()) + "Rpt";
	}
	/** 
	 更新之后的事情，也要更新缓存。
	*/
	@Override
	protected void afterUpdate()
	{
		// Flow fl = new Flow();
		// fl.No = this.getNo();
		// fl.RetrieveFromDBSources();
		//fl.Update();

		if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
		{
		   // DBAccess.RunSQL("UPDATE  GPM_Menu SET Name='" + this.Name + "' WHERE Flag='Flow" + this.getNo() + "' AND FK_App='" + Glo.getCCFlowAppPath() + "'");
		}
	}
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		//更新流程版本
		Flow.UpdateVer(this.getNo());


		///#region 校验 flowmark 是不是唯一.
		if (this.getFlowMark().length() > 0)
		{
			//如果是集团模式，则判断机构下的流程标记不可重复
			if(Glo.getIsUnit() == true)
			{
				FlowSort flSort = new FlowSort(this.getFK_FlowSort());
                FlowSorts flowSorts = new FlowSorts();
                //同一机构下不允许标记重复
                flowSorts.RetrieveByAttr(FlowSortAttr.OrgNo, flSort.getOrgNo());
                for(FlowSort mySort : flowSorts.ToJavaListFs())
                {
					//校验该标记是否重复.
					Flows fls = new Flows();
					fls.Retrieve(FlowAttr.FK_FlowSort,mySort.getNo(),FlowAttr.FlowMark, this.getFlowMark());
					for (Flow myfl : fls.ToJavaList())
					{
						if (!myfl.getNo().equals(this.getNo()))
						{
							throw new RuntimeException("@该流程标记{" + this.getFlowMark() + "}已经存在.");
						}
					}
                }
			}
		}
		
		try
		{
			
			String fee=BP.WF.Glo.GetFlowEventEntityStringByFlowMark(this.getFlowMark(), this.getNo());
			
			this.setFlowEventEntity(fee);
			
		}
		catch (java.lang.Exception e)
		{
			this.setFlowEventEntity("");
		}

			///#endregion 同步事件实体.

		//更新缓存数据。
		Flow fl = new Flow(this.getNo());
		fl.Copy(this);


			///#region 检查数据完整性 - 同步业务表数据。
		// 检查业务是否存在.
		if (fl.getDTSWay() != FlowDTSWay.None)
		{
			//检查业务表填写的是否正确.
			String sql = "select count(*) as Num from  " + fl.getDTSBTable() + " where 1=2";
			try
			{
				DBAccess.RunSQLReturnValInt(sql, 0);
			}
			catch (RuntimeException e2)
			{
				throw new RuntimeException("@业务表配置无效，您配置业务数据表[" + fl.getDTSBTable() + "]在数据中不存在，请检查拼写错误如果是跨数据库请加上用户名比如: for sqlserver: HR.dbo.Emps, For oracle: HR.Emps");
			}

			sql = "select " + fl.getDTSBTablePK() + " from " + fl.getDTSBTable() + " where 1=2";
			try
			{
				DBAccess.RunSQLReturnValInt(sql, 0);
			}
			catch (RuntimeException e3)
			{
				throw new RuntimeException("@业务表配置无效，您配置业务数据表[" + fl.getDTSBTablePK() + "]的主键不存在。");
			}


			//检查节点配置是否符合要求.
			if (fl.getDTSTime() == FlowDTSTime.SpecNodeSend)
			{
				// 检查节点ID，是否符合格式.
				String nodes = fl.getDTSSpecNodes();
				nodes = nodes.replace("，", ",");
				this.SetValByKey(FlowAttr.DTSSpecNodes, nodes);

				if (StringHelper.isNullOrEmpty(nodes) == true)
				{
					throw new RuntimeException("@业务数据同步数据配置错误，您设置了按照指定的节点配置，但是您没有设置节点,节点的设置格式如下：101,102,103");
				}

				String[] strs = nodes.split("[,]", -1);
				for (String str : strs)
				{
					if (StringHelper.isNullOrEmpty(str) == true)
					{
						continue;
					}

					if (BP.DA.DataType.IsNumStr(str) == false)
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置了按照指定的节点配置，但是节点格式错误[" + nodes + "]。正确的格式如下：101,102,103");
					}

					Node nd = new Node();
					nd.setNodeID(Integer.parseInt(str));
					if (nd.getIsExits() == false)
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置的节点格式错误，节点[" + str + "]不是有效的节点。");
					}

					nd.RetrieveFromDBSources();
					if (!nd.getFK_Flow().equals(this.getNo()))
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置的节点[" + str + "]不再本流程内。");
					}
				}
			}

			//检查流程数据存储表是否正确
			if (!StringHelper.isNullOrEmpty(fl.getPTable()))
			{
				//检查流程数据存储表填写的是否正确.
				sql = "select count(*) as Num from  " + fl.getPTable() + " where 1=2";
				try
				{
					DBAccess.RunSQLReturnValInt(sql, 0);
				}
				catch (RuntimeException e4)
				{
					throw new RuntimeException("@流程数据存储表配置无效，您配置流程数据存储表[" + fl.getPTable() + "]在数据中不存在，请检查拼写错误如果是跨数据库请加上用户名比如: for sqlserver: HR.dbo.Emps, For oracle: HR.Emps");
				}
			}
		}
		return super.beforeUpdate();
	}
	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		//同步流程数据表.
		String ndxxRpt = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		Flow fl = new Flow(this.getNo());
		if (!fl.getPTable().equals("ND" + Integer.parseInt(this.getNo()) + "Rpt"))
		{
			BP.Sys.MapData md = new MapData(ndxxRpt);
			if (!fl.getPTable().equals(md.getPTable()))
			{
				md.Update();
			}
		}


	    ///#region 为systype设置，当前所在节点的第2级别目录。
		FlowSort fs = new FlowSort(fl.getFK_FlowSort());
		if (fs.getParentNo().equals("99") || fs.getParentNo().equals("0"))
		{
			this.setSysType(fl.getFK_FlowSort());
		}
		else
		{
			FlowSort fsP = new FlowSort(fs.getParentNo());
			if (fsP.getParentNo().equals("99") || fsP.getParentNo().equals("0"))
			{
				this.setSysType(fsP.getNo());
			}
			else
			{
				FlowSort fsPP = new FlowSort(fsP.getParentNo());
				this.setSysType(fsPP.getNo());
			}
		}

		super.afterInsertUpdateAction();
	}
	 /** 
	 删除多余的字段.
	 
	 @return 
*/
	public final String DoDeleteFields()
	{
		return "尚未完成.";
	}
	/** 
	 删除多余的字段.
	 
	 @return 
	*/
	public final String DoDeleteFieldsIsNull()
	{
		return "尚未完成.";
	}
	/** 
	 一件设置审核模式.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSetFWCModel() throws Exception
	{
		Nodes nds = new Nodes(this.getNo());

		for (Node nd : nds.ToJavaList())
		{
			if (nd.getIsStartNode())
			{
				continue;
			}

			BP.WF.Template.FrmNodeComponent fnd = new FrmNodeComponent(nd.getNodeID());

			if (nd.getIsEndNode() == true)
			{
				nd.setFrmWorkCheckSta(FrmWorkCheckSta.Readonly);
				nd.setNodeFrmID("ND" + Integer.parseInt(this.getNo()) + "02");
				nd.Update();

				fnd.Update();
				continue;
			}

			fnd.Update(); //不执行更新，会导致部分字段错误.


			nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
			nd.setNodeFrmID("ND" + Integer.parseInt(this.getNo()) + "02");
			nd.Update();
		}

		return "设置成功...";
	}
}
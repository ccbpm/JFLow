package BP.WF.Template;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 流程
*/
public class FlowExt extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
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
	 系统类别（第2级流程树节点编号）
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
	 是否可以独立启动
	*/
	public final boolean getIsCanStart()
	{
		return this.GetValBooleanByKey(FlowAttr.IsCanStart);
	}
	public final void setIsCanStart(boolean value)
	{
		this.SetValByKey(FlowAttr.IsCanStart, value);
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
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return this.No;
		}
		return str;
	}
	public final void setFlowMark(String value)
	{
		this.SetValByKey(FlowAttr.FlowMark, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	/** 
	 是否可以在手机里启用
	*/
	public final boolean getIsStartInMobile()
	{
		return this.GetValBooleanByKey(FlowAttr.IsStartInMobile);
	}
	public final void setIsStartInMobile(boolean value)
	{
		this.SetValByKey(FlowAttr.IsStartInMobile, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	 设计时间
	*/
	public final String getDesignTime()
	{
		return this.GetValStringByKey(FlowAttr.DesignTime);
	}
	public final void setDesignTime(String value)
	{
		this.SetValByKey(FlowAttr.DesignTime, value);
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
	/** 
	 测试人员
	*/
	public final String getTester()
	{
		return this.GetValStringByKey(FlowAttr.Tester);
	}
	public final void setTester(String value)
	{
		this.SetValByKey(FlowAttr.Tester, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin") || this.getDesignerNo().equals(WebUser.getNo()))
		{
			uac.IsUpdate = true;
		}
		else
		{
			uac.IsUpdate = true;
		}
		return uac;
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
	*/
	public FlowExt(String _No)
	{
		this.No = _No;
		if (SystemConfig.IsDebug)
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性。
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

			//map.AddDDLSysEnum(FlowAttr.IsAutoSendSubFlowOver, 0, "为子流程时结束规则", true, true,
			// FlowAttr.IsAutoSendSubFlowOver, "@0=不处理@1=让父流程自动运行下一步@2=结束父流程");

		map.AddBoolean(FlowAttr.IsGuestFlow, false, "是否外部用户参与流程(非组织结构人员参与的流程)", true, true, false);
		map.SetHelperUrl(FlowAttr.IsGuestFlow, "http://ccbpm.mydoc.io/?v=5404&t=17039");

		map.AddDDLSysEnum(FlowAttr.FlowAppType, FlowAppType.Normal.getValue(), "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
		map.SetHelperUrl(FlowAttr.FlowAppType, "http://ccbpm.mydoc.io/?v=5404&t=17035");


			//map.AddDDLSysEnum(FlowAttr.SDTOfFlow, (int)TimelineRole.ByNodeSet, "时效性规则",
			// true, true, FlowAttr.SDTOfFlow, "@0=按节点(由节点属性来定义)@1=按发起人(开始节点SysSDTOfFlow字段计算)");
			//map.SetHelperUrl(FlowAttr.TimelineRole, "http://ccbpm.mydoc.io/?v=5404&t=17036");

			// 草稿
		map.AddDDLSysEnum(FlowAttr.Draft, DraftRole.None.getValue(), "草稿规则", true, true, FlowAttr.Draft, "@0=无(不设草稿)@1=保存到待办@2=保存到草稿箱");
		map.SetHelperUrl(FlowAttr.Draft, "http://ccbpm.mydoc.io/?v=5404&t=17037");

			// add for 华夏银行.
		map.AddDDLSysEnum(FlowAttr.FlowDeleteRole, FlowDeleteRole.AdminOnly.getValue(), "流程实例删除规则", true, true, FlowAttr.FlowDeleteRole, "@0=超级管理员可以删除@1=分级管理员可以删除@2=发起人可以删除@3=节点启动删除按钮的操作员");

			//子流程结束时，让父流程自动运行到下一步
		map.AddBoolean(FlowAttr.IsToParentNextNode, false, "子流程结束时，让父流程自动运行到下一步", true, true);

		map.AddDDLSysEnum(FlowAttr.FlowAppType, FlowAppType.Normal.getValue(), "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
		map.AddTBString(FlowAttr.HelpUrl, null, "帮助文档", true, false, 0, 300, 10, true);


			//为 莲荷科技增加一个系统类型, 用于存储当前所在流程树的第2级流程树编号.
		map.AddTBString(FlowAttr.SysType, null, "系统类型", false, false, 0, 100, 10, false);
		map.AddTBString(FlowAttr.Tester, null, "发起测试人", true, false, 0, 300, 10, true);

		String sql = "SELECT No,Name FROM Sys_EnumMain WHERE No LIKE 'Flow_%' ";
		map.AddDDLSQL("NodeAppType", null, "业务类型枚举(可为Null)", sql, true);

			// add 2014-10-19.
		map.AddDDLSysEnum(FlowAttr.ChartType, FlowChartType.Icon.getValue(), "节点图形类型", true, true, "ChartType", "@0=几何图形@1=肖像图片");

		map.AddTBString(FlowAttr.HostRun, null, "运行主机(IP+端口)", true, false, 0, 40, 10, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 表单数据.

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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 表单数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 开发者信息.

		map.AddTBString(FlowAttr.DesignerNo, null, "设计者编号", true, true, 0, 50, 10, false);
		map.AddTBString(FlowAttr.DesignerName, null, "设计者名称", true, true, 0, 50, 10, false);
		map.AddTBString(FlowAttr.DesignTime, null, "创建时间", true, true, 0, 50, 20, false);
		map.AddTBStringDoc(FlowAttr.Note, null, "流程描述", true, false, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 开发者信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本功能.
			//map.AddRefMethod(rm);
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "设计报表"; // "报表运行";
		rm.Icon = "../../WF/Img/Btn/Rpt.gif";
		rm.ClassMethodName = this.toString() + ".DoOpenRpt()";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "自动发起";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/AutoStart.png";
		rm.ClassMethodName = this.toString() + ".DoSetStartFlowDataSources()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发起限制规则";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Limit.png";
		rm.ClassMethodName = this.toString() + ".DoLimit()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发起前置导航";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/StartGuide.png";
		rm.ClassMethodName = this.toString() + ".DoStartGuide()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发起前置导航(实验中)";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/StartGuide.png";
		rm.ClassMethodName = this.toString() + ".DoStartGuideV2019()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "流程事件"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon = "../../WF/Img/Event.png";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoMessage";
		rm.Icon = "../../WF/Img/Message24.png";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "流程计划时间计算规则"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoSDTOfFlow";
			//rm.Icon = "../../WF/Img/Event.png";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "修改ICON"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoNodesICON";
			//  rm.Icon = "../../WF/Img/Event.png";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "版本管理";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoVer()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
			// rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "权限控制";
		   // rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoPowerModel()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
			// rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);



			//rm = new RefMethod();
			//rm.Title = "与业务表数据同步"; // "抄送规则";
			//rm.ClassMethodName = this.ToString() + ".DoBTable";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/DTS.png";
			//rm.RefAttrLinkLabel = "业务表字段同步配置";
			//rm.RefMethodType = RefMethodType.LinkeWinOpen;
			//rm.Target = "_blank";
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "独立表单树";
			//rm.Icon = "../../WF/Img/Btn/DTS.gif";
			//rm.ClassMethodName = this.ToString() + ".DoFlowFormTree()";
			//map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 流程设置.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 时限规则
		rm = new RefMethod();
		rm.GroupName = "时限规则";
		rm.Title = "时限规则";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/CH.png";
		rm.ClassMethodName = this.toString() + ".DoDeadLineRole()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
			// rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "时限规则";
		rm.Title = "预警、超期消息事件";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/OvertimeRole.png";
		rm.ClassMethodName = this.toString() + ".DoOverDeadLineRole()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
			// rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 时限规则

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 模拟测试.
		rm = new RefMethod();
		rm.GroupName = "模拟测试";
		rm.Title = "调试运行"; // "设计检查报告";
			//rm.ToolTip = "检查流程设计的问题。";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/Run.png";
		rm.ClassMethodName = this.toString() + ".DoRunIt";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "模拟测试";
		rm.Title = "检查报告"; // "设计检查报告";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/CheckRpt.png";
		rm.ClassMethodName = this.toString() + ".DoCheck2018Url";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.GroupName = "模拟测试";
		rm.Title = "检查报告(旧)"; // "设计检查报告";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/CheckRpt.png";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 模拟测试.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 流程模版管理.
		rm = new RefMethod();
		rm.Title = "模版导入";
		rm.Icon = "../../WF/Img/redo.png";
		rm.ClassMethodName = this.toString() + ".DoImp()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程模版";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "模版导出";
		rm.Icon = "../../WF/Img/undo.png";
		rm.ClassMethodName = this.toString() + ".DoExps()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程模版";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 流程模版管理.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 开发接口.
		rm = new RefMethod();
		rm.Title = "与业务表数据同步";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/DTS.png";

		rm.ClassMethodName = this.toString() + ".DoDTSBTable()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "URL调用接口";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/URL.png";
		rm.ClassMethodName = this.toString() + ".DoAPI()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "SDK开发接口";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/API.png";
		rm.ClassMethodName = this.toString() + ".DoAPICode()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "代码事件开发接口";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/API.png";
		rm.ClassMethodName = this.toString() + ".DoAPICodeFEE()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程属性自定义";
		rm.ClassMethodName = this.toString() + ".DoFlowAttrExt()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 开发接口.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		rm.RefMethodType = RefMethodType.Func;
		rm.Target = "_blank";
		rm.Warning = "您确定要根据新的规则重新产生标题吗？";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重生成FlowEmps字段";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoGenerFlowEmps()";
		rm.RefAttrLinkLabel = "补充修复emps字段，包括wf_generworkflow,NDxxxRpt字段.";
		rm.RefMethodType = RefMethodType.Func;
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
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Field.png";
		rm.ClassMethodName = this.toString() + ".DoFlowFields()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "删除该流程全部数据"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.Warning = "您确定要执行删除流程数据吗? \t\n该流程的数据删除后，就不能恢复，请注意删除的内容。"; // "您确定要执行删除流程数据吗？";
		rm.ClassMethodName = this.toString() + ".DoDelData";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);


			//带有参数的方法.
		rm = new RefMethod();
		rm.GroupName = "流程维护";
		rm.Title = "删除指定日期范围内的流程";
		rm.Warning = "您确定要删除吗？";
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.getHisAttrs().AddTBDateTime("DTFrom", null, "时间从", true, true);
		rm.getHisAttrs().AddTBDateTime("DTTo", null, "时间到", true, true);
		rm.getHisAttrs().AddBoolen("thisFlowOnly", true, "仅仅当前流程");
		rm.ClassMethodName = this.toString() + ".DoDelFlows";
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
		rm.Title = "强制设置接收人";
		rm.getHisAttrs().AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.getHisAttrs().AddTBInt("NodeID", 0, "节点ID", true, false);
		rm.getHisAttrs().AddTBString("Worker", null, "接受人编号", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoSetTodoEmps";
		map.AddRefMethod(rm);





		rm = new RefMethod();
			//   rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "按工作ID强制结束"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.GroupName = "流程维护";
		rm.ClassMethodName = this.toString() + ".DoStopFlow";
		rm.getHisAttrs().AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.getHisAttrs().AddTBString("beizhu", null, "备注", true, false, 0, 100, 100);
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 流程运行维护.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 流程监控.

			//rm = new RefMethod();
			//rm.Title = "监控面板";
			//rm.Icon = ../../Admin/CCBPMDesigner/Img/Monitor.png";
			//rm.ClassMethodName = this.ToString() + ".DoDataManger_Welcome()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.GroupName = "流程监控";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "综合查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Search()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "综合分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Group()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "实例增长分析";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Grow.png";
			//rm.ClassMethodName = this.ToString() + ".DoDataManger_InstanceGrowOneFlow()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.GroupName = "流程监控";
			//rm.Visable = false;
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "逾期未完成实例";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Warning.png";
			//rm.ClassMethodName = this.ToString() + ".DoDataManger_InstanceWarning()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.GroupName = "流程监控";
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "逾期已完成实例";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/overtime.png";
			//rm.ClassMethodName = this.ToString() + ".DoDataManger_InstanceOverTimeOneFlow()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.Visable = false;
			//rm.GroupName = "流程监控";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除日志";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/log.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_DeleteLog()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "流程监控";
		map.AddRefMethod(rm);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 流程监控.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 实验中的功能
		rm = new RefMethod();
		rm.Title = "数据订阅-实验中";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/RptOrder.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_RptOrder()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		rm.Visable = false;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程轨迹表单";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoBindFlowExt()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量设置节点";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoNodeAttrs()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "轨迹查看权限";
		rm.Icon = "../../WF/Img/Setting.png";
		rm.ClassMethodName = this.toString() + ".DoTruckRight()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "数据源管理(如果新增数据源后需要关闭重新打开)";
		rm.ClassMethodName = this.toString() + ".DoDBSrc";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSDBSrc;
		rm.RefAttrLinkLabel = "数据源管理";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "一键设置审核组件工作模式";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.RefMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 第2个节点以后的节点表单都指向第2个节点表单.  \t\n  2, 结束节点都设置为只读模式. ";
		rm.GroupName = "实验中的功能";
		rm.ClassMethodName = this.toString() + ".DoSetFWCModel()";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除NDxxxRpt表,多余字段.";
		rm.ClassMethodName = this.toString() + ".DoDeleteFields()";
		rm.RefMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 表NDxxxRpt是自动创建的.  \t\n  2, 在设置流程过程中有些多余的字段会生成到NDxxRpt表里. \t\n 3,这里是删除数据字段为null 并且是多余的字段.";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除NDxxxRpt表,数据为null的多余字段.";
		rm.ClassMethodName = this.toString() + ".DoDeleteFieldsIsNull()";
		rm.RefMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 表NDxxxRpt是自动创建的.  \t\n  2, 在设置流程过程中有些多余的字段会生成到NDxxxRpt表里. \t\n 3,这里是删除数据字段为null 并且是多余的字段.";
		rm.GroupName = "实验中的功能";
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 实验中的功能

			//rm = new RefMethod();
			//rm.Title = "执行流程数据表与业务表数据手工同步"; 
			//rm.ClassMethodName = this.ToString() + ".DoBTableDTS";
			//rm.Icon = ../../Img/Btn/DTS.gif";
			//rm.Warning = "您确定要执行吗？如果执行了可能会对业务表数据造成错误。";
			////设置相关字段.
			//rm.RefAttrKey = FlowAttr.DTSSpecNodes;
			//rm.RefAttrLinkLabel = "业务表字段同步配置";
			//rm.RefMethodType = RefMethodType.Func;
			//rm.Target = "_blank";
			////map.AddRefMethod(rm);

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
			// rm.Icon = "/WF/Img/Btn/Table.gif";
			//rm.ToolTip = "在流程完成时间，流程数据转储存到其它系统中应用。";
			//rm.ClassMethodName = this.ToString() + ".DoExp";
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 流程监控.


	public final String DoDataManger_Search()
	{
		return "../../Comm/Search.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow= " + this.getNo()+ " &WFSta=all";
	}
	public final String DoDataManger_Group()
	{
		return "../../Comm/Group.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow= " + this.getNo()+ " &WFSta=all";
	}


	public final String DoDataManger_DeleteLog()
	{
		return "../../Comm/Search.htm?EnsName=BP.WF.WorkFlowDeleteLogs&FK_Flow= " + this.getNo()+ " &WFSta=all";
	}

	/** 
	 数据订阅
	 
	 @return 
	*/
	public final String DoDataManger_RptOrder()
	{
		return "../../Admin/CCBPMDesigner/App/RptOrder.aspx?anaTime=mouth&FK_Flow=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 流程监控.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 开发接口.
	/** 
	 执行删除指定日期范围内的流程
	 
	 @param dtFrom 日期从
	 @param dtTo 日期到
	 @param isOk 仅仅删除当前流程？1=删除当前流程, 0=删除全部流程.
	 @return 
	*/
	public final String DoDelFlows(String dtFrom, String dtTo, String isDelCurrFlow)
	{
		if (!WebUser.getNo().equals("admin"))
		{
			return "非admin用户，不能删除。";
		}

		String sql = "";
		if (isDelCurrFlow.equals("1"))
		{
			sql = "SELECT WorkID, FK_Flow FROM WF_GenerWorkFlow  WHERE RDT >= '" + dtFrom + "' AND RDT <= '" + dtTo + "'  AND FK_Flow=' " + this.getNo()+ " ' ";
		}
		else
		{
			sql = "SELECT WorkID, FK_Flow FROM WF_GenerWorkFlow  WHERE RDT >= '" + dtFrom + "' AND RDT <= '" + dtTo + "' ";
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

		String msg = "如下流程ID被删除:";
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.get("WorkID").toString());
			String fk_flow = dr.get("FK_Flow").toString();
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
	*/
	public final String DoChangeFieldName(String fieldOld, String fieldNew, String FieldNewName, String thisFlowOnly)
	{

		if (thisFlowOnly.equals("1"))
		{
			return DoChangeFieldNameOne(this, fieldOld, fieldNew, FieldNewName);
		}

		FlowExts fls = new FlowExts();
		fls.RetrieveAll();

		String resu = "";
		for (FlowExt item : fls)
		{
			resu += "   ====   " + DoChangeFieldNameOne(item, fieldOld, fieldNew, FieldNewName);

		}
		return resu;
	}
	public final String DoChangeFieldNameOne(FlowExt flow, String fieldOld, String fieldNew, String FieldNewName)
	{
		String result = "开始执行对字段:" + fieldOld + " ，进行重命名。";
		result += "<br> ===============================================================   ";
		Nodes nds = new Nodes(flow.No);
		for (Node nd : nds.ToJavaList())
		{
			result += " @ 执行节点:" + nd.getName() + " 结果如下. <br>";
			result += "<br> ------------------------------------------------------------------------ ";
			MapDataExt md = new MapDataExt("ND" + nd.getNodeID());
			result += "\t\n @" + md.DoChangeFieldName(fieldOld, fieldNew, FieldNewName);
		}

		result += "@ 执行Rpt结果如下. <br>";
		MapDataExt rptMD = new MapDataExt("ND" + Integer.parseInt(flow.No) + "Rpt");
		result += "\t\n@ " + rptMD.DoChangeFieldName(fieldOld, fieldNew, FieldNewName);

		result += "@ 执行MyRpt结果如下. <br>";
		rptMD = new MapDataExt("ND" + Integer.parseInt(flow.No) + "MyRpt");

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
		return "../../Admin/AttrFlow/FlowFields.htm?FK_Flow=" + this.No;
	}
	/** 
	 与业务表数据同步
	 
	 @return 
	*/
	public final String DoDTSBTable()
	{
		return "../../Admin/AttrFlow/DTSBTable.htm?FK_Flow=" + this.No;
	}
	public final String DoAPI()
	{
		return "../../Admin/AttrFlow/API.htm?FK_Flow=" + this.No;
	}
	public final String DoAPICode()
	{
		return "../../Admin/AttrFlow/APICode.htm?FK_Flow=" + this.No;
	}
	public final String DoAPICodeFEE()
	{
		return "../../Admin/AttrFlow/APICodeFEE.htm?FK_Flow=" + this.No;
	}
	/** 
	 流程属性自定义
	 
	 @return 
	*/
	public final String DoFlowAttrExt()
	{
		return "../../../DataUser/OverrideFiles/FlowAttrExt.htm?FK_Flow=" + this.No;
	}
	public final String DoVer()
	{
		return "../../Admin/AttrFlow/Ver.htm?FK_Flow=" + this.No;
	}
	public final String DoPowerModel()
	{
		return "../../Admin/AttrFlow/PowerModel.htm?FK_Flow=" + this.No;
	}

	/** 
	 时限规则
	 
	 @return 
	*/
	public final String DoDeadLineRole()
	{
		return "../../Admin/AttrFlow/DeadLineRole.htm?FK_Flow=" + this.No;
	}

	/** 
	 预警、超期规则
	 
	 @return 
	*/
	public final String DoOverDeadLineRole()
	{
		return "../../Admin/AttrFlow/PushMessage.htm?FK_Flow=" + this.No;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 开发接口

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  基本功能
	/** 
	 事件
	 
	 @return 
	*/
	public final String DoAction()
	{
		return "../../Admin/AttrFlow/Action.htm?FK_Flow= " + this.getNo()+ " &tk=" + (new Random()).nextDouble();
	}
	/** 
	 流程事件
	 
	 @return 
	*/
	public final String DoMessage()
	{
		return "../../Admin/AttrFlow/PushMessage.htm?FK_Node=0&FK_Flow= " + this.getNo()+ " &tk=" + (new Random()).nextDouble();
	}
	/** 
	 计划玩成
	 
	 @return 
	*/
	public final String DoSDTOfFlow()
	{
		return "../../Admin/AttrFlow/SDTOfFlow.htm?FK_Flow= " + this.getNo()+ " &tk=" + (new Random()).nextDouble();
	}
	/** 
	 节点标签
	 
	 @return 
	*/
	public final String DoNodesICON()
	{
		return "../../Admin/AttrFlow/NodesIcon.htm?FK_Flow= " + this.getNo()+ " &tk=" + (new Random()).nextDouble();
	}
	public final String DoDBSrc()
	{
		return "../../Comm/Sys/SFDBSrcNewGuide.htm";
	}
	public final String DoBTable()
	{
		return "../../Admin/AttrFlow/DTSBTable.aspx?s=d34&ShowType=FlowFrms&FK_Node=" + Integer.parseInt(this.No) + "01&FK_Flow= " + this.getNo()+ " &ExtType=StartFlow&RefNo=" + DataType.getCurrentDataTime();
	}

	/** 
	 批量修改节点属性
	 
	 @return 
	*/
	public final String DoNodeAttrs()
	{
		return "../../Admin/AttrFlow/NodeAttrs.htm?NodeID=0&FK_Flow= " + this.getNo()+ " &tk=" + (new Random()).nextDouble();
	}
	public final String DoBindFlowExt()
	{
		return "../../Admin/Sln/BindFrms.htm?s=d34&ShowType=FlowFrms&FK_Node=0&FK_Flow= " + this.getNo()+ " &ExtType=StartFlow";
	}
	/** 
	 轨迹查看权限
	 
	 @return 
	*/
	public final String DoTruckRight()
	{
		return "../../Admin/AttrFlow/TruckViewPower.htm?FK_Flow=" + this.No;
	}
	/** 
	 批量发起字段
	 
	 @return 
	*/
	public final String DoBatchStartFields()
	{
		return "../../Admin/AttrNode/BatchStartFields.htm?s=d34&FK_Flow= " + this.getNo()+ " &ExtType=StartFlow";
	}
	/** 
	 执行流程数据表与业务表数据手工同步
	 
	 @return 
	*/
	public final String DoBTableDTS()
	{
		Flow fl = new Flow(this.No);
		return fl.DoBTableDTS();

	}
	/** 
	 恢复已完成的流程数据到指定的节点，如果节点为0就恢复到最后一个完成的节点上去.
	 
	 @param workid 要恢复的workid
	 @param backToNodeID 恢复到的节点编号，如果是0，标示回复到流程最后一个节点上去.
	 @param note
	 @return 
	*/
	public final String DoRebackFlowData(long workid, int backToNodeID, String note)
	{
		if (note.length() <= 2)
		{
			return "请填写恢复已完成的流程原因.";
		}

		Flow fl = new Flow(this.No);
		BP.WF.Data.GERpt rpt = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.No) + "Rpt");
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

			gwf.setFK_Flow(this.No);
			gwf.setFlowName(this.Name);
			gwf.setWorkID(workid);
			gwf.setPWorkID(rpt.getPWorkID());
			gwf.setPFlowNo(rpt.getPFlowNo());
			gwf.setPNodeID(rpt.getPNodeID());
			gwf.setPEmp(rpt.getPEmp());


			gwf.setFK_Node(backToNodeID);
			gwf.setNodeName(endN.getName());

			gwf.setStarter(rpt.getFlowStarter());
			gwf.setStarterName(empStarter.Name);
			gwf.setFK_FlowSort(fl.getFK_FlowSort());
			gwf.setSysType(fl.getSysType());

			gwf.setTitle(rpt.getTitle());
			gwf.setWFState(WFState.ReturnSta); //设置为退回的状态
			gwf.setFK_Dept(rpt.getFK_Dept());

			Dept dept = new Dept(empStarter.FK_Dept);

			gwf.setDeptName(dept.Name);
			gwf.setPRI(1);

			LocalDateTime dttime = LocalDateTime.now();
			dttime = dttime.plusDays(3);

			gwf.setSDTOfNode(dttime.toString("yyyy-MM-dd HH:mm:ss"));
			gwf.setSDTOfFlow(dttime.toString("yyyy-MM-dd HH:mm:ss"));
			if (isHaveGener)
			{
				gwf.Update();
			}
			else
			{
				gwf.Insert(); //插入流程引擎数据.
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 创建流程引擎主表数据
			String ndTrack = "ND" + Integer.parseInt(this.No) + "Track";
			String actionType = ActionType.Forward.getValue() + "," + ActionType.FlowOver.getValue() + "," + ActionType.ForwardFL.getValue() + "," + ActionType.ForwardHL.getValue();
			String sql = "SELECT  * FROM " + ndTrack + " WHERE   ActionType IN (" + actionType + ")  and WorkID=" + workid + " ORDER BY RDT DESC, NDFrom ";
			System.Data.DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@工作ID为:" + workid + "的数据不存在.");
			}

			String starter = "";
			boolean isMeetSpecNode = false;
			GenerWorkerList currWl = new GenerWorkerList();
			for (DataRow dr : dt.Rows)
			{
				int ndFrom = Integer.parseInt(dr.get("NDFrom").toString());
				Node nd = new Node(ndFrom);

				String ndFromT = dr.get("NDFromT").toString();
				String EmpFrom = dr.get(TrackAttr.EmpFrom).toString();
				String EmpFromT = dr.get(TrackAttr.EmpFromT).toString();

				// 增加上 工作人员的信息.
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.setWorkID(workid);
				gwl.setFK_Flow(this.No);

				gwl.setFK_Node(ndFrom);
				gwl.setFK_NodeText(ndFromT);

				if (gwl.getFK_Node() == backToNodeID)
				{
					gwl.setIsPass(false);
					currWl = gwl;
				}
				else
				{
					gwl.setIsPass(true);
				}

				gwl.setFK_Emp(EmpFrom);
				gwl.setFK_EmpText(EmpFromT);
				if (gwl.IsExits)
				{
					continue; //有可能是反复退回的情况.
				}

				Emp emp = new Emp(gwl.getFK_Emp());
				gwl.setFK_Dept(emp.FK_Dept);

				gwl.setSDT(dr.get("RDT").toString());
				gwl.setDTOfWarning(gwf.getSDTOfNode());

				gwl.setIsEnable(true);
				gwl.setWhoExeIt(nd.getWhoExeIt());
				gwl.Insert();
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入退回信息, 让接受人能够看到退回原因.
			ReturnWork rw = new ReturnWork();
			rw.Delete(ReturnWorkAttr.WorkID, workid); //先删除历史的信息.

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
			rw.setMyPK( BP.DA.DBAccess.GenerGUID();
			rw.Insert();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion   加入退回信息, 让接受人能够看到退回原因.

			//更新流程表的状态.
			rpt.setFlowEnder(currWl.getFK_Emp());
			rpt.setWFState(WFState.ReturnSta); //设置为退回的状态
			rpt.setFlowEndNode(currWl.getFK_Node());
			rpt.Update();

			// 向接受人发送一条消息.
			BP.WF.Dev2Interface.Port_SendMsg(currWl.getFK_Emp(), "工作恢复:" + gwf.getTitle(), "工作被:" + WebUser.getNo() + " 恢复." + note, "ReBack" + workid, BP.WF.SMSMsgType.SendSuccess, this.No, Integer.parseInt(this.No + "01"), workid, 0);

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
	*/
	public final String DoGenerFlowEmps()
	{
		if (!WebUser.getNo().equals("admin"))
		{
			return "非admin用户不能执行。";
		}

		Flow fl = new Flow(this.No);

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.getNo());

		for (GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			String emps = "";
			String sql = "SELECT EmpFrom FROM ND" + Integer.parseInt(this.No) + "Track  WHERE WorkID=" + gwf.getWorkID();

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				if (emps.contains("," + dr.get(0).toString() + ","))
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
		String table = nd.getHisWork().EnMap.PhysicsTable;
		String tableRpt = "ND" + Integer.parseInt(this.No) + "Rpt";
		Sys.MapData md = new Sys.MapData(tableRpt);
		for (Work wk : wks)
		{
			if (!wk.getRec().equals(WebUser.getNo()))
			{
				WebUser.Exit();
				try
				{
					Emp emp = new Emp(wk.getRec());
					WebUser.SignInOfGener(emp);
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

			ps.SQL = "UPDATE " + md.PTable + " SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);


		}
		Emp emp1 = new Emp("admin");
		WebUser.SignInOfGener(emp1);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}

	/** 
	 重新产生标题，根据新的规则.
	*/
	public final String DoGenerTitle()
	{
		if (!WebUser.getNo().equals("admin"))
		{
			return "非admin用户不能执行。";
		}
		Flow fl = new Flow(this.No);
		Node nd = fl.getHisStartNode();
		Works wks = nd.getHisWorks();
		wks.RetrieveAllFromDBSource(WorkAttr.Rec);
		String table = nd.getHisWork().EnMap.PhysicsTable;
		String tableRpt = "ND" + Integer.parseInt(this.No) + "Rpt";
		Sys.MapData md = new Sys.MapData(tableRpt);
		for (Work wk : wks)
		{

			if (!wk.getRec().equals(WebUser.getNo()))
			{
				WebUser.Exit();
				try
				{
					Emp emp = new Emp(wk.getRec());
					WebUser.SignInOfGener(emp);
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

			ps.SQL = "UPDATE " + md.PTable + " SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + SystemConfig.getAppCenterDBVarStr() + "Title WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

		}
		Emp emp1 = new Emp("admin");
		WebUser.SignInOfGener(emp1);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}

	/** 
	 绑定独立表单
	 
	 @return 
	*/
	public final String DoFlowFormTree()
	{
		return "../../Admin/FlowFormTree.aspx?s=d34&FK_Flow= " + this.getNo()+ " &ExtType=StartFlow&RefNo=" + DataType.getCurrentDataTime();
	}
	/** 
	 定义报表
	 
	 @return 
	*/
	public final String DoAutoStartIt()
	{
		Flow fl = new Flow();
		fl.No = this.No;
		fl.RetrieveFromDBSources();
		return fl.DoAutoStartIt();
	}

	/** 
	 强制设置接受人
	 
	 @param workid 工作人员ID
	 @param nodeID 节点ID
	 @param worker 工作人员
	 @return 执行结果.
	*/
	public final String DoSetTodoEmps(int workid, int nodeID, String worker)
	{
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			return "workid=" + workid + "不正确.";
		}

		BP.Port.Emp emp = new Emp();
		emp.setNo (worker;
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "人员编号不正确" + worker + ".";
		}

		BP.WF.Node nd = new Node();
		nd.setNodeID(nodeID);
		if (nd.RetrieveFromDBSources() == 0)
		{
			return "err@节点编号[" + nodeID + "]不正确.";
		}

		gwf.setFK_Node(nodeID);
		gwf.setNodeName(nd.getName());
		gwf.setTodoEmps(emp.No + "," + emp.Name + ";");
		gwf.setTodoEmpsNum(1);
		gwf.setHuiQianTaskSta(HuiQianTaskSta.None);
		gwf.Update();

		DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE WorkID=" + workid);

		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setFK_Node(nodeID);
		gwl.setWorkID(workid);
		gwl.setFK_Emp(emp.No);
		if (gwl.RetrieveFromDBSources() == 0)
		{
			LocalDateTime dt = LocalDateTime.now();
			gwl.setFK_EmpText(emp.Name);

			if (nd.getHisCHWay() == CHWay.None)
			{
				gwl.setSDT("无");
			}
			else
			{
				gwl.setSDT(dt.plusDays(3).toString("yyyy-MM-dd"));
			}

			gwl.setRDT(dt.toString("yyyy-MM-dd"));
			gwl.setIsRead(false);
			gwl.Insert();
		}
		else
		{
			gwl.setIsRead(false);
			gwl.setIsPassInt(0);
			gwl.Update();
		}
		return "执行成功.";
	}
	/** 
	 删除流程
	 
	 @param workid
	 @param sd
	 @return 
	*/
	public final String DoDelDataOne(int workid, String note)
	{
		try
		{
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.No, workid, true);
			return "删除成功 workid=" + workid + "  理由:" + note;
		}
		catch (RuntimeException ex)
		{
			return "删除失败:" + ex.getMessage();
		}
	}
	public final String DoStopFlow(long workid, String note)
	{
		try
		{
			BP.WF.Dev2Interface.Flow_DoFlowOver(this.No, workid, note);
			return "流程被强制结束 workid=" + workid + "  理由:" + note;
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
		if (DataType.IsNullOrEmpty(this.No) == true)
		{
			throw new RuntimeException("传入的流程编号为空，请检查流程");
		}
		String flowID = Integer.parseInt(this.No).toString() + "01";
		return "../../Admin/AttrFlow/AutoStart.htm?s=d34&FK_Flow= " + this.getNo()+ " &ExtType=StartFlow&RefNo=";
	}
	public final String DoCCNode()
	{
		return "../../Admin/CCNode.aspx?FK_Flow=" + this.No;
	}
	/** 
	 执行运行
	 
	 @return 
	*/
	public final String DoRunIt()
	{
		return "../../Admin/TestFlow.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 执行检查
	 
	 @return 
	*/
	public final String DoCheck()
	{
		return "../../Admin/AttrFlow/CheckFlow.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}

	public final String DoCheck2018Url()
	{
		return "../../Admin/Testing/FlowCheckError.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 启动限制规则
	 
	 @return 返回URL
	*/
	public final String DoLimit()
	{
		return "../../Admin/AttrFlow/Limit.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 设置发起前置导航
	 
	 @return 
	*/
	public final String DoStartGuide()
	{
		return "../../Admin/AttrFlow/StartGuide.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 设置发起前置导航
	 
	 @return 
	*/
	public final String DoStartGuideV2019()
	{
		return "../../Admin/AttrFlow/StartGuide/Default.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 执行数据同步
	 
	 @return 
	*/
	public final String DoDTS()
	{
		return "../../Admin/AttrFlow/DTSBTable.aspx?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 导入
	 
	 @return 
	*/
	public final String DoImp()
	{
		return "../../Admin/AttrFlow/Imp.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}

	/** 
	 导出
	 
	 @return 
	*/
	public final String DoExps()
	{
		return "../../Admin/AttrFlow/Exp.htm?FK_Flow= " + this.getNo()+ " &Lang=CH";
	}
	/** 
	 执行重新装载数据
	 
	 @return 
	*/
	public final String DoReloadRptData()
	{
		Flow fl = new Flow();
		fl.No = this.No;
		fl.RetrieveFromDBSources();
		return fl.DoReloadRptData();
	}
	/** 
	 删除数据.
	 
	 @return 
	*/
	public final String DoDelData()
	{
		Flow fl = new Flow();
		fl.No = this.No;
		fl.RetrieveFromDBSources();
		return fl.DoDelData();
	}
	/** 
	 运行报表
	 
	 @return 
	*/
	public final String DoOpenRpt()
	{
		if (DataType.IsNullOrEmpty(this.No) == true)
		{
			throw new RuntimeException("传入的流程编号为空，请检查流程");
		}
		return "../../Admin/RptDfine/Default.htm?FK_Flow= " + this.getNo()+ " &DoType=Edit&FK_MapData=ND" + Integer.parseInt(this.No) + "Rpt";
	}
	/** 
	 更新之后的事情，也要更新缓存。
	*/
	@Override
	protected void afterUpdate()
	{
		// Flow fl = new Flow();
		// fl.No = this.No;
		// fl.RetrieveFromDBSources();
		//fl.Update();

		if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
		{
			// DBAccess.RunSQL("UPDATE  GPM_Menu SET Name='" + this.Name + "' WHERE Flag='Flow " + this.getNo()+ " ' AND FK_App='" + SystemConfig.SysNo + "'");
		}
	}
	@Override
	protected boolean beforeUpdate()
	{
		//更新流程版本
		Flow.UpdateVer(this.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 同步事件实体.
		try
		{
			String flowMark = this.getFlowMark();
			if (DataType.IsNullOrEmpty(flowMark) == true)
			{
				flowMark = this.No;
			}

			this.setFlowEventEntity(BP.WF.Glo.GetFlowEventEntityStringByFlowMark(flowMark));
		}
		catch (java.lang.Exception e)
		{
			this.setFlowEventEntity("");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 同步事件实体.

		//更新缓存数据。
		Flow fl = new Flow(this.No);
		fl.RetrieveFromDBSources();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region StartFlows的清缓存
		if (fl.getIsStartInMobile() != this.getIsStartInMobile() || fl.getIsCanStart() != this.getIsCanStart())
		{
			//清空WF_Emp 的StartFlows
			DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion StartFlows的清缓存

		fl.Copy(this);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据完整性 - 同步业务表数据。
		// 检查业务是否存在.
		if (fl.getDTSWay() != FlowDTSWay.None)
		{
			/*检查业务表填写的是否正确.*/
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

				if (DataType.IsNullOrEmpty(nodes) == true)
				{
					throw new RuntimeException("@业务数据同步数据配置错误，您设置了按照指定的节点配置，但是您没有设置节点,节点的设置格式如下：101,102,103");
				}

				String[] strs = nodes.split("[,]", -1);
				for (String str : strs)
				{
					if (DataType.IsNullOrEmpty(str) == true)
					{
						continue;
					}

					if (BP.DA.DataType.IsNumStr(str) == false)
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置了按照指定的节点配置，但是节点格式错误[" + nodes + "]。正确的格式如下：101,102,103");
					}

					Node nd = new Node();
					nd.setNodeID(Integer.parseInt(str));
					if (nd.IsExits == false)
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置的节点格式错误，节点[" + str + "]不是有效的节点。");
					}

					nd.RetrieveFromDBSources();
					if (!nd.getFK_Flow().equals(this.No))
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置的节点[" + str + "]不再本流程内。");
					}
				}
			}

			//检查流程数据存储表是否正确
			if (!DataType.IsNullOrEmpty(fl.getPTable()))
			{
				/*检查流程数据存储表填写的是否正确.*/
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查数据完整性. - 同步业务表数据。



		return super.beforeUpdate();
	}
	@Override
	protected void afterInsertUpdateAction()
	{
		//同步流程数据表.
		String ndxxRpt = "ND" + Integer.parseInt(this.No) + "Rpt";
		Flow fl = new Flow(this.No);
		if (!fl.getPTable().equals("ND" + Integer.parseInt(this.No) + "Rpt"))
		{
			BP.Sys.MapData md = new Sys.MapData(ndxxRpt);
			if (!fl.getPTable().equals(md.PTable))
			{
				md.Update();
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 为systype设置，当前所在节点的第2级别目录。
		FlowSort fs = new FlowSort(fl.getFK_FlowSort());
		if (fs.ParentNo.equals("99") || fs.ParentNo.equals("0"))
		{
			this.setSysType(fl.getFK_FlowSort());
		}
		else
		{
			FlowSort fsP = new FlowSort(fs.ParentNo);
			if (fsP.ParentNo.equals("99") || fsP.ParentNo.equals("0"))
			{
				this.setSysType(fsP.No);
			}
			else
			{
				FlowSort fsPP = new FlowSort(fsP.ParentNo);
				this.setSysType(fsPP.No);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 为systype设置，当前所在节点的第2级别目录。

		fl = new Flow();
		fl.No = this.No;
		fl.RetrieveFromDBSources();
		fl.Update();



		super.afterInsertUpdateAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 实验中的功能.
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
	*/
	public final String DoSetFWCModel()
	{
		Nodes nds = new Nodes(this.No);
		for (Node nd : nds.ToJavaList())
		{
			if (nd.getIsStartNode())
			{
				nd.setHisFormType(NodeFormType.FoolForm);
				nd.Update();
				continue;
			}

			BP.WF.Template.FrmNodeComponent fnd = new FrmNodeComponent(nd.getNodeID());

			if (nd.getIsEndNode() == true || nd.getHisToNodes().size() == 0)
			{
				nd.setFrmWorkCheckSta(FrmWorkCheckSta.Readonly);
				nd.setNodeFrmID("ND" + Integer.parseInt(this.No) + "02");
				nd.setHisFormType(NodeFormType.FoolForm);
				nd.Update();


				fnd.SetValByKey(NodeAttr.NodeFrmID, nd.getNodeFrmID());
				fnd.SetValByKey(NodeAttr.FWCSta, nd.getFrmWorkCheckSta().getValue());

				fnd.Update();
				continue;
			}

		  //  fnd.HisFormType = NodeFormType.FoolForm;

			fnd.Update(); //不执行更新，会导致部分字段错误.

			nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
			nd.setNodeFrmID("ND" + Integer.parseInt(this.No) + "02");
			nd.setHisFormType(NodeFormType.FoolForm);
			nd.Update();
		}

		return "设置成功...";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
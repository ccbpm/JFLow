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
public class FlowSheet extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
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
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara1);
		return str.replace("~", "'");
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
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara2);
		return str.replace("~", "'");
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
		if (BP.Web.WebUser.No.equals("admin") || this.getDesignerNo().equals(WebUser.No))
		{
			uac.IsUpdate = true;
		}
		return uac;
	}
	/** 
	 流程
	*/
	public FlowSheet()
	{
	}
	/** 
	 流程
	 
	 @param _No 编号
	*/
	public FlowSheet(String _No)
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_Flow", "流程");
		map.Java_SetCodeStruct("3");

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

			//add  2013-08-30.
		map.AddTBString(FlowAttr.BillNoFormat, null, "单据编号格式", true, false, 0, 50, 10, false);
		map.SetHelperUrl(FlowAttr.BillNoFormat, "http://ccbpm.mydoc.io/?v=5404&t=17041");

			// add 2014-10-19.
		map.AddDDLSysEnum(FlowAttr.ChartType, FlowChartType.Icon.getValue(), "节点图形类型", true, true, "ChartType", "@0=几何图形@1=肖像图片");

		map.AddBoolean(FlowAttr.IsCanStart, true, "可以独立启动否？(独立启动的流程可以显示在发起流程列表里)", true, true, true);
		map.SetHelperUrl(FlowAttr.IsCanStart, "http://ccbpm.mydoc.io/?v=5404&t=17027");




		map.AddBoolean(FlowAttr.IsMD5, false, "是否是数据加密流程(MD5数据加密防篡改)", true, true, true);
		map.SetHelperUrl(FlowAttr.IsMD5, "http://ccbpm.mydoc.io/?v=5404&t=17028");

		map.AddBoolean(FlowAttr.IsFullSA, false, "是否自动计算未来的处理人？", true, true, true);
		map.SetHelperUrl(FlowAttr.IsFullSA, "http://ccbpm.mydoc.io/?v=5404&t=17034");


		map.AddBoolean(FlowAttr.IsGuestFlow, false, "是否外部用户参与流程(非组织结构人员参与的流程)", true, true, false);
		map.SetHelperUrl(FlowAttr.IsGuestFlow, "http://ccbpm.mydoc.io/?v=5404&t=17039");
			//批量发起 add 2013-12-27. 
		map.AddBoolean(FlowAttr.IsBatchStart, false, "是否可以批量发起流程？(如果是就要设置发起的需要填写的字段,多个用逗号分开)", true, true, true);
		map.AddTBString(FlowAttr.BatchStartFields, null, "发起字段s", true, false, 0, 500, 10, true);
		map.SetHelperUrl(FlowAttr.IsBatchStart, "http://ccbpm.mydoc.io/?v=5404&t=17047");
		map.AddDDLSysEnum(FlowAttr.FlowAppType, FlowAppType.Normal.getValue(), "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
		map.SetHelperUrl(FlowAttr.FlowAppType, "http://ccbpm.mydoc.io/?v=5404&t=17035");

			// 草稿
		map.AddDDLSysEnum(FlowAttr.Draft, DraftRole.None.getValue(), "草稿规则", true, true, FlowAttr.Draft, "@0=无(不设草稿)@1=保存到待办@2=保存到草稿箱");
		map.SetHelperUrl(FlowAttr.Draft, "http://ccbpm.mydoc.io/?v=5404&t=17037");

			// 数据存储.
		map.AddDDLSysEnum(FlowAttr.DataStoreModel, DataStoreModel.ByCCFlow.getValue(), "流程数据存储模式", true, true, FlowAttr.DataStoreModel, "@0=数据轨迹模式@1=数据合并模式");
		map.SetHelperUrl(FlowAttr.DataStoreModel, "http://ccbpm.mydoc.io/?v=5404&t=17038");

			//add 2013-05-22.
		map.AddTBString(FlowAttr.HistoryFields, null, "历史查看字段", true, false, 0, 500, 10, true);
			//map.SetHelperBaidu(FlowAttr.HistoryFields, "ccflow 历史查看字段");
		map.AddTBString(FlowAttr.FlowNoteExp, null, "备注的表达式", true, false, 0, 500, 10, true);
		map.SetHelperUrl(FlowAttr.FlowNoteExp, "http://ccbpm.mydoc.io/?v=5404&t=17043");
		map.AddTBString(FlowAttr.Note, null, "流程描述", true, false, 0, 100, 10, true);

		map.AddDDLSysEnum(FlowAttr.FlowAppType, FlowAppType.Normal.getValue(), "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
		map.AddTBString(FlowAttr.HelpUrl, null, "帮助文档", true, false, 0, 300, 10, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 启动方式
		map.AddDDLSysEnum(FlowAttr.FlowRunWay, getFlowRunWay().HandWork.getValue(), "启动方式", true, true, FlowAttr.FlowRunWay, "@0=手工启动@1=指定人员定时启动@2=定时访问数据集自动启动@3=触发式启动");

		map.SetHelperUrl(FlowAttr.FlowRunWay, "http://ccbpm.mydoc.io/?v=5404&t=17088");
			// map.AddTBString(FlowAttr.RunObj, null, "运行内容", true, false, 0, 100, 10, true);
		map.AddTBStringDoc(FlowAttr.RunObj, null, "运行内容", true, false, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 启动方式

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 流程启动限制
		String role = "@0=不限制";
		role += "@1=每人每天一次";
		role += "@2=每人每周一次";
		role += "@3=每人每月一次";
		role += "@4=每人每季一次";
		role += "@5=每人每年一次";
		role += "@6=发起的列不能重复,(多个列可以用逗号分开)";
		role += "@7=设置的SQL数据源为空,或者返回结果为零时可以启动.";
		role += "@8=设置的SQL数据源为空,或者返回结果为零时不可以启动.";
		map.AddDDLSysEnum(FlowAttr.StartLimitRole, StartLimitRole.None.getValue(), "启动限制规则", true, true, FlowAttr.StartLimitRole, role, true);
		map.AddTBString(FlowAttr.StartLimitPara, null, "规则参数", true, false, 0, 500, 10, true);
		map.AddTBStringDoc(FlowAttr.StartLimitAlert, null, "限制提示", true, false, true);
		map.SetHelperUrl(FlowAttr.StartLimitRole, "http://ccbpm.mydoc.io/?v=5404&t=17872");
			//   map.AddTBString(FlowAttr.StartLimitAlert, null, "限制提示", true, false, 0, 500, 10, true);
			//    map.AddDDLSysEnum(FlowAttr.StartLimitWhen, (int)StartLimitWhen.StartFlow, "提示时间", true, true, FlowAttr.StartLimitWhen, "@0=启动流程时@1=发送前提示", false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 流程启动限制

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 发起前导航。
			//map.AddDDLSysEnum(FlowAttr.DataStoreModel, (int)DataStoreModel.ByCCFlow,
			//    "流程数据存储模式", true, true, FlowAttr.DataStoreModel,
			//   "@0=数据轨迹模式@1=数据合并模式");

			//发起前设置规则.
		map.AddDDLSysEnum(FlowAttr.StartGuideWay, 0, "前置导航方式", true, true, FlowAttr.StartGuideWay, "@0=无@1=按系统的URL-(父子流程)单条模式@2=按系统的URL-(子父流程)多条模式@3=按系统的URL-(实体记录,未完成)单条模式@4=按系统的URL-(实体记录,未完成)多条模式@5=从开始节点Copy数据@10=按自定义的Url@11=按用户输入参数", true);
		map.SetHelperUrl(FlowAttr.StartGuideWay, "http://ccbpm.mydoc.io/?v=5404&t=17883");

		map.AddTBString(FlowAttr.StartGuidePara1, null, "参数1", true, false,0,500,20,true);
		map.AddTBString(FlowAttr.StartGuidePara2, null, "参数2", true, false, 0, 500, 20, true);
		map.AddTBString(FlowAttr.StartGuidePara3, null, "参数3", true, false, 0, 500, 20, true);

		map.AddBoolean(FlowAttr.IsResetData, false, "是否启用开始节点数据重置按钮？", true, true, true);
			//     map.AddBoolean(FlowAttr.IsImpHistory, false, "是否启用导入历史数据按钮？", true, true, true);
		map.AddBoolean(FlowAttr.IsLoadPriData, false, "是否自动装载上一笔数据？", true, true, true);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 发起前导航。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 延续流程。
			// add 2013-03-24.
		map.AddTBString(FlowAttr.DesignerNo, null, "设计者编号", false, false, 0, 32, 10);
		map.AddTBString(FlowAttr.DesignerName, null, "设计者名称", false, false, 0, 100, 10);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 延续流程。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 数据同步方案
			//数据同步方式.
		map.AddDDLSysEnum(FlowAttr.DTSWay, FlowDTSWay.None.getValue(), "同步方式", true, true, FlowAttr.DTSWay, "@0=不同步@1=同步");
		map.SetHelperUrl(FlowAttr.DTSWay, "http://ccbpm.mydoc.io/?v=5404&t=17893");

		map.AddDDLEntities(FlowAttr.DTSDBSrc, "", "数据库", new BP.Sys.SFDBSrcs(), true);

		map.AddTBString(FlowAttr.DTSBTable, null, "业务表名", true, false, 0, 50, 50, false);

		map.AddTBString(FlowAttr.DTSBTablePK, null, "业务表主键", true, false, 0, 50, 50, false);
		map.SetHelperAlert(FlowAttr.DTSBTablePK, "如果同步方式设置了按照业务表主键字段计算,那么需要在流程的节点表单里设置一个同名同类型的字段，系统将会按照这个主键进行数据同步。");

		map.AddTBString(FlowAttr.DTSFields, null, "要同步的字段s,中间用逗号分开.", false, false, 0, 200, 100, false);

		map.AddDDLSysEnum(FlowAttr.DTSTime, FlowDTSTime.AllNodeSend.getValue(), "执行同步时间点", true, true, FlowAttr.DTSTime, "@0=所有的节点发送后@1=指定的节点发送后@2=当流程结束时");
		map.SetHelperUrl(FlowAttr.DTSTime, "http://ccbpm.mydoc.io/?v=5404&t=17894");

		map.AddTBString(FlowAttr.DTSSpecNodes, null, "指定的节点ID", true, false, 0, 50, 50, false);
		map.SetHelperAlert(FlowAttr.DTSSpecNodes, "如果执行同步时间点选择了按指定的节点发送后,多个节点用逗号分开.比如: 101,102,103");


		map.AddDDLSysEnum(FlowAttr.DTSField, DTSField.SameNames.getValue(), "要同步的字段计算方式", true, true, FlowAttr.DTSField, "@0=字段名相同@1=按设置的字段匹配");
		map.SetHelperUrl(FlowAttr.DTSField, "http://ccbpm.mydoc.io/?v=5404&t=17895");

		map.AddTBString(FlowAttr.PTable, null, "流程数据存储表", true, false, 0, 30, 10);
		map.SetHelperUrl(FlowAttr.PTable, "http://ccbpm.mydoc.io/?v=5404&t=17897");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 数据同步方案

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 权限控制.
		map.AddBoolean(FlowAttr.PStarter, true, "发起人可看(必选)", true, false,true);
		map.AddBoolean(FlowAttr.PWorker, true, "参与人可看(必选)", true, false, true);
		map.AddBoolean(FlowAttr.PCCer, true, "被抄送人可看(必选)", true, false, true);

		map.AddBoolean(FlowAttr.PMyDept, true, "本部门人可看", true, true, true);
		map.AddBoolean(FlowAttr.PPMyDept, true, "直属上级部门可看(比如:我是)", true, true, true);

		map.AddBoolean(FlowAttr.PPDept, true, "上级部门可看", true, true, true);
		map.AddBoolean(FlowAttr.PSameDept, true, "平级部门可看", true, true, true);

		map.AddBoolean(FlowAttr.PSpecDept, true, "指定部门可看", true, true, false);
		map.AddTBString(FlowAttr.PSpecDept + "Ext", null, "部门编号", true, false, 0, 200, 100, false);


		map.AddBoolean(FlowAttr.PSpecSta, true, "指定的岗位可看", true, true, false);
		map.AddTBString(FlowAttr.PSpecSta + "Ext", null, "岗位编号", true, false, 0, 200, 100, false);

		map.AddBoolean(FlowAttr.PSpecGroup, true, "指定的权限组可看", true, true, false);
		map.AddTBString(FlowAttr.PSpecGroup + "Ext", null, "权限组", true, false, 0, 200, 100, false);

		map.AddBoolean(FlowAttr.PSpecEmp, true, "指定的人员可看", true, true, false);
		map.AddTBString(FlowAttr.PSpecEmp + "Ext", null, "指定的人员编号", true, false, 0, 200, 100, false);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 权限控制.


			//查询条件.
		map.AddSearchAttr(FlowAttr.FK_FlowSort);
		 //   map.AddSearchAttr(FlowAttr.TimelineRole);


			//map.AddRefMethod(rm);
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "调试运行"; // "设计检查报告";
			//rm.ToolTip = "检查流程设计的问题。";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/Run.png";
		rm.ClassMethodName = this.toString() + ".DoRunIt";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "检查报告"; // "设计检查报告";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/CheckRpt.png";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		   // rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "删除全部流程数据"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.Warning = "您确定要执行删除流程数据吗? \t\n该流程的数据删除后，就不能恢复，请注意删除的内容。"; // "您确定要执行删除流程数据吗？";
		rm.ClassMethodName = this.toString() + ".DoDelData";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "按照工作ID删除单个流程"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.ClassMethodName = this.toString() + ".DoDelDataOne";
		rm.HisAttrs.AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.HisAttrs.AddTBString("beizhu", null, "删除备注", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Title = "重新生成报表数据"; // "删除数据";
		rm.Warning = "您确定要执行吗? 注意:此方法耗费资源。"; // "您确定要执行删除流程数据吗？";
		rm.ClassMethodName = this.toString() + ".DoReloadRptData";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "设置自动发起数据源";
		rm.Icon = "../../WF/Img/EntityFunc/Flow/Run.png";
		rm.ClassMethodName = this.toString() + ".DoSetStartFlowDataSources()";
			//设置相关字段.
			// rm.RefAttrKey = FlowAttr.RunObj;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手工启动定时任务";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Warning = "您确定要执行吗? 注意:对于数据量交大的数据因为web上执行时间的限时问题，会造成执行失败。"; // "您确定要执行删除流程数据吗？";
		rm.ClassMethodName = this.toString() + ".DoAutoStartIt()";
			//设置相关字段.
		rm.RefAttrKey = FlowAttr.FlowRunWay;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "流程监控";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoDataManger()";
		map.AddRefMethod(rm);



		rm = new RefMethod();
		rm.Title = "重新生成FlowEmps字段";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoGenerFlowEmps()";
		rm.RefAttrLinkLabel = "补充修复emps字段，包括wf_generworkflow,NDxxxRpt字段.";
		rm.RefMethodType = RefMethodType.Func;
		rm.Target = "_blank";
		rm.Warning = "您确定要重新生成s吗？";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "重新生成流程标题";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoGenerTitle()";
			//设置相关字段.
			//rm.RefAttrKey = FlowAttr.TitleRole;
		rm.RefAttrLinkLabel = "重新生成流程标题";
		rm.RefMethodType = RefMethodType.Func;
		rm.Target = "_blank";
		rm.Warning = "您确定要根据新的规则重新产生标题吗？";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "回滚流程";
		rm.Icon = "../../WF/Img/Btn/Back.png";
		rm.ClassMethodName = this.toString() + ".DoRebackFlowData()";
			// rm.Warning = "您确定要回滚它吗？";
		rm.HisAttrs.AddTBInt("workid", 0, "请输入要会滚WorkID", true, false);
		rm.HisAttrs.AddTBInt("nodeid", 0, "回滚到的节点ID", true, false);
		rm.HisAttrs.AddTBString("note", null, "回滚原因", true, false, 0, 600, 200);
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "流程事件&消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon = "../../WF/Img/Event.png";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


			//rm = new RefMethod();
			//rm.Title = "独立表单树";
			//rm.Icon = "../../WF/Img/Btn/DTS.gif";
			//rm.ClassMethodName = this.ToString() + ".DoFlowFormTree()";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量发起规则";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoBatchStartFields()";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "流程轨迹表单";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoBindFlowSheet()";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "数据源管理(如果新增数据源后需要关闭重新打开)"; // "抄送规则";
		rm.ClassMethodName = this.toString() + ".DoDBSrc";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSDBSrc;
		rm.RefAttrLinkLabel = "数据源管理";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "业务表字段同步配置"; // "抄送规则";
		rm.ClassMethodName = this.toString() + ".DoBTable";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSField;
		rm.RefAttrLinkLabel = "业务表字段同步配置";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);



		rm = new RefMethod();
		rm.Title = "执行流程数据表与业务表数据手工同步"; // "抄送规则";
		rm.ClassMethodName = this.toString() + ".DoBTableDTS";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Warning = "您确定要执行吗？如果执行了可能会对业务表数据造成错误。";
			//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSSpecNodes;
		rm.RefAttrLinkLabel = "业务表字段同步配置";
		rm.RefMethodType = RefMethodType.Func;
		rm.Target = "_blank";
			//  map.AddRefMethod(rm);


		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  公共方法
	/** 
	 事件
	 
	 @return 
	*/
	public final String DoAction()
	{
		return "../../Admin/AttrNode/Action.htm?NodeID=0&FK_Flow=" + this.No + "&tk=" + (new Random()).nextDouble();
	}
	public final String DoDBSrc()
	{
		return "../../Comm/Sys/SFDBSrcNewGuide.htm";
	}
	public final String DoBTable()
	{
		return "../../Admin/AttrFlow/DTSBTable.aspx?s=d34&ShowType=FlowFrms&FK_Node=" + Integer.parseInt(this.No) + "01&FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=" + DataType.CurrentDataTime;
	}

	public final String DoBindFlowSheet()
	{
		return "../../Admin/Sln/BindFrms.htm?s=d34&ShowType=FlowFrms&FK_Node=0&FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=" + DataType.CurrentDataTime;
	}
	/** 
	 批量发起字段
	 
	 @return 
	*/
	public final String DoBatchStartFields()
	{
		return "../../Admin/AttrFlow/BatchStartFields.htm?s=d34&FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=" + DataType.CurrentDataTime;
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
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.No) + "Rpt");
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
			String todoEmps = "";
			int num = 0;
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
				gwl.setIsPass(true);
				if (gwl.getFK_Node() == backToNodeID)
				{
					gwl.setIsPass(false);
					currWl = gwl;
				}

				gwl.setFK_Emp(EmpFrom);
				gwl.setFK_EmpText(EmpFromT);
				if (gwl.IsExits)
				{
					continue; //有可能是反复退回的情况.
				}

				Emp emp = new Emp(gwl.getFK_Emp());
				gwl.setFK_Dept(emp.FK_Dept);
				gwl.setFK_DeptT(emp.FK_DeptText);


				todoEmps += emp.No + "," + emp.Name + ";";
				num++;


				gwl.setSDT(dr.get("RDT").toString());
				gwl.setDTOfWarning(gwf.getSDTOfNode());
				//gwl.WarningHour = nd.WarningHour;
				gwl.setIsEnable(true);
				gwl.setWhoExeIt(nd.getWhoExeIt());
				gwl.Insert();
			}

			//设置当前处理人员.
			gwf.SetValByKey(GenerWorkFlowAttr.TodoEmps, todoEmps);
			gwf.setTodoEmpsNum(num);

			if (isHaveGener)
			{
				gwf.Update();
			}
			else
			{
				gwf.Insert(); //插入流程引擎数据.
			}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入退回信息, 让接受人能够看到退回原因.
			ReturnWork rw = new ReturnWork();
			rw.setWorkID(workid);
			rw.setReturnNode(backToNodeID);
			rw.setReturnNodeName(endN.getName());
			rw.setReturner(WebUser.No);
			rw.setReturnerName(WebUser.Name);

			rw.setReturnToNode(currWl.getFK_Node());
			rw.setReturnToEmp(currWl.getFK_Emp());
			rw.setBeiZhu(note);
			rw.setRDT(DataType.CurrentDataTime);
			rw.setIsBackTracking(false);
			rw.MyPK = BP.DA.DBAccess.GenerGUID();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion   加入退回信息, 让接受人能够看到退回原因.

			//更新流程表的状态.
			rpt.setFlowEnder(currWl.getFK_Emp());
			rpt.setWFState(WFState.ReturnSta); //设置为退回的状态
			rpt.setFlowEndNode(currWl.getFK_Node());
			rpt.Update();

			// 向接受人发送一条消息.
			BP.WF.Dev2Interface.Port_SendMsg(currWl.getFK_Emp(), "工作恢复:" + gwf.getTitle(), "工作被:" + WebUser.No + " 恢复." + note, "ReBack" + workid, BP.WF.SMSMsgType.SendSuccess, this.No, Integer.parseInt(this.No + "01"), workid, 0);

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
		if (!WebUser.No.equals("admin"))
		{
			return "非admin用户不能执行。";
		}

		Flow fl = new Flow(this.No);

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.No);

		for (GenerWorkFlow gwf : gwfs)
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
			if (!wk.getRec().equals(WebUser.No))
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
			ps.SQL = "UPDATE " + table + " SET Title=" + SystemConfig.AppCenterDBVarStr + "Title WHERE OID=" + SystemConfig.AppCenterDBVarStr + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE " + md.PTable + " SET Title=" + SystemConfig.AppCenterDBVarStr + "Title WHERE OID=" + SystemConfig.AppCenterDBVarStr + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + SystemConfig.AppCenterDBVarStr + "Title WHERE WorkID=" + SystemConfig.AppCenterDBVarStr + "OID";
			DBAccess.RunSQL(ps);


		}
		Emp emp1 = new Emp("admin");
		BP.Web.WebUser.SignInOfGener(emp1);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}

	/** 
	 重新产生标题，根据新的规则.
	*/
	public final String DoGenerTitle()
	{
		if (!WebUser.No.equals("admin"))
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

			if (!wk.getRec().equals(WebUser.No))
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
			ps.SQL = "UPDATE " + table + " SET Title=" + SystemConfig.AppCenterDBVarStr + "Title WHERE OID=" + SystemConfig.AppCenterDBVarStr + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE " + md.PTable + " SET Title=" + SystemConfig.AppCenterDBVarStr + "Title WHERE OID=" + SystemConfig.AppCenterDBVarStr + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + SystemConfig.AppCenterDBVarStr + "Title WHERE WorkID=" + SystemConfig.AppCenterDBVarStr + "OID";
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
		//PubClass.WinOpen(Glo.CCFlowAppPath + "WF/Rpt/OneFlow.htm?FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=", 700, 500);
		return "../../Comm/Search.htm?s=d34&EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=";
	}
	/** 
	 绑定独立表单
	 
	 @return 
	*/
	public final String DoFlowFormTree()
	{
		return "../../Admin/FlowFormTree.aspx?s=d34&FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=" + DataType.CurrentDataTime;
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
	/** 
	 设置发起数据源
	 
	 @return 
	*/
	public final String DoSetStartFlowDataSources()
	{
		String flowID = Integer.parseInt(this.No).toString() + "01";
		return "../../Admin/FoolFormDesigner/MapExt.aspx?s=d34&FK_MapData=ND" + flowID + "&ExtType=StartFlow&RefNo=";
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
		return "../../Admin/TestFlow.htm?FK_Flow=" + this.No + "&Lang=CH";
	}
	/** 
	 执行检查
	 
	 @return 
	*/
	public final String DoCheck()
	{
		//Flow fl = new Flow();
		//fl.No = this.No;
		//fl.RetrieveFromDBSources();

		return "/WF/Admin/AttrFlow/CheckFlow.htm?FK_Flow=" + this.No;

		//return fl.DoCheck();
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
	@Override
	protected boolean beforeUpdate()
	{
		//更新流程版本
		Flow.UpdateVer(this.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据完整性 - 同步业务表数据。
		// 检查业务是否存在.
		Flow fl = new Flow(this.No);
		fl.Row = this.Row;

		if (fl.getDTSWay() != FlowDTSWay.None)
		{
			/*检查业务表填写的是否正确.*/
			String sql = "select count(*) as Num from  " + fl.getDTSBTable() + " where 1=2";
			try
			{
				DBAccess.RunSQLReturnValInt(sql, 0);
			}
			catch (RuntimeException e)
			{
				throw new RuntimeException("@业务表配置无效，您配置业务数据表[" + fl.getDTSBTable() + "]在数据中不存在，请检查拼写错误如果是跨数据库请加上用户名比如: for sqlserver: HR.dbo.Emps, For oracle: HR.Emps");
			}

			sql = "select " + fl.getDTSBTablePK() + " from " + fl.getDTSBTable() + " where 1=2";
			try
			{
				DBAccess.RunSQLReturnValInt(sql, 0);
			}
			catch (RuntimeException e2)
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
				catch (RuntimeException e3)
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
		super.afterInsertUpdateAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
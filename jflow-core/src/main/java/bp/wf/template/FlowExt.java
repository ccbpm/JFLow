package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.port.Dept;
import bp.port.Emp;
import bp.sys.*;
import bp.tools.DateUtils;
import bp.web.WebUser;
import bp.wf.Glo;
import bp.wf.*;
import bp.wf.template.frm.MapDataExt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** 
 流程
*/
public class FlowExt extends EntityNoName
{

		///#region 属性.
	/** 
	 存储表
	*/
	public final String getPTable() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.PTable);
	}
	public final void setPTable(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.PTable, value);
	}
	/** 
	 流程类别
	*/
	public final String getFK_FlowSort() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别（第2级流程树节点编号）
	*/
	public final String getSysType() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.SysType);
	}
	public final void setSysType(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.SysType, value);
	}

	/** 
	 是否可以独立启动
	*/
	public final boolean isCanStart() throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsCanStart);
	}
	public final void setCanStart(boolean value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.IsCanStart, value);
	}
	/** 
	 流程事件实体
	*/
	public final String getFlowEventEntity() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.FlowEventEntity);
	}
	public final void setFlowEventEntity(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.FlowEventEntity, value);
	}
	/** 
	 流程标记
	*/
	public final String getFlowMark() throws Exception {
		String str = this.GetValStringByKey(FlowAttr.FlowMark);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return this.getNo();
		}
		return str;
	}
	public final void setFlowMark(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.FlowMark, value);
	}


		///#region   前置导航
	/** 
	 前置导航方式
	*/
	public final StartGuideWay getStartGuideWay() throws Exception {
		return StartGuideWay.forValue(this.GetValIntByKey(FlowAttr.StartGuideWay));

	}
	public final void setStartGuideWay(StartGuideWay value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.StartGuideWay, value.getValue());
	}

	/** 
	 前置导航参数1
	*/

	public final String getStartGuidePara1() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.StartGuidePara1);
	}
	public final void setStartGuidePara1(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.StartGuidePara1, value);
	}

	/** 
	 前置导航参数2
	*/

	public final String getStartGuidePara2() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.StartGuidePara2);
	}
	public final void setStartGuidePara2(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.StartGuidePara2, value);
	}

	/** 
	 前置导航参数3
	*/

	public final String getStartGuidePara3() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.StartGuidePara3);
	}
	public final void setStartGuidePara3(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.StartGuidePara3, value);
	}


	/** 
	 启动方式
	*/
	public final FlowRunWay getFlowRunWay() throws Exception {
		return FlowRunWay.forValue(this.GetValIntByKey(FlowAttr.FlowRunWay));

	}
	public final void setFlowRunWay(FlowRunWay value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.FlowRunWay, value.getValue());
	}


	/** 
	 运行内容
	*/

	public final String getRunObj() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.RunObj);
	}
	public final void setRunObj(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.RunObj, value);
	}


	/** 
	 是否启用开始节点数据重置按钮
	*/

	public final boolean isResetData() throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsResetData);
	}
	public final void setResetData(boolean value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.IsResetData, value);
	}

	/** 
	 是否自动装载上一笔数据
	*/
	public final boolean isLoadPriData() throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsLoadPriData);
	}
	public final void setLoadPriData(boolean value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.IsLoadPriData, value);
	}
	/** 
	 是否可以在手机里启用
	*/
	public final boolean isStartInMobile() throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsStartInMobile);
	}
	public final void setStartInMobile(boolean value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.IsStartInMobile, value);
	}

		///#endregion
	/** 
	 设计者编号
	*/
	public final String getDesignerNo() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.DesignerNo);
	}
	public final void setDesignerNo(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.DesignerNo, value);
	}
	/** 
	 设计者名称
	*/
	public final String getDesignerName() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.DesignerName);
	}
	public final void setDesignerName(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.DesignerName, value);
	}
	/** 
	 设计时间
	*/
	public final String getDesignTime() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.DesignTime);
	}
	public final void setDesignTime(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.DesignTime, value);
	}
	/** 
	 编号生成格式
	*/
	public final String getBillNoFormat() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.BillNoFormat);
	}
	public final void setBillNoFormat(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.BillNoFormat, value);
	}
	/** 
	 测试人员
	*/
	public final String getTester() throws Exception
	{
		return this.GetValStringByKey(FlowAttr.Tester);
	}
	public final void setTester(String value)  throws Exception
	 {
		this.SetValByKey(FlowAttr.Tester, value);
	}

		///#endregion 属性.


		///#region 构造方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() {
		UAC uac = new UAC();

		if (WebUser.getIsAdmin() == false)
		{
			throw new RuntimeException("err@管理员登录用户信息丢失,当前会话[" + WebUser.getNo() + "," + WebUser.getName() + "]");
		}
		uac.IsUpdate = true;
			//uac.OpenForAdmin();  //zsy修改 2020.5.15
			//if (bp.web.WebUser.getNo().Equals("admin")==true || this.DesignerNo == WebUser.getNo())
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 流程
	*/
	public FlowExt()  {
	}
	/** 
	 流程
	 
	 param _No 编号
	*/
	public FlowExt(String _No) throws Exception {
		this.setNo(_No);
		if (bp.difference.SystemConfig.getIsDebug())
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
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Flow", "流程模版");

		map.AddGroupAttr("基本属性");

			///#region 基本属性。
		map.AddTBStringPK(FlowAttr.No, null, "编号", true, true, 1, 4, 3);
		map.SetHelperUrl(FlowAttr.No, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661868&doc_id=31094"); //使用alert的方式显示帮助信息.
			//处理流程类别.
		String sql = "";
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			map.AddDDLEntities(FlowAttr.FK_FlowSort, null, "类别", new FlowSorts(), true);
				// map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), true);
		}
		else
		{
			sql = "SELECT No,Name FROM WF_FlowSort WHERE OrgNo='@WebUser.OrgNo' ORDER BY NO, IDX";
			map.AddDDLSQL(FlowAttr.FK_FlowSort, null, "类别", sql, true);
		}

			//map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), false);
			//map.SetHelperUrl(FlowAttr.FK_FlowSort, "http://ccbpm.mydoc.io/?v=5404&t=17024");

		map.AddTBString(FlowAttr.Name, null, "名称", true, false, 0, 50, 10, true);

			// add 2013-02-14 唯一确定此流程的标记
		map.AddTBString(FlowAttr.FlowMark, null, "流程标记", true, false, 0, 150, 10);
		map.AddTBString(FlowAttr.FlowEventEntity, null, "流程事件实体", true, true, 0, 150, 10);
		map.SetHelperUrl(FlowAttr.FlowMark, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661870&doc_id=31094");
		map.SetHelperUrl(FlowAttr.FlowEventEntity, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661871&doc_id=31094");

			// add 2013-02-05.
		map.AddTBString(FlowAttr.TitleRole, null, "标题生成规则", true, false, 0, 150, 10, true);
		map.SetHelperUrl(FlowAttr.TitleRole, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661872&doc_id=31094");

		map.AddTBString(FlowAttr.TitleRoleNodes, null, "生成标题的节点", true, false, 0, 300, 10);
		String msg = "设置帮助";
			//msg += "\r\n 1. 如果为空表示只在开始节点生成标题.";
			//msg += "\r\n 2. * 表示在任意节点可生成标题.";
			//msg += "\r\n 3. 要在指定的节点重新生成标题用逗号分开,比如: 102,105,109";
		map.SetHelperUrl(FlowAttr.TitleRoleNodes, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661873&doc_id=31094");



		map.AddBoolean(FlowAttr.IsCanStart, true, "可以独立启动否？(独立启动的流程可以显示在发起流程列表里)", true, true, true);
		map.SetHelperUrl(FlowAttr.IsCanStart, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661874&doc_id=31094");

		map.AddBoolean(FlowAttr.IsFullSA, false, "是否自动计算未来的处理人？", true, true, true);
		map.SetHelperUrl(FlowAttr.IsFullSA, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661875&doc_id=31094");

			//map.AddDDLSysEnum(FlowAttr.IsAutoSendSubFlowOver, 0, "为子流程时结束规则", true, true,
			// FlowAttr.IsAutoSendSubFlowOver, "@0=不处理@1=让父流程自动运行下一步@2=结束父流程");

			//map.AddBoolean(FlowAttr.GuestFlowRole, false, "是否外部用户参与流程(非组织结构人员参与的流程)", true, true, false);
		map.AddDDLSysEnum(FlowAttr.GuestFlowRole, GuestFlowRole.None.getValue(), "外部用户参与流程规则", true, true, "GuestFlowRole", "@0=不参与@1=开始节点参与@2=中间节点参与");
		map.SetHelperUrl(FlowAttr.GuestFlowRole, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661876&doc_id=31094");

			// map.AddDDLSysEnum(FlowAttr.FlowAppType, 0, "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
			//map.SetHelperUrl(FlowAttr.FlowAppType, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661877&doc_id=31094");

			//map.AddDDLSysEnum(FlowAttr.SDTOfFlow, (int)TimelineRole.ByNodeSet, "时效性规则",
			// true, true, FlowAttr.SDTOfFlow, "@0=按节点(由节点属性来定义)@1=按发起人(开始节点SysSDTOfFlow字段计算)");
			//map.SetHelperUrl(FlowAttr.TimelineRole, "http://ccbpm.mydoc.io/?v=5404&t=17036");

			// 草稿
		map.AddDDLSysEnum(FlowAttr.Draft, DraftRole.None.getValue(), "草稿规则", true, true, FlowAttr.Draft, "@0=无(不设草稿)@1=保存到待办@2=保存到草稿箱");
		map.SetHelperUrl(FlowAttr.Draft, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661878&doc_id=31094");

			// add for 华夏银行.
		map.AddDDLSysEnum(FlowAttr.FlowDeleteRole, FlowDeleteRole.AdminOnly.getValue(), "流程实例删除规则", true, true, FlowAttr.FlowDeleteRole, "@0=超级管理员可以删除@1=分级管理员可以删除@2=发起人可以删除@3=节点启动删除按钮的操作员");
		map.SetHelperUrl(FlowAttr.FlowDeleteRole, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661879&doc_id=31094");


			//子流程结束时，让父流程自动运行到下一步。
			//map.AddBoolean(FlowAttr.IsToParentNextNode, false, "子流程结束时，让父流程自动运行到下一步", true, true, true);
			//map.SetHelperUrl(FlowAttr.IsToParentNextNode, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661880&doc_id=31094");

			//map.AddDDLSysEnum(FlowAttr.FlowAppType, (int)FlowAppType.Normal, "流程应用类型", true, true, "FlowAppType", "@0=业务流程@1=工程类(项目组流程)@2=公文流程(VSTO)");
			//map.SetHelperUrl(FlowAttr.FlowAppType, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661877&doc_id=31094");

			//    map.AddTBString(FlowAttr.HelpUrl, null, "帮助文档", true, false, 0, 100, 10, true);


			//为 莲荷科技增加一个系统类型, 用于存储当前所在流程树的第2级流程树编号.
		map.AddTBString(FlowAttr.SysType, null, "系统类型", false, false, 0, 50, 10, false);


		map.AddTBString(FlowAttr.Tester, null, "发起测试人", true, false, 0, 100, 10, true);
		map.SetHelperUrl(FlowAttr.Tester, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661882&doc_id=31094");

		sql = "SELECT No,Name FROM Sys_EnumMain WHERE No LIKE 'Flow_%' ";
		map.AddDDLSQL("NodeAppType", null, "业务类型枚举(可为Null)", sql, true);

			// add 2014-10-19.
		map.AddDDLSysEnum(FlowAttr.ChartType, FlowChartType.Icon.getValue(), "节点图形类型", true, true, "ChartType", "@0=几何图形@1=肖像图片");
		map.SetHelperUrl(FlowAttr.ChartType, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661883&doc_id=31094");

		map.AddTBString(FlowAttr.HostRun, null, "运行主机(IP+端口)", true, false, 0, 40, 10, true);

			///#endregion 基本属性。


			///#region 表单数据.

		map.AddGroupAttr("数据&表单");
		map.AddBoolean(FlowAttr.IsBatchStart, false, "是否可以批量发起流程？(如果是就要设置发起的需要填写的字段,多个用逗号分开)", true, true, true);
		map.AddTBString(FlowAttr.BatchStartFields, null, "发起字段s", true, false, 0, 100, 10, true);
		map.SetHelperUrl(FlowAttr.IsBatchStart, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3952886&doc_id=31094");

			//add 2013-05-22.
		map.AddTBString(FlowAttr.HistoryFields, null, "历史查看字段", true, false, 0, 100, 10, true);

			//移动到这里 by zhoupeng 2016.04.08.
		map.AddBoolean(FlowAttr.IsResetData, false, "是否启用开始节点数据重置按钮？已经取消)", false, true, true);
		map.AddBoolean(FlowAttr.IsLoadPriData, false, "是否自动装载上一笔数据？", true, true, true);
		map.AddBoolean(FlowAttr.IsDBTemplate, true, "是否启用数据模版？", true, true, true);
		map.AddBoolean(FlowAttr.IsStartInMobile, true, "是否可以在手机里启用？(如果发起表单特别复杂就不要在手机里启用了)", true, true, true);
		map.SetHelperAlert(FlowAttr.IsStartInMobile, "用于控制手机端流程发起列表.");

		map.AddBoolean(FlowAttr.IsMD5, false, "是否是数据防止篡改(MD5数据加密防篡改)", true, true, true);
		map.SetHelperUrl(FlowAttr.IsMD5, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3952971&doc_id=31094");

		map.AddBoolean(FlowAttr.IsJM, false, "是否是数据加密流程(把所有字段加密存储)", true, true, true);
		map.SetHelperUrl(FlowAttr.IsJM, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3952997&doc_id=31094");
		map.AddBoolean(FlowAttr.IsEnableDBVer, false, "是否是启用数据版本控制", true, true, true);

		// 数据存储.
		map.AddDDLSysEnum(FlowAttr.DataStoreModel, DataStoreModel.ByCCFlow.getValue(), "数据存储", true, true, FlowAttr.DataStoreModel, "@0=数据轨迹模式@1=数据合并模式");
		map.SetHelperUrl(FlowAttr.DataStoreModel, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3953009&doc_id=31094");

		map.AddTBString(FlowAttr.PTable, null, "流程数据存储表", true, false, 0, 30, 10);
		map.SetHelperUrl(FlowAttr.PTable, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=4000827&doc_id=31094");


			//map.SetHelperBaidu(FlowAttr.HistoryFields, "ccflow 历史查看字段");
		map.AddTBString(FlowAttr.FlowNoteExp, null, "备注的表达式", true, false, 0, 100, 10, true);
		map.SetHelperUrl(FlowAttr.FlowNoteExp, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3953022&doc_id=31094");

			//add  2013-08-30.
		map.AddTBString(FlowAttr.BillNoFormat, null, "单据编号格式", true, false, 0, 50, 10, false);
		map.SetHelperUrl(FlowAttr.BillNoFormat, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3953012&doc_id=31094");

			// add 2019-09-25 by zhoupeng
		map.AddTBString(FlowAttr.BuessFields, null, "关键业务字段", true, false, 0, 100, 10, false);
		msg = "用于显示在待办上的业务字段信息.";
		msg += "\t\n 1. 用户在看到待办的时候，就可以看到流程的实例的关键信息。";
		msg += "\t\n 2. 用于待办的列表信息显示.";
		msg += "\t\n 3. 配置格式为. Tel,Addr,Email  这些字段区分大小写并且是节点表单字段.";
		msg += "\t\n 4. 数据存储在WF_GenerWorkFlow.AtPara里面.";
		msg += "\t\n 5. 存储格式为: @BuessFields = 电话^Tel^18992323232;地址^Addr^山东成都;";
		map.SetHelperAlert(FlowAttr.BuessFields, msg);

			//表单URL. //@liuqiang 把他翻译到java里面去.
		map.AddDDLSysEnum(FlowAttr.FlowFrmModel, 0, "流程全局表单类型", true, false, FlowAttr.FlowFrmModel, "@0=完整版-2019年更早版本@1=绑定表单库的表单@2=表单树模式@3=自定义(嵌入)表单@4=SDK表单");
		map.AddTBString(FlowAttr.FrmUrl, null, "表单Url", true, false, 0, 150, 10, true);
		map.SetHelperAlert(FlowAttr.FrmUrl, "对嵌入式表单,SDK表单的url的表单,嵌入式表单有效,用与整体流程的设置.");

			///#endregion 表单数据.


			///#region 数据同步方案
			//数据同步方式.
			/**map.AddDDLSysEnum(FlowAttr.FlowDTSWay, (int)FlowDTSWay.None, "同步方式", true, true,
			    FlowAttr.FlowDTSWay, "@0=不同步@1=同步",true);
			map.SetHelperUrl(FlowAttr.FlowDTSWay, "http://ccbpm.mydoc.io/?v=5404&t=17893");

			map.AddDDLEntities(FlowAttr.DTSDBSrc, "", "数据库", new BP.Sys.SFDBSrcs(), true);

			map.AddTBString(FlowAttr.DTSBTable, null, "业务表名", true, false, 0, 50, 50, false);

			map.AddTBString(FlowAttr.DTSBTablePK, null, "业务表主键", true, false, 0, 50, 50, false);
			map.SetHelperAlert(FlowAttr.DTSBTablePK, "如果同步方式设置了按照业务表主键字段计算,那么需要在流程的节点表单里设置一个同名同类型的字段，系统将会按照这个主键进行数据同步。");

			map.AddTBString(FlowAttr.DTSFields, null, "要同步的字段s,中间用逗号分开.", false, false, 0, 200, 100, false);

			map.AddDDLSysEnum(FlowAttr.DTSTime, (int)FlowDTSTime.AllNodeSend, "执行同步时间点", true, true,
			   FlowAttr.DTSTime, "@0=所有的节点发送后@1=指定的节点发送后@2=当流程结束时");
			map.SetHelperUrl(FlowAttr.DTSTime, "http://ccbpm.mydoc.io/?v=5404&t=17894");

			map.AddTBString(FlowAttr.DTSSpecNodes, null, "指定的节点ID", true, false, 0, 50, 50, false);
			map.SetHelperAlert(FlowAttr.DTSSpecNodes, "如果执行同步时间点选择了按指定的节点发送后,多个节点用逗号分开.比如: 101,102,103");


			map.AddDDLSysEnum(FlowAttr.DTSField, (int)DTSField.SameNames, "要同步的字段计算方式", true, true,
			 FlowAttr.DTSField, "@0=字段名相同@1=按设置的字段匹配");
			map.SetHelperUrl(FlowAttr.DTSField, "http://ccbpm.mydoc.io/?v=5404&t=17895");
			*/
		///#endregion 数据同步方案
		map.AddTBString(FlowAttr.AdvEmps, null, "高级查询人员", true, false, 0, 100, 10, true);
		msg = "用于指定人员查询流程实例数据集合的设置.";
		msg += "\t\n 存储格式：人员编号以逗号分割如zhangsan,lisi,";
		map.SetHelperAlert(FlowAttr.AdvEmps, msg);
		//#endregion 表单数据.

			///#region 轨迹信息
		map.AddGroupAttr("轨迹");
		map.AddBoolean(FlowAttr.IsFrmEnable, false, "是否显示表单", true, true, false);
		map.AddBoolean(FlowAttr.IsTruckEnable, true, "是否显示轨迹图", true, true, false);
		map.AddBoolean(FlowAttr.IsTimeBaseEnable, true, "是否显示时间轴", true, true, false);
		map.AddBoolean(FlowAttr.IsTableEnable, true, "是否显示时间表", true, true, false);
		map.AddBoolean(FlowAttr.IsOPEnable, false, "是否显示操作", true, true, false);
		//@ZKR
		map.AddDDLSysEnum(FlowAttr.SubFlowShowType, 0, "子流程轨迹图显示模式", true, true, FlowAttr.SubFlowShowType, "@0=平铺模式显示@1=合并模式显示");
		map.AddDDLSysEnum(FlowAttr.TrackOrderBy, 0, "排序方式", true, true, FlowAttr.TrackOrderBy, "@0=按照时间先后顺序@1=倒序(新发生的在前面)");

			///#endregion 轨迹信息


			///#region 开发者信息.
		map.AddTBString(FlowAttr.DesignerNo, null, "设计者编号", false, true, 0, 50, 10, false);
		map.AddTBString(FlowAttr.DesignerName, null, "设计者名称", false, true, 0, 50, 10, false);
		map.AddTBDateTime(FlowAttr.DesignTime, null, "创建时间", false, true);
		map.AddTBString(FlowAttr.OrgNo, null, "组织编号", false, true, 0, 50, 10, false);
			// map.AddTBStringDoc(FlowAttr.Note, null, "流程描述", true, false, true);

			///#endregion 开发者信息.


		///#region 基本功能.
		map.AddGroupMethod("基本功能");
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "自动发起";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/AutoStart.png";
		rm.ClassMethodName = this.toString() + ".DoSetStartFlowDataSources()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-plane";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发起限制规则";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Limit.png";
		rm.ClassMethodName = this.toString() + ".DoLimit()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-ban";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "发起前置导航";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/StartGuide.png";
		rm.ClassMethodName = this.toString() + ".DoStartGuideV2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-list";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "流程事件"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-energy";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoMessage";
		rm.Icon = "../../WF/Img/Message24.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-bubbles";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "轨迹查看权限";
		rm.Icon = "../../WF/Img/Setting.png";
		rm.ClassMethodName = this.toString() + ".DoTruckRight()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "修改ICON"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoNodesICON";
			//  rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-heart";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "版本管理";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoVer()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
			// rm.GroupName = "实验中的功能";
		rm.Icon = "icon-doc";

		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "权限控制";
			// rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.ClassMethodName = this.toString() + ".DoPowerModel()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
			// rm.GroupName = "实验中的功能";
		rm.Icon = "icon-settings";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程模版";
		rm.Icon = "../../WF/Img/undo.png";
		rm.ClassMethodName = this.toString() + ".DoExps()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-paper-plane";
		map.AddRefMethod(rm);

		//#region 时限规则
		map.AddGroupMethod("时限规则");
		rm = new RefMethod();
		rm.Title = "计划时间计算规则";
		rm.ClassMethodName = this.toString() + ".DoSDTOfFlow";
		//rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-clock";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/CH.png";
		rm.ClassMethodName = this.toString() + ".DoDeadLineRole()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		// rm.GroupName = "实验中的功能";
		rm.Icon = "icon-clock";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "预警、超期消息事件";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/OvertimeRole.png";
		rm.ClassMethodName = this.toString() + ".DoOverDeadLineRole()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-energy";
		map.AddRefMethod(rm);
		//#endregion 时限规则

        //#region 开发接口.
		map.AddGroupMethod("开发接口");
		rm = new RefMethod();
		rm.Title = "与业务表数据同步";
		rm.ClassMethodName = this.toString() + ".DoDTSBTable()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-paper-plane";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "URL调用接口";
		//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/URL.png";
		rm.ClassMethodName = this.toString() + ".DoAPI()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-paper-plane";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "SDK开发接口";
		//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/API.png";
		rm.ClassMethodName = this.toString() + ".DoAPICode()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-paper-plane";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "代码事件开发接口";
		rm.ClassMethodName = this.toString() + ".DoAPICodeFEE()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-paper-plane";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程属性自定义";
		rm.ClassMethodName = this.toString() + ".DoFlowAttrExt()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-paper-plane";
		map.AddRefMethod(rm);
        //#endregion 开发接口.

        //#region 流程运行维护.
		map.AddGroupMethod("流程维护");
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
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重生成FlowEmps字段";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoGenerFlowEmps()";
		rm.RefAttrLinkLabel = "补充修复emps字段，包括wf_generworkflow,NDxxxRpt字段.";
		rm.refMethodType = RefMethodType.Func;
		rm.Target = "_blank";
		rm.Warning = "您确定要重新生成吗？";
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "重命名节点表单字段";
		//  rm.Warning = "您确定要处理吗？";
		rm.getHisAttrs().AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddBoolen("thisFlowOnly", true, "仅仅当前流程");
		rm.Icon = "icon-briefcase";
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "节点表单字段视图";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Field.png";
		rm.ClassMethodName = this.toString() + ".DoFlowFields()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "删除指定日期范围内的流程";
		rm.Warning = "您确定要删除吗？";
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.getHisAttrs().AddTBDateTime("DTFrom", null, "时间从", true, false);
		rm.getHisAttrs().AddTBDateTime("DTTo", null, "时间到", true, false);
		rm.getHisAttrs().AddBoolen("thisFlowOnly", true, "仅仅当前流程");
		rm.Icon = "icon-briefcase";
		rm.ClassMethodName = this.toString() + ".DoDelFlows";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "按工作ID删除"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.ClassMethodName = this.toString() + ".DoDelDataOne";
		rm.getHisAttrs().AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.getHisAttrs().AddTBString("beizhu", null, "删除备注", true, false, 0, 100, 100);
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "强制设置接收人";
		rm.getHisAttrs().AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.getHisAttrs().AddTBInt("NodeID", 0, "节点ID", true, false);
		rm.getHisAttrs().AddTBString("Worker", null, "接受人编号", true, false, 0, 100, 100);
		rm.Icon = "icon-briefcase";
		rm.ClassMethodName = this.toString() + ".DoSetTodoEmps";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		//   rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.Title = "按工作ID强制结束"; // this.ToE("DelFlowData", "删除数据"); // "删除数据";
		rm.ClassMethodName = this.toString() + ".DoStopFlow";
		rm.getHisAttrs().AddTBInt("WorkID", 0, "输入工作ID", true, false);
		rm.Icon = "icon-briefcase";
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
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);
        //#endregion 流程运行维护.

        //#region 流程监控.
		map.AddGroupMethod("流程监控");
		rm = new RefMethod();
		rm.Title = "设计报表"; // "报表运行";
		rm.Icon = "../../WF/Img/Btn/Rpt.gif";
		rm.ClassMethodName = this.toString() + ".DoOpenRpt()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		//rm = new RefMethod();
		//rm.Title = "监控面板";
		//rm.Icon = ../../Admin/CCBPMDesigner/Img/Monitor.png";
		//rm.ClassMethodName = this.ToString() + ".DoDataManger_Welcome()";
		//rm.RefMethodType = RefMethodType.RightFrameOpen;
		//rm.GroupName = "流程监控";
		//map.AddRefMethod(rm);

		//  rm = new RefMethod();
		//rm.Title = "图形分析";
		//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		//rm.ClassMethodName = this.ToString() + ".DoDataManger_DataCharts()";
		//rm.RefMethodType = RefMethodType.LinkeWinOpen;
		//rm.GroupName = "流程监控";
		//rm.Icon = "icon-briefcase";
		//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "综合查询";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Search.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Search()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "综合分析";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Group.png";
		rm.ClassMethodName = this.toString() + ".DoDataManger_Group()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Icon = "icon-briefcase";
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
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);
        //#endregion 流程监控.

        //#region 实验中的功能
		map.AddGroupMethod("实验中的功能");
		rm = new RefMethod();
		rm.Title = "数据源管理(如果新增数据源后需要关闭重新打开)";
		rm.ClassMethodName = this.toString() + ".DoDBSrc";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSDBSrc;
		rm.RefAttrLinkLabel = "数据源管理";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "业务表字段同步配置";
		rm.ClassMethodName = this.toString() + ".DoBTable";
		rm.Icon = bp.wf.Glo.getCCFlowAppPath()+ "WF/Img/Btn/DTS.gif";
		//设置相关字段.
		rm.RefAttrKey = FlowAttr.DTSField;
		rm.RefAttrLinkLabel = "业务表字段同步配置";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "一键设置审核组件工作模式";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "您确定要设置审核组件模式吗？ \t\n 1, 第2个节点以后的节点表单都指向第2个节点表单.  \t\n  2, 结束节点都设置为只读模式. ";
		rm.ClassMethodName = this.toString() + ".DoSetFWCModel()";
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		//rm = new RefMethod();
		//rm.Title = "删除NDxxRpt表,多余字段.";
		//rm.ClassMethodName = this.ToString() + ".DoDeleteFields()";
		//rm.RefMethodType = RefMethodType.Func;
		//rm.Warning = "您确定要执行吗？ \t\n 1, 表NDxxxRpt是自动创建的.  \t\n  2, 在设置流程过程中有些多余的字段会生成到NDxxRpt表里. \t\n 3,这里是删除数据字段为null 并且是多余的字段.";
		//rm.GroupName = "实验中的功能";
		//rm.Icon = "icon-briefcase";
		//map.AddRefMethod(rm);

		//rm = new RefMethod();
		//rm.Title = "删除NDxRpt表,数据为null的多余字段.";
		//rm.ClassMethodName = this.ToString() + ".DoDeleteFieldsIsNull()";
		//rm.RefMethodType = RefMethodType.Func;
		//rm.Warning = "您确定要执行吗？ \t\n 1, 表NDxxxRpt是自动创建的.  \t\n  2, 在设置流程过程中有些多余的字段会生成到NDxxxRpt表里. \t\n 3,这里是删除数据字段为null 并且是多余的字段.";
		//rm.GroupName = "实验中的功能";
		//rm.Icon = "icon-briefcase";
		//map.AddRefMethod(rm);
       //#endregion 实验中的功能


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 流程监控.

	public final String DoDataManger_DataCharts() throws Exception {
		return "../../Admin/AttrFlow/DataCharts.htm?FK_Flow=" + this.getNo();
		// return "../../Comm/Search.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.No + "&WFSta=all";
	}


	public final String DoDataManger_Search() throws Exception {
		return "../../Comm/Search.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.getNo() + "&WFSta=all";
	}
	public final String DoDataManger_Group() throws Exception {
		return "../../Comm/Group.htm?EnsName=BP.WF.Data.GenerWorkFlowViews&FK_Flow=" + this.getNo() + "&WFSta=all";
	}


	public final String DoDataManger_DeleteLog() throws Exception {
		return "../../Comm/Search.htm?EnsName=BP.WF.WorkFlowDeleteLogs&FK_Flow=" + this.getNo() + "&WFSta=all";
	}

	/** 
	 数据订阅
	 
	 @return 
	*/
	public final String DoDataManger_RptOrder() throws Exception {
		return "../../Admin/CCBPMDesigner/App/RptOrder.aspx?anaTime=mouth&FK_Flow=" + this.getNo();
	}

		///#endregion 流程监控.


		///#region 开发接口.
	/** 
	 执行删除指定日期范围内的流程
	 
	 param dtFrom 日期从
	 param dtTo 日期到
	 param isOk 仅仅删除当前流程？1=删除当前流程, 0=删除全部流程.
	 @return 
	*/
	public final String DoDelFlows(String dtFrom, String dtTo, boolean isDelCurrFlow) throws Exception {
		if (WebUser.getIsAdmin() == false)
			return "非管理员用户，不能删除。";


		String sql = "";
		if (isDelCurrFlow == true)
			sql = "SELECT WorkID, FK_Flow FROM WF_GenerWorkFlow  WHERE RDT >= '" + dtFrom + "' AND RDT <= '" + dtTo + "'  AND FK_Flow='" + this.getNo() + "' ";
		else
			sql = "SELECT WorkID, FK_Flow FROM WF_GenerWorkFlow  WHERE RDT >= '" + dtFrom + "' AND RDT <= '" + dtTo + "' ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String msg = "如下流程ID被删除:";
		//线程执行删除流程
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);
		final int POOL_SIZE = dt.Rows.size();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				POOL_SIZE,
				POOL_SIZE,
				POOL_SIZE, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(POOL_SIZE),
				new ThreadPoolExecutor.CallerRunsPolicy());
		List<CompletableFuture<Void>> futures =new ArrayList<CompletableFuture<Void>>();
		try{
			for (DataRow dr : dt.Rows)
			{
				long workid = Long.parseLong(dr.getValue("WorkID").toString());
				String fk_flow = dr.getValue("FK_Flow").toString();
				msg += " " + workid;
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					try {
						DoDelFlowByWorkID(workid, fk_flow);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}, executor);
				futures.add(future);
			}
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
			executor.shutdown();
		}catch(Exception e){
			throw new RuntimeException("部分流程数据删除成功:"+msg);
		}

		return msg;
	}
	public final void DoDelFlowByWorkID(long workid, String fk_flow) throws Exception {
		Flow flow = new Flow(fk_flow);

			///#region 删除独立表单数据
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, fk_flow, null);
		String strs = "";
		for (FrmNode frmNode : fns.ToJavaList())
		{
			if (strs.contains("@" + frmNode.getFKFrm()) == true)
			{
				continue;
			}
			strs += "@" + frmNode.getFKFrm() + "@";
			try
			{
				MapData md = new MapData(frmNode.getFKFrm());
				DBAccess.RunSQL("DELETE FROM " + md.getPTable() + " WHERE OID=" + workid);
			}
			catch (java.lang.Exception e)
			{

			}
		}

			///#endregion 删除独立表单数据

		//流程运行信息
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList Where (WorkID=" + workid + " OR FID=" + workid + ")");
		//删除退回信息
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork Where WorkID=" + workid);
		//考核信息
		DBAccess.RunSQL("DELETE FROM WF_CH Where (WorkID=" + workid + " OR FID=" + workid + ")");
		DBAccess.RunSQL("DELETE FROM WF_CHEval Where WorkID=" + workid);

		//接收人信息删除
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper Where WorkID=" + workid);
		//流程信息流转自定义
		DBAccess.RunSQL("DELETE FROM WF_TransferCustom Where WorkID=" + workid);

		//流程remberme的信息
		DBAccess.RunSQL("DELETE FROM WF_RememberMe Where FK_Node IN(SELECT NodeID From WF_Node WHERE FK_Flow='" + fk_flow + "')");
		//删除流程的抄送信息
		DBAccess.RunSQL("DELETE FROM WF_CCList WHERE WorkID=" + workid);

		//删除流程实例的信息
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow Where (WorkID=" + workid + " OR FID=" + workid + ")");

		//删除track信息
		if (DBAccess.IsExitsObject("ND" + Integer.parseInt(fk_flow) + "Track") == true)
		{
			DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(fk_flow) + "Track Where (WorkID=" + workid + " OR FID=" + workid + ")");
		}

		//删除表单信息
		if (DataType.IsNullOrEmpty(flow.getPTable()) == false && DBAccess.IsExitsObject(flow.getPTable()) == true)
		{
			DBAccess.RunSQL("DELETE FROM " + flow.getPTable() + " Where OID=" + workid);
		}
		//删除节点信息
		Nodes nds = new Nodes(fk_flow);
		for (Node nd : nds.ToJavaList())
		{
			try
			{
				Work wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.getEnMap().getPhysicsTable() + " Where OID=" + workid);
			}
			catch (java.lang.Exception e2)
			{

			}
			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			for (MapDtl dtl : dtls.ToJavaList())
			{
				try
				{
					DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + workid);
				}
				catch (java.lang.Exception e3)
				{

				}

			}
		}

	}
	/** 
	 批量重命名字段.
	 
	 param FieldOld
	 param FieldNew
	 param FieldNewName
	 @return 
	*/
	public final String DoChangeFieldName(String fieldOld, String fieldNew, String FieldNewName, String thisFlowOnly) throws Exception {

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
	public final String DoChangeFieldNameOne(FlowExt flow, String fieldOld, String fieldNew, String FieldNewName) throws Exception {
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
	public final String DoFlowFields() throws Exception {
		return "../../Admin/AttrFlow/FlowFields.htm?FK_Flow=" + this.getNo();
	}
	/** 
	 与业务表数据同步
	 
	 @return 
	*/
	public final String DoDTSBTable() throws Exception {
		return "../../Admin/AttrFlow/DTSBTable.htm?FK_Flow=" + this.getNo();
	}
	public final String DoAPI() throws Exception {
		return "../../Admin/AttrFlow/API.htm?FK_Flow=" + this.getNo();
	}
	public final String DoAPICode() throws Exception {
		return "../../Admin/AttrFlow/APICode.htm?FK_Flow=" + this.getNo();
	}
	public final String DoAPICodeFEE() throws Exception {
		return "../../Admin/AttrFlow/APICodeFEE.htm?FK_Flow=" + this.getNo();
	}
	/** 
	 流程属性自定义
	 
	 @return 
	*/
	public final String DoFlowAttrExt() throws Exception {
		return "../../../DataUser/OverrideFiles/FlowAttrExts.html?FK_Flow=" + this.getNo();
	}
	public final String DoVer() throws Exception {
		return "../../Admin/AttrFlow/Ver.htm?FK_Flow=" + this.getNo();
	}
	public final String DoPowerModel() throws Exception {
		return "../../Admin/AttrFlow/PowerModel.htm?FK_Flow=" + this.getNo();
	}

	/** 
	 时限规则
	 
	 @return 
	*/
	public final String DoDeadLineRole() throws Exception {
		return "../../Admin/AttrFlow/DeadLineRole.htm?FK_Flow=" + this.getNo();
	}

	/** 
	 预警、超期规则
	 
	 @return 
	*/
	public final String DoOverDeadLineRole() throws Exception {
		return "../../Admin/AttrFlow/PushMessage.htm?FK_Flow=" + this.getNo();
	}



		///#endregion 开发接口


		///#region  基本功能
	/** 
	 事件
	 
	 @return 
	*/
	public final String DoAction() throws Exception {
		return "../../Admin/AttrFlow/Action.htm?FK_Flow=" + this.getNo() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 流程事件
	 
	 @return 
	*/
	public final String DoMessage() throws Exception {
		return "../../Admin/AttrFlow/PushMessage.htm?FK_Node=0&FK_Flow=" + this.getNo() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 计划玩成
	 
	 @return 
	*/
	public final String DoSDTOfFlow() throws Exception {
		return "../../Admin/AttrFlow/SDTOfFlow/Default.htm?FK_Flow=" + this.getNo() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 节点标签
	 
	 @return 
	*/
	public final String DoNodesICON() throws Exception {
		return "../../Admin/AttrFlow/NodesIcon.htm?FK_Flow=" + this.getNo() + "&tk=" + (new Random()).nextDouble();
	}
	public final String DoDBSrc() throws Exception {
		return "../../Comm/Sys/SFDBSrcNewGuide.htm";
	}
	public final String DoBTable() throws Exception {
		return "../../Admin/AttrFlow/DTSBTable.htm?s=d34&ShowType=FlowFrms&FK_Node=" + Integer.parseInt(this.getNo()) + "01&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=" + DataType.getCurrentDateTime();
	}
	/** 
	 批量发起
	 
	 @return 
	*/
	public final String DoBatchStart() throws Exception {
		return "../../Admin/AttrFlow/BatchStart.htm?s=d34&ShowType=FlowFrms&FK_Node=" + Integer.parseInt(this.getNo()) + "01&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=" + DataType.getCurrentDateTime();
	}
	/** 
	 批量修改节点属性
	 
	 @return 
	*/
	public final String DoNodeAttrs() throws Exception {
		return "../../Admin/AttrFlow/NodeAttrs.htm?NodeID=0&FK_Flow=" + this.getNo() + "&tk=" + (new Random()).nextDouble();
	}
	public final String DoBindFlowExt() throws Exception {
		return "../../Admin/Sln/BindFrms.htm?s=d34&ShowType=FlowFrms&FK_Node=0&FK_Flow=" + this.getNo() + "&ExtType=StartFlow";
	}
	/** 
	 轨迹查看权限
	 
	 @return 
	*/
	public final String DoTruckRight() throws Exception {
		return "../../Admin/AttrFlow/TruckViewPower.htm?FK_Flow=" + this.getNo();
	}
	/** 
	 批量发起字段
	 
	 @return 
	*/
	public final String DoBatchStartFields() throws Exception {
		return "../../Admin/AttrNode/BatchStartFields.htm?s=d34&FK_Flow=" + this.getNo() + "&ExtType=StartFlow";
	}
	/** 
	 执行流程数据表与业务表数据手工同步
	 
	 @return 
	*/
	public final String DoBTableDTS() throws Exception {
		Flow fl = new Flow(this.getNo());
		return fl.DoBTableDTS();

	}
	/** 
	 恢复已完成的流程数据到指定的节点，如果节点为0就恢复到最后一个完成的节点上去.
	 
	 param workid 要恢复的workid
	 param backToNodeID 恢复到的节点编号，如果是0，标示回复到流程最后一个节点上去.
	 param note
	 @return 
	*/
	public final String DoRebackFlowData(long workid, int backToNodeID, String note) throws Exception {
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
			gwf.setTaskSta(TaskSta.None); //*取消共享模式
			gwf.setFK_Dept(rpt.getFK_Dept());

			Dept dept = new Dept(rpt.getFK_Dept());

			gwf.setDeptName(dept.getName());
			gwf.setPRI(1);


			Date date = DateUtils.addDay(new Date(), 3);
			String dttime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
			
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
			String actionType = ActionType.Forward.getValue() + "," + ActionType.FlowOver.getValue() + "," + ActionType.ForwardFL.getValue() + "," + ActionType.ForwardHL.getValue() + "," + ActionType.Skip.getValue();
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
				}
				else
				{
					gwl.setIsPass(true);
				}

				gwl.setFK_Emp(EmpFrom);
				gwl.setFK_EmpText(EmpFromT);
				if (gwl.getIsExits())
				{
					continue; //有可能是反复退回的情况.
				}

				Emp emp = new Emp(gwl.getFK_Emp());
				gwl.setFK_Dept(emp.getFK_Dept());

				gwl.setSDT(dr.getValue("RDT").toString());
				gwl.setDTOfWarning(gwf.getSDTOfNode());

				gwl.setEnable(true);
				gwl.setWhoExeIt(nd.getWhoExeIt());
				gwl.Insert();
			}


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
			rw.setRDT(DataType.getCurrentDateTime());
			rw.setBackTracking(false);
			rw.setMyPK(DBAccess.GenerGUID(0, null, null));
			rw.Insert();

				///#endregion   加入退回信息, 让接受人能够看到退回原因.

			//更新流程表的状态.
			rpt.setFlowEnder(currWl.getFK_Emp());
			rpt.setWFState(WFState.ReturnSta); //设置为退回的状态
			rpt.setFlowEndNode(currWl.getFK_Node());
			rpt.Update();

			// 向接受人发送一条消息.
			Dev2Interface.Port_SendMsg(currWl.getFK_Emp(), "工作恢复:" + gwf.getTitle(), "工作被:" + WebUser.getNo() + " 恢复." + note, "ReBack" + workid, SMSMsgType.SendSuccess, this.getNo(), Integer.parseInt(this.getNo() + "01"), workid, 0);

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
	 重新生成报表数据
	 
	 @return 
	*/
	public final String DoReloadRptData() throws Exception {
		Flow fl = new Flow(this.getNo());
		return fl.DoReloadRptData();
	}
	/** 
	 重新产生标题，根据新的规则.
	*/
	public final String DoGenerFlowEmps() throws Exception {
		if (!WebUser.getNo().equals("admin"))
		{
			return "非admin用户不能执行。";
		}

		Flow fl = new Flow(this.getNo());

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.getNo(), null);

		for (GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			String emps = "@";
			String sql = "SELECT EmpFrom,EmpFromT FROM ND" + Integer.parseInt(this.getNo()) + "Track  WHERE WorkID=" + gwf.getWorkID();

			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				if (emps.contains("@" + dr.getValue(0).toString() + "@") || emps.contains("@" + dr.getValue(0).toString() + "," + dr.getValue(1).toString() + "@"))
				{
					continue;
				}
				emps += dr.getValue(0).toString() + "," + dr.getValue(1).toString() + "@";
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
			GERpt rpt = new GERpt(tableRpt, wk.getOID());
			if (!rpt.getFlowStarter().equals(WebUser.getNo()))
			{
				WebUser.Exit();
				try
				{
					Emp emp = new Emp(rpt.getFlowStarter());
					WebUser.SignInOfGener(emp, "CH", false, false, null, null);
				}
				catch (java.lang.Exception e)
				{
					continue;
				}
			}
			String sql = "";
			String title = WorkFlowBuessRole.GenerTitle(fl, wk);
			Paras ps = new Paras();
			ps.Add("Title", title, false);
			ps.Add("OID", wk.getOID());
			ps.SQL = "UPDATE " + table + " SET Title=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE " + md.getPTable() + " SET Title=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "Title WHERE WorkID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);


		}
		Emp emp1 = new Emp("admin");
		WebUser.SignInOfGener(emp1, "CH", false, false, null, null);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}

	/** 
	 重新产生标题，根据新的规则.
	*/
	public final String DoGenerTitle() throws Exception {
		if (WebUser.getIsAdmin() == false)
		{
			return "非管理员用户不能执行。";
		}
		String adminNo = WebUser.getNo();
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
				WebUser.Exit();
				try
				{
					Emp emp = new Emp(wk.getRec());
					WebUser.SignInOfGener(emp, "CH", false, false, null, null);
				}
				catch (java.lang.Exception e)
				{
					continue;
				}
			}
			String title = WorkFlowBuessRole.GenerTitle(fl, wk);
			Paras ps = new Paras();
			ps.Add("Title", title, false);
			ps.Add("OID", wk.getOID());
			if (DBAccess.IsExitsTableCol(md.getPTable(), "Title") == true)
			{
				ps.SQL = "UPDATE " + md.getPTable() + " SET Title=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OID";
				DBAccess.RunSQL(ps);
			}

			if (table.equals(md.getPTable()) == false && DBAccess.IsExitsTableCol(table, "Title") == true)
			{
				ps.SQL = "UPDATE " + table + " SET Title=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "Title WHERE OID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OID";
				DBAccess.RunSQL(ps);
			}

			ps.SQL = "UPDATE WF_GenerWorkFlow SET Title=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "Title WHERE WorkID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "OID";
			DBAccess.RunSQL(ps);

		}
		Emp emp1 = new Emp(adminNo);
		WebUser.SignInOfGener(emp1, "CH", false, false, null, null);

		return "全部生成成功,影响数据(" + wks.size() + ")条";
	}


	/** 
	 定义报表
	 
	 @return 
	*/
	public final String DoAutoStartIt() throws Exception {
		Flow fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		return fl.DoAutoStartIt();
	}
	/** 
	 强制设置接受人
	 
	 param workid 工作人员ID
	 param nodeID 节点ID
	 param worker 工作人员
	 @return 执行结果.
	*/
	public final String DoSetTodoEmps(int workid, int nodeID, String worker) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			return "workid=" + workid + "不正确.";
		}

		bp.port.Emp emp = new Emp();
		emp.setNo(worker);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "人员编号不正确" + worker + ".";
		}

		bp.wf.Node nd = new Node();
		nd.setNodeID(nodeID);
		if (nd.RetrieveFromDBSources() == 0)
		{
			return "err@节点编号[" + nodeID + "]不正确.";
		}

		gwf.setFK_Node(nodeID);
		gwf.setNodeName(nd.getName());
		gwf.setTodoEmps(emp.getNo() + "," + emp.getName() + ";");
		gwf.setTodoEmpsNum(1);
		gwf.setHuiQianTaskSta(HuiQianTaskSta.None);
		gwf.Update();

		DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE WorkID=" + workid);

		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setFK_Node(nodeID);
		gwl.setWorkID(workid);
		gwl.setFK_Emp(emp.getNo());
		if (gwl.RetrieveFromDBSources() == 0)
		{
			Date date = DateUtils.addDay(new Date(), 3);
			String dttime = DateUtils.format(date, "yyyy-MM-dd");
			gwl.setFK_EmpText(emp.getName());

			if (nd.getHisCHWay() == CHWay.None)
			{
				gwl.setSDT("无");
			}
			else
			{
				gwl.setSDT(dttime);
			}

			gwl.setRDT(DateUtils.format(new Date(),"yyyy-MM-dd"));
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
	 
	 param workid
	 param sd
	 @return 
	*/
	public final String DoDelDataOne(int workid, String note)throws Exception
	{
		try
		{
			Dev2Interface.Flow_DoDeleteFlowByReal(workid, true);
			return "删除成功 workid=" + workid + "  理由:" + note;
		}
		catch (RuntimeException ex)
		{
			return "删除失败:" + ex.getMessage();
		}
	}
	public final String DoStopFlow(long workid, String note)throws Exception
	{
		try
		{
			Dev2Interface.Flow_DoFlowOver(workid, note);
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
	public final String DoSetStartFlowDataSources() throws Exception {
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			throw new RuntimeException("传入的流程编号为空，请检查流程");
		}
		String flowID = Integer.parseInt(this.getNo()) + "01";
		//return "../../Admin/AttrFlow/AutoStart.htm?s=d34&FK_Flow=" + this.No + "&ExtType=StartFlow&RefNo=";
		return "../../Admin/AttrFlow/AutoStart/Default.htm?s=d34&FK_Flow=" + this.getNo() + "&ExtType=StartFlow&RefNo=";
	}
	/** 
	 执行运行
	 
	 @return 
	*/
	public final String DoRunIt() throws Exception {
		return "../../Admin/TestFlow.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 执行检查
	 
	 @return 
	*/
	public final String DoCheck() throws Exception {
		return "../../Admin/AttrFlow/CheckFlow.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	public final String TestingContainer() throws Exception {
		return "../../Admin/TestingContainer/TestFlow2020.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	public final String DoCheck2018Url() throws Exception {
		return "../../Admin/Testing/FlowCheckError.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 启动限制规则
	 
	 @return 返回URL
	*/
	public final String DoLimit() throws Exception {
		return "../../Admin/AttrFlow/Limit/Default.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 设置发起前置导航
	 
	 @return 
	*/
	public final String DoStartGuide() throws Exception {
		return "../../Admin/AttrFlow/StartGuide.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}
	/** 
	 设置发起前置导航
	 
	 @return 
	*/
	public final String DoStartGuideV2019() throws Exception {
		return "../../Admin/AttrFlow/StartGuide/Default.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}

	/** 
	 导入
	 
	 @return 
	*/
	public final String DoImp() throws Exception {
		return "../../Admin/AttrFlow/Imp.htm?FK_Flow=" + this.getNo() + "&Lang=CH&FK_FlowSort=" + this.getFK_FlowSort();
	}

	/** 
	 导出
	 
	 @return 
	*/
	public final String DoExps() throws Exception {
		return "../../Admin/AttrFlow/Exp.htm?FK_Flow=" + this.getNo() + "&Lang=CH";
	}

	/** 
	 删除数据.
	 
	 @return 
	*/
	public final String DoDelData() throws Exception {
		Flow fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		return fl.DoDelData();
	}
	/** 
	 运行报表
	 
	 @return 
	*/
	public final String DoOpenRpt() throws Exception {
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			throw new RuntimeException("传入的流程编号为空，请检查流程");
		}
		return "../../Admin/RptDfine/Default.htm?FK_Flow=" + this.getNo() + "&DoType=Edit&FK_MapData=ND" + Integer.parseInt(this.getNo()) + "Rpt";
	}
	/** 
	 更新之后的事情，也要更新缓存。
	*/
	@Override
	protected void afterUpdate() throws Exception {
		//写入日志.
		bp.sys.base.Glo.WriteUserLog("更新流程属性：" + this.getName() + " - " + this.getNo(), "通用操作");

		// Flow fl = new Flow();
		// fl.No = this.No;
		// fl.RetrieveFromDBSources();
		//fl.Update();
	}
	@Override
	protected boolean beforeUpdate() throws Exception {
		//检查设计流程权限,集团模式下，不是自己创建的流程，不能设计流程.
		bp.wf.template.TemplateGlo.CheckPower(this.getNo());

		//更新流程版本
		Flow.UpdateVer(this.getNo());


			///#region 同步事件实体.
		try
		{
			String flowMark = this.getFlowMark();
			if (DataType.IsNullOrEmpty(flowMark) == true)
			{
				flowMark = this.getNo();
			}

			this.setFlowEventEntity(Glo.GetFlowEventEntityStringByFlowMark(flowMark));
		}
		catch (java.lang.Exception e)
		{
			this.setFlowEventEntity("");
		}

			///#endregion 同步事件实体.

		//更新缓存数据。
		Flow fl = new Flow(this.getNo());
		fl.RetrieveFromDBSources();


			///#region StartFlows的清缓存
		if (fl.isStartInMobile() != this.isStartInMobile() || fl.isCanStart() != this.isCanStart() || fl.getName().equals(this.getName()) == false)
		{
			//清空WF_Emp 的StartFlows
			DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
		}

			///#endregion StartFlows的清缓存

		fl.Copy(this);

		//2019-09-25 -by zhoupeng 为周大福增加 关键业务字段.
		fl.setBuessFields(this.GetValStrByKey(FlowAttr.BuessFields));
		fl.DirectUpdate();


			///#region 检查数据完整性 - 同步业务表数据。
		// 检查业务是否存在.
		if (fl.getDTSWay() != DataDTSWay.None)
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

					if (DataType.IsNumStr(str) == false)
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
					if (!this.getNo().equals(nd.getFK_Flow()))
					{
						throw new RuntimeException("@业务数据同步数据配置错误，您设置的节点[" + str + "]不再本流程内。");
					}
				}
			}

			//检查流程数据存储表是否正确
			if (DataType.IsNullOrEmpty(fl.getPTable()) == false)
			{
				/*检查流程数据存储表填写的是否正确.*/
				sql = "select count(*) as Num from  " + fl.getPTable() + " WHERE 1=2";
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

			///#endregion 检查数据完整性. - 同步业务表数据。

		return super.beforeUpdate();
	}
	@Override
	protected void afterInsertUpdateAction() throws Exception {

			///#region 更新PTale 后的业务处理
		// 同步流程数据表.
		String ndxxRpt = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		Flow fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		fl.Update();
		MapData md = new MapData(ndxxRpt);
		if (md.getPTable().equals(fl.getPTable()) == false)
		{
			md.setPTable ( fl.getPTable());
			md.Update();

			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, this.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
				String sql = "";
				if (nd.getHisRunModel() == RunModel.SubThread)
				{
					sql = "UPDATE Sys_MapData SET PTable=No WHERE No='ND" + nd.getNodeID() + "'";
				}
				else
				{
					sql = "UPDATE Sys_MapData SET PTable='" + fl.getPTable() + "' WHERE No='ND" + nd.getNodeID() + "'";
				}

				DBAccess.RunSQL(sql);
			}
			fl.CheckRpt(); // 检查业务表.
		}

			///#endregion 更新PTale 后的业务处理



			///#region 为systype设置，当前所在节点的第2级别目录。
		FlowSort fs = new FlowSort(fl.getFK_FlowSort());
		if (DataType.IsNullOrEmpty(fs.getParentNo()) == true)
		{
			fs.setParentNo("0");
		}

		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			if (fs.getParentNo().equals(WebUser.getOrgNo()) == true || fs.getParentNo().equals("100") == true)
			{
				this.setSysType(fl.getFK_FlowSort());
			}
			else
			{
				FlowSort fsP = new FlowSort(fs.getParentNo());
				if (fs.getParentNo().equals(WebUser.getOrgNo()) == true)
				{
					this.setSysType(fsP.getNo());
				}
				else
				{
					FlowSort fsPP = new FlowSort(fsP.getParentNo());
					this.setSysType(fsPP.getNo());
				}
			}
		}
		else
		{
			if (fs.getParentNo().equals("99") || fs.getParentNo().equals("0"))
			{
				this.setSysType(fl.getFK_FlowSort());
			}
			else
			{
				FlowSort fsP = new FlowSort(fs.getParentNo());
				fsP.setNo(fs.getParentNo());

				if (fsP.getParentNo().equals("99") || fsP.getParentNo().equals("0"))
				{
					this.setSysType(fsP.getNo());
				}
				else
				{
					FlowSort fsPP = new FlowSort();
					fsPP.setNo(fsP.getParentNo());
					if (fsPP.RetrieveFromDBSources() == 1)
					{
						this.setSysType(fsPP.getNo());
					}
				}
			}
		}

			///#endregion 为systype设置，当前所在节点的第2级别目录。

		fl = new Flow();
		fl.setNo(this.getNo());
		fl.RetrieveFromDBSources();
		fl.Update();



		super.afterInsertUpdateAction();
	}

		///#endregion


		///#region 实验中的功能.
	public final String DoNodeAttrsBatchSetting() throws Exception {
		return "../../Admin/AttrFlow/NodeAttrsBatchSetting.htm?FK_Flow=" + this.getNo() + "&tk=" + (new Random()).nextDouble();

	}
	/** 
	 删除多余的字段.
	 
	 @return 
	*/
	public final String DoDeleteFields() throws Exception {
		return "尚未完成.";
	}
	/** 
	 删除多余的字段.
	 
	 @return 
	*/
	public final String DoDeleteFieldsIsNull() throws Exception {
		return "尚未完成.";
	}
	/** 
	 一件设置审核模式.
	 
	 @return 
	*/
	public final String DoSetFWCModel() throws Exception {
		Nodes nds = new Nodes(this.getNo());
		GroupField gf = new GroupField();
		for (Node nd : nds.ToJavaList())
		{
			if (nd.isStartNode())
			{
				nd.setHisFormType(NodeFormType.FoolForm);
				nd.Update();
				continue;
			}

			bp.wf.template.FrmNodeComponent fnd = new FrmNodeComponent(nd.getNodeID());

			if (nd.isEndNode() == true || nd.getHisToNodes().isEmpty())
			{
				nd.setFrmWorkCheckSta(FrmWorkCheckSta.Readonly);
				nd.setNodeFrmID("ND" + Integer.parseInt(this.getNo()) + "02");
				nd.setHisFormType(NodeFormType.FoolForm);
				nd.Update();


				fnd.SetValByKey(NodeAttr.NodeFrmID, nd.getNodeFrmID());
				fnd.SetValByKey(NodeAttr.FWCSta, nd.getFrmWorkCheckSta().getValue());

				fnd.Update();

				if (gf.IsExit(GroupFieldAttr.FrmID, "ND"+nd.getNodeID(), GroupFieldAttr.CtrlType, GroupCtrlType.FWC) == false)
				{
					gf = new GroupField();
					gf.setFrmID("ND" + nd.getNodeID());
					gf.setCtrlType(GroupCtrlType.FWC);
					gf.setLab("审核组件");
					gf.setIdx(0);
					gf.Insert(); //插入.
				}

				continue;
			}

			//  fnd.HisFormType = NodeFormType.FoolForm;

			fnd.Update(); //不执行更新，会导致部分字段错误.

			nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
			nd.setNodeFrmID("ND" + Integer.parseInt(this.getNo()) + "02");
			nd.setHisFormType(NodeFormType.FoolForm);
			nd.Update();
			if (gf.IsExit(GroupFieldAttr.FrmID, "ND"+nd.getNodeID(), GroupFieldAttr.CtrlType, GroupCtrlType.FWC) == false)
			{
				gf = new GroupField();
				gf.setFrmID("ND" + nd.getNodeID());
				gf.setCtrlType(GroupCtrlType.FWC);
				gf.setLab("审核组件");
				gf.setIdx(0);
				gf.Insert(); //插入.
			}
		}

		return "设置成功...";
	}

		///#endregion
}
package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.port.*;
import bp.wf.Glo;
import bp.wf.template.sflow.*;
import bp.wf.template.ccen.*;
import bp.*;
import bp.wf.*;

import java.math.BigDecimal;
import java.util.*;
import java.io.*;

/** 
 节点属性.
*/
public class NodeExt extends Entity
{

		///#region 常量
	/** 
	 CCFlow流程引擎
	*/
	private static final String SYS_CCFLOW = "001";
	/** 
	 CCForm表单引擎
	*/
	private static final String SYS_CCFORM = "002";

		///#endregion


		///#region 属性.
	/** 
	 会签规则
	*/
	public final HuiQianRole getHuiQianRole() throws Exception {
		return HuiQianRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianRole));
	}
	public final void setHuiQianRole(HuiQianRole value)  throws Exception
	 {
		this.SetValByKey(BtnAttr.HuiQianRole, value.getValue());
	}
	public final HuiQianLeaderRole getHuiQianLeaderRole() throws Exception {
		return HuiQianLeaderRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianLeaderRole));
	}
	public final void setHuiQianLeaderRole(HuiQianLeaderRole value)  throws Exception
	 {
		this.SetValByKey(BtnAttr.HuiQianLeaderRole, value.getValue());
	}
	/** 
	 超时处理方式
	*/
	public final OutTimeDeal getHisOutTimeDeal() throws Exception {
		return OutTimeDeal.forValue(this.GetValIntByKey(NodeAttr.OutTimeDeal));
	}
	public final void setHisOutTimeDeal(OutTimeDeal value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.OutTimeDeal, value.getValue());
	}
	/** 
	 访问规则
	*/
	public final ReturnRole getHisReturnRole() throws Exception {
		return ReturnRole.forValue(this.GetValIntByKey(NodeAttr.ReturnRole));
	}
	public final void setHisReturnRole(ReturnRole value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.ReturnRole, value.getValue());
	}
	/** 
	 访问规则
	*/
	public final DeliveryWay getHisDeliveryWay() throws Exception {
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}
	public final void setHisDeliveryWay(DeliveryWay value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.DeliveryWay, value.getValue());
	}
	/** 
	 步骤
	*/
	public final int getStep() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.Step);
	}
	public final void setStep(int value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.Step, value);
	}
	/** 
	 节点ID
	*/
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 审核组件状态
	*/
	public final FrmWorkCheckSta getHisFrmWorkCheckSta() throws Exception {
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(NodeAttr.FWCSta));
	}

	public final FWCAth getFWCAth() throws Exception {
		return FWCAth.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCAth));
	}
	public final void setFWCAth(FWCAth value)  throws Exception
	 {
		this.SetValByKey(NodeWorkCheckAttr.FWCAth, value.getValue());
	}
	/** 
	 节点名称
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.Name, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.FlowName);
	}
	public final void setFlowName(String value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.FlowName, value);
	}
	/** 
	 接受人sql
	*/
	public final String getDeliveryParas11() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.DeliveryParas);
	}
	public final void setDeliveryParas11(String value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.DeliveryParas, value);
	}
	/** 
	 是否可以退回
	*/
	public final boolean getReturnEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ReturnRole);
	}
	public final boolean isYouLiTai() throws Exception
	{
		return this.GetValBooleanByKey(NodeAttr.IsYouLiTai);
	}
	/** 
	 是否是返回节点?
	*/
	public final boolean isSendBackNode() throws Exception
	{
		return this.GetValBooleanByKey(NodeAttr.IsSendBackNode);
	}
	public final void setSendBackNode(boolean value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.IsSendBackNode, value);
	}

	public final boolean getAddLeaderEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.AddLeaderEnable);
	}
	public final void setAddLeaderEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(BtnAttr.AddLeaderEnable, value);
	}

	/** 
	 主键
	*/
	@Override
	public String getPK()  {
		return "NodeID";
	}


		///#endregion 属性.


		///#region 初试化全局的 Node
	/** 
	 访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();

		if (bp.web.WebUser.getIsAdmin() == false)
		{
			throw new RuntimeException("err@管理员登录用户信息丢失,当前会话[" + bp.web.WebUser.getNo() + "," + bp.web.WebUser.getName() + "]");
		}

		uac.IsUpdate = true;
		uac.IsDelete = false;
		uac.IsInsert = false;

			//uac.OpenForAdmin();
		return uac;
	}

		///#endregion


		///#region 构造函数
	/** 
	 节点
	*/
	public NodeExt()  {
	}
	/** 
	 节点
	 
	 param nodeid 节点ID
	*/
	public NodeExt(int nodeid) throws Exception {
		this.setNodeID(nodeid);
		this.Retrieve();
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

		Map map = new Map("WF_Node", "节点");
			//map 的基 础信息.
		map.setDepositaryOfEntity( Depositary.Application);
		map.setDepositaryOfMap( Depositary.Application);

		map.IndexField = NodeAttr.FK_Flow;


		///#region  基础属性。
		map.AddGroupAttr("基本信息");
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
			// map.SetHelperUrl(NodeAttr.NodeID, "http://ccbpm.mydoc.io/?v=5404&t=17901");
		map.SetHelperUrl(NodeAttr.NodeID, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3576080&doc_id=31094");

			//map.SetHelperAlert(NodeAttr.Step, "它用于节点的排序，正确的设置步骤可以让流程容易读写."); //使用alert的方式显示帮助信息.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 0, 5, 10);
		map.AddTBString(NodeAttr.FlowName, null, "流程名", false, true, 0, 200, 10);


		map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 100, 10, false);
		map.SetHelperAlert(NodeAttr.Name, "修改节点名称时如果节点表单名称为空着节点表单名称和节点名称相同，否则节点名称和节点表单名称可以不相同");

		map.AddDDLSysEnum(NodeAttr.WhoExeIt, 0, "谁执行它", true, true, NodeAttr.WhoExeIt, "@0=操作员执行@1=机器执行@2=混合执行");
		map.SetHelperUrl(NodeAttr.WhoExeIt, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3576195&doc_id=31094");

		map.AddDDLSysEnum(NodeAttr.ReadReceipts, 0, "已读回执", true, true, NodeAttr.ReadReceipts, "@0=不回执@1=自动回执@2=由上一节点表单字段决定@3=由SDK开发者参数决定");
		map.SetHelperUrl(NodeAttr.ReadReceipts, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3882411&doc_id=31094");

			map.AddTBString(NodeAttr.DeliveryParas, null, "访问规则设置", false, false, 0, 300, 10);
			//map.AddDDLSysEnum(NodeAttr.CondModel, 0, "方向条件控制规则", true, true, NodeAttr.CondModel,
			//  "@0=由连接线条件控制@1=按照用户选择计算@2=发送按钮旁下拉框选择");
			//map.SetHelperUrl(NodeAttr.CondModel, "http://ccbpm.mydoc.io/?v=5404&t=17917"); //增加帮助

			// 撤销规则.
		map.AddDDLSysEnum(NodeAttr.CancelRole, CancelRole.OnlyNextStep.getValue(), "撤销规则", true, true, NodeAttr.CancelRole, "@0=上一步可以撤销@1=不能撤销@2=上一步与开始节点可以撤销@3=指定的节点可以撤销");
		map.SetHelperUrl(NodeAttr.CancelRole, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3576276&doc_id=31094");
		map.AddBoolean(NodeAttr.CancelDisWhenRead, false, "对方已经打开就不能撤销", true, true);

			//map.AddBoolean(NodeAttr.IsTask, true, "允许分配工作否?", true, true, false, "http://ccbpm.mydoc.io/?v=5404&t=17904");
			//map.AddBoolean(NodeAttr.IsExpSender, true, "本节点接收人不允许包含上一步发送人", true, true, false);
			//map.AddBoolean(NodeAttr.IsRM, true, "是否启用投递路径自动记忆功能?", true, true, false, "http://ccbpm.mydoc.io/?v=5404&t=17905");
		map.AddBoolean(NodeAttr.IsOpenOver, false, "已阅即完成?", true, true, false);
		map.SetHelperUrl(NodeAttr.IsOpenOver, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3653663&doc_id=31094");

			//为铁路局,会签子流程. 增加 
		map.AddBoolean(NodeAttr.IsSendDraftSubFlow, false, "是否发送草稿子流程?", true, true, true);
		map.SetHelperAlert(NodeAttr.IsSendDraftSubFlow, "如果有启动的草稿子流程，是否发送它们？"); //增加帮助。
			//map.AddBoolean(NodeAttr.IsToParentNextNode, false, "子流程运行到该节点时，让父流程自动运行到下一步", true, true, true);

		map.AddBoolean(NodeAttr.IsGuestNode, false, "是否是外部用户执行的节点(非组织结构人员参与处理工作的节点)?", true, true, true, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661834&doc_id=31094");

		map.AddBoolean(NodeAttr.IsYouLiTai, false, "该节点是否是游离态", true, true);
		map.SetHelperUrl(NodeAttr.IsYouLiTai, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3653664&doc_id=31094");
			//  map.SetHelperAlert(NodeAttr.IsYouLiTai, "当节点为游离状态的时候，只有连接的节点是固定节点才可以往下运行，否则流程结束");

			//map.AddTBDateTime("DTFrom", "生命周期从", true, true);
			//map.AddTBDateTime("DTTo", "生命周期到", true, true);

			//map.AddBoolean(NodeAttr.IsBUnit, false, "是否是节点模版（业务单元）?", true, true, true,
			//    "http://ccbpm.mydoc.io/?v=5404&t=17904");

		map.AddTBString(NodeAttr.FocusField, null, "焦点字段", true, false, 0, 50, 10, true, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3653665&doc_id=31094");


			//节点业务类型.
		map.AddTBInt("NodeAppType", 0, "节点业务类型", false, false);
		map.AddTBInt("FWCSta", 0, "节点状态", false, false);
		map.AddTBInt("FWCAth", 0, "审核附件是否启用", false, false);

			//如果不设置，就不能模版导入导出.
		map.AddTBInt(NodeAttr.DeliveryWay, 0, "接受人规则", false, false);

		map.AddTBString(NodeAttr.SelfParas, null, "自定义属性", true, false, 0, 500, 10, true);
		map.SetHelperUrl(NodeAttr.SelfParas, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3653666&doc_id=31094");

		map.AddTBInt(NodeAttr.Step, 0, "步骤(无计算意义)", true, false);
		map.SetHelperUrl(NodeAttr.Step, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3576085&doc_id=31094");

		map.AddTBString(NodeAttr.Tip, null, "操作提示", true, false, 0, 100, 10, false, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3653667&doc_id=31094");

			///#endregion  基础属性


			///#region 运行模式
		map.AddGroupAttr("运行模式");
		map.AddGroupAttr("运行模式");
		map.AddDDLSysEnum(NodeAttr.RunModel, 0, "节点类型", true, false, NodeAttr.RunModel, "@0=普通@1=合流@2=分流@3=分合流@4=子线程");
		map.SetHelperUrl(NodeAttr.RunModel, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661853&doc_id=31094"); //增加帮助.

			//子线程类型.
		map.AddDDLSysEnum(NodeAttr.SubThreadType, 0, "子线程类型", true, false, NodeAttr.SubThreadType, "@0=同表单@1=异表单");
		map.SetHelperUrl(NodeAttr.SubThreadType, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661855&doc_id=31094"); //增加帮助

		map.AddTBDecimal(NodeAttr.PassRate, BigDecimal.valueOf(100), "完成通过率", true, false);
		map.SetHelperUrl(NodeAttr.PassRate, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661856&doc_id=31094"); //增加帮助.

			// 启动子线程参数 2013-01-04
		map.AddDDLSysEnum(NodeAttr.SubFlowStartWay, SubFlowStartWay.None.getValue(), "子线程启动方式", true, true, NodeAttr.SubFlowStartWay, "@0=不启动@1=指定的字段启动@2=按明细表启动");
		map.AddTBString(NodeAttr.SubFlowStartParas, null, "启动参数", true, false, 0, 100, 10, true);
		map.SetHelperUrl(NodeAttr.SubFlowStartWay, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661859&doc_id=31094"); //增加帮助


			//增加对退回到合流节点的 子线城的处理控制.
		map.AddBoolean(BtnAttr.ThreadIsCanDel, true, "是否可以删除子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true, true);
		map.AddBoolean(BtnAttr.ThreadIsCanAdd, true, "是否可以增加子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效)？", true, true, true);

		map.AddBoolean(BtnAttr.ThreadIsCanShift, false, "是否可以移交子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true, true);

			////待办处理模式.
			//map.AddDDLSysEnum(NodeAttr.TodolistModel, (int)TodolistModel.QiangBan, "多人待办处理模式", true, true, NodeAttr.TodolistModel,
			//    "@0=抢办模式@1=协作模式@2=队列模式@3=共享模式@4=协作组长模式");
			//map.SetHelperUrl(NodeAttr.TodolistModel, "http://ccbpm.mydoc.io/?v=5404&t=17947"); //增加帮助.
			//发送阻塞模式.
			//map.AddDDLSysEnum(NodeAttr.BlockModel, (int)BlockModel.None, "发送阻塞模式", true, true, NodeAttr.BlockModel,
			//    "@0=不阻塞@1=当前节点有未完成的子流程时@2=按约定格式阻塞未完成子流程@3=按照SQL阻塞@4=按照表达式阻塞");
			//map.SetHelperUrl(NodeAttr.BlockModel, "http://ccbpm.mydoc.io/?v=5404&t=17948"); //增加帮助.
			//map.AddTBString(NodeAttr.BlockExp, null, "阻塞表达式", true, false, 0, 700, 10,true);
			//map.SetHelperUrl(NodeAttr.BlockExp, "http://ccbpm.mydoc.io/?v=5404&t=17948");
			//map.AddTBString(NodeAttr.BlockAlert, null, "被阻塞时提示信息", true, false, 0, 700, 10, true);
			//map.SetHelperUrl(NodeAttr.BlockAlert, "http://ccbpm.mydoc.io/?v=5404&t=17948");

		  //  map.AddBoolean(NodeAttr.USSWorkIDRole, false, "是否允许子线程接受人员重复(仅当分流点向子线程发送时有效)?", true, true, true);
		map.AddDDLSysEnum(NodeAttr.USSWorkIDRole, 0, "异表单子线程WorkID生成规则", true, true, NodeAttr.USSWorkIDRole, "@0=仅生成一个WorkID@1=按接受人生成WorkID");
		map.SetHelperAlert(NodeAttr.USSWorkIDRole, "对上一个节点是合流节点，当前节点是异表单子线程有效.");


		map.AddBoolean(NodeAttr.AutoRunEnable, false, "是否启用自动运行？(仅当分流点向子线程发送时有效)", true, true, true);
		map.AddTBString(NodeAttr.AutoRunParas, null, "自动运行SQL", true, false, 0, 100, 10, true);

			//为广西计算中心加.
		map.AddBoolean(NodeAttr.IsSendBackNode, false, "是否是发送返回节点(发送当前节点,自动发送给该节点的发送人,发送节点.)?", true, true, true);
		map.SetHelperUrl(NodeAttr.IsSendBackNode, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3661865&doc_id=31094");



			///#endregion 分合流子线程属性


			///#region 自动跳转规则
		map.AddGroupAttr("跳转规则");
		map.AddBoolean(NodeAttr.AutoJumpRole0, false, "处理人就是发起人", true, true, true);
		map.SetHelperUrl(NodeAttr.AutoJumpRole0, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3980077&doc_id=31094"); //增加帮助

		map.AddBoolean(NodeAttr.AutoJumpRole1, false, "处理人已经出现过", true, true, true);
		map.AddBoolean(NodeAttr.AutoJumpRole2, false, "处理人与上一步相同", true, true, true);
		map.AddBoolean(NodeAttr.WhenNoWorker, false, "(是)找不到人就跳转,(否)提示错误.", true, true, true);


		map.AddTBString(NodeAttr.AutoJumpExp, null, "表达式", true, false, 0, 200, 10, true);
		map.SetHelperAlert(NodeAttr.AutoJumpExp, "可以输入Url或SQL语句，请参考帮助文档。"); //增加帮助


		map.AddDDLSysEnum(NodeAttr.SkipTime, 0, "执行跳转事件", true, true, NodeAttr.SkipTime, "@0=上一个节点发送时@1=当前节点工作打开时");
		map.SetHelperUrl(NodeAttr.SkipTime, "https://gitee.com/opencc/JFlow/wikis/pages/preview?sort_id=3980077&doc_id=31094"); //增加帮助

			//map.AddDDLSysEnum(NodeAttr.WhenNoWorker, 0, "找不到处理人处理规则",
			//true, true, NodeAttr.WhenNoWorker, "@0=提示错误@1=自动转到下一步");

		map.AddTBAtParas(500);

			//最后增加按钮权限..
		BtnLab lab = new BtnLab();
		map.AddAttrs(lab.getEnMap().getAttrs(),true);

			//节点工具栏,主从表映射.
		map.AddDtl(new NodeToolbars(), NodeToolbarAttr.FK_Node, null);


			///#region 基础功能.
		map.AddGroupMethod("基本信息");
		RefMethod rm = null;
		rm = new RefMethod();
		rm.Title = "接收人规则";
			//  rm.Icon = "../../WF/Admin/AttrNode/Img/Sender.png";
		rm.Icon = "icon-people";
		rm.ClassMethodName = this.toString() + ".DoAccepterRoleNew";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "多人处理规则";
		rm.ClassMethodName = this.toString() + ".DoTodolistModel";
		rm.Icon = "../../WF/Img/Multiplayer.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-options";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "节点事件"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoAction";
			//rm.Icon = "../../WF/Img/Event.png";
		rm.Icon = "icon-energy";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "节点消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoMessage";
			//rm.Icon = "../../WF/Img/Message24.png";
		rm.Icon = "icon-bubbles";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发送后转向"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoTurnToDeal";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Turnto.png";
		rm.Icon = "icon-share";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "发送阻塞规则";
		rm.ClassMethodName = this.toString() + ".DoBlockModel";
			// rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/BlockModel.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-close";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程完成条件";
		rm.ClassMethodName = this.toString() + ".DoCondFlow";
		rm.Icon = "../../WF/Admin/AttrNode/Img/Cond.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-list";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "节点完成条件";
		rm.ClassMethodName = this.toString() + ".DoCondNode";
		rm.Icon = "../../WF/Admin/AttrNode/Img/Cond.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-list";
			//map.AddRefMethod(rm);

			////暂时去掉. 
			//rm = new RefMethod();
			//rm.Title = "待办删除规则";
			//rm.ClassMethodName = this.ToString() + ".DoGenerWorkerListDelRole";
			////rm.Icon = "../../WF/Admin/AttrNode/Img/Cond.png";
			//rm.refMethodType = RefMethodType.RightFrameOpen;
			//rm.Icon = "icon-layers";

			///#endregion 基础功能.


			///#region 字段相关功能（不显示在菜单里）
		rm = new RefMethod();
		rm.Title = "上传公文模板";
		rm.ClassMethodName = this.toString() + ".DocTemp";
			//  rm.Icon = "../../WF/Img/FileType/doc.gif";
			//设置相关字段.
		rm.RefAttrKey = BtnAttr.OfficeBtnEnable;
		rm.RefAttrLinkLabel = "公文模板维护";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.Icon = "icon-briefcase";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "可退回的节点(当退回规则设置可退回指定的节点时,该设置有效.)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoCanReturnNodes";
			//rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Icon = "icon-layers";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.ReturnRole;
		rm.RefAttrLinkLabel = "设置可退回的节点";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "可撤销的节点"; // "可撤销发送的节点";
		rm.ClassMethodName = this.toString() + ".DoCanCancelNodes";
			//rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Icon = "icon-layers";

		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.RefAttrKey = NodeAttr.CancelRole; //在该节点下显示连接.
		rm.RefAttrLinkLabel = "";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "绑定打印格式模版(当打印方式为打印RTF格式模版时,该设置有效)";
			//rm.ClassMethodName = this.ToString() + ".DoBill";
			//rm.Icon = "../../WF/Img/FileType/doc.gif";
			//rm.refMethodType = RefMethodType.LinkeWinOpen;

			//rm = new RefMethod();
			//rm.Title = "打印设置"; // "可撤销发送的节点";
			////设置相关字段.
			//rm.RefAttrKey = NodeAttr.PrintDocEnable;
			//rm.Target = "_blank";
			//rm.refMethodType = RefMethodType.LinkeWinOpen;
			//map.AddRefMethod(rm);
			//if (bp.difference.SystemConfig.getCustomerNo() == "HCBD")
			//{
			//    /* 为海成邦达设置的个性化需求. */
			//    rm = new RefMethod();
			//    rm.Title = "DXReport设置";
			//    rm.ClassMethodName = this.ToString() + ".DXReport";
			//    rm.Icon = "../../WF/Img/FileType/doc.gif";
			//    map.AddRefMethod(rm);
			//}

		rm = new RefMethod();
		rm.Title = "设置自动抄送规则(当节点为自动抄送时,该设置有效.)"; // "抄送规则";
		rm.ClassMethodName = this.toString() + ".DoCCRole";
			//rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Icon = "icon-layers";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.CCRole;
		rm.RefAttrLinkLabel = "自动抄送设置";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

			///#endregion 字段相关功能（不显示在菜单里）


			///#region 表单设置.
		map.AddGroupMethod("表单设置");
		rm = new RefMethod();
		rm.Title = "表单方案";
			//rm.Icon = "../../WF/Admin/CCFormDesigner/Img/Form.png";
		rm.Icon = "icon-present";
		rm.ClassMethodName = this.toString() + ".DoSheet";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手机表单字段顺序";
			//rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
		rm.Icon = "icon-layers";
			//rm.Icon = ../../Img/Mobile.png";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "审核组件";
		//rm.Icon = "../../WF/Img/Components.png";
		rm.Icon = "icon-puzzle";
		//rm.Icon = ../../Img/Mobile.png";
		rm.ClassMethodName = this.toString() + ".DoFrmNodeWorkCheck";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "父子流程组件";
		//rm.Icon = "../../WF/Img/Components.png";
		rm.Icon = "icon-puzzle";
		//rm.Icon = ../../Img/Mobile.png";
		rm.ClassMethodName = this.toString() + ".DoFrmSubFlow";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "轨迹组件";
		//rm.Icon = "../../WF/Img/Components.png";
		rm.Icon = "icon-puzzle";
		//rm.Icon = ../../Img/Mobile.png";
		rm.ClassMethodName = this.toString() + ".DoFrmTrack";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流转自定义组件";
		//rm.Icon = "../../WF/Img/Components.png";
		rm.Icon = "icon-puzzle";
		//rm.Icon = ../../Img/Mobile.png";
		rm.ClassMethodName = this.toString() + ".DoFrmTransferCustom";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "批量处理";
			//rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.Icon = "icon-calculator";

		rm.ClassMethodName = this.toString() + ".DoBatchStartFields()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "特别控件特别用户权限";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/SpecUserSpecFields.png";
		rm.ClassMethodName = this.toString() + ".DoSpecFieldsSpecUsers()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "表单设置";
		rm.Icon = "icon-note";
		map.AddRefMethod(rm);

			///#endregion 表单设置.


			///#region 考核属性.

		map.AddTBInt(NodeAttr.TAlertRole, 0, "逾期提醒规则", false, false); //"限期(天)"
		map.AddTBInt(NodeAttr.TAlertWay, 0, "逾期提醒方式", false, false); //"限期(天)
		map.AddTBInt(NodeAttr.WAlertRole, 0, "预警提醒规则", false, false); //"限期(天)"
		map.AddTBInt(NodeAttr.WAlertWay, 0, "预警提醒方式", false, false); //"限期(天)"

		map.AddTBFloat(NodeAttr.TCent, 2, "扣分(每延期1小时)", false, false);
		map.AddTBInt(NodeAttr.CHWay, 0, "考核方式", false, false); //"限期(天)"

			//考核相关.
		map.AddTBInt(NodeAttr.IsEval, 0, "是否工作质量考核", false, false);
		map.AddTBInt(NodeAttr.OutTimeDeal, 0, "超时处理方式", false, false);


			///#endregion 考核属性.


			///#region 父子流程.
		map.AddGroupMethod("父子流程");
		rm = new RefMethod();
		rm.Title = "父子流程表单组件";
			//rm.Icon = "../../WF/Admin/AttrNode/Img/SubFlows.png";
		rm.Icon = "icon-settings";
		rm.ClassMethodName = this.toString() + ".DoSubFlow";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "父子流程";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手动启动子流程"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoSubFlowHand";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "父子流程";
		rm.Icon = "icon-social-spotify";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "自动触发子流程"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoSubFlowAuto";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "父子流程";
			//rm.Icon = "icon-layers";
		rm.Icon = "icon-feed";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "延续子流程"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoSubFlowYanXu";
			//  rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "父子流程";
			//rm.Icon = "icon-layers";
		rm.Icon = "icon-organization";
		map.AddRefMethod(rm);

			///#endregion 父子流程.


			///#region 考核.
		map.AddGroupMethod("考核规则");
		rm = new RefMethod();
		rm.Title = "设置考核规则";
			//rm.Icon = "../../WF/Admin/CCFormDesigner/Img/CH.png";
		rm.Icon = "icon-book-open";
		rm.ClassMethodName = this.toString() + ".DoCHRole";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "考核规则";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "超时处理规则";
			//rm.Icon = "../../WF/Admin/CCFormDesigner/Img/OvertimeRole.png";
		rm.Icon = "icon-clock";
		rm.ClassMethodName = this.toString() + ".DoCHOvertimeRole";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "考核规则";
		map.AddRefMethod(rm);

			///#endregion 考核.


			///#region 实验中的功能
		map.AddGroupMethod("实验中的功能");
		rm = new RefMethod();
		rm.Title = "自定义属性(通用)";
		rm.ClassMethodName = this.toString() + ".DoSelfParas()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		rm.Visable = false;
		rm.Icon = "icon-layers";
		map.AddRefMethod(rm);

//		rm = new RefMethod();
//		rm.Title = "自定义属性(自定义)";
//		rm.ClassMethodName = this.toString() + ".DoNodeAttrExt()";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.GroupName = "实验中的功能";
//		rm.Visable = false;
//		rm.Icon = "icon-layers";
//		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置节点类型";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.Icon = "icon-layers";
		rm.ClassMethodName = this.toString() + ".DoNodeAppType()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
			//rm.Visable = false;
		map.AddRefMethod(rm);

		if (Glo.getCCBPMRunModel() != CCBPMRunModel.SAAS)
		{
			rm = new RefMethod();
			rm.Title = "设置为模版";

			String info = "如果把这个节点设置为模版,以后在新建节点的时候,就会按照当前的属性初始化节点数据.";
			info += "\t\n产生的数据文件存储在/DataUser/Xml/下.";
			rm.Warning = info;

				//  rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
			rm.ClassMethodName = this.toString() + ".DoSetTemplate()";
			rm.refMethodType = RefMethodType.Func;
			rm.GroupName = "实验中的功能";
			rm.Icon = "icon-layers";
				//rm.Visable = false;
			map.AddRefMethod(rm);
		}


		rm = new RefMethod();
		rm.Title = "批量设置节点属性";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/Node.png";
		rm.Icon = "icon-layers";
		rm.ClassMethodName = this.toString() + ".DoNodeAttrs()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "实验中的功能";
		rm.Visable = false;
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "设置独立表单树权限";
			//rm.Icon = ../../Img/Btn/DTS.gif";
			//rm.ClassMethodName = this.ToString() + ".DoNodeFormTree";
			//rm.refMethodType = RefMethodType.RightFrameOpen;
			//rm.GroupName = "实验中的功能";
			//map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "抄送人规则";
		rm.GroupName = "实验中的功能";
			//rm.Icon = "../../WF/Admin/AttrNode/Img/CC.png";
		rm.Icon = "icon-people";
		rm.ClassMethodName = this.toString() + ".DoCCer"; //要执行的方法名.
		rm.refMethodType = RefMethodType.RightFrameOpen; // 功能类型
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置提示信息";
		rm.GroupName = "实验中的功能";
			//     rm.Icon = "../../WF/Admin/AttrNode/Img/CC.png";
		rm.ClassMethodName = this.toString() + ".DoHelpRole"; //要执行的方法名.
		rm.RefAttrKey = BtnAttr.HelpRole; //帮助信息.
		rm.refMethodType = RefMethodType.LinkeWinOpen; // 功能类型
		rm.Icon = "icon-layers";
		map.AddRefMethod(rm);

			/*rm = new RefMethod();
			rm.Title = "退回扩展列";
			rm.ClassMethodName = this.ToString() + ".DtlOfReturn";
			rm.Visable = true;
			rm.refMethodType = RefMethodType.LinkModel;
			rm.RefAttrKey = NodeAttr.ReturnCHEnable;
			rm.Icon = "icon-layers";
			map.AddRefMethod(rm);*/

			///#endregion 实验中的功能

		this.set_enMap(map);
		return this.get_enMap();
	}




		///#region 考核规则.
	/** 
	 考核规则
	 
	 @return 
	*/
	public final String DoCHRole() throws Exception {
		return "../../Admin/AttrNode/EvaluationRole/Default.htm?FK_Node=" + this.getNodeID();
	}
	/** 
	 超时处理规则
	 
	 @return 
	*/
	public final String DoCHOvertimeRole() throws Exception {
		//return "../../Admin/AttrNode/CHOvertimeRole.htm?FK_Node=" + this.NodeID; 
		return "../../Admin/AttrNode/OvertimeRole/Default.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}

		///#endregion 考核规则.


		///#region 基础设置.
	/** 
	 批处理规则
	 
	 @return 
	*/
	public final String DoBatchStartFields() throws Exception {
		return "../../Admin/AttrNode/BatchRole/Default.htm?s=d34&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID();
	}
	/** 
	 批量修改节点属性
	 
	 @return 
	*/
	public final String DoNodeAttrs() throws Exception {
		return "../../Admin/AttrFlow/NodeAttrs.htm?NodeID=0&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 表单方案
	 
	 @return 
	*/
	public final String DoSheet() throws Exception {
		return "../../Admin/AttrNode/FrmSln/Default.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	public final String DoSheetOld() throws Exception {
		return "../../Admin/AttrNode/NodeFromWorkModel.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}

	/** 
	 接受人规则
	 
	 @return 
	*/
	public final String DoAccepterRoleNew() throws Exception {
		return "../../Admin/AttrNode/AccepterRole/Default.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	public final String DoTodolistModel() throws Exception {
		return "../../Admin/AttrNode/TodolistModel/Default.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}

	/** 
	 发送阻塞规则
	 
	 @return 
	*/
	public final String DoBlockModel() throws Exception {
		return "../../Admin/AttrNode/BlockModel/Default.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 发送后转向规则
	 
	 @return 
	*/
	public final String DoTurnToDeal() throws Exception {
		// return "../../Admin/AttrNode/TurnTo/0.TurntoDefault.htm?FK_Node=" + this.NodeID + "&FK_Flow=" + this.FK_Flow;
		return "../../Admin/AttrNode/TurnTo/Default.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();

	}
	/** 
	 抄送人规则
	 
	 @return 
	*/
	public final String DoCCer() throws Exception {
		return "../../Admin/AttrNode/CCRole.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 加载提示信息
	 
	 @return 
	*/
	public final String DoHelpRole() throws Exception {
		return "../../Admin/FoolFormDesigner/HelpRole.htm?NodeID=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}

		///#endregion


		///#region 表单相关.
	/** 
	 节点组件
	 
	 @return 
	*/
	public final String DoFrmNodeComponent() throws Exception {
		return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.FrmNodeComponent&PKVal=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}
	/** 
	 特别用户特殊字段权限.
	 
	 @return 
	*/
	public final String DoSpecFieldsSpecUsers() throws Exception {
		return "../../Admin/AttrNode/SepcFiledsSepcUsers.htm?FK_Flow=" + this.getFK_Flow() + "&FK_MapData=ND" + this.getNodeID() + "&FK_Node=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoSortingMapAttrs() throws Exception {
		return "../../Admin/MobileFrmDesigner/Default.htm?FK_Flow=" + this.getFK_Flow() + "&FK_MapData=ND" + this.getNodeID() + "&FK_Node=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}
	/// <summary>
	/// 审核组件
	/// </summary>
	/// <returns></returns>
	public String DoFrmNodeWorkCheck() throws Exception {
		return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.NodeWorkCheck&PKVal=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}
	/// <summary>
	/// 父子流程组件
	/// </summary>
	/// <returns></returns>
	public String DoFrmSubFlow() throws Exception {
		return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.SFlow.FrmSubFlow&PKVal=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}
	/// <summary>
	/// 轨迹组件
	/// </summary>
	/// <returns></returns>
	public String DoFrmTrack() throws Exception {
		return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.FrmTrack&PKVal=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}
	/// <summary>
	/// 流转自定义
	/// </summary>
	/// <returns></returns>
	public String DoFrmTransferCustom() throws Exception {
		return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.FrmTransferCustom&PKVal=" + this.getNodeID() + "&t=" + DataType.getCurrentDateTime();
	}

		///#endregion 表单相关.


		///#region 实验中的功能.
	/** 
	 设置模版
	 
	 @return 
	*/
	public final String DoSetTemplate() throws Exception {
		DataTable dt = this.ToDataTableField("Main");
		dt.TableName = "Node";
		dt.WriteXml(SystemConfig.getPathOfDataUser() + "/XML/DefaultNewNodeAttr.xml", dt);
		return "执行成功.";
	}
	/** 
	 自定义参数（通用）
	 
	 @return 
	*/
	public final String DoSelfParas() throws Exception {
		return "../../Admin/AttrNode/SelfParas.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 自定义参数（自定义）
	 
	 @return 
	*/
	public final String DoNodeAttrExt() throws Exception {
		return "../../../DataUser/OverrideFiles/NodeAttrExt.htm?FK_Flow=" + this.getNodeID();
	}

	/** 
	 设置节点类型
	 
	 @return 
	*/
	public final String DoNodeAppType() throws Exception {
		return "../../Admin/AttrNode/NodeAppType.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow() + "&tk=" + (new Random()).nextDouble();
	}

		///#endregion


		///#region 子流程。
	/** 
	 父子流程
	 
	 @return 
	*/
	public final String DoSubFlow() throws Exception {
		return "../../Comm/RefFunc/EnOnly.htm?EnName=BP.WF.Template.SFlow.FrmSubFlow&PK=" + this.getNodeID();
	}
	/** 
	 自动触发
	 
	 @return 
	*/
	public final String DoSubFlowAuto() throws Exception {
		return "../../Admin/AttrNode/SubFlow/SubFlowAuto.htm?FK_Node=" + this.getNodeID() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 手动启动子流程
	 
	 @return 
	*/
	public final String DoSubFlowHand() throws Exception {
		return "../../Admin/AttrNode/SubFlow/SubFlowHand.htm?FK_Node=" + this.getNodeID() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 延续子流程
	 
	 @return 
	*/
	public final String DoSubFlowYanXu() throws Exception {
		return "../../Admin/AttrNode/SubFlow/SubFlowYanXu.htm?FK_Node=" + this.getNodeID() + "&tk=" + (new Random()).nextDouble();
	}

		///#endregion 子流程。


	/** 
	 更新节点名称
	 
	 param name
	 @return 
	*/
	public final String Do_SaveAndUpdateNodeName(String name) throws Exception {
		//更新节点名称.
		DBAccess.RunSQL("UPDATE  WF_Node SET Name='" + name + "' WHERE NodeID=" + this.getNodeID());

		//修改表单名称.
		DBAccess.RunSQL("UPDATE  Sys_MapData SET Name='" + name + "' WHERE No='ND" + this.getNodeID() + "'");

		//修改分组名称.
		String oid = DBAccess.RunSQLReturnString("SELECT OID FROM Sys_GroupField WHERE FrmID='ND" + this.getNodeID() + "' ORDER BY Idx  ", null);
		if (oid == null)
		{
			return "更新成功.";
		}

		DBAccess.RunSQL("UPDATE   Sys_GroupField SET Lab='" + name + "' WHERE OID=" + oid);
		return "执行成功";
	}
	/** 
	 简单的更新节点名称
	 
	 param name 要更新的节点名称
	 @return 
	*/
	public final String Do_SaveNodeName(String name) throws Exception {
		//更新节点名称.
		DBAccess.RunSQL("UPDATE WF_Node SET Name='" + name + "' WHERE NodeID=" + this.getNodeID());

		// //修改表单名称.
		//     DBAccess.RunSQL("UPDATE SET Sys_MapDate SET Name='" + name + "' WHERE No='ND" + this.NodeID + "'");

		//修改分组名称.
		//  string oid = DBAccess.RunSQLReturnString("SELECT OID FROM Sys_GroupField WHERE FrmID='ND" + this.NodeID + "' ORDER BY Idx  ", null);
		//  if (oid == null)
		//     return "更新成功.";

		//  DBAccess.RunSQL("UPDATE SET Sys_GroupField SET Lab='" + name + "' WHERE OID=" + oid);
		return "执行成功";
	}


	public final String DoTurn() throws Exception {
		return "../../Admin/AttrNode/TurnTo.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
		//, "节点完成转向处理", "FrmTurn", 800, 500, 200, 300);
		//BP.WF.Node nd = new BP.WF.Node(this.NodeID);
		//return nd.DoTurn();
	}
	/** 
	 公文模板
	 
	 @return 
	*/
	public final String DocTemp() throws Exception {
		return "../../Admin/AttrNode/DocTemp.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 抄送规则
	 
	 @return 
	*/
	public final String DoCCRole() throws Exception {
		return "../../Comm/En.htm?EnName=BP.WF.Template.CCEn.CC&PKVal=" + this.getNodeID() + "&FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 个性化接受人窗口
	 
	 @return 
	*/
	public final String DoAccepter() throws Exception {
		return "../../Comm/En.htm?EnName=BP.WF.Template.Selector&PK=" + this.getNodeID();
	}
	/** 
	 可触发的子流程
	 
	 @return 
	*/
	public final String DoActiveFlows() throws Exception {
		return "../../Admin/ConditionSubFlow.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 退回节点
	 
	 @return 
	*/
	public final String DoCanReturnNodes() throws Exception {
		return "../../Admin/AttrNode/CanReturnNodes.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 撤销发送的节点
	 
	 @return 
	*/
	public final String DoCanCancelNodes() throws Exception {
		return "../../Admin/AttrNode/CanCancelNodes.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}

	/** 
	 流程完成条件
	 
	 @return 
	*/
	public final String DoCondFlow() throws Exception {
		return "../../Admin/Cond2020/List.htm?CondType=" + CondType.Flow.getValue() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&ToNodeID=" + this.getNodeID();
	}
	/** 
	 节点完成条件
	 
	 @return 
	*/
	public final String DoCondNode() throws Exception {
		return "../../Admin/Cond2020/List.htm?CondType=" + CondType.Node.getValue() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&ToNodeID=" + this.getNodeID();
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DoFormCol4() throws Exception {
		return "../../Admin/FoolFormDesigner/Designer.htm?PK=ND" + this.getNodeID();
	}
	/** 
	 设计自由表单
	 
	 @return 
	*/
	public final String DoFormFree() throws Exception {
		return "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData=ND" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 绑定独立表单
	 
	 @return 
	*/
	public final String DoFormTree() throws Exception {
		return "../../Admin/Sln/BindFrms.htm?ShowType=FlowFrms&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&Lang=CH";
	}

	public final String DoMapData() throws Exception {
		int i = this.GetValIntByKey(NodeAttr.FormType);

		// 类型.
		NodeFormType type = NodeFormType.forValue(i);
		switch (type)
		{
			case Develop:
				return "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData=ND" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
			default:
			case FoolForm:
				return "../../Admin/FoolFormDesigner/Designer.htm?PK=ND" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
		}
	}

	/** 
	 消息
	 
	 @return 
	*/
	public final String DoMessage() throws Exception {
		return "../../Admin/AttrNode/PushMessage.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 事件
	 
	 @return 
	*/
	public final String DoAction() throws Exception {
		return "../../Admin/AttrNode/Action.htm?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow() + "&tk=" + (new Random()).nextDouble();
	}

	/** 
	 保存提示信息
	 
	 @return 
	*/
	public final String SaveHelpAlert(String text) throws Exception {
		String file = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/HelpAlert/" + this.getNodeID() + ".htm";
		String folder = (new File(file)).getParent();
		//若文件夹不存在，则创建
		if ((new File(folder)).isDirectory() == false)
		{
			(new File(folder)).mkdirs();
		}

		DataType.WriteFile(file, text);
		return "保存成功！";
	}
	/** 
	 读取提示信息
	 
	 @return 
	*/
	public final String ReadHelpAlert() throws Exception {
		String doc = "";
		String file = bp.difference.SystemConfig.getPathOfDataUser() + "CCForm/HelpAlert/" + this.getNodeID() + ".htm";
		String folder = (new File(file)).getParent();
		if ((new File(folder)).isDirectory() != false)
		{
			if ((new File(file)).isFile())
			{
				doc = DataType.ReadTextFile(file);

			}
		}
		return doc;
	}
	public final String DtlOfReturn() throws Exception {
		String url = "../../Admin/FoolFormDesigner/MapDefDtlFreeFrm.htm?FK_MapDtl=BP.WF.ReturnWorks" + "&For=BP.WF.ReturnWorks&FK_Flow=" + this.getFK_Flow();
		return url;
	}
	@Override
	protected boolean beforeUpdate() throws Exception {
		//检查设计流程权限,集团模式下，不是自己创建的流程，不能设计流程.
		bp.wf.template.TemplateGlo.CheckPower(this.getFK_Flow());

		//更新流程版本
		Flow.UpdateVer(this.getFK_Flow());


			///#region 处理节点数据.
		Node nd = new Node(this.getNodeID());
		if (nd.isStartNode() == true)
		{
			//开始节点不能设置游离状态
			if (this.isYouLiTai() == true)
			{
				throw new RuntimeException("当前节点是开始节点不能设置游离状态");
			}
			if (this.getHuiQianRole() != HuiQianRole.None)
			{
				throw new RuntimeException("当前节点是开始节点不能启用会签按钮操作");
			}
			this.SetValByKey(BtnAttr.HungEnable, false);
			this.SetValByKey(BtnAttr.ThreadEnable, false); //子线程.
		}

		//是否是发送返回节点？
		nd.setSendBackNode(this.isSendBackNode());

		if (nd.isSendBackNode() == true)
		{
			//强制设置按照连接线控制.
			nd.setCondModel(DirCondModel.ByLineCond);
		}
		nd.DirectUpdate(); //直接更新.

		if (this.isSendBackNode() == true)
		{
			if (nd.getHisToNDNum() != 0)
			{
				this.setSendBackNode(false);
			}

			//    throw new Exception("err@您设置当前节点为【发送自动返回节点】，但是该节点配置到到达节点，是不正确的。");
		}



		//如果启动了会签,并且是抢办模式,强制设置为队列模式.或者组长模式.
		if (this.getHuiQianRole() != HuiQianRole.None)
		{
			if (this.getHuiQianRole() == bp.wf.HuiQianRole.Teamup)
			{
				DBAccess.RunSQL("UPDATE WF_Node SET TodolistModel=" + TodolistModel.Teamup.getValue() + "  WHERE NodeID=" + this.getNodeID());
			}

			if (this.getHuiQianRole() == bp.wf.HuiQianRole.TeamupGroupLeader)
			{
				DBAccess.RunSQL("UPDATE WF_Node SET TodolistModel=" + TodolistModel.TeamupGroupLeader.getValue() + ", TeamLeaderConfirmRole=" + TeamLeaderConfirmRole.HuiQianLeader.getValue() + " WHERE NodeID=" + this.getNodeID());
				if (this.getHuiQianLeaderRole() == HuiQianLeaderRole.OnlyOne && this.getAddLeaderEnable() == true)
				{
					throw new RuntimeException("当前节点是组长模式且组长只有一个，不能启用加主持人的操作");
					// this.AddLeaderEnable = false;
				}

			}
		}


		if (nd.getCondModel() == DirCondModel.ByLineCond)
		{
			/* 如果当前节点方向条件控制规则是按照连接线决定的, 
			 * 那就判断到达的节点的接受人规则，是否是按照上一步来选择，如果是就抛出异常.*/

			//获得到达的节点.
			Nodes nds = nd.getHisToNodes();
			for (Node mynd : nds.ToJavaList())
			{
				if (mynd.getHisDeliveryWay() == DeliveryWay.BySelected)
				{
					String errInfo = "设置矛盾:";
					errInfo += "@当前节点您设置的访问规则是按照方向条件控制的";
					errInfo += "但是到达的节点[" + mynd.getName() + "]的接收人规则是按照上一步选择的,设置矛盾.";
					// throw new Exception(errInfo);
				}
			}
		}

		//如果启用了在发送前打开, 当前节点的方向条件控制模式，是否是在下拉框边选择.?
		if (1 == 2 && nd.getCondModel() == DirCondModel.ByLineCond)
		{
			/*如果是启用了按钮，就检查当前节点到达的节点是否有【按照选择接受人】的方式确定接收人的范围. */
			Nodes nds = nd.getHisToNodes();
			for (Node mynd : nds.ToJavaList())
			{
				if (mynd.getHisDeliveryWay() == DeliveryWay.BySelected)
				{
					//强制设置安装人员选择器来选择.
					this.SetValByKey(NodeAttr.CondModel, DirCondModel.ByDDLSelected.getValue());
					break;
				}
			}
		}

			///#endregion 处理节点数据.


			///#region 创建审核组件附件
		if (this.getFWCAth() == FWCAth.MinAth)
		{
			FrmAttachment workCheckAth = new FrmAttachment();
			workCheckAth.setMyPK("ND" + this.getNodeID() + "_FrmWorkCheck");
			//不包含审核组件
			if (workCheckAth.RetrieveFromDBSources() == 0)
			{
				workCheckAth = new FrmAttachment();
				/*如果没有查询到它,就有可能是没有创建.*/
				workCheckAth.setMyPK("ND" + this.getNodeID() + "_FrmWorkCheck");
				workCheckAth.setFK_MapData("ND" + String.valueOf(this.getNodeID()));
				workCheckAth.setNoOfObj("FrmWorkCheck");
				workCheckAth.setExts("*.*");

				//存储路径.
				// workCheckAth.SaveTo = "/DataUser/UploadFile/";
				workCheckAth.setIsNote( false); //不显示note字段.
				workCheckAth.setIsVisable( false); // 让其在form 上不可见.

				//位置.

				workCheckAth.setH ((float)150);

				//多附件.
				workCheckAth.setUploadType(AttachmentUploadType.Multi);
				workCheckAth.setName("审核组件");
				workCheckAth.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=");
				workCheckAth.Insert();
			}
		}

			///#endregion 创建审核组件附件


			///#region 审核组件.
		GroupField gf = new GroupField();
		if (this.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Disable)
		{
			gf.Delete(GroupFieldAttr.FrmID, "ND" + this.getNodeID(), GroupFieldAttr.CtrlType, GroupCtrlType.FWC);
		}
		else
		{
			if (gf.IsExit(GroupFieldAttr.CtrlType, GroupCtrlType.FWC, GroupFieldAttr.FrmID, "ND" + this.getNodeID()) == false)
			{
				gf = new GroupField();
				gf.setEnName("ND" + this.getNodeID());
				gf.setCtrlType(GroupCtrlType.FWC);
				gf.setLab("审核信息");
				gf.setIdx(0);
				gf.Insert(); //插入.
			}
		}

			///#endregion 审核组件.

		BtnLab btnLab = new BtnLab(this.getNodeID());
		btnLab.RetrieveFromDBSources();

		//如果是合流. 就启用按钮.
		if (nd.isHL() == true)
		{
			this.SetValByKey(BtnAttr.ThreadEnable, true);
		}
		if(nd.getHisRunModel() == RunModel.SubThread && this.GetValBooleanByKey("ThreadEnable")==true){
			this.SetValByKey(BtnAttr.ThreadEnable, false);
			//throw new Exception("err@子线程不能启用子线程按钮，子线程操作只针对分流,合流,分合流节点有效") ;
		}
		//清除所有的缓存.
		Cash.ClearCash();

		return super.beforeUpdate();
	}
	@Override
	protected void afterInsertUpdateAction() throws Exception {
		Node fl = new Node();
		fl.setNodeID(this.getNodeID());
		fl.RetrieveFromDBSources();
		if (this.isYouLiTai() == true)
		{
			fl.SetPara("IsYouLiTai", 1);
		}
		else
		{
			fl.SetPara("IsYouLiTai", 0);
		}
		fl.Update();

		BtnLab btnLab = new BtnLab();
		btnLab.setNodeID(this.getNodeID());
		btnLab.RetrieveFromDBSources();
		Cash2019.UpdateRow(btnLab.toString(), String.valueOf(this.getNodeID()), btnLab.getRow());


		CC cc = new CC();
		cc.setNodeID(this.getNodeID());
		cc.RetrieveFromDBSources();
		Cash2019.UpdateRow(cc.toString(), String.valueOf(this.getNodeID()), cc.getRow());

		FrmNodeComponent frmNodeComponent = new FrmNodeComponent();
		frmNodeComponent.setNodeID(this.getNodeID());
		frmNodeComponent.RetrieveFromDBSources();
		Cash2019.UpdateRow(frmNodeComponent.toString(), String.valueOf(this.getNodeID()), frmNodeComponent.getRow());


		FrmTrack frmTrack = new FrmTrack();
		frmTrack.setNodeID(this.getNodeID());
		frmTrack.RetrieveFromDBSources();
		Cash2019.UpdateRow(frmTrack.toString(), String.valueOf(this.getNodeID()), frmTrack.getRow());

		FrmTransferCustom frmTransferCustom = new FrmTransferCustom();
		frmTransferCustom.setNodeID(this.getNodeID());
		frmTransferCustom.RetrieveFromDBSources();
		Cash2019.UpdateRow(frmTransferCustom.toString(), String.valueOf(this.getNodeID()), frmTransferCustom.getRow());

		NodeWorkCheck frmWorkCheck = new NodeWorkCheck();
		frmWorkCheck.setNodeID(this.getNodeID());
		frmWorkCheck.RetrieveFromDBSources();
		Cash2019.UpdateRow(frmWorkCheck.toString(), String.valueOf(this.getNodeID()), frmWorkCheck.getRow());

		NodeSheet nodeSheet = new NodeSheet();
		nodeSheet.setNodeID(this.getNodeID());
		nodeSheet.RetrieveFromDBSources();
		Cash2019.UpdateRow(nodeSheet.toString(), String.valueOf(this.getNodeID()), nodeSheet.getRow());

		NodeSimple nodeSimple = new NodeSimple();
		nodeSimple.setNodeID(this.getNodeID());
		nodeSimple.RetrieveFromDBSources();
		Cash2019.UpdateRow(nodeSimple.toString(), String.valueOf(this.getNodeID()), nodeSimple.getRow());

		FrmSubFlow frmSubFlow = new FrmSubFlow();
		frmSubFlow.setNodeID(this.getNodeID());
		frmSubFlow.RetrieveFromDBSources();
		Cash2019.UpdateRow(frmSubFlow.toString(), String.valueOf(this.getNodeID()), frmSubFlow.getRow());

		//GetTask getTask = new GetTask();
		//getTask.NodeID = this.NodeID;
		//getTask.RetrieveFromDBSources();
		//Cash2019.UpdateRow(getTask.ToString(), this.NodeID.ToString(), getTask.Row);

		//如果是组长会签模式，通用选择器只能单项选择
		if (this.getHuiQianRole() == HuiQianRole.TeamupGroupLeader && this.getHuiQianLeaderRole() == HuiQianLeaderRole.OnlyOne)
		{
			Selector selector = new Selector();
			selector.setNodeID(this.getNodeID());
			selector.RetrieveFromDBSources();
			selector.setSimpleSelector(true);
			selector.Update();

		}

		super.afterInsertUpdateAction();


		//写入日志.
		bp.sys.base.Glo.WriteUserLog("更新节点属性：" + this.getName() + " - " + this.getNodeID(), "通用操作");

	}

		///#endregion
}
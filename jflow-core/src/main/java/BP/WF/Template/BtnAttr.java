package BP.WF.Template;

import BP.WF.*;

/** 
 Btn属性
*/
public class BtnAttr extends BP.Sys.ToolbarExcelAttr
{
	/** 
	 节点ID
	*/
	public static final String NodeID = "NodeID";
	/** 
	 发送标签
	*/
	public static final String SendLab = "SendLab";
	/** 
	 子线程按钮是否启用
	*/
	public static final String ThreadEnable = "ThreadEnable";
	/** 
	 是否可以删除（已经发出去的）子线程.
	*/
	public static final String ThreadIsCanDel = "ThreadIsCanDel";
	/** 
	 是否可以移交
	*/
	public static final String ThreadIsCanShift = "ThreadIsCanShift";
	/** 
	 子线程按钮标签
	*/
	public static final String ThreadLab = "ThreadLab";
	/** 
	 子流程标签
	*/
	public static final String SubFlowLab = "SubFlowLab";
	/** 
	 子流程删除规则.
	*/
	public static final String SubFlowCtrlRole = "SubFlowCtrlRole";
	/** 
	 可否启用
	*/
	public static final String SubFlowEnable = "SubFlowEnable";
	/** 
	 保存是否启用
	*/
	public static final String SaveEnable = "SaveEnable";
	/** 
	 跳转规则
	*/
	public static final String JumpWayLab = "JumpWayLab";
	/** 
	 保存标签
	*/
	public static final String SaveLab = "SaveLab";
	/** 
	 退回是否启用
	*/
	public static final String ReturnRole = "ReturnRole";
	/** 
	 退回标签
	*/
	public static final String ReturnLab = "ReturnLab";
	/** 
	 退回的信息填写字段
	*/
	public static final String ReturnField = "ReturnField";
	/** 
	 打印单据标签
	*/
	public static final String PrintDocLab = "PrintDocLab";
	/** 
	 打印单据是否启用
	*/
	public static final String PrintDocEnable = "PrintDocEnable";
	/** 
	 移交是否启用
	*/
	public static final String ShiftEnable = "ShiftEnable";
	/** 
	 移交标签
	*/
	public static final String ShiftLab = "ShiftLab";
	/** 
	 查询标签
	*/
	public static final String SearchLab = "SearchLab";
	/** 
	 查询是否可用
	*/
	public static final String SearchEnable = "SearchEnable";
	/** 
	 轨迹
	*/
	public static final String TrackLab = "TrackLab";
	/** 
	 轨迹是否启用
	*/
	public static final String TrackEnable = "TrackEnable";
	/** 
	 抄送
	*/
	public static final String CCLab = "CCLab";
	/** 
	 抄送规则
	*/
	public static final String CCRole = "CCRole";
	/** 
	 删除
	*/
	public static final String DelLab = "DelLab";
	/** 
	 删除是否启用
	*/
	public static final String DelEnable = "DelEnable";
	/** 
	 结束流程
	*/
	public static final String EndFlowLab = "EndFlowLab";
	/** 
	 结束流程
	*/
	public static final String EndFlowEnable = "EndFlowEnable";
	/** 
	 发送按钮
	*/
	public static final String SendJS = "SendJS";
	/** 
	 挂起
	*/
	public static final String HungLab = "HungLab";
	/** 
	 是否启用挂起
	*/
	public static final String HungEnable = "HungEnable";
	/** 
	 查看父流程
	*/
	public static final String ShowParentFormLab = "ShowParentFormLab";
	/** 
	 是否启用查看父流程
	*/
	public static final String ShowParentFormEnable = "ShowParentFormEnable";
	/** 
	 审核
	*/
	public static final String WorkCheckLab = "WorkCheckLab";
	/** 
	 审核是否可用
	*/
	public static final String WorkCheckEnable = "WorkCheckEnable";
	/** 
	 批处理
	*/
	public static final String BatchLab = "BatchLab";
	/** 
	 批处理是否可用
	*/
	public static final String BatchEnable = "BatchEnable";
	/** 
	 加签
	*/
	public static final String AskforLab = "AskforLab";
	/** 
	 加签标签
	*/
	public static final String AskforEnable = "AskforEnable";

	/** 
	 会签标签
	*/
	public static final String HuiQianLab = "HuiQianLab";
	/** 
	 会签规则
	*/
	public static final String HuiQianRole = "HuiQianRole";
	/** 
	 会签组长模式
	*/
	public static final String HuiQianLeaderRole = "HuiQianLeaderRole";

	/** 
	 流转自定义 TransferCustomLab
	*/
	public static final String TCLab = "TCLab";
	/** 
	 是否启用-流转自定义
	*/
	public static final String TCEnable = "TCEnable";

	/** 
	 公文
	*/
	public static final String WebOfficeLab = "WebOffice";
	/** 
	 公文按钮标签
	*/
	public static final String WebOfficeEnable = "WebOfficeEnable";
	/** 
	 节点时限规则
	*/
	public static final String CHRole = "CHRole";
	/** 
	 节点时限lab
	*/
	public static final String CHLab = "CHLab";
	/** 
	 重要性 
	*/
	public static final String PRILab = "PRILab";
	/** 
	 是否启用-重要性
	*/
	public static final String PRIEnable = "PRIEnable";

	/** 
	 关注 
	*/
	public static final String FocusLab = "FocusLab";
	/** 
	 是否启用-关注
	*/
	public static final String FocusEnable = "FocusEnable";
	/** 
	 确认
	*/
	public static final String ConfirmLab = "ConfirmLab";
	/** 
	 是否启用确认
	*/
	public static final String ConfirmEnable = "ConfirmEnable";
	/** 
	 打印html
	*/
	public static final String PrintHtmlLab = "PrintHtmlLab";
	/** 
	 打印html
	*/
	public static final String PrintHtmlEnable = "PrintHtmlEnable";
	/** 
	 打印pdf
	*/
	public static final String PrintPDFLab = "PrintPDFLab";
	/** 
	 打印pdf
	*/
	public static final String PrintPDFEnable = "PrintPDFEnable";
	/** 
	 打印pdf规则
	*/
	public static final String PrintPDFModle = "PrintPDFModle";
	/** 
	 水印设置规则
	*/
	public static final String ShuiYinModle = "ShuiYinModle";
	/** 
	 打包下载
	*/
	public static final String PrintZipLab = "PrintZipLab";
	/** 
	 是否启用打包下载
	*/
	public static final String PrintZipEnable = "PrintZipEnable";

	/** 
	 分配
	*/
	public static final String AllotLab = "AllotLab";
	/** 
	 分配启用
	*/
	public static final String AllotEnable = "AllotEnable";
	/** 
	 选择接受人
	*/
	public static final String SelectAccepterLab = "SelectAccepterLab";
	/** 
	 是否启用选择接受人
	*/
	public static final String SelectAccepterEnable = "SelectAccepterEnable";

	/** 
	 备注
	*/
	public static final String NoteLab = "NoteLab";
	/** 
	*/
	//备注是否可用
	public static final String NoteEnable = "NoteEnable";

	/** 
	 帮助按钮
	*/
	public static final String HelpLab = "HelpLab";
	/** 
	 提示方式.
	*/
	public static final String HelpRole = "HelpRole";


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公文2019
	/** 
	 公文标签
	*/
	public static final String OfficeBtnLab = "OfficeBtnLab";
	/** 
	 公文标签接受人
	*/
	public static final String OfficeBtnEnable = "OfficeBtnEnable";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公文2019


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公文属性
	public static final String DocLeftWord = "DocLeftWord";
	public static final String DocRightWord = "DocRightWord";
	/** 
	 工作方式
	*/
	public static final String WebOfficeFrmModel = "WebOfficeFrmModel";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公文属性

	/** 
	 列表
	*/
	public static final String ListLab = "ListLab";
	/** 
	 是否启用-列表
	*/
	public static final String ListEnable = "ListEnable";
}
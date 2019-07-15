package BP.WF.Template;

/** 
 Btn属性
 
*/
public class BtnAttr extends BP.Sys.ToolbarExcelAttr
{
	 /// <summary>
    /// 节点ID
    /// </summary>
    public static final String NodeID = "NodeID";
    /// <summary>
    /// 发送标签
    /// </summary>
    public static final String SendLab = "SendLab";
    /// <summary>
    /// 子线程按钮是否启用
    /// </summary>
    public static final String ThreadEnable = "ThreadEnable";
    /// <summary>
    /// 是否可以删除（已经发出去的）子线程.
    /// </summary>
    public static final String ThreadIsCanDel = "ThreadIsCanDel";
    /// <summary>
    /// 是否可以移交
    /// </summary>
    public static final String ThreadIsCanShift = "ThreadIsCanShift";
    /// <summary>
    /// 子线程按钮标签
    /// </summary>
    public static final String ThreadLab = "ThreadLab";
    /// <summary>
    /// 子流程标签
    /// </summary>
    public static final String SubFlowLab = "SubFlowLab";
    /// <summary>
    /// 子流程删除规则.
    /// </summary>
    public static final String SubFlowCtrlRole = "SubFlowCtrlRole";
    /// <summary>
    /// 可否启用
    /// </summary>
    public static final String SubFlowEnable = "SubFlowEnable";
    /// <summary>
    /// 保存是否启用
    /// </summary>
    public static final String SaveEnable = "SaveEnable";
    /// <summary>
    /// 跳转规则
    /// </summary>
    public static final String JumpWayLab = "JumpWayLab";
    /// <summary>
    /// 保存标签
    /// </summary>
    public static final String SaveLab = "SaveLab";
    /// <summary>
    /// 退回是否启用
    /// </summary>
    public static final String ReturnRole = "ReturnRole";
    /// <summary>
    /// 退回标签
    /// </summary>
    public static final String ReturnLab = "ReturnLab";
    /// <summary>
    /// 退回的信息填写字段
    /// </summary>
    public static final String ReturnField = "ReturnField";
    /// <summary>
    /// 打印单据标签
    /// </summary>
    public static final String PrintDocLab = "PrintDocLab";
    /// <summary>
    /// 打印单据是否启用
    /// </summary>
    public static final String PrintDocEnable = "PrintDocEnable";
    /// <summary>
    /// 移交是否启用
    /// </summary>
    public static final String ShiftEnable = "ShiftEnable";
    /// <summary>
    /// 移交标签
    /// </summary>
    public static final String ShiftLab = "ShiftLab";
    /// <summary>
    /// 查询标签
    /// </summary>
    public static final String SearchLab = "SearchLab";
    /// <summary>
    /// 查询是否可用
    /// </summary>
    public static final String SearchEnable = "SearchEnable";
    /// <summary>
    /// 轨迹
    /// </summary>
    public static final String TrackLab = "TrackLab";
    /// <summary>
    /// 轨迹是否启用
    /// </summary>
    public static final String TrackEnable = "TrackEnable";
    /// <summary>
    /// 抄送
    /// </summary>
    public static final String CCLab = "CCLab";
    /// <summary>
    /// 抄送规则
    /// </summary>
    public static final String CCRole = "CCRole";
    /// <summary>
    /// 删除
    /// </summary>
    public static final String DelLab = "DelLab";
    /// <summary>
    /// 删除是否启用
    /// </summary>
    public static final String DelEnable = "DelEnable";
    /// <summary>
    /// 结束流程
    /// </summary>
    public static final String EndFlowLab = "EndFlowLab";
    /// <summary>
    /// 结束流程
    /// </summary>
    public static final String EndFlowEnable = "EndFlowEnable";
    /// <summary>
    /// 发送按钮
    /// </summary>
    public static final String SendJS = "SendJS";
    /// <summary>
    /// 挂起
    /// </summary>
    public static final String HungLab = "HungLab";
    /// <summary>
    /// 是否启用挂起
    /// </summary>
    public static final String HungEnable = "HungEnable";
    /// <summary>
    /// 审核
    /// </summary>
    public static final String WorkCheckLab = "WorkCheckLab";
    /// <summary>
    /// 审核是否可用
    /// </summary>
    public static final String WorkCheckEnable = "WorkCheckEnable";
    /// <summary>
    /// 批处理
    /// </summary>
    public static final String BatchLab = "BatchLab";
    /// <summary>
    /// 批处理是否可用
    /// </summary>
    public static final String BatchEnable = "BatchEnable";
    /// <summary>
    /// 加签
    /// </summary>
    public static final String AskforLab = "AskforLab";
    /// <summary>
    /// 加签标签
    /// </summary>
    public static final String AskforEnable = "AskforEnable";

    /// <summary>
    /// 会签标签
    /// </summary>
    public static final String HuiQianLab = "HuiQianLab";
    /// <summary>
    /// 会签规则
    /// </summary>
    public static final String HuiQianRole = "HuiQianRole";

    /// <summary>
    /// 流转自定义 TransferCustomLab
    /// </summary>
    public static final String TCLab = "TCLab";
    /// <summary>
    /// 是否启用-流转自定义
    /// </summary>
    public static final String TCEnable = "TCEnable";

    /// <summary>
    /// 公文
    /// </summary>
    public static final String WebOfficeLab = "WebOffice";
    /// <summary>
    /// 公文按钮标签
    /// </summary>
    public static final String WebOfficeEnable = "WebOfficeEnable";
    /// <summary>
    /// 是否启用-节点时限
    /// </summary>
    public static final String CHEnable = "CHEnable";
    /// <summary>
    /// lab
    /// </summary>
    public static final String CHLab = "CHLab";
    /// <summary>
    /// 重要性 
    /// </summary>
    public static final String PRILab = "PRILab";
    /// <summary>
    /// 是否启用-重要性
    /// </summary>
    public static final String PRIEnable = "PRIEnable";

    /// <summary>
    /// 关注 
    /// </summary>
    public static final String FocusLab = "FocusLab";
    /// <summary>
    /// 是否启用-关注
    /// </summary>
    public static final String FocusEnable = "FocusEnable";
    /// <summary>
    /// 确认
    /// </summary>
    public static final String ConfirmLab = "ConfirmLab";
    /// <summary>
    /// 是否启用确认
    /// </summary>
    public static final String ConfirmEnable = "ConfirmEnable";
    /// <summary>
    /// 打印html
    /// </summary>
    public static final String PrintHtmlLab = "PrintHtmlLab";
    /// <summary>
    /// 打印html
    /// </summary>
    public static final String PrintHtmlEnable = "PrintHtmlEnable";
    /// <summary>
    /// 打印pdf
    /// </summary>
    public static final String PrintPDFLab = "PrintPDFLab";
    /// <summary>
    /// 打印pdf
    /// </summary>
    public static final String PrintPDFEnable = "PrintPDFEnable";
    
   /// <summary>
    /// 打印pdf
    /// </summary>
    public static final String PrintPDFModle = "PrintPDFModle";
    /**
     * 打印水印规则
     */
    public static final String ShuiYinModle="ShuiYinModle";
    
    /// <summary>
    /// 打包下载
    /// </summary>
    public static final String PrintZipLab = "PrintZipLab";
    /// <summary>
    /// 是否启用打包下载
    /// </summary>
    public static final String PrintZipEnable = "PrintZipEnable";

    /// <summary>
    /// 分配
    /// </summary>
    public static final String AllotLab = "AllotLab";
    /// <summary>
    /// 分配启用
    /// </summary>
    public static final String AllotEnable = "AllotEnable";
    /// <summary>
    /// 选择接受人
    /// </summary>
    public static final String SelectAccepterLab = "SelectAccepterLab";
    /// <summary>
    /// 是否启用选择接受人
    /// </summary>
    public static final String SelectAccepterEnable = "SelectAccepterEnable";

   


    //#region 公文2019
    /// <summary>
    /// 公文标签
    /// </summary>
    public static final String OfficeBtnLab = "OfficeBtnLab";
    /// <summary>
    /// 公文标签接受人
    /// </summary>
    public static final String OfficeBtnEnable = "OfficeBtnEnable";
    //#endregion 公文2019 static final


  //  #region 公文属性
    public static final String DocLeftWord = "DocLeftWord";
    public static final String DocRightWord = "DocRightWord";
    /// <summary>
    /// 工作方式
    /// </summary>
    public static final String WebOfficeFrmModel = "WebOfficeFrmModel";
   // #endregion 公文属性

    /// <summary>
    /// 列表
    /// </summary>
    public static final String ListLab = "ListLab";
    /// <summary>
    /// 是否启用-列表
    /// </summary>
    public static final String ListEnable = "ListEnable";
    
    /// <summary>
    /// 备注
    /// </summary>
    public static final String NoteLab = "NoteLab";
    /// <summary>
    //备注是否可用
    /// </summary>
    public static final String NoteEnable = "NoteEnable";
}
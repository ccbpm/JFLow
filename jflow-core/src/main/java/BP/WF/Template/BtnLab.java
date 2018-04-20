package BP.WF.Template;



import BP.DA.Depositary;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;
import BP.WF.CCRole;
import BP.WF.HuiQianRole;
import BP.WF.JumpWay;
import BP.WF.SubFlowCtrlRole;

/** 
 节点按钮权限
*/
public class BtnLab extends Entity
{
	
	/// <summary>
    /// 会签规则
    /// </summary>
    public final HuiQianRole getHuiQianRole()
    {
         return HuiQianRole.forValue( this.GetValIntByKey(BtnAttr.HuiQianRole));
    }
    /// <summary>
    /// 会签标签
    /// </summary>
    public String getHuiQianLab()
    {
            return this.GetValStringByKey(BtnAttr.HuiQianLab);
    }
	/** 
	 访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		uac.IsDelete = false;
		return uac;
	}
	/** 
	 but
	*/
	public static String getBtns()
	{
		return "Send,Save,Thread,Return,CC,Shift,Del,Rpt,Ath,Track,Opt,EndFlow,SubFlow";
	}
	/** 
	 PK
	*/
	@Override
	public String getPK()
	{
		return NodeAttr.NodeID;
	}
	/** 
	 节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(BtnAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(BtnAttr.NodeID, value);
	}
	/** 
	 查询标签
	*/
	public final String getSearchLab()
	{
		return this.GetValStringByKey(BtnAttr.SearchLab);
	}
	public final void setSearchLab(String value)
	{
		this.SetValByKey(BtnAttr.SearchLab, value);
	}
	/** 
	 查询是否可用
	*/
	public final boolean getSearchEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.SearchEnable);
	}
	public final void setSearchEnable(boolean value)
	{
		this.SetValByKey(BtnAttr.SearchEnable, value);
	}
	/** 
	 移交
	*/
	public final String getShiftLab()
	{
		return this.GetValStringByKey(BtnAttr.ShiftLab);
	}
	public final void setShiftLab(String value)
	{
		this.SetValByKey(BtnAttr.ShiftLab, value);
	}
	/** 
	 是否启用移交
	*/
	public final boolean getShiftEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.ShiftEnable);
	}
	public final void setShiftEnable(boolean value)
	{
		this.SetValByKey(BtnAttr.ShiftEnable, value);
	}
	/** 
	 选择接受人
	*/
	public final String getSelectAccepterLab()
	{
		return this.GetValStringByKey(BtnAttr.SelectAccepterLab);
	}
	/** 
	 选择接受人类型
	*/
	public final int getSelectAccepterEnable()
	{
		return this.GetValIntByKey(BtnAttr.SelectAccepterEnable);
	}
	public final void setSelectAccepterEnable(int value)
	{
		this.SetValByKey(BtnAttr.SelectAccepterEnable, value);
	}
	/** 
	 保存
	*/
	public final String getSaveLab()
	{
		return this.GetValStringByKey(BtnAttr.SaveLab);
	}
	/** 
	 是否启用保存
	*/
	public final boolean getSaveEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.SaveEnable);
	}
	/** 
	 子线程按钮标签
	*/
	public final String getThreadLab()
	{
		return this.GetValStringByKey(BtnAttr.ThreadLab);
	}
	/** 
	 子线程按钮是否启用
	*/
	public final boolean getThreadEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadEnable);
	}
	/** 
	 是否可以删除（当前分流，分合流节点发送出去的）子线程.
	*/
	public final boolean getThreadIsCanDel()
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanDel);
	}
	/** 
	 是否可以移交.
	*/
	public final boolean getThreadIsCanShift()
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanShift);
	}
	/** 
	 子流程按钮标签
	*/
	public final String getSubFlowLab()
	{
		return this.GetValStringByKey(BtnAttr.SubFlowLab);
	}
	public final SubFlowCtrlRole getSubFlowCtrlRole()
	{
		return SubFlowCtrlRole.forValue(this.GetValIntByKey(BtnAttr.SubFlowCtrlRole));
	}
	/** 
	 跳转标签
	*/
	public final String getJumpWayLab()
	{
		return this.GetValStringByKey(BtnAttr.JumpWayLab);
	}
	public final JumpWay getJumpWayEnum()
	{
		return JumpWay.forValue(this.GetValIntByKey(NodeAttr.JumpWay));
	}
	/** 
	 是否启用跳转
	*/
	public final boolean getJumpWayEnable()
	{
		return this.GetValBooleanByKey(NodeAttr.JumpWay);
	}
	/** 
	 退回标签
	*/
	public final String getReturnLab()
	{
		return this.GetValStringByKey(BtnAttr.ReturnLab);
	}
	/** 
	 退回字段
	*/
	public final String getReturnField()
	{
		return this.GetValStringByKey(BtnAttr.ReturnField);
	}
	/** 
	 跳转是否启用
	*/
	public final boolean getReturnEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.ReturnRole);
	}
	/** 
	 挂起标签
	*/
	public final String getHungLab()
	{
		return this.GetValStringByKey(BtnAttr.HungLab);
	}
	/** 
	 是否启用挂起
	*/
	public final boolean getHungEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.HungEnable);
	}
	/** 
	 打印标签
	*/
	public final String getPrintDocLab()
	{
		return this.GetValStringByKey(BtnAttr.PrintDocLab);
	}
	/** 
	 是否启用打印
	*/
	public final boolean getPrintDocEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.PrintDocEnable);
	}
	/** 
	 发送标签
	*/
	public final String getSendLab()
	{
		return this.GetValStringByKey(BtnAttr.SendLab);
	}
	/** 
	 是否启用发送?
	*/
	public final boolean getSendEnable()
	{
		return true;
	}
	/** 
	 发送的Js代码
	*/
	public final String getSendJS()
	{
		String str = this.GetValStringByKey(BtnAttr.SendJS).replace("~", "'");
		if (this.getCCRole() == BP.WF.CCRole.WhenSend)
		{
			str = str + "  if ( OpenCC()==false) return false;";
		}
		return str;
	}
	/** 
	 轨迹标签
	*/
	public final String getTrackLab()
	{
		return this.GetValStringByKey(BtnAttr.TrackLab);
	}
	/** 
	 是否启用轨迹
	*/
	public final boolean getTrackEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.TrackEnable);
	}
	/** 
	 抄送标签
	*/
	public final String getCCLab()
	{
		return this.GetValStringByKey(BtnAttr.CCLab);
	}
	/** 
	 抄送规则
	*/
	public final CCRole getCCRole()
	{
		return CCRole.forValue(this.GetValIntByKey(BtnAttr.CCRole));
	}
	/** 
	 删除标签
	 
	*/
	public final String getDeleteLab()
	{
		return this.GetValStringByKey(BtnAttr.DelLab);
	}
	/** 
	 删除类型
	 
	*/
	public final int getDeleteEnable()
	{
		return this.GetValIntByKey(BtnAttr.DelEnable);
	}
	/** 
	 结束流程
	 
	*/
	public final String getEndFlowLab()
	{
		return this.GetValStringByKey(BtnAttr.EndFlowLab);
	}
	/** 
	 是否启用结束流程
	 
	*/
	public final boolean getEndFlowEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.EndFlowEnable);
	}
	/** 
	 是否启用流转自定义
	 
	*/
	public final String getTCLab()
	{
		return this.GetValStringByKey(BtnAttr.TCLab);
	}
	/** 
	 是否启用流转自定义
	 
	*/
	public final boolean getTCEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.TCEnable);
	}
	public final void setTCEnable(boolean value)
	{
		this.SetValByKey(BtnAttr.TCEnable, value);
	}

	/** 
	 审核标签
	 
	*/
	public final String getWorkCheckLab()
	{
		return this.GetValStringByKey(BtnAttr.WorkCheckLab);
	}
	/** 
	 标签是否启用？
	 
	*/
	public final boolean getSubFlowEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.SubFlowEnable);
	}
	public final void setSubFlowEnable(boolean value)
	{
		this.SetValByKey(BtnAttr.SubFlowEnable, value);
	}
	/** 
	 审核是否可用
	 
	*/
	public final boolean getWorkCheckEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.WorkCheckEnable);
	}
	public final void setWorkCheckEnable(boolean value)
	{
		this.SetValByKey(BtnAttr.WorkCheckEnable, value);
	}
	/** 
	 考核 是否可用
	 
	*/
	public final boolean getCHEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.CHEnable);
	}
	/** 
	 考核 标签
	 
	*/
	public final String getCHLab()
	{
		return this.GetValStringByKey(BtnAttr.CHLab);
	}
	/** 
	 重要性 是否可用
	 
	*/
	public final boolean getPRIEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.PRIEnable);
	}
	/** 
	 重要性 标签
	 
	*/
	public final String getPRILab()
	{
		return this.GetValStringByKey(BtnAttr.PRILab);
	}
	/** 
	 关注 是否可用
	 
	*/
	public final boolean getFocusEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.FocusEnable);
	}
	/** 
	 关注 标签
	 
	*/
	public final String getFocusLab()
	{
		return this.GetValStringByKey(BtnAttr.FocusLab);
	}
	/** 
	 批量处理是否可用
	 
	*/
	public final boolean getBatchEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.BatchEnable);
	}
	/** 
	 批处理标签
	 
	*/
	public final String getBatchLab()
	{
		return this.GetValStringByKey(BtnAttr.BatchLab);
	}
	/** 
	 加签
	 
	*/
	public final boolean getAskforEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.AskforEnable);
	}
	/** 
	 加签
	 
	*/
	public final String getAskforLab()
	{
		return this.GetValStringByKey(BtnAttr.AskforLab);
	}
	/** 
	是否启用文档,@0=不启用@1=按钮方式@2=公文在前@3=表单在前
	 
	*/
	public int getWebOfficeEnable()
	{
		return this.GetValIntByKey(BtnAttr.WebOfficeEnable);
	}
	/** 
	 公文的工作模式 @0=不启用@1=按钮方式@2=标签页置后方式@3=标签页置前方式
	 
	*/
	public final WebOfficeWorkModel getWebOfficeWorkModel()
	{
		return WebOfficeWorkModel.forValue(this.getWebOfficeEnable());
	}
	public final void setWebOfficeWorkModel(WebOfficeWorkModel value)
	{
		this.SetValByKey(BtnAttr.WebOfficeEnable, value.getValue());
	}

	/** 
	 文档按钮标签
	 
	*/
	public final String getWebOfficeLab()
	{
		return this.GetValStringByKey(BtnAttr.WebOfficeLab);
	}
	/** 
	 打开本地文件
	 
	*/
	public final boolean getOfficeOpenEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeOpenEnable);
	}
	/** 
	 打开本地标签      
	 
	*/
	public final String getOfficeOpenLab()
	{
		return this.GetValStrByKey(BtnAttr.OfficeOpenLab);
	}
	/** 
	 打开模板
	 
	*/
	public final boolean getOfficeOpenTemplateEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeOpenTemplateEnable);
	}
	/** 
	 打开模板标签
	 
	*/
	public final String getOfficeOpenTemplateLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeOpenTemplateLab);
	}
	/** 
	 保存按钮
	 
	*/
	public final boolean getOfficeSaveEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeSaveEnable);
	}
	/** 
	 保存标签
	 
	*/
	public final String getOfficeSaveLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeSaveLab);
	}
	/** 
	 接受修订
	 
	*/
	public final boolean getOfficeAcceptEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeAcceptEnable);
	}
	/** 
	 接受修订标签
	 
	*/
	public final String getOfficeAcceptLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeAcceptLab);
	}
	/** 
	 拒绝修订
	 
	*/
	public final boolean getOfficeRefuseEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeRefuseEnable);
	}
	/** 
	 拒绝修订标签
	 
	*/
	public final String getOfficeRefuseLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeRefuseLab);
	}
	public final String getOfficeOVerLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeOverLab);
	}
	/** 
	 是否套红
	 
	*/
	public final boolean getOfficeOverEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeOverEnable);
	}
	/** 
	 套红按钮标签
	 
	*/
	public final String getOfficeOverLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeOverLab);
	}
	/** 
	 是否打印
	 
	*/
	public final boolean getOfficePrintEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficePrintEnable);
	}
	/** 
	 是否查看用户留痕
	 
	*/
	public final boolean getOfficeMarksEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeMarksEnable);
	}
	/** 
	 打印按钮标签
	 
	*/
	public final String getOfficePrintLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficePrintLab);
	}
	/** 
	 签章按钮
	 
	*/
	public final boolean getOfficeSealEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeSealEnable);
	}
	/** 
	 签章标签
	 
	*/
	public final String getOfficeSealLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeSealLab);
	}

	/** 
	插入流程
	 
	*/
	public final boolean getOfficeInsertFlowEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeInsertFlowEnable);
	}
	/** 
	 流程标签
	 
	*/
	public final String getOfficeInsertFlowLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeInsertFlowLab);
	}


	/** 
	 是否自动记录节点信息
	 
	*/
	public final boolean getOfficeNodeInfo()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeNodeInfo);
	}

	/** 
	 是否自动记录节点信息
	 
	*/
	public final boolean getOfficeReSavePDF()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeReSavePDF);
	}

	/** 
	 是否进入留痕模式
	 
	*/
	public final boolean getOfficeIsMarks()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeIsMarks);
	}

	/** 
	 风险点模板
	 
	*/
	public final String getOfficeFengXianTemplate()
	{
		return this.GetValStringByKey(BtnAttr.OfficeFengXianTemplate);
	}

	public final boolean getOfficeReadOnly()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeReadOnly);
	}

	/** 
	 下载按钮标签
	 
	*/
	public final String getOfficeDownLab()
	{
		return this.GetValStringByKey(BtnAttr.OfficeDownLab);
	}
	/** 
	 下载按钮标签
	 
	*/
	public final boolean getOfficeIsDown()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeDownEnable);
	}

	/** 
	 是否启用下载
	 
	*/
	public final boolean getOfficeDownEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeDownEnable);
	}

	/** 
	 指定文档模板
	 
	*/
	public final String getOfficeTemplate()
	{
		return this.GetValStringByKey(BtnAttr.OfficeTemplate);
	}


	/** 
	 是否使用父流程的文档
	 
	*/
	public final boolean getOfficeIsParent()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeIsParent);
	}

	/** 
	 是否自动套红
	 
	*/
	public final boolean getOfficeTHEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeTHEnable);
	}
	/** 
	 自动套红模板
	 
	*/
	public final String getOfficeTHTemplate()
	{
		return this.GetValStringByKey(BtnAttr.OfficeTHTemplate);
	}

		///#endregion


		
	/** 
	 Btn
	 
	*/
	public BtnLab()
	{
	}
	/** 
	 节点按钮权限
	 
	 @param nodeid 节点ID
	 * @throws Exception 
	*/
	public BtnLab(int nodeid) throws Exception
	{
		this.setNodeID(nodeid);
		this.RetrieveFromDBSources();
	}
	/** 分配 是否可用
	 
	*/
	public final boolean getAllotEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.AllotEnable);
	}
	/** 
	 分配 标签
	 
	 */
	public final String getAllotLab()
	{
		return this.GetValStringByKey(BtnAttr.AllotLab);
	}
	/** 确认 是否可用
	 
	*/
	public final boolean getConfirmEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.ConfirmEnable);
	}
	/** 
	 确认标签
	 
	*/
	public final String getConfirmLab()
	{
		return this.GetValStringByKey(BtnAttr.ConfirmLab);
	}
	
	/** 
	 打包下载 是否可用
	 
*/
	public final boolean getPrintZipEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.PrintZipEnable);
	}
	/** 
	 打包下载 标签
	 
	*/
	public final String getPrintZipLab()
	{
		return this.GetValStringByKey(BtnAttr.PrintZipLab);
	}


	/** 
	 pdf 是否可用
	 
	*/
	public final boolean getPrintPDFEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.PrintPDFEnable);
	}
	/** 
	 打包下载 标签
	 
	*/
	public final String getPrintPDFLab()
	{
		return this.GetValStringByKey(BtnAttr.PrintPDFLab);
	}

	/** 
	 html 是否可用
	 
	*/
	public final boolean getPrintHtmlEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.PrintHtmlEnable);
	}
	/** 
	 html 标签
	 
	*/
	public final String getPrintHtmlLab()
	{
		return this.GetValStringByKey(BtnAttr.PrintHtmlLab);
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

		Map map = new Map("WF_Node", "节点标签");

		map.Java_SetDepositaryOfEntity(Depositary.Application);

		map.AddTBIntPK(BtnAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(BtnAttr.Name, null, "节点名称", true, true, 0, 200, 10);

		map.AddTBString(BtnAttr.SendLab, "发送", "发送按钮标签", true, false, 0, 50, 10);
		map.AddTBString(BtnAttr.SendJS, "", "发送按钮JS函数", true, false, 0, 50, 10, true);

			//map.AddBoolean(BtnAttr.SendEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.JumpWayLab, "跳转", "跳转按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(NodeAttr.JumpWay, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.SaveLab, "保存", "保存按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SaveEnable, true, "是否启用", true, true);


		map.AddTBString(BtnAttr.ThreadLab, "子线程", "子线程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ThreadEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.ThreadIsCanDel, false, "是否可以删除子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true, true);
		map.AddBoolean(BtnAttr.ThreadIsCanShift, false, "是否可以移交子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true, true);


		map.AddTBString(BtnAttr.SubFlowLab, "子流程", "子流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SubFlowEnable, false, "是否启用", true, true);


		map.AddTBString(BtnAttr.ReturnLab, "退回", "退回按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ReturnRole, true, "是否启用", true, true);
		map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0, 50, 10, true);


		map.AddTBString(BtnAttr.CCLab, "抄送", "抄送按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.CCRole, 0, "抄送规则", true, true, BtnAttr.CCRole);

			//map.AddBoolean(BtnAttr, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.ShiftLab, "移交", "移交按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ShiftEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.DelLab, "删除流程", "删除流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.DelEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.EndFlowLab, "结束流程", "结束流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.EndFlowEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.HungLab, "挂起", "挂起按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.HungEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.PrintDocLab, "打印单据", "打印单据按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintDocEnable, false, "是否启用", true, true);
		
		
        map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
        map.AddBoolean(BtnAttr.HuiQianRole, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.AthLab, "附件", "附件按钮标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.FJOpen, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.TrackLab, "轨迹", "轨迹按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.TrackEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.SelectAccepterLab, "接受人", "接受人按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.SelectAccepterEnable, 0, "方式", true, true, BtnAttr.SelectAccepterEnable);

			// map.AddBoolean(BtnAttr.SelectAccepterEnable, false, "是否启用", true, true);
			//map.AddTBString(BtnAttr.OptLab, "选项", "选项按钮标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.OptEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.SearchLab, "查询", "查询按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SearchEnable, true, "是否启用", true, true);

			// 
		map.AddTBString(BtnAttr.WorkCheckLab, "审核", "审核按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.WorkCheckEnable, false, "是否启用", true, true);

			// 
		map.AddTBString(BtnAttr.BatchLab, "批量审核", "批量审核标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.BatchEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.AskforLab, "加签", "加签标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.AskforEnable, false, "是否启用", true, true);

			// add by stone 2014-11-21. 让用户可以自己定义流转.
		map.AddTBString(BtnAttr.TCLab, "流转自定义", "流转自定义", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.TCEnable, false, "是否启用", true, true);


		map.AddTBString(BtnAttr.WebOfficeLab, "公文", "公文标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.WebOfficeEnable, false, "是否启用", true, true);
		map.AddDDLSysEnum(BtnAttr.WebOfficeEnable, 0, "文档启用方式", true, true, BtnAttr.WebOfficeEnable, "@0=不启用@1=按钮方式@2=标签页置后方式@3=标签页置前方式"); //edited by liuxc,2016-01-18,from xc.

			// add by 周朋 2015-08-06. 重要性.
		map.AddTBString(BtnAttr.PRILab, "重要性", "重要性", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PRIEnable, false, "是否启用", true, true);

			// add by 周朋 2015-08-06. 节点时限.
		map.AddTBString(BtnAttr.CHLab, "节点时限", "节点时限", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.CHEnable, false, "是否启用", true, true);

		// add 2017.5.4  邀请其他人参与当前的工作.
        map.AddTBString(BtnAttr.AllotLab, "分配", "分配按钮标签", true, false, 0, 50, 10);
        map.AddBoolean(BtnAttr.AllotEnable, false, "是否启用", true, true);
        
			// add by 周朋 2015-12-24. 节点时限.
		map.AddTBString(BtnAttr.FocusLab, "关注", "关注", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.FocusEnable, true, "是否启用", true, true);
	    // add 2017.5.4 确认就是告诉发送人，我接受这件工作了.
        map.AddTBString(BtnAttr.ConfirmLab, "确认", "确认按钮标签", true, false, 0, 50, 10);
        map.AddBoolean(BtnAttr.ConfirmEnable, false, "是否启用", true, true);

        // add 2017.9.1 for 天业集团.
        map.AddTBString(BtnAttr.PrintHtmlLab, "打印Html", "打印Html标签", true, false, 0, 50, 10);
        map.AddBoolean(BtnAttr.PrintHtmlEnable, false, "是否启用", true, true);

        // add 2017.9.1 for 天业集团.
        map.AddTBString(BtnAttr.PrintPDFLab, "打印pdf", "打印pdf标签", true, false, 0, 50, 10);
        map.AddBoolean(BtnAttr.PrintPDFEnable, false, "是否启用", true, true);

        // add 2017.9.1 for 天业集团.
        map.AddTBString(BtnAttr.PrintZipLab, "打包下载", "打包下载zip按钮标签", true, false, 0, 50, 10);
        map.AddBoolean(BtnAttr.PrintZipEnable, false, "是否启用", true, true);



			///#region 公文按钮
		map.AddTBString(BtnAttr.OfficeOpenLab, "打开本地", "打开本地标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOpenTemplateLab, "打开模板", "打开模板标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenTemplateEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSaveLab, "保存", "保存标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSaveEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeAcceptLab, "接受修订", "接受修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeAcceptEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeRefuseLab, "拒绝修订", "拒绝修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeRefuseEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOverLab, "套红", "套红标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOverEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeMarksEnable, true, "是否查看用户留痕", true, true, true);

		map.AddTBString(BtnAttr.OfficePrintLab, "打印", "打印标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficePrintEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSealLab, "签章", "签章标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSealEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeInsertFlowLab, "插入流程", "插入流程标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeInsertFlowEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeNodeInfo, false, "是否记录节点信息", true, true);
		map.AddBoolean(BtnAttr.OfficeReSavePDF, false, "是否该自动保存为PDF", true, true);


		map.AddTBString(BtnAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeDownEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeIsMarks, true, "是否进入留痕模式", true, true);
		map.AddTBString(BtnAttr.OfficeTemplate, "", "指定文档模板", true, false, 0, 100, 10);
		map.AddBoolean(BtnAttr.OfficeIsParent, true, "是否使用父流程的文档", true, true);

		map.AddBoolean(BtnAttr.OfficeTHEnable, false, "是否自动套红", true, true);
		map.AddTBString(BtnAttr.OfficeTHTemplate, "", "自动套红模板", true, false, 0, 200, 10);

		// add 2017.5.4 确认就是告诉发送人，我接受这件工作了.
//        map.AddTBString(BtnAttr.ConfirmLab, "确认", "确认按钮标签", true, false, 0, 50, 10);
//        map.AddBoolean(BtnAttr.ConfirmEnable, false, "是否启用", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
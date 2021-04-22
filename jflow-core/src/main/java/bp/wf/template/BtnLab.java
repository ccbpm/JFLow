package bp.wf.template;

import bp.en.*;
import bp.en.Map;
import bp.wf.*;

/** 
 节点按钮权限
*/
public class BtnLab extends Entity
{
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


		///基本属性
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
	 * @throws Exception 
	*/
	public final int getNodeID()  throws Exception
	{
		return this.GetValIntByKey(BtnAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(BtnAttr.NodeID, value);
	}
	/** 
	 名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.Name, value);
	}
	/** 
	 查询标签
	 * @throws Exception 
	*/
	public final String getSearchLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SearchLab);
	}
	public final void setSearchLab(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.SearchLab, value);
	}
	/** 
	 查询是否可用
	 * @throws Exception 
	*/
	public final boolean getSearchEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.SearchEnable);
	}
	public final void setSearchEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.SearchEnable, value);
	}
	/** 
	 移交
	 * @throws Exception 
	*/
	public final String getShiftLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ShiftLab);
	}
	public final void setShiftLab(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.ShiftLab, value);
	}
	/** 
	 是否启用移交
	 * @throws Exception 
	*/
	public final boolean getShiftEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ShiftEnable);
	}
	public final void setShiftEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.ShiftEnable, value);
	}
	/** 
	 选择接受人
	 * @throws Exception 
	*/
	public final String getSelectAccepterLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SelectAccepterLab);
	}
	/** 
	 选择接受人类型
	 * @throws Exception 
	*/
	public final int getSelectAccepterEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.SelectAccepterEnable);
	}
	public final void setSelectAccepterEnable(int value) throws Exception
	{
		this.SetValByKey(BtnAttr.SelectAccepterEnable, value);
	}
	/** 
	 保存
	 * @throws Exception 
	*/
	public final String getSaveLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SaveLab);
	}
	/** 
	 是否启用保存
	 * @throws Exception 
	*/
	public final boolean getSaveEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.SaveEnable);
	}
	/** 
	 子线程按钮标签
	 * @throws Exception 
	*/
	public final String getThreadLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ThreadLab);
	}
	/** 
	 子线程按钮是否启用
	 * @throws Exception 
	*/
	public final boolean getThreadEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadEnable);
	}
	/** 
	 是否可以删除（当前分流，分合流节点发送出去的）子线程.
	 * @throws Exception 
	*/
	public final boolean getThreadIsCanDel() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanDel);
	}
	/** 
	 是否可以移交.
	 * @throws Exception 
	*/
	public final boolean getThreadIsCanShift() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanShift);
	}
	/** 
	 子流程按钮标签
	 * @throws Exception 
	*/
	public final String getSubFlowLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SubFlowLab);
	}
	/** 
	 跳转标签
	 * @throws Exception 
	*/
	public final String getJumpWayLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.JumpWayLab);
	}
	public final JumpWay getJumpWayEnum() throws Exception
	{
		return JumpWay.forValue(this.GetValIntByKey(NodeAttr.JumpWay));
	}
	/** 
	 是否启用跳转
	 * @throws Exception 
	*/
	public final boolean getJumpWayEnable() throws Exception
	{
		return this.GetValBooleanByKey(NodeAttr.JumpWay);
	}
	/** 
	 退回标签
	 * @throws Exception 
	*/
	public final String getReturnLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ReturnLab);
	}
	/** 
	 退回字段
	 * @throws Exception 
	*/
	public final String getReturnField() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ReturnField);
	}
	/** 
	 退回是否启用
	 * @throws Exception 
	*/
	public final boolean getReturnEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ReturnRole);
	}
	/** 
	 挂起标签
	 * @throws Exception 
	*/
	public final String getHungLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.HungLab);
	}
	/** 
	 是否启用挂起
	 * @throws Exception 
	*/
	public final boolean getHungEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.HungEnable);
	}
	/** 
	 打印标签
	 * @throws Exception 
	*/
	public final String getPrintDocLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintDocLab);
	}
	/** 
	 是否启用打印
	 * @throws Exception 
	*/
	public final boolean getPrintDocEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintDocEnable);
	}
	/** 
	 发送标签
	 * @throws Exception 
	*/
	public final String getSendLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SendLab);
	}
	public final void setSendLab(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.SendLab, value);
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
	 * @throws Exception 
	*/
	public final String getSendJS() throws Exception
	{
		String str = this.GetValStringByKey(BtnAttr.SendJS).replace("~", "'");
		if (this.getCCRole() == bp.wf.CCRole.WhenSend)
		{
			str = str + "  if ( OpenCC()==false) return false;";
		}
		return str;
	}
	/** 
	 轨迹标签
	 * @throws Exception 
	*/
	public final String getTrackLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.TrackLab);
	}
	/** 
	 是否启用轨迹
	 * @throws Exception 
	*/
	public final boolean getTrackEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.TrackEnable);
	}
	/** 
	 查看父流程标签
	 * @throws Exception 
	*/
	public final String getShowParentFormLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ShowParentFormLab);
	}

	/** 
	 是否启用查看父流程
	 * @throws Exception 
	*/
	public final boolean getShowParentFormEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ShowParentFormEnable);
	}


	/** 
	 抄送标签
	 * @throws Exception 
	*/
	public final String getCCLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.CCLab);
	}
	/** 
	 抄送规则
	 * @throws Exception 
	*/
	public final CCRole getCCRole() throws Exception
	{
		return CCRole.forValue(this.GetValIntByKey(BtnAttr.CCRole));
	}
	/** 
	 删除标签
	 * @throws Exception 
	*/
	public final String getDeleteLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.DelLab);
	}
	/** 
	 删除类型
	 * @throws Exception 
	*/
	public final int getDeleteEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.DelEnable);
	}
	/** 
	 结束流程
	 * @throws Exception 
	*/
	public final String getEndFlowLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.EndFlowLab);
	}
	/** 
	 是否启用结束流程
	 * @throws Exception 
	*/
	public final boolean getEndFlowEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.EndFlowEnable);
	}
	/** 
	 是否启用流转自定义
	 * @throws Exception 
	*/
	public final String getTCLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.TCLab);
	}
	/** 
	 是否启用流转自定义
	 * @throws Exception 
	*/
	public final boolean getTCEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.TCEnable);
	}
	public final void setTCEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.TCEnable, value);
	}

	public final int getHelpRole() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.HelpRole);
	}

	public final String getHelpLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.HelpLab);
	}

	/** 
	 审核标签
	 * @throws Exception 
	*/
	public final String getWorkCheckLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.WorkCheckLab);
	}
	/** 
	 标签是否启用？
	*/
	//public bool SubFlowEnable111
	//{
	//    get
	//    {
	//        return this.GetValBooleanByKey(BtnAttr.SubFlowEnable);
	//    }
	//    set
	//    {
	//        this.SetValByKey(BtnAttr.SubFlowEnable, value);
	//    }
	//}
	/** 
	 审核是否可用
	 * @throws Exception 
	*/
	public final boolean getWorkCheckEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.WorkCheckEnable);
	}
	public final void setWorkCheckEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.WorkCheckEnable, value);
	}
	/** 
	 考核 是否可用
	 * @throws Exception 
	*/
	public final int getCHRole() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.CHRole);
	}
	/** 
	 考核 标签
	 * @throws Exception 
	*/
	public final String getCHLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.CHLab);
	}
	/** 
	 重要性 是否可用
	 * @throws Exception 
	*/
	public final int getPRIEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.PRIEnable);
	}
	/** 
	 重要性 标签
	 * @throws Exception 
	*/
	public final String getPRILab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PRILab);
	}
	/** 
	 关注 是否可用
	 * @throws Exception 
	*/
	public final boolean getFocusEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.FocusEnable);
	}
	/** 
	 关注 标签
	 * @throws Exception 
	*/
	public final String getFocusLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.FocusLab);
	}

	/** 
	 分配 是否可用
	 * @throws Exception 
	*/
	public final boolean getAllotEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.AllotEnable);
	}
	/** 
	 分配 标签
	 * @throws Exception 
	*/
	public final String getAllotLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.AllotLab);
	}

	/** 
	 确认 是否可用
	 * @throws Exception 
	*/
	public final boolean getConfirmEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ConfirmEnable);
	}
	/** 
	 确认标签
	 * @throws Exception 
	*/
	public final String getConfirmLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ConfirmLab);
	}

	/** 
	 打包下载 是否可用
	 * @throws Exception 
	*/
	public final boolean getPrintZipEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintZipEnable);
	}
	public final boolean getPrintZipMyCC() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintZipMyCC);
	}
	public final boolean getPrintZipMyView() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintZipMyView);
	}
	/** 
	 打包下载 标签
	 * @throws Exception 
	*/
	public final String getPrintZipLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintZipLab);
	}
	/** 
	 pdf 是否可用
	 * @throws Exception 
	*/
	public final boolean getPrintPDFEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintPDFEnable);
	}
	public final boolean getPrintPDFMyCC() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintPDFMyCC);
	}
	public final boolean getPrintPDFMyView() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintPDFMyView);
	}
	public final boolean getPrintDocMyView() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintDocMyView);
	}
	/** 
	 打包下载 标签
	 * @throws Exception 
	*/
	public final String getPrintPDFLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintPDFLab);
	}

	/** 
	 html 是否可用
	 * @throws Exception 
	*/
	public final boolean getPrintHtmlEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintHtmlEnable);
	}

	public final boolean getPrintHtmlMyCC() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintHtmlMyCC);
	}

	public final boolean getPrintHtmlMyView() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintHtmlMyView);
	}
	/** 
	 html 标签
	 * @throws Exception 
	*/
	public final String getPrintHtmlLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintHtmlLab);
	}


	/** 
	 批量处理是否可用
	 * @throws Exception 
	*/
	public final boolean getBatchEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.BatchEnable);
	}
	/** 
	 批处理标签
	 * @throws Exception 
	*/
	public final String getBatchLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.BatchLab);
	}
	/** 
	 加签
	 * @throws Exception 
	*/
	public final boolean getAskforEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.AskforEnable);
	}
	/** 
	 加签
	 * @throws Exception 
	*/
	public final String getAskforLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.AskforLab);
	}
	/** 
	 会签规则
	 * @throws Exception 
	*/
	public final HuiQianRole getHuiQianRole() throws Exception
	{
		return HuiQianRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianRole));
	}
	/** 
	 会签标签
	 * @throws Exception 
	*/
	public final String getHuiQianLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.HuiQianLab);
	}

	
	public final HuiQianLeaderRole getHuiQianLeaderRole() throws Exception
	{
		return HuiQianLeaderRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianLeaderRole));
	}

	public final String getAddLeaderLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.AddLeaderLab);
	}

	public final boolean getAddLeaderEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.AddLeaderEnable);
	}

	/** 
	是否启用文档,@0=不启用@1=按钮方式@2=公文在前@3=表单在前
	 * @throws Exception 
	*/
	private int getWebOfficeEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.WebOfficeEnable);
	}
	/** 
	 公文的工作模式 @0=不启用@1=按钮方式@2=标签页置后方式@3=标签页置前方式
	 * @throws Exception 
	*/
	public final WebOfficeWorkModel getWebOfficeWorkModel() throws Exception
	{
		return WebOfficeWorkModel.forValue(this.getWebOfficeEnable());
	}
	public final void setWebOfficeWorkModel(WebOfficeWorkModel value) throws Exception
	{
		this.SetValByKey(BtnAttr.WebOfficeEnable, value.getValue());
	}
	/** 
	 文档按钮标签
	 * @throws Exception 
	*/
	public final String getWebOfficeLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.WebOfficeLab);
	}


	/** 
	 公文标签
	 * @throws Exception 
	*/
	public final String getOfficeBtnLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeBtnLab);
	}
	/** 
	 公文标签
	 * @throws Exception 
	*/
	public final boolean getOfficeBtnEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeBtnEnable);
	}
	public final int getOfficeBtnEnableInt() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.OfficeBtnEnable);
	}

	public final int getOfficeBtnLocal() throws Exception{
		return this.GetValIntByKey(BtnAttr.OfficeBtnLocal);
	}
	/** 
	 备注标签
	 * @throws Exception 
	*/
	public final String getNoteLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.NoteLab);
	}
	/** 
	备注标签
	 * @throws Exception 
	*/
	public final int getNoteEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.NoteEnable);
	}


		///


		///构造方法
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
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "节点按钮标签");

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


			// add 2019.1.9 for 东孚.
		map.AddTBString(BtnAttr.OfficeBtnLab, "打开公文", "公文按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.OfficeBtnEnable, 0, "文件状态", true, true, BtnAttr.OfficeBtnEnable, "@@0=不可用@1=可编辑@2=不可编辑", false);
		map.AddDDLSysEnum(BtnAttr.OfficeFileType, 0, "文件类型", true, true, BtnAttr.OfficeFileType,
				"@0=word文件@1=WPS文件", false);

		map.AddDDLSysEnum(BtnAttr.OfficeBtnLocal, 0, "按钮位置", true, true, BtnAttr.OfficeBtnLocal,
				"@0=工具栏上@1=表单标签(divID=GovDocFile)", false);

		map.AddTBString(BtnAttr.ReturnLab, "退回", "退回按钮标签", true, false, 0, 50, 10);
		map.AddTBInt(BtnAttr.ReturnRole, 1, "是否启用", true, true);
		map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0, 50, 10, true);

		map.AddDDLSysEnum(NodeAttr.ReturnOneNodeRole, 0, "单节点退回规则", true, true, NodeAttr.ReturnOneNodeRole, "@@0=不启用@1=按照[退回信息填写字段]作为退回意见直接退回@2=按照[审核组件]填写的信息作为退回意见直接退回", true);


		map.AddTBString(BtnAttr.CCLab, "抄送", "抄送按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.CCRole, 0, "抄送规则", true, true, BtnAttr.CCRole);

			//  map.AddBoolean(BtnAttr, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.ShiftLab, "移交", "移交按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ShiftEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.DelLab, "删除流程", "删除流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.DelEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.EndFlowLab, "结束流程", "结束流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.EndFlowEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.HungLab, "挂起", "挂起按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.HungEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.ShowParentFormLab, "查看父流程", "查看父流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ShowParentFormEnable, false, "是否启用", true, true);
		map.SetHelperAlert(BtnAttr.ShowParentFormLab,"如果当前流程实例不是子流程，即时启用了，也不显示该按钮。");

		map.AddTBString(BtnAttr.PrintDocLab, "打印单据", "打印单据按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintDocEnable, false, "是否启用", true, true);

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

		map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.HuiQianRole, 0, "会签模式", true, true, BtnAttr.HuiQianRole, "@0=不启用@1=协作模式@4=组长模式");
			//map.AddDDLSysEnum(BtnAttr.IsCanAddHuiQianer, 0, "协作模式被加签的人处理规则", true, true, BtnAttr.IsCanAddHuiQianer,
			 //    "0=不允许增加其他协作人@1=允许增加协作人", false);


		map.AddDDLSysEnum(BtnAttr.HuiQianLeaderRole, 0, "会签组长规则", true, true, BtnAttr.HuiQianLeaderRole, "0=只有一个组长@1=最后一个组长发送@2=任意组长发送",true);

		map.AddTBString(BtnAttr.AddLeaderLab, "加主持人", "加主持人", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.AddLeaderEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
			//map.AddDDLSysEnum(BtnAttr.HuiQianRole, 0, "会签模式", true, true, BtnAttr.HuiQianRole, "@0=不启用@1=组长模式@2=协作模式");

			// map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
			//  map.AddBoolean(BtnAttr.HuiQianRole, false, "是否启用", true, true);

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
		map.AddDDLSysEnum(BtnAttr.CHRole, 0, "时限规则", true, true, BtnAttr.CHRole, "0=禁用@1=启用@2=只读@3=启用并可以调整流程应完成时间");

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

			// add 2020.5.25 for 交投集团.
		map.AddBoolean(BtnAttr.PrintHtmlMyView, false, "(打印Html)显示在查看器工具栏?", true, true);
		map.AddBoolean(BtnAttr.PrintHtmlMyCC, false, "(打印Html)显示在抄送工具栏?", true, true, true);

			// add 2017.9.1 for 天业集团.
		map.AddTBString(BtnAttr.PrintPDFLab, "打印pdf", "打印pdf标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintPDFEnable, false, "是否启用", true, true);

			// add 2020.5.25 for 交投集团.
		map.AddBoolean(BtnAttr.PrintPDFMyView, false, "(打印pdf)显示在查看器工具栏?", true, true);
		map.AddBoolean(BtnAttr.PrintPDFMyCC, false, "(打印pdf)显示在抄送工具栏?", true, true, false);


			// add 2017.9.1 for 天业集团.
		map.AddTBString(BtnAttr.PrintZipLab, "打包下载", "打包下载zip按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintZipEnable, false, "是否启用", true, true);

			// add 2020.5.25 for 交投集团.
		map.AddBoolean(BtnAttr.PrintZipMyView, false, "(打包下载zip)显示在查看器工具栏?", true, true);
		map.AddBoolean(BtnAttr.PrintZipMyCC, false, "(打包下载zip)显示在抄送工具栏?", true, true, false);

			// add 2019.3.10 增加List.
		map.AddTBString(BtnAttr.ListLab, "列表", "列表按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ListEnable, true, "是否启用", true, true);

			//备注 流程不流转，设置备注信息提醒已处理人员当前流程运行情况
		map.AddTBString(BtnAttr.NoteLab, "备注", "备注标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.NoteEnable, 0, "启用规则", true, true, BtnAttr.NoteEnable, "0=禁用@1=启用@2=只读");

			//for 周大福.
		map.AddTBString(BtnAttr.HelpLab, "帮助", "帮助标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.HelpRole, 0, "帮助显示规则", true, true, BtnAttr.HelpRole, "0=禁用@1=启用@2=强制提示@3=选择性提示");


		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		Node fl = new Node();
		fl.setNodeID(this.getNodeID());
		fl.RetrieveFromDBSources();
		fl.Update();

		BtnLab btnLab = new BtnLab();
		btnLab.setNodeID(this.getNodeID());
		btnLab.RetrieveFromDBSources();
		btnLab.Update();

		super.afterInsertUpdateAction();
	}

		///
}
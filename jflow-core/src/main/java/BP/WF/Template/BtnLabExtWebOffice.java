package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 公文属性控制
*/
public class BtnLabExtWebOffice extends Entity
{
	/** 
	 访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		uac.IsDelete = false;
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
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
	 子流程按钮标签
	*/
	public final String getSubFlowLab()
	{
		return this.GetValStringByKey(BtnAttr.SubFlowLab);
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
	public final int getCHRole()
	{
		return this.GetValIntByKey(BtnAttr.CHRole);
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
	private int getWebOfficeEnable()
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
	 表单工作方式.
	*/
	public final FrmType getWebOfficeFrmModel()
	{
		return FrmType.forValue( this.GetValIntByKey(BtnAttr.WebOfficeFrmModel, FrmType.FreeFrm.getValue()));
	}
	public final void setWebOfficeFrmModel(FrmType value)
	{
		this.SetValByKey(BtnAttr.WebOfficeFrmModel, value.getValue());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 Btn
	*/
	public BtnLabExtWebOffice()
	{
	}
	/** 
	 公文属性控制
	 
	 @param nodeid 节点ID
	 * @throws Exception 
	*/
	public BtnLabExtWebOffice(int nodeid) throws Exception
	{
		this.setNodeID(nodeid);
		this.RetrieveFromDBSources();
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

		Map map = new Map("WF_Node", "公文属性控制");

		map.Java_SetDepositaryOfEntity(Depositary.Application);

		map.AddTBIntPK(BtnAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(BtnAttr.Name, null, "节点名称", true, true, 0, 100, 10);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 公文按钮
		map.AddTBString(BtnAttr.OfficeOpen, "打开本地", "打开本地标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOpenTemplate, "打开模板", "打开模板标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenTemplateEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSave, "保存", "保存标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSaveEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeAccept, "接受修订", "接受修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeAcceptEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeRefuse, "拒绝修订", "拒绝修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeRefuseEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOver, "套红按钮", "套红按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOverEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeMarks, true, "是否查看用户留痕", true, true);
		map.AddBoolean(BtnAttr.OfficeReadOnly, false, "是否只读", true, true);

		map.AddTBString(BtnAttr.OfficePrint, "打印按钮", "打印按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficePrintEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSeal, "签章按钮", "签章按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSealEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeInsertFlow, "插入流程", "插入流程标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeInsertFlowEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeNodeInfo, false, "是否记录节点信息", true, true);
		map.AddBoolean(BtnAttr.OfficeReSavePDF, false, "是否该自动保存为PDF", true, true);

		map.AddTBString(BtnAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeDownEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeIsMarks, true, "是否进入留痕模式", true, true);
		map.AddTBString(BtnAttr.OfficeTemplate, "", "指定文档模板", true, false, 0, 50, 10);

		map.AddBoolean(BtnAttr.OfficeIsParent, true, "是否使用父流程的文档", true, true);



		map.AddBoolean(BtnAttr.OfficeIsTrueTH, false, "是否自动套红", true, true);
		map.AddTBString(BtnAttr.OfficeTHTemplate, "", "自动套红模板", true, false, 0, 50, 10);

		map.AddTBString(BtnAttr.WebOfficeLab, "公文", "公文标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.WebOfficeEnable, 0, "文档启用方式", true, true, BtnAttr.WebOfficeEnable, "@0=不启用@1=按钮方式@2=标签页置后方式@3=标签页置前方式"); //edited by liuxc,2016-01-18,from xc.

		map.AddDDLSysEnum(BtnAttr.WebOfficeFrmModel, 0, "表单工作方式", true, true, "FrmType");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion


		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		//同步更新表单的工作模式.
		MapData md = new MapData("ND" + this.getNodeID());
		md.setHisFrmType( this.getWebOfficeFrmModel());
		md.Update();

		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		Node fl = new Node();
		fl.setNodeID(this.getNodeID());
		fl.RetrieveFromDBSources();
		fl.Update();

		super.afterInsertUpdateAction();
	}
}
package BP.Sys;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import java.util.*;

/** 
  ToolbarExcel 控制器
*/
public class ToolbarExcelSln extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 界面上的访问控制
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsView = false;
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
			uac.IsView = true;
		}
		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 功能按钮.
	/** 
	 打开本地标签.
	*/
	public final String getOfficeOpenLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeOpenLab);
	}
	public final void setOfficeOpenLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenLab, value);
	}
	/** 
	 是否打开本地模版文件.
	*/
	public final boolean getOfficeOpenEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeOpenEnable);
	}
	public final void setOfficeOpenEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenEnable, value);
	}
	/** 
	 打开模板 标签.
	*/
	public final String getOfficeOpenTemplateLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeOpenTemplateLab);
	}
	public final void setOfficeOpenTemplateLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenTemplateLab, value);
	}
	/** 
	 打开模板.
	*/
	public final boolean getOfficeOpenTemplateEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeOpenTemplateEnable);
	}
	public final void setOfficeOpenTemplateEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenTemplateEnable, value);
	}
	/** 
	 保存 标签.
	*/
	public final String getOfficeSaveLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeSaveLab);
	}
	public final void setOfficeSaveLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSaveLab, value);
	}
	/** 
	 保存.是否启用.
	*/
	public final boolean getOfficeSaveEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeSaveEnable);
	}
	public final void setOfficeSaveEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSaveEnable, value);
	}
	/** 
	 接受修订 标签.
	*/
	public final String getOfficeAcceptLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeAcceptLab);
	}
	public final void setOfficeAcceptLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeAcceptLab, value);
	}
	/** 
	 接受修订.
	*/
	public final boolean getOfficeAcceptEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeAcceptEnable);
	}
	public final void setOfficeAcceptEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeAcceptEnable, value);
	}
	/** 
	 拒绝修订 标签.
	*/
	public final String getOfficeRefuseLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeRefuseLab);
	}
	public final void setOfficeRefuseLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeRefuseLab, value);
	}
	/** 
	 拒绝修订.
	*/
	public final boolean getOfficeRefuseEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeRefuseEnable);
	}
	public final void setOfficeRefuseEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeRefuseEnable, value);
	}
	/** 
	 套红按钮 标签.
	*/
	public final String getOfficeOverLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeOverLab);
	}
	public final void setOfficeOverLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOverLab, value);
	}
	/** 
	 套红按钮.
	*/
	public final boolean getOfficeOverEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeOverEnable);
	}
	public final void setOfficeOverEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOverEnable, value);
	}
	/** 
	 查看用户留痕
	*/
	public final boolean getOfficeMarksEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeMarksEnable);
	}
	public final void setOfficeMarksEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeMarksEnable, value);
	}
	/** 
	 打印按钮-标签
	*/
	public final String getOfficePrintLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficePrintLab);
	}
	public final void setOfficePrintLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficePrintLab, value);
	}
	/** 
	 打印
	*/
	public final boolean getOfficePrintEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficePrintEnable);
	}
	public final void setOfficePrintEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficePrintEnable, value);
	}
	/** 
	 签章-标签
	*/
	public final String getOfficeSealLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeSealLab);
	}
	public final void setOfficeSealLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSealLab, value);
	}
	/** 
	 签章
	*/
	public final boolean getOfficeSealEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeSealEnable);
	}
	public final void setOfficeSealEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSealEnable, value);
	}



	/** 
	 插入流程-标签
	*/
	public final String getOfficeInsertFlowLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeInsertFlowLab);
	}
	public final void setOfficeInsertFlowLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeInsertFlowLab, value);
	}
	/** 
	 插入流程
	*/
	public final boolean getOfficeInsertFlowEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeInsertFlowEnable);
	}
	public final void setOfficeInsertFlowEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeInsertFlowEnable, value);
	}

	/** 
	 是否自动记录节点信息
	*/
	public final boolean getOfficeNodeInfo()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeNodeInfo);
	}
	public final void setOfficeNodeInfo(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeNodeInfo, value);
	}
	/** 
	 是否该节点保存为PDF
	*/
	public final boolean getOfficeReSavePDF()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeReSavePDF);
	}
	public final void setOfficeReSavePDF(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeReSavePDF, value);
	}
	/** 
	 是否进入留痕模式
	*/
	public final boolean getOfficeIsMarks()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeIsMarks);
	}
	public final void setOfficeIsMarks(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeIsMarks, value);
	}
	/** 
	 指定文档模板
	*/
	public final String getOfficeTemplate()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeTemplate);
	}
	public final void setOfficeTemplate(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeTemplate, value);
	}
	/** 
	 是否使用父流程的文档
	*/
	public final boolean getOfficeIsParent()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeIsParent);
	}
	public final void setOfficeIsParent(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeIsParent, value);
	}
	/** 
	 是否启用标签
	*/
	public final String getOfficeDownLab()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeDownLab);
	}
	public final void setOfficeDownLab(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeDownLab, value);
	}
	/** 
	 下载
	*/
	public final boolean getOfficeIsDown()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeIsDown);
	}
	/** 
	 是否自动套红
	*/
	public final boolean getOfficeTHEnable()
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeTHEnable);
	}
	public final void setOfficeTHEnable(boolean value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeTHEnable, value);
	}
	/** 
	 套红模板
	*/
	public final String getOfficeTHTemplate()
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeTHTemplate);
	}
	public final void setOfficeTHTemplate(String value)
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeTHTemplate, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 ToolbarExcel功能控制区域
	*/
	public ToolbarExcelSln()
	{
	}
	/** 
	 ToolbarExcel功能控制
	 
	 @param no 表单ID
	*/
	public ToolbarExcelSln(String mypk)
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	public ToolbarExcelSln(String fk_flow, int fk_node, String fk_frm)
	{
		int i = this.Retrieve(ToolbarExcelSlnAttr.FK_Flow, fk_flow, ToolbarExcelSlnAttr.FK_Node, fk_node, ToolbarExcelSlnAttr.FK_Frm, fk_frm);
		if (i == 0)
		{
			return;
			throw new RuntimeException("@表单关联信息已被删除。");
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

		Map map = new Map("WF_FrmNode", "ToolbarExcelSln功能控制");

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddMyPK();
		map.AddTBString(ToolbarExcelSlnAttr.FK_Frm, null, "表单ID", true, true, 1, 32, 32);
		map.AddTBInt(ToolbarExcelSlnAttr.FK_Node, 0, "节点编号", true, true);
		map.AddTBString(ToolbarExcelSlnAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 公文按钮
		map.AddTBString(ToolbarExcelAttr.OfficeOpenLab, "打开本地", "打开本地标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeOpenEnable, false, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeOpenTemplateLab, "打开模板", "打开模板标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeOpenTemplateEnable, false, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeSaveLab, "保存", "保存标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeSaveEnable, true, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeAcceptLab, "接受修订", "接受修订标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeAcceptEnable, false, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeRefuseLab, "拒绝修订", "拒绝修订标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeRefuseEnable, false, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeOverLab, "套红按钮", "套红按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeOverEnable, false, "是否启用", true, true);

		map.AddBoolean(ToolbarExcelAttr.OfficeMarksEnable, true, "是否查看用户留痕", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficePrintLab, "打印按钮", "打印按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficePrintEnable, false, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeSealLab, "签章按钮", "签章按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeSealEnable, false, "是否启用", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeInsertFlowLab, "插入流程", "插入流程标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeInsertFlowEnable, false, "是否启用", true, true);

		map.AddBoolean(ToolbarExcelAttr.OfficeNodeInfo, false, "是否记录节点信息", true, true);
		map.AddBoolean(ToolbarExcelAttr.OfficeReSavePDF, false, "是否该自动保存为PDF", true, true);

		map.AddTBString(ToolbarExcelAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeDownEnable, false, "是否启用", true, true);

		map.AddBoolean(ToolbarExcelAttr.OfficeIsMarks, true, "是否进入留痕模式", true, true);
		map.AddTBString(ToolbarExcelAttr.OfficeTemplate, "", "指定文档模板", true, false, 0, 100, 10);
		map.AddBoolean(ToolbarExcelAttr.OfficeIsParent, true, "是否使用父流程的文档", true, true);

		map.AddBoolean(ToolbarExcelAttr.OfficeTHEnable, false, "是否自动套红", true, true);
		map.AddTBString(ToolbarExcelAttr.OfficeTHTemplate, "", "自动套红模板", true, false, 0, 200, 10);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
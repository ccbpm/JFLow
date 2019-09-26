package BP.Sys;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.WebUser;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
  ToolbarExcel 控制器
*/
public class ToolbarExcel extends EntityNoName
{

		///#region 界面上的访问控制
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
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

		///#endregion


		///#region 功能按钮.
	/** 
	 打开本地标签.
	 * @throws Exception 
	*/
	public final String getOfficeOpenLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeOpenLab);
	}
	public final void setOfficeOpenLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenLab, value);
	}
	/** 
	 是否打开本地模版文件.
	 * @throws Exception 
	*/
	public final boolean getOfficeOpenEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeOpenEnable);
	}
	public final void setOfficeOpenEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenEnable, value);
	}
	/** 
	 打开模板 标签.
	 * @throws Exception 
	*/
	public final String getOfficeOpenTemplateLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeOpenTemplateLab);
	}
	public final void setOfficeOpenTemplateLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenTemplateLab, value);
	}
	/** 
	 打开模板.
	 * @throws Exception 
	*/
	public final boolean getOfficeOpenTemplateEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeOpenTemplateEnable);
	}
	public final void setOfficeOpenTemplateEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOpenTemplateEnable, value);
	}
	/** 
	 保存 标签.
	 * @throws Exception 
	*/
	public final String getOfficeSaveLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeSaveLab);
	}
	public final void setOfficeSaveLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSaveLab, value);
	}
	/** 
	 保存.是否启用.
	 * @throws Exception 
	*/
	public final boolean getOfficeSaveEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeSaveEnable);
	}
	public final void setOfficeSaveEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSaveEnable, value);
	}
	/** 
	 接受修订 标签.
	 * @throws Exception 
	*/
	public final String getOfficeAcceptLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeAcceptLab);
	}
	public final void setOfficeAcceptLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeAcceptLab, value);
	}
	/** 
	 接受修订.
	*/
	public final boolean getOfficeAcceptEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeAcceptEnable);
	}
	public final void setOfficeAcceptEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeAcceptEnable, value);
	}
	/** 
	 拒绝修订 标签.
	*/
	public final String getOfficeRefuseLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeRefuseLab);
	}
	public final void setOfficeRefuseLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeRefuseLab, value);
	}
	/** 
	 拒绝修订.
	*/
	public final boolean getOfficeRefuseEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeRefuseEnable);
	}
	public final void setOfficeRefuseEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeRefuseEnable, value);
	}
	/** 
	 套红按钮 标签.
	*/
	public final String getOfficeOverLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeOverLab);
	}
	public final void setOfficeOverLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOverLab, value);
	}
	/** 
	 套红按钮.
	*/
	public final boolean getOfficeOverEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeOverEnable);
	}
	public final void setOfficeOverEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeOverEnable, value);
	}
	/** 
	 查看用户留痕
	*/
	public final boolean getOfficeMarksEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeMarksEnable);
	}
	public final void setOfficeMarksEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeMarksEnable, value);
	}
	/** 
	 打印按钮-标签
	*/ 
	public final String getOfficePrintLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficePrintLab);
	}
	public final void setOfficePrintLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficePrintLab, value);
	}
	/** 
	 打印
	*/
	public final boolean getOfficePrintEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficePrintEnable);
	}
	public final void setOfficePrintEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficePrintEnable, value);
	}
	/** 
	 签章-标签
	*/
	public final String getOfficeSealLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeSealLab);
	}
	public final void setOfficeSealLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSealLab, value);
	}
	/** 
	 签章
	*/
	public final boolean getOfficeSealEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeSealEnable);
	}
	public final void setOfficeSealEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeSealEnable, value);
	}



	/** 
	 插入流程-标签
	*/
	public final String getOfficeInsertFlowLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeInsertFlowLab);
	}
	public final void setOfficeInsertFlowLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeInsertFlowLab, value);
	}
	/** 
	 插入流程
	*/
	public final boolean getOfficeInsertFlowEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeInsertFlowEnable);
	}
	public final void setOfficeInsertFlowEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeInsertFlowEnable, value);
	}

	/** 
	 是否自动记录节点信息
	*/
	public final boolean getOfficeNodeInfo() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeNodeInfo);
	}
	public final void setOfficeNodeInfo(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeNodeInfo, value);
	}
	/** 
	 是否该节点保存为PDF
	*/
	public final boolean getOfficeReSavePDF() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeReSavePDF);
	}
	public final void setOfficeReSavePDF(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeReSavePDF, value);
	}
	/** 
	 是否进入留痕模式
	*/
	public final boolean getOfficeIsMarks() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeIsMarks);
	}
	public final void setOfficeIsMarks(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeIsMarks, value);
	}
	/** 
	 指定文档模板
	*/
	public final String getOfficeTemplate() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeTemplate);
	}
	public final void setOfficeTemplate(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeTemplate, value);
	}
	/** 
	 是否使用父流程的文档
	*/
	public final boolean getOfficeIsParent() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeIsParent);
	}
	public final void setOfficeIsParent(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeIsParent, value);
	}
	/** 
	 是否启用标签
	*/
	public final String getOfficeDownLab() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeDownLab);
	}
	public final void setOfficeDownLab(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeDownLab, value);
	}
	/** 
	 下载
	*/
	public final boolean getOfficeDownEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeDownEnable);
	}
	public final void setOfficeDownEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeDownEnable, value);
	}
	/** 
	 是否自动套红
	*/
	public final boolean getOfficeTHEnable() throws Exception
	{
		return this.GetValBooleanByKey(ToolbarExcelAttr.OfficeTHEnable);
	}
	public final void setOfficeTHEnable(boolean value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeTHEnable, value);
	}
	/** 
	 套红模板
	*/
	public final String getOfficeTHTemplate() throws Exception
	{
		return this.GetValStringByKey(ToolbarExcelAttr.OfficeTHTemplate);
	}
	public final void setOfficeTHTemplate(String value) throws Exception
	{
		this.SetValByKey(ToolbarExcelAttr.OfficeTHTemplate, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 ToolbarExcel功能控制区域
	*/
	public ToolbarExcel()
	{
	}
	/** 
	 ToolbarExcel功能控制
	 
	 @param no 表单ID
	*/
	public ToolbarExcel(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
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

		Map map = new Map("Sys_MapData", "ToolbarExcel功能控制");

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, false, 1, 200, 20);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20);


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

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}
package bp.sys;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.*;
import bp.web.*;
import bp.*;

import java.nio.file.Path;
import java.util.*;

/** 
 EnCfgs
*/
public class EnCfg extends EntityNo
{

		///UI设置.
	public final String getUI() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.UI);
	}
	public final void setUI(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.UI, value);
	}

		/// UI设置.


		///基本属性
	/** 
	 数据分析方式
	*/
	public final String getDatan() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.Datan);
	}
	public final void setDatan(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.Datan, value);
	}
	/** 
	 数据源
	*/
	public final String getGroupTitle() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.GroupTitle);
	}
	public final void setGroupTitle(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.GroupTitle, value);
	}
	/** 
	 附件路径
	*/
	public final String getFJSavePath() throws Exception
	{
		String str = this.GetValStringByKey(EnCfgAttr.FJSavePath);
		if (DataType.IsNullOrEmpty(str)==true )
		{
			return SystemConfig.getPathOfDataUser() + this.getNo() + "/";
		}
		return str;
	}
	public final void setFJSavePath(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.FJSavePath, value);
	}
	/** 
	 附件存储位置.
	 * @throws Exception 
	*/
	public final String getFJWebPath() throws Exception
	{
		String str = this.GetValStringByKey(EnCfgAttr.FJWebPath);
		if (DataType.IsNullOrEmpty(str)==true )
		{
			str = bp.sys.Glo.getRequest().getRemoteHost() + "/DataUser/"
					+ this.getNo() + "/";;
		}
		str = str.replace("\\", "/");
		if (!str.endsWith("/"))
		{
			str += "/";
		}
		return str;
	}
	public final void setFJWebPath(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.FJWebPath, value);
	}

		///


		///参数属性.
	/** 
	 批处理-设置页面大小
	*/
	public final int getPageSizeOfBatch() throws Exception
	{
		return this.GetParaInt("PageSizeOfBatch", 600);
	}
	public final void setPageSizeOfBatch(int value) throws Exception
	{
		this.SetPara("PageSizeOfBatch", value);
	}
	/** 
	 批处理-设置页面大小
	*/
	public final int getPageSizeOfSearch() throws Exception
	{
		return this.GetParaInt("PageSizeOfSearch", 15);
	}
	public final void setPageSizeOfSearch(int value) throws Exception
	{
		this.SetPara("PageSizeOfSearch", value);
	}

	public final String getFieldSet() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.FieldSet);
	}
	public final void setFieldSet(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.FieldSet, value);
	}

		/// 参数属性.


		///构造方法
	/** 
	 系统实体
	*/
	public EnCfg()
	{

	}
	/** 
	 系统实体
	 
	 @param no
	 * @throws Exception 
	*/
	public EnCfg(String enName) throws Exception
	{
		this.setNo(enName);
		try
		{
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
		}
	}
	/** 
	 map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_EnCfg", "实体配置");

		
		map.AddTBStringPK(EnCfgAttr.No, null, "实体名称", true, false, 1, 100, 60);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本信息设置.
		//string cfg1 = "@0=En.htm 实体与实体相关功能编辑器";
		map.AddDDLSysEnum("UIRowStyleGlo", 0, "表格数据行风格(应用全局)", true, true);
		map.AddBoolean("IsEnableDouclickGlo", true, "是否启动双击打开(应用全局)?", true, true);

		map.AddBoolean("IsEnableFocusField", false, "是否启用焦点字段?", true, true);
		map.AddTBString("FocusField", null, "焦点字段", true, false, 0, 30, 60, true);
		map.SetHelperAlert("FocusField", "用于显示点击打开的列，比如:Name, Title列.");

		map.AddBoolean("IsEnableRefFunc", false, "是否启用相关功能列?", true, true);
		map.AddBoolean("IsEnableOpenICON", false, "是否启用打开图标?", true, true);

		map.AddDDLSysEnum("MoveToShowWay", 0, "移动到显示方式", true, true);
		map.AddTBString("KeyLabel", null, "关键字Label", true, false, 0, 30, 60, true);
		map.SetHelperAlert("KeyLabel", "(默认为:关键字:)");

		map.AddTBInt("PageSize", 10, "页面显示的条数", true, true);
		map.SetHelperAlert("PageSize", "(默认:10)");

		map.AddTBInt("FontSize", 14, "页面字体大小", true, true);
		map.SetHelperAlert("FontSize", "(默认:14px)");
		map.AddDDLSysEnum("EditerType", 0, "大块文本编辑器", true, true);
		///#endregion 基本信息设置.


		///#region 分组标签.
		//字段分组标签设置.
		map.AddTBString(EnCfgAttr.GroupTitle, null, "分组标签", true, false, 0, 2000, 60, true);
		String msg = "字段显示分组标签:";
		msg += "\t\n格式为: @字段名1=标签1,标签描述1";
		msg += "\t\n@No=基础信息,单据基础配置信息.@BtnNewLable=单据按钮权限,用于控制每个功能按钮启用规则.@BtnImpExcel=列表按钮,列表按钮控制@Designer=设计者,流程开发设计者信息";
		map.SetHelperAlert(EnCfgAttr.GroupTitle, msg);

		///#endregion 分组标签.

		///#region 双击要打开的url.
		String cfg = "@0=En.htm 实体与实体相关功能编辑器";
		cfg += "@1=EnOnly.htm 实体编辑器";
		cfg += "@2=/CCForm/FrmGener.htm 傻瓜表单解析器";
		cfg += "@3=/CCForm/FrmGener.htm 自由表单解析器";
		cfg += "@9=自定义URL";

		map.AddDDLSysEnum("SearchUrlOpenType", 0, "双击行打开内容", true, true, "SearchUrlOpenType", cfg);

		map.AddTBString(EnCfgAttr.UrlExt, null, "要打开的Url", true, false, 0, 500, 60, true);
		map.AddDDLSysEnum("OpenModel", 0, "打开方式", true, true, "OpenModel", "@0=弹窗@1=新窗口打开");

		map.AddBoolean("IsRefreshParentPage", true, "关闭后是否刷新本页面", true, true);

		map.AddTBInt("WinCardW", 1000, "窗体宽度", true, false);
		map.AddTBInt("WinCardH", 600, "高度", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询排序.
		map.AddTBString("OrderBy", null, "查询排序字段", true, false, 0, 100, 60);
		map.AddBoolean("IsDeSc", true, "是否降序排序?", true, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 查询排序.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 附件保存路径.
		map.AddTBString(EnCfgAttr.FJSavePath, null, "附加保存路径", true, false, 0, 100, 60);
		map.AddTBString(EnCfgAttr.FJWebPath, null, "附件Web路径", true, false, 0, 100, 60);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 附件保存路径.

		map.AddTBString(EnCfgAttr.Datan, null, "字段数据分析方式", true, false, 0, 200, 60);
		map.AddTBString(EnCfgAttr.UI, null, "UI设置", true, false, 0, 2000, 60);

		//字段颜色设置.
		map.AddTBString(EnCfgAttr.ColorSet, null, "颜色设置", true, false, 0, 500, 60);
		//对字段求总和平均.
		map.AddTBString(EnCfgAttr.FieldSet, null, "字段求和/平均设置", true, false, 0, 500, 60);
		//字段格式化函数.
		map.AddTBString("ForamtFunc", null, "字段格式化函数", true, false, 0, 200, 60,true);
		msg = "对字段的显示使用函数进行处理";
		msg += "\t\n 1. 对于字段内容需要处理后在输出出来.";
		msg += "\t\n 2. 比如：原字段内容 @zhangsa,张三@lisi,李四 显示的内容为 张三,李四";
		msg += "\t\n 3. 配置格式: 字段名@函数名; 比如:  FlowEmps@DealFlowEmps; ";
		msg += "\t\n 4. 函数写入到 \\DataUser\\JSLibData\\SearchSelf.js";
		map.SetHelperAlert("ForamtFunc", msg);

		map.AddBoolean("BtnsShowLeft", false, "按钮显示到左边?", true, true, false);

		msg = "配置的按钮显示位置.";
		msg += "\t\n1.默认配置的按钮显示在右边位置. ";
		msg += "\t\n1.这些按钮包括自定义按钮，新建，导入，导出，分组。";
		map.SetHelperAlert("BtnsShowLeft", msg);

		//导入功能.
		map.AddBoolean("IsImp", true, "是否显示导入?", true, true, false);
		map.AddTBString("ImpFuncUrl", null, "导入功能Url", true, false, 0, 500, 60, true);
		map.SetHelperAlert("ImpFuncUrl", "如果为空，则使用通用的导入功能.");

		map.AddBoolean("IsGroup", true, "是否显示分析按钮（在查询工具栏里）?", true, true, true);
		map.AddBoolean("IsEnableLazyload", true, "是否启用懒加载？（对树结构实体有效）?", true, true, true);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 自定义按钮.
		map.AddTBString("BtnLab1", null, "自定义按钮标签1", true, false, 0, 70, 60, false);
		map.SetHelperAlert("BtnLab1", "自定义按钮与标签,函数可以写入到\\DataUser\\JSLabData\\SearchSelf.js里面.");
		map.AddTBString("BtnJS1", null, "Url/Javasccript", true, false, 0, 300, 60, false);

		map.AddTBString("BtnLab2", null, "自定义按钮标签2", true, false, 0, 70, 60, false);
		map.AddTBString("BtnJS2", null, "Url/Javasccript", true, false, 0, 300, 60, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 自定义按钮.


		map.AddTBAtParas(3000); //参数属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行的方法.

		RefMethod rm = new RefMethod();
		//rm = new RefMethod();
		//rm.Title = "基本信息";
		//rm.ClassMethodName = this.ToString() + ".SearchSetting()";
		//rm.RefMethodType = RefMethodType.RightFrameOpen;
		//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示的列";
		rm.ClassMethodName = this.toString() + ".SearchSettingCols()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		 
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "多表头设置";
		rm.ClassMethodName = this.toString() + ".MultiTitle()";
		rm.refMethodType=  RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "启用傻瓜表单设计器";
		rm.ClassMethodName = this.toString() + ".DesignerFool()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "导入数据";
		rm.ClassMethodName = this.toString() + ".ImpData()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "字段颜色范围设置";
		rm.ClassMethodName = this.toString() + ".FieldColors()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "字段求和/平均设置";
		rm.ClassMethodName = this.toString() + ".FieldAvgSum()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "清除设计内容";
		rm.ClassMethodName = this.toString() + ".ClearData()";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行的方法.

		
 
		this.set_enMap(map);
		return this.get_enMap();
	}

	 /** 
	 字段颜色设置
	 
	 @return 
	 * @throws Exception 
*/
	public final String FieldColors() throws Exception
	{
		return "../../Comm/Sys/FieldColors.htm?EnsName=" + this.getNo();
	}
	/** 
	 字段求和/平均设置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FieldAvgSum() throws Exception
	{
		return "../../Comm/Sys/FieldAvgSum.htm?EnsName=" + this.getNo();
	}
	/** 
	 清除数据.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ClearData() throws Exception
	{
		MapData md = new MapData(this.getNo());
	 
		if (md.RetrieveFromDBSources() == 0)
		{
			return "err@没有设计的数据要清除.";
		}
		md.Delete();


		return "清除成功.";
	}
	public final String ImpData() throws Exception
	{
		return "../../Comm/Sys/ImpData.htm?EnsName=" + this.getNo();
	}
	public final String DesignerFool() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo();
	}

	public final String SearchSettingCols() throws Exception
	{
		return "../../Comm/Sys/SearchSettingCols.htm?EnsName=" + this.getNo();
	}

	public final String SearchSetting() throws Exception
	{
		return "../../Comm/Sys/SearchSetting.htm?EnsName=" + this.getNo();
	}
	/** 
	 多表头
	 
	 @return 
	 * @throws Exception 
	*/
	public final String MultiTitle() throws Exception
	{
		return "../../Comm/Sys/MultiTitle.htm?EnsName=" + this.getNo() + "&DoType=Search";
	}

		///
}
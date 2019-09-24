package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.*;
import BP.Web.*;

import java.nio.file.Path;
import java.util.*;

/** 
 EnCfgs
*/
public class EnCfg extends EntityNo
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region UI设置.
	public final String getUI() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.UI);
	}
	public final void setUI(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.UI, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion UI设置.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 数据分析方式
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
	*/
	public final String getFJSavePath() throws Exception
	{
		String str = this.GetValStringByKey(EnCfgAttr.FJSavePath);
		if (str.equals("") || str == null || str.equals(""))
		{
			return BP.Sys.SystemConfig.getPathOfDataUser() + this.getNo() + "\\";
		}
		return str;
	}
	public final void setFJSavePath(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.FJSavePath, value);
	}
	/** 
	 附件存储位置.
	*/
	public final String getFJWebPath()
	{
		String str = this.GetValStringByKey(EnCfgAttr.FJWebPath);
		if (str.equals("") || str == null)
		{
			
			str = Path.combine(HttpContextHelper.getRequestApplicationPath(), "DataUser/", this.getNo());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 参数属性.
	/** 
	 批处理-设置页面大小
	 * @throws Exception 
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
	 * @throws Exception 
	*/
	public final int getPageSizeOfSearch() throws Exception
	{
		return this.GetParaInt("PageSizeOfSearch", 15);
	}
	public final void setPageSizeOfSearch(int value) throws Exception
	{
		this.SetPara("PageSizeOfSearch", value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 参数属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_EnCfg", "实体配置");
		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);


		map.AddTBStringPK(EnCfgAttr.No, null, "实体名称", true, false, 1, 100, 60);
		map.AddTBString(EnCfgAttr.GroupTitle, null, "分组标签", true, false, 0, 2000, 60);
		map.AddTBString(EnCfgAttr.Url, null, "要打开的Url", true, false, 0, 500, 60);

		map.AddTBString(EnCfgAttr.FJSavePath, null, "保存路径", true, false, 0, 100, 60);
		map.AddTBString(EnCfgAttr.FJWebPath, null, "附件Web路径", true, false, 0, 100, 60);
		map.AddTBString(EnCfgAttr.Datan, null, "字段数据分析方式", true, false, 0, 200, 60);
		map.AddTBString(EnCfgAttr.UI, null, "UI设置", true, false, 0, 2000, 60);


		map.AddTBAtParas(3000); //参数属性.
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
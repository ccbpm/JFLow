package BP.Sys;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;


/** 
 EnCfgs
*/
public class EnCfg extends EntityNo
{

		///#region UI设置.
	public final String getUI() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.UI);
	}
	public final void setUI(String value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.UI, value);
	}

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
		if (str == null || str.equals(""))
		{
			return SystemConfig.getPathOfDataUser() + this.getNo() + "\\";
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
		if (str == null ||str.equals(""))
		{
			
			return BP.Sys.Glo.getRequest().getRemoteHost() + "/DataUser/"
					+ this.getNo() + "/";
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

		///#endregion


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
	public final String getFieldSet() throws Exception
	{
		return this.GetValStringByKey(EnCfgAttr.FieldSet);
	}
	public final void setFieldSet(int value) throws Exception
	{
		this.SetValByKey(EnCfgAttr.FieldSet, value);
	}
		///#endregion 参数属性.


		///#region 构造方法
	/** 
	 系统实体
	*/
	public EnCfg()
	{

	}
	/** 
	 系统实体
	 
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
		//字段颜色设置
		map.AddTBString(EnCfgAttr.ColorSet, null, "颜色设置", true, false, 0, 500, 60);
		//对字段求总和平均
		map.AddTBString(EnCfgAttr.FieldSet, null, "字段设置", true, false, 0, 500, 60);

		map.AddTBAtParas(3000); //参数属性.
		this.set_enMap(map);
		return this.get_enMap();
	}
}
package BP.Sys;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityNo;
import BP.En.Map;

/**
 * EnCfgs
 */
public class EnCfg extends EntityNo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 基本属性
	/**
	 * 数据分析方式
	 */
	public final String getDatan()
	{
		return this.GetValStringByKey(EnCfgAttr.Datan);
	}
	
	public final void setDatan(String value)
	{
		this.SetValByKey(EnCfgAttr.Datan, value);
	}
	
	public final String getUI()
	{
		return this.GetValStringByKey(EnCfgAttr.UI);
	}
	
	public final void setUI(String value)
	{
		this.SetValByKey(EnCfgAttr.UI, value);
	}
	
	/**
	 * 数据源
	 */
	public final String getGroupTitle()
	{
		return this.GetValStringByKey(EnCfgAttr.GroupTitle);
	}
	
	public final void setGroupTitle(String value)
	{
		this.SetValByKey(EnCfgAttr.GroupTitle, value);
	}
	
	/**
	 * 附件路径
	 */
	public final String getFJSavePath()
	{
		String str = this.GetValStringByKey(EnCfgAttr.FJSavePath);
		if (str.equals("") || str == null || str.equals(""))
		{
			return BP.Sys.SystemConfig.getPathOfDataUser() + this.getNo()
					+ "/";
		}
		return str;
	}
	
	public final void setFJSavePath(String value)
	{
		this.SetValByKey(EnCfgAttr.FJSavePath, value);
	}
	
	public final String getFJWebPath()
	{
		String str = this.GetValStringByKey(EnCfgAttr.FJWebPath);
		if (str.equals("") || str == null)
		{
			return BP.Sys.Glo.getRequest().getRemoteHost() + "/DataUser/"
					+ this.getNo() + "/";
		}
		return str;
	}
	
	public final void setFJWebPath(String value)
	{
		this.SetValByKey(EnCfgAttr.FJWebPath, value);
	}
	
	// 构造方法
	/**
	 * 系统实体
	 */
	public EnCfg()
	{
	}
	
	/**
	 * 系统实体
	 * 
	 * @param no
	 * @throws Exception 
	 */
	public EnCfg(String enName) throws Exception
	{
		this.setNo(enName);
		 
			this.RetrieveFromDBSources();
		 
	}
	
	/**
	 * map
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_EnCfg");
		map.setDepositaryOfEntity(Depositary.Application);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("实体配置");
		map.setEnType(EnType.Sys);
		
		map.AddTBStringPK(EnCfgAttr.No, null, "实体名称", true, false, 1, 100, 60);
		map.AddTBString(EnCfgAttr.GroupTitle, null, "分组标签", true, false, 0,
				2000, 60);
        map.AddTBString(EnCfgAttr.Url, null, "要打开的Url", true, false, 0, 500, 60);

		map.AddTBString(EnCfgAttr.FJSavePath, null, "保存路径", true, false, 0,
				100, 60);
		map.AddTBString(EnCfgAttr.FJWebPath, null, "附件Web路径", true, false, 0,
				100, 60);
		map.AddTBString(EnCfgAttr.Datan, null, "字段数据分析方式", true, false, 0, 200,
				60);
		map.AddTBString(EnCfgAttr.UI, null, "UI设置", true, false, 0, 2000, 60);
		
		map.AddTBAtParas(3000);  //参数属性.
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}
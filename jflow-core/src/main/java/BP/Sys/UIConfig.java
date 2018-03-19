package BP.Sys;

import BP.DA.AtPara;
import BP.En.Attrs;
import BP.En.EditerType;
import BP.En.Entity;
import BP.En.MoveToShowWay;
import BP.Tools.StringHelper;

public class UIConfig
{
	public Entity HisEn;
	public AtPara HisAP;
	
	public UIConfig()
	{
	}
	
	/**
	 * UI的设置.Search. Card, Group信息.
	 * 
	 * @param enName
	 */
	public UIConfig(Entity en)
	{
		this.HisEn = en;
		EnCfg cfg = new EnCfg(en.toString());
		String paraStr = cfg.getUI();
		if (StringHelper.isNullOrEmpty(paraStr) == true)
		{
			paraStr = "@UIRowStyleGlo=0@IsEnableDouclickGlo=1@IsEnableRefFunc=1@IsEnableFocusField=1@IsEnableOpenICON=1@FocusField=''@WinCardH=600@@WinCardW=800@ShowColumns=";
		}
		HisAP = new AtPara(paraStr);
	}
	
	/**
	 * 获取显示列数组，中间用,隔开
	 */
	public final String[] getShowColumns()
	{
		String colstr = this.HisAP.GetValStrByKey("ShowColumns");
		
		if (StringHelper.isNullOrEmpty(colstr))
		{
			return new String[0];
		}
		
		return colstr.split("[,]+");
	}
	
	// 移动.
	/**
	 * 移动到方式.
	 */
	public final MoveToShowWay getMoveToShowWay()
	{
		return MoveToShowWay.forValue(this.HisAP
				.GetValIntByKey("MoveToShowWay"));
	}
	
	public final EditerType getEditerType()
	{
		return EditerType.forValue(this.HisAP.GetValIntByKey("EditerType"));
	}
	
	/**
	 * 移动到字段
	 */
	public final String getMoveTo()
	{
		String s = this.HisAP.GetValStrByKey("MoveTo");
		return s;
	}
	
	// 移动.
	
	/**
	 * 风格类型
	 */
	public final int getUIRowStyleGlo()
	{
		return this.HisAP.GetValIntByKey("UIRowStyleGlo");
	}
	
	/**
	 * 是否启用双击打开？
	 */
	public final boolean getIsEnableDouclickGlo()
	{
		return this.HisAP.GetValBoolenByKey("IsEnableDouclickGlo");
	}
	
	/**
	 * 是否显示相关功能?
	 */
	public final boolean getIsEnableRefFunc()
	{
		return this.HisAP.GetValBoolenByKey("IsEnableRefFunc");
	}
	
	/**
	 * 是否启用焦点字段
	 */
	public final boolean getIsEnableFocusField()
	{
		return this.HisAP.GetValBoolenByKey("IsEnableFocusField");
	}
	
	/**
	 * 是否打开ICON
	 */
	public final boolean getIsEnableOpenICON()
	{
		return this.HisAP.GetValBoolenByKey("IsEnableOpenICON");
	}
	
	/**
	 * 焦点字段
	 */
	public final String getFocusField()
	{
		String s = this.HisAP.GetValStrByKey("FocusField");
		if (StringHelper.isNullOrEmpty(s))
		{
			Attrs attrs = this.HisEn.getEnMap().getAttrs();
			if (attrs.Contains("Name"))
			{
				return "Name";
			}
			if (attrs.Contains("Title"))
			{
				return "Title";
			}
		}
		return s;
	}
	
	public final int getWinCardW()
	{
		return this.HisAP.GetValIntByKey("WinCardW");
	}
	
	public final int getWinCardH()
	{
		return this.HisAP.GetValIntByKey("WinCardH");
	}
	
	/**
	 * 保存
	 * 
	 * @return
	 */
	public final int Save()
	{
		EnCfg cfg = new EnCfg(this.HisEn.toString());
		cfg.setUI(this.HisAP.GenerAtParaStrs());
		return cfg.Save();
	}
}

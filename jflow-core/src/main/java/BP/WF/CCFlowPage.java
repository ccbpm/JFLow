package BP.WF;

import BP.Web.*;
import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.*;
import BP.En.*;

/** 
 PortalPage 的摘要说明。
*/
public class CCFlowPage extends BP.Web.PageBase
{
	public final String getRequestParas()
	{
		String urlExt = "";
		String rawUrl = ContextHolderUtils.getRequest().getQueryString();
		rawUrl = "&" + rawUrl.substring(rawUrl.indexOf('?') + 1);
		String[] paras = rawUrl.split("[&]", -1);
		for (String para : paras)
		{
			if (para == null || para.equals("") || para.contains("=") == false)
			{
				continue;
			}
			urlExt += "&" + para;
		}
		return urlExt;
	}
	/** 
	 key.
	*/
	public final String getKey()
	{
		return  getParamter("Key");
	}
	/** 
	 _HisEns
	*/
	public Entities _HisEns = null;
	/** 
	 他的相关功能
	*/
	public final Entities getHisEns()
	{
		if (this.getEnsName() != null)
		{
			if (this._HisEns == null)
			{
				_HisEns = BP.En.ClassFactory.GetEns(this.getEnsName());
			}
		}
		return _HisEns;
	}
	private Entity _HisEn = null;
	/** 
	 他的相关功能
	*/
	public final Entity getHisEn()
	{
		if (_HisEn == null)
		{
			_HisEn = this.getHisEns().getGetNewEntity();
		}
		return _HisEn;
	}
	public final String getPageID()
	{
		return this.getCurrPage();
	}
	public final String getCurrPage()
	{
		String url = BP.Sys.Glo.getRequest().getRequestURL().toString();
		int i = url.lastIndexOf("/") + 1;
		int i2 = url.indexOf(".jsp") - 6;
		try
		{
			url = url.substring(i);
			url = url.substring(0, url.indexOf(".jsp"));
			return url;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + url + " i=" + i + " i2=" + i2);
		}
	}
	public final String getDoType()
	{
		String str = getParamter("DoType");
		if ("".equals(str))
		{
			str = null;
		}
		return str;
	}
	public final String getEnsName()
	{
		String s = getParamter("EnsName");
		if (s == null)
		{
			s = this.getParamter("EnsName");
		}

		return s;
	}
	public final String getEnName()
	{
		String s = getParamter("EnName");
		if (s == null)
		{
			s = getParamter("EnName");
		}
		return s;
	}
	public final String getRefPK()
	{
		String s = getParamter("RefPK");
		if (s == null || s.equals(""))
		{
			s = getParamter("PK");
		}

		return s;
	}
	/** 
	 页面Index.
	*/
	public final int getPageIdx()
	{
        
		String str = getParamter("PageIdx");
		if (str == null || "".equals(str) || "undefined".equals(str) || "null".equals(str))
		{
			return 1;
		}
		return Integer.parseInt(str);
	}
//	public final void setPageIdx(int value)
//	{
//		ViewState["PageIdx"] = value;
//	}
	public final String getRefNo()
	{
		String s = getParamter("RefNo");
		if (s == null || s.equals(""))
		{
			s = getParamter("No");
		}

		if (s == null || "".equals(s))
		{
			s = null;
		}
		return s;
	}
	/** 
	 当前页面的参数．
	*/
	public final String getParas()
	{
		/*String str = "";
		for (String s : this.Request.QueryString)
		{
			str += "&" + s + "=" + this.Request.QueryString[s];
		}*/
		return BP.Sys.Glo.getRequest().getQueryString();
	}
//	protected void OnLoad(EventArgs e)
//	{
//		super.OnLoad(e);
//	}
	private String getParamter(String key)
	{
		return ContextHolderUtils.getRequest().getParameter(key);
	}
}
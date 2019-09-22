package BP.En;

import java.util.Enumeration;

import BP.Difference.ContextHolderUtils;
import WebUser;

/**
 * SearchKey 的摘要说明。 用来处理一条记录的存放，问题。
 */
public class AttrOfSearch
{
	// 基本属性
	/**
	 * 查询属性
	 */
	public Attr HisAttr = null;
	
	/**
	 * 是否隐藏
	 */
	private boolean _IsHidden = false;
	
	/**
	 * 是否隐藏
	 */
	public final boolean getIsHidden()
	{
		return _IsHidden;
	}
	
	public final void setIsHidden(boolean value)
	{
		_IsHidden = value;
	}
	
	/**
	 * 操作是否可用
	 */
	private boolean _SymbolEnable = true;
	
	/**
	 * 操作是否可用
	 */
	public final boolean getSymbolEnable()
	{
		return _SymbolEnable;
	}
	
	public final void setSymbolEnable(boolean value)
	{
		_SymbolEnable = value;
	}
	
	/**
	 * 标签
	 */
	private String _Lab = "";
	
	/**
	 * 标签
	 */
	public final String getLab()
	{
		return _Lab;
	}
	
	public final void setLab(String value)
	{
		_Lab = value;
	}
	
	/**
	 * 查询默认值
	 */
	private String _DefaultVal = "";
	
	/**
	 * OperatorKey
	 */
	public final String getDefaultVal()
	{
		return _DefaultVal;
	}
	
	public final void setDefaultVal(String value)
	{
		_DefaultVal = value;
	}
	
	/**
	 * 默认值
	 * @throws Exception 
	 */
	public final String getDefaultValRun() throws Exception
	{
		if (_DefaultVal == null)
		{
			return null;
		}
		
		if (_DefaultVal.contains("@"))
		{
			if (_DefaultVal.contains("@WebUser.getNo()"))
			{
				return _DefaultVal.replace("@WebUser.getNo()", WebUser.getNo());
			}
			
			if (_DefaultVal.contains("@WebUser.getName()"))
			{
				return _DefaultVal.replace("@WebUser.getName()", WebUser.getName());
			}
			
			if (_DefaultVal.contains("@WebUser.getFK_Dept()"))
			{
				return _DefaultVal.replace("@WebUser.getFK_Dept()",
						WebUser.getFK_Dept());
			}
			
			if (_DefaultVal.contains("@WebUser.DeptParentNo"))
			{
				return _DefaultVal.replace("@WebUser.DeptParentNo",
						WebUser.getDeptParentNo());
			}
			
			if (_DefaultVal.contains("@WebUser.getFK_Dept()Name"))
			{
				return _DefaultVal.replace("@WebUser.getFK_Dept()Name",
						WebUser.getFK_DeptName());
			}
			
			if (_DefaultVal.contains("@WebUser.getFK_Dept()NameOfFull"))
			{
				return _DefaultVal.replace("@WebUser.getFK_Dept()NameOfFull",
						WebUser.getFK_DeptNameOfFull());
			}
			
			//替换URL传的参数
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements())
			{
				// 判断是否有内容，hasNext()
				String key = (String) enu.nextElement();
				if (_DefaultVal.contains(key))
					return _DefaultVal.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key));
			
			}
			
			
		}
		return _DefaultVal;
	}
	
	/**
	 * 默认的操作符号.
	 */
	private String _defaultSymbol = "=";
	
	/**
	 * 操作符号
	 */
	public final String getDefaultSymbol()
	{
		return _defaultSymbol;
	}
	
	public final void setDefaultSymbol(String value)
	{
		_defaultSymbol = value;
	}
	
	/**
	 * 对应的属性
	 */
	private String _RefAttr = "";
	
	/**
	 * 对应的属性
	 */
	public final String getRefAttrKey()
	{
		return _RefAttr;
	}
	
	public final void setRefAttrKey(String value)
	{
		_RefAttr = value;
	}
	
	/**
	 * Key
	 */
	private String _Key = "";
	
	/**
	 * Key
	 */
	public final String getKey()
	{
		return _Key;
	}
	
	public final void setKey(String value)
	{
		_Key = value;
	}
	
	/**
	 * TB 宽度
	 */
	private int _TBWidth = 10;
	
	/**
	 * TBWidth
	 */
	public final int getTBWidth()
	{
		return _TBWidth;
	}
	
	public final void setTBWidth(int value)
	{
		_TBWidth = value;
	}
	
	// 构造方法
	/**
	 * 构造一个普通的查询属性
	 */
	public AttrOfSearch(String key, String lab, String refAttr,
			String DefaultSymbol, String defaultValue, int tbwidth,
			boolean isHidden)
	{
		this.setKey(key);
		this.setLab(lab);
		this.setRefAttrKey(refAttr);
		this.setDefaultSymbol(DefaultSymbol);
		this.setDefaultVal(defaultValue);
		this.setTBWidth(tbwidth);
		this.setIsHidden(isHidden);
	}
	
	public AttrOfSearch(Attr attr, String DefaultSymbol, String defaultValue,
			int tbwidth, boolean isHidden)
	{
		this.HisAttr = attr;
		this.setKey(attr.getKey());
		this.setLab(attr.getDesc());
		this.setRefAttrKey(attr.getKey());
		this.setDefaultSymbol(DefaultSymbol);
		this.setDefaultVal(defaultValue);
		this.setTBWidth(tbwidth);
		this.setIsHidden(isHidden);
	}
}
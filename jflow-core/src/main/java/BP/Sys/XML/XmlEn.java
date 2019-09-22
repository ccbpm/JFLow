package BP.Sys.XML;

import java.util.ArrayList;

import BP.DA.Cash;
import BP.DA.Depositary;
import BP.En.Row;

/**
 * XmlEn 的摘要说明。
 */
public abstract class XmlEn
{
	// 获取值
	private Row _row = null;
	
	public final Row getRow()
	{
		if (this._row == null)
		{
			this._row = new Row();
		}
		// throw new Exception("xmlEn 没有被实例化。");
		return this._row;
	}
	
	public final void setRow(Row value)
	{
		this._row = value;
	}
	
	/**
	 * 获取一个对象
	 * 
	 * @param attrKey
	 * @return
	 */
	public final Object GetValByKey(String attrKey)
	{
		if (this._row == null)
		{
			return null;
		}
		
		return this.getRow().GetValByKey(attrKey);
	}
	
	public final int GetValIntByKey(String key)
	{
		try
		{
			return Integer.parseInt(this.GetValByKey(key).toString().trim());
		} catch (java.lang.Exception e)
		{
			throw new RuntimeException("key=" + key + "不能向int 类型转换。val="
					+ this.GetValByKey(key));
		}
	}
	
	public final java.math.BigDecimal GetValDecimalByKey(String key)
	{
		return (java.math.BigDecimal) this.GetValByKey(key);
	}
	
	/**
	 * 获取一个对象
	 * 
	 * @param attrKey
	 * @return
	 */
	public String GetValStringByKey(String attrKey)
	{
		if (this._row == null)
		{
			return "";
		}
		
		try
		{
			Object tempVar = this.getRow().GetValByKey(attrKey);
			return (String) ((tempVar instanceof String) ? tempVar : null);
		} catch (RuntimeException ex)
		{
			throw new RuntimeException(" @XMLEN Error Attr=[" + attrKey
					+ "], ClassName= " + this.toString() + " , File ="
					+ this.getGetNewEntities().getFile() + " , Error = "
					+ ex.getMessage());
		}
	}
	
	public final String GetValStringHtmlByKey(String attrKey)
	{
		return this.GetValStringByKey(attrKey).replace("\n", "<BR>")
				.replace(" ", "&nbsp;");
	}
	
	/**
	 * 获取一个对象
	 * 
	 * @param key
	 * @return
	 */
	public final boolean GetValBoolByKey(String key)
	{
		String val = this.GetValStringByKey(key);
		if (val != null)
		{
			if (val.equals("1") || val.toUpperCase().equals("TRUE"))
			{
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}
	
	public final void SetVal(String k, Object val)
	{
		this.getRow().SetValByKey(k, val);
	}
	
	// 获取值
	
	// 构造函数
	/**
	 * 构造函数
	 */
	public XmlEn()
	{
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<XmlEn> convertXmlEns(Object obj)
	{
		return (ArrayList<XmlEn>) obj;
	}
	
	/**
	 * 指定属性查询
	 * 
	 * @param key
	 *            属性值
	 * @param val
	 *            属性
	 */
	public final int RetrieveBy_del(String key, String val)
	{
		XmlEns ens = this.getGetNewEntities();
		ens.RetrieveAll();
		
		ens.RetrieveBy(key, val);
		if (ens.size() == 0)
		{
			return 0;
		}
		this.setRow(ens.getItem(0)._row);
		/*
		 * warning this.setRow(ens.getValue(0).Row);
		 */
		return ens.size();
	}
	
	public final int RetrieveByPK(String key, String val)
	{
		Object tempVar = Cash.GetObj(this.getGetNewEntities().toString(),
				Depositary.Application);
		XmlEns ens = (XmlEns) ((tempVar instanceof XmlEns) ? tempVar : null);
		if (ens == null)
		{
			ens = this.getGetNewEntities();
			ens.RetrieveAll();
		}
		
		int i = 0;
		for (XmlEn en : convertXmlEns(ens))
		/*
		 * warning for (XmlEn en : ens)
		 */
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				this.setRow(en.getRow());
				i++;
			}
		}
		if (i == 1)
		{
			return 1;
		}
		
		if (i > 1)
		{
			// BP.SystemConfig.DoClearCash();
			throw new RuntimeException("@XML = " + this.toString() + " 中 PK="
					+ val + "不唯一。。。。");
		}
		return 0;
	}
	
	public final int Retrieve(String key, String val, String key1, String val1)
	{
		Object tempVar = Cash.GetObj(this.getGetNewEntities().toString(),
				Depositary.Application);
		XmlEns ens = (XmlEns) ((tempVar instanceof XmlEns) ? tempVar : null);
		if (ens == null)
		{
			ens = this.getGetNewEntities();
			ens.RetrieveAll();
		}
		
		int i = 0;
		for (XmlEn en : convertXmlEns(ens))
		/*
		 * warning for (XmlEn en : ens)
		 */
		{
			if (en.GetValStringByKey(key).equals(val)
					&& en.GetValStringByKey(key1).equals(val1))
			{
				this.setRow(en.getRow());
				i++;
			}
		}
		if (i == 1)
		{
			return 1;
		}
		
		return 0;
	}
	
	// 构造函数
	
	// 需要子类实现的方法
	public abstract XmlEns getGetNewEntities();
	
	// 需要子类实现的方法
	
	@Override
	public String toString()
	{
		return this.getClass().getName();
	}
}

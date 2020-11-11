package bp.sys.xml;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.*;
import bp.sys.*;
import java.util.*;
import java.io.*;
import java.math.*;

/** 
 XmlEn 的摘要说明。
*/
public abstract class XmlEn
{

		///获取值
	private Row _row = null;
	public final Row getRow()
	{
		if (this._row == null)
		{
			this._row = new Row();
		}
			//    throw new Exception("xmlEn 没有被实例化。");
		return this._row;
	}
	public final void setRow(Row value)
	{
		this._row = value;
	}
	/** 
	 获取一个对象
	 
	 @param attrKey
	 @return 
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
		}
		catch (java.lang.Exception e)
		{
			throw new RuntimeException("key=" + key + "不能向int 类型转换。val=" + this.GetValByKey(key));
		}
	}
	public final BigDecimal GetValDecimalByKey(String key)
	{
		return (BigDecimal)this.GetValByKey(key);
	}
	/** 
	 获取一个对象
	 
	 @param attrKey
	 @return 
	*/
	public final String GetValStringByKey(String attrKey)
	{
		if (this._row == null)
		{
			return "";
		}

		try
		{
			Object tempVar = this.getRow().GetValByKey(attrKey);
			return tempVar instanceof String ? (String)tempVar : null;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(" @XMLEN Error Attr=[" + attrKey + "], ClassName= " + this.toString() + " , File =" + this.getGetNewEntities().getFile() + " , Error = " + ex.getMessage());
		}
	}
	public final String GetValStringHtmlByKey(String attrKey)
	{
		return this.GetValStringByKey(attrKey).replace("\n", "<BR>").replace(" ", "&nbsp;");
	}
	/** 
	 获取一个对象
	 
	 @param key
	 @return 
	*/
	public final boolean GetValBoolByKey(String key)
	{
		String val = this.GetValStringByKey(key);
		if (DataType.IsNullOrEmpty(val))
		{
			return false;
		}

		if (val.equals("1") || val.toUpperCase().equals("TRUE"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public final void SetVal(String k, Object val)
	{
		this.getRow().SetValByKey(k, val);
	}

		/// 获取值


		///构造函数
	/** 
	 构造函数
	*/
	public XmlEn()
	{
	}

	public final int RetrieveByPK(String key, String val) throws Exception
	{
		Object tempVar = Cash.GetObj(this.getGetNewEntities().toString(), Depositary.Application);
		XmlEns ens = tempVar instanceof XmlEns ? (XmlEns)tempVar : null;
		if (ens == null)
		{
			ens = this.getGetNewEntities();
			ens.RetrieveAll();
		}

		int i = 0;
		for (XmlEn en : ens.ToJavaListXmlEns())
		{
			if (en.GetValStringByKey(key).equals(val))
			{
				this.setRow(en.getRow());
				i++;
				break;
			}
		}
		if (i == 1)
		{
			return 1;
		}

		if (i > 1)
		{
		   // SystemConfig.DoClearCash();
			throw new RuntimeException("@XML=[" + this.toString() + "]中PK=" + val + "不唯一...");
		}
		return 0;
	}
	public final int Retrieve(String key, String val, String key1, String val1) throws Exception
	{
		Object tempVar = Cash.GetObj(this.getGetNewEntities().toString(), Depositary.Application);
		XmlEns ens = tempVar instanceof XmlEns ? (XmlEns)tempVar : null;
		if (ens == null)
		{
			ens = this.getGetNewEntities();
			ens.RetrieveAll();
		}

		int i = 0;
		for (XmlEn en : ens.ToJavaListXmlEns())
		{
			if (en.GetValStringByKey(key).equals(val) && en.GetValStringByKey(key1).equals(val1))
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

		/// 构造函数


		///需要子类实现的方法
	public abstract XmlEns getGetNewEntities();

		/// 需要子类实现的方法
}
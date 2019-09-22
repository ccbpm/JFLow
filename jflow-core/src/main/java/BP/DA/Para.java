package BP.DA;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

/**
 * 参数
 */
public class Para
{
	// public System.Data.DbType DAType = System.Data.DbType.String;
	
	/*
	 * warning public final System.Data.OracleClient.OracleType getDATypeOfOra()
	 * { switch (this.DAType) { case System.Data.DbType.String: case
	 * System.Data.DbType.Object: return
	 * System.Data.OracleClient.OracleType.VarChar; case
	 * System.Data.DbType.Int32: case System.Data.DbType.Int16: return
	 * System.Data.OracleClient.OracleType.Number; case
	 * System.Data.DbType.Int64: return
	 * System.Data.OracleClient.OracleType.UInt32; case
	 * System.Data.DbType.Decimal: case System.Data.DbType.Double: return
	 * System.Data.OracleClient.OracleType.Double; default: throw new
	 * RuntimeException("没有涉及到的类型。typeof(obj)=" + this.DAType.toString()); } }
	 */
	public static Object getDAType(Object val)
	{
		if (val instanceof Integer)
		{
			return Integer.class;
		} else if (val instanceof String)
		{
			return String.class;
		} else if (val instanceof Double)
		{
			return Double.class;
		} else if (val instanceof Float)
		{
			return Float.class;
		} else if (val instanceof Long)
		{
			return Long.class;
		} else if (val instanceof BigDecimal)
		{
			return BigDecimal.class;
		} else if (val instanceof Boolean)
		{
			return Boolean.class;
		} else if (val instanceof Date)
		{
			return Date.class;
		} else if (val instanceof Timestamp)
		{
			return Timestamp.class;
		} else
		{
			return null;
		}
	}
	
	public static Object getDAType(Integer type)
	{
		Object obj = null;
		switch (type)
		{
			case Types.CHAR:// CHAR
				obj = String.class;
				break;
			case Types.VARCHAR:// VARCHAR
				obj = String.class;
				break;
			case Types.LONGVARCHAR:// LONGVARCHAR
				obj = String.class;
				break;
			case Types.INTEGER:// INTEGER
				obj = Integer.class;
				break;
			case Types.FLOAT:// FLOAT
				obj = Double.class;
				break;
			case Types.DOUBLE:// DOUBLE
				obj = Double.class;
				break;
			case Types.REAL:// REAL
				obj = Float.class;
				break;
			case Types.BIGINT:// BIGINT
				obj = Long.class;
				break;
			case Types.NUMERIC:// NUMERIC
				obj = BigDecimal.class;
				break;
			case Types.DECIMAL:// DECIMAL
				obj = BigDecimal.class;
				break;
			case Types.DATE:// DATE
				obj = Date.class;
				break;
			case Types.TIMESTAMP:// TIMESTAMP
				obj = Date.class;
				break;
			case Types.BIT:// BIT
				obj = Boolean.class;
				break;
			case Types.CLOB:// CLOB
				obj = String.class;
				break;
			default:
				obj = Object.class;
		}
		return obj;
	}
	
	public String ParaName = null;
	public int Size = 10;
	public Object val;
	public Object DAType;
	
	/**
	 * 参数
	 */
	public Para()
	{
	}
	
	/**
	 * 构造参数
	 * 
	 * @param _paraName
	 *            参数名称
	 * @param _DAType
	 *            System.Data.SqlDbType
	 * @param _val
	 *            值
	 */
	/*
	 * warning public Para(String _paraName, System.Data.DbType _DAType, Object
	 * _val) { this.ParaName = _paraName; this.DAType = _DAType; this.val =
	 * _val; }
	 */
	public Para(String _paraName, Object _DAType, Object _val)
	{
		this.ParaName = _paraName;
		this.DAType = _DAType;
		this.val = _val;
	}
}

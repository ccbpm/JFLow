package BP.DA;

import BP.Sys.*;
import java.math.*;

/** 
 参数
*/
public class Para
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public System.Data.DbType DAType = System.Data.DbType.String;
	public final Oracle.ManagedDataAccess.Client.OracleDbType getDATypeOfOra()
	{
		switch (this.DAType)
		{
			case String:
			case Object:
				if (this.IsBigText)
				{
					return Oracle.ManagedDataAccess.Client.OracleDbType.Clob;
				}
				else
				{
					return Oracle.ManagedDataAccess.Client.OracleDbType.Varchar2;
				}
			case Int32:
			case Int16:
				return Oracle.ManagedDataAccess.Client.OracleDbType.Int16;
			case Int64:
				return Oracle.ManagedDataAccess.Client.OracleDbType.Int64;
			case Decimal:
			case Double:
				return Oracle.ManagedDataAccess.Client.OracleDbType.Double;
			default:
				throw new RuntimeException("没有涉及到的类型。typeof(obj)=" + this.DAType.toString());
		}
	}
	public String ParaName = null;
	public int Size = 10;
	public Object val;
	/** 
	 是否是大文本?
	*/
	public boolean IsBigText = false;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 参数
	*/
	public Para()
	{
	}
	/** 
	 构造参数
	 
	 @param _paraName 参数名称
	 @param _DAType System.Data.SqlDbType
	 @param _val 值
	*/
	public Para(String _paraName, System.Data.DbType _DAType, Object _val)
	{
		this.ParaName = _paraName;
		this.DAType = _DAType;
		this.val = _val;
	}
}
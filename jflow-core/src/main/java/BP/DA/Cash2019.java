package BP.DA;

import BP.En.*;
import BP.Pub.*;
import BP.Sys.*;
import java.util.*;

public class Cash2019
{

		///#region 缓存ht
	private static Hashtable _hts;
	public static Hashtable gethts()
	{
		if (_hts == null)
		{
			_hts = new Hashtable();
		}
		return _hts;
	}

		///#endregion


		///#region 对实体的操作.
	/** 
	 把实体放入缓存里面
	 
	 @param enName
	 @param pkVal
	 @param row
	*/
	public static void PutRow(String enName, String pkVal, Row row)
	{
		synchronized (lockObj)
		{
			Object tempVar = gethts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				ht = new Hashtable();
				gethts().put(enName, ht);
			}
			if (ht.containsKey(pkVal) == true)
			{
				ht.put(pkVal, row);
			}
			else
			{
				ht.put(pkVal, row);
			}
		}
	}
	public static void UpdateRow(String enName, String pkVal, Row row)
	{
		synchronized (lockObj)
		{
			Object tempVar = gethts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				ht = new Hashtable();
				gethts().put(enName, ht);
			}
			ht.put(pkVal, row);
		}
	}
	public static void DeleteRow(String enName, String pkVal)
	{
		synchronized (lockObj)
		{
			Object tempVar = gethts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				ht = new Hashtable();
				gethts().put(enName, ht);
			}
			ht.remove(pkVal.toString());
		}
	}
	private static Object lockObj = new Object();
	/** 
	 获得实体类
	 
	 @param enName 实体名字
	 @param pkVal 键
	 @return row
	*/
	public static Row GetRow(String enName, String pkVal)
	{
		synchronized (lockObj)
		{
			Object tempVar = gethts().get(enName);
			Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;
			if (ht == null)
			{
				return null;
			}
			if (ht.containsKey(pkVal) == true)
			{
				return ht.get(pkVal) instanceof Row ? (Row)ht.get(pkVal) : null;
			}
			return null;
		}
	}

		///#endregion 对实体的操作.


		///#region 对实体的集合操作.
	/** 
	 把集合放入缓存.
	 
	 @param ensName 集合实体类名
	 @param ens 实体集合
	*/
	public static void PutEns(String ensName, Entities ens)
	{
		//StackExchange.Redis
	}
	/** 
	 获取实体集合类
	 
	 @param ensName 集合类名
	 @param pkVal 主键
	 @return 实体集合
	*/
	public static Entities GetEns(String ensName, Object pkVal)
	{
		return null;
	}

		///#endregion 对实体的集合操作.

}
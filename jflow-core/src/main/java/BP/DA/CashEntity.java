package BP.DA;

import BP.En.Entities;
import BP.En.Entity;

public class CashEntity
{
	// Hashtable 属性
	private static java.util.Hashtable _Cash;
	
	public static java.util.Hashtable getDCash()
	{
		if (_Cash == null)
		{
			_Cash = new java.util.Hashtable();
		}
		return _Cash;
	}
	
	/**
	 * 把实体放入缓存里面
	 * 
	 * @param enName
	 * @param ens
	 * @param enPK
	 */
	public static void PubEns(String enName, Entities ens, String enPK)
	{
		Object tempVar = CashEntity.getDCash().get(enName);
		java.util.Hashtable ht = (java.util.Hashtable) ((tempVar instanceof java.util.Hashtable) ? tempVar
				: null);
		if (ht == null)
		{
			ht = new java.util.Hashtable();
		}
		
		ht.clear();
		for (Object en : ens)
		{
			ht.put(((Entity) en).GetValStrByKey(enPK), (Entity) en);
		}
		// 把实体集合放入.
		CashEntity.getDCash().put(enName + "Ens", ens);
	}
	
	public static Entities GetEns(String enName)
	{
		Object tempVar = CashEntity.getDCash().get(enName + "Ens");
		Entities ens = (Entities) ((tempVar instanceof Entities) ? tempVar
				: null);
		return ens;
	}
	
	/**
	 * 更新对象
	 * 
	 * @param enName
	 * @param key
	 * @param en
	 */
	public static void Update(String enName, String key, Entity en)
	{
		Object tempVar = CashEntity.getDCash().get(enName);
		java.util.Hashtable ht = (java.util.Hashtable) ((tempVar instanceof java.util.Hashtable) ? tempVar
				: null);
		if (ht == null)
		{
			ht = new java.util.Hashtable();
			CashEntity.getDCash().put(enName, ht);
		}
		ht.put(key, en);
		
		//此处增加一个清缓存，解决java缓存问题
		CashEntity.getDCash().remove(enName);
		
		// 清除集合.
		CashEntity.getDCash().remove(enName + "Ens");
	}
	
	/**
	 * 获取一个实体
	 * 
	 * @param enName
	 *            实体Name
	 * @param pkVal
	 *            主键值
	 * @return 返回这个实体
	 */
	public static Entity Select(String enName, String pkVal)
	{
		Object tempVar = CashEntity.getDCash().get(enName);
		java.util.Hashtable ht = (java.util.Hashtable) ((tempVar instanceof java.util.Hashtable) ? tempVar
				: null);
		if (ht == null)
		{
			return null;
		}
		
		return (Entity) ((ht.get(pkVal) instanceof Entity) ? ht.get(pkVal)
				: null);
	}
	
	/**
	 * 删除
	 * 
	 * @param enName
	 * @param pkVal
	 */
	public static void Delete(String enName, String pkVal)
	{
		Object tempVar = CashEntity.getDCash().get(enName);
		java.util.Hashtable ht = (java.util.Hashtable) ((tempVar instanceof java.util.Hashtable) ? tempVar
				: null);
		if (ht == null)
		{
			return;
		}
		
		ht.remove(pkVal);
		// 清除集合.
		CashEntity.getDCash().remove(enName + "Ens");
	}
	
	/**
	 * 插入
	 * 
	 * @param enName
	 * @param en
	 * @param pkVal
	 */
	public static void Insert(String enName, String pkVal, Entity en)
	{
		Object tempVar = CashEntity.getDCash().get(enName);
		java.util.Hashtable ht = (java.util.Hashtable) ((tempVar instanceof java.util.Hashtable) ? tempVar
				: null);
		if (ht == null)
		{
			//CashEntity.getDCash().remove("BP.WF.Rpt.MapRptEns");
			return;
		}
		
		// edited by liuxc,2014-8-21 17:21
		if (ht.containsKey(pkVal))
		{
			ht.put(pkVal, en);
		} else
		{
			ht.put(pkVal, en);
		}
		
		// 清除集合.
		CashEntity.getDCash().remove(enName + "Ens");
	}
}
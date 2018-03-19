package BP.WF.Data;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 单据s
 */
public class Bills extends Entities
{
	// 构造方法属性
	public static ArrayList<Bill> convertBills(Object obj)
	{
		return (ArrayList<Bill>) obj;
	}
	public List<Bill> ToJavaList()
	{
		return (List<Bill>)(Object)this;
	}
	/**
	 * 单据s
	 */
	public Bills()
	{
	}
	
	// 属性
	/**
	 * 单据
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Bill();
	}
}
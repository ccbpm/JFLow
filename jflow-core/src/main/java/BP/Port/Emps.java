package BP.Port;

import java.util.ArrayList;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 操作员 //</summary>
 */
public class Emps extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<Emp> convertEmps(Object obj)
	{
		return (ArrayList<Emp>) obj;
	}
	
	public ArrayList<Emp> ToJavaList()
	{
		return (ArrayList<Emp>) (Object)this;
	}
	
	// /#region 构造方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Emp();
	}
	
	/**
	 * 操作员s
	 */
	public Emps()
	{
	}
}
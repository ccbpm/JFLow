package BP.Port;

import java.util.ArrayList;

import BP.En.EntitiesNoName;
import BP.En.EntitiesSimpleTree;
import BP.En.Entity;

/**
 * 部门s
 */
public class Depts extends EntitiesSimpleTree
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<Dept> convertDepts(Object obj)
	{
		return (ArrayList<Dept>) obj;
	}
	
	public ArrayList<Dept> ToJavaList()
	{
		return (ArrayList<Dept>) (Object)this;
	}
	
	/**
	 * 得到一个新实体
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Dept();
	}
	
	/**
	 * 部门集合
	 */
	public Depts()
	{
		
	}
	
	/**
	 * 部门集合
	 * 
	 * @param parentNo
	 *            父部门No
	 */
	public Depts(String parentNo)
	{
		this.Retrieve(DeptAttr.ParentNo, parentNo);
	}
}
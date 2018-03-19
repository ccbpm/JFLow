package BP.WF.Entity;

import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.Web.WebUser;

/**
 * 记忆我
 */
public class RememberMe extends EntityMyPK
{
	private Map _enMap;
	
	// 属性
	/**
	 * 操作员
	 */
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(RememberMeAttr.FK_Emp);
	}
	
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(RememberMeAttr.FK_Emp, value);
		this.setMyPK(this.getFK_Node() + "_" + WebUser.getNo());
	}
	
	/**
	 * 当前节点
	 */
	public final int getFK_Node()
	{
		return this.GetValIntByKey(RememberMeAttr.FK_Node);
	}
	
	public final void setFK_Node(int value)
	{
		this.SetValByKey(RememberMeAttr.FK_Node, value);
		this.setMyPK(this.getFK_Node() + "_" + WebUser.getNo());
	}
	
	/**
	 * 有效的工作人员
	 */
	public final String getObjs()
	{
		return this.GetValStringByKey(RememberMeAttr.Objs);
	}
	
	public final void setObjs(String value)
	{
		this.SetValByKey(RememberMeAttr.Objs, value);
	}
	
	/**
	 * 有效的操作人员ext
	 */
	public final String getObjsExt()
	{
		return this.GetValStringByKey(RememberMeAttr.ObjsExt);
	}
	
	public final void setObjsExt(String value)
	{
		this.SetValByKey(RememberMeAttr.ObjsExt, value);
	}
	
	/**
	 * 所有的人员数量.
	 */
	public final int getNumOfEmps()
	{
		return this.getEmps().split("[@]", -1).length - 2;
	}
	
	/**
	 * 可以处理的人员数量
	 */
	public final int getNumOfObjs()
	{
		return this.getObjs().split("[@]", -1).length - 2;
	}
	
	/**
	 * 所有的工作人员
	 */
	public final String getEmps()
	{
		return this.GetValStringByKey(RememberMeAttr.Emps);
	}
	
	public final void setEmps(String value)
	{
		this.SetValByKey(RememberMeAttr.Emps, value);
	}
	
	/**
	 * 所有的工作人员ext
	 */
	public final String getEmpsExt()
	{
		String str = this.GetValStringByKey(RememberMeAttr.EmpsExt).trim();
		if (str.length() == 0)
		{
			return str;
		}
		
		if (str.substring(str.length() - 1).equals("、"))
		{
			return str.substring(0, str.length() - 1);
		} else
		{
			return str;
		}
	}
	
	public final void setEmpsExt(String value)
	{
		this.SetValByKey(RememberMeAttr.EmpsExt, value);
	}
	
	// 构造函数
	/**
	 * RememberMe
	 */
	public RememberMe()
	{
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_RememberMe");
		map.setEnDesc("记忆我");
		map.setEnType(EnType.Admin);
		
		map.AddMyPK();
		
		map.AddTBInt(RememberMeAttr.FK_Node, 0, "节点", false, false);
		map.AddTBString(RememberMeAttr.FK_Emp, "", "当前操作人员", true, false, 1,
				30, 10);
		
		map.AddTBString(RememberMeAttr.Objs, "", "分配人员", true, false, 0, 4000,
				10);
		map.AddTBString(RememberMeAttr.ObjsExt, "", "分配人员Ext", true, false, 0,
				4000, 10);
		
		map.AddTBString(RememberMeAttr.Emps, "", "所有的工作人员", true, false, 0,
				4000, 10);
		map.AddTBString(RememberMeAttr.EmpsExt, "", "工作人员Ext", true, false, 0,
				4000, 10);
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setFK_Emp(WebUser.getNo());
		this.setMyPK(this.getFK_Node() + "_" + this.getFK_Emp());
		return super.beforeUpdateInsertAction();
	}
}
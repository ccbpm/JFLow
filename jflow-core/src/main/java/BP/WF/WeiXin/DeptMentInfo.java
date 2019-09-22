package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 部门信息
*/
public class DeptMentInfo
{
	/** 
	  部门id 
	*/
	private String id;
	public final String getid()
	{
		return id;
	}
	public final void setid(String value)
	{
		id = value;
	}
	/** 
	 部门名称 
	*/
	private String name;
	public final String getname()
	{
		return name;
	}
	public final void setname(String value)
	{
		name = value;
	}
	/** 
	  父亲部门id。根部门为1 
	*/
	private String parentid;
	public final String getparentid()
	{
		return parentid;
	}
	public final void setparentid(String value)
	{
		parentid = value;
	}
	/** 
	  在父部门中的次序值。order值小的排序靠前
	*/
	private String order;
	public final String getorder()
	{
		return order;
	}
	public final void setorder(String value)
	{
		order = value;
	}
}
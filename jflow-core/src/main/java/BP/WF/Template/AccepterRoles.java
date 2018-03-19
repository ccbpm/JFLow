package BP.WF.Template;

import BP.En.EntitiesOID;
import BP.En.Entity;

/** 
 接受人规则集合
*/
public class AccepterRoles extends EntitiesOID
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new AccepterRole();
	}
		
	/** 
	 接受人规则集合
	*/
	public AccepterRoles()
	{
	}
	/** 
	 接受人规则集合.
	 @param FlowNo
	*/
	public AccepterRoles(String FK_Node)
	{
		this.Retrieve(AccepterRoleAttr.FK_Node, FK_Node);
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<AccepterRole> ToJavaList()
	{
		return (java.util.List<AccepterRole>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<AccepterRole> Tolist()
	{
		java.util.ArrayList<AccepterRole> list = new java.util.ArrayList<AccepterRole>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((AccepterRole)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
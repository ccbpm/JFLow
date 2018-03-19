package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.En.QueryObject;

/** 
 表单s
 
*/
public class Frms extends EntitiesNoName
{
	/** 
	 Frm
	 
	*/
	public Frms()
	{
	}
	/** 
	 Frm
	 
	 @param fk_flow
	*/
	public Frms(String fk_flow)
	{
		this.Retrieve(FrmAttr.FK_Flow, fk_flow);
	}
	public Frms(int fk_node)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereInSQL(FrmAttr.No, "SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + fk_node);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Frm();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Frm> ToJavaList()
	{
		return (java.util.List<Frm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<Frm> Tolist()
	{
		java.util.ArrayList<Frm> list = new java.util.ArrayList<Frm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Frm)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
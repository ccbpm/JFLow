package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

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
	 * @throws Exception 
	*/
	public Frms(String fk_flow) throws Exception
	{
		this.Retrieve(FrmAttr.FK_Flow, fk_flow);
	}
	public Frms(int fk_node) throws Exception
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


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Frm> ToJavaList()
	{
		return (List<Frm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Frm> Tolist()
	{
		ArrayList<Frm> list = new ArrayList<Frm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Frm)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
package bp.wf.template;
import bp.en.*;
import java.util.*;

/** 
 Accpter
*/
public class Selectors extends Entities
{
	/** 
	 Accpter
	*/
	public Selectors()
	{
	}

	public Selectors(String fk_flow) throws Exception
	{
		String sql = "select NodeId from WF_Node where FK_Flow='" + fk_flow + "'";
		this.RetrieveInSQL(sql);
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Selector();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Selector> ToJavaList()
	{
		return (List<Selector>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Selector> Tolist()
	{
		ArrayList<Selector> list = new ArrayList<Selector>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Selector)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
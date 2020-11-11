package bp.ccbill.template;
import bp.en.*;
import java.util.*;

/** 
 表单方法
*/
public class Methods extends EntitiesMyPK
{
	private static final long serialVersionUID = 1L;
	/** 
	 表单方法
	*/
	public Methods()
	{
	}
	/** 
	 表单方法
	 
	 @param nodeid 方法IDID
	 * @throws Exception 
	*/
	public Methods(int nodeid) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MethodAttr.MethodID, nodeid);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Method();
	}

		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Method> ToJavaList()
	{
		return (List<Method>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Method> Tolist()
	{
		ArrayList<Method> list = new ArrayList<Method>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Method)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
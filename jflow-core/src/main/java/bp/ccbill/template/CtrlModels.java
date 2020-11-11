package bp.ccbill.template;
import bp.en.*;
import java.util.*;

/** 
 控制模型集合s
*/
public class CtrlModels extends EntitiesMyPK
{
	private static final long serialVersionUID = 1L;
		///构造方法.
	/** 
	 控制模型集合
	*/
	public CtrlModels()
	{
	}
	@Override
	public Entity getGetNewEntity()
	{
		return new CtrlModel();
	}

		/// 构造方法.


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CtrlModel> ToJavaList()
	{
		return (List<CtrlModel>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CtrlModel> Tolist()
	{
		ArrayList<CtrlModel> list = new ArrayList<CtrlModel>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CtrlModel)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}
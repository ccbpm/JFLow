package bp.wf.data;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 工作质量评价s bp.port.FK.Evals
*/
public class Evals extends EntitiesMyPK
{

		///方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Eval();
	}
	/** 
	 工作质量评价s
	*/
	public Evals()
	{
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Eval> ToJavaList()
	{
		return (List<Eval>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Eval> Tolist()
	{
		ArrayList<Eval> list = new ArrayList<Eval>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Eval)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
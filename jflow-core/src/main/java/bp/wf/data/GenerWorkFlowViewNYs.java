package bp.wf.data;

import bp.en.*;
import java.util.*;

/** 
 月份s
*/
public class GenerWorkFlowViewNYs extends EntitiesNoName
{

		///构造
	/** 
	 月份s
	*/
	public GenerWorkFlowViewNYs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerWorkFlowViewNY();
	}

		///

	@Override
	public int RetrieveAll() throws Exception
	{
		String sql = "SELECT DISTINCT FK_NY, FK_NY FROM WF_GenerWorkFlow";

		return super.RetrieveAll();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GenerWorkFlowViewNY> ToJavaList()
	{
		return (List<GenerWorkFlowViewNY>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GenerWorkFlowViewNY> Tolist()
	{
		ArrayList<GenerWorkFlowViewNY> list = new ArrayList<GenerWorkFlowViewNY>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerWorkFlowViewNY)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
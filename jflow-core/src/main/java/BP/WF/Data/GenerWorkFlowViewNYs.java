package BP.WF.Data;

import BP.En.EntitiesNoName;
import BP.En.Entity;
/** 
 月份s
*/
public class GenerWorkFlowViewNYs extends EntitiesNoName
{

		
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
	@Override
	public int RetrieveAll()
	{
		String sql = "SELECT DISTINCT FK_NY, FK_NY FROM WF_GenerWorkFlow";

		return super.RetrieveAll();
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<GenerWorkFlowViewNY> ToJavaList()
	{
		return (java.util.List<GenerWorkFlowViewNY>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<GenerWorkFlowViewNY> Tolist()
	{
		java.util.ArrayList<GenerWorkFlowViewNY> list = new java.util.ArrayList<GenerWorkFlowViewNY>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerWorkFlowViewNY)this.get(i));
		}
		return list;
	}
}
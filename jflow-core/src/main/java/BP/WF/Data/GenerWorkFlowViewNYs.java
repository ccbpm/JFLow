package BP.WF.Data;

import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 月份s
*/
public class GenerWorkFlowViewNYs extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
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
	public Entity getNewEntity()
	{
		return new GenerWorkFlowViewNY();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	public int RetrieveAll() throws Exception
	{
		String sql = "SELECT DISTINCT FK_NY, FK_NY FROM WF_GenerWorkFlow";

		return super.RetrieveAll();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
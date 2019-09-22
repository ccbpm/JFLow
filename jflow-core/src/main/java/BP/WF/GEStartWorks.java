package BP.WF;

import BP.DA.*;
import BP.En.*;

/** 
 开始工作节点s
*/
public class GEStartWorks extends Works
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载基类方法
	/** 
	 节点ID
	*/
	public int NodeID = 0;
	public String NodeFrmID = "";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		if (this.NodeID == 0)
		{
			return new GEStartWork();
		}
		return new GEStartWork(this.NodeID, this.NodeFrmID);
	}
	/** 
	 开始工作节点ID
	*/
	public GEStartWorks()
	{

	}
	/** 
	 开始工作节点ID
	 
	 @param nodeid
	*/
	public GEStartWorks(int nodeid, String nodeFrmID)
	{
		this.NodeID = nodeid;
		this.NodeFrmID = nodeFrmID;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
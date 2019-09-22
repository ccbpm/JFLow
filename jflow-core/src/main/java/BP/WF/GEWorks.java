package BP.WF;

import BP.DA.*;
import BP.WF.*;
import BP.En.*;

/** 
 普通工作s
*/
public class GEWorks extends Works
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载基类方法
	/** 
	 节点ID
	*/
	public int NodeID = 0;
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
			return new GEWork();
		}
		return new GEWork(this.NodeID, this.NodeFrmID);
	}
	/** 
	 普通工作ID
	*/
	public GEWorks()
	{
	}
	/** 
	 普通工作ID
	 
	 @param nodeid
	*/
	public GEWorks(int nodeid, String nodeFrmID)
	{
		this.NodeID = nodeid;
		this.NodeFrmID = nodeFrmID;
	}
	public String NodeFrmID = "";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
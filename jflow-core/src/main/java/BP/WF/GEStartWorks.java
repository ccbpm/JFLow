package BP.WF;

import BP.DA.*;
import BP.En.*;

/** 
 开始工作节点s
*/
public class GEStartWorks extends Works
{

		///#region 重载基类方法
	/** 
	 节点ID
	*/
	public int NodeID = 0;
	public String NodeFrmID = "";

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
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

		///#endregion
}
package bp.wf;

import bp.da.*;
import bp.wf.*;
import bp.en.*;

/** 
 普通工作s
*/
public class GEWorks extends Works
{

		///重载基类方法
	/** 
	 节点ID
	*/
	public int NodeID = 0;

		///


		///方法
	/** 
	 得到它的 Entity
	 * @throws Exception 
	*/
	@Override
	public Entity getGetNewEntity() throws Exception
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

		///
}
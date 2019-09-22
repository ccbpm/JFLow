package BP.WF;

import BP.DA.*;
import BP.En.*;

/** 
 开始工作节点
*/
public class GEStartWork extends StartWork
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 开始工作节点
	*/
	public GEStartWork()
	{
	}
	/** 
	 开始工作节点
	 
	 @param nodeid 节点ID
	*/
	public GEStartWork(int nodeid, String nodeFrmID)
	{
		this.setNodeID(nodeid);
		this.NodeFrmID = nodeFrmID;
		this.setSQLCash(null);

	}
	/** 
	 开始工作节点
	 
	 @param nodeid 节点ID
	 @param _oid OID
	*/
	public GEStartWork(int nodeid, String nodeFrmID, long _oid)
	{
		this.setNodeID(nodeid);
		this.NodeFrmID = nodeFrmID;
		this.setOID(_oid);
		this.setSQLCash(null);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Map
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		this._enMap = BP.Sys.MapData.GenerHisMap(this.NodeFrmID);
		return this._enMap;
	}
	/** 
	 获得他的集合
	*/
	@Override
	public Entities getGetNewEntities()
	{
		if (this.getNodeID() == 0)
		{
			return new GEStartWorks();
		}
		return new GEStartWorks(this.getNodeID(), this.NodeFrmID);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
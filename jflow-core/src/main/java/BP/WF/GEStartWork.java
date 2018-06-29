package BP.WF;

import BP.En.Entities;
import BP.En.Map;

/** 
 开始工作节点
 
*/
public class GEStartWork extends StartWork
{

	
		
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
	 
	/** 
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		 
			try {
				this.set_enMap(BP.Sys.MapData.GenerHisMap(this.NodeFrmID));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		return this.get_enMap();
	}
	@Override
	public Entities getGetNewEntities()
	{
		if (this.getNodeID() == 0)
		{
			return new GEStartWorks();
		}
		return new GEStartWorks(this.getNodeID(), this.NodeFrmID);
	}

		///#endregion
}
package bp.wf;

import bp.da.*;
import bp.wf.*;
import bp.en.*;
import bp.sys.MapData;

/** 
 普通工作
*/
public class GEWork extends Work
{

		///与_SQLCash 操作有关
	private SQLCash _SQLCash = null;
	@Override
	public SQLCash getSQLCash() throws Exception
	{
		if (_SQLCash == null)
		{
			_SQLCash = bp.da.Cash.GetSQL(this.NodeFrmID.toString());
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				bp.da.Cash.SetSQL(this.NodeFrmID.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}
	@Override
	public void setSQLCash(SQLCash value)
	{
		_SQLCash = value;
	}

		///


		///构造函数
	/** 
	 普通工作
	*/
	public GEWork()
	{
	}
	/** 
	 普通工作
	 
	 @param nodeid 节点ID
	 * @throws Exception 
	*/
	public GEWork(int nodeid, String nodeFrmID) throws Exception
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setSQLCash(null);
	}
	/** 
	 普通工作
	 
	 @param nodeid 节点ID
	 @param _oid OID
	 * @throws Exception 
	*/
	public GEWork(int nodeid, String nodeFrmID, long _oid) throws Exception
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setOID(_oid);
		this.setSQLCash(null);
	}

		///


		///Map
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		this.set_enMap(MapData.GenerHisMap(this.NodeFrmID));
		return this.get_enMap();
	}
	/** 
	 GEWorks
	 * @throws Exception 
	*/
	@Override
	public Entities getGetNewEntities() throws Exception
	{
		if (this.getNodeID() == 0)
		{
			return new GEWorks();
		}
		return new GEWorks(this.getNodeID(), this.NodeFrmID);
	}

		///

	/** 
	 重写tostring 返回fromID.
	 
	 @return 
	*/
	@Override
	public String toString()
	{
		return this.NodeFrmID;
	}
}
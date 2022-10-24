package bp.wf;

import bp.da.*;
import bp.en.*;

/** 
 普通工作
*/
public class GEWork extends Work
{

		///#region 与_SQLCash 操作有关
	private SQLCash _SQLCash = null;
	@Override
	public SQLCash getSQLCash() throws Exception {
		if (_SQLCash == null)
		{
			_SQLCash = Cash.GetSQL(this.NodeFrmID.toString());
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				Cash.SetSQL(this.NodeFrmID.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}
	@Override
	public void setSQLCash(SQLCash value)
	{_SQLCash = value;
	}

		///#endregion


		///#region 构造函数
	/** 
	 普通工作
	*/
	public GEWork()  {
	}
	/** 
	 普通工作
	 
	 param nodeid 节点ID
	*/
	public GEWork(int nodeid, String nodeFrmID)
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setSQLCash(null);
	}
	/** 
	 普通工作
	 
	 param nodeid 节点ID
	 param _oid OID
	*/
	public GEWork(int nodeid, String nodeFrmID, long _oid) throws Exception {
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setOID(_oid);
		this.setSQLCash(null);
	}

		///#endregion


		///#region Map
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		try {
			this.set_enMap(bp.sys.MapData.GenerHisMap(this.NodeFrmID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.get_enMap();
	}
	/** 
	 GEWorks
	*/
	@Override
	public Entities getGetNewEntities() throws Exception {
		if (this.getNodeID() == 0)
		{
			return new GEWorks();
		}
		return new GEWorks(this.getNodeID(), this.NodeFrmID);
	}

		///#endregion

	/** 
	 重写tostring 返回fromID.
	 
	 @return 
	*/
	@Override
	public String toString()  {
		return this.NodeFrmID;
	}
}
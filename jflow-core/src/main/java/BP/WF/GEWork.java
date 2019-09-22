package BP.WF;

import BP.DA.*;
import BP.WF.*;
import BP.En.*;

/** 
 普通工作
*/
public class GEWork extends Work
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与_SQLCash 操作有关
	private SQLCash _SQLCash = null;
	@Override
	public SQLCash getSQLCash() throws Exception
	{
		if (_SQLCash == null)
		{
			_SQLCash = BP.DA.Cash.GetSQL(this.NodeFrmID.toString());
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL(this.NodeFrmID.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}
	@Override
	public void setSQLCash(SQLCash value)
	{
		_SQLCash = value;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 普通工作
	*/
	public GEWork()
	{
	}
	/** 
	 普通工作
	 
	 @param nodeid 节点ID
	*/
	public GEWork(int nodeid, String nodeFrmID)
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setSQLCash(null);
	}
	/** 
	 普通工作
	 
	 @param nodeid 节点ID
	 @param _oid OID
	*/
	public GEWork(int nodeid, String nodeFrmID, long _oid)
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
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
		return this.get_enMap();
	}
	/** 
	 GEWorks
	*/
	@Override
	public Entities getGetNewEntities()
	{
		if (this.getNodeID() == 0)
		{
			return new GEWorks();
		}
		return new GEWorks(this.getNodeID(), this.NodeFrmID);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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
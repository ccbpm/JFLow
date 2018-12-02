package BP.WF;

import BP.DA.DBAccess;
import BP.En.SQLCash;

/** 	 
 开始工作基类,所有开始工作都要从这里继承
*/
public abstract class StartWork extends Work{

		///#region 与_SQLCash 操作有关
	private SQLCash _SQLCash = null;
	@Override
	public SQLCash getSQLCash() throws Exception
	{
		if (_SQLCash == null)
		{
			_SQLCash = BP.DA.Cash.GetSQL("ND" + (new Integer(this.getNodeID())).toString());
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL("ND" + (new Integer(this.getNodeID())).toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}
	@Override
	public void setSQLCash(SQLCash value)
	{
		_SQLCash = value;
	}

		///#endregion


		///#region  单据属性
	/** 
	 FK_Dept
	 
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(StartWorkAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(StartWorkAttr.FK_Dept, value);
	}
	 
		
	/** 
	 工作内容标题
	*/
	public final String getTitle()
	{
		return this.GetValStringByKey(StartWorkAttr.Title);
	}
	public final void setTitle(String value)
	{
		this.SetValByKey(StartWorkAttr.Title,value);
	}
	/** 
	 工作流程
	*/
	protected StartWork()
	{
	}
	protected StartWork(long oid) throws Exception
	{
		super(oid);
	}
	/** 
	 删除之前的操作。
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeDelete() throws Exception
	{
		if (super.beforeDelete()==false)
		{
			return false;
		}
		if (this.getOID() < 0)
		{
			throw new RuntimeException("@实体["+this.getEnDesc()+"]没有被实例化，不能Delete().");
		}
		return true;
	}
	/** 
	 插入之前的操作。
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getOID() > 0)
		{
			throw new RuntimeException("@实体[" + this.getEnDesc() + "], 已经被实例化，不能Insert.");
		}

		this.SetValByKey("OID", DBAccess.GenerOID());
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setEmps(BP.Web.WebUser.getNo());
		return super.beforeUpdateInsertAction();
	}
	/** 
	 更新操作
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		if (super.beforeUpdate()==false)
		{
			return false;
		}
		if (this.getOID() < 0)
		{
			throw new RuntimeException("@实体["+this.getEnDesc()+"]没有被实例化，不能Update().");
		}
		return super.beforeUpdate();
	}
}
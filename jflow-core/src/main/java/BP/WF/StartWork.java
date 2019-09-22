package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;

/** 	 
 开始工作基类,所有开始工作都要从这里继承
*/
public abstract class StartWork extends Work
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与_SQLCash 操作有关
	private SQLCash _SQLCash = null;
	@Override
	public SQLCash getSQLCash()
	{
		if (_SQLCash == null)
		{
			_SQLCash = BP.DA.Cash.GetSQL("ND" + String.valueOf(this.getNodeID()));
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL("ND" + String.valueOf(this.getNodeID()), _SQLCash);
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
	//public string FK_DeptOf2Code
	//{
	//    get
	//    {
	//        return this.FK_Dept.Substring(6);
	//    } 
	//}
	/** 
	 FK_XJ
	*/
	//public string FK_XJ
	//{
	//    get
	//    {
	//        return this.GetValStringByKey(StartWorkAttr.FK_Dept);
	//    }
	//    set
	//    {
	//        this.SetValByKey(StartWorkAttr.FK_Dept, value);
	//    }
	//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 工作流程
	*/
	protected StartWork()
	{
	}
	protected StartWork(long oid)
	{
		super(oid);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  重写基类的方法。
	/** 
	 删除之前的操作。
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete()
	{
		if (super.beforeDelete() == false)
		{
			return false;
		}
		if (this.getOID() < 0)
		{
			throw new RuntimeException("@实体[" + this.EnDesc + "]没有被实例化，不能Delete().");
		}
		return true;
	}
	/** 
	 插入之前的操作。
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		if (this.getOID() > 0)
		{
			throw new RuntimeException("@实体[" + this.EnDesc + "], 已经被实例化，不能Insert.");
		}

		this.SetValByKey("OID", DBAccess.GenerOID());
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setEmps(BP.Web.WebUser.No);
		return super.beforeUpdateInsertAction();
	}
	/** 
	 更新操作
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdate()
	{
		if (super.beforeUpdate() == false)
		{
			return false;
		}
		if (this.getOID() < 0)
		{
			throw new RuntimeException("@实体[" + this.EnDesc + "]没有被实例化，不能Update().");
		}
		return super.beforeUpdate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
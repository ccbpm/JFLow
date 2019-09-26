package BP.Sys.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 SQLList 的摘要说明，属性的配置。
*/
public class SQLList extends XmlEn
{

		///#region 属性
	public final String getNo()
	{
		return this.GetValStringByKey(SQLListAttr.No);
	}
	public final String getSQL()
	{
		return this.GetValStringByKey(SQLListAttr.SQL);
	}
	/** 
	 备注
	*/
	public final String getNote()
	{
		return this.GetValStringByKey(SQLListAttr.Note);
	}

		///#endregion


		///#region 构造
	/** 
	 查询
	*/
	public SQLList()
	{
	}
	/** 
	 按照SQL来查询
	 
	 @param no
	*/
	public SQLList(String no)
	{
		this.RetrieveByPK("No", no);
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new SQLLists();
	}

		///#endregion
}
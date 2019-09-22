package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 文件管理者 
*/
public class SysFileManagers extends EntitiesOID
{
	/** 
	 文件管理者
	*/
	public SysFileManagers()
	{
	}
	/** 
	 文件管理者
	 
	 @param EnName
	 @param refval
	*/
	public SysFileManagers(String EnName, String refval)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysFileManagerAttr.EnName, EnName);
		qo.addAnd();
		qo.AddWhere(SysFileManagerAttr.RefVal, refval);
		qo.DoQuery();
	}
	/** 
	 文件管理者
	 
	 @param EnName
	*/
	public SysFileManagers(String EnName)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysFileManagerAttr.EnName, EnName);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SysFileManager();
	}
	public final SysFileManager GetSysFileByAttrFileNo(String key)
	{
		for (SysFileManager en : this.ToJavaList())
		{
			if (en.getAttrFileNo().equals(key))
			{
				return en;
			}
		}
		return null;
	}
}
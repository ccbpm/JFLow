package BP.Sys;

import java.util.List;

import BP.DA.*;
import BP.En.*;
import BP.GPM.Emp;

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
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SysFileManager> ToJavaList()
	{
		return (List<SysFileManager>)(Object)this;
	}
	/** 
	 文件管理者
	 
	 @param EnName
	 @param refval
	 * @throws Exception 
	*/
	public SysFileManagers(String EnName, String refval) throws Exception
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
	 * @throws Exception 
	*/
	public SysFileManagers(String EnName) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysFileManagerAttr.EnName, EnName);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new SysFileManager();
	}
	public final SysFileManager GetSysFileByAttrFileNo(String key) throws Exception
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
package BP.Sys;

import Oracle.ManagedDataAccess.Client.*;
import BP.DA.*;
import BP.En.*;
import MySql.Data.MySqlClient.*;
import java.util.*;

/** 
 数据源s
*/
public class SFDBSrcs extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 数据源s
	*/
	public SFDBSrcs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new SFDBSrc();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	public int RetrieveAll()
	{
		int i = this.RetrieveAllFromDBSource();
		if (i == 0)
		{
			SFDBSrc src = new SFDBSrc();
			src.setNo("local");
			src.setName("应用系统主数据库(默认)");
			src.Insert();
			this.AddEntity(src);
			return 1;
		}
		return i;
	}
	/** 
	 查询数据源
	 
	 @return 返回查询的个数
	*/
	public final int RetrieveDBSrc()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SFDBSrcAttr.DBSrcType, " < ", 100);
		int i = qo.DoQuery();
		if (i == 0)
		{
			return this.RetrieveAll();
		}
		return i;
	}
	/** 
	 查询数据源
	 
	 @return 返回查询的个数
	*/
	public final int RetrieveWCSrc()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SFDBSrcAttr.DBSrcType, "= ",  DBSrcType.WebServices.getValue());
		int i = qo.DoQuery();
		if (i == 0)
		{
			return this.RetrieveAll();
		}
		return i;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SFDBSrc> ToJavaList()
	{
		return (List<SFDBSrc>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SFDBSrc> Tolist()
	{
		ArrayList<SFDBSrc> list = new ArrayList<SFDBSrc>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SFDBSrc)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
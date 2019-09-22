package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.io.*;

/** 
 文件管理者 
*/
public class SysDocFiles extends Entities
{
	public SysDocFiles()
	{
	}
	public SysDocFiles(String _tableName, String _key)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysDocFileAttr.EnName, _tableName);
		qo.addAnd();
		qo.AddWhere(SysDocFileAttr.RefKey, _key);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SysDocFile();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SysDocFile> ToJavaList()
	{
		return (List<SysDocFile>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SysDocFile> Tolist()
	{
		ArrayList<SysDocFile> list = new ArrayList<SysDocFile>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysDocFile)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.DTS;

import BP.Web.Controls.*;
import BP.Port.*;
import BP.DA.*;
import BP.En.*;
import BP.Sys.*;

/** 
 为表的字段增加中文注释
*/
public class TableFieldAddComment extends Method
{
	/** 
	 为表的字段增加中文注释
	*/
	public TableFieldAddComment()
	{
		this.Title = "为表的字段增加中文注释";
		this.Help = "从map里面去找表的每个字段.";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		PubClass.AddComment();
		return "执行成功.";
	}
}
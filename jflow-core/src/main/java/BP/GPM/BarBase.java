package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Web.Controls.*;
import BP.Web.*;
import BP.Sys.*;

/** 
 流程事件基类
 0,集成该基类的子类,可以重写事件的方法与基类交互.
 1,一个子类必须与一个流程模版绑定.
 2,基类里有很多流程运行过程中的变量，这些变量可以辅助开发者在编写复杂的业务逻辑的时候使用.
 3,该基类有一个子类模版，位于:\CCFlow\WF\Admin\AttrFlow\F001Templepte.cs .
*/
public abstract class BarBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 系统属性.
	/** 
	 流程编号/流程标记.
	*/
	public abstract String getNo();
	/** 
	 名称
	*/
	public abstract String getName();
	/** 
	 权限控制-是否可以查看
	 * @throws Exception 
	*/
	public abstract boolean getIsCanView() throws Exception;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 系统属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 外观行为.
	/** 
	 标题
	*/
	public abstract String getTitle();
	/** 
	 更多连接
	*/
	public abstract String getMore();
	/** 
	 内容信息
	 * @throws Exception 
	*/
	public abstract String getDocuments() throws Exception;
	/** 
	 宽度
	*/
	public abstract String getWidth();
	/** 
	 高度
	*/
	public abstract String getHeight();

	public abstract boolean getIsLine();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 外观行为.

}
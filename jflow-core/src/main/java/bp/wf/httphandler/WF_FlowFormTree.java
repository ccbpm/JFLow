package bp.wf.httphandler;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;

/** 
 页面功能实体
*/
public class WF_FlowFormTree extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_FlowFormTree()
	{
	}


		///执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" +CommonUtils.getRequest().getRequestURI());
	}

		/// 执行父类的重写方法.


		///xxx 界面 .
	public final String Default_Init()
	{
		return "";
	}


		/// xxx 界面方法.

}
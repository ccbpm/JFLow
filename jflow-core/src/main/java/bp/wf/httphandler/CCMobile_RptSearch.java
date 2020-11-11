package bp.wf.httphandler;
import bp.difference.handler.WebContralBase;
import bp.web.*;
/** 
 页面功能实体
*/
public class CCMobile_RptSearch extends WebContralBase
{

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
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + this.getRequest().getRequestURL());
	}

		/// 执行父类的重写方法.

	/** 
	 构造函数
	 * @throws Exception 
	*/
	public CCMobile_RptSearch() throws Exception
	{
		WebUser.setSheBei("Mobile");

	}


		///关键字查询.
	/** 
	 打开表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String KeySearch_OpenFrm() throws Exception
	{
		bp.wf.httphandler.WF_RptSearch search = new WF_RptSearch();
		return search.KeySearch_OpenFrm();
	}
	/** 
	 执行查询
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String KeySearch_Query() throws NumberFormatException, Exception
	{
		bp.wf.httphandler.WF_RptSearch search = new WF_RptSearch();
		return search.KeySearch_Query();
	}

		/// 关键字查询.

}
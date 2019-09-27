package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class CCMobile_RptSearch extends WebContralBase
{

		///#region 执行父类的重写方法.
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

		///#endregion 执行父类的重写方法.

	/** 
	 构造函数
	*/
	public CCMobile_RptSearch()
	{
	}


		///#region 关键字查询.
	/** 
	 打开表单
	 
	 @return 
	*/
	public final String KeySearch_OpenFrm()
	{
		BP.WF.HttpHandler.WF_RptSearch search = new WF_RptSearch();
		return search.KeySearch_OpenFrm();
	}
	/** 
	 执行查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String KeySearch_Query() throws Exception
	{
		BP.WF.HttpHandler.WF_RptSearch search = new WF_RptSearch();
		return search.KeySearch_Query();
	}

		///#endregion 关键字查询.

}
package BP.WF.HttpHandler;

import BP.Web.*;
import BP.WF.*;

public abstract class HttpHandlerBase implements IHttpHandler, IRequiresSessionState, IReadOnlySessionState
{
	/** 
	 获取 "Handler业务处理类"的Type
	 <p></p>
	 <p>注意： "Handler业务处理类"必须继承自BP.WF.HttpHandler.WebContralBase</p>
	*/
	public abstract java.lang.Class getCtrlType();

	public final boolean getIsReusable()
	{
		return false;
	}
	private HttpContext context = null;
	public final void ProcessRequest(HttpContext mycontext)
	{
		context = mycontext;

		//创建 ctrl 对象, 获得业务实体类.
		Object tempVar = this.getCtrlType().newInstance();
		DirectoryPageBase ctrl = tempVar instanceof DirectoryPageBase ? (DirectoryPageBase)tempVar : null;

		//让其支持跨域访问.
		if (!tangible.StringHelper.isNullOrEmpty(HttpContextHelper.Request.Headers["Origin"]))
		{
			String allAccess_Control_Allow_Origin = System.Web.Configuration.WebConfigurationManager.AppSettings["Access-Control-Allow-Origin"];


			if (!tangible.StringHelper.isNullOrEmpty(allAccess_Control_Allow_Origin))
			{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
				var origin = HttpContextHelper.Request.Headers["Origin"];
				if (System.Web.Configuration.WebConfigurationManager.AppSettings["Access-Control-Allow-Origin"].Contains(origin))
				{
					HttpContextHelper.Response.Headers["Access-Control-Allow-Origin"] = origin;
					HttpContextHelper.Response.Headers["Access-Control-Allow-Credentials"] = "true";
					HttpContextHelper.Response.Headers["Access-Control-Allow-Headers"] = "x-requested-with,content-type";
				}
			}
		}

		try
		{
			//执行方法返回json.
			String data = ctrl.DoMethod(ctrl, ctrl.getDoType());

			//返回执行的结果.
			HttpContextHelper.Response.Write(data);
		}
		catch (RuntimeException ex)
		{
			String paras = "";
			for (String key : context.Request.QueryString.keySet())
			{
				paras += "@" + key + "=" + context.Request.QueryString[key];
			}

			String err = "";
			//返回执行错误的结果.
			if (ex.getCause() != null)
			{
				err = "err@在执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]错误 \t\n @" + ex.getCause().getMessage() + " \t\n @技术信息:" + ex.StackTrace + " \t\n相关参数:" + paras;
			}
			else
			{
				err = "err@在执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]错误 \t\n @" + ex.getMessage() + " \t\n @技术信息:" + ex.StackTrace + " \t\n相关参数:" + paras;
			}

			if (Web.WebUser.No == null)
			{
				err = "err@登录时间过长,请重新登录. @其他信息:" + err;
			}

			//记录错误日志以方便分析.
			BP.DA.Log.DebugWriteError(err);

			HttpContextHelper.Response.Write(err);
		}
	}

}
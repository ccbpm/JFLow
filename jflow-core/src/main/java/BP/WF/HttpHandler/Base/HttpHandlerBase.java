package BP.WF.HttpHandler.Base;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HttpContext;

import BP.DA.Log;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.common.BaseController;

public abstract class HttpHandlerBase extends BaseController
{
	/** 
	 * 获取 "Handler业务处理类"的Type
	 * 注意： "Handler业务处理类"必须继承自BP.WF.HttpHandler.WebContralBase</p>
	*/
	public abstract java.lang.Class getCtrlType();

	public final boolean getIsReusable()
	{
		return false;
	}
	
	public void ProcessRequest(Object mycontext)
	{	
		PrintWriter out =null;
		//创建 ctrl 对象.
		Object tempVar = mycontext;
		
		WebContralBase ctrl = (WebContralBase)((tempVar instanceof WebContralBase) ? tempVar : null);
		
		
		String mName=ctrl.GetRequestVal("DoMethod");			
		String msg="";			
		if (mName==null || mName=="null")		
			msg ="方法[" + ctrl.getDoType() + "]类[" + this.getCtrlType().toString() + "]";
		else
			msg ="方法[" + mName+ "]类[" + this.getCtrlType().toString() + "]，";	 
		
		BP.DA.Log.DebugWriteInfo(msg);
	//	out.write(msg);
		
		   
		try
		{
			//执行方法返回json.
			String data = ctrl.DoMethod(ctrl, ctrl.getDoType());

			//返回执行的结果.
			//ctrl.context.Response.Write(data);
			this.getResponse().setHeader("content-type", "text/html;charset=UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			out.write(data);
			
		
		      
		}
		catch (Exception ex)
		{
			
			String err=ex.getMessage();	
			
			String paras="";	
			 
			paras+= ctrl.GetRequestVal("DoMethod");
			 
			//返回执行错误的结果.
			if (ex.getCause() != null)
			{
				err+="err@在执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]["+paras+"]错误 \t\n @" + ex.getCause().getMessage() + " \t\n @技术信息:" + ex.getStackTrace();
			}
			else
			{
				err+="err@在执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]["+paras+"]错误 \t\n @" + ex.getMessage() + " \t\n @技术信息:" + ex.getStackTrace();
			}
			
			BP.DA.Log.DebugWriteError(err);
			
			out.write(err);
			
			
		}finally{
			if(null !=out){
				out.close();
			}
		}
	}

}

package bp.difference.handler;

import javax.servlet.http.HttpServletRequest;

import bp.da.DataType;
import bp.difference.SystemConfig;
import bp.web.WebUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import bp.wf.httphandler.WF_Comm;

@Controller
@RequestMapping("/WF/Comm")
@ResponseBody
public class WF_Comm_Controller extends HttpHandlerBase {

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequest(HttpServletRequest request) throws Exception {
		SystemConfig.setIsBSsystem(true);
		//处理Token信息
		DealToken();
		WF_Comm CommHandler = new WF_Comm();
		if (request instanceof DefaultMultipartHttpServletRequest) {
			//如果是附件上传Request，则将该Request放入全局Request。为了解决springmvc中全局Request无法转化为附件Request
			HttpServletRequest request1 = CommonUtils.getRequest();
			request1.setAttribute("multipartRequest", request);
		}
		super.ProcessRequestPost(CommHandler);
	}

	@Override
	public Class<WF_Comm> getCtrlType() {
		return WF_Comm.class;
	}

	private void DealToken() throws Exception {
		String doMethod = this.GetRequestVal("DoMethod");
		String doType= this.getDoType();
		if(doType.equals("HttpHandler")==true)
			doType = doMethod;
		if(DataType.IsNullOrEmpty(doType)==true)
			throw new RuntimeException("err@没有获取到执行的方法");

		doType = doType.toLowerCase();
		if(doType.contains("login")==true
			|| doType.contains("index")
			|| doType.contains("admiin")
			|| doType.contains("dbinstall")
			|| doType.contains("default_logout")
			|| doType.contains("checkencryptenable")
			|| doType.contains("ccbpmservices"))
			return;
		String token = this.GetRequestVal("Token");
		if(DataType.IsNullOrEmpty(token)==false &&(token.equals("undefined")==true ||  token.equals("null")==true))
			token="";
		if(DataType.IsNullOrEmpty(token)==false && DataType.IsNullOrEmpty(WebUser.getToken())==true){
			bp.wf.Dev2Interface.Port_LoginByToken(token);
			return;
		}
		if(DataType.IsNullOrEmpty(WebUser.getNo())==true){
				throw new Exception("err@登录信息丢失，请重新登录");
		}
	}

}

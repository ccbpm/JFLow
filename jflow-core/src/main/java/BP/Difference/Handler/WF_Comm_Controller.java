package BP.Difference.Handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.WF.HttpHandler.WF_Comm;

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
	public final void ProcessRequest() {
		HttpServletRequest request = getRequest();
//		WF_Comm CommHandler = new WF_Comm();
//		if (request instanceof DefaultMultipartHttpServletRequest) {
//			CommHandler.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
//			BP.WF.Glo.request = (DefaultMultipartHttpServletRequest) request;
//		}
//		super.ProcessRequest(CommHandler);
		
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

}

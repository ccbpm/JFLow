package cn.jflow.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.Sys.MapData;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/WF/Comm")
public class HelperOfSigantureController {
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	public String getUTF8ToString(String param) {
		try {
			return java.net.URLDecoder.decode(_request.getParameter(param),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/HelperOfSiganture", method = RequestMethod.GET)
	public void executeHelper(HttpServletRequest request,
			HttpServletResponse response) {
		_request = request;
		_response = response;

		String method = "";
		// 返回值
		String s_responsetext = "";
		if (StringHelper.isNullOrEmpty(request.getParameter("method"))) {
			return;
		}

		method = request.getParameter("method").toString();

		if (method.equals("sigantureact")) {
			 s_responsetext = SigantureAction();
		} 
		
		if (StringHelper.isNullOrEmpty(s_responsetext))
			s_responsetext = "";

		// 组装ajax字符串格式,返回调用客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getOutputStream().write(
					s_responsetext.replace("][", "],[").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// / <summary>
	// / 数字签名
	// / </summary>
	// / <returns></returns>
	private String SigantureAction() {
		String imgSrc = getUTF8ToString("imgSrc");
		String UserNo = getUTF8ToString("UserNo");
		String FK_MapData = getUTF8ToString("FK_MapData");
		String KeyOfEn = getUTF8ToString("KeyOfEn");
		String WorkID = getUTF8ToString("WorkID");
		// 修改表数据
		MapData md = new MapData(FK_MapData);
		if (imgSrc.contains(UserNo) || imgSrc.contains("UnName")) {
			DBAccess.RunSQL("UPDATE " + md.getPTable() + " SET " + KeyOfEn
					+ "='' WHERE OID=" + WorkID);
			return "siganture";
		} else {
			DBAccess.RunSQL("UPDATE " + md.getPTable() + " SET " + KeyOfEn + "='"
					+ UserNo + "' WHERE OID=" + WorkID);
			return UserNo;
		}
	}
}

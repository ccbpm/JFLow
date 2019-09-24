package BP.Difference.Handler;

import java.util.Hashtable;

import BP.Port.Emp;
import BP.Tools.HttpClientUtil;
import BP.WF.GenerWorkFlow;
import net.sf.json.JSONObject;

public class PortalInterfaceWS {
	
	/**
	 * 发送消息
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param url
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public String Message_Send(String sender, String sendToEmpNo, String title,String msgInfo, String OpenUrl, String msgType) throws Exception {
		
		if (1==1)
		   return "" ;

	 
		Emp emp = new Emp(sender);
		String webPath = "http://192.168.43.48:8084/personal_center/message/getProcessMessage";
		Emp toemp = new Emp(sendToEmpNo);
		
		String[] params = OpenUrl.split("&");
		String sid="";
		for(String param :params){
			if(param.contains("SID") == true){
				sid = param;
				break;
			}
		}
		String[] strs = sid.replace("SID=","").split("_");

		long workId = Long.parseLong(strs[1]);
		GenerWorkFlow gwf = new GenerWorkFlow(workId);
		
		//增加header参数
		java.util.Map<String, String> headerMap = new Hashtable<String, String>();
		headerMap.put("Content-Type", "application/json");
		
		//增加body参数
		StringBuffer info = new StringBuffer();
		info.append("{");
		info.append("\""+sendToEmpNo+"\":[{");
		info.append("\"sender\":\""+sender+"\",");
		info.append("\"content\":\"" + msgInfo + "\",");
		info.append("\"workId\":\"\""+gwf.getWorkID()+"\",");
		info.append("\"type\":\"出国境\",");
		info.append("\"status\":\"未处理\"");
		info.append("}]");
		info.append("}");


		String json = HttpClientUtil.doPostJson(webPath, info.toString());
		JSONObject jd = JSONObject.fromObject(json);
		String code = jd.get("code").toString();
		if (code.equals("200") == false) {
			jd = jd.getJSONObject("result");
			throw new Exception(jd.toString());
		}
		return "发送成功";
	}

}

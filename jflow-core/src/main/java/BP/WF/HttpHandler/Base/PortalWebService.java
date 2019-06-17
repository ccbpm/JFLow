package BP.WF.HttpHandler.Base;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class PortalWebService {
	
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
	public boolean SendToWebServices(String sender, String sendToEmpNo, String title,String msgInfo, String OpenUrl, String msgType) throws Exception {
		String webPath="http://localhost:8080/jflow-web/services/PortalInterfaceWS";
		Service service = new Service();  
        Call call = (Call) service.createCall();  
        call.setTargetEndpointAddress(webPath);  
        call.setOperationName(new QName("http://WebServiceImp","SendToWebServices"));// WSDL里面描述的接口名称  
        call.addParameter("sender",  
                org.apache.axis.encoding.XMLType.XSD_LONG,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
        call.addParameter("sendToEmpNo",  
                org.apache.axis.encoding.XMLType.XSD_DATE,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
        call.addParameter("title",  
                org.apache.axis.encoding.XMLType.XSD_DATE,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
        call.addParameter("msgInfo",  
                org.apache.axis.encoding.XMLType.XSD_DATE,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
        call.addParameter("OpenUrl",  
                org.apache.axis.encoding.XMLType.XSD_DATE,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
       
        call.addParameter("msgType",  
                org.apache.axis.encoding.XMLType.XSD_DATE,  
                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
        
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_BOOLEAN);// 设置返回类型  

        
        boolean result =  (boolean)call.invoke(new Object[] { sender,sendToEmpNo,title,msgInfo,OpenUrl,msgType });
        // 给方法传递参数，并且调用方法  
        System.out.println("result is " + result);  
        
		return result;
		
		/*Emp emp = new Emp(sender);
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
		return "发送成功";*/
	}

}

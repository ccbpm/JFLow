package WebServiceImp;
import BP.Tools.HttpClientUtil;

import java.util.Date;  
import java.text.DateFormat;  
import org.apache.axis.client.Call;  
import org.apache.axis.client.Service;  
import javax.xml.namespace.QName;  
import java.lang.Integer;  
import javax.xml.rpc.ParameterMode;  

public class Test {
	
	public static void main(String[] args) throws Exception {
        //服务的地址
		String webPath="http://localhost:8080/jflow-web/services/LoacalWS";
		
		String UserNo="admin";
		 try {  
		
		// 以下都是套路  
	        Service service = new Service();  
	        Call call = (Call) service.createCall();  
	        call.setTargetEndpointAddress(webPath);  
	        call.setOperationName(new QName("http://WebServiceImp","DB_Todolist"));// WSDL里面描述的接口名称  
	        call.addParameter("userNo",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,  
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.addParameter("sysNo",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,  
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型  
	        String temp = "admin";  
	        String result = (String) call.invoke(new Object[] { temp,"" });  
	        // 给方法传递参数，并且调用方法  
	        System.out.println("result is " + result);  
	    } catch (Exception e) {  
	        System.err.println(e.toString());  
	    }  
		
		
		
    }

}

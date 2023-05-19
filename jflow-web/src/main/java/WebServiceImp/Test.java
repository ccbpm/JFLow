package WebServiceImp;

public class Test {
	
	public static void main(String[] args) throws Exception {
        //服务的地址
		String webPath="http://localhost:8080/jflow-web/services/LoacalWS";
		
		String UserNo="admin";
		 try {  
		
		// 以下都是套路  
	        /*Service service = new Service();
	        Call call = (Call) service.createCall();  
	        call.setTargetEndpointAddress(webPath);  
	        call.setOperationName(new QName("http://WebServiceImp","SendWork"));// WSDL里面描述的接口名称  
	        call.addParameter("flowNo",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,  
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.addParameter("workid",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.addParameter("ht",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,  
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.addParameter("toNodeID",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,  
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.addParameter("toEmps",  
	                org.apache.axis.encoding.XMLType.XSD_DATE,  
	                javax.xml.rpc.ParameterMode.IN);// 接口的参数  
	        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型  
	        String temp = "admin";  
	        Hashtable ht = new Hashtable();
	        ht.put("ZiDuan1", "111");
	        ht.put("ZiDuan2", "222");
	        String result = (String) call.invoke(new Object[] { "226",(long)798,ht,22602,"zhangyifan" });  
	        // 给方法传递参数，并且调用方法  
	        System.out.println("result is " + result);  */
	    } catch (Exception e) {  
	        System.err.println(e.toString());  
	    }  
		
		
		
    }

}

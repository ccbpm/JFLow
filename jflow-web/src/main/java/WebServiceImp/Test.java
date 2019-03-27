package WebServiceImp;
import BP.Tools.HttpClientUtil;


public class Test {
	
	public static void main(String[] args) throws Exception {
        //服务的地址
		String webPath="http://localhost:8098/WebService/LocalWService";
		
		String UserNo="admin";
		
		
		String json = HttpClientUtil.doPost(webPath+"/DB_Todolist?useNo=admin");
		System.out.println(json);
		
    }

}
